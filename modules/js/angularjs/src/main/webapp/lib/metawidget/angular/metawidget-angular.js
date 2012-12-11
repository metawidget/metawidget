// Metawidget
//
// This library is free software; you can redistribute it and/or
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

'use strict';

/**
 * Metawidget module.
 */

angular.module( 'metawidget.directives', [] )

/**
 * Angular directive to expose Metawidget.
 */

.directive(
		'metawidget',
		function( $compile ) {

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
					toInspect: '=',
					readOnly: '=',
					config: '='
				},

				/**
				 * Metawidget must transclude, so that transcluded DOM doesn't
				 * have to reference 'toInspect'.
				 */

				transclude: true,

				/**
				 * Angular compile function. Configures an Angular-specific
				 * Metawidget and invokes buildWidgets on it.
				 */

				compile: function compile( element, attrs, transclude ) {

					return function( scope, element, attrs ) {

						// Set up an Angular-specific Metawidget

						var mw = new metawidget.Metawidget( scope.$eval( 'config' ) );
						mw.buildNestedMetawidget = function( attributes ) {

							var nested = document.createElement( 'metawidget' );

							// Attributes are scoped to the directive, not
							// scope.$parent

							nested.setAttribute( 'to-inspect', 'toInspect.' + attributes.name );
							nested.setAttribute( 'read-only', 'readOnly' );
							nested.setAttribute( 'config', 'config' );
							return nested;
						};

						mw.inspectionResultProcessors.push( new metawidget.angular.AngularInspectionResultProcessor( scope, _buildWidgets ) );
						mw.widgetBuilder = new metawidget.CompositeWidgetBuilder( [ new metawidget.angular.AngularOverriddenWidgetBuilder( transclude, scope ), new metawidget.ReadOnlyWidgetBuilder(),
								new metawidget.HtmlWidgetBuilder() ] );
						mw.widgetProcessors.push( new metawidget.angular.AngularWidgetProcessor( $compile, scope ) );

						// Observe
						//
						// Do not observe simple types, such as 'string',
						// otherwise every keypress will recreate the widget

						if ( typeof ( scope.$eval( 'toInspect' ) ) == 'object' ) {
							scope.$watch( 'toInspect', function( newValue, oldValue ) {

								_buildWidgets();
							} );
						}
						scope.$watch( 'readOnly', function( newValue, oldValue ) {

							_buildWidgets();
						} );

						// Build

						function _buildWidgets() {

							// Invoke Metawidget

							mw.toInspect = scope.$eval( 'toInspect' );
							mw.path = attrs.toInspect;
							mw.readOnly = scope.$eval( 'readOnly' );
							var builtWidgets = mw.buildWidgets();

							// Clear all children (jqLite lacks .empty()?)

							element.children().remove();

							// Append the children of the Metawidget, to avoid a
							// repeat 'metawidget' tag

							element.append( angular.element( builtWidgets ).children() );
						}
					};
				}
			};
		} );

/**
 * Metawidget Angular namespace.
 */

metawidget.angular = metawidget.angular || {};

/**
 * InspectionResultProcessor to evaluate Angular expressions.
 * 
 * @param scope
 *            scope of the Metawidget directive
 * @param buildWidgets
 *            a function to use to rebuild the widgets following a $watch
 * @returns {metawidget.angular.AngularInspectionResultProcessor}
 */

metawidget.angular.AngularInspectionResultProcessor = function( scope, buildWidgets ) {

	if ( !( this instanceof metawidget.angular.AngularInspectionResultProcessor )) {
		throw new Error( "Constructor called as a function" );
	}
	
	this.processInspectionResult = function( inspectionResult, mw ) {

		for ( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {

			// For each attribute in the inspection result...

			var attributes = inspectionResult[loop];

			for ( var attribute in attributes ) {

				// ...that looks like an expression...

				var expression = attributes[attribute];

				if ( expression.length < 4 || expression.slice( 0, 2 ) != '{{' || expression.slice( expression.length - 2, expression.length ) != '}}' ) {
					continue;
				}

				// ...evaluate it...

				expression = expression.slice( 2, expression.length - 2 );
				attributes[attribute] = scope.$parent.$eval( expression ) + '';

				// ...and watch it for future changes

				scope.$parent.$watch( expression, function( newValue, oldValue ) {

					if ( newValue != oldValue ) {
						buildWidgets();
					}
				} );
			}
		}
	};
};

/**
 * WidgetBuilder to override widgets based on the original children in the
 * template (what Angular calls 'transcluded' children).
 * 
 * @param transclude
 *            the transclude function, as given to directive.compile
 * @returns {metawidget.angular.AngularOverriddenWidgetBuilder}
 */

metawidget.angular.AngularOverriddenWidgetBuilder = function( transclude, scope ) {

	if ( !( this instanceof metawidget.angular.AngularOverriddenWidgetBuilder )) {
		throw new Error( "Constructor called as a function" );
	}
	
	var transcluded = [];

	/**
	 * Rebuilds the transcluded tree at the start of each build.
	 * <p>
	 * Rebuilding only at the start of the <em>initial</em> build was
	 * sufficient for {{...}} expressions, but not 'ng-click' triggers.
	 */

	this.onStartBuild = function() {

		transcluded = transclude( scope.$parent, function( clone ) {

			return clone;
		} );
	};

	this.buildWidget = function( attributes, mw ) {

		for ( var loop = 0, length = transcluded.length; loop < length; loop++ ) {

			var child = transcluded[loop];
			if ( child.id == attributes.name ) {
				child.wasTranscluded = true;
				return child;
			}
		}
	};
};

/**
 * WidgetProcessor to add Angular bindings/validation, and compile the widget.
 * 
 * @returns {metawidget.angular.AngularWidgetProcessor}
 */

metawidget.angular.AngularWidgetProcessor = function( $compile, scope ) {

	if ( !( this instanceof metawidget.angular.AngularWidgetProcessor )) {
		throw new Error( "Constructor called as a function" );
	}

	this.processWidget = function( widget, attributes, mw ) {

		// Don't compile transcluded widgets, as they will be a) compiled
		// already; b) using scope.$parent

		if ( widget.wasTranscluded ) {
			return widget;
		}

		// Binding

		if ( mw.toInspect ) {
			var binding = 'toInspect';
	
			if ( attributes.name != '$root' ) {
				binding += '.' + attributes.name;
			}
	
			if ( widget.tagName == 'OUTPUT' ) {
				widget.innerHTML = '{{' + binding + '}}';
			} else if ( widget.tagName == 'BUTTON' ) {
				widget.setAttribute( 'ng-click', binding + '()' );
			} else {
				widget.setAttribute( 'ng-model', binding );
			}
		}

		// Validation

		if ( attributes.required ) {
			widget.setAttribute( 'ng-required', attributes.required );
		}

		if ( attributes.minimum - length ) {
			widget.setAttribute( 'ng-minlength', attributes.minimumLength );
		}

		if ( attributes.maximum - length ) {
			widget.setAttribute( 'ng-maxlength', attributes.maximumLength );
		}

		$compile( widget )( scope );

		return widget;
	};
};