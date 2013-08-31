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