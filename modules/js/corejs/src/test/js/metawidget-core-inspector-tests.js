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

describe( "The CompositeInspector", function() {

	it( "composes inspection results", function() {

		var inspector = new metawidget.inspector.CompositeInspector( [ function( toInspect, type ) {

			return [ {
				"name": "foo",
				"override": "base",
				"type": "fooType"
			}, {
				name: "bar"
			} ]
		}, function( toInspect, type ) {

			return [ {
				name: "baz"
			}, {
				"name": "foo",
				"override": "overridden",
				"required": "fooRequired"
			} ]
		} ] );

		var inspectionResult = inspector.inspect();

		expect( inspectionResult[0].name ).toBe( 'foo' );
		expect( inspectionResult[0].type ).toBe( 'fooType' );
		expect( inspectionResult[0].override ).toBe( 'overridden' );
		expect( inspectionResult[0].required ).toBe( 'fooRequired' );
		expect( inspectionResult[1].name ).toBe( 'bar' );
		expect( inspectionResult[2].name ).toBe( 'baz' );
	} );

	it( "defensively copies inspection results", function() {

		var originalResult = [ {
			"name": "foo",
			"type": "fooType"
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
		var inspectionResult = inspector.inspect( { "foo": "Foo", "bar": "Bar", "action": function() { } });

		expect( inspectionResult[0].name ).toBe( '$root' );
		expect( inspectionResult[0].type ).toBe( 'object' );
		expect( inspectionResult[1].name ).toBe( 'foo' );
		expect( inspectionResult[1].type ).toBe( 'string' );
		expect( inspectionResult[2].name ).toBe( 'bar' );
		expect( inspectionResult[2].type ).toBe( 'string' );
		expect( inspectionResult[3].name ).toBe( 'action' );
		expect( inspectionResult[3].type ).toBe( 'function' );
	} );
} );
