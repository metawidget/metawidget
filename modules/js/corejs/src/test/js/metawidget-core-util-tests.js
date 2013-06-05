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

	describe( "The getLabelString function", function() {

		it( "uncamel cases strings", function() {

			expect( metawidget.util.getLabelString( {
				name: 'foo'
			}, {} ) ).toBe( 'Foo' );
			expect( metawidget.util.getLabelString( {
				name: 'fooBar'
			}, {} ) ).toBe( 'Foo Bar' );
			expect( metawidget.util.getLabelString( {
				name: 'FooBar'
			}, {} ) ).toBe( 'Foo Bar' );
			expect( metawidget.util.getLabelString( {
				name: 'FooBar1'
			}, {} ) ).toBe( 'Foo Bar 1' );
		} );

		it( "doesn't mangle strings that are already uncamel-cased", function() {

			expect( metawidget.util.getLabelString( {
				name: 'Foo Bar'
			}, {} ) ).toBe( 'Foo Bar' );
			expect( metawidget.util.getLabelString( {
				name: 'Foo barBaz Abc'
			}, {} ) ).toBe( 'Foo bar Baz Abc' );
			expect( metawidget.util.getLabelString( {
				name: 'ID'
			}, {} ) ).toBe( 'ID' );
			expect( metawidget.util.getLabelString( {
				name: 'DOB'
			}, {} ) ).toBe( 'DOB' );
			expect( metawidget.util.getLabelString( {
				name: 'DOBirth'
			}, {} ) ).toBe( 'DO Birth' );
			expect( metawidget.util.getLabelString( {
				name: 'fooDOBirthBar'
			}, {} ) ).toBe( 'Foo DO Birth Bar' );
			expect( metawidget.util.getLabelString( {
				name: '123'
			}, {} ) ).toBe( '123' );
			expect( metawidget.util.getLabelString( {
				name: 'foo1'
			}, {} ) ).toBe( 'Foo 1' );
			expect( metawidget.util.getLabelString( {
				name: 'foo12'
			}, {} ) ).toBe( 'Foo 12' );
			expect( metawidget.util.getLabelString( {
				name: 'foo123'
			}, {} ) ).toBe( 'Foo 123' );
			expect( metawidget.util.getLabelString( {
				name: '123foo'
			}, {} ) ).toBe( '123foo' );
			expect( metawidget.util.getLabelString( {
				name: '123Foo'
			}, {} ) ).toBe( '123 Foo' );
		} );

		it( "supports localization", function() {

			expect( metawidget.util.getLabelString( {
				title: 'Foo Bar'
			}, {
				l10n: {
					FooBar: 'Foo Bar (i10n)'
				}
			} ) ).toBe( 'Foo Bar (i10n)' );
			expect( metawidget.util.getLabelString( {
				title: 'Foo Bar'
			}, {
				l10n: {
					FooBaz: 'Foo Baz (i10n)'
				}
			} ) ).toBe( 'Foo Bar' );
			expect( metawidget.util.getLabelString( {
				name: 'fooBar'
			}, {
				l10n: {
					fooBar: 'Foo Bar (i10n)'
				}
			} ) ).toBe( 'Foo Bar (i10n)' );
			expect( metawidget.util.getLabelString( {
				name: 'fooBar'
			}, {
				l10n: {
					fooBaz: 'Foo Baz (i10n)'
				}
			} ) ).toBe( 'Foo Bar' );
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

	describe( "The isTrueOrTrueString function", function() {

		it( "returns true for boolean true or string 'true'", function() {

			expect( metawidget.util.isTrueOrTrueString( 'true' ) ).toBe( true );
			expect( metawidget.util.isTrueOrTrueString( true ) ).toBe( true );

			expect( !metawidget.util.isTrueOrTrueString( 'false' ) ).toBe( true );
			expect( !metawidget.util.isTrueOrTrueString( false ) ).toBe( true );
		} );

		it( "returns false for everything else", function() {

			expect( metawidget.util.isTrueOrTrueString( 1 ) ).toBe( false );
			expect( metawidget.util.isTrueOrTrueString( 'yes' ) ).toBe( false );
			expect( metawidget.util.isTrueOrTrueString( false ) ).toBe( false );
			expect( metawidget.util.isTrueOrTrueString( 'false' ) ).toBe( false );
		} );
	} );

	describe( "The camelCase function", function() {

		it( "camel cases arrays", function() {

			expect( metawidget.util.camelCase( [] ) ).toBe( '' );
			expect( metawidget.util.camelCase( [ 'foo' ] ) ).toBe( 'foo' );
			expect( metawidget.util.camelCase( [ 'foo', 'bar' ] ) ).toBe( 'fooBar' );
			expect( metawidget.util.camelCase( [ 'foo', 'bar', 'baz' ] ) ).toBe( 'fooBarBaz' );
		} );

		it( "camel cases Strings", function() {

			expect( metawidget.util.camelCase( '' ) ).toBe( '' );
			expect( metawidget.util.camelCase( 'foo' ) ).toBe( 'foo' );
			expect( metawidget.util.camelCase( 'foo bar' ) ).toBe( 'fooBar' );
			expect( metawidget.util.camelCase( 'foo bar baz' ) ).toBe( 'fooBarBaz' );
		} );
	} );

	describe( "The getId function", function() {

		it( "creates an Id for an attribute", function() {

			expect( metawidget.util.getId( "property", {}, {} ) ).toBeUndefined();
			expect( metawidget.util.getId( "property", {
				name: "baz"
			}, {} ) ).toBe( "baz" );
			expect( metawidget.util.getId( "property", {}, {
				path: "foo.bar"
			} ) ).toBe( "fooBar" );
			expect( metawidget.util.getId( "property", {
				name: "baz"
			}, {
				path: "foo.bar"
			} ) ).toBe( "fooBarBaz" );
			expect( metawidget.util.getId( "property", {}, {
				path: 'object'
			} ) ).toBeUndefined();

			// Strip array qualifiers

			expect( metawidget.util.getId( "property", {
				name: 'addressLine1'
			}, {
				path: 'member.home[0]'
			} ) ).toBe( "memberHome0AddressLine1" );
		} );
	} );

	describe( "The hasChildElements function", function() {

		it( "checks if a node has child elements", function() {

			var div = simpleDocument.createElement( 'div' );
			expect( metawidget.util.hasChildElements( div ) ).toBe( false );
			div.appendChild( simpleDocument.createElement( 'span' ) );
			expect( metawidget.util.hasChildElements( div ) ).toBe( true );
		} );

		it( "ignores text nodes", function() {

			var div = simpleDocument.createElement( 'div' );
			div.appendChild( {} );
			expect( metawidget.util.hasChildElements( div ) ).toBe( false );
			div.appendChild( simpleDocument.createElement( 'span' ) );
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

		it( "supports intermediateName", function() {

			var object2 = {
				properties: {
					foo: "bar"
				}
			};
			var object1 = {
				properties: {
					object2: object2
				}
			};

			expect( metawidget.util.traversePath( object1 ) ).toBe( object1 );
			expect( metawidget.util.traversePath( object1, [ 'object2' ] ) ).toBeUndefined();
			expect( metawidget.util.traversePath( object1, [ 'object2' ], 'properties' ) ).toBe( object2 );
			expect( metawidget.util.traversePath( object1, [ 'object2', 'foo' ], 'properties' ) ).toBe( 'bar' );
			expect( metawidget.util.traversePath( object1, [ 'object2', 'foo', 'bar' ], 'properties' ) ).toBeUndefined();
			expect( metawidget.util.traversePath( object1, [ 'object2', 'baz' ], 'properties' ) ).toBeUndefined();
		} );
	} );

	describe( "The getSortedInspectionResultProperties function", function() {

		it( "sorts inspection result properties by propertyOrder", function() {

			var inspectionResult = {
				name: 'abc',
				foo: 'foo',
				properties: {
					'bar': {
						title: 'A Bar',
						propertyOrder: 3
					},
					'baz': {
						propertyOrder: 2,
						title: 'A Baz'
					},
					'def': {
						propertyOrder: 1,
						title: 'A Def'
					}
				}
			};

			var sorted = metawidget.util.getSortedInspectionResultProperties( inspectionResult );

			expect( sorted.length ).toBe( 3 );
			expect( sorted[0].name ).toBe( 'def' );
			expect( sorted[0].title ).toBe( 'A Def' );
			expect( sorted[1].name ).toBe( 'baz' );
			expect( sorted[1].title ).toBe( 'A Baz' );
			expect( sorted[2].name ).toBe( 'bar' );
			expect( sorted[2].title ).toBe( 'A Bar' );
		} );
	} );

	describe( "The combineInspectionResults function", function() {

		it( "combines inspection results", function() {

			var existingInspectionResult = {
				name: 'abc',
				foo: 'foo',
				properties: {
					'bar': {
						baz: 'baz',
						'anArray': [ '0', '1', '2' ]
					}
				}
			};
			var newInspectionResult = {
				name: 'new abc',
				def: 'def',
				properties: {
					'bar': {
						mno: 'mno',
						'anotherArray': [ '3', '4' ]
					},
					'ghi': {
						jkl: 'jkl'
					}
				}
			};

			metawidget.util.combineInspectionResults( existingInspectionResult, newInspectionResult );

			expect( existingInspectionResult.name ).toBe( 'new abc' );
			expect( existingInspectionResult.foo ).toBe( 'foo' );
			expect( existingInspectionResult.def ).toBe( 'def' );
			expect( existingInspectionResult.properties.bar.baz ).toBe( 'baz' );
			expect( existingInspectionResult.properties.bar.mno ).toBe( 'mno' );
			expect( existingInspectionResult.properties.bar.anArray[0] ).toBe( '0' );
			expect( existingInspectionResult.properties.bar.anArray[1] ).toBe( '1' );
			expect( existingInspectionResult.properties.bar.anArray[2] ).toBe( '2' );
			expect( existingInspectionResult.properties.bar.anArray.length ).toBe( 3 );
			expect( existingInspectionResult.properties.bar.anotherArray[0] ).toBe( '3' );
			expect( existingInspectionResult.properties.bar.anotherArray[1] ).toBe( '4' );
			expect( existingInspectionResult.properties.bar.anotherArray.length ).toBe( 2 );
			expect( existingInspectionResult.properties.ghi.jkl ).toBe( 'jkl' );
		} );
	} );

	describe( "The stripSection function", function() {

		it( "strips section names", function() {

			var attributes = {
				section: ''
			}
			expect( metawidget.util.stripSection( attributes ) ).toBe( '' );
			expect( attributes.section ).toBeUndefined();

			var attributes = {
				section: []
			}
			expect( metawidget.util.stripSection( attributes ) ).toBe( '' );
			expect( attributes.section ).toBeUndefined();

			attributes = {
				section: 'Foo'
			}
			expect( metawidget.util.stripSection( attributes ) ).toBe( 'Foo' );
			expect( attributes.section ).toBeUndefined();

			attributes = {
				section: [ 'Foo' ]
			}
			expect( metawidget.util.stripSection( attributes ) ).toBe( 'Foo' );
			expect( attributes.section ).toBeUndefined();

			attributes = {
				section: [ 'Foo', 'Bar' ]
			}
			expect( metawidget.util.stripSection( attributes ) ).toBe( 'Foo' );
			expect( attributes.section[0] ).toBe( 'Bar' );
			expect( attributes.section.length ).toBe( 1 );
		} );
	} );

	describe( "The appendToAttribute function", function() {

		it( "appends attribute values", function() {

			var widget = simpleDocument.createElement( 'widget' );
			expect( widget.getAttribute( 'class' ) ).toBeNull();

			metawidget.util.appendToAttribute( widget, 'class', 'btn' )
			expect( widget.getAttribute( 'class' ) ).toBe( 'btn' );

			metawidget.util.appendToAttribute( widget, 'class', 'btn2' )
			expect( widget.getAttribute( 'class' ) ).toBe( 'btn btn2' );
		} );
	} );
} )();