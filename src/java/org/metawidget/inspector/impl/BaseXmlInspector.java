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
import java.util.Set;

import org.metawidget.config.ResourceResolver;
import org.metawidget.inspector.iface.DomInspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.LogUtils.Log;
import org.metawidget.util.XmlUtils;
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
 * <h2>Schema Validation</h2>
 * <p>
 * This class does not support schema validation - it is not that useful in practice for two
 * reasons. First, Inspectors like <code>HibernateInspector</code> cannot use it because they can be
 * pointed at different kinds of files (eg. hibernate.cfg.xml or hibernate-mapping.hbm.xml). Second,
 * Inspectors that are intended for Android environments (eg. <code>XmlInspector</code>) cannot use
 * it because Android's Dalvik preprocessor balks at the unsupported schema classes (even if they're
 * wrapped in a <code>ClassNotFoundException</code>).
 * <p>
 * <h2>Object/Class Mismatch Problem</h2>
 * <p>
 * When mixing XML-based inspectors (eg. <code>XmlInspector</code>) and Object-based Inspectors (eg.
 * <code>PropertyTypeInspector</code>) in the same application (ie. via
 * <code>CompositeInspector</code>) you may encounter a problem if your XML-based Inspectors and
 * your Object-based Inspectors are inspecting the same classes. Specifically, the Object-based
 * Inspectors will always stop at <code>null</code> or recursive Object references, whereas the XML
 * Inspectors (which have no knowledge of Object values) will continue. This can lead to the
 * WidgetBuilders constructing a UI for a null Object, which may upset some WidgetProcessors (eg.
 * <code>BeansBindingProcessor</code>).
 * <p>
 * If you are encountering this particular scenario, you can set <code>restrictAgainstObject</code>,
 * whereby the XML-based Inspector will do a check for <code>null</code> or recursive Object
 * references, and not return any XML.
 *
 * @author Richard Kennard
 */

