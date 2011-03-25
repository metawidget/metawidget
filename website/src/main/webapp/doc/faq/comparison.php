<?php $title = 'Comparison to Other Projects'; require_once '../../include/page-start.php'; ?>

	<?php $useQuickStart = true; require_once '../../include/body-start.php'; ?>

		<h2>Frequently Asked Questions - Comparison</h2>

		<p>
			There are many great projects that address automatic UI generation, and we are honoured to be counted among them.
			At the time of writing, we are not aware of any with the same goals as Metawidget:
		</p>

		<ol>
			<li>to create UI widgets by inspecting existing architectures</li>
			<li>not to try to 'own' the entire UI, but to focus on creating native sub-widgets for slotting into existing UIs</li>
			<li>to perform inspection <em>at runtime</em>, detecting types and subtypes dynamically</li>
		</ol>

		<p>
			Based on these goals, the table below shows how Metawidget compares to other projects. You can see Metawidget's
			most unique feature is its integration with existing architectures, so Metawidget will be of most interest if
			your UI generation requirements are such that:
		</p>
		
		<ul>
			<li>you want to leverage the full ecosystem of frameworks and tools</li>
			<li>you are targeting multiple platforms</li>
			<li>you want to avoid vendor lock-in</li>			
			<li>you have unusual or specialized architectural requirements</li>
		</ul>

		<table id="comparison-table" class="data-table">
			<thead>
				<tr>
					<th>Project</th>
					<td>Inspects existing architectures</td>
					<td>Creates native sub-widgets for slotting into existing UIs</td>
					<td>Performs runtime UI generation, not static code generation</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th><a href="http://www.beanview.com" target="_blank">BeanView</a></th>
					<td>No, uses its own annotations</td>
					<td><img src="${context}/media/tick.gif" alt=""/></td>
					<td><img src="${context}/media/tick.gif" alt=""/></td>
				</tr>
				<tr>
					<th><a href="http://www.clickframes.org" target="_blank">Clickframes</a></th>
					<td>No</td>
					<td>No</td>
					<td>No</td>
				</tr>
				<tr>
					<th><a href="http://www.codesmithtools.com" target="_blank">CodeSmith</a></th>
					<td><img src="${context}/media/tick.gif" alt=""/></td>
					<td>Could do, if used carefully</td>
					<td>No</td>
				</tr>
				<tr>
					<th><a href="http://www.eclipse.org/modeling/emf" target="_blank">Eclipse Modeling Framework</a></th>
					<td>No, needs a model described in XMI</td>
					<td>Could do, if used carefully</td>
					<td>No</td>
				</tr>
				<tr>
					<th><a href="http://www.devexpress.com/Products/NET/Application_Framework" target="_blank">eXpressApp Framework</a></th>
					<td>No, expects a fixed-set of technologies for its Application Model</td>
					<td>No</td>
					<td>No, their Application Model is created statically</td>
				</tr>
				<tr>
					<th><a href="http://www.grails.org/Scaffolding" target="_blank">Grails Scaffolding</a></th>
					<td>No, requires Groovy and a <tt>constraints</tt> construct</td>
					<td>No</td>
					<td>Sort of, but to tweak the output
					you run <tt>grails install-templates</tt> or <tt>grails generate-views</tt> which use static code generation</td>
				</tr>
				<tr>
					<th><a href="http://www.jmatter.org" target="_blank">JMatter</a></th>
					<td>No, requires everything to be defined on domain objects</td>
					<td>No</td>
					<td><img src="${context}/media/tick.gif" alt=""/></td>
				</tr>
				<tr>
					<th><a href="http://3layer.no-ip.info:6666/confluence/display/MERLIN/Home" target="_blank">Merlin</a></th>
					<td>No, expects a fixed set of technologies (eg. they deprecated their <tt>@Alias</tt> annotation in favour of
					<tt>@org.jboss.seam.annotations.Name</tt>)</td>
					<td><img src="${context}/media/tick.gif" alt=""/></td>
					<td><img src="${context}/media/tick.gif" alt=""/></td>
				</tr>
				<tr>
					<th><a href="http://metawidget.org">Metawidget</a></th>
					<td><img src="${context}/media/tick.gif" alt=""/></td>
					<td><img src="${context}/media/tick.gif" alt=""/></td>
					<td><img src="${context}/media/tick.gif" alt=""/></td>
				</tr>
				<tr>
					<th><a href="http://www.nakedobjects.org" target="_blank">Naked Objects</a></th>
					<td>No, requires everything to be defined on domain objects</td>
					<td>No</td>
					<td><img src="${context}/media/tick.gif" alt=""/></td>
				</tr>
				<tr>
					<th><a href="http://www.openxava.org" target="_blank">OpenXava</a></th>
					<td>No, expects a fixed set of technologies (eg. JPA, Hibernate Validator)</td>
					<td>No</td>
					<td><img src="${context}/media/tick.gif" alt=""/></td>
				</tr>
				<tr>
					<th><a href="http://docs.jboss.com/seam/2.0.1.GA/reference/en/html/gettingstarted.html" target="_blank">Seam-Gen</a></th>
					<td>No, expects a fixed set of technologies (eg. Seam, JPA)</td>
					<td>No</td>
					<td>No</td>
				</tr>
				<tr>
					<th><a href="http://www.skywayperspectives.org" target="_blank">Skyway Builder</a></th>
					<td>No, expects a fixed set of technologies (eg. Spring)</td>
					<td>No</td>
					<td>No</td>
				</tr>
				<tr>
					<th><a href="http://tapestry.apache.org/tapestry5/tapestry-core/guide/beaneditform.html" target="_blank">Tapestry BeanEditForm</a></th>
					<td>No, expects a fixed set of technologies (eg. Tapestry, Javassist)</td>
					<td><img src="${context}/media/tick.gif" alt=""/></td>
					<td><img src="${context}/media/tick.gif" alt=""/></td>
				</tr>
				<tr>
					<th><a href="http://www.c1-setcon.de/widgetserver/ugat/introduction.html" target="_blank">UGAT</a></th>
					<td>No, expects a fixed set of technologies (eg. Hibernate)</td>
					<td>No</td>
					<td>No</td>
				</tr>
				<tr>
					<th><a href="http://wicketwebbeans.sourceforge.net" target="_blank">Wicket Web Beans</a></th>
					<td>No, expects a fixed set of technologies (eg. Wicket, JavaBeans)</td>
					<td><img src="${context}/media/tick.gif" alt=""/></td>
					<td><img src="${context}/media/tick.gif" alt=""/></td>
				</tr>
			</tbody>
		</table>

		<p>
			This page is meant as a useful comparison - not to bash other projects!	If you think any of the above is inaccurate,
			or know of other projects, <a href="mailto:support@metawidget.org">please let us know</a>.
		</p>
		
		<p>
			Note Metawidget is not an application framework. It does not compete with, say, GWT, JSF, Spring, Struts, or any number of other excellent
			frameworks. Rather, Metawidget <em>uses</em> those frameworks in its goal to address automatic UI generation.
		</p>

	<?php require_once '../../include/body-end.php'; ?>		

<?php require_once '../../include/page-end.php'; ?>
