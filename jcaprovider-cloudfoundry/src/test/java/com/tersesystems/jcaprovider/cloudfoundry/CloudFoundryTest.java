package com.tersesystems.jcaprovider.cloudfoundry;

import com.tersesystems.jcaprovider.JcaProviderService;
import org.cloudfoundry.security.CloudFoundryContainerProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.net.ssl.KeyManagerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class CloudFoundryTest extends AbstractLoggingTest
{
    @AfterEach
    public void after() {
        try {
            JcaProviderService.getInstance().removeProviders();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWithSystemProperty() throws Exception {
        System.setProperty("org.cloudfoundry.security.keymanager.enabled", "true");
        System.setProperty(JcaProviderService.PROVIDER_NAME, "org.cloudfoundry.security.CloudFoundryContainerProvider");

        JcaProviderService.getInstance().insertProviders();

        assertThat(KeyManagerFactory.getInstance("SunX509").getProvider()).isInstanceOf(CloudFoundryContainerProvider.class);
        assertThat(KeyManagerFactory.getInstance("NewSunX509").getProvider()).isInstanceOf(CloudFoundryContainerProvider.class);
        assertThat(KeyManagerFactory.getInstance("PKIX").getProvider()).isInstanceOf(CloudFoundryContainerProvider.class);
    }

    @Test
    public void testWithServiceLoader() throws Exception {
        System.setProperty("org.cloudfoundry.security.keymanager.enabled", "true");

        JcaProviderService.getInstance().insertProviders();

        assertThat(KeyManagerFactory.getInstance("SunX509").getProvider()).isInstanceOf(CloudFoundryContainerProvider.class);
        assertThat(KeyManagerFactory.getInstance("NewSunX509").getProvider()).isInstanceOf(CloudFoundryContainerProvider.class);
        assertThat(KeyManagerFactory.getInstance("PKIX").getProvider()).isInstanceOf(CloudFoundryContainerProvider.class);
    }
}
