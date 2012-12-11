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

	if ( !( this instanceof metawidget.Metawidget )) {
		throw new Error( "Constructor called as a function" );
	}
	
	this.toInspect = {};
	this.path = '';
	this.readOnly = false;

	// Configure

	if ( config && config.inspector ) {
		this.inspector = config.inspector;
	} else {
		this.inspector = new metawidget.PropertyInspector();
	}
	if ( config && config.inspectionResultProcessors ) {
		this.inspectionResultProcessors = config.inspectionResultProcessors;
	} else {
		this.inspectionResultProcessors = [];
	}
	this.widgetBuilder = new metawidget.CompositeWidgetBuilder( [ new metawidget.ReadOnlyWidgetBuilder(), new metawidget.HtmlWidgetBuilder() ] );
	this.widgetProcessors = [ new metawidget.IdWidgetProcessor() ];

	if ( config && config.layout ) {
		this.layout = config.layout;
	} else {
		this.layout = new metawidget.TableLayout();
	}

	this.buildWidgets = function() {

		// Inspector

		var inspectionResult;

		if ( this.inspector.inspect ) {
			inspectionResult = this.inspector.inspect( this.toInspect, this.path );
		} else {
			inspectionResult = this.inspector( this.toInspect, this.path );
		}

		// Inspector may return null

		if ( !inspectionResult ) {
			return;
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

		var container = document.createElement( 'metawidget' );

		// onStartBuild
		
		if ( this.widgetBuilder.onStartBuild ) {
			this.widgetBuilder.onStartBuild();
		}

		if ( this.layout.startContainerLayout ) {
			this.layout.startContainerLayout( container );
		}

		// Build top-level widget...
		
		for ( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {
			
			var attributes = inspectionResult[loop];
			
			if ( attributes.name != '$root' ) {
				continue;
			}
			
			var widget = buildWidget( attributes, this );
			
			if ( widget ) {
				widget = processWidget( widget, attributes, this );
				
				if ( widget ) {
					layoutWidget( widget, attributes, container, this );
					return container;
				}
			}
				
			break;
		}
				
		// ...or try compound widget

		for ( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {

			var attributes = inspectionResult[loop];

			if ( attributes.name == '$root' ) {
				continue;
			}
			
			var widget = buildWidget( attributes, this );

			if ( !widget ) {
				widget = this.buildNestedMetawidget( attributes );
			}

			widget = processWidget( widget, attributes, this );
			
			if ( widget ) {
				layoutWidget( widget, attributes, container, this );
			}
		}

		return container;
		
		//
		// Private methods
		//
		
		function buildWidget( attributes, mw ) {
			
			if ( mw.widgetBuilder.buildWidget ) {
				return mw.widgetBuilder.buildWidget( attributes, mw );
			}
			
			return mw.widgetBuilder( attributes, mw );
		}

		function processWidget( widget, attributes, mw ) {

			for ( var wp = 0, wpLength = mw.widgetProcessors.length; wp < wpLength; wp++ ) {

				var widgetProcessor = mw.widgetProcessors[wp];
				
				if ( widgetProcessor.processWidget ) {
					widget = widgetProcessor.processWidget( widget, attributes, mw );
				} else {
					widget = widgetProcessor( widget, attributes, mw );
				}
				
				if ( !widget ) {
					return;
				}
			}
			
			return widget;			
		}

		function layoutWidget( widget, attributes, container, mw ) {

			if ( mw.layout.layoutWidget ) {
				mw.layout.layoutWidget( widget, attributes, container, mw );
				return;
			}
			
			mw.layout( widget, attributes, container, mw );
		}
	};
};
