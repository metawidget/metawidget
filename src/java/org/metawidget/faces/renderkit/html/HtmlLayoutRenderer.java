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

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.renderkit.LayoutRenderer;

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

public class HtmlLayoutRenderer
	extends LayoutRenderer
{
	//
	// Private statics
	//

	private final static String	KEY_USE_INLINE_MESSAGES	= "inlineMessages";

	private final static String	KEY_MESSAGE_STYLE		= "messageStyle";

	private final static String	KEY_MESSAGE_STYLE_CLASS	= "messageStyleClass";

	private final static String	KEY_LABEL_SUFFIX		= "labelSuffix";

	//
	// Public methods
	//

	@SuppressWarnings( "unused" )
	@Override
	public void reentrantEncodeBegin( FacesContext context, UIComponent component )
		throws IOException
	{
		// Determine label suffix

		UIParameter parameterLabelSuffix = FacesUtils.findParameterWithName( component, KEY_LABEL_SUFFIX );

		if ( parameterLabelSuffix != null )
			putState( KEY_LABEL_SUFFIX, parameterLabelSuffix.getValue() );
	}

	//
	// Protected methods
	//

	/**
	 * Render the label text. Rendering is done via a <code>HtmlOutputText</code> renderer, so that
	 * the label may contain value expressions, such as <code>UiLabel( "#{foo.name}'s name" )</code>.
	 *
	 * @return whether a label was written
	 */

	@SuppressWarnings( "deprecation" )
	protected boolean layoutLabel( FacesContext context, UIComponent componentNeedingLabel )
		throws IOException
	{
		@SuppressWarnings( "unchecked" )
		Map<String, String> attributes = (Map<String, String>) componentNeedingLabel.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );
		String label = ( (UIMetawidget) componentNeedingLabel.getParent() ).getLabelString( context, attributes );

		if ( label == null )
			return false;

		if ( !"".equals( label.trim() ) && !( componentNeedingLabel instanceof UICommand ) )
		{
			HtmlOutputText componentLabel = (HtmlOutputText) context.getApplication().createComponent( "javax.faces.HtmlOutputText" );

			String labelSuffix = (String) getState( KEY_LABEL_SUFFIX );

			if ( labelSuffix == null )
				labelSuffix = ":";

			if ( label.indexOf( "#{" ) != -1 )
				componentLabel.setValueBinding( "value", context.getApplication().createValueBinding( label + labelSuffix ) );
			else
				componentLabel.setValue( label + labelSuffix );

			FacesUtils.render( context, componentLabel );
		}

		return true;
	}

	protected void layoutChild( FacesContext context, UIComponent component, UIComponent childComponent )
		throws IOException
	{
		FacesUtils.render( context, childComponent );

		// No need for inline messages?

		if ( childComponent instanceof HtmlInputHidden )
			return;

		String messageFor = childComponent.getId();

		if ( childComponent instanceof UIMetawidget )
		{
			if ( childComponent.getChildCount() != 1 )
				return;

			// (drill into single component UIMetawidgets)

			messageFor = childComponent.getChildren().get( 0 ).getId();
		}
		else if ( !( childComponent instanceof UIInput ) )
			return;

		@SuppressWarnings( "unchecked" )
		Map<String, String> attributes = (Map<String, String>) childComponent.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );

		if ( attributes != null )
		{
			if ( TRUE.equals( attributes.get( READ_ONLY ) ) || ( (UIMetawidget) component ).isReadOnly() )
				return;
		}

		// Not using inline messages?

		Boolean useInlineMessages = (Boolean) getState( KEY_USE_INLINE_MESSAGES );

		if ( useInlineMessages == null )
		{
			UIParameter useInlineMessagesParameter = FacesUtils.findParameterWithName( component, KEY_USE_INLINE_MESSAGES );

			if ( useInlineMessagesParameter == null )
				useInlineMessages = Boolean.TRUE;
			else
				useInlineMessages = Boolean.valueOf( (String) useInlineMessagesParameter.getValue() );

			putState( KEY_USE_INLINE_MESSAGES, useInlineMessages );
		}

		if ( !useInlineMessages )
			return;

		// Render inline message

		FacesUtils.render( context, createMessage( context, component, messageFor ) );
	}

	protected HtmlMessage createMessage( FacesContext context, UIComponent component, String messageFor )
	{
		HtmlMessage message = (HtmlMessage) context.getApplication().createComponent( "javax.faces.HtmlMessage" );
		message.setParent( component );
		message.setId( context.getViewRoot().createUniqueId() );
		message.setFor( messageFor );

		// Parse styles

		String messageStyle = (String) getState( KEY_MESSAGE_STYLE );

		if ( messageStyle == null )
		{
			UIParameter messageStyleParameter = FacesUtils.findParameterWithName( component, KEY_MESSAGE_STYLE );

			if ( messageStyleParameter == null )
				messageStyle = "";
			else
				messageStyle = (String) messageStyleParameter.getValue();

			putState( KEY_MESSAGE_STYLE, messageStyle );
		}

		String messageStyleClass = (String) getState( KEY_MESSAGE_STYLE_CLASS );

		if ( messageStyleClass == null )
		{
			UIParameter messageStyleClassParameter = FacesUtils.findParameterWithName( component, KEY_MESSAGE_STYLE_CLASS );

			if ( messageStyleClassParameter == null )
				messageStyleClass = "";
			else
				messageStyleClass = (String) messageStyleClassParameter.getValue();

			putState( KEY_MESSAGE_STYLE_CLASS, messageStyleClass );
		}

		if ( !"".equals( messageStyle ) )
			message.setStyle( messageStyle );

		if ( !"".equals( messageStyleClass ) )
			message.setStyleClass( messageStyleClass );

		return message;
	}
}
