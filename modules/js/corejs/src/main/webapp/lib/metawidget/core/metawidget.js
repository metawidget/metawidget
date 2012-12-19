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

	if ( ! ( this instanceof metawidget.Metawidget ) ) {
		throw new Error( "Constructor called as a function" );
	}

	this.toInspect = {};
	this.path = '';
	this.readOnly = false;

	var pipeline = new metawidget.Pipeline( config );

	// Configure defaults

	if ( !config || !config.inspector ) {
		pipeline.inspector = new metawidget.inspector.PropertyTypeInspector();
	}
	if ( !config || !config.widgetBuilder ) {
		pipeline.widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [ new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(), new metawidget.widgetbuilder.HtmlWidgetBuilder() ] );
	}
	if ( !config || !config.widgetBuilder ) {
		pipeline.widgetProcessors = [ new metawidget.widgetprocessor.IdWidgetProcessor(), new metawidget.widgetprocessor.SimpleBindingProcessor() ];
	}
	if ( !config || !config.layout ) {
		pipeline.layout = new metawidget.layout.TableLayout();
	}

	this.buildWidgets = function() {

		return pipeline.buildWidgets( this );
	};
	
	this.buildNestedMetawidget = function( attributes ) {

		var nested = new metawidget.Metawidget( this );

		if ( this.toInspect ) {
			nested.toInspect = this.toInspect[attributes.name];
		}

		if ( this.path ) {
			nested.path = this.path + '.' + attributes.name;
		} else {
			// TODO: temporary safeguard against infinite recursion
			nested.path = attributes.name;
		}

		return nested.buildWidgets();
	};	
};

/**
 * Pipeline.
 */

metawidget.Pipeline = function( config ) {

	if ( ! ( this instanceof metawidget.Pipeline ) ) {
		throw new Error( "Constructor called as a function" );
	}

	// Configure

	if ( config && config.inspector ) {
		this.inspector = config.inspector;
	}
	if ( config && config.inspectionResultProcessors ) {
		this.inspectionResultProcessors = config.inspectionResultProcessors.slice();
	} else {
		this.inspectionResultProcessors = [];
	}
	if ( config && config.widgetBuilder ) {
		this.widgetBuilder = config.widgetBuilder;
	}
	if ( config && config.widgetProcessors ) {
		this.widgetProcessors = config.widgetProcessors.slice();
	} else {
		this.widgetProcessors = [];
	}
	if ( config && config.layout ) {
		this.layout = config.layout;
	}
};

/**
 * Standard Metawidget pipeline.
 * 
 * @param mw
 *            Metawidget instance that will be passed down the pipeline
 *            (WidgetBuilders, WidgetProcessors etc). Expected to have
 *            'toInspect', 'path' and 'readOnly'
 */

metawidget.Pipeline.prototype.buildWidgets = function( mw ) {

	// Inspector

	var inspectionResult;

	if ( this.inspector.inspect ) {
		inspectionResult = this.inspector.inspect( mw.toInspect, mw.path );
	} else {
		inspectionResult = this.inspector( mw.toInspect, mw.path );
	}

	// Inspector may return null

	if ( !inspectionResult ) {
		return;
	}

	// InspectionResultProcessors

	for ( var loop = 0, length = this.inspectionResultProcessors.length; loop < length; loop++ ) {

		var inspectionResultProcessor = this.inspectionResultProcessors[loop];

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

		var widget = _buildWidget( this, attributes, mw );

		if ( widget ) {
			widget = _processWidget( this, widget, attributes, mw );

			if ( widget ) {
				_layoutWidget( this, widget, attributes, container, mw );
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

		var widget = _buildWidget( this, attributes, mw );

		if ( !widget ) {
			widget = mw.buildNestedMetawidget( attributes );
		}

		widget = _processWidget( this, widget, attributes, mw );

		if ( widget ) {
			_layoutWidget( this, widget, attributes, container, mw );
		}
	}

	return container;

	//
	// Private methods
	//

	function _buildWidget( pipeline, attributes, mw ) {

		if ( pipeline.widgetBuilder.buildWidget ) {
			return pipeline.widgetBuilder.buildWidget( attributes, mw );
		}

		return pipeline.widgetBuilder( attributes, mw );
	}

	function _processWidget( pipeline, widget, attributes, mw ) {

		for ( var loop = 0, length = pipeline.widgetProcessors.length; loop < length; loop++ ) {

			var widgetProcessor = pipeline.widgetProcessors[loop];

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

	function _layoutWidget( pipeline, widget, attributes, container, mw ) {

		if ( pipeline.layout.layoutWidget ) {
			pipeline.layout.layoutWidget( widget, attributes, container, mw );
			return;
		}

		pipeline.layout( widget, attributes, container, mw );
	}
};
