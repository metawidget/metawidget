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

package org.metawidget.faces.component.html.widgetbuilder.tomahawk;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.impl.BaseWidgetBuilder;

/**
 * WidgetBuilder for Tomahawk environments.
 * <p>
 * Creates native Tomahawk UIComponents, such as <code>HtmlInputFileUpload</code>, to suit the
 * inspected fields.
 *
 * @author Richard Kennard
 */

public class TomahawkWidgetBuilder
	extends BaseWidgetBuilder<UIComponent, UIMetawidget>
{
	//
	// Protected methods
	//

	@Override
	protected UIComponent buildActiveWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget )
		throws Exception
	{
		// Not for Tomahawk?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		if ( attributes.containsKey( FACES_LOOKUP ) || attributes.containsKey( LOOKUP ) )
			return null;

		Application application = FacesContext.getCurrentInstance().getApplication();
		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		if ( type == null )
			return null;

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz == null )
			return null;

		// HtmlInputFileUpload

		if ( UploadedFile.class.isAssignableFrom( clazz ) )
			return application.createComponent( "org.apache.myfaces.HtmlInputFileUpload" );

		// Not for Tomahawk

		return null;
	}
}
