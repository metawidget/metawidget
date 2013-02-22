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

package org.metawidget.util.simple;

import junit.framework.TestCase;

/**
 * @author Richard Kennard
 */

public class StringUtilsTest
	extends TestCase {

	//
	// Public methods
	//

	public void testCapitalize()
		throws Exception {

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

		// camelCase

		assertEquals( "dropdownFoo", StringUtils.camelCase( "Dropdown #Foo" ) );
		assertEquals( "dropdownFoo", StringUtils.camelCase( "Dropdown#foo", '#' ) );
		assertEquals( "dropdownfoo", StringUtils.camelCase( "Dropdown#foo" ) );
		assertEquals( "dropdown2", StringUtils.camelCase( "Dropdown #2" ) );
		assertEquals( "", StringUtils.camelCase( "" ) );
		assertEquals( "", StringUtils.camelCase( "_" ) );
		assertEquals( "a", StringUtils.camelCase( "A" ) );
		assertEquals( "AZ", StringUtils.camelCase( "AZ" ) );
		assertEquals( "aBC", StringUtils.camelCase( "A b c" ) );
		assertEquals( "aBC", StringUtils.camelCase( "A B C" ) );
		assertEquals( "AZBC", StringUtils.camelCase( "AZ B C" ) );
		assertEquals( "SPOUSE", StringUtils.camelCase( "SPOUSE" ) );
		assertEquals( "PERMANENTSTAFF", StringUtils.camelCase( "PERMANENT STAFF" ) );
		assertEquals( "itembar", StringUtils.camelCase( "_item.bar" ) );
		assertEquals( "itemBar", StringUtils.camelCase( "_item.bar", '.' ) );
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
