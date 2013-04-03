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
	 * @namespace WidgetBuilders.
	 */

	metawidget.widgetbuilder = metawidget.widgetbuilder || {};

	/**
	 * @class Delegates widget building to one or more sub-WidgetBuilders.
	 *        <p>
	 *        Each sub-WidgetBuilder in the list is invoked, in order, calling
	 *        its <code>buildWidget</code> method. The first result is
	 *        returned. If all sub-WidgetBuilders return undefined, undefined is
	 *        returned (the parent Metawidget will generally instantiate a
	 *        nested Metawidget in this case).
	 *        <p>
	 *        Note: the name <em>Composite</em>WidgetBuilder refers to the
	 *        Composite design pattern.
	 */

	metawidget.widgetbuilder.CompositeWidgetBuilder = function( config ) {

		if ( ! ( this instanceof metawidget.widgetbuilder.CompositeWidgetBuilder ) ) {
			throw new Error( 'Constructor called as a function' );
		}

		var _widgetBuilders;

		if ( config.widgetBuilders !== undefined ) {
			_widgetBuilders = config.widgetBuilders.slice( 0 );
		} else {
			_widgetBuilders = config.slice( 0 );
		}

		this.onStartBuild = function() {

			for ( var loop = 0, length = _widgetBuilders.length; loop < length; loop++ ) {

				var widgetBuilder = _widgetBuilders[loop];

				if ( widgetBuilder.onStartBuild !== undefined ) {
					widgetBuilder.onStartBuild();
				}
			}
		};

		this.buildWidget = function( elementName, attributes, mw ) {

			for ( var loop = 0, length = _widgetBuilders.length; loop < length; loop++ ) {

				var widget;
				var widgetBuilder = _widgetBuilders[loop];

				if ( widgetBuilder.buildWidget !== undefined ) {
					widget = widgetBuilder.buildWidget( elementName, attributes, mw );
				} else {
					widget = widgetBuilder( elementName, attributes, mw );
				}

				if ( widget !== undefined ) {
					return widget;
				}
			}
		};

		this.onEndBuild = function() {

			for ( var loop = 0, length = _widgetBuilders.length; loop < length; loop++ ) {

				var widgetBuilder = _widgetBuilders[loop];

				if ( widgetBuilder.onEndBuild !== undefined ) {
					widgetBuilder.onEndBuild();
				}
			}
		};
	};

	/**
	 * @class WidgetBuilder to override widgets based on
	 *        <tt>mw.overriddenNodes</tt>.
	 *        <p>
	 *        Widgets are overridden based on id, not name, because name is not
	 *        legal syntax for many nodes (e.g. <tt>table</tt>).
	 */

	metawidget.widgetbuilder.OverriddenWidgetBuilder = function() {

		if ( ! ( this instanceof metawidget.widgetbuilder.OverriddenWidgetBuilder ) ) {
			throw new Error( 'Constructor called as a function' );
		}
	};

	metawidget.widgetbuilder.OverriddenWidgetBuilder.prototype.buildWidget = function( elementName, attributes, mw ) {

		if ( mw.overriddenNodes === undefined ) {
			return;
		}

		var overrideId = metawidget.util.getId( elementName, attributes, mw );

		for ( var loop = 0, length = mw.overriddenNodes.length; loop < length; loop++ ) {

			var child = mw.overriddenNodes[loop];
			if ( child.nodeType === 1 && child.getAttribute( 'id' ) === overrideId ) {
				child.overridden = true;
				mw.overriddenNodes.splice( loop, 1 );
				return child;
			}
		}
	};

	/**
	 * @class WidgetBuilder for read-only widgets in HTML 5 environments.
	 */

	metawidget.widgetbuilder.ReadOnlyWidgetBuilder = function() {

		if ( ! ( this instanceof metawidget.widgetbuilder.ReadOnlyWidgetBuilder ) ) {
			throw new Error( 'Constructor called as a function' );
		}
	};

	metawidget.widgetbuilder.ReadOnlyWidgetBuilder.prototype.buildWidget = function( elementName, attributes, mw ) {

		// Not read-only?

		if ( attributes.readOnly !== 'true' ) {
			return;
		}

		// Hidden

		if ( attributes.hidden === 'true' || attributes.type === 'function' ) {
			return document.createElement( 'stub' );
		}

		if ( attributes['enum'] !== undefined || attributes.type === 'string' || attributes.type === 'boolean' || attributes.type === 'number' || attributes.type === 'date' ) {

			if ( attributes.masked === 'true' ) {

				// Masked (return a couple of nested Stubs, so that we DO still
				// render a label)

				var stub = document.createElement( 'stub' );
				stub.appendChild( document.createElement( 'stub' ) );
				return stub;
			}

			return document.createElement( 'output' );
		}

		// Not simple, but don't expand

		if ( attributes.dontExpand === 'true' ) {
			return document.createElement( 'output' );
		}
	};

	/**
	 * WidgetBuilder for pure JavaScript environments.
	 * <p>
	 * Creates native HTML 5 widgets, such as <code>input</code> and
	 * <code>select</code>, to suit the inspected fields.
	 */

	metawidget.widgetbuilder.HtmlWidgetBuilder = function( config ) {

		if ( ! ( this instanceof metawidget.widgetbuilder.HtmlWidgetBuilder ) ) {
			throw new Error( 'Constructor called as a function' );
		}

		var _buttonStyleClass = config !== undefined ? config.buttonStyleClass : undefined;

		this.buildWidget = function( elementName, attributes, mw ) {

			// Hidden

			if ( attributes.hidden === 'true' ) {
				return document.createElement( 'stub' );
			}

			// Select box

			if ( attributes['enum'] !== undefined ) {

				// Multi-select and radio buttons

				if ( attributes.type === 'array' || attributes.componentType !== undefined ) {

					var div = document.createElement( 'div' );

					for ( var loop = 0, length = attributes['enum'].length; loop < length; loop++ ) {

						// Uses 'implicit label association':
						// http://www.w3.org/TR/html4/interact/forms.html#h-17.9.1

						var label = document.createElement( 'label' );
						var option = document.createElement( 'input' );

						if ( attributes.componentType !== undefined ) {
							option.setAttribute( 'type', attributes.componentType );
						} else {
							option.setAttribute( 'type', 'checkbox' );
						}
						option.setAttribute( 'value', attributes['enum'][loop] );
						label.appendChild( option );

						if ( attributes.enumTitles !== undefined && attributes.enumTitles[loop] !== undefined ) {
							label.appendChild( document.createTextNode( attributes.enumTitles[loop] ) );
						} else {
							label.appendChild( document.createTextNode( attributes['enum'][loop] ) );
						}

						div.appendChild( label );
					}

					return div;
				}

				// Single-select

				var select = document.createElement( 'select' );

				if ( attributes.required === undefined || attributes.required === 'false' ) {
					select.appendChild( document.createElement( 'option' ) );
				}

				for ( var loop = 0, length = attributes['enum'].length; loop < length; loop++ ) {
					var option = document.createElement( 'option' );

					// HtmlUnit needs an 'option' to have a 'value', even if the
					// same as the innerHTML

					option.setAttribute( 'value', attributes['enum'][loop] );

					if ( attributes.enumTitles !== undefined && attributes.enumTitles[loop] !== undefined ) {
						option.innerHTML = attributes.enumTitles[loop];
					} else {
						option.innerHTML = attributes['enum'][loop];
					}

					select.appendChild( option );
				}
				return select;
			}

			// Button

			if ( attributes.type === 'function' ) {
				var button = document.createElement( 'button' );

				button.innerHTML = metawidget.util.getLabelString( attributes, mw );				

				if ( _buttonStyleClass !== undefined ) {
					button.setAttribute( 'class', _buttonStyleClass );
				}
				return button;
			}

			// Number

			if ( attributes.type === 'number' ) {

				if ( attributes.minimum !== undefined && attributes.maximum !== undefined ) {
					var range = document.createElement( 'input' );
					range.setAttribute( 'type', 'range' );
					range.setAttribute( 'min', attributes.minimum );
					range.setAttribute( 'max', attributes.maximum );
					return range;
				}

				var number = document.createElement( 'input' );
				number.setAttribute( 'type', 'number' );
				return number;
			}

			// Boolean

			if ( attributes.type === 'boolean' ) {
				var checkbox = document.createElement( 'input' );
				checkbox.setAttribute( 'type', 'checkbox' );
				return checkbox;
			}

			// Date

			if ( attributes.type === 'date' ) {
				var date = document.createElement( 'input' );
				date.setAttribute( 'type', 'date' );
				return date;
			}

			// String

			if ( attributes.type === 'string' ) {

				if ( attributes.masked === 'true' ) {
					var password = document.createElement( 'input' );
					password.setAttribute( 'type', 'password' );

					if ( attributes.maxLength !== undefined ) {
						password.setAttribute( 'maxlength', attributes.maxLength );
					}

					return password;
				}

				if ( attributes.large === 'true' ) {
					return document.createElement( 'textarea' );
				}

				var text = document.createElement( 'input' );
				text.setAttribute( 'type', 'text' );

				if ( attributes.maxLength !== undefined ) {
					text.setAttribute( 'maxlength', attributes.maxLength );
				}

				return text;
			}

			// Not simple, but don't expand

			if ( attributes.dontExpand === 'true' ) {
				var text = document.createElement( 'input' );
				text.setAttribute( 'type', 'text' );
				return text;
			}
		};
	};
} )();