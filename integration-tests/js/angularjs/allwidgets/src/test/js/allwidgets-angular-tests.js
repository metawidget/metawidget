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
		expect( $( '#textarea' ).text() ).toBe( 'Textarea' );

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

		expect( $( '#table-allWidgetsBoolean-label' ).prop( 'for' ) ).toBe( 'boolean' );
		expect( $( '#table-allWidgetsBoolean-label' ).text() ).toBe( 'Boolean:' );
		expect( $( '#boolean' )[0].tagName ).toBe( 'INPUT' );
		expect( $( '#boolean' )[0].type ).toBe( 'checkbox' );
		expect( $( '#boolean' ).attr( 'ng-model' ) ).toBe( 'toInspect.boolean' );
		
		expect( $( '#table-allWidgetsDropdown-label' ).prop( 'for' ) ).toBe( 'dropdown' );
		expect( $( '#table-allWidgetsDropdown-label' ).text() ).toBe( 'Dropdown:' );
		expect( $( '#dropdown' )[0].tagName ).toBe( 'SELECT' );
		expect( $( '#dropdown' ).attr( 'ng-model' ) ).toBe( 'toInspect.dropdown' );		
		expect( $( '#dropdown option' )[0].value ).toBe( '' );
		expect( $( '#dropdown option' )[1].value ).toBe( 'foo1' );
		expect( $( '#dropdown option' )[2].value ).toBe( 'dropdown1' );
		expect( $( '#dropdown option' )[3].value ).toBe( 'bar1' );
		expect( $( '#dropdown option' ).length ).toBe( 4 );
		
		expect( $( '#table-allWidgetsDropdownWithLabels-label' ).prop( 'for' ) ).toBe( 'dropdownWithLabels' );
		expect( $( '#table-allWidgetsDropdownWithLabels-label' ).text() ).toBe( 'Dropdown With Labels:' );
		expect( $( '#dropdownWithLabels' )[0].tagName ).toBe( 'SELECT' );
		expect( $( '#dropdownWithLabels' ).attr( 'ng-model' ) ).toBe( 'toInspect.dropdownWithLabels' );		
		expect( $( '#dropdownWithLabels option' )[0].value ).toBe( '' );
		expect( $( '#dropdownWithLabels option' )[1].value ).toBe( 'foo2' );
		expect( $( '#dropdownWithLabels option' )[1].text ).toBe( 'Foo #2' );
		expect( $( '#dropdownWithLabels option' )[2].value ).toBe( 'dropdown2' );
		expect( $( '#dropdownWithLabels option' )[2].text ).toBe( 'Dropdown #2' );
		expect( $( '#dropdownWithLabels option' )[3].value ).toBe( 'bar2' );
		expect( $( '#dropdownWithLabels option' )[3].text ).toBe( 'Bar #2' );
		expect( $( '#dropdownWithLabels option' )[4].value ).toBe( 'baz2' );
		expect( $( '#dropdownWithLabels option' )[4].text ).toBe( 'Baz #2' );
		expect( $( '#dropdownWithLabels option' ).length ).toBe( 5 );

		expect( $( '#table-allWidgetsNotNullDropdown-label' ).prop( 'for' ) ).toBe( 'notNullDropdown' );
		expect( $( '#table-allWidgetsNotNullDropdown-label' ).text() ).toBe( 'Not Null Dropdown:' );
		expect( $( '#notNullDropdown' )[0].tagName ).toBe( 'SELECT' );
		expect( $( '#notNullDropdown' ).attr( 'ng-model' ) ).toBe( 'toInspect.notNullDropdown' );
		expect( $( '#notNullDropdown option' )[0].value ).toBe( '-1' );
		expect( $( '#notNullDropdown option' )[1].value ).toBe( '0' );
		expect( $( '#notNullDropdown option' )[2].value ).toBe( '1' );
		expect( $( '#notNullDropdown option' ).length ).toBe( 3 );

		expect( $( '#table-allWidgetsDate-label' ).prop( 'for' ) ).toBe( 'date' );
		expect( $( '#table-allWidgetsDate-label' ).text() ).toBe( 'Date:' );
		expect( $( '#date' )[0].tagName ).toBe( 'INPUT' );
		expect( $( '#date' )[0].type ).toBe( 'date' );
		expect( $( '#date' ).attr( 'ng-model' ) ).toBe( 'toInspect.date' );

		expect( $( '#hidden' ).length ).toBe( 0 );
		
		expect( $( '#table-allWidgetsReadOnly-label' ).prop( 'for' ) ).toBe( 'readOnly' );
		expect( $( '#table-allWidgetsReadOnly-label' ).text() ).toBe( 'Read Only:' );
		expect( $( '#readOnly' )[0].tagName ).toBe( 'OUTPUT' );
		expect( $( '#readOnly' ).text() ).toBe( 'Read Only Value' );
	} );
} );
