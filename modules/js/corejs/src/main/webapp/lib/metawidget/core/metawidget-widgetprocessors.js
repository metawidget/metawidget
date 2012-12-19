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
 * WidgetProcessors.
 */

var metawidget = metawidget || {};
metawidget.widgetprocessor = metawidget.widgetprocessor || {};

//
// IdWidgetProcessor
//

metawidget.widgetprocessor.IdWidgetProcessor = function() {

	if ( ! ( this instanceof metawidget.widgetprocessor.IdWidgetProcessor ) ) {
		throw new Error( "Constructor called as a function" );
	}
};

metawidget.widgetprocessor.IdWidgetProcessor.prototype.processWidget = function( widget, attributes, mw ) {

	widget.setAttribute( 'id', attributes.name );
	return widget;
};

//
// ValueWidgetProcessor
//

/**
 * Simple data/action binding implementation. Frameworks that supply their own
 * data-binding mechanisms should override this with their own WidgetProcessor.
 */

metawidget.widgetprocessor.SimpleBindingProcessor = function() {

	if ( ! ( this instanceof metawidget.widgetprocessor.SimpleBindingProcessor ) ) {
		throw new Error( "Constructor called as a function" );
	}
};

metawidget.widgetprocessor.SimpleBindingProcessor.prototype.processWidget = function( widget, attributes, mw ) {

	if ( widget.tagName == 'BUTTON' ) {
		var onClick = 'toInspect';

		if ( attributes.name != '$root' ) {
			onClick += '.' + attributes.name;
		}

		widget.setAttribute( 'onClick', onClick + '()' );
	} else {

		var value;

		if ( attributes.name != '$root' ) {
			value = mw.toInspect[attributes.name];
		} else {
			value = mw.toInspect;
		}

		if ( value ) {
			if ( widget.tagName == 'OUTPUT' ) {
				widget.innerHTML = value;
			} else {
				widget.setAttribute( 'value', value );
			}
		}
	}

	return widget;
};
