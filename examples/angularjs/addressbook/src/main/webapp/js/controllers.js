'use strict';

/* Controllers */

function ContactsController( $scope, $location, contacts, metawidgetConfig ) {

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
			if ( $scope.search.firstname ) {
				$scope.filter.firstname = $scope.search.firstname;
			}
			if ( $scope.search.surname ) {
				$scope.filter.surname = $scope.search.surname;
			}
			if ( $scope.search.type ) {
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

	// Constructor

	switch ( $routeParams.contactId ) {
		case 'personal':
		case 'business':
			$scope.readOnly = false;
			$scope.current = {
				"title": "",
				"firstname": "",
				"surname": "",
				"communications": "",
			};
			$scope.current.type = $routeParams.contactId;
			if ( $scope.current.type == 'personal' ) {
				$scope.dialogTitle = 'Personal Contact';
			} else {
				$scope.dialogTitle = 'Business Contact';
			}						
			break;

		default:
			$scope.readOnly = true;
			contacts.then( function( result ) {

				for ( var loop = 0, length = result.data.length; loop < length; loop++ ) {
					if ( result.data[loop].id == $routeParams.contactId ) {
						// Return a copy of the entry, in case the user hits
						// cancel
						$scope.current = angular.fromJson( angular.toJson( result.data[loop] ) );
						$scope.dialogTitle = $scope.current.title + ' ' + $scope.current.firstname + ' ' + $scope.current.surname + ' - ';
						
						if ( $scope.current.type == 'personal' ) {
							$scope.dialogTitle += 'Personal Contact';
						} else {
							$scope.dialogTitle += 'Business Contact';
						}						
						break;
					}
				}
			} )
	}

	$scope.metawidgetConfig = metawidgetConfig;

	// CRUD operations

	$scope.crudActions = {

		edit: function() {

			$scope.readOnly = false;
		},

		save: function() {

			contacts.then( function( result ) {

				if ( !$scope.current.id ) {

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
						if ( result.data[loop].id == $scope.current.id ) {
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
					if ( result.data[loop].id == $scope.current.id ) {
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
	}

	// Communications table

	$scope.communication = {
			type: "",
			value: ""
	};
	
	$scope.addCommunication = function() {

		$scope.current.communications = $scope.current.communications || [];
		$scope.current.communications.push( angular.fromJson( angular.toJson( $scope.communication )));
		$scope.communication = {};
	}

	$scope.removeCommunication = function( index ) {
		
		$scope.current.communications.splice( index, 1 );
	}
}
