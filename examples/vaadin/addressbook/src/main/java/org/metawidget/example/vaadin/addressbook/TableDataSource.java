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

package org.metawidget.example.vaadin.addressbook;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.metawidget.util.ClassUtils;
import org.metawidget.util.simple.StringUtils;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

/**
 * TableDataSource
 *
 *
 *
 * @author Loghman Barari
 */

public abstract class TableDataSource<T extends Comparable<T>>
	extends IndexedContainer implements ItemClickListener {

	//
	// Private statics
	//

	private static final long	serialVersionUID	= 1l;

	//
	// Private members
	//

	private Class<T>			mClass;

	private Map<Object, T>		mDataSource;

	private List<String>		mColumns;

	private List<Class<?>>		mColumnsType;

	private List<Object> 		mColumnFieldOrGetter;



	//
	// Constructor
	//

	public TableDataSource( Class<T> clazz, Collection<T> collection, String... columns ) {

		mClass = clazz;

		mColumns = new ArrayList<String>();
		mColumnsType = new ArrayList<Class<?>>();
		mColumnFieldOrGetter = new ArrayList<Object>();
		mDataSource = new java.util.Hashtable<Object, T>();

		if (columns.length > 0) {
			for (String column : columns) {

				try {
					Field field = mClass.getField( column );

					mColumns.add( column );
					mColumnsType.add( field.getType() );
					mColumnFieldOrGetter.add( field );

				} catch (SecurityException e) {
				} catch (NoSuchFieldException e) {

					Method method = ClassUtils.getReadMethod(mClass, column);
					if (method != null) {

						mColumns.add( column );
						mColumnsType.add( method.getReturnType() );
						mColumnFieldOrGetter.add( method );
					}
				}

			}
		} else {
			for (Field field : mClass.getFields()) {
				mColumns.add( field.getName() );
				mColumnsType.add(field.getType());
			}

			for (Method method : mClass.getMethods()){

				String methodName = method.getName();

				if ((methodName.length() > ClassUtils.JAVABEAN_GET_PREFIX.length()) &&
					(method.getReturnType() != null) &&
					(method.getParameterTypes().length == 0) &&
					(methodName.startsWith(ClassUtils.JAVABEAN_GET_PREFIX))) {

					methodName = methodName.substring(ClassUtils.JAVABEAN_GET_PREFIX.length());
					mColumns.add( StringUtils.decapitalize(methodName) );
					mColumnsType.add( method.getReturnType() );
				}
			}
		}

		for (int i=0; i < mColumns.size(); i++){
			String column = mColumns.get(i);
			Class<?> columnClazz = mColumnsType.get(i);
			columnClazz = columnType(column, columnClazz);

			if (columnClazz != null) {
				addContainerProperty(column, columnClazz , null);
			}
		}

		importCollection( collection );
	}

	//
	// Public methods
	//

	public void importCollection( Collection<T> collection ) {

		this.removeAllItems();
		this.mDataSource.clear();

		if ( collection != null ) {
			for (T item : collection) {

				Object itemId =	this.addItem();

				for(int i = 0; i < mColumns.size(); i++) {

					String field = mColumns.get(i);
					Object value = null;

					if (mColumnFieldOrGetter.get(i) instanceof Field) {
						try {

							value = ((Field)mColumnFieldOrGetter.get(i)).get( item );

						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					} else if (mColumnFieldOrGetter.get(i) instanceof Method) {
						try {

							value = ((Method)mColumnFieldOrGetter.get(i)).invoke(item);

						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}

					value = formatValue(item, field, value);

					this.getItem(itemId).getItemProperty( field ).setValue( value );
				}

				this.mDataSource.put( itemId, item );
			}

		}
	}

	public abstract Class<?> columnType(String field, Class<?> clazz);

	public abstract Object formatValue(T item, String field, Object value);

	public abstract void itemClick(ItemClickEvent event);

	public T getDataRow(Object itemId) {
		return this.mDataSource.get( itemId );
	}

}
