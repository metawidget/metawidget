// Metawidget (licensed under LGPL)
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
import java.util.List;
import java.util.Map;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.AndroidValueAccessor;
import org.metawidget.android.widget.Stub;
import org.metawidget.android.widget.widgetprocessor.binding.BindingConverter;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

import android.R;
import android.content.Context;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.method.DateKeyListener;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * WidgetBuilder for Android environments.
 * <p>
 * Creates native Android Views, such as <code>EditText</code> and <code>Spinner</code>, to suit the
 * inspected fields.
 *
 * @author Richard Kennard
 */

public class AndroidWidgetBuilder
	implements WidgetBuilder<View, AndroidMetawidget>, AndroidValueAccessor {

	//
	// Public methods
	//

	@SuppressWarnings( "deprecation" )
	public Object getValue( View view ) {

		// CheckBox

		if ( view instanceof CheckBox ) {
			return Boolean.valueOf( ( (CheckBox) view ).isChecked() );
		}

		// TextView/EditText

		if ( view instanceof TextView ) {
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

		// DatePicker

		if ( view instanceof DatePicker ) {
			DatePicker datePicker = (DatePicker) view;
			return new Date( datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth() );
		}

		// Spinner

		if ( view instanceof Spinner ) {
			return ( (Spinner) view ).getSelectedItem();
		}

		return null;
	}

	@SuppressWarnings( "deprecation" )
	public boolean setValue( Object value, View view ) {

		// CheckBox

		if ( view instanceof CheckBox ) {
			( (CheckBox) view ).setChecked( (Boolean) value );
			return true;
		}

		// TextView/EditText

		if ( view instanceof TextView ) {
			( (TextView) view ).setText( StringUtils.quietValueOf( value ) );
			return true;
		}

		// DatePicker

		if ( view instanceof DatePicker ) {
			Date date = (Date) value;
			( (DatePicker) view ).updateDate( 1900 + date.getYear(), date.getMonth(), date.getDate() );
			return true;
		}

		// Spinner

		if ( view instanceof Spinner ) {
			AdapterView<?> adapterView = (AdapterView<?>) view;
			@SuppressWarnings( "unchecked" )
			ArrayAdapter<Object> adapter = (ArrayAdapter<Object>) adapterView.getAdapter();
			adapterView.setSelection( adapter.getPosition( value ) );
			return true;
		}

		// Unknown

		return false;
	}

	public View buildWidget( String elementName, Map<String, String> attributes, AndroidMetawidget metawidget ) {

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return new Stub( metawidget.getContext() );
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			return new Stub( metawidget.getContext() );
		}

		// Lookup the Class

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, String.class, getClass().getClassLoader() );

		// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
		// Lookup)

		if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) ) {
			return new CheckBox( metawidget.getContext() );
		}

		if ( clazz != null ) {
			// String Lookups

			String lookup = attributes.get( LOOKUP );

			if ( lookup != null && !"".equals( lookup ) ) {
				Spinner spinner = new Spinner( metawidget.getContext() );

				// Empty option

				List<Object> lookupList = CollectionUtils.newArrayList();

				if ( WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
					lookupList.add( null );
				}

				// Lookup the Class
				//
				// (use TYPE, not ACTUAL_TYPE, because an Enum with a value will get a type of Enum$1)

				Class<?> nonActualType;
				String type = attributes.get( TYPE );

				if ( type != null ) {
					nonActualType = ClassUtils.niceForName( type );
				} else {
					nonActualType = null;
				}

				BindingConverter bindingConverter = metawidget.getWidgetProcessor( BindingConverter.class );

				for ( String value : CollectionUtils.fromString( lookup ) ) {

					Object convertedValue = value;

					if ( bindingConverter != null && nonActualType != null ) {
						convertedValue = bindingConverter.convertFromString( value, nonActualType );
					}

					lookupList.add( convertedValue );
				}

				// Labels

				List<String> lookupLabelsList = null;
				String lookupLabels = attributes.get( LOOKUP_LABELS );

				// (CollectionUtils.fromString returns unmodifiable EMPTY_LIST if empty)

				lookupLabelsList = CollectionUtils.fromString( lookupLabels );

				if ( !lookupLabelsList.isEmpty() && WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
					lookupLabelsList.add( 0, null );
				}

				ArrayAdapter<Object> adapter = new LookupArrayAdapter<Object>( metawidget.getContext(), lookupList, lookupLabelsList );
				spinner.setAdapter( adapter );

				return spinner;
			}

			if ( clazz.isPrimitive() ) {
				// booleans

				if ( boolean.class.equals( clazz ) ) {
					return new CheckBox( metawidget.getContext() );
				}

				if ( char.class.equals( clazz ) ) {
					EditText editText = new EditText( metawidget.getContext() );
					editText.setFilters( new InputFilter[] { new InputFilter.LengthFilter( 1 ) } );

					return editText;
				}

				EditText editText = new EditText( metawidget.getContext() );

				// DigitsInputMethod is 0-9 and +

				if ( byte.class.equals( clazz ) || short.class.equals( clazz ) || int.class.equals( clazz ) || long.class.equals( clazz ) ) {
					editText.setKeyListener( new DigitsKeyListener() );
				}

				return editText;
			}

			// Strings

			if ( String.class.equals( clazz ) ) {
				EditText editText = new EditText( metawidget.getContext() );

				if ( TRUE.equals( attributes.get( MASKED ) ) ) {
					editText.setTransformationMethod( PasswordTransformationMethod.getInstance() );
				}

				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					editText.setMinLines( 3 );
				}

				String maximumLength = attributes.get( MAXIMUM_LENGTH );

				if ( maximumLength != null && !"".equals( maximumLength ) ) {
					editText.setFilters( new InputFilter[] { new InputFilter.LengthFilter( Integer.parseInt( maximumLength ) ) } );
				}

				return editText;
			}

			// Characters

			if ( Character.class.equals( clazz ) ) {
				EditText editText = new EditText( metawidget.getContext() );
				editText.setFilters( new InputFilter[] { new InputFilter.LengthFilter( 1 ) } );

				return editText;
			}

			// Dates

			if ( Date.class.equals( clazz ) ) {
				// Not-nullable dates can use a DatePicker...

				if ( TRUE.equals( attributes.get( REQUIRED ) ) ) {
					return new DatePicker( metawidget.getContext() );
				}

				// ...but nullable ones need a TextBox

				EditText editText = new EditText( metawidget.getContext() );
				editText.setKeyListener( new DateKeyListener() );

				return editText;

			}

			// Numbers

			if ( Number.class.isAssignableFrom( clazz ) ) {
				EditText editText = new EditText( metawidget.getContext() );

				// DigitsInputMethod is 0-9 and +

				if ( Byte.class.isAssignableFrom( clazz ) || Short.class.isAssignableFrom( clazz ) || Integer.class.isAssignableFrom( clazz ) || Long.class.isAssignableFrom( clazz ) ) {
					editText.setKeyListener( new DigitsKeyListener() );
				}

				return editText;
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				return new Stub( metawidget.getContext() );
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return new EditText( metawidget.getContext() );
		}

		// Nested Metawidget

		return null;
	}

	//
	// Inner class
	//

	/**
	 * ArrayAdapter for Spinner whose values use a lookup.
	 */

	static class LookupArrayAdapter<T>
		extends ArrayAdapter<T> {

		//
		// Private members
		//

		private List<String>	mLabels;

		//
		// Constructor
		//

		/**
		 * @param labels
		 *            List of human-readable labels. Never null.
		 */

		public LookupArrayAdapter( Context context, List<T> values, List<String> labels ) {

			super( context, 0, values );

			if ( labels != null && !labels.isEmpty() ) {
				if ( labels.size() != values.size() ) {
					throw MetawidgetException.newException( "Labels list must be same size as values list" );
				}

				mLabels = labels;
			}
		}

		//
		// Public methods
		//

		@Override
		public View getView( int position, View convertView, ViewGroup parentView ) {

			return initView( position, convertView, parentView, R.layout.simple_spinner_item );
		}

		@Override
		public View getDropDownView( int position, View convertView, ViewGroup parentView ) {

			return initView( position, convertView, parentView, R.layout.simple_spinner_dropdown_item );
		}

		//
		// Private methods
		//

		private View initView( int position, View convertView, ViewGroup parentView, int textViewResourceId ) {

			View viewToUse = convertView;

			if ( viewToUse == null ) {
				Context context = getContext();

				if ( context != null ) {
					viewToUse = ( (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE ) ).inflate( textViewResourceId, parentView, false );
				}

				// Just-in-time create: layoutInflater.inflate does not work during onMeasure. Why?

				if ( viewToUse == null ) {
					viewToUse = new TextView( context );
				}
			}

			if ( mLabels == null ) {
				( (TextView) viewToUse ).setText( StringUtils.quietValueOf( getItem( position ) ) );
			} else {
				( (TextView) viewToUse ).setText( StringUtils.quietValueOf( mLabels.get( position ) ) );
			}

			return viewToUse;
		}
	}
}
