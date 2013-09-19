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
	 * @class WidgetProcessor that sets the HTML 5 'placeholder' attribute.
	 */

	metawidget.widgetprocessor.PlaceholderAttributeProcessor = function() {

		if ( ! ( this instanceof metawidget.widgetprocessor.PlaceholderAttributeProcessor ) ) {
			throw new Error( 'Constructor called as a function' );
		}
	};

	metawidget.widgetprocessor.PlaceholderAttributeProcessor.prototype.processWidget = function( widget, elementName, attributes, mw ) {

		if ( attributes.placeholder !== undefined ) {
			widget.setAttribute( 'placeholder', attributes.placeholder );
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

		mw._simpleBindingProcessor = {};
	};

	metawidget.widgetprocessor.SimpleBindingProcessor.prototype.processWidget = function( widget, elementName, attributes, mw ) {

		if ( widget.tagName === 'BUTTON' ) {
			widget.onclick = function() {

				try {
					return mw.toInspect[attributes.name]();
				} catch ( e ) {
					if ( alert ) {
						alert( e );
					} else {
						throw e;
					}
				}
			};

			return widget;
		}

		var value;
		var typeAndNames = metawidget.util.splitPath( mw.path );

		if ( elementName === 'entity' ) {

			value = metawidget.util.traversePath( mw.toInspect, typeAndNames.names );

			// We cannot typically save to a top-level value. However if
			// we are using a path, we can navigate to its parent

			if ( typeAndNames.names !== undefined ) {
				elementName = 'property';
				mw._simpleBindingProcessor.topLevel = true;
			}
		} else {
			var toInspect = metawidget.util.traversePath( mw.toInspect, typeAndNames.names );

			if ( toInspect !== undefined ) {
				value = toInspect[attributes.name];
			} else {
				value = undefined;
			}
		}

		var rememberBinding = this.bindToWidget( widget, value, elementName, attributes, mw );

		if ( elementName !== 'entity' && ( rememberBinding === true || widget.getMetawidget !== undefined ) ) {
			mw._simpleBindingProcessor.bindings = mw._simpleBindingProcessor.bindings || [];
			mw._simpleBindingProcessor.bindings[attributes.name] = {
				widget: widget,
				elementName: elementName,
				attributes: attributes
			};
		}

		return widget;
	};

	/**
	 * Bind the given widget to the given value.
	 * 
	 * @return true if this binding should be remembered for when the user calls
	 *         'save'
	 */

	metawidget.widgetprocessor.SimpleBindingProcessor.prototype.bindToWidget = function( widget, value, elementName, attributes, mw ) {

		var isBindable = ( widget.tagName === 'INPUT' || widget.tagName === 'SELECT' || widget.tagName === 'TEXTAREA' );

		if ( isBindable === true && widget.hasAttribute( 'id' ) ) {

			// Standard HTML needs 'name', not 'id', for binding

			widget.setAttribute( 'name', widget.getAttribute( 'id' ) );
		}
		
		// Special support for arrays of checkboxes
		// TODO: divs of radio buttons?
		
		if ( attributes.type === 'array' && attributes['enum'] !== undefined && widget.tagName === 'DIV' ) {
			
			isBindable = true;
			
			for( var loop = 0, length = widget.childNodes.length; loop < length; loop++ ) {
				var childNode = widget.childNodes[loop];
				if ( childNode.tagName === 'LABEL' ) {
					var labelChildNode = childNode.childNodes[0];
					if ( labelChildNode.tagName === 'INPUT' && attributes['enum'].valueOf( labelChildNode.value ) !== -1 ) {
						labelChildNode.setAttribute( 'name', widget.getAttribute( 'id' ) );
						labelChildNode.checked = ( value !== undefined && value.indexOf( labelChildNode.value ) !== -1 );
					}
				}
			}			
		}

		// Check 'not undefined', rather than 'if value', in case value is a
		// boolean of false
		//
		// Note: this is a general convention throughout Metawidget, as
		// JavaScript has a surprisingly large number of 'falsy' values)

		if ( value !== undefined ) {
			if ( widget.tagName === 'OUTPUT' || widget.tagName === 'TEXTAREA' ) {

				if ( attributes.masked === true ) {

					// Special support for masked output

					widget.innerHTML = metawidget.util.fillString( '*', value.length );

				} else if ( attributes.enumTitles !== undefined ) {

					// Special support for enumTitles

					widget.innerHTML = metawidget.util.lookupEnumTitle( value, attributes['enum'], attributes.enumTitles );

				} else if ( attributes.type === 'boolean' ) {

					// Special support for boolean

					if ( value === true ) {
						widget.innerHTML = metawidget.util.getLocalizedString( 'Yes', mw );
					} else if ( value === false ) {
						widget.innerHTML = metawidget.util.getLocalizedString( 'No', mw );
					} else {
						widget.innerHTML = value;
					}

				} else {
					widget.innerHTML = value;
				}

			} else if ( widget.tagName === 'INPUT' && widget.getAttribute( 'type' ) === 'checkbox' ) {
				widget.checked = value;
			} else if ( isBindable === true ) {
				widget.value = value;
			}
		}

		return isBindable;
	};

	/**
	 * Save the bindings associated with the given Metawidget.
	 * 
	 * @return true if data was actually changed. False otherwise. Can be useful
	 *         for 'dirty' flags
	 */

	metawidget.widgetprocessor.SimpleBindingProcessor.prototype.save = function( mw ) {

		var toInspect;
		var dirty = false;

		// Traverse to the parent...

		var typeAndNames = metawidget.util.splitPath( mw.path );

		if ( typeAndNames.names === undefined ) {
			toInspect = metawidget.util.traversePath( mw.toInspect );
		} else {
			var namesToParent = typeAndNames.names.slice( 0, typeAndNames.names.length - 1 );
			var parent = metawidget.util.traversePath( mw.toInspect, namesToParent );

			// ...then to the child...

			if ( mw._simpleBindingProcessor.topLevel === true ) {
				toInspect = parent;
			} else {
				var childName = typeAndNames.names[typeAndNames.names.length - 1];
				toInspect = parent[childName];

				// ...create the child 'just in time' if necessary...

				if ( toInspect === undefined ) {
					toInspect = {};
					parent[childName] = toInspect;
				}
			}
		}

		// ...and populate it

		for ( var name in mw._simpleBindingProcessor.bindings ) {

			var binding = mw._simpleBindingProcessor.bindings[name];

			if ( binding.widget.getMetawidget !== undefined ) {
				this.save( binding.widget.getMetawidget() );
				continue;
			}

			var value = this.saveFromWidget( binding );

			if ( dirty === false && toInspect[name] !== value ) {
				dirty = true;
			}

			toInspect[name] = value;
		}

		return dirty;
	};

	/**
	 * @return the given binding's widget value
	 */

	metawidget.widgetprocessor.SimpleBindingProcessor.prototype.saveFromWidget = function( binding ) {

		if ( binding.widget.getAttribute( 'type' ) === 'checkbox' ) {
			return binding.widget.checked;
		}

		if ( binding.attributes.type === 'number' ) {

			// parseFloat can parse ints, but parseInt can't parse floats

			var parsed = parseFloat( binding.widget.value );

			// Avoid pushing back 'NaN'

			if ( isNaN( parsed ) ) {
				return undefined;
			}

			return parsed;
		}

		// Support non-checkbox booleans
		
		if ( binding.attributes.type === 'boolean' ) {
			return ( binding.widget.value === true || binding.widget.value === 'true' );
		}

		// Support arrays of checkboxes
		
		if ( binding.attributes.type === 'array' ) {
			var toReturn = [];
			for( var loop = 0, length = binding.widget.childNodes.length; loop < length; loop++ ) {
				var childNode = binding.widget.childNodes[loop];
				if ( childNode.tagName === 'LABEL' ) {
					var labelChildNode = childNode.childNodes[0];
					if ( labelChildNode.checked ) {
						toReturn.push( labelChildNode.value );
					}
				}
			}
			return toReturn;
		}
		
		// Avoid pushing back 'null'

		if ( binding.widget.value === '' || binding.widget.value === null ) {
			return;
		}

		return binding.widget.value;
	};

	/**
	 * Reloads the values in the widgets using the values in the given Object.
	 * The names of the values in the Object must match the 'name' attribute of
	 * the widget.
	 * <p>
	 * Note this method does not update <tt>mw.toInspect</tt>, nor does it
	 * save any values back from the widgets. It can be useful for re-populating
	 * the widgets based on an HTTP request POST-back.
	 */

	metawidget.widgetprocessor.SimpleBindingProcessor.prototype.reload = function( reloadFrom, mw ) {

		for ( var name in mw._simpleBindingProcessor.bindings ) {

			var binding = mw._simpleBindingProcessor.bindings[name];

			if ( binding.widget.getMetawidget !== undefined ) {
				this.reload( reloadFrom, binding.widget.getMetawidget() );
				continue;
			}

			this.bindToWidget( binding.widget, reloadFrom[binding.widget.getAttribute( 'name' )], binding.elementName, binding.attributes, mw );
		}
	};

} )();
