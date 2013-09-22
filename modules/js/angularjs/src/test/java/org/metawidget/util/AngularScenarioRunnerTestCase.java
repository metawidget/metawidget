// Metawidget (licensed under LGPL)
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

package org.metawidget.util;

import junit.framework.TestCase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class AngularScenarioRunnerTestCase
	extends TestCase {

	//
	// Private statics
	//

	private static final int	TEST_TIMEOUT_IN_SECONDS	= 60 * 5;

	//
	// Protected methods
	//

	/**
	 * Run the Angular Scenario Runner by hitting the given URL and waiting until all tests
	 * complete.
	 */

	protected void runScenarioRunner( String url ) {

		WebDriver driver = new FirefoxDriver();

		try {

			// Hit the url

			driver.get( url );

			// Wait for all scenario runner tests to run, and fail on error

			new WebDriverWait( driver, TEST_TIMEOUT_IN_SECONDS ).until( new ExpectedCondition<Boolean>() {

				public Boolean apply( WebDriver theDriver ) {

					for ( WebElement test : theDriver.findElements( By.className( "test-it" ) ) ) {
						String classAttribute = test.getAttribute( "class" );

						if ( classAttribute.contains( "status-pending" ) ) {
							return false;
						}
					}

					return true;
				}
			} );

			// Display the result

			String failed = null;

			for ( WebElement test : driver.findElements( By.className( "test-it" ) ) ) {

				StringBuilder builder = new StringBuilder( "\t" );
				builder.append( test.getText() );
				String classAttribute = test.getAttribute( "class" );

				if ( classAttribute.contains( "status-failure" ) || classAttribute.contains( "status-error" ) ) {
					failed = test.getText();
					builder.append( " - failed" );
				} else {
					builder.append( " - passed" );
				}

				System.out.println( builder );
			}

			System.out.println( "Tests run: " + driver.findElement( By.className( "status-success" ) ).getText() + ", " + driver.findElement( By.className( "status-failure" ) ).getText() + ", " + driver.findElement( By.className( "status-error" ) ).getText() );

			// Fail if necessary

			if ( failed != null ) {
				throw new RuntimeException( failed );
			}

		} finally {
			driver.quit();
		}
	}
}