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

$.widget( "custom.metawidget", {

	/**
	 * Default configuration
	 */

	options: {
		readOnly: false,
		inspector: new metawidget.inspector.PropertyTypeInspector(),
		widgetBuilder: new metawidget.widgetbuilder.CompositeWidgetBuilder( [ new metawidget.widgetbuilder.OverriddenWidgetBuilder(), new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(),
				new metawidget.widgetbuilder.HtmlWidgetBuilder() ] ),
		widgetProcessors: [ new metawidget.widgetprocessor.IdProcessor(), new metawidget.widgetprocessor.RequiredAttributeProcessor(), new metawidget.widgetprocessor.SimpleBindingProcessor() ],
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
			
			var nestedToInspect = {};

			if ( mw.toInspect ) {
				nestedToInspect = mw.toInspect[attributes.name];
			}

			var nestedPath = attributes.name;
			
			if ( mw.path ) {
				nestedPath = mw.path + '.' + nestedPath;
			}

			// Attach ourselves as a property of the tag, rather than try to
			// 'extend' the built-in HTML tags

			nestedWidget.metawidget = nestedMetawidget.metawidget( "getThis" )[0];
			
			nestedMetawidget.metawidget( "buildWidgets", nestedToInspect, nestedPath );
			return nestedWidget;
		};

		// Configure defaults

		this._pipeline.configure( this.options );

		// First time in, capture the contents of the Metawidget (if any). Do not
		// actually 'removeChild' yet, so that we can still use
		// 'document.getElementById'

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
		// TODO: not DRY with corejs?

		this.overriddenNodes = [];

		for ( var loop = 0, length = this._overriddenNodes.length; loop < length; loop++ ) {
			this.overriddenNodes.push( this._overriddenNodes[loop].cloneNode( true ) );
		}
		
		this._pipeline.buildWidgets( this );
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

	getThis: function() {

		// TODO: jQuery appears to wrap 'this' into something else. How to stop this?
		
		return [ this ];
	},
} );
