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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

var metawidget = ( typeof window !== 'undefined' ? window.metawidget : undefined ) || metawidget || {};

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

		if ( !metawidget.util.hasAttribute( widget, 'id' )) {
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

	metawidget.widgetprocessor.RequiredAttributeProcessor.prototype.processWidget = function( widget, elementName, attributes ) {

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

	metawidget.widgetprocessor.PlaceholderAttributeProcessor.prototype.processWidget = function( widget, elementName, attributes ) {

		if ( attributes.placeholder !== undefined ) {
			widget.setAttribute( 'placeholder', attributes.placeholder );
		}

		return widget;
	};

	/**
	 * @class WidgetProcessor that sets the HTML 'disabled' attribute.
	 */

	metawidget.widgetprocessor.DisabledAttributeProcessor = function() {

		if ( ! ( this instanceof metawidget.widgetprocessor.DisabledAttributeProcessor ) ) {
			throw new Error( 'Constructor called as a function' );
		}
	};

	metawidget.widgetprocessor.DisabledAttributeProcessor.prototype.processWidget = function( widget, elementName, attributes ) {

		if ( metawidget.util.isTrueOrTrueString( attributes.disabled ) ) {
			widget.setAttribute( 'disabled', 'disabled' );
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

		var typeAndNames = metawidget.util.splitPath( mw.path );

		if ( widget.tagName === 'INPUT' && ( widget.getAttribute( 'type' ) === 'button' || widget.getAttribute( 'type' ) === 'submit' ) ) {
			widget.onclick = function() {

				try {
					return metawidget.util.traversePath( mw.toInspect, typeAndNames.names )[attributes.name]();
				} catch ( e ) {
					if ( alert !== undefined ) {
						alert( e );
					} else {
						throw e;
					}
				}
			};

			return widget;
		}

		var value;

		if ( elementName === 'entity' ) {

			value = metawidget.util.traversePath( mw.toInspect, typeAndNames.names );

			if ( typeAndNames.names === undefined ) {
				mw._simpleBindingProcessor.topLevel = true;
			} else {
				mw._simpleBindingProcessor.topLevelWithPath = true;
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

		if ( rememberBinding === true || widget.getMetawidget !== undefined || widget.nestedMetawidgets !== undefined ) {
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

		if ( isBindable === true && metawidget.util.hasAttribute( widget, 'id' )) {

			// Standard HTML needs 'name', not 'id', for binding

			widget.setAttribute( 'name', widget.getAttribute( 'id' ) );
		}

		var loop, length;

		// Special support for arrays of checkboxes/radio buttons

		if ( attributes['enum'] !== undefined && widget.tagName === 'DIV' ) {

			if ( attributes.type === 'array' || attributes.componentType !== undefined ) {

				isBindable = true;

				length = widget.childNodes.length;
				for ( loop = 0; loop < length; loop++ ) {
					var childNode = widget.childNodes[loop];
					if ( childNode.tagName === 'DIV' ) {
						childNode = childNode.childNodes[0];
					}					
					if ( childNode.tagName === 'LABEL' ) {
						var inputChildNode = childNode.childNodes[0];
						if ( inputChildNode.tagName === 'INPUT' ) {

							// Name must be common across group

							inputChildNode.setAttribute( 'name', widget.getAttribute( 'id' ) );

							if ( attributes.type === 'array' ) {
								inputChildNode.checked = ( value !== undefined && value.indexOf( inputChildNode.value ) !== -1 );
							} else if ( attributes.type === 'boolean' ) {
								inputChildNode.checked = ( value === inputChildNode.value || inputChildNode.value === '' + value );
							} else {
								inputChildNode.checked = ( value === inputChildNode.value );
							}
						}
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

				if ( metawidget.util.isTrueOrTrueString( attributes.masked )) {

					// Special support for masked output

					widget.textContent = metawidget.util.fillString( '*', value.length );

				} else if ( attributes.enumTitles !== undefined ) {

					// Special support for enumTitles

					if ( attributes.type === 'array' ) {

						length = value.length;
						for ( loop = 0; loop < length; loop++ ) {

							if ( loop === 0 ) {
								widget.textContent = '';
							} else {
								widget.textContent += ', ';
							}

							widget.textContent += metawidget.util.lookupEnumTitle( value[loop], attributes['enum'], attributes.enumTitles );
						}

					} else {
						widget.textContent = metawidget.util.lookupEnumTitle( value, attributes['enum'], attributes.enumTitles );
					}

				} else if ( attributes.type === 'boolean' ) {

					// Special support for boolean

					if ( value === true ) {
						widget.textContent = metawidget.util.getLocalizedString( 'Yes', mw );
					} else if ( value === false ) {
						widget.textContent = metawidget.util.getLocalizedString( 'No', mw );
					} else {
						widget.textContent = value;
					}

				} else {
					widget.textContent = value;
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
	 * @return true if data (including data from nested Metawidgets) was
	 *         actually changed. False otherwise. Can be useful for 'dirty'
	 *         flags
	 */

	metawidget.widgetprocessor.SimpleBindingProcessor.prototype.save = function( mw ) {

		var toInspect;
		var dirty = false;

		// Traverse to the parent...

		var typeAndNames = metawidget.util.splitPath( mw.path );

		if ( typeAndNames.names === undefined ) {
			toInspect = mw.toInspect;
		} else {
			var namesToParent = typeAndNames.names.slice( 0, typeAndNames.names.length - 1 );
			var parent = metawidget.util.traversePath( mw.toInspect, namesToParent );

			// ...then to the child...

			if ( mw._simpleBindingProcessor.topLevelWithPath === true ) {
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
			var widgetFromBinding = this.getWidgetFromBinding( binding, mw );

			// Support nested Metawidgets

			if ( widgetFromBinding.getMetawidget !== undefined ) {
				var nestedDirty = this.save( widgetFromBinding.getMetawidget() );
				
				if ( nestedDirty === true ) {
					dirty = true;
				}
				
				continue;
			}

			// saveFromWidget

			var value = this.saveFromWidget( binding, mw );

			if ( dirty === false && toInspect[name] !== value ) {
				dirty = true;
			}

			if ( mw._simpleBindingProcessor.topLevel === true ) {
				mw.toInspect = value;
				return dirty;
			}

			toInspect[name] = value;
		}

		// Support alwaysUseNestedMetawidgetInTables

		if ( mw.nestedMetawidgets !== undefined ) {

			for ( var loop = 0, length = mw.nestedMetawidgets.length; loop < length; loop++ ) {
				nestedDirty = this.save( mw.nestedMetawidgets[loop].getMetawidget() );
				
				if ( nestedDirty === true ) {
					dirty = true;
				}
			}
		}

		return dirty;
	};

	/**
	 * @return the given binding's widget value
	 */

	metawidget.widgetprocessor.SimpleBindingProcessor.prototype.saveFromWidget = function( binding, mw ) {

		var widget = this.getWidgetFromBinding( binding, mw );

		if ( widget.getAttribute( 'type' ) === 'checkbox' ) {
			return widget.checked;
		}

		if ( binding.attributes.type === 'integer' ) {

			var parsed = parseInt( widget.value );

			// Avoid pushing back 'NaN' (note: can't use Number.isNaN, is ES6 only)

			if ( isNaN( parsed ) ) {
				return undefined;
			}

			return parsed;
		}

		if ( binding.attributes.type === 'number' ) {

			// parseFloat can parse ints, but parseInt can't parse floats

			parsed = parseFloat( widget.value );

			// Avoid pushing back 'NaN' (note: can't use Number.isNaN, is ES6 only)

			if ( isNaN( parsed ) ) {
				return undefined;
			}

			return parsed;
		}

		// Support arrays of checkboxes/radio buttons

		if ( binding.attributes['enum'] !== undefined && widget.tagName === 'DIV' ) {

			if ( binding.attributes.type === 'array' || binding.attributes.componentType !== undefined ) {

				var toReturn;
				for ( var loop = 0, length = widget.childNodes.length; loop < length; loop++ ) {
					var childNode = widget.childNodes[loop];
					if ( childNode.tagName === 'DIV' ) {
						childNode = childNode.childNodes[0];
					}					
					if ( childNode.tagName === 'LABEL' ) {
						var inputChildNode = childNode.childNodes[0];
						if ( inputChildNode.checked ) {

							if ( binding.attributes.type === 'boolean' ) {
								return ( inputChildNode.value === true || inputChildNode.value === 'true' );
							}

							if ( binding.attributes.type !== 'array' ) {
								return inputChildNode.value;
							}

							toReturn = toReturn || [];
							toReturn.push( inputChildNode.value );
						}
					}
				}
				return toReturn;
			}
		}

		// Support non-checkbox booleans (e.g. a select box)

		if ( binding.attributes.type === 'boolean' ) {
			return ( widget.value === true || widget.value === 'true' );
		}

		// Avoid pushing back 'null'

		if ( widget.value === '' || widget.value === null ) {
			return;
		}

		return widget.value;
	};

	/**
	 * Returns the widget associated with the given binding. By default, calls
	 * <tt>binding.widget</tt>. Subclasses may override this method if their
	 * framework has swapped out the widget.
	 */

	metawidget.widgetprocessor.SimpleBindingProcessor.prototype.getWidgetFromBinding = function( binding /*, mw */ ) {

		return binding.widget;
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
			var widgetFromBinding = this.getWidgetFromBinding( binding, mw );

			if ( widgetFromBinding.getMetawidget !== undefined ) {
				this.reload( reloadFrom, widgetFromBinding.getMetawidget() );
				continue;
			}

			// Use id, not name, to support arrays of checkboxes (id should be
			// the same as name anyway)

			this.bindToWidget( widgetFromBinding, reloadFrom[widgetFromBinding.getAttribute( 'id' )], binding.elementName, binding.attributes, mw );
		}
	};

} )();
