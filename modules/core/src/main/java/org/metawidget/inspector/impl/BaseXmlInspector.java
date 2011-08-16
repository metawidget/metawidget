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

import org.metawidget.config.ResourceResolver;
import org.metawidget.inspector.iface.DomInspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.InspectorUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.LogUtils.Log;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.Pair;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
 * <h2>Mixing XML and Object-based Inspectors</h2>
 * <p>
 * Several pieces of functionality apply to mixing XML-based <code>Inspector</code>s (e.g.
 * <code>XmlInspector</code>) and Object-based <code>Inspector</code>s (e.g.
 * <code>PropertyTypeInspector</code>) in the same application (i.e. via
 * <code>CompositeInspector</code>).
 * <p>
 * First, you may encounter a problem whereby the Object-based <code>Inspector</code>s will always
 * stop at <code>null</code> or recursive references, whereas the XML <code>Inspector</code>s (which
 * have no knowledge of Object values) will continue. This can lead to the
 * <code>WidgetBuilder</code>s constructing a UI for a <code>null</code> Object, which may upset
 * some <code>WidgetProcessor</code>s (e.g. <code>BeansBindingProcessor</code>). To resolve this,
 * you can set <code>BaseXmlInspectorConfig.setRestrictAgainstObject</code>, whereby the XML-based
 * <code>Inspector</code> will do a check for <code>null</code> or recursive references, and not
 * return any XML.
 * <p>
 * Second, by default you need to explicitly specify any inheritance relationships between types in
 * the XML, because the XML has no knowledge of your Java classes. This includes the names of any
 * proxied classes. If this becomes laborious, you can set
 * <code>BaseXmlInspectorConfig.setInferInheritanceHierarchy</code> to infer the relationships
 * automatically from your Java classes. If you are using <code>setRestrictAgainstObject</code>,
 * <code>setInferInheritanceHierarchy</code> is implied.
 * <p>
 * Third, it is important the properties defined by the XML and the ones defined by the Java classes
 * stay in sync. To enforce this, you can set
 * <code>BaseXmlInspectorConfig.setValidateAgainstClasses</code>.
 *
 * @author Richard Kennard
 */

