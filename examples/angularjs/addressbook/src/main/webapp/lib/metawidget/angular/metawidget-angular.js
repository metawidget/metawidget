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
			  
			  mw.widgetProcessors.push( function( widget, attributes ) {
				  
				  if ( widget.tagName == 'SPAN' ) {
					  widget.innerHTML = '{{toInspect.' + attributes.name + '}}';
				  } else {
					  widget.setAttribute( 'ng-model', 'toInspect.' + attributes.name );
				  }		  
			  } );				  
			  
			  // Observe
			  
			  var readOnly = false;
			  
			  attrs.$observe( 'readOnly', function( value ) {
				  
				  if ( value == readOnly ) {
					  return;
				  }
				  readOnly = value;
				  _buildWidgets( scope, element, attrs );
			  });
			  attrs.$observe( 'toInspect', function( value ) {
				  _buildWidgets( scope, element, attrs );
			  });
			  
			  // Build
			  
			  function _buildWidgets( scope, element, attrs ) {
				  				  
				  mw.toInspect = scope.$eval( 'toInspect' );
				  mw.path = attrs.toInspect;
				  mw.readOnly = attrs.readOnly;
				  element.html( mw.buildWidgets().innerHTML );
				  $compile( element.contents() )( scope );
			  }
		  }
	  };
  });

