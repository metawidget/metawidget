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
	 * @namespace Utilities.
	 */

	metawidget.util = metawidget.util || {};

	/**
	 * Returns a label for the given set of attributes.
	 * <p>
	 * The label is determined using the following algorithm:
	 * <p>
	 * <ul>
	 * <li> if <tt>attributes.title</tt> exists...
	 * <ul>
	 * <li>if the given <tt>mw</tt> has a property <tt>l10n</tt>, then
	 * <tt>attributes.title</tt> is camel-cased and used as a lookup into
	 * <tt>mw.i10n[camelCasedTitle]</tt>. This means developers can initially
	 * build their UIs without worrying about localization, then turn it on
	 * later</li>
	 * <li>if no such lookup exists (or <tt>mw.l10n</tt> does not exist),
	 * return <tt>attributes.title</tt>
	 * </ul>
	 * </li>
	 * <li> if <tt>attributes.title</tt> does not exist...
	 * <ul>
	 * <li>if the given <tt>mw</tt> has a property <tt>l10n</tt>, then
	 * <tt>attributes.name</tt> is used as a lookup into
	 * <tt>mw.i10n[attributes.name]</tt></li>
	 * <li>if no such lookup exists (or <tt>mw.l10n</tt> does not exist),
	 * return <tt>attributes.name</tt>
	 * </ul>
	 * </li>
	 * </ul>
	 */

	metawidget.util.getLabelString = function( attributes, mw ) {

		// Explicit title

		if ( attributes.title !== undefined ) {

			// (localize if possible)

			var camelCased = metawidget.util.camelCase( attributes.title );

			if ( mw.l10n !== undefined && mw.l10n[camelCased] !== undefined ) {
				return mw.l10n[camelCased];
			}

			return attributes.title;
		}

		// Localize if possible

		var name = attributes.name;

		if ( mw.l10n !== undefined && mw.l10n[name] !== undefined ) {
			return mw.l10n[name];
		}

		// Default name, uncamel case (e.g. from 'fooBarBaz' to 'Foo Bar Baz')

		return name.charAt( 0 ).toUpperCase() + name.slice( 1 ).replace( /([^ ])([A-Z0-9])/g, function( $1, $2, $3 ) {

			return $2 + ' ' + $3;
		} );
	};

	/**
	 * Capitalizes the first letter of the given name (e.g. from 'fooBarBaz' to
	 * 'FooBarBaz').
	 */

	metawidget.util.capitalize = function( name ) {

		return name.charAt( 0 ).toUpperCase() + name.slice( 1 );
	};

	/**
	 * @return true if the value is boolean true or string 'true', but false for
	 *         any other value (including other JavaScript 'truthy' values)
	 */

	metawidget.util.isTrueOrTrueString = function( value ) {

		return ( value === 'true' || value === true );
	};

	/**
	 * Camel cases the given array of names (e.g. from ['foo','bar','baz'] to
	 * 'fooBarBaz'). Note the second and third names are capitalized. However no
	 * attempt is made to <em>de</em>capitalize the first name, because that
	 * gets very ambiguous with names like 'URL', 'ID' etc.
	 * <p>
	 * If <tt>names</tt> is not an array, first calls
	 * <tt>names.split( ' ' )</tt>.
	 * 
	 * @return the camel cased name. Or an empty string if no name
	 */

	metawidget.util.camelCase = function( names ) {

		if ( ! ( names instanceof Array ) ) {
			names = names.split( ' ' );
		}

		var toString = '';
		var length = names.length;

		if ( length > 0 ) {
			toString += names[0];
		}

		for ( var loop = 1; loop < length; loop++ ) {
			toString += metawidget.util.capitalize( names[loop] );
		}

		return toString;
	};

	/**
	 * Gets a camelCased id based on the given attributes.name and the given
	 * mw.path.
	 */

	metawidget.util.getId = function( elementName, attributes, mw ) {

		if ( mw.path !== undefined ) {
			var splitPath = mw.path.split( '.' );

			if ( splitPath[0] === 'object' ) {
				splitPath = splitPath.slice( 1 );
			}

			if ( attributes.name && elementName !== 'entity' ) {
				splitPath.push( attributes.name );
			} else if ( splitPath.length == 0 ) {
				return undefined;
			}

			return metawidget.util.camelCase( splitPath );
		}

		if ( attributes !== undefined ) {
			return attributes.name;
		}
	};

	/**
	 * Returns true if the given node has child <em>elements</em>. That is,
	 * their <tt>nodeType === 1</tt>. Ignores other sorts of child nodes,
	 * such as text nodes.
	 */

	metawidget.util.hasChildElements = function( node ) {

		var childNodes = node.childNodes;

		for ( var loop = 0, length = childNodes.length; loop < length; loop++ ) {

			if ( childNodes[loop].nodeType === 1 ) {
				return true;
			}
		}

		return false;
	};

	/**
	 * @true if the given attributes define 'large' or 'wide'.
	 */

	metawidget.util.isSpanAllColumns = function( attributes ) {

		if ( attributes === undefined ) {
			return false;
		}

		if ( metawidget.util.isTrueOrTrueString( attributes.large ) ) {
			return true;
		}

		if ( metawidget.util.isTrueOrTrueString( attributes.wide ) ) {
			return true;
		}

		return false;
	};

	/**
	 * Splits the given path into its type and an array of names (e.g.
	 * 'foo.bar['baz']' into type 'foo' and names ['bar','baz']).
	 * 
	 * @returns an object with properties 'type' and 'names' (provided there is
	 *          at least 1 name)
	 */

	metawidget.util.splitPath = function( path ) {

		var splitPath = {};

		if ( path !== undefined ) {

			// Match at every '.' and '[' boundary

			var pathArray = path.match( /([^\.\[\]]*)/g );
			splitPath.type = pathArray[0];

			for ( var loop = 1, length = pathArray.length; loop < length; loop++ ) {

				// Ignore empty matches

				if ( pathArray[loop] === '' ) {
					continue;
				}

				if ( splitPath.names === undefined ) {
					splitPath.names = [];
				}

				// Strip surrounding spaces and quotes (eg. foo[ 'bar' ])

				var stripQuotes = pathArray[loop].match( /^(?:\s*(?:\'|\"))([^\']*)(?:(?:\'|\")\s*)$/ );

				if ( stripQuotes !== null && stripQuotes[1] !== undefined ) {
					pathArray[loop] = stripQuotes[1];
				}

				splitPath.names.push( pathArray[loop] );
			}
		}

		return splitPath;
	};

	/**
	 * Appends the 'path' property from the given Metawidget to the 'name'
	 * property in the given attributes.
	 */

	metawidget.util.appendPath = function( attributes, mw ) {

		if ( mw.path !== undefined ) {
			return mw.path + '.' + attributes.name;
		}

		if ( mw.toInspect !== undefined ) {
			return typeof ( mw.toInspect ) + '.' + attributes.name;
		}

		return 'object.' + attributes.name;
	};

	/**
	 * Traverses the given 'toInspect' along properties defined by the array of
	 * 'names'.
	 * 
	 * @param intermediateName
	 *            optional intermediate name to traverse at each step. Useful
	 *            for traversing JSON Schemas (e.g.
	 *            toInspect->properties->name1->properties->name2)
	 */

	metawidget.util.traversePath = function( toInspect, names, intermediateName ) {

		if ( toInspect === undefined ) {
			return undefined;
		}

		if ( names !== undefined ) {
			for ( var loop = 0, length = names.length; loop < length; loop++ ) {

				if ( intermediateName !== undefined ) {
					toInspect = toInspect[intermediateName];

					if ( toInspect === undefined ) {
						return undefined;
					}
				}

				toInspect = toInspect[names[loop]];

				// We don't need to worry about array indexes here: they should
				// have been parsed out by splitPath

				if ( toInspect === undefined ) {
					return undefined;
				}
			}
		}

		return toInspect;
	};

	/**
	 * Return an array of the given inspection result's properties, sorted by
	 * 'propertyOrder' (if any).
	 * <p>
	 * See: https://github.com/json-stylesheet/json-stylesheet/issues/1
	 * https://github.com/json-schema/json-schema/issues/87
	 */

	metawidget.util.getSortedInspectionResultProperties = function( inspectionResult ) {

		// Extract the given inspection result's properties into an array...

		var sortedProperties = [];

		for ( var propertyName in inspectionResult.properties ) {

			var properties = inspectionResult.properties[propertyName];
			sortedProperties.push( properties );

			properties.name = propertyName;
			properties._syntheticOrder = sortedProperties.length;
		}

		// ...sort the array...

		sortedProperties.sort( function( a, b ) {

			if ( a.propertyOrder === undefined ) {
				if ( b.propertyOrder === undefined ) {
					return ( a._syntheticOrder - b._syntheticOrder );
				}
				return 1;
			}

			if ( b.propertyOrder === undefined ) {
				return -1;
			}

			var diff = ( a.propertyOrder - b.propertyOrder );

			if ( diff === 0 ) {
				return ( a._syntheticOrder - b._syntheticOrder );
			}

			return diff;
		} );

		// ...and return it

		return sortedProperties;
	};

	/**
	 * Combines the given first inspection result with the given second
	 * inspection result.
	 * <p>
	 * Inspection results are expected to be JSON Schema objects. They are
	 * combined based on their property name. If no elements match, new
	 * properties are appended.
	 */

	metawidget.util.combineInspectionResults = function( existingInspectionResult, newInspectionResult ) {

		// Inspector may return undefined

		if ( newInspectionResult === undefined ) {
			return;
		}

		// Combine based on propertyName

		_copyPrimitives( newInspectionResult, existingInspectionResult );

		if ( newInspectionResult.properties === undefined ) {
			return;
		}

		existingInspectionResult.properties = existingInspectionResult.properties || {};

		for ( var propertyName in newInspectionResult.properties ) {

			existingInspectionResult.properties[propertyName] = existingInspectionResult.properties[propertyName] || {};
			_copyPrimitives( newInspectionResult.properties[propertyName], existingInspectionResult.properties[propertyName] );
		}

		//
		// Private methods
		//

		function _copyPrimitives( from, to ) {

			for ( var propertyName in from ) {

				var propertyValue = from[propertyName];

				if ( propertyValue instanceof Array ) {
					to[propertyName] = propertyValue.slice( 0 );
					continue;
				}

				if ( propertyValue instanceof Object ) {
					continue;
				}

				to[propertyName] = from[propertyName];
			}
		}
	};

	/**
	 * Strips the first section off the section attribute (if any).
	 */

	metawidget.util.stripSection = function( attributes ) {

		var section = attributes.section;

		// (undefined means 'no change to current section')

		if ( section === undefined ) {
			return undefined;
		}

		if ( ! ( section instanceof Array ) ) {
			delete attributes.section;
			return section;
		}

		switch ( section.length ) {

			// (empty String means 'end current section')
			case 0:
				delete attributes.section;
				return '';

			case 1:
				delete attributes.section;
				return section[0];

			case 2:
				attributes.section = section.slice( 1 );
				return section[0];
		}
	};

	/**
	 * Sets the given 'toAppend' to the given widget's 'attributeName'. If the
	 * given widget already has a value for 'attributeName', appends a space and
	 * then adds 'toAppend'.
	 */

	metawidget.util.appendToAttribute = function( widget, attributeName, toAppend ) {

		var existingAttribute = widget.getAttribute( attributeName );

		if ( existingAttribute === null ) {
			widget.setAttribute( attributeName, toAppend );
		} else {
			widget.setAttribute( attributeName, existingAttribute + ' ' + toAppend );
		}
	};
} )();