package org.metawidget.example.faces.penguincolony.managedbean;

import java.util.Date;

import org.metawidget.inspector.annotation.UiComesAfter;

public class Penguin
{
	//
	// Private members
	//

	private String	mName;

	private Date	mDateOfBirth = new Date();

	//
	// Public methods
	//

	public String getName()
	{
		return mName;
	}

	public void setName( String name )
	{
		mName = name;
	}

	@UiComesAfter( "name" )
	public Date getDateOfBirth()
	{
		return mDateOfBirth;
	}

	public void setDateOfBirth( Date dateOfBirth )
	{
		mDateOfBirth = dateOfBirth;
	}
}
