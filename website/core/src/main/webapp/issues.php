<?php $title = 'Issue Tracking'; require_once 'include/page-start.php'; ?>

	<?php $floater='mobile.png'; require_once 'include/body-start.php'; ?>

		<h2>Issue Tracking</h2>

		<p>
			The Metawidget team use an Issue Tracker to track bugs and feature requests.
		</p>		
		
		<p>
			Before reporting a new issue, please make sure you've searched the <a href="/documentation.php">documentation</a> and the
			<a href="/support.php">forums</a>, as your query may have already been
			answered.
		</p>
	
		<p>
			Metawidget's Issue Tracker is hosted on GitHub. <a href="https://github.com/metawidget/metawidget/issues">Click here</a> to visit the Issue Tracker.
		</p>
		
		<h2 class="h2-underneath">Code Coverage</h2>
		
		<p>
			Metawidget uses a combination of
			<a href="http://eclemma.org/jacoco" target="_blank">Jacoco</a>,
			<a href="http://jenkins-ci.org" target="_blank">Jenkins</a>,
			<a href="http://www.junit.org" target="_blank">JUnit</a>,
			<a href="http://sonarsource.org" target="_blank">Sonar</a> and
			<a href="http://webtest.canoo.com" target="_blank">WebTest</a>
			and other tools to monitor code coverage and project quality metrics.
		</p>

		<p>
			<a href="/doc/coverage/index.html" target="_blank">Click here</a> to see how we're doing.
		</p>
				
	<?php require_once 'include/body-end.php'; ?>		

<?php require_once 'include/page-end.php'; ?>
