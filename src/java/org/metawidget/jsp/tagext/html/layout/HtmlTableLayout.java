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
import org.metawidget.layout.iface.Layout;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.SimpleLayoutUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to arrange widgets in a table, with one column for labels and another for the widget.
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
	// Private members
	//

	private int					mNumberOfColumns;

	private String				mTableStyle;

	private String				mTableStyleClass;

	private String[]			mColumnStyleClasses;

	private String				mSectionStyleClass;

	private String				mFooterStyle;

	private String				mFooterStyleClass;

	//
	// Constructor
	//

	public HtmlTableLayout()
	{
		this( new HtmlTableLayoutConfig() );
	}

	public HtmlTableLayout( HtmlTableLayoutConfig config )
	{
		mNumberOfColumns = config.getNumberOfColumns();
		mTableStyle = config.getTableStyle();
		mTableStyleClass = config.getTableStyleClass();
		mColumnStyleClasses = config.getColumnStyleClasses();
		mSectionStyleClass = config.getSectionStyleClass();
		mFooterStyle = config.getFooterStyle();
		mFooterStyleClass = config.getFooterStyleClass();
	}

	//
	// Public methods
	//

	public void startLayout( Tag container, MetawidgetTag metawidgetTag )
	{
		metawidgetTag.putClientProperty( HtmlTableLayout.class, null );
		State state = getState( metawidgetTag );

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

			if ( mTableStyle != null )
			{
				writer.write( " style=\"" );
				writer.write( mTableStyle );
				writer.write( "\"" );
			}

			if ( mTableStyleClass != null )
			{
				writer.write( " class=\"" );
				writer.write( mTableStyleClass );
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

				int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, mNumberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED );
				writer.write( String.valueOf( colspan ) );
				writer.write( "\"" );

				// CSS styles

				if ( mFooterStyle != null )
				{
					writer.write( " style=\"" );
					writer.write( mFooterStyle );
					writer.write( "\"" );
				}

				if ( mFooterStyleClass != null )
				{
					writer.write( " class=\"" );
					writer.write( mFooterStyleClass );
					writer.write( "\"" );
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

	public void layoutWidget( Tag tag, String elementName, Map<String, String> attributes, Tag container, MetawidgetTag metawidgetTag )
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
			layoutBeforeChild( tag, elementName, attributes, metawidgetTag );
			writer.write( literal );
			layoutAfterChild( attributes, metawidgetTag );
		}
		catch ( Exception e )
		{
			throw LayoutException.newException( e );
		}
	}

	@Override
	public void endLayout( Tag container, MetawidgetTag metawidgetTag )
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

	protected void layoutBeforeChild( Tag tag, String elementName, Map<String, String> attributes, MetawidgetTag metawidgetTag )
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

				if ( SimpleLayoutUtils.isSpanAllColumns( attributes ) && state.currentColumn != 1 )
				{
					writer.write( "</tr>" );
					state.currentColumn = 1;
				}
			}

			// Start a new row, if necessary

			if ( state.currentColumn == 1 || state.currentColumn > mNumberOfColumns )
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

			boolean labelRendered = layoutLabel( elementName, attributes, metawidgetTag );

			// Zero-column layouts need an extra row

			if ( mNumberOfColumns == 0 )
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

			writeStyleClass( 1, metawidgetTag );

			int colspan = 1;

			if ( !labelRendered )
				colspan = 2;

			// Metawidgets and large components span all columns

			if ( tag instanceof MetawidgetTag || ( attributes != null && TRUE.equals( attributes.get( LARGE ) ) ))
			{
				colspan = ( ( mNumberOfColumns - 1 ) * LABEL_AND_COMPONENT_AND_REQUIRED ) + 1;
				state.currentColumn = mNumberOfColumns;
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
			writeStyleClass( 2, metawidgetTag );
			writer.write( ">" );

			writer.write( layoutRequired( attributes, metawidgetTag ) );

			writer.write( "</td>" );

			// End the row, if necessary

			if ( state.currentColumn >= mNumberOfColumns )
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

	protected boolean layoutLabel( String elementName, Map<String, String> attributes, MetawidgetTag metawidgetTag )
	{
		String labelText = metawidgetTag.getLabelString( attributes );

		if ( labelText == null )
			return false;

		try
		{
			JspWriter writer = metawidgetTag.getPageContext().getOut();

			// Output a (possibly localized) label

			writer.write( "<th" );
			writeStyleClass( 0, metawidgetTag );
			writer.write( ">" );

			if ( SimpleLayoutUtils.needsLabel( labelText, elementName ) )
			{
				writer.write( labelText );
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

			int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, mNumberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED );
			writer.write( String.valueOf( colspan ) );

			writer.write( "\"" );

			if ( mSectionStyleClass != null )
			{
				writer.write( " class=\"" );
				writer.write( mSectionStyleClass );
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

			// Reset to first column

			getState( metawidgetTag ).currentColumn = 1;
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

	protected void writeStyleClass( int styleClass, MetawidgetTag metawidgetTag )
	{
		if ( mColumnStyleClasses == null || mColumnStyleClasses.length <= styleClass )
			return;

		String columnClass = mColumnStyleClasses[styleClass];

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
		public int			currentColumn;

		public String		currentSection;

		public Set<String>	hiddenFields;

		public String		tableType;
	}
}
