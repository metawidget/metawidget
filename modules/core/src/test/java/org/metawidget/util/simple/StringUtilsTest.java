// Metawidget
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

	public void testIsCapitalized()
		throws Exception {

		assertTrue( false == StringUtils.isCapitalized( "" ) );
		assertTrue( false == StringUtils.isCapitalized( "ab" ) );
		assertTrue( StringUtils.isCapitalized( "Ab" ) );
		assertTrue( StringUtils.isCapitalized( "ID" ) );
	}

	public void testCapitalize()
		throws Exception {

		assertEquals( "FooBah", StringUtils.capitalize( "fooBah" ) );
		assertEquals( "X", StringUtils.capitalize( "x" ) );
		assertEquals( "URL", StringUtils.capitalize( "URL" ) );
		assertEquals( "ID", StringUtils.capitalize( "iD" ) );
	}

	public void testDecapitalize()
		throws Exception {

		assertEquals( "fooBah", StringUtils.decapitalize( "FooBah" ) );
		assertEquals( "x", StringUtils.decapitalize( "X" ) );
		assertEquals( "URL", StringUtils.decapitalize( "URL" ) );
		assertEquals( "ID", StringUtils.decapitalize( "ID" ) );
	}

	public void testCamelCase()
		throws Exception {

		// uncamelCase

		assertTrue( null == StringUtils.uncamelCase( null ) );
		assertEquals( "Camel Cased", StringUtils.uncamelCase( "camelCased" ) );
		assertEquals( "Camel CASED", StringUtils.uncamelCase( "camelCASED" ) );
		assertEquals( "Camel-Cased", StringUtils.uncamelCase( "camelCased", '-' ) );

		// camelCase

		assertEquals( "dropdownFoo", StringUtils.camelCase( "Dropdown #Foo" ) );
		assertEquals( "dropdownFoo", StringUtils.camelCase( "Dropdown#foo", '#' ) );
		assertEquals( "dropdownfoo", StringUtils.camelCase( "Dropdown#foo" ) );
		assertEquals( "dropdown2", StringUtils.camelCase( "Dropdown #2" ) );
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
