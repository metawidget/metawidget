// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

( function() {

	'use strict';

	describe( 'AngularJS REST', function() {

		beforeEach( function() {

			browser().navigateTo( 'index.html' );
		} );

		it( 'tests REST functionality', function() {

			expect( element( 'table thead tr td' ).text() ).toContain( 'A Header' );
			expect( element( 'table tfoot tr td' ).text() ).toContain( 'A Footer' );

			expect( element( '#restTestSave' ).prop( 'tagName' ) ).toBe( 'INPUT' );
			expect( element( '#restTestSave' ).attr( 'type' ) ).toBe( 'button' );
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