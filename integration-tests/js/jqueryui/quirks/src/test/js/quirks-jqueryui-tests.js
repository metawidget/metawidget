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

describe( "JQuery UI Quirks", function() {

	it( "tests tab decorator", function() {

		expect( $( 'table tbody tr:eq(0)' ).attr( 'id' ) ).toBe( 'table-foo-row' );
		expect( $( 'table tbody tr:eq(1) td' ).attr( 'colspan' ) ).toBe( '2' );
		expect( $( 'table tbody tr:eq(1) td div' ).attr( 'id' ) ).toBe( 'bar-tabs' );
		expect( $( '#bar-tabs' ).attr( 'class' ) ).toContain( 'ui-tabs' );
		expect( $( '#bar-tabs ul' ).attr( 'role' ) ).toBe( 'tablist' );
		expect( $( '#bar-tabs ul li:eq(0)' ).attr( 'role' ) ).toBe( 'tab' );
		expect( $( '#bar-tabs ul li:eq(0) a' ).attr( 'href' ) ).toBe( '#bar-tabs1' );
		expect( $( '#bar-tabs ul li:eq(1)' ).attr( 'role' ) ).toBe( 'tab' );
		expect( $( '#bar-tabs ul li:eq(1) a' ).attr( 'href' ) ).toBe( '#bar-tabs2' );
		expect( $( '#bar-tabs ul li' ).length ).toBe( 2 );
		expect( $( '#bar-tabs div:eq(0)' ).attr( 'id' ) ).toBe( 'bar-tabs1' );
		expect( $( '#bar-tabs div:eq(0)' ).attr( 'role' ) ).toBe( 'tabpanel' );
		expect( $( '#bar-tabs div:eq(1)' ).attr( 'id' ) ).toBe( 'bar-tabs2' );
		expect( $( '#bar-tabs div:eq(1)' ).attr( 'role' ) ).toBe( 'tabpanel' );
		expect( $( '#bar-tabs div' ).length ).toBe( 2 );
	} );
} );
