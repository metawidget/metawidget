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

package org.metawidget.inspector.composite;

import java.io.InputStream;

import org.metawidget.config.iface.NeedsResourceResolver;
import org.metawidget.config.iface.ResourceResolver;
import org.metawidget.config.impl.SimpleResourceResolver;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a CompositeInspector prior to use. Once instantiated, Inspectors are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ValidatingCompositeInspectorConfig
	extends CompositeInspectorConfig
	implements NeedsResourceResolver {

	//
	// Private members
	//

	private String				mSchemaFile	= "org/metawidget/inspector/inspection-result-1.0.xsd";

	private ResourceResolver	mResourceResolver;

	//
	// Public methods
	//

	public void setSchemaFile( String schemaFile ) {

		mSchemaFile = schemaFile;
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

		if ( !ObjectUtils.nullSafeEquals( mSchemaFile, ( (ValidatingCompositeInspectorConfig) that ).mSchemaFile ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mResourceResolver, ( (ValidatingCompositeInspectorConfig) that ).mResourceResolver ) ) {
			return false;
		}

		return super.equals( that );
	}

	@Override
	public int hashCode() {

		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mSchemaFile );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mResourceResolver );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected InputStream getSchema() {

		return getResourceResolver().openResource( mSchemaFile );
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
