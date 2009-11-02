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

import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

import org.metawidget.config.ResourceResolver;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.ArrayUtils;
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
 * <p>
 * This class does not support schema validation - it is not that useful in practice for two reasons.
 * First, Inspectors like <code>HibernateInspector</code> cannot use it because they can be pointed
 * at different kinds of files (eg. hibernate.cfg.xml or hibernate-mapping.hbm.xml). Second,
 * Inspectors that are intended for Android environments (eg. <code>XmlInspector</code>) cannot use
 * it because Android's Dalvik preprocessor balks at the unsupported schema classes (even if they're
 * wrapped in a <code>ClassNotFoundException</code>).
 *
 * @author Richard Kennard
 */

public abstract class BaseXmlInspector
	implements Inspector
{
	//
	// Protected members
	//

	protected Log		mLog	= LogUtils.getLog( getClass() );

	protected Element	mRoot;

	//
	// Constructor
	//

	/**
	 * Config-based constructor.
	 * <p>
	 * All BaseXmlInspector inspectors must be configurable, to allow specifying an XML file.
	 */

	protected BaseXmlInspector( BaseXmlInspectorConfig config )
	{
		try
		{
			DocumentBuilder builder = newDocumentBuilder( config );

			// Look up the XML file

			builder.setEntityResolver( new NopEntityResolver() );
			InputStream[] files = config.getInputStreams();

			if ( files != null && files.length > 0 )
				mRoot = getDocumentElement( builder, config.getResourceResolver(), config.getInputStreams() );

			if ( mRoot == null )
				throw InspectorException.newException( "No XML input file specified" );

			// Debug

			if ( mLog.isTraceEnabled() )
				mLog.trace( XmlUtils.documentToString( mRoot.getOwnerDocument(), false ) );
		}
		catch ( Exception e )
		{
			throw InspectorException.newException( e );
		}
	}

	//
	// Public methods
	//

	public String inspect( Object toInspect, String type, String... names )
	{
		// If no type, return nothing

		if ( type == null )
			return null;

		try
		{
			Element elementToInspect = null;
			Map<String, String> parentAttributes = null;

			// If the path has a parent...

			if ( names != null && names.length > 0 )
			{
				// ...inspect its property for useful attributes...

				Element propertyInParent = traverse( type, true, names );

				if ( propertyInParent == null )
					return null;

				parentAttributes = inspectProperty( propertyInParent );

				String typeAttribute = getTypeAttribute();

				// If the property in the parent has no @type, then we cannot traverse to the child.
				// Even if we wanted to just return the parent attributes, we have no @type to
				// attach to the top-level 'entity' node. So we must fail hard here. If we just
				// return 'null', we may silently ignore parent attributes (such as a lookup)
				//
				// Note: this rule should never be triggered if the property has the 'dont-expand'
				// attribute set, because we should never try to traverse the child

				if ( !propertyInParent.hasAttribute( typeAttribute ) )
					throw InspectorException.newException( "Property " + names[names.length - 1] + " has no @" + typeAttribute + " attribute, so cannot navigate to " + type + ArrayUtils.toString( names, StringUtils.SEPARATOR_DOT, true, false ) );

				elementToInspect = traverse( propertyInParent.getAttribute( typeAttribute ), false );
			}
			else
			{
				// ...otherwise, just start at the end point

				elementToInspect = traverse( type, false, names );

				if ( elementToInspect == null )
					return null;
			}

			Document document = XmlUtils.newDocumentBuilder().newDocument();
			Element entity = document.createElementNS( NAMESPACE, ENTITY );

			// Inspect properties

			inspect( elementToInspect, entity );

			// Nothing of consequence to return?

			if ( !entity.hasChildNodes() && ( parentAttributes == null || parentAttributes.isEmpty() ) )
				return null;

			Element root = document.createElementNS( NAMESPACE, ROOT );
			root.setAttribute( VERSION, "1.0" );
			document.appendChild( root );
			root.appendChild( entity );

			// Add any parent attributes

			if ( parentAttributes == null )
				entity.setAttribute( TYPE, type );
			else
				XmlUtils.setMapAsAttributes( entity, parentAttributes );

			// Return the document

			return XmlUtils.documentToString( document, false );
		}
		catch ( Exception e )
		{
			throw InspectorException.newException( e );
		}
	}

	//
	// Protected methods
	//

	protected DocumentBuilder newDocumentBuilder( BaseXmlInspectorConfig config )
		throws Exception
	{
		return XmlUtils.newDocumentBuilder();
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

	protected void inspect( Element toInspect, Element toAddTo )
	{
		if ( toInspect == null )
			return;

		Document document = toAddTo.getOwnerDocument();

		// Do 'extends' attribute first

		String extendsAttribute = getExtendsAttribute();

		if ( extendsAttribute != null )
		{
			if ( toInspect.hasAttribute( extendsAttribute ) )
				inspect( traverse( toInspect.getAttribute( extendsAttribute ), false ), toAddTo );
		}

		// Next, for each child...

		Element element = document.createElementNS( NAMESPACE, ENTITY );
		NodeList children = toInspect.getChildNodes();

		for ( int loop = 0, length = children.getLength(); loop < length; loop++ )
		{
			Node child = children.item( loop );

			if ( !( child instanceof Element ) )
				continue;

			// ...inspect its attributes...

			Element inspected = inspect( document, (Element) child );

			if ( inspected == null )
				continue;

			element.appendChild( inspected );
		}

		// ...and combine them all. Note the element may already exist from the superclass,
		// and its attributes will get overridden by the subclass

		XmlUtils.combineElements( toAddTo, element, NAME, NAME );
	}

	protected Element inspect( Document toAddTo, Element toInspect )
	{
		// Properties

		Map<String, String> propertyAttributes = inspectProperty( toInspect );

		if ( propertyAttributes != null && !propertyAttributes.isEmpty() )
		{
			Element child = toAddTo.createElementNS( NAMESPACE, PROPERTY );
			XmlUtils.setMapAsAttributes( child, propertyAttributes );

			return child;
		}

		// Actions

		Map<String, String> actionAttributes = inspectAction( toInspect );

		if ( actionAttributes != null && !actionAttributes.isEmpty() )
		{
			// Sanity check

			if ( propertyAttributes != null )
				throw InspectorException.newException( "Ambigious match: " + toInspect.getNodeName() + " matches as both a property and an action" );

			Element child = toAddTo.createElementNS( NAMESPACE, ACTION );
			XmlUtils.setMapAsAttributes( child, actionAttributes );

			return child;
		}

		return null;
	}

	/**
	 * Inspect the given Element and return a Map of attributes if it is a property.
	 */

	protected Map<String, String> inspectProperty( Element toInspect )
	{
		return null;
	}

	/**
	 * Inspect the given Element and return a Map of attributes if it is an action.
	 */

	protected Map<String, String> inspectAction( Element toInspect )
	{
		return null;
	}

	protected Element traverse( String type, boolean onlyToParent, String... names )
	{
		// Validate type

		String topLevelTypeAttribute = getTopLevelTypeAttribute();
		Element entityElement = XmlUtils.getChildWithAttributeValue( mRoot, topLevelTypeAttribute, type );

		if ( entityElement == null )
			return null;

		if ( names == null )
			return entityElement;

		int length = names.length;

		if ( length == 0 )
			return entityElement;

		// Traverse names

		String extendsAttribute = getExtendsAttribute();
		String nameAttribute = getNameAttribute();
		String typeAttribute = getTypeAttribute();

		for ( int loop = 0; loop < length; loop++ )
		{
			String name = names[loop];
			Element property = XmlUtils.getChildWithAttributeValue( entityElement, nameAttribute, name );

			if ( property == null )
			{
				// XML structure may not support 'extends'

				if ( extendsAttribute == null )
					return null;

				// Property may be defined in an 'extends'

				while ( true )
				{
					if ( !entityElement.hasAttribute( extendsAttribute ) )
						return null;

					String childExtends = entityElement.getAttribute( extendsAttribute );
					entityElement = XmlUtils.getChildWithAttributeValue( mRoot, topLevelTypeAttribute, childExtends );

					if ( entityElement == null )
						return null;

					property = XmlUtils.getChildWithAttributeValue( entityElement, nameAttribute, name );

					if ( property != null )
						break;
				}

				if ( property == null )
					return null;
			}

			if ( onlyToParent && loop >= ( length - 1 ) )
				return property;

			if ( !property.hasAttribute( typeAttribute ) )
				throw InspectorException.newException( "Property " + name + " in entity " + entityElement.getAttribute( typeAttribute ) + " has no @" + typeAttribute + " attribute, so cannot navigate to " + type + ArrayUtils.toString( names, StringUtils.SEPARATOR_DOT, true, false ) );

			String propertyType = property.getAttribute( typeAttribute );
			entityElement = XmlUtils.getChildWithAttributeValue( mRoot, topLevelTypeAttribute, propertyType );

			if ( entityElement == null )
				break;

			// Unlike BaseObjectInspector, we cannot test for cyclic references because
			// we're only looking at types, not objects
		}

		return entityElement;
	}

	/**
	 * The attribute on top-level elements that uniquely identifies them.
	 */

	protected String getTopLevelTypeAttribute()
	{
		return TYPE;
	}

	/**
	 * The attribute on child elements that uniquely identifies them.
	 */

	protected String getNameAttribute()
	{
		return NAME;
	}

	/**
	 * The attribute on child elements that identifies another top-level element.
	 * <p>
	 * This is necessary for path traversal. If an XML format does not specify a way to traverse
	 * from a child to another top-level element, the Inspector cannot find information along paths
	 * (eg. <code>foo/bar/baz</code>). There <em>is</em> a way around this but, on balance, we
	 * decided against it (see http://kennardconsulting.blogspot.com/2008/01/ask-your-father.html).
	 */

	protected String getTypeAttribute()
	{
		return TYPE;
	}

	/**
	 * The attribute on top-level elements that identifies a superclass relationship (if any).
	 */

	protected String getExtendsAttribute()
	{
		return null;
	}
}
