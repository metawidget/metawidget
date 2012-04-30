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

import java.util.Collection;
import java.util.Map;

import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;

import com.vaadin.data.util.IndexedContainer;

/**
 * TableDataSource
 *
 * @author Loghman Barari
 */

public class TableDataSource<T extends Comparable<T>>
	extends IndexedContainer {

	//
	// Private members
	//

	private Class<T>		mClass;

	private Map<Object, T>	mDataSource;

	private String[]		mColumns;

	//
	// Constructor
	//

	public TableDataSource( Class<T> clazz, Collection<T> collection, String... columns ) {

		mClass = clazz;
		mColumns = columns;
		mDataSource = CollectionUtils.newHashMap();

		for ( String column : mColumns ) {
			addContainerProperty( column, getColumnType( column ), null );
		}

		importCollection( collection );
	}

	//
	// Public methods
	//

	public void importCollection( Collection<T> collection ) {

		removeAllItems();
		mDataSource.clear();

		if ( collection != null ) {
			for ( T item : collection ) {

				Object itemId = addItem();

				for ( String column : mColumns ) {

					getItem( itemId ).getItemProperty( column ).setValue( getValue( item, column ) );
				}

				mDataSource.put( itemId, item );
			}
		}
	}

	public T getDataRow( Object itemId ) {

		return mDataSource.get( itemId );
	}

	//
	// Protected methods
	//

	protected Class<?> getColumnType( String column ) {

		return ClassUtils.getReadMethod( mClass, column ).getReturnType();
	}

	protected Object getValue( T item, String column ) {

		return ClassUtils.getProperty( item, column );
	}
}
