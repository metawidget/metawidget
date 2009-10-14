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
import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.layout.impl.LayoutUtils;

/**
 * Base class for all JSF HTML layout renderers. This implementation recognizes the following
 * <code>&lt;f:param&gt;</code> parameters:
 * <p>
 * <ul>
 * <li><code>inlineMessages</code> - whether to wrap input components with inline &lt;h:message&gt;
 * tags. True by default
 * <li><code>messageStyle</code>
 * <li><code>messageStyleClass</code>
 * <li><code>labelSuffix</code>
 * </ul>
 *
 * @author Richard Kennard
 */

public abstract class HtmlLayoutRenderer
	extends Renderer
{
	//
	// Public methods
	//

	@Override
	public void encodeBegin( FacesContext context, UIComponent metawidget )
		throws IOException
	{
		( (UIMetawidget) metawidget ).putClientProperty( HtmlLayoutRenderer.class, null );
		super.encodeBegin( context, metawidget );

		// Determine label suffix

		State state = getState( metawidget );
		UIParameter parameterLabelSuffix = FacesUtils.findParameterWithName( metawidget, "labelSuffix" );

		if ( parameterLabelSuffix != null )
			state.labelSuffix = (String) parameterLabelSuffix.getValue();

		// Using inline messages?

		UIParameter useInlineMessagesParameter = FacesUtils.findParameterWithName( metawidget, "useInlineMessages" );

		if ( useInlineMessagesParameter != null )
			state.useInlineMessages = Boolean.valueOf( (String) useInlineMessagesParameter.getValue() );

		// Message styles

		UIParameter messageStyleParameter = FacesUtils.findParameterWithName( metawidget, "messageStyle" );

		if ( messageStyleParameter != null )
			state.messageStyle = (String) messageStyleParameter.getValue();

		UIParameter messageStyleClassParameter = FacesUtils.findParameterWithName( metawidget, "messageStyleClass" );

		if ( messageStyleClassParameter != null )
			state.messageStyleClass = (String) messageStyleClassParameter.getValue();
	}

	/**
	 * Denotes that this Renderer renders its own children (eg. JSF should not call
	 * <code>encodeBegin</code> on each child for us)
	 */

	@Override
	public boolean getRendersChildren()
	{
		return true;
	}

	//
	// Protected methods
	//

	protected String getLabelText( UIComponent componentNeedingLabel )
	{
		@SuppressWarnings( "unchecked" )
		Map<String, String> metadataAttributes = (Map<String, String>) componentNeedingLabel.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );
		return ( (UIMetawidget) componentNeedingLabel.getParent() ).getLabelString( metadataAttributes );
	}

	/**
	 * Render the label text. Rendering is done via a <code>HtmlOutputText</code> renderer, so that
	 * the label may contain value expressions, such as <code>UiLabel( "#{foo.name}'s name" )</code>.
	 *
	 * @return whether a label was written
	 */

	@SuppressWarnings( "deprecation" )
	protected boolean layoutLabel( FacesContext context, UIComponent metawidget, UIComponent componentNeedingLabel )
		throws IOException
	{
		if ( componentNeedingLabel instanceof UICommand )
			return false;

		String labelText = getLabelText( componentNeedingLabel );

		if ( !LayoutUtils.needsLabel( labelText, null ))
			return false;

		// Render the label

		HtmlOutputText componentLabel = (HtmlOutputText) context.getApplication().createComponent( "javax.faces.HtmlOutputText" );

		State state = getState( metawidget );

		if ( state.labelSuffix == null )
			state.labelSuffix = ":";

		if ( labelText.indexOf( "#{" ) != -1 )
			componentLabel.setValueBinding( "value", context.getApplication().createValueBinding( labelText + state.labelSuffix ) );
		else
			componentLabel.setValue( labelText + state.labelSuffix );

		FacesUtils.render( context, componentLabel );
		return true;
	}

	protected void layoutChild( FacesContext context, UIComponent metawidget, UIComponent childComponent )
		throws IOException
	{
		FacesUtils.render( context, childComponent );

		// No need for inline messages?

		if ( childComponent instanceof HtmlInputHidden )
			return;

		String messageFor = childComponent.getId();

		if ( childComponent instanceof UIMetawidget )
		{
			// (drill into single component UIMetawidgets)

			UIComponent childOfChild = null;

			for ( UIComponent child : childComponent.getChildren() )
			{
				if ( child instanceof UIParameter )
					continue;

				if ( childOfChild != null )
					return;

				childOfChild = child;
			}

			if ( childOfChild == null )
				return;

			messageFor = childOfChild.getId();
		}
		else if ( !( childComponent instanceof UIInput ) )
			return;

		@SuppressWarnings( "unchecked" )
		Map<String, String> metadataAttributes = (Map<String, String>) childComponent.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );

		if ( metadataAttributes != null )
		{
			if ( TRUE.equals( metadataAttributes.get( READ_ONLY ) ) || ( (UIMetawidget) metawidget ).isReadOnly() )
				return;
		}

		// Not using inline messages?

		State state = getState( metawidget );

		if ( !state.useInlineMessages )
			return;

		// Render inline message

		FacesUtils.render( context, createMessage( context, metawidget, messageFor ) );
	}

	protected HtmlMessage createMessage( FacesContext context, UIComponent metawidget, String messageFor )
	{
		HtmlMessage message = (HtmlMessage) context.getApplication().createComponent( "javax.faces.HtmlMessage" );
		message.setParent( metawidget );
		message.setId( context.getViewRoot().createUniqueId() );
		message.setFor( messageFor );

		// Parse styles

		State state = getState( metawidget );

		if ( !"".equals( state.messageStyle ) )
			message.setStyle( state.messageStyle );

		if ( !"".equals( state.messageStyleClass ) )
			message.setStyleClass( state.messageStyleClass );

		return message;
	}

	protected void writeStyleAndClass( UIComponent metawidget, ResponseWriter writer, String style )
		throws IOException
	{
		UIParameter parameterStyle = FacesUtils.findParameterWithName( metawidget, style + "Style" );

		if ( parameterStyle != null )
			writer.writeAttribute( "style", parameterStyle.getValue(), "style" );

		UIParameter parameterStyleClass = FacesUtils.findParameterWithName( metawidget, style + "StyleClass" );

		if ( parameterStyleClass != null )
			writer.writeAttribute( "class", parameterStyleClass.getValue(), "class" );
	}

	//
	// Private methods
	//

	private State getState( UIComponent metawidget )
	{
		State state = (State) ( (UIMetawidget) metawidget ).getClientProperty( HtmlLayoutRenderer.class );

		if ( state == null )
		{
			state = new State();
			( (UIMetawidget) metawidget ).putClientProperty( HtmlLayoutRenderer.class, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */class State
	{
		/* package private */boolean	useInlineMessages	= true;

		/* package private */String		inlineMessages;

		/* package private */String		messageStyle;

		/* package private */String		messageStyleClass;

		/* package private */String		labelSuffix;
	}
}
