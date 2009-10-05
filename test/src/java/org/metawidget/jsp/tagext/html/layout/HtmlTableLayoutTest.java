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

package org.metawidget.jsp.tagext.html.layout;

import junit.framework.TestCase;

/**
 * @author Richard Kennard
 */

public class HtmlTableLayoutTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testConfig()
	{
		HtmlTableLayoutConfig config1 = new HtmlTableLayoutConfig();
		HtmlTableLayoutConfig config2 = new HtmlTableLayoutConfig();

		assertTrue( !config1.equals( "foo" ) );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// numberOfColumns

		config1.setNumberOfColumns( 2 );
		assertTrue( 2 == config1.getNumberOfColumns() );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setNumberOfColumns( 2 );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// tableStyle

		config1.setTableStyle( "table-style" );
		assertTrue( "table-style".equals( config1.getTableStyle() ) );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setTableStyle( "table-style" );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// tableStyleClass

		config1.setTableStyleClass( "table-style-class" );
		assertTrue( "table-style-class".equals( config1.getTableStyleClass() ) );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setTableStyleClass( "table-style-class" );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// columnStyleClasses

		config1.setColumnStyleClasses( "column-style-class1", "column-style-class2" );
		assertTrue( "column-style-class1".equals( config1.getColumnStyleClasses()[0] ) );
		assertTrue( "column-style-class2".equals( config1.getColumnStyleClasses()[1] ) );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setColumnStyleClasses( "column-style-class1", "column-style-class2" );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// sectionStyleClass

		config1.setSectionStyleClass( "section-style-class" );
		assertTrue( "section-style-class".equals( config1.getSectionStyleClass() ) );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setSectionStyleClass( "section-style-class" );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// footerStyle

		config1.setFooterStyle( "footer-style" );
		assertTrue( "footer-style".equals( config1.getFooterStyle() ) );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setFooterStyle( "footer-style" );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// footerStyleClass

		config1.setFooterStyleClass( "footer-style-class" );
		assertTrue( "footer-style-class".equals( config1.getFooterStyleClass() ) );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setFooterStyleClass( "footer-style-class" );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );
	}
}
