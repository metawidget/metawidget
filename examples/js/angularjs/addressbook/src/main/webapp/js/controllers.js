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

/* Controllers */

function ContactsController( $scope, $location, contacts, metawidgetConfig ) {

	'use strict';

	// Load all contacts

	contacts.then( function( result ) {

		$scope.contacts = result.data;
	} );

	// Prepare search boxes

	$scope.metawidgetConfig = metawidgetConfig;
	$scope.search = {
		firstname: '',
		surname: '',
		type: ''
	};

	$scope.searchActions = {

		// Copy search criteria into Angular filter

		search: function() {

			$scope.filter = {};
			if ( $scope.search.firstname !== '' ) {
				$scope.filter.firstname = $scope.search.firstname;
			}
			if ( $scope.search.surname !== '' ) {
				$scope.filter.surname = $scope.search.surname;
			}
			if ( $scope.search.type !== '' ) {
				$scope.filter.type = $scope.search.type;
			}
		},

		createPersonal: function() {

			$location.path( '/contact/personal' );
		},

		createBusiness: function() {

			$location.path( '/contact/business' );
		}
	};
}

function ContactController( $scope, $routeParams, $location, contacts, metawidgetConfig ) {

	'use strict';

	// Constructor

	switch ( $routeParams.contactId ) {
		case 'personal':
		case 'business':
			$scope.readOnly = false;
			$scope.current = {};
			$scope.current.title = "Mr";
			$scope.current.type = $routeParams.contactId;
			if ( $scope.current.type === 'personal' ) {
				$scope.dialogTitle = 'Personal Contact';
			} else {
				$scope.dialogTitle = 'Business Contact';
			}
			break;

		default:
			$scope.readOnly = true;
			contacts.then( function( result ) {

				var contactId = parseInt( $routeParams.contactId );

				for ( var loop = 0, length = result.data.length; loop < length; loop++ ) {
					if ( result.data[loop].id === contactId ) {
						// Return a copy of the entry, in case the user hits
						// cancel
						$scope.current = angular.fromJson( angular.toJson( result.data[loop] ) );
						$scope.dialogTitle = $scope.current.title + ' ' + $scope.current.firstname + ' ' + $scope.current.surname + ' - ';

						if ( $scope.current.type === 'personal' ) {
							$scope.dialogTitle += 'Personal Contact';
						} else {
							$scope.dialogTitle += 'Business Contact';
						}
						break;
					}
				}
			} );
	}

	$scope.metawidgetConfig = metawidgetConfig;

	// CRUD operations

	$scope.crudActions = {

		edit: function() {

			$scope.readOnly = false;
		},

		save: function() {

			if ( $scope.current.firstname === undefined ) {
				alert( 'Firstname is required' );
				return;
			}

			if ( $scope.current.surname === undefined ) {
				alert( 'Surname is required' );
				return;
			}

			contacts.then( function( result ) {

				if ( $scope.current.id === undefined ) {

					// Save new

					var nextId = 0;
					for ( var loop = 0, length = result.data.length; loop < length; loop++ ) {
						if ( result.data[loop].id > nextId ) {
							nextId = result.data[loop].id;
						}
					}
					$scope.current.id = nextId + 1;
					result.data.push( $scope.current );
				} else {

					// Update existing

					for ( var loop = 0, length = result.data.length; loop < length; loop++ ) {
						if ( result.data[loop].id === $scope.current.id ) {
							result.data.splice( loop, 1, $scope.current );
							break;
						}
					}
				}
				$location.path( '' );
			} );
		},

		"delete": function() {

			contacts.then( function( result ) {

				for ( var loop = 0, length = result.data.length; loop < length; loop++ ) {
					if ( result.data[loop].id === $scope.current.id ) {
						result.data.splice( loop, 1 );
						break;
					}
				}
				$location.path( '' );
			} );
		},

		cancel: function() {

			$location.path( '' );
		}
	};

	// Communications table

	$scope.communication = {
		type: "",
		value: ""
	};

	$scope.addCommunication = function() {

		if ( $scope.communication.type === '' ) {
			alert( 'Communication type is required' );
			return;
		}

		if ( $scope.communication.value === '' ) {
			alert( 'Communication value is required' );
			return;
		}

		$scope.current.communications = $scope.current.communications || [];
		$scope.current.communications.push( angular.fromJson( angular.toJson( $scope.communication ) ) );
		$scope.communication.type = '';
		$scope.communication.value = '';
	};

	$scope.removeCommunication = function( index ) {

		$scope.current.communications.splice( index, 1 );
	};
}
