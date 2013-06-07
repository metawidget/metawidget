	<div id="logo">
		<h1><a href="http://metawidget.org">Metawidget</a></h1>
	</div>
	
	<div id="top-tabs">
		<ul>
			<li<?php if ( $_SERVER['PHP_SELF'] == '/videos.php' ) echo ' class="active"'; ?>>
				<a href="/videos.php"
					<?php if ( $_SERVER['PHP_SELF'] != '/videos.php' ) echo 'onmouseover="bounceInQueue( this, -7 )" onmouseout="bounceInQueue( this, 0 )"'; ?>
					id="videos">
					Videos
				</a>
			</li>
			<li class="wide<?php if ( $_SERVER['PHP_SELF'] == '/screenshots.php' ) echo ' wide-active'; ?>">
				<a href="/screenshots.php"
					<?php if ( $_SERVER['PHP_SELF'] != '/screenshots.php' ) echo 'onmouseover="bounceInQueue( this, -7 )" onmouseout="bounceInQueue( this, 0 )"'; ?>
					id="screenshots">
					Screenshots
				</a>
			</li>
			<li class="wide<?php if ( $_SERVER['PHP_SELF'] == '/download.php' ) echo ' wide-active'; ?>">				
				<a href="/download.php"
					<?php if ( $_SERVER['PHP_SELF'] != '/download.php' ) echo 'onmouseover="bounceInQueue( this, -7 )" onmouseout="bounceInQueue( this, 0 )"'; ?>						
					id="download">
					Download v${project.version}
				</a>
			</li>
		</ul>
	</div>
	
	<div id="content-spacer-for-ie"></div>
	
	<table id="content">
		<tr>			
			<td id="left-bar">
				<div id="left-menu1" class="left-menu">
					<h2>Get Started</h2>
					<ul>
						<li<?php if ( $_SERVER['PHP_SELF'] == '/index.php' ) echo ' class="active"'; ?>><a href="/index.php">Overview</a></li>
						<li<?php if ( $_SERVER['PHP_SELF'] == '/news.php' ) echo ' class="active"'; ?>><a href="/news.php">News</a></li>
						<li<?php if ( $_SERVER['PHP_SELF'] == '/download.php' ) echo ' class="active"'; ?>><a href="/download.php">Download</a></li>
						<li<?php if ( $_SERVER['PHP_SELF'] == '/documentation.php' ) echo ' class="active"'; ?>><a href="/documentation.php">Documentation</a></li>
						<li<?php if ( $_SERVER['PHP_SELF'] == '/support.php' ) echo ' class="active"'; ?>><a href="/support.php">Support</a></li>
					</ul>
				</div>
				<div id="left-menu2" class="left-menu">
					<h2>Get Involved</h2>
					<ul>
						<li<?php if ( $_SERVER['PHP_SELF'] == '/blogs.php' ) echo ' class="active"'; ?>><a href="/blogs.php">Blogs and Articles</a></li>
						<li<?php if ( $_SERVER['PHP_SELF'] == '/survey.php' ) echo ' class="active"'; ?>><a href="/survey.php">Complete our Survey</a></li>
						<li<?php if ( $_SERVER['PHP_SELF'] == '/contributing.php' ) echo ' class="active"'; ?>><a href="/contributing.php">Contributing</a></li>
						<li<?php if ( $_SERVER['PHP_SELF'] == '/issues.php' ) echo ' class="active"'; ?>><a href="/issues.php">Issue Tracker</a></li>
						<li<?php if ( $_SERVER['PHP_SELF'] == '/coverage.php' ) echo ' class="active"'; ?>><a href="/coverage.php">Code Coverage</a></li>
					</ul>
				</div>
				<div class="badges" style="height: 519px; padding-top: 5px; border: 1px solid #dddddd; background-color: #eeeeee">
					<span>Metawidget integrates with:</span>

					<a href="http://java.sun.com/javaee" target="_blank" style="top: 30px; left: 5px">
						<img src="/media/logos/logo-java.gif" alt="Java" style="border: 0px" />
					</a>
					<a href="http://code.google.com/android" target="_blank" style="top: 40px; left: 65px">
						<img src="/media/logos/logo-android.gif" alt="Android" style="border: 0px" />
					</a>
					<a href="http://angularjs.org" target="_blank" style="top: 87px; left: 40px">
						<img src="/media/logos/logo-angularjs.gif" alt="AngularJS" style="border: 0px" />
					</a>
					<a href="http://bootstrap.org" target="_blank" style="top: 87px; left: 40px">
						<img src="/media/logos/logo-bootstrap.gif" alt="Bootstrap" style="border: 0px" />
					</a>
					<a href="http://freemarker.sourceforge.net" target="_blank" style="top: 70px; left: 67px">
						<img src="/media/logos/logo-freemarker.gif" alt="FreeMarker" style="border: 0px" />
					</a>
					<a href="http://jboss.org/forge" target="_blank" style="top: 93px; left: 95px">
						<img src="/media/logos/logo-forge.gif" alt="JBoss Forge" style="border: 0px" />
					</a>
					<a href="http://code.google.com/webtoolkit" target="_blank" style="top: 135px; left: 25px">
						<img src="/media/logos/logo-gwt.gif" alt="Google Web Toolkit" style="border: 0px" />
					</a>
					<a href="http://groovy.codehaus.org" target="_blank" style="top: 135px; left: 90px">
						<img src="/media/logos/logo-groovy.gif" alt="Groovy" style="border: 0px" />
					</a>
					<a href="http://hibernate.org" target="_blank" style="top: 199px; left: 68px">
						<img src="/media/logos/logo-hibernate.gif" alt="Hibernate" style="border: 0px" />
					</a>
					<a href="http://icefaces.org" target="_blank" style="top: 238px; left: 2px">
						<img src="/media/logos/logo-icefaces.gif" alt="ICEfaces" style="border: 0px" />
					</a>
					<a href="http://jqueryui.com" target="_blank" style="top: 192px; left: 10px">
						<img src="/media/logos/logo-jqueryui.gif" alt="JQuery UI" style="border: 0px" />
					</a>
					<a href="http://java.sun.com/javaee/javaserverfaces" target="_blank" style="top: 225px; left: 138px">
						<img src="/media/logos/logo-jsf.gif" alt="Java Server Faces" style="border: 0px" />
					</a>
					<a href="http://nodejs.org" target="_blank" style="top: 300px; left: 3px">
						<img src="/media/logos/logo-nodejs.gif" alt="Node.js" style="border: 0px" />
					</a>
					<a href="http://oval.sourceforge.net" target="_blank" style="top: 300px; left: 3px">
						<img src="/media/logos/logo-oval.gif" alt="OVal" style="border: 0px" />
					</a>
					<a href="http://primefaces.org" target="_blank" style="top: 276px; left: 46px">
						<img src="/media/logos/logo-primefaces.gif" alt="PrimeFaces" style="border: 0px" />
					</a>
					<a href="http://jboss.org/jbossrichfaces" target="_blank" style="top: 313px; left: 70px">
						<img src="/media/logos/logo-richfaces.gif" alt="RichFaces" style="border: 0px" />
					</a>
					<a href="http://www.scala-lang.org" target="_blank" style="top: 360px; left: 10px">
						<img src="/media/logos/logo-scala.gif" alt="Scala" style="border: 0px" />
					</a>
					<a href="http://springframework.org" target="_blank" style="top: 353px; left: 110px">
						<img src="/media/logos/logo-spring.gif" alt="Spring" style="border: 0px" />
					</a>
					<a href="http://struts.apache.org" target="_blank" style="top: 394px; left: 90px">
						<img src="/media/logos/logo-struts.gif" alt="Struts" style="border: 0px" />
					</a>
					<a href="http://java.sun.com/javase/technologies/desktop" target="_blank" style="top: 394px; left: 20px">
						<img src="/media/logos/logo-swing.gif" alt="Swing" style="border: 0px" />
					</a>
					<a href="http://eclipse.org/swt" target="_blank" style="top: 430px; left: 75px">
						<img src="/media/logos/logo-swt.gif" alt="SWT" style="border: 0px" />
					</a>
					<a href="http://myfaces.apache.org/tomahawk" target="_blank" style="top: 448px; left: 15px">
						<img src="/media/logos/logo-tomahawk.gif" alt="Tomahawk" style="border: 0px" />
					</a>
					<a href="http://vaadin.org" target="_blank" style="top: 489px; left: 45px">
						<img src="/media/logos/logo-vaadin.gif" alt="Vaadin" style="border: 0px" />
					</a>
				</div>
				<div class="badges" style="height: 200px">
					<span>Metawidget is proudly:</span>
	
					<a href="http://opensource.org/docs/definition.php" target="_blank" style="top: 30px; left: 50px">
						<img src="/media/logos/logo-opensource.gif" alt="Open Source (OSI)" style="border: 0px" />
					</a>
					
					<a href="http://jboss.org" target="_blank" style="top: 120px; left: 60px">
						<img src="/media/logos/jbosscommunity-friend_badge-82x.png" alt="Friend of the JBoss Community" style="border: 0px"/>
					</a>					
				</div>
			</td>
			<td id="content-text">
				<?php if ( $floater ) echo '<img id="floater" src="/media/'.$floater.'" alt=""/>'; ?>				
				<?php if ( $useQuickStart ) { echo '<div style="position: relative"><div id="quick-start">'; require_once 'quickstart.php'; echo '</div></div>'; } ?>
