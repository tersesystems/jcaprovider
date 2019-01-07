package com.tersesystems.jcaprovider.debugjsse;

import com.tersesystems.debugjsse.DebugJSSEProvider;
import com.tersesystems.jcaprovider.JcaProviderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.net.ssl.KeyManagerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class DebugJsseTest extends AbstractLoggingTest
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
        System.setProperty(JcaProviderService.PROVIDER_NAME, DebugJSSEProvider.class.getName());

        JcaProviderService.getInstance().insertProviders();

        assertThat(KeyManagerFactory.getInstance("SunX509").getProvider()).isInstanceOf(DebugJSSEProvider.class);
        assertThat(KeyManagerFactory.getInstance("NewSunX509").getProvider()).isInstanceOf(DebugJSSEProvider.class);
        assertThat(KeyManagerFactory.getInstance("PKIX").getProvider()).isInstanceOf(DebugJSSEProvider.class);
    }

    @Test
    public void testWithServiceLoader() throws Exception {
        JcaProviderService.getInstance().insertProviders();

        assertThat(KeyManagerFactory.getInstance("SunX509").getProvider()).isInstanceOf(DebugJSSEProvider.class);
        assertThat(KeyManagerFactory.getInstance("NewSunX509").getProvider()).isInstanceOf(DebugJSSEProvider.class);
        assertThat(KeyManagerFactory.getInstance("PKIX").getProvider()).isInstanceOf(DebugJSSEProvider.class);
    }
}
