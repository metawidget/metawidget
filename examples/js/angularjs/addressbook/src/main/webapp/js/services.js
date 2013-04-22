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

		// Construct JSON schemas for metadata

		var _contact = {
			properties: {
				id: {
					hidden: true
				},
				title: {
					enum: [ "Mr", "Mrs", "Miss", "Dr", "Cpt" ],
					required: true,
					propertyOrder: 1
				},
				firstname: {
					type: "string",
					required: true,
					propertyOrder: 2
				},
				surname: {
					type: "string",
					required: true,
					propertyOrder: 3
				},
				gender: {
					enum: [ "Male", "Female" ],
					propertyOrder: 10
				},
				address: {
					section: "Contact Details",
					propertyOrder: 20
				},
				communications: {
					propertyOrder: 21
				},
				notes: {
					type: "string",
					large: true,
					section: "Other",
					propertyOrder: 30
				},
				type: {
					hidden: true
				}
			}
		};

		var _personalContact = {
			properties: {}
		};
		for ( var propertyName in _contact.properties ) {
			_personalContact.properties[propertyName] = _contact.properties[propertyName];
		}
		_personalContact.properties.dateOfBirth = {
			type: "date",
			title: "Date of Birth",
			propertyOrder: 11
		};
		var _businessContact = {
			properties: {}
		};
		for ( var propertyName in _contact.properties ) {
			_businessContact.properties[propertyName] = _contact.properties[propertyName];
		}
		_businessContact.properties.company = {
			type: "string",
			propertyOrder: 4
		};
		_businessContact.properties.numberOfStaff = {
			type: "number",
			minimum: 0,
			maximum: 100,
			section: "Other",
			propertyOrder: 29
		};

		// Custom layout

		var _tableLayout = new metawidget.layout.HeadingTagLayoutDecorator( {
			delegate: new metawidget.layout.TableLayout( {
				tableStyleClass: "table-form",
				columnStyleClasses: [ "table-label-column", "table-component-column", "table-required-column" ],
				footerStyleClass: "buttons"
			} )
		} );

		return {

			// For the search box

			search: {
				inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), function( toInspect, type, names ) {

					return {
						properties: {
							type: {
								enum: [ "personal", "business" ],
								enumTitles: [ "Personal", "Business" ]
							}
						}
					}
				} ] ),
				layout: _tableLayout
			},

			// For the body of the form

			form: {
				inspector: new metawidget.inspector.CompositeInspector( [ function( toInspect, type, names ) {

					if ( names === undefined ) {
						if ( toInspect !== undefined && toInspect.type === 'business' ) {
							return _businessContact;
						}
						return _personalContact;
					}

					if ( names.length === 1 && names[0] === 'address' ) {
						return {
							properties: {
								street: {
									type: "string"
								},
								city: {
									type: "string"
								},
								state: {
									enum: [ "Anytown", "Cyberton", "Lostville", "Whereverton" ]
								},
								postcode: {
									type: "string"
								}
							}
						};
					}
				}, new metawidget.inspector.PropertyTypeInspector() ] ),
				layout: _tableLayout
			},

			// For the button bar

			buttons: {
				inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), function() {

					return {
						properties: {
							edit: {
								hidden: "{{!readOnly}}"
							},
							save: {
								hidden: "{{readOnly}}"
							},
							"delete": {
								hidden: "{{readOnly || !current.id}}"
							}
						}
					};
				} ] ),
				layout: new metawidget.layout.SimpleLayout()
			},

			simple: {
				inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), function( toInspect, type, names ) {

					if ( type === 'communication' && names.length === 1 && names[0] === 'type' ) {
						return {
							enum: [ "Telephone", "Mobile", "Fax", "E-mail" ]
						};
					}
				} ] ),
				layout: new metawidget.layout.SimpleLayout()
			}
		};
	} );
} )();