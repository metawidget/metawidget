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
import org.metawidget.util.simple.StringUtils;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * Layout to arrange widgets in a <code>FlexTable</code>, with one column for labels and another
 * for the widget.
 *
 * This implementation recognizes the following parameters:
 * <p>
 * <ul>
 * <li><code>numberOfColumns<code> - number of columns. Each label/component pair is considered one column
 * <li><code>columnStyleNames</code> - comma delimited string of CSS style classes to apply to table
 * columns in order of: label, component, required
 * <li><code>sectionStyleName</code> - CSS style class to apply to section heading
 * </ul>
 *
 * @author Richard Kennard
 */

public class FlexTableLayout
	extends BaseLayout
{
	//
	// Private statics
	//

	private final static int	LABEL_AND_COMPONENT_AND_REQUIRED	= 3;

	//
	// Private members
	//

	private FlexTable			mLayout;

	private FlexCellFormatter	mFormatter;

	private int					mNumberOfColumns;

	private String[]			mColumnStyleNames;

	private String				mSectionStyleName;

	private int					mCurrentColumn;

	private String				mCurrentSection;

	//
	// Constructor
	//

	public FlexTableLayout( GwtMetawidget metawidget )
	{
		super( metawidget );

		// Parse column style names
		//
		// (note: ColumnFormatter uses COL elements, so is not suitable for this)

		String columnStyleNames = metawidget.getParameter( "columnStyleNames" );

		if ( columnStyleNames != null )
			mColumnStyleNames = columnStyleNames.split( "," );

		// Section style name

		mSectionStyleName = (String) metawidget.getParameter( "sectionStyleName" );

		// Number of columns

		Integer numberOfColumns = metawidget.getParameter( "numberOfColumns" );

		if ( numberOfColumns == null )
		{
			mNumberOfColumns = 1;
		}
		else
		{
			mNumberOfColumns = numberOfColumns;

			if ( numberOfColumns < 0 )
				throw new RuntimeException( "columns must be >= 0" );
		}
	}

	//
	// Public methods
	//

	@Override
	public void layoutBegin()
	{
		mLayout = new FlexTable();
		mFormatter = mLayout.getFlexCellFormatter();

		String styleName = getMetawidget().getParameter( "tableStyleName" );

		if ( styleName != null )
			mLayout.setStyleName( styleName );

		getMetawidget().add( mLayout );
	}

	public void layoutChild( Widget widget, Map<String, String> attributes )
	{
		// Do not render empty stubs

		if ( widget instanceof Stub && ( (Stub) widget ).getWidget() == null )
			return;

		// Section headings

		if ( attributes != null )
		{
			String section = attributes.get( SECTION );

			if ( section != null && !section.equals( mCurrentSection ) )
			{
				mCurrentSection = section;
				layoutSection( section );
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
		// the actualColumn is always just the next column, regardless of what mCurrentColumn is

		int actualColumn;
		int row = mLayout.getRowCount();

		if ( mCurrentColumn < mNumberOfColumns && row > 0 )
		{
			row--;
			actualColumn = mLayout.getCellCount( row );
		}
		else
		{
			mCurrentColumn = 0;
			actualColumn = 0;
		}

		// Label

		String labelText = getMetawidget().getLabelString( attributes );

		if ( labelText != null && !"".equals( labelText.trim() ) && !( widget instanceof Button ) )
		{
			Label label = new Label( labelText + ":" );

			String styleName = getStyleName( mCurrentColumn * LABEL_AND_COMPONENT_AND_REQUIRED );

			if ( styleName != null )
				mFormatter.setStyleName( row, actualColumn, styleName );

			mLayout.setWidget( row, actualColumn, label );
		}

		// Widget

		// Widget column (null labels get collapsed, blank Strings get preserved)

		if ( labelText != null )
		{
			// Zero-column layouts need an extra row

			if ( mNumberOfColumns == 0 )
			{
				mCurrentColumn = 0;
				actualColumn = 0;
				row++;
			}
			else
			{
				actualColumn++;
			}
		}

		String styleName = getStyleName( ( mCurrentColumn * LABEL_AND_COMPONENT_AND_REQUIRED ) + 1 );

		if ( styleName != null )
			mFormatter.setStyleName( row, actualColumn, styleName );

		mLayout.setWidget( row, actualColumn, widget );

		// Colspan

		int colspan;

		// Metawidgets and large components span all columns

		if ( widget instanceof GwtMetawidget || ( attributes != null && TRUE.equals( attributes.get( LARGE ) ) ) )
		{
			colspan = ( mNumberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED ) - 2;
			mCurrentColumn = mNumberOfColumns;

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
			mFormatter.setColSpan( row, actualColumn, colspan );

		// Required

		if ( !( widget instanceof GwtMetawidget ) )
			layoutRequired( attributes );

		mCurrentColumn++;
	}

	@Override
	public void layoutEnd()
	{
		Facet facet = getMetawidget().getFacet( "buttons" );

		if ( facet != null )
		{
			int row = mLayout.getRowCount();

			if ( mNumberOfColumns > 0 )
				mFormatter.setColSpan( row, 0, mNumberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED );

			String styleName = getMetawidget().getParameter( "footerStyleName" );

			if ( styleName != null )
				mFormatter.setStyleName( row, 0, styleName );

			mLayout.setWidget( row, 0, facet );
		}
	}

	//
	// Protected methods
	//

	protected void layoutRequired( Map<String, String> attributes )
	{
		int row = mLayout.getRowCount() - 1;
		int column = mLayout.getCellCount( row );

		mFormatter.setStyleName( row, column, getStyleName( ( mCurrentColumn * LABEL_AND_COMPONENT_AND_REQUIRED ) + 2 ) );

		if ( attributes != null && TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY ) ) && !getMetawidget().isReadOnly() )
		{
			mLayout.setText( row, column, "*" );
			return;
		}

		// Render an empty div, so that the CSS can force it to a certain
		// width if desired for the layout (browsers seem to not respect
		// widths set on empty table columns)
		//
		// Note: don't do <div/>, as we may not be XHTML

		mLayout.setHTML( row, column, "<div></div>" );
	}

	protected void layoutSection( String section )
	{
		// No section?

		if ( "".equals( section ) )
			return;

		int row = mLayout.getRowCount();

		// Section name (possibly localized)

		String localizedSection = getMetawidget().getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection != null )
			mLayout.setText( row, 0, localizedSection );
		else
			mLayout.setText( row, 0, section );

		// Span and style

		if ( mNumberOfColumns > 0 )
			mFormatter.setColSpan( row, 0, mNumberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED );

		if ( mSectionStyleName != null )
			mFormatter.setStyleName( row, 0, mSectionStyleName );

		mCurrentColumn = mNumberOfColumns;
	}

	protected String getStyleName( int styleName )
	{
		if ( mColumnStyleNames == null || mColumnStyleNames.length <= styleName )
			return null;

		return mColumnStyleNames[styleName];
	}
}
