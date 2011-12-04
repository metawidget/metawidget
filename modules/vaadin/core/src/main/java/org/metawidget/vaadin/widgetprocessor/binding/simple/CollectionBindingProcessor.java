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

package org.metawidget.vaadin.widgetprocessor.binding.simple;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collection;
import java.util.Map;

import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.vaadin.VaadinMetawidget;
import org.metawidget.vaadin.widgetbuilder.VaadinWidgetBuilder.WrapperComponent;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;

/**
 * @author Loghman Barari
 */
public class CollectionBindingProcessor implements
		WidgetProcessor<Component, VaadinMetawidget> {

	protected final static String TABLE_COLUMNS = "tablecolumns";

	private CollectionBindingNotifier collectionBindingNotifier;

	@SuppressWarnings( { "unchecked", "serial" })
	public Component processWidget(Component component, String elementName,
			Map<String, String> attributes, VaadinMetawidget metawidget) {

		AbstractSelect abstractSelect;

		if (component instanceof AbstractSelect) {

			abstractSelect = (AbstractSelect) component;
		} else if ((component instanceof WrapperComponent)
				&& (((WrapperComponent) component)
						.getMasterComponent() instanceof AbstractSelect)) {

			abstractSelect = (AbstractSelect) ((WrapperComponent) component)
					.getMasterComponent();
		} else {
			return component;
		}

		String type = attributes.get(PARAMETERIZED_TYPE);

		if (type == null || "".equals(type)) {
			return component;
		}

		Class<?> componentClass = ClassUtils.niceForName(type);

		// if (componentClass.asSubclass(Comparable.class) == null) {
		// return component;
		// }

		Collection<?> collection = (Collection<?>) ClassUtils.getProperty(
				metawidget.getToInspect(), attributes.get(NAME));

		String[] columns = ArrayUtils.fromString(attributes.get(TABLE_COLUMNS));

		if (columns == null || columns.length == 0) {
			return component;
		}

		TableDataSource tableDataSource = new TableDataSource(componentClass,
				collection, columns) {

			@Override
			public Class<?> columnType(String field, Class clazz) {

				if (collectionBindingNotifier != null) {
					return collectionBindingNotifier.columnType(field, clazz);
				}

				return clazz;
			}

			@Override
			public Object formatValue(Comparable item, String field,
					Object value) {

				if (collectionBindingNotifier != null) {
					return collectionBindingNotifier.formatValue(item, field,
							value);
				}

				return value;
			}

			@Override
			public boolean addItemToCollection(Comparable item) throws RuntimeException {
				if (collectionBindingNotifier != null) {

					collectionBindingNotifier.addItemToCollection(item);
					return true;
				} else {
					return false;
				}
			}

			@Override
			public boolean removeItemFromCollection(Comparable item)
					throws RuntimeException {

				if (collectionBindingNotifier != null) {
					collectionBindingNotifier.removeItemFromCollection(item);
					return true;
				} else {
					return false;
				}
			}
		};

		abstractSelect.setPropertyDataSource(new TableProperty(
				tableDataSource));

		abstractSelect.setContainerDataSource(tableDataSource);

		abstractSelect.setValue(collection);

		return component;

	}

	public CollectionBindingNotifier getCollectionBindingNotifier() {
		return collectionBindingNotifier;
	}

	public void setCollectionBindingNotifier(
			CollectionBindingNotifier collectionBindingNotifier) {

		this.collectionBindingNotifier = collectionBindingNotifier;
	}

	public interface CollectionBindingNotifier {

		Class<?> columnType(String field, Class<?> clazz);

		Object formatValue(Comparable<?> item, String field, Object value);

		void addItemToCollection(Object item) throws RuntimeException;

		void removeItemFromCollection(Object item) throws RuntimeException;

	}

}
