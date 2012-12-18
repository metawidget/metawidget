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

describe( "AngularJS AllWidgets", function() {

	it( "tests every sort of widget", function() {

		angular.bootstrap( document, [ 'allWidgets' ] );

		expect( $( '#table-allWidgetsTextbox-label' ).prop( 'for' ) ).toBe( 'textbox' );
		expect( $( '#table-allWidgetsTextbox-label' ).text() ).toBe( 'Textbox:' );
		expect( $( '#textbox' )[0].tagName ).toBe( 'INPUT' );
		expect( $( '#textbox' )[0].type ).toBe( 'text' );
		expect( $( '#textbox' ).attr( 'ng-model' ) ).toBe( 'toInspect.textbox' );

		expect( $( '#table-allWidgetsTextarea-label' ).prop( 'for' ) ).toBe( 'textarea' );
		expect( $( '#table-allWidgetsTextarea-label' ).text() ).toBe( 'Textarea:' );
		expect( $( '#textarea' )[0].tagName ).toBe( 'TEXTAREA' );
		expect( $( '#textarea' ).attr( 'ng-model' ) ).toBe( 'toInspect.textarea' );

		expect( $( '#table-allWidgetsPassword-label' ).prop( 'for' ) ).toBe( 'password' );
		expect( $( '#table-allWidgetsPassword-label' ).text() ).toBe( 'Password:' );
		expect( $( '#password' )[0].tagName ).toBe( 'INPUT' );
		expect( $( '#password' )[0].type ).toBe( 'password' );
		expect( $( '#password' ).attr( 'ng-model' ) ).toBe( 'toInspect.password' );

		expect( $( '#table-allWidgetsNumber-label' ).prop( 'for' ) ).toBe( 'number' );
		expect( $( '#table-allWidgetsNumber-label' ).text() ).toBe( 'Number:' );
		expect( $( '#number' )[0].tagName ).toBe( 'INPUT' );
		expect( $( '#number' )[0].type ).toBe( 'number' );
		expect( $( '#number' ).attr( 'ng-model' ) ).toBe( 'toInspect.number' );

		expect( $( '#table-allWidgetsRangedNumber-label' ).prop( 'for' ) ).toBe( 'rangedNumber' );
		expect( $( '#table-allWidgetsRangedNumber-label' ).text() ).toBe( 'Ranged Number:' );
		expect( $( '#rangedNumber' )[0].tagName ).toBe( 'INPUT' );
		expect( $( '#rangedNumber' )[0].type ).toBe( 'range' );
		expect( $( '#rangedNumber' ).attr( 'ng-model' ) ).toBe( 'toInspect.rangedNumber' );
		expect( $( '#rangedNumber' ).attr( 'min' ) ).toBe( '1' );
		expect( $( '#rangedNumber' ).attr( 'max' ) ).toBe( '100' );
	} );
} );
