Metawidget for Java
===================

The file metawidget-all.jar contains all of Metawidget bundled into one JAR for easy deployment. To get started, we strongly recommend the Metawidget tutorial: http://metawidget.org/doc/reference/en/html/ch01.html#section-introduction-part1

If you need more control over how Metawidget is included in your application, fine-grained JARs are also available. For example you can:

* reduce the final size of your application by only including those Metawidget plugins for technologies you use; or
* resolve class loading issues by including different parts of Metawidget for different application tiers

To use fine-grained JARs, use Maven to specify dependencies on a per-technology basis (instead of using metawidget-all). For example:

    org.metawidget.modules.faces:metawidget-richfaces
    org.metawidget.modules.swing:metawidget-beansbinding
    org.metawidet.modules:metawidget-jpa

Maven will automatically drag in related dependencies for you, such as org.metawidget.modules:metawidget-core.

To browse all available fine-grained dependencies search under: http://search.maven.org/#browse|5459107
