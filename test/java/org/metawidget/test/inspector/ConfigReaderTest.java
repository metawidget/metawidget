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

package org.metawidget.test.inspector;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.metawidget.inspector.ConfigReader;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.faces.FacesInspector;
import org.metawidget.inspector.hibernate.validator.HibernateValidatorInspector;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.jpa.JpaInspector;
import org.metawidget.inspector.jsp.JspAnnotationInspector;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.spring.SpringAnnotationInspector;
import org.metawidget.inspector.struts.StrutsAnnotationInspector;
import org.metawidget.inspector.struts.StrutsInspector;
import org.metawidget.inspector.xml.XmlInspector;

/**
 * @author Richard Kennard
 */

public class ConfigReaderTest
	extends TestCase
{
	//
	//
	// Public methods
	//
	//

	@SuppressWarnings( "unchecked" )
	public void testReader()
		throws Exception
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspector-config xmlns=\"http://metawidget.org/inspector-config\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspector-config http://metawidget.org/inspector-config/inspector-config-1.0.xsd\">";
		xml += "<compositeInspector xmlns=\"java:org.metawidget.inspector.composite\" config=\"CompositeInspectorConfig\">";
		xml += "<inspectors>";
		xml += "<metawidgetAnnotationInspector xmlns=\"java:org.metawidget.inspector.annotation\"/>";
		xml += "<facesInspector xmlns=\"java:org.metawidget.inspector.faces\"/>";
		xml += "<hibernateValidatorInspector xmlns=\"java:org.metawidget.inspector.hibernate.validator\"/>";
		xml += "<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\" config=\"org.metawidget.inspector.impl.BasePropertyInspectorConfig\">";
		xml += "<propertyStyle>org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle</propertyStyle>";
		xml += "<proxyPattern>ByCGLIB\\$\\$|_\\$\\$_javassist_</proxyPattern>";
		xml += "</propertyTypeInspector>";
		xml += "<jpaInspector xmlns=\"java:org.metawidget.inspector.jpa\"/>";
		xml += "<jspAnnotationInspector xmlns=\"java:org.metawidget.inspector.jsp\"/>";
		xml += "<springAnnotationInspector xmlns=\"java:org.metawidget.inspector.spring\"/>";
		xml += "<strutsInspector xmlns=\"java:org.metawidget.inspector.struts\" config=\"StrutsInspectorConfig\">";
		xml += "<files>";
		xml += "<string>org/metawidget/test/inspector/struts/test-struts-config1.xml</string>";
		xml += "<string>org/metawidget/test/inspector/struts/test-struts-config2.xml</string>";
		xml += "</files>";
		xml += "</strutsInspector>";
		xml += "<strutsAnnotationInspector xmlns=\"java:org.metawidget.inspector.struts\"/>";
		xml += "<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "<file>org/metawidget/example/swing/addressbook/metawidget-metadata.xml</file>";
		xml += "</xmlInspector>";
		xml += "<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "<inputStream>org/metawidget/example/swing/addressbook/metawidget-metadata.xml</inputStream>";
		xml += "</xmlInspector>";
		xml += "</inspectors>";
		xml += "<validating>true</validating>";
		xml += "</compositeInspector></inspector-config>";

		ConfigReader reader = new ConfigReader();
		reader.setValidating( true );
		CompositeInspector inspector = (CompositeInspector) reader.read( new ByteArrayInputStream( xml.getBytes() ) );

		// Use reflection to test config result

		Field field = CompositeInspector.class.getDeclaredField( "mInspectors" );
		field.setAccessible( true );

		Inspector[] inspectors = (Inspector[]) field.get( inspector );

		assertTrue( inspectors.length == 11 );
		assertTrue( inspectors[0] instanceof MetawidgetAnnotationInspector );
		assertTrue( inspectors[1] instanceof FacesInspector );
		assertTrue( inspectors[2] instanceof HibernateValidatorInspector );
		assertTrue( inspectors[3] instanceof PropertyTypeInspector );
		assertTrue( inspectors[4] instanceof JpaInspector );
		assertTrue( inspectors[5] instanceof JspAnnotationInspector );
		assertTrue( inspectors[6] instanceof SpringAnnotationInspector );
		assertTrue( inspectors[7] instanceof StrutsInspector );
		assertTrue( inspectors[8] instanceof StrutsAnnotationInspector );
		assertTrue( inspectors[9] instanceof XmlInspector );
		assertTrue( inspectors[10] instanceof XmlInspector );
	}

	@SuppressWarnings( "unchecked" )
	public void testMandatoryConfigAttribute()
		throws Exception
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspector-config xmlns=\"http://metawidget.org/inspector-config\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspector-config http://metawidget.org/inspector-config/inspector-config-1.0.xsd\">";
		xml += "<compositeInspector xmlns=\"java:org.metawidget.inspector.composite\"/>";
		xml += "</inspector-config>";

		try
		{
			new ConfigReader().read( new ByteArrayInputStream( xml.getBytes() ) );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( e.getMessage().contains( "requires a 'config' attribute" ));
		}
	}

	@SuppressWarnings( "unchecked" )
	public void testForgottenConfigAttribute()
		throws Exception
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspector-config xmlns=\"http://metawidget.org/inspector-config\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspector-config http://metawidget.org/inspector-config/inspector-config-1.0.xsd\">";
		xml += "<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\">";
		xml += "<file>foo</file>";
		xml += "</xmlInspector></inspector-config>";

		try
		{
			ConfigReader reader = new ConfigReader();
			reader.setValidating( true );
			reader.setValidating( false );
			reader.read( new ByteArrayInputStream( xml.getBytes() ) );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( e.getMessage().contains( "forget a 'config' attribute?" ));
		}
	}

	public void testSupportedTypes()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspector-config xmlns=\"http://metawidget.org/inspector-config\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspector-config http://metawidget.org/inspector-config/inspector-config-1.0.xsd\">";
		xml += "<badInspector xmlns=\"java:org.metawidget.test.inspector\" config=\"BadInspectorConfig\">";
		xml += "<int>3</int>";
		xml += "<list>";
		xml += "<string>foo</string>";
		xml += "<string>bar</string>";
		xml += "</list>";
		xml += "</badInspector>";
		xml += "</inspector-config>";

		BadInspector inspector = (BadInspector) new ConfigReader().read( new ByteArrayInputStream( xml.getBytes() ) );
		assertTrue( 3 == inspector.getInt() );
		assertTrue( "foo".equals( inspector.getList().get( 0 ) ));
		assertTrue( "bar".equals( inspector.getList().get( 1 ) ));
		assertTrue( 2 == inspector.getList().size() );
	}

	public void testUnsupportedType()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspector-config xmlns=\"http://metawidget.org/inspector-config\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspector-config http://metawidget.org/inspector-config/inspector-config-1.0.xsd\">";
		xml += "<badInspector xmlns=\"java:org.metawidget.test.inspector\" config=\"BadInspectorConfig\">";
		xml += "<date>1/1/2001</date>";
		xml += "</badInspector>";
		xml += "</inspector-config>";

		try
		{
			new ConfigReader().read( new ByteArrayInputStream( xml.getBytes() ) );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( e.getMessage().endsWith( "Don't know how to convert '1/1/2001' to class java.util.Date" ));
		}
	}

	public void testSetterWithNoParameters()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspector-config xmlns=\"http://metawidget.org/inspector-config\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspector-config http://metawidget.org/inspector-config/inspector-config-1.0.xsd\">";
		xml += "<badInspector xmlns=\"java:org.metawidget.test.inspector\" config=\"BadInspectorConfig\">";
		xml += "<noParameters>foo</noParameters>";
		xml += "</badInspector>";
		xml += "</inspector-config>";

		try
		{
			new ConfigReader().read( new ByteArrayInputStream( xml.getBytes() ) );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( e.getMessage().endsWith( "public void org.metawidget.test.inspector.BadInspectorConfig.setNoParameters() is not a setter method - has no parameters" ));
		}
	}

	public void testSetterWithMultipleParameters()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspector-config xmlns=\"http://metawidget.org/inspector-config\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspector-config http://metawidget.org/inspector-config/inspector-config-1.0.xsd\">";
		xml += "<badInspector xmlns=\"java:org.metawidget.test.inspector\" config=\"BadInspectorConfig\">";
		xml += "<multipleParameters>foo</multipleParameters>";
		xml += "</badInspector>";
		xml += "</inspector-config>";

		try
		{
			new ConfigReader().read( new ByteArrayInputStream( xml.getBytes() ) );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( e.getMessage().endsWith( "public void org.metawidget.test.inspector.BadInspectorConfig.setMultipleParameters(java.lang.String,java.lang.String) is not a setter method - has multiple parameters" ));
		}
	}

	public void testNoInspector()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspector-config xmlns=\"http://metawidget.org/inspector-config\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspector-config http://metawidget.org/inspector-config/inspector-config-1.0.xsd\">";
		xml += "</inspector-config>";

		try
		{
			new ConfigReader().read( new ByteArrayInputStream( xml.getBytes() ) );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( "No Inspector declared".equals( e.getMessage() ));
		}
	}

	public void testMultipleInspectors()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspector-config xmlns=\"http://metawidget.org/inspector-config\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspector-config http://metawidget.org/inspector-config/inspector-config-1.0.xsd\">";
		xml += "<badInspector xmlns=\"java:org.metawidget.test.inspector\" config=\"BadInspectorConfig\"/>";
		xml += "<badInspector xmlns=\"java:org.metawidget.test.inspector\" config=\"BadInspectorConfig\"/>";
		xml += "</inspector-config>";

		try
		{
			new ConfigReader().read( new ByteArrayInputStream( xml.getBytes() ) );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( e.getMessage().endsWith( "Don't know how to combine a class org.metawidget.test.inspector.BadInspector with a class org.metawidget.test.inspector.BadInspector" ) );
		}
	}

	public void testMissingResource()
	{
		ConfigReader reader = new ConfigReader();

		try
		{
			reader.read( (String) null );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( "No resource specified".equals( e.getMessage() ));
		}

		try
		{
			reader.read( "" );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( "No resource specified".equals( e.getMessage() ));
		}

		try
		{
			reader.read( " " );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( "No resource specified".equals( e.getMessage() ));
		}

		try
		{
			reader.read( " foo" );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( "Unable to locate  foo".equals( e.getMessage() ));
		}
	}

	//
	//
	// Constructor
	//
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public ConfigReaderTest( String name )
	{
		super( name );
	}
}
