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

package org.metawidget.integrationtest.gwt.quirks.client.model;

import java.io.Serializable;

import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;

public class GwtTabQuirks
	implements Serializable {

	//
	// Public methods
	//
	
	public String getAbc() {

		return null;
	}

	public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

		// Do nothing
	}

	@UiSection( { "Foo", "Bar" } )
	public boolean getDef() {

		return false;
	}

	public void setDef( @SuppressWarnings( "unused" ) String def ) {

		// Do nothing
	}

	@UiLarge
	public String getGhi() {

		return null;
	}

	public void setGhi( @SuppressWarnings( "unused" ) String ghi ) {

		// Do nothing
	}

	@UiSection( { "Foo", "Baz" } )
	public String getJkl() {

		return null;
	}

	public void setJkl( @SuppressWarnings( "unused" ) String jkl ) {

		// Do nothing
	}

	@UiSection( { "Foo", "" } )
	public boolean isMno() {

		return false;
	}

	public void setMno( @SuppressWarnings( "unused" ) boolean mno ) {

		// Do nothing
	}

	@UiSection( { "Foo", "Moo" } )
	public String getPqr() {

		return null;
	}

	public void setPqr( @SuppressWarnings( "unused" ) String pqr ) {

		// Do nothing
	}

	@UiSection( "" )
	public String getStu() {

		return null;
	}

	public void setStu( @SuppressWarnings( "unused" ) String stu ) {

		// Do nothing
	}
}
