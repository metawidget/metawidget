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
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.actionstyle.Action;
import org.metawidget.inspector.impl.actionstyle.ActionStyle;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.LogUtils.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Convenience implementation for Inspectors that inspect Objects.
 * <p>
 * Handles iterating over an Object for properties and actions, and supporting pluggable property
 * and action conventions. Also handles unwrapping an Object wrapped by a proxy library (such as
 * CGLIB or Javassist).
 * <p>
 * <h2>Inspecting classes</h2>
 * <p>
 * In general <code>BaseObjectInspector</code> inspects <em>objects</em>, not classes. It will
 * return null if the object value is null, rather than returning the properties of its class. This
 * is generally what is expected. In particular, <code>WidgetProcessor</code>s such as binding
 * implementations would not expect to be given a list of properties and asked to bind to a null
 * object.
 * <p>
 * However, there is a special concession. If <code>BaseObjectInspector</code> is pointed
 * <em>directly</em> at a type (ie. names == null), it will return properties even if the actual
 * value is null. This is important so we can inspect parameterized types of Collections without
 * having to iterate over and grab the first element in that Collection.
 *
 * @author Richard Kennard
 */

public abstract class BaseObjectInspector
	implements Inspector
{
	//
	// Private members
	//

	private PropertyStyle	mPropertyStyle;

	private ActionStyle		mActionStyle;

	//
	// Protected members
	//

	protected Log			mLog	= LogUtils.getLog( getClass() );

	//
	// Constructors
	//

	protected BaseObjectInspector()
	{
		this( new BaseObjectInspectorConfig() );
	}

	/**
	 * Config-based constructor.
	 * <p>
	 * BaseObjectInspector-derived inspectors should generally support configuration, to allow
	 * configuring property styles and action styles.
	 */

	protected BaseObjectInspector( BaseObjectInspectorConfig config )
	{
		mPropertyStyle = config.getPropertyStyle();
		mActionStyle = config.getActionStyle();
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
			Object childToInspect = null;
			String childName = null;
			Class<?> declaredChildType;
			Map<String, String> parentAttributes = null;

			// If the path has a parent...

			if ( names != null && names.length > 0 )
			{
				// ...inspect its property for useful annotations...

				Object[] tuple = traverse( toInspect, type, true, names );

				if ( tuple == null )
					return null;

				childName = names[names.length - 1];

				// Use the actual, runtime class (tuple[0].getClass()) not the declared class
				// (tuple[1]), in case the declared class is an interface or subclass

				Class<?> parentType = tuple[0].getClass();

				Property propertyInParent = mPropertyStyle.getProperties( parentType ).get( childName );

				if ( propertyInParent == null )
					return null;

				declaredChildType = propertyInParent.getType();

				// ...provided it has a getter

				if ( propertyInParent.isReadable() )
				{
					parentAttributes = inspectTrait( propertyInParent );

					Map<String, String> parentPropertyAttributes = inspectProperty( propertyInParent );

					if ( parentPropertyAttributes != null )
					{
						if ( parentAttributes == null )
							parentAttributes = parentPropertyAttributes;
						else
							parentPropertyAttributes.putAll( parentAttributes );
					}

					childToInspect = propertyInParent.read( tuple[0] );
				}
			}

			// ...otherwise, just start at the end point

			else
			{
				Object[] tuple = traverse( toInspect, type, false );

				if ( tuple == null )
					return null;

				childToInspect = tuple[0];
				declaredChildType = (Class<?>) tuple[1];
			}

			Document document = XmlUtils.newDocumentBuilder().newDocument();
			Element entity = document.createElementNS( NAMESPACE, ENTITY );

			// Inspect child properties

			if ( childToInspect == null || declaredChildType.isPrimitive() )
			{
				XmlUtils.setMapAsAttributes( entity, inspectEntity( declaredChildType, declaredChildType ) );

				// If pointed directly at a type, return properties even
				// if the actual value is null. This is a special concession so
				// we can inspect parameterized types of Collections without having
				// to iterate over and grab the first element in that Collection

				if ( names == null || names.length == 0 )
					inspect( childToInspect, declaredChildType, entity );
			}
			else
			{
				Class<?> actualChildType = childToInspect.getClass();
				XmlUtils.setMapAsAttributes( entity, inspectEntity( declaredChildType, actualChildType ) );
				inspect( childToInspect, actualChildType, entity );
			}

			// Add parent attributes (if any)

			XmlUtils.setMapAsAttributes( entity, parentAttributes );

			// Nothing of consequence to return?

			if ( isInspectionEmpty( entity ) )
				return null;

			// Start a new DOM Document

			Element root = document.createElementNS( NAMESPACE, ROOT );
			root.setAttribute( VERSION, "1.0" );
			document.appendChild( root );
			root.appendChild( entity );

			// If there were parent attributes, we may have a useful child name

			if ( childName != null )
				entity.setAttribute( NAME, childName );

			// Every Inspector needs to attach a type to the entity, so that CompositeInspector can
			// merge it. The type should be the *declared* type, not the *actual* type, as otherwise
			// subtypes (and proxied types) will stop XML and Object-based Inspectors merging back
			// together properly

			entity.setAttribute( TYPE, declaredChildType.getName() );

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
	 *            the object to inspect. May be null
	 * @param clazz
	 *            the class to inspect. If toInspect is not null, will be the actual class of
	 *            toInspect. If toInspect is null, will be the class to lookup
	 */

	protected void inspect( Object toInspect, Class<?> classToInspect, Element toAddTo )
		throws Exception
	{
		Document document = toAddTo.getOwnerDocument();

		// Inspect properties

		for ( Property property : getProperties( classToInspect ).values() )
		{
			Map<String, String> traitAttributes = inspectTrait( property );
			Map<String, String> propertyAttributes = inspectProperty( property );
			Map<String, String> entityAttributes = inspectPropertyAsEntity( property, toInspect );

			if ( ( traitAttributes == null || traitAttributes.isEmpty() ) && ( propertyAttributes == null || propertyAttributes.isEmpty() ) && ( entityAttributes == null || entityAttributes.isEmpty() ) )
				continue;

			Element element = document.createElementNS( NAMESPACE, PROPERTY );
			element.setAttribute( NAME, property.getName() );

			XmlUtils.setMapAsAttributes( element, traitAttributes );
			XmlUtils.setMapAsAttributes( element, propertyAttributes );
			XmlUtils.setMapAsAttributes( element, entityAttributes );

			toAddTo.appendChild( element );
		}

		// Inspect actions

		for ( Action action : getActions( classToInspect ).values() )
		{
			Map<String, String> traitAttributes = inspectTrait( action );
			Map<String, String> actionAttributes = inspectAction( action );

			if ( ( traitAttributes == null || traitAttributes.isEmpty() ) && ( actionAttributes == null || actionAttributes.isEmpty() ) )
				continue;

			Element element = document.createElementNS( NAMESPACE, ACTION );
			element.setAttribute( NAME, action.getName() );

			XmlUtils.setMapAsAttributes( element, traitAttributes );
			XmlUtils.setMapAsAttributes( element, actionAttributes );

			toAddTo.appendChild( element );
		}
	}

	/**
	 * Inspect the given entity's class (<em>not</em> its child properties/actions) and return a Map
	 * of attributes.
	 * <p>
	 * Note: for convenience, this method does not expect subclasses to deal with DOMs and Elements.
	 * Those subclasses wanting more control over these features should override methods higher in
	 * the call stack instead.
	 * <p>
	 * For example usage, see <code>PropertyTypeInspector</code> and <code>Java5Inspector</code>.
	 */

	protected Map<String, String> inspectEntity( Class<?> declaredClass, Class<?> actualClass )
		throws Exception
	{
		return null;
	}

	/**
	 * Inspect the given trait and return a Map of attributes.
	 * <p>
	 * A 'trait' is an interface common to both <code>Property</code> and <code>Action</code>, so
	 * you can override this single method if your annotation is applicable to both. For example,
	 * <code>UiLabel</code>.
	 * <p>
	 * Note: for convenience, this method does not expect subclasses to deal with DOMs and Elements.
	 * Those subclasses wanting more control over these features should override methods higher in
	 * the call stack instead.
	 */

	protected Map<String, String> inspectTrait( Trait trait )
		throws Exception
	{
		return null;
	}

	/**
	 * Inspect the given property and return a Map of attributes.
	 * <p>
	 * Note: for convenience, this method does not expect subclasses to deal with DOMs and Elements.
	 * Those subclasses wanting more control over these features should override methods higher in
	 * the call stack instead.
	 */

	protected Map<String, String> inspectProperty( Property property )
		throws Exception
	{
		return null;
	}

	/**
	 * Inspect the given action and return a Map of attributes.
	 * <p>
	 * Note: for convenience, this method does not expect subclasses to deal with DOMs and Elements.
	 * Those subclasses wanting more control over these features should override methods higher in
	 * the call stack instead.
	 */

	protected Map<String, String> inspectAction( Action action )
		throws Exception
	{
		return null;
	}

	/**
	 * Whether to additionally inspect each child property using <code>inspectEntity</code> from its
	 * class level.
	 * <p>
	 * This can be useful if the property's value defines useful class-level annotations, but it is
	 * expensive (as it requires invoking the property's getter to retrieve the value) so is
	 * <code>false</code> by default.
	 * <p>
	 * For example usage, see <code>PropertyTypeInspector</code> and <code>Java5Inspector</code>.
	 */

	protected boolean shouldInspectPropertyAsEntity( Property property )
	{
		return false;
	}

	//
	// Protected final methods
	//

	protected final Map<String, Property> getProperties( Class<?> clazz )
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

	protected final Map<String, Action> getActions( Class<?> clazz )
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

	//
	// Private methods
	//

	/**
	 * Inspect the given property 'as an entity'.
	 * <p>
	 * This method delegates to <code>inspectEntity</code>.
	 * <p>
	 * If the property is readable and its type is not final, this method first invokes the
	 * property's getter so that it can pass the <em>runtime</em> type of the object to
	 * <code>inspectEntity</code>.
	 */

	private Map<String, String> inspectPropertyAsEntity( Property property, Object toInspect )
		throws Exception
	{
		if ( !shouldInspectPropertyAsEntity( property ) )
			return null;

		Class<?> entityClass = property.getType();

		// Inspect the runtime type
		//
		// Note: it is tempting to provide a less-expensive version of
		// inspectPropertyAsEntity that inspects the property's type without invoking
		// the getter. However that places a burden on the individual Inspector,
		// because what if the field is declared to be of type Object but its
		// actual value is a Boolean?
		//
		// Note: If the type is final (which includes Java primitives) there is no
		// need to call the getter because there cannot be a subtype

		if ( property.isReadable() && !Modifier.isFinal( entityClass.getModifiers() ) )
		{
			Object propertyValue = null;

			try
			{
				propertyValue = property.read( toInspect );
			}
			catch ( Throwable t )
			{
				// By definition, a 'getter' method should not affect the state
				// of the object, so it should not fail. However, sometimes a getter's
				// implementation may rely on another object being in a certain state (eg.
				// JSF's DataModel.getRowData) - in which case it will not be readable.
				// We therefore treat value as 'null', so that at least we inspect the type
			}

			if ( propertyValue != null )
				entityClass = propertyValue.getClass();
		}

		// Delegate to inspectEntity

		return inspectEntity( property.getType(), entityClass );
	}

	/**
	 * Returns true if the inspection returned nothing of consequence. This is an optimization that
	 * allows our <code>Inspector</code> to return <code>null</code> overall, rather than creating
	 * and serializing an XML document, which <code>CompositeInspector</code> then deserializes and
	 * merges, all for no meaningful content.
	 *
	 * @return true if the inspection is 'empty'
	 */

	private boolean isInspectionEmpty( Element elementEntity )
	{
		if ( elementEntity.hasAttributes() )
			return false;

		if ( elementEntity.hasChildNodes() )
			return false;

		return true;
	}

	/**
	 * @return If found, a tuple of Object and declared type (not actual type). If not found,
	 *         returns null.
	 */

	private Object[] traverse( Object toTraverse, String type, boolean onlyToParent, String... names )
	{
		// Special support for class lookup

		if ( toTraverse == null )
		{
			Class<?> clazz = ClassUtils.niceForName( type );

			if ( clazz == null )
				return null;

			return new Object[] { null, clazz };
		}

		// Use the toTraverse's ClassLoader, to support Groovy dynamic classes
		//
		// (note: for Groovy dynamic classes, this needs the applet to be signed - I think this is
		// still better than 'relaxing' this sanity check, as that would lead to differing behaviour
		// when deployed as an unsigned applet versus a signed applet)

		Class<?> traverseDeclaredType = ClassUtils.niceForName( type, toTraverse.getClass().getClassLoader() );

		if ( traverseDeclaredType == null || !traverseDeclaredType.isAssignableFrom( toTraverse.getClass() ) )
			return null;

		// Traverse through names (if any)

		Object traverse = toTraverse;

		if ( names != null && names.length > 0 )
		{
			Set<Object> traversed = CollectionUtils.newHashSet();
			traversed.add( traverse );

			int length = names.length;

			for ( int loop = 0; loop < length; loop++ )
			{
				String name = names[loop];
				Property property = mPropertyStyle.getProperties( traverse.getClass() ).get( name );

				if ( property == null || !property.isReadable() )
					return null;

				Object parentTraverse = traverse;
				traverse = property.read( traverse );

				// Unlike BaseXmlInspector (which can never be certain it has detected a
				// cyclic reference because it only looks at types, not objects),
				// BaseObjectInspector can detect cycles and nip them in the bud

				if ( !traversed.add( traverse ) )
				{
					// Fail silently, rather than do...
					//
					// LogUtils.getLog( getClass() ).debug( ClassUtils.getSimpleName( getClass() ) +
					// " prevented infinite recursion on " + type + ArrayUtils.toString( names,
					// StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) + ". Consider annotating "
					// + name + " as @UiHidden" );
					//
					// ...because it makes for a nicer 'out of the box' experience

					return null;
				}

				// Always come in this loop once, even if onlyToParent, because we
				// want to do the recursion check

				if ( onlyToParent && loop >= length - 1 )
					return new Object[] { parentTraverse, traverseDeclaredType };

				if ( traverse == null )
					return null;

				traverseDeclaredType = property.getType();
			}
		}

		return new Object[] { traverse, traverseDeclaredType };
	}
}
