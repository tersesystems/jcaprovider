package com.tersesystems.jcaprovider.spi;

import java.security.Provider;

/**
 * Supplier interface for ServiceLoader SPI.
 */
public interface JcaProvider {
    Provider apply();
}
