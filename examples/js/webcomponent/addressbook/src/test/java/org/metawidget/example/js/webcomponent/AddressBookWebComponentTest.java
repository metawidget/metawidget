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

			// Edit

			wait.until( presenceOfElementLocated( By.id( "currentTitle" ) ) );
			assertEquals( "Title:", driver.findElementById( "table-currentTitle-label" ).getText() );
			assertEquals( "output", driver.findElementById( "currentTitle" ).getTagName() );
			assertEquals( "Mr", driver.findElementById( "currentTitle" ).getText() );

		} finally {
			driver.quit();
		}
	}
}