public abstract class BaseXmlInspector
	implements DomInspector<Element> {

	//
	// Protected members
	//

	protected Log				mLog	= LogUtils.getLog( getClass() );

	//
	// Private members
	//

	/**
	 * Shared DOM to store this Inspector's source XML.
	 * <p>
	 * This member is private because, as <a
	 * href="https://issues.apache.org/jira/browse/XERCESJ-727">pointed out here</a>: "There's no
	 * requirement that a DOM be thread safe, so applications need to make sure that threads are
	 * properly synchronized for concurrent access to [a shared] DOM. This is true even if you're
	 * just invoking read operations".
	 */

	private Element				mRoot;

	private final PropertyStyle	mRestrictAgainstObject;

	private final boolean		mInferInheritanceHierarchy;

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

			// inferInheritanceHierarchy

			mInferInheritanceHierarchy = config.isInferInheritanceHierarchy();

			// validateAgainstClasses

			PropertyStyle validateAgainstClasses = config.getValidateAgainstClasses();

			if ( validateAgainstClasses != null ) {

				String topLevelTypeAttribute = getTopLevelTypeAttribute();
				String extendsAttribute = getExtendsAttribute();
				String nameAttribute = getNameAttribute();
				String typeAttribute = getTypeAttribute();

				// For each entity...

				Element entity = XmlUtils.getChildWithAttribute( mRoot, topLevelTypeAttribute );

				while ( entity != null ) {

					// ...the maps to a Java class...

					String topLevelType = entity.getAttribute( topLevelTypeAttribute );
					Class<?> actualClass = ClassUtils.niceForName( topLevelType );

					if ( actualClass != null ) {

						// ...check its extends...

						String extendz = entity.getAttribute( extendsAttribute );
						Class<?> actualSuperclass = actualClass.getSuperclass();

						if ( !"".equals( extendz ) && !extendz.equals( actualSuperclass.getName() ) ) {
							throw InspectorException.newException( actualClass + " extends " + actualSuperclass + ", not '" + extendz + "'" );
						}

						// ...then for each property...

						Map<String, Property> actualProperties = validateAgainstClasses.getProperties( actualClass );
						Element property = XmlUtils.getChildWithAttribute( entity, nameAttribute );

						while ( property != null ) {

							// ...check it exists

							String propertyName = property.getAttribute( nameAttribute );
							Property actualProperty = actualProperties.get( propertyName );

							if ( actualProperty == null ) {
								throw InspectorException.newException( actualClass + " does not define a property '" + propertyName + "'" );
							}

							String propertyType = property.getAttribute( typeAttribute );
							Class<?> actualType = actualProperty.getType();

							if ( !"".equals( propertyType ) && !propertyType.equals( actualType.getName() ) ) {
								throw InspectorException.newException( actualClass + " defines property '" + propertyName + "' to be of " + actualType + ", not '" + propertyType + "'" );
							}

							property = XmlUtils.getSiblingWithAttribute( property, nameAttribute );
						}
					}

					entity = XmlUtils.getSiblingWithAttribute( entity, topLevelTypeAttribute );
				}
			}

		} catch ( Exception e ) {
			throw InspectorException.newException( e );
		}
	}

	//
	// Public methods
	//

	/**
	 * Inspect the given Object according to the given path, and return the result as a String
	 * conforming to inspection-result-1.0.xsd.
	 * <p>
	 * This method is marked <code>final</code> because most Metawidget implementations will call
	 * <code>inspectAsDom</code> directly instead.
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
			Document document;
			Element entity;
			Pair<Element, String> pair;
			Map<String, String> parentAttributes = null;

			// "There's no requirement that a DOM be thread safe, so applications need to make sure
			// that threads are properly synchronized for concurrent access to [a shared] DOM. This
			// is true even if you're just invoking read operations"
			//
			// https://issues.apache.org/jira/browse/XERCESJ-727

			synchronized ( mRoot ) {

				// If the path has a parent...

				if ( names != null && names.length > 0 ) {
					// ...inspect its property for useful attributes...

					Element propertyInParent = traverse( toInspect, type, true, names ).getLeft();

					if ( propertyInParent != null ) {
						parentAttributes = inspectProperty( propertyInParent );
					}
				}

				// ...otherwise, just start at the end point

				pair = traverse( toInspect, type, false, names );

				if ( pair.getLeft() == null ) {

					if ( parentAttributes == null || parentAttributes.isEmpty() ) {
						return null;
					}

					document = XmlUtils.newDocument();
					entity = document.createElementNS( NAMESPACE, ENTITY );

				} else {

					// Inspect traits

					document = XmlUtils.newDocument();
					entity = document.createElementNS( NAMESPACE, ENTITY );
					inspectTraits( pair.getLeft(), entity );

					// Nothing of consequence to return?

					if ( !entity.hasChildNodes() ) {
						return null;
					}
				}
			}

			Element root = document.createElementNS( NAMESPACE, ROOT );
			root.setAttribute( VERSION, "1.0" );
			document.appendChild( root );
			root.appendChild( entity );

			if ( parentAttributes != null ) {

				// Add any parent attributes (including type)

				XmlUtils.setMapAsAttributes( entity, parentAttributes );

			} else {

				// Use the declared type so as to align with other Inspectors

				entity.setAttribute( TYPE, pair.getRight() );
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

	protected void inspectTraits( Element entity, Element toAddTo ) {

		if ( entity == null ) {
			return;
		}

		Document document = toAddTo.getOwnerDocument();

		// Do 'extends' attribute first

		String extendsAttribute = getExtendsAttribute();

		if ( extendsAttribute != null ) {
			if ( entity.hasAttribute( extendsAttribute ) ) {
				inspectTraits( traverse( null, entity.getAttribute( extendsAttribute ), false ).getLeft(), toAddTo );
			}
		}

		// Next, for each child...

		Element element = document.createElementNS( NAMESPACE, ENTITY );
		Element trait = XmlUtils.getFirstChildElement( entity );

		while ( trait != null ) {

			// ...inspect its attributes...

			Element inspectedTrait = inspectTrait( document, trait );

			if ( inspectedTrait != null ) {
				element.appendChild( inspectedTrait );
			}

			trait = XmlUtils.getNextSiblingElement( trait );
		}

		// ...and combine them all. Note the element may already exist from the superclass,
		// and its attributes will get overridden by the subclass

		XmlUtils.combineElements( toAddTo, element, NAME, NAME );
	}

	/**
	 * Inspect the given Element and return a Map of attributes if it is a trait.
	 * <p>
	 * It is this method's responsibility to decide whether the given Element does, in fact, qualify
	 * as a 'trait' - based on its own rules.
	 *
	 * @param toInspect
	 *            DOM element to inspect
	 */

	protected Element inspectTrait( Document toAddTo, Element toInspect ) {

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
	 * <p>
	 * It is this method's responsibility to decide whether the given Element does, in fact, qualify
	 * as a 'property' - based on its own rules. Does nothing by default.
	 *
	 * @param toInspect
	 *            DOM element to inspect
	 * @return a Map of the property's attributes, or null if this Element is not a property
	 */

	protected Map<String, String> inspectProperty( Element toInspect ) {

		return null;
	}

	/**
	 * Inspect the given Element and return a Map of attributes if it is an action.
	 * <p>
	 * It is this method's responsibility to decide whether the given Element does, in fact, qualify
	 * as an 'action' - based on its own rules. Does nothing by default.
	 *
	 * @param toInspect
	 *            DOM element to inspect
	 * @return a Map of the property's attributes, or null if this Element is not an action
	 */

	protected Map<String, String> inspectAction( Element toInspect ) {

		return null;
	}

	/**
	 * @return a tuple of Element (may be null) and declared type (not actual type). Never null
	 */

	protected Pair<Element, String> traverse( Object toTraverse, String type, boolean onlyToParent, String... names ) {

		// If given a non-null Object, use it to restrictAgainstObject

		String typeToInspect = type;
		String[] namesToInspect = names;
		Object traverseAgainstObject = null;
		String declaredType = null;

		// TODO: this is resolving to the DynaForm!!

		if ( toTraverse != null && mRestrictAgainstObject != null ) {
			Pair<Object, Class<?>> pair = InspectorUtils.traverse( mRestrictAgainstObject, toTraverse, typeToInspect, onlyToParent, namesToInspect );
			traverseAgainstObject = pair.getLeft();

			if ( pair.getRight() != null ) {
				declaredType = pair.getRight().getName();
			}

			if ( traverseAgainstObject == null ) {
				return new Pair<Element, String>( null, declaredType );
			}

			// If we are 'onlyToParent' but our value is ultimately going to be null, return null

			if ( onlyToParent ) {
				String lastName = namesToInspect[namesToInspect.length - 1];

				// TODO: test this?

				namesToInspect = new String[] { lastName };
			} else {
				namesToInspect = null;
			}

			typeToInspect = traverseAgainstObject.getClass().getName();
		}

		if ( declaredType == null ) {
			declaredType = typeToInspect;
		}

		// Validate type

		String topLevelTypeAttribute = getTopLevelTypeAttribute();
		Element entityElement = XmlUtils.getChildWithAttributeValue( mRoot, topLevelTypeAttribute, typeToInspect );

		if ( entityElement == null ) {

			if ( traverseAgainstObject == null && !mInferInheritanceHierarchy ) {
				return new Pair<Element, String>( null, declaredType );
			}

			// If using mRestrictAgainstObject or mInferInheritanceHierarchy, attempt to match
			// superclasses by checking against the Java class heirarchy

			Class<?> actualClass;

			if ( traverseAgainstObject != null ) {
				actualClass = traverseAgainstObject.getClass();
			} else {
				actualClass = ClassUtils.niceForName( typeToInspect );

				if ( actualClass == null ) {
					return new Pair<Element, String>( null, typeToInspect );
				}
			}

			while ( entityElement == null && ( actualClass = actualClass.getSuperclass() ) != null ) {

				entityElement = XmlUtils.getChildWithAttributeValue( mRoot, topLevelTypeAttribute, actualClass.getName() );
			}

			if ( entityElement == null ) {
				return new Pair<Element, String>( null, declaredType );
			}
		}

		if ( namesToInspect == null ) {
			return new Pair<Element, String>( entityElement, declaredType );
		}

		int length = namesToInspect.length;

		if ( length == 0 ) {
			return new Pair<Element, String>( entityElement, declaredType );
		}

		// Traverse names

		String extendsAttribute = getExtendsAttribute();
		String nameAttribute = getNameAttribute();
		String typeAttribute = getTypeAttribute();

		for ( int loop = 0; loop < length; loop++ ) {
			String name = namesToInspect[loop];
			Element property = XmlUtils.getChildWithAttributeValue( entityElement, nameAttribute, name );

			if ( property == null ) {
				// XML structure may not support 'extends'

				if ( extendsAttribute == null ) {
					return new Pair<Element, String>( null, null );
				}

				// Property may be defined in an 'extends'

				while ( true ) {
					if ( !entityElement.hasAttribute( extendsAttribute ) ) {
						return new Pair<Element, String>( null, null );
					}

					String childExtends = entityElement.getAttribute( extendsAttribute );
					entityElement = XmlUtils.getChildWithAttributeValue( mRoot, topLevelTypeAttribute, childExtends );

					if ( entityElement == null ) {
						return new Pair<Element, String>( null, null );
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
				return new Pair<Element, String>( property, property.getAttribute( typeAttribute ) );
			}

			if ( !property.hasAttribute( typeAttribute ) ) {
				throw InspectorException.newException( "Property " + name + " in entity " + entityElement.getAttribute( typeAttribute ) + " has no @" + typeAttribute + " attribute in the XML, so cannot navigate to " + type + ArrayUtils.toString( namesToInspect, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) );
			}

			declaredType = property.getAttribute( typeAttribute );
			entityElement = XmlUtils.getChildWithAttributeValue( mRoot, topLevelTypeAttribute, declaredType );

			if ( entityElement == null ) {
				break;
			}
		}

		return new Pair<Element, String>( entityElement, declaredType );
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
	 * decided against it (see http://blog.kennardconsulting.com/2008/01/ask-your-father.html).
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
}
