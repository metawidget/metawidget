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
 * Web Components wrapper for Metawidget.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

var metawidget = metawidget || {};

( function( globalScope ) {

	'use strict';

	/**
	 * Use the value of the given HTML 5 attribute to lookup an object in the
	 * global scope. This includes traversing simple namespace paths such as
	 * 'foo.bar'
	 */

	function _lookupObject( attributeName ) {

		var attributeValue = this.getAttribute( attributeName );

		if ( attributeValue === null ) {
			return;
		}

		var typeAndNames = metawidget.util.splitPath( attributeValue );

		if ( typeAndNames === undefined ) {
			return;
		}

		var toInspect = globalScope[typeAndNames.type];
		return metawidget.util.traversePath( toInspect, typeAndNames.names );
	}

	/**
	 * Initialize an internal 'metawidget.Metawidget' object, that will be
	 * wrapped by this Web Component.
	 */
	
	function _initMetawidget() {

		new metawidget.Metawidget( this, _lookupObject.call( this, 'config' ) );
		this.buildWidgets();
	}

	if ( globalScope.document !== undefined && globalScope.document.registerElement !== undefined ) {

		var metawidgetPrototype = Object.create( HTMLElement.prototype );

		/**
		 * Upon createdCallback, initialize an internal metawidget.Metawidget
		 * object using the current 'config' attribute (if any).
		 */

		metawidgetPrototype.createdCallback = function() {

			_initMetawidget.call( this );
		}

		/**
		 * If 'inspect' is pointed at a different path, or 'readonly' or
		 * 'config' are updated, rebuild the Metawidget.
		 */

		metawidgetPrototype.attributeChangedCallback = function( attrName, oldVal, newVal ) {

			switch ( attrName ) {
				case 'config':
					_initMetawidget();
					break;
				case 'inspect':
					this.buildWidgets();
					break;
				case 'readonly':
					this.buildWidgets();
					break;
			}
		}

		/**
		 * Rebuild the Metawidget, using the value of the current 'inspect'
		 * attribute.
		 */

		metawidgetPrototype.buildWidgets = function() {

			// Unobserve

			var mw = this.getMetawidget();

			if ( this.observer !== undefined ) {
				Object.unobserve( mw.toInspect, this.observer );
			}

			// Traverse and build

			mw.toInspect = _lookupObject.call( this, 'inspect' );
			mw.readOnly = metawidget.util.isTrueOrTrueString( this.getAttribute( 'readonly' ) );
			mw.buildWidgets();

			// Observe for next time

			var that = this;
			this.observer = function() {

				that.buildWidgets.call( that );
			}

			Object.observe( mw.toInspect, this.observer );
		}

		/**
		 * Save the contents of the Metawidget using a SimpleBindingProcessor.
		 * <p>
		 * This is a convenience method. To access other Metawidget APIs,
		 * clients can use the 'getMetawidget' method. For example
		 * 'document.getElementById(...).getMeta.getWidgetProcessor(...)'
		 */

		metawidgetPrototype.save = function() {

			var mw = this.getMetawidget();

			mw.getWidgetProcessor( function( widgetProcessor ) {

				return widgetProcessor instanceof metawidget.widgetprocessor.SimpleBindingProcessor;
			} ).save( mw );
		}

		/**
		 * Upon removedCallback, cleanup any observers.
		 */

		metawidgetPrototype.removedCallback = function() {

			if ( this.observer !== undefined ) {
				Object.unobserve( this.getMetawidget().toInspect, this.observer );
			}
		}

		// Register Metawidget as a Web Component

		globalScope.document.registerElement( 'x-metawidget', {
			prototype: metawidgetPrototype
		} );
	}
} )( this );
