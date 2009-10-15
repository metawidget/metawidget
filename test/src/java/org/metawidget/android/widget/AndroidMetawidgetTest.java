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

package org.metawidget.android.widget;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.android.AndroidMetawidgetTests.MockAttributeSet;
import org.metawidget.android.widget.widgetbuilder.AndroidWidgetBuilder;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.util.CollectionUtils;

import android.widget.EditText;

/**
 * @author Richard Kennard
 */

public class AndroidMetawidgetTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testGettersAndSetters()
	{
		// setToInspect and getPath

		AndroidMetawidget androidMetawidget = new AndroidMetawidget( null );
		androidMetawidget.setToInspect( new Date() );
		assertTrue( "java.util.Date".equals( androidMetawidget.getPath() ) );
		androidMetawidget.setPath( "foo" );
		assertTrue( "foo".equals( androidMetawidget.getPath() ) );

		// getLabelString and getLocalizedKey

		assertTrue( "".equals( androidMetawidget.getLabelString( null ) ) );

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( LABEL, "camelCase" );
		assertTrue( "camelCase".equals( androidMetawidget.getLabelString( attributes ) ) );
		attributes.put( NAME, "foo" );
		assertTrue( "camelCase".equals( androidMetawidget.getLabelString( attributes ) ) );
		attributes.remove( LABEL );
		assertTrue( "Foo".equals( androidMetawidget.getLabelString( attributes ) ) );
		attributes.put( NAME, "fooBar" );
		assertTrue( "Foo bar".equals( androidMetawidget.getLabelString( attributes ) ) );
		attributes.put( LABEL, "" );
		assertTrue( null == androidMetawidget.getLabelString( attributes ) );

		assertTrue( null == androidMetawidget.getLocalizedKey( null ) );

		// clientProperties

		assertTrue( null == androidMetawidget.getClientProperty( "foo" ) );
		androidMetawidget.putClientProperty( "foo", "bar" );
		assertTrue( "bar".equals( androidMetawidget.getClientProperty( "foo" ) ) );

		// maximumInspectionDepth

		assertTrue( 10 == androidMetawidget.getMaximumInspectionDepth() );
		androidMetawidget.setMaximumInspectionDepth( 2 );
		assertTrue( 2 == androidMetawidget.getMaximumInspectionDepth() );

		// readOnly

		assertTrue( !androidMetawidget.isReadOnly() );
		androidMetawidget.setReadOnly( true );
		assertTrue( androidMetawidget.isReadOnly() );

		MockAttributeSet mockAttributeSet = new MockAttributeSet();
		androidMetawidget = new AndroidMetawidget( null, mockAttributeSet );
		assertTrue( !androidMetawidget.isReadOnly() );

		mockAttributeSet.setAttributeValue( "readOnly", "false" );
		androidMetawidget = new AndroidMetawidget( null, mockAttributeSet );
		assertTrue( !androidMetawidget.isReadOnly() );

		mockAttributeSet.setAttributeValue( "readOnly", "true" );
		androidMetawidget = new AndroidMetawidget( null, mockAttributeSet );
		assertTrue( androidMetawidget.isReadOnly() );
	}

	public void testGetSetValue()
	{
		AndroidMetawidget androidMetawidget = new AndroidMetawidget( null );

		try
		{
			androidMetawidget.getValue();
			assertTrue( false );
		}
		catch( MetawidgetException e )
		{
			assertTrue( "No names specified".equals( e.getMessage() ) );
		}

		try
		{
			androidMetawidget.getValue( "foo" );
			assertTrue( false );
		}
		catch( MetawidgetException e )
		{
			assertTrue( "No View with tag foo".equals( e.getMessage() ) );
		}

		try
		{
			androidMetawidget.setValue( 2 );
			assertTrue( false );
		}
		catch( MetawidgetException e )
		{
			assertTrue( "No names specified".equals( e.getMessage() ) );
		}

		try
		{
			androidMetawidget.setValue( 2, "foo" );
			assertTrue( false );
		}
		catch( MetawidgetException e )
		{
			assertTrue( "No View with tag foo".equals( e.getMessage() ) );
		}

		EditText editText = new EditText( null );
		editText.setTag( "foo" );
		editText.setText( "Bar" );
		androidMetawidget.addView( editText );
		assertTrue( null == androidMetawidget.getValue( "foo" ));
		// TODO: remove this once .addView overridden
		androidMetawidget.setWidgetBuilder( new AndroidWidgetBuilder() );

		assertTrue( "Bar".equals( androidMetawidget.getValue( "foo" )));
		androidMetawidget.setValue( "Baz", "foo" );
		assertTrue( "Baz".equals( androidMetawidget.getValue( "foo" )));
	}
}
