'use strict';

var metawidget = metawidget || {};
metawidget.test = metawidget.test || {};

var allWidgets = {
	"textbox": "Textbox",
	"limitedTextbox": "Limited Textbox",
	"textarea": "Textarea",
	"password": "Password",
	"number": 31,
	"rangedNumber": 32,
	"boolean": false,
	"dropdown": "dropdown1",
	"dropdownWithLabels": "dropdown2",
	"notNullDropdown": "0",
	"nestedWidgets": {
		"nestedTextbox1": "Nested Textbox 1",
		"nestedTextbox2": "Nested Textbox 2",
		"furtherNestedWidgets": {
			"nestedTextbox1": "Nested Textbox 1",
			"nestedTextbox2": "Nested Textbox 2"
		}
	},
	"readOnlyNestedWidgets": {
		"nestedTextbox1": "Nested Textbox 1",
		"nestedTextbox2": "Nested Textbox 2"
	},
	"nestedWidgetsDontExpand": {
		"nestedTextbox1": "Nested Textbox 1",
		"nestedTextbox2": "Nested Textbox 2"
	},
	"readOnlyNestedWidgetsDontExpand": {
		"nestedTextbox1": "Nested Textbox 1",
		"nestedTextbox2": "Nested Textbox 2"
	},
	"date": new Date("9/4/1975"),
	"hidden": "Hidden",
	"readOnly": "Read Only Value"
};

metawidget.test.allWidgetsMetadata = [ {
	"name": "textbox",
	"type": "string"
}, {
	"name": "limitedTextbox",
	"type": "string",
	"maximumLength": "30"
}, {
	"name": "textarea",
	"type": "string",
	"large": "true"
}, {
	"name": "password",
	"type": "string",
	"masked": "true"
}, {
	"name": "number",
	"type": "number"
}, {
	"name": "rangedNumber",
	"type": "number",
	"minimumValue": "1",
	"maximumValue": "100",
}, {
	"name": "boolean",
	"type": "boolean"
}, {
	"name": "dropdown",
	"lookup": "foo1,dropdown1,bar1"
}, {
	"name": "dropdownWithLabels",
	"lookup": "foo2,dropdown2,bar2,baz2",
	"lookupLabels": "Foo #2,Dropdown #2,Bar #2,Baz #2"
}, {
	"name": "notNullDropdown",
	"lookup": "-1,0,1",
	"required": "true"
}, {
	"name": "nestedWidgets"
}, {
	"name": "readOnlyNestedWidgets",
	"readOnly": "true"
}, {
	"name": "nestedWidgetsDontExpand",
	"dontExpand": "true"
}, {
	"name": "readOnlyNestedWidgetsDontExpand",
	"readOnly": "true",
	"dontExpand": "true"
}, {
	"name": "date",
	"type": "date"
}, {
	"name": "hidden",
	"hidden": "true",
	"type": "string"
}, {
	"name": "readOnly",
	"readOnly": "true",
	"type": "string"
} ];

metawidget.test.nestedWidgetsMetadata = [ {
	"name": "nestedTextbox1",
	"type": "string"
}, {
	"name": "nestedTextbox2",
	"type": "string"
}, {
	"name": "furtherNestedWidgets"
} ];
