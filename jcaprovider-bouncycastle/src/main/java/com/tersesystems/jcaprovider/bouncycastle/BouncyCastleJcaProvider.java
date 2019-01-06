package com.tersesystems.jcaprovider.bouncycastle;

import com.tersesystems.jcaprovider.spi.JcaProvider;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;

public class BouncyCastleJcaProvider implements JcaProvider {
    @Override
    public Provider apply() {
        return new BouncyCastleProvider();
    }
}
