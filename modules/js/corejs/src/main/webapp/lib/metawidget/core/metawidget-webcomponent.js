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

		var lookup = globalScope[typeAndNames.type];
		return metawidget.util.traversePath( lookup, typeAndNames.names );
	}

	/**
	 * Unobserves the currently observed 'path' (if any).
	 */

	function _unobserve() {

		if ( this.observer === undefined ) {
			return;
		}

		Object.unobserve( this.getMetawidget().toInspect, this.observer );
		this.observer = undefined;
	}

	if ( globalScope.document !== undefined && globalScope.document.registerElement !== undefined ) {

		var metawidgetPrototype = Object.create( HTMLElement.prototype );

		/**
		 * Upon attachedCallback, initialize an internal metawidget.Metawidget
		 * object using the current 'config' attribute (if any).
		 * <p>
		 * During initialization, a Metawidget will take a copy of any
		 * overridden child nodes, so this must be called after the document is
		 * ready.
		 */

		metawidgetPrototype.attachedCallback = function() {

			// Pipeline (private)

			this._pipeline = new metawidget.Pipeline( this );

			// Configure defaults

			this._pipeline.inspector = new metawidget.inspector.PropertyTypeInspector();
			this._pipeline.widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [ new metawidget.widgetbuilder.OverriddenWidgetBuilder(), new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(),
					new metawidget.widgetbuilder.HtmlWidgetBuilder() ] );
			this._pipeline.widgetProcessors = [ new metawidget.widgetprocessor.IdProcessor(), new metawidget.widgetprocessor.RequiredAttributeProcessor(),
					new metawidget.widgetprocessor.PlaceholderAttributeProcessor(), new metawidget.widgetprocessor.DisabledAttributeProcessor(), new metawidget.widgetprocessor.SimpleBindingProcessor() ];
			this._pipeline.layout = new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.TableLayout() );
			this._pipeline.configure( _lookupObject.call( this, 'config' ) );

			// First time in, capture the contents of the Metawidget, if any
			// (private)

			this._overriddenNodes = [];

			while ( this.childNodes.length > 0 ) {
				var childNode = this.childNodes[0];
				this.removeChild( childNode );

				if ( childNode.nodeType === 1 ) {
					this._overriddenNodes.push( childNode );
				}
			}

			this.buildWidgets();
		}

		/**
		 * If 'path', 'readonly' or 'config' are updated, rebuild the
		 * Metawidget.
		 */

		metawidgetPrototype.attributeChangedCallback = function( attrName, oldVal, newVal ) {

			switch ( attrName ) {
				case 'path':
					this.buildWidgets();
					break;
				case 'readonly':
					this.buildWidgets();
					break;
				case 'config':
					this._pipeline.configure( _lookupObject.call( this, 'config' ) );
					break;
			}
		}

		/**
		 * Clear all child elements from the Metawidget element.
		 * <p>
		 * This implementation uses plain JavaScript <tt>removeChild</tt>,
		 * which has known problems (on some browsers) leaking event handlers.
		 * This is not a problem for plain Metawidget, as it doesn't use event
		 * handlers. However clients that introduce custom widgetprocessors that
		 * use event handlers may wish to adopt a more robust technology for
		 * tracking/clearing event handlers (such as JQuery.empty)
		 */

		metawidgetPrototype.clearWidgets = function() {

			while ( this.childNodes.length > 0 ) {
				this.removeChild( this.childNodes[0] );
			}			
		}
		
		/**
		 * Rebuild the Metawidget, using the value of the current 'path'
		 * attribute.
		 */

		metawidgetPrototype.buildWidgets = function( inspectionResult ) {

			// Unobserve

			_unobserve.call( this );

			// Traverse and build

			this.path = this.getAttribute( 'path' );
			this._pipeline.readOnly = metawidget.util.isTrueOrTrueString( this.getAttribute( 'readonly' ) );

			// Defensive copy
			
			this.overriddenNodes = [];

			for ( var loop = 0, length = this._overriddenNodes.length; loop < length; loop++ ) {
				this.overriddenNodes.push( this._overriddenNodes[loop].cloneNode( true ) );
			}
			
			// Inspect (if necessary)

			if ( inspectionResult === undefined ) {

				// Safeguard against improperly implementing:
				// http://blog.kennardconsulting.com/2013/02/metawidget-and-rest.html

				if ( arguments.length > 0 ) {
					throw new Error( "Calling buildWidgets( undefined ) may cause infinite loop. Check your argument, or pass no arguments instead" );
				}

				var splitPath = metawidget.util.splitPath( this.path );
				this.toInspect = globalScope[splitPath.type];
				inspectionResult = this._pipeline.inspect( this.toInspect, splitPath.type, splitPath.names, this );
			}

			// Build widgets

			this._pipeline.buildWidgets( inspectionResult, this );
			
			// Observe for next time. toInspect may be undefined because
			// Metawidget can be used purely for layout

			if ( this.toInspect !== undefined ) {

				var that = this;
				this.observer = function() {

					that.buildWidgets.call( that );
				}

				Object.observe( this.toInspect, this.observer );
			}
		}

		/**
		 * Returns a nested version of this same Metawidget, using the given
		 * attributes.
		 * <p>
		 * Subclasses should override this method to use their preferred widget
		 * creation methodology.
		 */

		metawidgetPrototype.buildNestedMetawidget = function( attributes, config ) {

			var nestedMetawidget = metawidget.util.createElement( this, 'x-metawidget' );

			// Duck-type our 'pipeline' as the 'config' of the nested
			// Metawidget. This neatly passes everything down, including a
			// decremented 'maximumInspectionDepth'

			nestedMetawidget.setAttribute( 'path', metawidget.util.appendPath( attributes, this ));
			nestedMetawidget.setAttribute( 'readonly', this.readOnly || metawidget.util.isTrueOrTrueString( attributes.readOnly ));

			return nestedMetawidget;
		};

		/**
		 * Save the contents of the Metawidget using a SimpleBindingProcessor.
		 * <p>
		 * This is a convenience method. To access other Metawidget APIs,
		 * clients can use the 'getWidgetProcessor' method
		 */

		metawidgetPrototype.save = function() {

			this.getWidgetProcessor( function( widgetProcessor ) {

				return widgetProcessor instanceof metawidget.widgetprocessor.SimpleBindingProcessor;
			} ).save( mw );
		}

		/**
		 * Useful for WidgetBuilders to perform nested inspections (eg. for
		 * Collections).
		 */

		metawidgetPrototype.inspect = function( toInspect, type, names ) {

			return this._pipeline.inspect( toInspect, type, names, this );
		};


		metawidgetPrototype.getWidgetProcessor = function( testInstanceOf ) {

			return this._pipeline.getWidgetProcessor( testInstanceOf );
		};
		
		metawidgetPrototype.setLayout = function( layout ) {

			this._pipeline.layout = layout;
		};

		/**
		 * Upon detachedCallback, cleanup any observers.
		 */

		metawidgetPrototype.detachedCallback = function() {

			_unobserve.call( this );
		}

		// Register Metawidget as a Web Component

		globalScope.document.registerElement( 'x-metawidget', {
			prototype: metawidgetPrototype
		} );
	}
} )( this );
