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

package org.metawidget.android;

import java.util.Iterator;
import java.util.LinkedHashMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.metawidget.android.widget.AndroidMetawidgetTest;
import org.metawidget.android.widget.FacetTest;
import org.metawidget.android.widget.StubTest;
import org.metawidget.android.widget.layout.HeadingSectionLayoutTest;
import org.metawidget.android.widget.layout.LinearLayoutTest;
import org.metawidget.android.widget.layout.TabSectionLayoutTest;
import org.metawidget.android.widget.layout.TableLayoutTest;
import org.metawidget.android.widget.widgetbuilder.AndroidWidgetBuilderTest;

import android.util.AttributeSet;

/**
 * @author Richard Kennard
 */

public class AndroidMetawidgetTests
	extends TestCase
{
	//
	// Public statics
	//

	public static Test suite()
	{
		TestSuite suite = new TestSuite( "Android Metawidget Tests" );

		suite.addTestSuite( AndroidConfigReaderTest.class );
		suite.addTestSuite( AndroidMetawidgetTest.class );
		suite.addTestSuite( AndroidWidgetBuilderTest.class );
		suite.addTestSuite( FacetTest.class );
		suite.addTestSuite( HeadingSectionLayoutTest.class );
		suite.addTestSuite( LinearLayoutTest.class );
		suite.addTestSuite( StubTest.class );
		suite.addTestSuite( TableLayoutTest.class );
		suite.addTestSuite( TabSectionLayoutTest.class );

		return suite;
	}

	//
	// Inner class
	//

	public static class MockAttributeSet
		implements AttributeSet
	{
		//
		// Private members
		//

		private LinkedHashMap<String, String>	mAttributes	= new LinkedHashMap<String, String>();

		//
		// Supported public methods
		//

		public void setAttributeValue( String key, String value )
		{
			mAttributes.put( key, value );
		}

		public String getAttributeValue( String object, String key )
		{
			return mAttributes.get( key );
		}

		public int getAttributeCount()
		{
			return mAttributes.size();
		}

		public String getAttributeValue( int index )
		{
			Iterator<String> iterator = mAttributes.values().iterator();

			for ( int loop = 0; loop < index - 1; loop++ )
			{
				iterator.next();
			}

			return iterator.next();
		}

		public String getAttributeName( int index )
		{
			Iterator<String> iterator = mAttributes.keySet().iterator();

			for ( int loop = 0; loop < index - 1; loop++ )
			{
				iterator.next();
			}

			return iterator.next();
		}

		public int getAttributeResourceValue( String object, String string, int i )
		{
			return 0;
		}

		//
		// Unsupported public methods
		//

		@Override
		public boolean getAttributeBooleanValue( int arg0, boolean arg1 )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean getAttributeBooleanValue( String arg0, String arg1, boolean arg2 )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public float getAttributeFloatValue( int arg0, float arg1 )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public float getAttributeFloatValue( String arg0, String arg1, float arg2 )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public int getAttributeIntValue( int arg0, int arg1 )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public int getAttributeIntValue( String arg0, String arg1, int arg2 )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public int getAttributeListValue( int arg0, String[] arg1, int arg2 )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public int getAttributeListValue( String arg0, String arg1, String[] arg2, int arg3 )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public int getAttributeNameResource( int arg0 )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public int getAttributeResourceValue( int arg0, int arg1 )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public int getAttributeUnsignedIntValue( int arg0, int arg1 )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public int getAttributeUnsignedIntValue( String arg0, String arg1, int arg2 )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public String getClassAttribute()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public String getIdAttribute()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public int getIdAttributeResourceValue( int arg0 )
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public String getPositionDescription()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public int getStyleAttribute()
		{
			throw new UnsupportedOperationException();
		}
	}
}
