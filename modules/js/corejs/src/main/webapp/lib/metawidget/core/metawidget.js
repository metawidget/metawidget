// Metawidget ${project.version} (licensed under LGPL)
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

/**
 * @namespace Metawidget for pure JavaScript environments.
 */

var metawidget = metawidget || {};

( function() {

	'use strict';

	/**
	 * Pure JavaScript Metawidget.
	 * 
	 * @param element
	 *            the element to populate with UI components matching the
	 *            properties of the business object
	 * @param config
	 *            optional configuration object (see
	 *            metawidget.Pipeline.configure)
	 * @returns {metawidget.Metawidget}
	 */

	metawidget.Metawidget = function( element, config ) {

		if ( ! ( this instanceof metawidget.Metawidget ) ) {
			throw new Error( 'Constructor called as a function' );
		}

		// Pipeline (private)

		var _pipeline = new metawidget.Pipeline( element );
		_pipeline.buildNestedMetawidget = function( attributes, mw ) {

			var nestedWidget = document.createElement( 'div' );

			// Duck-type our 'pipeline' as the 'config' of the nested Metawidget

			var nestedMetawidget = new metawidget.Metawidget( nestedWidget, _pipeline );
			nestedMetawidget.toInspect = mw.toInspect;
			nestedMetawidget.path = metawidget.util.appendPath( attributes, mw );
			nestedMetawidget.readOnly = mw.readOnly || attributes.readOnly === 'true';

			// Attach ourselves as a property of the tag, rather than try to
			// 'extend' the built-in HTML tags. This is used by
			// SimpleBindingProcessor, among others

			nestedWidget.metawidget = nestedMetawidget;
			nestedMetawidget.buildWidgets();

			return nestedWidget;
		};

		// Configure defaults

		_pipeline.inspector = new metawidget.inspector.PropertyTypeInspector();
		_pipeline.widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [ new metawidget.widgetbuilder.OverriddenWidgetBuilder(), new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(),
				new metawidget.widgetbuilder.HtmlWidgetBuilder() ] );
		_pipeline.widgetProcessors = [ new metawidget.widgetprocessor.IdProcessor(), new metawidget.widgetprocessor.RequiredAttributeProcessor(),
				new metawidget.widgetprocessor.SimpleBindingProcessor() ];
		_pipeline.layout = new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.TableLayout() );
		_pipeline.configure( config );

		// First time in, capture the contents of the Metawidget, if any
		// (private)

		var _overriddenNodes = [];

		while ( element.childNodes.length > 0 ) {
			var childNode = element.childNodes[0];
			element.removeChild( childNode );

			if ( childNode.nodeType === 1 ) {
				_overriddenNodes.push( childNode );
			}
		}

		//
		// Public methods
		//

		this.getWidgetProcessor = function( testInstanceOf ) {

			return _pipeline.getWidgetProcessor( testInstanceOf );
		};

		this.setLayout = function( layout ) {

			_pipeline.layout = layout;
		};

		this.buildWidgets = function( inspectionResult ) {

			// Defensive copy

			this.overriddenNodes = [];

			for ( var loop = 0, length = _overriddenNodes.length; loop < length; loop++ ) {
				this.overriddenNodes.push( _overriddenNodes[loop].cloneNode( true ) );
			}

			// Inspect (if necessary)

			if ( inspectionResult === undefined ) {
				var splitPath = metawidget.util.splitPath( this.path );
				inspectionResult = _pipeline.inspect( this.toInspect, splitPath.type, splitPath.names, this );
			}

			// Build widgets

			_pipeline.buildWidgets( inspectionResult, this );
		};
	};

	/**
	 * @class Convenience implementation for implementing pipelines (see
	 *        http://metawidget.org/doc/reference/en/html/ch02.html).
	 *        <p>
	 *        Specifically, BasePipeline provides support for:
	 *        </p>
	 *        <ul>
	 *        <li>Inspectors, InspectionResultProcessors, WidgetBuilders,
	 *        WidgetProcessors and Layouts</li>
	 *        <li>single/compound widgets</li>
	 *        <li>stubs/stub attributes</li>
	 *        <li>read-only/active widgets</li>
	 *        <li>maximum inspection depth</li>
	 *        </ul>
	 *        <p>
	 *        Clients should override 'buildNestedMetawidget'.
	 *        </p>
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
	 * defaults. The following configuration properties are supported:
	 * <ul>
	 * <li>inspector - an Inspector</li>
	 * <li>inspectionResultProcessors - an array of InspectionResultProcessors</li>
	 * <li>widgetBuilder - a WidgetBuilder</li>
	 * <li>widgetProcessors - an array of WidgetProcessors</li>
	 * <li>layout - a Layout</li>
	 * </ul>
	 * 
	 * @param config
	 *            the config object to use. This can be an array, in which case
	 *            multiple configs will be applied (in the order they appear in
	 *            the array)
	 */

	metawidget.Pipeline.prototype.configure = function( config ) {

		if ( config === undefined ) {
			return;
		}

		// Support arrays of configs

		if ( config instanceof Array ) {
			for ( var loop = 0, length = config.length; loop < length; loop++ ) {
				this.configure( config[loop] );
			}
			return;
		}
		if ( config.inspector !== undefined ) {
			this.inspector = config.inspector;
		}
		if ( config.inspectionResultProcessors !== undefined ) {
			this.inspectionResultProcessors = config.inspectionResultProcessors.slice( 0 );
		}

		// Support adding to the existing array of InspectionResultProcessors
		// (it may be hard for clients to redefine the originals)

		if ( config.addInspectionResultProcessors !== undefined ) {
			for ( var loop = 0, length = config.addInspectionResultProcessors.length; loop < length; loop++ ) {
				this.inspectionResultProcessors.push( config.addInspectionResultProcessors[loop] );
			}
		}
		if ( config.widgetBuilder !== undefined ) {
			this.widgetBuilder = config.widgetBuilder;
		}
		if ( config.widgetProcessors !== undefined ) {
			this.widgetProcessors = config.widgetProcessors.slice( 0 );
		}

		// Support prepending/adding to the existing array of WidgetProcessors
		// (it may be hard for clients to redefine the originals)

		if ( config.prependWidgetProcessors !== undefined ) {
			for ( var loop = 0, length = config.prependWidgetProcessors.length; loop < length; loop++ ) {
				this.widgetProcessors.splice( loop, 0, config.prependWidgetProcessors[loop] );
			}
		}
		if ( config.addWidgetProcessors !== undefined ) {
			for ( var loop = 0, length = config.addWidgetProcessors.length; loop < length; loop++ ) {
				this.widgetProcessors.push( config.addWidgetProcessors[loop] );
			}
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
	 * Inspect the 'toInspect' according to its 'type' and 'names', and return
	 * the result as a JSON String.
	 * <p>
	 * This method mirrors the <code>Inspector</code> interface. Internally it
	 * looks up the Inspector to use. It is a useful hook for subclasses wishing
	 * to inspect different Objects using our same <code>Inspector</code>.
	 * <p>
	 * In addition, this method runs the <code>InspectionResultProcessors</code>.
	 */

	metawidget.Pipeline.prototype.inspect = function( toInspect, type, names, mw ) {

		// Inspector

		var inspectionResult;

		if ( this.inspector.inspect !== undefined ) {
			inspectionResult = this.inspector.inspect( toInspect, type, names );
		} else {
			inspectionResult = this.inspector( toInspect, type, names );
		}

		// Inspector may return undefined

		if ( inspectionResult === undefined ) {
			return;
		}

		// InspectionResultProcessors

		for ( var loop = 0, length = this.inspectionResultProcessors.length; loop < length; loop++ ) {

			var inspectionResultProcessor = this.inspectionResultProcessors[loop];

			if ( inspectionResultProcessor.processInspectionResult !== undefined ) {
				inspectionResult = inspectionResultProcessor.processInspectionResult( inspectionResult, mw, toInspect, type, names );
			} else {
				inspectionResult = inspectionResultProcessor( inspectionResult, mw, toInspect, type, names );
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
	 * Note: the Pipeline expects the JSON to be passed in externally, rather
	 * than fetching it itself, because some JSON inspections may be
	 * asynchronous.
	 * 
	 * @param inspectionResult
	 *            array of metadata to base widgets on.
	 * @param mw
	 *            Metawidget instance that will be passed down the pipeline
	 *            (WidgetBuilders, WidgetProcessors etc). Expected to have
	 *            'toInspect', 'path' and 'readOnly'.
	 */

	metawidget.Pipeline.prototype.buildWidgets = function( inspectionResult, mw ) {

		// Clear existing contents

		while ( this.element.childNodes.length > 0 ) {
			this.element.removeChild( this.element.childNodes[0] );
		}

		_startBuild( this, mw );

		// Build top-level widget...

		if ( inspectionResult !== undefined ) {

			var copiedAttributes = _forceReadOnly( inspectionResult, mw, 'properties' );
			copiedAttributes._root = 'true';
			var widget = _buildWidget( this, copiedAttributes, mw );

			if ( widget !== undefined ) {

				widget = _processWidget( this, widget, copiedAttributes, mw );

				if ( widget !== undefined ) {
					this.layoutWidget( widget, copiedAttributes, this.element, mw );
				}

			} else {

				// ...or try compound widget

				var inspectionResultProperties = _sortByPropertyOrder( inspectionResult.properties );

				for ( var loop = 0, length = inspectionResultProperties.length; loop < length; loop++ ) {

					copiedAttributes = _forceReadOnly( inspectionResultProperties[loop], mw );
					var widget = _buildWidget( this, copiedAttributes, mw );

					if ( widget === undefined ) {

						if ( this.maximumInspectionDepth <= 0 ) {
							continue;
						}

						widget = this.buildNestedMetawidget( copiedAttributes, mw );

						if ( widget === undefined ) {
							continue;
						}
					}

					widget = _processWidget( this, widget, copiedAttributes, mw );

					if ( widget !== undefined ) {
						this.layoutWidget( widget, copiedAttributes, this.element, mw );
					}
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
		 * Sort the given object's properties by 'propertyOrder' (if any)
		 */

		function _sortByPropertyOrder( toSort ) {

			// Extract the given object's properties into an array...

			var sorted = [];

			for ( var propertyName in toSort ) {

				var properties = toSort[propertyName];
				sorted.push( properties );

				properties.name = propertyName;
				properties._syntheticOrder = sorted.length;
			}

			// ...sort the array...

			sorted.sort( function( a, b ) {

				if ( a.propertyOrder === undefined ) {
					if ( b.propertyOrder === undefined ) {
						return ( a._syntheticOrder - b._syntheticOrder );
					}
					return 1;
				}

				if ( b.propertyOrder === undefined ) {
					return -1;
				}

				var diff = ( a.propertyOrder - b.propertyOrder );

				if ( diff === 0 ) {
					return ( a._syntheticOrder - b._syntheticOrder );
				}

				return diff;
			} );

			// ...and return it

			return sorted;
		}

		/**
		 * Defensively copies the attributes (in case something like
		 * stripSection changes them) and adds 'readOnly' if the given
		 * Metawidget is readOnly.
		 */

		function _forceReadOnly( attributes, mw, excludes ) {

			var copiedAttributes = {};

			for ( var name in attributes ) {

				if ( excludes !== undefined && excludes.indexOf( name ) !== -1 ) {
					continue;
				}

				copiedAttributes[name] = attributes[name];
			}

			// Try to keep the exact nature of the 'readOnly' mechanism (i.e.
			// set on attribute, or set on overall Metawidget) out of the
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

		function _endBuild( pipeline, mw ) {

			if ( mw.onEndBuild !== undefined ) {
				mw.onEndBuild();
			} else {
				while ( mw.overriddenNodes.length > 0 ) {

					var child = mw.overriddenNodes[0];
					mw.overriddenNodes.splice( 0, 1 );

					// Unused facets don't count

					if ( child.tagName === 'FACET' ) {
						continue;
					}

					// Stubs can supply their own metadata (such as 'label')

					var childAttributes = {
						section: ''
					};

					if ( child.tagName === 'STUB' ) {
						for ( var loop = 0, length = child.attributes.length; loop < length; loop++ ) {
							var prop = child.attributes[loop];
							childAttributes[prop.nodeName] = prop.nodeValue;
						}
					}

					// Manually created components default to no section

					pipeline.layoutWidget( child, childAttributes, pipeline.element, mw );
				}
			}

			// End all stages of the pipeline

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

	metawidget.Pipeline.prototype.layoutWidget = function( widget, attributes, container, mw ) {

		if ( this.layout.layoutWidget !== undefined ) {
			this.layout.layoutWidget( widget, attributes, container, mw );
			return;
		}

		this.layout( widget, attributes, container, mw );
	};

	/**
	 * Subclasses should override this method to create a nested Metawidget,
	 * using their preferred widget creation methodology.
	 */

	metawidget.Pipeline.prototype.buildNestedMetawidget = function( attributes, mw ) {

		return undefined;
	};
} )();