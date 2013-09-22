// Metawidget (licensed under LGPL)
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

package org.metawidget.integrationtest.faces.quirks.model.richfaces4;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiAttributes;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.faces.UiFacesSuggest;
import org.metawidget.util.CollectionUtils;

/**
 * Models an entity that tests some RichFaces 4.x specific quirks.
 * <p>
 * Note: this bean is <em>RequestScoped</em>, to test some tricky scenarios about maintaining values
 * upon POST-back.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@ManagedBean( name = "richFacesQuirks" )
@RequestScoped
public class RichFaces4Quirks {

	//
	// Private members
	//

	private String	mName;

	private int		mSpinner;

	private int		mSlider;

	private Date	mDate;

	private String	mSuggestion;

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

	/**
	 * Deliberately defined on the setter.
	 */

	@UiSection( "Section #2" )
	@UiComesAfter( "slider" )
	public void setDate( Date date ) {

		mDate = date;
	}

	public Date getDate() {

		return mDate;
	}

	@UiComesAfter( "date" )
	@UiFacesSuggest( "#{richFacesQuirks.suggestions}" )
	public void setSuggestion( String suggestion ) {

		mSuggestion = suggestion;
	}

	public String getSuggestion() {

		return mSuggestion;
	}

	public List<String> suggestions( String prefix ) {

		return CollectionUtils.newArrayList( prefix + "foo", prefix + "bar", prefix + "baz" );
	}

	public String save() {

		return "save";
	}

	public String edit() {

		return "edit";
	}
}
