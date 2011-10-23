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

import java.util.Map;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.faces.StaticStub;
import org.metawidget.statically.faces.component.html.HtmlWidget;

/**
 * Static support: Java Server Faces layouts.
 *
 * @author Richard Kennard
 */

public class HtmlTableLayout
	implements AdvancedLayout<StaticXmlWidget, StaticXmlWidget, StaticXmlMetawidget> {

	//
	// Private statics
	//

	private static final String	LABEL_SUFFIX	= "-label";

	//
	// Public methods
	//

	public void onStartBuild( StaticXmlMetawidget metawidget ) {

		// Do nothing
	}

	public void startContainerLayout( StaticXmlWidget container, StaticXmlMetawidget metawidget ) {

		try {
			HtmlWidget panelGrid = new HtmlWidget( "panelGrid" );
			panelGrid.putAttribute( "columns", "2" );
			panelGrid.putAttribute( "id", metawidget.getAttribute( "id" ) );
			panelGrid.writeStartTag( metawidget.getWriter() );
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

			// Label

			String id = widget.getAttribute( "id" );

			if ( id != null ) {
				HtmlWidget label = new HtmlWidget( "outputLabel" );
				label.putAttribute( "id", id + LABEL_SUFFIX );
				label.putAttribute( "for", id );
				String labelText = metawidget.getLabelString( attributes );
				label.putAttribute( "value", labelText + ":" );
				label.write( metawidget.getWriter() );
			}

			// Widget

			widget.write( metawidget.getWriter() );

			// Spacer (because vanilla h:panelGrid cannot span)

			if ( id == null ) {
				HtmlWidget spacer = new HtmlWidget( "outputText" );
				spacer.write( metawidget.getWriter() );
			}
		} catch ( Exception e ) {
			throw LayoutException.newException( e );
		}
	}

	public void endContainerLayout( StaticXmlWidget container, StaticXmlMetawidget metawidget ) {

		try {
			HtmlWidget panelGrid = new HtmlWidget( "panelGrid" );
			panelGrid.writeEndTag( metawidget.getWriter() );
		} catch ( Exception e ) {
			throw LayoutException.newException( e );
		}
	}

	public void onEndBuild( StaticXmlMetawidget metawidget ) {

		// Do nothing
	}
}
