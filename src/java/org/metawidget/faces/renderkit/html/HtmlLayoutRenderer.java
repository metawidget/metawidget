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

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlMessage;
import javax.faces.context.FacesContext;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.renderkit.LayoutRenderer;

/**
 * Base class for all JSF HTML layout renderers. This implementation recognizes the following
 * <code>&lt;f:param&gt;</code> parameters:
 * <p>
 * <ul>
 * <li><code>inlineMessages</code> - whether to wrap input components with inline
 * &lt;h:message&gt; tags. True by default
 * <li><code>messageStyle</code>
 * <li><code>messageStyleClass</code>
 * </ul>
 *
 * @author Richard Kennard
 */

public class HtmlLayoutRenderer
	extends LayoutRenderer
{
	//
	//
	// Private statics
	//
	//

	private final static String	KEY_USE_INLINE_MESSAGES	= "inlineMessages";

	private final static String	KEY_MESSAGE_STYLE		= "messageStyle";

	private final static String	KEY_MESSAGE_STYLE_CLASS	= "messageStyleClass";

	//
	//
	// Protected methods
	//
	//

	protected void layoutChild( FacesContext context, UIComponent component, UIComponent childComponent )
		throws IOException
	{
		FacesUtils.render( context, childComponent );

		// No need for inline messages?

		if ( childComponent instanceof HtmlInputHidden )
			return;

		if ( !( childComponent instanceof UIInput || childComponent instanceof UIMetawidget ))
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

		FacesUtils.render( context, createMessage( context, component, childComponent ) );
	}

	protected HtmlMessage createMessage( FacesContext context, UIComponent component, UIComponent childComponent )
	{
		HtmlMessage message = (HtmlMessage) context.getApplication().createComponent( "javax.faces.HtmlMessage" );
		message.setParent( component );
		message.setId( context.getViewRoot().createUniqueId() );
		message.setFor( childComponent.getId() );

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
