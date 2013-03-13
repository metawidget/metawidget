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

	metawidget.test.allWidgetsMetadata = [ {
		name: "textbox",
		type: "string",
		lookup: "",
		masked: "",
		large: "",
		maximumLength: "",
		required: "true"
	}, {
		name: "limitedTextbox",
		type: "string",
		maximumLength: "20"
	}, {
		name: "textarea",
		type: "string",
		large: "true"
	}, {
		name: "password",
		type: "string",
		masked: "true"
	}, {
		name: "number",
		type: "number"
	}, {
		name: "rangedNumber",
		type: "number",
		minimumValue: "1",
		maximumValue: "100",
	}, {
		name: "toBeStubbed"
	}, {
		name: "boolean",
		type: "boolean"
	}, {
		name: "dropdown",
		lookup: "foo1,dropdown1,bar1",
		lookupLabels: ""
	}, {
		name: "dropdownWithLabels",
		lookup: "foo2,dropdown2,bar2,baz2",
		lookupLabels: "Foo #2,Dropdown #2,Bar #2,Baz #2"
	}, {
		name: "notNullDropdown",
		lookup: "-1,0,1",
		required: "true"
	}, {
		name: "nestedWidgets"
	}, {
		name: "readOnlyNestedWidgets",
		readOnly: "true"
	}, {
		name: "nestedWidgetsDontExpand",
		dontExpand: "true"
	}, {
		name: "readOnlyNestedWidgetsDontExpand",
		readOnly: "true",
		dontExpand: "true"
	}, {
		name: "date",
		type: "date"
	}, {
		name: "hidden",
		hidden: "true",
		type: "string"
	}, {
		name: "readOnly",
		readOnly: "true",
		type: "string",
		section: "Section Break"
	} ];

	metawidget.test.nestedWidgetsMetadata = [ {
		name: "furtherNestedWidgets"
	}, {
		name: "nestedTextbox1",
		type: "string"
	}, {
		name: "nestedTextbox2",
		type: "string"
	} ];
} )();