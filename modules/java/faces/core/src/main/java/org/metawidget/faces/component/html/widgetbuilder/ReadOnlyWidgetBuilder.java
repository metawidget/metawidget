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

package org.metawidget.faces.component.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.layout.SimpleLayout;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * WidgetBuilder for read-only widgets in Java Server Faces environments.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ReadOnlyWidgetBuilder
	implements WidgetBuilder<UIComponent, UIMetawidget> {

	//
	// Public methods
	//

	public UIComponent buildWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Not read-only?

		if ( !WidgetBuilderUtils.isReadOnly( attributes ) ) {
			return null;
		}

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return application.createComponent( UIStub.COMPONENT_TYPE );
		}

		// Masked (return a couple of nested Stubs, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) ) {
			UIComponent component = application.createComponent( UIStub.COMPONENT_TYPE );
			List<UIComponent> listChildren = component.getChildren();
			listChildren.add( application.createComponent( UIStub.COMPONENT_TYPE ) );

			return component;
		}

		// Action

		if ( ACTION.equals( elementName ) ) {

			// (no guarantee we can disable a custom FACES_COMPONENT)

			if ( attributes.get( FACES_COMPONENT ) != null ) {
				return null;
			}

			HtmlCommandButton button = (HtmlCommandButton) application.createComponent( HtmlCommandButton.COMPONENT_TYPE );
			button.setDisabled( true );
			button.setValue( metawidget.getLabelString( attributes ) );

			return button;
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			String lookupLabels = attributes.get( LOOKUP_LABELS );

			if ( lookupLabels == null ) {
				return application.createComponent( HtmlOutputText.COMPONENT_TYPE );
			}

			// Special support for read-only lookups with labels

			List<String> labels = CollectionUtils.fromString( lookupLabels );

			if ( labels.isEmpty() ) {
				return application.createComponent( HtmlOutputText.COMPONENT_TYPE );
			}

			HtmlLookupOutputText lookupOutputText = (HtmlLookupOutputText) application.createComponent( HtmlLookupOutputText.COMPONENT_TYPE );
			lookupOutputText.setLabels( CollectionUtils.fromString( lookup ), labels );

			return lookupOutputText;
		}

		String facesLookup = attributes.get( FACES_LOOKUP );

		if ( facesLookup != null && !"".equals( facesLookup ) ) {
			return application.createComponent( HtmlOutputText.COMPONENT_TYPE );
		}

		// Lookup the class

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, String.class );

		if ( clazz != null ) {
			// Primitives

			if ( clazz.isPrimitive() ) {
				return application.createComponent( HtmlOutputText.COMPONENT_TYPE );
			}

			// Object primitives

			if ( ClassUtils.isPrimitiveWrapper( clazz ) ) {
				return application.createComponent( HtmlOutputText.COMPONENT_TYPE );
			}

			// Dates

			if ( Date.class.isAssignableFrom( clazz ) ) {
				return application.createComponent( HtmlOutputText.COMPONENT_TYPE );
			}

			// Strings

			if ( String.class.equals( clazz ) ) {
				return application.createComponent( HtmlOutputText.COMPONENT_TYPE );
			}

			// Collections that will be supported by HtmlWidgetBuilder

			if ( List.class.isAssignableFrom( clazz ) || DataModel.class.isAssignableFrom( clazz ) || clazz.isArray() ) {
				return null;
			}

			// Other Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				return application.createComponent( HtmlOutputText.COMPONENT_TYPE );
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) || metawidget.getLayout() instanceof SimpleLayout ) {
			return application.createComponent( HtmlOutputText.COMPONENT_TYPE );
		}

		// Nested Metawidget

		return null;
	}
}
