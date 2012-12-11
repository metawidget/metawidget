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
	
	if ( !( this instanceof metawidget.SimpleLayout )) {
		throw new Error( "Constructor called as a function" );
	}
	
	this.layoutWidget = function( widget, attributes, container, mw ) {

		container.appendChild( widget );
	};
};

metawidget.TableLayout = function( options ) {
		
	if ( !( this instanceof metawidget.TableLayout )) {
		throw new Error( "Constructor called as a function" );
	}

	var tableStyleClass = options ? options.tableStyleClass : null;
	var columnStyleClasses = options ? options.columnStyleClasses : null;

	this.startContainerLayout = function( container ) {

		var table = document.createElement( 'table' );
		
		if ( tableStyleClass ) {
			table.setAttribute( 'class', tableStyleClass );
		}
		
		container.appendChild( table );
		
		table.appendChild( document.createElement( 'tbody' ));
	},

	this.layoutWidget = function( widget, attributes, container, mw ) {

		if ( widget.tagName == 'STUB' ) {
			return;
		}

		var tr = document.createElement( 'tr' );
		
		tr.setAttribute( 'id', 'table' + mw.path + attributes.name + '-row' );
		
		// Label
		
		var th = document.createElement( 'th' );
		
		if ( columnStyleClasses ) {
			th.setAttribute( 'class', columnStyleClasses.split( ',' )[0] );
		}
		
		var label = document.createElement( 'label' );
		label.setAttribute( 'for', attributes.name );
		
		if ( attributes.label ) {
			label.innerHTML = attributes.label + ':';
		} else {
			label.innerHTML = metawidget.util.uncamelCase( attributes.name ) + ':';
		}
		th.appendChild( label );
		tr.appendChild( th );

		// Widget
		
		var td = document.createElement( 'td' );

		if ( columnStyleClasses ) {
			td.setAttribute( 'class', columnStyleClasses.split( ',' )[1] );
		}

		td.appendChild( widget );
		tr.appendChild( td );

		// Error
		
		td = document.createElement( 'td' );

		if ( columnStyleClasses ) {
			td.setAttribute( 'class', columnStyleClasses.split( ',' )[2] );
		}

		if ( !metawidget.util.isReadOnly( attributes, mw ) && attributes.required == 'true' ) {
			td.innerHTML = '*';
		}

		tr.appendChild( td );

		container.firstChild.firstChild.appendChild( tr );
	};
};