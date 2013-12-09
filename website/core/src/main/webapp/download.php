<?php $title = 'Download'; require_once 'include/page-start.php'; ?>

	<?php require_once 'include/body-start.php'; ?>

		<h2>Download</h2>
		
		<p>
			New users are recommended to <a href="http://metawidget.org/doc/reference/en/html/pr01.html">follow the tutorial</a>.
		</p>		
		
		<p>
			There are three downloads: one containing Java binaries, JavaScript libraries and documentation; another containing examples; and the third containing
			source code and tests.
		</p>
	
		<table class="data-table">
			<thead>
				<tr>
					<th>Product</th>
					<th>Download</th>
					<th style="text-align: center">Version</th>
					<th style="text-align: center">Release Date</th>
					<th>&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th>Metawidget</th>
					<td>Java Binaries, JavaScript libraries and Documentation</td>
					<td style="text-align: center">${project.version}</td>
					<td style="text-align: center">${timestamp}</td>
					<td class="link"><a href="https://sourceforge.net/project/downloading.php?group_id=208482&amp;filename=metawidget-${project.version}-bin.zip">Download</a></td>
				</tr>
				<tr>
					<th></th>
					<td>Examples</td>
					<td style="text-align: center">${project.version}</td>
					<td style="text-align: center">${timestamp}</td>
					<td class="link"><a href="https://sourceforge.net/project/downloading.php?group_id=208482&amp;filename=metawidget-${project.version}-examples.zip">Download</a></td>
				</tr>
				<tr>
					<th></th>
					<td>Source Code and Tests</td>
					<td style="text-align: center">${project.version}</td>
					<td style="text-align: center">${timestamp}</td>
					<td class="link"><a href="https://sourceforge.net/project/downloading.php?group_id=208482&amp;filename=metawidget-${project.version}-src.zip">Download</a></td>
				</tr>
			</tbody>
		</table>

		<h2 class="h2-underneath">CDN</h2>
		
		<p>
			Alternatively, for the JavaScript-based Metawidgets, you can download from our CDNs at:
		</p>
		
		<ul>
			<li><a href="http://metawidget.org/js/${project.version}/metawidget-core.min.js" target="_blank">http://metawidget.org/js/${project.version}/metawidget-core.min.js</a></li>
			<li><a href="http://metawidget.org/js/${project.version}/metawidget-angular.min.js" target="_blank">http://metawidget.org/js/${project.version}/metawidget-angular.min.js</a></li>
			<li><a href="http://metawidget.org/js/${project.version}/metawidget-bootstrap.min.js" target="_blank">http://metawidget.org/js/${project.version}/metawidget-bootstrap.min.js</a></li>
			<li><a href="http://metawidget.org/js/${project.version}/metawidget-jquerymobile.min.js" target="_blank">http://metawidget.org/js/${project.version}/metawidget-jquerymobile.min.js</a></li>
			<li><a href="http://metawidget.org/js/${project.version}/metawidget-jqueryui.min.js" target="_blank">http://metawidget.org/js/${project.version}/metawidget-jqueryui.min.js</a></li>
		</ul>
		
		<p>
			Note it is recommended you
			<a href="http://metawidget.sourceforge.net/doc/reference/en/html/ch01s02.html">follow the tutorial</a> before downloading from the CDNs.
		</p>

		<h2 class="h2-underneath">Maven</h2>

		<p>
			Metawidget binaries are deployed at <a href="http://search.maven.org/#search|ga|1|metawidget" target="_blank">Maven Central</a>.
			Add the following dependency to your <tt>pom.xml</tt>...
		</p>
		
		<div class="code"><tt>&lt;dependency&gt;<br/>
		&nbsp;&nbsp;&nbsp;&lt;groupId&gt;<strong>org.metawidget.modules</strong>&lt;/groupId&gt;<br/>
		&nbsp;&nbsp;&nbsp;&lt;artifactId&gt;<strong>metawidget-all</strong>&lt;/artifactId&gt;<br/>
		&nbsp;&nbsp;&nbsp;&lt;version&gt;<strong>${project.version}</strong>&lt;/version&gt;<br/>
		&lt;/dependency&gt;</tt></div>
		
		<h2 class="h2-underneath">Maven Fine-grained Dependencies</h2>

		<p>
			If you need more control over how Metawidget is included in your application, fine-grained dependencies are also available. For example you can:
		</p>
		<ul>
			<li>reduce the final size of your application by only including those Metawidget plugins for technologies you use; or</li>
			<li>resolve class loading issues by including different parts of Metawidget for different application tiers</li>
		</ul>
		<p>
			To use fine-grained dependencies, specify dependencies on a per-technology basis (instead of using <tt>metawidget-all</tt>). For example:
		</p>
		<ul>
			<li><tt>org.metawidget.modules.faces:metawidget-richfaces</tt></li>
			<li><tt>org.metawidget.modules.swing:metawidget-beansbinding</tt></li>
			<li><tt>org.metawidet.modules:metawidget-jpa</tt></li>
		</ul>
		<p>
			Maven will automatically drag in related dependencies for you, such as <tt>org.metawidget.modules:metawidget-core</tt>.
		</p>
		<p>
			To browse all available fine-grained dependencies
			<a href="http://search.maven.org/#browse|5459107" target="_blank">search under metawidget-modules</a>. 
		</p>
		
		<h2 class="h2-underneath">Node.js Module</h2>
		
		<p>
			<a href="http://nodejs.org">Node.js</a> users can install Metawidget via <a href="http://npmjs.org/package/metawidget">npm</a>:			
		</p>
		
		<div class="code">
			<tt>npm install metawidget</tt>
		</div>

		<h2 class="h2-underneath">Nightly Builds</h2>
		
		<p>
			Snapshot releases are available at <a href="https://kennardconsulting.ci.cloudbees.com/job/Metawidget">https://kennardconsulting.ci.cloudbees.com/job/Metawidget</a>.
			They are also deployed to <a href="http://repository.jboss.org/nexus/content/repositories/snapshots" target="_blank">http://repository.jboss.org/nexus/content/repositories/snapshots</a>.
			These nightly releases are not stable, and should not be used in production environments.
			Thanks to <a href="http://cloudbees.com">CloudBees</a> and <a href="http://jboss.org">JBoss</a> for hosting.
		</p>

		<h2 class="h2-underneath">Archived Releases</h2>

		<p>
			Archived versions of previous releases <a href="https://sourceforge.net/projects/metawidget/files/Metawidget">are also available</a>.
		</p>
		

		<!-- Google Code for Download Conversion Page -->
		<script type="text/javascript">
		<!--
		var google_conversion_id = 1070855183;
		var google_conversion_language = "en";
		var google_conversion_format = "1";
		var google_conversion_color = "ffffff";
		var google_conversion_label = "wr9aCJnGvAEQj-jP_gM";
		var google_conversion_value = 0;
		//-->
		</script>
		<script type="text/javascript" src="http://www.googleadservices.com/pagead/conversion.js">
		</script>
		<noscript>
		<div style="display:inline;">
		<img height="1" width="1" style="border-style:none;" alt="" src="http://www.googleadservices.com/pagead/conversion/1070855183/?label=wr9aCJnGvAEQj-jP_gM&amp;guid=ON&amp;script=0"/>
		</div>
		</noscript>
				
	<?php require_once 'include/body-end.php'; ?>		

<?php require_once 'include/page-end.php'; ?>
