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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
