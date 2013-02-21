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

package org.metawidget.example.swing.appframework;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.application.Action;
import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLookup;

/**
 * @author Richard Kennard
 */

public class Car {

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

	public String getMake() {

		return mMake;
	}

	public void setMake( String make ) {

		mMake = make;
	}

	@UiComesAfter( "make" )
	@UiLookup( { "Sport", "Hatch", "Wagon" } )
	public String getType() {

		return mType;
	}

	public void setType( String type ) {

		mType = type;
	}

	@UiAttribute( name = HIDDEN, value = "${this.owner == null}" )
	@UiComesAfter( "type" )
	public Owner getOwner() {

		return mOwner;
	}

	public void setOwner( Owner owner ) {

		mOwner = owner;
	}

	@Action( name = "add" )
	@UiAttribute( name = HIDDEN, value = "${this.owner != null}" )
	public void addOwner() {

		mOwner = new Owner();
		fireActionEvent( "addOwner" );
	}

	public void addActionListener( ActionListener listener ) {

		mActionListeners.add( listener );
	}

	public void removeActionListener( ActionListener listener ) {

		mActionListeners.remove( listener );
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		if ( mMake != null ) {
			builder.append( mMake );
		}

		if ( mType != null ) {
			if ( builder.length() > 0 ) {
				builder.append( " " );
			}

			builder.append( mType );
		}

		if ( mOwner != null ) {
			if ( builder.length() == 0 ) {
				builder.append( "Owned by " );
			} else {
				builder.append( ", owned by " );
			}

			builder.append( mOwner );
		}

		if ( builder.length() == 0 ) {
			builder.append( "(no car specified)" );
		}

		return builder.toString();
	}

	//
	// Protected methods
	//

	protected void fireActionEvent( String command ) {

		ActionEvent event = new ActionEvent( this, 0, command );

		for ( ActionListener actionListener : mActionListeners ) {
			actionListener.actionPerformed( event );
		}
	}
}
