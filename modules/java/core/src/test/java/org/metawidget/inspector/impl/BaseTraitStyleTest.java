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

package org.metawidget.inspector.impl;

import java.util.Date;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.inspector.impl.propertystyle.BasePropertyStyle;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;
import org.metawidget.util.MetawidgetTestUtils;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class BaseTraitStyleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( BaseTraitStyleConfig.class, new BaseTraitStyleConfig() {
			// Subclass
		} );
	}

	public void testCacheProperties() {

		// With caching

		JavaBeanPropertyStyleConfig config = new JavaBeanPropertyStyleConfig();
		BaseTraitStyle<Property> traitStyle = new JavaBeanPropertyStyle( config );

		assertFalse( traitStyle.mCache instanceof WeakHashMap );
		assertTrue( traitStyle.mCache.isEmpty() );
		assertTrue( traitStyle.getTraits( Date.class.getName() ) != null );
		assertEquals( 1, traitStyle.mCache.size() );
		assertTrue( traitStyle.mCache.get( Date.class.getName() ) != null );

		// Without caching

		config.setCacheLookups( false );
		traitStyle = new JavaBeanPropertyStyle( config );
		assertEquals( traitStyle.mCache, null );
		assertTrue( traitStyle.getTraits( Date.class.getName() ) != null );
		assertEquals( traitStyle.mCache, null );
	}

	public void testExcludedBaseType() {

		// Default excludeBaseType

		BasePropertyStyle traitStyle = new JavaBeanPropertyStyle();
		assertEquals( true, traitStyle.isExcludedBaseType( Date.class ) );
		assertEquals( true, traitStyle.isExcludedBaseType( JTextField.class ) );
		assertEquals( false, traitStyle.isExcludedBaseType( Element.class ) );

		// Null excludeBaseType

		JavaBeanPropertyStyleConfig config = new JavaBeanPropertyStyleConfig();
		config.setExcludeBaseType( null );
		traitStyle = new JavaBeanPropertyStyle( config );

		assertEquals( false, traitStyle.isExcludedBaseType( Date.class ) );
		assertEquals( false, traitStyle.isExcludedBaseType( JTextField.class ) );
		assertEquals( false, traitStyle.isExcludedBaseType( Element.class ) );

		// Not-null it again

		config.setExcludeBaseType( Pattern.compile( "^(org)\\..*$" ));
		traitStyle = new JavaBeanPropertyStyle( config );

		assertEquals( false, traitStyle.isExcludedBaseType( Date.class ) );
		assertEquals( false, traitStyle.isExcludedBaseType( JTextField.class ) );
		assertEquals( true, traitStyle.isExcludedBaseType( Element.class ) );
	}

	public void testExcludeReturnType() {

		// Default excludeName

		BasePropertyStyle traitStyle = new JavaBeanPropertyStyle();
		assertEquals( false, traitStyle.isExcludedReturnType( Date.class ) );
		assertEquals( false, traitStyle.isExcludedReturnType( JTextField.class ) );
		assertEquals( false, traitStyle.isExcludedReturnType( Element.class ) );

		// Specific excludeName

		JavaBeanPropertyStyleConfig config = new JavaBeanPropertyStyleConfig();
		config.setExcludeReturnType( JComponent.class, Element.class );
		traitStyle = new JavaBeanPropertyStyle( config );

		assertEquals( false, traitStyle.isExcludedReturnType( Date.class ) );
		assertEquals( true, traitStyle.isExcludedReturnType( JTextField.class ) );
		assertEquals( true, traitStyle.isExcludedReturnType( Element.class ) );
	}

	public void testExcludedName() {

		// Default excludeName

		BasePropertyStyle traitStyle = new JavaBeanPropertyStyle();
		assertEquals( false, traitStyle.isExcludedName( "Foo" ));
		assertEquals( true, traitStyle.isExcludedName( "propertyChangeListeners" ));
		assertEquals( true, traitStyle.isExcludedName( "vetoableChangeListeners" ));

		// Null excludeName

		JavaBeanPropertyStyleConfig config = new JavaBeanPropertyStyleConfig();
		config.setExcludeName( (String[]) null );
		traitStyle = new JavaBeanPropertyStyle( config );

		assertEquals( false, traitStyle.isExcludedName( "Foo" ));
		assertEquals( false, traitStyle.isExcludedName( "propertyChangeListeners" ));
		assertEquals( false, traitStyle.isExcludedName( "vetoableChangeListeners" ));

		// Not-null it again

		config.setExcludeName( "propertyChangeListeners" );
		traitStyle = new JavaBeanPropertyStyle( config );

		assertEquals( false, traitStyle.isExcludedName( "Foo" ));
		assertEquals( true, traitStyle.isExcludedName( "propertyChangeListeners" ));
		assertEquals( false, traitStyle.isExcludedName( "vetoableChangeListeners" ));
	}
}
