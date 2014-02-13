// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.swing.widgetbuilder;

import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;

/**
 * Simple implementation of a <code>TableModel</tt> that supports <tt>Collections</tt>s.
 */

class CollectionTableModel<T>
	extends AbstractTableModel {

	//
	// Private members
	//

	private List<T>			mList;

	private List<String>	mColumns;

	private List<String>	mColumnNames;

	//
	// Constructor
	//

	/**
	 * @param columns
	 *            attribute names of the columns (eg. foo, bar)
	 * @param columnNames
	 *            human readable names of the columns (eg. Foo, Bar)
	 */

	public CollectionTableModel( Collection<T> collection, List<String> columns, List<String> columnNames ) {

		if ( collection instanceof List<?> ) {
			mList = (List<T>) collection;
		} else if ( collection != null ) {
			mList = CollectionUtils.newArrayList( collection );
		}
		mColumns = columns;
		mColumnNames = columnNames;
	}

	//
	// Public methods
	//

	public int getColumnCount() {

		return mColumns.size();
	}

	@Override
	public String getColumnName( int columnIndex ) {

		if ( columnIndex >= getColumnCount() ) {
			return null;
		}

		return mColumnNames.get( columnIndex );
	}

	public int getRowCount() {

		if ( mList == null ) {
			return 0;
		}

		return mList.size();
	}

	public T getValueAt( int rowIndex ) {

		if ( rowIndex >= getRowCount() ) {
			return null;
		}

		return mList.get( rowIndex );
	}

	public Object getValueAt( int rowIndex, int columnIndex ) {

		if ( columnIndex >= getColumnCount() ) {
			return null;
		}

		T t = getValueAt( rowIndex );

		if ( t == null ) {
			return null;
		}

		String column = mColumns.get( columnIndex );

		// Special support for toString

		if ( "toString".equals( column )) {
			return t.toString();
		}

		return ClassUtils.getProperty( t, column );
	}
}
