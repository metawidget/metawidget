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

package org.metawidget.integrationtest.faces.quirks.model.primefaces;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Color;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiAttributes;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiSection;

/**
 * Models an entity that tests some PrimeFaces-specific quirks.
 *
 * @author Richard Kennard
 */

@ManagedBean( name = "primeFacesQuirks" )
@SessionScoped
public class PrimeFacesQuirks {

	//
	// Private members
	//

	private String	mName;

	private int		mSpinner;

	private int		mSlider;

	private Date	mDate;

	private Color	mColor;

	//
	// Public methods
	//

	@UiSection( "Section #1" )
	public String getName() {

		return mName;
	}

	public void setName( String name ) {

		mName = name;
	}

	@UiComesAfter( "name" )
	public int getSpinner() {

		return mSpinner;
	}

	public void setSpinner( int spinner ) {

		mSpinner = spinner;
	}

	@UiComesAfter( "spinner" )
	@UiAttributes( { @UiAttribute( name = MINIMUM_VALUE, value = "0" ), @UiAttribute( name = MAXIMUM_VALUE, value = "10" ) } )
	public int getSlider() {

		return mSlider;
	}

	public void setSlider( int slider ) {

		mSlider = slider;
	}

	@UiSection( "Section #2" )
	@UiComesAfter( "slider" )
	public Color getColor() {

		return mColor;
	}

	public void setColor( Color color ) {

		mColor = color;
	}

	@UiComesAfter( "color" )
	public void setDate( Date date ) {

		mDate = date;
	}

	public Date getDate() {

		return mDate;
	}

	public String save() {

		return "save";
	}

	public String edit() {

		return "edit";
	}
}
