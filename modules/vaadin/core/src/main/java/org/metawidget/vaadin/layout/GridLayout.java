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

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.vaadin.Facet;
import org.metawidget.vaadin.VaadinMetawidget;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.Reindeer;

/**
 * Layout to arrange widgets.
 *
 *
 *
 * @author Loghman Barari
 */

public class GridLayout implements
		AdvancedLayout<Component, ComponentContainer, VaadinMetawidget> {

	//
	// Private members
	//

	private final int 		mNumberOfColumns;
	private final int 		mNumberOfRows;
	private final boolean 	mAlignCaptionOnLeft;
	private final String	mLabelSuffix;
	//
	// Constructor
	//

	public GridLayout() {

		this(new GridLayoutConfig());
	}

	public GridLayout(GridLayoutConfig config) {

		mNumberOfColumns = config.getNumberOfColumns();
		mNumberOfRows = config.getNumberOfRows();
		mAlignCaptionOnLeft = config.isAlignCaptionOnLeft();
		mLabelSuffix = config.getLabelSuffix();
	}

	//
	// Public methods
	//

	public void onStartBuild(VaadinMetawidget metawidget) {
		if (mNumberOfColumns > 0) {
			metawidget.setColumns(mNumberOfColumns);
		}
	}

	public void startContainerLayout(ComponentContainer container,
			VaadinMetawidget metawidget) {

		// Do nothing
	}

	public void layoutWidget(Component component, String elementName,
			Map<String, String> attributes, ComponentContainer container,
			VaadinMetawidget metawidget) {

		// Add it

		if (container instanceof com.vaadin.ui.GridLayout) {
			com.vaadin.ui.GridLayout gridLayout = (com.vaadin.ui.GridLayout) container;

			int colIndex = gridLayout.getCursorX();
			int rowIndex = gridLayout.getCursorY();

			if (mNumberOfColumns > 0) {
				if (willFillRow(component, attributes)) {

					if (colIndex > 0) {
						rowIndex++;
						if (rowIndex >= gridLayout.getRows()) {
							gridLayout.setRows(rowIndex + 1);
						}
					}

					colIndex = gridLayout.getColumns() - 1;

					gridLayout.addComponent(
							wrapComponent(component, attributes, metawidget),
							0, rowIndex, colIndex++, rowIndex);
				} else {

					gridLayout.addComponent(
							wrapComponent(component, attributes, metawidget),
							colIndex++, rowIndex);
				}

				if (colIndex >= gridLayout.getColumns()) {
					colIndex = 0;
					rowIndex++;

					if (rowIndex >= gridLayout.getRows()) {
						gridLayout.setRows(rowIndex + 1);
					}
				}
			}
			else if (mNumberOfRows > 0) {

				if (gridLayout.getComponent(colIndex, rowIndex) != null) {
					colIndex++;
				}

				if (colIndex >= gridLayout.getColumns()){
						gridLayout.setColumns(colIndex+1);
				}

				gridLayout.addComponent(
						wrapComponent(component, attributes, metawidget),
						colIndex, rowIndex);
			}

			gridLayout.setCursorX(colIndex);
			gridLayout.setCursorY(rowIndex);

		} else {
			container.addComponent( wrapComponent(component,attributes, metawidget) );
		}

	}

	public void endContainerLayout(ComponentContainer container,
			VaadinMetawidget metawidget) {
	}

	public void onEndBuild(VaadinMetawidget metawidget) {

		// Buttons

		Facet buttonsFacet = metawidget.getFacet("buttons");

		if (buttonsFacet != null) {
			metawidget.addComponent(buttonsFacet);
		}
	}

	//
	// Protected methods
	//

	/*
	 * Internal method to wrap all components.
	 * It wraps nested @link VaadinMetawidget components in the com.vaadin.ui.Panel
	 * and others in the com.vaadin.ui.FormLayout (It makes all caption align on the left)
	 */
	protected Component wrapComponent(Component component,
			Map<String, String> attributes, VaadinMetawidget metawidget) {

		if (component instanceof VaadinMetawidget) {

			String labelString = metawidget.getLabelString(attributes);
			if (mLabelSuffix.length() != 0) {
				labelString += mLabelSuffix;
			}

			Panel panel = new Panel(labelString);
			panel.setWidth( "100%" );
			panel.setStyleName(Reindeer.PANEL_LIGHT);
			panel.addComponent( component );

			return panel;
		}

		if (component instanceof Button) {
			return component;
		}

		if((component.getCaption() != null) &&
			component.getCaption().length() != 0 &&
			mLabelSuffix.length() != 0) {

			component.setCaption(component.getCaption() + mLabelSuffix);
		}

		if ( !mAlignCaptionOnLeft ) {
			return component;
		}

		if (component instanceof SeparatorLayoutDecorator.Separator) {
			return component;
		}

		FormLayout layout = new FormLayout();
		layout.setSpacing(false);
		layout.setMargin(false);
		layout.addComponent(component);

		return layout;
	}

	protected boolean willFillRow(Component component,
			Map<String, String> attributes) {

		if (attributes != null) {
			if (TRUE.equals(attributes.get(LARGE))) {
				return true;
			}

			if (TRUE.equals(attributes.get(WIDE))) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return "GridLayout:" + mNumberOfColumns + "*" + mNumberOfRows;
	}

}
