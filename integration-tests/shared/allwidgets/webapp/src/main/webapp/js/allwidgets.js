'use strict';

var metawidget = metawidget || {};
metawidget.test = metawidget.test || {};

metawidget.test.allWidgets = [ {
	"name" : "textbox",
	"type" : "string"
}, {
	"name" : "limitedTextbox",
	"type" : "string",
	"maximumLength" : "30"
}, {
	"name" : "textarea",
	"type" : "string",
	"large" : "true"
}, {
	"name" : "password",
	"type" : "string",
	"masked" : "true"
}, {
	"name" : "number",
	"type" : "number"
}, {
	"name" : "rangedNumber",
	"type" : "number",
	"minimumValue" : "1",
	"maximumValue" : "100",
}, {
	"name" : "boolean",
	"type" : "boolean"
}, {
	"name" : "dropdown",
	"lookup" : "foo1,dropdown1,bar1"
}, {
	"name" : "dropdownWithLabels",
	"lookup" : "foo2,dropdown2,bar2,baz2",
	"lookupLabels" : "Foo #2,Dropdown #2,Bar #2,Baz #2"
}, {
	"name" : "notNullDropdown",
	"lookup" : "-1,0,1",
	"required" : "true"
}, {
	"name" : "nestedWidgets"
}, {
	"name" : "date",
	"type": "date"
}, {
	"name" : "hidden",
	"hidden" : "true",
	"type": "string"
}, {
	"name" : "readOnly",
	"readOnly" : "true",
	"type" : "string"
} ];