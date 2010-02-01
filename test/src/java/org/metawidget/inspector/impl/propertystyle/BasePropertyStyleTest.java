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

package org.metawidget.inspector.impl.propertystyle;

import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class BasePropertyStyleTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testConfig()
	{
		BasePropertyStyleConfig config1 = new BasePropertyStyleConfig();
		BasePropertyStyleConfig config2 = new BasePropertyStyleConfig();

		assertTrue( !config1.equals( "foo" ) );
		assertTrue( config1.equals( config1 ) );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// default excludeBaseType

		assertTrue( "^(java|javax)\\..*$".equals( config1.getExcludeBaseType().pattern() ));

		// excludeBaseType

		config1.setExcludeBaseType( Pattern.compile( "bar" ) );
		assertTrue( "bar".equals( config1.getExcludeBaseType().pattern() ));
		assertTrue( !config1.equals( config2 ) );

		config2.setExcludeBaseType( Pattern.compile( "bar" ) );
		assertTrue( !config1.equals( config2 ) );

		// mNullExcludeBaseType

		config1 = new BasePropertyStyleConfig();
		config2 = new BasePropertyStyleConfig();
		config1.setExcludeBaseType( null );
		assertTrue( null == config1.getExcludeBaseType() );
		assertTrue( !config1.equals( config2 ) );

		config2.setExcludeBaseType( null );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		config1.setExcludeBaseType( Pattern.compile( "baz" ) );
		assertTrue( "baz".equals( config1.getExcludeBaseType().pattern() ));
	}

	public void testExcludedBaseType()
	{
		// Default excludeBaseType

		BasePropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		assertTrue( true == propertyStyle.isExcludedBaseType( Date.class ));
		assertTrue( true == propertyStyle.isExcludedBaseType( JTextField.class ));
		assertTrue( false == propertyStyle.isExcludedBaseType( Element.class ));

		// Null excludeBaseType

		BasePropertyStyleConfig config = new BasePropertyStyleConfig();
		config.setExcludeBaseType( null );
		propertyStyle = new JavaBeanPropertyStyle( config );

		assertTrue( false == propertyStyle.isExcludedBaseType( Date.class ));
		assertTrue( false == propertyStyle.isExcludedBaseType( JTextField.class ));
		assertTrue( false == propertyStyle.isExcludedBaseType( Element.class ));
	}
}
