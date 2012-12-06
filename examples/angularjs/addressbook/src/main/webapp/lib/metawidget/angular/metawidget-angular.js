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

/**
 * Metawidget module.
 */

angular.module( 'metawidget.directives', [] )
  
  .directive( 'metawidget', function( $compile ) {
	  
	  // Returns the Metawidget
	  
	  return {
		  
		  /**
			 * Metawidget is (E)lement level.
			 */
		  
		  restrict: 'E',
		  transclude: true,
		  scope: {
			  toInspect: '=',
			  readOnly: '@',
			  config: '='			
		  },
		  
		  /**
			 * Delegate to Metawidget.
			 */
		  
		  link: function( scope, element, attrs ) {

			  // Set up an Angular-specific Metawidget
			  
			  var mw = new metawidget.Metawidget( scope.$eval( 'config' ));
			  
			  // InspectionResultProcessor to evaluate Angular expressions
			  
			  mw.inspectionResultProcessors.push( function( inspectionResult, metawidget ) {
				  
				  for( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {
					  
					  // For each attribute in the inspection result...

					  var attributes = inspectionResult[loop];

					  for( var attribute in attributes ) {
						  
						  // ...that looks like an expression...
						  
						  var expression = attributes[attribute];
						  
						  if ( expression.length < 4 || expression.slice( 0, 2 ) != '{{' || expression.slice( expression.length - 2, expression.length ) != '}}' ) {
							  continue;
						  }
						  
						  // ...evaluate it...
						  
						  expression = expression.slice( 2, expression.length - 2 );
						  attributes[attribute] = scope.$parent.$eval( expression ) + '';
						  
						  // ...and watch it for future changes
						  
						  scope.$parent.$watch( expression, function( newValue, oldValue ) {
							  if ( newValue != oldValue ) {
								  _buildWidgets();
							  }
						  });
					  }
				  }
			  } );

			  // WidgetProcessor to add Angular bindings

			  mw.widgetProcessors.push( function( widget, attributes ) {
				  
				  if ( widget.tagName == 'OUTPUT' ) {
					  widget.innerHTML = '{{toInspect.' + attributes.name + '}}';
				  } else if ( widget.tagName == 'BUTTON' ){
					  widget.setAttribute( 'ng-click', 'toInspect.' + attributes.name + '()' );
				  } else {
					  widget.setAttribute( 'ng-model', 'toInspect.' + attributes.name );
				  }
			  } );
			  
			  // Observe
			  
			  scope.$watch( 'toInspect', function( newValue, oldValue ) {
				  _buildWidgets();
			  });
			  scope.$watch( 'readOnly', function( newValue, oldValue ) {
				  _buildWidgets();
			  });
			  
			  // Build
			  
			  function _buildWidgets() {
				  				  
				  mw.toInspect = scope.$eval( 'toInspect' );
				  mw.path = attrs.toInspect;
				  mw.readOnly = attrs.readOnly;
				  element.html( mw.buildWidgets().innerHTML );
				  $compile( element.contents() )( scope );
			  }
		  }
	  };
  });

