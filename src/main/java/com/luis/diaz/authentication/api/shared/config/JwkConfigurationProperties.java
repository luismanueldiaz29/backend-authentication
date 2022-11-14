package com.luis.diaz.authentication.api.shared.config;

import com.luis.diaz.authentication.api.shared.interfaces.JwkConfiguration;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties("jwk")
public class JwkConfigurationProperties implements JwkConfiguration {

    @NonNull
    @NotBlank
    private String primary;

    @NonNull
    @NotBlank
    private String secondary;

    @NonNull
    @NotBlank
    private String kid;

    @NonNull
    @NotBlank
    private String kid2;

    @NonNull
    @NotBlank
    private Integer expirationToken;

    @NonNull
    @NotBlank
    private Integer expirationRefreshToken;

    @Override
    @NonNull
    public String getPrimary() {
        return primary;
    }

    @Override
    @NonNull
    public String getSecondary() {
        return secondary;
    }

    @Override
    @NonNull
    public String getKid() {
        return kid;
    }

    @Override
    @NonNull
    public String getKid2() {
        return kid2;
    }

    @Override
    @NonNull
    public Integer getExpirationToken() {
        return expirationToken;
    }

    @Override
    @NonNull
    public Integer getExpirationRefreshToken() {
        return expirationRefreshToken;
    }

    public void setPrimary(@NonNull String primary) {
        this.primary = primary;
    }

    public void setSecondary(@NonNull String secondary) {
        this.secondary = secondary;
    }

    public void setKid(@NonNull String kid) {
        this.kid = kid;
    }

    public void setKid2(@NonNull String kid2) {
        this.kid2 = kid2;
    }

    public void setExpirationToken(@NonNull Integer expirationToken) {
        this.expirationToken = expirationToken;
    }

    public void setExpirationRefreshToken(@NonNull Integer expirationRefreshToken) {
        this.expirationRefreshToken = expirationRefreshToken;
    }
}
