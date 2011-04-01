<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><?php
				if ( $title ) {
					echo $title.' - ';
				}
			?>Metawidget</title>
		<meta name="description" content="Metawidget takes your domain objects and automatically creates, at runtime, native User Interface widgets for them - saving you handcoding your UIs" />
		<meta name="keywords" content="<?=$title?>,Domain objects,automatically create User Interface,Swing,Java Server Faces,JSF,Facelets,Spring,Spring Web MVC,Struts,Android,Hibernate" />
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />		
		<link rel="stylesheet" type="text/css" href="/css/metawidget-all.css" media="all" />		
		<link rel="stylesheet" type="text/css" href="/css/metawidget-screen.css" media="screen" />		
		<link rel="stylesheet" type="text/css" href="/css/metawidget-print.css" media="print" />
		<script src="http://www.google-analytics.com/ga.js" type="text/javascript"></script>
		<script src="/js/prototype.js" type="text/javascript"></script>
		<script src="/js/effects.js" type="text/javascript"></script>
		<script src="/js/bounce.js" type="text/javascript"></script>
		<?php
			if ( $useTooltips ) {
				echo '<script src="/js/newsticker.js" type="text/javascript"></script>';
				echo '<script src="/js/tooltip-v0.2.js" type="text/javascript"></script>';
			}			
			if ( $useThumbailViewer ) {
				echo '<link rel="stylesheet" type="text/css" href="/css/lightbox.css" />';
				echo '<script type="text/javascript" src="/js/lightbox.js"></script>';
			}
		?>
	</head>	
		
	<body>
