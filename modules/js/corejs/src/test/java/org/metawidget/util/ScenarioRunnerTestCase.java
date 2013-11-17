// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.util;

import junit.framework.TestCase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Utility class to load Selenium and wait for a test case to complete. Designed to
 * integrate into JUnit and Maven.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class ScenarioRunnerTestCase
	extends TestCase {

	//
	// Private statics
	//

	/* package private */static final int	TEST_TIMEOUT_IN_SECONDS	= 60 * 5;

	//
	// Protected methods
	//

	/**
	 * Run the Scenario Runner by hitting the given URL and waiting until all tests complete.
	 */

	protected final void runScenarioRunner( String url ) {

		WebDriver driver = new FirefoxDriver();

		try {

			// Hit the url

			driver.get( url );

			// Wait for all scenario runner tests to run, and fail on error

			new WebDriverWait( driver, TEST_TIMEOUT_IN_SECONDS ).until( new ExpectedCondition<Boolean>() {

				public Boolean apply( WebDriver theDriver ) {

					return applyExpectedCondition( theDriver );
				}
			} );

			displayResult( driver );

		} finally {
			driver.quit();
		}
	}

	protected abstract Boolean applyExpectedCondition( WebDriver driver );

	protected abstract void displayResult( WebDriver driver );
}