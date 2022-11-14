package com.luis.diaz.authentication.api.shared.config;

import com.luis.diaz.authentication.api.shared.interfaces.JwkConfiguration;
import io.micronaut.runtime.context.scope.Refreshable;

@Refreshable
public class SecondarySignatureConfiguration
    extends AbstractRSASignatureConfiguration {

    public SecondarySignatureConfiguration(JwkConfiguration jwkConfiguration) {
        super(jwkConfiguration.getSecondary());
    }
}
