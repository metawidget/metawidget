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
import org.metawidget.gwt.quirks.client.model.GwtQuirks;
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
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

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
		assertEquals( "org.metawidget.gwt.quirks.client.model.GwtQuirks", metawidget.getPath() );

		executeAfterBuildWidgets( metawidget, new Timer()
		{
			@Override
			public void run()
			{
				// Test fields

				final FlexTable flexTable = (FlexTable) metawidget.getWidget( 0 );

				// Check what created

				assertEquals( "Boolean:", flexTable.getText( 0, 0 ) );
				final CheckBox checkbox = (CheckBox) flexTable.getWidget( 0, 1 );
				assertTrue( false == checkbox.getValue() );
				assertTrue( false == (Boolean) metawidget.getValue( "boolean" ) );
				checkbox.setValue( true );
				assertEquals( "componentStyleName", checkbox.getStyleName() );

				assertEquals( "Foo:", flexTable.getText( 2, 0 ) );
				Stub stub = (Stub) flexTable.getWidget( 2, 1 );
				assertEquals( "foo", stub.getAttributes().get( "name" ) );

				// Click a nested button

				assertEquals( "Nested quirks:", flexTable.getText( 1, 0 ) );
				final GwtMetawidget nestedMetawidget = (GwtMetawidget) flexTable.getWidget( 1, 1 );

				assertTrue( 3 == flexTable.getRowCount() );

				executeAfterBuildWidgets( nestedMetawidget, new Timer()
				{
					@Override
					public void run()
					{
						final FlexTable nestedFlexTable = (FlexTable) nestedMetawidget.getWidget( 0 );
						assertEquals( "", nestedFlexTable.getText( 0, 0 ) );
						Button nestedActionButton = (Button) nestedFlexTable.getWidget( 0, 1 );
						assertEquals( "Nested action", nestedActionButton.getText() );

						try
						{
							fireClickEvent( nestedActionButton );
							assertTrue( false );
						}
						catch ( Exception e )
						{
							assertEquals( "nestedAction called", e.getMessage() );
						}

						// Save and refresh

						metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );

						metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).rebind( metawidget.getToInspect(), metawidget );

						// Test checkbox was still checked and has no text (ie. HasText didn't get
						// hit first in GwtMetawidget.setValue)

						assertEquals( "", checkbox.getText() );
						assertTrue( true == checkbox.getValue() );
						assertTrue( true == (Boolean) metawidget.getValue( "boolean" ) );

						// Test rebind binds to new object

						( (GwtQuirks) metawidget.getToInspect() ).setBoolean( false );

						GwtQuirks quirks2 = new GwtQuirks();
						metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).rebind( quirks2, metawidget );
						assertTrue( quirks2 == metawidget.getToInspect() );
						assertEquals( "", checkbox.getText() );
						assertTrue( false == checkbox.getValue() );
						checkbox.setValue( true );
						metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );

						assertTrue( true == (Boolean) metawidget.getValue( "boolean" ) );
						assertTrue( quirks2.isBoolean() );
						assertTrue( ( (GwtQuirks) metawidget.getToInspect() ).isBoolean() );

						// All done

						finish();
					}
				} );
			}
		} );

		// Test runs asynchronously

		delayTestFinish( TEST_FINISH_DELAY );
	}

	public void testTabQuirks()
		throws Exception
	{
		// Start app

		final FlowPanel panel = new FlowPanel();
		final QuirksModule quirksModule = new QuirksModule( panel );
		quirksModule.onModuleLoad();

		final GwtMetawidget metawidget = (GwtMetawidget) panel.getWidget( 1 );
		assertEquals( "org.metawidget.gwt.quirks.client.model.GwtTabQuirks", metawidget.getPath() );

		executeAfterBuildWidgets( metawidget, new Timer()
		{
			@Override
			public void run()
			{
				// Test fields

				final FlexTable flexTable = (FlexTable) metawidget.getWidget( 0 );

				// Check what created

				assertEquals( "Abc:", flexTable.getText( 0, 0 ) );
				assertTrue( flexTable.getWidget( 0, 1 ) instanceof TextBox );

				TabPanel outerTabPanel = ((TabPanel) flexTable.getWidget( 1, 0 ));
				assertEquals( "Foo", outerTabPanel.getTabBar().getTabHTML( 0 ));
				assertTrue( 1 == outerTabPanel.getTabBar().getTabCount() );

				FlexTable outerTable = (FlexTable) ((FlowPanel) outerTabPanel.getWidget( 0 )).getWidget( 0 );
				TabPanel innerTabPanel = ((TabPanel) outerTable.getWidget( 0, 0 ));
				assertTrue( 2 == innerTabPanel.getTabBar().getTabCount() );

				assertEquals( "Bar", innerTabPanel.getTabBar().getTabHTML( 0 ));
				FlexTable innerTable = (FlexTable) ((FlowPanel) innerTabPanel.getWidget( 0 )).getWidget( 0 );
				assertEquals( "Def:", innerTable.getText( 0, 0 ) );
				assertTrue( innerTable.getWidget( 0, 1 ) instanceof CheckBox );
				assertEquals( "Ghi:", innerTable.getText( 1, 0 ) );
				assertTrue( innerTable.getWidget( 1, 1 ) instanceof TextArea );
				assertTrue( 2 == innerTable.getRowCount() );

				assertEquals( "Baz", innerTabPanel.getTabBar().getTabHTML( 1 ));
				innerTable = (FlexTable) ((FlowPanel) innerTabPanel.getWidget( 1 )).getWidget( 0 );
				assertEquals( "Jkl:", innerTable.getText( 0, 0 ) );
				assertTrue( innerTable.getWidget( 0, 1 ) instanceof TextBox );
				assertTrue( 1 == innerTable.getRowCount() );

				assertEquals( "Mno:", outerTable.getText( 1, 0 ) );
				assertTrue( outerTable.getWidget( 1, 1 ) instanceof CheckBox );

				innerTabPanel = ((TabPanel) outerTable.getWidget( 2, 0 ));
				assertTrue( 1 == innerTabPanel.getTabBar().getTabCount() );

				assertEquals( "Moo", innerTabPanel.getTabBar().getTabHTML( 0 ));
				innerTable = (FlexTable) ((FlowPanel) innerTabPanel.getWidget( 0 )).getWidget( 0 );
				assertEquals( "Pqr:", innerTable.getText( 0, 0 ) );
				assertTrue( innerTable.getWidget( 0, 1 ) instanceof TextBox );
				assertTrue( 1 == innerTable.getRowCount() );

				assertEquals( "Stu:", flexTable.getText( 2, 0 ) );
				assertTrue( flexTable.getWidget( 2, 1 ) instanceof TextBox );
				assertTrue( 3 == flexTable.getRowCount() );

				// All done

				finish();
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
		assertFalse( GwtUtils.isPrimitive( Byte.class.getName() ) );
		assertFalse( GwtUtils.isPrimitive( Short.class.getName() ) );
		assertFalse( GwtUtils.isPrimitive( Integer.class.getName() ) );
		assertFalse( GwtUtils.isPrimitive( Long.class.getName() ) );
		assertFalse( GwtUtils.isPrimitive( Float.class.getName() ) );
		assertFalse( GwtUtils.isPrimitive( Double.class.getName() ) );
		assertFalse( GwtUtils.isPrimitive( Boolean.class.getName() ) );
		assertFalse( GwtUtils.isPrimitive( Character.class.getName() ) );

		// isPrimitiveWrapper

		assertTrue( GwtUtils.isPrimitiveWrapper( Byte.class.getName() ) );
		assertTrue( GwtUtils.isPrimitiveWrapper( Short.class.getName() ) );
		assertTrue( GwtUtils.isPrimitiveWrapper( Integer.class.getName() ) );
		assertTrue( GwtUtils.isPrimitiveWrapper( Long.class.getName() ) );
		assertTrue( GwtUtils.isPrimitiveWrapper( Float.class.getName() ) );
		assertTrue( GwtUtils.isPrimitiveWrapper( Double.class.getName() ) );
		assertTrue( GwtUtils.isPrimitiveWrapper( Boolean.class.getName() ) );
		assertTrue( GwtUtils.isPrimitiveWrapper( Character.class.getName() ) );
		assertFalse( GwtUtils.isPrimitiveWrapper( byte.class.getName() ) );
		assertFalse( GwtUtils.isPrimitiveWrapper( short.class.getName() ) );
		assertFalse( GwtUtils.isPrimitiveWrapper( int.class.getName() ) );
		assertFalse( GwtUtils.isPrimitiveWrapper( long.class.getName() ) );
		assertFalse( GwtUtils.isPrimitiveWrapper( float.class.getName() ) );
		assertFalse( GwtUtils.isPrimitiveWrapper( double.class.getName() ) );
		assertFalse( GwtUtils.isPrimitiveWrapper( boolean.class.getName() ) );
		assertFalse( GwtUtils.isPrimitiveWrapper( char.class.getName() ) );

		// toString

		assertEquals( "", GwtUtils.toString( (String[]) null, ',' ) );
		assertEquals( "foo#bar#baz", GwtUtils.toString( new String[] { "foo", "bar", "baz" }, '#' ) );
	}

	public void testSimpleConverter()
		throws Exception
	{
		SimpleConverter converter = new SimpleConverter();

		assertTrue( 1 == (Byte) converter.convertFromWidget( null, "1", byte.class ) );
		assertTrue( 2 == (Short) converter.convertFromWidget( null, "2", short.class ) );
		assertTrue( 3 == (Integer) converter.convertFromWidget( null, "3", int.class ) );
		assertTrue( 4l == (Long) converter.convertFromWidget( null, "4", long.class ) );
		assertTrue( 5f == (Float) converter.convertFromWidget( null, "5", float.class ) );
		assertTrue( 6d == (Double) converter.convertFromWidget( null, "6", double.class ) );
		assertTrue( true == (Boolean) converter.convertFromWidget( null, "true", boolean.class ) );
		assertTrue( 'a' == (Character) converter.convertFromWidget( null, "a", char.class ) );

		try
		{
			converter.convertFromWidget( null, "Foo", Date.class );
			assertTrue( false );
		}
		catch ( Exception e )
		{
			assertEquals( "Don't know how to convert a String to a java.util.Date", e.getMessage() );
		}
	}

	public void testGwtRemoteInspectorProxy()
	{
		try
		{
			new GwtRemoteInspectorProxy().inspect( null, null, (String[]) null );
			assertTrue( false );
		}
		catch ( Exception e )
		{
			assertEquals( "Use async inspection instead", e.getMessage() );
		}
	}

	//
	// Private methods
	//

	/**
	 * Wrapped to avoid 'synthetic access' warning
	 */

	/* package private */void finish()
	{
		super.finishTest();
	}

	/* package private */void fireClickEvent( HasHandlers widget )
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