// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//
// Commercial License: See http://metawidget.org for details

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
