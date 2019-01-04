# JCA Provider Service

This is a very small package which allows a custom JCA provider to be used as the default JCA provider, by using a Java agent.

You can specify a JCA provider using a system property, or you can provide your own plugin.

## Installation

Add the JAR and the classes of the JCA provider you want to the classpath, either directly (boot classpath is safest)

```bash
java -Xbootclasspath/p:jcaproviderservice-1.0-SNAPSHOT.jar
```

Or by adding it to `JAVA_TOOL_OPTIONS`:

```bash
export JAVA_TOOL_OPTIONS="-Xbootclasspath/p:jcaproviderservice-1.0-SNAPSHOT.jar"
```

## Usage

You specify the JCA provider you want in one of two ways, either by using the system property:

```bash
java -Dcom.tersesystems.jcaprovider.name=com.tersesystems.debugjsse.DebugJSSEProvider
```

or by using a provider with the `ServiceLoader` pattern:

```java
public class CloudFoundryJcaProvider implements com.tersesystems.jcaprovider.spi.JcaProvider {
    @Override
    public Provider apply() {
        return new org.cloudfoundry.security.CloudFoundryContainerProvider();
    }
}
```

The serviceloader pattern is more flexible, because it allows for more configuration options and doesn't hardcode to a no-args constructor, and also lets you chain / delegate more easily.

## Adding a Provider by Hand

For the sake of completeness, I'll describe how to add a JCA provider by hand, the old-school way:

You must declare the JCA provider in a [security file](https://docs.oracle.com/javase/9/security/java-secure-socket-extension-jsse-reference-guide.htm#JSSEC-GUID-59723547-D466-44C9-B066-EC5098B508E6)

```bash
security.provider.1=debugJSSE|com.tersesystems.debugjsse.DebugJSSEProvider
```

You must then append your security file by using the `java.security.properties` option wiht a single equals sign:

```bash
java -Djava.security.properties=my.java.security
```

If you want to do reordering, you have to specify everything.

This means you have to go to `$JAVA_HOME/jre/lib/security/java.security` and copy all of the providers over:

```bash
security.provider.1=sun.security.provider.Sun
security.provider.2=sun.security.rsa.SunRsaSign
security.provider.3=sun.security.ec.SunEC
security.provider.4=com.sun.net.ssl.internal.ssl.Provider
security.provider.5=com.sun.crypto.provider.SunJCE
security.provider.6=sun.security.jgss.SunProvider
security.provider.7=com.sun.security.sasl.Provider
security.provider.8=org.jcp.xml.dsig.internal.dom.XMLDSigRI
security.provider.9=sun.security.smartcardio.SunPCSC
```
