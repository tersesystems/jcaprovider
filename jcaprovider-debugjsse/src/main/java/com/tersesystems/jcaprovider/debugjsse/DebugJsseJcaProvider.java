package com.tersesystems.jcaprovider.debugjsse;

import com.tersesystems.debugjsse.DebugJSSEProvider;
import com.tersesystems.jcaprovider.spi.JcaProvider;

import java.security.Provider;

public class DebugJsseJcaProvider implements JcaProvider {
    @Override
    public Provider apply() {
        return new DebugJSSEProvider();
    }
}
