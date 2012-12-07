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

var metawidget = metawidget || {};

metawidget.Metawidget = function( config ) {

	this.toInspect = {};
	this.path = '';
	this.readOnly = false;

	// Configure

	if ( config && config.inspector != null ) {
		this.inspector = config.inspector;
	} else {
		this.inspector = metawidget.propertyInspector;
	}
	if ( config && config.inspectionResultProcessors ) {
		this.inspectionResultProcessors = config.inspectionResultProcessors;
	} else {
		this.inspectionResultProcessors = [];
	}
	this.widgetBuilder = new metawidget.CompositeWidgetBuilder( [ metawidget.readOnlyWidgetBuilder, metawidget.htmlWidgetBuilder ] );
	this.widgetProcessors = [ metawidget.idWidgetProcessor ];

	if ( config && config.layout != null ) {
		this.layout = config.layout;
	} else {
		this.layout = metawidget.tableLayout;
	}

	this.buildWidgets = function() {

		// Inspector

		var inspectionResult;

		if ( this.inspector.inspect ) {
			inspectionResult = this.inspector.inspect( this.toInspect, this.path );
		} else {
			inspectionResult = this.inspector( this.toInspect, this.path );
		}

		// InspectionResultProcessors

		for ( var irp = 0, irpLength = this.inspectionResultProcessors.length; irp < irpLength; irp++ ) {

			var inspectionResultProcessor = this.inspectionResultProcessors[irp];

			if ( inspectionResultProcessor.processInspectionResult ) {
				inspectionResultProcessor.processInspectionResult( inspectionResult );
			} else {
				inspectionResultProcessor( inspectionResult );
			}
		}

		console.log( inspectionResult );
		var element = document.createElement( 'metawidget' );

		if ( this.layout.startContainerLayout ) {
			this.layout.startContainerLayout( element );
		}

		for ( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {

			var attributes = inspectionResult[loop];

			// WidgetBuilder

			var widget;

			if ( this.widgetBuilder.buildWidget ) {
				widget = this.widgetBuilder.buildWidget( attributes, this );
			} else {
				widget = this.widgetBuilder( attributes, this );
			}
			
			if ( widget == null ) {
				widget = this.buildNestedMetawidget( attributes );
			}

			// WidgetProcessors

			for ( var wp = 0, wpLength = this.widgetProcessors.length; wp < wpLength; wp++ ) {

				var widgetProcessor = this.widgetProcessors[wp];

				if ( widgetProcessor.processWidget ) {
					widget = widgetProcessor.processWidget( widget, attributes );
				} else {
					widget = widgetProcessor( widget, attributes );
				}
			}
			
			// Layout

			if ( this.layout.layoutWidget ) {
				this.layout.layoutWidget( widget, attributes, element );
			} else {
				this.layout( widget, attributes, element );
			}
		}

		return element;
	};
};
