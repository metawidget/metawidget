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

package org.metawidget.android.widget.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.AndroidValueAccessor;
import org.metawidget.android.widget.Stub;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

/**
 * WidgetBuilder for read-only widgets in Android environments.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ReadOnlyWidgetBuilder
	implements WidgetBuilder<View, AndroidMetawidget>, AndroidValueAccessor {

	//
	// Public methods
	//

	public Object getValue( View view ) {

		// TextView
		//
		// (don't use instanceof, because CheckBox instanceof TextView)

		if ( TextView.class.equals( view.getClass() ) ) {
			CharSequence text = ( (TextView) view ).getText();

			// Convert SpannableStringBuilder to Strings
			//
			// This is a little controversial, but it's painful to handle SpannableStringBuilder
			// everywhere in client code

			if ( text instanceof SpannableStringBuilder ) {
				text = text.toString();
			}

			return text;
		}

		return null;
	}

	public boolean setValue( Object value, View view ) {

		// TextView
		//
		// (don't use instanceof, because CheckBox instanceof TextView)

		if ( TextView.class.equals( view.getClass() ) ) {
			( (TextView) view ).setText( StringUtils.quietValueOf( value ) );
			return true;
		}

		// Unknown

		return false;
	}

	public View buildWidget( String elementName, Map<String, String> attributes, AndroidMetawidget metawidget ) {

		// Not read-only?

		if ( !WidgetBuilderUtils.isReadOnly( attributes ) ) {
			return null;
		}

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return new Stub( metawidget.getContext() );
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			return new Stub( metawidget.getContext() );
		}

		// Masked (return an invisible View, so that we DO still
		// render a label and reserve some blank space)

		if ( TRUE.equals( attributes.get( MASKED ) ) ) {
			TextView view = new TextView( metawidget.getContext() );
			view.setVisibility( View.INVISIBLE );

			return view;
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			return new TextView( metawidget.getContext() );
		}

		// Lookup the Class

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, String.class );

		if ( clazz != null ) {
			if ( clazz.isPrimitive() ) {
				return new TextView( metawidget.getContext() );
			}

			if ( String.class.equals( clazz ) ) {
				return new TextView( metawidget.getContext() );
			}

			if ( Character.class.equals( clazz ) ) {
				return new TextView( metawidget.getContext() );
			}

			if ( Date.class.equals( clazz ) ) {
				return new TextView( metawidget.getContext() );
			}

			if ( Boolean.class.equals( clazz ) ) {
				return new TextView( metawidget.getContext() );
			}

			if ( Number.class.isAssignableFrom( clazz ) ) {
				return new TextView( metawidget.getContext() );
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				return new Stub( metawidget.getContext() );
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return new TextView( metawidget.getContext() );
		}

		// Nested Metawidget

		return null;
	}
}
