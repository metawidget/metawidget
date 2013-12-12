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