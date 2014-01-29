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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class AngularScenarioRunnerTestCase
	extends ScenarioRunnerTestCase {

	//
	// Protected methods
	//

	@Override
	protected Boolean applyExpectedCondition( WebDriver driver ) {

		List<WebElement> testElements = driver.findElements( By.className( "test-it" ) );

		// Test elements may not appear until they start running

		if ( testElements.size() < getExpectedNumberOfTests() ) {
			return false;
		}

		// Once all test elements on the page, check them all

		for ( WebElement test : testElements ) {
			String classAttribute = test.getAttribute( "class" );

			if ( classAttribute.contains( "status-pending" ) ) {
				return false;
			}
		}

		return true;
	}

	protected abstract int getExpectedNumberOfTests();

	@Override
	protected void displayResult( WebDriver driver ) {

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
	}
}