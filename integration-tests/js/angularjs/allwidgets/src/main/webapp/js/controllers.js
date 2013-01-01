// Metawidget
//
// This library is free software; you can redistribute it and/or
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

'use strict';

/* Controllers */

function AllWidgetsController( $scope ) {

	$scope.allWidgets = allWidgets;

	$scope.actions = {
		"save": function() {

			$scope.readOnly = true;
			$scope.metawidgetConfig = {

				layout: new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.DivLayout() )
			};
		}
	}

	$scope.metawidgetConfig = {

		inspector: new metawidget.inspector.CompositeInspector( [ function( toInspect, type ) {

			switch ( type ) {
				case 'allWidgets':
					return metawidget.test.allWidgetsMetadata;
				case 'allWidgets.nestedWidgets':
					return metawidget.test.nestedWidgetsMetadata;
				case 'allWidgets.readOnlyNestedWidgets':
					return metawidget.test.nestedWidgetsMetadata;
				case 'allWidgets.nestedWidgetsDontExpand':
					return metawidget.test.nestedWidgetsMetadata;
			}
		}, new metawidget.inspector.PropertyTypeInspector() ] )
	};

	$scope.metawidgetActionsConfig = {

		layout: new metawidget.layout.SimpleLayout()
	};
}
