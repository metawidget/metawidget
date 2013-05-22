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

var metawidget = metawidget || {};

( function() {

	'use strict';

	/**
	 * @namespace WidgetProcessors.
	 */

	metawidget.widgetprocessor = metawidget.widgetprocessor || {};

	/**
	 * @class WidgetProcessor that sets the HTML 'id' attribute.
	 */

	metawidget.widgetprocessor.IdProcessor = function() {

		if ( ! ( this instanceof metawidget.widgetprocessor.IdProcessor ) ) {
			throw new Error( 'Constructor called as a function' );
		}
	};

	metawidget.widgetprocessor.IdProcessor.prototype.processWidget = function( widget, elementName, attributes, mw ) {

		// Dangerous to reassign an id. For example, some JQuery UI widgets
		// assign temporary ids when they wrap widgets

		if ( !widget.hasAttribute( 'id' ) ) {
			var id = metawidget.util.getId( elementName, attributes, mw );

			if ( id !== undefined ) {
				widget.setAttribute( 'id', id );
			}
		}

		return widget;
	};

	/**
	 * @class WidgetProcessor that sets the HTML 5 'required' attribute.
	 */

	metawidget.widgetprocessor.RequiredAttributeProcessor = function() {

		if ( ! ( this instanceof metawidget.widgetprocessor.RequiredAttributeProcessor ) ) {
			throw new Error( 'Constructor called as a function' );
		}
	};

	metawidget.widgetprocessor.RequiredAttributeProcessor.prototype.processWidget = function( widget, elementName, attributes, mw ) {

		if ( metawidget.util.isTrueOrTrueString( attributes.required ) ) {
			widget.setAttribute( 'required', 'required' );
		}

		return widget;
	};

	/**
	 * @class Simple data/action binding implementation. Frameworks that supply
	 *        their own data-binding mechanisms (such as Angular JS) should
	 *        override this with their own WidgetProcessor.
	 */

	metawidget.widgetprocessor.SimpleBindingProcessor = function() {

		if ( ! ( this instanceof metawidget.widgetprocessor.SimpleBindingProcessor ) ) {
			throw new Error( 'Constructor called as a function' );
		}
	};

	metawidget.widgetprocessor.SimpleBindingProcessor.prototype.onStartBuild = function( mw ) {

		mw._simpleBindingProcessorBindings = {};
	};

	metawidget.widgetprocessor.SimpleBindingProcessor.prototype.processWidget = function( widget, elementName, attributes, mw ) {

		if ( widget.tagName === 'BUTTON' ) {
			widget.onclick = function() {

				try {
					return mw.toInspect[attributes.name]();
				} catch ( e ) {
					alert( e );
				}
			};
		} else {

			var value;
			var typeAndNames = metawidget.util.splitPath( mw.path );
			var toInspect = metawidget.util.traversePath( mw.toInspect, typeAndNames.names );

			if ( elementName !== 'entity' && toInspect !== undefined ) {
				value = toInspect[attributes.name];
			} else {
				value = toInspect;
			}

			var isBindable = ( widget.tagName === 'INPUT' || widget.tagName === 'SELECT' || widget.tagName === 'TEXTAREA' );

			if ( isBindable === true && widget.hasAttribute( 'id' ) ) {

				// Standard HTML works off 'name', not 'id', for binding

				widget.setAttribute( 'name', widget.getAttribute( 'id' ) );
			}

			// Check 'not undefined', rather than 'if value', in case value is a
			// boolean of false
			//
			// Note: this is a general convention throughout Metawidget, as
			// JavaScript has a surprisingly large number of 'falsy' values)

			if ( value !== undefined ) {
				if ( widget.tagName === 'OUTPUT' || widget.tagName === 'TEXTAREA' ) {

					widget.innerHTML = value;

					// Special support for enumTitles

					if ( attributes.enumTitles !== undefined ) {
						var indexOf = attributes.enum.indexOf( value );

						if ( indexOf !== -1 ) {
							widget.innerHTML = attributes.enumTitles[indexOf];
						}
					}
				} else if ( widget.tagName === 'INPUT' && widget.getAttribute( 'type' ) === 'checkbox' ) {
					widget.checked = value;
				} else if ( isBindable === true ) {
					widget.value = value;
				}
			}

			if ( isBindable === true || widget.metawidget !== undefined ) {
				mw._simpleBindingProcessorBindings[attributes.name] = widget;
			}
		}

		return widget;
	};

	/**
	 * Save the bindings associated with the given Metawidget.
	 */

	metawidget.widgetprocessor.SimpleBindingProcessor.prototype.save = function( mw ) {

		var typeAndNames = metawidget.util.splitPath( mw.path );
		var toInspect = metawidget.util.traversePath( mw.toInspect, typeAndNames.names );

		for ( var name in mw._simpleBindingProcessorBindings ) {

			var widget = mw._simpleBindingProcessorBindings[name];

			if ( widget.metawidget !== undefined ) {
				this.save( widget.metawidget );
				continue;
			}

			if ( widget.getAttribute( 'type' ) === 'checkbox' ) {
				toInspect[name] = widget.checked;
				continue;
			}

			toInspect[name] = widget.value;
		}
	};
} )();