public abstract class BaseXmlInspector
	implements DomInspector<Element> {

	//
	// Protected members
	//

	protected Log				mLog	= LogUtils.getLog( getClass() );

	protected Element			mRoot;

	//
	// Private members
	//

	private final PropertyStyle	mRestrictAgainstObject;

	//
	// Constructor
	//

	/**
	 * Config-based constructor.
	 * <p>
	 * All BaseXmlInspector inspectors must be configurable, to allow specifying an XML file.
	 */

	protected BaseXmlInspector( BaseXmlInspectorConfig config ) {

		try {
			// Look up the XML file

			InputStream[] files = config.getInputStreams();

			if ( files != null && files.length > 0 ) {
				mRoot = getDocumentElement( config.getResourceResolver(), config.getInputStreams() );
			}

			if ( mRoot == null ) {
				throw InspectorException.newException( "No XML input file specified" );
			}

			// Debug

			if ( mLog.isTraceEnabled() ) {
				mLog.trace( XmlUtils.documentToString( mRoot.getOwnerDocument(), false ) );
			}

			// restrictAgainstObject

			mRestrictAgainstObject = config.getRestrictAgainstObject();
		} catch ( Exception e ) {
			throw InspectorException.newException( e );
		}
	}

	//
	// Public methods
	//

	@Override
	public String inspect( Object toInspect, String type, String... names ) {

		Element element = inspectAsDom( toInspect, type, names );

		if ( element == null ) {
			return null;
		}

		return XmlUtils.nodeToString( element, false );
	}

	@Override
	public Element inspectAsDom( Object toInspect, String type, String... names ) {

		// If no type, return nothing

		if ( type == null ) {
			return null;
		}

		// If restricted against Object, return nothing

		if ( mRestrictAgainstObject != null && isRestrictedAgainstObject( toInspect, type, names ) ) {
			return null;
		}

		try {
			Element elementToInspect = null;
			Map<String, String> parentAttributes = null;

			// If the path has a parent...

			if ( names != null && names.length > 0 ) {
				// ...inspect its property for useful attributes...

				Element propertyInParent = traverse( type, true, names );

				if ( propertyInParent == null ) {
					return null;
				}

				parentAttributes = inspectProperty( propertyInParent );

				String typeAttribute = getTypeAttribute();

				// If the property in the parent has no @type, then we cannot traverse to the child.
				// Even if we wanted to just return the parent attributes, we have no @type to
				// attach to the top-level 'entity' node. So we must fail hard here. If we just
				// return 'null', we may silently ignore parent attributes (such as a lookup)
				//
				// This can cause problems in cases where, say, an XmlInspector (based on classes
				// not objects) and a PropertyTypeInspector (based on objects not classes) both try
				// and codify the same object graph. The PropertyTypeInspector will
				// stop if the child value of a property is null, but the XmlInspector will carry on
				// (because it has no way to know the PropertyTypeInspector has
				// returned null)
				//
				// Note: this rule should never be triggered if the property has the 'dont-expand'
				// attribute set, because we should never try to traverse the child

				if ( !propertyInParent.hasAttribute( typeAttribute ) ) {
					throw InspectorException.newException( "Property " + names[names.length - 1] + " has no @" + typeAttribute + " attribute in the XML, so cannot navigate to " + type + ArrayUtils.toString( names, StringUtils.SEPARATOR_DOT, true, false ) );
				}

				elementToInspect = traverse( propertyInParent.getAttribute( typeAttribute ), false );
			} else {
				// ...otherwise, just start at the end point

				elementToInspect = traverse( type, false, names );

				if ( elementToInspect == null ) {
					return null;
				}
			}

			Document document = XmlUtils.newDocument();
			Element entity = document.createElementNS( NAMESPACE, ENTITY );

			// Inspect properties

			inspect( elementToInspect, entity );

			// Nothing of consequence to return?

			if ( !entity.hasChildNodes() && ( parentAttributes == null || parentAttributes.isEmpty() ) ) {
				return null;
			}

			Element root = document.createElementNS( NAMESPACE, ROOT );
			root.setAttribute( VERSION, "1.0" );
			document.appendChild( root );
			root.appendChild( entity );

			// Add any parent attributes

			if ( parentAttributes == null ) {
				entity.setAttribute( TYPE, type );
			} else {
				XmlUtils.setMapAsAttributes( entity, parentAttributes );
			}

			// Return the root

			return root;
		} catch ( Exception e ) {
			throw InspectorException.newException( e );
		}
	}

	//
	// Protected methods
	//

	/**
	 * Parse the given InputStreams into a single DOM Document, and return its root.
	 *
	 * @param resolver
	 *            helper in case <code>getDocumentElement</code> needs to resolve references defined
	 *            in the <code>InputStream</code>.
	 */

	protected Element getDocumentElement( ResourceResolver resolver, InputStream... files )
		throws Exception {

		Document documentMaster = null;

		for ( InputStream file : files ) {
			Document documentParsed = XmlUtils.parse( file );

			if ( !documentParsed.hasChildNodes() ) {
				continue;
			}

			preprocessDocument( documentParsed );

			if ( documentMaster == null || !documentMaster.hasChildNodes() ) {
				documentMaster = documentParsed;
				continue;
			}

			XmlUtils.combineElements( documentMaster.getDocumentElement(), documentParsed.getDocumentElement(), getTopLevelTypeAttribute(), getNameAttribute() );
		}

		if ( documentMaster == null ) {
			return null;
		}

		return documentMaster.getDocumentElement();
	}

	/**
	 * Hook for subclasses to preprocess the document after the Inspector is initialized.
	 * <p>
	 * For example, <code>HibernateInspector</code> preprocesses the class names in Hibernate
	 * mapping files to make them fully qualified.
	 *
	 * @param document
	 *            DOM of XML being processed
	 */

	protected void preprocessDocument( Document document ) {

		// Do nothing by default
	}

	protected void inspect( Element toInspect, Element toAddTo ) {

		if ( toInspect == null ) {
			return;
		}

		Document document = toAddTo.getOwnerDocument();

		// Do 'extends' attribute first

		String extendsAttribute = getExtendsAttribute();

		if ( extendsAttribute != null ) {
			if ( toInspect.hasAttribute( extendsAttribute ) ) {
				inspect( traverse( toInspect.getAttribute( extendsAttribute ), false ), toAddTo );
			}
		}

		// Next, for each child...

		Element element = document.createElementNS( NAMESPACE, ENTITY );
		NodeList children = toInspect.getChildNodes();

		for ( int loop = 0, length = children.getLength(); loop < length; loop++ ) {
			Node child = children.item( loop );

			if ( !( child instanceof Element ) ) {
				continue;
			}

			// ...inspect its attributes...

			Element inspected = inspect( document, (Element) child );

			if ( inspected == null ) {
				continue;
			}

			element.appendChild( inspected );
		}

		// ...and combine them all. Note the element may already exist from the superclass,
		// and its attributes will get overridden by the subclass

		XmlUtils.combineElements( toAddTo, element, NAME, NAME );
	}

	protected Element inspect( Document toAddTo, Element toInspect ) {

		// Properties

		Map<String, String> propertyAttributes = inspectProperty( toInspect );

		if ( propertyAttributes != null && !propertyAttributes.isEmpty() ) {
			Element child = toAddTo.createElementNS( NAMESPACE, PROPERTY );
			XmlUtils.setMapAsAttributes( child, propertyAttributes );

			return child;
		}

		// Actions

		Map<String, String> actionAttributes = inspectAction( toInspect );

		if ( actionAttributes != null && !actionAttributes.isEmpty() ) {
			// Sanity check

			if ( propertyAttributes != null ) {
				throw InspectorException.newException( "Ambigious match: " + toInspect.getNodeName() + " matches as both a property and an action" );
			}

			Element child = toAddTo.createElementNS( NAMESPACE, ACTION );
			XmlUtils.setMapAsAttributes( child, actionAttributes );

			return child;
		}

		return null;
	}

	/**
	 * Inspect the given Element and return a Map of attributes if it is a property.
	 *
	 * @param toInspect
	 *            DOM element to inspect
	 */

	protected Map<String, String> inspectProperty( Element toInspect ) {

		return null;
	}

	/**
	 * Inspect the given Element and return a Map of attributes if it is an action.
	 *
	 * @param toInspect
	 *            DOM element to inspect
	 */

	protected Map<String, String> inspectAction( Element toInspect ) {

		return null;
	}

	protected Element traverse( String type, boolean onlyToParent, String... names ) {

		// Validate type

		String topLevelTypeAttribute = getTopLevelTypeAttribute();
		Element entityElement = XmlUtils.getChildWithAttributeValue( mRoot, topLevelTypeAttribute, type );

		if ( entityElement == null ) {
			return null;
		}

		if ( names == null ) {
			return entityElement;
		}

		int length = names.length;

		if ( length == 0 ) {
			return entityElement;
		}

		// Traverse names

		String extendsAttribute = getExtendsAttribute();
		String nameAttribute = getNameAttribute();
		String typeAttribute = getTypeAttribute();

		for ( int loop = 0; loop < length; loop++ ) {
			String name = names[loop];
			Element property = XmlUtils.getChildWithAttributeValue( entityElement, nameAttribute, name );

			if ( property == null ) {
				// XML structure may not support 'extends'

				if ( extendsAttribute == null ) {
					return null;
				}

				// Property may be defined in an 'extends'

				while ( true ) {
					if ( !entityElement.hasAttribute( extendsAttribute ) ) {
						return null;
					}

					String childExtends = entityElement.getAttribute( extendsAttribute );
					entityElement = XmlUtils.getChildWithAttributeValue( mRoot, topLevelTypeAttribute, childExtends );

					if ( entityElement == null ) {
						return null;
					}

					property = XmlUtils.getChildWithAttributeValue( entityElement, nameAttribute, name );

					if ( property != null ) {
						break;
					}
				}

				// This check is unnecessary. However it stops a 'potential null pointer access'
				// warning in Eclipse!

				if ( property == null ) {
					throw new NullPointerException( "property" );
				}
			}

			if ( onlyToParent && loop >= ( length - 1 ) ) {
				return property;
			}

			if ( !property.hasAttribute( typeAttribute ) ) {
				throw InspectorException.newException( "Property " + name + " in entity " + entityElement.getAttribute( typeAttribute ) + " has no @" + typeAttribute + " attribute in the XML, so cannot navigate to " + type + ArrayUtils.toString( names, StringUtils.SEPARATOR_DOT, true, false ) );
			}

			String propertyType = property.getAttribute( typeAttribute );
			entityElement = XmlUtils.getChildWithAttributeValue( mRoot, topLevelTypeAttribute, propertyType );

			if ( entityElement == null ) {
				break;
			}

			// Unlike BaseObjectInspector, we cannot test for cyclic references because
			// we're only looking at types, not objects
		}

		return entityElement;
	}

	/**
	 * The attribute on top-level elements that uniquely identifies them.
	 */

	protected String getTopLevelTypeAttribute() {

		return TYPE;
	}

	/**
	 * The attribute on child elements that uniquely identifies them.
	 */

	protected String getNameAttribute() {

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

	protected String getTypeAttribute() {

		return TYPE;
	}

	/**
	 * The attribute on top-level elements that identifies a superclass relationship (if any).
	 */

	protected String getExtendsAttribute() {

		return null;
	}

	//
	// Private methods
	//

	/**
	 * @return true if the type is a Java Class (ie. is not 'Login Screen') and the Object it maps
	 *         to is null or recursive
	 */

	private boolean isRestrictedAgainstObject( Object toTraverse, String type, String... names ) {

		// Special support for class lookup

		if ( toTraverse == null ) {
			// If there are names, return true

			if ( names != null && names.length > 0 ) {
				return true;
			}

			// If no such class, return false

			Class<?> clazz = ClassUtils.niceForName( type );

			if ( clazz == null ) {
				return false;
			}

			return false;
		}

		// Use the toTraverse's ClassLoader, to support Groovy dynamic classes
		//
		// (note: for Groovy dynamic classes, this needs the applet to be signed - I think this is
		// still better than 'relaxing' this sanity check, as that would lead to differing behaviour
		// when deployed as an unsigned applet versus a signed applet)

		Class<?> traverseDeclaredType = ClassUtils.niceForName( type, toTraverse.getClass().getClassLoader() );

		if ( traverseDeclaredType == null || !traverseDeclaredType.isAssignableFrom( toTraverse.getClass() ) ) {
			return false;
		}

		// Traverse through names (if any)

		Object traverse = toTraverse;

		if ( names != null && names.length > 0 ) {
			Set<Object> traversed = CollectionUtils.newHashSet();
			traversed.add( traverse );

			int length = names.length;

			for ( int loop = 0; loop < length; loop++ ) {
				String name = names[loop];
				Property property = mRestrictAgainstObject.getProperties( traverse.getClass() ).get( name );

				if ( property == null || !property.isReadable() ) {
					return true;
				}

				traverse = property.read( traverse );

				// Detect cycles and nip them in the bud

				if ( !traversed.add( traverse ) ) {
					// Trace, rather than do a debug log, because it makes for a nicer 'out
					// of the box' experience

					mLog.trace( "{0} prevented infinite recursion on {1}{2}. Consider marking {3} as hidden=''true''", ClassUtils.getSimpleName( getClass() ), type, ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ), name );
					return true;
				}

				// Always come in this loop once, because we want to do the recursion check

				if ( traverse == null ) {
					return true;
				}

				traverseDeclaredType = property.getType();
			}
		}

		return false;
	}
}
