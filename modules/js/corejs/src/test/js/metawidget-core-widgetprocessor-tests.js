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

( function() {

	'use strict';

	describe( "The IdProcessor", function() {

		it( "assigns ids to widgets", function() {

			var processor = new metawidget.widgetprocessor.IdProcessor();

			var widget = document.createElement( 'input' );
			var mw = {};
			processor.processWidget( widget, {
				name: "foo"
			}, mw );
			expect( widget.toString() ).toBe( 'input id="foo"' );

			// With subpath

			widget = document.createElement( 'input' );
			mw.path = 'foo.bar';
			processor.processWidget( widget, {
				name: "baz"
			}, mw );
			expect( widget.toString() ).toBe( 'input id="fooBarBaz"' );

			// With root

			widget = document.createElement( 'input' );
			mw.path = 'foo.bar';
			processor.processWidget( widget, {
				_root: "true"
			}, mw );
			expect( widget.toString() ).toBe( 'input id="fooBar"' );
		} );

		it( "does not reassigns ids", function() {

			var processor = new metawidget.widgetprocessor.IdProcessor();

			var widget = document.createElement( 'input' );
			widget.setAttribute( 'id', 'do-not-touch' );
			var mw = {};
			processor.processWidget( widget, {
				name: "foo"
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
			expect( widget.hasAttribute( 'required' ) ).toBe( false );

			processor.processWidget( widget, {
				required: "false"
			}, mw );
			expect( widget.hasAttribute( 'required' ) ).toBe( false );

			processor.processWidget( widget, {
				required: "true"
			}, mw );
			expect( widget.getAttribute( 'required' ) ).toBe( 'required' );
		} );
	} );

	describe( "The SimpleBindingProcessor", function() {

		it( "processes widgets and binds them", function() {

			var processor = new metawidget.widgetprocessor.SimpleBindingProcessor();
			var attributes = {
				name: "foo",
			};
			var mw = {
				toInspect: {
					foo: "fooValue",
					bar: "barValue",
					baz: "bazValue",
					boolean: true,
					select: false
				},
				path: "testPath"
			};

			processor.onStartBuild( mw );

			// Inputs

			var widget = document.createElement( 'input' );
			widget.setAttribute( 'id', 'fooId' );
			processor.processWidget( widget, attributes, mw );
			expect( widget.toString() ).toBe( 'input id="fooId" name="fooId"' );
			expect( widget.value ).toBe( 'fooValue' );

			// Buttons

			attributes = {
				name: "bar"
			};
			widget = document.createElement( 'button' );
			processor.processWidget( widget, attributes, mw );
			expect( widget.toString() ).toBe( 'button' );
			expect( widget.onclick.toString() ).toContain( 'return mw.toInspect[attributes.name]();' );

			// Outputs

			widget = document.createElement( 'output' );
			processor.processWidget( widget, attributes, mw );
			expect( widget.toString() ).toBe( 'output' );
			expect( widget.innerHTML ).toBe( 'barValue' );

			// Textareas

			attributes = {
				name: "baz"
			};
			widget = document.createElement( 'textarea' );
			processor.processWidget( widget, attributes, mw );
			expect( widget.toString() ).toBe( 'textarea' );
			expect( widget.innerHTML ).toBe( 'bazValue' );

			// Checkboxes

			attributes = {
				name: "boolean"
			};
			widget = document.createElement( 'input' );
			widget.setAttribute( 'type', 'checkbox' );
			processor.processWidget( widget, attributes, mw );
			expect( widget.toString() ).toBe( 'input type="checkbox"' );
			expect( widget.checked ).toBe( true );

			// Select boxes

			attributes = {
				name: "select"
			};
			widget = document.createElement( 'select' );
			processor.processWidget( widget, attributes, mw );
			expect( widget.toString() ).toBe( 'select' );
			expect( widget.value ).toBe( false );

			// Root-level

			attributes = {
				_root: "true"
			};
			widget = document.createElement( 'output' );
			processor.processWidget( widget, attributes, mw );
			expect( widget.toString() ).toBe( 'output' );
			expect( widget.innerHTML ).toBe( mw.toInspect );
		} );

		it( "supports nested widgets", function() {

			var element = document.createElement( 'div' );
			var mw = new metawidget.Metawidget( element, {
				layout: new metawidget.layout.SimpleLayout()
			} );

			mw.toInspect = {
				nested: {
					"nestedFoo": "nestedFooValue"
				}
			};
			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'div id="nested"' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'input type="text" id="nestedNestedFoo" name="nestedNestedFoo"' );
			expect( element.childNodes[0].childNodes[0].value ).toBe( 'nestedFooValue' );
			expect( element.childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes.length ).toBe( 1 );

			element.childNodes[0].childNodes[0].value = 'nestedFooValue1';
			mw.getWidgetProcessor( function( testInstanceOf ) {

				return testInstanceOf instanceof metawidget.widgetprocessor.SimpleBindingProcessor;
			} ).save( mw );
			expect( mw.toInspect.nested.nestedFoo ).toBe( 'nestedFooValue1' );
		} );
	} );
} )();