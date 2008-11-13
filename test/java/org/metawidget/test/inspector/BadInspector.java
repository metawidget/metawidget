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

import java.util.List;
import java.util.regex.Pattern;

import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;

/**
 * @author Richard Kennard
 */

public class BadInspector
	implements Inspector
{
	//
	// Private members
	//

	private List<String>	mListOfStrings;

	private int				mInt;

	private List<Class<?>>	mListOfClasses;

	private boolean			mBoolean;

	private Pattern			mPattern;

	//
	// Constructor
	//

	public BadInspector( BadInspectorConfig config )
	{
		mListOfStrings = config.getListOfStrings();
		mInt = config.getInt();
		mListOfClasses = config.getListOfClasses();
		mBoolean = config.isBoolean();
		mPattern = config.getPattern();
	}

	//
	// Public methods
	//

	public String inspect( Object toInspect, String type, String... names )
		throws InspectorException
	{
		return null;
	}

	public List<String> getListOfStrings()
	{
		return mListOfStrings;
	}

	public int getInt()
	{
		return mInt;
	}

	public List<Class<?>> getListOfClasses()
	{
		return mListOfClasses;
	}

	public boolean isBoolean()
	{
		return mBoolean;
	}

	public Pattern getPattern()
	{
		return mPattern;
	}
}
