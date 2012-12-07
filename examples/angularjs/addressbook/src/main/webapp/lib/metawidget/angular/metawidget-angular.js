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
			  readOnly: '=',
			  config: '='			
		  },
		  
		  compile: function compile( element, attrs, transclude ) {

			  return function( scope, element, attrs ) {

				  // Capture transcluded children for WidgetBuilder later
				  
				  var transcludedChildren;
				  
				  transclude( scope.$parent, function( clone ) {
					transcludedChildren = clone;  
				  } );
				  
				  // Set up an Angular-specific Metawidget
				  
				  var mw = new metawidget.Metawidget( scope.$eval( 'config' ));
				  mw.buildNestedMetawidget = function( attributes ) {
	
					  var nestedMetawidget = document.createElement( 'metawidget' );
					  nestedMetawidget.setAttribute( 'to-inspect', 'toInspect.' + attributes.name );
					  nestedMetawidget.setAttribute( 'read-only', attrs.readOnly );
					  nestedMetawidget.setAttribute( 'config', attrs.config );
					  return nestedMetawidget;
				  };
				  
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
	
				  // WidgetBuilder to override based on the original children in the template
				  // (what Angular calls 'transcluded' children)
				  
				  mw.widgetBuilder = new metawidget.CompositeWidgetBuilder( [
	
				    function( attributes, metawidget ) {

				    	for( var loop = 0, length = transcludedChildren.length; loop < length; loop++ ) {
						
				    		var child = transcludedChildren[loop];
						
				    		if ( child.id == attributes.name ) {
				    			//console.log( child );
				    			return child;
				    		}
				    	}
				    },
					metawidget.readOnlyWidgetBuilder, metawidget.htmlWidgetBuilder ] );
				  
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
					  mw.readOnly = scope.$eval( 'readOnly' );
					  element.html( mw.buildWidgets( element ).innerHTML );
					  $compile( element.contents() )( scope );
				  }
			  };
		  }
	  };
  });

