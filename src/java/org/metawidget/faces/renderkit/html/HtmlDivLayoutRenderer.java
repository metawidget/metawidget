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
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to arrange widgets in HTML <code>DIV</code> tags, with one <code>DIV</code> per label and
 * per widget, and a wrapper <code>DIV</code> around both.
 * <p>
 * This implementation recognizes the following <code>&lt;f:param&gt;</code> parameters:
 * <p>
 * <ul>
 * <li><code>divStyleClasses</code> - comma separated list of style classes to apply to the DIVs, in
 * order of outer, label, required, component, errors
 * <li><code>outerStyle</code> - CSS styles to apply to the outer DIV
 * <li><code>labelStyle</code> - CSS styles to apply to the label DIV
 * <li><code>componentStyle</code> - this is the style applied to the DIV <em>around</em> the
 * component, not to the component itself. The component itself can be styled using the
 * <code>style</code> attribute on the Metawidget tag
 * <li><code>requiredStyle</code> - CSS styles to apply to the required DIV
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

	//
	// Public methods
	//

	@Override
	public void encodeBegin( FacesContext context, UIComponent metawidget )
		throws IOException
	{
		( (UIMetawidget) metawidget ).putClientProperty( HtmlDivLayoutRenderer.class, null );
		super.encodeBegin( context, metawidget );

		// Determine outer styles

		State state = getState( metawidget );
		UIParameter parameterOuterStyle = FacesUtils.findParameterWithName( metawidget, "outerStyle" );

		if ( parameterOuterStyle != null )
			state.outerStyle = (String) parameterOuterStyle.getValue();

		// Determine label styles

		UIParameter parameterLabelStyle = FacesUtils.findParameterWithName( metawidget, "labelStyle" );

		if ( parameterLabelStyle != null )
			state.labelStyle = (String) parameterLabelStyle.getValue();

		// Determine label styles

		UIParameter parameterRequiredStyle = FacesUtils.findParameterWithName( metawidget, "requiredStyle" );

		if ( parameterRequiredStyle != null )
			state.requiredStyle = (String) parameterRequiredStyle.getValue();

		// Determine component styles

		UIParameter parameterComponentStyle = FacesUtils.findParameterWithName( metawidget, "componentStyle" );

		if ( parameterComponentStyle != null )
			state.componentStyle = (String) parameterComponentStyle.getValue();

		// Determine style classes

		UIParameter parameterStyleClasses = FacesUtils.findParameterWithName( metawidget, "divStyleClasses" );

		if ( parameterStyleClasses != null )
			state.divStyleClasses = ( (String) parameterStyleClasses.getValue() ).split( StringUtils.SEPARATOR_COMMA );

		// Start component

		ResponseWriter writer = context.getResponseWriter();
		writer.startElement( "div", metawidget );
		writer.writeAttribute( "id", metawidget.getClientId( context ), "id" );
	}

	@Override
	public void encodeChildren( FacesContext context, UIComponent component )
		throws IOException
	{
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
	public void encodeEnd( FacesContext context, UIComponent metawidget )
		throws IOException
	{
		super.encodeEnd( context, metawidget );

		ResponseWriter writer = context.getResponseWriter();

		// Footer facet

		UIComponent componentFooter = metawidget.getFacet( "footer" );

		if ( componentFooter != null )
		{
			writer.startElement( "div", metawidget );
			writeStyleAndClass( metawidget, writer, "footer" );

			// Render facet

			FacesUtils.render( context, componentFooter );

			writer.endElement( "div" );
		}

		// End component

		writer.endElement( "div" );
	}

	@Override
	protected HtmlMessage createMessage( FacesContext context, UIComponent metawidget, String messageFor )
	{
		HtmlMessage message = super.createMessage( context, metawidget, messageFor );

		// Apply alternate style class (if any)

		if ( message.getStyleClass() == null )
		{
			State state = getState( metawidget );

			if ( state.divStyleClasses != null && state.divStyleClasses.length > 4 )
				message.setStyleClass( state.divStyleClasses[4] );
		}

		return message;
	}

	//
	// Protected methods
	//

	protected void layoutBeforeChild( FacesContext context, UIComponent metawidget, UIComponent componentChild )
		throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();

		State state = getState( metawidget );

		// Outer

		writer.startElement( "div", metawidget );

		if ( state.outerStyle != null )
			writer.writeAttribute( "style", state.outerStyle, null );

		writeStyleClass( metawidget, writer, 0 );

		// Label

		layoutLabel( context, metawidget, componentChild );

		// Component
		//
		// Note: it is debatable whether we should use DIVs inside DIVs or SPANs inside DIVs here.
		// We choose the former, and the JBoss Seam demos do it both ways (Hotel Booking the latter,
		// Groovy Hotel Booking the former)

		writer.startElement( "div", metawidget );

		if ( state.componentStyle != null )
			writer.writeAttribute( "style", state.componentStyle, null );

		writeStyleClass( metawidget, writer, 3 );
	}

	/**
	 * @return whether a label was written
	 */

	@Override
	protected boolean layoutLabel( FacesContext context, UIComponent metawidget, UIComponent componentNeedingLabel )
		throws IOException
	{
		if ( getLabelText( componentNeedingLabel ) == null )
			return false;

		ResponseWriter writer = context.getResponseWriter();

		writer.startElement( "div", metawidget );

		State state = getState( metawidget );

		if ( state.labelStyle != null )
			writer.writeAttribute( "style", state.labelStyle, null );

		writeStyleClass( metawidget, writer, 1 );

		super.layoutLabel( context, metawidget, componentNeedingLabel );

		layoutRequired( context, metawidget, componentNeedingLabel );

		writer.endElement( "div" );

		return true;
	}

	protected void layoutRequired( FacesContext context, UIComponent metawidget, UIComponent child )
		throws IOException
	{
		@SuppressWarnings( "unchecked" )
		Map<String, String> metadataAttributes = (Map<String, String>) child.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );

		if ( metadataAttributes == null )
			return;

		ResponseWriter writer = context.getResponseWriter();

		if ( TRUE.equals( metadataAttributes.get( REQUIRED ) ) && !TRUE.equals( metadataAttributes.get( READ_ONLY ) ) && !( (UIMetawidget) metawidget ).isReadOnly() )
		{
			writer.startElement( "span", metawidget );

			State state = getState( metawidget );
			String requiredStyle = metadataAttributes.get( state.requiredStyle );

			if ( requiredStyle != null )
				writer.writeAttribute( "style", requiredStyle, null );

			writeStyleClass( metawidget, writer, 2 );
			writer.write( "*" );
			writer.endElement( "span" );
		}
	}

	protected void layoutAfterChild( FacesContext context, UIComponent component, UIComponent childComponent )
		throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();

		writer.endElement( "div" );
		writer.endElement( "div" );
	}

	protected void writeStyleClass( UIComponent metawidget, ResponseWriter writer, int styleClass )
		throws IOException
	{
		State state = getState( metawidget );

		if ( state.divStyleClasses == null || state.divStyleClasses.length <= styleClass )
			return;

		String columnClass = state.divStyleClasses[styleClass];

		if ( columnClass.length() == 0 )
			return;

		writer.writeAttribute( "class", columnClass.trim(), null );
	}

	//
	// Private methods
	//

	private State getState( UIComponent metawidget )
	{
		State state = (State) ( (UIMetawidget) metawidget ).getClientProperty( HtmlDivLayoutRenderer.class );

		if ( state == null )
		{
			state = new State();
			( (UIMetawidget) metawidget ).putClientProperty( HtmlDivLayoutRenderer.class, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */static class State
	{
		/* package private */String		outerStyle;

		/* package private */String		labelStyle;

		/* package private */String		requiredStyle;

		/* package private */String		componentStyle;

		/* package private */String[]	divStyleClasses;
	}
}
