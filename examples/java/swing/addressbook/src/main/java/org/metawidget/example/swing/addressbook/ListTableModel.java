// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

package org.metawidget.example.swing.addressbook;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;

/**
 * TableModel for Lists of Objects.
 * <p>
 * As well as wrapping Lists of Objects, <code>ListTableModel</code> supports dynamically adding a
 * blank row to the model to accomodate entering new Objects.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ListTableModel<T extends Comparable<T>>
	extends AbstractTableModel {

	//
	// Private members
	//

	private Class<T>			mClass;

	private List<T>				mList;

	private String[]			mColumns;

	private boolean				mEditable;

	private boolean				mExtraBlankRow;

	//
	// Constructor
	//

	public ListTableModel( Class<T> clazz, Collection<T> collection, String... columns ) {

		mClass = clazz;
		mColumns = columns;

		importCollection( collection );
	}

	//
	// Public methods
	//

	public void importCollection( Collection<T> collection ) {

		if ( collection == null ) {
			mList = CollectionUtils.newArrayList();
		} else {
			mList = CollectionUtils.newArrayList( collection );
			Collections.sort( mList );
		}

		fireTableDataChanged();
	}

	public List<T> exportList() {

		return CollectionUtils.newArrayList( mList );
	}

	public void setEditable( boolean editable ) {

		mEditable = editable;
	}

	public void setExtraBlankRow( boolean extraBlankRow ) {

		mExtraBlankRow = extraBlankRow;
	}

	@Override
	public boolean isCellEditable( int rowIndex, int columnIndex ) {

		return mEditable;
	}

	public int getColumnCount() {

		// (mColumns can never be null)

		return mColumns.length;
	}

	@Override
	public String getColumnName( int columnIndex ) {

		if ( columnIndex >= getColumnCount() ) {
			return null;
		}

		return mColumns[columnIndex];
	}

	public int getRowCount() {

		// (mList can never be null)

		int rows = mList.size();

		if ( mExtraBlankRow ) {
			rows++;
		}

		return rows;
	}

	@Override
	public Class<?> getColumnClass( int columnIndex ) {

		String column = getColumnName( columnIndex );

		if ( column == null ) {
			return null;
		}

		return ClassUtils.getReadMethod( mClass, column ).getReturnType();
	}

	public T getValueAt( int rowIndex ) {

		// Sanity check

		if ( rowIndex >= mList.size() ) {
			return null;
		}

		return mList.get( rowIndex );
	}

	public Object getValueAt( int rowIndex, int columnIndex ) {

		// Sanity check

		if ( columnIndex >= getColumnCount() ) {
			return null;
		}

		// Fetch the object

		T t = getValueAt( rowIndex );

		if ( t == null ) {
			return null;
		}

		// Inspect it

		return ClassUtils.getProperty( t, getColumnName( columnIndex ) );
	}

	@Override
	public void setValueAt( Object value, int rowIndex, int columnIndex ) {

		// Sanity check

		if ( columnIndex >= getColumnCount() ) {
			return;
		}

		// Just-in-time creation

		if ( rowIndex == ( getRowCount() - 1 ) && mExtraBlankRow ) {
			if ( value == null || "".equals( value ) ) {
				return;
			}

			try {
				mList.add( mClass.newInstance() );
				fireTableRowsInserted( rowIndex, rowIndex );
			} catch ( Exception e ) {
				throw new RuntimeException( e );
			}
		}

		// Fetch the object

		T t = getValueAt( rowIndex );

		if ( t == null ) {
			return;
		}

		// Update it

		ClassUtils.setProperty( t, getColumnName( columnIndex ), value );
	}
}
