// Metawidget (licensed under LGPL)
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
		readOnly: "Read Only Value"
	};

	metawidget.test.allWidgetsMetadata = {
		"properties": {
			"textbox": {
				type: "string",
				enum: undefined,
				masked: "",
				large: "",
				maxLength: "",
				required: "true"
			},
			"limitedTextbox": {
				type: "string",
				maxLength: "20"
			},
			"textarea": {
				type: "string",
				large: "true"
			},
			"password": {
				type: "string",
				masked: "true"
			},
			"number": {
				type: "number"
			},
			"rangedNumber": {
				type: "number",
				minimum: "1",
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
				required: "true"
			},
			"nestedWidgets": {},
			"readOnlyNestedWidgets": {
				readOnly: "true"
			},
			"nestedWidgetsDontExpand": {
				dontExpand: "true"
			},
			"readOnlyNestedWidgetsDontExpand": {
				readOnly: "true",
				dontExpand: "true"
			},
			"date": {
				type: "date"
			},
			"hidden": {
				hidden: "true",
				type: "string"
			},
			"readOnly": {
				readOnly: "true",
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