# 1. Install Ubuntu on WSL

## A. Enable WSL in PowerShell (Admin)
```
> Enable-WindowsOptionalFeature -Online -FeatureName Microsoft-Windows-Subsystem-Linux
> dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all
```
(Restart needed)

## B. Install WSL
[Windows Subsystem for Linux - Microsoft Store Apps](https://apps.microsoft.com/store/detail/windows-subsystem-for-linux/9P9TQF7MRM4R)

## C. Install Ubuntu
[Ubuntu - Microsoft Store Apps](https://apps.microsoft.com/store/detail/ubuntu/9PDXGNCFSCZV)

## D. Set WSL version to 2 in PowerShell (Admin)
```
> dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart
> dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart
> shutdown /r /t 0
> wsl –-list –-verbose
> wsl –-setdefault <DistributionName>
> wsl –-set-default-version 2
```

# 2. Install Java in Ubuntu

## A. Install JDK
```
$ sudo apt update
$ sudo apt-get install openjdk-11-jdk
```

## B. Install Maven
```
$ sudo apt update
$ sudo apt install maven
```

## C. Install Ant
```
$ sudo apt update
$ sudo apt install ant
```

# 3. Setup application server on Ubuntu

## A. Download and install Wildfly
[Wildfly Downloads](https://www.wildfly.org/downloads/)
Choose Jakarta EE 8 Full & Web Distribution (version 24+) and copy tgz link
```
$ sudo apt install curl wget
$ wget <copied link>
$ tar xvf <downloaded file name>
$ sudo mv <extracted folder name> /opt/wildfly
```

#### Start Wildfly
```
$ sudo /opt/wildfly/bin/standalone.sh
```

## B. Add Wildfly user
```
$ sudo /opt/wildfly/bin/add-user.sh
```
Then provide username, password

## C. Config Wildfly
#### Add /opt/wildfly/bin/ to $PATH
```
$ cat >> ~/.bashrc <<EOF
> export WildFly_BIN=”/opt/wildfly/bin/”
> export PATH=\$PATH:\$WildFly_BIN
> EOF
$ source ~/.bashrc
```

#### Remove RESTEasy-Crypto
```
$ sed -i '/.*org.jboss.resteasy.resteasy-crypto.*/d' /opt/wildfly/modules/system/layers/base/org/jboss/as/jaxrs/main/module.xml
$ rm -rf /opt/wildfly/modules/system/layers/base/org/jboss/resteasy/resteasy-crypto/
```

#### Create a custom configuration
Replace `/opt/wildfly/bin/standalone.conf` with the following Jinja2 template
```
if [ "x$JBOSS_MODULES_SYSTEM_PKGS" = "x" ]; then
     JBOSS_MODULES_SYSTEM_PKGS="org.jboss.byteman"
fi


if [ "x$JAVA_OPTS" = "x" ]; then
     JAVA_OPTS="-Xms{{ HEAP_SIZE }}m -Xmx{{ HEAP_SIZE }}m -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=256m"
     JAVA_OPTS="$JAVA_OPTS -Dhttps.protocols=TLSv1.2,TLSv1.3"
     JAVA_OPTS="$JAVA_OPTS -Djdk.tls.client.protocols=TLSv1.2,TLSv1.3"
     JAVA_OPTS="$JAVA_OPTS -Djava.net.preferIPv4Stack=true"
     JAVA_OPTS="$JAVA_OPTS -Djboss.modules.system.pkgs=$JBOSS_MODULES_SYSTEM_PKGS"
     JAVA_OPTS="$JAVA_OPTS -Djava.awt.headless=true"
     JAVA_OPTS="$JAVA_OPTS -Djboss.tx.node.id={{ TX_NODE_ID }}"
     JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError"
     JAVA_OPTS="$JAVA_OPTS -Djdk.tls.ephemeralDHKeySize=2048"
else
     echo "JAVA_OPTS already set in environment; overriding default settings with values: $JAVA_OPTS"
fi
```

#### Set Allowed Memory Usage
````
$ sed -i -e 's/{{ HEAP_SIZE }}/2048/g' /opt/wildfly/bin/standalone.conf
````

#### Set the Transaction Node ID
````
$ sed -i -e "s/{{ TX_NODE_ID }}/$(od -A n -t d -N 1 /dev/urandom | tr -d ' ')/g" /opt/wildfly/bin/standalone.conf
````

## D. Create an Elytron Credential Store

#### Create a Master Password
````
$ echo '#!/bin/sh' > wildfly_pass
$ echo "echo '$(openssl rand -base64 24)'" >> wildfly_pass
$ chmod 700 wildfly_pass
$ sudo mv wildfly_pass /usr/bin/wildfly_pass
````

#### Create the Credential Store
````
$ mkdir /opt/wildfly/standalone/configuration/keystore
$ /opt/wildfly/bin/jboss-cli.sh --connect '/subsystem=elytron/credential-store=defaultCS:add(location=keystore/credentials, relative-to=jboss.server.config.dir, credential-reference={clear-text="{EXT}/usr/bin/wildfly_pass", type="COMMAND"}, create=true)'
````

## E. Configure WildFly Remoting
````
$ /opt/wildfly/bin/jboss-cli.sh --connect '/subsystem=remoting/http-connector=http-remoting-connector:write-attribute(name=connector-ref,value=remoting)'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/socket-binding-group=standard-sockets/socket-binding=remoting:add(port=4447,interface=management)'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/subsystem=undertow/server=default-server/http-listener=remoting:add(socket-binding=remoting,enable-http2=true)'
$ /opt/wildfly/bin/jboss-cli.sh --connect ':reload'
````

## F. HTTP(S) Configuration

#### Remove existing TLS and HTTP Configuration
```
$ /opt/wildfly/bin/jboss-cli.sh --connect '/subsystem=undertow/server=default-server/http-listener=default:remove()'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/subsystem=undertow/server=default-server/https-listener=https:remove()'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/socket-binding-group=standard-sockets/socket-binding=http:remove()'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/socket-binding-group=standard-sockets/socket-binding=https:remove()'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/core-service=management/security-realm=ApplicationRealm/server-identity=ssl:remove()'
$ /opt/wildfly/bin/jboss-cli.sh --connect ':reload'
```

#### Add new Interfaces and Sockets
````
$ /opt/wildfly/bin/jboss-cli.sh --connect '/interface=http:add(inet-address="0.0.0.0")'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/interface=https:add(inet-address="0.0.0.0")'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/socket-binding-group=standard-sockets/socket-binding=http:add(port="8080",interface="http")'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/socket-binding-group=standard-sockets/socket-binding=https:add(port="8443",interface="https")'
````

#### Configure TLS
````
$ /opt/wildfly/bin/jboss-cli.sh --connect '/subsystem=elytron/credential-store=defaultCS:add-alias(alias=httpsKeystorePassword, secret-value="serverpwd")'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/subsystem=elytron/credential-store=defaultCS:add-alias(alias=httpsTruststorePassword, secret-value="changeit")'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/subsystem=elytron/key-store=httpsKS:add(path="keystore/keystore.p12",relative-to=jboss.server.config.dir,credential-reference={store=defaultCS, alias=httpsKeystorePassword},type=PKCS12)'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/subsystem=elytron/key-store=httpsTS:add(path="keystore/truststore.p12",relative-to=jboss.server.config.dir,credential-reference={store=defaultCS, alias=httpsTruststorePassword},type=PKCS12)'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/subsystem=elytron/key-manager=httpsKM:add(key-store=httpsKS,algorithm="SunX509",credential-reference={store=defaultCS, alias=httpsKeystorePassword})'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/subsystem=elytron/trust-manager=httpsTM:add(key-store=httpsTS)'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/subsystem=elytron/server-ssl-context=https:add(key-manager=httpsKM,protocols=["TLSv1.3","TLSv1.2"],use-cipher-suites-order=false,cipher-suite-filter="TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256,TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256",cipher-suite-names="TLS_AES_256_GCM_SHA384:TLS_AES_128_GCM_SHA256:TLS_CHACHA20_POLY1305_SHA256",trust-manager=httpsTM,want-client-auth=true,authentication-optional=true)'
````

#### Add HTTP(S) Listeners:
````
$ /opt/wildfly/bin/jboss-cli.sh --connect '/subsystem=undertow/server=default-server/http-listener=http:add(socket-binding="http", redirect-socket="https")'
$ /opt/wildfly/bin/jboss-cli.sh --connect '/subsystem=undertow/server=default-server/https-listener=https:add(socket-binding="https", ssl-context="https", max-parameters=2048)'
$ /opt/wildfly/bin/jboss-cli.sh --connect ':reload'
````

## G. Generate keystore file
````
$ cd /opt/wildfly/standalone/configuration
$ sudo keytool -genkeypair -alias httpsKeystorePassword -keyalg RSA -keysize 2048 -validity 365 -keystore application.keystore
$ cd keystore
$ sudo keytool -genkeypair -alias httpsKeystorePassword -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12
$ sudo keytool -genkeypair -alias httpsTruststorePassword -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore truststore.p12
````
(You will be asked to enter password for these fields, keep them to use later)

## H. Edit standalone.xml
````
$ sudo nano /opt/wildfly/standalone/configuration/standalone.xml
````
Find the attributes `clear-text=”password”` and replace `password` with the password you entered when creating application.keystore

## I. Create ManagementCA.pem and .p12 certificate file

#### Install Docker
[Install Docker Desktop on Windows](https://docs.docker.com/desktop/install/windows-install/)

#### Create ManagementCA.pem and .p12 certificate file
1. [Quick start EJBCA Docker Container - YouTube](https://www.youtube.com/watch?v=x-kEqPrz1g0)
2. [EJBCA Container - Issue Client Authentication Certificate - YouTube](https://www.youtube.com/watch?v=wMD1GgSF-JE)

Copy `ManagementCA.pem` file to `/opt/wildfly/standalone/configuration` and import the .p12 certificate file to your browser.

#### Import ManagementCA.pem to keystore file
````
$ sudo keytool -import -alias myca -file ManagementCA.pem -keystore application.keystore
$ sudo cp ManagementCA.pem keystore/ManagementCA.pem
$ cd keystore
$ sudo keytool -import -alias myca -file ManagementCA.pem -keystore keystore.p12
$ sudo keytool -import -alias myca -file ManagementCA.pem -keystore truststore.p12
````

# 4. Install SignServer

## A. Clone source code
Clone source code from this repo: [SignServer - Repos](https://github.com/ncc-erp/metasign-signserver/)

## B. Build SignServer
Go to `signserver` directory

#### Ensure Secure Maven Installation
````
$ mvn help:effective-settings
````

#### Set Edition
````
$ sudo bin/ant init
````

#### Build from source
````
$ sudo mvn install -DskipTests
````
Or use this command instead if you have `SunCertPathBuilderException`
````
$ mvn clean install -DskipTests -P autoInstallPackage -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true
````

## C. Set Environment Variables
````
$ sudo nano /etc/environment
````
Paste these following lines:
````
APPSRV_HOME=/opt/wildfly
SIGNSERVER_NODEID=node1
````

## D. Configure deployment
SignServer without database

#### Copy .properties file
````
$ sudo cp conf/signserver_deploy.properties.sample conf/signserver_deploy.properties
````
Paste these following lines:
````
appserver.home=${env.APPSRV_HOME}
database.name=nodb
database.nodb.location=/opt/signserver/nodb
````

#### Specify a path for SignServer to write files
````
$ mkdir /opt/signserver/nodb
````

## E. Deploy SignServer
````
$ sudo bin/ant deploy
````

## F. Administration Web
````
$ sudo bin/signserver wsadmins -allowany
````

# 5. Setup CryptoWorker and PDFSigner to sign documents

Please follow instructions in this video: [Container Signing with SignServer and Cosign](https://www.youtube.com/watch?v=sNIv0nYRs1g)












