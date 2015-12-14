// Metawidget ${project.version}
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

/**
 * @namespace Metawidget for pure JavaScript environments.
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

var metawidget = metawidget || {};

( function() {

	'use strict';

	/**
	 * Pure JavaScript Metawidget.
	 * 
	 * @param element
	 *            the element to populate with UI components matching the
	 *            properties of the domain object
	 * @param config
	 *            optional configuration object (see
	 *            metawidget.Pipeline.configure)
	 * @returns {metawidget.Metawidget}
	 */

	metawidget.Metawidget = function( element, config ) {

		if ( ! ( this instanceof metawidget.Metawidget ) ) {
			throw new Error( 'Constructor called as a function' );
		}

		// Attach ourselves as a property of the tag, rather than try to
		// 'extend' the built-in HTML tags. This is used by
		// SimpleBindingProcessor, among others

		var mw = this;

		element.getMetawidget = function() {

			return mw;
		};

		// Pipeline (private)

		var _pipeline = new metawidget.Pipeline( element );

		// Configure defaults

		_pipeline.inspector = new metawidget.inspector.PropertyTypeInspector();
		_pipeline.widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [ new metawidget.widgetbuilder.OverriddenWidgetBuilder(), new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(),
				new metawidget.widgetbuilder.HtmlWidgetBuilder() ] );
		_pipeline.widgetProcessors = [ new metawidget.widgetprocessor.IdProcessor(), new metawidget.widgetprocessor.RequiredAttributeProcessor(),
				new metawidget.widgetprocessor.PlaceholderAttributeProcessor(), new metawidget.widgetprocessor.DisabledAttributeProcessor(), new metawidget.widgetprocessor.SimpleBindingProcessor() ];
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

		this.reconfigure = function( config ) {

			return _pipeline.configure( config );
		};

		/**
		 * Save the contents of the Metawidget using a SimpleBindingProcessor.
		 * <p>
		 * This is a convenience method. To access other Metawidget APIs,
		 * clients can use the 'getWidgetProcessor' method
		 * 
		 * @returns true if the 'toInspect' was updated (i.e. is dirty)
		 */

		this.save = function() {

			return _pipeline.getWidgetProcessor( function( widgetProcessor ) {

				return widgetProcessor instanceof metawidget.widgetprocessor.SimpleBindingProcessor;
			} ).save( this );
		};

		this.getWidgetProcessor = function( testInstanceOf ) {

			return _pipeline.getWidgetProcessor( testInstanceOf );
		};

		this.setLayout = function( layout ) {

			_pipeline.layout = layout;
		};

		/**
		 * Useful for WidgetBuilders to perform nested inspections (eg. for
		 * Collections).
		 */

		this.inspect = function( toInspect, type, names ) {

			return _pipeline.inspect( toInspect, type, names, this );
		};

		this.buildWidgets = function( inspectionResult ) {

			// Defensive copy

			this.overriddenNodes = [];

			for ( var loop = 0, length = _overriddenNodes.length; loop < length; loop++ ) {
				this.overriddenNodes.push( _overriddenNodes[loop].cloneNode( true ) );
			}

			// Inspect (if necessary)

			if ( inspectionResult === undefined ) {

				// Safeguard against improperly implementing:
				// http://blog.kennardconsulting.com/2013/02/metawidget-and-rest.html

				if ( arguments.length > 0 ) {
					throw new Error( "Calling buildWidgets( undefined ) may cause infinite loop. Check your argument, or pass no arguments instead" );
				}

				var splitPath = metawidget.util.splitPath( this.path );
				inspectionResult = _pipeline.inspect( this.toInspect, splitPath.type, splitPath.names, this );
			}

			// Build widgets

			_pipeline.buildWidgets( inspectionResult, this );
		};

		/**
		 * Returns the element this Metawidget is attached to.
		 */

		this.getElement = function() {

			return _pipeline.element;
		};

		/**
		 * Clear all child elements from the Metawidget element.
		 * <p>
		 * This implementation uses plain JavaScript <tt>removeChild</tt>,
		 * which has known problems (on some browsers) leaking event handlers.
		 * This is not a problem for plain Metawidget, as it doesn't use event
		 * handlers. However clients that introduce custom widgetprocessors that
		 * use event handlers may wish to adopt a more robust technology for
		 * tracking/clearing event handlers (such as JQuery.empty)
		 */

		this.clearWidgets = function() {

			var element = this.getElement();

			while ( element.childNodes.length > 0 ) {
				element.removeChild( element.childNodes[0] );
			}
		};

		/**
		 * Returns a nested version of this same Metawidget, using the given
		 * attributes.
		 * <p>
		 * Subclasses should override this method to use their preferred widget
		 * creation methodology.
		 */

		this.buildNestedMetawidget = function( attributes, config ) {

			// Create a 'div' not a 'metawidget', because whilst it's up to the
			// user what they want their top-level element to be, for browser
			// compatibility we should stick with something benign for nested
			// elements

			var nestedWidget = metawidget.util.createElement( this, 'div' );

			// Duck-type our 'pipeline' as the 'config' of the nested
			// Metawidget. This neatly passes everything down, including a
			// decremented 'maximumInspectionDepth'

			var nestedMetawidget = new metawidget.Metawidget( nestedWidget, [ _pipeline, config ] );
			nestedMetawidget.toInspect = this.toInspect;
			nestedMetawidget.path = metawidget.util.appendPath( attributes, this );
			nestedMetawidget.readOnly = this.readOnly || metawidget.util.isTrueOrTrueString( attributes.readOnly );
			nestedMetawidget.buildWidgets();

			return nestedWidget;
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

		var loop;

		if ( config instanceof Array ) {
			for ( loop = 0; loop < config.length; loop++ ) {
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

		// Support prepending/adding to the existing array of
		// InspectionResultProcessors
		// (it may be hard for clients to redefine the originals)

		if ( config.prependInspectionResultProcessors !== undefined ) {
			if ( !( config.prependInspectionResultProcessors instanceof Array )) {
				config.prependInspectionResultProcessors = [ config.prependInspectionResultProcessors ];
			}
			for ( loop = 0; loop < config.prependInspectionResultProcessors.length; loop++ ) {
				this.inspectionResultProcessors.splice( loop, 0, config.prependInspectionResultProcessors[loop] );
			}
		}
		if ( config.appendInspectionResultProcessors !== undefined ) {
			if ( !( config.appendInspectionResultProcessors instanceof Array )) {
				config.appendInspectionResultProcessors = [ config.appendInspectionResultProcessors ];
			}
			for ( loop = 0; loop < config.appendInspectionResultProcessors.length; loop++ ) {
				this.inspectionResultProcessors.push( config.appendInspectionResultProcessors[loop] );
			}
		}
		if ( config.widgetBuilder !== undefined ) {
			this.widgetBuilder = config.widgetBuilder;
		}
		if ( config.widgetProcessors !== undefined ) {
			this.widgetProcessors = config.widgetProcessors.slice( 0 );
		}

		// Support prepending/appending to the existing array of
		// WidgetProcessors
		// (it may be hard for clients to redefine the originals)

		if ( config.prependWidgetProcessors !== undefined ) {
			if ( !( config.prependWidgetProcessors instanceof Array )) {
				config.prependWidgetProcessors = [ config.prependWidgetProcessors ];
			}
			for ( loop = 0; loop < config.prependWidgetProcessors.length; loop++ ) {
				this.widgetProcessors.splice( loop, 0, config.prependWidgetProcessors[loop] );
			}
		}
		if ( config.appendWidgetProcessors !== undefined ) {
			if ( !( config.appendWidgetProcessors instanceof Array )) {
				config.appendWidgetProcessors = [ config.appendWidgetProcessors ];
			}
			for ( loop = 0; loop < config.appendWidgetProcessors.length; loop++ ) {
				this.widgetProcessors.push( config.appendWidgetProcessors[loop] );
			}
		}
		if ( config.layout !== undefined ) {
			this.layout = config.layout;
		}

		// Safeguard against infinite recursion

		if ( config.maximumInspectionDepth !== undefined ) {
			this.maximumInspectionDepth = config.maximumInspectionDepth - 1;
		}

		// CSS support

		if ( config.styleClass !== undefined ) {
			this.styleClass = config.styleClass;
			metawidget.util.appendToAttribute( this.element, 'class', config.styleClass );
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

		mw.clearWidgets();

		_startBuild( this, mw );

		// Build top-level widget...

		if ( inspectionResult !== undefined ) {

			var copiedAttributes = _forceReadOnly( inspectionResult, mw, 'properties' );
			var elementName = "entity";
			var widget = _buildWidget( this, elementName, copiedAttributes, mw );

			if ( widget !== undefined ) {

				widget = _processWidget( this, widget, elementName, copiedAttributes, mw );

				if ( widget !== undefined ) {
					this.layoutWidget( widget, elementName, copiedAttributes, this.element, mw );
				}

			} else {

				// ...or try compound widget

				var inspectionResultProperties = metawidget.util.getSortedInspectionResultProperties( inspectionResult );

				for ( var loop = 0, length = inspectionResultProperties.length; loop < length; loop++ ) {

					copiedAttributes = _forceReadOnly( inspectionResultProperties[loop], mw );

					if ( copiedAttributes.type === 'function' ) {
						elementName = "action";
					} else {
						elementName = "property";
					}

					widget = _buildWidget( this, elementName, copiedAttributes, mw );

					if ( widget === undefined ) {

						if ( this.maximumInspectionDepth <= 0 ) {
							continue;
						}

						widget = mw.buildNestedMetawidget( copiedAttributes );

						if ( widget === undefined ) {
							continue;
						}
					}

					widget = _processWidget( this, widget, elementName, copiedAttributes, mw );

					if ( widget !== undefined ) {
						this.layoutWidget( widget, elementName, copiedAttributes, this.element, mw );
					}
				}
			}
		}

		// Even if no inspectors match, we still call startBuild()/endBuild()
		// because you can use a Metawidget purely for layout, with no
		// inspection

		_endBuild( this, mw );

		// Throw an event for interested parties (such as tests). Does not work
		// on IE8

		if ( this.element.dispatchEvent !== undefined ) {
			this.element.dispatchEvent( metawidget.util.createEvent( mw, 'buildEnd' ) );
		}

		//
		// Private methods
		//

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

			// Mark overridden widgets. This is useful for Angular so that it
			// doesn't $compile them again. It's useful for JQuery Mobile so it
			// doesn't .trigger( 'create' ) them again

			for ( var loop = 0, length = mw.overriddenNodes.length; loop < length; loop++ ) {
				mw.overriddenNodes[loop].overridden = true;
			}

			if ( pipeline.widgetBuilder.onStartBuild !== undefined ) {
				pipeline.widgetBuilder.onStartBuild( mw );
			}

			_onStartEndBuild( 'onStartBuild', pipeline, mw );

			if ( pipeline.layout.onStartBuild !== undefined ) {
				pipeline.layout.onStartBuild( mw );
			}

			if ( pipeline.layout.startContainerLayout !== undefined ) {
				pipeline.layout.startContainerLayout( pipeline.element, mw );
			}
		}

		function _buildWidget( pipeline, elementName, attributes, mw ) {

			if ( pipeline.widgetBuilder.buildWidget !== undefined ) {
				return pipeline.widgetBuilder.buildWidget( elementName, attributes, mw );
			}

			return pipeline.widgetBuilder( elementName, attributes, mw );
		}

		function _processWidget( pipeline, widget, elementName, attributes, mw ) {

			for ( var loop = 0, length = pipeline.widgetProcessors.length; loop < length; loop++ ) {

				var widgetProcessor = pipeline.widgetProcessors[loop];

				if ( widgetProcessor.processWidget !== undefined ) {
					widget = widgetProcessor.processWidget( widget, elementName, attributes, mw );
				} else {
					widget = widgetProcessor( widget, elementName, attributes, mw );
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

					// Stubs can supply their own metadata (such as 'title')

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

					pipeline.layoutWidget( child, "property", childAttributes, pipeline.element, mw );
				}
			}

			// End all stages of the pipeline

			if ( pipeline.layout.endContainerLayout !== undefined ) {
				pipeline.layout.endContainerLayout( pipeline.element, mw );
			}

			if ( pipeline.layout.onEndBuild !== undefined ) {
				pipeline.layout.onEndBuild( mw );
			}

			_onStartEndBuild( 'onEndBuild', pipeline, mw );

			if ( pipeline.widgetBuilder.onEndBuild !== undefined ) {
				pipeline.widgetBuilder.onEndBuild( mw );
			}
		}

		function _onStartEndBuild( functionName, pipeline, mw ) {

			for ( var loop = 0, length = pipeline.widgetProcessors.length; loop < length; loop++ ) {

				var widgetProcessor = pipeline.widgetProcessors[loop];

				if ( widgetProcessor[functionName] !== undefined ) {
					widgetProcessor[functionName]( mw );
				}
			}
		}
	};

	/**
	 * Layout the given widget by delegating to the configured Layout.
	 * <p>
	 * Subclasses can override this method to perform any post-processing of the
	 * widget, following layout. For <em>pre</em>-processing, subclasses
	 * should use a WidgetProcessor.
	 */

	metawidget.Pipeline.prototype.layoutWidget = function( widget, elementName, attributes, container, mw ) {

		if ( this.layout.layoutWidget !== undefined ) {
			this.layout.layoutWidget( widget, elementName, attributes, container, mw );
			return;
		}

		this.layout( widget, elementName, attributes, container, mw );
	};
} )();