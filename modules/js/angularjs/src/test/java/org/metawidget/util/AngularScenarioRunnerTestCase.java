package org.metawidget.util;

import junit.framework.TestCase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AngularScenarioRunnerTestCase
	extends TestCase {

	//
	// Private statics
	//

	private final static int	TEST_TIMEOUT_IN_SECONDS	= 60 * 5;

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

						if ( classAttribute.contains( "status-failure" ) || classAttribute.contains( "status-error" ) ) {
							throw new RuntimeException( test.getText() );
						}

						if ( classAttribute.contains( "status-pending" ) ) {
							return false;
						}
					}

					return true;
				}
			} );

			// Display the result

			System.out.println( "Tests run: " + driver.findElement( By.className( "status-success" ) ).getText() );
		} finally {
			driver.quit();
		}
	}
}