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

var metawidget = ( typeof window !== 'undefined' ? window.metawidget : undefined ) || metawidget || {};

( function() {

	'use strict';

	/**
	 * @namespace Metawidget for JQuery UI environments.
	 */

	metawidget.jqueryui = metawidget.jqueryui || {};

	/**
	 * @namespace JQuery UI WidgetBuilders.
	 */

	metawidget.jqueryui.widgetbuilder = metawidget.jqueryui.widgetbuilder || {};

	/**
	 * @class Builds widgets using JQuery UI.
	 *        <p>
	 *        Chooses JQuery UI widgets such as <tt>slider</tt> and
	 *        <tt>spinner</tt> to suit the inspected fields. Returns undefined
	 *        for everything else.
	 */

	metawidget.jqueryui.widgetbuilder.JQueryUIWidgetBuilder = function() {

		if ( ! ( this instanceof metawidget.jqueryui.widgetbuilder.JQueryUIWidgetBuilder ) ) {
			throw new Error( "Constructor called as a function" );
		}
	};

	metawidget.jqueryui.widgetbuilder.JQueryUIWidgetBuilder.prototype.buildWidget = function( elementName, attributes, mw ) {

		// Not for us?

		if ( metawidget.util.isTrueOrTrueString( attributes.readOnly ) ) {
			return;
		}

		if ( metawidget.util.isTrueOrTrueString( attributes.hidden ) ) {
			return;
		}

		// Number

		if ( attributes.type === 'number' || attributes.type === 'integer' ) {

			if ( attributes.minimum && attributes.maximum ) {
				var slider = metawidget.util.createElement( mw, 'div' );
				$( slider ).slider();
				return slider;
			}

			var spinner = metawidget.util.createElement( mw, 'input' );
			$( spinner ).spinner();
			return $( spinner ).spinner( 'widget' )[0];
		}

		// Datepicker

		if ( attributes.type === 'date' ) {
			var date = metawidget.util.createElement( mw, 'input' );
			$( date ).datepicker();
			return date;
		}
	};

	/**
	 * @namespace JQuery UI WidgetProcessors.
	 */

	metawidget.jqueryui.widgetprocessor = metawidget.jqueryui.widgetprocessor || {};

	/**
	 * @class Binds JQuery UI specific widgets, using the JQuery
	 *        <tt>$( widget ).foo( 'value', value )</tt> syntax. Clients
	 *        should still use SimpleBindingProcessor for all non-JQuery UI
	 *        widgets.
	 */

	// Note: it makes sense for JQueryUIBindingProcessor to be a standalone
	// WidgetProcessor, rather than replacing SimpleBindingProcessor, because
	// JQuery UI is a widget framework rather than an application framework. On
	// the other hand, JQuery Mobile and Angular are application frameworks and
	// therefore replace SimpleBindingProcessor
	//
	metawidget.jqueryui.widgetprocessor.JQueryUIBindingProcessor = function() {

		if ( ! ( this instanceof metawidget.jqueryui.widgetprocessor.JQueryUIBindingProcessor ) ) {
			throw new Error( "Constructor called as a function" );
		}
	};

	metawidget.jqueryui.widgetprocessor.JQueryUIBindingProcessor.prototype.onStartBuild = function( mw ) {

		mw._jQueryUIBindingProcessorBindings = {};
	};

	metawidget.jqueryui.widgetprocessor.JQueryUIBindingProcessor.prototype.processWidget = function( widget, elementName, attributes, mw ) {

		var value;
		var typeAndNames = metawidget.util.splitPath( mw.path );
		var toInspect = metawidget.util.traversePath( mw.toInspect, typeAndNames.names );

		if ( elementName !== 'entity' && toInspect ) {
			value = toInspect[attributes.name];
		} else {
			value = toInspect;
		}

		var isBindable = false;

		if ( widget.hasAttribute( 'class' ) ) {
			var styleClass = widget.getAttribute( 'class' );

			if ( styleClass.indexOf( 'ui-slider' ) !== -1 ) {
				$( widget ).slider( 'value', value );
				isBindable = true;
			} else if ( styleClass.indexOf( 'ui-spinner' ) !== -1 ) {
				$( widget.childNodes[0] ).spinner( 'value', value );
				isBindable = true;
			}
		}

		if ( isBindable === true || widget.getMetawidget !== undefined ) {
			mw._jQueryUIBindingProcessorBindings[attributes.name] = widget;
		}

		return widget;
	};

	/**
	 * Save the bindings associated with the given Metawidget.
	 */

	metawidget.jqueryui.widgetprocessor.JQueryUIBindingProcessor.prototype.save = function( mw ) {

		var typeAndNames = metawidget.util.splitPath( mw.path );
		var toInspect = metawidget.util.traversePath( mw.toInspect, typeAndNames.names );

		for ( var name in mw._jQueryUIBindingProcessorBindings ) {

			var widget = mw._jQueryUIBindingProcessorBindings[name];

			if ( widget.getMetawidget !== undefined ) {
				this.save( widget.getMetawidget() );
				continue;
			}

			widget = mw.getElement().ownerDocument.getElementById( widget.id );

			var styleClass = widget.getAttribute( 'class' );

			if ( styleClass.indexOf( 'ui-slider' ) !== -1 ) {
				toInspect[name] = $( widget ).slider( 'value' );
			} else if ( styleClass.indexOf( 'ui-spinner' ) !== -1 ) {
				toInspect[name] = $( widget.childNodes[0] ).spinner( 'value' );
			}
		}
	};

	metawidget.jqueryui.layout = metawidget.jqueryui.layout || {};

	/**
	 * @class LayoutDecorator to decorate widgets from different sections using
	 *        JQuery UI tabs.
	 */

	metawidget.jqueryui.layout.TabLayoutDecorator = function( config ) {

		if ( ! ( this instanceof metawidget.jqueryui.layout.TabLayoutDecorator ) ) {
			throw new Error( "Constructor called as a function" );
		}

		metawidget.layout.createNestedSectionLayoutDecorator( config, this, 'tabLayoutDecorator' );

		var _superOnEndBuild = this.onEndBuild;

		/**
		 * Wrap the tabs at the very end, to save using 'tabs.add'.
		 */

		this.onEndBuild = function( mw ) {

			if ( mw.tabLayoutDecorator !== undefined ) {
				for ( var loop = 0, length = mw.tabLayoutDecorator.length; loop < length; loop++ ) {
					$( mw.tabLayoutDecorator[loop] ).tabs();
				}
			}

			if ( _superOnEndBuild !== undefined ) {
				_superOnEndBuild.call( this, mw );
			}
		};
	};

	metawidget.jqueryui.layout.TabLayoutDecorator.prototype.createSectionWidget = function( previousSectionWidget, section, attributes, container, mw ) {

		var tabs = previousSectionWidget;

		// Whole new tabbed pane?

		if ( tabs === undefined ) {
			tabs = metawidget.util.createElement( mw, 'div' );
			tabs.setAttribute( 'id', metawidget.util.getId( "property", attributes, mw ) + '-tabs' );
			tabs.appendChild( metawidget.util.createElement( mw, 'ul' ) );
			this.getDelegate().layoutWidget( tabs, "property", {
				wide: "true"
			}, container, mw );

			mw.tabLayoutDecorator = mw.tabLayoutDecorator || [];
			mw.tabLayoutDecorator.push( tabs );
		} else {
			tabs = previousSectionWidget.parentNode;
		}

		// New Tab

		var ul = tabs.childNodes[0];
		var tabId = tabs.getAttribute( 'id' ) + ( ul.childNodes.length + 1 );
		var li = metawidget.util.createElement( mw, 'li' );
		var a = metawidget.util.createElement( mw, 'a' );
		a.setAttribute( 'href', '#' + tabId );
		li.appendChild( a );
		ul.appendChild( li );

		var tab = metawidget.util.createElement( mw, 'div' );
		tab.setAttribute( 'id', tabId );
		tabs.appendChild( tab );

		// Tab name

		a.textContent = section;

		return tab;
	};

	/**
	 * JQuery UI WidgetFactory-based Metawidget.
	 */

	$.widget( "metawidget.metawidget", {

		/**
		 * Default configuration
		 */

		options: {
			inspector: new metawidget.inspector.PropertyTypeInspector(),
			widgetBuilder: new metawidget.widgetbuilder.CompositeWidgetBuilder( [ new metawidget.widgetbuilder.OverriddenWidgetBuilder(),
					new metawidget.jqueryui.widgetbuilder.JQueryUIWidgetBuilder(), new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(), new metawidget.widgetbuilder.HtmlWidgetBuilder() ] ),
			widgetProcessors: [ new metawidget.widgetprocessor.IdProcessor(), new metawidget.widgetprocessor.RequiredAttributeProcessor(),
					new metawidget.widgetprocessor.PlaceholderAttributeProcessor(), new metawidget.widgetprocessor.DisabledAttributeProcessor(),
					new metawidget.jqueryui.widgetprocessor.JQueryUIBindingProcessor(), new metawidget.widgetprocessor.SimpleBindingProcessor() ],
			layout: new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.TableLayout() )
		},

		/**
		 * Constructor
		 */

		_create: function() {

			// Pipeline (private, based on convention here:
			// http://forum.jquery.com/topic/what-s-the-right-way-to-store-private-data-in-widget-s-instance)

			this._pipeline = new metawidget.Pipeline( this.element[0] );

			// Configure defaults

			this._pipeline.configure( this.options );

			// First time in, capture the contents of the Metawidget (if any)

			this._overriddenNodes = [];

			var element = this.element[0];

			var mw = this;

			element.getMetawidget = function() {

				return mw;
			};

			for ( var loop = 0; loop < element.childNodes.length; ) {
				if ( element.childNodes[loop].nodeType !== 1 ) {
					loop++;
					continue;
				}

				var childNode = element.childNodes[loop];
				element.removeChild( childNode );
				this._overriddenNodes.push( childNode );
			}
		},

		/**
		 * Called when created, and later when changing options.
		 */

		_refresh: function( inspectionResult ) {

			// Defensive copy

			this.overriddenNodes = [];

			for ( var loop = 0, length = this._overriddenNodes.length; loop < length; loop++ ) {
				this.overriddenNodes.push( this._overriddenNodes[loop].cloneNode( true ) );
			}

			// Inspect (if necessary)

			if ( inspectionResult === undefined ) {

				// Safeguard against improperly implementing:
				// http://blog.kennardconsulting.com/2013/02/metawidget-and-rest.html

				if ( arguments.length > 0 ) {
					throw new Error( "Calling _refresh( undefined ) may cause infinite loop. Check your argument, or pass no arguments instead" );
				}

				var splitPath = metawidget.util.splitPath( this.path );
				inspectionResult = this._pipeline.inspect( this.toInspect, splitPath.type, splitPath.names, this );
			}

			// Build widgets

			this._pipeline.buildWidgets( inspectionResult, this );
		},

		/**
		 * _setOptions is called with a hash of all options that are changing.
		 */

		_setOptions: function() {

			this._superApply( arguments );
			this._pipeline.configure( this.options );
		},

		setReadOnly: function( readOnly ) {

			this.readOnly = readOnly;
		},

		/**
		 * Useful for WidgetBuilders to perform nested inspections (eg. for
		 * Collections).
		 */

		inspect: function( toInspect, type, names ) {

			return this._pipeline.inspect( toInspect, type, names, this );
		},

		/**
		 * Overridden to use JQuery.empty (safer for memory leaks).
		 */

		clearWidgets: function() {

			$( this.getElement() ).empty();
		},

		/**
		 * Inspect the given toInspect/path and build widgets.
		 * <p>
		 * Invoke using
		 * <tt>$( '#metawidget' ).metawidget( "buildWidgets", toInspect, path )</tt>.
		 */

		buildWidgets: function( toInspect, path ) {

			if ( toInspect !== undefined ) {
				this.toInspect = toInspect;
				this.path = undefined;
			}

			if ( path !== undefined ) {
				this.path = path;
			}

			this._refresh();
		},

		getWidgetProcessor: function( testInstanceOf ) {

			return this._pipeline.getWidgetProcessor( testInstanceOf );
		},

		/**
		 * Returns the element this Metawidget is attached to.
		 */

		getElement: function() {

			return this._pipeline.element;
		},

		buildNestedMetawidget: function( attributes, config ) {

			// Create a 'div' not a 'metawidget', because whilst it's up to the
			// user what they want their top-level element to be, for browser
			// compatibility we should stick with something benign for nested
			// elements

			var nestedWidget = metawidget.util.createElement( this, 'div' );

			// Duck-type our 'pipeline' as the 'config' of the nested
			// Metawidget. This neatly passes everything down, including a
			// decremented 'maximumInspectionDepth'

			var nestedMetawidget = $( nestedWidget ).metawidget( this._pipeline );

			nestedMetawidget.metawidget( "setReadOnly", this.readOnly || metawidget.util.isTrueOrTrueString( attributes.readOnly ) );
			var nestedToInspect = this.toInspect;
			var nestedPath = metawidget.util.appendPath( attributes, this );

			nestedMetawidget.metawidget( "buildWidgets", nestedToInspect, nestedPath );
			return nestedWidget;
		}
	} );
} )();

if ( typeof module !== 'undefined' && typeof module.exports !== 'undefined' ) {
	module.exports = metawidget.jqueryui;
}