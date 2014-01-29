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

package org.metawidget.jsp.tagext.html.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.FacetTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.StubTag;
import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.SimpleLayoutUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to arrange widgets in a table, with one column for labels and another for the widget.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlTableLayout
	implements AdvancedLayout<Tag, BodyTag, MetawidgetTag> {

	//
	// Private statics
	//

	private static final String	TABLE_PREFIX						= "table-";

	private static final String	ROW_SUFFIX							= "-row";

	private static final String	CELL_SUFFIX							= "-cell";

	private static final int	JUST_COMPONENT_AND_REQUIRED			= 2;

	private static final int	LABEL_AND_COMPONENT_AND_REQUIRED	= 3;

	//
	// Private members
	//

	private final int			mNumberOfColumns;

	private final String		mTableStyle;

	private final String		mTableStyleClass;

	private final String[]		mColumnStyleClasses;

	private final String		mFooterStyle;

	private final String		mFooterStyleClass;

	//
	// Constructor
	//

	public HtmlTableLayout() {

		this( new HtmlTableLayoutConfig() );
	}

	public HtmlTableLayout( HtmlTableLayoutConfig config ) {

		mNumberOfColumns = config.getNumberOfColumns();
		mTableStyle = config.getTableStyle();
		mTableStyleClass = config.getTableStyleClass();
		mColumnStyleClasses = config.getColumnStyleClasses();
		mFooterStyle = config.getFooterStyle();
		mFooterStyleClass = config.getFooterStyleClass();
	}

	//
	// Public methods
	//

	public void onStartBuild( MetawidgetTag metawidgetTag ) {

		metawidgetTag.putClientProperty( HtmlTableLayout.class, null );
	}

	public void startContainerLayout( BodyTag container, MetawidgetTag metawidgetTag ) {

		State state = getState( metawidgetTag );

		try {
			// Start table

			JspWriter writer = metawidgetTag.getPageContext().getOut();
			writer.write( "<table" );

			// Id

			writer.write( " id=\"" );
			writer.write( TABLE_PREFIX );
			state.setTableType( StringUtils.camelCase( metawidgetTag.getPath(), StringUtils.SEPARATOR_DOT_CHAR ) );
			writer.write( state.getTableType() );
			writer.write( "\"" );

			// Styles

			if ( mTableStyle != null ) {
				writer.write( " style=\"" );
				writer.write( mTableStyle );
				writer.write( "\"" );
			}

			if ( mTableStyleClass != null ) {
				writer.write( " class=\"" );
				writer.write( mTableStyleClass );
				writer.write( "\"" );
			}

			writer.write( ">" );

			// Footer parameter (XHTML requires TFOOT to come before TBODY)

			FacetTag facetFooter = metawidgetTag.getFacet( "footer" );

			if ( facetFooter != null ) {
				writer.write( "\r\n<tfoot>" );
				writer.write( "<tr>" );
				writer.write( "<td colspan=\"" );

				// Footer spans multiples of label/component/required

				int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, mNumberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED );
				writer.write( String.valueOf( colspan ) );
				writer.write( "\"" );

				// CSS styles

				if ( mFooterStyle != null ) {
					writer.write( " style=\"" );
					writer.write( mFooterStyle );
					writer.write( "\"" );
				}

				if ( mFooterStyleClass != null ) {
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
		} catch ( Exception e ) {
			throw LayoutException.newException( e );
		}
	}

	public void layoutWidget( Tag tag, String elementName, Map<String, String> attributes, BodyTag containerTag, MetawidgetTag metawidgetTag ) {

		try {
			String literal = null;

			if ( tag instanceof StubTag ) {
				literal = ( (StubTag) tag ).getSavedBodyContent();

				// Ignore empty stubs
				//
				// Note: interestingly, you can use literal.isEmpty here and it still compiles and
				// works under JDK 1.5 (even though 'isEmpty' is as of 1.6). The compiler must be
				// inlining it or something. It seems a bit risky though!

				if ( literal == null || literal.length() == 0 ) {
					return;
				}
			} else {
				literal = JspUtils.writeTag( metawidgetTag.getPageContext(), tag, containerTag );
			}

			// If the String is just hidden fields...

			if ( JspUtils.isJustHiddenFields( literal ) ) {
				// ...store it up for later (eg. don't render a row in the table
				// and a label)

				State state = getState( metawidgetTag );
				state.addHiddenField( literal );

				return;
			}

			// Write child normally

			JspWriter writer = metawidgetTag.getPageContext().getOut();
			layoutBeforeChild( tag, elementName, attributes, metawidgetTag );
			writer.write( literal );
			layoutAfterChild( attributes, metawidgetTag );
		} catch ( Exception e ) {
			throw LayoutException.newException( e );
		}
	}

	public void endContainerLayout( BodyTag container, MetawidgetTag metawidgetTag ) {

		try {
			JspWriter writer = metawidgetTag.getPageContext().getOut();
			writer.write( "</tbody>" );
			writer.write( "</table>" );
		} catch ( IOException e ) {
			throw LayoutException.newException( e );
		}
	}

	public void onEndBuild( MetawidgetTag metawidgetTag ) {

		try {
			JspWriter writer = metawidgetTag.getPageContext().getOut();

			// Output any hidden fields

			State state = getState( metawidgetTag );

			if ( state.getHiddenFields() != null ) {
				for ( String hiddenField : state.getHiddenFields() ) {
					writer.write( "\r\n" );
					writer.write( hiddenField );
				}
			}
		} catch ( IOException e ) {
			throw LayoutException.newException( e );
		}
	}

	//
	// Protected methods
	//

	protected void layoutBeforeChild( Tag tag, String elementName, Map<String, String> attributes, MetawidgetTag metawidgetTag ) {

		State state = getState( metawidgetTag );
		state.incrementCurrentColumn();

		try {
			JspWriter writer = metawidgetTag.getPageContext().getOut();

			// Section headings

			// (layoutBeforeChild may get called even if layoutBegin crashed. Try
			// to fail gracefully)

			String id = null;

			if ( attributes != null ) {
				id = attributes.get( NAME );

				if ( id != null ) {
					id = StringUtils.capitalize( StringUtils.camelCase( id ) );
				}

				if ( SimpleLayoutUtils.isSpanAllColumns( attributes ) && state.getCurrentColumn() != 1 ) {
					writer.write( "</tr>" );
					state.setCurrentColumn( 1 );
				}
			}

			// Start a new row, if necessary

			if ( state.getCurrentColumn() == 1 || state.getCurrentColumn() > mNumberOfColumns ) {
				state.setCurrentColumn( 1 );

				writer.write( "\r\n<tr" );

				if ( id != null ) {
					writer.write( " id=\"" );
					writer.write( TABLE_PREFIX );
					writer.write( state.getTableType() );
					writer.write( id );
					writer.write( ROW_SUFFIX );
					writer.write( "\"" );
				}

				writer.write( ">" );
			}

			// Start the label column

			boolean labelWritten = layoutLabel( elementName, attributes, metawidgetTag );

			// Zero-column layouts need an extra row

			if ( mNumberOfColumns == 0 ) {
				writer.write( "</tr>\r\n<tr" );

				if ( id != null ) {
					writer.write( " id=\"" );
					writer.write( TABLE_PREFIX );
					writer.write( state.getTableType() );
					writer.write( id );
					writer.write( ROW_SUFFIX );
					writer.write( "2\"" );
				}

				writer.write( ">" );
			}

			// Start the component column

			writer.write( "<td" );

			if ( id != null ) {
				writer.write( " id=\"" );
				writer.write( TABLE_PREFIX );
				writer.write( state.getTableType() );
				writer.write( id );
				writer.write( CELL_SUFFIX );
				writer.write( "\"" );
			}

			writeStyleClass( 1, metawidgetTag );

			// Colspan

			int colspan;

			// Metawidgets, tables and large components span all columns

			if ( tag instanceof MetawidgetTag || SimpleLayoutUtils.isSpanAllColumns( attributes ) ) {
				colspan = ( mNumberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED ) - 2;
				state.setCurrentColumn( mNumberOfColumns );

				if ( !labelWritten ) {
					colspan++;
				}

				// Nested table Metawidgets span the required column too (as they have their own
				// required column)

				if ( tag instanceof MetawidgetTag ) {
					colspan++;
				}
			} else if ( !labelWritten ) {

				// Components without labels span two columns

				colspan = 2;
			} else {

				// Everyone else spans just one

				colspan = 1;
			}

			if ( colspan > 1 ) {
				writer.write( " colspan=\"" );
				writer.write( String.valueOf( colspan ) );
				writer.write( "\"" );
			}

			writer.write( ">" );
		} catch ( IOException e ) {
			throw LayoutException.newException( e );
		}
	}

	protected void layoutAfterChild( Map<String, String> attributes, MetawidgetTag metawidgetTag ) {

		try {
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

			if ( state.getCurrentColumn() >= mNumberOfColumns ) {
				state.setCurrentColumn( 0 );
				writer.write( "</tr>" );
			}
		} catch ( IOException e ) {
			throw LayoutException.newException( e );
		}
	}

	/**
	 * @return true if a label was rendered
	 */

	protected boolean layoutLabel( String elementName, Map<String, String> attributes, MetawidgetTag metawidgetTag ) {

		String labelText = metawidgetTag.getLabelString( attributes );

		if ( labelText == null ) {
			return false;
		}

		try {
			JspWriter writer = metawidgetTag.getPageContext().getOut();

			// Output a (possibly localized) label

			writer.write( "<th" );
			writeStyleClass( 0, metawidgetTag );
			writer.write( ">" );

			if ( SimpleLayoutUtils.needsLabel( labelText, elementName ) ) {
				writer.write( "<label>" );
				writer.write( labelText );
				writer.write( ":</label>" );
			}

			writer.write( "</th>" );

			return true;
		} catch ( IOException e ) {
			throw LayoutException.newException( e );
		}
	}

	protected String layoutRequired( Map<String, String> attributes, MetawidgetTag metawidgetTag ) {

		if ( attributes != null && TRUE.equals( attributes.get( REQUIRED ) ) && !WidgetBuilderUtils.isReadOnly( attributes ) && !metawidgetTag.isReadOnly() ) {
			return "*";
		}

		// Render an empty div, so that the CSS can force it to a certain
		// width if desired for the layout (browsers seem to not respect
		// widths set on empty table columns)
		//
		// Note: don't do <div/>, as we may not be XHTML

		return "<div></div>";
	}

	protected void writeStyleClass( int styleClass, MetawidgetTag metawidgetTag ) {

		if ( mColumnStyleClasses == null || mColumnStyleClasses.length <= styleClass ) {
			return;
		}

		String columnClass = mColumnStyleClasses[styleClass];

		if ( columnClass.length() == 0 ) {
			return;
		}

		try {
			JspWriter writer = metawidgetTag.getPageContext().getOut();

			writer.write( " class=\"" );
			writer.write( columnClass.trim() );
			writer.write( "\"" );
		} catch ( IOException e ) {
			throw LayoutException.newException( e );
		}
	}

	//
	// Private methods
	//

	private State getState( MetawidgetTag metawidget ) {

		State state = (State) metawidget.getClientProperty( HtmlTableLayout.class );

		if ( state == null ) {
			state = new State();
			metawidget.putClientProperty( HtmlTableLayout.class, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/* package private */static class State {

		//
		// Private members
		//

		private int				mCurrentColumn;

		private List<String>	mHiddenFields;

		private String			mTableType;

		//
		// Public methods
		//

		public int getCurrentColumn() {

			return mCurrentColumn;
		}

		public void setCurrentColumn( int currentColumn ) {

			mCurrentColumn = currentColumn;
		}

		public void incrementCurrentColumn() {

			mCurrentColumn++;
		}

		public List<String> getHiddenFields() {

			return mHiddenFields;
		}

		public void addHiddenField( String hiddenField ) {

			if ( mHiddenFields == null ) {
				mHiddenFields = CollectionUtils.newArrayList();
			}

			mHiddenFields.add( hiddenField );
		}

		public String getTableType() {

			return mTableType;
		}

		public void setTableType( String tableType ) {

			mTableType = tableType;
		}
	}
}
