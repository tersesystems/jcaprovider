package com.tersesystems.jcaprovider.bouncycastle;

import com.tersesystems.jcaprovider.JcaProviderService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.security.KeyStore;

import static org.assertj.core.api.Assertions.assertThat;

public class BouncyCastleTest extends AbstractLoggingTest {
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
        System.setProperty(JcaProviderService.PROVIDER_NAME, BouncyCastleProvider.class.getName());

        JcaProviderService.getInstance().insertProviders();

        assertThat(KeyStore.getInstance("PKCS12").getProvider()).isInstanceOf(BouncyCastleProvider.class);
    }

    @Test
    public void testWithServiceLoader() throws Exception {
        JcaProviderService.getInstance().insertProviders();
        assertThat(KeyStore.getInstance("PKCS12").getProvider()).isInstanceOf(BouncyCastleProvider.class);
    }
}
