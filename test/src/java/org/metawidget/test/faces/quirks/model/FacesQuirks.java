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

package org.metawidget.test.faces.quirks.model;

import java.util.List;

import javax.faces.model.SelectItem;

import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.faces.UiFacesComponent;
import org.metawidget.inspector.faces.UiFacesLookup;
import org.metawidget.util.CollectionUtils;

/**
 * Models an entity that tests some Faces-specific quirks.
 *
 * @author Richard Kennard
 */

public class FacesQuirks
{
	//
	// Private members
	//

	private Boolean					mBoolean;

	private String					mLarge;

	private List<? extends Object>	mStrings = CollectionUtils.newArrayList();

	private List<Integer>			mIntegers = CollectionUtils.newArrayList();

	//
	// Public methods
	//

	@UiFacesComponent( "javax.faces.HtmlSelectOneRadio" )
	@UiLabel( "#{1+2} boolean" )
	public Boolean getBoolean()
	{
		return mBoolean;
	}

	public void setBoolean( Boolean b )
	{
		mBoolean = b;
	}

	@UiLarge
	@UiLabel( "" )
	@UiComesAfter( "boolean" )
	@UiRequired
	public String getLarge()
	{
		return mLarge;
	}

	public void setLarge( String large )
	{
		mLarge = large;
	}

	@UiComesAfter( "large" )
	@UiLookup( { "Foo", "Bar", "Baz" } )
	public List<? extends Object> getStrings()
	{
		return mStrings;
	}

	public void setStrings( List<? extends Object> strings )
	{
		mStrings = strings;
	}

	@UiComesAfter( "strings" )
	@UiFacesLookup( "#{quirks.possibleIntegers}" )
	public List<Integer> getIntegers()
	{
		return mIntegers;
	}

	public void setIntegers( List<Integer> integers )
	{
		// Check converters are hooked up

		if ( integers != null )
		{
			for( Object integer : integers )
			{
				if ( !( integer instanceof Integer ))
					throw new ClassCastException( integer + " is not of type Integer" );
			}
		}

		mIntegers = integers;
	}

	@UiHidden
	public SelectItem[] getPossibleIntegers()
	{
		return new SelectItem[] { new SelectItem( 1 ), new SelectItem( 2 ), new SelectItem( 3 ), new SelectItem( 4 ) };
	}

	@UiAction
	@UiComesAfter
	public void refresh()
	{
		// Test refreshing the screen
	}
}
