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

/**
 * Base class for BaseXmlInspectorConfig configurations.
 * <p>
 * Handles specifying XML file and schema file input.
 *
 * @author Richard Kennard
 */

public class BaseXmlInspectorConfig
{
	//
	//
	// Private members
	//
	//

	private String[]		mFiles;

	private InputStream[]	mFileStreams;

	private String			mSchema;

	private InputStream		mSchemaStream;

	//
	//
	// Public methods
	//
	//

	public String[] getFiles()
	{
		return mFiles;
	}

	/**
	 * Sets the location of the XML. Location will be searched using
	 * <code>ResourceUtils.getResource</code>.
	 */

	public void setFile( String file )
	{
		mFiles = new String[]{ file };
	}

	/**
	 * Sets the location of multiple XML files. Locations will be searched using
	 * <code>ResourceUtils.getResource</code>.
	 */

	public void setFiles( String... files )
	{
		mFiles = files;
	}

	public InputStream[] getInputStreams()
	{
		return mFileStreams;
	}

	/**
	 * Sets the InputStream of the XML. If set, <code>setFile</code> is ignored.
	 */

	public void setInputStream( InputStream stream )
	{
		mFileStreams = new InputStream[]{ stream };
	}

	public void setInputStreams( InputStream... streams )
	{
		mFileStreams = streams;
	}

	public String getSchema()
	{
		return mSchema;
	}

	/**
	 * Sets the location of the XML Schema. Location will be searched using
	 * <code>ResourceUtils.getResource</code>.
	 */

	public void setSchema( String schema )
	{
		mSchema = schema;
	}

	public InputStream getSchemaInputStream()
	{
		return mSchemaStream;
	}

	/**
	 * Sets the InputStream of the XML Schema. If set, <code>setSchema</code> is ignored.
	 */

	public void setSchemaInputStream( InputStream schemaStream )
	{
		mSchemaStream = schemaStream;
	}
}
