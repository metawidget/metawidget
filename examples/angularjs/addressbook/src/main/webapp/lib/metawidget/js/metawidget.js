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
 * Metawidget for pure JavaScript environments.
 */

function Metawidget() {

	this.toInspect = {};
	this.readOnly = false;

	// Defaults
	
	this.inspector = PropertyInspector;
	this.inspectionResultProcessors = [ HideIdInspectionResultProcessor ];
	this.widgetBuilder = HtmlWidgetBuilder;
	this.widgetProcessors = [ IdWidgetProcessor ];
	this.layout = DivLayout; 

	this.buildWidgets = function() {

		// Inspector

		var inspectionResult = this.inspector();

		// InspectionResultProcessors

		for ( var irp = 0, irpLength = this.inspectionResultProcessors.length; irp < irpLength; irp++ ) {
			this.inspectionResultProcessors[irp]( inspectionResult );
		}

		console.log( inspectionResult );
		var element = document.createElement( 'metawidget' );

		for ( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {

			var attributes = inspectionResult[loop];

			// WidgetBuilder

			var widget = this.widgetBuilder( attributes, this );

			// WidgetProcessors

			for ( var wp = 0, wpLength = this.widgetProcessors.length; wp < wpLength; wp++ ) {
				this.widgetProcessors[wp]( widget, attributes );
			}

			// Layout

			this.layout( widget, attributes, element );
		}

		return element;
	}
};

/**
 * Inspectors.
 */

var PropertyInspector = function() {
	
	var inspectionResult = [];
	
	for ( var property in this.toInspect ) {
	
		var inspectedProperty = {};
		inspectedProperty.name = property;
		inspectedProperty.label = property.toUpperCase();
		inspectedProperty.type = ( typeof property );
	
		inspectionResult.push( inspectedProperty );
	}
	
	return inspectionResult;
}

/**
 * InspectionResultProcessors.
 */

var HideIdInspectionResultProcessor = function( inspectionResult ) {

	for ( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {

		var attributes = inspectionResult[loop];

		if ( attributes.name == 'id' ) {
			attributes.readOnly = true;
		}
	}
}

/**
 * WidgetBuilders.
 */

var HtmlWidgetBuilder = function( attributes, metawidget ) {

	if ( metawidget.readOnly == 'true' || attributes.readOnly ) {
		return document.createElement( 'span' );
	}

	var widget = document.createElement( 'input' );
	widget.setAttribute( 'type', 'text' );
	return widget;
}

/**
 * WidgetProcessors.
 */

var IdWidgetProcessor = function( widget, attributes ) {

	widget.setAttribute( 'id', attributes.name );
}

/**
 * Layouts.
 */

var DivLayout = function( widget, attributes, container ) {

	var label = document.createElement( 'label' );
	label.setAttribute( 'for', attributes.name );
	label.innerHTML = attributes.label + ':';

	var div = document.createElement( 'div' );
	div.appendChild( label );
	div.appendChild( widget );

	container.appendChild( div );
}