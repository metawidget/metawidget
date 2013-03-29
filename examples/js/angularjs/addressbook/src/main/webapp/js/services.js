// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

( function() {

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

					var personalContact = contact.slice( 0 );
					personalContact.splice( 5, 0, {
						"name": "dateOfBirth",
						"type": "date"
					} );
					var businessContact = contact.slice( 0 );
					businessContact.splice( 4, 0, {
						"name": "company",
						"type": "string"
					} );
					businessContact.splice( 8, 0, {
						"name": "numberOfStaff",
						"type": "number",
						"minimum": "0",
						"maximum": "100",
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

							if ( names === undefined ) {
								if ( toInspect !== undefined && toInspect.type === 'business' ) {
									return businessContact;
								}
								return personalContact;
							}

							if ( names.length === 1 && names[0] === 'address' ) {
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
						"columnStyleClasses": [ "table-label-column", "table-component-column", "table-required-column" ],
						"footerStyleClass": "buttons"
					} )
				} )
			},

			// For the button bar

			buttons: {
				inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), function( toInspect, type, names ) {

					if ( type === 'crudActions' ) {
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

					if ( type === 'communication' && names.length === 1 && names[0] === 'type' ) {
						return [ {
							"_root": 'true',
							"lookup": "Telephone,Mobile,Fax,E-mail"
						} ];
					}
				} ] ),
				layout: new metawidget.layout.SimpleLayout()
			}
		};
	} );
} )();