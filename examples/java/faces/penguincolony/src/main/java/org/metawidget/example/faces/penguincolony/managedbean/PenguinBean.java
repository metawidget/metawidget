// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

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

public class PenguinBean {

	//
	// Private statics
	//

	private ListDataModel	mAll;

	private Penguin			mPenguinCurrent;

	private boolean			mPopupVisible;

	//
	// Constructor
	//

	public PenguinBean() {

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
	 * ListDataModel itself can be parameterized instead (ie. ListDataModel&lt;Penguin&gt;).
	 */

	@UiAttribute( name = PARAMETERIZED_TYPE, value = "org.metawidget.example.faces.penguincolony.model.Penguin" )
	public ListDataModel getAll() {

		return mAll;
	}

	public void createNew() {

		mPenguinCurrent = new Penguin( "New penguin", "Great penguin" );

		@SuppressWarnings( "unchecked" )
		List<Penguin> list = (List<Penguin>) mAll.getWrappedData();
		list.add( mPenguinCurrent );
		mPopupVisible = true;
	}

	public void edit() {

		mPenguinCurrent = (Penguin) mAll.getRowData();
		mPopupVisible = true;
	}

	@UiHidden
	public Penguin getCurrent() {

		return mPenguinCurrent;
	}

	public void setCurrent( Penguin current ) {

		mPenguinCurrent = current;
	}

	@UiHidden
	public boolean isPopupVisible() {

		return mPopupVisible;
	}

	public void setPopupVisible( boolean popupVisible ) {

		mPopupVisible = popupVisible;
	}

	@UiAction
	public void back() {

		mPopupVisible = false;
	}

	@UiAttribute( name = FACES_IMMEDIATE, value = "true" )
	@UiAction
	@UiComesAfter( "back" )
	public void delete() {

		@SuppressWarnings( "unchecked" )
		List<Penguin> list = (List<Penguin>) mAll.getWrappedData();
		list.remove( mPenguinCurrent );
		mPopupVisible = false;
	}
}
