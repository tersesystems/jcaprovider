package com.tersesystems.jcaprovider.bouncycastletls;

import com.tersesystems.jcaprovider.JcaProviderService;
import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.net.ssl.KeyManagerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class BouncyCastleTlsTest extends AbstractLoggingTest {
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
        System.setProperty(JcaProviderService.PROVIDER_NAME, BouncyCastleJsseProvider.class.getName());

        JcaProviderService.getInstance().insertProviders();

        assertThat(KeyManagerFactory.getInstance("X.509").getProvider()).isInstanceOf(BouncyCastleJsseProvider.class);
    }

    @Test
    public void testWithServiceLoader() throws Exception {
        JcaProviderService.getInstance().insertProviders();

        assertThat(KeyManagerFactory.getInstance("X.509").getProvider()).isInstanceOf(BouncyCastleJsseProvider.class);
    }
}
