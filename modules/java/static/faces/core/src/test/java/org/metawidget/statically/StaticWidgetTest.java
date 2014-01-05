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

package org.metawidget.statically;

import junit.framework.TestCase;

import org.metawidget.statically.faces.component.html.layout.HtmlPanelGrid;
import org.metawidget.statically.faces.component.html.layout.HtmlPanelGroup;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlInputText;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlOutputText;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlSelectBooleanCheckbox;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlSelectOneMenu;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StaticWidgetTest
	extends TestCase {

	//
	// Public methods
	//

	@SuppressWarnings( "unchecked" )
	public static void testAdd() {

		// Default

		HtmlOutputText htmlOutputText = new HtmlOutputText();
		assertEquals( null, htmlOutputText.getParent() );

		// Add

		HtmlPanelGrid htmlPanelGrid = new HtmlPanelGrid();
		htmlPanelGrid.getChildren().add( htmlOutputText );
		assertEquals( htmlPanelGrid, htmlOutputText.getParent() );

		// Add at index

		HtmlPanelGroup htmlPanelGroup = new HtmlPanelGroup();
		htmlPanelGroup.getChildren().add( 0, htmlOutputText );
		assertEquals( htmlPanelGroup, htmlOutputText.getParent() );

		// Set at index

		HtmlInputText htmlInputText = new HtmlInputText();
		htmlPanelGroup.getChildren().set( 0, htmlInputText );
		assertEquals( htmlPanelGroup, htmlInputText.getParent() );
		assertEquals( null, htmlOutputText.getParent() );

		// Add all

		HtmlSelectOneMenu htmlSelectOneMenu = new HtmlSelectOneMenu();
		htmlPanelGroup.getChildren().addAll( CollectionUtils.newArrayList( htmlOutputText, htmlSelectOneMenu ) );
		assertEquals( htmlPanelGroup, htmlOutputText.getParent() );
		assertEquals( htmlPanelGroup, htmlSelectOneMenu.getParent() );

		// Add all at index

		HtmlSelectBooleanCheckbox htmlSelectBooleanCheckbox = new HtmlSelectBooleanCheckbox();
		htmlPanelGroup.getChildren().addAll( CollectionUtils.newArrayList( htmlSelectBooleanCheckbox ) );
		assertEquals( htmlPanelGroup, htmlSelectBooleanCheckbox.getParent() );
	}
}
