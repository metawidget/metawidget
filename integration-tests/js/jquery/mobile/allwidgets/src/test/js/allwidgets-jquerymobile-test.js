describe( 'The JQuery Mobile All Widgets Test', function() {

	it( 'tests all widgets', function() {
		
		var _done = false;

		$( document ).one( 'pageshow', '#allwidgets-page', function( event ) {

			_done = true;
		} );
		
		waitsFor( function() {

			return _done;
		});
	} );
} );