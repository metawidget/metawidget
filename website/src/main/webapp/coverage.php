<?php $title = 'Code Coverage'; require_once 'include/page-start.php'; ?>

	<?php $floater='xml.png'; require_once 'include/body-start.php'; ?>

		<h2>Code Coverage</h2>
		
		<p>
			Metawidget uses a combination of
			<a href="http://cobertura.sourceforge.net" target="_blank">Cobertura</a>,
			<a href="http://eclemma.org/jacoco" target="_blank">Jacoco</a>,
			<a href="http://jenkins-ci.org" target="_blank">Jenkins</a>,
			<a href="http://www.junit.org" target="_blank">JUnit</a>,
			<a href="http://sonarsource.org" target="_blank">Sonar</a> and
			<a href="http://webtest.canoo.com" target="_blank">WebTest</a>
			to monitor code coverage and other project quality metrics.
		</p>

		<p>
			<a href="${context}/doc/coverage/index.html" target="_blank">Click here</a> to see how we're doing.
		</p> 		
		
	<?php require_once 'include/body-end.php'; ?>

<?php require_once 'include/page-end.php'; ?>
