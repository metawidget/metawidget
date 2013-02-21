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

package org.metawidget.statically;

import junit.framework.TestCase;

import org.metawidget.statically.faces.component.html.layout.HtmlPanelGrid;
import org.metawidget.statically.faces.component.html.layout.HtmlPanelGroup;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlInputText;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlOutputText;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlSelectBooleanCheckbox;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlSelectOneMenu;
import org.metawidget.util.CollectionUtils;

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
