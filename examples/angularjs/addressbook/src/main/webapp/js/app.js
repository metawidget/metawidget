'use strict';

/* App Module */

angular.module('addressBook', ['addressBookServices'])
  .config(['$routeProvider', function( $routeProvider ) {
	  $routeProvider.
      	when('/contact/:contactId', {templateUrl: 'partials/contact-detail.html', controller: ContactController}).
      	when('', {templateUrl: 'partials/contact-none.html'}).
      	otherwise({redirectTo: ''});
  }])
  
  /* Metawidget */
  
  .directive('metawidget', function( $compile ) {
	  return {		  
		  restrict: 'E',
		  transclude: true,
		  scope: {
			  toInspect: '=',
			  readOnly: '@'
		  },
		  link: function( scope, element, attrs ) {
			  attrs.$observe( 'readOnly', function( value ) {
				  
				  // Build the widgets
				  
				  var html = '';
				  
				  for( var property in scope.$eval( 'toInspect' )) {
				  
					  if ( value == 'true' ) {
						  html += '<div>';
						  html += '<label for="' + property + '">' + property + ':</label>';
						  html += '<span id="' + property + '">{{toInspect.' + property + '}}</span>';
						  html += '</div>';
					  } else {
						  html += '<div>';
						  html += '<label for="' + property + '">' + property + ':</label>';
						  html += '<input id="' + property + '" type="text" ng-model="toInspect.' + property + '"/>';
						  html += '</div>';
					  }
				  }
				  
				  // Compile and apply to DOM
				  
				  element.html( html );
				  $compile(element.contents())(scope);
			  })
		  }
	  }
  })
;

