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
metawidget.layout = metawidget.layout || {};

//
// SimpleLayout
//

metawidget.layout.SimpleLayout = function() {

	if ( ! ( this instanceof metawidget.layout.SimpleLayout ) ) {
		throw new Error( "Constructor called as a function" );
	}
};

metawidget.layout.SimpleLayout.prototype.layoutWidget = function( widget, attributes, container, mw ) {

	container.appendChild( widget );
};

//
// DivLayout
//

metawidget.layout.DivLayout = function() {

	if ( ! ( this instanceof metawidget.layout.DivLayout ) ) {
		throw new Error( "Constructor called as a function" );
	}
};

metawidget.layout.DivLayout.prototype.layoutWidget = function( widget, attributes, container, mw ) {

	if ( widget.tagName == 'STUB' && widget.childNodes.length == 0 ) {
		return;
	}

	var outerDiv = document.createElement( 'div' );

	// Label

	var labelDiv = document.createElement( 'div' );
	var label = document.createElement( 'label' );
	label.setAttribute( 'for', widget.getAttribute( 'id' ) );

	if ( attributes.label ) {
		label.innerHTML = attributes.label + ':';
	} else {
		label.innerHTML = metawidget.util.uncamelCase( attributes.name ) + ':';
	}

	labelDiv.appendChild( label );
	outerDiv.appendChild( labelDiv );

	// Widget

	var widgetDiv = document.createElement( 'div' );
	widgetDiv.appendChild( widget );
	outerDiv.appendChild( widgetDiv );

	container.appendChild( outerDiv );
};

//
// TableLayout
//

metawidget.layout.TableLayout = function( config ) {

	if ( ! ( this instanceof metawidget.layout.TableLayout ) ) {
		throw new Error( "Constructor called as a function" );
	}

	var tableStyleClass = config ? config.tableStyleClass : null;
	var columnStyleClasses = config ? config.columnStyleClasses : null;

	this.startContainerLayout = function( container, mw ) {

		var table = document.createElement( 'table' );
		if ( mw.path ) {
			var id = metawidget.util.camelCase( mw.path.split( '.' ) );
			table.setAttribute( 'id', 'table-' + id );
		}

		if ( tableStyleClass ) {
			table.setAttribute( 'class', tableStyleClass );
		}

		container.appendChild( table );

		table.appendChild( document.createElement( 'tbody' ) );
	},

	this.layoutWidget = function( widget, attributes, container, mw ) {

		if ( widget.tagName == 'STUB' && widget.childNodes.length == 0 ) {
			return;
		}

		var tr = document.createElement( 'tr' );

		var idPrefix = container.childNodes[0].getAttribute( 'id' );

		if ( idPrefix ) {
			if ( idPrefix.charAt( idPrefix.length - 1 ) != '-' ) {
				idPrefix += metawidget.util.capitalize( attributes.name );
			} else {
				idPrefix += attributes.name;
			}
		} else {
			idPrefix = 'table-' + attributes.name;
		}

		tr.setAttribute( 'id', idPrefix + '-row' );

		// Label

		var th = document.createElement( 'th' );

		if ( columnStyleClasses ) {
			th.setAttribute( 'class', columnStyleClasses.split( ',' )[0] );
		}

		var label = document.createElement( 'label' );
		label.setAttribute( 'for', widget.getAttribute( 'id' ) );
		label.setAttribute( 'id', idPrefix + '-label' );

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

		container.childNodes[0].childNodes[0].appendChild( tr );
	};
};