// Metawidget (licensed under LGPL)
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

package org.metawidget.faces.renderkit.html;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.SimpleLayoutUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to arrange components in a table, with one column for labels and another for the
 * component.
 * <p>
 * This implementation recognizes the following <code>&lt;f:facet&gt;</code> names:
 * <p>
 * <ul>
 * <li><code>header<code></li>
 * <li><code>footer<code></li>
 * </ul>
 * <p>
 * This implementation recognizes the following <code>&lt;f:param&gt;</code> parameters:
 * <p>
 * <ul>
 * <li><code>tableStyle</code> - CSS styles to apply to outer table tag
 * <li><code>tableStyleClass</code> - CSS style class to apply to outer table tag
 * <li><code>columns<code> - number of columns. Each label/component pair is considered one column
 * <li><code>columnClasses</code> - comma delimited string of CSS style classes to apply to table
 * columns in order of: label, component, required
 * <li><code>labelStyle</code> - CSS styles to apply to label column
 * <li><code>componentStyle</code> - CSS styles to apply to component column
 * <li><code>requiredStyle</code> - CSS styles to apply to required column (ie. the star)
 * <li><code>headerStyle</code> - CSS styles to apply to table header
 * <li><code>headerStyleClass</code> - CSS style class to apply to table header
 * <li><code>footerStyle</code> - CSS styles to apply to table footer
 * <li><code>footerStyleClass</code> - CSS style class to apply to table footer
 * <li><code>rowClasses</code> - comma delimited string of CSS style classes to apply to alternating
 * table rows
 * <li><code>labelSuffix</code> - suffix to put after the label name. Defaults to a colon (ie.
 * 'Name:')
 * </ul>
 * <p>
 * The parameters <code>columns</code> and <code>columnClasses</code> might more properly be named
 * <code>numberOfColumns</code> and <code>columnStyleClasses</code>, but we are trying to follow the
 * <code>javax.faces.component.html.HtmlDataTable</code> convention.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@SuppressWarnings( "deprecation" )
