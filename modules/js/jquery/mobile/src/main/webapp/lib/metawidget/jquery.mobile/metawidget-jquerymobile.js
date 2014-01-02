// Metawidget ${project.version}
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

var metawidget = metawidget || {};

( function() {

	'use strict';

	/**
	 * @namespace Metawidget for JQuery Mobile environments.
	 */

	metawidget.jquerymobile = metawidget.jquerymobile || {};

	/**
	 * @namespace JQuery Mobile WidgetProcessors.
	 */

	metawidget.jquerymobile.widgetprocessor = metawidget.jquerymobile.widgetprocessor || {};

	/**
	 * @class adapts to JQuery Mobile-specific syntax.
	 */

	metawidget.jquerymobile.widgetprocessor.JQueryMobileWidgetProcessor = function() {

		if ( !( this instanceof metawidget.jquerymobile.widgetprocessor.JQueryMobileWidgetProcessor ) ) {
			throw new Error( "Constructor called as a function" );
		}
	};

	metawidget.jquerymobile.widgetprocessor.JQueryMobileWidgetProcessor.prototype.processWidget = function( widget, elementName, attributes, mw ) {

		// JQuery Mobile has a special syntax for arrays

		if ( widget.tagName === 'DIV' && attributes.type === 'array' ) {

			var fieldset = metawidget.util.createElement( mw, 'fieldset' );
			fieldset.setAttribute( 'data-role', 'controlgroup' );

			while ( widget.childNodes.length > 0 ) {
				var label = widget.childNodes[0];
				var id = widget.getAttribute( 'id' ) + widget.childNodes.length;
				label.setAttribute( 'for', id );
				var input = label.childNodes[0];
				input.setAttribute( 'id', id );

				fieldset.appendChild( input );
				fieldset.appendChild( label );
			}

			widget = fieldset;
		}

		return widget;
	};

	metawidget.jquerymobile.widgetprocessor.JQueryMobileSimpleBindingProcessor = function() {

		if ( !( this instanceof metawidget.jquerymobile.widgetprocessor.JQueryMobileSimpleBindingProcessor ) ) {
			throw new Error( "Constructor called as a function" );
		}

		var processor = new metawidget.widgetprocessor.SimpleBindingProcessor();

		// Overridden because some JQuery Mobile widgets (such as search inputs)
		// swap out the existing DOM. We can resolve this using JQuery more
		// safely then with pure JavaScript, because we can find *within* a node

		processor.getWidgetFromBinding = function( binding, mw ) {

			if ( binding.widget.getAttribute( 'type' ) === 'search' ) {
				return $( mw.getElement() ).find( '#' + binding.widget.getAttribute( 'id' ) )[0];
			}

			// Try not to use a DOM search, because mobile is very performance
			// sensitive

			return binding.widget;
		};

		return processor;
	};

	/**
	 * JQuery Mobile WidgetFactory-based Metawidget.
	 */

	$.widget( "mobile.metawidget", {

		/**
		 * Default configuration
		 */

		options: {
			inspector: new metawidget.inspector.PropertyTypeInspector(),
			widgetBuilder: new metawidget.widgetbuilder.CompositeWidgetBuilder( [ new metawidget.widgetbuilder.OverriddenWidgetBuilder(), new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(),
					new metawidget.widgetbuilder.HtmlWidgetBuilder() ] ),
			widgetProcessors: [ new metawidget.widgetprocessor.IdProcessor(), new metawidget.widgetprocessor.RequiredAttributeProcessor(),
					new metawidget.widgetprocessor.PlaceholderAttributeProcessor(), new metawidget.widgetprocessor.DisabledAttributeProcessor(),
					new metawidget.jquerymobile.widgetprocessor.JQueryMobileSimpleBindingProcessor(), new metawidget.jquerymobile.widgetprocessor.JQueryMobileWidgetProcessor() ],
			layout: new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.DivLayout( {
				suppressLabelSuffixOnCheckboxes: true
			} ) )
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

			// JQuery Mobile automatically augments widgets with additional
			// HTML. Clients must call trigger( 'create' ) manually for
			// dynamically created components. This must be done on the widget's
			// container, not the widget itself. However, it cannot be done at
			// the top Metawidget-level, as that will 'double augment' any
			// overridden widgets

			var _superLayoutWidget = this._pipeline.layoutWidget;
			this._pipeline.layoutWidget = function( widget, elementName, attributes, container, mw ) {

				_superLayoutWidget.call( this, widget, elementName, attributes, container, mw );
				if ( widget.overridden === undefined ) {

					var childNodes = container.childNodes;
					var containerNode = childNodes[childNodes.length - 1];

					if ( containerNode === widget ) {

						// Support SimpleLayout

						container.removeChild( widget );
						var wrapper = $( '<span>' ).append( widget );
						container.appendChild( wrapper[0] );
						wrapper.trigger( 'create' );

					} else {

						$( containerNode ).trigger( 'create' );
					}
				}
			};

			// Force a useful convention from JQuery UI that JQuery Mobile
			// doesn't seem to have (yet?)

			this.element.data( 'metawidget', this );

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