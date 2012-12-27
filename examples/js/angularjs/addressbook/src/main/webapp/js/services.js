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
			inspector: new metawidget.inspector.CompositeInspector( [ function( toInspect, type ) {

				var contact = [ {
					"name": "id",
					"hidden": "true"
				}, {
					"name": "title",
					"lookup": "Mr,Mrs,Miss,Dr,Cpt",
					"required": "true"
				}, {
					"name": "firstname",
					"type": "string",
					"required": "true"
				}, {
					"name": "surname",
					"type": "string",
					"required": "true"
				}, {
					"name": "gender",
					"lookup": "Male,Female"
				}, {
					"name": "address",
					"section": "Contact Details"
				}, {
					"name": "communications"
				}, {
					"name": "notes",
					"type": "string",
					"large": "true",
					"section": "Other"
				}, {
					"name": "type",
					"hidden": "true"
				} ];

				var personalContact = contact.slice();
				personalContact.splice( 5, 0, {
					"name": "dateOfBirth",
					"type": "date"
				} );
				var businessContact = contact.slice();
				businessContact.splice( 7, 0, {
					"name": "numberOfStaff",
					"type": "number",
					"section": "Other"
				} );

				switch ( type ) {
					case 'search':
						return [ {
							"name": "firstname"
						}, {
							"name": "surname"
						}, {
							"name": "type",
							"lookup": "personal,business",
							"lookupLabels": "Personal,Business"
						} ];

					case 'current':
						if ( toInspect.type == 'business' ) {
							return businessContact;
						} else {
							return personalContact;
						}

					case 'current.address':
						return [ {
							"name": "street",
							"type": "string"
						}, {
							"name": "city",
							"type": "string"
						}, {
							"name": "state",
							"lookup": "Anytown,Cyberton,Lostville,Whereverton"
						}, {
							"name": "postcode",
							"type": "string"
						} ];
				}
			}, new metawidget.inspector.PropertyTypeInspector() ] ),
			layout: new metawidget.layout.HeadingLayoutDecorator( {
				delegate: new metawidget.layout.TableLayout( {
					"tableStyleClass": "table-form",
					"columnStyleClasses": "table-label-column,table-component-column,table-required-column"
				} )
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
						"hidden": "{{readOnly || !current.id}}"
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