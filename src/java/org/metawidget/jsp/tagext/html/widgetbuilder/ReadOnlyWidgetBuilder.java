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

package org.metawidget.jsp.tagext.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.jsp.JspInspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.tagext.LiteralTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.HtmlStubTag;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * ReadOnlyWidgetBuilder for 'plain' JSP environment (eg. just a servlet-based backend, no
 * Struts/Spring etc) that outputs HTML.
 * <p>
 * When used in a JSP 2.0 environment, automatically initializes tags using JSP EL.
 *
 * @author Richard Kennard
 */

public class ReadOnlyWidgetBuilder
	implements WidgetBuilder<Tag, MetawidgetTag> {

	//
	// Public methods
	//

	public Tag buildWidget( String elementName, Map<String, String> attributes, MetawidgetTag metawidget ) {

		// Not read-only?

		if ( !WidgetBuilderUtils.isReadOnly( attributes ) ) {
			return null;
		}

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return null;
		}

		// Action (read-only actions ignored)

		if ( ACTION.equals( elementName ) ) {
			return new HtmlStubTag();
		}

		// Masked (return an empty String, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) ) {
			return new LiteralTag( "" );
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			return setAttributeAndCreateReadOnlyTag( attributes, metawidget );
		}

		String jspLookup = attributes.get( JSP_LOOKUP );

		if ( jspLookup != null && !"".equals( jspLookup ) ) {
			return setAttributeAndCreateReadOnlyTag( attributes, metawidget );
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
				return setAttributeAndCreateReadOnlyTag( attributes, metawidget );
			}

			// Object primitives

			if ( ClassUtils.isPrimitiveWrapper( clazz ) ) {
				return setAttributeAndCreateReadOnlyTag( attributes, metawidget );
			}

			// Dates

			if ( Date.class.isAssignableFrom( clazz ) ) {
				return setAttributeAndCreateReadOnlyTag( attributes, metawidget );
			}

			// Strings

			if ( String.class.equals( clazz ) ) {
				return setAttributeAndCreateReadOnlyTag( attributes, metawidget );
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				return new HtmlStubTag();
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return setAttributeAndCreateReadOnlyTag( attributes, metawidget );
		}

		// Nested Metawidget

		return null;
	}

	//
	// Protected methods
	//

	protected Tag createReadOnlyTag( Map<String, String> attributes, MetawidgetTag metawidget ) {

		String value = HtmlWidgetBuilderUtils.evaluateAsText( attributes, metawidget );

		// Support lookup labels

		String lookupLabels = attributes.get( LOOKUP_LABELS );

		if ( lookupLabels != null ) {
			List<String> lookupList = CollectionUtils.fromString( attributes.get( LOOKUP ) );
			int indexOf = lookupList.indexOf( value );

			if ( indexOf != -1 ) {
				List<String> lookupLabelsList = CollectionUtils.fromString( lookupLabels );

				if ( indexOf < lookupLabelsList.size() ) {
					value = lookupLabelsList.get( indexOf );
				}
			}
		}

		return new LiteralTag( value );
	}

	//
	// Private methods
	//

	protected Tag setAttributeAndCreateReadOnlyTag( Map<String, String> attributes, MetawidgetTag metawidget ) {

		attributes.put( MetawidgetTag.ATTRIBUTE_NEEDS_HIDDEN_FIELD, TRUE );
		return createReadOnlyTag( attributes, metawidget );
	}
}
