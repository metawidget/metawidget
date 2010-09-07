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

import java.io.InputStream;

import org.metawidget.config.ConfigReader;
import org.metawidget.config.ResourceResolver;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Base class for BaseXmlInspectorConfig configurations.
 * <p>
 * Handles specifying XML file input.
 *
 * @author Richard Kennard
 */

public class BaseXmlInspectorConfig {

	//
	// Private members
	//

	private String				mDefaultFile;

	private ResourceResolver	mResourceResolver;

	private InputStream[]		mInputStreams;

	private PropertyStyle		mRestrictAgainstObject;

	//
	// Constructor
	//

	/**
	 * @param resourceResolver
	 *            ResourceResolver to use to locate XML files. May be null.
	 */

	public BaseXmlInspectorConfig( ResourceResolver resourceResolver ) {

		mResourceResolver = resourceResolver;
	}

	//
	// Public methods
	//

	public InputStream[] getInputStreams() {

		if ( mInputStreams == null && mDefaultFile != null ) {
			if ( mResourceResolver != null ) {
				return new InputStream[] { mResourceResolver.openResource( mDefaultFile ) };
			}

			// Support programmatic configuration (ie. mResourceResolver is specified automatically
			// by ConfigReader when using metawidget.xml, but is generally not set manually when
			// people are creating Inspectors by hand)

			try {
				return new InputStream[] { ClassUtils.openResource( mDefaultFile ) };
			} catch ( Exception e ) {
				throw InspectorException.newException( e );
			}
		}

		return mInputStreams;
	}

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

	public ResourceResolver getResourceResolver() {

		if ( mResourceResolver == null ) {
			mResourceResolver = new ConfigReader();
		}

		return mResourceResolver;
	}

	/**
	 * Gets the style used to restrict against <code>null</code> or recursive Objects (see
	 * BaseXmlInspector JavaDoc)
	 */

	public PropertyStyle getRestrictAgainstObject() {

		return mRestrictAgainstObject;
	}

	/**
	 * Sets the style used to restrict against <code>null</code> or recursive Objects (see
	 * BaseXmlInspector JavaDoc)
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseXmlInspectorConfig setRestrictAgainstObject( PropertyStyle restrictAgainstObject ) {

		mRestrictAgainstObject = restrictAgainstObject;

		// Fluent interface

		return this;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( that == null ) {
			return false;
		}

		if ( getClass() != that.getClass() ) {
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

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mDefaultFile );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mResourceResolver );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mInputStreams );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mRestrictAgainstObject );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected void setDefaultFile( String defaultFile ) {

		mDefaultFile = defaultFile;
	}
}
