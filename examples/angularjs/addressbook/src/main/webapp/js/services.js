'use strict';

/* Services */

angular.module( 'addressBookServices', [] )

/**
 * Simulate database.
 */

.factory( 'contacts', function( $http ) {

	// Return a promise

	return $http.get( 'js/contacts.json' );
} )

/**
 * App-specific Metawidget configuration.
 */

.factory( 'metawidgetConfig', function() {

	return {
		
		// For the body of the form
		
		form: {
			inspector: new metawidget.CompositeInspector( [
				    new metawidget.PropertyInspector(),
				    function( toInspect, type ) {
				    	
				    	switch( type ) {
				    		case 'search':
				    			return [ { "name": "type", "lookup": "personal,business" } ];
				    			
				    		case 'current':
				    			return [ { "name": "id", "hidden": "true" } ];
				    	}
				    }
			] )
		},

		// For the button bar
		
		buttons: {
			inspector: new metawidget.CompositeInspector( [
   				    new metawidget.PropertyInspector(),
   				    function( toInspect, type ) {
   				    	if ( type == 'crudActions' ) {
   				    		return [ { "name": "edit", "hidden": "{{!readOnly}}" }, { "name": "save", "hidden": "{{readOnly}}" }, { "name": "delete", "hidden": "{{readOnly || current.id == null}}" } ];
   				    	}
   				    }
   			] ),
			layout: new metawidget.SimpleLayout()
		}
	};
} );