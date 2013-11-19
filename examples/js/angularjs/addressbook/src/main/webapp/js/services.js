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

	angular.module( 'services', [] )

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

	.factory( 'metawidgetConfig', function( $http ) {

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
								"enum": [ "personal", "business" ],
								enumTitles: [ "Personal", "Business" ]
							}
						}
					};
				} ] ),
				layout: _tableLayout
			},

			// For the body of the form
			//
			// Note: put PropertyTypeInspector first so that our second
			// Inspector determines the type. This is important for 'range'
			// controls like 'numberOfStaff' which always return 'string'. The
			// alternative is to use Object.defineProperty to create a
			// 'numberOfStaff' setter that converts the value to a 'number'

			form: {
				inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), function( toInspect, type, names ) {

					if ( names !== undefined && names.length === 1 && names[0] === 'address' ) {

						// Example of client-side schema

						return {
							properties: {
								street: {
									type: "string"
								},
								city: {
									type: "string"
								},
								state: {
									"enum": [ "Anytown", "Cyberton", "Lostville", "Whereverton" ]
								},
								postcode: {
									type: "string"
								}
							}
						};
					}
				} ] ),
				inspectionResultProcessors: [ function( inspectionResult, mw, toInspect, type, names ) {

					// Example of server-side, asynchronous schema

					if ( names === undefined && toInspect !== undefined && toInspect.type !== undefined ) {
						$http.get( 'js/' + toInspect.type + '-contact-schema.json' ).then( function( result ) {

							metawidget.util.combineInspectionResults( inspectionResult, result.data );
							mw.buildWidgets( inspectionResult );
						} );
					} else {
						return inspectionResult;
					}
				} ],
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
							"enum": [ "Telephone", "Mobile", "Fax", "E-mail" ]
						};
					}
				} ] ),
				layout: new metawidget.layout.SimpleLayout()
			}
		};
	} );
} )();