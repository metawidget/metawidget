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

package org.metawidget.vaadin.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.vaadin.VaadinMetawidget;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;

/**
 * Layout to decorate widgets from different sections using a Label.
 * 
 * @author Loghman Barari
 */

public class SeparatorLayoutDecorator extends VaadinFlatSectionLayoutDecorator {

	//
	// Constructor
	//

	public SeparatorLayoutDecorator() {
		super(
				new LayoutDecoratorConfig<Component, ComponentContainer, VaadinMetawidget>()
						.setLayout(new GridLayout()));
	}

	public SeparatorLayoutDecorator(
			LayoutDecoratorConfig<Component, ComponentContainer, VaadinMetawidget> config) {
		
		super(config);
	}

	//
	// Protected methods
	//

	
	@Override
	protected void addSectionWidget(String section, int level,
			ComponentContainer container, VaadinMetawidget metawidget) {

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey(StringUtils
				.camelCase(section));

		if (localizedSection == null) {
			localizedSection = section;
		}

		String currentActiveSection = metawidget
				.getClientProperty("currentActiveSection");

		if ((currentActiveSection != null)
				&& currentActiveSection.equals(section))
			return;

		Label label = new Separator(localizedSection);

		// Add to parent container

		Map<String, String> separatorPanelAttributes = CollectionUtils
				.newHashMap();
		separatorPanelAttributes.put(WIDE, TRUE);

		getDelegate().layoutWidget(label, PROPERTY, separatorPanelAttributes,
				container, metawidget);

		metawidget.putClientProperty("currentActiveSection", section);
	}

	@SuppressWarnings("serial")
	/* package private */static class Separator extends Label {

		private Separator(String caption) {
			super("<br/><b>" + caption + "</b><hr/>", Label.CONTENT_XHTML);
		}
	}

}
