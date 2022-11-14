package com.luis.diaz.authentication.api.shared.utils;

import io.micronaut.context.annotation.Property;
import lombok.Getter;

@Getter
public class PropertyUtil {
    @Property(name = "jwk.primary", defaultValue = "")
    private String primary;

    @Property(name = "jwk.secondary", defaultValue = "")
    private String secondary;

    @Property(name = "jwk.kid", defaultValue = "")
    private String kid;

    @Property(name = "jwk.kid2", defaultValue = "")
    private String kid2;

    @Property(name = "jwk.expirationToken", defaultValue = "")
    private String expirationToken;

    @Property(name = "jwk.expirationRefreshToken", defaultValue = "")
    private String expirationRefreshToken;
}
