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

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlMessage;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.util.simple.SimpleLayoutUtils;
import org.metawidget.util.simple.StringUtils;

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
	extends Renderer {

	//
	// Private statics
	//

	private static final String	LABEL_ID_SUFFIX	= "-label";

	//
	// Public methods
	//

	@Override
	public void encodeBegin( FacesContext context, UIComponent metawidgetComponent )
		throws IOException {

		UIMetawidget metawidget = (UIMetawidget) metawidgetComponent;

		metawidget.putClientProperty( HtmlLayoutRenderer.class, null );
		super.encodeBegin( context, metawidget );

		// Determine label suffix

		State state = getState( metawidget );
		state.labelSuffix = metawidget.getParameter( "labelSuffix" );

		// Using inline messages?

		String inlineMessagesParameter = metawidget.getParameter( "inlineMessages" );

		if ( inlineMessagesParameter != null ) {
			state.inlineMessages = Boolean.valueOf( inlineMessagesParameter );
		}

		// Message styles

		state.messageStyle = metawidget.getParameter( "messageStyle" );
		state.messageStyleClass = metawidget.getParameter( "messageStyleClass" );
	}

	/**
	 * Denotes that this Renderer renders its own children (eg. JSF should not call
	 * <code>encodeBegin</code> on each child for us)
	 */

	@Override
	public boolean getRendersChildren() {

		return true;
	}

	//
	// Protected methods
	//

	protected String getLabelText( UIComponent componentNeedingLabel ) {

		@SuppressWarnings( "unchecked" )
		Map<String, String> metadataAttributes = (Map<String, String>) componentNeedingLabel.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );
		return ( (UIMetawidget) componentNeedingLabel.getParent() ).getLabelString( metadataAttributes );
	}

	/**
	 * Render the label text. Rendering is done via an <code>HtmlOutputText</code> renderer, so that
	 * it is properly escaped. Any JSF EL expressions embedded in the label should have already been
	 * evaluated by <code>FacesInspectionResultProcessor</code>.
	 *
	 * @return whether a label was written
	 */

	protected boolean layoutLabel( FacesContext context, UIComponent metawidget, UIComponent componentNeedingLabel )
		throws IOException {

		// Generally speaking, UICommands are their own label (ie. the text on a button)
		//
		// In cases where a component is defined as a UICommand but you *do* still want a label
		// (e.g. the RichFaces ProgressBar), consider wrapping the component in a Stub.

		if ( componentNeedingLabel instanceof UICommand ) {
			return false;
		}

		String labelText = getLabelText( componentNeedingLabel );

		if ( !SimpleLayoutUtils.needsLabel( labelText, null ) ) {
			return false;
		}

		// Render the label

		UIOutput componentLabel = createLabel( context, componentNeedingLabel );

		State state = getState( metawidget );

		if ( state.labelSuffix == null ) {
			state.labelSuffix = StringUtils.SEPARATOR_COLON;
		}

		// (any embedded expressions should have gotten evaluated by FacesInspectionResultProcessor)

		componentLabel.setValue( labelText + state.labelSuffix );

		FacesUtils.render( context, componentLabel );
		return true;
	}

	/**
	 * Create a label component for the given <code>UIComponent</code>. Clients may override this
	 * method to create a different type of label component.
	 */

	protected UIOutput createLabel( FacesContext context, UIComponent componentNeedingLabel ) {

		HtmlOutputLabel componentLabel = (HtmlOutputLabel) context.getApplication().createComponent( HtmlOutputLabel.COMPONENT_TYPE );

		if ( componentNeedingLabel instanceof UIStub ) {

			// Not setFor on UIStub, because stubs never render id. However we can take a guess if
			// there's only one child component

			if ( componentNeedingLabel.getChildren().size() == 1 ) {
				componentLabel.setFor( componentNeedingLabel.getChildren().get( 0 ).getId() );
			}
		} else if ( componentNeedingLabel.getId() != null ) {
			componentLabel.setFor( componentNeedingLabel.getId() );
		}

		// Call .setParent before .setId to avoid 'unable to find component with id' warning and/or
		// 'duplicate component' error (depending on the JSF implementation)

		if ( componentLabel.getFor() != null ) {
			componentLabel.setParent( componentNeedingLabel.getParent() );
			componentLabel.setId( componentLabel.getFor() + LABEL_ID_SUFFIX );
		}

		return componentLabel;
	}

	protected void layoutChild( FacesContext context, UIComponent metawidget, UIComponent childComponent )
		throws IOException {

		FacesUtils.render( context, childComponent );

		// No need for inline messages?

		if ( childComponent instanceof HtmlInputHidden ) {
			return;
		}

		String messageFor = childComponent.getId();

		if ( childComponent instanceof UIMetawidget ) {
			// (drill into single component UIMetawidgets)

			UIComponent childOfChild = null;

			for ( UIComponent child : childComponent.getChildren() ) {
				if ( child instanceof UIParameter ) {
					continue;
				}

				if ( childOfChild != null ) {
					return;
				}

				childOfChild = child;
			}

			if ( childOfChild == null ) {
				return;
			}

			messageFor = childOfChild.getId();
		} else if ( !( childComponent instanceof UIInput ) ) {
			return;
		}

		@SuppressWarnings( "unchecked" )
		Map<String, String> metadataAttributes = (Map<String, String>) childComponent.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );

		if ( metadataAttributes != null ) {
			if ( TRUE.equals( metadataAttributes.get( READ_ONLY ) ) || ( (UIMetawidget) metawidget ).isReadOnly() ) {
				return;
			}
		}

		// Not using inline messages?

		State state = getState( metawidget );

		if ( !state.inlineMessages ) {
			return;
		}

		// Render inline message
		//
		// Temporarily create an HtmlMessage and render it. It needs a parent to be able to render,
		// so add it/remove it again from the Metawidget. Do not call setParent directly, as
		// cautioned here:
		//
		// http://javaserverfaces.java.net/nonav/docs/2.0/javadocs/javax/faces/component/UIComponent.html#setParent(javax.faces.component.UIComponent)

		List<UIComponent> children = metawidget.getChildren();
		UIComponent inlineMessage = createInlineMessage( context, metawidget, messageFor );

		try {
			children.add( inlineMessage );
			FacesUtils.render( context, inlineMessage );
		} finally {
			children.remove( inlineMessage );
		}
	}

	/**
	 * Creates an inline <code>HtmlMessage</code> attached to the given <code>messageFor</code> id.
	 * <p>
	 * Subclasses can override this method to create a different messaging object, such as an
	 * <code>HtmlMessages</code> (with an 's').
	 */

	protected UIComponent createInlineMessage( FacesContext context, UIComponent metawidget, String messageFor ) {

		HtmlMessage message = (HtmlMessage) context.getApplication().createComponent( HtmlMessage.COMPONENT_TYPE );
		message.setId( FacesUtils.createUniqueId() );
		message.setFor( messageFor );

		// Parse styles

		State state = getState( metawidget );
		FacesUtils.setStyleAndStyleClass( message, state.messageStyle, state.messageStyleClass );

		return message;
	}

	protected void writeStyleAndClass( UIMetawidget metawidget, ResponseWriter writer, String style )
		throws IOException {

		String styleParameter = metawidget.getParameter( style + "Style" );

		if ( styleParameter != null ) {
			writer.writeAttribute( "style", styleParameter, "style" );
		}

		String styleClassParameter = metawidget.getParameter( style + "StyleClass" );

		if ( styleClassParameter != null ) {
			writer.writeAttribute( "class", styleClassParameter, "class" );
		}
	}

	//
	// Private methods
	//

	private State getState( UIComponent metawidget ) {

		State state = (State) ( (UIMetawidget) metawidget ).getClientProperty( HtmlLayoutRenderer.class );

		if ( state == null ) {
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

	/* package private */static class State {

		/* package private */boolean	inlineMessages	= true;

		/* package private */String		messageStyle;

		/* package private */String		messageStyleClass;

		/* package private */String		labelSuffix;
	}
}
