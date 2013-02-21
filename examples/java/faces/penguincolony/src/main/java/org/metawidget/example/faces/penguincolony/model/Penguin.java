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

package org.metawidget.example.faces.penguincolony.model;

import java.util.Date;

import org.metawidget.inspector.InspectionResultConstants;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.util.ArrayUtils;

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
	@UiAttribute( name = InspectionResultConstants.HIDDEN, value = "#{!empty _this.condition}" )
	@UiComesAfter( "dateOfBirth" )
	@UiSection( "Detail" )
	public void addCondition() {

		mCondition = new PenguinCondition();
	}

	@UiAttribute( name = InspectionResultConstants.HIDDEN, value = "#{empty _this.condition}" )
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

		mHobbies = new String[hobbies.length];
		System.arraycopy( hobbies, 0, mHobbies, 0, hobbies.length );
	}

	@UiLarge
	@UiAttribute( name = InspectionResultConstants.HIDDEN, value = "#{!_this.otherHobby}" )
	@UiComesAfter( "hobbies" )
	public String getDescribeHobby() {

		return mDescribeHobby;
	}

	public void setDescribeHobby( String DescribeHobby ) {

		mDescribeHobby = DescribeHobby;
	}

	@UiHidden
	public boolean isOtherHobby() {

		return ArrayUtils.contains( mHobbies, "Other" );
	}
}
