KarafFeatureSerHelloRsKarafFeatureSerHelloRsKarafFeatureSerHelloRsKarafFeatureSerHelloRsKarafFeatureSerHelloRs= Project Karaf.DOE

Demonstration of deployment on the Karaf/OSGi runtime

Install Karaf from http://karaf.apache.org/download.html[link] and unzip to e.g., c:\Java directory.

Define the environment variable KARAF_HOME and add to path %KARAF_HOME%\bin

Start the Karaf in a cleaned state, depending on you want or not preserve previous state. 

* <KARAF_HOME>\bin\karaf [clean]

You can  install the webconsole feature and open it through the address http://localhost:8181/system/console/bundles[link].

See more about karaf installation at https://karaf.apache.org/manual/latest/#_prerequisites[link].

In the Karaf console give the following commands sequence:

[standard output]
----
feature:install webconsole

feature:install scr
feature:repo-add cxf-dosgi
feature:install cxf-dosgi-provider-ws

feature:repo-add aries-rsa 1.14.0
feature:install aries-rsa-core
feature:install aries-rsa-discovery-zookeeper
feature:install aries-rsa-discovery-config

feature:repo-add mvn:isos.tutorial.isyiesd.cesosgi.serosgihellorscli.serosgihellorsclidoe/KarafFeatureSerHelloRsCli/0.1.0/xml/features
feature:install KarafFeatureSerHelloRsCli

HelloWorldImpl.sayHelloWorld(): ## Hello World! ##
HelloWorldClient.start(): ## HELLO WORLD! ##
karaf@root()>             
karaf@root()> bundle:list
START LEVEL 100 , List Threshold: 50
ID | State    | Lvl | Version | Name
---+----------+-----+---------|-----------------------------------------
22 | Active   |  80 | 4.2.8   | Apache Karaf :: OSGi Services :: Event
44 | Resolved |  50 | 1.0.0   | helloworldapi
45 | Resolved |  50 | 1.0.0   | helloworldimpl Bundle
46 | Active   |  50 | 1.0.0   | helloworldclient Bundle
----

