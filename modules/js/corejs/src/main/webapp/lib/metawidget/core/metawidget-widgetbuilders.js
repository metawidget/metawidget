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

		function _onStartEndBuild( functionName, mw ) {

			for ( var loop = 0, length = _widgetBuilders.length; loop < length; loop++ ) {

				var widgetBuilder = _widgetBuilders[loop];

				if ( widgetBuilder[functionName] !== undefined ) {
					widgetBuilder[functionName]( mw );
				}
			}
		}

		this.onStartBuild = function( mw ) {

			_onStartEndBuild( 'onStartBuild', mw );
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

		this.onEndBuild = function( mw ) {

			_onStartEndBuild( 'onEndBuild', mw );
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

		if ( !metawidget.util.isTrueOrTrueString( attributes.readOnly ) ) {
			return;
		}

		// Hidden

		if ( metawidget.util.isTrueOrTrueString( attributes.hidden ) || attributes.type === 'function' ) {
			return metawidget.util.createElement( mw, 'stub' );
		}

		if ( attributes['enum'] !== undefined || attributes.type === 'string' || attributes.type === 'boolean' || attributes.type === 'number' || attributes.type === 'date'
				|| attributes.type === 'color' ) {
			return metawidget.util.createElement( mw, 'output' );
		}

		// Not simple, but don't expand

		if ( metawidget.util.isTrueOrTrueString( attributes.dontExpand ) ) {
			return metawidget.util.createElement( mw, 'output' );
		}
	};

	/**
	 * WidgetBuilder for pure JavaScript environments.
	 * <p>
	 * Creates native HTML 5 widgets, such as <code>input</code> and
	 * <code>select</code>, to suit the inspected fields.
	 * <p>
	 * This WidgetBuilder can be configured with the following settings:
	 * <ul>
	 * <li>alwaysUseNestedMetawidgetInTables - by default, HtmlWidgetBuilder
	 * will render simple values in tables as read-only labels. It will only
	 * resort to using nested Metawidgets inside tables if the value is an
	 * object. However, sometimes using a nested Metawidget is the desired
	 * behaviour, even for simple values. Setting this flag forces this</li>
	 * </ul>
	 */

	metawidget.widgetbuilder.HtmlWidgetBuilder = function( config ) {

		if ( ! ( this instanceof metawidget.widgetbuilder.HtmlWidgetBuilder ) ) {
			throw new Error( 'Constructor called as a function' );
		}

		var _alwaysUseNestedMetawidgetInTables = false;

		if ( config !== undefined ) {
			_alwaysUseNestedMetawidgetInTables = config.alwaysUseNestedMetawidgetInTables;
		}

		this.buildWidget = function( elementName, attributes, mw ) {

			// Hidden

			if ( metawidget.util.isTrueOrTrueString( attributes.hidden ) ) {
				return metawidget.util.createElement( mw, 'stub' );
			}

			// Support booleans as radio buttons

			if ( attributes.type === 'boolean' && attributes.componentType === 'radio' && attributes['enum'] === undefined ) {
				attributes['enum'] = [ true, false ];
				attributes['enumTitles'] = [ 'Yes', 'No' ];
			}

			// Select box

			if ( attributes['enum'] !== undefined ) {

				var loop, length, option;

				// Multi-select and radio buttons

				if ( attributes.type === 'array' || attributes.componentType !== undefined ) {

					var div = metawidget.util.createElement( mw, 'div' );
					length = attributes['enum'].length;

					for ( loop = 0; loop < length; loop++ ) {

						// Uses 'implicit label association':
						// http://www.w3.org/TR/html4/interact/forms.html#h-17.9.1

						var label = metawidget.util.createElement( mw, 'label' );
						option = metawidget.util.createElement( mw, 'input' );

						if ( attributes.componentType !== undefined ) {
							label.setAttribute( 'class', attributes.componentType );
							option.setAttribute( 'type', attributes.componentType );
						} else {
							label.setAttribute( 'class', 'checkbox' );
							option.setAttribute( 'type', 'checkbox' );
						}
						option.value = attributes['enum'][loop];
						label.appendChild( option );

						if ( attributes.enumTitles !== undefined && attributes.enumTitles[loop] !== undefined ) {
							label.appendChild( metawidget.util.createTextNode( mw, attributes.enumTitles[loop] ) );
						} else {
							label.appendChild( metawidget.util.createTextNode( mw, attributes['enum'][loop] ) );
						}

						div.appendChild( label );
					}

					return div;
				}

				// Single-select

				var select = metawidget.util.createElement( mw, 'select' );

				if ( !metawidget.util.isTrueOrTrueString( attributes.required ) ) {
					select.appendChild( metawidget.util.createElement( mw, 'option' ) );
				}

				length = attributes['enum'].length;

				for ( loop = 0; loop < length; loop++ ) {
					option = metawidget.util.createElement( mw, 'option' );

					// HtmlUnit needs an 'option' to have a 'value', even if the
					// same as the innerHTML

					option.value = attributes['enum'][loop];

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
				var button = metawidget.util.createElement( mw, 'input' );
				if ( metawidget.util.isTrueOrTrueString( attributes.submit ) ) {
					button.setAttribute( 'type', 'submit' );
				} else {
					button.setAttribute( 'type', 'button' );
				}
				button.setAttribute( 'value', metawidget.util.getLabelString( attributes, mw ) );
				return button;
			}

			// Number

			if ( attributes.type === 'number' ) {

				if ( attributes.minimum !== undefined && attributes.maximum !== undefined ) {
					var range = metawidget.util.createElement( mw, 'input' );
					range.setAttribute( 'type', 'range' );
					range.setAttribute( 'min', attributes.minimum );
					range.setAttribute( 'max', attributes.maximum );
					return range;
				}

				var number = metawidget.util.createElement( mw, 'input' );
				number.setAttribute( 'type', 'number' );

				if ( attributes.minimum !== undefined ) {
					number.setAttribute( 'min', attributes.minimum );
				} else if ( attributes.maximum !== undefined ) {
					number.setAttribute( 'max', attributes.maximum );
				}

				return number;
			}

			// Boolean

			if ( attributes.type === 'boolean' ) {
				var checkbox = metawidget.util.createElement( mw, 'input' );
				checkbox.setAttribute( 'type', 'checkbox' );
				return checkbox;
			}

			// Date

			if ( attributes.type === 'date' ) {
				var date = metawidget.util.createElement( mw, 'input' );
				date.setAttribute( 'type', 'date' );
				return date;
			}

			// Color

			if ( attributes.type === 'color' ) {
				var color = metawidget.util.createElement( mw, 'input' );
				color.setAttribute( 'type', 'color' );
				return color;
			}

			// String

			if ( attributes.type === 'string' ) {

				if ( metawidget.util.isTrueOrTrueString( attributes.masked ) ) {
					var password = metawidget.util.createElement( mw, 'input' );
					password.setAttribute( 'type', 'password' );

					if ( attributes.maxLength !== undefined ) {
						password.setAttribute( 'maxlength', attributes.maxLength );
					}

					return password;
				}

				if ( metawidget.util.isTrueOrTrueString( attributes.large ) ) {
					return metawidget.util.createElement( mw, 'textarea' );
				}

				var input = metawidget.util.createElement( mw, 'input' );

				if ( attributes.componentType !== undefined ) {
					input.setAttribute( 'type', attributes.componentType );
				} else {
					input.setAttribute( 'type', 'text' );
				}

				if ( attributes.maxLength !== undefined ) {
					input.setAttribute( 'maxlength', attributes.maxLength );
				}

				return input;
			}

			// Collection

			if ( attributes.type === 'array' ) {
				return this.createTable( elementName, attributes, mw );
			}

			// Not simple, but don't expand

			if ( metawidget.util.isTrueOrTrueString( attributes.dontExpand ) ) {
				var text = metawidget.util.createElement( mw, 'input' );
				text.setAttribute( 'type', 'text' );
				return text;
			}
		};

		/**
		 * Create a table populated with the contents of an array property.
		 * <p>
		 * Subclasses may override this method to customize table creation.
		 * Alternatively, they could override one of the sub-methods
		 * <tt>addHeaderRow</tt>, <tt>addHeader</tt>, <tt>addRow</tt> or
		 * <tt>addColumn</tt>.
		 */

		this.createTable = function( elementName, attributes, mw ) {

			var table = metawidget.util.createElement( mw, 'table' );

			// Inspect the first entry in the array to determine the table
			// columns. This assumes the array is homogeneous. However because
			// you can use JsonSchemaInspector as one of your Inspectors, it
			// doesn't assume the array is populated, nor that the first entry
			// has values in all fields

			var typeAndNames = metawidget.util.splitPath( mw.path );
			var toInspect = metawidget.util.traversePath( mw.toInspect, typeAndNames.names );

			if ( typeAndNames.names === undefined ) {
				typeAndNames.names = [];
			}

			var value;

			if ( elementName !== 'entity' && toInspect !== undefined ) {
				value = toInspect[attributes.name];
				typeAndNames.names.push( attributes.name );
			} else {
				value = toInspect;
			}

			// Push '0' so that object-based inspectors (like
			// PropertyTypeInspector) will try to look at the first entry.
			// However this will fail gracefully if the array is empty or
			// undefined

			typeAndNames.names.push( '0' );

			var inspectionResult = mw.inspect( mw.toInspect, typeAndNames.type, typeAndNames.names );

			if ( inspectionResult !== undefined ) {

				var tbody = metawidget.util.createElement( mw, 'tbody' );
				var row, rows;

				if ( inspectionResult.properties === undefined ) {

					// Simple, single-column table. It is still useful to pass
					// 'type', but we must be careful not to pass 'name'.

					table.appendChild( tbody );

					if ( value !== undefined ) {
						rows = value.length;
						for ( row = 0; row < rows; row++ ) {
							this.addRow( tbody, value, row, [ {
								type: inspectionResult.type
							} ], elementName, attributes, mw );
						}
					}

				} else {
					var inspectionResultProperties = metawidget.util.getSortedInspectionResultProperties( inspectionResult );

					// Create headers

					var thead = metawidget.util.createElement( mw, 'thead' );
					table.appendChild( thead );

					var columnAttributes = this.addHeaderRow( thead, inspectionResultProperties, mw );

					// Create body

					table.appendChild( tbody );

					if ( value !== undefined ) {
						rows = value.length;
						for ( row = 0; row < rows; row++ ) {
							this.addRow( tbody, value, row, columnAttributes, elementName, attributes, mw );
						}
					}
				}
			}

			return table;
		};

		/**
		 * Adds a row to the table header. Subclasses may override this method
		 * to add additional columns, or suppress the header row.
		 * 
		 * @param inspectionResultProperties
		 *            an array of sorted inspection result properties
		 * @return array of column attributes. For example, columnAttributes[0]
		 *         contains an object containing attributes for the first column
		 */

		this.addHeaderRow = function( thead, inspectionResultProperties, mw ) {

			var tr = metawidget.util.createElement( mw, 'tr' );
			thead.appendChild( tr );

			var columnAttributes = [];

			for ( var loop = 0, length = inspectionResultProperties.length; loop < length; loop++ ) {

				var columnAttribute = inspectionResultProperties[loop];

				if ( this.addHeader( tr, columnAttribute, mw ) ) {
					columnAttributes.push( columnAttribute );
				}
			}

			return columnAttributes;
		};

		/**
		 * Add a header column for the given attributes. Subclasses may override
		 * this method to suppress certain columns. By default, suppresses
		 * columns where 'hidden' is true.
		 * 
		 * @returns true if a header was added, false otherwise
		 */

		this.addHeader = function( tr, attributes, mw ) {

			if ( metawidget.util.isTrueOrTrueString( attributes.hidden ) ) {
				return false;
			}

			var th = metawidget.util.createElement( mw, 'th' );

			// Support column widths

			var style = '';
			
			if ( attributes.columnWidth !== undefined ) {
				style += 'width:' + attributes.columnWidth + ';';
			}

			if ( attributes.columnAlign !== undefined ) {
				style += 'text-align:' + attributes.columnAlign + ';';
			}

			if ( style !== '' ) {
				th.setAttribute( 'style', style );
			}

			if ( attributes.type !== 'function' ) {
				th.innerHTML = metawidget.util.getLabelString( attributes, mw );
			}

			tr.appendChild( th );

			return true;
		};

		/**
		 * Adds a row to the table body. Subclasses may override this method to
		 * add additional columns, or suppress the row.
		 * 
		 * @param columnAttributesArray
		 *            array of column attributes. For example,
		 *            columnAttributesArray[0] contains an object containing
		 *            columnAttributes for the first column
		 * @return the added row, or undefined if no row was added. This can be
		 *         useful for subclasses
		 */

		this.addRow = function( tbody, value, row, columnAttributesArray, elementName, tableAttributes, mw ) {

			var tr = metawidget.util.createElement( mw, 'tr' );
			tbody.appendChild( tr );

			for ( var loop = 0, length = columnAttributesArray.length; loop < length; loop++ ) {
				this.addColumn( tr, value, row, columnAttributesArray[loop], elementName, tableAttributes, mw );
			}

			return tr;
		};

		/**
		 * Add a column to the given row, displaying the given value. Subclasses
		 * may override this method to modify the column contents (for example,
		 * to wrap them in an anchor tag).
		 * 
		 * @return the added column, or undefined if no column was added. This
		 *         can be useful for subclasses
		 */

		this.addColumn = function( tr, value, row, columnAttributes, elementName, tableAttributes, mw ) {

			var td = metawidget.util.createElement( mw, 'td' );

			// Support column widths

			var style = '';
			
			if ( columnAttributes.columnWidth !== undefined ) {
				style += 'width:' + columnAttributes.columnWidth + ';';
			}

			if ( columnAttributes.columnAlign !== undefined ) {
				style += 'text-align:' + columnAttributes.columnAlign + ';';
			}

			if ( style !== '' ) {
				td.setAttribute( 'style', style );
			}
			
			// Render either top-level value, or a property of that value

			var valueToRender;

			if ( columnAttributes.name === undefined ) {
				valueToRender = value[row];
			} else {
				valueToRender = value[row][columnAttributes.name];
			}

			// Render either nothing, a nested read-only Metawidget, or a
			// toString()

			if ( columnAttributes.type === undefined || columnAttributes.type === 'array' || columnAttributes.type === 'function' || _alwaysUseNestedMetawidgetInTables === true ) {

				var attributes = {};

				for ( var attributeName in columnAttributes ) {
					attributes[attributeName] = columnAttributes[attributeName];
				}

				if ( attributes.name === undefined ) {
					attributes.name = '[' + row + ']';
				} else {
					attributes.name = metawidget.util.appendPathWithName( '[' + row + ']', attributes );
				}

				attributes.nameIncludesSeparator = true;

				if ( elementName !== 'entity' ) {
					attributes.name = '.' + metawidget.util.appendPathWithName( tableAttributes.name, attributes );
				}

				// Allow users to mark the whole table as readOnly

				if ( attributes.readOnly === undefined ) {
					attributes.readOnly = tableAttributes.readOnly;
				}

				var nestedMetawidget;

				if ( columnAttributes.type === undefined ) {

					// If type is undefined, we will likely recurse, so use
					// leading labels

					nestedMetawidget = mw.buildNestedMetawidget( attributes );
				} else {

					// Render simple types with a simple layout, to avoid a
					// leading label

					nestedMetawidget = mw.buildNestedMetawidget( attributes, {
						layout: new metawidget.layout.SimpleLayout()
					} );
				}

				// Support SimpleBindingProcessor

				mw.nestedMetawidgets = mw.nestedMetawidgets || [];
				mw.nestedMetawidgets.push( nestedMetawidget );

				td.appendChild( nestedMetawidget );
			} else if ( valueToRender !== undefined ) {
				td.innerHTML = '' + valueToRender;
			}

			tr.appendChild( td );

			return td;
		};
	};
} )();