// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.faces.component.html.widgetprocessor.richfaces;

import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.html.HtmlAjaxSupport;
import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * WidgetProcessor for RichFaces environments.
 * <p>
 * Adds native RichFaces behaviours, such as <code>HtmlAjaxSupport</code>, to suit the inspected
 * fields.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class RichFacesProcessor
	implements WidgetProcessor<UIComponent, UIMetawidget> {

	//
	// Public methods
	//

	@SuppressWarnings( "deprecation" )
	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Ignore empty stubs

		if ( component instanceof UIStub && component.getChildCount() == 0 ) {
			return component;
		}

		// Ajax

		String ajaxEvent = attributes.get( FACES_AJAX_EVENT );

		if ( ajaxEvent != null ) {
			FacesContext context = FacesContext.getCurrentInstance();
			Application application = context.getApplication();

			HtmlAjaxSupport ajaxSupport = (HtmlAjaxSupport) application.createComponent( HtmlAjaxSupport.COMPONENT_TYPE );
			ajaxSupport.setId( FacesUtils.createUniqueId() );

			// Sanity check

			if ( "".equals( ajaxEvent )) {
				throw WidgetProcessorException.newException( "Must specify an event for " + component.getClass() );
			}

			ajaxSupport.setEvent( ajaxEvent );

			// Set reRender to the parent Metawidget level. This is not perfect, as there may be
			// cases where we want the AJAX event to, say, update a different Metawidget - but it
			// should work in the majority of cases. It is very problematic to ask the developer to
			// specify the 'reRender' id, because in most cases that id will be dynamically
			// generated (may even be randomly generated). They can always use a custom
			// WidgetProcessor in that case

			ajaxSupport.setReRender( metawidget.getId() );

			// Action

			String ajaxAction = attributes.get( FACES_AJAX_ACTION );

			if ( ajaxAction != null ) {
				ajaxSupport.setAction( application.createMethodBinding( ajaxAction, new Class[0] ) );
			}

			component.getChildren().add( ajaxSupport );
		}

		return component;
	}
}
