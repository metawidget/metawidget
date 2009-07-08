<%@ tag language="java" %>
<%@ attribute name="floater" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="path" scope="request" value="${pageContext.request.servletPath}"/>
		
	<div id="logo">
		<h1><a href="${context}/index.html">Metawidget</a></h1>
	</div>
	
	<div id="top-tabs">
		<ul>
			<li class="wide<c:if test="${fn:startsWith(path, '/live-demo')}"> wide-active</c:if>"><a href="http://metawidget.sourceforge.net/live-demo/" onclick="pageTracker._link(this.href); return false;">Live Demo!</a></li>
			<li class="wide<c:if test="${path == '/screenshots.html'}"> wide-active</c:if>"><a href="${context}/screenshots.html">Screenshots</a></li>
			<li class="wide<c:if test="${path == '/download.html'}"> wide-active</c:if>"><a href="${context}/download.html">Download v0.8</a></li>
		</ul>
	</div>
	
	<div id="content">
	
		<div id="content-text">
			<c:if test="${!empty floater}"><img id="floater" src="${context}/media/${floater}" alt=""/></c:if>

			<jsp:doBody />

			<div id="footer">
				<a href="/gettingstarted.html">Quick Start</a> |
				<a href="/download.html">Download</a> |
				<a href="/documentation.html">Documentation</a> |
				<a href="/support.html">Support</a>
				<br/>
				Artwork by <a href="http://susiewalkerdesign.com" target="_blank">Susie Walker Design</a>.
				Template by <a href="http://freecsstemplates.org/" target="_blank">Free CSS Templates</a>.
				Additional artwork by <a href="http://visualpharm.com" target="_blank">Visual Pharm</a><br/>
				All trademarks are the property of their respective owners<br/><br/>
				<a href="http://sourceforge.net/projects/metawidget" target="_blank" style="position: relative; top: -10px; left: -5px"><img src="/media/logo-sourceforge.gif" alt="Hosted on SourceForge.net" style="border: 0px"/></a>
				<a href="http://validator.w3.org/check?uri=referer" target="_blank" ><img src="/media/logo-xhtml.gif" alt="Valid XHTML 1.0 Transitional" style="border: 0px" /></a>
			</div>
		</div>
	
		<div id="left-bar">			
			<div id="left-menu1" class="left-menu">
				<h2>Get Started</h2>
				<ul>
					<li <c:if test="${path == '/index.html'}">class="active"</c:if>><a href="${context}/index.html">Overview</a></li>
					<li <c:if test="${fn:startsWith(path, '/live-demo')}">class="active"</c:if>><a href="http://metawidget.sourceforge.net/live-demo/" onclick="pageTracker._link(this.href); return false;">Live Demo!</a></li>
					<li <c:if test="${path == '/news.html'}">class="active"</c:if>><a href="${context}/news.html">News</a></li>
					<li <c:if test="${path == '/download.html'}">class="active"</c:if>><a href="${context}/download.html">Download</a></li>
					<li <c:if test="${path == '/documentation.html'}">class="active"</c:if>><a href="${context}/documentation.html">Documentation</a></li>
					<li <c:if test="${path == '/forums.html'}">class="active"</c:if>><a href="${context}/forums.html">Forums</a></li>
					<li <c:if test="${path == '/support.html'}">class="active"</c:if>><a href="${context}/support.html">Support &amp; Training</a></li>
					<li <c:if test="${path == '/wiki.html'}">class="active"</c:if>><a href="${context}/wiki.html">Wiki Community Area</a></li>
				</ul>
			</div>
			<div id="left-menu2" class="left-menu">
				<h2>Get Involved</h2>
				<ul>
					<li <c:if test="${path == '/blogs.html'}">class="active"</c:if>><a href="${context}/blogs.html">Blogs</a></li>
					<li <c:if test="${path == '/survey.html'}">class="active"</c:if>><a href="${context}/survey.html">Complete our Survey</a></li>
					<li <c:if test="${path == '/contributing.html'}">class="active"</c:if>><a href="${context}/contributing.html">Contributing</a></li>
					<li <c:if test="${path == '/issues.html'}">class="active"</c:if>><a href="${context}/issues.html">Issue Tracker</a></li>
					<li <c:if test="${path == '/coverage.html'}">class="active"</c:if>><a href="${context}/coverage.html">Code Coverage</a></li>
				</ul>
			</div>
			<div class="badges" style="height: 250px">
				<span>Metawidget integrates with:</span>

				<a href="http://java.sun.com/javaee" target="_blank" style="top: 30px; left: 0px">
					<img src="/media/logo-java.gif" alt="Java" style="border: 0px" />
				</a>
				<a href="http://code.google.com/android" target="_blank" style="top: 40px; left: 70px">
					<img src="/media/logo-android.gif" alt="Android" style="border: 0px" />
				</a>
				<a href="http://code.google.com/webtoolkit" target="_blank" style="top: 70px; left: 45px">
					<img src="/media/logo-gwt.gif" alt="Google Web Toolkit" style="border: 0px" />
				</a>
				<a href="http://groovy.codehaus.org" target="_blank" style="top: 70px; left: 100px">
					<img src="/media/logo-groovy.gif" alt="Groovy" style="border: 0px" />
				</a>
				<a href="http://hibernate.org" target="_blank" style="top: 130px; left: 0px">
					<img src="/media/logo-hibernate.gif" alt="Hibernate" style="border: 0px" />
				</a>
				<a href="http://java.sun.com/javaee/javaserverfaces" target="_blank" style="top: 150px; left: 115px">
					<img src="/media/logo-jsf.gif" alt="Java Server Faces" style="border: 0px" />
				</a>
				<a href="http://jboss.org/jbossrichfaces" target="_blank" style="top: 165px; left: 10px">
					<img src="/media/logo-richfaces.gif" alt="RichFaces" style="border: 0px" />
				</a>
				<a href="http://springframework.org/" target="_blank" style="top: 200px; left: 120px">
					<img src="/media/logo-spring.gif" alt="Spring" style="border: 0px" />
				</a>
				<a href="http://struts.apache.org" target="_blank" style="top: 235px; left: 90px">
					<img src="/media/logo-struts.gif" alt="Struts" style="border: 0px" />
				</a>
				<a href="http://java.sun.com/javase/technologies/desktop" target="_blank" style="top: 205px; left: 35px">
					<img src="/media/logo-swing.gif" alt="Swing" style="border: 0px" />
				</a>
			</div>
			<div class="badges" style="height: 100px">
				<span>Metawidget is proudly:</span>

				<a href="http://opensource.org/docs/definition.php" target="_blank" style="top: 30px; left: 50px">
					<img src="/media/logo-opensource.gif" alt="Open Source (OSI)" style="border: 0px" />
				</a>
			</div>
		</div>

	</div>