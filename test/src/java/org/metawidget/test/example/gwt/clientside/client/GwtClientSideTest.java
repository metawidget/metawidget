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

package org.metawidget.test.example.gwt.clientside.client;

import org.metawidget.example.gwt.clientside.client.ui.ClientSideModule;
import org.metawidget.gwt.client.ui.GwtMetawidget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Richard Kennard
 */

public class GwtClientSideTest
	extends GWTTestCase
{
	//
	// Public methods
	//

	@Override
	public String getModuleName()
	{
		return "org.metawidget.test.example.gwt.clientside.GwtClientSideTest";
	}

	public void testClientSide()
		throws Exception
	{
		FlowPanel panel = new FlowPanel();
		final ClientSideModule clientSideModule = new ClientSideModule( panel );
		clientSideModule.onModuleLoad();

		// Check searching

		final Button sampleButton1 = (Button) ( (FlowPanel) panel.getWidget( 0 ) ).getWidget( 0 );
		final Button sampleButton2 = (Button) ( (FlowPanel) panel.getWidget( 0 ) ).getWidget( 1 );
		final Button sampleButton3 = (Button) ( (FlowPanel) panel.getWidget( 0 ) ).getWidget( 2 );
		final Button generateButton = (Button) panel.getWidget( 2 );
		final GwtMetawidget metawidget = (GwtMetawidget) panel.getWidget( 3 );

		executeAfterBuildWidgets( metawidget, new Timer()
		{
			@Override
			public void run()
			{
				FlexTable flexTable1 = (FlexTable) metawidget.getWidget( 0 );
				assertTrue( "Artist:".equals( flexTable1.getText( 0, 0 ) ) );
				assertTrue( flexTable1.getWidget( 0, 1 ) instanceof TextBox );
				assertTrue( "Album:".equals( flexTable1.getText( 1, 0 ) ) );
				assertTrue( flexTable1.getWidget( 1, 1 ) instanceof TextBox );
				assertTrue( flexTable1.getWidget( 2, 1 ) instanceof Button );
				assertTrue( "Genre:".equals( flexTable1.getText( 3, 0 ) ) );
				assertTrue( "Release date:".equals( flexTable1.getText( 4, 0 ) ) );
				assertTrue( "Notes:".equals( flexTable1.getText( 4, 0 ) ) );
				assertTrue( flexTable1.getWidget( 4, 1 ) instanceof TextArea );

				fireClickEvent( sampleButton2 );
				fireClickEvent( generateButton );

				executeAfterBuildWidgets( metawidget, new Timer()
				{
					@Override
					public void run()
					{
						executeAfterBuildWidgets( (GwtMetawidget) metawidget.getWidget( "homeAddress" ), new Timer()
						{
							@Override
							public void run()
							{
								executeAfterBuildWidgets( (GwtMetawidget) metawidget.getWidget( "workAddress" ), new Timer()
								{
									@Override
									public void run()
									{
										FlexTable flexTable2 = (FlexTable) metawidget.getWidget( 0 );
										assertTrue( "Title:".equals( flexTable2.getText( 0, 0 ) ) );

										fireClickEvent( sampleButton3 );
										fireClickEvent( generateButton );

										executeAfterBuildWidgets( metawidget, new Timer()
										{
											@Override
											public void run()
											{
												FlexTable flexTable3 = (FlexTable) metawidget.getWidget( 0 );
												assertTrue( "Pet name:".equals( flexTable3.getText( 0, 0 ) ) );

												fireClickEvent( sampleButton1 );
												fireClickEvent( generateButton );

												executeAfterBuildWidgets( metawidget, new Timer()
												{
													@Override
													public void run()
													{
														FlexTable flexTable4 = (FlexTable) metawidget.getWidget( 0 );
														assertTrue( "Artist:".equals( flexTable4.getText( 0, 0 ) ) );
													}
												} );
											}
										} );
									}
								} );
							}
						} );
					}
				} );
			}
		} );
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

	void fireClickEvent( HasHandlers widget )
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
