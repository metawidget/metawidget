// Metawidget (licensed under LGPL)
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
 *        <tt>spinner</tt> to suit the inspected fields. Returns undefined for
 *        everything else.
 */

metawidget.jqueryui.widgetbuilder.JQueryUIWidgetBuilder = function() {

	if ( ! ( this instanceof metawidget.jqueryui.widgetbuilder.JQueryUIWidgetBuilder ) ) {
		throw new Error( "Constructor called as a function" );
	}
};

metawidget.jqueryui.widgetbuilder.JQueryUIWidgetBuilder.prototype.buildWidget = function( attributes, mw ) {

	// Not for us?

	if ( attributes.readOnly === 'true' ) {
		return;
	}

	if ( attributes.hidden === 'true' ) {
		return;
	}

	// Number

	if ( attributes.type === 'number' ) {

		if ( attributes.minimumValue && attributes.maximumValue ) {
			var slider = document.createElement( 'div' );
			$( slider ).slider();
			return slider;
		}

		var spinner = document.createElement( 'input' );
		$( spinner ).spinner();
		return $( spinner ).spinner( 'widget' )[0];
	}

	// Datepicker

	if ( attributes.type === 'date' ) {
		var date = document.createElement( 'input' );
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
 *        <tt>$( widget ).foo( 'value', value )</tt> syntax.
 */

metawidget.jqueryui.widgetprocessor.JQueryUIBindingProcessor = function() {

	if ( ! ( this instanceof metawidget.jqueryui.widgetprocessor.JQueryUIBindingProcessor ) ) {
		throw new Error( "Constructor called as a function" );
	}
};

metawidget.jqueryui.widgetprocessor.JQueryUIBindingProcessor.prototype.onStartBuild = function( mw ) {

	mw._jQueryUIBindingProcessorBindings = {};
};

metawidget.jqueryui.widgetprocessor.JQueryUIBindingProcessor.prototype.processWidget = function( widget, attributes, mw ) {

	var value;
	var typeAndNames = metawidget.util.splitPath( mw.path );
	var toInspect = metawidget.util.traversePath( mw.toInspect, typeAndNames.names );

	if ( attributes._root !== 'true' && toInspect ) {
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

	if ( isBindable === true || widget.metawidget !== undefined ) {
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

		if ( widget.metawidget !== undefined ) {
			this.save( widget.metawidget );
			continue;
		}

		widget = document.getElementById( widget.id );

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
			_superOnEndBuild( mw );
		}
	};
};

metawidget.jqueryui.layout.TabLayoutDecorator.prototype.createSectionWidget = function( previousSectionWidget, section, attributes, container, mw ) {

	var tabs = previousSectionWidget;

	// Whole new tabbed pane?

	if ( tabs === undefined ) {
		tabs = document.createElement( 'div' );
		tabs.setAttribute( 'id', metawidget.util.getId( attributes, mw ) + '-tabs' );
		tabs.appendChild( document.createElement( 'ul' ) );
		this.getDelegate().layoutWidget( tabs, {
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
	var li = document.createElement( 'li' );
	var a = document.createElement( 'a' );
	a.setAttribute( 'href', '#' + tabId );
	a.hash = '#' + tabId;
	li.appendChild( a );
	ul.appendChild( li );

	var tab = document.createElement( 'div' );
	tab.setAttribute( 'id', tabId );
	tabs.appendChild( tab );

	// Tab name

	a.innerHTML = section;

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
		readOnly: false,
		inspector: new metawidget.inspector.PropertyTypeInspector(),
		widgetBuilder: new metawidget.widgetbuilder.CompositeWidgetBuilder( [ new metawidget.widgetbuilder.OverriddenWidgetBuilder(), new metawidget.jqueryui.widgetbuilder.JQueryUIWidgetBuilder(),
				new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(), new metawidget.widgetbuilder.HtmlWidgetBuilder() ] ),
		widgetProcessors: [ new metawidget.widgetprocessor.IdProcessor(), new metawidget.widgetprocessor.RequiredAttributeProcessor(),
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
		this._pipeline.buildNestedMetawidget = function( attributes, mw ) {

			var nestedWidget = document.createElement( 'div' );
			var nestedMetawidget = $( nestedWidget ).metawidget( mw.options );

			nestedMetawidget.metawidget( "option", "readOnly", mw.readOnly || attributes.readOnly === 'true' );
			var nestedToInspect = mw.toInspect;
			var nestedPath = metawidget.util.appendPath( attributes, mw );

			// Attach ourselves as a property of the tag, rather than try to
			// 'extend' the built-in HTML tags

			nestedWidget.metawidget = $( nestedWidget ).data( 'metawidget' );

			nestedMetawidget.metawidget( "buildWidgets", nestedToInspect, nestedPath );
			return nestedWidget;
		};

		// Configure defaults

		this._pipeline.configure( this.options );

		// First time in, capture the contents of the Metawidget (if any)

		this._overriddenNodes = [];

		var element = this.element[0];

		for ( var loop = 0; loop < element.childNodes.length; ) {
			if ( element.childNodes[loop].nodeType !== 1 ) {
				loop++;
				continue;
			}

			var childNode = element.childNodes[loop];
			element.removeChild( childNode );
			this._overriddenNodes.push( childNode );
		}

		this._refresh();
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
			var splitPath = metawidget.util.splitPath( this.path );
			inspectionResult = this._pipeline.inspect( this.toInspect, splitPath.type, splitPath.names, this );
		}

		this._pipeline.buildWidgets( inspectionResult, this );
	},

	/**
	 * _setOptions is called with a hash of all options that are changing.
	 */

	_setOptions: function() {

		this._superApply( arguments );
		this._pipeline.configure( this.options );
		this._refresh();
	},

	/**
	 * _setOption is called for each individual option that is changing.
	 */

	_setOption: function( key, value ) {

		if ( key === "readOnly" ) {
			this.readOnly = value;
		}

		this._super( key, value );
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
	}
} );
