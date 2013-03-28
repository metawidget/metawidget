Metawidget Source Code
======================

This folder contains full source code (including tests) for Metawidget. To build, change to the /src folder and type:

	mvn clean install
	
This will require you to have the Android SDK installed, as some portions of Metawidget are Android-based. You will also require at least -Xmx2048m -XX:MaxPermSize=512m of RAM.

To build individual modules, change to that module's root first. For example:
	
	cd /src/modules/java/vaadin/core
	mvn clean install
	
This will build just the individual module to its target folder, for example /src/modules/java/vaadin/core/target.
	
Steps that can be run after the build include:

	mvn -f examples/pom.xml install -Dappserver=jetty
			- retest the examples under Jetty (default is Tomcat)
	mvn -f modules/pom.xml javadoc:aggregate
			- build JavaDoc
	mvn -f website/pom.xml clean install
			- build the Metawidget web site
	mvn -N assembly:single
			- build the *-bin, *-examples and *-src distribution packages
	mvn -X -f modules/pom.xml -Dmaven.test.skip=true deploy
			- build modules in debug mode			
	mvn -X -f modules/java/all/pom.xml -Dmaven.test.skip=true deploy
			- build metawidget-all.jar in debug mode
	mvn -X -N deploy
			- release to Maven repository
