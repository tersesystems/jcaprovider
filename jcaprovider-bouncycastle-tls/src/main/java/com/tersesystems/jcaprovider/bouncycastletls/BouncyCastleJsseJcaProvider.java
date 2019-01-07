package com.tersesystems.jcaprovider.bouncycastletls;

import com.tersesystems.jcaprovider.spi.JcaProvider;

import java.security.Provider;

public class BouncyCastleJsseJcaProvider implements JcaProvider {
    @Override
    public Provider apply() {
        return new org.bouncycastle.jsse.provider.BouncyCastleJsseProvider();
    }
}
