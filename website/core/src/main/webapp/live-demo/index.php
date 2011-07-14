<?php $title = 'Live Demo'; require_once '../include/page-start.php'; ?>

	<?php $floater='browser.png'; require_once '../include/body-start.php'; ?>

		<h2>Play with Metawidget right now, in your browser!</h2>
		
		<p style="font-style: italic">
			Metawidget is a smart User Interface widget that populates itself, at runtime, with UI components to match
			the properties of your business objects.			
		</p>
		<p style="margin-bottom: 40px; font-style: italic">
			Even though Metawidget is Free Software, licensed under the <a href="http://www.gnu.org/licenses/lgpl.html">LGPL</a>, we
			understand it still requires your commitment to download, install and evaluate. That's why we've put together this
			Live Demo - it gives you an immediate taste of Metawidget right here, in your browser.
		</p>
		
		<p>
			Click the button below to start a Groovy console applet (<a href="http://jira.codehaus.org/browse/GROOVY-3604">may not work in all browsers</a>) and populate it with a small script that:
		</p>
		
		<ul>
			<li>defines a business model</li>
			<li>configures Metawidget inspectors for Groovy, JPA and Hibernate Validator</li>
			<li>launches a Swing-based Metawiget</li>
		</ul>
		
		<p>
			You can then play with Metawidget by:
		</p>
		
		<ul>
			<li>running the code</li>
			<li>changing the business model code (adding fields, altering annotations etc.)</li>
			<li>importing your own business object JARs to see how Metawidget renders them</li>
		</ul>
		
		<center>
			<a href="demo.php"><img src="/media/start-live-demo.jpg" alt="" style="border: 0; margin-top: 20px"/></a>
		</center>
		
	<?php require_once '../include/body-end.php'; ?>		

<?php require_once '../include/page-end.php'; ?>
