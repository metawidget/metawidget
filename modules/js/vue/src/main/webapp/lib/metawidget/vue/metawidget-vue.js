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
 * @namespace Metawidget for pure Vue environments.
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

metawidget.vue = metawidget.vue || {};

/**
 * @namespace WidgetProcessors for Vue environments.
 */

metawidget.vue.widgetprocessor = metawidget.vue.widgetprocessor || {};

/**
 * @class WidgetProcessor to add Vue bindings.
 * 
 * @returns {metawidget.vue.widgetprocessor.VueWidgetProcessor}
 */

metawidget.vue.widgetprocessor.VueWidgetProcessor = function() {
	
	if ( ! ( this instanceof metawidget.vue.widgetprocessor.VueWidgetProcessor ) ) {
		throw new Error( "Constructor called as a function" );
	}
	
	this.processWidget = function( widget, elementName, attributes, mw ) {
	
		if ( widget.tagName === 'OUTPUT' ) {
			widget.setAttribute( 'v-text', mw.path + '.' + attributes.name );
		} else if ( widget.hasAttribute ( 'v-model' ) === false && widget.hasAttribute ( 'v-bind:value' ) === false ) {
			var attributeName = 'v-model';
			if ( attributes.type === 'number' || attributes.type === 'integer' ) {
				attributeName += '.number';
			}
			widget.setAttribute( attributeName, mw.path + '.' + attributes.name );
		}
		return widget;
	}
}

/**
 * @namespace Metawidget for Vue environments.
 */

module.exports = Vue.component( 'wc-metawidget', Vue.extend( {
	
	props: [ 'value', 'read-only', 'config' ],
	created: function() {
		// Create a top-level DIV
		
		var element = document.createElement( 'div' );
		
		for( var attributeName in this.$attrs ) {
			element.setAttribute( attributeName, this.$attrs[attributeName] );
		}
		
		// Pipeline (private)

		var pipeline = new metawidget.Pipeline( element );

		// Configure defaults

		pipeline.inspector = new metawidget.inspector.PropertyTypeInspector();
		pipeline.widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [ new metawidget.widgetbuilder.OverriddenWidgetBuilder(), new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(),
				new metawidget.widgetbuilder.HtmlWidgetBuilder() ] );
		pipeline.widgetProcessors = [ new metawidget.widgetprocessor.IdProcessor(), new metawidget.widgetprocessor.RequiredAttributeProcessor(),
				new metawidget.widgetprocessor.PlaceholderAttributeProcessor(), new metawidget.widgetprocessor.DisabledAttributeProcessor(),
				new metawidget.vue.widgetprocessor.VueWidgetProcessor() ];
		pipeline.layout = new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.TableLayout() );
		pipeline.configure( this.config );
		
		/**
		 * Useful for WidgetBuilders to perform nested inspections (eg. for Collections).
		 */
		
		this.inspect = function( toInspect, type, names ) {

			return pipeline.inspect( toInspect, type, names, this );
		};

		this.getElement = function() {
			
			return element;
		}
		
		this.clearWidgets = function() {
			element.innerHTML = '';
		}
		
		this.buildWidgets = function( inspectionResult ) {

			this.overriddenNodes = [];
			
			// Build widgets

			pipeline.buildWidgets( inspectionResult, this );
		};
		
		this.buildNestedMetawidget = function( attributes, config ) {

			var nestedMetawidget = metawidget.util.createElement( this, 'wc-metawidget' );
			nestedMetawidget.setAttribute( 'v-model', metawidget.util.appendPath( attributes, this ) );

			if ( metawidget.util.isTrueOrTrueString( attributes.readOnly ) ) {
				nestedMetawidget.setAttribute( 'read-only', 'true' );
			} else if ( metawidget.util.isTrueOrTrueString( this.readOnly )) {
				nestedMetawidget.setAttribute( 'read-only', 'true' );
			}

			return nestedMetawidget;
		};
	},
	render: function( createElement ) {
		
		this.toInspect = this.value;
		this.path = 'value';
		var inspectionResult = this.inspect( this.toInspect, this.path, undefined, this );
		this.buildWidgets( inspectionResult );
		var res = Vue.compile( this.getElement().outerHTML );
		this.$options.staticRenderFns = res.staticRenderFns;
		return res.render.call( this, createElement );
	}
} ) );
