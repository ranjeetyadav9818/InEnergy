
Inenergis:
Author: Immo Benjes & John Squire

Database setup:
================

This describes the mysql config for Wildfly Development Environment. For production and QA, a managed datasource should be configured through the admin console or command line.

1. Download and Copy the MySQL Connector to [wildfly-root-install-DIR]/modules/system/layers/base/com/mysql/main
2. Also ensure you have the following module.xml in the same directory
	<?xml version="1.0" encoding="UTF-8"?>
	 <module xmlns="urn:jboss:module:1.0" name="com.mysql">
	  <resources>
	    <resource-root path="[mysql-connector-jar-filename]"/>
	  </resources>
	  <dependencies>
	    <module name="javax.api"/>
	  </dependencies>
	</module>
3. Edit [wildfly-install-root]/standalone/configuration/standalone.xml and add the DRCC DataSource to the datasources subsystem as follows:
        <datasource jta="true" jndi-name="java:jboss/datasources/DRCCDS" pool-name="DRCCDS" enabled="true" use-ccm="true">
            <connection-url>jdbc:mysql://localhost:8889/DRCC?useUnicode=true&amp;characterEncoding=utf8&amp;charactetrResultSets=utf8</connection-url>
            <driver-class>com.mysql.jdbc.Driver</driver-class>
            <driver>mysql</driver>
            <security>
                <user-name>root</user-name>
                <password>root</password>
            </security>
            <validation>
                <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLValidConnectionChecker"/>
                <background-validation>true</background-validation>
                <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLExceptionSorter"/>
            </validation>
        </datasource>
4. Edit [wildfly-install-root]/standalone/configuration/standalone.xml and add the mysql driver stanza to the datasource subsystem as follows:
		<subsystem xmlns="urn:jboss:domain:datasources:3.0">
            <datasources>
                <datasource jndi-name="java:jboss/datasources/ExampleDS" pool-name="ExampleDS" enabled="true" use-java-context="true">
                    <connection-url>jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE</connection-url>
                    <driver>h2</driver>
                    <security>
                        <user-name>sa</user-name>
                        <password>sa</password>
                    </security>
                </datasource>
                <drivers>
                    <driver name="h2" module="com.h2database.h2">
                        <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
                    </driver>
                    <driver name="mysql" module="com.mysql"/>      <!-- THIS IS THE LINE YOU WANT TO ADD --> 
                </drivers>
            </datasources>
        </subsystem>        
5. create your database in mysql: CREATE DATABASE DRCC
6. create the required schema and tables using the DBSchema.sql file in the datamodel directory of the project. remember to switch to DRCC so that you create the tables in the correct database: USE DRCC
7. DataSource is configured in webapp/WEB-INF/drcc-ds.xml which is referenced in the persistence.xml. Only change this if it is going to change for the development team. If you change it you need to update src/main/resources/META-INF/persistence.xml
8. In [wildfly-install-root]/standalone/configuration/standalone.xml edit the logging part to include the username in the logs:
        <formatter name="PATTERN">
            <pattern-formatter pattern="%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p [%c][%X{username}] (%t) %s%e%n"/>
        </formatter>


IMPORTANT: Check on the wiki how to configure your settings.xml to use liquibase

Amazon RedShift Connection:
=============================

CREATING JDBC CONNECTION ON INTELLIJ

1.- Download the driver at `https://console.aws.amazon.com/redshift/home?region=us-east-1#cluster-connection`
2.- On the same page select the cluster (public) and copy the jdbc url generated. In the example 
`jdbc:redshift://public.c4t8oqlq7wzz.us-east-1.redshift.amazonaws.com:5439/test`
3.- Create a new driver in intellij (Data Source Properties -> click Add and select Database Driver)
4.- Uncheck "Use provided driver" and add the jar file downloaded on step 1
5.- Select PostgreSQL dialect, un check autocommit
6.- Add an url template `jdbc:redshift://{host}:{port}/{database}`
7.- Create a connection, select the driver, provide user and password and the url from step 2
provide also the database name (test)
8.- Test the connection to see if it works

CREATING RedShift DataSource on Wildfly

1.- Under de folder [wildfly-install-root]/libexec/modules/system/layers/base/com
 Create the following folder structure (the files will be included later)
 
    redshift
        └── main
            ├── RedshiftJDBC42-1.1.17.1017.jar
            └── module.xml

2.- Copy to the main folder the jar for whe JDBC Driver downloaded from Amazon redshift console (left pannel -> Connect Client)
`https://console.aws.amazon.com/redshift/home?region=us-east-1#cluster-connection:`
The driver proposed next to the pick list labeled JDBCDriver must be selected and downloaded (in this case the version 1.1.17).
3.- Create the file module.xml with the following content:

    <module xmlns="urn:jboss:module:1.3" name="com.redshift">
        <resources>
            <resource-root path="RedshiftJDBC42-1.1.17.1017.jar"/>
        </resources>
        <dependencies>
            <module name="javax.api"/>
            <module name="javax.transaction.api"/>
        </dependencies>
    </module>
    
