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
 * @author Richard Kennard
 */

public class FlexTableLayout
	extends LayoutImpl
{
	//
	// Private members
	//

	private FlexTable			mLayout;

	private FlexCellFormatter	mFormatter;

	private String[]			mColumnStyleNames;

	private String				mSectionStyleName;

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

		String columnStyleNames = (String) metawidget.getParameter( "columnStyleNames" );

		if ( columnStyleNames != null )
			mColumnStyleNames = columnStyleNames.split( "," );

		// Section style name

		mSectionStyleName = (String) metawidget.getParameter( "sectionStyleName" );
	}

	//
	// Public methods
	//

	@Override
	public void layoutBegin()
	{
		mLayout = new FlexTable();
		mFormatter = mLayout.getFlexCellFormatter();

		String styleName = (String) getMetawidget().getParameter( "tableStyleName" );

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

		String section = attributes.get( SECTION );

		if ( section != null && !section.equals( mCurrentSection ) )
		{
			mCurrentSection = section;
			layoutSection( section );
		}

		int row = mLayout.getRowCount();

		// Label

		String labelText = getMetawidget().getLabelString( attributes );

		if ( labelText != null && !"".equals( labelText.trim() ) && !( widget instanceof Button ) )
		{
			Label label = new Label( labelText + ":" );

			String styleName = getStyleName( 0 );

			if ( styleName != null )
				mFormatter.setStyleName( row, 0, styleName );

			mLayout.setWidget( row, 0, label );
		}

		// Widget

		// Widget column (null labels get collapsed, blank Strings get preserved)

		int widgetColumn = ( labelText == null ? 0 : 1 );

		String styleName = getStyleName( 1 );

		if ( styleName != null )
			mFormatter.setStyleName( row, widgetColumn, styleName );

		mLayout.setWidget( row, widgetColumn, widget );

		// Colspan

		int colspan = 1;

		if ( widgetColumn == 0 )
			colspan++;

		// Nested Metawidgets span the required column too

		boolean isMetawidget = ( widget instanceof GwtMetawidget );

		if ( isMetawidget )
			colspan++;

		if ( colspan > 1 )
			mFormatter.setColSpan( row, widgetColumn, colspan );

		// Required

		if ( !isMetawidget )
			layoutRequired( attributes );
	}

	@Override
	public void layoutEnd()
	{
		Facet facet = getMetawidget().getFacet( "buttons" );

		if ( facet != null )
		{
			int row = mLayout.getRowCount();
			mFormatter.setColSpan( row, 0, 2 );

			String styleName = (String) getMetawidget().getParameter( "footerStyleName" );

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
		mFormatter.setStyleName( row, 2, getStyleName( 2 ) );

		if ( attributes != null && TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY ) ) && !getMetawidget().isReadOnly() )
		{
			mLayout.setText( row, 2, "*" );
			return;
		}

		// Render an empty div, so that the CSS can force it to a certain
		// width if desired for the layout (browsers seem to not respect
		// widths set on empty table columns)

		mLayout.setHTML( row, 2, "<div/>" );
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

		mFormatter.setColSpan( row, 0, 3 );

		if ( mSectionStyleName != null )
			mFormatter.setStyleName( row, 0, mSectionStyleName );
	}

	protected String getStyleName( int styleName )
	{
		if ( mColumnStyleNames == null || mColumnStyleNames.length <= styleName )
			return null;

		return mColumnStyleNames[styleName];
	}
}
