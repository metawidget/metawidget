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
			<li class="wide<c:if test="${fn:startsWith(path, '/live-demo')}"> wide-active</c:if>">
				<a href="http://metawidget.sourceforge.net/live-demo/" onclick="pageTracker._link(this.href); return false;"
					<c:if test="${!fn:startsWith(path, '/live-demo')}">
						id="demo"
						onmouseover="bounceInQueue( this, -7 )" onmouseout="bounceInQueue( this, 0 )"
					</c:if>>
					Live Demo!
				</a>
			</li>
			<li class="<c:if test="${path == '/videos.html'}"> active</c:if>">
				<a href="${context}/videos.html"
					<c:if test="${path != '/videos.html'}">
						id="videos"
						onmouseover="bounceInQueue( this, -7 )" onmouseout="bounceInQueue( this, 0 )"
					</c:if>>
					Videos
				</a>
			</li>
			<li class="wide<c:if test="${path == '/screenshots.html'}"> wide-active</c:if>">
				<a href="${context}/screenshots.html"
					<c:if test="${path != '/screenshots.html'}">
						id="screenshots"
						onmouseover="bounceInQueue( this, -7 )" onmouseout="bounceInQueue( this, 0 )"
					</c:if>>
					Screenshots
				</a>
			</li>
			<li class="wide<c:if test="${path == '/download.html'}"> wide-active</c:if>">				
				<a href="${context}/download.html"
					<c:if test="${path != '/download.html'}">
						id="download"
						onmouseover="bounceInQueue( this, -7 )" onmouseout="bounceInQueue( this, 0 )"
					</c:if>>
					Download v1.05
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
						<li <c:if test="${path == '/index.html'}">class="active"</c:if>><a href="${context}/index.html">Overview</a></li>
						<li <c:if test="${path == '/news.html'}">class="active"</c:if>><a href="${context}/news.html">News</a></li>
						<li <c:if test="${path == '/download.html'}">class="active"</c:if>><a href="${context}/download.html">Download</a></li>
						<li <c:if test="${path == '/documentation.html'}">class="active"</c:if>><a href="${context}/documentation.html">Documentation</a></li>
						<li <c:if test="${path == '/support.html'}">class="active"</c:if>><a href="${context}/support.html">Support</a></li>
					</ul>
				</div>
				<div id="left-menu2" class="left-menu">
					<h2>Get Involved</h2>
					<ul>
						<li <c:if test="${path == '/blogs.html'}">class="active"</c:if>><a href="${context}/blogs.html">Blogs and Articles</a></li>
						<li <c:if test="${path == '/survey.html'}">class="active"</c:if>><a href="${context}/survey.html">Complete our Survey</a></li>
						<li <c:if test="${path == '/contributing.html'}">class="active"</c:if>><a href="${context}/contributing.html">Contributing</a></li>
						<li <c:if test="${path == '/issues.html'}">class="active"</c:if>><a href="${context}/issues.html">Issue Tracker</a></li>
						<li <c:if test="${path == '/coverage.html'}">class="active"</c:if>><a href="${context}/coverage.html">Code Coverage</a></li>
					</ul>
				</div>
				<div class="badges" style="height: 395px; padding-top: 5px; border: 1px solid #dddddd; background-color: #eeeeee">
					<span>Metawidget integrates with:</span>

					<a href="http://java.sun.com/javaee" target="_blank" style="top: 30px; left: 2px">
						<img src="/media/logos/logo-java.gif" alt="Java" style="border: 0px" />
					</a>
					<a href="http://code.google.com/android" target="_blank" style="top: 40px; left: 70px">
						<img src="/media/logos/logo-android.gif" alt="Android" style="border: 0px" />
					</a>
					<a href="http://code.google.com/webtoolkit" target="_blank" style="top: 70px; left: 45px">
						<img src="/media/logos/logo-gwt.gif" alt="Google Web Toolkit" style="border: 0px" />
					</a>
					<a href="http://groovy.codehaus.org" target="_blank" style="top: 70px; left: 100px">
						<img src="/media/logos/logo-groovy.gif" alt="Groovy" style="border: 0px" />
					</a>
					<a href="http://hibernate.org" target="_blank" style="top: 130px; left: 2px">
						<img src="/media/logos/logo-hibernate.gif" alt="Hibernate" style="border: 0px" />
					</a>
					<a href="http://icefaces.org" target="_blank" style="top: 175px; left: 2px">
						<img src="/media/logos/logo-icefaces.gif" alt="ICEfaces" style="border: 0px" />
					</a>
					<a href="http://java.sun.com/javaee/javaserverfaces" target="_blank" style="top: 153px; left: 110px">
						<img src="/media/logos/logo-jsf.gif" alt="Java Server Faces" style="border: 0px" />
					</a>
					<a href="http://oval.sourceforge.net" target="_blank" style="top: 215px; left: 5px">
						<img src="/media/logos/logo-oval.gif" alt="OVal" style="border: 0px" />
					</a>
					<a href="http://jboss.org/jbossrichfaces" target="_blank" style="top: 208px; left: 70px">
						<img src="/media/logos/logo-richfaces.gif" alt="RichFaces" style="border: 0px" />
					</a>
					<a href="http://www.scala-lang.org" target="_blank" style="top: 265px; left: 10px">
						<img src="/media/logos/logo-scala.gif" alt="Scala" style="border: 0px" />
					</a>
					<a href="http://springframework.org" target="_blank" style="top: 255px; left: 110px">
						<img src="/media/logos/logo-spring.gif" alt="Spring" style="border: 0px" />
					</a>
					<a href="http://struts.apache.org" target="_blank" style="top: 301px; left: 90px">
						<img src="/media/logos/logo-struts.gif" alt="Struts" style="border: 0px" />
					</a>
					<a href="http://java.sun.com/javase/technologies/desktop" target="_blank" style="top: 300px; left: 20px">
						<img src="/media/logos/logo-swing.gif" alt="Swing" style="border: 0px" />
					</a>
					<a href="http://eclipse.org/swt" target="_blank" style="top: 338px; left: 75px">
						<img src="/media/logos/logo-swt.gif" alt="SWT" style="border: 0px" />
					</a>
					<a href="http://myfaces.apache.org/tomahawk/index.html" target="_blank" style="top: 355px; left: 15px">
						<img src="/media/logos/logo-tomahawk.gif" alt="Tomahawk" style="border: 0px" />
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
				<c:if test="${!empty floater}"><img id="floater" src="${context}/media/${floater}" alt=""/></c:if>
	
				<jsp:doBody />		
			</td>		
		</tr>
	</table>
	
	<div id="footer-links">
		<a href="/gettingstarted.html">Quick Start</a> |
		<a href="/download.html">Download</a> |
		<a href="/documentation.html">Documentation</a> |
		<a href="/support.html">Support</a>
	</div>

	<div id="footer">
		Artwork by <a href="http://susiewalkerdesign.com" target="_blank">Susie Walker Design</a>.
		Template by <a href="http://freecsstemplates.org/" target="_blank">Free CSS Templates</a>.
		Additional artwork by <a href="http://visualpharm.com" target="_blank">Visual Pharm</a><br/>
		and <a href="http://everaldo.com/crystal" target="_blank">Crystal Project</a>
		All trademarks are the property of their respective owners<br/><br/>
		<a href="http://sourceforge.net/projects/metawidget" target="_blank" style="position: relative; top: -10px; left: -5px"><img src="/media/logos/logo-sourceforge.gif" alt="Hosted on SourceForge.net" style="border: 0px"/></a>
		<a href="http://validator.w3.org" target="_blank" ><img src="/media/logos/logo-xhtml.gif" alt="Valid XHTML 1.0 Transitional" style="border: 0px" /></a>
	</div>
