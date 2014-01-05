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

	describe( "JQuery UI Quirks", function() {

		it( "tests tab decorator", function() {

			expect( $( 'table tbody tr:eq(0)' ).attr( 'id' ) ).toBe( 'table-foo-row' );
			expect( $( 'table tbody tr:eq(1) td' ).attr( 'colspan' ) ).toBe( '2' );
			expect( $( 'table tbody tr:eq(1) td div' ).attr( 'id' ) ).toBe( 'bar-tabs' );

			// Tabs

			expect( $( '#bar-tabs' ).attr( 'class' ) ).toContain( 'ui-tabs' );
			expect( $( '#bar-tabs ul' ).attr( 'role' ) ).toBe( 'tablist' );
			expect( $( '#bar-tabs ul li:eq(0)' ).attr( 'role' ) ).toBe( 'tab' );
			expect( $( '#bar-tabs ul li:eq(0) a' ).attr( 'href' ) ).toBe( '#bar-tabs1' );
			expect( $( '#bar-tabs ul li:eq(1)' ).attr( 'role' ) ).toBe( 'tab' );
			expect( $( '#bar-tabs ul li:eq(1) a' ).attr( 'href' ) ).toBe( '#bar-tabs2' );
			expect( $( '#bar-tabs > ul li' ).length ).toBe( 2 );
			expect( $( '#bar-tabs div:eq(0)' ).attr( 'id' ) ).toBe( 'bar-tabs1' );
			expect( $( '#bar-tabs1' ).attr( 'role' ) ).toBe( 'tabpanel' );
			expect( $( '#bar-tabs1 table tbody tr:eq(0)' ).attr( 'id' ) ).toBe( 'table-bar-row' );
			expect( $( '#bar-tabs1 table tbody tr:eq(1)' ).attr( 'id' ) ).toBe( 'table-baz-row' );
			expect( $( '#bar-tabs1 table tbody tr:eq(2) td div' ).attr( 'id' ) ).toBe( 'abc-tabs' );

			// Nested tabs

			expect( $( '#abc-tabs ul li:eq(0)' ).text() ).toBe( 'Section 1.1' );
			expect( $( '#abc-tabs ul li' ).length ).toBe( 1 );
			expect( $( '#abc-tabs div' ).attr( 'id' ) ).toBe( 'abc-tabs1' );
			expect( $( '#abc-tabs div table tbody tr' ).attr( 'id' ) ).toBe( 'table-abc-row' );
			expect( $( '#abc-tabs div table tbody tr' ).length ).toBe( 1 );
			expect( $( '#bar-tabs1 > table > tbody > tr' ).length ).toBe( 3 );
			expect( $( '#bar-tabs > div:eq(1)' ).attr( 'id' ) ).toBe( 'bar-tabs2' );
			expect( $( '#bar-tabs2' ).attr( 'role' ) ).toBe( 'tabpanel' );
			expect( $( '#bar-tabs2 table tbody tr:eq(0)' ).attr( 'id' ) ).toBe( 'table-def-row' );
			expect( $( '#bar-tabs2 table tbody tr' ).length ).toBe( 1 );
			expect( $( '#bar-tabs > div' ).length ).toBe( 2 );

			expect( $( '#metawidget > table > tbody > tr' ).length ).toBe( 3 );
		} );
	} );
} )();