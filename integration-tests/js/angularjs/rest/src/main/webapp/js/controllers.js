// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

/* Controllers */

function RestTestController( $scope, $http ) {

	'use strict';

	$scope.numberOfRestCalls = 0;

	$scope.restTest = {
		"save": function() {

			$scope.readOnly = true;
		}
	};

	$scope.metawidgetConfig = {

		inspectionResultProcessors: [ function( inspectionResult, mw, toInspect, type, names ) {

			// Shouldn't get called again when we reset 'readOnly'

			$http.get( 'rest/metadata/get' ).then( function( result ) {

				metawidget.util.combineInspectionResults( inspectionResult, result.data );
				$scope.numberOfRestCalls++;
				mw.buildWidgets( inspectionResult );
			} );
		} ]
	};
}
