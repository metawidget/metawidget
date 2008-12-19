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

package org.metawidget.jsp.tagext.html.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;
import java.util.Set;

import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.BaseLayout;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.FacetTag.FacetContent;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to arrange widgets in a table, with one column for labels and another for the widget. This
 * implementation recognizes the following <code>&lt;m:param&gt;</code> parameters:
 * <p>
 * <ul>
 * <li><code>tableStyle</code>
 * <li><code>tableStyleClass</code>
 * <li><code>numberOfColumns</code>
 * <li><code>columnStyleClasses</code> - comma delimited string of CSS style classes to apply to
 * table columns in order of: label, component, required
 * <li><code>sectionStyleClass</code>
 * </ul>
 * <p>
 * Styles and style classes can be applied to any facets (such as the 'footer' facet) by specifying
 * them on the <code>&lt;m:facet&gt;</code> tag.
 *
 * @author Richard Kennard
 */

public class HtmlTableLayout
	extends BaseLayout
{
	//
	// Private statics
	//

	private final static String		TABLE_PREFIX						= "table-";

	private final static String		ROW_SUFFIX							= "-row";

	private final static String		CELL_SUFFIX							= "-cell";

	private final static int		JUST_COMPONENT_AND_REQUIRED			= 2;

	private final static int		LABEL_AND_COMPONENT_AND_REQUIRED	= 3;

	//
	// Private members
	//

	private int						mNumberOfColumns;

	private String					mTableStyle;

	private String					mTableStyleClass;

	private String[]				mColumnStyleClasses;

	private String					mSectionStyleClass;

	private int						mCurrentColumn;

	private String					mCurrentSection;

	private Set<String>				mHiddenFields;

	private String					mTableType;

	//
	// Constructor
	//

	public HtmlTableLayout( MetawidgetTag metawidget )
	{
		super( metawidget );

		// Table styles

		mTableStyle = metawidget.getParam( "tableStyle" );
		mTableStyleClass = metawidget.getParam( "tableStyleClass" );

		// Inner styles

		String columnStyleClasses = metawidget.getParam( "columnStyleClasses" );

		if ( columnStyleClasses != null )
			mColumnStyleClasses = columnStyleClasses.split( StringUtils.SEPARATOR_COMMA );

		// Section styles

		mSectionStyleClass = metawidget.getParam( "sectionStyleClass" );

		// Number of columns

		String numberOfColumns = metawidget.getParam( "numberOfColumns" );

		if ( numberOfColumns == null )
			mNumberOfColumns = 1;
		else
			mNumberOfColumns = Integer.parseInt( numberOfColumns );
	}

	//
	// Public methods
	//

	@Override
	public String layoutBegin( String value )
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		// Start table

		buffer.append( "<table" );

		// Id

		buffer.append( " id=\"" );
		buffer.append( TABLE_PREFIX );
		mTableType = StringUtils.camelCase( value, StringUtils.SEPARATOR_DOT_CHAR );
		buffer.append( mTableType );
		buffer.append( "\"" );

		// Styles

		if ( mTableStyle != null )
		{
			buffer.append( " style=\"" );
			buffer.append( mTableStyle );
			buffer.append( "\"" );
		}

		if ( mTableStyleClass != null )
		{
			buffer.append( " class=\"" );
			buffer.append( mTableStyleClass );
			buffer.append( "\"" );
		}

		buffer.append( ">" );

		// Footer parameter (XHTML requires TFOOT to come before TBODY)

		FacetContent facetFooter = getMetawidgetTag().getFacet( "footer" );

		if ( facetFooter != null )
		{
			buffer.append( "\r\n<tfoot>" );
			buffer.append( "<tr>" );
			buffer.append( "<td colspan=\"" );

			// Footer spans multiples of label/component/required

			int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, mNumberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED );
			buffer.append( String.valueOf( colspan ) );
			buffer.append( "\"" );

			// CSS styles

			String footerStyle = facetFooter.getAttribute( "style" );

			if ( footerStyle != null )
			{
				buffer.append( " style=\"" );
				buffer.append( footerStyle );
				buffer.append( "\"" );
			}

			String footerStyleClass = facetFooter.getAttribute( "styleClass" );

			if ( footerStyleClass != null )
			{
				buffer.append( " class=\"" );
				buffer.append( footerStyleClass );
				buffer.append( "\"" );
			}

			buffer.append( ">" );
			buffer.append( facetFooter.getContent() );
			buffer.append( "</td>" );
			buffer.append( "</tr>" );
			buffer.append( "</tfoot>" );
		}

		buffer.append( "<tbody>" );

		return buffer.toString();
	}

	@Override
	public String layoutChild( String child, Map<String, String> attributes )
	{
		if ( child == null )
			return "";

		// If the String is just hidden fields...

		if ( JspUtils.isJustHiddenFields( child ))
		{
			// ...store it up for later (eg. don't render a row in the table
			// and a label)

			if ( mHiddenFields == null )
				mHiddenFields = CollectionUtils.newHashSet();

			mHiddenFields.add( child );

			return "";
		}

		// Write child normally
		//
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();
		buffer.append( layoutBeforeChild( attributes ) );
		buffer.append( child );
		buffer.append( layoutAfterChild( attributes ) );

		return buffer.toString();
	}

	@Override
	public String layoutEnd()
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();
		buffer.append( "</tbody>" );
		buffer.append( "</table>" );

		// Output any hidden fields

		if ( mHiddenFields != null )
		{
			for ( String hiddenField : mHiddenFields )
			{
				buffer.append( "\r\n" );
				buffer.append( hiddenField );
			}
		}

		return buffer.toString();
	}

	//
	// Protected methods
	//

	protected String layoutBeforeChild( Map<String, String> attributes )
	{
		mCurrentColumn++;

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		// Section headings

		// (layoutBeforeChild may get called even if layoutBegin crashed. Try
		// to fail gracefully)

		String id = null;

		if ( attributes != null )
		{
			String section = attributes.get( SECTION );

			if ( section != null && !section.equals( mCurrentSection ) )
			{
				mCurrentSection = section;
				buffer.append( layoutSection( section ) );
			}

			id = attributes.get( NAME );

			if ( id != null )
				id = StringUtils.uppercaseFirstLetter( StringUtils.camelCase( id ) );

			if ( TRUE.equals( attributes.get( LARGE ) ) && mCurrentColumn != 1 )
			{
				buffer.append( "</tr>" );
				mCurrentColumn = 1;
			}
		}

		// Start a new row, if necessary

		if ( mCurrentColumn == 1 || mCurrentColumn > mNumberOfColumns )
		{
			mCurrentColumn = 1;

			buffer.append( "\r\n<tr" );

			if ( id != null )
			{
				buffer.append( " id=\"" );
				buffer.append( TABLE_PREFIX );
				buffer.append( mTableType );
				buffer.append( id );
				buffer.append( ROW_SUFFIX );
				buffer.append( "\"" );
			}

			buffer.append( ">" );
		}

		// Start the label column

		String labelColumn = layoutLabel( attributes );
		buffer.append( labelColumn );

		// Zero-column layouts need an extra row

		if ( mNumberOfColumns == 0 )
		{
			buffer.append( "</tr>\r\n<tr" );

			if ( id != null )
			{
				buffer.append( " id=\"" );
				buffer.append( TABLE_PREFIX );
				buffer.append( mTableType );
				buffer.append( id );
				buffer.append( ROW_SUFFIX );
				buffer.append( "2\"" );
			}

			buffer.append( ">" );
		}

		// Start the component column

		buffer.append( "<td" );

		if ( id != null )
		{
			buffer.append( " id=\"" );
			buffer.append( TABLE_PREFIX );
			buffer.append( mTableType );
			buffer.append( id );
			buffer.append( CELL_SUFFIX );
			buffer.append( "\"" );
		}

		buffer.append( getStyleClass( 1 ) );

		int colspan = 1;

		if ( "".equals( labelColumn ) )
			colspan = 2;

		// Large components span all columns
		//
		// Note: we cannot span all columns for Metawidgets, as we do in HtmlTableLayoutRenderer,
		// because JSP lacks a true component model such that we can ask which sort of component we
		// are rendering

		if ( mNumberOfColumns > 1 && attributes != null && TRUE.equals( attributes.get( "large" ) ) )
		{
			colspan = ( ( mNumberOfColumns - 1 ) * LABEL_AND_COMPONENT_AND_REQUIRED ) + 1;
			mCurrentColumn = mNumberOfColumns;
		}

		if ( colspan > 1 )
		{
			buffer.append( " colspan=\"" );
			buffer.append( colspan );
			buffer.append( "\"" );
		}

		buffer.append( ">" );

		return buffer.toString();
	}

	protected String layoutAfterChild( Map<String, String> attributes )
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		// End the component column

		buffer.append( "</td>" );

		// Render the 'required' column

		buffer.append( "<td" );
		buffer.append( getStyleClass( 2 ) );
		buffer.append( ">" );

		buffer.append( layoutRequired( attributes ) );

		buffer.append( "</td>" );

		// End the row, if necessary

		if ( mCurrentColumn >= mNumberOfColumns )
		{
			mCurrentColumn = 0;
			buffer.append( "</tr>" );
		}

		return buffer.toString();
	}

	protected String layoutLabel( Map<String, String> attributes )
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		String label = getMetawidgetTag().getLabelString( attributes );

		if ( label == null )
			return "";

		// Output a (possibly localized) label

		buffer.append( "<th" );
		buffer.append( getStyleClass( 0 ) );
		buffer.append( ">" );

		if ( !"".equals( label ) )
		{
			buffer.append( label );
			buffer.append( ":" );
		}

		buffer.append( "</th>" );

		return buffer.toString();
	}

	protected String layoutSection( String section )
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		// No section?

		if ( "".equals( section ) )
			return "";

		buffer.append( "\r\n<tr>" );
		buffer.append( "<th colspan=\"" );

		// Sections span multiples of label/component/required

		int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, mNumberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED );
		buffer.append( String.valueOf( colspan ) );

		buffer.append( "\"" );

		if ( mSectionStyleClass != null )
		{
			buffer.append( " class=\"" );
			buffer.append( mSectionStyleClass );
			buffer.append( "\"" );
		}

		buffer.append( ">" );

		// Section name (possibly localized)

		String localizedSection = getMetawidgetTag().getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection != null )
			buffer.append( localizedSection );
		else
			buffer.append( section );

		buffer.append( "</th>" );
		buffer.append( "</tr>" );

		return buffer.toString();
	}

	protected String layoutRequired( Map<String, String> attributes )
	{
		if ( attributes != null && TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY ) ) && !getMetawidgetTag().isReadOnly() )
			return "*";

		// Render an empty div, so that the CSS can force it to a certain
		// width if desired for the layout (browsers seem to not respect
		// widths set on empty table columns)

		return "<div/>";
	}

	protected String getStyleClass( int styleClass )
	{
		if ( mColumnStyleClasses == null || mColumnStyleClasses.length <= styleClass )
			return "";

		String columnClass = mColumnStyleClasses[styleClass];

		if ( columnClass.length() == 0 )
			return "";

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		buffer.append( " class=\"" );
		buffer.append( columnClass.trim() );
		buffer.append( "\"" );

		return buffer.toString();
	}
}
