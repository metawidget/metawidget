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

		expect( repeater( '.data-table tbody tr' ).count() ).toBe( 3 );

		select( 'search.type' ).option( 'personal' );
		element( '#searchActionsSearch' ).click();
		expect( repeater( '.data-table tbody tr' ).count() ).toBe( 2 );		

		select( 'search.type' ).option( 'business' );
		element( '#searchActionsSearch' ).click();
		expect( repeater( '.data-table tbody tr' ).count() ).toBe( 1 );		

		select( 'search.type' ).option( '' );
		element( '#searchActionsSearch' ).click();
		expect( repeater( '.data-table tbody tr' ).count() ).toBe( 3 );		
	} );

	it( 'should allow retrieving contacts', function() {

		expect( element( 'a:eq(0)' ).text() ).toContain( 'Homer Simpson' );
		element( 'a:eq(0)' ).click();
		expect( element( '#table-currentTitle-label' ).text() ).toBe( 'Title:' );
		expect( element( '#currentTitle' ).text() ).toBe( 'Mr' );
		expect( element( '#table-currentFirstname-label' ).text() ).toBe( 'Firstname:' );
		expect( element( '#currentFirstname' ).text() ).toBe( 'Homer' );
		expect( element( '#table-currentSurname-label' ).text() ).toBe( 'Surname:' );
		expect( element( '#currentSurname' ).text() ).toBe( 'Simpson' );
		
		element( '#crudActionsEdit' ).click();		

		expect( element( '#table-currentTitle-label' ).text() ).toBe( 'Title:' );
		expect( element( '#table-currentFirstname-label' ).text() ).toBe( 'Firstname:' );
		expect( input( 'current.firstname' ).val() ).toBe( 'Homer' );
		expect( element( '#table-currentSurname-label' ).text() ).toBe( 'Surname:' );
		expect( input( 'current.surname' ).val() ).toBe( 'Simpson' );
		
		input( 'current.firstname' ).val( 'Homer1' );
		element( '#crudActionsSave' ).click();

		expect( element( 'a:eq(0)' ).text() ).toContain( 'Homer1 Simpson' );
	} );
} );
