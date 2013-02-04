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

describe( "The uncamelCase function", function() {

	it( "uncamel cases strings", function() {

		expect( metawidget.util.uncamelCase( 'foo' ) ).toBe( 'Foo' );
		expect( metawidget.util.uncamelCase( 'fooBar' ) ).toBe( 'Foo Bar' );
		expect( metawidget.util.uncamelCase( 'FooBar' ) ).toBe( 'Foo Bar' );
		expect( metawidget.util.uncamelCase( 'FooBar1' ) ).toBe( 'Foo Bar 1' );
	} );

	it( "doesn't mangle strings that are already uncamel-cased", function() {

		expect( metawidget.util.uncamelCase( 'Foo Bar' ) ).toBe( 'Foo Bar' );
		expect( metawidget.util.uncamelCase( 'Foo barBaz Abc' ) ).toBe( 'Foo bar Baz Abc' );
	} );
} );

describe( "The capitalize function", function() {

	it( "capitalizes strings", function() {

		expect( metawidget.util.capitalize( 'fooBah' ) ).toBe( 'FooBah' );
		expect( metawidget.util.capitalize( 'x' ) ).toBe( 'X' );
		expect( metawidget.util.capitalize( 'URL' ) ).toBe( 'URL' );
		expect( metawidget.util.capitalize( 'ID' ) ).toBe( 'ID' );
	} );
} );

describe( "The camelCase function", function() {

	it( "camel cases arrays", function() {

		expect( metawidget.util.camelCase( [] ) ).toBe( '' );
		expect( metawidget.util.camelCase( [ 'foo' ] ) ).toBe( 'foo' );
		expect( metawidget.util.camelCase( [ 'foo', 'bar' ] ) ).toBe( 'fooBar' );
		expect( metawidget.util.camelCase( [ 'foo', 'bar', 'baz' ] ) ).toBe( 'fooBarBaz' );
	} );
} );

describe( "The getId function", function() {

	it( "creates an Id for an attribute", function() {

		expect( metawidget.util.getId( {
			name: "baz"
		}, {
			path: "foo.bar"
		} ) ).toBe( "fooBarBaz" );
	} );
} );

describe( "The hasChildElements function", function() {

	it( "checks if a node has child elements", function() {

		var div = document.createElement( 'div' );
		expect( metawidget.util.hasChildElements( div ) ).toBe( false );
		div.appendChild( document.createElement( 'span' ) );
		expect( metawidget.util.hasChildElements( div ) ).toBe( true );
	} );

	it( "ignores text nodes", function() {

		var div = document.createElement( 'div' );
		div.appendChild( {} );
		expect( metawidget.util.hasChildElements( div ) ).toBe( false );
		div.appendChild( document.createElement( 'span' ) );
		expect( metawidget.util.hasChildElements( div ) ).toBe( true );
	} );
} );

describe( "The splitPath function", function() {

	it( "splits a path into types and names", function() {

		expect( metawidget.util.splitPath( 'foo' ).type ).toBe( 'foo' );
		expect( metawidget.util.splitPath( 'foo' ).names.length ).toBe( 0 );

		expect( metawidget.util.splitPath( 'foo.bar' ).type ).toBe( 'foo' );
		expect( metawidget.util.splitPath( 'foo.bar' ).names[0] ).toBe( 'bar' );
		expect( metawidget.util.splitPath( 'foo.bar' ).names.length ).toBe( 1 );

		expect( metawidget.util.splitPath( 'foo.bar.baz' ).type ).toBe( 'foo' );
		expect( metawidget.util.splitPath( 'foo.bar.baz' ).names[0] ).toBe( 'bar' );
		expect( metawidget.util.splitPath( 'foo.bar.baz' ).names[1] ).toBe( 'baz' );
		expect( metawidget.util.splitPath( 'foo.bar.baz' ).names.length ).toBe( 2 );
	} );
} );

describe( "The appendPath function", function() {

	it( "appends attribute names to paths", function() {

		expect( metawidget.util.appendPath( {
			name: "foo"
		}, {} ) ).toBe( 'object.foo' );
		expect( metawidget.util.appendPath( {
			name: "foo"
		}, {
			toInspect: "aString"
		} ) ).toBe( 'string.foo' );
		expect( metawidget.util.appendPath( {
			name: "foo"
		}, {
			path: "bar.baz"
		} ) ).toBe( 'bar.baz.foo' );
	} );
} );

describe( "The traversePath function", function() {

	it( "traverses names", function() {

		var object2 = {
			foo: "bar"
		};
		var object1 = {
			object2: object2
		};

		expect( metawidget.util.traversePath( object1 ) ).toBe( object1 );
		expect( metawidget.util.traversePath( object1, 'ignore' ) ).toBe( object1 );
		expect( metawidget.util.traversePath( object1, 'ignore', [ 'object2' ] ) ).toBe( object2 );
		expect( metawidget.util.traversePath( object1, 'ignore', [ 'object2', 'foo' ] ) ).toBe( 'bar' );
		expect( metawidget.util.traversePath( object1, 'ignore', [ 'object2', 'foo', 'bar' ] ) ).toBeUndefined();
		expect( metawidget.util.traversePath( object1, 'ignore', [ 'object2', 'baz' ] ) ).toBeUndefined();
	} );
} );

describe( "The combineInspectionResults function", function() {

	it( "combines inspection results", function() {

		var existingInspectionResult = [ {
			_root: 'true',
			name: 'abc',
			foo: 'foo'
		}, {
			name: 'bar',
			baz: 'baz'
		} ];
		var newInspectionResult = [ {
			_root: 'true',
			name: 'new abc',
			def: 'def'
		}, {
			name: 'bar',
			mno: 'mno'
		}, {
			name: 'ghi',
			jkl: 'jkl'
		} ];

		metawidget.util.combineInspectionResults( existingInspectionResult, newInspectionResult );

		expect( existingInspectionResult[0]._root ).toBe( 'true' );
		expect( existingInspectionResult[0].name ).toBe( 'new abc' );
		expect( existingInspectionResult[0].foo ).toBe( 'foo' );
		expect( existingInspectionResult[0].def ).toBe( 'def' );
		expect( existingInspectionResult[1].name ).toBe( 'bar' );
		expect( existingInspectionResult[1].baz ).toBe( 'baz' );
		expect( existingInspectionResult[1].mno ).toBe( 'mno' );
		expect( existingInspectionResult[2].name ).toBe( 'ghi' );
		expect( existingInspectionResult[2].jkl ).toBe( 'jkl' );
		expect( existingInspectionResult.length ).toBe( 3 );
	} );
} );
