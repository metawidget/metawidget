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

		expect( $( '#content' ).attr( 'ng-controller' ) ).toBe( 'ContactsController' );
		expect( $( '#table-searchFirstname-label' ).prop( 'for' ) ).toBe( 'firstname' );
		expect( $( '#table-searchFirstname-label' ).text() ).toBe( 'Firstname:' );
		expect( $( '#firstname' )[0].tagName ).toBe( 'INPUT' );
		expect( $( '#firstname' )[0].type ).toBe( 'text' );
		expect( $( '#firstname' ).attr( 'ng-model' ) ).toBe( 'toInspect.firstname' );		

		expect( $( '#search' )[0].tagName ).toBe( 'BUTTON' );
		expect( $( '#search' ).attr( 'ng-click' ) ).toBe( 'toInspect.search()' );		
		expect( $( '#search' ).text() ).toBe( 'Search' );

		expect( $( '#createPersonal' )[0].tagName ).toBe( 'BUTTON' );
		expect( $( '#createPersonal' ).attr( 'ng-click' ) ).toBe( 'toInspect.createPersonal()' );		
		expect( $( '#createPersonal' ).text() ).toBe( 'Create Personal' );
	} );
} );
