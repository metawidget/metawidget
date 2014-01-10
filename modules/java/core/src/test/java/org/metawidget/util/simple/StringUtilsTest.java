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

package org.metawidget.util.simple;

import junit.framework.TestCase;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StringUtilsTest
	extends TestCase {

	//
	// Public methods
	//

	public void testCapitalize()
		throws Exception {

		assertEquals( "", StringUtils.capitalize( "" ) );
		assertEquals( "FooBah", StringUtils.capitalize( "fooBah" ) );
		assertEquals( "X", StringUtils.capitalize( "x" ) );
		assertEquals( "URL", StringUtils.capitalize( "URL" ) );
		assertEquals( "ID", StringUtils.capitalize( "ID" ) );
		assertEquals( "aFIELD", StringUtils.capitalize( "aFIELD" ) );
		assertEquals( "aI", StringUtils.capitalize( "aI" ) );
		assertEquals( "jAXBElementLongConverter", StringUtils.capitalize( "jAXBElementLongConverter" ) );
		assertEquals( "JAXBElementLongConverter", StringUtils.capitalize( "JAXBElementLongConverter" ) );
	}

	public void testDecapitalize()
		throws Exception {

		assertEquals( "", StringUtils.decapitalize( "" ) );
		assertEquals( "fooBah", StringUtils.decapitalize( "FooBah" ) );
		assertEquals( "x", StringUtils.decapitalize( "X" ) );
		assertEquals( "URL", StringUtils.decapitalize( "URL" ) );
		assertEquals( "ID", StringUtils.decapitalize( "ID" ) );

		// See: https://community.jboss.org/thread/203202?start=0&tstart=0

		assertEquals( "aFIELD", StringUtils.decapitalize( "aFIELD" ) );
		assertEquals( "aI", StringUtils.decapitalize( "aI" ) );
	}

	public void testCapitalizeDecapitalize()
		throws Exception {

		assertEquals( "fooBah", StringUtils.decapitalize( StringUtils.capitalize( "fooBah" ) ) );
		assertEquals( "x", StringUtils.decapitalize( StringUtils.capitalize( "x" ) ) );
		assertEquals( "URL", StringUtils.decapitalize( StringUtils.capitalize( "URL" ) ) );
		assertEquals( "ID", StringUtils.decapitalize( StringUtils.capitalize( "ID" ) ) );

		// These are only the inverse of each other because of the 'second character' clause

		assertEquals( "aFIELD", StringUtils.decapitalize( StringUtils.capitalize( "aFIELD" ) ) );
		assertEquals( "aI", StringUtils.decapitalize( StringUtils.capitalize( "aI" ) ) );
	}

	public void testCamelCase()
		throws Exception {

		// uncamelCase

		assertEquals( null, StringUtils.uncamelCase( null ) );
		assertEquals( "Camel Cased", StringUtils.uncamelCase( "camelCased" ) );
		assertEquals( "Camel CASED", StringUtils.uncamelCase( "camelCASED" ) );
		assertEquals( "Camel-Cased", StringUtils.uncamelCase( "camelCased", '-' ) );
		assertEquals( "Age 18 To 30 Employees", StringUtils.uncamelCase( "age18To30Employees" ) );
		assertEquals( "Press Me", StringUtils.uncamelCase( "Press Me" ) );

		assertEquals( "Foo bar Baz Abc", StringUtils.uncamelCase( "Foo barBaz Abc" ) );
		assertEquals( "ID", StringUtils.uncamelCase( "ID" ) );
		assertEquals( "DOB", StringUtils.uncamelCase( "DOB" ) );
		assertEquals( "DO Birth", StringUtils.uncamelCase( "DOBirth" ) );
		assertEquals( "Foo DO Birth Bar", StringUtils.uncamelCase( "fooDOBirthBar" ) );
		assertEquals( "123", StringUtils.uncamelCase( "123" ) );
		assertEquals( "Foo 1", StringUtils.uncamelCase( "foo1" ) );
		assertEquals( "Foo 12", StringUtils.uncamelCase( "foo12" ) );
		assertEquals( "Foo 123", StringUtils.uncamelCase( "foo123" ) );
		assertEquals( "1foo", StringUtils.uncamelCase( "1foo" ) );
		assertEquals( "1 Foo", StringUtils.uncamelCase( "1Foo" ) );
		assertEquals( "12 Foo", StringUtils.uncamelCase( "12Foo" ) );
		assertEquals( "123 Foo", StringUtils.uncamelCase( "123Foo" ) );
		assertEquals( "123 Foo", StringUtils.uncamelCase( "123 Foo" ) );
		assertEquals( "_foo Bar", StringUtils.uncamelCase( "_fooBar" ) );

		// camelCase

		assertEquals( "dropdown#Foo", StringUtils.camelCase( "Dropdown #Foo" ) );
		assertEquals( "dropdownFoo", StringUtils.camelCase( "Dropdown#foo", '#' ) );
		assertEquals( "dropdown#foo", StringUtils.camelCase( "Dropdown#foo" ) );
		assertEquals( "dropdown#2", StringUtils.camelCase( "Dropdown #2" ) );
		assertEquals( "", StringUtils.camelCase( "" ) );
		assertEquals( "_", StringUtils.camelCase( "_" ) );
		assertEquals( "a", StringUtils.camelCase( "A" ) );
		assertEquals( "AZ", StringUtils.camelCase( "AZ" ) );
		assertEquals( "aBC", StringUtils.camelCase( "A b c" ) );
		assertEquals( "aBC", StringUtils.camelCase( "A B C" ) );
		assertEquals( "AZBC", StringUtils.camelCase( "AZ B C" ) );
		assertEquals( "SPOUSE", StringUtils.camelCase( "SPOUSE" ) );
		assertEquals( "PERMANENTSTAFF", StringUtils.camelCase( "PERMANENT STAFF" ) );
		assertEquals( "_item.bar", StringUtils.camelCase( "_item.bar" ) );
		assertEquals( "_itemBar", StringUtils.camelCase( "_item.bar", '.' ) );
		assertEquals( "DOBirth", StringUtils.camelCase( "DO Birth" ) );
		assertEquals( "fooDOBirthBar", StringUtils.camelCase( "Foo DO Birth Bar" ) );
		assertEquals( "1Foo", StringUtils.camelCase( "1 Foo" ) );
		assertEquals( "_fooBar", StringUtils.camelCase( "_foo bar" ) );
	}

	public void testSubstringBefore()
		throws Exception {

		assertEquals( "foo", StringUtils.substringBefore( "foo.bar.baz", "." ) );
		assertEquals( "baz", StringUtils.substringBefore( "baz", "." ) );
	}

	public void testSubstringAfter()
		throws Exception {

		assertEquals( "bar.baz", StringUtils.substringAfter( "foo.bar.baz", "." ) );
		assertEquals( "baz", StringUtils.substringAfter( "baz", "." ) );
	}

	public void testSubstringAfterLast()
		throws Exception {

		assertEquals( "baz", StringUtils.substringAfterLast( "foo.bar.baz", "." ) );
		assertEquals( "baz", StringUtils.substringAfterLast( "baz", "." ) );
	}

	public void testCaseInsensitiveComparator() {

		assertTrue( StringUtils.CASE_INSENSITIVE_COMPARATOR.compare( "Foo", "foo" ) < 1 );
		assertTrue( StringUtils.CASE_INSENSITIVE_COMPARATOR.compare( "foo", "Foo" ) > 1 );
		assertEquals( 0, StringUtils.CASE_INSENSITIVE_COMPARATOR.compare( "Foo", "Foo" ) );
		assertEquals( 0, StringUtils.CASE_INSENSITIVE_COMPARATOR.compare( "foo", "foo" ) );
	}
}
