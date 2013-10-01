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