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
		MockAndroidWidgetBuilder androidWidgetBuilder = new MockAndroidWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( READ_ONLY, TRUE );

		// Hidden

		attributes.put( HIDDEN, TRUE );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "Stub".equals( androidWidgetBuilder.getLastCreated() ) );
		attributes.remove( HIDDEN );

		// Action

		androidWidgetBuilder.buildWidget( ACTION, attributes, null );
		assertTrue( "Stub".equals( androidWidgetBuilder.getLastCreated() ) );

		// Lookup

		attributes.put( LOOKUP, "foo, bar, baz" );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "TextView".equals( androidWidgetBuilder.getLastCreated() ) );
		attributes.remove( LOOKUP );

		// No type

		attributes.remove( TYPE );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "TextView".equals( androidWidgetBuilder.getLastCreated() ) );

		// Primitive

		attributes.put( TYPE, int.class.getName() );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "TextView".equals( androidWidgetBuilder.getLastCreated() ) );

		// Date

		attributes.put( TYPE, Date.class.getName() );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "TextView".equals( androidWidgetBuilder.getLastCreated() ) );

		// Boolean

		attributes.put( TYPE, Boolean.class.getName() );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "TextView".equals( androidWidgetBuilder.getLastCreated() ) );

		// Number

		attributes.put( TYPE, Number.class.getName() );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "TextView".equals( androidWidgetBuilder.getLastCreated() ) );

		// Collection

		attributes.put( TYPE, Collection.class.getName() );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "Stub".equals( androidWidgetBuilder.getLastCreated() ) );

		// Metawidget

		attributes.put( TYPE, "unknown-type" );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( null == androidWidgetBuilder.getLastCreated() );

		// Don't expand

		attributes.put( DONT_EXPAND, TRUE );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "TextView".equals( androidWidgetBuilder.getLastCreated() ) );
	}

	public void testActive()
	{
		MockAndroidWidgetBuilder androidWidgetBuilder = new MockAndroidWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Hidden

		attributes.put( HIDDEN, TRUE );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "Stub".equals( androidWidgetBuilder.getLastCreated() ) );
		attributes.remove( HIDDEN );

		// Action

		androidWidgetBuilder.buildWidget( ACTION, attributes, null );
		assertTrue( "Stub".equals( androidWidgetBuilder.getLastCreated() ) );

		// No type

		attributes.remove( TYPE );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "EditText".equals( androidWidgetBuilder.getLastCreated() ) );

		// Primitive

		attributes.put( TYPE, float.class.getName() );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "EditText".equals( androidWidgetBuilder.getLastCreated() ) );

		// Float

		attributes.put( TYPE, Float.class.getName() );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "EditText".equals( androidWidgetBuilder.getLastCreated() ) );

		// boolean (little B)

		attributes.put( TYPE, boolean.class.getName() );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "CheckBox".equals( androidWidgetBuilder.getLastCreated() ) );

		// Boolean (big B)

		attributes.put( TYPE, Boolean.class.getName() );
		attributes.put( REQUIRED, TRUE );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "CheckBox".equals( androidWidgetBuilder.getLastCreated() ) );

		// Date

		attributes.put( TYPE, Date.class.getName() );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "DatePicker".equals( androidWidgetBuilder.getLastCreated() ) );

		// Collection

		attributes.put( TYPE, Collection.class.getName() );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "Stub".equals( androidWidgetBuilder.getLastCreated() ) );

		// Metawidget

		attributes.put( TYPE, "unknown-type" );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( null == androidWidgetBuilder.getLastCreated() );

		// Don't expand

		attributes.put( DONT_EXPAND, TRUE );
		androidWidgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "EditText".equals( androidWidgetBuilder.getLastCreated() ) );
	}

	//
	// Inner class
	//

	/* package private */static class MockAndroidWidgetBuilder
		extends AndroidWidgetBuilder
	{
		//
		// Private members
		//

		private String	mLastCreated;

		//
		// Public methods
		//

		public String getLastCreated()
		{
			String lastCreated = mLastCreated;
			mLastCreated = null;

			return lastCreated;
		}

		//
		// Protected methods
		//

		@Override
		CheckBox newCheckBox( AndroidMetawidget metawidget )
		{
			mLastCreated = "CheckBox";
			return null;
		}

		@Override
		DatePicker newDatePicker( AndroidMetawidget metawidget )
		{
			mLastCreated = "DatePicker";
			return null;
		}

		@Override
		EditText newEditText( AndroidMetawidget metawidget )
		{
			mLastCreated = "EditText";
			return null;
		}

		@Override
		Stub newStub( AndroidMetawidget metawidget )
		{
			mLastCreated = "Stub";
			return null;
		}

		@Override
		TextView newTextView( AndroidMetawidget metawidget )
		{
			mLastCreated = "TextView";
			return null;
		}
	}
}
