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
 * Layouts.
 */

var metawidget = metawidget || {};

metawidget.SimpleLayout = function() {
	
	this.layoutWidget = function( widget, attributes, container ) {

		container.appendChild( widget );
	};
};

metawidget.DivLayout = function() {
	
	this.layoutWidget = function( widget, attributes, container ) {

		var label = document.createElement( 'label' );
		label.setAttribute( 'for', attributes.name );
		label.innerHTML = attributes.label + ':';
	
		var div = document.createElement( 'div' );
		div.appendChild( label );
		div.appendChild( widget );
	
		container.appendChild( div );
	};
};

metawidget.TableLayout = function() {

	this.startContainerLayout = function( container ) {

		container.appendChild( document.createElement( 'table' ) );
	},

	this.layoutWidget = function( widget, attributes, container ) {

		if ( widget.tagName == 'STUB' ) {
			return;
		}

		var th = document.createElement( 'th' );
		var label = document.createElement( 'label' );
		label.setAttribute( 'for', attributes.name );
		label.innerHTML = attributes.label + ':';
		th.appendChild( label );

		var td = document.createElement( 'td' );
		td.appendChild( widget );

		var tr = document.createElement( 'tr' );
		tr.appendChild( th );
		tr.appendChild( td );

		container.firstChild.appendChild( tr );
	}
};
