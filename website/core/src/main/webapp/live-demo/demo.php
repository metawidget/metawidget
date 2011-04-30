<?php $title = 'Live Demo'; require_once '../include/page-start.php'; ?>

	<?php require_once '../include/body-start.php'; ?>

		<h2>Groovy Console with a Swing-based Metawidget</h2>
		
		<p>
			Play with Metawidget by:
		</p>
		
		<ul>
			<li>running the script (click the <img src="../media/groovy-run.gif" alt=""/> button)</li>
			<li>changing the business model code (adding fields, altering annotations etc.)</li>
			<li>using <strong>Script > Add Jar to Classpath</strong> to import your own business classes and see how they render</li>
		</ul>
		
		<p>
			<a href="#not-working">Applet not working for you? Please see below</a>.
		</p>

		<div style="border: 1px solid #cccccc; margin-bottom: 20px">
			<applet archive="metawidget-annotation.jar, metawidget-core.jar, metawidget-groovy.jar, metawidget-hibernatevalidator.jar, metawidget-java5.jar, metawidget-jpa.jar, metawidget-swing.jar, groovy-all.jar, hibernate-validator.jar, persistence-api.jar, console-applet.jar" code="org.metawidget.website.swing.console.GroovyConsoleApplet" width="100%" height="600">
				<param name="codebase_lookup" value="false"/>
				<param name="java_arguments" value="-Djnlp.packEnabled=true"/>
				<param name="script" value="import javax.swing.*; import javax.persistence.*;\nimport org.hibernate.validator.*; import org.metawidget.inspector.annotation.*;\nimport org.metawidget.inspector.composite.*; import org.metawidget.inspector.hibernate.validator.*;\nimport org.metawidget.inspector.iface.*; import org.metawidget.inspector.impl.*;\nimport org.metawidget.inspector.impl.propertystyle.*; import org.metawidget.inspector.impl.propertystyle.groovy.*;\nimport org.metawidget.inspector.java5.*; import org.metawidget.inspector.jpa.*;\nimport org.metawidget.inspector.propertytype.*; import org.metawidget.swing.*;\nimport org.metawidget.swing.layout.*;\n\n// Groovy/JPA/Hibernate Validator Business Model\n// (many other back-end architectures also supported, including non-annotation-based ones)\n\nenum Title { Mr, Mrs, Miss }\n\nclass Person {\n\t@Id\n\tString id;\n\n\t@NotNull\n\tTitle title;\n\n\t@UiComesAfter(&quot;title&quot;)\n\tString firstname;\n\n\t@Column(nullable=false) @UiComesAfter(&quot;firstname&quot;)\n\tString surname;\n\n\t@UiComesAfter(&quot;surname&quot;) @Min(0l) @Max(100l)\n\tint age;\n\n\t@UiComesAfter(&quot;age&quot;)\n\tboolean retired;\n\n\t@UiSection(&quot;Address&quot;) @UiLabel(&quot;&quot;) @UiComesAfter(&quot;retired&quot;)\n\tAddress address = new Address();\n\n\t@Lob @UiComesAfter(&quot;address&quot;) @UiSection(&quot;Other&quot;)\n\tString notes;\n}\n\nclass Address {\n\tString street;\n\n\t@UiComesAfter(&quot;street&quot;)\n\tString city;\n\n\t@UiLookup([&quot;Anytown&quot;,&quot;Cyberton&quot;,&quot;Lostville&quot;,&quot;Whereverton&quot;]) @UiComesAfter(&quot;city&quot;)\n\tString state;\n\n\t@UiComesAfter(&quot;state&quot;)\n\tString postcode;\n}\n\n// Simple UI around a couple of Metawidgets (Metawidget does not try to 'own' the entire UI)\n\nclass PersonDialog extends JDialog {\n\n\tPersonDialog() {\n\t\tsuper(SwingUtilities.getSharedOwnerFrame(),SwingMetawidget.class.getPackage().getSpecificationTitle() + &quot; v&quot; + SwingMetawidget.class.getPackage().getSpecificationVersion() + &quot; Live Demo&quot;,true);\n\n\t\t// Metawidget Inspectors (can alternatively be configured via XML)\n\n\t\tPropertyStyle propertyStyle = new GroovyPropertyStyle();\n\t\tBaseObjectInspectorConfig groovyConfig = new BaseObjectInspectorConfig().setPropertyStyle(propertyStyle);\n\t\tJpaInspectorConfig jpaConfig = new JpaInspectorConfig().setPropertyStyle(propertyStyle);\n\t\tInspector inspector = new CompositeInspector( new CompositeInspectorConfig().setInspectors(\n\t\t\tnew PropertyTypeInspector(groovyConfig),\n\t\t\tnew MetawidgetAnnotationInspector(groovyConfig),\n\t\t\tnew HibernateValidatorInspector(groovyConfig),\n\t\t\tnew Java5Inspector(groovyConfig),\n\t\t\tnew JpaInspector(jpaConfig) ));\n\n\t\t// Metawidget for Swing (many other front-end frameworks also supported)\n\n\t\tSwingMetawidget metawidget1 = new SwingMetawidget();\n\t\tmetawidget1.setInspector(inspector);\n\t\tmetawidget1.setToInspect(new Person());\n\t\tTabbedPaneLayoutDecorator layout = new TabbedPaneLayoutDecorator(new TabbedPaneLayoutDecoratorConfig()\n\t\t\t.setLayout(new GridBagLayout()));\n\t\tmetawidget1.setMetawidgetLayout(layout);\n\t\tmetawidget1.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));\n\t\tadd(metawidget1,java.awt.BorderLayout.CENTER);\n\n\t\tSwingMetawidget metawidget2 = new SwingMetawidget();\n\t\tmetawidget2.setInspector(inspector);\n\t\tmetawidget2.setToInspect(this);\n\t\tmetawidget2.setMetawidgetLayout(new FlowLayout());\n\t\tadd(metawidget2,java.awt.BorderLayout.SOUTH);\n\n\t\tpack();\n\t}\n\n\t// Actions (many other action styles also supported, such as BPM ones)\n\n\t@UiAction\n\tvoid save() {\n\t\tJOptionPane.showMessageDialog(this,&quot;Save button clicked!&quot;,getTitle(),JOptionPane.INFORMATION_MESSAGE);\n\t}\n\n\t@UiAction @UiComesAfter(&quot;save&quot;)\n\tvoid cancel() {\n\t\tsetVisible(false);\n\t}\n}\n\nnew PersonDialog().show();"/>
			</applet>
		</div>

		<a name="not-working"><h2 style="color: black">Applet not working for you?</h2></a>
		
		<p>
			Please note:
		</p>
		
		<ul>
			<li>
				the Groovy Console applet itself is over 4MB, so may take a few minutes to load first time
			</li>
			<li>
				the Groovy Console applet is
				<a href="http://jira.codehaus.org/browse/GROOVY-3604" target="_blank">only known to work with the Oracle Java plug-in on Windows</a>
				(IE/Firefox/Chrome). If you use a different environment, try <a href="/download.php">downloading the full distribution</a> instead
			</li> 
		</ul>
		
		<p>
			If none of the above helps, please <a href="mailto:support@metawidget.org">let us know</a>!
		</p>

		<h2>Like what you see?</h2>
		
		<p>
			Then <a href="/download.php">download the full distribution</a> which includes:
		</p>
		
		<ul>
			<li>extensive documentation (including step-by-step tutorials)</li>
			<li>pluggable widget libraries</li>
			<li>pluggable layouts</li>
			<li>multiple inspectors</li>
			<li>data binding</li>
			<li>Web-based and mobile-based Metawidgets</li>
			<li>...and much more!</li>
		</ul>
		
	<?php require_once '../include/body-end.php'; ?>		

<?php require_once '../include/page-end.php'; ?>
