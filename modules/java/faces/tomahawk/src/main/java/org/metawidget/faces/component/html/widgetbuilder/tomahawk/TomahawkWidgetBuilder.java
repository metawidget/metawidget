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

package org.metawidget.faces.component.html.widgetbuilder.tomahawk;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.fileupload.HtmlInputFileUpload;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * WidgetBuilder for Tomahawk environments.
 * <p>
 * Creates native Tomahawk UIComponents, such as <code>HtmlInputFileUpload</code>, to suit the
 * inspected fields.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TomahawkWidgetBuilder
	implements WidgetBuilder<UIComponent, UIMetawidget> {

	//
	// Public methods
	//

	public UIComponent buildWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Not for Tomahawk?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return null;
		}

		if ( attributes.containsKey( FACES_LOOKUP ) || attributes.containsKey( LOOKUP ) ) {
			return null;
		}

		Application application = FacesContext.getCurrentInstance().getApplication();
		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, null );

		if ( clazz == null ) {
			return null;
		}

		// HtmlInputFileUpload

		if ( UploadedFile.class.isAssignableFrom( clazz ) ) {
			return application.createComponent( HtmlInputFileUpload.COMPONENT_TYPE );
		}

		// Not for Tomahawk

		return null;
	}
}
