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
import java.util.List;

import org.metawidget.inspector.ConfigReader2;
import org.metawidget.inspector.ResourceResolver;
import org.metawidget.inspector.ResourceResolvingConfig;

/**
 * Base class for BaseXmlInspectorConfig configurations.
 * <p>
 * Handles specifying XML file input.
 *
 * @author Richard Kennard
 */

public class BaseXmlInspectorConfig
	implements ResourceResolvingConfig
{
	//
	// Private members
	//

	private String				mDefaultFile;

	private ResourceResolver	mResourceResolver;

	private InputStream[]		mFileStreams;

	//
	// Public methods
	//

	/**
	 * Sets the location of the XML.
	 * <p>
	 * The location is resolved to an <code>InputStream</code> by the config using its
	 * <code>ConfigReader</code>.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseXmlInspectorConfig setFile( String file )
	{
		mDefaultFile = null;
		mFileStreams = new InputStream[] { getResourceResolver().openResource( file ) };

		// Fluent interface

		return this;
	}

	/**
	 * Sets the location of multiple XML files.
	 * <p>
	 * The location of each file is resolved to an <code>InputStream</code> by the config using
	 * its <code>ConfigReader</code>.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseXmlInspectorConfig setFiles( String... files )
	{
		mDefaultFile = null;

		int length = files.length;
		mFileStreams = new InputStream[length];

		for ( int loop = 0; loop < length; loop++ )
		{
			mFileStreams[loop] = getResourceResolver().openResource( files[loop] );
		}

		// Fluent interface

		return this;
	}

	/**
	 * Sets the location of multiple XML files. Locations will be searched using
	 * <code>ResourceUtils.getResource</code>.
	 * <p>
	 * This overloaded form of the setter is useful for <code>metawidget.xml</code>.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseXmlInspectorConfig setFiles( List<String> files )
	{
		String[] filesArray = new String[files.size()];
		return setFiles( files.toArray( filesArray ) );
	}

	/**
	 * Sets the InputStream of the XML. If set, <code>setFile</code> is ignored.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseXmlInspectorConfig setInputStream( InputStream stream )
	{
		mDefaultFile = null;
		mFileStreams = new InputStream[] { stream };

		// Fluent interface

		return this;
	}

	public InputStream[] getInputStreams()
	{
		if ( mFileStreams == null && mDefaultFile != null )
			return new InputStream[]{ getResourceResolver().openResource( mDefaultFile ) };

		return mFileStreams;
	}

	public void setInputStreams( InputStream... streams )
	{
		mFileStreams = streams;
	}

	@Override
	public void setResourceResolver( ResourceResolver resourceResolver )
	{
		mResourceResolver = resourceResolver;
	}

	public ResourceResolver getResourceResolver()
	{
		if ( mResourceResolver == null )
			mResourceResolver = new ConfigReader2();

		return mResourceResolver;
	}

	//
	// Protected methods
	//

	protected void setDefaultFile( String defaultFile )
	{
		mDefaultFile = defaultFile;
	}
}
