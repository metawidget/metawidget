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
 * WidgetBuilders.
 */

var metawidget = metawidget || {};

metawidget.CompositeWidgetBuilder = function( widgetBuilders ) {

	this.buildWidget = function( attributes, metawidget ) {

		for ( var wb = 0, wbLength = widgetBuilders.length; wb < wbLength; wb++ ) {

			var widget;
			var widgetBuilder = widgetBuilders[wb];
			
			if ( widgetBuilder.buildWidget ) {
				widget = widgetBuilder.buildWidget( attributes, metawidget );
			} else {
				widget = widgetBuilder( attributes, metawidget );
			}

			if ( widget ) {
				return widget;
			}
		}
	};
};

metawidget.ReadOnlyWidgetBuilder = function() {

	this.buildWidget = function( attributes, metawidget ) {

		// Not read-only?

		if ( !metawidget.readOnly && !attributes.readOnly ) {
			return null;
		}

		// Hidden

		if ( attributes.hidden == 'true' ) {
			return document.createElement( 'stub' );
		}

		if ( attributes.type == 'string' ) {
			return document.createElement( 'output' );
		}
		
		return null;
	};
};

metawidget.HtmlWidgetBuilder = function() {

	this.buildWidget = function( attributes, metawidget ) {

		// Hidden

		if ( attributes.hidden == 'true' ) {
			return document.createElement( 'stub' );
		}

		// Select box

		if ( attributes.lookup ) {
			var select = document.createElement( 'select' );
			select.appendChild( document.createElement( 'option' ) );
			var lookupSplit = attributes.lookup.split( ',' );

			for ( var loop = 0, length = lookupSplit.length; loop < length; loop++ ) {
				var option = document.createElement( 'option' );
				option.innerHTML = lookupSplit[loop];
				select.appendChild( option );
			}
			return select;
		}

		// Action

		if ( attributes.type == 'function' ) {
			var button = document.createElement( 'button' );
			button.innerHTML = attributes.label;
			return button;
		}

		// String

		if ( attributes.type == 'string' ) {
			var text = document.createElement( 'input' );
			text.setAttribute( 'type', 'text' );
			return text;
		}
		
		return null;
	};
};
