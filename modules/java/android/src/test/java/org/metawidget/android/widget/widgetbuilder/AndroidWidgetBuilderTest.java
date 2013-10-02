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

import junit.framework.TestCase;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.Stub;
import org.metawidget.android.widget.widgetbuilder.AndroidWidgetBuilder.LookupArrayAdapter;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.util.CollectionUtils;

import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.method.DateKeyListener;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class AndroidWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testReadOnly() {

		AndroidMetawidget metawidget = new AndroidMetawidget( null );
		ReadOnlyWidgetBuilder androidWidgetBuilder = new ReadOnlyWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( READ_ONLY, TRUE );

		// Hidden

		attributes.put( HIDDEN, TRUE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof Stub );
		attributes.remove( HIDDEN );

		// Action

		assertTrue( androidWidgetBuilder.buildWidget( ACTION, attributes, metawidget ) instanceof Stub );

		// Masked

		assertEquals( View.VISIBLE, ( (TextView) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getVisibility() );
		attributes.put( MASKED, TRUE );
		assertEquals( View.INVISIBLE, ( (TextView) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getVisibility() );
		attributes.remove( MASKED );

		// Lookup

		attributes.put( LOOKUP, "foo, bar, baz" );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof TextView );
		attributes.remove( LOOKUP );

		// No type

		attributes.remove( TYPE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof TextView );

		// Primitive

		attributes.put( TYPE, int.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof TextView );

		attributes.put( TYPE, char.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof TextView );

		// Character

		attributes.put( TYPE, Character.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof TextView );

		// Date

		attributes.put( TYPE, Date.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof TextView );

		// Boolean

		attributes.put( TYPE, Boolean.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof TextView );

		// Number

		attributes.put( TYPE, Number.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof TextView );

		// Collection

		attributes.put( TYPE, Collection.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof Stub );

		// Metawidget

		attributes.put( TYPE, "unknown-type" );
		assertEquals( null, androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) );

		// Don't expand

		attributes.put( DONT_EXPAND, TRUE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof TextView );
	}

	public void testActive()
		throws Exception {

		AndroidMetawidget metawidget = new AndroidMetawidget( null );
		AndroidWidgetBuilder androidWidgetBuilder = new AndroidWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Hidden

		attributes.put( HIDDEN, TRUE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof Stub );
		attributes.remove( HIDDEN );

		// Action

		attributes.put( NAME, "fooButton" );
		Button button = (Button) androidWidgetBuilder.buildWidget( ACTION, attributes, metawidget );
		assertEquals( "Foo Button", button.getText() );

		// No type

		attributes.remove( TYPE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof EditText );

		// Boolean (big B)

		attributes.put( TYPE, Boolean.class.getName() );
		attributes.put( REQUIRED, TRUE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof CheckBox );

		// Lookups (without labels)

		attributes.remove( TYPE );
		attributes.put( LOOKUP, "foo, bar,baz" );
		Spinner spinner = (Spinner) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		@SuppressWarnings( "unchecked" )
		ArrayAdapter<String> adapter1 = (ArrayAdapter<String>) ( (AdapterView<?>) spinner ).getAdapter();
		assertEquals( "foo", adapter1.getItem( 0 ) );
		assertEquals( "foo", ( (TextView) adapter1.getView( 0, null, null ) ).getText() );
		assertEquals( "bar", adapter1.getItem( 1 ) );
		assertEquals( "bar", ( (TextView) adapter1.getView( 1, null, null ) ).getText() );
		assertEquals( "baz", adapter1.getItem( 2 ) );
		assertEquals( "baz", ( (TextView) adapter1.getView( 2, null, null ) ).getText() );

		// Lookups (with labels)

		attributes.put( LOOKUP_LABELS, "Foo #1, Bar #2" );

		try {
			spinner = (Spinner) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
			fail();
		} catch ( MetawidgetException e ) {
			assertEquals( "Labels list must be same size as values list", e.getMessage() );
		}

		// Lookups (with labels)

		attributes.put( LOOKUP_LABELS, "Foo #1, Bar #2, Baz #3" );
		spinner = (Spinner) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		@SuppressWarnings( "unchecked" )
		ArrayAdapter<String> adapter2 = (ArrayAdapter<String>) ( (AdapterView<?>) spinner ).getAdapter();
		assertEquals( "foo", adapter2.getItem( 0 ) );
		assertEquals( "Foo #1", ( (TextView) adapter2.getView( 0, null, null ) ).getText() );
		assertEquals( "bar", adapter2.getItem( 1 ) );
		assertEquals( "Bar #2", ( (TextView) adapter2.getView( 1, null, null ) ).getText() );
		assertEquals( "baz", adapter2.getItem( 2 ) );
		assertEquals( "Baz #3", ( (TextView) adapter2.getView( 2, null, null ) ).getText() );

		// Lookups (with required)

		attributes.remove( REQUIRED );
		attributes.put( TYPE, String.class.getName() );
		spinner = (Spinner) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		@SuppressWarnings( "unchecked" )
		ArrayAdapter<String> adapter3 = (ArrayAdapter<String>) ( (AdapterView<?>) spinner ).getAdapter();
		assertEquals( null, adapter3.getItem( 0 ) );
		assertEquals( "", ( (TextView) adapter3.getView( 0, null, null ) ).getText() );
		assertEquals( "foo", adapter3.getItem( 1 ) );
		assertEquals( "Foo #1", ( (TextView) adapter3.getView( 1, null, null ) ).getText() );
		assertEquals( "bar", adapter3.getItem( 2 ) );
		assertEquals( "Bar #2", ( (TextView) adapter3.getView( 2, null, null ) ).getText() );
		assertEquals( "baz", adapter3.getItem( 3 ) );
		assertEquals( "Baz #3", ( (TextView) adapter3.getView( 3, null, null ) ).getText() );

		// boolean (little B)

		attributes.remove( LOOKUP );
		attributes.put( TYPE, boolean.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof CheckBox );

		// char

		attributes.put( TYPE, char.class.getName() );
		EditText editText = (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( 1, ((InputFilter.LengthFilter) editText.getFilters()[0]).getLength() );

		// float

		attributes.put( TYPE, float.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof EditText );

		// int

		assertEquals( ( (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getKeyListener(), null );
		attributes.put( TYPE, int.class.getName() );
		assertTrue( ( (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getKeyListener() instanceof DigitsKeyListener );

		// Masked

		attributes.put( TYPE, String.class.getName() );
		attributes.put( MASKED, TRUE );
		assertTrue( ( (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getTransformationMethod() instanceof PasswordTransformationMethod );

		// Float

		attributes.put( TYPE, Float.class.getName() );
		assertEquals( ( (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getKeyListener(), null );

		// Int

		attributes.put( TYPE, Integer.class.getName() );
		assertTrue( ( (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getKeyListener() instanceof DigitsKeyListener );

		// Character

		attributes.put( TYPE, Character.class.getName() );
		editText = (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( 1, ((InputFilter.LengthFilter) editText.getFilters()[0]).getLength() );

		// Date

		attributes.put( REQUIRED, TRUE );
		attributes.put( TYPE, Date.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof DatePicker );
		attributes.remove( REQUIRED );
		assertTrue( ( (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getKeyListener() instanceof DateKeyListener );

		// Collection

		attributes.put( TYPE, Collection.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof Stub );

		// Large

		attributes.put( TYPE, String.class.getName() );
		attributes.put( LARGE, TRUE );
		editText = (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertTrue( 3 == (Integer) editText.getClass().getMethod( "getMinLines", (Class[]) null ).invoke( editText, (Object[]) null ) );

		// Limited length

		attributes.put( MAXIMUM_LENGTH, "12" );
		InputFilter filter = ( (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getFilters()[0];
		assertTrue( 12 == (Integer) filter.getClass().getMethod( "getLength", (Class[]) null ).invoke( filter, (Object[]) null ) );

		// Metawidget

		attributes.put( TYPE, "unknown-type" );
		assertEquals( null, androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) );

		// Don't expand

		attributes.put( DONT_EXPAND, TRUE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof EditText );
	}

	@SuppressWarnings( "deprecation" )
	public void testGetSetValue() {

		AndroidWidgetBuilder androidWidgetBuilder = new AndroidWidgetBuilder();

		// Pass through

		assertEquals( null, androidWidgetBuilder.getValue( null ) );
		assertEquals( null, androidWidgetBuilder.getValue( new View( null ) ) );
		assertFalse( androidWidgetBuilder.setValue( null, null ) );

		// CheckBox

		CheckBox checkBox = new CheckBox( null );
		assertTrue( false == (Boolean) androidWidgetBuilder.getValue( checkBox ) );
		androidWidgetBuilder.setValue( true, checkBox );
		assertTrue( checkBox.isChecked() );
		assertTrue( true == (Boolean) androidWidgetBuilder.getValue( checkBox ) );

		// TextView

		TextView textView = new TextView( null );
		assertEquals( null, androidWidgetBuilder.getValue( textView ) );
		androidWidgetBuilder.setValue( "foo", textView );
		assertEquals( "foo", textView.getText() );
		assertEquals( "foo", androidWidgetBuilder.getValue( textView ) );

		textView.setText( new SpannableStringBuilder( "foo" ) );
		assertEquals( "foo", androidWidgetBuilder.getValue( textView ) );

		// DatePicker

		DatePicker datePicker = new DatePicker( null );
		Date date = new Date( 75, 4, 9 );
		androidWidgetBuilder.setValue( date, datePicker );
		assertEquals( 1975, datePicker.getYear() );
		assertEquals( 4, datePicker.getMonth() );
		assertEquals( 9, datePicker.getDayOfMonth() );
		assertEquals( 75, ( (Date) androidWidgetBuilder.getValue( datePicker ) ).getYear() );
		assertEquals( 4, ( (Date) androidWidgetBuilder.getValue( datePicker ) ).getMonth() );
		assertEquals( 9, ( (Date) androidWidgetBuilder.getValue( datePicker ) ).getDate() );

		// Spinner

		Spinner spinner = new Spinner( null );
		List<String> lookupList = CollectionUtils.newArrayList( "foo", "bar", "baz" );
		spinner.setAdapter( new LookupArrayAdapter<String>( null, lookupList, null ) );
		assertEquals( "foo", androidWidgetBuilder.getValue( spinner ) );
		androidWidgetBuilder.setValue( "bar", spinner );
		assertEquals( "bar", androidWidgetBuilder.getValue( spinner ) );
	}
}
