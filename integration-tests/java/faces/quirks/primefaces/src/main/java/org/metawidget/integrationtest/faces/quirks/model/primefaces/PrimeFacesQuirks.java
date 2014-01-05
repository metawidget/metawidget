// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.integrationtest.faces.quirks.model.primefaces;

import static org.metawidget.inspector.InspectionResultConstants.*;

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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
