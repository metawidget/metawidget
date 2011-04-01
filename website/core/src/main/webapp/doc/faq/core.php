<?php $title = 'Frequently Asked Questions'; require_once '../../include/page-start.php'; ?>

	<?php $useQuickStart = true; require_once '../../include/body-start.php'; ?>

		<h2>Frequently Asked Questions</h2>

		<ul>
			<li><a href="#what">What is Metawidget?</a></li>
			<li><a href="#who">Who makes Metawidget?</a></li>
			<li><a href="#goals">What are the goals of Metawidget?</a></li>
			<li><a href="#requirements">What are Metawidget's system requirements?</a></li>
			<li><a href="#why">Why is it called Metawidget?</a></li>
			<li><a href="#impl">Why is (some feature) of Metawidget implemented the way it is?</a></li>
			<li><a href="#usage">Who uses Metawidget?</a></li>
			<li><a href="#web">Is Metawidget (yet) another Web application framework?</a></li>
			<li><a href="#compare">How does Metawidget compare to Swing/Java Server Faces/Hibernate/(some other framework)?</a></li>
			<li><a href="#compete">Who are Metawidget's competitors?</a></li>
			<li><a href="#oim">Is an OIM like an ORM?</a></li>
			<li><a href="#technologies">Why does Metawidget support so many different technologies?</a></li>
		</ul>

		<h3><a name="what">What is Metawidget?</a></h3>

		<p>
			Metawidget is an object/user interface mapping tool. The term object/user interface mapping (OIM) refers to the
			technique of inspecting objects, at runtime, and creating User Interface (UI) widgets.
		</p>

		<p>
			As much as possible, Metawidget does this without introducing new technologies. Metawidget inspects an application's
			<em>existing</em> back-end architecture and creates widgets native to its <em>existing</em> front-end framework.
		</p>

		<p>
			See also the <a href="${context}/elevator.html">Elevator Pitch Cartoon</a>.
		</p>
		
		<h3><a name="who">Who makes Metawidget?</a></h3>

		<p>
			Metawidget is an Open Source project licensed under the <a href="${context}/doc/faq/licensing.html">LGPL</a>. It is developed by the
			community. It was started by Richard Kennard of <a href="http://www.kennardconsulting.com" target="_blank">Kennard Consulting</a>.
		</p>

		<h3><a name="goals">What are the goals of Metawidget?</a></h3>

		<p>
			Metawidget continues the trend in recent years toward more POJO-centric development. This trend allows
			developers to rely on 'sensible defaults', writing as little 'boilerplate code' as possible. The Goals Of
			Metawidget are:
		</p>

		<ol>
			<li>to create UI widgets by inspecting existing architectures</li>
			<li>not to try to 'own' the entire UI, but to focus on creating native sub-widgets for slotting into existing UIs</li>
			<li>to perform inspection <em>at runtime</em>, detecting types and subtypes dynamically</li>
		</ol>

		<p>
			Note it is <em>not</em> a goal of Metawidget that the widgets look the same on every UI framework: every UI has
			different features, and Metawidget takes advantage of this.
		</p>

		<h3><a name="requirements">What are Metawidget's system requirements?</a></h3>

		<p>
			Metawidget requires at least J2SE 1.4, and varies depending on which optional modules you use.
		</p>

		<p>
			Its annotation support requires at least Java SE 5. Its <tt>javax.swing.GroupLayout</tt> support requires at
			least Java SE 6.
		</p>

		<h3><a name="why">Why is it called Metawidget?</a></h3>

		<p>
			The name has two meanings:
		</p>

		<ul>
			<li>a 'widget' is the technical term for a Graphical User Interface component (such as a text box). A 'meta' widget is a widget composed of other widgets.</li>
			<li>Metawidget is a widget built by metadata gathered from the back-end</li>
		</ul>

		<h3><a name="impl">Why is (some feature) of Metawidget implemented the way it is?</a></h3>

		<p>
			See the <a href="${context}/blogs.html">developer blogs</a> for insights into, and to provide feedback on, Metawidget's development.
		</p>

		<h3><a name="usage">Who uses Metawidget?</a></h3>

		<p>
			GWT Metawidget, Java Server Faces Metawidget, Spring Metawidget, Struts Metawidget and Swing Metawidget are
			all deployed in production systems.
		</p>

		<h3><a name="web">Is Metawidget (yet) another Web application framework?</a></h3>

		<p>
			No. Metawidget <em>uses</em> existing Web application frameworks, it does not compete with them.
		</p>

		<h3><a name="compare">How does Metawidget compare to Swing/Hibernate/(some other framework)?</a></h3>

		<p>
			Metawidget <em>uses</em> other frameworks, it does not compete with them.
		</p>

		<h3><a name="compete">Who are Metawidget's competitors?</a></h3>

		<p>
			Please see the <a href="comparison.html">comparison FAQ</a>.
		</p>
		
		<h3><a name="oim">Is an OIM like an ORM?</a></h3>

		<p>
			Object Relational Mapping (ORM) technologies, such as Hibernate, are concerned with the back-end: mapping an Object (the <em>O</em> in
			ORM) to a Relational Database (the <em>R</em> in ORM). Object Interface Mapping (OIM) technologies, such as Metawidget, are concerned
			with the front-end: mapping an Object (the <em>O</em> in OIM) to a User Interface framework (the <em>I</em> in OIM).
		</p>

		<p>
			OIMs and ORMs are complementary and meet in the middle: they share the same <em>O</em>. By using an OIM in conjunction with
			an ORM, developers can significantly reduce the amount of code required for an application.
		</p>

		<h3><a name="technologies">Why does Metawidget support so many different technologies?</a></h3>
		
		<p>
			Supporting so many different front-end and back-end technologies carries with it the risk of spreading Metawidget too thin. However,
			it's important because:
		</p>
		
		<ul>
			<li>
				The more technologies Metawidget supports, the more useful it is to more developers - increasing its potential user base.
			</li>
			<li>
				Metawidget is designed to be extensible, and the best way to validate extensibility is to extend it! Integrating lots of
				different technologies helps mature the architecture and make it as flexible as possible.
			</li>
			<li>
				Metawidget is pluggable in many different dimensions, allowing a large amount of shared code between the supported
				technologies. Often, adding a new technology is less code than it may seem.
			</li>
		</ul>

	<?php require_once '../../include/body-end.php'; ?>		

<?php require_once '../../include/page-end.php'; ?>
