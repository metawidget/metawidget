var allwidgets = allwidgets || {};

( function() {

	'use strict';

	$( document ).on( 'pageinit', '#allwidgets-page', function( event ) {

		var page = $( event.target );

		var mw = page.find( "#metawidget" );
		mw.metawidget( {

			inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), function( toInspect, type, names ) {

				// Test 'rolling our own' names traversal (not using
				// JsonSchemaInspector)

				if ( type === 'allWidgets' ) {
					if ( names === undefined ) {
						return metawidget.test.allWidgetsMetadata;
					} else if ( names.length === 1 ) {
						if ( names[0] === 'nestedWidgets' || names[0] === 'readOnlyNestedWidgets' || names[0] === 'nestedWidgetsDontExpand' ) {
							return metawidget.test.nestedWidgetsMetadata;
						}
					}
				}
			} ] )
		} );

		mw.metawidget( "buildWidgets", metawidget.test.allWidgets, "allWidgets" );
	} );

	allwidgets.saveAllWidgets = function( event ) {

		var page = $( event ).parents( 'article' );
		var mw = page.find( '#metawidget' );

		var processor = mw.metawidget( "getWidgetProcessor", function( widgetProcessor ) {
			return widgetProcessor instanceof metawidget.widgetprocessor.SimpleBindingProcessor;
		} );
		processor.save( mw.data( 'metawidget' ) );
		mw.metawidget( "option", "layout", new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.DivLayout() ) );
		mw.metawidget( "setReadOnly", true );
		mw.metawidget( "buildWidgets" );
	}
} )();