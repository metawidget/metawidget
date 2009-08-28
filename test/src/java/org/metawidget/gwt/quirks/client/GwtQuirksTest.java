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

package org.metawidget.gwt.quirks.client;

import java.util.Date;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.GwtUtils;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.gwt.client.widgetprocessor.binding.simple.SimpleBindingProcessor;
import org.metawidget.gwt.client.widgetprocessor.binding.simple.SimpleConverter;
import org.metawidget.gwt.quirks.client.ui.QuirksModule;
import org.metawidget.inspector.gwt.remote.client.GwtRemoteInspectorProxy;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * @author Richard Kennard
 */

public class GwtQuirksTest
	extends GWTTestCase
{
	//
	// Private statics
	//

	private final static int	TEST_FINISH_DELAY	= 50 * 5000;

	//
	// Public methods
	//

	@Override
	public String getModuleName()
	{
		return "org.metawidget.gwt.quirks.GwtQuirksTest";
	}

	public void testQuirks()
		throws Exception
	{
		// Start app

		final FlowPanel panel = new FlowPanel();
		final QuirksModule quirksModule = new QuirksModule( panel );
		quirksModule.onModuleLoad();

		final GwtMetawidget metawidget = (GwtMetawidget) panel.getWidget( 0 );
		assertTrue( "org.metawidget.gwt.quirks.client.model.GwtQuirks".equals( metawidget.getPath() ));

		executeAfterBuildWidgets( metawidget, new Timer()
		{
			@Override
			public void run()
			{
				// Test fields

				final FlexTable flexTable = (FlexTable) metawidget.getWidget( 0 );

				// Check what created

				assertTrue( "Boolean:".equals( flexTable.getText( 0, 0 ) ) );
				assertTrue( flexTable.getWidget( 0, 1 ) instanceof CheckBox );
				assertTrue( false == (Boolean) metawidget.getValue( "boolean" ) );
				( (CheckBox) flexTable.getWidget( 0, 1 ) ).setValue( true );
				assertTrue( "componentStyleName".equals( ( (CheckBox) flexTable.getWidget( 0, 1 ) ).getStyleName() ));

				assertTrue( "Foo:".equals( flexTable.getText( 2, 0 ) ) );
				Stub stub = (Stub) flexTable.getWidget( 2, 1 );
				assertTrue( "foo".equals( stub.getAttributes().get( "name" )));

				// Click a nested button

				assertTrue( "Nested quirks:".equals( flexTable.getText( 1, 0 ) ) );
				final GwtMetawidget nestedMetawidget = (GwtMetawidget) flexTable.getWidget( 1, 1 );

				assertTrue( 3 == flexTable.getRowCount() );

				executeAfterBuildWidgets( nestedMetawidget, new Timer()
				{
					@Override
					public void run()
					{
						final FlexTable nestedFlexTable = (FlexTable) nestedMetawidget.getWidget( 0 );
						assertTrue( "".equals( nestedFlexTable.getText( 0, 0 ) ) );
						Button nestedActionButton = (Button) nestedFlexTable.getWidget( 0, 1 );
						assertTrue( "Nested action".equals( nestedActionButton.getText() ) );

						try
						{
							fireClickEvent( nestedActionButton );
							assertTrue( false );
						}
						catch ( Exception e )
						{
							assertTrue( "nestedAction called".equals( e.getMessage() ) );
						}

						// Save and refresh

						metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );
						metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).rebind( metawidget.getToInspect(), metawidget );

						// Test checkbox was still checked (ie. HasText didn't get hit
						// first in GwtMetawidget.setValue)

						assertTrue( true == (Boolean) metawidget.getValue( "boolean" ) );

						// All done

						finish();
					}
				} );
			}
		} );

		// Test runs asynchronously

		delayTestFinish( TEST_FINISH_DELAY );
	}

	public void testGwtUtils()
		throws Exception
	{
		// isPrimitive

		assertTrue( GwtUtils.isPrimitive( byte.class.getName() ) );
		assertTrue( GwtUtils.isPrimitive( short.class.getName() ) );
		assertTrue( GwtUtils.isPrimitive( int.class.getName() ) );
		assertTrue( GwtUtils.isPrimitive( long.class.getName() ) );
		assertTrue( GwtUtils.isPrimitive( float.class.getName() ) );
		assertTrue( GwtUtils.isPrimitive( double.class.getName() ) );
		assertTrue( GwtUtils.isPrimitive( boolean.class.getName() ) );
		assertTrue( GwtUtils.isPrimitive( char.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitive( Byte.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitive( Short.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitive( Integer.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitive( Long.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitive( Float.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitive( Double.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitive( Boolean.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitive( Character.class.getName() ) );

		// isPrimitiveWrapper

		assertTrue( GwtUtils.isPrimitiveWrapper( Byte.class.getName() ));
		assertTrue( GwtUtils.isPrimitiveWrapper( Short.class.getName() ) );
		assertTrue( GwtUtils.isPrimitiveWrapper( Integer.class.getName() ) );
		assertTrue( GwtUtils.isPrimitiveWrapper( Long.class.getName() ) );
		assertTrue( GwtUtils.isPrimitiveWrapper( Float.class.getName() ) );
		assertTrue( GwtUtils.isPrimitiveWrapper( Double.class.getName() ) );
		assertTrue( GwtUtils.isPrimitiveWrapper( Boolean.class.getName() ) );
		assertTrue( GwtUtils.isPrimitiveWrapper( Character.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitiveWrapper( byte.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitiveWrapper( short.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitiveWrapper( int.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitiveWrapper( long.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitiveWrapper( float.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitiveWrapper( double.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitiveWrapper( boolean.class.getName() ) );
		assertTrue( !GwtUtils.isPrimitiveWrapper( char.class.getName() ) );

		// toString

		assertTrue( "".equals( GwtUtils.toString( (String[]) null, ',' ) ) );
		assertTrue( "foo#bar#baz".equals( GwtUtils.toString( new String[] { "foo", "bar", "baz" }, '#' ) ) );
	}

	public void testSimpleConverter()
		throws Exception
	{
		SimpleConverter converter = new SimpleConverter();

		assertTrue( 1 == (Byte) converter.convertFromWidget( null, "1", byte.class ));
		assertTrue( 2 == (Short) converter.convertFromWidget( null, "2", short.class ));
		assertTrue( 3 == (Integer) converter.convertFromWidget( null, "3", int.class ));
		assertTrue( 4l == (Long) converter.convertFromWidget( null, "4", long.class ));
		assertTrue( 5f == (Float) converter.convertFromWidget( null, "5", float.class ));
		assertTrue( 6d == (Double) converter.convertFromWidget( null, "6", double.class ));
		assertTrue( true == (Boolean) converter.convertFromWidget( null, "true", boolean.class ));
		assertTrue( 'a' == (Character) converter.convertFromWidget( null, "a", char.class ));

		try
		{
			converter.convertFromWidget( null, "Foo", Date.class );
			assertTrue( false );
		}
		catch( Exception e )
		{
			assertTrue( "Don't know how to convert a String to a java.util.Date".equals( e.getMessage() ));
		}
	}

	public void testGwtRemoteInspectorProxy()
	{
		try
		{
			new GwtRemoteInspectorProxy().inspect( null, null, (String[]) null );
			assertTrue( false );
		}
		catch( Exception e )
		{
			assertTrue( "Use async inspection instead".equals( e.getMessage() ));
		}
	}

	//
	// Private methods
	//

	/**
	 * Wrapped to avoid 'synthetic access' warning
	 */

	/*package private*/void finish()
	{
		super.finishTest();
	}

	/*package private*/void fireClickEvent( HasHandlers widget )
	{
		Document document = Document.get();
		NativeEvent nativeEvent = document.createClickEvent( 0, 0, 0, 0, 0, false, false, false, false );
		DomEvent.fireNativeEvent( nativeEvent, widget );
	}

	//
	// Native methods
	//

	native void executeAfterBuildWidgets( GwtMetawidget metawidget, Timer timer )
	/*-{
		metawidget.@org.metawidget.gwt.client.ui.GwtMetawidget::mExecuteAfterBuildWidgets = timer;
		metawidget.@org.metawidget.gwt.client.ui.GwtMetawidget::buildWidgets()();
	}-*/;
}