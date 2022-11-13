package com.luis.diaz.authentication.shared.config;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.token.jwt.signature.rsa.RSASignatureConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Optional;

public abstract class AbstractRSASignatureConfiguration
    implements RSASignatureConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRSASignatureConfiguration.class);

    protected final JWK publicJWK;
    protected final RSAPublicKey publicKey;
    protected final RSAPrivateKey privateKey;
    protected final JWSAlgorithm jwsAlgorithm;

    protected AbstractRSASignatureConfiguration(String jsonJwk) {
        RSAKey primaryRSAKey = parseRSAKey(jsonJwk)
                                   .orElseThrow(() -> new ConfigurationException("No se pudo parsear el JWK primario a clave RSA"));

        publicJWK = primaryRSAKey.toPublicJWK();

        try {
            privateKey = primaryRSAKey.toRSAPrivateKey();
        } catch (JOSEException e) {
            throw new ConfigurationException("No se pudo parsear la clave RSA a clave RAS privada");
        }

        try {
            publicKey = primaryRSAKey.toRSAPublicKey();
        } catch (JOSEException e) {
            throw new ConfigurationException("No se pudo parsear la clave RSA a clave RAS pública");
        }

        jwsAlgorithm = parseJWSAlgorithm(primaryRSAKey)
                           .orElseThrow(() -> new ConfigurationException("No se pudo parsear el algoritmo JWS desde la clave RSA"));
    }

    @NonNull
    public JWK getPublicJWK() {
        return publicJWK;
    }

    @Override
    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * If the algorithm is null, return an empty Optional. Otherwise, if the
     * algorithm is a JWSAlgorithm, return an Optional containing the algorithm.
     * Otherwise, return an Optional containing the parsed algorithm.
     *
     * @param rsaKey The RSA key to be used for signing.
     * @return Optional<JWSAlgorithm>
     */
    @NonNull
    private Optional<JWSAlgorithm> parseJWSAlgorithm(@NonNull RSAKey rsaKey) {
        Algorithm algorithm = rsaKey.getAlgorithm();
        if (algorithm == null) {
            return Optional.empty();
        }

        if (algorithm instanceof JWSAlgorithm) {
            return Optional.of((JWSAlgorithm) algorithm);
        }

        return Optional.of(JWSAlgorithm.parse(algorithm.getName()));
    }

    /**
     * It parses a JSON string into a JWK object, and if the JWK object is an
     * RSAKey, it returns it as an Optional
     *
     * @param jsonJwk The JSON string that represents the JWK.
     * @return Optional<RSAKey>
     */
    @NonNull
    private Optional<RSAKey> parseRSAKey(@NonNull String jsonJwk) {
        try {
            JWK jwk = JWK.parse(jsonJwk);
            if (!(jwk instanceof RSAKey)) {
                LOG.warn("JWK no es una RSAKey");
                return Optional.empty();
            }
            return Optional.of((RSAKey) jwk);
        } catch (ParseException e) {
            LOG.warn("No se pudo parsear JWK JSON string {}", jsonJwk);
            return Optional.empty();
        }
    }
}
