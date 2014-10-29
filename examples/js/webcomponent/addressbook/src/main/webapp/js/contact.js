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

			if ( names !== undefined && names[names.length - 1] === 'address' ) {

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

			if ( names !== undefined && names[names.length - 1] === 'current' ) {
				addressbook.fetchJSON( 'js/' + addressbook.current.type + '-contact-schema.json', function( data ) {

					metawidget.util.combineInspectionResults( inspectionResult, data );
					mw.buildWidgets( inspectionResult );

					// Example of manually updating a nested template

					var detailTableRows = document.getElementById( 'detail-table-rows' );
					var communications = [];

					for ( var loop = 0; loop < addressbook.current.communications.length; loop++ ) {
						communications.push( {
							index: loop,
							type: addressbook.current.communications[loop].type,
							value: addressbook.current.communications[loop].value
						} );
					}

					detailTableRows.model = {
						communications: communications
					};
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

			if ( addressbook.current.id === undefined ) {
				addressbook.current.id = addressbook.nextId++;
				addressbook.contacts.push( addressbook.current );
			}

			addressbook.crudActions.cancel();
		},

		"delete": function() {

			var contactId = addressbook.current.id;

			for ( var loop = 0, length = addressbook.contacts.length; loop < length; loop++ ) {
				if ( addressbook.contacts[loop].id === contactId ) {
					addressbook.contacts.splice( loop, 1 );
					break;
				}
			}
			addressbook.crudActions.cancel();
		},

		cancel: function() {

			document.getElementById( 'dialog-contact' ).style.display = 'none';
		}
	};

	addressbook.crudActionsConfig = {
		inspectionResultProcessors: [ function( inspectionResult, mw, toInspect, type, names ) {

			if ( document.getElementById( 'detail' ).readOnly === true ) {
				return {
					properties: {
						edit: {
							type: 'function'
						},
						cancel: {
							type: 'function'
						}
					}
				}
			}

			return {
				properties: {
					save: {
						type: 'function'
					},
					'delete': {
						type: 'function'
					},
					cancel: {
						type: 'function'
					}
				}
			}
		} ],
		layout: new metawidget.layout.SimpleLayout()
	};

	/**
	 * Edit Communications
	 */

	function _showCommunication() {
		var mw = document.getElementById( 'communication' );
		mw.setAttribute( 'path', 'addressbook.currentCommunication' );
		mw.buildWidgets();
		document.getElementById( 'dialog-communication' ).style.display = 'block';		
	}
	
	addressbook.addCommunication = function() {
		addressbook.currentCommunicationIndex = undefined;
		addressbook.currentCommunication = {};
		_showCommunication();
	};

	addressbook.editCommunication = function( index ) {
		addressbook.currentCommunicationIndex = index;
		addressbook.currentCommunication = addressbook.current.communications[index];
		_showCommunication();
	};

	addressbook.communicationsConfig = {
		inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), new metawidget.inspector.JsonSchemaInspector( {

			// Example of JSON schema
			
			properties: {
				currentCommunication: {
					properties: {
						type: {
							"enum": [ "Telephone", "Mobile", "Fax", "E-mail" ],
							"required": true
						},
						value: {
							"type": "string",
							"required": true
						}
					}
				}
			}
		} ) ] ),
		layout: addressbook.tableLayout
	};

	addressbook.communicationActions = {

		save: function() {

			document.getElementById( 'communication' ).save();
			var detailTableRows = document.getElementById( 'detail-table-rows' );

			if ( addressbook.currentCommunicationIndex === undefined ) {
				if ( detailTableRows.model === undefined ) {
					detailTableRows.model = {
						communications: []
					}
				}
				detailTableRows.model.communications.push( addressbook.currentCommunication );
			} else {
				detailTableRows.model.communications.splice( addressbook.currentCommunicationIndex, 1, addressbook.currentCommunication );
			}

			document.getElementById( 'dialog-communication' ).style.display = 'none';
		},

		"delete": function() {

			addressbook.current.communications.splice( addressbook.currentCommunicationIndex, 1 );

			var detailTableRows = document.getElementById( 'detail-table-rows' );
			detailTableRows.model.communications.splice( addressbook.currentCommunicationIndex, 1 );

			document.getElementById( 'dialog-communication' ).style.display = 'none';
		}
	};

} )();