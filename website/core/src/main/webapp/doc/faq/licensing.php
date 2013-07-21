<?php $title = 'Frequently Asked Questions - Licensing'; require_once '../../include/page-start.php'; ?>

	<?php $useQuickStart = true; require_once '../../include/body-start.php'; ?>

		<h2>Frequently Asked Questions - Licensing</h2>
		
		<h3>Metawidget (LGPL)</h3>
		
		<p>
			Metawidget is Free Software.
		</p>
	
		<p>
			Metawidget is licensed under the <a href="http://www.gnu.org/licenses/lgpl.html" target="_blank">LGPL</a>. The LGPL is sufficiently flexible
			to allow the use of Metawidget in both open source and commercial projects.
			The LGPL guarantees that Metawidget and any modifications made to Metawidget will stay open source, protecting our and your work. Anyone who
			wanted to release Metawidget under a different license would have to obtain permission from everybody who has contributed code to Metawidget,
			which would be impractical. Metawidget is and will be open source distributed under the LGPL.
		</p>
		
		<p>
			Using Metawidget (by importing Metawidget's public interfaces in your code) and extending Metawidget (by subclassing or implementation of an
			extension interface) is considered by the authors of Metawidget to be dynamic linking. Hence our interpretation of the LGPL is that the use
			of the unmodified Metawidget source does not affect the license of your application code.
		</p>
		
		<p>
			The use of the unmodified Metawidget binary of course never affects the license of your application or distribution. For example, you
			can distribute a commercially licensed, or Apache licensed, application that includes the LGPL Metawidget binary.			
		</p>
		
		<p>
			If you modify Metawidget and redistribute your modifications, the LGPL applies. This cannot be avoided simply by attaching a different
			source file header and/or providing proper attribution (i.e. a statement clarifying the code is modified from the original). Rather, you must pursue
			one of the following options:
		</p>
		
		<ol>
			<li>
				You work to have your modifications integrated back into the original code base
				(please submit them to our <a href="/issues.php">issue tracker</a>)
			</li>
			<li>We work to add sufficient hooks that you can subclass the originals and override methods</li>
		</ol>
		
		<p>
			Option 1 can be problematic for the following reasons:
		</p>
		
		<ul>
			<li>Your modifications may be confidential, so you don't want to open source them</li>
			<li>
				We may feel your modifications are not acceptable to integrate. For example, they may introduce third-party dependencies or
				have insufficient unit tests. If they are not accepted, you <em>cannot</em> continue to use your modifications. It is
				not sufficient just to have submitted them. Rather, you would need to refactor your modifications from the rest of
				your code base, open source them, and host them in a public repository (see the
				<a href="http://weblogs.java.net/blog/kirillcool/archive/2007/03/genuitec_hibern.html">Genuitec/Hibernate incident</a>)
			</li>
		</ul>
		
		<p>
			Option 2 is cleaner in most circumstances, and what we have done for other clients. Please consider taking out
			a <a href="/support.php">small commercial support package</a> and we can make the required changes within that
			time frame.
		</p>

		<h3>Metawidget Examples (BSD)</h3>
		
		<p>
			The Metawidget Examples are Free Software.
		</p>
		
		<p>
			They are licensed under the <a href="http://opensource.org/licenses/BSD-2-Clause" target="_blank">BSD 2-Clause License</a>. This allows
			redistribution and use in source and binary forms, with or without modification. You can freely copy code from the examples, modify it
			and redistribute it. This is <em>not</em> the case for Metawidget itself, which is licensed under LGPL (see above).
		</p>
		
	<?php require_once '../../include/body-end.php'; ?>		

<?php require_once '../../include/page-end.php'; ?>
