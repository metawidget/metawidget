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

package org.metawidget.faces.component.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * WidgetBuilder for read-only widgets in Java Server Faces environments.
 *
 * @author Richard Kennard
 */

public class ReadOnlyWidgetBuilder
	implements WidgetBuilder<UIComponent, UIMetawidget> {

	//
	// Public methods
	//

	@Override
	public UIComponent buildWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Not read-only?

		if ( !WidgetBuilderUtils.isReadOnly( attributes ) ) {
			return null;
		}

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return application.createComponent( "org.metawidget.Stub" );
		}

		// Masked (return a couple of nested Stubs, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) ) {
			UIComponent component = application.createComponent( "org.metawidget.Stub" );
			List<UIComponent> listChildren = component.getChildren();
			listChildren.add( application.createComponent( "org.metawidget.Stub" ) );

			return component;
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			return application.createComponent( "org.metawidget.Stub" );
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			String lookupLabels = attributes.get( LOOKUP_LABELS );

			if ( lookupLabels == null ) {
				return application.createComponent( "javax.faces.HtmlOutputText" );
			}

			// Special support for read-only lookups with labels

			List<String> labels = CollectionUtils.fromString( lookupLabels );

			if ( labels.isEmpty() ) {
				return application.createComponent( "javax.faces.HtmlOutputText" );
			}

			HtmlLookupOutputText lookupOutputText = (HtmlLookupOutputText) application.createComponent( "org.metawidget.HtmlLookupOutputText" );
			lookupOutputText.setLabels( CollectionUtils.fromString( lookup ), labels );

			return lookupOutputText;
		}

		String facesLookup = attributes.get( FACES_LOOKUP );

		if ( facesLookup != null && !"".equals( facesLookup ) ) {
			return application.createComponent( "javax.faces.HtmlOutputText" );
		}

		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, assume a String

		if ( type == null ) {
			type = String.class.getName();
		}

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz != null ) {
			// Primitives

			if ( clazz.isPrimitive() ) {
				return application.createComponent( "javax.faces.HtmlOutputText" );
			}

			// Object primitives

			if ( ClassUtils.isPrimitiveWrapper( clazz ) ) {
				return application.createComponent( "javax.faces.HtmlOutputText" );
			}

			// Dates

			if ( Date.class.isAssignableFrom( clazz ) ) {
				return application.createComponent( "javax.faces.HtmlOutputText" );
			}

			// Strings

			if ( String.class.equals( clazz ) ) {
				return application.createComponent( "javax.faces.HtmlOutputText" );
			}

			// Collections that will be supported by HtmlWidgetBuilder

			if ( List.class.isAssignableFrom( clazz ) || DataModel.class.isAssignableFrom( clazz ) || clazz.isArray() ) {
				return null;
			}

			// Other Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				return application.createComponent( "javax.faces.HtmlOutputText" );
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return application.createComponent( "javax.faces.HtmlOutputText" );
		}

		// Nested Metawidget

		return null;
	}
}
