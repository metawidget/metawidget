// Metawidget
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
 * @namespace Metawidget for pure JavaScript environments.
 */

var metawidget = metawidget || {};

( function() {

	'use strict';

	metawidget.test = metawidget.test || {};

	metawidget.test.allWidgets = {
		textbox: "Textbox",
		limitedTextbox: "Limited Textbox",
		textarea: "Textarea",
		password: "Password",
		number: 31,
		rangedNumber: 32,
		boolean: false,
		dropdown: "dropdown1",
		dropdownWithLabels: "dropdown2",
		notNullDropdown: "0",
		nestedWidgets: {
			furtherNestedWidgets: {
				nestedTextbox1: "Nested Textbox 1",
				nestedTextbox2: "Nested Textbox 2"
			},
			nestedTextbox1: "Nested Textbox 1",
			nestedTextbox2: "Nested Textbox 2"
		},
		readOnlyNestedWidgets: {
			furtherNestedWidgets: null,
			nestedTextbox1: "Nested Textbox 1",
			nestedTextbox2: "Nested Textbox 2"
		},
		nestedWidgetsDontExpand: {
			nestedTextbox1: "Nested Textbox 1",
			nestedTextbox2: "Nested Textbox 2"
		},
		readOnlyNestedWidgetsDontExpand: {
			nestedTextbox1: "Nested Textbox 1",
			nestedTextbox2: "Nested Textbox 2"
		},
		date: new Date( "9/4/1975" ),
		hidden: "Hidden",
		readOnly: "Read Only Value",
		collection: [ "element1", "element2" ],
		objectCollection: [ {
			name: "element1",
			description: "First"
		}, {
			name: "element2",
			description: "Second"
		} ]
	};

	/**
	 * Tests styles of metadata, such as boolean true versus string 'true'.
	 */

	metawidget.test.allWidgetsMetadata = {
		type: "allWidgets",
		"properties": {
			"textbox": {
				type: "string",
				enum: undefined,
				hidden: "",
				masked: "",
				large: "",
				maxLength: "",
				required: true,
				placeholder: "A Placeholder"
			},
			"limitedTextbox": {
				type: "string",
				maxLength: 20,
				hidden: false,
				masked: false,
				large: false
			},
			"textarea": {
				type: "string",
				large: true
			},
			"password": {
				type: "string",
				masked: true
			},
			"number": {
				type: "number"
			},
			"rangedNumber": {
				type: "number",
				minimum: 1,
				maximum: "100"
			},
			"toBeStubbed": {},
			"boolean": {
				type: "boolean"
			},
			"dropdown": {
				enum: [ "foo1", "dropdown1", "bar1" ],
				enumTitles: []
			},
			"dropdownWithLabels": {
				enum: [ "foo2", "dropdown2", "bar2", "baz2" ],
				enumTitles: [ "Foo #2", "Dropdown #2", "Bar #2", "Baz #2" ]
			},
			"notNullDropdown": {
				enum: [ "-1", "0", "1" ],
				required: true
			},
			"nestedWidgets": {},
			"readOnlyNestedWidgets": {
				readOnly: true
			},
			"nestedWidgetsDontExpand": {
				dontExpand: true
			},
			"readOnlyNestedWidgetsDontExpand": {
				readOnly: true,
				dontExpand: true
			},
			"date": {
				type: "date"
			},
			"hidden": {
				hidden: true,
				type: "string"
			},
			"readOnly": {
				readOnly: true,
				type: "string",
				section: "Section Break"
			},
		}
	};

	metawidget.test.nestedWidgetsMetadata = {
		"properties": {
			"furtherNestedWidgets": {},
			"nestedTextbox1": {
				type: "string"
			},
			"nestedTextbox2": {
				type: "string"
			}
		}
	};
} )();