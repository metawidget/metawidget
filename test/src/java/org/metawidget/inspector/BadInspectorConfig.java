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

package org.metawidget.inspector;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Richard Kennard
 */

public class BadInspectorConfig
{
	//
	// Private members
	//

	private List<Object>	mList;

	private Set<Object>		mSet;

	private int				mInt;

	private boolean			mBoolean;

	private Pattern			mPattern;

	private InputStream		mInputStream;

	private ResourceBundle	mResourceBundle;

	private boolean			mFailDuringConstruction;

	//
	// Public methods
	//

	public void setList( List<Object> list )
	{
		mList = list;
	}

	public List<Object> getList()
	{
		return mList;
	}

	public void setSet( Set<Object> set )
	{
		mSet = set;
	}

	public Set<Object> getSet()
	{
		return mSet;
	}

	public void setInt( int anInt )
	{
		mInt = anInt;
	}

	public int getInt()
	{
		return mInt;
	}

	public boolean isBoolean()
	{
		return mBoolean;
	}

	public void setBoolean( boolean aBoolean )
	{
		mBoolean = aBoolean;
	}

	public Pattern getPattern()
	{
		return mPattern;
	}

	public void setPattern( Pattern pattern )
	{
		mPattern = pattern;
	}

	public InputStream getInputStream()
	{
		return mInputStream;
	}

	public void setInputStream( InputStream inputStream )
	{
		mInputStream = inputStream;
	}

	public ResourceBundle getResourceBundle()
	{
		return mResourceBundle;
	}

	public void setResourceBundle( ResourceBundle resourceBundle )
	{
		mResourceBundle = resourceBundle;
	}

	public void setFailDuringConstruction( boolean failDuringConstruction )
	{
		mFailDuringConstruction = true;
	}

	public boolean isFailDuringConstruction()
	{
		return mFailDuringConstruction;
	}

	public void setDate( Date date )
	{
		// Test unsupported types
	}

	public void setNoParameters()
	{
		throw new UnsupportedOperationException( "Called setNoParameters" );
	}
}
