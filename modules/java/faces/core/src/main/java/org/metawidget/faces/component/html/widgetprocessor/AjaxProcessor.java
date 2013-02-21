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

package org.metawidget.faces.component.html.widgetprocessor;

import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.el.MethodExpression;
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
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * WidgetProcessor for JSF2 environments.
 * <p>
 * Adds <code>AjaxBehavior</code> (equivalent to <code>f:ajax</code>) to suit the inspected fields.
 * Note that <code>f:ajax</code> is only supported under Facelets, as per section 10.4.1 of the JSF
 * 2 specification: "The following additional tags [f:ajax] apply to the Facelet Core Tag Library
 * <em>only</em>".
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

			ClientBehaviorHolder clientBehaviorHolder = (ClientBehaviorHolder) component;

			// Sanity check

			if ( "".equals( ajaxEvent ) ) {
				ajaxEvent = clientBehaviorHolder.getDefaultEventName();
			} else {
				Collection<String> eventNames = clientBehaviorHolder.getEventNames();

				if ( eventNames == null || !eventNames.contains( ajaxEvent ) ) {
					throw WidgetProcessorException.newException( "'" + ajaxEvent + "' not a valid event for " + component.getClass() + ". Must be one of " + CollectionUtils.toString( eventNames ) );
				}
			}

			// Add behaviour

			FacesContext context = FacesContext.getCurrentInstance();
			AjaxBehavior ajaxBehaviour = (AjaxBehavior) context.getApplication().createBehavior( AjaxBehavior.BEHAVIOR_ID );
			clientBehaviorHolder.addClientBehavior( ajaxEvent, ajaxBehaviour );

			// Set render to the parent Metawidget level. This is not perfect, as there may be cases
			// where we want the AJAX event to, say, update a different Metawidget - but it should
			// work in the majority of cases. It is very problematic to ask the developer to specify
			// the 'render id' in the annotation, because in most cases that id will be dynamically
			// generated (may even be randomly generated). They can always use a custom
			// WidgetProcessor in that case
			//
			// If using a persistent scope (such as conversation scope or view scope) it may be more
			// optimal to use setExecute( "@this" ) instead

			ajaxBehaviour.setExecute( CollectionUtils.newArrayList( metawidget.getClientId() ) );
			ajaxBehaviour.setRender( ajaxBehaviour.getExecute() );

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

	/**
	 * As per section 3.7.10.2 of the JSF 2 specification : "The method signature defined by the
	 * listener interface must take a single parameter, an instance of the event class for which
	 * this listener is being created"
	 * <p>
	 * Must be marked <code>Serializable</code> for StateSaving.
	 */

	static class AjaxBehaviorListenerImpl
		implements AjaxBehaviorListener, Serializable {

		//
		// Private members
		//

		MethodExpression	mListenerMethod;

		//
		// Constructor
		//

		public AjaxBehaviorListenerImpl() {

			// Needed for StateSaving
		}

		public AjaxBehaviorListenerImpl( String listenerExpression ) {

			FacesContext context = FacesContext.getCurrentInstance();
			Application application = context.getApplication();

			mListenerMethod = application.getExpressionFactory().createMethodExpression( context.getELContext(), listenerExpression, Object.class, new Class[] { AjaxBehaviorEvent.class } );
		}

		//
		// Public methods
		//

		public void processAjaxBehavior( AjaxBehaviorEvent event )
			throws AbortProcessingException {

			mListenerMethod.invoke( FacesContext.getCurrentInstance().getELContext(), new Object[] { event } );
		}
	}
}
