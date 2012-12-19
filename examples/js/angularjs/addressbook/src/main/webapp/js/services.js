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
			inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), function( toInspect, type ) {

				switch ( type ) {
					case 'search':
						return [ {
							"name": "type",
							"lookup": "personal,business",
							"lookupLabels": "Personal,Business"
						} ];

					case 'current':
						return [ {
							"name": "id",
							"hidden": "true"
						}, {
							"name": "title",
							"lookup": "Mr,Mrs,Miss,Dr,Cpt",
							"required": "true"
						}, {
							"name": "firstname",
							"required": "true"
						}, {
							"name": "surname",
							"required": "true"
						}, {
							"name": "type",
							"hidden": "true"
						} ];
				}
			} ] ),
			layout: new metawidget.layout.TableLayout( {
				"tableStyleClass": "table-form",
				"columnStyleClasses": "table-label-column,table-component-column,table-required-column"
			} )
		},

		// For the button bar

		buttons: {
			inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), function( toInspect, type ) {

				if ( type == 'crudActions' ) {
					return [ {
						"name": "edit",
						"hidden": "{{!readOnly}}"
					}, {
						"name": "save",
						"hidden": "{{readOnly}}"
					}, {
						"name": "delete",
						"hidden": "{{readOnly || !current.id }}"
					} ];
				}
			} ] ),
			layout: new metawidget.layout.SimpleLayout()
		},
		
		simple: {
			inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), function( toInspect, type ) {

				if ( type == 'communication.type' ) {
					return [ {
						"name": "$root",
						"lookup": "Telephone,Mobile,Fax,E-mail"
					} ];
				}
			} ] ),
			layout: new metawidget.layout.SimpleLayout()
		}		
	};
} );