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

metawidget.Metawidget = function( element, config ) {

	if ( ! ( this instanceof metawidget.Metawidget ) ) {
		throw new Error( 'Constructor called as a function' );
	}

	var pipeline = new metawidget.Pipeline( element );
	pipeline.buildNestedMetawidget = function( attributes, mw ) {

		var nestedWidget = document.createElement( 'div' );

		// Duck-type our 'pipeline' as the 'config' of the nested Metawidget

		var nestedMetawidget = new metawidget.Metawidget( nestedWidget, pipeline );
		nestedMetawidget.toInspect = mw.toInspect;
		nestedMetawidget.path = metawidget.util.appendPath( attributes, mw );
		nestedMetawidget.readOnly = mw.readOnly || attributes.readOnly === 'true';

		// Attach ourselves as a property of the tag, rather than try to
		// 'extend' the built-in HTML tags

		nestedWidget.metawidget = nestedMetawidget;
		nestedMetawidget.buildWidgets();

		return nestedWidget;
	};

	// Configure defaults

	pipeline.inspector = new metawidget.inspector.PropertyTypeInspector();
	pipeline.widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [ new metawidget.widgetbuilder.OverriddenWidgetBuilder(), new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(),
			new metawidget.widgetbuilder.HtmlWidgetBuilder() ] );
	pipeline.widgetProcessors = [ new metawidget.widgetprocessor.IdProcessor(), new metawidget.widgetprocessor.RequiredAttributeProcessor(), new metawidget.widgetprocessor.SimpleBindingProcessor() ];
	pipeline.layout = new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.TableLayout() );
	pipeline.configure( config );

	// First time in, capture the contents of the Metawidget (if any)

	this._overriddenNodes = [];

	for ( var loop = 0, length = element.childNodes.length; loop < length; loop++ ) {
		if ( element.childNodes[loop].nodeType === 1 ) {
			this._overriddenNodes.push( element.childNodes[loop] );
		}
	}

	//
	// Public methods
	//

	this.getWidgetProcessor = function( testInstanceOf ) {

		return pipeline.getWidgetProcessor( testInstanceOf );
	};

	this.setLayout = function( layout ) {

		pipeline.layout = layout;
	};

	this.buildWidgets = function( inspectionResult ) {

		// Defensive copy

		this.overriddenNodes = [];

		for ( var loop = 0, length = this._overriddenNodes.length; loop < length; loop++ ) {
			this.overriddenNodes.push( this._overriddenNodes[loop].cloneNode( true ) );
		}

		// Inspect (if necessary)

		if ( inspectionResult === undefined ) {
			inspectionResult = pipeline.inspect( this );
		}

		pipeline.buildWidgets( inspectionResult, this );
	};
};

/**
 * Pipeline.
 * <p>
 * Clients should override 'buildNestedMetawidget'.
 */

metawidget.Pipeline = function( element ) {

	if ( ! ( this instanceof metawidget.Pipeline ) ) {
		throw new Error( 'Constructor called as a function' );
	}

	this.inspectionResultProcessors = [];
	this.widgetProcessors = [];
	this.element = element;
	this.maximumInspectionDepth = 10;
};

/**
 * Configures the pipeline using the given config object.
 * <p>
 * This method is separate to the constructor, so that subclasses can set
 * defaults.
 */

metawidget.Pipeline.prototype.configure = function( config ) {

	if ( config === undefined ) {
		return;
	}
	if ( config.inspector !== undefined ) {
		this.inspector = config.inspector;
	}
	if ( config.inspectionResultProcessors !== undefined ) {
		this.inspectionResultProcessors = config.inspectionResultProcessors.slice();
	}
	if ( config.widgetBuilder !== undefined ) {
		this.widgetBuilder = config.widgetBuilder;
	}
	if ( config.widgetProcessors !== undefined ) {
		this.widgetProcessors = config.widgetProcessors.slice();
	}
	if ( config.layout !== undefined ) {
		this.layout = config.layout;
	}

	// Safeguard against infinite recursion

	if ( config.maximumInspectionDepth !== undefined ) {
		this.maximumInspectionDepth = config.maximumInspectionDepth - 1;
	}
};

