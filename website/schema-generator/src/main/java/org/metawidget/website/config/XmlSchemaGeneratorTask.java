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

package org.metawidget.website.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
import java.util.regex.Pattern;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.metawidget.config.ResourceResolver;
import org.metawidget.iface.Immutable;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.util.ClassUtils;
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
	extends Task {

	//
	// Private statics
	//

	private static final String	CLASS_SUFFIX	= ".class";

	private static final String	XSD_SUFFIX		= "-1.0.xsd";

	private static final String	SCHEMA_END		= "\r\n</xs:schema>";

	//
	// Private members
	//

	private String				mJar;

	private String				mDestDir;

	//
	// Public methods
	//

	public void setJar( String jar ) {

		mJar = jar;
	}

	public void setDestDir( String destDir ) {

		mDestDir = destDir;
	}

	@Override
	public void execute()
		throws BuildException {

		try {
			// Create/clear the dest dir

			File destDir;

			if ( getProject() != null ) {
				destDir = new File( getProject().getBaseDir(), mDestDir );
			} else {
				destDir = new File( mDestDir );
			}
			destDir.mkdirs();

			for ( File file : destDir.listFiles() ) {
				if ( !file.isDirectory() ) {
					file.delete();
				}
			}

			// Start index.html

			FileWriter indexWriter = new FileWriter( new File( destDir, "index.php" ) );
			indexWriter.write( "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n" );
			indexWriter.write( "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">\r\n" );
			indexWriter.write( "\t<head>\r\n" );
			indexWriter.write( "\t\t<title>Metawidget XML Schemas</title>" );
			indexWriter.write( "\t</head>\r\n" );
			indexWriter.write( "\t<body>\r\n" );
			indexWriter.write( "\t\t<h1>Metawidget XML Schemas</h1>\r\n" );
			indexWriter.write( "\t\t<h2>Inspection Results</h2>\r\n" );
			indexWriter.write( "\t\t<p>This schema is used for inspection results returned by <a href=\"http://metawidget.org/doc/api/org/metawidget/inspector/iface/Inspector.html\">Inspectors</a>:</p>\r\n" );
			indexWriter.write( "\t\t<ul>\r\n" );
			indexWriter.write( "\t\t\t<li><a href=\"inspection-result-1.0.xsd\">inspection-result-1.0.xsd</a></li>\r\n" );
			indexWriter.write( "\t\t</ul>\r\n" );
			indexWriter.write( "\t\t<h2>External Configuration</h2>\r\n" );
			indexWriter.write( "\t\t<p>These schemas are (optionally) used when externally configuring Metawidget via <tt>metawidget.xml</tt>. For example</p>\r\n" );
			indexWriter.write( "<div style=\"background-color: #eeeeee; border: 1px solid #cccccc; padding: 5px\"><tt>&lt;metawidget xmlns=\"<strong>http://metawidget.org</strong>\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"<br/>" );
			indexWriter.write( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;xsi:schemaLocation=\"<strong>http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd<br/>" );
			indexWriter.write( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;java:org.metawidget.jsp.tagext.html.spring http://metawidget.org/xsd/org.metawidget.jsp.tagext.html.spring-1.0.xsd</strong>\"<br/>" );
			indexWriter.write( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;version=\"1.0\"&gt;<br/>" );
			indexWriter.write( "&nbsp;&nbsp;&nbsp;&lt;springMetawidgetTag xmlns=\"<strong>java:org.metawidget.jsp.tagext.html.spring</strong>\"/&gt;<br/>" );
			indexWriter.write( "&lt;/metawidget&gt;</tt></div>\r\n" );
			indexWriter.write( "\t\t<ul>\r\n" );
			indexWriter.write( "\t\t\t<li><a href=\"metawidget-1.0.xsd\">metawidget-1.0.xsd</a></li>\r\n" );

			// For each entry in the JAR file...

			File file;

			if ( getProject() != null ) {
				file = new File( getProject().getBaseDir(), mJar );
			} else {
				file = new File( mJar );
			}

			if ( !file.exists() ) {
				throw new FileNotFoundException( file.getAbsolutePath() );
			}

			JarFile jarFile = new JarFile( file );
			log( "Writing " + jarFile.getName() + " to " + destDir.getAbsolutePath(), Project.MSG_INFO );

			String lastXsdFilename = null;

			for ( Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements(); ) {
				JarEntry jarEntry = e.nextElement();

				// ...that is a Java class...

				if ( jarEntry.isDirectory() ) {
					continue;
				}

				String name = jarEntry.getName();

				if ( !name.endsWith( CLASS_SUFFIX ) ) {
					continue;
				}

				// ...determine the package name...

				String qualifiedClassName = name.replace( StringUtils.SEPARATOR_FORWARD_SLASH, StringUtils.SEPARATOR_DOT ).substring( 0, name.length() - CLASS_SUFFIX.length() );

				int lastIndexOf = qualifiedClassName.lastIndexOf( StringUtils.SEPARATOR_DOT );

				if ( lastIndexOf == -1 ) {
					continue;
				}

				String className = qualifiedClassName.substring( lastIndexOf + 1 );

				// ...ignore inner classes...

				if ( className.contains( "$" ) ) {
					continue;
				}

				String packageName = qualifiedClassName.substring( 0, lastIndexOf );
				log( "\t" + qualifiedClassName, Project.MSG_INFO );

				// ...find the XSD file...

				File xsdFile = new File( destDir, packageName + XSD_SUFFIX );

				// ...load it if it already exists...

				StringBuilder xsdBuilder = new StringBuilder();

				if ( xsdFile.exists() ) {
					ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
					IOUtils.streamBetween( new FileInputStream( xsdFile ), streamOut );
					xsdBuilder.append( streamOut.toString() );
				}

				// ...or create it if it doesn't...

				else {
					xsdBuilder.append( "<?xml version=\"1.0\" ?>\r\n" );
					xsdBuilder.append( "<xs:schema targetNamespace=\"java:" );
					xsdBuilder.append( packageName );
					xsdBuilder.append( "\" xmlns=\"java:" );
					xsdBuilder.append( packageName );
					xsdBuilder.append( "\"" );
					xsdBuilder.append( " xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" elementFormDefault=\"qualified\" version=\"1.0\">\r\n" );
					xsdBuilder.append( SCHEMA_END );

					// Note: as determined in this thread...
					//
					// http://old.nabble.com/Possible-to-restrict-top-level-xs%3Aelement-names--ts26227610.html
					//
					// ..."The [XML Schema] spec doesn't provide a mechanism by which a schema can
					// constrain the document element of schema-valid documents". So we can't flag
					// an error if someone tries to declare <siwngMetawidget> (ie.
					// siwng instead of swing). This is unfortunate.
				}

				// ...add our element block...

				int indexOf = xsdBuilder.indexOf( SCHEMA_END );

				if ( indexOf == -1 ) {
					continue;
				}

				String elementBlock = generateClassBlock( packageName, className );

				if ( elementBlock == null ) {
					continue;
				}

				xsdBuilder.insert( indexOf, elementBlock );

				// ...and write it out

				IOUtils.streamBetween( new ByteArrayInputStream( xsdBuilder.toString().getBytes() ), new FileOutputStream( xsdFile ) );

				String xsdFilename = xsdFile.getName();

				if ( !xsdFilename.equals( lastXsdFilename ) ) {
					indexWriter.write( "\t\t\t<li><a href=\"" + xsdFilename + "\">" + xsdFilename + "</a></li>\r\n" );
					lastXsdFilename = xsdFilename;
				}
			}

			jarFile.close();

			// End index.html

			indexWriter.write( "\t\t</ul>\r\n\t</body>\r\n</html>" );
			indexWriter.close();
		} catch ( Exception e ) {
			throw new BuildException( e );
		}
	}

	//
	// Package private methods
	//

	/* package private */String generateClassBlock( String packageName, String className )
		throws Exception {

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

		Class<?> clazz = Class.forName( packageName + '.' + className, false, Thread.currentThread().getContextClassLoader() );

		if ( clazz.isInterface() || Modifier.isAbstract( clazz.getModifiers() ) ) {
			return null;
		}

		// Not immutable and not a Metawidget?

		if ( !Immutable.class.isAssignableFrom( clazz ) && !className.endsWith( "Metawidget" ) && !MetawidgetTag.class.isAssignableFrom( clazz ) ) {
			return null;
		}

		// Configurable?

		boolean configurable = false;
		boolean optionallyConfigurable = false;

		for ( Constructor<?> constructor : clazz.getConstructors() ) {
			if ( constructor.getParameterTypes().length == 0 ) {
				optionallyConfigurable = true;
				continue;
			}

			if ( constructor.getParameterTypes().length > 1 ) {
				continue;
			}

			Class<?> parameterType = constructor.getParameterTypes()[0];

			if ( !parameterType.getName().endsWith( "Config" ) ) {
				continue;
			}

			clazz = parameterType;
			configurable = true;
		}

		// Iterate through each JavaBean property

		String propertiesBlock = generatePropertiesBlock( clazz );

		if ( propertiesBlock != null ) {
			builder.append( propertiesBlock );
		}

		// Configurable?

		builder.append( "\t\t\t<xs:attribute name=\"config\" type=\"xs:string\"" );

		if ( !configurable ) {
			builder.append( " use=\"prohibited\"" );
		} else if ( !optionallyConfigurable ) {
			builder.append( " use=\"required\"" );
		}

		builder.append( "/>\r\n" );

		// Finish off

		builder.append( "\t\t</xs:complexType>\r\n" );
		builder.append( "\t</xs:element>\r\n" );

		return builder.toString();
	}

	//
	// Private methods
	//

	private String generatePropertiesBlock( Class<?> clazz )
		throws Exception {

		StringBuilder builder = new StringBuilder();

		// Special support for setParameter

		boolean supportSetParameter = false;

		try {
			clazz.getMethod( "setParameter", String.class, Object.class );
			supportSetParameter = true;
		} catch ( Exception e ) {
			// Okay to fail
		}

		if ( supportSetParameter ) {
			// A limitation of XML Schema is that if you want to support multiples of
			// an element (ie. xs:sequence, maxOccurs='unbounded') then you can't also
			// support arbitrary order (ie. xs:all). You can't mix the two :(

			builder.append( "\t\t\t<xs:sequence>\r\n" );
			builder.append( generatePropertyBlock( "parameter", true, String.class, Object.class ) );
		} else {
			builder.append( "\t\t\t<xs:all>\r\n" );
		}

		boolean foundProperty = false;

		JavaBeanPropertyStyleConfig propertyStyleConfig = new JavaBeanPropertyStyleConfig();
		propertyStyleConfig.setExcludeBaseType( Pattern.compile( "^(java|javax|org\\.eclipse)\\..*$" ) );
		JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle( propertyStyleConfig );
		Map<String, Property> properties = propertyStyle.getProperties( clazz.getName() );

		for ( Property property : properties.values() ) {
			if ( !property.isWritable() ) {
				continue;
			}

			if ( "config".equals( property.getName() ) ) {
				continue;
			}

			String propertyBlock = generatePropertyBlock( property.getName(), false, ClassUtils.niceForName( property.getType() ));

			if ( propertyBlock == null ) {
				continue;
			}

			foundProperty = true;
			builder.append( propertyBlock );
		}

		if ( !foundProperty ) {
			return null;
		}

		if ( supportSetParameter ) {
			builder.append( "\t\t\t</xs:sequence>\r\n" );
		} else {
			builder.append( "\t\t\t</xs:all>\r\n" );
		}

		return builder.toString();
	}

	private String generatePropertyBlock( String name, boolean canOccurMultipleTimes, Class<?>... types ) {

		StringBuilder builder = new StringBuilder();

		// Name

		builder.append( "\t\t\t\t<xs:element name=\"" );
		builder.append( name );
		builder.append( "\" minOccurs=\"0\"" );

		if ( canOccurMultipleTimes ) {
			builder.append( " maxOccurs=\"unbounded\"" );
		}

		builder.append( ">\r\n" );
		builder.append( "\t\t\t\t\t<xs:complexType>\r\n" );
		builder.append( "\t\t\t\t\t\t<xs:sequence>\r\n" );

		// Types

		for ( Class<?> type : types ) {
			String typeBlock = generateTypeBlock( type );

			if ( typeBlock == null ) {
				return null;
			}

			builder.append( typeBlock );
		}

		// Finish off

		builder.append( "\t\t\t\t\t\t</xs:sequence>\r\n" );
		builder.append( "\t\t\t\t\t</xs:complexType>\r\n" );
		builder.append( "\t\t\t\t</xs:element>\r\n" );

		return builder.toString();
	}

	private String generateTypeBlock( Class<?> type ) {

		// Properties to ignore (should never be set via metawidget.xml)

		if ( ResourceResolver.class.equals( type ) ) {
			return null;
		}

		// Primitives

		if ( boolean.class.equals( type ) ) {
			return "\t\t\t\t\t\t\t<xs:element name=\"boolean\" type=\"xs:boolean\"/>\r\n";
		}

		if ( int.class.equals( type ) ) {
			return "\t\t\t\t\t\t\t<xs:element name=\"int\" type=\"xs:int\"/>\r\n";
		}

		if ( ResourceBundle.class.equals( type ) ) {
			return "\t\t\t\t\t\t\t<xs:element name=\"bundle\" type=\"xs:string\"/>\r\n";
		}

		if ( String.class.equals( type ) ) {
			return "\t\t\t\t\t\t\t<xs:element name=\"string\" type=\"xs:string\"/>\r\n";
		}

		// InputStreams

		if ( InputStream.class.equals( type ) ) {
			StringBuilder builder = new StringBuilder();

			builder.append( "\t\t\t\t\t\t\t<xs:choice>\r\n" );
			builder.append( "\t\t\t\t\t\t\t\t<xs:element name=\"file\" type=\"xs:string\"/>\r\n" );
			builder.append( "\t\t\t\t\t\t\t\t<xs:element name=\"resource\" type=\"xs:string\"/>\r\n" );
			builder.append( "\t\t\t\t\t\t\t\t<xs:element name=\"url\" type=\"xs:anyURI\"/>\r\n" );
			builder.append( "\t\t\t\t\t\t\t</xs:choice>\r\n" );

			return builder.toString();
		}

		// Collections

		if ( type.isArray() || List.class.equals( type ) || Set.class.equals( type ) ) {
			StringBuilder builder = new StringBuilder();

			builder.append( "\t\t\t\t\t\t\t<xs:element name=\"" );

			if ( type.isArray() ) {
				builder.append( "array" );
			} else if ( List.class.equals( type ) ) {
				builder.append( "list" );
			} else if ( Set.class.equals( type ) ) {
				builder.append( "set" );
			}

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
