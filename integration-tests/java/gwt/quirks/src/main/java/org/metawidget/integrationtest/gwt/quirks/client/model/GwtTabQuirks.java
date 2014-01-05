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

package org.metawidget.integrationtest.gwt.quirks.client.model;

import java.io.Serializable;

import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

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
