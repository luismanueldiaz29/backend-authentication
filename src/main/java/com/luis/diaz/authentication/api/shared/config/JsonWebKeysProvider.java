package com.luis.diaz.authentication.api.shared.config;

import com.nimbusds.jose.jwk.JWK;
import io.micronaut.runtime.context.scope.Refreshable;
import io.micronaut.security.token.jwt.endpoints.JwkProvider;

import java.util.Arrays;
import java.util.List;

@Refreshable
public class JsonWebKeysProvider implements JwkProvider {

    private final List<JWK> jwks;
    private final PrimarySignatureConfiguration primarySignatureConfiguration;
    private final SecondarySignatureConfiguration secondarySignatureConfiguration;

    public JsonWebKeysProvider(PrimarySignatureConfiguration primaryRsaPrivateKey,
                               SecondarySignatureConfiguration secondarySignatureConfiguration) {
        this.primarySignatureConfiguration = primaryRsaPrivateKey;
        this.secondarySignatureConfiguration = secondarySignatureConfiguration;
        jwks = Arrays.asList(primaryRsaPrivateKey.getPublicJWK(), secondarySignatureConfiguration.getPublicJWK());
    }

    @Override
    public List<JWK> retrieveJsonWebKeys() {
        return jwks;
    }
}
