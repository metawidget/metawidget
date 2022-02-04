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

var metawidget = ( typeof window !== 'undefined' ? window.metawidget : undefined ) || metawidget || {};

( function() {

	'use strict';

	/**
	 * Angular directive to expose <tt>metawidget.angular.AngularMetawidget</tt>.
	 */

	var directive = [ '$compile', '$parse', function( $compile, $parse ) {

		// Returns the Metawidget

		return {

			/**
			 * Metawidget is (E)lement level.
			 */

			restrict: 'E',

			/**
			 * Metawidget isolated scope.
			 */

			scope: {
				ngModel: '=',
				readOnly: '=',
				config: '=',
				ngShow: '=',
				ngHide: '=',

				// Configs cannot be 2-way ('=') because cannot 'watch' arrays

				configs: '&'
			},

			/**
			 * Metawidget must transclude child wigets, so that bindings in the
			 * child widgets can natually refer to names in our parent scope,
			 * rather than having to reference 'ng-model'.
			 */

			transclude: true,

			/**
			 * Angular compile function. Captures transcluded widgets, then
			 * returns a <tt>postLink</tt> function that configures an
			 * Angular-specific Metawidget and invokes buildWidgets on it.
			 */

			compile: function( element, attrs, transclude ) {

				// Return postLink function

				return function( scope, element, attrs ) {

					// Set up an AngularMetawidget

					var mw = new metawidget.angular.AngularMetawidget( element, attrs, transclude, scope, $compile, $parse );

					// Build

					var _oldToInspect = undefined;
					_buildWidgets();

					// Observe

					var _watchConfig = scope.$watch( 'config', function( newValue, oldValue ) {

						// Watch for config changes. These are rare, but
						// otherwise we'd need to provide a way to externally
						// trigger _buildWidgets
						//
						// Note: to be proper, we should process config changes
						// *before* data changes, in the event they both change
						// at once

						if ( newValue !== oldValue ) {
							mw.configure( newValue );
							_buildWidgets();
						}
					} );

					var _watchModel = scope.$watch( 'ngModel', function( newValue ) {

						// Cannot test against mw.toInspect, because is pointed
						// at the splitPath.type
						//
						// Re-inspect for 'undefined becoming defined' and
						// 'object being updated'. But *not* for 'undefined
						// becoming primitive, and then primitive being
						// updated'. Otherwise every keypress will recreate the
						// widget

						if ( newValue !== _oldToInspect && typeof ( newValue ) === 'object' ) {
							mw.invalidateInspection();
							_buildWidgets();
						}
					} );

					var _watchReadOnly = scope.$watch( 'readOnly', function( newValue ) {

						// Test against mw.readOnly, not oldValue, because it
						// may have been reset already by _buildWidgets

						if ( newValue !== mw.readOnly ) {
							// Do not mw.invalidateInspection()
							_buildWidgets();
						}
					} );

					var _watchNgShow = scope.$watch( 'ngShow', function( newValue, oldValue ) {

						if ( newValue !== oldValue ) {
							// Do not mw.invalidateInspection()
							_buildWidgets();
						}
					} );

					var _watchNgHide = scope.$watch( 'ngHide', function( newValue, oldValue ) {

						if ( newValue !== oldValue ) {
							// Do not mw.invalidateInspection()
							_buildWidgets();
						}
					} );

					// Clean up watches when element is destroyed

					element.on( '$destroy', function() {

						_watchConfig();
						_watchModel();
						_watchReadOnly();
						_watchNgShow();
						_watchNgHide();
					} );

					//
					// Private method
					//

					function _buildWidgets() {

						if ( scope.$eval( 'ngShow' ) === false || scope.$eval( 'ngHide' ) === true ) {
							return;
						}

						_oldToInspect = scope.$eval( 'ngModel' );

						mw.path = attrs.ngModel;
						mw.toInspect = scope.$parent.$eval( metawidget.util.splitPath( mw.path ).type );
						mw.readOnly = scope.$eval( 'readOnly' );
						mw.buildWidgets();

						// Note: when running under unit tests, errors get here.
						// However, testing for 'jasmine !== undefined' caused
						// problems at runtime
					}
				};
			}
		};
	} ];

	/**
	 * AngularJS Metawidget module.
	 */

	var module = angular.module( 'metawidget', [] );
	module.directive( 'metawidget', directive );

	/**
	 * Duplicate 'metawidget' directive, but with a namespace 'mw'. This allows
	 * clients wishing to support IE8 to use &lt;mw:metawidget&gt; as the tag
	 * name, as described at http://docs.angularjs.org/guide/ie
	 */

	module.directive( 'mwMetawidget', directive );

	/**
	 * @namespace Metawidget for AngularJS environments.
	 */

	metawidget.angular = metawidget.angular || {};

	metawidget.angular.AngularMetawidget = function( element, attrs, transclude, scope, $compile, $parse ) {

		if ( ! ( this instanceof metawidget.angular.AngularMetawidget ) ) {
			throw new Error( "Constructor called as a function" );
		}

		// Pipeline (private)

		var _pipeline = new metawidget.Pipeline( element[0] );
		_pipeline._superLayoutWidget = _pipeline.layoutWidget;

		_pipeline.layoutWidget = function( widget, elementName, attributes, container, mw ) {

			_pipeline._superLayoutWidget.call( this, widget, elementName, attributes, container, mw );

			// Compile so that 'ng-model', 'ng-required' etc become active. Do
			// this as late as possible, in case directives want to use
			// 'element.controller( 'form' )'
			//
			// Note: we do this here, rather than in onEndBuild, because we must
			// be particular about which widgets to $compile. In particular, we
			// must ignore transcluded widgets. Compiling them again using
			// $compile seemed to trigger 'ng-click' listeners twice?

			if ( widget.overridden === undefined ) {
				$compile( widget )( scope.$parent );
			}
		};

		var _lastInspectionResult = undefined;

		this.invalidateInspection = function() {

			_lastInspectionResult = undefined;
		};

		// Configure defaults

		_pipeline.inspector = new metawidget.inspector.PropertyTypeInspector();
		_pipeline.inspectionResultProcessors = [ new metawidget.angular.inspectionresultprocessor.AngularInspectionResultProcessor() ];
		_pipeline.widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [ new metawidget.widgetbuilder.OverriddenWidgetBuilder(), new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(),
				new metawidget.widgetbuilder.HtmlWidgetBuilder() ] );
		_pipeline.widgetProcessors = [ new metawidget.widgetprocessor.IdProcessor(), new metawidget.widgetprocessor.PlaceholderAttributeProcessor(),
				new metawidget.widgetprocessor.DisabledAttributeProcessor(), new metawidget.angular.widgetprocessor.AngularWidgetProcessor( $parse ) ];
		_pipeline.layout = new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.TableLayout() );

		this.configure = function( config ) {

			_pipeline.configure( config );
			this.invalidateInspection();
		};

		this.configure( scope.$eval( 'config' ) );
		this.configure( scope.configs() );

		// toInspect, path and readOnly set by _buildWidgets()

		/**
		 * Useful for WidgetBuilders to perform nested inspections (eg. for
		 * Collections).
		 */

		this.inspect = function( toInspect, type, names ) {

			return _pipeline.inspect( toInspect, type, names, this );
		};

		/**
		 * Overridden to use jqLite.empty (safer for memory leaks).
		 */

		this.clearWidgets = function() {

			var jqElement = angular.element( this.getElement() );

			if ( jqElement.empty !== undefined ) {
				jqElement.empty();
			} else {

				// Support older versions of Angular

				jqElement.html( '' );
			}
		};

		this.buildWidgets = function( inspectionResult ) {

			// Rebuild the transcluded tree at the start of each build.
			//
			// Rebuilding only at the start of the <em>initial</em>
			// build was sufficient for {{...}} expressions, but not
			// 'ng-click' triggers.

			var cloned = transclude( scope.$parent, function( clone ) {

				return clone;
			} );

			this.overriddenNodes = [];

			for ( var loop = 0; loop < cloned.length; loop++ ) {
				var cloneNode = cloned[loop];

				// Must check nodeType *and* other attributes,
				// because Angular wraps everything (even text
				// nodes) with a 'span class='ng-scope'' tag
				//
				// https://github.com/angular/angular.js/issues/1059

				if ( cloneNode.nodeType === 1 && ( cloneNode.tagName !== 'SPAN' || cloneNode.attributes.length > 1 ) ) {
					this.overriddenNodes.push( cloneNode );
				}
			}

			// Inspect (if necessary)

			if ( inspectionResult !== undefined ) {
				_lastInspectionResult = inspectionResult;
			} else if ( _lastInspectionResult === undefined ) {

				// Safeguard against improperly implementing:
				// http://blog.kennardconsulting.com/2013/02/metawidget-and-rest.html

				if ( arguments.length > 0 ) {
					throw new Error( "Calling buildWidgets( undefined ) may cause infinite loop. Check your argument, or pass no arguments instead" );
				}

				var splitPath = metawidget.util.splitPath( this.path );
				_lastInspectionResult = _pipeline.inspect( this.toInspect, splitPath.type, splitPath.names, this );
			}

			// Cleanup children using Angular, so that $destroy gets triggered
			// for nested Metawidgets

			element.children().remove();

			// Build widgets

			_pipeline.buildWidgets( _lastInspectionResult, this );
		};

		/**
		 * Overridden to inspect unused nodes by evaluating their 'ng-bind' or
		 * 'ng-model' attribute.
		 */

		this.onEndBuild = function() {

			while ( this.overriddenNodes.length > 0 ) {

				var child = this.overriddenNodes[0];
				this.overriddenNodes.splice( 0, 1 );

				// Unused facets don't count

				if ( child.tagName === 'FACET' ) {
					continue;
				}

				var childAttributes = {};
				var loop, length;

				// Lookup binding attribute
				//
				// Note: be sure to normalize it
				//
				// Note: be sure to lowercase it too, because HTML attribute
				// names are case-insensitive and Angular's template mechanism
				// lowercases them

				length = child.attributes.length;
				for ( loop = 0; loop < length; loop++ ) {
					var attribute = child.attributes[loop];
					var normalizedName = attrs.$normalize( attribute.name ).toLowerCase();

					if ( normalizedName === 'ngbind' || normalizedName === 'ngmodel' ) {
						var splitPath = metawidget.util.splitPath( attribute.value );
						var toInspect = scope.$parent.$eval( splitPath.type );
						childAttributes = _pipeline.inspect( toInspect, splitPath.type, splitPath.names, this );
						break;
					}
				}

				// Manually created components default to no section

				if ( childAttributes === undefined ) {
					childAttributes = {
						section: ''
					};
				}

				// Stubs can supply their own metadata (such as 'title')

				if ( child.tagName === 'STUB' ) {
					length = child.attributes.length;
					for ( loop = 0; loop < length; loop++ ) {
						var prop = child.attributes[loop];
						childAttributes[prop.nodeName] = prop.nodeValue;
					}
				}

				child = _pipeline.processWidget( child, "property", childAttributes, this );

				if ( child !== undefined ) {
					_pipeline.layoutWidget( child, "property", childAttributes, _pipeline.element, this );
				}
			}
		};

		/**
		 * Returns the element this Metawidget is attached to.
		 */

		this.getElement = function() {

			return _pipeline.element;
		};

		/**
		 * Returns the scope this Metawidget is attached to. This is identical
		 * to <code>$( mw.getElement() ).scope()</code>, but that is only
		 * available if <code>$compileProvider.debugInfoEnabled( true )</code>.
		 */

		this.getScope = function() {

			return scope;
		};

		this.buildNestedMetawidget = function( attributes, config ) {

			var nestedMetawidget = metawidget.util.createElement( this, 'metawidget' );
			nestedMetawidget.setAttribute( 'ng-model', metawidget.util.appendPath( attributes, this ) );

			if ( metawidget.util.isTrueOrTrueString( attributes.readOnly ) ) {
				nestedMetawidget.setAttribute( 'read-only', 'true' );
			} else if ( attrs.readOnly !== undefined ) {
				nestedMetawidget.setAttribute( 'read-only', attrs.readOnly );
			}

			// Duck-type our 'pipeline' as the 'config' of the nested
			// Metawidget. This neatly passes everything down, including a
			// decremented 'maximumInspectionDepth'
			//
			// Use a private counter to stop configIds conflicting. This is
			// because scope.$parent is a very broad scope - it's hard to
			// know what might be in it. We must use scope.$parent because we
			// $compile relative to our $parent. And we do *that* so that our
			// bindings look more 'natural' (eg. 'foo.bar' not 'toInspect.bar')

			scope.$parent._nestedMetawidgetConfigId = scope.$parent._nestedMetawidgetConfigId || 0;
			var configId = '_metawidgetConfig' + scope.$parent._nestedMetawidgetConfigId++;
			scope.$parent[configId] = _pipeline;

			if ( config !== undefined ) {
				var configId2 = '_metawidgetConfig' + scope.$parent._nestedMetawidgetConfigId++;
				scope.$parent[configId2] = config;
				nestedMetawidget.setAttribute( 'configs', '[' + configId + ',' + configId2 + ']' );
			} else {
				nestedMetawidget.setAttribute( 'config', configId );
			}

			return nestedMetawidget;
		};
	};

	/**
	 * @namespace InspectionResultProcessors for AngularJS environments.
	 */

	metawidget.angular.inspectionresultprocessor = metawidget.angular.inspectionresultprocessor || {};

	/**
	 * @class InspectionResultProcessor to evaluate Angular expressions.
	 * 
	 * @returns {metawidget.angular.AngularInspectionResultProcessor}
	 */

	metawidget.angular.inspectionresultprocessor.AngularInspectionResultProcessor = function() {

		if ( ! ( this instanceof metawidget.angular.inspectionresultprocessor.AngularInspectionResultProcessor ) ) {
			throw new Error( "Constructor called as a function" );
		}

		this.processInspectionResult = function( inspectionResult, mw ) {

			/**
			 * When a watched expression changes, reinspect and rebuild.
			 */

			function _watchExpression( newValue, oldValue ) {

				if ( newValue !== oldValue ) {

					// Clear all watches...

					for ( var loop = 0, length = mw._angularInspectionResultProcessor.length; loop < length; loop++ ) {
						mw._angularInspectionResultProcessor[loop]();
					}

					// ..and then reinspect

					mw.invalidateInspection();
					mw.buildWidgets();
				}
			}

			mw._angularInspectionResultProcessor = mw._angularInspectionResultProcessor || [];

			// For each property in the inspection result...

			var scope = mw.getScope();
			
			for ( var propertyName in inspectionResult ) {

				if ( !inspectionResult.hasOwnProperty( propertyName ) ) {
					continue;
				}

				// ...including recursing into 'properties'...

				var expression = inspectionResult[propertyName];

				if ( expression instanceof Object ) {
					this.processInspectionResult( expression, mw );
					continue;
				}

				// ...if the value looks like an expression...

				if ( expression === undefined || expression === null || expression.slice === undefined ) {
					continue;
				}

				if ( expression.length < 4 || expression.slice( 0, 2 ) !== '{{' || expression.slice( expression.length - 2, expression.length ) !== '}}' ) {
					continue;
				}

				// ...evaluate it...

				expression = expression.slice( 2, expression.length - 2 );
				
				inspectionResult[propertyName] = scope.$parent.$eval( expression ) + '';

				// ...and watch it for future changes

				var watch = scope.$parent.$watch( expression, _watchExpression );

				mw._angularInspectionResultProcessor.push( watch );
			}

			return inspectionResult;
		};
	};

	/**
	 * @namespace WidgetProcessors for AngularJS environments.
	 */

	metawidget.angular.widgetprocessor = metawidget.angular.widgetprocessor || {};

	/**
	 * @class WidgetProcessor to add Angular bindings and validation.
	 * 
	 * @returns {metawidget.angular.widgetprocessor.AngularWidgetProcessor}
	 */

	metawidget.angular.widgetprocessor.AngularWidgetProcessor = function( $parse ) {

		if ( ! ( this instanceof metawidget.angular.widgetprocessor.AngularWidgetProcessor ) ) {
			throw new Error( "Constructor called as a function" );
		}

		this.processWidget = function( widget, elementName, attributes, mw ) {

			// Binding
			//
			// Scope the binding to scope.$parent, not scope, so that the
			// generated bindings look more 'natural' (eg. 'foo.bar' not
			// 'toInspect.bar')

			var scope = mw.getScope();
			var binding = mw.path;
			var name = attributes.name;

			if ( name !== undefined || elementName === 'entity' ) {

				if ( elementName !== 'entity' ) {
					binding = metawidget.util.appendPathWithName( binding, attributes );
				}

				if ( widget.tagName === 'OUTPUT' ) {

					// Don't overwrite existing binding (if set by the
					// WidgetBuilder)

					if ( !widget.hasAttribute( 'ng-bind' ) ) {
						if ( metawidget.util.isTrueOrTrueString( attributes.masked ) ) {

							// Special support for masked output

							scope.$parent.mwMaskedOutput = _maskedOutput;
							widget.setAttribute( 'ng-bind', 'mwMaskedOutput(' + binding + ')' );
						} else if ( attributes.type === 'array' ) {

							// Special support for outputting arrays

							widget.setAttribute( 'ng-bind', binding + ".join(', ')" );
						} else if ( attributes.enumTitles !== undefined ) {

							// Special support for enumTitles

							scope.$parent.mwLookupEnumTitle = scope.$parent.mwLookupEnumTitle || {};
							scope.$parent.mwLookupEnumTitle[binding] = function( value ) {

								return metawidget.util.lookupEnumTitle( value, attributes['enum'], attributes.enumTitles );
							};
							widget.setAttribute( 'ng-bind', 'mwLookupEnumTitle["' + binding + '"](' + binding + ')' );

						} else if ( attributes.type === 'date' ) {

							// Special support for date formatting

							widget.setAttribute( 'ng-bind', binding + "|date" );

						} else {
							widget.setAttribute( 'ng-bind', binding );
						}
					}

				} else if ( widget.tagName === 'INPUT' && widget.getAttribute( 'type' ) === 'submit' ) {

					// input type='submit' should not be bound: should go via
					// ng-submit at the form level

					widget.removeAttribute( 'ng-click' );

				} else if ( ( widget.tagName === 'INPUT' && widget.getAttribute( 'type' ) === 'button' ) || widget.tagName === 'BUTTON' ) {

					widget.setAttribute( 'ng-click', binding + '()' );

				} else if ( attributes['enum'] !== undefined && ( attributes.type === 'array' || attributes.componentType !== undefined ) && widget.tagName === 'DIV' ) {

					// Special support for multi-selects and radio buttons

					for ( var loop = 0, length = widget.childNodes.length; loop < length; loop++ ) {
						var label = widget.childNodes[loop];

						if ( label.tagName === 'LABEL' && label.childNodes.length === 2 ) {
							var child = label.childNodes[0];

							if ( child.tagName === 'INPUT' ) {
								if ( child.getAttribute( 'type' ) === 'radio' ) {
									child.setAttribute( 'ng-model', binding );
									if ( child.value === true || child.value === 'true' ) {
										child.setAttribute( 'ng-value', 'true' );
									} else if ( child.value === false || child.value === 'false' ) {
										child.setAttribute( 'ng-value', 'false' );
									} else if ( isNaN( child.value ) === false ) {

										// Support numeric values

										child.setAttribute( 'ng-value', child.value );
									}
									
									// Angular imposes its own name
									
									child.removeAttribute( 'name' );

								} else if ( child.getAttribute( 'type' ) === 'checkbox' ) {
									child.setAttribute( 'ng-checked', binding + ".indexOf('" + child.value + "')>=0" );
									scope.mwUpdateSelection = _updateSelection;
									child.setAttribute( 'ng-click', "mwUpdateSelection($event,'" + binding + "')" );
								}
							}
						}
					}

				} else if ( widget.tagName === 'SELECT' ) {

					widget.setAttribute( 'ng-model', binding );

					// Special support for non-string selects
					//
					// We reuse the existing (non-Angular) HtmlWidgetBuilder to
					// build our SELECTs. This builds them using normal SELECT
					// and OPTION tags, without 'ng-options' or 'ng-value'.
					// Therefore we cannot rely on Angular's type conversions

					if ( attributes.type === 'boolean' || attributes.type === 'integer' || attributes.type === 'number' ) {

						if ( widget.hasAttribute( 'ng-options' )) {
							
							// ng-options, as of Angular 1.6, has its own type conversion
							
						} else {
													
							// Convert value to a string, and store within a temporary variable
	
							widget.setAttribute( 'ng-model', 'mwSelectedItems.' + attributes.name );
							scope.$parent.mwSelectedItems = scope.$parent.mwSelectedItems || {};
							scope.$parent.mwSelectedItems[attributes.name] = $parse( binding )( scope.$parent );
							if ( scope.$parent.mwSelectedItems[attributes.name] !== undefined ) {
								scope.$parent.mwSelectedItems[attributes.name] += '';
							} else {
								scope.$parent.mwSelectedItems[attributes.name] = '';
							}
	
							// When temporary variable changes, convert it back to correct type
	
							widget.setAttribute( 'ng-change', "mwChangeAsType('" + attributes.name + "','" + attributes.type + "','" + binding + "')" );
							scope.$parent.mwChangeAsType = function( name, type, binding ) {
	
								_changeAsType( scope.$parent, name, type, binding );
							}
	
							for ( var loop = 0, length = widget.childNodes.length; loop < length; loop++ ) {
	
								var child = widget.childNodes[loop];
	
								// Match each option based on the temporary string variable
	
								if ( child.tagName === 'OPTION' && child.getAttribute( 'value' ) !== null && child.getAttribute( 'value' ) !== '' ) {
									child.setAttribute( 'ng-selected', "mwSelectedItems." + attributes.name + "=='" + child.getAttribute( 'value' ) + "'" );
								}
							}
						}
					}

				} else if ( widget.tagName === 'INPUT' || widget.tagName === 'TEXTAREA' ) {
					// Don't overwrite existing binding (if set by the
					// WidgetBuilder)
					if ( !widget.hasAttribute( 'ng-model' ) ) {
						widget.setAttribute( 'ng-model', binding );
					}
				}
			}

			// Validation

			if ( !metawidget.util.isTrueOrTrueString( attributes.readOnly ) ) {

				if ( attributes.required !== undefined ) {
					widget.setAttribute( 'ng-required', attributes.required );
				}

				// (only add ng-minlength/ng-maxlength for INPUT/TEXTAREA, as
				// they need an ng-model)

				if ( widget.tagName === 'INPUT' || widget.tagName === 'TEXTAREA' ) {

					if ( attributes.minLength !== undefined ) {
						widget.setAttribute( 'ng-minlength', attributes.minLength );
					}

					if ( attributes.maxLength !== undefined ) {
						widget.setAttribute( 'ng-maxlength', attributes.maxLength );

						// (retain maxlength set by HtmlWidgetBuilder)
					}
				}
			}

			return widget;
		};

		//
		// Private methods
		//

		/**
		 * Special support for multi-select checkboxes.
		 */

		function _updateSelection( $event, binding ) {

			// Lookup the bound array (if any)...

			var selected = scope.$eval( binding );

			if ( selected === undefined ) {
				selected = [];
				$parse( binding ).assign( scope, selected );
			}

			// ...and either add our checkbox's value into it...

			var checkbox = $event.target;
			var indexOf = selected.indexOf( checkbox.value );

			if ( checkbox.checked === true ) {
				if ( indexOf === -1 ) {
					selected.push( checkbox.value );
				}
				return;
			}

			// ...or remove our checkbox's value from it

			if ( indexOf !== -1 ) {
				selected.splice( indexOf, 1 );
			}
		}

		/**
		 * Special support for masked output.
		 */

		function _maskedOutput( value ) {

			if ( value === undefined ) {
				return;
			}

			return metawidget.util.fillString( '*', value.length );
		}

		/**
		 * Special support for non-string selects.
		 */

		function _changeAsType( scope, name, type, binding ) {

			var parsedBinding = $parse( binding );

			if ( scope.mwSelectedItems[name] === '' || scope.mwSelectedItems[name] === null ) {
				parsedBinding.assign( scope, undefined );
				return;
			}

			if ( type === 'boolean' ) {
				parsedBinding.assign( scope, scope.mwSelectedItems[name] === 'true' || scope.mwSelectedItems[name] === true );
				return;
			}

			if ( type === 'integer' ) {
				parsedBinding.assign( scope, parseInt( scope.mwSelectedItems[name] ) );
				return;
			}

			if ( type === 'number' ) {
				parsedBinding.assign( scope, parseFloat( scope.mwSelectedItems[name] ) );
				return;
			}
		}
	};
} )();

if ( typeof module !== 'undefined' && typeof module.exports !== 'undefined' ) {
	module.exports = metawidget.angular;
}