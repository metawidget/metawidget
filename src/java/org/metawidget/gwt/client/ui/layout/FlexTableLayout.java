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

package org.metawidget.gwt.client.ui.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.layout.iface.Layout;
import org.metawidget.layout.impl.LayoutUtils;
import org.metawidget.util.simple.StringUtils;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * Layout to arrange widgets in a <code>FlexTable</code>, with one column for labels and another for
 * the widget.
 *
 * @author Richard Kennard
 */

public class FlexTableLayout
	implements Layout<Widget, GwtMetawidget>
{
	//
	// Private statics
	//

	private final static int	LABEL_AND_COMPONENT_AND_REQUIRED	= 3;

	//
	// Private members
	//

	private int					mNumberOfColumns;

	private String				mTableStyleName;

	private String[]			mColumnStyleNames;

	private String				mSectionStyleName;

	private String				mFooterStyleName;

	//
	// Constructor
	//

	public FlexTableLayout()
	{
		this( new FlexTableLayoutConfig() );
	}

	public FlexTableLayout( FlexTableLayoutConfig config )
	{
		mNumberOfColumns = config.getNumberOfColumns();
		mTableStyleName = config.getTableStyleName();
		mColumnStyleNames = config.getColumnStyleNames();
		mSectionStyleName = config.getSectionStyleName();
		mFooterStyleName = config.getFooterStyleName();
	}

	//
	// Public methods
	//

	@Override
	public void onStartBuild( GwtMetawidget metawidget )
	{
		metawidget.putClientProperty( FlexTableLayout.class, null );

		State state = getState( metawidget );
		FlexTable flexTable = new FlexTable();
		flexTable.setStyleName( mTableStyleName );
		metawidget.add( flexTable );

		state.formatter = flexTable.getFlexCellFormatter();
	}

	public Widget layoutChild( Widget widget, String elementName, Map<String, String> attributes, GwtMetawidget metawidget )
	{
		// Do not render empty stubs

		if ( widget instanceof Stub && ( (Stub) widget ).getWidget() == null )
			return null;

		// Section headings

		State state = getState( metawidget );
		if ( attributes != null )
		{
			String section = attributes.get( SECTION );

			if ( section != null && !section.equals( state.currentSection ) )
			{
				state.currentSection = section;
				layoutSection( section, metawidget );
			}
		}

		// Calculate row and actualColumn. Note FlexTable doesn't work quite as might be
		// expected. Specifically, it doesn't understand 'colspan' in relation to previous rows. So
		// if you do...
		//
		// layout.setWidget( row, 0, widget1 );
		// layout.setColSpan( row, 0, 2 );
		// layout.setWidget( row, 2, widget2 );
		//
		// ...you'll actually get...
		//
		// <td colspan="2">widget1</td>
		// <td/>
		// <td>widget2</td>
		//
		// ...note FlexTable inserts an extra <td/> because it thinks column 1 is missing. Therefore
		// the actualColumn is always just the next column, regardless of what state.mCurrentColumn
		// is

		int actualColumn;
		FlexTable flexTable = (FlexTable) metawidget.getWidget( 0 );
		int row = flexTable.getRowCount();

		int numberOfColumns = getEffectiveNumberOfColumns( metawidget );

		if ( state.currentColumn < numberOfColumns && row > 0 )
		{
			row--;
			actualColumn = flexTable.getCellCount( row );
		}
		else
		{
			state.currentColumn = 0;
			actualColumn = 0;
		}

		// Label

		String labelText = metawidget.getLabelString( attributes );

		if ( LayoutUtils.needsLabel( labelText, elementName ))
		{
			Label label = new Label( labelText + ":" );

			String styleName = getStyleName( state.currentColumn * LABEL_AND_COMPONENT_AND_REQUIRED, metawidget );

			if ( styleName != null )
				state.formatter.setStyleName( row, actualColumn, styleName );

			flexTable.setWidget( row, actualColumn, label );
		}

		// Widget

		// Widget column (null labels get collapsed, blank Strings get preserved)

		if ( labelText != null )
		{
			// Zero-column layouts need an extra row

			if ( numberOfColumns == 0 )
			{
				state.currentColumn = 0;
				actualColumn = 0;
				row++;
			}
			else
			{
				actualColumn++;
			}
		}

		String styleName = getStyleName( ( state.currentColumn * LABEL_AND_COMPONENT_AND_REQUIRED ) + 1, metawidget );

		if ( styleName != null )
			state.formatter.setStyleName( row, actualColumn, styleName );

		flexTable.setWidget( row, actualColumn, widget );

		// Colspan

		int colspan;

		// Metawidgets and large components span all columns

		if ( widget instanceof GwtMetawidget || LayoutUtils.isSpanAllColumns( attributes ) )
		{
			colspan = ( numberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED ) - 2;
			state.currentColumn = numberOfColumns;

			if ( labelText == null )
				colspan++;

			// Metawidgets span the required column too

			if ( widget instanceof GwtMetawidget )
				colspan++;
		}

		// Components without labels span two columns

		else if ( labelText == null )
		{
			colspan = 2;
		}

		// Everyone else spans just one

		else
		{
			colspan = 1;
		}

		if ( colspan > 1 )
			state.formatter.setColSpan( row, actualColumn, colspan );

		// Required

		if ( !( widget instanceof GwtMetawidget ) )
			layoutRequired( attributes, metawidget );

		state.currentColumn++;

		return null;
	}

	@Override
	public void onEndBuild( GwtMetawidget metawidget )
	{
		Facet facet = metawidget.getFacet( "buttons" );

		if ( facet != null )
		{
			State state = getState( metawidget );
			FlexTable flexTable = (FlexTable) metawidget.getWidget( 0 );
			int row = flexTable.getRowCount();

			int numberOfColumns = getEffectiveNumberOfColumns( metawidget );

			if ( numberOfColumns > 0 )
				state.formatter.setColSpan( row, 0, numberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED );

			if ( mFooterStyleName != null )
				state.formatter.setStyleName( row, 0, mFooterStyleName );

			flexTable.setWidget( row, 0, facet );
		}
	}

	//
	// Protected methods
	//

	protected void layoutRequired( Map<String, String> attributes, GwtMetawidget metawidget )
	{
		State state = getState( metawidget );
		FlexTable flexTable = (FlexTable) metawidget.getWidget( 0 );
		int row = flexTable.getRowCount() - 1;
		int column = flexTable.getCellCount( row );

		state.formatter.setStyleName( row, column, getStyleName( ( state.currentColumn * LABEL_AND_COMPONENT_AND_REQUIRED ) + 2, metawidget ) );

		if ( attributes != null && TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY ) ) && !metawidget.isReadOnly() )
		{
			flexTable.setText( row, column, "*" );
			return;
		}

		// Render an empty div, so that the CSS can force it to a certain
		// width if desired for the layout (browsers seem to not respect
		// widths set on empty table columns)
		//
		// Note: don't do <div/>, as we may not be XHTML

		flexTable.setHTML( row, column, "<div></div>" );
	}

	protected void layoutSection( String section, GwtMetawidget metawidget )
	{
		// No section?

		if ( "".equals( section ) )
			return;

		State state = getState( metawidget );
		FlexTable flexTable = (FlexTable) metawidget.getWidget( 0 );
		int row = flexTable.getRowCount();

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection != null )
			flexTable.setText( row, 0, localizedSection );
		else
			flexTable.setText( row, 0, section );

		// Span and style

		int numberOfColumns = getEffectiveNumberOfColumns( metawidget );

		if ( numberOfColumns > 0 )
			state.formatter.setColSpan( row, 0, numberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED );

		if ( mSectionStyleName != null )
			state.formatter.setStyleName( row, 0, mSectionStyleName );

		state.currentColumn = numberOfColumns;
	}

	protected String getStyleName( int styleName, GwtMetawidget metawidget )
	{
		if ( mColumnStyleNames == null || mColumnStyleNames.length <= styleName )
			return null;

		return mColumnStyleNames[styleName];
	}

	//
	// Private methods
	//

	/**
	 * Get the number of columns to use in the layout.
	 * <p>
	 * Nested Metawidgets are always just single column.
	 */

	private int getEffectiveNumberOfColumns( GwtMetawidget metawidget )
	{
		if ( metawidget.getParent() instanceof FlexTable && metawidget.getParent().getParent() instanceof GwtMetawidget )
			return 1;

		return mNumberOfColumns;
	}

	private State getState( GwtMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( FlexTableLayout.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( FlexTableLayout.class, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */class State
	{
		/* package private */FlexCellFormatter	formatter;

		/* package private */int				currentColumn;

		/* package private */String				currentSection;
	}
}
