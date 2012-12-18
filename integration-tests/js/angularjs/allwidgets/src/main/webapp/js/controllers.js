'use strict';

/* Controllers */

function AllWidgetsController( $scope ) {

	$scope.allWidgets = {
		"nestedWidgets": {
			"nestedTextbox1": "foo"
		}
	};
	$scope.metawidgetConfig = {

		inspector: new metawidget.CompositeInspector( [ function( toInspect, type ) {

			switch ( type ) {
				case 'allWidgets':
					return metawidget.test.allWidgets;
				case 'allWidgets.nestedWidgets':
					return [ {
						"name": "nestedTextbox1"
					}, {
						"name": "nestedTextbox2"
					} ];
			}
		}, new metawidget.PropertyInspector() ] )
	};
}
