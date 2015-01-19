var overridden = overridden || {};

( function() {

	'use strict';

	$( document ).on( 'pageinit', '#overridden-page', function( event ) {

		var page = $( event.target );
		
		var model = {
			foo: 'Foo',
			bar: 'Bar',
			baz: 'Baz'
		};

		var mw = page.find( "#metawidget" );
		mw.metawidget();
		mw.metawidget( 'buildWidgets', model, 'overridden' );
	} );

	overridden.save = function( event ) {

		var page = $( event ).parents( 'article' );
		var mw = page.find( '#metawidget' );
		mw.metawidget( 'save' );
		
		mw.metawidget( 'setReadOnly', true );
		mw.metawidget( 'buildWidgets' );
	};
} )();