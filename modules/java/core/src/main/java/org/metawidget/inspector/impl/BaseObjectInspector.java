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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Map;

import org.metawidget.inspector.iface.DomInspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.actionstyle.Action;
import org.metawidget.inspector.impl.actionstyle.ActionStyle;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.inspector.impl.propertystyle.ValueAndDeclaredType;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.LogUtils.Log;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Convenience implementation for Inspectors that inspect Objects.
 * <p>
 * Handles iterating over an Object for properties and actions, and supporting pluggable property
 * and action conventions.
 * <p>
 * <h3>Inspecting classes</h3>
 * <p>
 * In general <code>BaseObjectInspector</code> inspects <em>objects</em>, not classes. It will
 * return null if the object value is null, rather than returning the properties of its class. This
 * is generally what is expected. In particular, <code>WidgetProcessor</code>s such as binding
 * implementations would not expect to be given a list of properties and asked to bind to a null
 * object.
 * <p>
 * However, there are special concessions:
 * <p>
 * <ol>
 * <li>If <code>BaseObjectInspector</code> is pointed <em>directly</em> at a type (ie. names ==
 * null), it will return properties even if the actual value is null. This is important so we can
 * inspect parameterized types of Collections without having to iterate over and grab the first
 * element in that Collection.</li>
 * <li>Because object traversal is managed by the <code>PropertyStyle</code>, some support class
 * traversal (eg. <code>StaticPropertyStyle</code>)</li>
 * </ol>
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class BaseObjectInspector
	implements DomInspector<Element> {

	//
	// Protected members
	//

	protected final Log			mLog	= LogUtils.getLog( getClass() );

	//
	// Private members
	//

	private final PropertyStyle	mPropertyStyle;

	private final ActionStyle	mActionStyle;

	//
	// Constructors
	//

	protected BaseObjectInspector() {

		this( new BaseObjectInspectorConfig() );
	}

	/**
	 * Config-based constructor.
	 * <p>
	 * BaseObjectInspector-derived inspectors should generally support configuration, to allow
	 * configuring property styles and action styles.
	 */

	protected BaseObjectInspector( BaseObjectInspectorConfig config ) {

		mPropertyStyle = config.getPropertyStyle();
		mActionStyle = config.getActionStyle();
	}

	//
	// Public methods
	//

	/**
	 * Inspect the given Object according to the given path, and return the result as a String
	 * conforming to inspection-result-1.0.xsd.
	 * <p>
	 * This method is marked <code>final</code> because most Metawidget implementations will call
	 * <code>inspectAsDom</code> directly instead. So subclasses need to override
	 * <code>inspectAsDom</code>, not <code>inspect</code>.
	 */

	public final String inspect( Object toInspect, String type, String... names ) {

		Element element = inspectAsDom( toInspect, type, names );

		if ( element == null ) {
			return null;
		}

		return XmlUtils.nodeToString( element, false );
	}

	public Element inspectAsDom( Object toInspect, String type, String... names ) {

		// If no type, return nothing

		if ( type == null ) {
			return null;
		}

		try {
			Object childToInspect;
			String childName;
			String declaredChildType;
			Map<String, String> parentAttributes;
			boolean abortTraversingPastNull = false;

			if ( toInspect != null ) {
				ClassUtils.registerAlienClassLoader( toInspect.getClass().getClassLoader() );
			}

			// If the path has a parent...

			if ( names != null && names.length > 0 ) {

				// ...inspect its property for useful annotations

				ValueAndDeclaredType valueAndDeclaredType = mPropertyStyle.traverse( toInspect, type, true, names );

				String parentType = valueAndDeclaredType.getDeclaredType();

				// If parentType is null, the mPropertyStyle does not want us to continue

				if ( parentType == null ) {
					return null;
				}

				// If possible use the actual class rather than the declared class, in case
				// the declared class is an interface or superclass.
				//
				// Parent can be null if we are just traversing Classes (i.e. StaticPropertyStyle)

				Object parent = valueAndDeclaredType.getValue();

				if ( parent != null ) {
					parentType = parent.getClass().getName();
				}

				childName = names[names.length - 1];
				Property propertyInParent = mPropertyStyle.getProperties( parentType ).get( childName );

				// If the parent does not define such a property, cannot continue. Note Inspectors
				// should fail gracefully if they cannot find what they are told to inspect. They
				// rely on other Inspectors (via CompositeInspector) to find the metadata

				if ( propertyInParent == null ) {
					return null;
				}

				declaredChildType = propertyInParent.getType();
				parentAttributes = inspectParent( parent, propertyInParent );

				// Now step forward to the usual end of the path

				if ( parent == null || !propertyInParent.isReadable() ) {
					childToInspect = null;
				} else {
					childToInspect = propertyInParent.read( parent );

					// Stop if childToInspect==null, given we know names.length > 0
					//
					// If we are inspecting Objects, we never want to traverse past a null. If we
					// are just inspecting Classes (i.e. StaticPropertyStyle) we will never come in
					// here because parent==null

					if ( childToInspect == null ) {
						abortTraversingPastNull = true;
					} else {
						ClassUtils.registerAlienClassLoader( childToInspect.getClass().getClassLoader() );
					}
				}
			} else {

				// ...otherwise, just start at the end point

				childToInspect = toInspect;
				childName = null;
				declaredChildType = type;
				parentAttributes = null;

				// Proceed even if childToInspect==null, given we know names.length==0
				//
				// If pointed directly at a type, we return properties even if the toInspect is
				// null. This is a special concession so we can inspect parameterized types of
				// Collections without having to iterate over and grab the first element in that
				// Collection
			}

			Document document = XmlUtils.newDocument();
			Element entity = document.createElementNS( NAMESPACE, ENTITY );

			// Inspect child properties

			String actualChildType;

			if ( childToInspect == null || ClassUtils.isPrimitive( declaredChildType ) ) {
				actualChildType = declaredChildType;
			} else {
				actualChildType = childToInspect.getClass().getName();
			}

			XmlUtils.setMapAsAttributes( entity, inspectEntity( declaredChildType, actualChildType ) );

			if ( !abortTraversingPastNull ) {
				inspectTraits( childToInspect, actualChildType, entity );
			}

			// Add parent attributes (if any)

			XmlUtils.setMapAsAttributes( entity, parentAttributes );

			// Nothing of consequence to return?

			if ( isInspectionEmpty( entity ) ) {
				return null;
			}

			// Start a new DOM Document

			Element root = document.createElementNS( NAMESPACE, ROOT );
			root.setAttribute( VERSION, "1.0" );
			document.appendChild( root );
			root.appendChild( entity );

			// If there were parent attributes, we may have a useful child name

			if ( childName != null ) {
				entity.setAttribute( NAME, childName );
			}

			// Every Inspector needs to attach a type to the entity, so that CompositeInspector can
			// merge it. The type should be the *declared* type, not the *actual* type, as otherwise
			// subtypes will stop XML and Object-based Inspectors merging back together properly

			entity.setAttribute( TYPE, declaredChildType );

			// Return the document

			return root;
		} catch ( Exception e ) {
			throw InspectorException.newException( e );
		}
	}

	//
	// Protected methods
	//

	/**
	 * Inspect the parent property leading to the <code>toInspect</code>. Often the parent property
	 * contains useful annotations, such as <code>UiLookup</code>.
	 * <p>
	 * This method can be overridden by clients wishing to modify the parent inspection process.
	 *
	 * @param parentToInspect
	 *            the parent to inspect. Can be null (eg. when using static inspection). May be
	 *            useful to clients in determining the context of the subsequent
	 *            <tt>inspectTrait</tt> and <tt>inspectProperty</tt> calls (eg.
	 *            they relate to the parent, not the normal toInspect)
	 * @param propertyInParent
	 *            the property in the parent that points to the original toInspect
	 */

	protected Map<String, String> inspectParent( Object parentToInspect, Property propertyInParent )
		throws Exception {

		Map<String, String> traitAttributes = inspectTrait( propertyInParent );
		Map<String, String> propertyAttributes = inspectProperty( propertyInParent );

		if ( traitAttributes == null ) {
			return propertyAttributes;
		}

		if ( propertyAttributes == null ) {
			return traitAttributes;
		}

		traitAttributes.putAll( propertyAttributes );
		return traitAttributes;
	}

	/**
	 * Inspect the <code>toInspect</code> for properties and actions.
	 * <p>
	 * This method can be overridden by clients wishing to modify the inspection process. Most
	 * clients will find it easier to override one of the sub-methods, such as
	 * <code>inspectTrait</code> or <code>inspectProperty</code>.
	 *
	 * @param toInspect
	 *            the object to inspect. May be null
	 * @param clazz
	 *            the class to inspect. If toInspect is not null, will be the actual class of
	 *            toInspect. If toInspect is null, will be the class to lookup
	 */

	protected void inspectTraits( Object toInspect, String type, Element toAddTo )
		throws Exception {

		Document document = toAddTo.getOwnerDocument();

		// Inspect properties

		for ( Property property : getProperties( type ).values() ) {
			Map<String, String> traitAttributes = inspectTrait( property );
			Map<String, String> propertyAttributes = inspectProperty( property );
			Map<String, String> entityAttributes = inspectPropertyAsEntity( property, toInspect );

			if ( ( traitAttributes == null || traitAttributes.isEmpty() ) && ( propertyAttributes == null || propertyAttributes.isEmpty() ) && ( entityAttributes == null || entityAttributes.isEmpty() ) ) {
				continue;
			}

			Element element = document.createElementNS( NAMESPACE, PROPERTY );
			element.setAttribute( NAME, property.getName() );

			XmlUtils.setMapAsAttributes( element, traitAttributes );
			XmlUtils.setMapAsAttributes( element, propertyAttributes );
			XmlUtils.setMapAsAttributes( element, entityAttributes );

			toAddTo.appendChild( element );
		}

		// Inspect actions

		for ( Action action : getActions( type ).values() ) {
			Map<String, String> traitAttributes = inspectTrait( action );
			Map<String, String> actionAttributes = inspectAction( action );

			if ( ( traitAttributes == null || traitAttributes.isEmpty() ) && ( actionAttributes == null || actionAttributes.isEmpty() ) ) {
				continue;
			}

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
	 * Does nothing by default. For example usage, see <code>PropertyTypeInspector</code>.
	 *
	 * @param declaredClass
	 *            the class passed to <code>inspect</code>, or the class declared by the Object's
	 *            parent (ie. its getter method)
	 * @param actualClass
	 *            the actual class of the Object. If you are searching for annotations, generally
	 *            you should inspect actualClass rather than declaredClass
	 */

	protected Map<String, String> inspectEntity( String declaredClass, String actualClass )
		throws Exception {

		return null;
	}

	/**
	 * Inspect the given trait and return a Map of attributes.
	 * <p>
	 * A 'trait' is an interface common to both <code>Property</code> and <code>Action</code>, so
	 * you can override this single method if your annotation is applicable to both. For example,
	 * <code>UiLabel</code>.
	 * <p>
	 * In the event of an overlap between the attributes set by <code>inspectTrait</code> and those
	 * set by <code>inspectProperty</code>/<code>inspectAction</code>, the latter will receive
	 * precedence.
	 * <p>
	 * Note: for convenience, this method does not expect subclasses to deal with DOMs and Elements.
	 * Those subclasses wanting more control over these features should override methods higher in
	 * the call stack instead.
	 * <p>
	 * Does nothing by default.
	 *
	 * @param trait
	 *            the trait to inspect
	 */

	protected Map<String, String> inspectTrait( Trait trait )
		throws Exception {

		return null;
	}

	/**
	 * Inspect the given property and return a Map of attributes.
	 * <p>
	 * Note: for convenience, this method does not expect subclasses to deal with DOMs and Elements.
	 * Those subclasses wanting more control over these features should override methods higher in
	 * the call stack instead.
	 * <p>
	 * Does nothing by default.
	 *
	 * @param property
	 *            the property to inspect
	 */

	protected Map<String, String> inspectProperty( Property property )
		throws Exception {

		return null;
	}

	/**
	 * Inspect the given action and return a Map of attributes.
	 * <p>
	 * Note: for convenience, this method does not expect subclasses to deal with DOMs and Elements.
	 * Those subclasses wanting more control over these features should override methods higher in
	 * the call stack instead.
	 * <p>
	 * Does nothing by default.
	 *
	 * @param action
	 *            the action to inspect
	 */

	protected Map<String, String> inspectAction( Action action )
		throws Exception {

		return null;
	}

	/**
	 * Whether to additionally inspect each child property using <code>inspectEntity</code> from its
	 * object level.
	 * <p>
	 * This can be useful if the property's value defines useful class-level semantics (eg.
	 * <classname>TYPE</classname>), but it is expensive (as it requires invoking the property's
	 * getter to retrieve the value) so is <code>false</code> by default.
	 * <p>
	 * Returns <code>false</code> by default. For example usage, see
	 * <code>PropertyTypeInspector</code>.
	 *
	 * @param property
	 *            the property to inspect
	 */

	protected boolean shouldInspectPropertyAsEntity( Property property ) {

		return false;
	}

	//
	// Protected final methods
	//

	protected final Map<String, Property> getProperties( String type ) {

		if ( mPropertyStyle == null ) {
			return Collections.emptyMap();
		}

		return mPropertyStyle.getProperties( type );
	}

	protected final Map<String, Action> getActions( String type ) {

		if ( mActionStyle == null ) {
			return Collections.emptyMap();
		}

		return mActionStyle.getActions( type );
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
		throws Exception {

		if ( !shouldInspectPropertyAsEntity( property ) ) {
			return null;
		}

		String actualType = property.getType();

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

		if ( toInspect != null ) {
			Class<?> actualClass = ClassUtils.niceForName( actualType );

			if ( property.isReadable() && ( actualClass == null || !Modifier.isFinal( actualClass.getModifiers() ) ) ) {
				Object propertyValue = null;

				try {
					propertyValue = property.read( toInspect );
				} catch ( Exception e ) {
					// By definition, a 'getter' method should not affect the state
					// of the object, so it should not fail. However, sometimes a getter's
					// implementation may rely on another object being in a certain state (eg.
					// JSF's DataModel.getRowData) - in which case it will not be readable.
					// We therefore treat value as 'null', so that at least we inspect the type
				}

				if ( propertyValue != null ) {
					actualType = propertyValue.getClass().getName();
				}
			}
		}

		// Delegate to inspectEntity

		return inspectEntity( property.getType(), actualType );
	}

	/**
	 * Returns true if the inspection returned nothing of consequence. This is an optimization that
	 * allows our <code>Inspector</code> to return <code>null</code> overall, rather than creating
	 * and serializing an XML document, which <code>CompositeInspector</code> then deserializes and
	 * merges, all for no meaningful content.
	 *
	 * @return true if the inspection is 'empty'
	 */

	private boolean isInspectionEmpty( Element elementEntity ) {

		if ( elementEntity.hasAttributes() ) {
			return false;
		}

		if ( elementEntity.hasChildNodes() ) {
			return false;
		}

		return true;
	}
}
