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

package org.metawidget.integrationtest.faces.quirks.model;

import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiSection;

/**
 * Models an entity that tests UIComponentNestedSectionLayoutDecorator-specific quirks.
 * <p>
 * Specifically:
 * <p>
 * <ul>
 *  <li>that UIComponentNestedSectionLayoutDecorator doesn't get confused with identically named tabs</li>
 * </ul>
 *
 * @author Richard Kennard
 */

public class TabQuirks {

	//
	// Public methods
	//

	@UiSection( { "First", "One" } )
	public String getFoo() {

		return null;
	}

	/**
	 * @param foo
	 */

	public void setFoo( String foo ) {

		// Do nothing
	}

	@UiComesAfter( "foo" )
	@UiSection( { "First", "Two" } )
	public String getBar() {

		return null;
	}

	/**
	 * @param bar
	 */

	public void setBar( String bar ) {

		// Do nothing
	}

	@UiComesAfter( "bar" )
	@UiSection( { "First", "One" } )
	public String getBaz() {

		return null;
	}

	/**
	 * @param baz
	 */

	public void setBaz( String baz ) {

		// Do nothing
	}

	@UiComesAfter( "baz" )
	@UiSection( { "Second", "One" } )
	public String getAbc() {

		return null;
	}

	/**
	 * @param abc
	 */

	public void setAbc( String abc ) {

		// Do nothing
	}

	@UiComesAfter( "abc" )
	@UiSection( { "Second", "Two" } )
	public String getDef() {

		return null;
	}

	/**
	 * @param def
	 */

	public void setDef( String def ) {

		// Do nothing
	}

	@UiComesAfter( "def" )
	public String getDef2() {

		return null;
	}

	/**
	 * @param def2
	 */

	public void setDef2( String def2 ) {

		// Do nothing
	}

	@UiComesAfter( "def2" )
	@UiSection( { "Second", "Two" } )
	public String getDef3() {

		return null;
	}

	/**
	 * @param def3
	 */

	public void setDef3( String def3 ) {

		// Do nothing
	}


	@UiComesAfter( "def3" )
	@UiSection( { "First", "Two" } )
	public String getGhi() {

		return null;
	}

	/**
	 * @param ghi
	 */

	public void setGhi( String ghi ) {

		// Do nothing
	}

	@UiComesAfter( "ghi" )
	@UiAction
	public void save() {

		// Do nothing
	}
}
