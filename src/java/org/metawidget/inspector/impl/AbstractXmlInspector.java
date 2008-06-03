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

package org.metawidget.inspector.impl;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.metawidget.inspector.InspectorException;
import org.metawidget.inspector.ResourceResolver;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.LogUtils.Log;
import org.metawidget.util.XmlUtils.NopEntityResolver;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Convenience implementation for Inspectors that inspect XML files.
 * <p>
 * Handles taking a 'flat' XML file (eg. only one <code>&lt;entity&gt;</code> node deep) and
 * traversing nested paths, such as <code>foo/bar</code>, from...
 * <p>
 * <code>
 * &lt;entity type="foo"&gt;<br/>
 * &nbsp;&nbsp;&nbsp;&lt;property name="myBar" type="bar"&gt;<br/>
 * &lt;/entity&gt;
 * </code>
 * <p>
 * ...to top level...
 * <p>
 * <code>
 * &lt;entity type="bar"&gt;
 * </code>
 * <p>
 * ...and constructing...
 * <p>
 * <code>&lt;entity name="myBar" type="bar"&gt;</code>
 * <p>
 * ...as output.
 *
 * @author Richard Kennard
 */

public abstract class AbstractXmlInspector
	extends AbstractInspector
{
	//
	//
	// Private statics
	//
	//

	private final static Log					LOG	= LogUtils.getLog( AbstractXmlInspector.class );

	private final static DocumentBuilderFactory	SHARED_DOCUMENT_BUILDER_FACTORY;

	static
	{
		SHARED_DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
		SHARED_DOCUMENT_BUILDER_FACTORY.setNamespaceAware( true );
		SHARED_DOCUMENT_BUILDER_FACTORY.setIgnoringComments( true );
		SHARED_DOCUMENT_BUILDER_FACTORY.setIgnoringElementContentWhitespace( true );
	}

	//
	//
	// Protected members
	//
	//

	protected Element							mRoot;

	//
	//
	// Constructor
	//
	//

	/**
	 * Config-based constructor.
	 * <p>
	 * All AbstractXmlInspector inspectors must be configurable, to allow specifying an XML file.
	 */

	protected AbstractXmlInspector( AbstractXmlInspectorConfig config, ResourceResolver resolver )
	{
		try
		{
			DocumentBuilderFactory factory = SHARED_DOCUMENT_BUILDER_FACTORY;

			// Look up the schema
			//
			// (J2SE1.4 and Android don't support java.xml.validation)

			if ( ClassUtils.classExists( "javax.xml.validation.SchemaFactory" ) )
			{
				SchemaFactory factorySchema = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
				InputStream schemaStream = config.getSchemaInputStream();

				Object schema = null;

				if ( schemaStream != null )
				{
					schema = factorySchema.newSchema( new StreamSource( schemaStream ) );
				}
				else
				{
					String schemaFile = config.getSchema();

					if ( schemaFile != null && !"".equals( schemaFile ) )
						schema = factorySchema.newSchema( new StreamSource( resolver.openResource( schemaFile ) ) );
				}

				if ( schema != null )
				{
					factory = DocumentBuilderFactory.newInstance();
					factory.setNamespaceAware( true );
					factory.setIgnoringComments( true );
					factory.setIgnoringElementContentWhitespace( true );

					factory.setSchema( (Schema) schema );
				}
			}

			// Look up the XML file

			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver( new NopEntityResolver() );
			mRoot = getDocumentElement( builder, resolver, config );

			if ( mRoot == null )
				throw InspectorException.newException( "No XML input file specified" );

			// Debug

			if ( LOG.isTraceEnabled() )
			{
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, "yes" );
				transformer.setOutputProperty( OutputKeys.INDENT, "yes" );

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				transformer.transform( new DOMSource( mRoot ), new StreamResult( out ) );
				LOG.trace( out.toString() );
			}
		}
		catch ( Exception e )
		{
			throw InspectorException.newException( e );
		}
	}

	//
	//
	// Public methods
	//
	//

	public Document inspect( Object toInspect, String type, String... names )
		throws InspectorException
	{
		try
		{
			Element elementToInspect = null;
			String childName = null;
			Map<String, String> parentAttributes = null;

			// If the path has a parent...

			if ( names != null && names.length > 0 )
			{
				// ...inspect its property for useful attributes...

				Element parent = traverse( type, true, names );

				if ( parent == null )
					return null;

				childName = names[names.length - 1];
				Element parentProperty = XmlUtils.getChildWithAttributeValue( parent, getNameAttribute(), childName );
				parentAttributes = inspect( parentProperty );
				elementToInspect = traverse( parentProperty.getAttribute( getTypeAttribute() ), false );
			}
			else
			{
				// ...otherwise, just start at the end point

				elementToInspect = traverse( type, false, names );

				if ( elementToInspect == null )
					return null;
			}

			Document document = newDocumentBuilder().newDocument();
			Element entity = document.createElementNS( NAMESPACE, ENTITY );

			// Inspect properties

			inspect( elementToInspect, entity );

			// Nothing of consequence to return?

			if ( !entity.hasChildNodes() && ( parentAttributes == null || parentAttributes.isEmpty() ) )
				return null;

			Element root = document.createElementNS( NAMESPACE, ROOT );
			document.appendChild( root );
			root.appendChild( entity );

			// Add any parent attributes

			if ( parentAttributes == null )
			{
				entity.setAttribute( TYPE, type );
			}
			else
			{
				XmlUtils.setMapAsAttributes( entity, parentAttributes );

				String parentType = parentAttributes.get( TYPE );

				if ( parentType == null || "".equals( parentType ) )
					throw InspectorException.newException( "Path " + type + ArrayUtils.toString( names, StringUtils.SEPARATOR_DOT, true, false ) + " has no @type" );
			}

			// Return the document

			return document;
		}
		catch ( Exception e )
		{
			throw InspectorException.newException( e );
		}
	}

	//
	//
	// Protected methods
	//
	//

	protected Element getDocumentElement( DocumentBuilder builder, ResourceResolver resolver, AbstractXmlInspectorConfig config )
		throws Exception
	{
		InputStream[] files = config.getInputStreams();

		if ( files != null )
			return getDocumentElement( builder, resolver, files );

		String[] fileList = config.getFiles();

		if ( fileList != null )
			return getDocumentElement( builder, resolver, fileList );

		return null;
	}

	protected Element getDocumentElement( DocumentBuilder builder, ResourceResolver resolver, InputStream... files )
		throws Exception
	{
		Document documentMaster = null;

		for ( InputStream file : files )
		{
			Document documentParsed = builder.parse( file );

			if ( !documentParsed.hasChildNodes() )
				continue;

			preprocessDocument( documentParsed );

			if ( documentMaster == null || !documentMaster.hasChildNodes() )
			{
				documentMaster = documentParsed;
				continue;
			}

			XmlUtils.combineElements( documentMaster.getDocumentElement(), documentParsed.getDocumentElement(), getTopLevelTypeAttribute(), getNameAttribute() );
		}

		if ( documentMaster == null )
			return null;

		return documentMaster.getDocumentElement();
	}

	protected void preprocessDocument( Document document )
	{
		// Do nothing by default
	}

	/**
	 * Initializes the root element from an array of file paths.
	 * <p>
	 * After resolving the file paths to <code>InputStream</code>s, calls
	 * <code>initRootElement( DocumentBuilder, InputStream... )</code>.
	 */

	protected Element getDocumentElement( DocumentBuilder builder, ResourceResolver resolver, String[] files )
		throws Exception
	{
		InputStream[] fileList = new InputStream[files.length];

		for ( int loop = 0, length = files.length; loop < length; loop++ )
		{
			fileList[loop] = resolver.openResource( files[loop] );
		}

		return getDocumentElement( builder, resolver, fileList );
	}

	protected void inspect( Element toInspect, Element toAddTo )
	{
		if ( toInspect == null )
			return;

		Document document = toAddTo.getOwnerDocument();

		// Do 'extends' attribute first

		String extendsAttribute = getExtendsAttribute();

		if ( extendsAttribute != null )
		{
			String extendsClass = toInspect.getAttribute( extendsAttribute );

			if ( extendsClass != null )
				inspect( traverse( extendsClass, false ), toAddTo );
		}

		// Next, for each child...

		Element element = document.createElementNS( NAMESPACE, PROPERTY );
		NodeList children = toInspect.getChildNodes();

		for ( int loop = 0, length = children.getLength(); loop < length; loop++ )
		{
			Node node = children.item( loop );

			if ( !( node instanceof Element ) )
				continue;

			// ...inspect its attributes...

			Map<String, String> attributes = inspect( (Element) node );

			if ( attributes == null || attributes.isEmpty() )
				continue;

			// ...create an element...

			Element child = document.createElementNS( NAMESPACE, PROPERTY );
			element.appendChild( child );

			XmlUtils.setMapAsAttributes( child, attributes );
		}

		// ...and combine them all

		XmlUtils.combineElements( toAddTo, element, getNameAttribute(), getNameAttribute() );
	}

	protected abstract Map<String, String> inspect( Element toInspect );

	protected Element traverse( String type, boolean onlyToParent, String... names )
		throws InspectorException
	{
		// Validate type

		Element entityElement = XmlUtils.getChildWithAttributeValue( mRoot, getTopLevelTypeAttribute(), type );

		if ( entityElement == null )
			return null;

		if ( names == null )
			return entityElement;

		int length = names.length;

		if ( length == 0 )
			return entityElement;

		// Traverse names

		String extendsAttribute = getExtendsAttribute();

		for ( int loop = 0; loop < length; loop++ )
		{
			String name = names[loop];
			Element property = XmlUtils.getChildWithAttributeValue( entityElement, getNameAttribute(), name );

			if ( property == null )
			{
				// Property may be defined in an 'extends'

				if ( extendsAttribute == null )
					return null;

				while ( true )
				{
					String childExtends = entityElement.getAttribute( extendsAttribute );

					if ( childExtends == null || "".equals( childExtends ) )
						return null;

					entityElement = traverse( childExtends, false );

					if ( entityElement == null )
						return null;

					property = XmlUtils.getChildWithAttributeValue( entityElement, getNameAttribute(), name );

					if ( property != null )
						break;
				}
			}

			if ( onlyToParent && loop >= ( length - 1 ) )
				return entityElement;

			String propertyType = property.getAttribute( getTypeAttribute() );

			if ( propertyType == null )
				throw InspectorException.newException( "Property '" + name + "' has no @" + getTypeAttribute() );

			entityElement = traverse( propertyType, false );

			if ( entityElement == null )
				break;
		}

		return entityElement;
	}

	protected String getTopLevelTypeAttribute()
	{
		return TYPE;
	}

	protected String getNameAttribute()
	{
		return NAME;
	}

	protected String getTypeAttribute()
	{
		return TYPE;
	}

	protected String getExtendsAttribute()
	{
		return null;
	}
}
