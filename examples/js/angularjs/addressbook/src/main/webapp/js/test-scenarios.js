'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe( 'AddressBook App', function() {

	beforeEach( function() {

		browser().navigateTo( '/addressbook-angularjs/index.html' );
	} );

	it( 'should redirect index.html to root', function() {

		expect( browser().location().url() ).toBe( '' );
	} );

	it( 'should allow searching contacts', function() {

		expect( repeater( '.data-table tr' ).count() ).toBe( 4 );
		
		//select( 'searchType' ).option( 'personal' );
		//element( 'searchActionsSearch' ).click();
		//expect( repeater( '.data-table tr' ).count() ).toBe( 2 );		
	} );
} );
