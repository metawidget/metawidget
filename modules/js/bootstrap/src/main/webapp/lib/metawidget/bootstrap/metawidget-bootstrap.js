// Metawidget ${project.version}
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

// TODO: Bootstrap 3

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

var metawidget = metawidget || {};

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

		if ( widget.tagName === 'TABLE' ) {

			metawidget.util.appendToAttribute( widget, 'class', 'table table-striped table-bordered table-hover' );

		} else if ( widget.tagName === 'INPUT' ) {

			if ( widget.getAttribute( 'type' ) === 'submit' ) {
				metawidget.util.appendToAttribute( widget, 'class', 'btn btn-primary' );
			} else if ( widget.getAttribute( 'type' ) === 'button' ) {
				metawidget.util.appendToAttribute( widget, 'class', 'btn' );
			} else if ( attributes.inputPrepend !== undefined || attributes.inputAppend !== undefined ) {
				var div = metawidget.util.createElement( mw, 'div' );
				var span;
				if ( attributes.inputPrepend !== undefined ) {
					div.setAttribute( 'class', 'input-prepend' );
					span = metawidget.util.createElement( mw, 'span' );
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
					span = metawidget.util.createElement( mw, 'span' );
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

		var ul, content;

		if ( tabs === undefined ) {
			tabs = metawidget.util.createElement( mw, 'div' );
			tabs.setAttribute( 'id', metawidget.util.getId( "property", attributes, mw ) + '-tabs' );
			tabs.setAttribute( 'class', 'tabs' );
			ul = metawidget.util.createElement( mw, 'ul' );
			ul.setAttribute( 'class', 'nav nav-tabs' );
			tabs.appendChild( ul );
			content = metawidget.util.createElement( mw, 'div' );
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

		ul = tabs.childNodes[0];
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

		content = tabs.childNodes[1];
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