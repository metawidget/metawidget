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

import junit.framework.TestCase;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.Stub;
import org.metawidget.util.CollectionUtils;

import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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
		assertTrue( null == androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ));

		// Don't expand

		attributes.put( DONT_EXPAND, TRUE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof TextView );
	}

	public void testActive()
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

		// Primitive

		attributes.put( TYPE, float.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof EditText );

		// Float

		attributes.put( TYPE, Float.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof EditText );

		// boolean (little B)

		attributes.put( TYPE, boolean.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof CheckBox );

		// Boolean (big B)

		attributes.put( TYPE, Boolean.class.getName() );
		attributes.put( REQUIRED, TRUE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof CheckBox );

		// Date

		attributes.put( TYPE, Date.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof DatePicker );

		// Collection

		attributes.put( TYPE, Collection.class.getName() );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof Stub );

		// Large

		attributes.put( TYPE, String.class.getName() );
		attributes.put( LARGE, TRUE );
		assertTrue( 3 == ((EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget )).getMinLines() );

		// Limited length

		//attributes.put( MAXIMUM_LENGTH, "10" );
		//assertTrue( ((EditText) androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget )).getFilters()[0] instanceof InputFilter.LengthFilter );

		// Metawidget

		attributes.put( TYPE, "unknown-type" );
		assertTrue( null == androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ));

		// Don't expand

		attributes.put( DONT_EXPAND, TRUE );
		assertTrue( androidWidgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof EditText );
	}
}
