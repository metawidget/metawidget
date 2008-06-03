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

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.regex.Pattern;

import org.metawidget.inspector.InspectorException;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Convenience implementation for Inspectors that inspect properties.
 * <p>
 * Handles iterating over a class for properties, and supporting pluggable properties conventions.
 * Also handles unwrapping a POJO wrapped by a proxy library (such as CGLIB or Javassist).
 *
 * @author Richard Kennard
 */

public abstract class AbstractPropertyInspector
	extends AbstractInspector
{
	//
	//
	// Private members
	//
	//

	private PropertyStyle	mPropertyStyle;

	//
	//
	// Protected members
	//
	//

	protected Pattern		mPatternProxy;

	//
	//
	// Constructors
	//
	//

	/**
	 * Config-based constructor.
	 * <p>
	 * All AbstractPropertyInspector inspectors must be configurable, to allow configuring property
	 * styles and proxy patterns.
	 */

	protected AbstractPropertyInspector( AbstractPropertyInspectorConfig config )
	{
		try
		{
			mPropertyStyle = config.getPropertyStyle();
			mPatternProxy = config.getProxyPattern();
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
			Object childToInspect = null;
			String strChildName = null;
			Map<String, String> mapParentAttributes = null;
			Class<?> clazz = null;

			// If the path has a parent...

			if ( names != null && names.length > 0 )
			{
				// ...inspect its property for useful annotations...

				Object objParentToInspect = traverse( toInspect, type, true, names );

				if ( objParentToInspect == null )
					return null;

				Class<?> classParent = ClassUtils.getUnproxiedClass( objParentToInspect.getClass(), mPatternProxy );
				strChildName = names[names.length - 1];

				Property propertyInParent = mPropertyStyle.getProperties( classParent ).get( strChildName );

				if ( propertyInParent == null )
					return null;

				clazz = propertyInParent.getType();

				if ( propertyInParent.isReadable() )
				{
					childToInspect = propertyInParent.read( objParentToInspect );

					mapParentAttributes = inspect( propertyInParent, toInspect );

					if ( !Modifier.isFinal( clazz.getModifiers() ) && childToInspect != null )
						clazz = ClassUtils.getUnproxiedClass( childToInspect.getClass(), mPatternProxy );
				}
			}

			// ...otherwise, just start at the end point

			if ( clazz == null )
			{
				childToInspect = traverse( toInspect, type, false, names );

				if ( childToInspect == null )
					return null;

				clazz = ClassUtils.getUnproxiedClass( childToInspect.getClass(), mPatternProxy );
			}

			Document document = newDocumentBuilder().newDocument();
			Element elementEntity = document.createElementNS( NAMESPACE, ENTITY );

			// Inspect child properties

			if ( childToInspect != null )
				inspect( clazz, childToInspect, elementEntity );

			// Nothing of consequence to return?

			if ( !elementEntity.hasChildNodes() && ( mapParentAttributes == null || mapParentAttributes.isEmpty() ) )
				return null;

			// Start a new DOM Document

			Element elementRoot = document.createElementNS( NAMESPACE, ROOT );
			document.appendChild( elementRoot );
			elementRoot.appendChild( elementEntity );
			elementEntity.setAttribute( TYPE, clazz.getName() );

			// Add any parent attributes

			if ( mapParentAttributes != null )
			{
				XmlUtils.setMapAsAttributes( elementEntity, mapParentAttributes );
				elementEntity.setAttribute( NAME, strChildName );
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

	protected void inspect( Class<?> clazz, Object toInspect, Element toAddTo )
		throws Exception
	{
		Document document = toAddTo.getOwnerDocument();

		for ( Property property : getProperties( clazz ).values() )
		{
			Map<String, String> attributes = inspect( property, toInspect );

			if ( attributes == null || attributes.isEmpty() )
				continue;

			Element element = document.createElementNS( NAMESPACE, PROPERTY );
			element.setAttribute( NAME, property.getName() );

			XmlUtils.setMapAsAttributes( element, attributes );

			toAddTo.appendChild( element );
		}
	}

	/**
	 * Inspect the given property and return a Map of attributes.
	 * <p>
	 * Note: for convenience, this method does not expect subclasses to deal with DOMs and Elements.
	 * Those subclasses wanting more control over these features should override methods higher in
	 * the call stack instead.
	 */

	protected abstract Map<String, String> inspect( Property property, Object toInspect )
		throws Exception;

	protected Map<String, Property> getProperties( Class<?> clazz )
	{
		return mPropertyStyle.getProperties( clazz );
	}

	//
	//
	// Private methods
	//
	//

	private Object traverse( Object toTraverse, String type, boolean onlyToParent, String... names )
		throws InspectorException
	{
		// Validate type

		if ( toTraverse == null )
			return null;

		if ( !ClassUtils.getUnproxiedClass( toTraverse.getClass(), mPatternProxy ).getName().equals( type ) )
			return null;

		if ( names == null )
			return toTraverse;

		int length = names.length;

		if ( length == 0 )
			return toTraverse;

		// Traverse names

		if ( onlyToParent )
			length--;

		Object traverse = toTraverse;

		for ( int loop = 0; loop < length; loop++ )
		{
			Property property = mPropertyStyle.getProperties( ClassUtils.getUnproxiedClass( traverse.getClass(), mPatternProxy ) ).get( names[loop] );

			if ( !property.isReadable() )
				return null;

			traverse = property.read( traverse );

			if ( traverse == null )
				return null;
		}

		return traverse;
	}
}
