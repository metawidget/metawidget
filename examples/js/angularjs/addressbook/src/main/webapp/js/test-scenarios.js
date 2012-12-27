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

		expect( element( '#table-search tbody tr:eq(0)' ).attr( 'id' ) ).toBe( 'table-searchFirstname-row' );
		expect( element( '#table-search tbody tr:eq(0) td' ).attr( 'id' ) ).toBe( 'table-searchFirstname-cell' );
		
		expect( element( '#table-search' ).attr( 'class' ) ).toBe( 'table-form' );
		expect( element( '#table-search tbody tr:eq(0) th label' ).text() ).toBe( 'Firstname:' );
		expect( element( '#table-search tbody tr:eq(0) th label' ).attr( 'for' ) ).toBe( 'searchFirstname' );
		expect( element( '#table-search tbody tr:eq(0) td input' ).attr( 'type' ) ).toBe( 'text' );
		expect( element( '#table-search tbody tr:eq(0) td input' ).attr( 'id' ) ).toBe( 'searchFirstname' );
		expect( element( '#table-search tbody tr:eq(1) th' ).text() ).toBe( 'Surname:' );
		expect( element( '#table-search tbody tr:eq(1) th label' ).attr( 'for' ) ).toBe( 'searchSurname' );
		expect( element( '#table-search tbody tr:eq(1) td input' ).attr( 'type' ) ).toBe( 'text' );
		expect( element( '#table-search tbody tr:eq(1) td input' ).attr( 'id' ) ).toBe( 'searchSurname' );
		expect( element( '#table-search tbody tr:eq(2) th' ).text() ).toBe( 'Type:' );
		expect( element( '#table-search tbody tr:eq(2) th label' ).attr( 'for' ) ).toBe( 'searchType' );
		expect( element( '#table-search tbody tr:eq(2) td select' ).attr( 'id' ) ).toBe( 'searchType' );
		
		expect( element( '.data-table tbody a:eq(0)' ).text() ).toContain( 'Mr Charles Montgomery Burns' );
		expect( element( '.data-table tbody a:eq(1)' ).text() ).toContain( 'Mr Homer Simpson' );
		expect( element( '.data-table tbody a:eq(2)' ).text() ).toContain( 'Mrs Marjorie Simpson' );
		expect( element( '.data-table tbody a:eq(3)' ).text() ).toContain( 'Mrs Maude Flanders' );
		expect( element( '.data-table tbody a:eq(4)' ).text() ).toContain( 'Mr Nedward Flanders' );
		expect( element( '.data-table tbody a:eq(5)' ).text() ).toContain( 'Mr Waylon Smithers' );
		expect( element( '.data-table tbody tr' ).count() ).toBe( 6 );

		select( 'search.type' ).option( 'personal' );
		element( '#searchActionsSearch' ).click();
		expect( element( '.data-table tbody a:eq(0)' ).text() ).toContain( 'Mr Homer Simpson' );
		expect( element( '.data-table tbody a:eq(1)' ).text() ).toContain( 'Mrs Marjorie Simpson' );
		expect( element( '.data-table tbody a:eq(2)' ).text() ).toContain( 'Mrs Maude Flanders' );
		expect( element( '.data-table tbody a:eq(3)' ).text() ).toContain( 'Mr Nedward Flanders' );
		expect( repeater( '.data-table tbody tr' ).count() ).toBe( 4 );		

		select( 'search.type' ).option( 'business' );
		element( '#searchActionsSearch' ).click();
		expect( element( '.data-table tbody a:eq(0)' ).text() ).toContain( 'Mr Charles Montgomery Burns' );
		expect( element( '.data-table tbody a:eq(1)' ).text() ).toContain( 'Mr Waylon Smithers' );
		expect( repeater( '.data-table tbody tr' ).count() ).toBe( 2 );		

		select( 'search.type' ).option( '' );
		element( '#searchActionsSearch' ).click();
		expect( repeater( '.data-table tbody tr' ).count() ).toBe( 6 );		

		input( 'search.surname' ).enter( 'Simpson' );
		element( '#searchActionsSearch' ).click();
		expect( element( '.data-table tbody a:eq(0)' ).text() ).toContain( 'Mr Homer Simpson' );
		expect( element( '.data-table tbody a:eq(1)' ).text() ).toContain( 'Mrs Marjorie Simpson' );
		expect( repeater( '.data-table tbody tr' ).count() ).toBe( 2 );		
	} );

	it( 'should allow retrieving contacts', function() {

		expect( element( 'a:eq(1)' ).text() ).toContain( 'Homer Simpson' );
		element( 'a:eq(1)' ).click();
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
		
		input( 'current.firstname' ).enter( 'Homer1' );
		element( '#crudActionsSave' ).click();

		expect( element( 'a:eq(1)' ).text() ).toContain( 'Homer1 Simpson' );
	} );
} );
