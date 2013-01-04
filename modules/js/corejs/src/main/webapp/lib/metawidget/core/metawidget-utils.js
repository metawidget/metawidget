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

/**
 * Utilities.
 */

var metawidget = metawidget || {};
metawidget.util = metawidget.util || {};

metawidget.util.uncamelCase = function( name ) {

	return name.charAt( 0 ).toUpperCase() + name.slice( 1 ).replace( /([^ ])([A-Z0-9])/g, function( $1, $2, $3 ) {

		return $2 + ' ' + $3;
	} );
};

metawidget.util.capitalize = function( name ) {

	return name.charAt( 0 ).toUpperCase() + name.slice( 1 );
};

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

	var id = '';
	
	if ( attributes ) {
		id = attributes.name;
	}

	if ( mw.path ) {
		var splitPath = mw.path.split( '.' );

		if ( splitPath[0] == 'object' ) {
			splitPath = splitPath.slice( 1 );
		}
		
		if ( attributes.name && attributes.name != '__root' ) {
			splitPath.push( attributes.name );
		}

		id = metawidget.util.camelCase( splitPath );
	}

	return id;
};

metawidget.util.hasChildElements = function( node ) {

	var childNodes = node.childNodes;

	for ( var loop = 0, length = childNodes.length; loop < length; loop++ ) {

		if ( childNodes[loop].getAttribute ) {
			return true;
		}
	}

	return false;
};

metawidget.util.isReadOnly = function( attributes, mw ) {

	if ( attributes.readOnly == 'true' ) {
		return true;
	}

	return ( mw.readOnly );
};

metawidget.util.splitPath = function( path ) {
	
	var type = '';
	var names = [];

	if ( path ) {
		var splitPath = path.split( '.' );
		type = splitPath[0];
		names = splitPath.slice( 1 );
	}
	
	return {
		"type": type,
		"names": names
	};
};

metawidget.util.appendPath = function( attributes, mw ) {
	
	if ( mw.path ) {
		return mw.path + '.' + attributes.name;
	}

	return typeof( mw.toInspect ) + '.' + attributes.name;
};

metawidget.util.traversePath = function( toInspect, type, names ) {
	
	if ( toInspect == null ) {
		return null;
	}

	if ( names ) {
		for( var loop = 0, length = names.length; loop < length; loop++ ) {

			toInspect = toInspect[names[loop]];
			
			if ( toInspect == null ) {
				return null;
			}
		}
	}
	
	return toInspect;
};