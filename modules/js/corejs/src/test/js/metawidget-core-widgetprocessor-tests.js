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

describe( "The IdProcessor", function() {

	it( "assigns ids to widgets", function() {

		var processor = new metawidget.widgetprocessor.IdProcessor();

		var widget = document.createElement( 'input' );
		var mw = {};
		processor.processWidget( widget, {
			"name": "foo"
		}, mw );
		expect( widget.toString() ).toBe( 'input id="foo"' );

		// With subpath

		widget = document.createElement( 'input' );
		mw.path = 'foo.bar';
		processor.processWidget( widget, {
			"name": "baz"
		}, mw );
		expect( widget.toString() ).toBe( 'input id="fooBarBaz"' );
		
		// With root

		widget = document.createElement( 'input' );		
		mw.path = 'foo.bar';		
		processor.processWidget( widget, {
			"name": "$root"
		}, mw );
		expect( widget.toString() ).toBe( 'input id="fooBar"' );
	} );

	it( "does not reassigns ids", function() {

		var processor = new metawidget.widgetprocessor.IdProcessor();

		var widget = document.createElement( 'input' );
		widget.setAttribute( 'id', 'do-not-touch' );
		var mw = {};
		processor.processWidget( widget, {
			"name": "foo"
		}, mw );
		expect( widget.toString() ).toBe( 'input id="do-not-touch"' );
	} );
} );

describe( "The RequiredAttributeProcessor", function() {

	it( "assigns the required attribute to widgets", function() {

		var processor = new metawidget.widgetprocessor.RequiredAttributeProcessor();

		var widget = document.createElement( 'input' );
		var mw = {};

		processor.processWidget( widget, {}, mw );
		expect( widget.getAttribute( 'required' ) ).toBeUndefined();

		processor.processWidget( widget, {
			"required": "false"
		}, mw );
		expect( widget.getAttribute( 'required' ) ).toBeUndefined();

		processor.processWidget( widget, {
			"required": "true"
		}, mw );
		expect( widget.getAttribute( 'required' ) ).toBe( 'required' );
	} );
} );

describe( "The SimpleBindingProcessor", function() {

	it( "processes widgets and binds them", function() {

		var processor = new metawidget.widgetprocessor.SimpleBindingProcessor();
		var attributes = {
			"name": "foo",
		};
		var mw = {
			"toInspect": {
				"foo": "fooValue",
				"bar": "barValue",
				"baz": "bazValue",
				"boolean": true
			},
			"path": "testPath"
		};

		processor.onStartBuild( mw );

		// Inputs

		var widget = document.createElement( 'input' );
		widget.setAttribute( 'id', 'fooId' );
		processor.processWidget( widget, attributes, mw );
		expect( widget.toString() ).toBe( 'input id="fooId" name="fooId" value="fooValue"' );

		// Buttons

		attributes = {
			"name": "bar"
		};
		widget = document.createElement( 'button' );
		processor.processWidget( widget, attributes, mw );
		expect( widget.toString() ).toBe( 'button onClick="return testPath.bar()"' );

		// Outputs

		widget = document.createElement( 'output' );
		processor.processWidget( widget, attributes, mw );
		expect( widget.toString() ).toBe( 'output' );
		expect( widget.innerHTML ).toBe( 'barValue' );

		// Textareas

		attributes = {
			"name": "baz"
		};
		widget = document.createElement( 'textarea' );
		processor.processWidget( widget, attributes, mw );
		expect( widget.toString() ).toBe( 'textarea' );
		expect( widget.innerHTML ).toBe( 'bazValue' );

		// Checkboxes

		attributes = {
			"name": "boolean"
		};
		widget = document.createElement( 'input' );
		widget.setAttribute( 'type', 'checkbox' );
		processor.processWidget( widget, attributes, mw );
		expect( widget.toString() ).toBe( 'input type="checkbox" checked="checked"' );
		
		// Root-level

		attributes = {
			"name": "$root"
		};
		widget = document.createElement( 'output' );
		processor.processWidget( widget, attributes, mw );
		expect( widget.toString() ).toBe( 'output' );
		expect( widget.innerHTML ).toBe( mw.toInspect );
		
		// TODO: nested widgets
	} );
} );
