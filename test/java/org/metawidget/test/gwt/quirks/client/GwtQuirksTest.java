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

package org.metawidget.test.gwt.quirks.client;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.test.gwt.quirks.client.ui.QuirksModule;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
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
		return "org.metawidget.test.gwt.quirks.GwtQuirksTest";
	}

	public void testAllWidgets()
		throws Exception
	{
		// Start app

		final FlowPanel panel = new FlowPanel();
		final QuirksModule quirksModule = new QuirksModule( panel );
		quirksModule.onModuleLoad();

		final GwtMetawidget metawidget = (GwtMetawidget) panel.getWidget( 0 );

		executeAfterBuildWidgets( metawidget, new Timer()
		{
			@Override
			public void run()
			{
				// Test fields

				final FlexTable flexTable = (FlexTable) metawidget.getWidget( 0 );

				// Check what created, and edit it

				assertTrue( "Boolean:".equals( flexTable.getText( 0, 0 ) ) );
				assertTrue( flexTable.getWidget( 0, 1 ) instanceof CheckBox );
				assertTrue( false == (Boolean) metawidget.getValue( "boolean" ) );
				( (CheckBox) flexTable.getWidget( 0, 1 ) ).setChecked( true );

				// Save and refresh

				metawidget.save();
				metawidget.rebind( metawidget.getToInspect() );

				// Test checkbox was still checked (eg. HasText didn't get hit
				// first in GwtMetawidget.setValue)

				assertTrue( true == (Boolean) metawidget.getValue( "boolean" ) );

				// All done

				finish();
			}
		} );

		// Test runs asynchronously

		delayTestFinish( TEST_FINISH_DELAY );
	}

	//
	// Protected methods
	//

	/**
	 * Wrapped to avoid 'synthetic access' warning
	 */

	void finish()
	{
		super.finishTest();
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