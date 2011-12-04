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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.metawidget.util.ClassUtils;
import org.metawidget.util.simple.StringUtils;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

/**
 * @author Loghman Barari
 */
/*package private*/abstract class TableDataSource<T extends Comparable<T>> extends
		IndexedContainer {

	//
	// Private statics
	//

	private static final long serialVersionUID = 1l;

	//
	// Private members
	//

	private Class<T> mClass;

	private Map<Object, T> mDataSource;

	private List<String> mColumns;

	private List<Class<?>> mColumnsType;

	private List<Object> mColumnFieldOrGetter;

	private Collection<T> mCollection;

	private Collection<T> mRemovedItemCollection;

	//
	// Constructor
	//

	public TableDataSource(Class<T> clazz, Collection<T> collection,
			String... columns) {

		mClass = clazz;
		mColumns = new ArrayList<String>();
		mColumnsType = new ArrayList<Class<?>>();
		mColumnFieldOrGetter = new ArrayList<Object>();
		mDataSource = new java.util.Hashtable<Object, T>();

		if (columns.length > 0) {
			for (String column : columns) {

				try {
					Field field = mClass.getField(column);

					mColumns.add(column);
					mColumnsType.add(field.getType());
					mColumnFieldOrGetter.add(field);

				} catch (SecurityException e) {
				} catch (NoSuchFieldException e) {

					Method method = ClassUtils.getReadMethod(mClass, column);
					if (method != null) {

						mColumns.add(column);
						mColumnsType.add(method.getReturnType());
						mColumnFieldOrGetter.add(method);
					}
				}

			}
		} else {
			for (Field field : mClass.getFields()) {
				mColumns.add(field.getName());
				mColumnsType.add(field.getType());
			}

			for (Method method : mClass.getMethods()) {

				String methodName = method.getName();

				if ((methodName.length() > ClassUtils.JAVABEAN_GET_PREFIX.length())
						&& (method.getReturnType() != null)
						&& (method.getParameterTypes().length == 0)
						&& (methodName.startsWith(ClassUtils.JAVABEAN_GET_PREFIX))) {

					methodName = methodName.substring(ClassUtils.JAVABEAN_GET_PREFIX.length());

					mColumns.add(StringUtils.decapitalize( methodName));
					mColumnsType.add(method.getReturnType());
				}
			}
		}

		for (int i = 0; i < mColumns.size(); i++) {
			String column = mColumns.get(i);
			Class<?> columnClazz = mColumnsType.get(i);
			columnClazz = columnType(column, columnClazz);

			Object defaultValue = null;

			try {

				defaultValue = columnClazz.newInstance();

			} catch (Exception e) {

				defaultValue = null;
			}

			if (columnClazz != null) {
				this.addContainerProperty(column, columnClazz, defaultValue);
			}
		}

		this.importCollection(collection);

		this.mCollection = collection;

		this.mRemovedItemCollection = new ArrayList<T>();
	}

	//
	// Public methods
	//

	public void importCollection(Collection<T> collection) {

		this.removeAllItems();
		this.mDataSource.clear();

		if (collection != null) {
			for (T item : collection) {

				Object itemId = this.addItem();

				for (int i = 0; i < mColumns.size(); i++) {

					String field = mColumns.get(i);
					Object value = null;

					if (mColumnFieldOrGetter.get(i) instanceof Field) {
						try {

							value = ((Field) mColumnFieldOrGetter.get(i))
									.get(item);

						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					} else if (mColumnFieldOrGetter.get(i) instanceof Method) {
						try {

							value = ((Method) mColumnFieldOrGetter.get(i))
									.invoke(item);

						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}

					value = formatValue(item, field, value);

					this.getItem(itemId).getItemProperty(field).setValue(value);
				}

				this.mDataSource.put(itemId, item);
			}

		}
	}

	public abstract Class<?> columnType(String field, Class<?> clazz);

	public abstract Object formatValue(T item, String field, Object value);

	public abstract boolean addItemToCollection(T item) throws RuntimeException;

	public abstract boolean removeItemFromCollection(T item)
			throws RuntimeException;

	public Map<Object, T> getDataSource() {

		return mDataSource;
	}

	public T getDataRow(Object itemId) {
		return this.mDataSource.get(itemId);
	}

	public Object getValue() {

		return mCollection;
	}

	@Override
	public String toString() {
		String val = "";
		for (Object object : getItemIds()) {
			Item item = getItem(object);

			for (Object id : item.getItemPropertyIds()) {
				val += id + ":" + item.getItemProperty(id) + ", ";
			}

			val += "\r\n";
		}
		return val;
	}

	@Override
	public boolean removeItem(Object itemId) {

		T item = this.mDataSource.get(itemId);

		if (item != null) {
			this.mRemovedItemCollection.add(item);

			this.mCollection.remove(itemId);
		}

		return super.removeItem(itemId);
	}

	public void commit() {
		for (Object itemId : getItemIds()) {
			Item item = getItem(itemId);

			boolean newRow = false;

			T data = mDataSource.get(itemId);

			if (data == null) {
				try {
					data = mClass.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();

					continue;
				} catch (IllegalAccessException e) {
					e.printStackTrace();

					continue;
				}

				newRow = true;
			}

			for (int i = 0; i < mColumns.size(); i++) {
				Object newValue = item.getItemProperty(mColumns.get(i))
						.getValue();

				if (mColumnFieldOrGetter.get(i) instanceof Field) {
					Field field = (Field) mColumnFieldOrGetter.get(i);
					try {
						field.set(data, newValue);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				} else {
					Method method = ClassUtils.getWriteMethod(mClass, mColumns
							.get(i), mColumnsType.get(i));
					try {

						method.invoke(data, newValue);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}

			if (newRow) {
				if (!this.addItemToCollection(data)) {
					this.mCollection.add(data);
				}
			}
		}

		for (T item : this.mRemovedItemCollection) {
			this.removeItemFromCollection(item);
		}

	}

}
