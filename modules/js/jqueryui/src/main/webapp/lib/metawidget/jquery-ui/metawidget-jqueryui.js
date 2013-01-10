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

//
// JQueryUIWidgetBuilder
//

metawidget.jqueryui = metawidget.jqueryui || {};
metawidget.jqueryui.widgetbuilder = metawidget.jqueryui.widgetbuilder || {};

metawidget.jqueryui.widgetbuilder.JQueryUIWidgetBuilder = function() {

	if ( ! ( this instanceof metawidget.jqueryui.widgetbuilder.JQueryUIWidgetBuilder ) ) {
		throw new Error( "Constructor called as a function" );
	}
};

metawidget.jqueryui.widgetbuilder.JQueryUIWidgetBuilder.prototype.buildWidget = function( attributes, mw ) {

	// Not for us?

	if ( metawidget.util.isReadOnly( attributes, mw ) ) {
		return;
	}

	if ( attributes.hidden == 'true' ) {
		return;
	}

	// Number

	if ( attributes.type == 'number' ) {

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

	if ( attributes.type == 'date' ) {
		var date = document.createElement( 'input' );
		$( date ).datepicker();
		return date;
	}
};

//
// JQueryUIBindingProcessor
//

metawidget.jqueryui.widgetprocessor = metawidget.jqueryui.widgetprocessor || {};

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
	var toInspect = metawidget.util.traversePath( mw.toInspect, typeAndNames.type, typeAndNames.names );

	if ( attributes.name != '__root' && toInspect ) {
		value = toInspect[attributes.name];
	} else {
		value = toInspect;
	}

	var isBindable = false;
	var styleClass = widget.getAttribute( 'class' );

	if ( styleClass ) {
		if ( styleClass.indexOf( 'ui-slider' ) != -1 ) {
			$( widget ).slider( 'value', value );
			isBindable = true;
		} else if ( styleClass.indexOf( 'ui-spinner' ) != -1 ) {
			$( widget.childNodes[0] ).spinner( 'value', value );
			isBindable = true;
		}
	}

	if ( isBindable || widget.metawidget ) {
		mw._jQueryUIBindingProcessorBindings[attributes.name] = widget;
	}

	return widget;
};

/**
 * Save the bindings associated with the given Metawidget.
 */

metawidget.jqueryui.widgetprocessor.JQueryUIBindingProcessor.prototype.save = function( mw ) {

	var typeAndNames = metawidget.util.splitPath( mw.path );
	var toInspect = metawidget.util.traversePath( mw.toInspect, typeAndNames.type, typeAndNames.names );
	
	for ( var name in mw._jQueryUIBindingProcessorBindings ) {

		var widget = mw._jQueryUIBindingProcessorBindings[name];

		if ( widget.metawidget ) {
			this.save( widget.metawidget );
			continue;
		}

		widget = document.getElementById( widget.id );

		var styleClass = widget.getAttribute( 'class' );

		if ( styleClass.indexOf( 'ui-slider' ) != -1 ) {
			toInspect[name] = $( widget ).slider( 'value' );
		} else if ( styleClass.indexOf( 'ui-spinner' ) != -1 ) {
			toInspect[name] = $( widget.childNodes[0] ).spinner( 'value' );
		}
	}
};

//
// Widget Factory
//

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

		this._pipeline = new metawidget.Pipeline( this.element[0] );
		this._pipeline.buildNestedMetawidget = function( attributes, mw ) {

			var nestedWidget = document.createElement( 'div' );
			var nestedMetawidget = $( nestedWidget ).metawidget( mw.options );

			nestedMetawidget.metawidget( "option", "readOnly", mw.readOnly || attributes.readOnly == 'true' );
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

		for ( var loop = 0, length = this.element[0].childNodes.length; loop < length; loop++ ) {
			this._overriddenNodes.push( this.element[0].childNodes[loop] );
		}

		this._refresh();
	},

	/**
	 * Called when created, and later when changing options.
	 */

	_refresh: function() {

		// Defensive copy

		this.overriddenNodes = [];

		for ( var loop = 0, length = this._overriddenNodes.length; loop < length; loop++ ) {
			this.overriddenNodes.push( this._overriddenNodes[loop].cloneNode( true ) );
		}

		this._pipeline.buildWidgets( this._pipeline.inspect( this ), this );
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

		if ( key == "readOnly" ) {
			this.readOnly = value;
		}

		this._super( key, value );
	},

	/**
	 * Inspect the given toInspect/path and build widgets.
	 */

	buildWidgets: function( toInspect, path ) {

		if ( toInspect ) {
			this.toInspect = toInspect;
			this.path = '';
		}

		if ( path ) {
			this.path = path;
		}

		this._refresh();
	},

	getWidgetProcessor: function( testInstanceOf ) {

		return this._pipeline.getWidgetProcessor( testInstanceOf );
	},
} );
