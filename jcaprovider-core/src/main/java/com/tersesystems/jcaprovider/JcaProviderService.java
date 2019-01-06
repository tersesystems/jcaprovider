package com.tersesystems.jcaprovider;

import com.tersesystems.jcaprovider.spi.JcaProvider;

import java.security.Provider;
import java.security.Security;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is an SPI enabled service which produces a JCA provider, and can insert it.
 */
public class JcaProviderService {
    public static final String PROVIDER_NAME = "com.tersesystems.jcaprovider.name";

    private static final Logger logger = Logger.getLogger(JcaProviderService.class.getName());

    private static JcaProviderService service;
    private ServiceLoader<JcaProvider> loader;

    private JcaProviderService() {
        loader = ServiceLoader.load(JcaProvider.class);
    }

    public static synchronized JcaProviderService getInstance() {
        if (service == null) {
            service = new JcaProviderService();
        }
        return service;
    }

    public Provider generateProvider() throws Exception {
        Provider provider = generateProviderFromSystemProperty();
        if (provider == null) {
            provider = generateProviderFromServiceLoader();
        }
        return provider;
    }

    /**
     * Inserts the provider dynamically.
     *
     * @return the position that the provider was installed as, -1 if it was already installed, -2 if no provider was found.
     */
    public int insertProvider() throws Exception {
        Provider provider = generateProvider();
        if (provider != null) {
            return Security.insertProviderAt(provider, 1);
        }  else {
            logger.warning("No JCA provider specified!");
            return -2;
        }
    }

    private Provider generateProviderFromServiceLoader() {
        Provider provider = null;
        try {
            Iterator<JcaProvider> providers = loader.iterator();
            while (provider == null && providers.hasNext()) {
                JcaProvider d = providers.next();
                provider = d.apply();
            }
        } catch (ServiceConfigurationError serviceError) {
            provider = null;
            logger.log(Level.SEVERE, "Cannot load service: " + serviceError.getMessage());
        }
        return provider;
    }

    private Provider generateProviderFromSystemProperty() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = generateClassName();
        if (className == null) {
           return null;
        }

        // https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/HowToImplAProvider.html
        // https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/MyJCE.java
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Class<?> providerClass = classLoader.loadClass(className);

        // Assume that the provider has a no-args constructor
        Provider provider = (Provider) providerClass.newInstance();
        return provider;
    }

    private static String generateClassName() {
        return System.getProperty(PROVIDER_NAME);
    }
}
