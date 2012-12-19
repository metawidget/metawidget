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

describe( "The testIsReadOnly function", function() {

	it( "tests if an attribute is read only", function() {

		expect( metawidget.util.isReadOnly( {}, {
			readOnly: false
		} ) ).toBe( false );

		expect( metawidget.util.isReadOnly( {}, {
			readOnly: "true"
		} ) ).toBe( "true" );

		expect( metawidget.util.isReadOnly( {
			readOnly: "false"
		}, {
			readOnly: true
		} ) ).toBe( true );

		expect( metawidget.util.isReadOnly( {
			readOnly: "false"
		}, {
			readOnly: false
		} ) ).toBe( false );
	} );
} );
