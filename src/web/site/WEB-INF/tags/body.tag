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
			<div class="badges">
				<span>Metawidget integrates with:</span>
				
				<a href="http://java.sun.com/javaee" target="_blank">
					<img src="${context}/media/logo-java.gif" alt="Java Enterprise Edition" style="border: 0px" />
				</a>
				<a href="http://code.google.com/android" target="_blank">
					<img src="${context}/media/logo-android.gif" alt="Android" style="border: 0px" />
				</a>
				<a href="http://code.google.com/webtoolkit" target="_blank">
					<img src="${context}/media/logo-gwt.gif" alt="Google Web Toolkit" style="border: 0px" />
				</a>
				<a href="http://groovy.codehaus.org" target="_blank">
					<img src="${context}/media/logo-groovy.gif" alt="Groovy" style="border: 0px" />
				</a>
				<a href="http://www.hibernate.org" target="_blank">
					<img src="${context}/media/logo-hibernate.gif" alt="Hibernate" style="border: 0px" />
				</a>
				<a href="http://java.sun.com/javaee/javaserverfaces" target="_blank">
					<img src="${context}/media/logo-jsf.gif" alt="Java Server Faces" style="border: 0px" />
				</a>
				<a href="http://www.springframework.org/" target="_blank">
					<img src="${context}/media/logo-spring.gif" alt="Spring" style="border: 0px" />
				</a>
				<a href="http://struts.apache.org" target="_blank">
					<img src="${context}/media/logo-struts.gif" alt="Struts" style="border: 0px" />
				</a>
				<a href="http://java.sun.com/javase/technologies/desktop" target="_blank">
					<img src="${context}/media/logo-swing.gif" alt="Swing" style="border: 0px" />
				</a>
			</div>
			<div class="badges">
				<span>Metawidget is proudly:</span>
								
				<a href="http://www.opensource.org/docs/definition.php" target="_blank">
					<img src="${context}/media/logo-opensource.gif" alt="Open Source (OSI)" style="border: 0px" />
				</a>
				<a href="http://sourceforge.net/projects/metawidget" target="_blank">
					<img src="${context}/media/logo-sourceforge.gif" alt="Hosted on SourceForge.net" style="border: 0px"/>
				</a>
			</div>
			<div class="badges">
				<span>
					Artwork by <a href="http://www.susiewalkerdesign.com" target="_blank">Susie Walker Design</a>
					<br/>
					Template by <a href="http://www.freecsstemplates.org/" target="_blank">Free CSS Templates</a>
					<br/>
					Additional artwork by <a href="http://www.visualpharm.com" target="_blank">Visual Pharm</a>
				</span>
				
				<a href="http://validator.w3.org/check?uri=referer" target="_blank">
					<img src="${context}/media/logo-xhtml.gif" alt="Valid XHTML 1.0 Transitional" style="border: 0px" />
				</a>					
			</div>
			<div class="badges">
				<span>All trademarks are the property of their respective owners</span>
			</div>
		</div>

	</div>
