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
 * Web Component wrapper for Metawidget.
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

	if ( globalScope.document !== undefined && globalScope.document.registerElement !== undefined ) {

		var metawidgetPrototype = Object.create( HTMLElement.prototype );

		/**
		 * Upon attachedCallback, initialize an internal metawidget.Metawidget
		 * object using the current 'config' attribute (if any).
		 * <p>
		 * During initialization, a Metawidget create a shadow root so this must
		 * be called after the document is ready.
		 */

		metawidgetPrototype.attachedCallback = function() {

			// First time in, create a shadow root. This allows us to preserve
			// our original override nodes (if any)

			var shadowRoot = this.createShadowRoot();

			// Pipeline (private)

			this._pipeline = new metawidget.Pipeline( shadowRoot );

			// Configure defaults

			this._pipeline.inspector = new metawidget.inspector.PropertyTypeInspector();
			this._pipeline.widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [ new metawidget.widgetbuilder.OverriddenWidgetBuilder(),
					new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(), new metawidget.widgetbuilder.HtmlWidgetBuilder() ] );
			this._pipeline.widgetProcessors = [ new metawidget.widgetprocessor.IdProcessor(), new metawidget.widgetprocessor.RequiredAttributeProcessor(),
					new metawidget.widgetprocessor.PlaceholderAttributeProcessor(), new metawidget.widgetprocessor.DisabledAttributeProcessor(),
					new metawidget.widgetprocessor.SimpleBindingProcessor() ];
			this._pipeline.layout = new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.TableLayout() );
			this._pipeline.configure( [ _lookupObject.call( this, 'config' ), this.config ] );

			this.buildWidgets();
		};

		/**
		 * If 'path', 'readonly' or 'config' are updated, rebuild the
		 * Metawidget.
		 */

		metawidgetPrototype.attributeChangedCallback = function( attrName, oldVal, newVal ) {

			if ( this._pipeline === undefined ) {
				return;
			}

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
		};

		/**
		 * Clear all child elements from the shadow root.
		 */

		metawidgetPrototype.clearWidgets = function() {

			while ( this.shadowRoot.childNodes.length > 0 ) {
				this.shadowRoot.removeChild( this.shadowRoot.childNodes[0] );
			}
		};

		/**
		 * Rebuild the Metawidget, using the value of the current 'path'
		 * attribute.
		 * 
		 * @param inspectionResult
		 *            optional inspectionResult to use
		 */

		metawidgetPrototype.buildWidgets = function( inspectionResult ) {

			// Take a copy of the original nodes. These may be inserted into the
			// shadow DOM if the WidgetBuilders/Layouts wish

			this.overriddenNodes = [];

			for ( var loop = 0, length = this.childNodes.length; loop < length; loop++ ) {
				if ( this.childNodes[loop].nodeType === 1 ) {
					this.overriddenNodes.push( this.childNodes[loop].cloneNode( true ) );
				}
			}

			// Traverse and build

			if ( this.getAttribute( 'path' ) !== null ) {

				this.path = this.getAttribute( 'path' );
				this.readOnly = metawidget.util.isTrueOrTrueString( this.getAttribute( 'readonly' ) );

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
			}

			// Build widgets

			this._pipeline.buildWidgets( inspectionResult, this );

			// Note: we don't attempt to use Object.observe on this.toInspect,
			// at least not by default (clients could observe and call
			// buildWidgets if they want). AngularJS Metawidget does this, but
			// in Angular all sub-widgets are 2-way bound by default, so you
			// never risk losing data when you rebuild. In Web Components,
			// however, sub-widget values are only saved when clients call
			// save()
		};

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

			nestedMetawidget.setAttribute( 'path', metawidget.util.appendPath( attributes, this ) );
			nestedMetawidget.setAttribute( 'readonly', this.readOnly || metawidget.util.isTrueOrTrueString( attributes.readOnly ) );
			nestedMetawidget.config = this._pipeline;

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
			} ).save( this );
		};

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

		// Register Metawidget as a Web Component

		globalScope.document.registerElement( 'x-metawidget', {
			prototype: metawidgetPrototype
		} );
	}
} )( this );
