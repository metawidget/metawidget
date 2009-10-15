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

import junit.framework.TestCase;

import org.metawidget.android.AndroidUtils.ResourcelessArrayAdapter;
import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.Stub;
import org.metawidget.util.CollectionUtils;

import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.method.DateKeyListener;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author Richard Kennard
 */

public class AndroidWidgetBuilderTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testReadOnly()
	{
		AndroidMetawidget metawidget = new AndroidMetawidget( null );
		AndroidWidgetBuilder androidWidgetBuilder = new AndroidWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( READ_ONLY, TRUE );

		// Hidden

		attributes.put( HIDDEN, TRUE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof Stub );
		attributes.remove( HIDDEN );

		// Action

		assertTrue( androidWidgetBuilder.buildWidget( ACTION, attributes, metawidget ) instanceof Stub );

		// Masked

		assertTrue( View.VISIBLE == ( (TextView) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getVisibility() );
		attributes.put( MASKED, TRUE );
		assertTrue( View.INVISIBLE == ( (TextView) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getVisibility() );
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
		assertTrue( null == androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) );

		// Don't expand

		attributes.put( DONT_EXPAND, TRUE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof TextView );
	}

	public void testActive()
		throws Exception
	{
		AndroidMetawidget metawidget = new AndroidMetawidget( null );
		AndroidWidgetBuilder androidWidgetBuilder = new AndroidWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Hidden

		attributes.put( HIDDEN, TRUE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof Stub );
		attributes.remove( HIDDEN );

		// Action

		assertTrue( androidWidgetBuilder.buildWidget( ACTION, attributes, metawidget ) instanceof Stub );

		// No type

		attributes.remove( TYPE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof EditText );

		// Boolean (big B)

		attributes.put( TYPE, Boolean.class.getName() );
		attributes.put( REQUIRED, TRUE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof CheckBox );

		// Lookups

		attributes.remove( TYPE );
		attributes.put( LOOKUP, "foo, bar,baz" );
		attributes.put( LOOKUP_LABELS, "Foo #1, Bar #2, Baz #3" );
		Spinner spinner = (Spinner) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		@SuppressWarnings( "unchecked" )
		ResourcelessArrayAdapter<String> adapter1 = (ResourcelessArrayAdapter<String>) spinner.getAdapter();
		assertTrue( "foo".equals( adapter1.getItem( 0 )));
		assertTrue( "Foo #1".equals( ((TextView) adapter1.getView( 0, null, null )).getText() ));
		assertTrue( "bar".equals( adapter1.getItem( 1 )));
		assertTrue( "Bar #2".equals( ((TextView) adapter1.getView( 1, null, null )).getText() ));
		assertTrue( "baz".equals( adapter1.getItem( 2 )));
		assertTrue( "Baz #3".equals( ((TextView) adapter1.getView( 2, null, null )).getText() ));

		attributes.remove( REQUIRED );
		attributes.put( TYPE, String.class.getName() );
		spinner = (Spinner) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		@SuppressWarnings( "unchecked" )
		ResourcelessArrayAdapter<String> adapter2 = (ResourcelessArrayAdapter<String>) spinner.getAdapter();
		assertTrue( null == adapter2.getItem( 0 ));
		assertTrue( "".equals( ((TextView) adapter2.getView( 0, null, null )).getText() ));
		assertTrue( "foo".equals( adapter2.getItem( 1 )));
		assertTrue( "Foo #1".equals( ((TextView) adapter2.getView( 1, null, null )).getText() ));
		assertTrue( "bar".equals( adapter2.getItem( 2 )));
		assertTrue( "Bar #2".equals( ((TextView) adapter2.getView( 2, null, null )).getText() ));
		assertTrue( "baz".equals( adapter2.getItem( 3 )));
		assertTrue( "Baz #3".equals( ((TextView) adapter2.getView( 3, null, null )).getText() ));

		// boolean (little B)

		attributes.remove( LOOKUP );
		attributes.put( TYPE, boolean.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof CheckBox );

		// float

		attributes.put( TYPE, float.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof EditText );

		// int

		assertTrue( ( (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getKeyListener() == null );
		attributes.put( TYPE, int.class.getName() );
		assertTrue( ( (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getKeyListener() instanceof DigitsKeyListener );

		// Masked

		attributes.put( TYPE, String.class.getName() );
		attributes.put( MASKED, TRUE );
		assertTrue( ( (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getTransformationMethod() instanceof PasswordTransformationMethod );

		// Float

		attributes.put( TYPE, Float.class.getName() );
		assertTrue( ( (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getKeyListener() == null );

		// Int

		attributes.put( TYPE, Integer.class.getName() );
		assertTrue( ( (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getKeyListener() instanceof DigitsKeyListener );

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
		EditText editText = (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertTrue( 3 == (Integer) editText.getClass().getMethod( "getMinLines", (Class[]) null ).invoke( editText, (Object[]) null ) );

		// Limited length

		attributes.put( MAXIMUM_LENGTH, "12" );
		InputFilter filter = ( (EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) ).getFilters()[0];
		assertTrue( 12 == (Integer) filter.getClass().getMethod( "getLength", (Class[]) null ).invoke( filter, (Object[]) null ) );

		// Metawidget

		attributes.put( TYPE, "unknown-type" );
		assertTrue( null == androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) );

		// Don't expand

		attributes.put( DONT_EXPAND, TRUE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof EditText );
	}

	@SuppressWarnings( "deprecation" )
	public void testGetSetValue()
	{
		AndroidWidgetBuilder androidWidgetBuilder = new AndroidWidgetBuilder();

		// Pass through

		assertTrue( null == androidWidgetBuilder.getValue( null ) );
		assertTrue( null == androidWidgetBuilder.getValue( new View( null ) ) );
		assertTrue( !androidWidgetBuilder.setValue( null, null ) );

		// CheckBox

		CheckBox checkBox = new CheckBox( null );
		assertTrue( false == (Boolean) androidWidgetBuilder.getValue( checkBox ) );
		androidWidgetBuilder.setValue( true, checkBox );
		assertTrue( checkBox.isChecked() );
		assertTrue( true == (Boolean) androidWidgetBuilder.getValue( checkBox ) );

		// TextView

		TextView textView = new TextView( null );
		assertTrue( null == androidWidgetBuilder.getValue( textView ) );
		androidWidgetBuilder.setValue( "foo", textView );
		assertTrue( "foo".equals( textView.getText() ) );
		assertTrue( "foo".equals( androidWidgetBuilder.getValue( textView ) ) );

		textView.setText( new SpannableStringBuilder( "foo" ) );
		assertTrue( "foo".equals( androidWidgetBuilder.getValue( textView ) ) );

		// DatePicker

		DatePicker datePicker = new DatePicker( null );
		Date date = new Date( 75, 4, 9 );
		androidWidgetBuilder.setValue( date, datePicker );
		assertTrue( 1975 == datePicker.getYear() );
		assertTrue( 4 == datePicker.getMonth() );
		assertTrue( 9 == datePicker.getDayOfMonth() );
		assertTrue( 75 == ( (Date) androidWidgetBuilder.getValue( datePicker ) ).getYear() );
		assertTrue( 4 == ( (Date) androidWidgetBuilder.getValue( datePicker ) ).getMonth() );
		assertTrue( 9 == ( (Date) androidWidgetBuilder.getValue( datePicker ) ).getDate() );

		// Spinner

		Spinner spinner = new Spinner( null );
		List<String> lookupList = CollectionUtils.newArrayList( "foo", "bar", "baz" );
		spinner.setAdapter( new ResourcelessArrayAdapter<String>( null, lookupList, null ) );
		assertTrue( "foo".equals( androidWidgetBuilder.getValue( spinner ) ));
		androidWidgetBuilder.setValue( "bar", spinner );
		assertTrue( "bar".equals( androidWidgetBuilder.getValue( spinner ) ));
	}
}
