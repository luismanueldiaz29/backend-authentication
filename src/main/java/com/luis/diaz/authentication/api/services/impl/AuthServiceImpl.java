package com.luis.diaz.authentication.api.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luis.diaz.authentication.api.dto.requests.PayloadTokenRequest;
import com.luis.diaz.authentication.api.dto.requests.RefressTokenRequest;
import com.luis.diaz.authentication.api.dto.requests.TokenRequest;
import com.luis.diaz.authentication.api.dto.responses.AuthResponse;
import com.luis.diaz.authentication.api.services.AuthService;
import com.luis.diaz.authentication.api.shared.interfaces.JwkConfiguration;
import com.luis.diaz.authentication.api.dto.responses.UserResponse;
import com.nimbusds.jose.*;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.mint.ConfigurableJWSMinter;
import com.nimbusds.jose.mint.DefaultJWSMinter;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.time.DateTimeException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Singleton
public class AuthServiceImpl implements AuthService {
    private final ObjectMapper objectMapper;
    private final JwkConfiguration jwkConfiguration;

    public AuthServiceImpl(JwkConfiguration jwkConfiguration) {
        this.jwkConfiguration = jwkConfiguration;
        objectMapper = new ObjectMapper();
    }

    @Override
    public AuthResponse login(UserResponse userResponse, boolean isRenew) throws JOSEException, DateTimeException, JsonProcessingException {
        TokenRequest<UserResponse> userResponseTokenRequest = new TokenRequest<>();
        userResponseTokenRequest.setData(userResponse);

        return generateToken(userResponseTokenRequest, isRenew, false);
    }


    public AuthResponse generateToken(TokenRequest<?> tokenRequest, boolean isRenew, boolean isApp) throws JOSEException, JsonProcessingException {
        AuthResponse authResponse = new AuthResponse();

        if(!isRenew){
            authResponse.setAccessToken(returnStringToken(tokenRequest, false, isApp));
            authResponse.setRefreshToken(returnStringToken(tokenRequest, true, isApp));
        }else{
            authResponse.setAccessToken(returnStringToken(tokenRequest, false, isApp));
        }

        return authResponse;
    }

    public String returnStringToken(TokenRequest<?> userTokenDto, boolean isRefreshToken, boolean isApp)
            throws JOSEException, JsonProcessingException {
        TokenRequest dto = null;
        String json = objectMapper.writeValueAsString(userTokenDto.getData());
        JsonNode node = objectMapper.readTree(json);
        if(!isApp){
            PayloadTokenRequest cruedUserDto = objectMapper.convertValue(node, PayloadTokenRequest.class);
            dto = new TokenRequest<PayloadTokenRequest>();
            dto.setData(cruedUserDto);
        }else {
            RefressTokenRequest appUserDto = objectMapper.convertValue(node, RefressTokenRequest.class);
            dto = new TokenRequest<RefressTokenRequest>();
            dto.setData(appUserDto);
        }

        JsonNode nodePrimary = null;
        JsonNode nodeSecondary = null;
        try {
            nodePrimary = objectMapper.readTree(jwkConfiguration.getPrimary());
            nodeSecondary = objectMapper.readTree(jwkConfiguration.getSecondary());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        JWK primaryJWK = mapKey(nodePrimary, jwkConfiguration.getKid());

        JWK secondaryJWK = mapKey(nodeSecondary, jwkConfiguration.getKid2());

        JWKSet jwkSet = new JWKSet(Arrays.asList(primaryJWK, secondaryJWK));

        return generateAndSignToken(jwkSet, dto, isRefreshToken);
    }

    private JWK mapKey(JsonNode node, String kid) {
        return new RSAKey.Builder(Base64URL.from(node.get("n")
                .asText()), Base64URL.from(node
                .get("e")
                .asText()))
                .keyID(kid)
                .keyUse(KeyUse.SIGNATURE)
                .firstCRTCoefficient(Base64URL.from(node.get("qi").asText()))
                .firstFactorCRTExponent(Base64URL.from(node.get("dp")
                        .asText()))
                .privateExponent(Base64URL.from(node.get("d").asText()))
                .firstPrimeFactor(Base64URL.from(node.get("p").asText()))
                .secondFactorCRTExponent(Base64URL.from(node.get("dq")
                        .asText()))
                .secondPrimeFactor(Base64URL.from(node.get("q").asText()))
                .build();
    }


    private String generateAndSignToken(JWKSet jwkSet, TokenRequest<?> userTokenDto, boolean isRefreshToken)
            throws JOSEException, DateTimeException, JsonProcessingException {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        if(!isRefreshToken){
            calendar.add(Calendar.SECOND, jwkConfiguration.getExpirationToken());
        }else{
            calendar.add(Calendar.HOUR, jwkConfiguration.getExpirationRefreshToken());
        }

        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);

        ConfigurableJWSMinter<SecurityContext> minter = new DefaultJWSMinter<>();
        minter.setJWKSource(jwkSource);

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .build();

        JWTClaimsSet claimsSet = null;

        String json = objectMapper.writeValueAsString(userTokenDto.getData());
        JsonNode node = objectMapper.readTree(json);

        if(!isRefreshToken){
            claimsSet = new JWTClaimsSet.Builder()
                    .subject(node.get("username").asText())
                    .issueTime(date)
                    .expirationTime(calendar.getTime())
                    .claim("data", userTokenDto.getData())
                    .claim("refreshToken", "false")
                    .build();
        }else{
            claimsSet = new JWTClaimsSet.Builder()
                    .subject(node.get("username").asText())
                    .issueTime(date)
                    .expirationTime(calendar.getTime())
                    .claim("refreshToken", "true")
                    .build();
        }

        JWSObject jwsObject = minter.mint(header, claimsSet.toPayload(), null);

        return jwsObject.serialize();
    }

    @Override
    public String getNameFromToken(String token) throws ParseException {
        JWSObject jwsObject = JWSObject.parse(token);
        return jwsObject.getPayload().toJSONObject().get("sub").toString();
    }

    @Override
    public boolean isTokenRefresh(String token) throws ParseException {
        JWSObject jwsObject = JWSObject.parse(token);
        String value = jwsObject.getPayload().toJSONObject().get("refreshToken").toString();
        log.info("refreshToken -> "+ value);
        return Boolean.valueOf(value);
    }
}
