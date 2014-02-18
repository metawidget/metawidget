// Metawidget ${project.version}
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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

( function() {

	'use strict';

	describe( "The getLabelString function", function() {

		it( "supports localization", function() {

			expect( metawidget.util.getLabelString( {
				title: 'Foo Bar'
			}, {
				l10n: {
					fooBar: 'Foo Bar (i10n)'
				}
			} ) ).toBe( 'Foo Bar (i10n)' );
			expect( metawidget.util.getLabelString( {
				title: 'Foo Bar'
			}, {
				l10n: {
					fooBaz: 'Foo Baz (i10n)'
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
			expect( metawidget.util.uncamelCase( 'ID' ) ).toBe( 'ID' );
			expect( metawidget.util.uncamelCase( 'DOB' ) ).toBe( 'DOB' );
			expect( metawidget.util.uncamelCase( 'DOBirth' ) ).toBe( 'DO Birth' );
			expect( metawidget.util.uncamelCase( 'fooDOBirthBar' ) ).toBe( 'Foo DO Birth Bar' );
			expect( metawidget.util.uncamelCase( '123' ) ).toBe( '123' );
			expect( metawidget.util.uncamelCase( 'foo1' ) ).toBe( 'Foo 1' );
			expect( metawidget.util.uncamelCase( 'foo12' ) ).toBe( 'Foo 12' );
			expect( metawidget.util.uncamelCase( 'foo123' ) ).toBe( 'Foo 123' );
			expect( metawidget.util.uncamelCase( '123foo' ) ).toBe( '123foo' );
			expect( metawidget.util.uncamelCase( '123Foo' ) ).toBe( '123 Foo' );
		} );
	} );

	describe( "The capitalize function", function() {

		it( "capitalizes strings", function() {

			expect( metawidget.util.capitalize( '' ) ).toBe( '' );
			expect( metawidget.util.capitalize( 'fooBah' ) ).toBe( 'FooBah' );
			expect( metawidget.util.capitalize( 'x' ) ).toBe( 'X' );
			expect( metawidget.util.capitalize( 'URL' ) ).toBe( 'URL' );
			expect( metawidget.util.capitalize( 'ID' ) ).toBe( 'ID' );
			expect( metawidget.util.capitalize( 'aFIELD' ) ).toBe( 'aFIELD' );
			expect( metawidget.util.capitalize( 'aI' ) ).toBe( 'aI' );
			expect( metawidget.util.capitalize( 'jAXBElementLongConverter' ) ).toBe( 'jAXBElementLongConverter' );
			expect( metawidget.util.capitalize( 'JAXBElementLongConverter' ) ).toBe( 'JAXBElementLongConverter' );
		} );
	} );

	describe( "The decapitalize function", function() {

		it( "decapitalizes strings", function() {

			expect( metawidget.util.decapitalize( '' ) ).toBe( '' );
			expect( metawidget.util.decapitalize( 'FooBah' ) ).toBe( 'fooBah' );
			expect( metawidget.util.decapitalize( 'X' ) ).toBe( 'x' );
			expect( metawidget.util.decapitalize( 'URL' ) ).toBe( 'URL' );
			expect( metawidget.util.decapitalize( 'ID' ) ).toBe( 'ID' );

			// See: https://community.jboss.org/thread/203202?start=0&tstart=0

			expect( metawidget.util.decapitalize( 'aFIELD' ) ).toBe( 'aFIELD' );
			expect( metawidget.util.decapitalize( 'aI' ) ).toBe( 'aI' );
		} );

		it( "is the inverse of capitalize", function() {

			expect( metawidget.util.decapitalize( metawidget.util.capitalize( 'fooBah' ) ) ).toBe( 'fooBah' );
			expect( metawidget.util.decapitalize( metawidget.util.capitalize( 'x' ) ) ).toBe( 'x' );
			expect( metawidget.util.decapitalize( metawidget.util.capitalize( 'URL' ) ) ).toBe( 'URL' );
			expect( metawidget.util.decapitalize( metawidget.util.capitalize( 'ID' ) ) ).toBe( 'ID' );

			// These are only the inverse of each other because of the 'second
			// character' clause

			expect( metawidget.util.decapitalize( metawidget.util.capitalize( 'aFIELD' ) ) ).toBe( 'aFIELD' );
			expect( metawidget.util.decapitalize( metawidget.util.capitalize( 'aI' ) ) ).toBe( 'aI' );
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

			expect( metawidget.util.camelCase( 'Dropdown Foo' ) ).toBe( 'dropdownFoo' );
			expect( metawidget.util.camelCase( 'a' ) ).toBe( 'a' );
			expect( metawidget.util.camelCase( 'A' ) ).toBe( 'a' );
			expect( metawidget.util.camelCase( 'AZ' ) ).toBe( 'AZ' );
			expect( metawidget.util.camelCase( 'A b c' ) ).toBe( 'aBC' );
			expect( metawidget.util.camelCase( 'A B C' ) ).toBe( 'aBC' );
			expect( metawidget.util.camelCase( 'AZBC' ) ).toBe( 'AZBC' );
			expect( metawidget.util.camelCase( 'SPOUSE' ) ).toBe( 'SPOUSE' );
			expect( metawidget.util.camelCase( 'PERMANENT STAFF' ) ).toBe( 'PERMANENTSTAFF' );
			expect( metawidget.util.camelCase( 'item bar' ) ).toBe( 'itemBar' );
			expect( metawidget.util.camelCase( 'item.bar' ) ).toBe( 'item.bar' );
			expect( metawidget.util.camelCase( 'DOB' ) ).toBe( 'DOB' );
			expect( metawidget.util.camelCase( 'DO Birth' ) ).toBe( 'DOBirth' );
			expect( metawidget.util.camelCase( 'DO birth' ) ).toBe( 'DOBirth' );
			expect( metawidget.util.camelCase( 'Foo DO Birth Bar' ) ).toBe( 'fooDOBirthBar' );
			expect( metawidget.util.camelCase( '1 Foo' ) ).toBe( '1Foo' );
		} );
	} );

	describe( "The fillString function", function() {

		it( "fills strings", function() {

			expect( metawidget.util.fillString( '*', 0 ) ).toBe( '' );
			expect( metawidget.util.fillString( '*', 5 ) ).toBe( '*****' );
			expect( metawidget.util.fillString( '*', 7 ) ).toBe( '*******' );
		} );
	} );

	describe( "The lookupEnumTitle function", function() {

		it( "looks up enumTitles", function() {

			expect( metawidget.util.lookupEnumTitle( undefined, [ 'foo', 'bar', 'baz' ], [ 'FOO', 'BAR', 'BAZ' ] ) ).toBeUndefined();
			expect( metawidget.util.lookupEnumTitle( null, [ null, 'bar', 'baz' ], [ 'NULL', 'BAR', 'BAZ' ] ) ).toBe( 'NULL' );
			expect( metawidget.util.lookupEnumTitle( '', [ 'foo', 'bar', 'baz' ], [ 'FOO', 'BAR', 'BAZ' ] ) ).toBe( '' );
			expect( metawidget.util.lookupEnumTitle( 'foo', [ 'foo', 'bar', 'baz' ], [ 'FOO', 'BAR', 'BAZ' ] ) ).toBe( 'FOO' );
			expect( metawidget.util.lookupEnumTitle( 'bar', [ 'foo', 'bar', 'baz' ], [ 'FOO', 'BAR', 'BAZ' ] ) ).toBe( 'BAR' );
			expect( metawidget.util.lookupEnumTitle( 'baz', [ 'foo', 'bar', 'baz' ], [ 'FOO', 'BAR', 'BAZ' ] ) ).toBe( 'BAZ' );
			expect( metawidget.util.lookupEnumTitle( 'baz', [ 'foo', 'bar', 'baz' ], [ 'FOO', 'BAR' ] ) ).toBe( 'baz' );
			expect( metawidget.util.lookupEnumTitle( 'baz', [ 'foo', 'bar' ], [ 'FOO', 'BAR' ] ) ).toBe( 'baz' );
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

		it( "supports includesSeparator", function() {

			expect( metawidget.util.appendPath( {
				name: "[0].foo",
				nameIncludesSeparator: true
			}, {} ) ).toBe( 'object[0].foo' );
			expect( metawidget.util.appendPath( {
				name: "[1].foo",
				nameIncludesSeparator: true
			}, {
				toInspect: "aString"
			} ) ).toBe( 'string[1].foo' );
			expect( metawidget.util.appendPath( {
				name: "[2].foo",
				nameIncludesSeparator: true
			}, {
				path: "bar.baz"
			} ) ).toBe( 'bar.baz[2].foo' );
		} );
	} );

	describe( "The appendPathWithName function", function() {

		it( "appends attribute names to paths", function() {

			expect( metawidget.util.appendPathWithName( 'object', {
				name: "foo"
			} ) ).toBe( 'object.foo' );
		} );

		it( "supports includesSeparator", function() {

			expect( metawidget.util.appendPathWithName( 'object', {
				name: "[0].foo",
				nameIncludesSeparator: true
			} ) ).toBe( 'object[0].foo' );
		} );

		it( "escapes names", function() {

			expect( metawidget.util.appendPathWithName( 'object', {
				name: "normal"
			} ) ).toBe( 'object.normal' );
			expect( metawidget.util.appendPathWithName( 'object', {
				name: "with.dot"
			} ) ).toBe( 'object[\'with.dot\']' );
			expect( metawidget.util.appendPathWithName( 'object', {
				name: "with'apostrophe"
			} ) ).toBe( 'object[\'with\\\'apostrophe\']' );
			expect( metawidget.util.appendPathWithName( 'object', {
				name: "with\"quote"
			} ) ).toBe( 'object[\'with"quote\']' );
			expect( metawidget.util.appendPathWithName( 'object', {
				name: "with space"
			} ) ).toBe( 'object[\'with space\']' );
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
			};
			expect( metawidget.util.stripSection( attributes ) ).toBe( '' );
			expect( attributes.section ).toBeUndefined();

			var attributes = {
				section: []
			};
			expect( metawidget.util.stripSection( attributes ) ).toBe( '' );
			expect( attributes.section ).toBeUndefined();

			attributes = {
				section: 'Foo'
			};
			expect( metawidget.util.stripSection( attributes ) ).toBe( 'Foo' );
			expect( attributes.section ).toBeUndefined();

			attributes = {
				section: [ 'Foo' ]
			};
			expect( metawidget.util.stripSection( attributes ) ).toBe( 'Foo' );
			expect( attributes.section ).toBeUndefined();

			attributes = {
				section: [ 'Foo', 'Bar' ]
			};
			expect( metawidget.util.stripSection( attributes ) ).toBe( 'Foo' );
			expect( attributes.section[0] ).toBe( 'Bar' );
			expect( attributes.section.length ).toBe( 1 );

			attributes = {
				section: [ 'Foo', 'Bar', 'Baz' ]
			};
			expect( metawidget.util.stripSection( attributes ) ).toBe( 'Foo' );
			expect( attributes.section[0] ).toBe( 'Bar' );
			expect( attributes.section.length ).toBe( 2 );
		} );
	} );

	describe( "The appendToAttribute function", function() {

		it( "appends attribute values", function() {

			var widget = simpleDocument.createElement( 'widget' );
			expect( widget.getAttribute( 'class' ) ).toBeNull();

			metawidget.util.appendToAttribute( widget, 'class', 'btn' )
			expect( widget.getAttribute( 'class' ) ).toBe( 'btn' );

			metawidget.util.appendToAttribute( widget, 'class', 'btn' )
			expect( widget.getAttribute( 'class' ) ).toBe( 'btn' );

			metawidget.util.appendToAttribute( widget, 'class', 'btn2' )
			expect( widget.getAttribute( 'class' ) ).toBe( 'btn btn2' );

			metawidget.util.appendToAttribute( widget, 'class', 'btn2' )
			expect( widget.getAttribute( 'class' ) ).toBe( 'btn btn2' );

			metawidget.util.appendToAttribute( widget, 'class', 'btn' )
			expect( widget.getAttribute( 'class' ) ).toBe( 'btn btn2' );
		} );
	} );

	describe( "The createElement function", function() {

		it( "uppercases tag names", function() {

			var element = metawidget.util.createElement( {
				getElement: function() {

					return {
						ownerDocument: {
							createElement: function( elementName ) {

								return {
									tagName: elementName
								}
							}
						}
					};
				}
			}, 'output' );

			expect( element.tagName ).toBe( 'OUTPUT' );
		} );
	} );
} )();