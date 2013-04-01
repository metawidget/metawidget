// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

( function() {

	'use strict';

	describe( 'AddressBook App', function() {

		beforeEach( function() {

			browser().navigateTo( 'index.html' );
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
			expect( element( '#table-search tfoot tr td' ).attr( 'colspan' ) ).toBe( '2' );
			expect( element( '#table-search tfoot tr td' ).attr( 'class' ) ).toBe( 'buttons' );
			expect( element( '#table-search tfoot tr td button:eq(0)' ).text() ).toBe( 'Search' );
			expect( element( '#table-search tfoot tr td button:eq(1)' ).text() ).toBe( 'Create Personal' );
			expect( element( '#table-search tfoot tr td button:eq(2)' ).text() ).toBe( 'Create Business' );
			expect( element( '#table-search tfoot tr td button' ).count() ).toBe( 3 );

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

		it( 'should allow editing contacts', function() {

			expect( element( 'a:eq(1)' ).text() ).toContain( 'Homer Simpson' );
			element( 'a:eq(1)' ).click();

			expect( element( '#dialog-image' ).prop( 'src' ) ).toContain( '/media/personal.gif' );

			expect( element( '#dialog-content metawidget > *:eq(0)' ).prop( 'tagName' ) ).toBe( 'TABLE' );
			expect( element( '#table-current' ).attr( 'class' ) ).toBe( 'table-form' );
			expect( element( '#table-current tbody tr:eq(0) th label' ).text() ).toBe( 'Title:' );
			expect( element( '#table-current tbody tr:eq(0) th label' ).attr( 'for' ) ).toBe( 'currentTitle' );
			expect( element( '#table-current tbody tr:eq(0) td output' ).attr( 'id' ) ).toBe( 'currentTitle' );
			expect( element( '#table-current tbody tr:eq(0) td:eq(1)' ).text() ).toBe( '' );
			expect( element( '#currentTitle' ).text() ).toBe( 'Mr' );
			expect( element( '#table-current tbody tr:eq(1) th' ).text() ).toBe( 'Firstname:' );
			expect( element( '#table-current tbody tr:eq(1) th label' ).attr( 'for' ) ).toBe( 'currentFirstname' );
			expect( element( '#table-current tbody tr:eq(1) td output' ).attr( 'id' ) ).toBe( 'currentFirstname' );
			expect( element( '#table-current tbody tr:eq(1) td:eq(1)' ).text() ).toBe( '' );
			expect( element( '#currentFirstname' ).text() ).toBe( 'Homer' );
			expect( element( '#table-current tbody tr:eq(2) th' ).text() ).toBe( 'Surname:' );
			expect( element( '#table-current tbody tr:eq(2) th label' ).attr( 'for' ) ).toBe( 'currentSurname' );
			expect( element( '#table-current tbody tr:eq(2) td output' ).attr( 'id' ) ).toBe( 'currentSurname' );
			expect( element( '#table-current tbody tr:eq(2) td:eq(1)' ).text() ).toBe( '' );
			expect( element( '#currentSurname' ).text() ).toBe( 'Simpson' );
			expect( element( '#table-current tbody tr:eq(3) th' ).text() ).toBe( 'Gender:' );
			expect( element( '#table-current tbody tr:eq(3) th label' ).attr( 'for' ) ).toBe( 'currentGender' );
			expect( element( '#table-current tbody tr:eq(3) td output' ).attr( 'id' ) ).toBe( 'currentGender' );
			expect( element( '#table-current tbody tr:eq(3) td:eq(1)' ).text() ).toBe( '' );
			expect( element( '#currentGender' ).text() ).toBe( 'Male' );
			expect( element( '#table-current tbody tr:eq(4) th' ).text() ).toBe( 'Date of Birth:' );
			expect( element( '#table-current tbody tr:eq(4) th label' ).attr( 'for' ) ).toBe( 'currentDateOfBirth' );
			expect( element( '#table-current tbody tr:eq(4) td output' ).attr( 'id' ) ).toBe( 'currentDateOfBirth' );

			expect( element( '#table-current tbody tr:eq(5) td' ).attr( 'colspan' ) ).toBe( '2' );
			expect( element( '#table-current tbody tr:eq(5) td h1' ).text() ).toBe( 'Contact Details' );

			expect( element( '#table-current tbody tr:eq(6) > th:eq(0)' ).text() ).toBe( 'Address:' );
			expect( element( '#table-current tbody tr:eq(6) > th:eq(0) label' ).attr( 'for' ) ).toBe( 'currentAddress' );
			expect( element( '#table-current tbody tr:eq(6) > td:eq(0) metawidget' ).attr( 'id' ) ).toBe( 'currentAddress' );

			expect( element( '#table-currentAddress' ).attr( 'class' ) ).toBe( 'table-form' );
			expect( element( '#table-currentAddress tbody tr:eq(0) th label' ).text() ).toBe( 'Street:' );
			expect( element( '#table-currentAddress tbody tr:eq(0) th label' ).attr( 'for' ) ).toBe( 'currentAddressStreet' );
			expect( element( '#table-currentAddress tbody tr:eq(0) td output' ).attr( 'id' ) ).toBe( 'currentAddressStreet' );
			expect( element( '#currentAddressStreet' ).text() ).toBe( '742 Evergreen Terrace' );
			expect( element( '#table-currentAddress tbody tr:eq(1) th label' ).text() ).toBe( 'City:' );
			expect( element( '#table-currentAddress tbody tr:eq(1) th label' ).attr( 'for' ) ).toBe( 'currentAddressCity' );
			expect( element( '#table-currentAddress tbody tr:eq(1) td output' ).attr( 'id' ) ).toBe( 'currentAddressCity' );
			expect( element( '#currentAddressCity' ).text() ).toBe( 'Springfield' );
			expect( element( '#table-currentAddress tbody tr:eq(2) th label' ).text() ).toBe( 'State:' );
			expect( element( '#table-currentAddress tbody tr:eq(2) th label' ).attr( 'for' ) ).toBe( 'currentAddressState' );
			expect( element( '#table-currentAddress tbody tr:eq(2) td output' ).attr( 'id' ) ).toBe( 'currentAddressState' );
			expect( element( '#currentAddressState' ).text() ).toBe( 'Anytown' );
			expect( element( '#table-currentAddress tbody tr:eq(3) th label' ).text() ).toBe( 'Postcode:' );
			expect( element( '#table-currentAddress tbody tr:eq(3) th label' ).attr( 'for' ) ).toBe( 'currentAddressPostcode' );
			expect( element( '#table-currentAddress tbody tr:eq(3) td output' ).attr( 'id' ) ).toBe( 'currentAddressPostcode' );
			expect( element( '#currentAddressPostcode' ).text() ).toBe( '90701' );
			expect( element( '#table-currentAddress tbody tr' ).count() ).toBe( 4 );

			expect( element( '#table-current tbody > tr:eq(7) th:eq(0)' ).text() ).toBe( 'Communications:' );
			expect( element( '#table-current tbody > tr:eq(7) th:eq(0) label' ).attr( 'for' ) ).toBe( 'currentCommunications' );
			expect( element( '#table-current tbody > tr:eq(7) td:eq(0) table' ).attr( 'id' ) ).toBe( 'currentCommunications' );
			expect( element( '#table-current tbody > tr:eq(7) td:eq(0) table' ).attr( 'id' ) ).toBe( 'currentCommunications' );
			expect( element( '#table-current tbody > tr:eq(7) td:eq(0) table tbody tr:eq(0) td:eq(0)' ).text() ).toBe( 'Telephone' );
			expect( element( '#table-current tbody > tr:eq(7) td:eq(0) table tbody tr:eq(0) td:eq(1)' ).text() ).toBe( '(939) 555-0113' );
			expect( element( '#table-current tbody > tr:eq(7) td:eq(0) table tbody tr' ).count() ).toBe( 1 );
			expect( element( '#currentCommunications tfoot' ).attr( 'style' ) ).toContain( 'display: none;' );

			expect( element( '#table-current tbody > tr:eq(8) td' ).attr( 'colspan' ) ).toBe( '2' );
			expect( element( '#table-current tbody > tr:eq(8) td h1' ).text() ).toBe( 'Other' );

			expect( element( '#table-current tbody > tr:eq(9) th:eq(0)' ).text() ).toBe( 'Notes:' );
			expect( element( '#table-current tbody > tr:eq(9) th:eq(0) label' ).attr( 'for' ) ).toBe( 'currentNotes' );
			expect( element( '#table-current tbody > tr:eq(9) td:eq(0) output' ).attr( 'id' ) ).toBe( 'currentNotes' );
			expect( element( '#table-current tfoot:eq(0) tr td' ).attr( 'colspan' ) ).toBe( '2' );
			expect( element( '#table-current tfoot:eq(0) tr td' ).attr( 'class' ) ).toBe( 'buttons' );
			expect( element( '#table-current tfoot:eq(0) tr td button' ).count() ).toBe( 2 );

			element( '#crudActionsEdit' ).click();

			expect( element( '#table-current tbody tr:eq(0) td select' ).attr( 'id' ) ).toBe( 'currentTitle' );
			expect( element( '#table-current tbody tr:eq(0) td select' ).attr( 'required' ) ).toBe( 'required' );
			expect( element( '#table-current tbody tr:eq(0) td:eq(1)' ).text() ).toBe( '*' );
			expect( input( 'current.title' ).val() ).toBe( 'Mr' );
			expect( element( '#table-current tbody tr:eq(1) td input' ).attr( 'id' ) ).toBe( 'currentFirstname' );
			expect( element( '#table-current tbody tr:eq(1) td input' ).attr( 'required' ) ).toBe( 'required' );
			expect( element( '#table-current tbody tr:eq(1) td:eq(1)' ).text() ).toBe( '*' );
			expect( input( 'current.firstname' ).val() ).toBe( 'Homer' );
			expect( element( '#table-current tbody tr:eq(2) td input' ).attr( 'id' ) ).toBe( 'currentSurname' );
			expect( element( '#table-current tbody tr:eq(2) td input' ).attr( 'required' ) ).toBe( 'required' );
			expect( element( '#table-current tbody tr:eq(2) td:eq(1)' ).text() ).toBe( '*' );
			expect( input( 'current.surname' ).val() ).toBe( 'Simpson' );
			expect( element( '#table-current tbody tr:eq(3) td select' ).attr( 'id' ) ).toBe( 'currentGender' );
			expect( element( '#table-current tbody tr:eq(3) td select' ).attr( 'required' ) ).toBe( undefined );
			expect( element( '#table-current tbody tr:eq(3) td:eq(1)' ).text() ).toBe( '' );
			expect( input( 'current.gender' ).val() ).toBe( 'Male' );
			expect( element( '#table-current tbody tr:eq(4) td input' ).attr( 'id' ) ).toBe( 'currentDateOfBirth' );
			expect( element( '#table-current tbody tr:eq(4) td input' ).attr( 'type' ) ).toBe( 'date' );

			expect( element( '#table-current tbody tr:eq(5) td' ).attr( 'colspan' ) ).toBe( '2' );
			expect( element( '#table-current tbody tr:eq(5) td h1' ).text() ).toBe( 'Contact Details' );

			expect( element( '#table-currentAddress tbody tr:eq(0) td input' ).attr( 'id' ) ).toBe( 'currentAddressStreet' );
			expect( input( 'current.address.street' ).val() ).toBe( '742 Evergreen Terrace' );
			expect( element( '#table-currentAddress tbody tr:eq(1) td input' ).attr( 'id' ) ).toBe( 'currentAddressCity' );
			expect( input( 'current.address.city' ).val() ).toBe( 'Springfield' );
			expect( element( '#table-currentAddress tbody tr:eq(2) td select' ).attr( 'id' ) ).toBe( 'currentAddressState' );
			expect( input( 'current.address.state' ).val() ).toBe( 'Anytown' );
			expect( element( '#table-currentAddress tbody tr:eq(3) td input' ).attr( 'id' ) ).toBe( 'currentAddressPostcode' );
			expect( input( 'current.address.postcode' ).val() ).toBe( '90701' );

			expect( element( '#table-current tbody > tr:eq(7) td:eq(0) table' ).attr( 'id' ) ).toBe( 'currentCommunications' );
			expect( element( '#currentCommunications tfoot tr td:eq(0) select' ).attr( 'id' ) ).toBe( 'communicationType' );
			expect( element( '#currentCommunications tfoot tr td:eq(1) input' ).attr( 'id' ) ).toBe( 'communicationValue' );

			expect( element( '#table-current tbody > tr:eq(8) td' ).attr( 'colspan' ) ).toBe( '2' );
			expect( element( '#table-current tbody > tr:eq(8) td h1' ).text() ).toBe( 'Other' );

			expect( element( '#table-current tbody > tr:eq(9) td:eq(0) textarea' ).attr( 'id' ) ).toBe( 'currentNotes' );

			input( 'current.firstname' ).enter( 'Homer Jay' );
			select( 'communication.type' ).option( 'Fax' );
			input( 'communication.value' ).enter( '(939) 555-0114' );
			element( '#currentCommunications tfoot tr td:eq(2) button' ).click();
			expect( element( '#table-current tbody > tr:eq(7) td:eq(0) table tbody tr:eq(1) td:eq(0)' ).text() ).toBe( 'Fax' );
			expect( element( '#table-current tbody > tr:eq(7) td:eq(0) table tbody tr:eq(1) td:eq(1)' ).text() ).toBe( '(939) 555-0114' );
			expect( element( '#table-current tbody > tr:eq(7) td:eq(0) table tbody tr' ).count() ).toBe( 2 );
			expect( input( 'communication.type' ).val() ).toBe( '' );
			select( 'communication.type' ).option( 'E-mail' );
			input( 'communication.value' ).enter( 'homer@simpsons.com' );
			expect( element( '#currentCommunications tfoot tr td:eq(2)' ).attr( 'class' ) ).toBe( 'column-tiny, table-buttons' );
			element( '#currentCommunications tfoot tr td:eq(2) button' ).click();
			expect( element( '#table-current tbody > tr:eq(7) td:eq(0) table tbody tr:eq(2) td:eq(0)' ).text() ).toBe( 'E-mail' );
			expect( element( '#table-current tbody > tr:eq(7) td:eq(0) table tbody tr:eq(2) td:eq(1)' ).text() ).toBe( 'homer@simpsons.com' );
			expect( element( '#table-current tbody > tr:eq(7) td:eq(0) table tbody tr' ).count() ).toBe( 3 );
			element( '#table-current tbody > tr:eq(7) td:eq(0) table tbody tr:eq(0) td:eq(2) button' ).click();
			expect( element( '#table-current tbody > tr:eq(7) td:eq(0) table tbody tr' ).count() ).toBe( 2 );
			input( 'current.address.street' ).enter( '742 Evergreen Terrace #2' );
			input( 'current.address.city' ).enter( 'Springfield #2' );
			select( 'current.address.state' ).option( 'Cyberton' );
			input( 'current.address.postcode' ).enter( '90701 #2' );

			element( '#crudActionsSave' ).click();

			expect( element( '.data-table tbody tr:eq(1) td:eq(0) a' ).text() ).toContain( 'Mr Homer Jay Simpson' );
			expect( element( '.data-table tbody tr:eq(1) td:eq(1)' ).text() ).toContain( 'Fax: (939) 555-0114' );
			expect( element( '.data-table tbody tr:eq(1) td:eq(1)' ).text() ).toContain( 'E-mail: homer@simpsons.com' );
			element( 'a:eq(1)' ).click();

			expect( element( '#currentAddressStreet' ).text() ).toBe( '742 Evergreen Terrace #2' );
			expect( element( '#currentAddressCity' ).text() ).toBe( 'Springfield #2' );
			expect( element( '#currentAddressState' ).text() ).toBe( 'Cyberton' );
			expect( element( '#currentAddressPostcode' ).text() ).toBe( '90701 #2' );

			element( '#crudActionsCancel' ).click();

			expect( element( '.data-table tbody tr:eq(2) td:eq(0) a' ).text() ).toContain( 'Mrs Marjorie Simpson' );
			element( 'a:eq(2)' ).click();
			element( '#crudActionsEdit' ).click();

			expect( input( 'current.notes' ).val() ).toBe( "Known as 'Marge'" );
			input( 'current.notes' ).enter( "Known as 'Marge'!" );
			element( '#crudActionsSave' ).click();

			element( 'a:eq(2)' ).click();
			expect( element( '#currentNotes' ).text() ).toBe( "Known as 'Marge'!" );

			expect( element( '.data-table tbody tr:eq(0) td:eq(0) a' ).text() ).toContain( 'Mr Charles Montgomery Burns' );
			element( 'a:eq(0)' ).click();
			expect( element( '#dialog-image' ).prop( 'src' ) ).toContain( '/media/business.gif' );
			element( '#crudActionsEdit' ).click();

			expect( element( '#table-current tbody > tr:eq(9) td:eq(0) input' ).attr( 'type' ) ).toBe( 'range' );
			expect( element( '#table-current tbody > tr:eq(9) td:eq(0) input' ).attr( 'min' ) ).toBe( '0' );
			expect( element( '#table-current tbody > tr:eq(9) td:eq(0) input' ).attr( 'max' ) ).toBe( '100' );
		} );

		it( 'should allow creating new contacts', function() {

			element( '#searchActionsCreatePersonal' ).click();
			expect( element( '#table-current tbody tr:eq(4) td input' ).attr( 'type' ) ).toBe( 'date' );
			expect( element( '#dialog-content tfoot:eq(0) button:eq(0)' ).text() ).toBe( 'Save' );
			expect( element( '#dialog-content tfoot:eq(0) button:eq(1)' ).text() ).toBe( 'Cancel' );
			expect( element( '#dialog-content tfoot:eq(0) button' ).count() ).toBe( 2 );
			expect( element( '#dialog-content tfoot:eq(1) button' ).count() ).toBe( 1 );
			element( '#crudActionsCancel' ).click();

			element( '#searchActionsCreateBusiness' ).click();
			expect( input( 'current.title' ).val() ).toBe( 'Mr' );
			select( 'current.title' ).option( 'Miss' );
			input( 'current.firstname' ).enter( 'Business' );
			input( 'current.surname' ).enter( 'Contact' );
			input( 'current.company' ).enter( 'Some Company' );
			select( 'current.gender' ).option( 'Female' );
			select( 'communication.type' ).option( 'Mobile' );
			input( 'communication.value' ).enter( '(0402) 456 123' );
			element( '#currentCommunications tfoot tr td:eq(2) button' ).click();
			input( 'current.numberOfStaff' ).enter( '2' );

			element( '#crudActionsSave' ).click();

			expect( element( '.data-table tbody tr:eq(0) td:eq(0) a' ).text() ).toContain( 'Miss Business Contact' );
			expect( element( '.data-table tbody tr:eq(0) td:eq(1)' ).text() ).toContain( 'Mobile: (0402) 456 123' );
			expect( element( '.data-table tbody tr' ).count() ).toBe( 7 );
			element( 'a:eq(0)' ).click();

			element( '#crudActionsEdit' ).click();
			expect( input( 'current.company' ).val() ).toBe( 'Some Company' );
			expect( input( 'current.numberOfStaff' ).val() ).toBe( '2' );

			element( '#crudActionsDelete' ).click();

			expect( element( '.data-table tbody tr:eq(0) td:eq(0) a' ).text() ).toContain( 'Mr Charles Montgomery Burns' );
			expect( element( '.data-table tbody tr' ).count() ).toBe( 6 );
		} );
	} );
} )();