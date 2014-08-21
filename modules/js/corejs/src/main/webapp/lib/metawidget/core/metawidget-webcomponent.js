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

if ( this.document !== undefined && this.document.registerElement !== undefined ) {

	( function( global ) {

		/**
		 * Use the value of the given HTML 5 attribute to lookup an object in
		 * scope. This includes traversing paths such as 'foo.bar'
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

			var toInspect = window[typeAndNames.type];
			return metawidget.util.traversePath( toInspect, typeAndNames.names );
		}

		var metawidgetPrototype = Object.create( HTMLElement.prototype );

		/**
		 * Upon createdCallback, initialize an internal metawidget.Metawidget
		 * using the current 'config' attribute (if any).
		 */

		metawidgetPrototype.createdCallback = function() {

			this.mw = new metawidget.Metawidget( this, _lookupObject.call( this, 'config' ) );
			this.buildWidgets();
		}

		/**
		 * If 'inspect' is pointed at a different path, rebuild the Metawidget.
		 */

		metawidgetPrototype.attributeChangedCallback = function( attrName, oldVal, newVal ) {

			this.buildWidgets();
		}

		/**
		 * Rebuild the Metawidget, using the current 'inspect' attribute.
		 */

		metawidgetPrototype.buildWidgets = function() {

			// Unobserve

			if ( this.observer !== undefined ) {
				Object.unobserve( this.mw.toInspect, this.observer );
			}

			// Traverse and build

			this.mw.toInspect = _lookupObject.call( this, 'inspect' );
			this.mw.buildWidgets();

			// Observe for next time

			var that = this;
			this.observer = function() {

				that.buildWidgets.call( that );
			}

			Object.observe( this.mw.toInspect, this.observer );
		}

		/**
		 * Save the contents of the Metawidget using a SimpleBindingProcessor.
		 */

		metawidgetPrototype.save = function() {

			this.mw.getWidgetProcessor( function( widgetProcessor ) {

				return widgetProcessor instanceof metawidget.widgetprocessor.SimpleBindingProcessor;
			} ).save( this.mw );
		}

		/**
		 * Upon removedCallback, cleanup any observers.
		 */

		metawidgetPrototype.removedCallback = function() {

			if ( this.observer !== undefined ) {
				Object.unobserve( this.mw.toInspect, this.observer );
			}
		}

		// Register Metawidget as a Web Component

		this.document.registerElement( 'x-metawidget', {
			prototype: metawidgetPrototype
		} );
	} ).call( this );
}
