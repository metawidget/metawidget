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
		search: {
			inspector: new metawidget.CompositeInspector( [
				    metawidget.PropertyInspector,
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

		buttons: {
			inspector: new metawidget.CompositeInspector( [
   				    metawidget.PropertyInspector,
   				    function( toInspect, type ) {
   				    	if ( type == 'crudActions' ) {
   				    		return [ { "name": "edit", "hidden": "{{!readOnly}}" }, { "name": "save", "hidden": "{{readOnly}}" }, { "name": "delete", "hidden": "{{readOnly || current.id == null}}" } ];
   				    	}
   				    }
   			] ),
			layout: metawidget.SimpleLayout
		}
	}
} );