// Metawidget ${project.version}
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

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
	 * @class WidgetProcessor to add CSS styles for Bootstrap.
	 *        <p>
	 *        Note: in some cases this WidgetProcessor wraps the given widget
	 *        with Bootstrap-specific markup (e.g. &lt;div
	 *        class="input-prepend"&gt;). Therefore, BootstrapWidgetProcessor
	 *        should come <em>after</em> WidgetProcessors that expect widgets
	 *        to be unwrapped (such as <tt>SimpleBindingProcessor</tt>).
	 */

	metawidget.bootstrap.widgetprocessor.BootstrapWidgetProcessor = function() {

		if ( ! ( this instanceof metawidget.bootstrap.widgetprocessor.BootstrapWidgetProcessor ) ) {
			throw new Error( "Constructor called as a function" );
		}
	};

	metawidget.bootstrap.widgetprocessor.BootstrapWidgetProcessor.prototype.processWidget = function( widget, elementName, attributes, mw ) {

		switch ( widget.tagName ) {

			case 'TABLE':
				metawidget.util.appendToAttribute( widget, 'class', 'table table-striped table-bordered table-hover' );
				break;

			case 'INPUT':
				if ( widget.getAttribute( 'type' ) === 'submit' ) {
					metawidget.util.appendToAttribute( widget, 'class', 'btn btn-primary' );
					break;
				}
				if ( widget.getAttribute( 'type' ) === 'button' ) {
					metawidget.util.appendToAttribute( widget, 'class', 'btn' );
					break;
				}
				if ( attributes.inputPrepend !== undefined || attributes.inputAppend !== undefined ) {
					var div = metawidget.util.createElement( mw, 'div' );
					if ( attributes.inputPrepend !== undefined ) {
						div.setAttribute( 'class', 'input-prepend' );
						var span = metawidget.util.createElement( mw, 'span' );
						span.setAttribute( 'class', 'add-on' );
						span.innerHTML = attributes.inputPrepend;
						div.appendChild( span );
					}
					div.appendChild( widget );
					if ( attributes.inputAppend !== undefined ) {
						if ( attributes.inputPrepend !== undefined ) {
							div.setAttribute( 'class', 'input-prepend input-append' );
						} else {
							div.setAttribute( 'class', 'input-append' );
						}
						var span = metawidget.util.createElement( mw, 'span' );
						span.setAttribute( 'class', 'add-on' );
						span.innerHTML = attributes.inputAppend;
						div.appendChild( span );
					}
					return div;
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
	 *        <p>
	 *        This Layout extends metawidget.layout.DivLayout. It adds Bootstrap
	 *        CSS classes such as 'control-group' and 'control-label' to the
	 *        divs.
	 * 
	 * @returns {metawidget.bootstrap.layout.BootstrapDivLayout}
	 */

	metawidget.bootstrap.layout.BootstrapDivLayout = function( config ) {

		if ( ! ( this instanceof metawidget.bootstrap.layout.BootstrapDivLayout ) ) {
			throw new Error( "Constructor called as a function" );
		}

		if ( config === undefined ) {
			config = {};
		}
		if ( config.divStyleClasses === undefined ) {
			config.divStyleClasses = [ 'control-group', undefined, 'controls' ];
		}
		if ( config.labelStyleClass === undefined ) {
			config.labelStyleClass = 'control-label';
		}

		return new metawidget.layout.DivLayout( config );
	};

	/**
	 * @class LayoutDecorator to decorate widgets from different sections using
	 *        Bootstrap tabs.
	 */

	metawidget.bootstrap.layout.TabLayoutDecorator = function( config ) {

		if ( ! ( this instanceof metawidget.bootstrap.layout.TabLayoutDecorator ) ) {
			throw new Error( 'Constructor called as a function' );
		}

		metawidget.layout.createNestedSectionLayoutDecorator( config, this, 'bootstrapTabLayoutDecorator' );
	};

	metawidget.bootstrap.layout.TabLayoutDecorator.prototype.createSectionWidget = function( previousSectionWidget, section, attributes, container, mw ) {

		var tabs = previousSectionWidget;

		// Whole new tabbed pane?

		if ( tabs === undefined ) {
			tabs = metawidget.util.createElement( mw, 'div' );
			tabs.setAttribute( 'id', metawidget.util.getId( "property", attributes, mw ) + '-tabs' );
			tabs.setAttribute( 'class', 'tabs' );
			var ul = metawidget.util.createElement( mw, 'ul' );
			ul.setAttribute( 'class', 'nav nav-tabs' );
			tabs.appendChild( ul );
			var content = metawidget.util.createElement( mw, 'div' );
			content.setAttribute( 'class', 'tab-content' );
			tabs.appendChild( content );
			this.getDelegate().layoutWidget( tabs, "property", {
				wide: "true"
			}, container, mw );

			mw.bootstrapTabLayoutDecorator = mw.bootstrapTabLayoutDecorator || [];
			mw.bootstrapTabLayoutDecorator.push( tabs );
		} else {
			tabs = previousSectionWidget.parentNode.parentNode;
		}

		// New Tab

		var ul = tabs.childNodes[0];
		var tabId = tabs.getAttribute( 'id' ) + ( ul.childNodes.length + 1 );
		var li = metawidget.util.createElement( mw, 'li' );
		if ( ul.childNodes.length === 0 ) {
			li.setAttribute( 'class', 'active' );
		}
		var a = metawidget.util.createElement( mw, 'a' );
		a.setAttribute( 'data-toggle', 'tab' );
		a.setAttribute( 'href', '#' + tabId );
		a.hash = '#' + tabId;
		li.appendChild( a );
		ul.appendChild( li );

		var content = tabs.childNodes[1];
		var tab = metawidget.util.createElement( mw, 'div' );
		if ( content.childNodes.length === 0 ) {
			tab.setAttribute( 'class', 'tab-pane active' );
		} else {
			tab.setAttribute( 'class', 'tab-pane' );
		}
		tab.setAttribute( 'id', tabId );
		content.appendChild( tab );

		// Tab name

		a.innerHTML = section;

		return tab;
	};

} )();