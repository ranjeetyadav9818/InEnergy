
This documentation has been tested for Jboss Fuse version 6.2.1
Newer versions could not work as expected.

Camel Router Project for Blueprint (OSGi)
=========================================

To build this project use

    mvn clean install

To run the project you can execute the following Maven goal

to run the project in Fuse:

To install 

features:chooseurl camel 2.15.1.redhat-620133
features:install camel-sql
features:install camel-jsch
features:install camel-quartz2
features:install camel-ssh
features:install camel-zipfile
features:install camel-jpa
features:install jdbc
features:install transaction
features:install jpa
features:install hibernate
features:install jndi
features:addurl mvn:org.ops4j.pax.jdbc/pax-jdbc-features/0.8.0/xml/features
features:install pax-jdbc-config
features:install pax-jdbc-mysql
features:install pax-jdbc-pool-dbcp2
features:install cxf-ws-security



osgi:install wrap:mvn:mysql/mysql-connector-java/5.1.27
osgi:install wrap:mvn:org.apache.commons/commons-lang3/3.4
osgi:install wrap:mvn:commons-beanutils/commons-beanutils/1.9.2
osgi:install wrap:mvn:commons-dbcp/commons-dbcp/1.4
osgi:install wrap:mvn:org.apache.camel/camel-core-osgi/2.15.1.redhat-620133
osgi:install wrap:mvn:joda-time/joda-time/2.9.2
osgi:install wrap:mvn:com.amazonaws/aws-java-sdk-osgi/1.11.29
osgi:install wrap:mvn:com.amazonaws/jmespath-java/1.0
osgi:install wrap:mvn:com.fasterxml.jackson.core/jackson-annotations/2.6.7
osgi:install wrap:mvn:com.fasterxml.jackson.core/jackson-core/2.6.7
osgi:install wrap:mvn:com.fasterxml.jackson.core/jackson-databind/2.6.7
osgi:install wrap:mvn:com.fasterxml.jackson.dataformat/jackson-dataformat-cbor/2.6.7
osgi:install wrap:mvn:org.apache.httpcomponents/httpclient-osgi/4.5.2
osgi:install wrap:mvn:org.apache.httpcomponents/httpcore-osgi/4.4.5
osgi:install wrap:mvn:org.aspectj/aspectjrt/1.8.9
osgi:install wrap:mvn:junit/junit/4.12
osgi:install wrap:mvn:org.projectlombok/lombok/1.16.10
osgi:install wrap:mvn:com.google.code.gson/gson/2.7
osgi:install wrap:mvn:com.squareup.okhttp3/okhttp/3.4.1
osgi:install wrap:mvn:com.squareup.okio/okio/1.11.0
osgi:install wrap:mvn:org.ini4j/ini4j/0.5.4


Database configuration:

copy file from profiles/org.ops4j.datasource-mysql.cfg to etc in your fuse installation and adapt it to your local database configurations

Client certificate configuration:
To connect with PG&E we have to use a certificate so the communication is done on the basis of mutual SSL. So the certificate has to be somewhere in developer's system outside of the code.

copy file from profiles/certificates/pgeCertificate.p12 to /opt/certificates

osgi:install -s mvn:com.inenergis/DRCCModel/1.0.0-SNAPSHOT
osgi:install -s mvn:com.inenergis/drcc-camel/1.0.0-SNAPSHOT


osgi:install -s mvn:com.<groupId>/<artifactId>/<version>

That will return an ID

to stop the route:

osgi:uninstall






IB: Not applicable to FUSE but can be used for testing
    mvn camel:run

To deploy the project in OSGi. For example using Apache ServiceMix
or Apache Karaf. You can run the following command from its shell:

    osgi:install -s mvn:com.inenergis/drcc-camel/1.0.0-SNAPSHOT

For more help see the Apache Camel documentation

    http://camel.apache.org/
    
    
    
    
 Configuring profiles for dev, qa and prod
 
 In order to allow different DB settings, ftp server etc for the different profiles we have 
 profile properties under profiles/ for dev prod and qa.
 The profile is selected by setting the environment variable DRCC_ENV. If nothing is set the dev profile is used
 For qa use DRCC_ENV=QA and for prod DRCC_ENV=PROD.
 
 You can either specify the environment variable on the console or bashrc or at compile time with 
 mvn clean install -DDRCC_ENV=QA
 
 When compiling, variables in the files under src/main/resources like ${some_variable_name} are being replaced with the values
 specified in the config.properties files for the different profiles:
 The variables are specified in the profiles/XXX/config.properties 
 
 Apart from configuring the DB connection in the blueprint.xml file it also sets the properties in the src/main/resources/drcc.properties file
 This file is loaded at runtime
    
    InputStream in = this.getClass().getResourceAsStream("/drcc.properties");
	Properties p = new Properties();
	p.load(in);
    
The properties can then be accessed with e.g. 
    String ftpIn = p.getProperty("drcc.ftp.in.url");
    
    
