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

		var tagName = widget.tagName;

		if ( tagName === 'TABLE' ) {

			metawidget.util.appendToAttribute( widget, 'class', 'table table-striped table-bordered table-hover' );

		} else if ( tagName === 'SELECT' || tagName === 'TEXTAREA' ) {

			metawidget.util.appendToAttribute( widget, 'class', 'form-control' );

		} else if ( tagName === 'OUTPUT' ) {

			// Pad output tags the same way as .form-control pads input tags.
			// See:
			// https://github.com/twbs/bootstrap/issues/9969

			metawidget.util.appendToAttribute( widget, 'style', 'padding:6px 12px', ';' );

		} else if ( tagName === 'INPUT' ) {

			var type = widget.getAttribute( 'type' );

			switch ( type ) {

				case 'submit':
					metawidget.util.appendToAttribute( widget, 'class', 'btn btn-primary' );
					break;

				case 'button':
					metawidget.util.appendToAttribute( widget, 'class', 'btn btn-default' );
					break;

				default: {

					if ( type !== 'checkbox' ) {
						metawidget.util.appendToAttribute( widget, 'class', 'form-control' );
					}

					if ( attributes.inputPrepend !== undefined || attributes.inputAppend !== undefined ) {
						var div = metawidget.util.createElement( mw, 'div' );
						var span;
						if ( attributes.inputPrepend !== undefined ) {
							div.setAttribute( 'class', 'input-prepend input-group' );
							span = metawidget.util.createElement( mw, 'span' );
							span.setAttribute( 'class', 'add-on input-group-addon' );
							span.innerHTML = attributes.inputPrepend;
							div.appendChild( span );
						}
						div.appendChild( widget );
						if ( attributes.inputAppend !== undefined ) {
							if ( attributes.inputPrepend !== undefined ) {
								div.setAttribute( 'class', 'input-prepend input-append input-group' );
							} else {
								div.setAttribute( 'class', 'input-append input-group' );
							}
							span = metawidget.util.createElement( mw, 'span' );
							span.setAttribute( 'class', 'add-on input-group-addon' );
							span.innerHTML = attributes.inputAppend;
							div.appendChild( span );
						}
						return div;
					}
				}
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
	 *        CSS classes such as 'form-group' and 'control-label' to the divs.
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

		if ( config.version === 2 ) {
			if ( config.divStyleClasses === undefined ) {
				config.divStyleClasses = [ 'control-group', undefined, 'controls' ];
			}
			if ( config.labelStyleClass === undefined ) {
				config.labelStyleClass = 'control-label';
			}
		} else {
			if ( config.divStyleClasses === undefined ) {
				config.divStyleClasses = [ 'form-group', 'col-sm-2 control-label', 'col-sm-10' ];
			}
			if ( config.widgetDivSpanAllClass === undefined ) {
				config.widgetDivSpanAllClass = 'col-sm-12';
			}
			if ( config.widgetDivOffsetClass === undefined ) {
				config.widgetDivOffsetClass = 'col-sm-offset-2';
			}
		}

		var layout = new metawidget.layout.DivLayout( config );

		// If there is no label, Bootstrap 3 requires an explicit grid position
		// to be set or the widget div will not automatically 'pull right'

		if ( config.version !== 2 ) {
			var superLayoutWidget = layout.layoutWidget;
			layout.layoutWidget = function( widget, elementName, attributes, container, mw ) {

				superLayoutWidget.call( this, widget, elementName, attributes, container, mw );

				var outerDiv = container.childNodes[container.childNodes.length - 1];
				if ( outerDiv.childNodes.length === 1 ) {
					if ( attributes.title === null ) {
						outerDiv.childNodes[0].setAttribute( 'class', config.widgetDivSpanAllClass );
					} else {
						metawidget.util.appendToAttribute( outerDiv.childNodes[0], 'class', config.widgetDivOffsetClass );
					}
				}
			};
		}

		return layout;
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

		// If Bootstrap is used with AngularJS, target=_self stops Angular from
		// rewriting this link:
		// https://groups.google.com/forum/#!topic/angular/yKv8jXYBsBI

		a.setAttribute( 'target', '_self' );
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