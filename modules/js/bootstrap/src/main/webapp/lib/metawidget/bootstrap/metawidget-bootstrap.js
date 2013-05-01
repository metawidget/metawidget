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

( function() {

	'use strict';

	/**
	 * @namespace Metawidget Twitter Bootstrap support.
	 */

	metawidget.bootstrap = metawidget.bootstrap || {};

	/**
	 * @namespace WidgetProcessors for Twitter Bootstrap environments.
	 */

	metawidget.bootstrap.widgetprocessor = metawidget.bootstrap.widgetprocessor || {};

	/**
	 * @class WidgetProcessor to add CSS styles for Boostrap.
	 */

	metawidget.bootstrap.widgetprocessor.BootstrapWidgetProcessor = function() {

		if ( ! ( this instanceof metawidget.bootstrap.widgetprocessor.BootstrapWidgetProcessor ) ) {
			throw new Error( "Constructor called as a function" );
		}
	};

	metawidget.bootstrap.widgetprocessor.BootstrapWidgetProcessor.prototype.processWidget = function( widget ) {

		if ( widget.tagName === 'BUTTON' ) {
			var existingClass = widget.getAttribute( 'class' );

			if ( existingClass === null ) {
				widget.setAttribute( 'class', 'btn' );
			} else {
				widget.setAttribute( 'class', existingClass + ' btn' );
			}
		}
		
		return widget;
	};

	/**
	 * @namespace Layouts for Twitter Bootstrap environments.
	 */

	metawidget.bootstrap.layout = metawidget.bootstrap.layout || {};

	/**
	 * @class Layout to wrap widgets with divs suitable for 'form-vertical' or
	 *        'form-horizontal' Bootstrap layouts.
	 * 
	 * @returns {metawidget.bootstrap.layout.BootstrapDivLayout}
	 */

	metawidget.bootstrap.layout.BootstrapDivLayout = function() {

		if ( ! ( this instanceof metawidget.bootstrap.layout.BootstrapDivLayout ) ) {
			throw new Error( "Constructor called as a function" );
		}

		var layout = new metawidget.layout.DivLayout( {
			divStyleClasses: [ 'control-group', undefined, 'controls' ],
			labelStyleClass: 'control-label'
		} );

		var _superLayoutLabel = layout.layoutLabel;

		layout.layoutLabel = function( outerDiv, widget, elementName, attributes, mw ) {

			_superLayoutLabel( outerDiv, widget, elementName, attributes, mw );

			if ( !metawidget.util.isTrueOrTrueString( attributes.readOnly ) && metawidget.util.isTrueOrTrueString( attributes.required ) ) {

				var label = outerDiv.childNodes[0].childNodes[0];
				var existingClass = label.getAttribute( 'class' );

				if ( existingClass === null ) {
					label.setAttribute( 'class', 'required' );
				} else {
					label.setAttribute( 'class', existingClass + ' required' );
				}
			}
		};

		return layout;
	};
} )();