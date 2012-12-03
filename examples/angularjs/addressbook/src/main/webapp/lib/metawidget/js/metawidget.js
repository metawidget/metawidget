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
 * Metawidget for (pure) JavaScript environments.
 */

function Metawidget() {

	this.toInspect = {};	
	this.readOnly = false;
}

/**
 * Inspect the given 'toInspect' and build the UI.
 */

Metawidget.prototype.buildWidgets = function() {

	  // Inspector
	  
	  var inspectionResult = [];

	  for( var property in this.toInspect ) {
		  
		  var inspectedProperty = {};
		  inspectedProperty.name = property;
		  inspectedProperty.label = property.toUpperCase();
		  inspectedProperty.type = (typeof property);
		  
		  inspectionResult.push( inspectedProperty );
	  }
	  
	  // InspectionResultProcessor
	  
	  for( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {
		  
		  var property = inspectionResult[loop];
		  
		  if ( property.name == 'id' ) {
			  property.readOnly = true;
		  }
	  }
	  
	  console.log( inspectionResult );
	  
	  // Build/process/layout

	  var element = document.createElement( 'div' );

	  for( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {
	  
		  var property = inspectionResult[loop];
		  
		  // WidgetBuilder
		  
		  var widget;
		  
		  if ( this.readOnly == 'true' || property.readOnly ) {
			  widget = document.createElement( 'span' );
			  widget.innerHTML = '{{toInspect.' + property.name + '}}';
		  } else {
			  widget = document.createElement( 'input' );
			  widget.setAttribute( 'type', 'text' );
			  widget.setAttribute( 'ng-model', 'toInspect.' + property.name );
		  }

		  // WidgetProcessor

		  widget.setAttribute( 'id', property.name );
		  
		  // Layout
		  
		  var label = document.createElement( 'label' );
		  label.setAttribute( 'for', property.name );
		  label.innerHTML = property.label + ':';
		  
		  var div = document.createElement( 'div' );
		  div.appendChild( label );
		  div.appendChild( widget );
		  
		  element.appendChild( div );
	  }
	  
	  return element;
};
