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

package org.metawidget.faces.component.html.widgetprocessor;

import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor for JSF2 environments.
 * <p>
 * Adds <code>AjaxBehavior</code> (equivalent to <code>f:ajax</code>) to suit the inspected fields.
 *
 * @author Richard Kennard
 */

public class AjaxProcessor
	implements WidgetProcessor<UIComponent, UIMetawidget> {

	//
	// Public methods
	//

	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Ignore empty stubs

		if ( component instanceof UIStub && component.getChildCount() == 0 ) {
			return component;
		}

		// Ignore non-AJAX components

		if ( !( component instanceof ClientBehaviorHolder ) ) {
			return component;
		}

		// Ajax

		String ajaxEvent = attributes.get( FACES_AJAX_EVENT );

		if ( ajaxEvent != null ) {
			AjaxBehavior ajaxBehaviour = new AjaxBehavior();
			( (ClientBehaviorHolder) component ).addClientBehavior( ajaxEvent, ajaxBehaviour );

			// Set reRender to the parent Metawidget level. This is not perfect, as there may be
			// cases where we want the AJAX event to, say, update a different Metawidget - but it
			// should work in the majority of cases. It is very problematic to ask the developer to
			// specify the 'reRender' id, because in most cases that id will be dynamically
			// generated (may even be randomly generated)

			ajaxBehaviour.setRender( CollectionUtils.newArrayList( metawidget.getId() ) );

			// Listener

			String ajaxListener = attributes.get( FACES_AJAX_ACTION );

			if ( ajaxListener != null ) {
				ajaxBehaviour.addAjaxBehaviorListener( new AjaxBehaviorListenerImpl( ajaxListener ) );
			}
		}

		return component;
	}

	//
	// Inner class
	//

	static class AjaxBehaviorListenerImpl
		implements AjaxBehaviorListener {

		//
		// Private members
		//

		MethodExpression	mZeroArgument;

		MethodExpression	mSingleArgument;

		//
		// Public methods
		//

		public AjaxBehaviorListenerImpl( String listener ) {

			FacesContext context = FacesContext.getCurrentInstance();
			Application application = context.getApplication();

			mZeroArgument = application.getExpressionFactory().createMethodExpression( context.getELContext(), listener, Object.class, null );
			mSingleArgument = application.getExpressionFactory().createMethodExpression( context.getELContext(), listener, Object.class, new Class[] { Object.class } );
		}

		public void processAjaxBehavior( AjaxBehaviorEvent event )
			throws AbortProcessingException {

			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			try {
				mZeroArgument.invoke( elContext, new Object[] {} );
			} catch ( MethodNotFoundException e ) {
				mSingleArgument.invoke( elContext, new Object[] { event } );
			}
		}
	}
}