4.- Go to /usr/local/opt/wildfly-as/libexec/standalone/configuration and edit (with the server shut down) standalone.xml
5.- Find the node `<subsystem xmlns="urn:jboss:domain:datasources:4.0">`
6.- Add the entries for the datasource and the driver. 

    <subsystem xmlns="urn:jboss:domain:datasources:4.0">
            <datasources>
               ...
                <datasource jndi-name="java:jboss/datasources/RedShift" pool-name="RedShift">
                    <connection-url>jdbc:redshift://public.c4t8oqlq7wzz.us-east-1.redshift.amazonaws.com:5439/test</connection-url>
                    <driver>redShift</driver>
                    <pool>
                        <min-pool-size>10</min-pool-size>
                        <max-pool-size>20</max-pool-size>
                        <prefill>true</prefill>
                    </pool>
                    <security>
                        <user-name>dbuser</user-name>
                        <password>pXfLT4rwDZkviFQ4*K7s</password>
                    </security>
                </datasource>
                ...
                <drivers>
                    ...
                    <driver name="redShift" module="com.redshift">
                        <xa-datasource-class>com.amazon.redshift.jdbc42.Driver</xa-datasource-class>
                    </driver>
                </drivers>
            </datasources>

7.- Look for the following server log lines (search redShift) to see if everything was ok. 

`13:50:15,271 INFO  [org.jboss.as.connector.subsystems.datasources] (ServerService Thread Pool -- 33) WFLYJCA0005: Deploying non-JDBC-compliant driver class com.amazon.redshift.jdbc42.Driver (version 1.1)
13:50:15,272 INFO  [org.jboss.as.connector.deployers.jdbc] (MSC service thread 1-4) WFLYJCA0018: Started Driver service with driver-name = redShift`

8.- To access the datSource from the controller add the following var:
	
	@Resource(name="RedShift", lookup="java:jboss/datasources/RedShift")
	DataSource redShiftDataSource;
	
Datasource is the type javax.sql.DataSource	
	

Mail Setup:
=============
Modify the wildfly configuration file on wildfly-as/libexec/standalone/configuration/standalone.xml :

+ Add/modify the node socket-binding-group (at the end of the file) 

        <outbound-socket-binding name="mail-smtp">
            <remote-destination host="smtp.gmail.com" port="465"/>
        </outbound-socket-binding>

+ Add/modify te node profile/subsystem  with namespace xmlns="urn:jboss:domain:mail:2.0" to look like this :

        <subsystem xmlns="urn:jboss:domain:mail:2.0">
            <mail-session name="default" jndi-name="java:jboss/mail/Default">
                <smtp-server outbound-socket-binding-ref="mail-smtp"/>
            </mail-session>
            <mail-session name="gmail" jndi-name="java:jboss/mail/gmail">
                <smtp-server outbound-socket-binding-ref="mail-smtp" ssl="true" username="no-reply@inenergis.com" password="3iEOJmw87FE2rZt"/>
            </mail-session>
        </subsystem>
 The node to add is mail-session name="gmail"

Compilation:
============
Before you compile the project you have to install local libraries so that maven can find them.
cd lib
./installLibs.sh

If you are using JRebel run
mvn jrebel:generate

to generate the rebel.xml file for your system (that file should not be checked in)

Now read the 'generated' Readme below. Once you have done that you are good to go (start wildfly first):

mvn clean package wildfly:deploy -P {profile}

When the application is started the first time 4 users are created:

admin, test1, test2 and test3. All 4 have the initial password 'demo'
 


Inenergis: Assortment of technologies including Arquillian
========================
Author: Pete Muir
Level: Intermediate
Technologies: CDI, JSF, JPA, EJB, JPA, JAX-RS, BV
Summary: An example that incorporates multiple technologies
Target Project: WildFly
Source: <https://github.com/wildfly/quickstart/>

What is it?
-----------

This is your project! It is a sample, deployable Maven 3 project to help you get your foot in the door developing with Java EE 7 on JBoss WildFly.

This project is setup to allow you to create a compliant Java EE 7 application using JSF 2.2, CDI 1.1, EJB 3.3, JPA 2.1 and Bean Validation 1.1. It includes a persistence unit and some sample persistence and transaction code to introduce you to database access in enterprise Java.

There is a tutorial for this quickstart in the [Getting Started Developing Applications Guide](https://github.com/wildfly/quickstart/guide/Inenergis/).

System requirements
-------------------

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.1 or better.

The application this project produces is designed to be run on JBoss WildFly.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md) before testing the quickstarts.


Start JBoss WildFly with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](https://github.com/jboss-developer/jboss-eap-quickstarts#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Configure a profile in your settings.xml that contains this property (any time you package the solution thorugh maven the configuration files will be updated)

        <wildflyConfigurationFolder>{your_wildfly_folder}/standalone/configuration</wildflyConfigurationFolder>

4. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy -P {name_of_the_profile_that_you_created_in_previous_point}

5. This will deploy `target/DRCC.war` to the running instance of the server.
 

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/DRCC/>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-wildfly-remote


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

Elasticsearch Setup:
=============

1. Install Elasticsearch using Homebrew:

        brew update
        brew install elasticsearch

2. Run Elastic search instance:

        elasticsearch
    
Kibana Setup:
==============

1. Download kibana-xxx.tar.gz from <https://www.elastic.co/downloads/kibana>

        tar -zxvf kibana-xxx.tar.gz
        cd kibana-xxx
        
2. Open `config/kibana.yml` in an editor and set `elasticsearch.url` to point at your Elasticsearch instance

3. Run `bin/kibana`
    
4. Point your browser at <http://localhost:5601>