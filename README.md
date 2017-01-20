# microservice
Example microservice for showing tasks with dropwizard.

Build it with `mvn install`. You can run it with `java -jar target/microservice-1.0-SNAPSHOT.jar server microservice.yml`




Note: 
In order to include the LS3-1.0.jar in the maven repository run 
"mvn install:install-file -Dfile="src\main\resources\ls3\LS3-1.0.jar" -DgroupId=de.andreasschoknecht -DartifactId=LS3 -Dversion=1.0 -Dpackaging=jar"
(and include it in the pom.xml)

		<dependency>
			<groupId>de.andreasschoknecht</groupId>
			<artifactId>LS3</artifactId>
			<version>1.0</version>
		</dependency>
		
		install:install-file -Dfile="C:\Users\Carol\ECLIPSE_WORKSPACE\git\MicroService\microservice\src\main\resources\ls3\LS3-1.0.1-jar-with-dependencies.jar" -DgroupId=de.andreasschoknecht -DartifactId=LS3 -Dversion=1.0.1 -Dpackaging=jar