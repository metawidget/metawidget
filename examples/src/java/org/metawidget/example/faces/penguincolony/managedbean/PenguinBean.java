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

package org.metawidget.example.faces.penguincolony.managedbean;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.metawidget.example.faces.penguincolony.model.Penguin;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard
 */

// Note: we SuppressWarnings( "unchecked" ) in a few places so that we can compile against JSF 2.0
// but still run against JSF 1.2
//
public class PenguinBean
{
	//
	// Private statics
	//

	@SuppressWarnings( "unchecked" )
	private ListDataModel	mAll;

	private Penguin			mPenguinCurrent;

	private boolean			mPopupVisible;

	//
	// Constructor
	//

	@SuppressWarnings( "unchecked" )
	public PenguinBean()
	{
		List<Penguin> all = CollectionUtils.newArrayList();
		all.add( new Penguin( "Mumble", "Great penguin" ) );
		all.add( new Penguin( "Tux", "Little penguin" ) );

		mAll = new ListDataModel( all );
	}

	//
	// Public methods
	//

	/**
	 * Returns all penguins in the colony.
	 * <p>
	 * Note: this getter is annotated with PARAMETERIZED_TYPE for JSF 1.2. As of JSF 2.0,
	 * ListDataModel itself can be parameterized instead (ie. ListDataModel<Penguin>)
	 */

	@SuppressWarnings( "unchecked" )
	@UiAttribute( name = PARAMETERIZED_TYPE, value = "org.metawidget.example.faces.penguincolony.model.Penguin" )
	public ListDataModel getAll()
	{
		return mAll;
	}

	public void createNew()
	{
		mPenguinCurrent = new Penguin( "New penguin", "Great penguin" );

		@SuppressWarnings( "unchecked" )
		List<Penguin> list = (List<Penguin>) mAll.getWrappedData();
		list.add( mPenguinCurrent );
		mPopupVisible = true;
	}

	public void edit()
	{
		mPenguinCurrent = (Penguin) mAll.getRowData();
		mPopupVisible = true;
	}

	@UiHidden
	public Penguin getCurrent()
	{
		return mPenguinCurrent;
	}

	public void setCurrent( Penguin PenguinCurrent )
	{
		mPenguinCurrent = PenguinCurrent;
	}

	@UiHidden
	public boolean isPopupVisible()
	{
		return mPopupVisible;
	}

	public void setPopupVisible( boolean popupVisible )
	{
		mPopupVisible = popupVisible;
	}

	@UiAction
	public void back()
	{
		mPopupVisible = false;
	}

	@UiAttribute( name = FACES_IMMEDIATE, value = "true" )
	@UiAction
	@UiComesAfter( "back" )
	public void delete()
	{
		@SuppressWarnings( "unchecked" )
		List<Penguin> list = (List<Penguin>) mAll.getWrappedData();
		list.remove( mPenguinCurrent );
		mPopupVisible = false;
	}
}
