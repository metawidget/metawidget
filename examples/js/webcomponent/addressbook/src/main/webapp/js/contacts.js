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

	// Prepare model

	addressbook.fetchJSON( 'js/contacts.json', function( data ) {

		addressbook.contacts = data;
		addressbook.nextId = data.length + 1;

		Object.observe( addressbook.search, _populateSummaryPage );
		Object.observe( addressbook.contacts, _populateSummaryPage );
		_populateSummaryPage();
	} );

	function _populateSummaryPage() {

		var summaryTableRows = document.getElementById( 'summary-table-rows' );
		summaryTableRows.model = {
			contacts: []
		};

		for( var loop = 0, length = addressbook.contacts.length; loop < length; loop++ ) {

			var contact = addressbook.contacts[loop];
			
			// Filter

			if ( addressbook.search.firstname !== undefined && contact.firstname.indexOf( addressbook.search.firstname ) === -1 ) {
				continue;
			}

			if ( addressbook.search.surname !== undefined && contact.surname.indexOf( addressbook.search.surname ) === -1 ) {
				continue;
			}
			
			if ( addressbook.search.type !== undefined && addressbook.search.type !== '' && contact.type !== addressbook.search.type ) {
				continue;
			}

			summaryTableRows.model.contacts.push( contact );
		}
	}

	function _showDialog( current ) {
	
		addressbook.current = current; 
		var mw = document.getElementById( 'detail' );
		mw.setAttribute( 'path', 'addressbook.current' );
		mw.setAttribute( 'readonly', current.id !== undefined );
		mw.buildWidgets();
		document.getElementById( 'dialog-position' ).style.display = 'block';
		
		if ( current.id === undefined ) {
			document.getElementById( 'dialog-heading' ).innerHTML = 'New Contact';
		} else {
			document.getElementById( 'dialog-heading' ).innerHTML = current.firstname + ' ' + current.surname;
		}
		
		document.getElementById( 'dialog-image' ).src = 'media/' + current.type + '.gif';
	}
	
	/**
	 * Search
	 */

	addressbook.search = {
		firstname: '',
		surname: '',
		type: ''
	};

	addressbook.searchConfig = {
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
		layout: addressbook.tableLayout
	};

	addressbook.searchActions = {

		search: function() {
			document.getElementById( 'search' ).save();
		},

		createPersonal: function() {

			_showDialog( {
				type: 'personal'
			} );
		},

		createBusiness: function() {

			_showDialog( {
				type: 'business'
			} );
		}
	};

	addressbook.searchActionsConfig = {
		layout: new metawidget.layout.SimpleLayout()
	};

	/**
	 * Retrieve
	 */

	addressbook.load = function( contactId ) {

		for ( var loop = 0, length = addressbook.contacts.length; loop < length; loop++ ) {
			if ( addressbook.contacts[loop].id === contactId ) {
				_showDialog( addressbook.contacts[loop] );
				break;
			}
		}
	};

} )();