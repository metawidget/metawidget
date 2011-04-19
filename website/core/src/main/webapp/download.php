<?php $title = 'Download'; require_once 'include/page-start.php'; ?>

	<?php require_once 'include/body-start.php'; ?>

		<h2>Download</h2>
		
		<p>
			Metawidget downloads are hosted on SourceForge.
		</p>		
		
		<p>
			There are three downloads: one containing binaries and documentation, one containing examples, and the third containing
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
					<td>Binaries and Documentation</td>
					<td style="text-align: center">${project.version}</td>
					<td style="text-align: center">May 2011</td>
					<td class="link"><a href="https://sourceforge.net/project/downloading.php?group_id=208482&amp;filename=metawidget-${project.version}-bin.zip">Download</a></td>
				</tr>
				<tr>
					<th></th>
					<td>Examples</td>
					<td style="text-align: center">${project.version}</td>
					<td style="text-align: center">May 2011</td>
					<td class="link"><a href="https://sourceforge.net/project/downloading.php?group_id=208482&amp;filename=metawidget-${project.version}-examples.zip">Download</a></td>
				</tr>
				<tr>
					<th></th>
					<td>Source Code and Tests</td>
					<td style="text-align: center">${project.version}</td>
					<td style="text-align: center">May 2011</td>
					<td class="link"><a href="https://sourceforge.net/project/downloading.php?group_id=208482&amp;filename=metawidget-${project.version}-src.zip">Download</a></td>
				</tr>
			</tbody>
		</table>

		<h2 class="h2-underneath">Maven</h2>

		<p>
			Alternatively, Metawidget binaries are deployed at <a href="http://repository.jboss.org/nexus/content/groups/public-jboss" target="_blank">http://repository.jboss.org/nexus/content/groups/public-jboss</a>.
			Add the following repository definition to your <tt>pom.xml</tt>...
		</p>
		
		<div class="code"><tt>&lt;repository&gt;<br/>
		&nbsp;&nbsp;&nbsp;&lt;id&gt;<strong>jboss-public-repository-group</strong>&lt;/id&gt;<br/>
		&nbsp;&nbsp;&nbsp;&lt;url&gt;<strong>http://repository.jboss.org/nexus/content/groups/public-jboss</strong>&lt;/url&gt;<br/>
		&lt;/repository&gt;</tt></div>

		<p>
			...and add the dependency configuration as:
		</p>
		
		<div class="code"><tt>&lt;dependency&gt;<br/>
		&nbsp;&nbsp;&nbsp;&lt;groupId&gt;<strong>org.metawidget.modules</strong>&lt;/groupId&gt;<br/>
		&nbsp;&nbsp;&nbsp;&lt;artifactId&gt;<strong>metawidget-all</strong>&lt;/artifactId&gt;<br/>
		&nbsp;&nbsp;&nbsp;&lt;version&gt;<strong>${project.version}</strong>&lt;/version&gt;<br/>
		&lt;/dependency&gt;</tt></div>
		
		<p> 
			Thanks to <a href="http://jboss.org" target="_blank">JBoss</a> for hosting.
		</p>
			
		<h2 class="h2-underneath">Fine-grained Dependencies</h2>

		<p>			
			If you need more control over how Metawidget is included in your application, fine-grained dependencies are also available. For example you can:
		</p>
		<ul>
			<li>reduce the final size of your application by only including those Metawidget plugins for technologies you actually use; or</li>
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
			To browse all available fine-grained dependencies visit
			<a href="http://repository.jboss.org/nexus/content/groups/public-jboss/org/metawidget/modules">http://repository.jboss.org/nexus/content/groups/public-jboss/org/metawidget/modules</a>. 
		</p>
		
		<h2 class="h2-underneath">Nightly Builds</h2>
		
		<p>
			Snapshot releases are deployed regularly to <a href="http://repository.jboss.org/nexus/content/repositories/snapshots" target="_blank">http://repository.jboss.org/nexus/content/repositories/snapshots</a>.
			These releases are not stable, and should not be used in production environments.
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
