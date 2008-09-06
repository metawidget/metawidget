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

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import org.metawidget.MetawidgetException;
import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to arrange components in a table, with one column for labels and another for the
 * component.
 * <p>
 * This implementation recognizes the following <code>&lt;f:facet&gt;</code> names:
 * <p>
 * <ul>
 * 	<li><code>header<code></li>
 * 	<li><code>footer<code></li>
 * </ul>
 * <p>
 * This implementation recognizes the following <code>&lt;f:param&gt;</code> parameters:
 * <p>
 * <ul>
 * <li><code>tableStyle</code>
 * <li><code>tableStyleClass</code>
 * <li><code>columns<code> - number of columns. Each label/component pair is considered one column
 * <li><code>columnClasses</code> - comma delimited string of CSS style classes to apply to
 * table columns in order of: label, component, required
 * <li><code>labelStyle</code>
 * <li><code>componentStyle</code>
 * <li><code>requiredStyle</code>
 * <li><code>sectionStyle</code>
 * <li><code>sectionStyleClass</code>
 * <li><code>headerStyle</code>
 * <li><code>headerStyleClass</code>
 * <li><code>footerStyle</code>
 * <li><code>footerStyleClass</code>
 * </ul>
 * <p>
 * The parameters <code>columns</code> and <code>columnClasses</code> might more properly be named
 * <code>numberOfColumns</code> and <code>columnStyleClasses</code>, but we are trying to follow
 * the <code>javax.faces.component.html.HtmlDataTable</code> convention.
 *
 * @author Richard Kennard
 */

