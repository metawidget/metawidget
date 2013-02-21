// Metawidget (licensed under LGPL)
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

import java.io.InputStream;

import org.metawidget.config.iface.NeedsResourceResolver;
import org.metawidget.config.iface.ResourceResolver;
import org.metawidget.config.impl.SimpleResourceResolver;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Base class for BaseXmlInspectorConfig configurations.
 * <p>
 * Handles specifying XML file input.
 *
 * @author Richard Kennard
 */

// Note: we considered the approach from this blog post...
//
// http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation
//
// ...but ultimately it becomes too cumbersome for the end-user to instantiate configs (see comments
// at the bottom of that blog post)

public class BaseXmlInspectorConfig
	implements NeedsResourceResolver {

	//
	// Private members
	//

	private String				mDefaultFile;

	private ResourceResolver	mResourceResolver;

	private InputStream[]		mInputStreams;

	private PropertyStyle		mRestrictAgainstObject;

	private boolean				mInferInheritanceHierarchy;

	private PropertyStyle		mValidateAgainstClasses;

	//
	// Public methods
	//

	/**
	 * Sets the InputStreams of multiple XML files.
	 * <p>
	 * This method is more advanced than <code>setInputStream</code>, as it combines multiple files,
	 * but it is slightly more cumbersome to configure in <code>metawidget.xml</code>.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseXmlInspectorConfig setInputStreams( InputStream... streams ) {

		mInputStreams = streams;

		return this;
	}

	/**
	 * Sets the InputStream of the XML.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseXmlInspectorConfig setInputStream( InputStream stream ) {

		mDefaultFile = null;
		mInputStreams = new InputStream[] { stream };

		// Fluent interface

		return this;
	}

	public void setResourceResolver( ResourceResolver resourceResolver ) {

		mResourceResolver = resourceResolver;
	}

	/**
	 * Sets the property style used to restrict XML inspection against the given Object. This
	 * applies when mixing XML-based <code>Inspector</code>s (e.g. <code>XmlInspector</code>) and
	 * Object-based <code>Inspector</code>s (e.g. <code>PropertyTypeInspector</code>) in the same
	 * application (i.e. via <code>CompositeInspector</code>).
	 * <p>
	 * You may encounter a problem whereby the Object-based <code>Inspector</code> will always stop
	 * at <code>null</code> or recursive references, whereas the XML <code>Inspector</code> (which
	 * have no knowledge of Object values) will continue. This can lead to the
	 * <code>WidgetBuilder</code>s constructing a UI for a <code>null</code> Object, which may upset
	 * some <code>WidgetProcessor</code>s (e.g. <code>BeansBindingProcessor</code>). To resolve
	 * this, you can set <code>BaseXmlInspectorConfig.setRestrictAgainstObject</code>, whereby the
	 * XML-based <code>Inspector</code> will do a check for <code>null</code> or recursive
	 * references, and not return any XML. In addition, setting <code>restrictAgainstObject</code>
	 * allows the XML <code>Inspector</code> to traverse child relationships and infer their types
	 * using the Object. This saves having to explicitly specify those relationships in the XML.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseXmlInspectorConfig setRestrictAgainstObject( PropertyStyle restrictAgainstObject ) {

		mRestrictAgainstObject = restrictAgainstObject;

		// Fluent interface

		return this;
	}

	/**
	 * Sets whether to infer the inheritance heirarchy of types in the XML by looking them up
	 * against corresponding Java <code>Classes</code>. This saves having to explicitly specify the
	 * inheritance heirarchy in the XML.
	 * <p>
	 * Note this does <em>not</em> infer child relationships. For that, use
	 * <code>setRestrictAgainstObject</code> (which also implies
	 * <code>setInferInheritanceHierarchy</code>).
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseXmlInspectorConfig setInferInheritanceHierarchy( boolean inferInheritanceHierarchy ) {

		mInferInheritanceHierarchy = inferInheritanceHierarchy;

		// Fluent interface

		return this;
	}

	/**
	 * Sets the property style used to validate whether properties defined in the XML match those
	 * defined by the corresponding Java <code>Classes</code>.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseXmlInspectorConfig setValidateAgainstClasses( PropertyStyle validateAgainstClasses ) {

		mValidateAgainstClasses = validateAgainstClasses;

		// Fluent interface

		return this;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mDefaultFile, ( (BaseXmlInspectorConfig) that ).mDefaultFile ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mResourceResolver, ( (BaseXmlInspectorConfig) that ).mResourceResolver ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mInputStreams, ( (BaseXmlInspectorConfig) that ).mInputStreams ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mRestrictAgainstObject, ( (BaseXmlInspectorConfig) that ).mRestrictAgainstObject ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mInferInheritanceHierarchy, ( (BaseXmlInspectorConfig) that ).mInferInheritanceHierarchy ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mValidateAgainstClasses, ( (BaseXmlInspectorConfig) that ).mValidateAgainstClasses ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mDefaultFile );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mResourceResolver );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mInputStreams );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mRestrictAgainstObject );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mInferInheritanceHierarchy );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mValidateAgainstClasses );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected void setDefaultFile( String defaultFile ) {

		mDefaultFile = defaultFile;
	}

	protected InputStream[] getInputStreams() {

		if ( mInputStreams == null && mDefaultFile != null ) {
			return new InputStream[] { getResourceResolver().openResource( mDefaultFile ) };
		}

		return mInputStreams;
	}

	protected ResourceResolver getResourceResolver() {

		if ( mResourceResolver == null ) {

			// Support programmatic configuration (ie. mResourceResolver is specified automatically
			// by ConfigReader when using metawidget.xml, but is generally not set manually when
			// people are creating Inspectors by hand)

			return new SimpleResourceResolver();
		}

		return mResourceResolver;
	}

	protected PropertyStyle getRestrictAgainstObject() {

		return mRestrictAgainstObject;
	}

	protected boolean isInferInheritanceHierarchy() {

		return mInferInheritanceHierarchy;
	}

	protected PropertyStyle getValidateAgainstClasses() {

		return mValidateAgainstClasses;
	}
}
