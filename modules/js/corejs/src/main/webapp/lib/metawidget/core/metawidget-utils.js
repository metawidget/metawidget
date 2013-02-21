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

var metawidget = metawidget || {};

/**
 * @namespace Utilities.
 */

metawidget.util = metawidget.util || {};

/**
 * Uncamel cases the given name (e.g. from 'fooBarBaz' to 'Foo Bar Baz').
 */

metawidget.util.uncamelCase = function( name ) {

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
 * Camel cases the given array of names (e.g. from ['foo','bar','baz'] to
 * 'fooBarBaz').
 * 
 * @return the camel cased name. Or an empty string if no name
 */

metawidget.util.camelCase = function( names ) {

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

metawidget.util.getId = function( attributes, mw ) {

	if ( mw.path !== undefined ) {
		var splitPath = mw.path.split( '.' );

		if ( splitPath[0] === 'object' ) {
			splitPath = splitPath.slice( 1 );
		}

		if ( attributes.name && attributes._root !== 'true' ) {
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
 * Returns true if the given node has child <em>elements</em>. That is, their
 * <tt>nodeType === 1</tt>. Ignores other sorts of child nodes, such as text
 * nodes.
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

	if ( attributes.large === 'true' ) {
		return true;
	}

	if ( attributes.wide === 'true' ) {
		return true;
	}

	return false;
};

/**
 * Splits the given path into its type and an array of names (e.g. 'foo.bar.baz'
 * into type 'foo' and names ['bar','baz']).
 * 
 * @returns an object with properties 'type' and 'names' (provided there is at
 *          least 1 name)
 */

metawidget.util.splitPath = function( path ) {

	var splitPath = {};

	if ( path !== undefined ) {
		var pathArray = path.split( '.' );
		splitPath.type = pathArray[0];

		if ( pathArray.length > 1 ) {
			splitPath.names = pathArray.slice( 1 );
		}
	}

	return splitPath;
};

/**
 * Appends the 'path' property from the given Metawidget to the 'name' property
 * in the given attributes.
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
 */

metawidget.util.traversePath = function( toInspect, names ) {

	if ( toInspect === undefined ) {
		return undefined;
	}

	if ( names !== undefined ) {
		for ( var loop = 0, length = names.length; loop < length; loop++ ) {

			var name = names[loop];
			var indexOf = name.indexOf( '[' );
			var arrayIndex = undefined;

			if ( indexOf !== -1 ) {
				arrayIndex = name.substring( indexOf + 1, name.length - 1 );
				name = name.substring( 0, indexOf );
			}

			toInspect = toInspect[name];

			if ( arrayIndex !== undefined ) {
				toInspect = toInspect[arrayIndex];
			}

			if ( toInspect === undefined ) {
				return undefined;
			}
		}
	}

	return toInspect;
};

/**
 * Combines the given first array with the given second array.
 * <p>
 * Array elements are expected to be objects. They are combined based on their
 * 'name' property (or their '_root' property). If no elements match, new
 * elements are appended to the end of the array.
 */

metawidget.util.combineInspectionResults = function( existingInspectionResult, newInspectionResult ) {

	// Inspector may return undefined

	if ( newInspectionResult === undefined ) {
		return existingInspectionResult;
	}

	// If this is the first result...

	if ( existingInspectionResult.length === 0 ) {

		// ...copy it

		for ( var loop = 0, length = newInspectionResult.length; loop < length; loop++ ) {

			var newAttributes = newInspectionResult[loop];
			var existingAttributes = {};

			for ( var attribute in newAttributes ) {
				existingAttributes[attribute] = newAttributes[attribute];
			}

			existingInspectionResult.push( existingAttributes );
		}

	} else {

		// ...otherwise merge it

		outer: for ( var loop1 = 0, length1 = newInspectionResult.length; loop1 < length1; loop1++ ) {

			var newAttributes = newInspectionResult[loop1];

			for ( var loop2 = 0, length2 = existingInspectionResult.length; loop2 < length2; loop2++ ) {
				var existingAttributes = existingInspectionResult[loop2];

				if ( existingAttributes.name === newAttributes.name || ( existingAttributes._root === 'true' && newAttributes._root === 'true' ) ) {

					for ( var attribute in newAttributes ) {
						existingAttributes[attribute] = newAttributes[attribute];
					}

					continue outer;
				}
			}

			// If no existing attributes matched, push a new one

			var existingAttributes = {};

			for ( var attribute in newAttributes ) {
				existingAttributes[attribute] = newAttributes[attribute];
			}

			existingInspectionResult.push( existingAttributes );
		}
	}

	return existingInspectionResult;
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

	var sections = metawidget.util.splitArray( section );

	switch ( sections.length ) {

		// (empty String means 'end current section')
		case 0:
			return '';

		case 1:
			delete attributes.section;
			return sections[0];

		case 2:
			attributes.section = metawidget.util.joinArray( sections.slice( 1 ) );
			return sections[0];
	}
};

/**
 * Similar to String.split(',') but recognizes escaped commas.
 */

metawidget.util.splitArray = function( toSplit ) {

	var array = [];

	var regex = /(?:[^\,\\]+|\\.)+/g;
	var matched;
	while ( matched = regex.exec( toSplit ) ) {
		array.push( matched[0].replace( /\\,/g, ',' ) );
	}

	return array;
};

/**
 * Similar to Array.join(',') but escapes any commas.
 */

metawidget.util.joinArray = function( array ) {

	var toReturn = '';

	for ( var loop = 0, length = array.length; loop < length; loop++ ) {

		if ( toReturn.length != 0 ) {
			toReturn += ',';
		}

		toReturn += array[loop].replace( /,/g, '\\,' );
	}

	return toReturn;
};