package com.tersesystems.jcaprovider;

/**
 * Class to instantiate a JCA provider, given a class name or resource.
 */
public class JcaProviderAgent {

    public static void premain(String args) throws Exception {
        JcaProviderService.getInstance().insertProvider();
    }

    public static void agentmain(String agentArgs) throws Exception {
        JcaProviderService.getInstance().insertProvider();
    }
}