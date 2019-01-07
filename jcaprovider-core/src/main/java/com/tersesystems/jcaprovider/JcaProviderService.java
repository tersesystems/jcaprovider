package com.tersesystems.jcaprovider;

import com.tersesystems.jcaprovider.spi.JcaProvider;

import java.security.Provider;
import java.security.Security;
import java.util.*;
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

    public void removeProviders() throws Exception {
        List<Provider> providers = generateProviders();
        for (Provider provider : providers) {
            Security.removeProvider(provider.getName());
        }
    }

    public List<Provider> generateProviders() throws Exception {
        List<Provider> systemPropertyProviders = generateProvidersFromSystemProperty();
        List<Provider> serviceLoaderProviders = generateProvidersFromServiceLoader();
        ArrayList<Provider> providers = new ArrayList<>();
        providers.addAll(systemPropertyProviders);
        providers.addAll(serviceLoaderProviders);
        return providers;
    }

    /**
     * Inserts the provider dynamically.
     *
     * @return the position that the provider was installed as, -1 if it was already installed, -2 if no provider was found.
     */
    public int[] insertProviders() throws Exception {
        List<Provider> providers = generateProviders();
        if (providers.isEmpty()) {
            logger.warning("No JCA provider specified!");
            return new int[0];
        } else {
            int[] codes = new int[providers.size()];
            for (int i = 0; i < providers.size(); i++) {
                Provider provider = providers.get(i);
                codes[i] = Security.insertProviderAt(provider, 1);
            }
            return codes;
        }
    }

    private List<Provider> generateProvidersFromServiceLoader() {
        List<Provider> providers = new ArrayList<>();
        try {
            for (JcaProvider jcaProvider : loader) {
                Provider provider = jcaProvider.apply();
                providers.add(provider);
            }
        } catch (ServiceConfigurationError serviceError) {
            logger.log(Level.SEVERE, "Cannot load service: " + serviceError.getMessage());
        }
        return providers;
    }

    private List<Provider> generateProvidersFromSystemProperty() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = generateClassName();
        if (className == null) {
           return Collections.emptyList();
        }

        // https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/HowToImplAProvider.html
        // https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/MyJCE.java
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Class<?> providerClass = classLoader.loadClass(className);

        // Assume that the provider has a no-args constructor
        Provider provider = (Provider) providerClass.newInstance();
        return Collections.singletonList(provider);
    }

    private static String generateClassName() {
        return System.getProperty(PROVIDER_NAME);
    }
}
