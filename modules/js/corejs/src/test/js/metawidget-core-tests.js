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

describe( "The core Metawidget", function() {

	it( "populates itself with widgets to match the properties of business objects", function() {

		// Defaults

		var element = document.createElement( 'div' );
		var mw = new metawidget.Metawidget( element );

		mw.toInspect = {
			"foo": "Foo"
		};
		mw.buildWidgets();

		expect( element.childNodes[0].toString() ).toBe( 'table' );
		expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-foo-row"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-foo-label-cell"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo" id="table-foo-label"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo:' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-foo-cell"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="foo" name="foo" value="Foo"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
		expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
		expect( element.childNodes[0].childNodes.length ).toBe( 1 );
		expect( element.childNodes.length ).toBe( 1 );

		// Configured

		// TODO: test defensive copy

		var element = document.createElement( 'div' );
		mw = new metawidget.Metawidget( element, {
			layout: new metawidget.layout.SimpleLayout()
		} );

		mw.toInspect = {
			"bar": "Bar"
		};
		mw.buildWidgets();

		expect( element.childNodes[0].toString() ).toBe( 'input type="text" id="bar" name="bar" value="Bar"' );
		expect( element.childNodes.length ).toBe( 1 );
	} );
	
	it( "builds nested Metawidgets", function() {

		var element = document.createElement( 'div' );
		var mw = new metawidget.Metawidget( element );

		mw.toInspect = {
			"foo": { "nestedFoo": "Foo" }
		};
		mw.buildWidgets();

		expect( element.childNodes[0].toString() ).toBe( 'table' );
		expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-foo-row"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-foo-label-cell"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo" id="table-foo-label"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo:' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-foo-cell"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'div id="foo"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'table id="table-foo"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-fooNestedFoo-row"' );		
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-fooNestedFoo-label-cell"' );		
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="fooNestedFoo" id="table-fooNestedFoo-label"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Nested Foo:' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-fooNestedFoo-cell"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="fooNestedFoo" name="fooNestedFoo" value="Foo"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
		expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
		expect( element.childNodes[0].childNodes.length ).toBe( 1 );
		expect( element.childNodes.length ).toBe( 1 );
	} );
} );
