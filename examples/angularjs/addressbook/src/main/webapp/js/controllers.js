'use strict';

/* Controllers */

function ContactsController( $scope, $location, contacts ) {

	$scope.contacts = contacts;
	$scope.search = {
		firstname : '',
		surname : '',
		type : ''
	};

	// Copy search criteria into Angular filter

	$scope.runSearch = function() {

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
	};

	$scope.createPersonal = function() {

		$location.path( '/contact/personal' );
	};

	$scope.createBusiness = function() {

		$location.path( '/contact/business' );
	};
}

function ContactController( $scope, $routeParams, $location, contacts ) {

	// Constructor

	switch ( $routeParams.contactId ) {
	case 'personal':
	case 'business':
		$scope.readOnly = false;
		$scope.current = {};
		$scope.current.type = $routeParams.contactId;
		break;

	default:
		$scope.readOnly = true;
		for ( var loop = 0, length = contacts.length; loop < length; loop++ ) {
			if ( contacts[loop].id == $routeParams.contactId ) {
				$scope.current = angular.fromJson( angular.toJson( contacts[loop] ) );
				break;
			}
		}
	}

	// CRUD operations

	$scope.editContact = function() {

		$scope.readOnly = false;
	};

	$scope.saveContact = function() {

		if ( $scope.current.id == null ) {

			// Save new

			var nextId = 0;
			for ( var loop = 0, length = contacts.length; loop < length; loop++ ) {
				if ( contacts[loop].id > nextId ) {
					nextId = contacts[loop].id;
				}
			}
			$scope.current.id = nextId + 1;
			contacts.push( $scope.current );
		} else {

			// Update existing

			for ( var loop = 0, length = contacts.length; loop < length; loop++ ) {
				if ( contacts[loop].id == $scope.current.id ) {
					contacts.splice( loop, 1, $scope.current );
					break;
				}
			}
		}
		$location.path( '' );
	};

	$scope.deleteContact = function() {

		for ( var loop = 0, length = contacts.length; loop < length; loop++ ) {
			if ( contacts[loop].id == $scope.current.id ) {
				contacts.splice( loop, 1 );
				break;
			}
		}
		$location.path( '' );
	};

	$scope.cancelContact = function() {

		$location.path( '' );
	};
}
