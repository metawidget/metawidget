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

	//
	// Constructor
	//

	public CollectionTableModel( Collection<T> collection, List<String> columns ) {

		if ( collection instanceof List<?> ) {
			mList = (List<T>) collection;
		} else if ( collection != null ) {
			mList = CollectionUtils.newArrayList( collection );
		}
		mColumns = columns;
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

		return mColumns.get( columnIndex );
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

		return ClassUtils.getProperty( t, getColumnName( columnIndex ) );
	}
}