public class HtmlTableLayoutRenderer
	extends HtmlLayoutRenderer {

	//
	// Private statics
	//

	private static final String	TABLE_ID_PREFIX						= "table-";

	private static final String	ROW_ID_SUFFIX						= "-row";

	private static final String	LABEL_CELL_ID_SUFFIX				= "-label-cell";

	private static final String	COMPONENT_CELL_ID_SUFFIX			= "-cell";

	private static final int	JUST_COMPONENT_AND_REQUIRED			= 2;

	private static final int	LABEL_AND_COMPONENT_AND_REQUIRED	= 3;

	//
	// Public methods
	//

	@Override
	public void encodeBegin( FacesContext context, UIComponent metawidgetComponent )
		throws IOException {

		UIMetawidget metawidget = (UIMetawidget) metawidgetComponent;
		metawidget.putClientProperty( HtmlTableLayoutRenderer.class, null );
		super.encodeBegin( context, metawidget );

		ResponseWriter writer = context.getResponseWriter();

		// Start table or, if there are hidden fields, a div around the table/hidden field
		// siblings. It is very important the clientId is written on to a 'root' node, else
		// AJAX will not correctly re-render the whole block

		if ( hasHtmlInputHiddenChildren( metawidget ) ) {
			writer.startElement( "div", metawidget );
			writer.writeAttribute( "id", metawidget.getClientId( context ), "id" );
			layoutHtmlInputHiddenChildren( context, metawidget );
			writer.startElement( "table", metawidget );
		} else {
			writer.startElement( "table", metawidget );
			writer.writeAttribute( "id", metawidget.getClientId( context ), "id" );
		}

		// Styles

		writeStyleAndClass( metawidget, writer, "table" );

		// Determine label, component, required styles

		State state = getState( metawidget );
		state.labelStyle = metawidget.getParameter( "labelStyle" );
		state.componentStyle = metawidget.getParameter( "componentStyle" );
		state.requiredStyle = metawidget.getParameter( "requiredStyle" );

		// Determine inner styles

		String columnClassesParameter = metawidget.getParameter( "columnClasses" );

		if ( columnClassesParameter != null ) {
			state.columnClasses = columnClassesParameter.split( StringUtils.SEPARATOR_COMMA );
		}

		String rowClassesParameter = metawidget.getParameter( "rowClasses" );

		if ( rowClassesParameter != null ) {
			state.rowClasses = rowClassesParameter.split( StringUtils.SEPARATOR_COMMA );
		}

		// Determine number of columns

		String columnsParameter = metawidget.getParameter( "columns" );

		if ( columnsParameter != null ) {
			state.columns = Integer.parseInt( columnsParameter );

			if ( state.columns < 0 ) {
				throw LayoutException.newException( "columns must be >= 0" );
			}
		}

		// Render header facet

		UIComponent componentHeader = metawidget.getFacet( "header" );

		if ( componentHeader != null ) {
			writer.startElement( "thead", metawidget );
			writer.startElement( "tr", metawidget );
			writer.startElement( "td", metawidget );

			// Header spans multiples of label/component/required

			int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, state.columns * LABEL_AND_COMPONENT_AND_REQUIRED );
			writer.writeAttribute( "colspan", String.valueOf( colspan ), null );

			writeStyleAndClass( metawidget, writer, "header" );

			// Render facet

			FacesUtils.render( context, componentHeader );

			writer.endElement( "td" );
			writer.endElement( "tr" );
			writer.endElement( "thead" );
		}

		// Render footer facet (XHTML requires TFOOT come before TBODY)

		UIComponent componentFooter = metawidget.getFacet( "footer" );

		if ( componentFooter != null ) {
			writer.startElement( "tfoot", metawidget );
			writer.startElement( "tr", metawidget );
			writer.startElement( "td", metawidget );

			// Footer spans multiples of label/component/required

			int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, state.columns * LABEL_AND_COMPONENT_AND_REQUIRED );
			writer.writeAttribute( "colspan", String.valueOf( colspan ), null );

			writeStyleAndClass( metawidget, writer, "footer" );

			// Render facet

			FacesUtils.render( context, componentFooter );

			writer.endElement( "td" );
			writer.endElement( "tr" );
			writer.endElement( "tfoot" );
		}

		writer.startElement( "tbody", metawidget );
	}

	/**
	 * Determines if the Metawidget has children which are instances of
	 * <code>javax.faces.component.html.HtmlInputHidden</code>. If so, the hidden fields will be
	 * written as siblings of the &lt;table&gt; and the whole thing wrapped in an outer
	 * &lt;div&gt;.
	 */

	protected boolean hasHtmlInputHiddenChildren( UIComponent metawidget ) {

		for ( UIComponent componentChild : metawidget.getChildren() ) {
			if ( componentChild instanceof HtmlInputHidden ) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Layout any <code>javax.faces.component.html.HtmlInputHidden</code> children first, before the
	 * &lt;table&gt;.
	 */

	protected void layoutHtmlInputHiddenChildren( FacesContext context, UIComponent metawidget )
		throws IOException {

		for ( UIComponent componentChild : metawidget.getChildren() ) {
			if ( !( componentChild instanceof HtmlInputHidden ) ) {
				continue;
			}

			FacesUtils.render( context, componentChild );
		}
	}

	@Override
	public void encodeChildren( FacesContext context, UIComponent metawidget )
		throws IOException {

		State state = getState( metawidget );

		// (layoutChildren may get called even if layoutBegin crashed. Try
		// to fail gracefully)

		if ( state == null ) {
			return;
		}

		List<UIComponent> children = metawidget.getChildren();

		// Next, for each child component...

		state.currentColumn = 0;
		state.currentRow = 0;

		for ( UIComponent componentChild : children ) {
			// ...that is visible...

			if ( componentChild instanceof UIStub ) {
				boolean visibleChildren = false;

				for ( UIComponent stubChild : componentChild.getChildren() ) {
					if ( !stubChild.isRendered() ) {
						continue;
					}

					visibleChildren = true;
					break;
				}

				if ( !visibleChildren ) {
					continue;
				}
			}

			if ( componentChild instanceof UIParameter ) {
				continue;
			}

			if ( componentChild instanceof HtmlInputHidden ) {
				continue;
			}

			if ( !componentChild.isRendered() ) {
				continue;
			}

			// ...count columns...

			state.currentColumn++;

			// ...render a label...

			layoutBeforeChild( context, metawidget, componentChild );

			// ...and render the component

			layoutChild( context, metawidget, componentChild );
			layoutAfterChild( context, metawidget, componentChild );
		}
	}

	@Override
	public void encodeEnd( FacesContext context, UIComponent metawidget )
		throws IOException {

		ResponseWriter writer = context.getResponseWriter();
		writer.endElement( "tbody" );
		writer.endElement( "table" );

		// End div around the whole table

		if ( hasHtmlInputHiddenChildren( metawidget ) ) {
			writer.endElement( "div" );
		}
	}

	//
	// Protected methods
	//

	protected void layoutBeforeChild( FacesContext context, UIComponent metawidget, UIComponent childComponent )
		throws IOException {

		ResponseWriter writer = context.getResponseWriter();

		String cssId = getCssId( childComponent );

		// Section headings

		@SuppressWarnings( "unchecked" )
		Map<String, String> metadataAttributes = (Map<String, String>) childComponent.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );

		// (layoutBeforeChild may get called even if layoutBegin crashed. Try
		// to fail gracefully)

		State state = getState( metawidget );

		if ( metadataAttributes != null ) {
			// Large components get a whole row

			boolean largeComponent = ( metawidget instanceof UIData || TRUE.equals( metadataAttributes.get( LARGE ) ) );

			if ( largeComponent && state.currentColumn != 1 ) {
				writer.endElement( "tr" );
				state.currentColumn = 1;
			}
		}

		// Start a new row, if necessary

		if ( state.currentColumn == 1 || state.currentColumn > state.columns ) {
			state.currentColumn = 1;

			writer.startElement( "tr", metawidget );

			if ( cssId != null ) {
				writer.writeAttribute( "id", TABLE_ID_PREFIX + cssId + ROW_ID_SUFFIX, null );
			}

			writeRowStyleClass( metawidget, writer, state.currentRow );
			state.currentRow++;
		}

		// Start the label column

		boolean labelWritten = layoutLabel( context, metawidget, childComponent );

		// Zero-column layouts need an extra row
		// (though we colour it the same from a CSS perspective)

		if ( labelWritten && state.columns == 0 ) {
			writer.endElement( "tr" );
			writer.startElement( "tr", metawidget );

			if ( cssId != null ) {
				writer.writeAttribute( "id", TABLE_ID_PREFIX + cssId + ROW_ID_SUFFIX + "2", null );
			}

			writeRowStyleClass( metawidget, writer, state.currentRow );
		}

		// Start the component column

		writer.startElement( "td", metawidget );

		if ( cssId != null ) {
			writer.writeAttribute( "id", TABLE_ID_PREFIX + cssId + COMPONENT_CELL_ID_SUFFIX, null );
		}

		// CSS

		if ( state.componentStyle != null ) {
			writer.writeAttribute( "style", state.componentStyle, null );
		}

		writeColumnStyleClass( metawidget, writer, 1 );

		// Colspan

		int colspan;

		// Metawidgets, tables and large components span all columns

		if ( childComponent instanceof UIMetawidget || childComponent instanceof UIData || SimpleLayoutUtils.isSpanAllColumns( metadataAttributes ) ) {
			colspan = ( state.columns * LABEL_AND_COMPONENT_AND_REQUIRED ) - 2;
			state.currentColumn = state.columns;

			if ( !labelWritten ) {
				colspan++;
			}

			// Nested table Metawidgets span the required column too (as they have their own
			// required column)

			if ( childComponent instanceof UIMetawidget && "table".equals( childComponent.getRendererType() ) ) {
				colspan++;
			}
		}

		// Components without labels span two columns

		else if ( !labelWritten ) {
			colspan = 2;
		}

		// Everyone else spans just one

		else {
			colspan = 1;
		}

		if ( colspan > 1 ) {
			writer.writeAttribute( "colspan", String.valueOf( colspan ), null );
		}
	}

	/**
	 * @return whether a label was written
	 */

	@Override
	protected boolean layoutLabel( FacesContext context, UIComponent metawidget, UIComponent componentNeedingLabel )
		throws IOException {

		if ( getLabelText( componentNeedingLabel ) == null ) {
			return false;
		}

		ResponseWriter writer = context.getResponseWriter();
		writer.startElement( "th", metawidget );

		String cssId = getCssId( componentNeedingLabel );
		if ( cssId != null ) {
			writer.writeAttribute( "id", TABLE_ID_PREFIX + cssId + LABEL_CELL_ID_SUFFIX, null );
		}

		// CSS

		State state = getState( metawidget );

		if ( state.labelStyle != null ) {
			writer.writeAttribute( "style", state.labelStyle, null );
		}

		writeColumnStyleClass( metawidget, writer, 0 );

		super.layoutLabel( context, metawidget, componentNeedingLabel );

		writer.endElement( "th" );

		return true;
	}

	protected void layoutAfterChild( FacesContext context, UIComponent metawidget, UIComponent childComponent )
		throws IOException {

		ResponseWriter writer = context.getResponseWriter();

		// End the component column

		writer.endElement( "td" );

		// Render the 'required' column

		State state = getState( metawidget );

		// (except embedded table Metawidgets, which have their own required column)

		if ( !( childComponent instanceof UIMetawidget && "table".equals( childComponent.getRendererType() ) ) && childComponent.getAttributes().containsKey( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA ) ) {

			writer.startElement( "td", metawidget );

			// CSS

			if ( state.requiredStyle != null ) {
				writer.writeAttribute( "style", state.requiredStyle, null );
			}

			writeColumnStyleClass( metawidget, writer, 2 );

			layoutRequired( context, metawidget, childComponent );

			writer.endElement( "td" );
		}

		// End the row, if necessary

		if ( state.currentColumn >= state.columns ) {
			state.currentColumn = 0;
			writer.endElement( "tr" );
		}
	}

	protected void layoutRequired( FacesContext context, UIComponent metawidget, UIComponent child )
		throws IOException {

		@SuppressWarnings( "unchecked" )
		Map<String, String> attributes = (Map<String, String>) child.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );

		ResponseWriter writer = context.getResponseWriter();

		if ( attributes != null ) {
			if ( TRUE.equals( attributes.get( REQUIRED ) ) && !WidgetBuilderUtils.isReadOnly( attributes ) && !( (UIMetawidget) metawidget ).isReadOnly() ) {
				// UIStubs can have attributes="required: true". UIMetawidgets with
				// rendererType="simple" can be over required fields

				if ( child instanceof UIInput || child instanceof UIStub || child instanceof UIMetawidget ) {
					writer.write( "*" );
					return;
				}
			}
		}

		// Render an empty div, so that the CSS can force it to a certain
		// width if desired for the layout (browsers seem to not respect
		// widths set on empty table columns)

		writer.startElement( "div", metawidget );
		writer.endElement( "div" );
	}

	protected String getCssId( UIComponent metawidget ) {

		ValueBinding binding = metawidget.getValueBinding( "value" );

		if ( binding == null ) {
			return null;
		}

		return StringUtils.camelCase( FacesUtils.unwrapExpression( binding.getExpressionString() ), StringUtils.SEPARATOR_DOT_CHAR );
	}

	protected void writeColumnStyleClass( UIComponent metawidget, ResponseWriter writer, int columnStyleClass )
		throws IOException {

		State state = getState( metawidget );

		// Note: As per the JSF spec, columnClasses do not repeat like rowClasses do. See...
		//
		// http://java.sun.com/javaee/javaserverfaces/1.2_MR1/docs/renderkitdocs/HTML_BASIC/javax.faces.Datajavax.faces.Table.html
		//
		// ...where it says 'If the number of [styleClasses] is less than the number of
		// columns specified in the "columns" attribute, no "class" attribute is output for each
		// column greater than the number of [styleClasses]'

		if ( state.columnClasses == null || state.columnClasses.length <= columnStyleClass ) {
			return;
		}

		String columnClass = state.columnClasses[columnStyleClass];

		if ( columnClass.length() == 0 ) {
			return;
		}

		writer.writeAttribute( "class", columnClass.trim(), null );
	}

	protected void writeRowStyleClass( UIComponent metawidget, ResponseWriter writer, int rowStyleClass )
		throws IOException {

		State state = getState( metawidget );

		if ( state.rowClasses == null ) {
			return;
		}

		String rowClass = state.rowClasses[rowStyleClass % state.rowClasses.length];

		if ( rowClass.length() == 0 ) {
			return;
		}

		writer.writeAttribute( "class", rowClass.trim(), null );
	}

	//
	// Private methods
	//

	/* package private */State getState( UIComponent metawidget ) {

		State state = (State) ( (UIMetawidget) metawidget ).getClientProperty( HtmlTableLayoutRenderer.class );

		if ( state == null ) {
			state = new State();
			( (UIMetawidget) metawidget ).putClientProperty( HtmlTableLayoutRenderer.class, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */static class State {

		/* package private */int		currentColumn;

		/* package private */int		columns	= 1;

		/* package private */int		currentRow;

		/* package private */String		labelStyle;

		/* package private */String		componentStyle;

		/* package private */String		requiredStyle;

		/* package private */String[]	columnClasses;

		/* package private */String[]	rowClasses;
	}
}
