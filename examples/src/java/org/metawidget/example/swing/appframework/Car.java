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

package org.metawidget.example.swing.appframework;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.application.Action;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.commons.jexl.UiJexlAttribute;

/**
 * @author Richard Kennard
 */

public class Car
{
	//
	// Private members
	//

	private String					mMake;

	private String					mType;

	private Owner					mOwner;

	private List<ActionListener>	mActionListeners	= new ArrayList<ActionListener>();

	//
	// Public methods
	//

	public String getMake()
	{
		return mMake;
	}

	public void setMake( String make )
	{
		mMake = make;
	}

	@UiComesAfter( "make" )
	@UiLookup( { "Sport", "Hatch", "Wagon" } )
	public String getType()
	{
		return mType;
	}

	public void setType( String type )
	{
		mType = type;
	}

	@UiJexlAttribute( name = HIDDEN, expression = "this.owner == null" )
	@UiComesAfter( "type" )
	public Owner getOwner()
	{
		return mOwner;
	}

	public void setOwner( Owner owner )
	{
		mOwner = owner;
	}

	@Action( name = "add" )
	@UiJexlAttribute( name = HIDDEN, expression = "this.owner != null" )
	public void addOwner()
	{
		mOwner = new Owner();
		fireActionEvent( "addOwner" );
	}

	public void addActionListener( ActionListener listener )
	{
		mActionListeners.add( listener );
	}

	public void removeActionListener( ActionListener listener )
	{
		mActionListeners.remove( listener );
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		if ( mMake != null )
		{
			builder.append( mMake );
		}

		if ( mType != null )
		{
			if ( builder.length() > 0 )
			{
				builder.append( " " );
			}

			builder.append( mType );
		}

		if ( mOwner != null )
		{
			if ( builder.length() == 0 )
			{
				builder.append( "Owned by " );
			}
			else
			{
				builder.append( ", owned by " );
			}

			builder.append( mOwner );
		}

		if ( builder.length() == 0 )
		{
			builder.append( "(no car specified)" );
		}

		return builder.toString();
	}

	//
	// Protected methods
	//

	protected void fireActionEvent( String command )
	{
		ActionEvent event = new ActionEvent( this, 0, command );

		for ( ActionListener actionListener : mActionListeners )
		{
			actionListener.actionPerformed( event );
		}
	}
}