/**
 * Searches the pipeline's current list of WidgetProcessors and matches each
 * against the given function
 * 
 * @param testInstanceOf
 *            a function that accepts a WidgetProcessor and will perform an
 *            'instanceof' test on it
 */

metawidget.Pipeline.prototype.getWidgetProcessor = function( testInstanceOf ) {

	for ( var loop = 0, length = this.widgetProcessors.length; loop < length; loop++ ) {

		var widgetProcessor = this.widgetProcessors[loop];

		if ( testInstanceOf( widgetProcessor ) ) {
			return widgetProcessor;
		}
	}
};

/**
 * Inspect the given Metawidget's 'toInspect' according to its 'path', and
 * return the result as a JSON String.
 * <p>
 * This method mirrors the <code>Inspector</code> interface. Internally it
 * looks up the Inspector to use. It is a useful hook for subclasses wishing to
 * inspect different Objects using our same <code>Inspector</code>.
 * <p>
 * In addition, this method runs the <code>InspectionResultProcessors</code>.
 * 
 * @param mw
 *            Metawidget instance that will be passed down the pipeline
 *            (WidgetBuilders, WidgetProcessors etc). Expected to have
 *            'toInspect' and 'path'.
 */

metawidget.Pipeline.prototype.inspect = function( mw ) {

	// Inspector

	var splitPath = metawidget.util.splitPath( mw.path );
	var inspectionResult;

	if ( this.inspector.inspect !== undefined ) {
		inspectionResult = this.inspector.inspect( mw.toInspect, splitPath.type, splitPath.names );
	} else {
		inspectionResult = this.inspector( mw.toInspect, splitPath.type, splitPath.names );
	}

	// Inspector may return undefined

	if ( inspectionResult === undefined ) {
		return;
	}

	// InspectionResultProcessors

	for ( var loop = 0, length = this.inspectionResultProcessors.length; loop < length; loop++ ) {

		var inspectionResultProcessor = this.inspectionResultProcessors[loop];

		if ( inspectionResultProcessor.processInspectionResult !== undefined ) {
			inspectionResult = inspectionResultProcessor.processInspectionResult( inspectionResult, mw, mw.toInspect, splitPath.type, splitPath.names );
		} else {
			inspectionResult = inspectionResultProcessor( inspectionResult, mw, mw.toInspect, splitPath.type, splitPath.names );
		}

		// InspectionResultProcessor may return undefined

		if ( inspectionResult === undefined ) {
			return;
		}
	}

	return inspectionResult;
};

/**
 * Build widgets from the given JSON inspection result.
 * <p>
 * Note: the Pipeline expects the JSON to be passed in externally, rather than
 * fetching it itself, because some JSON inspections may be asynchronous.
 * 
 * @param mw
 *            Metawidget instance that will be passed down the pipeline
 *            (WidgetBuilders, WidgetProcessors etc). Expected to have
 *            'toInspect', 'path' and 'readOnly'.
 */

