'use strict';

/* App Module */

angular.module('addressBook', ['addressBookServices'])
  .config(['$routeProvider', function( $routeProvider ) {
	  $routeProvider.
      	when('/contact/:contactId', {templateUrl: 'partials/contact-detail.html', controller: ContactController}).
      	when('', {templateUrl: 'partials/contact-none.html'}).
      	otherwise({redirectTo: ''});
  }])
  
  /**
   * Metawidget
   */
  
  .directive('metawidget', function( $compile ) {
	  return {

		  /**
		   * Metawidget is (E)lement level, and has parameters 'toInspect' (2-way)
		   * and 'readOnly' (1-way)
		   */
		  
		  restrict: 'E',
		  transclude: true,
		  scope: {
			  toInspect: '=',
			  readOnly: '@'
		  },
		  
		  /**
		   * Inspect the given 'toInspect' and build UI.
		   */
		  
		  link: function( scope, element, attrs ) {

			  var readOnly = false;
			  
			  attrs.$observe( 'readOnly', function( value ) {
				  
				  if ( value == readOnly ) {
					  return;
				  }
				  readOnly = value;
				  _buildWidgets( scope, element, attrs );
			  });
			  attrs.$observe( 'toInspect', function( value ) {
				  _buildWidgets( scope, element, attrs );
			  });
			  
			  function _buildWidgets( scope, element, attrs ) {
				  
				  // Inspector
				  
				  var inspectionResult = [];

				  for( var property in scope.$eval( 'toInspect' )) {
					  
					  var inspectedProperty = {};
					  inspectedProperty.name = property;
					  inspectedProperty.label = property.toUpperCase();
					  inspectedProperty.type = (typeof property);
					  
					  inspectionResult.push( inspectedProperty );
				  }
				  
				  // InspectionResultProcessor
				  
				  for( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {
					  
					  var property = inspectionResult[loop];
					  
					  if ( property.name == 'id' ) {
						  property.readOnly = true;
					  }
				  }
				  
				  console.log( inspectionResult );
				  
				  // Build/process/layout

				  element.html( '' );

				  for( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {
				  
					  var property = inspectionResult[loop];
					  
					  // WidgetBuilder
					  
					  var widget;
					  
					  if ( attrs.readOnly == 'true' || property.readOnly ) {
						  widget = angular.element( '<span/>' );
						  widget.text( '{{toInspect.' + property.name + '}}' );
					  } else {
						  widget = angular.element( '<input/>' );
						  widget.attr( 'type', 'text' );
						  widget.attr( 'ng-model', 'toInspect.' + property.name );
					  }

					  // WidgetProcessor

					  widget.attr( 'id', property.name );
					  
					  // Layout
					  
					  var label = angular.element( '<label/>' );
					  label.attr( 'for', property.name );
					  label.text( property.label + ':' );
					  
					  var div = angular.element( '<div/>' );
					  div.append( label );
					  div.append( widget );
					  
					  element.append( div );					  
				  }
				  
				  // Compile and apply to DOM
				  				  
				  $compile(element.contents())(scope);
			  };
		  }
	  };
  })
;

