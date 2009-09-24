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

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.FacetTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.StubTag;
import org.metawidget.jsp.tagext.html.HtmlFacetTag;
import org.metawidget.layout.iface.Layout;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.layout.impl.LayoutUtils;
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
	implements Layout<Tag, MetawidgetTag>
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

	public void onStartBuild( MetawidgetTag metawidgetTag )
	{
		metawidgetTag.putClientProperty( HtmlTableLayout.class, null );
		State state = getState( metawidgetTag );

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

		try
		{
			// Start table

			JspWriter writer = metawidgetTag.getPageContext().getOut();
			writer.write( "<table" );

			// Id

			writer.write( " id=\"" );
			writer.write( TABLE_PREFIX );
			state.tableType = StringUtils.camelCase( metawidgetTag.getPath(), StringUtils.SEPARATOR_DOT_CHAR );
			writer.write( state.tableType );
			writer.write( "\"" );

			// Styles

			if ( state.tableStyle != null )
			{
				writer.write( " style=\"" );
				writer.write( state.tableStyle );
				writer.write( "\"" );
			}

			if ( state.tableStyleClass != null )
			{
				writer.write( " class=\"" );
				writer.write( state.tableStyleClass );
				writer.write( "\"" );
			}

			writer.write( ">" );

			// Footer parameter (XHTML requires TFOOT to come before TBODY)

			FacetTag facetFooter = metawidgetTag.getFacet( "footer" );

			if ( facetFooter != null )
			{
				writer.write( "\r\n<tfoot>" );
				writer.write( "<tr>" );
				writer.write( "<td colspan=\"" );

				// Footer spans multiples of label/component/required

				int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, state.numberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED );
				writer.write( String.valueOf( colspan ) );
				writer.write( "\"" );

				// CSS styles

				if ( facetFooter instanceof HtmlFacetTag )
				{
					String footerStyle = ((HtmlFacetTag) facetFooter).getStyle();

					if ( footerStyle != null )
					{
						writer.write( " style=\"" );
						writer.write( footerStyle );
						writer.write( "\"" );
					}

					String footerStyleClass = ((HtmlFacetTag) facetFooter).getStyleClass();

					if ( footerStyleClass != null )
					{
						writer.write( " class=\"" );
						writer.write( footerStyleClass );
						writer.write( "\"" );
					}
				}

				writer.write( ">" );
				writer.write( facetFooter.getSavedBodyContent() );
				writer.write( "</td>" );
				writer.write( "</tr>" );
				writer.write( "</tfoot>" );
			}

			writer.write( "<tbody>" );
		}
		catch ( Exception e )
		{
			throw LayoutException.newException( e );
		}
	}

	public void layoutChild( Tag tag, Map<String, String> attributes, MetawidgetTag metawidgetTag )
	{
		try
		{
			String literal = null;

			if ( tag instanceof StubTag )
			{
				literal = ( (StubTag) tag ).getSavedBodyContent();

				// Ignore empty stubs

				if ( literal == null || literal.isEmpty() )
					return;
			}
			else
			{
				literal = JspUtils.writeTag( metawidgetTag.getPageContext(), tag, metawidgetTag, null );
			}

			// If the String is just hidden fields...

			if ( JspUtils.isJustHiddenFields( literal ) )
			{
				// ...store it up for later (eg. don't render a row in the table
				// and a label)

				State state = getState( metawidgetTag );

				if ( state.hiddenFields == null )
					state.hiddenFields = CollectionUtils.newHashSet();

				state.hiddenFields.add( literal );

				return;
			}

			// Write child normally

			JspWriter writer = metawidgetTag.getPageContext().getOut();
			layoutBeforeChild( attributes, metawidgetTag );
			writer.write( literal );
			layoutAfterChild( attributes, metawidgetTag );
		}
		catch ( Exception e )
		{
			throw LayoutException.newException( e );
		}
	}

	@Override
	public void onEndBuild( MetawidgetTag metawidgetTag )
	{
		try
		{
			JspWriter writer = metawidgetTag.getPageContext().getOut();
			writer.write( "</tbody>" );
			writer.write( "</table>" );

			// Output any hidden fields

			State state = getState( metawidgetTag );

			if ( state.hiddenFields != null )
			{
				for ( String hiddenField : state.hiddenFields )
				{
					writer.write( "\r\n" );
					writer.write( hiddenField );
				}
			}
		}
		catch ( IOException e )
		{
			throw LayoutException.newException( e );
		}
	}

	//
	// Protected methods
	//

	protected void layoutBeforeChild( Map<String, String> attributes, MetawidgetTag metawidgetTag )
	{
		State state = getState( metawidgetTag );
		state.currentColumn++;

		try
		{
			JspWriter writer = metawidgetTag.getPageContext().getOut();

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
					layoutSection( section, metawidgetTag );
				}

				id = attributes.get( NAME );

				if ( id != null )
					id = StringUtils.uppercaseFirstLetter( StringUtils.camelCase( id ) );

				if ( LayoutUtils.isSpanAllColumns( attributes ) && state.currentColumn != 1 )
				{
					writer.write( "</tr>" );
					state.currentColumn = 1;
				}
			}

			// Start a new row, if necessary

			if ( state.currentColumn == 1 || state.currentColumn > state.numberOfColumns )
			{
				state.currentColumn = 1;

				writer.write( "\r\n<tr" );

				if ( id != null )
				{
					writer.write( " id=\"" );
					writer.write( TABLE_PREFIX );
					writer.write( state.tableType );
					writer.write( id );
					writer.write( ROW_SUFFIX );
					writer.write( "\"" );
				}

				writer.write( ">" );
			}

			// Start the label column

			boolean labelRendered = layoutLabel( attributes, metawidgetTag );

			// Zero-column layouts need an extra row

			if ( state.numberOfColumns == 0 )
			{
				writer.write( "</tr>\r\n<tr" );

				if ( id != null )
				{
					writer.write( " id=\"" );
					writer.write( TABLE_PREFIX );
					writer.write( state.tableType );
					writer.write( id );
					writer.write( ROW_SUFFIX );
					writer.write( "2\"" );
				}

				writer.write( ">" );
			}

			// Start the component column

			writer.write( "<td" );

			if ( id != null )
			{
				writer.write( " id=\"" );
				writer.write( TABLE_PREFIX );
				writer.write( state.tableType );
				writer.write( id );
				writer.write( CELL_SUFFIX );
				writer.write( "\"" );
			}

			writeStyleClass( 1, state, metawidgetTag );

			int colspan = 1;

			if ( !labelRendered )
				colspan = 2;

			// Large components span all columns
			//
			// Note: we cannot span all columns for Metawidgets, as we do in
			// HtmlTableLayoutRenderer,
			// because JSP lacks a true component model such that we can ask which sort of component
			// we are rendering

			if ( state.numberOfColumns > 1 && attributes != null && TRUE.equals( attributes.get( "large" ) ) )
			{
				colspan = ( ( state.numberOfColumns - 1 ) * LABEL_AND_COMPONENT_AND_REQUIRED ) + 1;
				state.currentColumn = state.numberOfColumns;
			}

			if ( colspan > 1 )
			{
				writer.write( " colspan=\"" );
				writer.write( String.valueOf( colspan ) );
				writer.write( "\"" );
			}

			writer.write( ">" );
		}
		catch ( IOException e )
		{
			throw LayoutException.newException( e );
		}
	}

	protected void layoutAfterChild( Map<String, String> attributes, MetawidgetTag metawidgetTag )
	{
		try
		{
			JspWriter writer = metawidgetTag.getPageContext().getOut();

			// End the component column

			writer.write( "</td>" );

			// Render the 'required' column

			writer.write( "<td" );
			State state = getState( metawidgetTag );
			writeStyleClass( 2, state, metawidgetTag );
			writer.write( ">" );

			writer.write( layoutRequired( attributes, metawidgetTag ) );

			writer.write( "</td>" );

			// End the row, if necessary

			if ( state.currentColumn >= state.numberOfColumns )
			{
				state.currentColumn = 0;
				writer.write( "</tr>" );
			}
		}
		catch ( IOException e )
		{
			throw LayoutException.newException( e );
		}
	}

	/**
	 * @return true if a label was rendered
	 */

	protected boolean layoutLabel( Map<String, String> attributes, MetawidgetTag metawidgetTag )
	{
		String label = metawidgetTag.getLabelString( attributes );

		if ( label == null )
			return false;

		try
		{
			JspWriter writer = metawidgetTag.getPageContext().getOut();

			// Output a (possibly localized) label

			writer.write( "<th" );
			State state = getState( metawidgetTag );
			writeStyleClass( 0, state, metawidgetTag );
			writer.write( ">" );

			if ( !"".equals( label ) )
			{
				writer.write( label );
				writer.write( ":" );
			}

			writer.write( "</th>" );

			return true;
		}
		catch ( IOException e )
		{
			throw LayoutException.newException( e );
		}
	}

	protected void layoutSection( String section, MetawidgetTag metawidgetTag )
	{
		// No section?

		if ( "".equals( section ) )
			return;

		try
		{
			JspWriter writer = metawidgetTag.getPageContext().getOut();

			writer.write( "\r\n<tr>" );
			writer.write( "<th colspan=\"" );

			// Sections span multiples of label/component/required

			State state = getState( metawidgetTag );
			int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, state.numberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED );
			writer.write( String.valueOf( colspan ) );

			writer.write( "\"" );

			if ( state.sectionStyleClass != null )
			{
				writer.write( " class=\"" );
				writer.write( state.sectionStyleClass );
				writer.write( "\"" );
			}

			writer.write( ">" );

			// Section name (possibly localized)

			String localizedSection = metawidgetTag.getLocalizedKey( StringUtils.camelCase( section ) );

			if ( localizedSection != null )
				writer.write( localizedSection );
			else
				writer.write( section );

			writer.write( "</th>" );
			writer.write( "</tr>" );
		}
		catch ( IOException e )
		{
			throw LayoutException.newException( e );
		}
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

	protected void writeStyleClass( int styleClass, State state, MetawidgetTag metawidgetTag )
	{
		if ( state.columnStyleClasses == null || state.columnStyleClasses.length <= styleClass )
			return;

		String columnClass = state.columnStyleClasses[styleClass];

		if ( columnClass.length() == 0 )
			return;

		try
		{
			JspWriter writer = metawidgetTag.getPageContext().getOut();

			writer.write( " class=\"" );
			writer.write( columnClass.trim() );
			writer.write( "\"" );
		}
		catch ( IOException e )
		{
			throw LayoutException.newException( e );
		}
	}

	//
	// Private methods
	//

	private State getState( MetawidgetTag metawidget )
	{
		State state = (State) metawidget.getClientProperty( HtmlTableLayout.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( HtmlTableLayout.class, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/* package private */class State
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
