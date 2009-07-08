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
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.iface.MetawidgetException;
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
 * <li><code>tableStyle</code>
 * <li><code>tableStyleClass</code>
 * <li><code>columns<code> - number of columns. Each label/component pair is considered one column
 * <li><code>columnClasses</code> - comma delimited string of CSS style classes to apply to table
 * columns in order of: label, component, required
 * <li><code>labelStyle</code> - CSS styles to apply to label column
 * <li><code>componentStyle</code> - CSS styles to apply to component column
 * <li><code>requiredStyle</code> - CSS styles to apply to required column
 * <li><code>sectionStyle</code>
 * <li><code>sectionStyleClass</code>
 * <li><code>headerStyle</code>
 * <li><code>headerStyleClass</code>
 * <li><code>footerStyle</code>
 * <li><code>footerStyleClass</code>
 * <li><code>rowClasses</code>
 * <li><code>labelSuffix</code> - defaults to a colon
 * </ul>
 * <p>
 * The parameters <code>columns</code> and <code>columnClasses</code> might more properly be named
 * <code>numberOfColumns</code> and <code>columnStyleClasses</code>, but we are trying to follow the
 * <code>javax.faces.component.html.HtmlDataTable</code> convention.
 *
 * @author Richard Kennard
 */

@SuppressWarnings( "deprecation" )
public class HtmlTableLayoutRenderer
	extends HtmlLayoutRenderer
{
	//
	// Private statics
	//

	private final static String	TABLE_PREFIX						= "table-";

	private final static String	ROW_SUFFIX							= "-row";

	private final static String	LABEL_CELL_SUFFIX					= "-label-cell";

	private final static String	COMPONENT_CELL_SUFFIX				= "-cell";

	private final static String	KEY_CURRENT_COLUMN					= "currentColumn";

	private final static String	KEY_NUMBER_OF_COLUMNS				= "columns";

	private final static String	KEY_CURRENT_ROW						= "currentRow";

	private final static String	KEY_CURRENT_SECTION					= "currentSection";

	private final static String	KEY_LABEL_STYLE						= "labelStyle";

	private final static String	KEY_COMPONENT_STYLE					= "componentStyle";

	private final static String	KEY_REQUIRED_STYLE					= "requiredStyle";

	private final static String	KEY_SECTION_STYLE					= "sectionStyle";

	private final static String	KEY_SECTION_STYLE_CLASS				= "sectionStyleClass";

	private final static String	KEY_COLUMN_CLASSES					= "columnClasses";

	private final static String	KEY_ROW_CLASSES						= "rowClasses";

	private final static int	JUST_COMPONENT_AND_REQUIRED			= 2;

	private final static int	LABEL_AND_COMPONENT_AND_REQUIRED	= 3;

	//
	// Public methods
	//

	@Override
	public void reentrantEncodeBegin( FacesContext context, UIComponent component )
		throws IOException
	{
		super.reentrantEncodeBegin( context, component );

		ResponseWriter writer = context.getResponseWriter();

		layoutHiddenChildren( context, component );

		// Start table

		writer.startElement( "table", component );
		writer.writeAttribute( "id", component.getClientId( context ), "id" );

		// Styles

		writeStyleAndClass( component, writer, "table" );

		// Determine label, component, required styles

		UIParameter parameterLabelStyle = FacesUtils.findParameterWithName( component, KEY_LABEL_STYLE );

		if ( parameterLabelStyle != null )
			putState( KEY_LABEL_STYLE, parameterLabelStyle.getValue() );

		UIParameter parameterComponentStyle = FacesUtils.findParameterWithName( component, KEY_COMPONENT_STYLE );

		if ( parameterComponentStyle != null )
			putState( KEY_COMPONENT_STYLE, parameterComponentStyle.getValue() );

		UIParameter parameterRequiredStyle = FacesUtils.findParameterWithName( component, KEY_REQUIRED_STYLE );

		if ( parameterRequiredStyle != null )
			putState( KEY_REQUIRED_STYLE, parameterRequiredStyle.getValue() );

		// Determine section styles

		UIParameter parameterSectionStyle = FacesUtils.findParameterWithName( component, KEY_SECTION_STYLE );

		if ( parameterSectionStyle != null )
			putState( KEY_SECTION_STYLE, parameterSectionStyle.getValue() );

		UIParameter parameterSectionStyleClass = FacesUtils.findParameterWithName( component, KEY_SECTION_STYLE_CLASS );

		if ( parameterSectionStyleClass != null )
			putState( KEY_SECTION_STYLE_CLASS, parameterSectionStyleClass.getValue() );

		// Determine inner styles

		UIParameter parameterColumnClasses = FacesUtils.findParameterWithName( component, KEY_COLUMN_CLASSES );

		if ( parameterColumnClasses != null )
			putState( KEY_COLUMN_CLASSES, ( (String) parameterColumnClasses.getValue() ).split( StringUtils.SEPARATOR_COMMA ) );

		UIParameter parameterRowClasses = FacesUtils.findParameterWithName( component, KEY_ROW_CLASSES );

		if ( parameterRowClasses != null )
			putState( KEY_ROW_CLASSES, ( (String) parameterRowClasses.getValue() ).split( StringUtils.SEPARATOR_COMMA ) );

		// Determine number of columns

		UIParameter parameterColumns = FacesUtils.findParameterWithName( component, KEY_NUMBER_OF_COLUMNS );
		int columns;

		if ( parameterColumns == null )
		{
			columns = 1;
		}
		else
		{
			columns = Integer.parseInt( (String) parameterColumns.getValue() );

			if ( columns < 0 )
				throw MetawidgetException.newException( "columns must be >= 0" );
		}

		putState( KEY_NUMBER_OF_COLUMNS, columns );

		// Render header facet

		UIComponent componentHeader = component.getFacet( "header" );

		if ( componentHeader != null )
		{
			writer.startElement( "thead", component );
			writer.startElement( "tr", component );
			writer.startElement( "td", component );

			// Header spans multiples of label/component/required

			int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, columns * LABEL_AND_COMPONENT_AND_REQUIRED );
			writer.writeAttribute( "colspan", String.valueOf( colspan ), null );

			writeStyleAndClass( component, writer, "header" );

			// Render facet

			FacesUtils.render( context, componentHeader );

			writer.endElement( "td" );
			writer.endElement( "tr" );
			writer.endElement( "thead" );
		}

		// Render footer facet (XHTML requires TFOOT come before TBODY)

		UIComponent componentFooter = component.getFacet( "footer" );

		if ( componentFooter != null )
		{
			writer.startElement( "tfoot", component );
			writer.startElement( "tr", component );
			writer.startElement( "td", component );

			// Footer spans multiples of label/component/required

			int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, columns * LABEL_AND_COMPONENT_AND_REQUIRED );
			writer.writeAttribute( "colspan", String.valueOf( colspan ), null );

			writeStyleAndClass( component, writer, "footer" );

			// Render facet

			FacesUtils.render( context, componentFooter );

			writer.endElement( "td" );
			writer.endElement( "tr" );
			writer.endElement( "tfoot" );
		}

		writer.startElement( "tbody", component );
	}

	/**
	 * layout any hidden child components first, before the table.
	 */

	protected void layoutHiddenChildren( FacesContext context, UIComponent component )
		throws IOException
	{
		List<UIComponent> children = component.getChildren();

		for ( UIComponent componentChild : children )
		{
			if ( !( componentChild instanceof HtmlInputHidden ) )
				continue;

			FacesUtils.render( context, componentChild );
		}
	}

	@Override
	public void encodeChildren( FacesContext context, UIComponent component )
		throws IOException
	{
		Integer numberOfColumns = getState( KEY_NUMBER_OF_COLUMNS );

		// (layoutChildren may get called even if layoutBegin crashed. Try
		// to fail gracefully)

		if ( numberOfColumns == null )
			return;

		List<UIComponent> children = component.getChildren();

		// Next, for each child component...

		putState( KEY_CURRENT_COLUMN, 0 );
		putState( KEY_CURRENT_ROW, 0 );

		for ( UIComponent componentChild : children )
		{
			// ...that is visible...

			if ( componentChild instanceof UIStub )
			{
				boolean visibleChildren = false;

				for ( UIComponent stubChild : componentChild.getChildren() )
				{
					if ( !stubChild.isRendered() )
						continue;

					visibleChildren = true;
					break;
				}

				if ( !visibleChildren )
					continue;
			}

			if ( componentChild instanceof UIParameter )
				continue;

			if ( componentChild instanceof HtmlInputHidden )
				continue;

			if ( !componentChild.isRendered() )
				continue;

			// ...count columns...

			putState( KEY_CURRENT_COLUMN, ( (Integer) getState( KEY_CURRENT_COLUMN ) ) + 1 );

			// ...render a label...

			layoutBeforeChild( context, component, componentChild );

			// ...and render the component

			layoutChild( context, component, componentChild );
			layoutAfterChild( context, component, componentChild );
		}
	}

	@Override
	public void reentrantEncodeEnd( FacesContext context, UIComponent component )
		throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement( "tbody" );
		writer.endElement( "table" );
	}

	//
	// Protected methods
	//

	protected void layoutBeforeChild( FacesContext context, UIComponent component, UIComponent childComponent )
		throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();

		int currentColumn = (Integer) getState( KEY_CURRENT_COLUMN );
		int numberOfColumns = (Integer) getState( KEY_NUMBER_OF_COLUMNS );
		int currentRow = (Integer) getState( KEY_CURRENT_ROW );
		String cssId = getCssId( childComponent );

		// Section headings

		String currentSection = getState( KEY_CURRENT_SECTION );

		@SuppressWarnings( "unchecked" )
		Map<String, String> attributes = (Map<String, String>) childComponent.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );

		// (layoutBeforeChild may get called even if layoutBegin crashed. Try
		// to fail gracefully)

		if ( attributes != null )
		{
			String section = attributes.get( SECTION );

			if ( section != null && !section.equals( currentSection ) )
			{
				putState( KEY_CURRENT_SECTION, section );
				layoutSection( context, component, section, childComponent );
				putState( KEY_CURRENT_COLUMN, 1 );
			}

			// Large components get a whole row

			boolean largeComponent = ( component instanceof UIData || TRUE.equals( attributes.get( LARGE ) ) );

			if ( largeComponent && currentColumn != 1 )
			{
				writer.endElement( "tr" );
				currentColumn = 1;
			}
		}

		// Start a new row, if necessary

		if ( currentColumn == 1 || currentColumn > numberOfColumns )
		{
			putState( KEY_CURRENT_COLUMN, 1 );

			writer.startElement( "tr", component );

			if ( cssId != null )
				writer.writeAttribute( "id", TABLE_PREFIX + cssId + ROW_SUFFIX, null );

			writeRowStyleClass( writer, currentRow );
			putState( KEY_CURRENT_ROW, currentRow + 1 );
		}

		// Start the label column

		boolean labelWritten = layoutLabel( context, component, childComponent );

		// Zero-column layouts need an extra row
		// (though we colour it the same from a CSS perspective)

		if ( labelWritten && numberOfColumns == 0 )
		{
			writer.endElement( "tr" );
			writer.startElement( "tr", component );

			if ( cssId != null )
				writer.writeAttribute( "id", TABLE_PREFIX + cssId + ROW_SUFFIX + "2", null );

			writeRowStyleClass( writer, currentRow );
		}

		// Start the component column

		writer.startElement( "td", component );

		if ( cssId != null )
			writer.writeAttribute( "id", TABLE_PREFIX + cssId + COMPONENT_CELL_SUFFIX, null );

		// CSS

		String componentStyle = getState( KEY_COMPONENT_STYLE );

		if ( componentStyle != null )
			writer.writeAttribute( "style", componentStyle, null );

		writeColumnStyleClass( writer, 1 );

		// Colspan

		int colspan;

		// Metawidgets, tables and large components span all columns

		if ( childComponent instanceof UIMetawidget || childComponent instanceof UIData || ( attributes != null && TRUE.equals( attributes.get( LARGE ) ) ) )
		{
			colspan = ( numberOfColumns * LABEL_AND_COMPONENT_AND_REQUIRED ) - 2;
			putState( KEY_CURRENT_COLUMN, numberOfColumns );

			if ( !labelWritten )
				colspan++;

			// Nested table Metawidgets span the required column too (as they have their own required column)

			if ( childComponent instanceof UIMetawidget && "table".equals( childComponent.getRendererType() ))
				colspan++;
		}

		// Components without labels span two columns

		else if ( !labelWritten )
		{
			colspan = 2;
		}

		// Everyone else spans just one

		else
		{
			colspan = 1;
		}

		if ( colspan > 1 )
			writer.writeAttribute( "colspan", String.valueOf( colspan ), null );
	}

	/**
	 * @return whether a label was written
	 */

	@Override
	protected boolean layoutLabel( FacesContext context, UIComponent component, UIComponent componentNeedingLabel )
		throws IOException
	{
		@SuppressWarnings( "unchecked" )
		Map<String, String> attributes = (Map<String, String>) componentNeedingLabel.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );
		String label = ( (UIMetawidget) componentNeedingLabel.getParent() ).getLabelString( context, attributes );

		if ( label == null )
			return false;

		ResponseWriter writer = context.getResponseWriter();
		writer.startElement( "th", component );

		String cssId = getCssId( componentNeedingLabel );
		if ( cssId != null )
			writer.writeAttribute( "id", TABLE_PREFIX + cssId + LABEL_CELL_SUFFIX, null );

		// CSS

		String labelStyle = getState( KEY_LABEL_STYLE );

		if ( labelStyle != null )
			writer.writeAttribute( "style", labelStyle, null );

		writeColumnStyleClass( writer, 0 );

		super.layoutLabel( context, component, componentNeedingLabel );

		writer.endElement( "th" );

		return true;
	}

	protected void layoutSection( FacesContext context, UIComponent component, String section, UIComponent childComponent )
		throws IOException
	{
		// Blank section?

		if ( "".equals( section ) )
			return;

		ResponseWriter writer = context.getResponseWriter();

		writer.startElement( "tr", component );
		writer.startElement( "th", component );

		// Sections span multiples of label/component/required

		int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, ( (Integer) getState( KEY_NUMBER_OF_COLUMNS ) ) * LABEL_AND_COMPONENT_AND_REQUIRED );
		writer.writeAttribute( "colspan", String.valueOf( colspan ), null );

		// CSS

		String sectionStyle = getState( KEY_SECTION_STYLE );

		if ( sectionStyle != null )
			writer.writeAttribute( "style", sectionStyle, null );

		String sectionStyleClass = getState( KEY_SECTION_STYLE_CLASS );

		if ( sectionStyleClass != null )
			writer.writeAttribute( "class", sectionStyleClass, null );

		// Section name (possibly localized)

		HtmlOutputText output = (HtmlOutputText) context.getApplication().createComponent( "javax.faces.HtmlOutputText" );

		String localizedSection = ( (UIMetawidget) childComponent.getParent() ).getLocalizedKey( context, StringUtils.camelCase( section ) );

		if ( localizedSection != null )
			output.setValue( localizedSection );
		else
			output.setValue( section );

		FacesUtils.render( context, output );

		writer.endElement( "th" );
		writer.endElement( "tr" );
	}

	protected void layoutAfterChild( FacesContext context, UIComponent component, UIComponent childComponent )
		throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();

		// End the component column

		writer.endElement( "td" );

		// Render the 'required' column

		if ( ( childComponent instanceof UIMetawidget && "table".equals( childComponent.getRendererType() ) ) || !childComponent.getAttributes().containsKey( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA ))
		{
			// (except embedded Metawidgets, which have their own required
			// column)
		}
		else
		{
			writer.startElement( "td", component );

			// CSS

			String requiredStyle = getState( KEY_REQUIRED_STYLE );

			if ( requiredStyle != null )
				writer.writeAttribute( "style", requiredStyle, null );

			writeColumnStyleClass( writer, 2 );

			layoutRequired( context, component, childComponent );

			writer.endElement( "td" );
		}

		// End the row, if necessary

		if ( ( (Integer) getState( KEY_CURRENT_COLUMN ) ).intValue() >= ( (Integer) getState( KEY_NUMBER_OF_COLUMNS ) ).intValue() )
		{
			putState( KEY_CURRENT_COLUMN, 0 );
			writer.endElement( "tr" );
		}
	}

	protected void layoutRequired( FacesContext context, UIComponent component, UIComponent child )
		throws IOException
	{
		@SuppressWarnings( "unchecked" )
		Map<String, String> attributes = (Map<String, String>) child.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );

		ResponseWriter writer = context.getResponseWriter();

		if ( attributes != null )
		{
			if ( TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY )) && !((UIMetawidget) component ).isReadOnly() )
			{
				// (UIStubs can have attributes="required: true")

				if ( child instanceof UIInput || child instanceof UIStub )
				{
					writer.write( "*" );
					return;
				}
			}
		}

		// Render an empty div, so that the CSS can force it to a certain
		// width if desired for the layout (browsers seem to not respect
		// widths set on empty table columns)

		writer.startElement( "div", component );
		writer.endElement( "div" );
	}

	protected String getCssId( UIComponent component )
	{
		ValueBinding binding = component.getValueBinding( "value" );

		if ( binding == null )
			return null;

		return StringUtils.camelCase( FacesUtils.unwrapExpression( binding.getExpressionString() ), StringUtils.SEPARATOR_DOT_CHAR );
	}

	protected void writeColumnStyleClass( ResponseWriter writer, int columnStyleClass )
		throws IOException
	{
		String[] columnClasses = getState( KEY_COLUMN_CLASSES );

		// Note: As per the JSF spec, columnClasses do not repeat like rowClasses do. See...
		//
		// http://java.sun.com/javaee/javaserverfaces/1.2_MR1/docs/renderkitdocs/HTML_BASIC/javax.faces.Datajavax.faces.Table.html
		//
		// ...where it says 'If the number of [styleClasses] is less than the number of
		// columns specified in the "columns" attribute, no "class" attribute is output for each
		// column greater than the number of [styleClasses]'

		if ( columnClasses == null || columnClasses.length <= columnStyleClass )
			return;

		String columnClass = columnClasses[columnStyleClass];

		if ( columnClass.length() == 0 )
			return;

		writer.writeAttribute( "class", columnClass.trim(), null );
	}

	protected void writeRowStyleClass( ResponseWriter writer, int rowStyleClass )
		throws IOException
	{
		String[] rowClasses = getState( KEY_ROW_CLASSES );

		if ( rowClasses == null )
			return;

		String rowClass = rowClasses[rowStyleClass % rowClasses.length];

		if ( rowClass.length() == 0 )
			return;

		writer.writeAttribute( "class", rowClass.trim(), null );
	}
}
