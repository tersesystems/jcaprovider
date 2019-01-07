# JCA Provider Service

This is a very small package which allows a custom JCA provider to be used as the default JCA provider, by using a Java agent.

## Installation

The core of JCA provider service is a small java agent and some ServiceLoader code.

```xml
<dependencies>
    <dependency>
        <groupId>com.tersesystems.jcaprovider</groupId>
        <artifactId>jcaprovider-core</artifactId>
        <version>0.1.0-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

Add the java agent, to your program at runtime.  

```bash
java -javaagent:/home/wsargent/.m2/repository/com/tersesystems/jcaprovider/jcaprovider-core/0.1.0-SNAPSHOT/jcaprovider-core-0.1.0-SNAPSHOT.jar
```

Or by adding it to `JAVA_TOOL_OPTIONS`:

```bash
export JAVA_TOOL_OPTIONS="-javaagent:/home/wsargent/.m2/repository/com/tersesystems/jcaprovider/jcaprovider-core/0.1.0-SNAPSHOT/jcaprovider-core-0.1.0-SNAPSHOT.jar"
```

This will then load any JCA service provider implementation that you have in your classpath automatically

### Manual Installation

If you do not have the core loaded as a Java agent, then you can load any providers manually by doing the following:

```java
import com.tersesystems.jcaprovider.JcaProviderService;

JcaProviderService instance = JcaProviderService.getInstance();
instance.insertProviders(); // Calls Security.insertProviderAt(provider, 1) for listed providers
```

## Specifying a JCA Provider implementation

You specify the provider implementation in one of several ways. 

### Using Pre-built Service Provider

There are several subprojects which have implementations available.  You add these to your project, and then you're done.

#### DebugJSSE

The [DebugJSSE Provider](https://github.com/tersesystems/debugjsse): 

```xml
<dependency>
    <groupId>com.tersesystems.jcaprovider</groupId>
    <artifactId>jcaprovider-debugjsse</artifactId>
    <version>0.1.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

#### CloudFoundry Provider

The [CloudFoundry Provider](https://github.com/cloudfoundry/java-buildpack-security-provider):

```xml
<dependency>
    <groupId>com.tersesystems.jcaprovider</groupId>
    <artifactId>jcaprovider-cloudfoundry</artifactId>
    <version>0.1.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

#### BouncyCastle Provider

The [BouncyCastle Provider](https://bouncycastle.org/docs/docs1.5on/org/bouncycastle/jce/provider/BouncyCastleProvider.html):

```xml
<dependency>
    <groupId>com.tersesystems.jcaprovider</groupId>
    <artifactId>jcaprovider-bouncycastle</artifactId>
    <version>0.1.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

#### BouncyCastle TLS

The [BouncyCastle TLS Provider](https://www.bouncycastle.org/docs/tlsdocs1.5on/org/bouncycastle/jsse/provider/BouncyCastleJsseProvider.html):

```xml
<dependency>
    <groupId>com.tersesystems.jcaprovider</groupId>
    <artifactId>jcaprovider-bouncycastle-tls</artifactId>
    <version>0.1.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

### Using Reflection

You can also specify a provider through reflection, using the `com.tersesystems.jcaprovider.name` system property.

```bash
java -Dcom.tersesystems.jcaprovider.name=com.tersesystems.debugjsse.DebugJSSEProvider
```

### Writing a custom service provider

Finally, you can always specify a provider yourself with the [service loader pattern](https://docs.oracle.com/javase/tutorial/ext/basics/spi.html), which is as simple as implementing the `JcaProvider` interface:

```java
package mypackage;

public class MyJcaProvider implements com.tersesystems.jcaprovider.spi.JcaProvider {
    @Override
    public Provider apply() {
        return new MyProvider();
    }
}
```

and a `META-INF/services/com.tersesystems.jcaprovider.spi.JcaProvider` file containing

```
mypackage.MyJcaProvider
```

## Examples

### Using Maven

TODO

### Using SBT

You can specify the java agent and implementation directly in `build.sbt` and a couple of plugins:

```scala
addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.4")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.15")
```

and then in `build.sbt`:

```scala
// https://github.com/sbt/sbt-javaagent / sbt native packager handles java agent packaging
lazy val root = (project in file(".")).enablePlugins(JavaAppPackaging, JavaAgent)
  .settings(
    inThisBuild(List(
      organization := "com.example",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "project-with-jcaprovider",
    mainClass := Some("com.example.SecurityProgram"),
    resolvers += Resolver.jcenterRepo,
    javaAgents += "com.tersesystems.jcaprovider" % "jcaprovider-core" % "1.0-SNAPSHOT" % "dist;compile;test",
    libraryDependencies += "com.tersesystems.jcaprovider" % "jcaprovider-debugjsse" % "1.0-SNAPSHOT" // use debugjsse as the implementation
  )
```

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

I've always found this very strange and hard to remember, and I think using an agent and an implementation is a cleaner way to go.