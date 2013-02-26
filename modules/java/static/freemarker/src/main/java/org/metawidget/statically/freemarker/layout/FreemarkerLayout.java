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

package org.metawidget.statically.html.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.StaticXmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.HtmlTable;
import org.metawidget.statically.html.widgetbuilder.HtmlTableBody;
import org.metawidget.statically.html.widgetbuilder.HtmlTableCell;
import org.metawidget.statically.html.widgetbuilder.HtmlTableHeader;
import org.metawidget.statically.html.widgetbuilder.HtmlTableRow;
import org.metawidget.statically.html.widgetbuilder.HtmlTag;
import org.metawidget.util.WidgetBuilderUtils;

/**
 * Layout to arrange widgets using a FreeMarker template.
 * 
 * @author Richard Kennard
 */

public class FreemarkerLayout implements
		AdvancedLayout<StaticXmlWidget, StaticXmlWidget, StaticXmlMetawidget> {

	//
	// Private members
	//

	private String mTableStyle;

	private String mTableStyleClass;

	private String mLabelColumnClass;

	private String mComponentColumnClass;

	private String mRequiredColumnClass;

	//
	// Constructor
	//

	public FreemarkerLayout(FreemarkerLayoutConfig config) {

		mTableStyle = config.getTableStyle();
		mTableStyleClass = config.getTableStyleClass();
		mLabelColumnClass = config.getLabelColumnStyleClass();
		mComponentColumnClass = config.getComponentColumnStyleClass();
		mRequiredColumnClass = config.getRequiredColumnStyleClass();
	}

	//
	// Public methods
	//

	public void onStartBuild(StaticXmlMetawidget metawidget) {

		// Do nothing
	}

	public void startContainerLayout(StaticXmlWidget container,
			StaticXmlMetawidget metawidget) {

		// Do nothing
	}

	public void layoutWidget(StaticXmlWidget widget, String elementName,
			Map<String, String> attributes, StaticXmlWidget container,
			StaticXmlMetawidget metawidget) {

		State state = getState();
		
		state.widgets.add( widget );
		state.widgets.add( attributes );
	}

	public void endContainerLayout(StaticXmlWidget container,
			StaticXmlMetawidget metawidget) {

	}

	public void onEndBuild(StaticXmlMetawidget metawidget) {

		// Do nothing
	}

	//
	// Private methods
	//

	/* package private */State getState(UIComponent metawidget) {

		State state = (State) ((StaticXmlMetawidget) metawidget)
				.getClientProperty(FreemarkerLayout.class);

		if (state == null) {
			state = new State();
			((StaticXmlMetawidget) metawidget).putClientProperty(
					FreemarkerLayout.class, state);
		}

		return state;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */static class State {

		/* package private */List<StaticXmlWidget> widgets = CollectionUtils.newArrayList();

		/* package private */List<Map<String, String>> attributes = CollectionUtils.newArrayList();
	}
}
