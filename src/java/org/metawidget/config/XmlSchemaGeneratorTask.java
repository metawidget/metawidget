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

package org.metawidget.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.metawidget.inspector.ResourceResolver;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.util.IOUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Ant task to generate the XML Schemas for the <code>Inspector</code>s, <code>WidgetBuilder</code>
 * s, <code>WidgetProcessor</code>s, <code>Layout</code>s and other JavaBeans used in
 * <code>metawidget.xml</code>.
 * <p>
 * Note: automated schema generation is inherently brittle. The goal of this Ant task is only to be
 * robust enough to generate schemas for the classes included in the standard Metawidget
 * distribution. Use of this task to generate schemas for your own classes may give unexpected
 * results.
 *
 * @author Richard Kennard
 */

public class XmlSchemaGeneratorTask
	extends Task
{
	//
	// Public statics
	//

	public static void main( String[] args )
	{
		XmlSchemaGeneratorTask task = new XmlSchemaGeneratorTask();
		task.setJar( "N:\\My Documents\\Kennard Consulting\\Perforce\\Generation 02\\Development\\metawidget\\build\\metawidget.jar" );
		task.setDestDir( "C:\\deleteme\\xsd" );
		task.execute();
	}

	//
	// Private statics
	//

	private final static String	CLASS_SUFFIX	= ".class";

	private final static String	XSD_SUFFIX		= "-1.0.xsd";

	private final static String	SCHEMA_END		= "\r\n</xs:schema>";

	//
	// Private members
	//

	private String				mJar;

	private String				mDestDir;

	//
	// Public methods
	//

	public void setJar( String jar )
	{
		mJar = jar;
	}

	public void setDestDir( String destDir )
	{
		mDestDir = destDir;
	}

	@Override
	public void execute()
		throws BuildException
	{
		// TODO: Unit test XmlSchemaGeneratorTask

		try
		{
			// Create/clear the dest dir

			File destDir = new File( mDestDir );
			destDir.mkdirs();

			for ( File file : destDir.listFiles() )
			{
				if ( !file.isDirectory() )
					file.delete();
			}

			// For each entry in the JAR file...

			JarFile jarFile = new JarFile( mJar );

			for ( Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements(); )
			{
				JarEntry jarEntry = e.nextElement();

				// ...that is a Java class...

				if ( jarEntry.isDirectory() )
					continue;

				String name = jarEntry.getName();

				if ( !name.endsWith( CLASS_SUFFIX ) )
					continue;

				// ...determine the package name...

				String qualifiedClassName = name.replace( '/', '.' ).substring( 0, name.length() - CLASS_SUFFIX.length() );

				int lastIndexOf = qualifiedClassName.lastIndexOf( '.' );

				if ( lastIndexOf == -1 )
					continue;

				String className = qualifiedClassName.substring( lastIndexOf + 1 );

				// ...ignore inner classes...

				if ( className.contains( "$" ) )
					continue;

				String packageName = qualifiedClassName.substring( 0, lastIndexOf );

				// ...find the XSD file...

				File xsdFile = new File( destDir, packageName + XSD_SUFFIX );

				// ...load it if it already exists...

				StringBuilder xsdBuilder = new StringBuilder();

				if ( xsdFile.exists() )
				{
					ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
					IOUtils.streamBetween( new FileInputStream( xsdFile ), streamOut );
					xsdBuilder.append( streamOut.toString() );
				}

				// ...or create it if it doesn't...

				else
				{
					xsdBuilder.append( "<?xml version=\"1.0\" ?>\r\n" );
					xsdBuilder.append( "<xs:schema targetNamespace=\"java:" );
					xsdBuilder.append( packageName );
					xsdBuilder.append( "\" xmlns=\"java:" );
					xsdBuilder.append( packageName );
					xsdBuilder.append( "\"" );
					xsdBuilder.append( " xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" elementFormDefault=\"qualified\" version=\"1.0\">\r\n" );
					xsdBuilder.append( SCHEMA_END );
				}

				// ...add our element block...

				int indexOf = xsdBuilder.indexOf( SCHEMA_END );

				if ( indexOf == -1 )
					continue;

				String elementBlock = generateClassBlock( packageName, className );

				if ( elementBlock == null )
					continue;

				xsdBuilder.insert( indexOf, elementBlock );

				// ...and write it out

				IOUtils.streamBetween( new ByteArrayInputStream( xsdBuilder.toString().getBytes() ), new FileOutputStream( xsdFile ) );
			}
		}
		catch ( Exception e )
		{
			throw new BuildException( e );
		}
	}

	//
	// Private methods
	//

	private String generateClassBlock( String packageName, String className )
		throws Exception
	{
		StringBuilder builder = new StringBuilder();

		// Name

		builder.append( "\r\n\t<!-- " );
		builder.append( className );
		builder.append( " -->\r\n" );
		builder.append( "\r\n" );
		builder.append( "\t<xs:element name=\"" );
		builder.append( StringUtils.lowercaseFirstLetter( className ) );
		builder.append( "\">\r\n" );

		builder.append( "\t\t<xs:complexType>\r\n" );

		// Concrete?

		Class<?> clazz = Class.forName( packageName + '.' + className );

		if ( clazz.isInterface() || Modifier.isAbstract( clazz.getModifiers() ) )
			return null;

		// Not immutable/threadsafe and not a Metawidget?

		if ( !new ConfigReader().isImmutableThreadsafe( clazz ) && !className.endsWith( "Metawidget" ) && !MetawidgetTag.class.isAssignableFrom( clazz ) )
			return null;

		// Configurable?

		boolean configurable = false;
		boolean optionallyConfigurable = false;

		for ( Constructor<?> constructor : clazz.getConstructors() )
		{
			if ( constructor.getParameterTypes().length == 0 )
			{
				optionallyConfigurable = true;
				continue;
			}

			if ( constructor.getParameterTypes().length > 1 )
				continue;

			Class<?> parameterType = constructor.getParameterTypes()[0];

			if ( !parameterType.getName().endsWith( "Config" ) )
				continue;

			clazz = parameterType;
			configurable = true;
		}

		// Iterate through each JavaBean property

		String propertiesBlock = generatePropertiesBlock( clazz );

		if ( propertiesBlock != null )
			builder.append( propertiesBlock );

		// Configurable?

		builder.append( "\t\t\t<xs:attribute name=\"config\" type=\"xs:string\"" );

		if ( !configurable )
			builder.append( " use=\"prohibited\"" );
		else if ( !optionallyConfigurable )
			builder.append( " use=\"required\"" );

		builder.append( "/>\r\n" );

		// Finish off

		builder.append( "\t\t</xs:complexType>\r\n" );
		builder.append( "\t</xs:element>\r\n\r\n" );

		return builder.toString();
	}

	private String generatePropertiesBlock( Class<?> clazz )
	{
		StringBuilder builder = new StringBuilder();

		// Special support for setParameter

		boolean supportSetParameter = false;

		try
		{
			clazz.getMethod( "setParameter", String.class, Object.class );
			supportSetParameter = true;
		}
		catch ( Exception e )
		{
			// Okay to fail
		}

		if ( supportSetParameter )
		{
			// A limitation of XML Schema is that if you want to support multiples of
			// an element (ie. xs:sequence, maxOccurs='unbounded') then you can't also
			// support arbitrary order (ie. xs:all). You can't mix the two :(

			builder.append( "\t\t\t<xs:sequence>\r\n" );
			builder.append( generatePropertyBlock( "parameter", true, String.class, Object.class ) );
		}
		else
		{
			builder.append( "\t\t\t<xs:all>\r\n" );
		}

		boolean foundProperty = false;

		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();
		Map<String, Property> properties = propertyStyle.getProperties( clazz );

		for ( Property property : properties.values() )
		{
			if ( !property.isWritable() )
				continue;

			if ( "config".equals( property.getName() ) )
				continue;

			String propertyBlock = generatePropertyBlock( property.getName(), false, property.getType() );

			if ( propertyBlock == null )
				continue;

			foundProperty = true;
			builder.append( propertyBlock );
		}

		if ( !foundProperty )
			return null;

		if ( supportSetParameter )
			builder.append( "\t\t\t</xs:sequence>\r\n" );
		else
			builder.append( "\t\t\t</xs:all>\r\n" );

		return builder.toString();
	}

	private String generatePropertyBlock( String name, boolean canOccurMultipleTimes, Class<?>... types )
	{
		StringBuilder builder = new StringBuilder();

		// Name

		builder.append( "\t\t\t\t<xs:element name=\"" );
		builder.append( name );
		builder.append( "\" minOccurs=\"0\"" );

		if ( canOccurMultipleTimes )
			builder.append( " maxOccurs=\"unbounded\"" );

		builder.append( ">\r\n" );
		builder.append( "\t\t\t\t\t<xs:complexType>\r\n" );
		builder.append( "\t\t\t\t\t\t<xs:sequence>\r\n" );

		// Types

		for ( Class<?> type : types )
		{
			String typeBlock = generateTypeBlock( type );

			if ( typeBlock == null )
				return null;

			builder.append( typeBlock );
		}

		// Finish off

		builder.append( "\t\t\t\t\t\t</xs:sequence>\r\n" );
		builder.append( "\t\t\t\t\t</xs:complexType>\r\n" );
		builder.append( "\t\t\t\t</xs:element>\r\n" );

		return builder.toString();
	}

	private String generateTypeBlock( Class<?> type )
	{
		// Properties to ignore (should never be set via metawidget.xml)

		if ( ResourceResolver.class.equals( type ) )
			return null;

		// Primitives

		if ( boolean.class.equals( type ) )
			return "\t\t\t\t\t\t\t<xs:element name=\"boolean\" type=\"xs:boolean\"/>\r\n";

		if ( int.class.equals( type ) )
			return "\t\t\t\t\t\t\t<xs:element name=\"int\" type=\"xs:int\"/>\r\n";

		if ( ResourceBundle.class.equals( type ) )
			return "\t\t\t\t\t\t\t<xs:element name=\"bundle\" type=\"xs:string\"/>\r\n";

		if ( String.class.equals( type ) )
			return "\t\t\t\t\t\t\t<xs:element name=\"string\" type=\"xs:string\"/>\r\n";

		// InputStreams

		if ( InputStream.class.equals( type ) )
		{
			StringBuilder builder = new StringBuilder();

			builder.append( "\t\t\t\t\t\t\t<xs:choice>\r\n" );
			builder.append( "\t\t\t\t\t\t\t\t<xs:element name=\"file\" type=\"xs:string\"/>\r\n" );
			builder.append( "\t\t\t\t\t\t\t\t<xs:element name=\"resource\" type=\"xs:string\"/>\r\n" );
			builder.append( "\t\t\t\t\t\t\t\t<xs:element name=\"url\" type=\"xs:anyURI\"/>\r\n" );
			builder.append( "\t\t\t\t\t\t\t</xs:choice>\r\n" );

			return builder.toString();
		}

		// Collections

		if ( type.isArray() || List.class.equals( type ) || Set.class.equals( type ) )
		{
			StringBuilder builder = new StringBuilder();

			builder.append( "\t\t\t\t\t\t\t<xs:element name=\"" );

			if ( type.isArray() )
				builder.append( "array" );
			else if ( List.class.equals( type ) )
				builder.append( "list" );
			else if ( Set.class.equals( type ) )
				builder.append( "set" );

			builder.append( "\">\r\n" );
			builder.append( "\t\t\t\t\t\t\t\t<xs:complexType>\r\n" );
			builder.append( "\t\t\t\t\t\t\t\t\t<xs:sequence>\r\n" );
			builder.append( "\t\t\t\t\t\t\t\t\t\t<xs:any maxOccurs=\"unbounded\" processContents=\"lax\"/>\r\n" );
			builder.append( "\t\t\t\t\t\t\t\t\t</xs:sequence>\r\n" );
			builder.append( "\t\t\t\t\t\t\t\t</xs:complexType>\r\n" );
			builder.append( "\t\t\t\t\t\t\t</xs:element>\r\n" );

			return builder.toString();
		}

		// Types that must themselves be instantiated as Java objects

		return "\t\t\t\t\t\t\t<xs:any processContents=\"lax\"/>\r\n";
	}
}
