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

var metawidget = {

	/**
	 * Metawidget
	 */

	Metawidget : function() {

		this.toInspect = {};
		this.readOnly = false;

		// Defaults

		this.inspector = metawidget.PropertyInspector;
		this.inspectionResultProcessors = [ metawidget.HideIdInspectionResultProcessor ];
		this.widgetBuilder = metawidget.HtmlWidgetBuilder;
		this.widgetProcessors = [ metawidget.IdWidgetProcessor ];
		this.layout = metawidget.TableLayout;

		this.buildWidgets = function() {

			// Inspector

			var inspectionResult;

			if ( this.inspector.inspect ) {
				inspectionResult = this.inspector.inspect( this.toInspect );
			} else {
				inspectionResult = this.inspector( this.toInspect );
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

				// WidgetProcessors

				for ( var wp = 0, wpLength = this.widgetProcessors.length; wp < wpLength; wp++ ) {

					var widgetProcessor = this.widgetProcessors[wp];

					if ( widgetProcessor.processWidget ) {
						widgetProcessor = widgetProcessor.processWidget( widget, attributes );
					} else {
						widgetProcessor = widgetProcessor( widget, attributes );
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
		}
	}
}

/**
 * Inspectors.
 */

metawidget.PropertyInspector = {

	inspect : function( toInspect ) {

		var inspectionResult = [];

		for ( var property in toInspect ) {

			var inspectedProperty = {};
			inspectedProperty.name = property;
			inspectedProperty.label = property.toUpperCase();
			inspectedProperty.type = ( typeof property );

			inspectionResult.push( inspectedProperty );
		}

		return inspectionResult;
	}
};

/**
 * InspectionResultProcessors.
 */

metawidget.HideIdInspectionResultProcessor = {

	processInspectionResult : function( inspectionResult ) {

		for ( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {

			var attributes = inspectionResult[loop];

			if ( attributes.name == 'id' ) {
				attributes.readOnly = true;
			}
		}
	}
};

/**
 * WidgetBuilders.
 */

metawidget.HtmlWidgetBuilder = {

	buildWidget : function( attributes, metawidget ) {

		if ( metawidget.readOnly == 'true' || attributes.readOnly ) {
			return document.createElement( 'span' );
		}

		var widget = document.createElement( 'input' );
		widget.setAttribute( 'type', 'text' );
		return widget;
	}
};

/**
 * WidgetProcessors.
 */

metawidget.IdWidgetProcessor = {

	processWidget : function( widget, attributes ) {

		widget.setAttribute( 'id', attributes.name );
	}
};

/**
 * Layouts.
 */

metawidget.DivLayout = {

	layoutWidget : function( widget, attributes, container ) {

		var label = document.createElement( 'label' );
		label.setAttribute( 'for', attributes.name );
		label.innerHTML = attributes.label + ':';

		var div = document.createElement( 'div' );
		div.appendChild( label );
		div.appendChild( widget );

		container.appendChild( div );
	}
};

metawidget.TableLayout = {

	startContainerLayout : function( container ) {
		container.appendChild( document.createElement( 'table' ) );
	},

	layoutWidget : function( widget, attributes, container ) {

		var th = document.createElement( 'th' );
		var label = document.createElement( 'label' );
		label.setAttribute( 'for', attributes.name );
		label.innerHTML = attributes.label + ':';
		th.appendChild( label );

		var td = document.createElement( 'td' );
		td.appendChild( widget );

		var tr = document.createElement( 'tr' );
		tr.appendChild( th );
		tr.appendChild( td );

		container.firstChild.appendChild( tr );
	}
};
