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

package org.metawidget.util;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import junit.framework.TestCase;

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

		File firefoxBinaryFile = new File( "../../../../repository/firefox-win32/firefox.exe" );
		FirefoxBinary firefoxBinary = new FirefoxBinary( firefoxBinaryFile );

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference( "browser.helperApps.alwaysAsk.force", false );
		profile.setPreference( "browser.download.manager.showWhenStarting", false );

		WebDriver driver = new FirefoxDriver( firefoxBinary, profile );

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
			try {
				Thread.sleep( 1000 );
			} catch ( InterruptedException e ) {
				throw new RuntimeException( e );
			}
		}
	}

	protected abstract Boolean applyExpectedCondition( WebDriver driver );

	protected abstract void displayResult( WebDriver driver );
}