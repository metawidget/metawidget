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

var addressbook = addressbook || {};

( function() {

	'use strict';

	addressbook.currentConfig = {
		inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), function( toInspect, type, names ) {

			if ( names !== undefined && names.length === 1 && names[0] === 'address' ) {

				// Example of client-side schema

				return {
					properties: {
						street: {
							type: "string",
							propertyOrder: 1
						},
						city: {
							type: "string",
							propertyOrder: 2
						},
						state: {
							"enum": [ "Anytown", "Cyberton", "Lostville", "Whereverton" ],
							propertyOrder: 3
						},
						postcode: {
							type: "string",
							propertyOrder: 4
						}
					}
				};
			}
		} ] ),
		inspectionResultProcessors: [ function( inspectionResult, mw, toInspect, type, names ) {

			// Example of server-side, asynchronous schema

			if ( names === undefined && toInspect !== undefined && toInspect.type !== undefined ) {
				addressbook.fetchJSON( 'js/' + toInspect.type + '-contact-schema.json', function( data ) {

					metawidget.util.combineInspectionResults( inspectionResult, data );
					mw.buildWidgets( inspectionResult );
					
					// Example of manually updating a nested template
					
					var detailTableRows = document.getElementById( 'detail-table-rows' );
					detailTableRows.model = {
						communications: toInspect.communications
					}
				} );
			} else {
				return inspectionResult;
			}
		} ],
		layout: addressbook.tableLayout
	};

	/**
	 * Save and Delete.
	 */
	
	addressbook.crudActions = {

		edit: function() {

			document.getElementById( 'detail' ).setAttribute( 'readonly', false );
		},

		save: function() {

			document.getElementById( 'detail' ).save();
			addressbook.crudActions.cancel();
		},

		"delete": function() {

			var contactId = document.getElementById( 'detail' ).getMetawidget().toInspect.id;
			
			for ( var loop = 0, length = addressbook.model.length; loop < length; loop++ ) {
				if ( addressbook.model[loop].id === contactId ) {
					addressbook.model.splice( loop, 1 );
					break;
				}
			}			
			addressbook.crudActions.cancel();
		},

		cancel: function() {

			document.getElementById( 'dialog-position' ).style.display = 'none';
		}
	};

	addressbook.crudActionsConfig = {
		layout: new metawidget.layout.SimpleLayout()
	};

} )();