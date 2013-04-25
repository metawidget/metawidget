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

	describe( 'AngularJS REST', function() {

		beforeEach( function() {

			browser().navigateTo( 'index.html' );
		} );

		it( 'tests REST functionality', function() {

			expect( element( 'table thead tr td' ).text() ).toContain( 'A Header' );
			expect( element( 'table tfoot tr td' ).text() ).toContain( 'A Footer' );

			expect( element( '#restTestSave' ).prop( 'tagName' ) ).toBe( 'BUTTON' );
			expect( element( '#restTestSave' ).attr( 'ng-click' ) ).toBe( 'restTest.save()' );

			expect( element( '#table-restTestName-label' ).prop( 'for' ) ).toBe( 'restTestName' );
			expect( element( '#table-restTestName-label' ).text() ).toBe( 'Name:' );
			expect( element( '#restTestName' ).prop( 'tagName' ) ).toBe( 'INPUT' );
			expect( element( '#restTestName' ).attr( 'type' ) ).toBe( 'text' );
			input( 'restTest.name' ).enter( 'Test Name' );

			expect( element( '#table-restTestAge-label' ).prop( 'for' ) ).toBe( 'restTestAge' );
			expect( element( '#table-restTestAge-label' ).text() ).toBe( 'Age:' );
			expect( element( '#restTestAge' ).prop( 'tagName' ) ).toBe( 'INPUT' );
			expect( element( '#restTestAge' ).attr( 'type' ) ).toBe( 'number' );
			input( 'restTest.age' ).enter( '42' );

			element( '#restTestSave' ).click();

			expect( element( 'table thead tr td' ).text() ).toContain( 'A Header' );
			expect( element( 'table tfoot tr td' ).text() ).toContain( 'A Footer' );

			expect( element( '#restTestName' ).prop( 'tagName' ) ).toBe( 'OUTPUT' );
			expect( element( '#restTestName' ).text() ).toBe( 'Test Name' );

			expect( element( '#restTestAge' ).prop( 'tagName' ) ).toBe( 'OUTPUT' );
			expect( element( '#restTestAge' ).text() ).toBe( '42' );
			expect( element( '#numberOfRestCalls' ).text() ).toBe( '1' );
		} );
	} );
} )();