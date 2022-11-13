package com.luis.diaz.authentication.shared.config;

import com.luis.diaz.authentication.shared.interfaces.JwkConfiguration;
import io.micronaut.runtime.context.scope.Refreshable;

@Refreshable
public class SecondarySignatureConfiguration
    extends AbstractRSASignatureConfiguration {

    public SecondarySignatureConfiguration(JwkConfiguration jwkConfiguration) {
        super(jwkConfiguration.getSecondary());
    }
}
