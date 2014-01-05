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

	describe( "JQuery UI REST", function() {

		it( "tests REST functionality", function() {

			expect( $( '#save' ).attr( 'type' ) ).toBe( 'button' );
			expect( $( '#save' ).val() ).toBe( 'Save' );
			expect( $( '#name' ).attr( 'type' ) ).toBe( 'text' );
			$( '#name' ).val( 'Name1' );
			expect( $( '#age' ).attr( 'class' ) ).toContain( 'ui-spinner' );
			$( '#age input' ).val( '42' );

			$( '#save' ).click();

			expect( $( '#name' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#name' ).text() ).toBe( 'Name1' );
			expect( $( '#age' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#age' ).text() ).toBe( '42' );
		} );
	} );
} )();