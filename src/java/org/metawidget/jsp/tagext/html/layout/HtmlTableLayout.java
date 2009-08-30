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
import org.metawidget.jsp.tagext.Layout;
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
	implements Layout
{
	//
	// Private statics
	//

	private final static String	TABLE_PREFIX						= "table-";

	private final static String	ROW_SUFFIX							= "-row";

	private final static String	CELL_SUFFIX							= "-cell";

	private final static int	JUST_COMPONENT_AND_REQUIRED			= 2;

	private final static int	LABEL_AND_COMPONENT_AND_REQUIRED	= 3;

	//
	// Public methods
	//

	@Override
	public String layoutBegin( String value, MetawidgetTag metawidgetTag )
	{
		State state = new State();
		metawidgetTag.putClientProperty( HtmlTableLayout.class, state );

		// Table styles

		state.tableStyle = metawidgetTag.getParameter( "tableStyle" );
		state.tableStyleClass = metawidgetTag.getParameter( "tableStyleClass" );

		// Inner styles

		String columnStyleClasses = metawidgetTag.getParameter( "columnStyleClasses" );

		if ( columnStyleClasses != null )
			state.columnStyleClasses = columnStyleClasses.split( StringUtils.SEPARATOR_COMMA );

		// Section styles

		state.sectionStyleClass = metawidgetTag.getParameter( "sectionStyleClass" );

		// Number of columns

		String numberOfColumns = metawidgetTag.getParameter( "numberOfColumns" );

		if ( numberOfColumns == null )
			state.numberOfColumns = 1;
		else
			state.numberOfColumns = Integer.parseInt( numberOfColumns );

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		// Start table

		buffer.append( "<table" );

		// Id

		buffer.append( " id=\"" );
		buffer.append( TABLE_PREFIX );
		state.tableType = StringUtils.camelCase( value, StringUtils.SEPARATOR_DOT_CHAR );
		buffer.append( state.tableType );
		buffer.append( "\"" );

		// Styles

		if ( state.tableStyle != null )
		{
			buffer.append( " style=\"" );
			buffer.append( state.tableStyle );
			buffer.append( "\"" );
		}

		if ( state.tableStyleClass != null )
		{
			buffer.append( " class=\"" );
			buffer.append( state.tableStyleClass );
			buffer.append( "\"" );
		}

		buffer.append( ">" );

		// Footer parameter (XHTML requires TFOOT to come before TBODY)

		FacetContent facetFooter = metawidgetTag.getFacet( "footer" );

		if ( facetFooter != null )
		{
			buffer.append( "\r\n<tfoot>" );
			buffer.append( "<tr>" );
			buffer.append( "<td colspan=\"" );

			// Footer spans multiples of label/component/required

			int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, state.numberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED );
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
	public String layoutChild( String child, Map<String, String> attributes, MetawidgetTag metawidgetTag )
	{
		if ( child == null )
			return "";

		// If the String is just hidden fields...

		if ( JspUtils.isJustHiddenFields( child ) )
		{
			// ...store it up for later (eg. don't render a row in the table
			// and a label)

			State state = metawidgetTag.getClientProperty( HtmlTableLayout.class );

			if ( state.hiddenFields == null )
				state.hiddenFields = CollectionUtils.newHashSet();

			state.hiddenFields.add( child );

			return "";
		}

		// Write child normally
		//
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();
		buffer.append( layoutBeforeChild( attributes, metawidgetTag ) );
		buffer.append( child );
		buffer.append( layoutAfterChild( attributes, metawidgetTag ) );

		return buffer.toString();
	}

	@Override
	public String layoutEnd( MetawidgetTag metawidgetTag )
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();
		buffer.append( "</tbody>" );
		buffer.append( "</table>" );

		// Output any hidden fields

		State state = metawidgetTag.getClientProperty( HtmlTableLayout.class );

		if ( state.hiddenFields != null )
		{
			for ( String hiddenField : state.hiddenFields )
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

	protected String layoutBeforeChild( Map<String, String> attributes, MetawidgetTag metawidgetTag )
	{
		State state = metawidgetTag.getClientProperty( HtmlTableLayout.class );
		state.currentColumn++;

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		// Section headings

		// (layoutBeforeChild may get called even if layoutBegin crashed. Try
		// to fail gracefully)

		String id = null;

		if ( attributes != null )
		{
			String section = attributes.get( SECTION );

			if ( section != null && !section.equals( state.currentSection ) )
			{
				state.currentSection = section;
				buffer.append( layoutSection( section, metawidgetTag ) );
			}

			id = attributes.get( NAME );

			if ( id != null )
				id = StringUtils.uppercaseFirstLetter( StringUtils.camelCase( id ) );

			if ( TRUE.equals( attributes.get( LARGE ) ) && state.currentColumn != 1 )
			{
				buffer.append( "</tr>" );
				state.currentColumn = 1;
			}
		}

		// Start a new row, if necessary

		if ( state.currentColumn == 1 || state.currentColumn > state.numberOfColumns )
		{
			state.currentColumn = 1;

			buffer.append( "\r\n<tr" );

			if ( id != null )
			{
				buffer.append( " id=\"" );
				buffer.append( TABLE_PREFIX );
				buffer.append( state.tableType );
				buffer.append( id );
				buffer.append( ROW_SUFFIX );
				buffer.append( "\"" );
			}

			buffer.append( ">" );
		}

		// Start the label column

		String labelColumn = layoutLabel( attributes, metawidgetTag );
		buffer.append( labelColumn );

		// Zero-column layouts need an extra row

		if ( state.numberOfColumns == 0 )
		{
			buffer.append( "</tr>\r\n<tr" );

			if ( id != null )
			{
				buffer.append( " id=\"" );
				buffer.append( TABLE_PREFIX );
				buffer.append( state.tableType );
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
			buffer.append( state.tableType );
			buffer.append( id );
			buffer.append( CELL_SUFFIX );
			buffer.append( "\"" );
		}

		buffer.append( getStyleClass( 1, state ) );

		int colspan = 1;

		if ( "".equals( labelColumn ) )
			colspan = 2;

		// Large components span all columns
		//
		// Note: we cannot span all columns for Metawidgets, as we do in HtmlTableLayoutRenderer,
		// because JSP lacks a true component model such that we can ask which sort of component we
		// are rendering

		if ( state.numberOfColumns > 1 && attributes != null && TRUE.equals( attributes.get( "large" ) ) )
		{
			colspan = ( ( state.numberOfColumns - 1 ) * LABEL_AND_COMPONENT_AND_REQUIRED ) + 1;
			state.currentColumn = state.numberOfColumns;
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

	protected String layoutAfterChild( Map<String, String> attributes, MetawidgetTag metawidgetTag )
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		// End the component column

		buffer.append( "</td>" );

		// Render the 'required' column

		buffer.append( "<td" );
		State state = metawidgetTag.getClientProperty( HtmlTableLayout.class );
		buffer.append( getStyleClass( 2, state ) );
		buffer.append( ">" );

		buffer.append( layoutRequired( attributes, metawidgetTag ) );

		buffer.append( "</td>" );

		// End the row, if necessary

		if ( state.currentColumn >= state.numberOfColumns )
		{
			state.currentColumn = 0;
			buffer.append( "</tr>" );
		}

		return buffer.toString();
	}

	protected String layoutLabel( Map<String, String> attributes, MetawidgetTag metawidgetTag )
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		String label = metawidgetTag.getLabelString( attributes );

		if ( label == null )
			return "";

		// Output a (possibly localized) label

		buffer.append( "<th" );
		State state = metawidgetTag.getClientProperty( HtmlTableLayout.class );
		buffer.append( getStyleClass( 0, state ) );
		buffer.append( ">" );

		if ( !"".equals( label ) )
		{
			buffer.append( label );
			buffer.append( ":" );
		}

		buffer.append( "</th>" );

		return buffer.toString();
	}

	protected String layoutSection( String section, MetawidgetTag metawidgetTag )
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		// No section?

		if ( "".equals( section ) )
			return "";

		buffer.append( "\r\n<tr>" );
		buffer.append( "<th colspan=\"" );

		// Sections span multiples of label/component/required

		State state = metawidgetTag.getClientProperty( HtmlTableLayout.class );
		int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, state.numberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED );
		buffer.append( String.valueOf( colspan ) );

		buffer.append( "\"" );

		if ( state.sectionStyleClass != null )
		{
			buffer.append( " class=\"" );
			buffer.append( state.sectionStyleClass );
			buffer.append( "\"" );
		}

		buffer.append( ">" );

		// Section name (possibly localized)

		String localizedSection = metawidgetTag.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection != null )
			buffer.append( localizedSection );
		else
			buffer.append( section );

		buffer.append( "</th>" );
		buffer.append( "</tr>" );

		return buffer.toString();
	}

	protected String layoutRequired( Map<String, String> attributes, MetawidgetTag metawidgetTag )
	{
		if ( attributes != null && TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY ) ) && !metawidgetTag.isReadOnly() )
			return "*";

		// Render an empty div, so that the CSS can force it to a certain
		// width if desired for the layout (browsers seem to not respect
		// widths set on empty table columns)
		//
		// Note: don't do <div/>, as we may not be XHTML

		return "<div></div>";
	}

	protected String getStyleClass( int styleClass, State state )
	{
		if ( state.columnStyleClasses == null || state.columnStyleClasses.length <= styleClass )
			return "";

		String columnClass = state.columnStyleClasses[styleClass];

		if ( columnClass.length() == 0 )
			return "";

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		buffer.append( " class=\"" );
		buffer.append( columnClass.trim() );
		buffer.append( "\"" );

		return buffer.toString();
	}

	//
	// Inner class
	//

	/*package private*/ class State
	{
		public int			numberOfColumns;

		public String		tableStyle;

		public String		tableStyleClass;

		public String[]		columnStyleClasses;

		public String		sectionStyleClass;

		public int			currentColumn;

		public String		currentSection;

		public Set<String>	hiddenFields;

		public String		tableType;
	}
}