metawidget.Pipeline.prototype.buildWidgets = function( inspectionResult, mw ) {

	// Clear existing contents

	this.element.innerHTML = '';

	_startBuild( this, mw );

	// Build top-level widget...

	if ( inspectionResult !== undefined ) {
		for ( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {

			var attributes = inspectionResult[loop];

			if ( attributes.name !== '__root' ) {
				continue;
			}

			var copiedAttributes = _forceReadOnly( attributes, mw );
			var widget = _buildWidget( this, copiedAttributes, mw );

			if ( widget !== undefined ) {
				widget = _processWidget( this, widget, copiedAttributes, mw );

				if ( widget !== undefined ) {
					_layoutWidget( this, widget, copiedAttributes, this.element, mw );
					return;
				}
			}

			break;
		}

		// ...or try compound widget

		for ( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {

			var attributes = inspectionResult[loop];

			if ( attributes.name === '__root' ) {
				continue;
			}

			var copiedAttributes = _forceReadOnly( attributes, mw );
			var widget = _buildWidget( this, copiedAttributes, mw );

			if ( widget === undefined ) {
				if ( this.maximumInspectionDepth <= 0 ) {
					return;
				}

				widget = this.buildNestedMetawidget( copiedAttributes, mw );

				if ( widget === undefined ) {
					return;
				}
			}

			widget = _processWidget( this, widget, copiedAttributes, mw );

			if ( widget !== undefined ) {
				_layoutWidget( this, widget, copiedAttributes, this.element, mw );
			}
		}
	}

	// Even if no inspectors match, we still call startBuild()/endBuild()
	// because you can use a Metawidget purely for layout, with no
	// inspection

	_endBuild( this, mw );

	return;

	//
	// Private methods
	//

	/**
	 * Defensively copies the attributes (in case something like stripSection
	 * changes them) and adds 'readOnly' if the given Metawidget is readOnly.
	 */

	function _forceReadOnly( attributes, mw ) {

		var copiedAttributes = {};

		for ( var name in attributes ) {
			copiedAttributes[name] = attributes[name];
		}

		// Try to keep the exact nature of the 'readOnly' mechanism (i.e. set on
		// attribute, or set on overall Metawidget) out of the
		// WidgetBuilders/WidgetProcessors/Layouts. This is because not
		// everybody will need/want a Metawidget-level 'setReadOnly'

		if ( mw.readOnly === true ) {
			copiedAttributes.readOnly = 'true';
		}

		return copiedAttributes;
	}

	function _startBuild( pipeline, mw ) {

		if ( pipeline.widgetBuilder.onStartBuild !== undefined ) {
			pipeline.widgetBuilder.onStartBuild( mw );
		}

		for ( var loop = 0, length = pipeline.widgetProcessors.length; loop < length; loop++ ) {

			var widgetProcessor = pipeline.widgetProcessors[loop];

			if ( widgetProcessor.onStartBuild !== undefined ) {
				widgetProcessor.onStartBuild( mw );
			}
		}

		if ( pipeline.layout.onStartBuild !== undefined ) {
			pipeline.layout.onStartBuild( mw );
		}

		if ( pipeline.layout.startContainerLayout !== undefined ) {
			pipeline.layout.startContainerLayout( pipeline.element, mw );
		}
	}

	function _buildWidget( pipeline, attributes, mw ) {

		if ( pipeline.widgetBuilder.buildWidget !== undefined ) {
			return pipeline.widgetBuilder.buildWidget( attributes, mw );
		}

		return pipeline.widgetBuilder( attributes, mw );
	}

	function _processWidget( pipeline, widget, attributes, mw ) {

		for ( var loop = 0, length = pipeline.widgetProcessors.length; loop < length; loop++ ) {

			var widgetProcessor = pipeline.widgetProcessors[loop];

			if ( widgetProcessor.processWidget !== undefined ) {
				widget = widgetProcessor.processWidget( widget, attributes, mw );
			} else {
				widget = widgetProcessor( widget, attributes, mw );
			}

			if ( widget === undefined ) {
				return;
			}
		}

		return widget;
	}

	function _layoutWidget( pipeline, widget, attributes, container, mw ) {

		if ( pipeline.layout.layoutWidget !== undefined ) {
			pipeline.layout.layoutWidget( widget, attributes, container, mw );
			return;
		}

		pipeline.layout( widget, attributes, container, mw );
	}

	function _endBuild( pipeline, mw ) {

		if ( pipeline.layout.endContainerLayout !== undefined ) {
			pipeline.layout.endContainerLayout( pipeline.element, mw );
		}

		if ( pipeline.layout.onEndBuild !== undefined ) {
			pipeline.layout.onEndBuild( mw );
		}

		for ( var loop = 0, length = pipeline.widgetProcessors.length; loop < length; loop++ ) {

			var widgetProcessor = pipeline.widgetProcessors[loop];

			if ( widgetProcessor.onEndBuild !== undefined ) {
				widgetProcessor.onEndBuild( mw );
			}
		}

		if ( pipeline.widgetBuilder.onEndBuild !== undefined ) {
			pipeline.widgetBuilder.onEndBuild( mw );
		}
	}
};

/**
 * Subclasses should override this method to create a nested Metawidget, using
 * their preferred widget creation methodology.
 */

metawidget.Pipeline.prototype.buildNestedMetawidget = function( attributes, mw ) {

	return undefined;
};
