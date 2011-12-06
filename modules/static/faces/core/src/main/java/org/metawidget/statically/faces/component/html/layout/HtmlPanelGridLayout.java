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
import org.metawidget.statically.StaticStub;
import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.faces.component.EditableValueHolder;
import org.metawidget.statically.faces.component.html.HtmlWidget;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlOutputText;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.WidgetBuilderUtils;

/**
 * Layout to arrange widgets using a panelGrid.
 *
 * @author Richard Kennard
 */

public class HtmlPanelGridLayout
	implements AdvancedLayout<StaticXmlWidget, StaticXmlWidget, StaticXmlMetawidget> {

	//
	// Private members
	//

	private final String	mColumnStyleClasses;

	//
	// Constructor
	//

	public HtmlPanelGridLayout() {

		this( new HtmlPanelGridLayoutConfig() );
	}

	public HtmlPanelGridLayout( HtmlPanelGridLayoutConfig config ) {

		mColumnStyleClasses = ArrayUtils.toString( config.getColumnStyleClasses() );
	}

	//
	// Public methods
	//

	public void onStartBuild( StaticXmlMetawidget metawidget ) {

		// Do nothing
	}

	public void startContainerLayout( StaticXmlWidget container, StaticXmlMetawidget metawidget ) {

		try {
			HtmlPanelGrid panelGrid = new HtmlPanelGrid();
			panelGrid.putAttribute( "columns", "3" );
			panelGrid.putAttribute( "id", metawidget.getAttribute( "id" ) );
			panelGrid.putAttribute( "columnClasses", mColumnStyleClasses );
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

			// Support sections

			if ( widget instanceof HtmlSectionOutputText ) {
				panelGrid.getChildren().add( widget );
				panelGrid.getChildren().add( new HtmlOutputText() );
				panelGrid.getChildren().add( new HtmlOutputText() );
				return;
			}

			// Label

			// TODO: recurse into panelGroups looking for an id

			HtmlOutputLabel label = new HtmlOutputLabel();
			String id = widget.getAttribute( "id" );

			if ( id != null ) {
				label.putAttribute( "for", id );
			}
			String labelText = metawidget.getLabelString( attributes );
			label.putAttribute( "value", labelText + ":" );
			panelGrid.getChildren().add( label );

			if ( !( widget instanceof EditableValueHolder ) || metawidget.isReadOnly() || WidgetBuilderUtils.isReadOnly( attributes ) ) {

				// Non-editable

				panelGrid.getChildren().add( widget );

			} else {
				// Group starts

				HtmlPanelGroup panelGroup = new HtmlPanelGroup();
				panelGrid.getChildren().add( panelGroup );

				// Widget

				panelGroup.getChildren().add( widget );

				// Error message

				HtmlMessage message = new HtmlMessage();
				message.putAttribute( "for", id );
				panelGroup.getChildren().add( message );
			}

			// Required star

			HtmlOutputText required = new HtmlOutputText();
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
