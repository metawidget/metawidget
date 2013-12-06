<?php $title = 'Frequently Asked Questions'; require_once '../../include/page-start.php'; ?>

	<?php $useQuickStart = true; require_once '../../include/body-start.php'; ?>

		<h2>Frequently Asked Questions - Motivation</h2>

		<p>
			If you're asking yourself, or Googling for, the following motivating factors then Metawidget may be for you:
		</p>
		
		<h3 class="h3-underneath">
			"I want a lightweight UI generator I can just throw an object at"
		</h3>
		
		<p style="margin-top: 5px">
			Metawidget works as a lightweight, no fuss solution out-of-the-box. You can
			<a href="http://blog.kennardconsulting.com/2013/03/lightweight-json-to-ui-generator.html">render a JSON object to the UI in just 3 lines</a>.
		</p>

		<h3 class="h3-underneath">
			"I need a UI generator I can customize"
		</h3>
		
		<p style="margin-top: 5px">
			Most UI generation scenarios involve a lot of things that are generally the same, with a few that are specific
			to your app.
		</p>
		
		<p>
			For example, for an intranet app you may be fine with Metawidget's out-of-the-box
			UI rendering, but have exacting requirements around the way metadata gets sourced. Metawidet's pluggable
			<tt>Inspector</tt> architecture allows you to swap out the bits you need to change, whilst keeping the rest of
			the UI generation pipeline the same.
		</p>
		
		<p>
			Alternatively, for a consumer app you may have simple metadata requirements (say, just a POJO) but
			exacting requirements around how the UI gets rendered. Metawidget's pluggable <tt>Layout</tt> architecture allows
			you to reuse the metadata parsing whilst swapping in a custom layout.
		</p>
		
		<p>
			As a final example, if your app uses third-party widget libraries you can swap in support for specific widgets and
			custom datatypes, whilst leaving the rest of the pipeline at its default settings.  
		</p>

		<h3 class="h3-underneath">
			"I want a UI generator that separates inspection of metadata from generation of the UI.
			 So I can use other sources of metadata like REST endpoints and SOAP WSDLs"
		</h3>
		
		<p style="margin-top: 5px">
			Metawidget's pluggable <tt>Inspector</tt> architecture separates inspection from generation, so that
			it can be performed across different tiers of your application - or even remotely
			using technologies such as REST. <tt>Inspector</tt>s are flexible enough to support a wide variety
			of metadata including: Java objects; XML files; SQL schemas; Business Rule Engines and
			REST endpoints.
		</p>
		
		<h3 class="h3-underneath">
			"I want a UI generator that can build a rich meta-database describing entities and their relationships -
			 a context belonging to each screen"
		</h3>
		
		<p>
			Metawidget's pluggable <tt>Inspector</tt>s can extract metadata from a broad range of sources, which
			its <tt>CompositeInspector</tt> can then combine together. Next, its <tt>InspectionResultProcessor</tt>s
			can process this overall metadata in the context of each screen. Finally, its <tt>WidgetBuilder</tt>s,
			<tt>WidgetProcessor</tt>s and <tt>Layout</tt>s iterate over this metadata to generate the UI. 
		</p>
		
		<p>
			This approach ensures metadata inspection is kept orthogonal to metadata processing, which in turn is
			kept orthogonal to widget choosing, which in turn is kept orthogonal to layout style.
		</p>
		
		<p>
			Such separation maximizes reuse. For example, you don't have to redefine how to
			construct a <tt>div</tt>-based layout for both the summary and the detail screens of your app - you only
			have to hook in a different <tt>InspectionResultProcessor</tt> to prepare the metadata differently
			(such as limiting the number of fields for the summary screen).
		</p>

	<?php require_once '../../include/body-end.php'; ?>		

<?php require_once '../../include/page-end.php'; ?>
