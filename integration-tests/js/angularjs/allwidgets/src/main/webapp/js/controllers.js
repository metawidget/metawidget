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

( function() {

	'use strict';

	/* Controllers */

	var module = angular.module( 'controllers', [] )

	.controller( 'allWidgetsController', function( $scope ) {

		$scope.allWidgets = metawidget.test.allWidgets;

		$scope.actions = {
			"save": function() {

				$scope.readOnly = true;
				$scope.metawidgetConfig = {

					layout: new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.DivLayout() )
				};
			}
		}

		$scope.metawidgetConfig = {

			inspector: new metawidget.inspector.CompositeInspector( [ function( toInspect, type, names ) {

				// Test 'rolling our own' names traversal (not using
				// JsonSchemaInspector)

				if ( type === 'allWidgets' ) {
					if ( names === undefined ) {
						return metawidget.test.allWidgetsMetadata;
					} else if ( names.length === 1 ) {
						if ( names[0] === 'nestedWidgets' || names[0] === 'readOnlyNestedWidgets' || names[0] === 'nestedWidgetsDontExpand' ) {
							return metawidget.test.nestedWidgetsMetadata;
						}
					}
				}
			}, new metawidget.inspector.PropertyTypeInspector() ] )
		};

		$scope.metawidgetActionsConfig = {

			layout: new metawidget.layout.SimpleLayout()
		};
	} );
} )();
