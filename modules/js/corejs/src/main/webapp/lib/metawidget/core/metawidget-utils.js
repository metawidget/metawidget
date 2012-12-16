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
	
	return name.charAt( 0 ).toUpperCase() + name.slice( 1 ).replace( /([^ ])([A-Z])/g, function( $1, $2, $3 ) {

		return $2 + ' ' + $3;
	} );
};

metawidget.util.isReadOnly = function( attributes, mw ) {
	
	if ( attributes.readOnly == 'true' ) {
		return true;
	}
	
	return ( mw.readOnly );
};