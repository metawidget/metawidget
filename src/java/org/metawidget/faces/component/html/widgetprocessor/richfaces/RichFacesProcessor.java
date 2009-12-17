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

package org.metawidget.faces.component.html.widgetprocessor.richfaces;

import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.html.HtmlAjaxSupport;
import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.widgetprocessor.impl.BaseWidgetProcessor;
import org.richfaces.component.html.HtmlCalendar;
import org.richfaces.component.html.HtmlInputNumberSlider;
import org.richfaces.component.html.HtmlInputNumberSpinner;
import org.richfaces.component.html.HtmlSuggestionBox;

/**
 * WidgetProcessor for RichFaces environments.
 * <p>
 * Adds native RichFaces behaviours, such as <code>HtmlAjaxSupport</code>, to suit the inspected
 * fields.
 *
 * @author Richard Kennard
 */

public class RichFacesProcessor
	extends BaseWidgetProcessor<UIComponent, UIMetawidget>
{
	//
	// Public methods
	//

	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget )
	{
		// Ajax

		String ajaxEvent = attributes.get( FACES_AJAX_EVENT );

		if ( ajaxEvent != null )
		{
			FacesContext context = FacesContext.getCurrentInstance();
			Application application = context.getApplication();

			HtmlAjaxSupport ajaxSupport = (HtmlAjaxSupport) application.createComponent( "org.ajax4jsf.Support" );
			ajaxSupport.setId( context.getViewRoot().createUniqueId() );
			ajaxSupport.setEvent( ajaxEvent );

			// Set reRender to the parent Metawidget level. This is not perfect, as there may be
			// cases where we want the AJAX event to, say, update a different Metawidget - but it
			// should work in the majority of cases. It is
			// very problematic to ask the developer to specify the 'reRender' id, because in most
			// cases that id will be dynamically generated

			ajaxSupport.setReRender( metawidget.getId() );

			component.getChildren().add( ajaxSupport );
		}

		return component;
	}

	//
	// Inner class
	//

	protected static class MockRichFacesFacesContext
		extends MockFacesContext
	{
		//
		// Protected methods
		//

		@Override
		public UIComponent createComponent( String componentName )
			throws FacesException
		{
			if ( "org.richfaces.inputNumberSlider".equals( componentName ) )
				return new HtmlInputNumberSlider();

			if ( "org.richfaces.inputNumberSpinner".equals( componentName ) )
				return new HtmlInputNumberSpinner();

			if ( "org.richfaces.Calendar".equals( componentName ) )
				return new HtmlCalendar();

			if ( "org.richfaces.SuggestionBox".equals( componentName ) )
				return new HtmlSuggestionBox();

			return super.createComponent( componentName );
		}
	}
}
