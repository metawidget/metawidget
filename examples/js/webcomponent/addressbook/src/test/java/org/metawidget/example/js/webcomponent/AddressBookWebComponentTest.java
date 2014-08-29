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
	// Public methods
	//

	@Test
	public void testAddressBook()
		throws Exception {

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference( "browser.helperApps.alwaysAsk.force", false );
		profile.setPreference( "browser.download.manager.showWhenStarting", false );
		profile.setPreference( "browser.helperApps.neverAsk.saveToDisk", "application/pdf,image/png" );
		profile.setPreference( "general.useragent.override", "Firefox Integration-Test" );

		FirefoxDriver driver = new FirefoxDriver( profile );
		WebDriverWait wait = new WebDriverWait( driver, 30 );

		try {
			driver.get( "http://localhost:8180/addressbook-webcomponent" );

			// Search

			wait.until( presenceOfElementLocated( By.id( "addressbookSearchFirstname" ) ) );
			assertEquals( "Firstname:", driver.findElementById( "table-addressbookSearchFirstname-label" ).getText() );
			assertEquals( "input", driver.findElementById( "addressbookSearchFirstname" ).getTagName() );
			assertEquals( "text", driver.findElementById( "addressbookSearchFirstname" ).getAttribute( "type" ) );
			assertEquals( "Surname:", driver.findElementById( "table-addressbookSearchSurname-label" ).getText() );
			assertEquals( "input", driver.findElementById( "addressbookSearchSurname" ).getTagName() );
			assertEquals( "text", driver.findElementById( "addressbookSearchSurname" ).getAttribute( "type" ) );
			assertEquals( "Type:", driver.findElementById( "table-addressbookSearchType-label" ).getText() );
			assertEquals( "select", driver.findElementById( "addressbookSearchType" ).getTagName() );
			assertEquals( "", driver.findElementsByCssSelector( "#addressbookSearchType option" ).get( 0 ).getText() );
			assertEquals( "Personal", driver.findElementsByCssSelector( "#addressbookSearchType option" ).get( 1 ).getText() );
			assertEquals( "Business", driver.findElementsByCssSelector( "#addressbookSearchType option" ).get( 2 ).getText() );
			assertEquals( 3, driver.findElementsByCssSelector( "#addressbookSearchType option" ).size() );
			assertEquals( 3, driver.findElementsByCssSelector( "#table-addressbookSearch tbody tr" ).size() );
			assertEquals( 1, driver.findElementsByCssSelector( "#table-addressbookSearch tfoot tr" ).size() );

			assertEquals( 6, driver.findElementsByCssSelector( ".data-table tbody tr" ).size() );
			driver.findElementById( "addressbookSearchType" ).sendKeys( "p" );
			driver.findElementById( "addressbookSearchActionsSearch" ).click();
			Thread.sleep( 1000 );

			assertEquals( 4, driver.findElementsByCssSelector( ".data-table tbody tr" ).size() );

			driver.findElementById( "addressbookSearchFirstname" ).sendKeys( "Homer" );
			driver.findElementById( "addressbookSearchActionsSearch" ).click();
			Thread.sleep( 1000 );

			assertEquals( 1, driver.findElementsByCssSelector( ".data-table tbody tr" ).size() );
			driver.findElementByLinkText( "Mr Homer Simpson" ).click();

			// View

			wait.until( presenceOfElementLocated( By.id( "currentTitle" ) ) );
			assertEquals( "Title:", driver.findElementById( "table-currentTitle-label" ).getText() );
			assertEquals( "output", driver.findElementById( "currentTitle" ).getTagName() );
			assertEquals( "Mr", driver.findElementById( "currentTitle" ).getText() );
			assertEquals( "Firstname:", driver.findElementById( "table-currentFirstname-label" ).getText() );
			assertEquals( "output", driver.findElementById( "currentFirstname" ).getTagName() );
			assertEquals( "Homer", driver.findElementById( "currentFirstname" ).getText() );
			assertEquals( "Surname:", driver.findElementById( "table-currentSurname-label" ).getText() );
			assertEquals( "output", driver.findElementById( "currentSurname" ).getTagName() );
			assertEquals( "Simpson", driver.findElementById( "currentSurname" ).getText() );
			assertEquals( "Gender:", driver.findElementById( "table-currentGender-label" ).getText() );
			assertEquals( "output", driver.findElementById( "currentGender" ).getTagName() );
			assertEquals( "Male", driver.findElementById( "currentGender" ).getText() );
			assertEquals( "Contact Details", driver.findElementsByTagName( "h1" ).get( 0 ).getText() );
			assertEquals( "Address:", driver.findElementById( "table-currentAddress-label" ).getText() );
			assertEquals( "Street:", driver.findElementById( "table-currentAddressStreet-label" ).getText() );
			assertEquals( "output", driver.findElementById( "currentAddressStreet" ).getTagName() );
			assertEquals( "742 Evergreen Terrace", driver.findElementById( "currentAddressStreet" ).getText() );
			assertEquals( "City:", driver.findElementById( "table-currentAddressCity-label" ).getText() );
			assertEquals( "output", driver.findElementById( "currentAddressCity" ).getTagName() );
			assertEquals( "Springfield", driver.findElementById( "currentAddressCity" ).getText() );
			assertEquals( "State:", driver.findElementById( "table-currentAddressState-label" ).getText() );
			assertEquals( "output", driver.findElementById( "currentAddressState" ).getTagName() );
			assertEquals( "Anytown", driver.findElementById( "currentAddressState" ).getText() );
			assertEquals( "Postcode:", driver.findElementById( "table-currentAddressPostcode-label" ).getText() );
			assertEquals( "output", driver.findElementById( "currentAddressPostcode" ).getTagName() );
			assertEquals( "90701", driver.findElementById( "currentAddressPostcode" ).getText() );
			assertEquals( "Communications:", driver.findElementById( "table-currentCommunications-label" ).getText() );
			assertEquals( "Other", driver.findElementsByTagName( "h1" ).get( 1 ).getText() );
			assertEquals( "Notes:", driver.findElementById( "table-currentNotes-label" ).getText() );
			assertEquals( "output", driver.findElementById( "currentNotes" ).getTagName() );

			// Edit

			driver.findElementById( "addressbookCrudActionsEdit" ).click();
			assertEquals( "Title:", driver.findElementById( "table-currentTitle-label" ).getText() );
			assertEquals( "select", driver.findElementById( "currentTitle" ).getTagName() );
			Select titleSelect = new Select( driver.findElementById( "currentTitle" ) );
			assertEquals( "Mr", titleSelect.getFirstSelectedOption().getText() );
			assertEquals( "Mr", titleSelect.getOptions().get( 0 ).getText() );
			assertEquals( "Cpt", titleSelect.getOptions().get( 4 ).getText() );
			assertEquals( 5, titleSelect.getOptions().size() );
			assertEquals( "Firstname:", driver.findElementById( "table-currentFirstname-label" ).getText() );
			assertEquals( "input", driver.findElementById( "currentFirstname" ).getTagName() );
			assertEquals( "Homer", driver.findElementById( "currentFirstname" ).getAttribute( "value" ) );
			assertEquals( "Surname:", driver.findElementById( "table-currentSurname-label" ).getText() );
			assertEquals( "input", driver.findElementById( "currentSurname" ).getTagName() );
			assertEquals( "Simpson", driver.findElementById( "currentSurname" ).getAttribute( "value" ) );
			assertEquals( "Gender:", driver.findElementById( "table-currentGender-label" ).getText() );
			Select genderSelect = new Select( driver.findElementById( "currentGender" ) );
			assertEquals( "Male", genderSelect.getFirstSelectedOption().getText() );
			assertEquals( "", genderSelect.getOptions().get( 0 ).getText() );
			assertEquals( "Male", genderSelect.getOptions().get( 1 ).getText() );
			assertEquals( "Female", genderSelect.getOptions().get( 2 ).getText() );
			assertEquals( 3, genderSelect.getOptions().size() );
			assertEquals( "Contact Details", driver.findElementsByTagName( "h1" ).get( 0 ).getText() );
			assertEquals( "Address:", driver.findElementById( "table-currentAddress-label" ).getText() );
			assertEquals( "Street:", driver.findElementById( "table-currentAddressStreet-label" ).getText() );
			assertEquals( "input", driver.findElementById( "currentAddressStreet" ).getTagName() );
			assertEquals( "742 Evergreen Terrace", driver.findElementById( "currentAddressStreet" ).getAttribute( "value" ) );
			assertEquals( "City:", driver.findElementById( "table-currentAddressCity-label" ).getText() );
			assertEquals( "input", driver.findElementById( "currentAddressCity" ).getTagName() );
			assertEquals( "Springfield", driver.findElementById( "currentAddressCity" ).getAttribute( "value" ) );
			assertEquals( "State:", driver.findElementById( "table-currentAddressState-label" ).getText() );
			Select stateSelect = new Select( driver.findElementById( "currentAddressState" ) );
			assertEquals( "Anytown", stateSelect.getFirstSelectedOption().getText() );
			assertEquals( "", stateSelect.getOptions().get( 0 ).getText() );
			assertEquals( 5, stateSelect.getOptions().size() );
			assertEquals( "Postcode:", driver.findElementById( "table-currentAddressPostcode-label" ).getText() );
			assertEquals( "input", driver.findElementById( "currentAddressPostcode" ).getTagName() );
			assertEquals( "90701", driver.findElementById( "currentAddressPostcode" ).getAttribute( "value" ) );
			assertEquals( "Communications:", driver.findElementById( "table-currentCommunications-label" ).getText() );
			assertEquals( "Other", driver.findElementsByTagName( "h1" ).get( 1 ).getText() );
			assertEquals( "Notes:", driver.findElementById( "table-currentNotes-label" ).getText() );
			assertEquals( "textarea", driver.findElementById( "currentNotes" ).getTagName() );

			// Save

			driver.findElementById( "currentFirstname" ).clear();
			driver.findElementById( "currentFirstname" ).sendKeys( "Homer1" );
			driver.findElementById( "addressbookCrudActionsSave" ).click();
			assertEquals( "Mr Homer1 Simpson", driver.findElementsByCssSelector( ".data-table tbody tr td a" ).get( 0 ).getText() );

		} finally {
			driver.quit();
		}
	}
}