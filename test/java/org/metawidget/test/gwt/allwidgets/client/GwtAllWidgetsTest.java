package org.metawidget.test.gwt.allwidgets.client;

import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.test.gwt.allwidgets.client.ui.AllWidgetsModule;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.TextBox;

public class GwtAllWidgetsTest
	extends GWTTestCase
{
	//
	//
	// Private statics
	//
	//

	private final static int	TIMER_SCHEDULE_DELAY	= 1000;

	private final static int	TEST_FINISH_DELAY		= 50 * TIMER_SCHEDULE_DELAY;

	//
	//
	// Public methods
	//
	//

	@Override
	public String getModuleName()
	{
		return "org.metawidget.test.gwt.allwidgets.GwtAllWidgetsTest";
	}

	public void testAllWidgets()
		throws Exception
	{
		// Start app

		final FlowPanel panel = new FlowPanel();
		final AllWidgetsModule allWidgetsModule = new AllWidgetsModule( panel );
		allWidgetsModule.onModuleLoad();

		Timer timerLoad = new Timer()
		{
			@Override
			public void run()
			{
				// Test fields

				final GwtMetawidget metawidget = (GwtMetawidget) panel.getWidget( 0 );
				FlexTable flexTable = (FlexTable) metawidget.getWidget( 0 );

				// Check what created, and edit it

				assertTrue( "textbox:".equals( flexTable.getText( 0, 0 ) ));
				assertTrue( flexTable.getWidget( 0, 1 ) instanceof TextBox );
				assertTrue( "Textbox".equals( metawidget.getValue( "textbox" ) ) );
				((TextBox) flexTable.getWidget( 0, 1 )).setText( "Textbox1" );

				// Check saving

				Button saveButton = (Button) ( (Facet) flexTable.getWidget( flexTable.getRowCount() - 1 , 0 ) ).getWidget();
				assertTrue( "Save".equals( saveButton.getText() ) );
				fireClickListeners( saveButton );

				Timer timerSave = new Timer()
				{
					@Override
					public void run()
					{
						FlexTable readOnlyFlexTable = (FlexTable) metawidget.getWidget( 0 );

						assertTrue( "textbox:".equals( readOnlyFlexTable.getText( 0, 0 )));
						assertTrue( "Textbox1".equals( readOnlyFlexTable.getText( 0, 1 )));

						finish();
					}
				};

				timerSave.schedule( TIMER_SCHEDULE_DELAY );
			}
		};

		timerLoad.schedule( TIMER_SCHEDULE_DELAY );

		// Test runs asynchronously

		delayTestFinish( TEST_FINISH_DELAY );

	}

	//
	//
	// Protected methods
	//
	//

	/**
	 * Wrapped to avoid 'synthetic access' warning
	 */

	void finish()
	{
		super.finishTest();
	}

	//
	//
	// Native methods
	//
	//

	native void fireClickListeners( FocusWidget focusWidget )
	/*-{
		focusWidget.@com.google.gwt.user.client.ui.FocusWidget::fireClickListeners()();
	}-*/;
}
