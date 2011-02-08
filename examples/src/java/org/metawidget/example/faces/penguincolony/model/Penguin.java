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

package org.metawidget.example.faces.penguincolony.model;

import java.util.Date;

import org.metawidget.inspector.InspectionResultConstants;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.faces.UiFacesAttribute;

/**
 * @author Richard Kennard
 */

public class Penguin {

	//
	// Private members
	//

	private String				mName;

	private String				mLocation;

	private String				mSpecies;

	private Date				mDateOfBirth;

	private PenguinCondition	mCondition;

	private String[]			mHobbies;

	private String				mDescribeHobby;

	//
	// Constructor
	//

	public Penguin() {

		// Default constructor
	}

	public Penguin( String name, String species ) {

		mName = name;
		mSpecies = species;
	}

	//
	// Public methods
	//

	@UiRequired
	public String getName() {

		return mName;
	}

	public void setName( String name ) {

		mName = name;
	}

	@UiRequired
	@UiLookup( { "Banded penguin", "Brush-tailed penguin", "Crested penguin", "Great penguin", "Little penguin" } )
	@UiComesAfter( "name" )
	@UiSection( "Summary" )
	public String getSpecies() {

		return mSpecies;
	}

	public void setSpecies( String species ) {

		mSpecies = species;
	}

	@UiComesAfter( "species" )
	public String getLocation() {

		return mLocation;
	}

	public void setLocation( String location ) {

		mLocation = location;
	}

	@UiComesAfter( "location" )
	public Date getDateOfBirth() {

		return mDateOfBirth;
	}

	public void setDateOfBirth( Date dateOfBirth ) {

		mDateOfBirth = dateOfBirth;
	}

	@UiAction
	@UiFacesAttribute( name = InspectionResultConstants.HIDDEN, expression = "#{!empty this.condition}" )
	@UiComesAfter( "dateOfBirth" )
	@UiSection( "Detail" )
	public void addCondition() {

		mCondition = new PenguinCondition();
	}

	@UiFacesAttribute( name = InspectionResultConstants.HIDDEN, expression = "#{empty this.condition}" )
	@UiComesAfter( "dateOfBirth" )
	@UiSection( "Detail" )
	public PenguinCondition getCondition() {

		return mCondition;
	}

	public void setCondition( PenguinCondition condition ) {

		mCondition = condition;
	}

	@UiComesAfter( { "addCondition", "condition" } )
	@UiLookup( { "Dancing", "Fishing", "Mating", "Skiing", "Other" } )
	public String[] getHobbies() {

		return mHobbies;
	}

	public void setHobbies( String[] hobbies ) {

		mHobbies = hobbies;
	}

	@UiLarge
	@UiFacesAttribute( name = InspectionResultConstants.HIDDEN, expression = "#{empty this.hobbies}" )
	@UiComesAfter( "hobbies" )
	public String getDescribeHobby() {

		return mDescribeHobby;
	}

	public void setDescribeHobby( String DescribeHobby ) {

		mDescribeHobby = DescribeHobby;
	}
}
