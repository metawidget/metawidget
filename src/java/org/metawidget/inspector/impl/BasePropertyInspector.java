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
import java.util.Set;
import java.util.regex.Pattern;

import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.LogUtils.Log;
import org.metawidget.util.simple.StringUtils;
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

public abstract class BasePropertyInspector
	implements Inspector
{
	//
	//
	// Private statics
	//
	//

	private final static Map<Class<? extends PropertyStyle>, PropertyStyle>	PROPERTY_STYLE_CACHE	= CollectionUtils.newHashMap();

	//
	//
	// Private members
	//
	//

	private PropertyStyle													mPropertyStyle;

	//
	//
	// Protected members
	//
	//

	protected Log															mLog					= LogUtils.getLog( getClass() );

	/**
	 * Pattern to use to detect (and unwrap) proxied classes (such as CGLIB and Javassist).
	 * <p>
	 * The proxy pattern is defined in <code>BasePropertyInspector</code>, rather than in
	 * <code>PropertyStyle</code> for the following reasons:
	 * <ul>
	 * <li><code>inspect</code> needs to match <code>type</code> to <code>toInspect</code>,
	 * which may involve unwrapping
	 * <li>Different PropertyStyles (eg. JavaBean, GroovyBean) would need to re-implement
	 * proxy-support
	 * <li>Having the pattern here makes it configurable
	 * </ul>
	 */

	protected Pattern														mPatternProxy;

	//
	//
	// Constructors
	//
	//

	/**
	 * Config-based constructor.
	 * <p>
	 * All BasePropertyInspector-derived inspectors must be configurable, to allow configuring
	 * property styles and proxy patterns.
	 */

	protected BasePropertyInspector( BasePropertyInspectorConfig config )
	{
		try
		{
			Class<? extends PropertyStyle> propertyStyle = config.getPropertyStyle();

			mPropertyStyle = PROPERTY_STYLE_CACHE.get( propertyStyle );

			if ( mPropertyStyle == null )
			{
				mPropertyStyle = propertyStyle.newInstance();
				PROPERTY_STYLE_CACHE.put( propertyStyle, mPropertyStyle );
			}

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

	public String inspect( Object toInspect, String type, String... names )
		throws InspectorException
	{
		try
		{
			Object childToInspect = null;
			String childName = null;
			Map<String, String> parentAttributes = null;
			Class<?> clazz = null;

			// If the path has a parent...

			if ( names != null && names.length > 0 )
			{
				// ...inspect its property for useful annotations...

				Object parentToInspect = traverse( toInspect, type, true, names );

				if ( parentToInspect == null )
					return null;

				Class<?> classParent = ClassUtils.getUnproxiedClass( parentToInspect.getClass(), mPatternProxy );
				childName = names[names.length - 1];

				Property propertyInParent = mPropertyStyle.getProperties( classParent ).get( childName );

				if ( propertyInParent == null )
					return null;

				clazz = propertyInParent.getType();

				if ( propertyInParent.isReadable() )
				{
					childToInspect = propertyInParent.read( parentToInspect );
					parentAttributes = inspectProperty( propertyInParent, toInspect );

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

			Document document = XmlUtils.newDocumentBuilder().newDocument();
			Element elementEntity = document.createElementNS( NAMESPACE, ENTITY );

			// Inspect child properties

			if ( childToInspect != null )
				inspect( clazz, childToInspect, elementEntity );

			// Nothing of consequence to return?

			if ( isInspectionEmpty( elementEntity, parentAttributes ) )
				return null;

			// Start a new DOM Document

			Element elementRoot = document.createElementNS( NAMESPACE, ROOT );
			document.appendChild( elementRoot );
			elementRoot.appendChild( elementEntity );

			// Every Inspector needs to attach a type to the root entity, so
			// that CompositeInspector can merge it

			elementEntity.setAttribute( TYPE, clazz.getName() );

			// Add any parent attributes

			if ( parentAttributes != null )
			{
				XmlUtils.setMapAsAttributes( elementEntity, parentAttributes );
				elementEntity.setAttribute( NAME, childName );
			}

			// Return the document

			return XmlUtils.documentToString( document );
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
			Map<String, String> attributes = inspectProperty( property, toInspect );

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

	protected abstract Map<String, String> inspectProperty( Property property, Object toInspect )
		throws Exception;

	protected Map<String, Property> getProperties( Class<?> clazz )
	{
		return mPropertyStyle.getProperties( clazz );
	}

	/**
	 * Returns true if the inspection returned nothing of consequence. This is an optimization that
	 * allows our <code>Inspector</code> to return <code>null</code> overall, rather than
	 * creating and serializing an XML document, which <code>CompositeInspector</code> then
	 * deserializes and merges, all for no meaningful content.
	 *
	 * @return true if the inspection is 'empty'
	 */

	protected boolean isInspectionEmpty( Element elementEntity, Map<String, String> parentAttributes )
	{
		if ( elementEntity.hasChildNodes() )
			return false;

		if ( parentAttributes != null && !parentAttributes.isEmpty() )
			return false;

		return true;
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

		Object traverse = toTraverse;
		Object parentTraverse = null;

		Set<Object> traversed = CollectionUtils.newHashSet();
		traversed.add( traverse );

		for ( String name : names )
		{
			Property property = mPropertyStyle.getProperties( ClassUtils.getUnproxiedClass( traverse.getClass(), mPatternProxy ) ).get( name );

			if ( property == null || !property.isReadable() )
				return null;

			parentTraverse = traverse;
			traverse = property.read( traverse );

			if ( traverse == null )
				break;

			// Unlike BaseXmlInspector (which can never be certain it has detected a
			// cyclic reference because it only looks at types, not objects), BasePropertyInspector
			// can detect cycles and nip them in the bud

			if ( !traversed.add( traverse ) )
			{
				LogUtils.getLog( getClass() ).warn( getClass().getSimpleName() + " prevented infinite recursion on " + type + ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) + ". Consider annotating " + name + " as @UiHidden" );
				return null;
			}
		}

		if ( onlyToParent )
			return parentTraverse;

		return traverse;
	}
}
