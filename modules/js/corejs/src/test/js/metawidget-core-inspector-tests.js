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

	describe( "The CompositeInspector", function() {

		it( "composes inspection results", function() {

			var inspector = new metawidget.inspector.CompositeInspector( [ function( toInspect, type ) {

				return [ {
					name: "foo",
					override: "base",
					type: "fooType"
				}, {
					name: "bar"
				} ];
			}, function( toInspect, type ) {

				return [ {
					name: "baz"
				}, {
					name: "foo",
					override: "overridden",
					required: "fooRequired"
				} ];
			} ] );

			var inspectionResult = inspector.inspect();

			expect( inspectionResult[0].name ).toBe( 'foo' );
			expect( inspectionResult[0].type ).toBe( 'fooType' );
			expect( inspectionResult[0].override ).toBe( 'overridden' );
			expect( inspectionResult[0].required ).toBe( 'fooRequired' );
			expect( inspectionResult[1].name ).toBe( 'bar' );
			expect( inspectionResult[2].name ).toBe( 'baz' );
		} );

		it( "defensively copies inspectors", function() {

			// Direct

			var inspectors = [ function( toInspect, type ) {

				return [ {
					name: "foo"
				} ];
			} ];

			var inspector = new metawidget.inspector.CompositeInspector( inspectors );
			var inspectionResult = inspector.inspect();
			expect( inspectionResult[0].name ).toBe( 'foo' );

			expect( inspectors.length ).toBe( 1 );
			inspectors.splice( 0, 1 );
			expect( inspectors.length ).toBe( 0 );
			inspectionResult = inspector.inspect();
			expect( inspectionResult[0].name ).toBe( 'foo' );

			// Via config

			var config = {
				inspectors: [ function( toInspect, type ) {

					return [ {
						name: "foo"
					} ];
				} ]
			};

			inspector = new metawidget.inspector.CompositeInspector( config );
			inspectionResult = inspector.inspect();
			expect( inspectionResult[0].name ).toBe( 'foo' );

			expect( config.inspectors.length ).toBe( 1 );
			config.inspectors.splice( 0, 1 );
			expect( config.inspectors.length ).toBe( 0 );
			inspectionResult = inspector.inspect();
			expect( inspectionResult[0].name ).toBe( 'foo' );
		} );

		it( "defensively copies inspection results", function() {

			var originalResult = [ {
				name: "foo",
				type: "fooType"
			} ];

			var inspector = new metawidget.inspector.CompositeInspector( [ function( toInspect, type ) {

				return originalResult;
			} ] );

			var inspectionResult = inspector.inspect();

			expect( inspectionResult[0].name ).toBe( 'foo' );
			expect( inspectionResult[0].type ).toBe( 'fooType' );

			inspectionResult[0].type = 'barType';

			inspectionResult = inspector.inspect();

			expect( inspectionResult[0].name ).toBe( 'foo' );
			expect( inspectionResult[0].type ).toBe( 'fooType' );
		} );
	} );

	describe( "The PropertyTypeInspector", function() {

		it( "inspects JavaScript objects", function() {

			var inspector = new metawidget.inspector.PropertyTypeInspector();
			var inspectionResult = inspector.inspect( {
				foo: "Foo",
				bar: "Bar",
				date: new Date(),
				object: {},
				action: function() {

				},
				array: [],
				bool: true,
				num: 46
			} );

			expect( inspectionResult[0]._root ).toBe( 'true' );
			expect( inspectionResult[0].type ).toBe( 'object' );
			expect( inspectionResult[1].name ).toBe( 'foo' );
			expect( inspectionResult[1].type ).toBe( 'string' );
			expect( inspectionResult[2].name ).toBe( 'bar' );
			expect( inspectionResult[2].type ).toBe( 'string' );
			expect( inspectionResult[3].name ).toBe( 'date' );
			expect( inspectionResult[3].type ).toBe( 'date' );
			expect( inspectionResult[4].name ).toBe( 'object' );
			expect( inspectionResult[4].type ).toBeUndefined();
			expect( inspectionResult[5].name ).toBe( 'action' );
			expect( inspectionResult[5].type ).toBe( 'function' );
			expect( inspectionResult[6].name ).toBe( 'array' );
			expect( inspectionResult[6].type ).toBe( 'array' );
			expect( inspectionResult[7].name ).toBe( 'bool' );
			expect( inspectionResult[7].type ).toBe( 'boolean' );
			expect( inspectionResult[8].name ).toBe( 'num' );
			expect( inspectionResult[8].type ).toBe( 'number' );
			expect( inspectionResult.length ).toBe( 9 );
		} );

		it( "ignores undefined objects", function() {

			var inspector = new metawidget.inspector.PropertyTypeInspector();

			expect( inspector.inspect() ).toBeUndefined();
			expect( inspector.inspect( undefined ) ).toBeUndefined();
			expect( inspector.inspect( {} )[0]._root ).toBe( 'true' );
			expect( inspector.inspect( {}, 'foo' )[0]._root ).toBe( 'true' );
			expect( inspector.inspect( {}, 'foo', [ 'bar' ] )[0]._root ).toBe( 'true' );
			expect( inspector.inspect( {}, 'foo', [ 'bar' ] )[0].name ).toBe( 'bar' );
			expect( inspector.inspect( {}, 'foo', [ 'bar' ] ).length ).toBe( 1 );
		} );

		it( "does not ignore empty strings", function() {

			var inspector = new metawidget.inspector.PropertyTypeInspector();

			expect( inspector.inspect( '' )[0].type ).toBe( 'string' );
			expect( inspector.inspect( {
				'foo': ''
			}, 'ignore', [ 'foo' ] )[0].type ).toBe( 'string' );
		} );

		it( "inspects parent name", function() {

			var inspector = new metawidget.inspector.PropertyTypeInspector();

			expect( inspector.inspect( {
				'foo': ''
			}, 'ignore', [ 'foo' ] )[0]._root ).toBe( 'true' );
			expect( inspector.inspect( {
				'foo': ''
			}, 'ignore', [ 'foo' ] )[0].name ).toBe( 'foo' );

			expect( inspector.inspect( {
				'foo': ''
			}, 'ignore' )[0]._root ).toBe( 'true' );
			for ( var prop in inspector.inspect( {
				'foo': ''
			}, 'ignore', [] )[0] ) {
				expect( prop ).toNotBe( 'name' );
			}

			expect( inspector.inspect( {
				'foo': ''
			}, 'ignore', [] )[0]._root ).toBe( 'true' );
			for ( var prop in inspector.inspect( {
				'foo': ''
			}, 'ignore', [] )[0] ) {
				expect( prop ).toNotBe( 'name' );
			}
		} );
	} );
} )();