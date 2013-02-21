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

package org.metawidget.inspector.composite;

import java.io.InputStream;

import org.metawidget.config.iface.NeedsResourceResolver;
import org.metawidget.config.iface.ResourceResolver;
import org.metawidget.config.impl.SimpleResourceResolver;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a CompositeInspector prior to use. Once instantiated, Inspectors are immutable.
 *
 * @author Richard Kennard
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
