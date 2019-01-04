package com.example.provider;

import com.tersesystems.jcaprovider.spi.JcaProvider;

import java.security.Provider;

public class CloudFoundryJcaProvider implements JcaProvider {
    @Override
    public Provider apply() {
        System.out.println("Using provider!");
        return new org.cloudfoundry.security.CloudFoundryContainerProvider();
    }
}
