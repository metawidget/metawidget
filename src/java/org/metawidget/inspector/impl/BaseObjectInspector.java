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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.actionstyle.Action;
import org.metawidget.inspector.impl.actionstyle.ActionStyle;
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
 * Convenience implementation for Inspectors that inspect Objects.
 * <p>
 * Handles iterating over an Object for properties and actions, and supporting pluggable property
 * and action conventions. Also handles unwrapping an Object wrapped by a proxy library (such as
 * CGLIB or Javassist).
 *
 * @author Richard Kennard
 */

public abstract class BaseObjectInspector
	implements Inspector
{
	//
	// Private statics
	//

	private final static Map<Class<? extends PropertyStyle>, PropertyStyle>	PROPERTY_STYLE_CACHE	= CollectionUtils.newHashMap();

	private final static Map<Class<? extends ActionStyle>, ActionStyle>		ACTION_STYLE_CACHE		= CollectionUtils.newHashMap();

	//
	// Private members
	//

	private PropertyStyle													mPropertyStyle;

	private ActionStyle														mActionStyle;

	//
	// Protected members
	//

	protected Log															mLog					= LogUtils.getLog( getClass() );

	//
	// Constructors
	//

	/**
	 * Config-based constructor.
	 * <p>
	 * All BaseObjectInspector-derived inspectors must be configurable, to allow configuring
	 * property styles and proxy patterns.
	 */

	protected BaseObjectInspector( BaseObjectInspectorConfig config )
	{
		try
		{
			// Property

			Class<? extends PropertyStyle> propertyStyle = config.getPropertyStyle();

			if ( propertyStyle != null )
			{
				mPropertyStyle = PROPERTY_STYLE_CACHE.get( propertyStyle );

				if ( mPropertyStyle == null )
				{
					mPropertyStyle = propertyStyle.newInstance();
					PROPERTY_STYLE_CACHE.put( propertyStyle, mPropertyStyle );
				}
			}

			// Action

			Class<? extends ActionStyle> actionStyle = config.getActionStyle();

			if ( actionStyle != null )
			{
				mActionStyle = ACTION_STYLE_CACHE.get( actionStyle );

				if ( mActionStyle == null )
				{
					mActionStyle = actionStyle.newInstance();
					ACTION_STYLE_CACHE.put( actionStyle, mActionStyle );
				}
			}
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
		throws InspectorException
	{
		// If no type, return nothing

		if ( type == null )
			return null;

		try
		{
			Object childToInspect = null;
			String childName = null;
			String childType;
			Map<String, String> parentAttributes = null;

			// If the path has a parent...

			if ( names != null && names.length > 0 )
			{
				// ...inspect its property for useful annotations...

				Object parentToInspect = traverse( toInspect, type, true, names );

				if ( parentToInspect == null )
					return null;

				childName = names[names.length - 1];
				Property propertyInParent = mPropertyStyle.getProperties( parentToInspect.getClass() ).get( childName );

				if ( propertyInParent == null )
					return null;

				childType = propertyInParent.getType().getName();

				if ( propertyInParent.isReadable() )
				{
					childToInspect = propertyInParent.read( parentToInspect );
					parentAttributes = inspectProperty( propertyInParent, toInspect );
				}
			}

			// ...otherwise, just start at the end point

			else
			{
				childToInspect = traverse( toInspect, type, false );

				if ( childToInspect == null )
					return null;

				childType = type;
			}

			Document document = XmlUtils.newDocumentBuilder().newDocument();
			Element elementEntity = document.createElementNS( NAMESPACE, ENTITY );

			// Inspect child properties

			if ( childToInspect != null )
				inspect( childToInspect, elementEntity );

			// Nothing of consequence to return?

			if ( isInspectionEmpty( elementEntity, parentAttributes ) )
				return null;

			// Start a new DOM Document

			Element elementRoot = document.createElementNS( NAMESPACE, ROOT );
			document.appendChild( elementRoot );
			elementRoot.appendChild( elementEntity );

			// Add any parent attributes

			if ( parentAttributes != null )
			{
				XmlUtils.setMapAsAttributes( elementEntity, parentAttributes );
				elementEntity.setAttribute( NAME, childName );
			}

			// Every Inspector needs to attach a type to the entity, so that CompositeInspector can
			// merge it. The type should be the *declared* type, not the *actual* type, as otherwise
			// subtypes (and proxied types) will stop XML and Object-based Inspectors merging back
			// together properly

			elementEntity.setAttribute( TYPE, childType );

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

	/**
	 * @param toInspect
	 *            the obejct to inspect. Never null
	 */

	protected void inspect( Object toInspect, Element toAddTo )
		throws Exception
	{
		Document document = toAddTo.getOwnerDocument();
		Class<?> clazz = toInspect.getClass();

		// Inspect properties

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

		// Inspect actions

		for ( Action action : getActions( clazz ).values() )
		{
			Map<String, String> attributes = inspectAction( action, toInspect );

			if ( attributes == null || attributes.isEmpty() )
				continue;

			Element element = document.createElementNS( NAMESPACE, ACTION );
			element.setAttribute( NAME, action.getName() );

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
		if ( mPropertyStyle == null )
		{
			// (use Collections.EMPTY_MAP, not Collections.emptyMap, so that we're 1.4 compatible)

			@SuppressWarnings( "unchecked" )
			Map<String, Property> map = Collections.EMPTY_MAP;
			return map;
		}

		return mPropertyStyle.getProperties( clazz );
	}

	/**
	 * Inspect the given action and return a Map of attributes.
	 * <p>
	 * Note: for convenience, this method does not expect subclasses to deal with DOMs and Elements.
	 * Those subclasses wanting more control over these features should override methods higher in
	 * the call stack instead.
	 * <p>
	 * Note: unlike <code>inspectProperty</code>, this method has a default implementation that
	 * returns <code>null</code>. This is because most Inspectors will not implement
	 * <code>inspectAction</code>.
	 */

	protected Map<String, String> inspectAction( Action action, Object toInspect )
		throws Exception
	{
		return null;
	}

	protected Map<String, Action> getActions( Class<?> clazz )
	{
		if ( mActionStyle == null )
		{
			// (use Collections.EMPTY_MAP, not Collections.emptyMap, so that we're 1.4 compatible)

			@SuppressWarnings( "unchecked" )
			Map<String, Action> map = Collections.EMPTY_MAP;
			return map;
		}

		return mActionStyle.getActions( clazz );
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
	// Private methods
	//

	private Object traverse( Object toTraverse, String type, boolean onlyToParent, String... names )
		throws InspectorException
	{
		// Validate type

		if ( toTraverse == null )
			return null;

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz == null )
			return null;

		if ( !clazz.isAssignableFrom( toTraverse.getClass() ) )
			return null;

		// Traverse through names

		if ( names == null || names.length == 0 )
			return toTraverse;

		Object traverse = toTraverse;
		Object parentTraverse = null;

		Set<Object> traversed = CollectionUtils.newHashSet();
		traversed.add( traverse );

		for ( String name : names )
		{
			Property property = mPropertyStyle.getProperties( traverse.getClass() ).get( name );

			if ( property == null || !property.isReadable() )
				return null;

			parentTraverse = traverse;
			traverse = property.read( traverse );

			if ( traverse == null )
				break;

			// Unlike BaseXmlInspector (which can never be certain it has detected a
			// cyclic reference because it only looks at types, not objects), BaseObjectInspector
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
