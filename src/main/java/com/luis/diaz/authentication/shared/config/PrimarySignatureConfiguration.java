package com.luis.diaz.authentication.shared.config;

import com.luis.diaz.authentication.shared.interfaces.JwkConfiguration;
import io.micronaut.runtime.context.scope.Refreshable;
import jakarta.inject.Named;

@Refreshable
@Named("generator")
public class PrimarySignatureConfiguration
    extends AbstractRSAGeneratorSignatureConfiguration {

    public PrimarySignatureConfiguration(JwkConfiguration jwkConfiguration) {
        super(jwkConfiguration.getPrimary());
    }
}
