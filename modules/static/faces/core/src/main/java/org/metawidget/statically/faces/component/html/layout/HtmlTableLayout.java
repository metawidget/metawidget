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

package org.metawidget.statically.faces.component.html.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.faces.component.StaticStub;
import org.metawidget.statically.faces.component.html.HtmlWidget;

/**
 * Static support: Java Server Faces layouts.
 *
 * @author Richard Kennard
 */

public class HtmlTableLayout
	implements AdvancedLayout<StaticXmlWidget, StaticXmlWidget, StaticXmlMetawidget> {

	//
	// Public methods
	//

	public void onStartBuild( StaticXmlMetawidget metawidget ) {

		// Do nothing
	}

	public void startContainerLayout( StaticXmlWidget container, StaticXmlMetawidget metawidget ) {

		try {
			HtmlWidget panelGrid = new HtmlWidget( "panelGrid" );
			panelGrid.putAttribute( "columns", "3" );
			panelGrid.putAttribute( "id", metawidget.getAttribute( "id" ) );
			container.getChildren().add( panelGrid );
		} catch ( Exception e ) {
			throw LayoutException.newException( e );
		}
	}

	public void layoutWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticXmlWidget container, StaticXmlMetawidget metawidget ) {

		try {
			// Ignore stubs

			if ( widget instanceof StaticStub ) {
				return;
			}

			HtmlWidget panelGrid = (HtmlWidget) container.getChildren().get( 0 );

			// Label

			HtmlWidget label = new HtmlWidget( "outputLabel" );
			String id = widget.getAttribute( "id" );

			if ( id != null ) {
				label.putAttribute( "for", id );
			}
			String labelText = metawidget.getLabelString( attributes );
			label.putAttribute( "value", labelText + ":" );
			panelGrid.getChildren().add( label );

			// Group starts

			HtmlWidget panelGroup = new HtmlWidget( "panelGroup" );
			panelGrid.getChildren().add( panelGroup );

			// Widget

			panelGroup.getChildren().add( widget );

			// Error message

			HtmlWidget message = new HtmlWidget( "message" );
			message.putAttribute( "for", id );
			panelGroup.getChildren().add( message );

			// Required star

			HtmlWidget required = new HtmlWidget( "outputText" );
			if ( TRUE.equals( attributes.get( REQUIRED ) ) ) {
				required.putAttribute( "value", "*" );
			}
			panelGrid.getChildren().add( required );

		} catch ( Exception e ) {
			throw LayoutException.newException( e );
		}
	}

	public void endContainerLayout( StaticXmlWidget container, StaticXmlMetawidget metawidget ) {

		// Do nothing
	}

	public void onEndBuild( StaticXmlMetawidget metawidget ) {

		// Do nothing
	}
}
