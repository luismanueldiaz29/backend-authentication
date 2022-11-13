package com.luis.diaz.authentication.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import com.luis.diaz.authentication.dto.AppUserDto;
import com.luis.diaz.authentication.dto.CruedUserDto;
import com.luis.diaz.authentication.dto.UserTokenDto;
import com.luis.diaz.authentication.dto.requests.AuthRequest;
import com.luis.diaz.authentication.dto.responses.TokenResponse;
import com.luis.diaz.authentication.dto.responses.UserResponse;
import com.luis.diaz.authentication.infraestructure.entities.User;
import com.luis.diaz.authentication.infraestructure.repositories.UserRepository;
import com.luis.diaz.authentication.services.AuthService;
import com.luis.diaz.authentication.shared.interfaces.JwkConfiguration;
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
import org.modelmapper.ModelMapper;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.DateTimeException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Singleton
public class AuthServiceImpl implements AuthService {
    private final ObjectMapper objectMapper;
    private final JwkConfiguration jwkConfiguration;
    private final UserRepository userRepository;
    private ModelMapper modelMapper;

    public AuthServiceImpl(JwkConfiguration jwkConfiguration, UserRepository userRepository) {
        this.jwkConfiguration = jwkConfiguration;
        this.userRepository = userRepository;
        objectMapper = new ObjectMapper();
        modelMapper = new ModelMapper();
    }

    @Override
    public TokenResponse login(AuthRequest authRequest) throws JOSEException, DateTimeException, JsonProcessingException {
        String password = Hashing.sha512().hashString(authRequest.getPassword(), StandardCharsets.UTF_8).toString();

        Optional<User> user = userRepository.findByUsernameAndPassword(authRequest.getUsername(), password);

        UserTokenDto<UserResponse> userToken = new UserTokenDto<>();
        userToken.setData(convertEntityToResponse(user.get()));
        return generateToken(userToken, false, false);
    }


    public TokenResponse generateToken(UserTokenDto userTokenDto, boolean isRenew, boolean isApp) throws JOSEException, JsonProcessingException {
        TokenResponse responseToken = new TokenResponse();

        if(!isRenew){
            responseToken.setAccessToken(returnStringToken(userTokenDto, false, isApp));
            responseToken.setRefreshToken(returnStringToken(userTokenDto, true, isApp));
        }else{
            responseToken.setAccessToken(returnStringToken(userTokenDto, false, isApp));
        }

        return responseToken;
    }

    public String returnStringToken(UserTokenDto userTokenDto, boolean isRefreshToken, boolean isApp)
            throws JOSEException, JsonProcessingException {
        UserTokenDto dto = null;
        String json = objectMapper.writeValueAsString(userTokenDto.getData());
        JsonNode node = objectMapper.readTree(json);
        if(isApp == false){
            CruedUserDto cruedUserDto = objectMapper.convertValue(node, CruedUserDto.class);
            dto = new UserTokenDto<CruedUserDto>();
            dto.setData(cruedUserDto);
        }else {
            AppUserDto appUserDto = objectMapper.convertValue(node, AppUserDto.class);
            dto = new UserTokenDto<AppUserDto>();
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


    private String generateAndSignToken(JWKSet jwkSet, UserTokenDto userTokenDto, boolean isRefreshToken)
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

    public String getNameFromToken(String token) throws ParseException {
        JWSObject jwsObject = JWSObject.parse(token);
        return jwsObject.getPayload().toJSONObject().get("sub").toString();
    }

    private UserResponse convertEntityToResponse(User user){
        return modelMapper.map(user, UserResponse.class);
    }
}
