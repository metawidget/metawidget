'use strict';

/* Controllers */

function AllWidgetsController($scope) {

	$scope.metawidgetConfig = {

		inspector : new metawidget.CompositeInspector([ function(toInspect,
				type) {

			return [ {
				"name" : "textbox",
				"type" : "string"
			}, {
				"name" : "limitedTextbox",				
				"type" : "string",
				"maximumLength": "30"
			}, {
				"name" : "textarea",				
				"type" : "string",
				"large" : "true"					
			}, {
				"name" : "password",				
				"type" : "string",
				"masked" : "true"					
			}, {
				"name" : "boolean",				
				"type" : "boolean"
			}, {
				"name" : "dropdown",				
				"lookup" : "foo1,dropdown1,bar1"
			}, {
				"name" : "dropdownWithLabels",				
				"lookup" : "foo2,dropdown2,bar2,baz2",
				"lookupLabels": "Foo #2,Dropdown #2,Bar #2,Baz #2"
			}, {
				"name" : "notNullDropdown",
				"lookup" : "-1,0,1",
				"required": "true"
			}, /*{
				"name" : "nestedWidgets"
			},*/ {
				"name" : "hidden",
				"hidden": "true"
			} ];
		} ])
	};
}
