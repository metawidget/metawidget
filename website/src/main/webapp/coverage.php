<?php $title = 'Code Coverage'; require_once 'include/page-start.php'; ?>

	<?php $floater='xml.png'; require_once 'include/body-start.php'; ?>

		<h2>Code Coverage</h2>
		
		<p>
			Metawidget uses <a href="http://www.junit.org" target="_blank">JUnit</a>, <a href="http://webtest.canoo.com" target="_blank">WebTest</a> and
			<a href="http://emma.sourceforge.net" target="_blank">EMMA</a> code coverage to develop our test suite.
		</p>

		<p>
			<a href="${context}/doc/coverage/index.html" target="_blank">Click here</a> to see how we're doing.
		</p> 		
		
	<?php require_once 'include/body-end.php'; ?>		

<?php require_once 'include/page-end.php'; ?>
