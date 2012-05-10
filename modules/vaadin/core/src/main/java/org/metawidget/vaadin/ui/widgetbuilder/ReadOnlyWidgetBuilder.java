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

package org.metawidget.vaadin.ui.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.vaadin.ui.VaadinMetawidget;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;

/**
 * WidgetBuilder for Vaadin environments.
 * <p>
 * Creates native Vaadin read-only <code>Components</code>, such as <code>Labels</code>, to suit the
 * inspected fields.
 *
 * @author Loghman Barari
 */

public class ReadOnlyWidgetBuilder
	implements WidgetBuilder<Component, VaadinMetawidget> {

	//
	// Public methods
	//

	public Component buildWidget( String elementName, Map<String, String> attributes, VaadinMetawidget metawidget ) {

		// Not read-only?

		if ( !WidgetBuilderUtils.isReadOnly( attributes ) ) {
			return null;
		}

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return null;
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			Button button = new Button( metawidget.getLabelString( attributes ) );
			button.setEnabled( false );

			return button;
		}

		if ( TRUE.equals( attributes.get( MASKED ) ) ) {
			return new Label();
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			// May have alternate labels

			String lookupLabels = attributes.get( LOOKUP_LABELS );

			if ( lookupLabels != null && !"".equals( lookupLabels ) ) {
				return new LookupLabel( CollectionUtils.newHashMap( CollectionUtils.fromString( lookup ), CollectionUtils.fromString( lookupLabels ) ) );
			}

			return new Label();
		}

		// Lookup the Class

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, String.class );

		if ( clazz != null ) {
			// Primitives

			if ( clazz.isPrimitive() ) {
				return new Label();
			}

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					TextArea textarea = new TextArea();
					textarea.setReadOnly( true );

					return textarea;
				}

				return new Label();
			}

			if ( Character.class.equals( clazz ) ) {
				return new Label();
			}

			if ( Date.class.equals( clazz ) ) {
				return new Label();
			}

			if ( Boolean.class.equals( clazz ) ) {
				return new Label();
			}

			if ( Number.class.isAssignableFrom( clazz ) ) {
				return new Label();
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				return null;
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return new Label();
		}

		// Nested Metawidget

		return null;
	}
}
