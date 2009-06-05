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
import java.util.List;
import java.util.Map;

import org.metawidget.android.AndroidUtils.ResourcelessArrayAdapter;
import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.AndroidValueAccessor;
import org.metawidget.android.widget.Stub;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.impl.BaseWidgetBuilder;

import android.text.InputFilter;
import android.text.method.DateKeyListener;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
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
 * Automatically creates native Android Views, such as <code>EditText</code> and
 * <code>Spinner</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

public class AndroidWidgetBuilder
	extends BaseWidgetBuilder<View, AndroidMetawidget>
	implements AndroidValueAccessor
{
	//
	// Public methods
	//

	@SuppressWarnings( "deprecation" )
	public Object getValue( View view )
	{
		// CheckBox

		if ( view instanceof CheckBox )
			return Boolean.valueOf( ( (CheckBox) view ).isChecked() );

		// EditText

		if ( view instanceof EditText )
			return ( (EditText) view ).getText().toString();

		// TextView

		if ( view instanceof TextView )
			return ( (TextView) view ).getText();

		// DatePicker

		if ( view instanceof DatePicker )
		{
			DatePicker datePicker = (DatePicker) view;
			return new Date( datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth() );
		}

		// AdapterView

		if ( view instanceof AdapterView )
			return ( (AdapterView<?>) view ).getSelectedItem();

		return null;
	}

	@SuppressWarnings( "deprecation" )
	public boolean setValue( Object value, View view )
	{
		// CheckBox

		if ( view instanceof CheckBox )
		{
			( (CheckBox) view ).setChecked( (Boolean) value );
			return true;
		}

		// EditView/TextView

		if ( view instanceof TextView )
		{
			( (TextView) view ).setText( StringUtils.quietValueOf( value ) );
			return true;
		}

		// DatePicker

		if ( view instanceof DatePicker )
		{
			Date date = (Date) value;
			( (DatePicker) view ).updateDate( 1900 + date.getYear(), date.getMonth(), date.getDate() );
			return true;
		}

		// AdapterView

		if ( view instanceof AdapterView )
		{
			@SuppressWarnings( "unchecked" )
			AdapterView<ArrayAdapter<Object>> adapterView = (AdapterView<ArrayAdapter<Object>>) view;

			// Set the backing collection

			if ( value instanceof Collection )
			{
				@SuppressWarnings( "unchecked" )
				Collection<Object> collection = (Collection<Object>) value;
				adapterView.setAdapter( new ResourcelessArrayAdapter<Object>( view.getContext(), collection ) );
			}

			// Set the selected value

			else
			{
				adapterView.setSelection( adapterView.getAdapter().getPosition( value ) );
			}

			return true;
		}

		// Unknown

		return false;
	}

	//
	// Protected methods
	//

	@Override
	protected View buildReadOnlyWidget( String elementName, Map<String, String> attributes, AndroidMetawidget metawidget )
		throws Exception
	{
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

		String type = getType( attributes );

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

	@Override
	protected View buildActiveWidget( String elementName, Map<String, String> attributes, AndroidMetawidget metawidget )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return new Stub( metawidget.getContext() );

		// Action

		if ( ACTION.equals( elementName ) )
			return new Stub( metawidget.getContext() );

		String type = getType( attributes );

		// If no type, assume a String

		if ( type == null )
			type = String.class.getName();

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
		// Lookup)

		if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) )
			return new CheckBox( metawidget.getContext() );

		if ( clazz != null )
		{
			// String Lookups

			String lookup = attributes.get( LOOKUP );

			if ( lookup != null && !"".equals( lookup ) )
			{
				List<String> lookupList = CollectionUtils.fromString( lookup );

				// Add an empty choice (if nullable, and not required)
				//
				// (CollectionUtils.fromString returns unmodifiable EMPTY_LIST if empty)

				if ( !clazz.isPrimitive() && !TRUE.equals( attributes.get( REQUIRED ) ) )
					lookupList.add( 0, null );

				List<String> lookupLabelsList = null;
				String lookupLabels = attributes.get( LOOKUP_LABELS );

				if ( lookupLabels != null && !"".equals( lookupLabels ) )
				{
					lookupLabelsList = CollectionUtils.fromString( lookupLabels );

					// (CollectionUtils.fromString returns unmodifiable EMPTY_LIST if empty)

					if ( !lookupLabelsList.isEmpty() )
						lookupLabelsList.add( 0, null );
				}

				Spinner spinner = new Spinner( metawidget.getContext() );
				spinner.setAdapter( new ResourcelessArrayAdapter<String>( metawidget.getContext(), lookupList, lookupLabelsList ) );

				return spinner;
			}

			if ( clazz.isPrimitive() )
			{
				// booleans

				if ( boolean.class.equals( clazz ) )
					return new CheckBox( metawidget.getContext() );

				EditText editText = new EditText( metawidget.getContext() );

				// DigitsInputMethod is 0-9 and +

				if ( byte.class.equals( clazz ) || short.class.equals( clazz ) || int.class.equals( clazz ) || long.class.equals( clazz ) )
					editText.setKeyListener( new DigitsKeyListener() );

				return editText;
			}

			// Strings

			if ( String.class.equals( clazz ) )
			{
				EditText editText = new EditText( metawidget.getContext() );

				if ( TRUE.equals( attributes.get( MASKED ) ) )
					editText.setTransformationMethod( PasswordTransformationMethod.getInstance() );

				if ( TRUE.equals( attributes.get( LARGE ) ) )
					editText.setMinLines( 3 );

				String maximumLength = attributes.get( MAXIMUM_LENGTH );

				if ( maximumLength != null && !"".equals( maximumLength ) )
					editText.setFilters( new InputFilter[] { new InputFilter.LengthFilter( Integer.parseInt( maximumLength ) ) } );

				return editText;
			}

			// Dates

			if ( Date.class.equals( clazz ) )
			{
				// Not-nullable dates can use a DatePicker...

				if ( TRUE.equals( attributes.get( REQUIRED ) ) )
					return new DatePicker( metawidget.getContext() );

				// ...but nullable ones need a TextBox

				EditText editText = new EditText( metawidget.getContext() );
				editText.setFilters( new InputFilter[] { new DateKeyListener() } );

				return editText;

			}

			// Numbers

			if ( Number.class.isAssignableFrom( clazz ) )
			{
				EditText editText = new EditText( metawidget.getContext() );

				// DigitsInputMethod is 0-9 and +

				if ( Byte.class.isAssignableFrom( clazz ) || Short.class.isAssignableFrom( clazz ) || Integer.class.isAssignableFrom( clazz ) || Long.class.isAssignableFrom( clazz ) )
					editText.setKeyListener( new DigitsKeyListener() );

				return editText;
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return new Stub( metawidget.getContext() );
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return new EditText( metawidget.getContext() );

		// Nested Metawidget

		return null;
	}
}
