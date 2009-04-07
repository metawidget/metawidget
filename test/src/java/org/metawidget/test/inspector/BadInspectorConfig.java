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

package org.metawidget.test.inspector;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Richard Kennard
 */

public class BadInspectorConfig
{
	//
	// Private members
	//

	private List<String>	mListOfStrings;

	private int				mInt;

	private List<Class<?>>	mListOfClasses;

	private boolean			mBoolean;

	private Pattern			mPattern;

	private String			mNull = "not-null";

	private boolean			mFailDuringConstruction;

	//
	// Public methods
	//

	public void setListOfStrings( List<String> listOfStrings )
	{
		mListOfStrings = listOfStrings;
	}

	public List<String> getListOfStrings()
	{
		return mListOfStrings;
	}

	public void setInt( int anInt )
	{
		mInt = anInt;
	}

	public int getInt()
	{
		return mInt;
	}

	public void setListOfClasses( List<Class<?>> listOfClasses )
	{
		mListOfClasses = listOfClasses;
	}

	public List<Class<?>> getListOfClasses()
	{
		return mListOfClasses;
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

	public String getNull()
	{
		return mNull;
	}

	public void setNull( String value )
	{
		mNull = value;
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
