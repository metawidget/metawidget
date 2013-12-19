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

var metawidget = metawidget || {};

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

				// Configs cannot be 2-way ('=') because cannot 'watch' arrays

				configs: '&'
			},

			/**
			 * Metawidget must transclude, so that transcluded DOM doesn't have
			 * to reference 'ng-model'.
			 */

			transclude: true,

			/**
			 * Angular compile function. Configures an Angular-specific
			 * Metawidget and invokes buildWidgets on it.
			 */

			compile: function compile( element, attrs, transclude ) {

				return function( scope, element, attrs ) {

					// Set up an AngularMetawidget

					var mw = new metawidget.angular.AngularMetawidget( element, attrs, transclude, scope, $compile, $parse );

					// Build

					var _oldToInspect;
					_buildWidgets();

					// Observe

					scope.$watch( 'ngModel', function( newValue ) {

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

					scope.$watch( 'readOnly', function( newValue ) {

						// Test against mw.readOnly, not oldValue, because it
						// may have been reset already by _buildWidgets

						if ( newValue !== mw.readOnly ) {
							// Do not mw.invalidateInspection()
							_buildWidgets();
						}
					} );

					scope.$watch( 'config', function( newValue, oldValue ) {

						// Watch for config changes. These are rare, but
						// otherwise we'd need to provide a way to externally
						// trigger _buildWidgets

						if ( newValue !== oldValue ) {
							mw.configure( newValue );
							_buildWidgets();
						}
					} );

					//
					// Private method
					//

					function _buildWidgets() {

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

	var _nestedMetawidgetConfigId = 0;

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
			// Note: we ignore transcluded widgets. Compiling them again using
			// $compile seemed to trigger 'ng-click' listeners twice?

			if ( widget.overridden === undefined ) {
				$compile( widget )( scope.$parent );
			}
		};

		var _lastInspectionResult;

		this.invalidateInspection = function() {

			_lastInspectionResult = undefined;
		};

		// Configure defaults

		_pipeline.inspector = new metawidget.inspector.PropertyTypeInspector();
		_pipeline.inspectionResultProcessors = [ new metawidget.angular.inspectionresultprocessor.AngularInspectionResultProcessor( scope ) ];
		_pipeline.widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [ new metawidget.widgetbuilder.OverriddenWidgetBuilder(), new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(),
				new metawidget.widgetbuilder.HtmlWidgetBuilder() ] );
		_pipeline.widgetProcessors = [ new metawidget.widgetprocessor.IdProcessor(), new metawidget.widgetprocessor.PlaceholderAttributeProcessor(),
				new metawidget.widgetprocessor.DisabledAttributeProcessor(), new metawidget.angular.widgetprocessor.AngularWidgetProcessor( $parse, scope ) ];
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

				var childAttributes;
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

				_pipeline.layoutWidget( child, "property", childAttributes, _pipeline.element, this );
			}
		};

		/**
		 * Returns the element this Metawidget is attached to.
		 */

		this.getElement = function() {

			return _pipeline.element;
		};

		this.buildNestedMetawidget = function( attributes, config ) {

			var nestedMetawidget = metawidget.util.createElement( this, 'metawidget' );
			nestedMetawidget.setAttribute( 'ng-model', attrs.ngModel + '.' + attributes.name );
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
			// know what might be in it

			var configId = '_metawidgetConfig' + _nestedMetawidgetConfigId++;
			scope.$parent[configId] = _pipeline;

			if ( config !== undefined ) {
				var configId2 = '_metawidgetConfig' + _nestedMetawidgetConfigId++;
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
	 * @param scope
	 *            scope of the Metawidget directive
	 * @param buildWidgets
	 *            a function to use to rebuild the widgets following a $watch
	 * @returns {metawidget.angular.AngularInspectionResultProcessor}
	 */

	metawidget.angular.inspectionresultprocessor.AngularInspectionResultProcessor = function( scope ) {

		if ( ! ( this instanceof metawidget.angular.inspectionresultprocessor.AngularInspectionResultProcessor ) ) {
			throw new Error( "Constructor called as a function" );
		}

		this.processInspectionResult = function( inspectionResult, mw ) {

			mw._angularInspectionResultProcessor = mw._angularInspectionResultProcessor || [];

			// For each property in the inspection result...

			for ( var propertyName in inspectionResult ) {

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
			
			/**
			 * When a watched expression changes, reinspect and rebuild.
			 */
			
			function _watchExpression( newValue, oldValue ) {

				if ( newValue !== oldValue ) {
					
					// Clear all watches...
					
					for( var loop = 0, length = mw._angularInspectionResultProcessor.length; loop < length; loop++ ) {
						mw._angularInspectionResultProcessor[loop]();
					}
					
					// ..and then reinspect
					
					mw.invalidateInspection();
					mw.buildWidgets();
				}
			}
		};
	};

	/**
	 * @namespace WidgetProcessors for AngularJS environments.
	 */

	metawidget.angular.widgetprocessor = metawidget.angular.widgetprocessor || {};

	/**
	 * @class WidgetProcessor to add Angular bindings and validation.
	 *
	 * @returns {metawidget.angular.AngularWidgetProcessor}
	 */

	metawidget.angular.widgetprocessor.AngularWidgetProcessor = function( $parse, scope ) {

		if ( ! ( this instanceof metawidget.angular.widgetprocessor.AngularWidgetProcessor ) ) {
			throw new Error( "Constructor called as a function" );
		}

		this.processWidget = function( widget, elementName, attributes, mw ) {

			// Binding
			//
			// Scope the binding to scope.$parent, not scope, so that bindings
			// look more 'natural' (eg. 'foo.bar' not 'toInspect.bar')

			var binding = mw.path;

			if ( elementName !== 'entity' ) {
				binding += '.' + attributes.name;
			}

			if ( widget.tagName === 'OUTPUT' ) {
				if ( attributes.masked === true ) {

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

				} else {
					widget.setAttribute( 'ng-bind', binding );
				}

			} else if ( widget.tagName === 'INPUT' && ( widget.getAttribute( 'type' ) === 'button' || widget.getAttribute( 'type' ) === 'submit' ) ) {
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
							} else if ( child.getAttribute( 'type' ) === 'checkbox' ) {
								child.setAttribute( 'ng-checked', binding + ".indexOf('" + child.value + "')>=0" );
								scope.$parent.mwUpdateSelection = _updateSelection;
								child.setAttribute( 'ng-click', "mwUpdateSelection($event,'" + binding + "')" );
							}
						}
					}
				}
			} else {
				widget.setAttribute( 'ng-model', binding );
			}

			// Validation

			if ( attributes.required !== undefined ) {
				widget.setAttribute( 'ng-required', attributes.required );
			}

			if ( attributes.minLength !== undefined ) {
				widget.setAttribute( 'ng-minlength', attributes.minLength );
			}

			if ( attributes.maxLength !== undefined ) {
				widget.setAttribute( 'ng-maxlength', attributes.maxLength );

				// (maxlength set by WidgetBuilder)

				widget.removeAttribute( 'maxlength' );
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

			var selected = scope.$parent.$eval( binding );

			if ( selected === undefined ) {
				selected = [];
				$parse( binding ).assign( scope.$parent, selected );
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
	};
} )();