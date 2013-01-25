// Metawidget
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

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
			inspector: new metawidget.inspector.CompositeInspector( [ function( toInspect, type, names ) {

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
				businessContact.splice( 4, 0, {
					"name": "company",
					"type": "string"
				} );
				businessContact.splice( 8, 0, {
					"name": "numberOfStaff",
					"type": "number",
					"minimumValue": "0",
					"maximumValue": "100",
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
						
						if ( names.length == 0 ) {
							if ( toInspect && toInspect.type == 'business' ) {
								return businessContact;
							} else {
								return personalContact;
							}
						}
						
						if ( names.length == 1 && names[0] == 'address' ) {
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
				}
			}, new metawidget.inspector.PropertyTypeInspector() ] ),
			layout: new metawidget.layout.HeadingTagLayoutDecorator( {
				delegate: new metawidget.layout.TableLayout( {
					"tableStyleClass": "table-form",
					"columnStyleClasses": "table-label-column,table-component-column,table-required-column",
					"footerStyleClass": "buttons"
				} )
			} )
		},

		// For the button bar

		buttons: {
			inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), function( toInspect, type, names ) {

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
			inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), function( toInspect, type, names ) {

				if ( type == 'communication' && names.length == 1 && names[0] == 'type' ) {
					return [ {
						"name": "__root",
						"lookup": "Telephone,Mobile,Fax,E-mail"
					} ];
				}
			} ] ),
			layout: new metawidget.layout.SimpleLayout()
		}
	};
} );