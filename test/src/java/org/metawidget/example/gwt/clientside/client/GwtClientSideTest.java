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

package org.metawidget.example.gwt.clientside.client;

import java.util.Date;
import java.util.Map;

import org.metawidget.example.gwt.clientside.client.ui.ClientSideModule;
import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;

import com.extjs.gxt.ui.client.widget.form.DateField;
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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
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
		return "org.metawidget.example.gwt.clientside.GwtClientSideTest";
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
		final TextArea textArea = (TextArea) panel.getWidget( 1 );
		final Button generateButton = (Button) panel.getWidget( 2 );
		final GwtMetawidget metawidget = (GwtMetawidget) panel.getWidget( 3 );

		executeAfterBuildWidgets( metawidget, new Timer()
		{
			@SuppressWarnings( { "deprecation", "unchecked" } )
			@Override
			public void run()
			{
				// Save before populating

				FlexTable flexTable1 = (FlexTable) metawidget.getWidget( 0 );
				final Button saveButton = (Button) ( (Facet) flexTable1.getWidget( 6, 0 ) ).getWidget();
				fireClickEvent( saveButton );
				assertTrue( "{album=, artist=, genre=, notes=, releaseDate=null}".equals( metawidget.getToInspect().toString() ) );

				// Populate

				assertTrue( "Artist:".equals( flexTable1.getText( 0, 0 ) ) );
				( (TextBox) flexTable1.getWidget( 0, 1 ) ).setText( "Foo" );
				assertTrue( "*".equals( flexTable1.getText( 0, 2 ) ) );
				assertTrue( "Album:".equals( flexTable1.getText( 1, 0 ) ) );
				( (TextBox) flexTable1.getWidget( 1, 1 ) ).setText( "Bar" );
				assertTrue( flexTable1.getWidget( 2, 1 ) instanceof Button );
				fireClickEvent( flexTable1.getWidget( 2, 1 ) );

				assertTrue( "Genre:".equals( flexTable1.getText( 3, 0 ) ) );
				assertTrue( flexTable1.getWidget( 3, 1 ) instanceof ListBox );
				assertTrue( 8 == ( (ListBox) flexTable1.getWidget( 3, 1 ) ).getItemCount() );
				( (ListBox) flexTable1.getWidget( 3, 1 ) ).setSelectedIndex( 1 );
				assertTrue( "Release date:".equals( flexTable1.getText( 4, 0 ) ) );
				Date releaseDate = new Date( 101, 0, 1 );
				( (DateField) flexTable1.getWidget( 4, 1 ) ).setValue( releaseDate );
				assertTrue( "Notes:".equals( flexTable1.getText( 5, 0 ) ) );
				( (TextArea) flexTable1.getWidget( 5, 1 ) ).setText( "Baz" );
				assertTrue( flexTable1.getWidget( 6, 0 ) instanceof Facet );

				// Save after populating

				fireClickEvent( saveButton );
				assertTrue( metawidget.getToInspect().toString().startsWith( "{addTracks=clicked, album=Bar, artist=Foo, genre=Art rock, notes=Baz, releaseDate=" ));
				assertTrue( releaseDate.equals( ((Map<String, Object>) metawidget.getToInspect()).get( "releaseDate" )));

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
										// Default generation

										FlexTable flexTable2 = (FlexTable) metawidget.getWidget( 0 );
										assertTrue( "Title:".equals( flexTable2.getText( 0, 0 ) ) );
										assertTrue( 3 == ( (ListBox) flexTable2.getWidget( 0, 1 ) ).getItemCount() );
										( (ListBox) flexTable2.getWidget( 0, 1 ) ).setSelectedIndex( 1 );
										assertTrue( "*".equals( flexTable2.getText( 0, 2 ) ) );
										assertTrue( "Firstname:".equals( flexTable2.getText( 1, 0 ) ) );
										( (TextBox) flexTable2.getWidget( 1, 1 ) ).setText( "Richard" );
										assertTrue( "*".equals( flexTable2.getText( 1, 2 ) ) );
										assertTrue( "Surname:".equals( flexTable2.getText( 2, 0 ) ) );
										( (TextBox) flexTable2.getWidget( 2, 1 ) ).setText( "Kennard" );
										assertTrue( "*".equals( flexTable2.getText( 2, 2 ) ) );
										assertTrue( "Password:".equals( flexTable2.getText( 3, 0 ) ) );
										( (PasswordTextBox) flexTable2.getWidget( 3, 1 ) ).setText( "sssh!" );
										assertTrue( "Home address:".equals( flexTable2.getText( 4, 0 ) ) );
										assertTrue( flexTable2.getWidget( 4, 1 ) instanceof GwtMetawidget );

										FlexTable nestedFlexTable1 = (FlexTable) ( (GwtMetawidget) flexTable2.getWidget( 4, 1 ) ).getWidget( 0 );
										assertTrue( "Street:".equals( nestedFlexTable1.getText( 0, 0 ) ) );
										( (TextBox) nestedFlexTable1.getWidget( 0, 1 ) ).setText( "Home Street" );
										assertTrue( "City:".equals( nestedFlexTable1.getText( 1, 0 ) ) );
										( (TextBox) nestedFlexTable1.getWidget( 1, 1 ) ).setText( "Home City" );
										assertTrue( "State:".equals( nestedFlexTable1.getText( 2, 0 ) ) );
										( (TextBox) nestedFlexTable1.getWidget( 2, 1 ) ).setText( "Home State" );
										assertTrue( "Postcode:".equals( nestedFlexTable1.getText( 3, 0 ) ) );
										( (TextBox) nestedFlexTable1.getWidget( 3, 1 ) ).setText( "Home Postcode" );

										assertTrue( "Work address:".equals( flexTable2.getText( 5, 0 ) ) );

										FlexTable nestedFlexTable2 = (FlexTable) ( (GwtMetawidget) flexTable2.getWidget( 5, 1 ) ).getWidget( 0 );
										assertTrue( "Street:".equals( nestedFlexTable2.getText( 0, 0 ) ) );
										( (TextBox) nestedFlexTable2.getWidget( 0, 1 ) ).setText( "Work Street" );
										assertTrue( "City:".equals( nestedFlexTable2.getText( 1, 0 ) ) );
										( (TextBox) nestedFlexTable2.getWidget( 1, 1 ) ).setText( "Work City" );
										assertTrue( "State:".equals( nestedFlexTable2.getText( 2, 0 ) ) );
										( (TextBox) nestedFlexTable2.getWidget( 2, 1 ) ).setText( "Work State" );
										assertTrue( "Postcode:".equals( nestedFlexTable2.getText( 3, 0 ) ) );
										( (TextBox) nestedFlexTable2.getWidget( 3, 1 ) ).setText( "Work Postcode" );

										fireClickEvent( saveButton );
										assertTrue( "{firstname=Richard, homeAddress.city=Home City, homeAddress.postcode=Home Postcode, homeAddress.state=Home State, homeAddress.street=Home Street, password=sssh!, surname=Kennard, title=Mrs, workAddress.city=Work City, workAddress.postcode=Work Postcode, workAddress.state=Work State, workAddress.street=Work Street}".equals( metawidget.getToInspect().toString() ) );

										// Edit XML

										textArea.setText( textArea.getText().replace( "<property name=\"postcode\"/>", "<property name=\"postcode\"/><action name=\"lookupPostcode\"/>" ) );
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
																FlexTable flexTableRegenerated = (FlexTable) metawidget.getWidget( 0 );
																FlexTable nestedFlexTableRegenerated1 = (FlexTable) ( (GwtMetawidget) flexTableRegenerated.getWidget( 4, 1 ) ).getWidget( 0 );
																assertTrue( nestedFlexTableRegenerated1.getWidget( 4, 1 ) instanceof Button );
																fireClickEvent( nestedFlexTableRegenerated1.getWidget( 4, 1 ) );

																FlexTable nestedFlexTableRegenerated2 = (FlexTable) ( (GwtMetawidget) flexTableRegenerated.getWidget( 5, 1 ) ).getWidget( 0 );
																assertTrue( nestedFlexTableRegenerated2.getWidget( 4, 1 ) instanceof Button );
																fireClickEvent( nestedFlexTableRegenerated2.getWidget( 4, 1 ) );

																fireClickEvent( saveButton );
																assertTrue( "{firstname=, homeAddress.city=, homeAddress.lookupPostcode=clicked, homeAddress.postcode=, homeAddress.state=, homeAddress.street=, password=, surname=, title=Mr, workAddress.city=, workAddress.lookupPostcode=clicked, workAddress.postcode=, workAddress.state=, workAddress.street=}".equals( metawidget.getToInspect().toString() ) );

																// Regenerate

																fireClickEvent( sampleButton3 );
																fireClickEvent( generateButton );

																executeAfterBuildWidgets( metawidget, new Timer()
																{
																	@Override
																	public void run()
																	{
																		FlexTable flexTable3 = (FlexTable) metawidget.getWidget( 0 );
																		assertTrue( "Pet name:".equals( flexTable3.getText( 0, 0 ) ) );
																		( (TextBox) flexTable3.getWidget( 0, 1 ) ).setText( "Millie" );
																		assertTrue( "*".equals( flexTable3.getText( 0, 2 ) ) );
																		assertTrue( "Gender:".equals( flexTable3.getText( 1, 0 ) ) );
																		assertTrue( 3 == ( (ListBox) flexTable3.getWidget( 1, 1 ) ).getItemCount() );
																		( (ListBox) flexTable3.getWidget( 1, 1 ) ).setSelectedIndex( 1 );
																		assertTrue( "".equals( flexTable3.getText( 1, 2 ) ) );
																		assertTrue( "Species (eg. dog):".equals( flexTable3.getText( 2, 0 ) ) );
																		( (TextBox) flexTable3.getWidget( 2, 1 ) ).setText( "Dog" );
																		assertTrue( "Deceased:".equals( flexTable3.getText( 3, 0 ) ) );
																		( (CheckBox) flexTable3.getWidget( 3, 1 ) ).setValue( true );

																		fireClickEvent( saveButton );
																		assertTrue( "{deceased=true, gender=Male, petName=Millie, species=Dog}".equals( metawidget.getToInspect().toString() ) );

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
