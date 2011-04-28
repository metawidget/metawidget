To build all examples, change to the /src/examples folder and type:

	mvn clean install
	
This will require you to have the Android SDK installed, as some of the examples are Android-based.

To build individual examples, some will build just by changing to their folder and typing:

	mvn clean install
	
However the Address Book samples have shared, dependent projects that must be built first. The easiest way to build these is to change to the /src/examples folder and type:

	mvn -am -pl org.metawidget.examples.faces:addressbook-faces integration-test
	
Where '-pl' means 'build the following project' and '-am' means 'also build shared, dependent projects'
