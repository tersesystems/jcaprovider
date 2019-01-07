package com.tersesystems.jcaprovider.bouncycastletls;


import org.slf4j.bridge.SLF4JBridgeHandler;

abstract class AbstractLoggingTest {

    static {
        // Optionally remove existing handlers attached to j.u.l root logger
        SLF4JBridgeHandler.removeHandlersForRootLogger();  // (since SLF4J 1.6.5)

        // add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
        // the initialization phase of your application
        SLF4JBridgeHandler.install();
    }

}