@SuppressWarnings( "deprecation" )
public class HtmlTableLayoutRenderer
	extends HtmlLayoutRenderer
{
	//
	//
	// Private statics
	//
	//

	private final static String	TABLE_PREFIX						= "table-";

	private final static String	ROW_SUFFIX							= "-row";

	private final static String	LABEL_CELL_SUFFIX					= "-label-cell";

	private final static String	COMPONENT_CELL_SUFFIX				= "-cell";

	private final static String	KEY_CURRENT_COLUMN					= "currentColumn";

	private final static String	KEY_NUMBER_OF_COLUMNS				= "columns";

	private final static String	KEY_CURRENT_SECTION					= "currentSection";

	private final static String	KEY_LABEL_STYLE						= "labelStyle";

	private final static String	KEY_COMPONENT_STYLE					= "componentStyle";

	private final static String	KEY_REQUIRED_STYLE					= "requiredStyle";

	private final static String	KEY_SECTION_STYLE					= "sectionStyle";

	private final static String	KEY_SECTION_STYLE_CLASS				= "sectionStyleClass";

	private final static String	KEY_COLUMN_CLASSES					= "columnClasses";

	private final static int	JUST_COMPONENT_AND_REQUIRED			= 2;

	private final static int	LABEL_AND_COMPONENT_AND_REQUIRED	= 3;

	//
	//
	// Public methods
	//
	//

	@Override
	public void reentrantEncodeBegin( FacesContext context, UIComponent component )
		throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();

		layoutHiddenChildren( context, component );

		// Start table

		writer.write( "<table" );

		// Id

		String id = getId( component );

		if ( id != null )
		{
			writer.write( " id=\"" );
			writer.write( TABLE_PREFIX );
			writer.write( id );
			writer.write( "\"" );
		}

		// Styles

		writeStyleAndClass( component, writer, "table" );
		writer.write( ">" );

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

		// Header facet

		UIComponent componentHeader = component.getFacet( "header" );

		if ( componentHeader != null )
		{
			writer.write( "\r\n<thead>" );
			writer.write( "<tr>" );
			writer.write( "<td colspan=\"" );

			// Header spans multiples of label/component/required

			int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, columns * LABEL_AND_COMPONENT_AND_REQUIRED );
			writer.write( String.valueOf( colspan ) );

			writer.write( "\"" );
			writeStyleAndClass( component, writer, "header" );
			writer.write( ">" );

			// Render facet

			FacesUtils.render( context, componentHeader );

			writer.write( "</td>" );
			writer.write( "</tr>" );
			writer.write( "</thead>" );
		}

		// Footer facet (XHTML requires TFOOT come before TBODY)

		UIComponent componentFooter = component.getFacet( "footer" );

		if ( componentFooter != null )
		{
			writer.write( "\r\n<tfoot>" );
			writer.write( "<tr>" );
			writer.write( "<td colspan=\"" );

			// Footer spans multiples of label/component/required

			int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, columns * LABEL_AND_COMPONENT_AND_REQUIRED );
			writer.write( String.valueOf( colspan ) );

			writer.write( "\"" );
			writeStyleAndClass( component, writer, "footer" );
			writer.write( ">" );

			// Render facet

			FacesUtils.render( context, componentFooter );

			writer.write( "</td>" );
			writer.write( "</tr>" );
			writer.write( "</tfoot>" );
		}

		writer.write( "\r\n<tbody>" );
	}

	/**
	 * layout any hidden child components first, before the table.
	 */

	protected void layoutHiddenChildren( FacesContext context, UIComponent component )
		throws IOException
	{
		@SuppressWarnings( "unchecked" )
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
		Integer numberOfColumns = (Integer) getState( KEY_NUMBER_OF_COLUMNS );

		// (layoutChildren may get called even if layoutBegin crashed. Try
		// to fail gracefully)

		if ( numberOfColumns == null )
			return;

		@SuppressWarnings( "unchecked" )
		List<UIComponent> children = component.getChildren();

		// Next, for each child component...

		putState( KEY_CURRENT_COLUMN, 0 );

		for ( UIComponent componentChild : children )
		{
			// ...that is visible...

			if ( componentChild instanceof UIStub && componentChild.getChildCount() == 0 )
				continue;

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
		writer.write( "</tbody>" );
		writer.write( "\r\n</table>" );
	}

	//
	//
	// Protected methods
	//
	//

	protected void layoutBeforeChild( FacesContext context, UIComponent component, UIComponent componentChild )
		throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();

		int currentColumn = (Integer) getState( KEY_CURRENT_COLUMN );
		int numberOfColumns = (Integer) getState( KEY_NUMBER_OF_COLUMNS );
		String id = getId( componentChild );

		// Section headings

		String currentSection = (String) getState( KEY_CURRENT_SECTION );

		@SuppressWarnings( "unchecked" )
		Map<String, String> attributes = (Map<String, String>) componentChild.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );

		// (layoutBeforeChild may get called even if layoutBegin crashed. Try
		// to fail gracefully)

		if ( attributes != null )
		{
			String section = attributes.get( SECTION );

			if ( section != null && !section.equals( currentSection ) )
			{
				putState( KEY_CURRENT_SECTION, section );
				layoutSection( context, section, componentChild );
				putState( KEY_CURRENT_COLUMN, 1 );
			}

			// Large components get a whole row

			boolean largeComponent = ( component instanceof UIData || TRUE.equals( attributes.get( LARGE )));

			if ( largeComponent && currentColumn != 1 )
			{
				writer.write( "</tr>" );
				currentColumn = 1;
			}
		}

		// Start a new row, if necessary

		if ( currentColumn == 1 || currentColumn > numberOfColumns )
		{
			putState( KEY_CURRENT_COLUMN, 1 );

			writer.write( "\r\n<tr" );

			if ( id != null )
			{
				writer.write( " id=\"" );
				writer.write( TABLE_PREFIX );
				writer.write( id );
				writer.write( ROW_SUFFIX );
				writer.write( "\"" );
			}

			writer.write( ">" );
		}

		// Start the label column

		boolean labelWritten = layoutLabel( context, componentChild );

		// Zero-column layouts need an extra row

		if ( labelWritten && numberOfColumns == 0 )
		{
			writer.write( "</tr>\r\n<tr" );

			if ( id != null )
			{
				writer.write( " id=\"" );
				writer.write( TABLE_PREFIX );
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
			writer.write( id );
			writer.write( COMPONENT_CELL_SUFFIX );
			writer.write( "\"" );
		}

		// CSS

		String componentStyle = (String) getState( KEY_COMPONENT_STYLE );

		if ( componentStyle != null )
		{
			writer.write( " style=\"" );
			writer.write( componentStyle );
			writer.write( "\"" );
		}

		writeStyleClass( writer, 1 );

		int colspan = 1;

		if ( !labelWritten )
			colspan = 2;

		// Embedded Metawidgets span the component and the required column

		if ( ( componentChild instanceof UIMetawidget && "table".equals( componentChild.getRendererType() ) ) || componentChild.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA ) == null )
			colspan += JUST_COMPONENT_AND_REQUIRED;

		// Large components span all columns

		if ( numberOfColumns > 1 && attributes != null && TRUE.equals( attributes.get( "large" ) ) )
		{
			colspan += ( ( numberOfColumns - 1 ) * LABEL_AND_COMPONENT_AND_REQUIRED ) + 1;
			putState( KEY_CURRENT_COLUMN, numberOfColumns );
		}

		if ( colspan > 1 )
		{
			writer.write( " colspan=\"" );
			writer.write( String.valueOf( colspan ) );
			writer.write( "\"" );
		}

		writer.write( ">" );
	}

	/**
	 * @return whether a label was written
	 */

	protected boolean layoutLabel( FacesContext context, UIComponent componentNeedingLabel )
		throws IOException
	{
		@SuppressWarnings( "unchecked" )
		Map<String, String> attributes = (Map<String, String>) componentNeedingLabel.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );
		String label = ( (UIMetawidget) componentNeedingLabel.getParent() ).getLabelString( context, attributes );

		if ( label == null )
			return false;

		ResponseWriter writer = context.getResponseWriter();

		writer.write( "<th" );

		String id = getId( componentNeedingLabel );
		if ( id != null )
		{
			writer.write( " id=\"" );
			writer.write( TABLE_PREFIX );
			writer.write( id );
			writer.write( LABEL_CELL_SUFFIX );
			writer.write( "\"" );
		}

		// CSS

		String labelStyle = (String) getState( KEY_LABEL_STYLE );

		if ( labelStyle != null )
		{
			writer.write( " style=\"" );
			writer.write( labelStyle );
			writer.write( "\"" );
		}

		writeStyleClass( writer, 0 );
		writer.write( ">" );

		if ( !"".equals( label.trim() ) && !( componentNeedingLabel instanceof UICommand ))
		{
			HtmlOutputText componentLabel = (HtmlOutputText) context.getApplication().createComponent( "javax.faces.HtmlOutputText" );
			componentLabel.setValue( label + ':' );
			FacesUtils.render( context, componentLabel );
		}

		writer.write( "</th>" );

		return true;
	}

	protected void layoutSection( FacesContext context, String section, UIComponent childComponent )
		throws IOException
	{
		// Blank section?

		if ( "".equals( section ) )
			return;

		ResponseWriter writer = context.getResponseWriter();

		writer.write( "\r\n<tr>" );
		writer.write( "<th colspan=\"" );

		// Sections span multiples of label/component/required

		int colspan = Math.max( JUST_COMPONENT_AND_REQUIRED, ( (Integer) getState( KEY_NUMBER_OF_COLUMNS ) ) * LABEL_AND_COMPONENT_AND_REQUIRED );
		writer.write( String.valueOf( colspan ) );
		writer.write( "\"" );

		// CSS

		String sectionStyle = (String) getState( KEY_SECTION_STYLE );

		if ( sectionStyle != null )
		{
			writer.write( " style=\"" );
			writer.write( sectionStyle );
			writer.write( "\"" );
		}

		String sectionStyleClass = (String) getState( KEY_SECTION_STYLE_CLASS );

		if ( sectionStyleClass != null )
		{
			writer.write( " class=\"" );
			writer.write( sectionStyleClass );
			writer.write( "\"" );
		}

		writer.write( ">" );

		// Section name (possibly localized)

		UIOutput output = (UIOutput) context.getApplication().createComponent( "javax.faces.Output" );

		String localizedSection = ( (UIMetawidget) childComponent.getParent() ).getLocalizedKey( context, StringUtils.camelCase( section ) );

		if ( localizedSection != null )
			output.setValue( localizedSection );
		else
			output.setValue( section );

		FacesUtils.render( context, output );

		writer.write( "</th>" );
		writer.write( "</tr>" );
	}

	protected void layoutAfterChild( FacesContext context, UIComponent component, UIComponent childComponent )
		throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();

		// End the component column

		writer.write( "</td>" );

		// Render the 'required' column

		if ( ( childComponent instanceof UIMetawidget && "table".equals( childComponent.getRendererType() ) ) || childComponent.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA ) == null )
		{
			// (except embedded Metawidgets, which have their own required
			// column)
		}
		else
		{
			writer.write( "<td" );

			// CSS

			String requiredStyle = (String) getState( KEY_REQUIRED_STYLE );

			if ( requiredStyle != null )
			{
				writer.write( " style=\"" );
				writer.write( requiredStyle );
				writer.write( "\"" );
			}

			writeStyleClass( writer, 2 );
			writer.write( ">" );

			layoutRequired( context, component, childComponent );

			writer.write( "</td>" );
		}

		// End the row, if necessary

		if ( ( (Integer) getState( KEY_CURRENT_COLUMN ) ).intValue() >= ( (Integer) getState( KEY_NUMBER_OF_COLUMNS ) ).intValue() )
		{
			putState( KEY_CURRENT_COLUMN, 0 );
			writer.write( "</tr>" );
		}
	}

	protected void layoutRequired( FacesContext context, UIComponent component, UIComponent child )
		throws IOException
	{
		@SuppressWarnings( "unchecked" )
		Map<String, String> attributes = (Map<String, String>) child.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );

		if ( attributes == null )
			return;

		ResponseWriter writer = context.getResponseWriter();

		if ( TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY ) ) && !( (UIMetawidget) component ).isReadOnly() )
			writer.write( "*" );
	}

	protected String getId( UIComponent component )
	{
		ValueBinding binding = component.getValueBinding( "value" );

		if ( binding == null )
			return null;

		return StringUtils.camelCase( FacesUtils.unwrapValueReference( binding.getExpressionString() ), StringUtils.SEPARATOR_DOT_CHAR );
	}

	protected void writeStyleClass( ResponseWriter writer, int styleClass )
		throws IOException
	{
		String[] columnClasses = (String[]) getState( KEY_COLUMN_CLASSES );

		if ( columnClasses == null || columnClasses.length <= styleClass )
			return;

		String columnClass = columnClasses[styleClass];

		if ( columnClass.length() == 0 )
			return;

		writer.write( " class=\"" );
		writer.write( columnClass.trim() );
		writer.write( "\"" );
	}
}
