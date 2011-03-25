<?php $title = 'Contributing'; require_once 'include/page-start.php'; ?>

	<?php $floater='jigsaw-green.png'; require_once 'include/body-start.php'; ?>

		<h2>Contributing</h2>
		
		<p>
			Metawidget is developed by the community. Our mission is:			
		</p>
		
		<h3 style="text-align: center">
			To develop and promote the industry's most practical User Interface generator, by automating
			and integrating with as many existing technologies as possible  
		</h3>
		
		<p>
			Let's unpack that sentence:
		</p>
		
		<ul>
			<li>
				We say <strong>industry</strong> to emphasize our target of diverse, real-world, mainstream applications: not
				just prototypes or niche products (like <a href="http://en.wikipedia.org/wiki/Create,_read,_update_and_delete" target="_blank">CRUD</a> applications)
			</li>
			<li>
				We say <strong>most practical</strong> to mean not
				trying to 'own' the entire UI, but to focus on being a native subcomponent
				for slotting into existing UIs. Also, to mean performing UI generation at runtime not
				through static code generation
			</li>
			<li>
				Finally we say <strong>existing technologies</strong> to mean adapting to as many existing back-end architectures and programming
				languages, and as many existing front-end frameworks as possible
			</li>
		</ul>
		
		<h2 class="h2-underneath">What To Contribute</h2>
		
		<p>
			If you would like to contribute, you can help by:
		</p>
		
		<ul>
			<li>promoting Metawidget to your colleagues (use our <a href="http://metawidget.org/media/presentation/MetawidgetPresentation.pdf">presentation material</a> to help)</li>
			<li><a href="${context}/support.html">providing feedback on the forums</a></li>
			<li><a href="${context}/issues.html">reporting issues</a></li>
			<li><a href="${context}/support.html">adding to the Wiki</a></li>
			<li>contributing enhancements to the code and documentation</li>
		</ul>
		
		<h2 class="h2-underneath">How To Contribute</h2>
		
		<p>
			Submit a patch either via the <a href="${context}/issues.html">issue tracker</a> or by sending it to
			<a href="mailto:support@metawidget.org">support@metawidget.org</a>. Full source code and documentation is included
			in the <a href="${context}/download.html">download</a>. Some starting points in the documentation include: 
		</p>
		
		<ul>
			<li><a href="${context}/doc/reference/en/html/ch02.html" target="_blank">Architecture overview</a></li>
			<li><a href="${context}/doc/reference/en/html/ch02s02.html#section-architecture-inspectors-implementing-your-own" target="_blank">Implementing your own Inspector</a></li>
			<li><a href="${context}/doc/reference/en/html/ch02s04.html#section-architecture-widgetbuilders-implementing-your-own" target="_blank">Implementing your own WidgetBuilder</a></li>
		</ul>
		
		<p>
			You can also <a href="http://metawidget.svn.sourceforge.net/viewvc/metawidget/trunk" target="_blank">browse the source code online</a>. Because Metawidget
			is built using Maven, each module (eg. Swing, JPA, etc.) has its own small, standalone project. This makes it easy to use tools such as
			<a href="http://m2eclipse.sonatype.org">m2eclipse</a> to open the <tt>pom.xml</tt> in your IDE and start contributing!
		</p>
		
		<h2 class="h2-underneath">Suggested Enhancements</h2>
		
		<p>
			Some enhancements suggested by users to date include (in alphabetical order):
		</p>
	
		<table class="data-table">
			<thead>
				<tr>
					<th>Suggestion</th>
					<th>Description</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th>FlexMetawidget</th>
					<td>
						Flex is a popular RIA platform and has interesting architectual differences
						compared to our currently supported Web frameworks. In particular, it is not
						Java-based.
					</td> 
				</tr>
				<tr>
					<th>ZkMetawidget</th>
					<td>
						ZK is a very popular Web framework, and has interesting architectual differences
						compared to our currently supported Web frameworks.
					</td> 
				</tr>
			</tbody>
		</table>
		
		<h2 class="h2-underneath">Contributors</h2>
		
		<p>
			Huge thanks to all those who have contributed their time and effort to improving Metawidget. In alphabetical order:
		</p>
		
		<table class="acknowledgements">
			<tr>
				<td style="width: 33%">Ashlin Eldridge<span>(testing)</span></td>
				<td style="width: 33%">Bernhard Huber<span>(Swing mnemonics)</span></td>
				<td style="width: 33%">G&eacute;rardo Diaz Corujo<span>(testing)</span></td>
			</tr>
			<tr>
				<td>Girolamo Violante<span>(testing)</span></td>
				<td>G&eacute;rard Collin<span>(testing)</span></td>
				<td>Ian Darwin<span>(testing)</span></td>
			</tr>
			<tr>
				<td>Illya Yalovyy<span>(testing)</span></td>
				<td>Ivaylo Kovatchev<span>(UiWide)</span></td>
				<td>Leon E<span>(testing)</span></td>
			</tr>
			<tr>
				<td>Lincoln Baxter III<span>(Maven, testing)</span></td>
				<td>Michael Studman<span>(testing)</span></td>
				<td>Renato Garcia<span>(Maven, OVal)</span></td>
			</tr>
			<tr>
				<td>Rintcius Blok<span>(GWT build)</span></td>
				<td>Ronald van Kuijk<span>(testing)</span></td>
				<td>Ryan Cornia<span>(testing)</span></td>
			</tr>
			<tr>
				<td>Stefan Ackermann<span>(JGoodies, Scala, SWT)</span></td>
				<td>Steffan Luypaert<span>(wallchart)</span></td>
				<td>Tom Bee<span>(testing)</span></td>
			</tr>
		</table>
	
	<?php require_once 'include/body-end.php'; ?>		

<?php require_once 'include/page-end.php'; ?>
