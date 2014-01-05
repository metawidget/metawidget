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

	describe( 'AngularJS Quirks', function() {

		beforeEach( function() {

			browser().navigateTo( 'index.html' );
		} );

		it( 'tests transcluded widgets are not recompiled', function() {

			expect( element( '#quirksClick' ).prop( 'tagName' ) ).toBe( 'BUTTON' );
			expect( element( '#quirksClick' ).attr( 'ng-click' ) ).toBe( 'clicked()' );

			expect( element( '#numberOfClicks' ).text() ).toBe( '0' );
			element( '#quirksClick' ).click();
			expect( element( '#numberOfClicks' ).text() ).toBe( '1' );
		} );
	} );
} )();