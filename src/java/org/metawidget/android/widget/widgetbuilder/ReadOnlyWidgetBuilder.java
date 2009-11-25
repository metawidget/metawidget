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

package org.metawidget.android.widget.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.AndroidValueAccessor;
import org.metawidget.android.widget.Stub;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

/**
 * WidgetBuilder for Android environments.
 * <p>
 * Creates native Android Views, such as <code>EditText</code> and <code>Spinner</code>, to suit the
 * inspected fields.
 *
 * @author Richard Kennard
 */

public class ReadOnlyWidgetBuilder
	implements WidgetBuilder<View, AndroidMetawidget>, AndroidValueAccessor
{
	//
	// Public methods
	//

	public Object getValue( View view )
	{
		// TextView
		//
		// (don't use instanceof, because CheckBox instanceof TextView)

		if ( TextView.class.equals( view.getClass() ) )
		{
			CharSequence text = ( (TextView) view ).getText();

			// Convert SpannableStringBuilder to Strings
			//
			// This is a little controversial, but it's painful to handle SpannableStringBuilder
			// everywhere in client code

			if ( text instanceof SpannableStringBuilder )
				text = text.toString();

			return text;
		}

		return null;
	}

	public boolean setValue( Object value, View view )
	{
		// TextView
		//
		// (don't use instanceof, because CheckBox instanceof TextView)

		if ( TextView.class.equals( view.getClass() ) )
		{
			( (TextView) view ).setText( StringUtils.quietValueOf( value ) );
			return true;
		}

		// Unknown

		return false;
	}

	public View buildWidget( String elementName, Map<String, String> attributes, AndroidMetawidget metawidget )
	{
		// Not read-only?

		if ( !WidgetBuilderUtils.isReadOnly( attributes ))
			return null;

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return new Stub( metawidget.getContext() );

		// Action

		if ( ACTION.equals( elementName ) )
			return new Stub( metawidget.getContext() );

		// Masked (return an invisible View, so that we DO still
		// render a label and reserve some blank space)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
		{
			TextView view = new TextView( metawidget.getContext() );
			view.setVisibility( View.INVISIBLE );

			return view;
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
			return new TextView( metawidget.getContext() );

		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, assume a String

		if ( type == null )
			type = String.class.getName();

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz != null )
		{
			if ( clazz.isPrimitive() )
				return new TextView( metawidget.getContext() );

			if ( String.class.equals( clazz ) )
				return new TextView( metawidget.getContext() );

			if ( Date.class.equals( clazz ) )
				return new TextView( metawidget.getContext() );

			if ( Boolean.class.equals( clazz ) )
				return new TextView( metawidget.getContext() );

			if ( Number.class.isAssignableFrom( clazz ) )
				return new TextView( metawidget.getContext() );

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return new Stub( metawidget.getContext() );
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return new TextView( metawidget.getContext() );

		// Nested Metawidget

		return null;
	}
}
