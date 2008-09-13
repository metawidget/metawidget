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
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to arrange widgets in HTML <code>DIV</code> tags, with one <code>DIV</code> per
 * label and per widget, and a wrapper <code>DIV</code> around both.
 * <p>
 * This implementation recognizes the following <code>&lt;f:param&gt;</code> parameters:
 * <p>
 * <ul>
 * <li><code>outerStyle</code>
 * <li><code>labelStyle</code>
 * <li><code>componentStyle</code> - this is the style applied to the DIV <em>around</em> the
 * widget, not to the widget itself. The widget itself can be styled using the
 * <code>style</code> attribute on the Metawidget tag
 * <li><code>divStyleClasses</code> - comma separated list of style classes to apply to the DIVs,
 * in order of outer, label, required, widget, errors
 * <li><code>footerStyle</code>
 * <li><code>footerStyleClass</code>
 * </ul>
 * <p>
 *
 * @author Richard Kennard
 */

public class HtmlDivLayoutRenderer
	extends HtmlLayoutRenderer
{
	//
	// Private statics
	//

	private final static String	KEY_OUTER_STYLE		= "outerStyle";

	private final static String	KEY_LABEL_STYLE		= "labelStyle";

	private final static String	KEY_REQUIRED_STYLE	= "requiredStyle";

	private final static String	KEY_COMPONENT_STYLE	= "componentStyle";

	private final static String	KEY_STYLE_CLASSES	= "divStyleClasses";

	//
	// Public methods
	//

	@Override
	public void reentrantEncodeBegin( FacesContext context, UIComponent component )
	{
		// Determine outer styles

		UIParameter parameterOuterStyle = FacesUtils.findParameterWithName( component, KEY_OUTER_STYLE );

		if ( parameterOuterStyle != null )
			putState( KEY_OUTER_STYLE, parameterOuterStyle.getValue() );

		// Determine label styles

		UIParameter parameterLabelStyle = FacesUtils.findParameterWithName( component, KEY_LABEL_STYLE );

		if ( parameterLabelStyle != null )
			putState( KEY_LABEL_STYLE, parameterLabelStyle.getValue() );

		// Determine label styles

		UIParameter parameterRequiredStyle = FacesUtils.findParameterWithName( component, KEY_REQUIRED_STYLE );

		if ( parameterRequiredStyle != null )
			putState( KEY_REQUIRED_STYLE, parameterRequiredStyle.getValue() );

		// Determine component styles

		UIParameter parameterComponentStyle = FacesUtils.findParameterWithName( component, KEY_COMPONENT_STYLE );

		if ( parameterComponentStyle != null )
			putState( KEY_COMPONENT_STYLE, parameterComponentStyle.getValue() );

		// Determine style classes

		String[] styleClasses = (String[]) getState( KEY_STYLE_CLASSES );

		if ( styleClasses == null )
		{
			UIParameter parameterStyleClasses = FacesUtils.findParameterWithName( component, KEY_STYLE_CLASSES );

			if ( parameterStyleClasses != null )
				putState( KEY_STYLE_CLASSES, ( (String) parameterStyleClasses.getValue() ).split( StringUtils.SEPARATOR_COMMA ) );
		}
	}

	@Override
	public void encodeChildren( FacesContext context, UIComponent component )
		throws IOException
	{
		@SuppressWarnings( "unchecked" )
		List<UIComponent> children = component.getChildren();

		// For each child component...

		for ( UIComponent childComponent : children )
		{
			// ...that is visible...

			if ( childComponent instanceof UIStub && childComponent.getChildCount() == 0 )
				continue;

			if ( childComponent instanceof UIParameter )
				continue;

			if ( !childComponent.isRendered() )
				continue;

			// ...(and is not a hidden field)...

			if ( childComponent instanceof HtmlInputHidden )
			{
				FacesUtils.render( context, childComponent );
				continue;
			}

			// ...render the label...

			layoutBeforeChild( context, component, childComponent );

			// ...and render the component

			layoutChild( context, component, childComponent );
			layoutAfterChild( context, component, childComponent );
		}
	}

	@Override
	public void reentrantEncodeEnd( FacesContext context, UIComponent component )
		throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();

		// Footer facet

		UIComponent componentFooter = component.getFacet( "footer" );

		if ( componentFooter != null )
		{
			writer.write( "\r\n<div" );
			writeStyleAndClass( component, writer, "footer" );
			writer.write( ">" );

			// Render facet

			FacesUtils.render( context, componentFooter );

			writer.write( "</div>" );
		}
	}

	@Override
	protected HtmlMessage createMessage( FacesContext context, UIComponent component, String messageFor )
	{
		HtmlMessage message = super.createMessage( context, component, messageFor );

		// Apply alternate style class (if any)

		if ( message.getStyleClass() == null )
		{
			String[] styleClasses = (String[]) getState( KEY_STYLE_CLASSES );

			if ( styleClasses != null && styleClasses.length > 4 )
				message.setStyleClass( styleClasses[4] );
		}

		return message;
	}

	//
	// Protected methods
	//

	protected void layoutBeforeChild( FacesContext context, UIComponent component, UIComponent componentChild )
		throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();

		// Outer

		writer.write( "\r\n<div" );

		String outerStyle = (String) getState( KEY_OUTER_STYLE );

		if ( outerStyle != null )
		{
			writer.write( " style=\"" );
			writer.write( outerStyle );
			writer.write( "\"" );
		}

		writeStyleClass( writer, 0 );
		writer.write( ">" );

		// Label

		layoutLabel( context, component, componentChild );

		// Component

		writer.write( "<div" );

		String componentStyle = (String) getState( KEY_COMPONENT_STYLE );

		if ( componentStyle != null )
		{
			writer.write( " style=\"" );
			writer.write( componentStyle );
			writer.write( "\"" );
		}

		writeStyleClass( writer, 3 );
		writer.write( ">" );
	}

	/**
	 * @return whether a label was written
	 */

	protected boolean layoutLabel( FacesContext context, UIComponent component, UIComponent componentNeedingLabel )
		throws IOException
	{
		@SuppressWarnings( "unchecked" )
		Map<String, String> attributes = (Map<String, String>) componentNeedingLabel.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );
		String label = ( (UIMetawidget) componentNeedingLabel.getParent() ).getLabelString( context, attributes );

		if ( label == null )
			return false;

		ResponseWriter writer = context.getResponseWriter();

		writer.write( "<div" );

		String labelStyle = (String) getState( KEY_LABEL_STYLE );

		if ( labelStyle != null )
		{
			writer.write( " style=\"" );
			writer.write( labelStyle );
			writer.write( "\"" );
		}

		writeStyleClass( writer, 1 );
		writer.write( ">" );

		if ( !"".equals( label.trim() ) )
		{
			HtmlOutputText componentLabel = (HtmlOutputText) context.getApplication().createComponent( "javax.faces.HtmlOutputText" );
			componentLabel.setValue( label + ':' );
			FacesUtils.render( context, componentLabel );
		}

		layoutRequired( context, component, componentNeedingLabel );

		writer.write( "</div>" );

		return true;
	}

	protected void layoutRequired( FacesContext context, UIComponent component, UIComponent child )
		throws IOException
	{
		@SuppressWarnings( "unchecked" )
		Map<String, String> attributes = (Map<String, String>) child.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );

		if ( attributes == null )
			return;

		ResponseWriter writer = context.getResponseWriter();

		if ( TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY )) && !((UIMetawidget) component).isReadOnly() )
		{
			writer.write( "<span" );

			String requiredStyle = (String) getState( KEY_REQUIRED_STYLE );

			if ( requiredStyle != null )
			{
				writer.write( " style=\"" );
				writer.write( requiredStyle );
				writer.write( "\"" );
			}

			writeStyleClass( writer, 2 );
			writer.write( ">*</span>" );
		}
	}

	protected void layoutAfterChild( FacesContext context, UIComponent component, UIComponent childComponent )
		throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();

		writer.write( "</div>" );
		writer.write( "</div>" );
	}

	protected void writeStyleClass( ResponseWriter writer, int styleClass )
		throws IOException
	{
		String[] styleClasses = (String[]) getState( KEY_STYLE_CLASSES );

		if ( styleClasses == null || styleClasses.length <= styleClass )
			return;

		String columnClass = styleClasses[styleClass];

		if ( columnClass.length() == 0 )
			return;

		writer.write( " class=\"" );
		writer.write( columnClass.trim() );
		writer.write( "\"" );
	}

}
