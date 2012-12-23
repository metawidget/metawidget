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

describe( "AngularJS AddressBook", function() {

	it( "can search existing contacts", function() {

		// TODO: not supported?
		$( '.data-table' ).empty();
		
		angular.bootstrap( document, [ 'addressBook' ] );

		expect( $( '#table-searchFirstname-label' ).prop( 'for' ) ).toBe( 'searchFirstname' );
		expect( $( '#table-searchFirstname-label' ).text() ).toBe( 'Firstname:' );
		expect( $( '#searchFirstname' )[0].tagName ).toBe( 'INPUT' );
		expect( $( '#searchFirstname' )[0].type ).toBe( 'text' );
		expect( $( '#searchFirstname' ).attr( 'ng-model' ) ).toBe( 'search.firstname' );		
		expect( $( '#table-searchSurname-label' ).prop( 'for' ) ).toBe( 'searchSurname' );
		expect( $( '#table-searchSurname-label' ).text() ).toBe( 'Surname:' );
		expect( $( '#searchSurname' )[0].tagName ).toBe( 'INPUT' );
		expect( $( '#searchSurname' )[0].type ).toBe( 'text' );
		expect( $( '#searchSurname' ).attr( 'ng-model' ) ).toBe( 'search.surname' );		
		expect( $( '#table-searchType-label' ).prop( 'for' ) ).toBe( 'searchType' );
		expect( $( '#table-searchType-label' ).text() ).toBe( 'Type:' );
		expect( $( '#searchType' )[0].tagName ).toBe( 'SELECT' );
		expect( $( '#searchType' ).attr( 'ng-model' ) ).toBe( 'search.type' );		
		expect( $( '#searchType option' )[0].value ).toBe( '' );
		expect( $( '#searchType option' )[1].value ).toBe( 'personal' );
		expect( $( '#searchType option' )[1].text ).toBe( 'Personal' );
		expect( $( '#searchType option' )[2].value ).toBe( 'business' );
		expect( $( '#searchType option' )[2].text ).toBe( 'Business' );
		expect( $( '.table-form tbody tr' ).length ).toBe( 3 );

		expect( $( '#searchActionsSearch' )[0].tagName ).toBe( 'BUTTON' );
		expect( $( '#searchActionsSearch' ).attr( 'ng-click' ) ).toBe( 'searchActions.search()' );		
		expect( $( '#searchActionsSearch' ).text() ).toBe( 'Search' );

		expect( $( '#searchActionsCreatePersonal' )[0].tagName ).toBe( 'BUTTON' );
		expect( $( '#searchActionsCreatePersonal' ).attr( 'ng-click' ) ).toBe( 'searchActions.createPersonal()' );		
		expect( $( '#searchActionsCreatePersonal' ).text() ).toBe( 'Create Personal' );

		expect( $( '#searchActionsCreateBusiness' )[0].tagName ).toBe( 'BUTTON' );
		expect( $( '#searchActionsCreateBusiness' ).attr( 'ng-click' ) ).toBe( 'searchActions.createBusiness()' );		
		expect( $( '#searchActionsCreateBusiness' ).text() ).toBe( 'Create Business' );
	} );
} );
