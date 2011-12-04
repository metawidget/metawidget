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

package org.metawidget.vaadin.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.vaadin.VaadinMetawidget;
import org.metawidget.vaadin.VaadinValuePropertyProvider;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * WidgetBuilder for Vaadin environments.
 * <p>
 * Creates native Vaadin read-only <code>Components</code>, such as <code>Labels</code>, to suit the
 * inspected fields.
 *
 * @author Loghman Barari
 */

public class ReadOnlyWidgetBuilder
	implements
		WidgetBuilder<Component, VaadinMetawidget>, VaadinValuePropertyProvider {

	//
	// Public methods
	//

	public String getValueProperty( Component component ) {

		if ( component instanceof Label ) {
			return "value";
		}

		return null;
	}

	public Component buildWidget( String elementName,
			Map<String, String> attributes, VaadinMetawidget metawidget ) {

		String id = metawidget.getDebugId() + "$" + attributes.get( NAME );

		String labelString = metawidget.getLabelString( attributes );

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
			button.setDebugId( id );
			button.setEnabled( false );

			return button;
		}

		if ( TRUE.equals( attributes.get( MASKED ) ) ) {
			return new Label( id, labelString );
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			// May have alternate labels

			String lookupLabels = attributes.get( LOOKUP_LABELS );

			if ( lookupLabels != null && !"".equals( lookupLabels ) ) {
				return new LookupLabel( id, labelString,
						VaadinWidgetBuilderUtils.getLabelsMap(
								CollectionUtils.fromString( lookup ),
								CollectionUtils.fromString( lookupLabels ) ),
						metawidget.getBundle() );
			}

			return new Label( id, labelString );
		}

		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, assume a String

		if ( type == null ) {
			type = String.class.getName();
		}

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );
		if ( clazz != null ) {
			// Primitives

			if ( clazz.isPrimitive() ) {
				return new Label( id, labelString );
			}

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					Label label = new Label( id, labelString );
					label.setContentMode( Label.CONTENT_XHTML );
					return label;
				}

				return new Label( id, labelString );
			}

			if ( Character.class.equals( clazz ) ) {
				return new Label( id, labelString );
			}

			if ( Date.class.equals( clazz ) ) {
				return new Label( id, labelString );
			}

			if ( Boolean.class.equals( clazz ) ) {
				return new Label( id, labelString );
			}

			if ( Number.class.isAssignableFrom( clazz ) ) {
				return new Label( id, labelString );
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				return null;
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return new Label( id, labelString );
		}

		// Nested Metawidget

		return null;
	}

	// Inner Class

	/*
	 * Label whose values use a lookup.
	 */

	/* package private */static class LookupLabel
		extends com.vaadin.ui.Label {

		//
		// Private statics
		//

		private static final long	serialVersionUID	= 1l;

		//
		// Private members
		//

		private Map<String, String>	mLookup;

		private ResourceBundle		mBundle;

		//
		// Constructor
		//

		public LookupLabel( String id, String caption, Map<String, String> lookup, ResourceBundle bundle ) {

			super();
			this.setDebugId( id );
			this.setCaption( caption );

			if ( lookup == null ) {
				throw new NullPointerException( "lookup" );
			}

			mLookup = lookup;

			mBundle = bundle;
		}

		public LookupLabel( String id, String labelString, Class<?> clazz,
				ResourceBundle bundle ) {

		}

		//
		// Public methods
		//
		@Override
		public void setValue( Object text ) {

			String lookup = "";

			if ( text != null ) {
				lookup = ( text instanceof Enum<?> ) ? ( (Enum<?>) text ).name() : text.toString();
			}

			if ( lookup != null && mLookup != null ) {
				lookup = mLookup.get( lookup );
			}

			if ( mBundle != null && lookup != null ) {
				try {
					lookup = mBundle.getString( lookup );
				} catch ( MissingResourceException e ) {
					// Use default lookup
				}
			}

			super.setValue( lookup );
		}

	}

	/* package private */static class Label
		extends com.vaadin.ui.Label {

		//
		// Private statics
		//

		private static final long	serialVersionUID	= 1l;

		public Label( String id, String caption ) {

			super();
			setCaption( caption );
			setDebugId( id );
			setValue( "                    " );
			setContentMode( Label.CONTENT_TEXT );
		}
	}
}
