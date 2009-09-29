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

import org.metawidget.inspector.NeedsResourceResolver;
import org.metawidget.inspector.ResourceResolver;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.ClassUtils;

/**
 * Base class for BaseXmlInspectorConfig configurations.
 * <p>
 * Handles specifying XML file input.
 *
 * @author Richard Kennard
 */

public class BaseXmlInspectorConfig
	implements NeedsResourceResolver
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

	public InputStream[] getInputStreams()
	{
		if ( mFileStreams == null && mDefaultFile != null )
		{
			if ( mResourceResolver != null )
				return new InputStream[]{ mResourceResolver.openResource( mDefaultFile ) };

			try
			{
				return new InputStream[]{ ClassUtils.openResource( mDefaultFile ) };
			}
			catch( Exception e )
			{
				throw InspectorException.newException( e );
			}
		}

		return mFileStreams;
	}

	/**
	 * Sets the InputStreams of multiple XML files.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseXmlInspectorConfig setInputStreams( InputStream... streams )
	{
		mFileStreams = streams;

		return this;
	}

	/**
	 * Sets the InputStreams of multiple XML files.
	 * <p>
	 * This overloaded form of the setter is useful for <code>metawidget.xml</code>.
	 *
	 * @return this, as part of a fluent interface
	 */

	public BaseXmlInspectorConfig setInputStreams( List<InputStream> inputStreams )
	{
		InputStream[] inputStreamsArray = new InputStream[inputStreams.size()];
		return setInputStreams( inputStreams.toArray( inputStreamsArray ) );
	}

	/**
	 * Sets the InputStream of the XML.
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

	public ResourceResolver getResourceResolver()
	{
		if ( mResourceResolver == null )
		{
			return new ResourceResolver()
			{
				@Override
				public InputStream openResource( String resource )
				{
					try
					{
						return ClassUtils.openResource( resource );
					}
					catch( Exception e )
					{
						throw InspectorException.newException( e );
					}
				}
			};
		}

		return mResourceResolver;
	}

	@Override
	public void setResourceResolver( ResourceResolver resourceResolver )
	{
		mResourceResolver = resourceResolver;
	}

	//
	// Protected methods
	//

	protected void setDefaultFile( String defaultFile )
	{
		mDefaultFile = defaultFile;
	}
}
