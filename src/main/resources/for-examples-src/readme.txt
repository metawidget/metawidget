Metawidget Examples Source Code
===============================

This folder contains full source code (including tests) for all Metawidget examples. To build them all, change to the /src/examples folder and type:

	mvn integration-test
	
This will require you to have the Android SDK installed, as some of the examples are Android-based.

To build individual examples, some will build just by changing to their folder and typing:

	mvn integration-test
	
However the Address Book samples have shared, dependent projects that must be built first. The easiest way to build these is to change to the /src/examples folder and type:

	mvn -pl org.metawidget.examples.faces:addressbook-faces -am integration-test
	
Where '-pl' means 'build the following project' and '-am' means 'also build shared, dependent projects'
