package com.tersesystems.jcaprovider;

import org.cloudfoundry.security.CloudFoundryContainerProvider;
import org.junit.Test;

import javax.net.ssl.KeyManagerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class JcaProviderServiceTest extends AbstractLoggingTest
{
    @Test
    public void testWithSystemProperty() throws Exception {
        System.setProperty("org.cloudfoundry.security.keymanager.enabled", "true");
        System.setProperty("com.tersesystems.jcaprovider.name", "org.cloudfoundry.security.CloudFoundryContainerProvider");

        JcaProviderService.getInstance().insertProvider();

        assertThat(KeyManagerFactory.getInstance("SunX509").getProvider()).isInstanceOf(CloudFoundryContainerProvider.class);
        assertThat(KeyManagerFactory.getInstance("NewSunX509").getProvider()).isInstanceOf(CloudFoundryContainerProvider.class);
        assertThat(KeyManagerFactory.getInstance("PKIX").getProvider()).isInstanceOf(CloudFoundryContainerProvider.class);
    }

    @Test
    public void testWithServiceLoader() throws Exception {
        System.setProperty("org.cloudfoundry.security.keymanager.enabled", "true");

        JcaProviderService.getInstance().insertProvider();

        assertThat(KeyManagerFactory.getInstance("SunX509").getProvider()).isInstanceOf(CloudFoundryContainerProvider.class);
        assertThat(KeyManagerFactory.getInstance("NewSunX509").getProvider()).isInstanceOf(CloudFoundryContainerProvider.class);
        assertThat(KeyManagerFactory.getInstance("PKIX").getProvider()).isInstanceOf(CloudFoundryContainerProvider.class);
    }
}
