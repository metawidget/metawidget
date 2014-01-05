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

package org.metawidget.inspector.seam;

import java.io.InputStream;

import org.metawidget.config.iface.NeedsResourceResolver;
import org.metawidget.config.iface.ResourceResolver;
import org.metawidget.config.impl.SimpleResourceResolver;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a SeamInspector prior to use. Once instantiated, Inspectors are immutable.
 * <p>
 * Handles specifying <code>components.xml</code> file input.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SeamInspectorConfig
	implements NeedsResourceResolver {

	//
	// Private members
	//

	private ResourceResolver	mResourceResolver;

	private InputStream			mComponentsInputStream;

	//
	// Public methods
	//

	/**
	 * Sets the InputStream of <code>components.xml</code>.
	 *
	 * @return this, as part of a fluent interface
	 */

	public SeamInspectorConfig setComponentsInputStream( InputStream stream ) {

		mComponentsInputStream = stream;

		// Fluent interface

		return this;
	}

	public void setResourceResolver( ResourceResolver resourceResolver ) {

		mResourceResolver = resourceResolver;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that )) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mResourceResolver, ( (SeamInspectorConfig) that ).mResourceResolver ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mComponentsInputStream, ( (SeamInspectorConfig) that ).mComponentsInputStream ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mResourceResolver );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mComponentsInputStream );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected InputStream getComponentsInputStream() {

		if ( mComponentsInputStream != null ) {
			return mComponentsInputStream;
		}

		return getResourceResolver().openResource( "components.xml" );
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

}
