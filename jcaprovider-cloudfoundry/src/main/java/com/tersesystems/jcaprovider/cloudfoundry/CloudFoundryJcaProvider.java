package com.tersesystems.jcaprovider.cloudfoundry;

import com.tersesystems.jcaprovider.spi.JcaProvider;
import org.cloudfoundry.security.CloudFoundryContainerProvider;

import java.security.Provider;

public class CloudFoundryJcaProvider implements JcaProvider {
    @Override
    public Provider apply() {
        return new CloudFoundryContainerProvider();
    }
}
