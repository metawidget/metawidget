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

			expect( metawidget.util.getId( {}, {} ) ).toBeUndefined();
			expect( metawidget.util.getId( {
				name: "baz"
			}, {} ) ).toBe( "baz" );
			expect( metawidget.util.getId( {}, {
				path: "foo.bar"
			} ) ).toBe( "fooBar" );
			expect( metawidget.util.getId( {
				name: "baz"
			}, {
				path: "foo.bar"
			} ) ).toBe( "fooBarBaz" );
			expect( metawidget.util.getId( {}, {
				path: 'object'
			} ) ).toBeUndefined();
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

			expect( metawidget.util.splitPath( undefined ).type ).toBe( undefined );
			expect( metawidget.util.splitPath( undefined ).names ).toBe( undefined );

			expect( metawidget.util.splitPath( 'foo' ).type ).toBe( 'foo' );
			expect( metawidget.util.splitPath( 'foo' ).names ).toBeUndefined();

			// Dot notation

			expect( metawidget.util.splitPath( 'foo.bar' ).type ).toBe( 'foo' );
			expect( metawidget.util.splitPath( 'foo.bar' ).names[0] ).toBe( 'bar' );
			expect( metawidget.util.splitPath( 'foo.bar' ).names.length ).toBe( 1 );

			expect( metawidget.util.splitPath( 'foo.bar.baz' ).type ).toBe( 'foo' );
			expect( metawidget.util.splitPath( 'foo.bar.baz' ).names[0] ).toBe( 'bar' );
			expect( metawidget.util.splitPath( 'foo.bar.baz' ).names[1] ).toBe( 'baz' );
			expect( metawidget.util.splitPath( 'foo.bar.baz' ).names.length ).toBe( 2 );

			// Square bracket notation

			expect( metawidget.util.splitPath( 'foo[bar]' ).type ).toBe( 'foo' );
			expect( metawidget.util.splitPath( 'foo[bar]' ).names[0] ).toBe( 'bar' );
			expect( metawidget.util.splitPath( 'foo[bar]' ).names.length ).toBe( 1 );

			expect( metawidget.util.splitPath( 'foo[bar][baz]' ).type ).toBe( 'foo' );
			expect( metawidget.util.splitPath( 'foo[bar][baz]' ).names[0] ).toBe( 'bar' );
			expect( metawidget.util.splitPath( 'foo[bar][baz]' ).names[1] ).toBe( 'baz' );
			expect( metawidget.util.splitPath( 'foo[bar][baz]' ).names.length ).toBe( 2 );

			// Array notation
			
			expect( metawidget.util.splitPath( 'foo[1]' ).type ).toBe( 'foo' );
			expect( metawidget.util.splitPath( 'foo[1]' ).names[0] ).toBe( '1' );
			expect( metawidget.util.splitPath( 'foo[1]' ).names.length ).toBe( 1 );

			// Mixed notation

			expect( metawidget.util.splitPath( 'foo.bar[baz]' ).type ).toBe( 'foo' );
			expect( metawidget.util.splitPath( 'foo[bar].baz' ).names[0] ).toBe( 'bar' );
			expect( metawidget.util.splitPath( 'foo[bar].baz' ).names[1] ).toBe( 'baz' );
			expect( metawidget.util.splitPath( 'foo[bar].baz' ).names.length ).toBe( 2 );

			// Automatic unwrapping

			expect( metawidget.util.splitPath( "foo['bar'].baz" ).names[0] ).toBe( 'bar' );
			expect( metawidget.util.splitPath( 'foo["bar"].baz' ).names[0] ).toBe( 'bar' );
			expect( metawidget.util.splitPath( "foo[ 'bar' ].baz" ).names[0] ).toBe( 'bar' );
			expect( metawidget.util.splitPath( 'foo[ "bar" ].baz' ).names[0] ).toBe( 'bar' );

			// Error notation (should handle gracefully?)

			expect( metawidget.util.splitPath( 'foo.bar.[baz]' ).type ).toBe( 'foo' );
			expect( metawidget.util.splitPath( 'foo[bar].[baz]' ).names[0] ).toBe( 'bar' );
			expect( metawidget.util.splitPath( 'foo[bar].[baz]' ).names[1] ).toBe( 'baz' );
			expect( metawidget.util.splitPath( 'foo[bar].[baz]' ).names.length ).toBe( 2 );

			// Workarounds (when you don't want the strings unwrapped)

			expect( metawidget.util.splitPath( "foo[('bar')]" ).names[0] ).toBe( "('bar')" );
			expect( metawidget.util.splitPath( 'foo[("bar")]' ).names[0] ).toBe( '("bar")' );
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
			expect( metawidget.util.traversePath( object1, [ 'object2' ] ) ).toBe( object2 );
			expect( metawidget.util.traversePath( object1, [ 'object2', 'foo' ] ) ).toBe( 'bar' );
			expect( metawidget.util.traversePath( object1, [ 'object2', 'foo', 'bar' ] ) ).toBeUndefined();
			expect( metawidget.util.traversePath( object1, [ 'object2', 'baz' ] ) ).toBeUndefined();
		} );

		it( "supports array indexes", function() {

			var object2 = {
				foo: "bar"
			};
			var object3 = {
				baz: "Baz"
			};
			var object1 = {
				array: [ object2, object3 ]
			};

			expect( metawidget.util.traversePath( object1 ) ).toBe( object1 );
			expect( metawidget.util.traversePath( object1, [ 'array' ] ).length ).toBe( 2 );
			expect( metawidget.util.traversePath( object1, [ 'array', 'foo' ] ) ).toBeUndefined();
			expect( metawidget.util.traversePath( object1, [ 'array', '0' ] ) ).toBe( object2 );
			expect( metawidget.util.traversePath( object1, [ 'array', '0', 'foo' ] ) ).toBe( 'bar' );
			expect( metawidget.util.traversePath( object1, [ 'array', '0', 'baz' ] ) ).toBeUndefined();
			expect( metawidget.util.traversePath( object1, [ 'array', '1' ] ) ).toBe( object3 );
			expect( metawidget.util.traversePath( object1, [ 'array', '1', 'foo' ] ) ).toBeUndefined()
			expect( metawidget.util.traversePath( object1, [ 'array', '1', 'baz' ] ) ).toBe( 'Baz' );
			expect( metawidget.util.traversePath( object1, [ 'array', '2' ] ) ).toBeUndefined();
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

	describe( "The split and joinArray functions", function() {

		it( "splits a string into an array", function() {

			expect( metawidget.util.splitArray( 'foo,bar,baz' )[0] ).toBe( 'foo' );
			expect( metawidget.util.splitArray( 'foo,bar,baz' )[1] ).toBe( 'bar' );
			expect( metawidget.util.splitArray( 'foo,bar,baz' )[2] ).toBe( 'baz' );
			expect( metawidget.util.splitArray( 'foo,bar,baz' ).length ).toBe( 3 );

			expect( metawidget.util.splitArray( 'foo,bar\\,bar,baz' )[0] ).toBe( 'foo' );
			expect( metawidget.util.splitArray( 'foo,bar\\,bar,baz' )[1] ).toBe( "bar,bar" );
			expect( metawidget.util.splitArray( 'foo,bar\\,bar,baz' )[2] ).toBe( 'baz' );
			expect( metawidget.util.splitArray( 'foo,bar\\,bar,baz' ).length ).toBe( 3 );

			expect( metawidget.util.splitArray( 'foo,\\,bar\\,bar\\,,baz' )[0] ).toBe( 'foo' );
			expect( metawidget.util.splitArray( 'foo,\\,bar\\,bar\\,,baz' )[1] ).toBe( ",bar,bar," );
			expect( metawidget.util.splitArray( 'foo,\\,bar\\,bar\\,,baz' )[2] ).toBe( 'baz' );
			expect( metawidget.util.splitArray( 'foo,\\,bar\\,bar\\,,baz' ).length ).toBe( 3 );
		} );

		it( "joins an array into a string", function() {

			expect( metawidget.util.joinArray( [ 'foo', 'bar', 'baz' ] ) ).toBe( 'foo,bar,baz' );
			expect( metawidget.util.joinArray( [ 'foo', 'bar,bar', 'baz' ] ) ).toBe( 'foo,bar\\,bar,baz' );
			expect( metawidget.util.joinArray( [ 'foo', ',bar,bar,', 'baz' ] ) ).toBe( 'foo,\\,bar\\,bar\\,,baz' );
		} );
	} );
} )();