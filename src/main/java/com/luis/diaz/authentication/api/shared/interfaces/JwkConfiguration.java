package com.luis.diaz.authentication.api.shared.interfaces;

import io.micronaut.core.annotation.NonNull;

public interface JwkConfiguration {

    @NonNull
    String getPrimary();

    @NonNull
    String getSecondary();

    @NonNull
    String getKid();

    @NonNull
    String getKid2();

    @NonNull
    Integer getExpirationToken();

    @NonNull
    Integer getExpirationRefreshToken();
}

