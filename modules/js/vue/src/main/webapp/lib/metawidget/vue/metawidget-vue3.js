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
 * @namespace Metawidget for pure Vue 3 environments.
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

import { compile, h } from 'vue'

var metawidget = ( typeof window !== 'undefined' ? window.metawidget : undefined ) || metawidget || {};

export default {

	// Vue3 does not allow reading binding.expression, so cannot use
	// v-model (must pass path explicitly):
	// https://github.com/vuejs/core/issues/3107

	props: ['id', 'path', 'readOnly', 'config', 'configs'],
	data: function() {

		// "Vue doesn't allow dynamically adding root-level reactive properties", so declare a
		// generic one our WidgetBuilders can use if they need to

		return {
			reactive: {}
		}
	},
	beforeCreate: function() {

		// At the earliest opportunity, grab our contents

		this._overriddenNodes = [];
		this._overriddenVNodes = [];

		if ( this.$vnode !== undefined && this.$vnode.componentOptions.children !== undefined ) {

			// Iterate our virtual DOM contents and create synthetic DOM nodes for them

			for ( var loop = 0, length = this.$vnode.componentOptions.children.length; loop < length; loop++ ) {

				var vnode = this.$vnode.componentOptions.children[loop];
				var tagName;
				var hasChildren = false;

				if ( vnode.componentOptions !== undefined ) {
					tagName = vnode.componentOptions.tag;
					hasChildren = ( vnode.componentOptions.children !== undefined );
				} else if ( vnode.tag !== undefined ) {
					tagName = vnode.tag;
					hasChildren = ( vnode.children !== undefined );
				} else {
					continue;
				}

				var childNode = document.createElement( tagName );

				if ( vnode.data !== undefined ) {
					if ( vnode.data.attrs !== undefined ) {
						for ( var attrName in vnode.data.attrs ) {
							childNode.setAttribute( attrName, vnode.data.attrs[attrName] );
						}
					}
					if ( vnode.data.model !== undefined ) {
						childNode.setAttribute( 'id', metawidget.util.camelCase( vnode.data.model.expression.replace( '$', '' ).split( '.' ) ) );
					}
				}

				// Facets and stubs are special

				if ( tagName === 'facet' ) {
					var divNode = document.createElement( 'div' );
					divNode.setAttribute( 'metawidget-overridden-node', this._overriddenNodes.length );
					childNode.appendChild( divNode );
				} else {
					childNode.setAttribute( 'metawidget-overridden-node', this._overriddenNodes.length );

					if ( tagName === 'stub' && hasChildren ) {
						divNode = document.createElement( 'div' );
						childNode.appendChild( divNode );
					}
				}

				this._overriddenNodes.push( childNode );
				this._overriddenVNodes.push( vnode );
			}
		}
	},

	/**
	 * Use 'beforeMount' so that 'this.buildWidgets' is ready for 'render'
	 */

	beforeMount: function() {

		// Create a top-level DIV

		var element = document.createElement( 'div' );

		if ( this.$props.id !== undefined ) {
			element.setAttribute( 'id', this.$props.id );
		}

		for ( var attributeName in this.$attrs ) {
			element.setAttribute( attributeName, this.$attrs[attributeName] );
		}

		// Pipeline (private)

		var pipeline = new metawidget.Pipeline( element );

		var lastInspectionResult = undefined;

		this.invalidateInspection = function() {

			lastInspectionResult = undefined;
			this.lastRes = undefined;
		};

		// Configure defaults

		pipeline.inspector = new metawidget.inspector.PropertyTypeInspector();
		pipeline.inspectionResultProcessors = [new metawidget.vue.inspectionresultprocessor.VueInspectionResultProcessor()];
		pipeline.widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [new metawidget.widgetbuilder.OverriddenWidgetBuilder(), new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(), new metawidget.widgetbuilder.HtmlWidgetBuilder()] );
		pipeline.widgetProcessors = [new metawidget.widgetprocessor.IdProcessor(), new metawidget.widgetprocessor.RequiredAttributeProcessor(), new metawidget.widgetprocessor.PlaceholderAttributeProcessor(), new metawidget.widgetprocessor.DisabledAttributeProcessor(),
		new metawidget.vue.widgetprocessor.VueWidgetProcessor()];
		pipeline.layout = new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.TableLayout() );
		pipeline.configure( this.config );
		pipeline.configure( this.configs );

		//
		// Public methods
		//

		/**
		 * Useful for WidgetBuilders to perform nested inspections (eg. for Collections).
		 */

		this.inspect = function( toInspect, type, names ) {

			return pipeline.inspect( toInspect, type, names, this );
		};

		this.getElement = function() {

			return element;
		}

		/**
		 * Returns the component this Metawidget is attached to.
		 */

		var vm = this;

		this.getComponent = function() {

			return vm;
		};

		this.clearWidgets = function() {

			element.innerHTML = '';
		};

		this.buildWidgets = function( inspectionResult ) {

			this.lastRes = undefined;

			// Defensive copy

			this.overriddenNodes = [];

			for ( var loop = 0, length = this._overriddenNodes.length; loop < length; loop++ ) {
				this.overriddenNodes.push( this._overriddenNodes[loop].cloneNode( true ) );
			}

			// Inspect (if necessary)

			if ( inspectionResult !== undefined ) {
				lastInspectionResult = inspectionResult;
			} else if ( lastInspectionResult === undefined ) {

				// Safeguard against improperly implementing:
				// http://blog.kennardconsulting.com/2013/02/metawidget-and-rest.html

				if ( arguments.length > 0 ) {
					throw new Error( "Calling buildWidgets( undefined ) may cause infinite loop. Check your argument, or pass no arguments instead" );
				}

				var splitPath = metawidget.util.splitPath( this.path );
				lastInspectionResult = pipeline.inspect( this.toInspect, splitPath.type, splitPath.names, this );
			}

			// Create the child 'just in time' if necessary, otherwise v-model balks

			if ( lastInspectionResult !== undefined && lastInspectionResult.properties !== undefined ) {
				var typeAndNames = metawidget.util.splitPath( this.path );
				if ( typeAndNames.names !== undefined ) {
					var namesToParent = typeAndNames.names.slice( 0, typeAndNames.names.length - 1 );
					var lastName = typeAndNames.names[typeAndNames.names.length - 1];
					var parent = metawidget.util.traversePath( this.toInspect, namesToParent );
					if ( parent[lastName] === undefined ) {
						parent[lastName] = {};
					}
				}
			}

			this.clearWidgets();

			if ( this.inRenderFunction === undefined ) {

				// Support inspectors that lazy-load the schema

				this.$forceUpdate();
			} else {
				pipeline.buildWidgets( lastInspectionResult, this );
			}
		};

		this.buildNestedMetawidget = function( attributes, config ) {

			// (do not use metawidget.util.createElement, as it uppercases the node name, which means that Vue doesn't match it as a component)

			var nestedMetawidget;

			if ( this.ownerDocument !== undefined ) {
				nestedMetawidget = this.ownerDocument.createElement( 'metawidget' );
			} else {
				nestedMetawidget = this.getElement().ownerDocument.createElement( 'metawidget' );
			}

			nestedMetawidget.setAttribute( 'path', metawidget.util.appendPath( attributes, this ) );

			if ( metawidget.util.isTrueOrTrueString( attributes.readOnly ) ) {
				nestedMetawidget.setAttribute( 'read-only', 'true' );
			} else if ( metawidget.util.isTrueOrTrueString( this.readOnly ) ) {
				nestedMetawidget.setAttribute( 'read-only', 'true' );
			}

			// Duck-type our 'pipeline' as the 'config' of the nested
			// Metawidget. This neatly passes everything down, including a
			// decremented 'maximumInspectionDepth'

			this.$data._nestedMetawidgetConfigId = this.$data._nestedMetawidgetConfigId || 0;
			var configId = '_metawidgetConfig' + this.$data._nestedMetawidgetConfigId++;
			this.$data[configId] = pipeline;

			if ( config !== undefined ) {
				var configId2 = '_metawidgetConfig' + this.$data._nestedMetawidgetConfigId++;
				this.$data[configId2] = config;
				nestedMetawidget.setAttribute( 'v-bind:configs', '[' + configId + ',' + configId2 + ']' );
			} else {
				nestedMetawidget.setAttribute( 'v-bind:config', configId );
			}

			return nestedMetawidget;
		};

		// Observe

		var numberOfConfigChanges = 0;

		this.$watch( 'config', function( newValue ) {

			// If v-bind:config is pointed at a function, it will fire repeatedly

			if ( numberOfConfigChanges++ > 10 ) {
				throw new Error( "Metawidget config changed too many times" );
			}

			pipeline.configure( newValue );
			this.invalidateInspection();
			this.buildWidgets();
		} );

		// We suppress re-render for every child change (see below), but
		// still force it on a top-level change

		const that = this;

		this.pathWatcher = this.$parent.$watch( this.path, function( newValue, oldValue ) {

			if ( newValue !== oldValue || Array.isArray( newValue ) ) {
				that.buildWidgets();
			}
		} );

		this.$watch( 'readOnly', function( newValue, oldValue ) {

			if ( newValue !== oldValue || Array.isArray( newValue ) ) {
				this.buildWidgets();
			}
		} );
	},
	beforeUnmount() {
		
		// Since path watchers are defined on the parent, we need to clean them up
		
		try {
			this.pathWatcher();
		} catch( e ) {
			// TODO: fails sometimes?
		}
	},
	render: function() {

		// Vue will call 'render' whenever any part of our reactive model changes, which will
		// be every keypress on a child input box, or click on a select box. This is too often
		// to be calling buildWidgets

		var rendered;

		if ( this.lastRes !== undefined ) {
			rendered = h( this.lastRes, this.$data, {} );
		} else {

			// Build the path to inspect. This must be expressed from the
			// context level, so that we can support Metawidgets pointed
			// directly at a property (e.g. v-model="person.firstname")

			var splitPath = metawidget.util.splitPath( this.path );
			this.toInspect = undefined;
			var parent = this.$.vnode.component.parent;

			while ( this.toInspect === undefined && parent !== undefined && parent !== null ) {
				this.toInspect = parent.data[splitPath.type];
				if ( this.toInspect === undefined ) {
					parent = parent.vnode.component.parent;
				}
			}

			// Ideally we will be able to resolve the expression using $vnode.context. However this
			// won't work for synthetic variables like those created by 'v-for'. For those, dummy
			// up a path. This will only work if the value is an object

			if ( this.toInspect === undefined ) {
				if ( splitPath.names === undefined ) {
					this.toInspect = this.value;
				} else {
					this.toInspect = {};

					for ( var loop = 0, length = splitPath.names.length; loop < length - 1; loop++ ) {
						var newToInspect = {};
						this.toInspect[splitPath.names[loop]] = newToInspect;
						this.toInspect = newToInspect;
					}

					this.toInspect[splitPath.names[splitPath.names.length - 1]] = this.value;
				}
			}

			// Copy the parent's scope into our Metawidget, so that v-model expressions
			// can be written more naturally

			if ( parent !== null ) {
				for ( var key in parent.data ) {
					if ( Object.prototype.hasOwnProperty.call( parent.data, key ) ) {
						this.$data[key] = parent.data[key];
					}
				}
				this.$data.$emit = this.$emit;
			}

			// Render the result

			this.inRenderFunction = true;
			this.buildWidgets();

			var res = compile( this.getElement().outerHTML );
			this.lastRes = res;

			// Clear _staticTrees to prevent corrupt re-render upon lazy-loaded schema. Note: this is an undocumented API, so may break in the future

			//this._staticTrees = [];
			//this.$options.staticRenderFns = res.staticRenderFns;
			rendered = h( res, this.$data, {} );
			this.inRenderFunction = undefined;
		}

		// TODO: walk the virtual DOM and replace any overridden nodes

		/*var self = this;

		function replaceVNode( children, index, toReplace ) {

			if ( toReplace === undefined ) {
				return;
			}

			if ( toReplace.tag === 'stub' ) {
				children[index] = toReplace.children[0];
				return;
			}

			children[index] = toReplace;
		}

		function findAndReplaceVNodes( vnode ) {

			if ( vnode === undefined ) {
				return;
			}

			if ( vnode.componentOptions !== undefined && vnode.componentOptions.children !== undefined ) {
				for ( var loop = 0, length = vnode.componentOptions.children.length; loop < length; loop++ ) {
					replaceVNode( vnode.componentOptions.children, loop, findAndReplaceVNodes( vnode.componentOptions.children[loop] ) );
				}
			} else if ( vnode.children !== undefined ) {
				for ( loop = 0, length = vnode.children.length; loop < length; loop++ ) {
					replaceVNode( vnode.children, loop, findAndReplaceVNodes( vnode.children[loop] ) );
				}
			}

			if ( vnode.data !== undefined && vnode.data.attrs !== undefined ) {
				var overriddenIndex = vnode.data.attrs['metawidget-overridden-node'];
				if ( overriddenIndex !== undefined ) {
					return self._overriddenVNodes[overriddenIndex];
				}
			}
		}

		findAndReplaceVNodes( rendered );*/
		return rendered;
	}
}