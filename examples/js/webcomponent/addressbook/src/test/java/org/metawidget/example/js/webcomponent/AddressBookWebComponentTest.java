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
// this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
// this list of conditions and the following disclaimer in the documentation
// and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
// be used to endorse or promote products derived from this software without
// specific prior written permission.
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

package org.metawidget.example.js.webcomponent;

import static org.junit.Assert.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class AddressBookWebComponentTest {

	//
	// Private members
	//

	private static final String	BASE_URL	= "http://localhost:8180/addressbook-webcomponent";

	//
	// Private members
	//

	private FirefoxDriver		mDriver;

	private WebDriverWait		mWait;

	//
	// Public methods
	//

	@Test
	public void testEditing()
		throws Exception {

		mDriver.get( BASE_URL );

		// Search

		mWait.until( visibilityOfElementLocated( By.id( "addressbookSearchFirstname" ) ) );
		assertEquals( "Firstname:", mDriver.findElementById( "table-addressbookSearchFirstname-label" ).getText() );
		assertEquals( "input", mDriver.findElementById( "addressbookSearchFirstname" ).getTagName() );
		assertEquals( "text", mDriver.findElementById( "addressbookSearchFirstname" ).getAttribute( "type" ) );
		assertEquals( "Surname:", mDriver.findElementById( "table-addressbookSearchSurname-label" ).getText() );
		assertEquals( "input", mDriver.findElementById( "addressbookSearchSurname" ).getTagName() );
		assertEquals( "text", mDriver.findElementById( "addressbookSearchSurname" ).getAttribute( "type" ) );
		assertEquals( "Type:", mDriver.findElementById( "table-addressbookSearchType-label" ).getText() );
		assertEquals( "select", mDriver.findElementById( "addressbookSearchType" ).getTagName() );
		assertEquals( "", mDriver.findElementsByCssSelector( "#addressbookSearchType option" ).get( 0 ).getText() );
		assertEquals( "Personal", mDriver.findElementsByCssSelector( "#addressbookSearchType option" ).get( 1 ).getText() );
		assertEquals( "Business", mDriver.findElementsByCssSelector( "#addressbookSearchType option" ).get( 2 ).getText() );
		assertEquals( 3, mDriver.findElementsByCssSelector( "#addressbookSearchType option" ).size() );
		assertEquals( 3, mDriver.findElementsByCssSelector( "#table-addressbookSearch tbody tr" ).size() );
		assertEquals( 1, mDriver.findElementsByCssSelector( "#table-addressbookSearch tfoot tr" ).size() );

		assertEquals( 6, mDriver.findElementsByCssSelector( ".data-table tbody tr" ).size() );
		new Select( mDriver.findElementById( "addressbookSearchType" )).selectByIndex( 1 );
		mDriver.findElementById( "addressbookSearchActionsSearch" ).click();
		Thread.sleep( 1000 );

		assertEquals( 4, mDriver.findElementsByCssSelector( ".data-table tbody tr" ).size() );

		mDriver.findElementById( "addressbookSearchFirstname" ).sendKeys( "Homer" );
		mDriver.findElementById( "addressbookSearchActionsSearch" ).click();
		Thread.sleep( 1000 );

		assertEquals( 1, mDriver.findElementsByCssSelector( ".data-table tbody tr" ).size() );
		mDriver.findElementByLinkText( "Mr Homer Simpson" ).click();

		// View

		mWait.until( visibilityOfElementLocated( By.id( "addressbookCurrentTitle" ) ) );
		assertEquals( "Homer Simpson", mDriver.findElementById( "dialog-heading" ).getText() );
		assertEquals( BASE_URL + "/media/personal.gif", mDriver.findElementById( "dialog-image" ).getAttribute( "src" ) );
		assertEquals( "Title:", mDriver.findElementById( "table-addressbookCurrentTitle-label" ).getText() );
		assertEquals( "output", mDriver.findElementById( "addressbookCurrentTitle" ).getTagName() );
		assertEquals( "Mr", mDriver.findElementById( "addressbookCurrentTitle" ).getText() );
		assertEquals( "Firstname:", mDriver.findElementById( "table-addressbookCurrentFirstname-label" ).getText() );
		assertEquals( "output", mDriver.findElementById( "addressbookCurrentFirstname" ).getTagName() );
		assertEquals( "Homer", mDriver.findElementById( "addressbookCurrentFirstname" ).getText() );
		assertEquals( "Surname:", mDriver.findElementById( "table-addressbookCurrentSurname-label" ).getText() );
		assertEquals( "output", mDriver.findElementById( "addressbookCurrentSurname" ).getTagName() );
		assertEquals( "Simpson", mDriver.findElementById( "addressbookCurrentSurname" ).getText() );
		assertEquals( "Gender:", mDriver.findElementById( "table-addressbookCurrentGender-label" ).getText() );
		assertEquals( "output", mDriver.findElementById( "addressbookCurrentGender" ).getTagName() );
		assertEquals( "Male", mDriver.findElementById( "addressbookCurrentGender" ).getText() );
		assertEquals( "Contact Details", mDriver.findElementsByTagName( "h1" ).get( 0 ).getText() );
		assertEquals( "Address:", mDriver.findElementById( "table-addressbookCurrentAddress-label" ).getText() );
		assertEquals( "Street:", mDriver.findElementById( "table-addressbookCurrentAddressStreet-label" ).getText() );
		assertEquals( "output", mDriver.findElementById( "addressbookCurrentAddressStreet" ).getTagName() );
		assertEquals( "742 Evergreen Terrace", mDriver.findElementById( "addressbookCurrentAddressStreet" ).getText() );
		assertEquals( "City:", mDriver.findElementById( "table-addressbookCurrentAddressCity-label" ).getText() );
		assertEquals( "output", mDriver.findElementById( "addressbookCurrentAddressCity" ).getTagName() );
		assertEquals( "Springfield", mDriver.findElementById( "addressbookCurrentAddressCity" ).getText() );
		assertEquals( "State:", mDriver.findElementById( "table-addressbookCurrentAddressState-label" ).getText() );
		assertEquals( "output", mDriver.findElementById( "addressbookCurrentAddressState" ).getTagName() );
		assertEquals( "Anytown", mDriver.findElementById( "addressbookCurrentAddressState" ).getText() );
		assertEquals( "Postcode:", mDriver.findElementById( "table-addressbookCurrentAddressPostcode-label" ).getText() );
		assertEquals( "output", mDriver.findElementById( "addressbookCurrentAddressPostcode" ).getTagName() );
		assertEquals( "90701", mDriver.findElementById( "addressbookCurrentAddressPostcode" ).getText() );
		assertEquals( "Communications:", mDriver.findElementById( "table-addressbookCurrentCommunications-label" ).getText() );
		assertEquals( "Other", mDriver.findElementsByTagName( "h1" ).get( 1 ).getText() );
		assertEquals( "Notes:", mDriver.findElementById( "table-addressbookCurrentNotes-label" ).getText() );
		assertEquals( "output", mDriver.findElementById( "addressbookCurrentNotes" ).getTagName() );
		assertEquals( 0, mDriver.findElementsById( "addressbookCrudActionsSave" ).size() );
		assertEquals( 0, mDriver.findElementsById( "addressbookCrudActionsDelete" ).size() );

		// Edit

		mDriver.findElementById( "addressbookCrudActionsEdit" ).click();
		mWait.until( visibilityOfElementLocated( By.id( "addressbookCrudActionsSave" ) ) );
		assertEquals( 1, mDriver.findElementsById( "addressbookCrudActionsDelete" ).size() );
		assertEquals( "Title:", mDriver.findElementById( "table-addressbookCurrentTitle-label" ).getText() );
		Select titleSelect = new Select( mDriver.findElementById( "addressbookCurrentTitle" ) );
		assertEquals( "Mr", titleSelect.getFirstSelectedOption().getText() );
		assertEquals( "Mr", titleSelect.getOptions().get( 0 ).getText() );
		assertEquals( "Cpt", titleSelect.getOptions().get( 4 ).getText() );
		assertEquals( 5, titleSelect.getOptions().size() );
		assertEquals( "Firstname:", mDriver.findElementById( "table-addressbookCurrentFirstname-label" ).getText() );
		assertEquals( "input", mDriver.findElementById( "addressbookCurrentFirstname" ).getTagName() );
		assertEquals( "Homer", mDriver.findElementById( "addressbookCurrentFirstname" ).getAttribute( "value" ) );
		assertEquals( "Surname:", mDriver.findElementById( "table-addressbookCurrentSurname-label" ).getText() );
		assertEquals( "input", mDriver.findElementById( "addressbookCurrentSurname" ).getTagName() );
		assertEquals( "Simpson", mDriver.findElementById( "addressbookCurrentSurname" ).getAttribute( "value" ) );
		assertEquals( "Gender:", mDriver.findElementById( "table-addressbookCurrentGender-label" ).getText() );
		Select genderSelect = new Select( mDriver.findElementById( "addressbookCurrentGender" ) );
		assertEquals( "Male", genderSelect.getFirstSelectedOption().getText() );
		assertEquals( "", genderSelect.getOptions().get( 0 ).getText() );
		assertEquals( "Male", genderSelect.getOptions().get( 1 ).getText() );
		assertEquals( "Female", genderSelect.getOptions().get( 2 ).getText() );
		assertEquals( 3, genderSelect.getOptions().size() );
		assertEquals( "input", mDriver.findElementById( "addressbookCurrentDateOfBirth" ).getTagName() );
		assertEquals( "Contact Details", mDriver.findElementsByTagName( "h1" ).get( 0 ).getText() );
		assertEquals( "Address:", mDriver.findElementById( "table-addressbookCurrentAddress-label" ).getText() );
		assertEquals( "Street:", mDriver.findElementById( "table-addressbookCurrentAddressStreet-label" ).getText() );
		assertEquals( "input", mDriver.findElementById( "addressbookCurrentAddressStreet" ).getTagName() );
		assertEquals( "742 Evergreen Terrace", mDriver.findElementById( "addressbookCurrentAddressStreet" ).getAttribute( "value" ) );
		assertEquals( "City:", mDriver.findElementById( "table-addressbookCurrentAddressCity-label" ).getText() );
		assertEquals( "input", mDriver.findElementById( "addressbookCurrentAddressCity" ).getTagName() );
		assertEquals( "Springfield", mDriver.findElementById( "addressbookCurrentAddressCity" ).getAttribute( "value" ) );
		assertEquals( "State:", mDriver.findElementById( "table-addressbookCurrentAddressState-label" ).getText() );
		Select stateSelect = new Select( mDriver.findElementById( "addressbookCurrentAddressState" ) );
		assertEquals( "Anytown", stateSelect.getFirstSelectedOption().getText() );
		assertEquals( "", stateSelect.getOptions().get( 0 ).getText() );
		assertEquals( 5, stateSelect.getOptions().size() );
		assertEquals( "Postcode:", mDriver.findElementById( "table-addressbookCurrentAddressPostcode-label" ).getText() );
		assertEquals( "input", mDriver.findElementById( "addressbookCurrentAddressPostcode" ).getTagName() );
		assertEquals( "90701", mDriver.findElementById( "addressbookCurrentAddressPostcode" ).getAttribute( "value" ) );
		assertEquals( "Communications:", mDriver.findElementById( "table-addressbookCurrentCommunications-label" ).getText() );
		assertEquals( "Other", mDriver.findElementsByTagName( "h1" ).get( 1 ).getText() );
		assertEquals( "Notes:", mDriver.findElementById( "table-addressbookCurrentNotes-label" ).getText() );
		assertEquals( "textarea", mDriver.findElementById( "addressbookCurrentNotes" ).getTagName() );

		// Save

		mDriver.findElementById( "addressbookCurrentFirstname" ).clear();
		mDriver.findElementById( "addressbookCurrentFirstname" ).sendKeys( "Homer1" );
		mDriver.findElementById( "addressbookCrudActionsSave" ).click();

		Thread.sleep( 1000 );
		assertEquals( "Mr Homer1 Simpson", mDriver.findElementsByCssSelector( ".data-table tbody tr td a" ).get( 0 ).getText() );

		// View another

		mDriver.findElementById( "addressbookSearchFirstname" ).clear();
		mDriver.findElementById( "addressbookSearchActionsSearch" ).click();
		Thread.sleep( 1000 );

		mDriver.findElementByLinkText( "Mrs Marjorie Simpson" ).click();
		mWait.until( visibilityOfElementLocated( By.id( "addressbookCurrentTitle" ) ) );
		assertEquals( "Marjorie", mDriver.findElementById( "addressbookCurrentFirstname" ).getText() );
		assertEquals( 1, mDriver.findElementsById( "addressbookCrudActionsEdit" ).size() );
		assertEquals( 0, mDriver.findElementsById( "addressbookCrudActionsSave" ).size() );
		assertEquals( 0, mDriver.findElementsById( "addressbookCrudActionsDelete" ).size() );
		assertEquals( 1, mDriver.findElementsById( "addressbookCrudActionsCancel" ).size() );
	}

	@Test
	public void testCreating()
		throws Exception {

		mDriver.get( BASE_URL );

		Thread.sleep( 1000 );
		assertEquals( 6, mDriver.findElementsByCssSelector( ".data-table tbody tr" ).size() );

		// Create

		mDriver.findElementById( "addressbookSearchActionsCreatePersonal" ).click();
		mWait.until( visibilityOfElementLocated( By.id( "addressbookCurrentTitle" ) ) );
		assertEquals( "Title:", mDriver.findElementById( "table-addressbookCurrentTitle-label" ).getText() );
		assertEquals( "select", mDriver.findElementById( "addressbookCurrentTitle" ).getTagName() );
		assertEquals( "input", mDriver.findElementById( "addressbookCurrentFirstname" ).getTagName() );
		assertEquals( "input", mDriver.findElementById( "addressbookCurrentSurname" ).getTagName() );
		assertEquals( "select", mDriver.findElementById( "addressbookCurrentGender" ).getTagName() );
		assertEquals( "input", mDriver.findElementById( "addressbookCurrentDateOfBirth" ).getTagName() );
		mDriver.findElementById( "addressbookCrudActionsCancel" ).click();

		mDriver.findElementById( "addressbookSearchActionsCreateBusiness" ).click();
		mWait.until( visibilityOfElementLocated( By.id( "addressbookCurrentTitle" ) ) );
		assertEquals( "New Contact", mDriver.findElementById( "dialog-heading" ).getText() );
		assertEquals( BASE_URL + "/media/business.gif", mDriver.findElementById( "dialog-image" ).getAttribute( "src" ) );
		assertEquals( "input", mDriver.findElementById( "addressbookCurrentCompany" ).getTagName() );
		assertEquals( "text", mDriver.findElementById( "addressbookCurrentCompany" ).getAttribute( "type" ) );
		mDriver.findElementById( "addressbookCurrentTitle" ).sendKeys( "Miss" );
		mDriver.findElementById( "addressbookCurrentFirstname" ).clear();
		mDriver.findElementById( "addressbookCurrentFirstname" ).sendKeys( "Business" );
		mDriver.findElementById( "addressbookCurrentSurname" ).clear();
		mDriver.findElementById( "addressbookCurrentSurname" ).sendKeys( "Contact" );
		mDriver.findElementById( "addressbookCrudActionsSave" ).click();

		// Delete

		Thread.sleep( 1000 );
		assertEquals( 7, mDriver.findElementsByCssSelector( ".data-table tbody tr" ).size() );
		mDriver.findElementByLinkText( "Miss Business Contact" ).click();
		mWait.until( visibilityOfElementLocated( By.id( "addressbookCurrentTitle" ) ) );
		assertEquals( "Miss", mDriver.findElementById( "addressbookCurrentTitle" ).getAttribute( "value" ) );
		assertEquals( "Business", mDriver.findElementById( "addressbookCurrentFirstname" ).getAttribute( "value" ) );
		assertEquals( "Contact", mDriver.findElementById( "addressbookCurrentSurname" ).getAttribute( "value" ) );
		mDriver.findElementById( "addressbookCrudActionsEdit" ).click();
		mWait.until( visibilityOfElementLocated( By.id( "addressbookCrudActionsSave" ) ) );
		mDriver.findElementById( "addressbookCrudActionsDelete" ).click();

		Thread.sleep( 1000 );
		assertEquals( 6, mDriver.findElementsByCssSelector( ".data-table tbody tr" ).size() );
	}

	@Before
	public void before() {

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference( "browser.helperApps.alwaysAsk.force", false );
		profile.setPreference( "browser.download.manager.showWhenStarting", false );
		profile.setPreference( "browser.helperApps.neverAsk.saveToDisk", "application/pdf,image/png" );
		profile.setPreference( "general.useragent.override", "Firefox Integration-Test" );

		mDriver = new FirefoxDriver( profile );
		mWait = new WebDriverWait( mDriver, 60 );
	}

	@After
	public void after() {

		mDriver.quit();
	}
}