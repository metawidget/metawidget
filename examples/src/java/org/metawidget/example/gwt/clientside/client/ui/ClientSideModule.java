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

package org.metawidget.example.gwt.clientside.client.ui;

import java.util.HashMap;
import java.util.Map;

import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * @author Richard Kennard
 */

public class ClientSideModule
	implements EntryPoint
{
	//
	// Private statics
	//

	private final static String SAMPLE1;

	private final static String SAMPLE2 = "<metawidget-metadata>\r\n\t<entity type=\"foo\">\r\n\t\t<property name=\"bar\"/>\r\n\t</entity>\r\n</metawidget-metadata>";

	private final static String SAMPLE3 = "<metawidget-metadata>\r\n\t<entity type=\"foo\">\r\n\t\t<property name=\"bar\"/>\r\n\t</entity>\r\n</metawidget-metadata>";

	static
	{
		String sample1 = "<metawidget-metadata>\r\n\t<entity type=\"sample\">";
		sample1 += "\r\n\t\t<property name=\"albumTitle\"/>";
		sample1 += "\r\n\t\t<property name=\"artist\"/>";
		sample1 += "\r\n\t\t<property name=\"releaseDate\"/>";
		sample1 += "\r\n\t\t<property name=\"genre\" lookup=\"Art rock, Experimental rock, Glam rock, Protopunk, Punk, Rock\"/>";
		sample1 += "\r\n\t</entity>\r\n</metawidget-metadata>";

		SAMPLE1 = sample1;
	}

	//
	// Package-level members
	//

	Panel	mPanel;

	//
	// Constructor
	//

	public ClientSideModule()
	{
		this( RootPanel.get() );
	}

	public ClientSideModule( Panel panel )
	{
		mPanel = panel;
	}

	//
	// Public methods
	//

	public void onModuleLoad()
	{
		// TextArea

		final TextArea textarea = new TextArea();
		textarea.setStylePrimaryName( "input" );
		textarea.setText( SAMPLE1 );

		// Samples

		FlowPanel sampleButtons = new FlowPanel();
		sampleButtons.setStylePrimaryName( "samples" );
		Button sampleButton1 = new Button( "Sample #1" );
		sampleButton1.addClickHandler( new ClickHandler()
		{
			@Override
			public void onClick( ClickEvent event )
			{
				textarea.setText( SAMPLE1 );
			}
		} );
		sampleButtons.add( sampleButton1 );
		Button sampleButton2 = new Button( "Sample #2" );
		sampleButton2.addClickHandler( new ClickHandler()
		{
			@Override
			public void onClick( ClickEvent event )
			{
				textarea.setText( SAMPLE2 );
			}
		} );
		sampleButtons.add( sampleButton2 );
		Button sampleButton3 = new Button( "Sample #3" );
		sampleButton3.addClickHandler( new ClickHandler()
		{
			@Override
			public void onClick( ClickEvent event )
			{
				textarea.setText( SAMPLE3 );
			}
		} );
		sampleButtons.add( sampleButton3 );

		// Metawidget

		final GwtMetawidget metawidget = new GwtMetawidget();
		metawidget.setInspector( new TextAreaInspector( textarea ) );
		metawidget.setPropertyBindingClass( MapPropertyBinding.class );
		metawidget.setActionBindingClass( AlertActionBinding.class );
		metawidget.setPath( "foo" );

		// Model

		final Map<String, Object> model = new HashMap<String, Object>();
		metawidget.setToInspect( model );

		// Save button (as a facet)

		Facet buttonsFacet = new Facet();
		buttonsFacet.setName( "buttons" );

		Button saveButton = new Button( "Save" );
		saveButton.addClickHandler( new ClickHandler()
		{
			@Override
			public void onClick( ClickEvent event )
			{
				metawidget.save();
				Window.alert( "MapPropertyBinding retrieved the following values:\n\n" + model.toString() );
			}
		} );

		buttonsFacet.add( saveButton );
		metawidget.add( buttonsFacet );
		metawidget.setParameter( "tableStyleName", "table-form" );
		metawidget.setParameter( "columnStyleNames", "table-label-column,table-component-column" );
		metawidget.setParameter( "footerStyleName", "buttons" );

		// Load button

		Button generateButton = new Button( "Generate >" );
		generateButton.addClickHandler( new ClickHandler()
		{
			@Override
			public void onClick( ClickEvent event )
			{
				model.clear();
				metawidget.setToInspect( model );
			}
		} );

		// Add to either RootPanel or the given Panel (for unit tests)

		if ( mPanel instanceof RootPanel )
		{
			RootPanel.get( "textarea-column" ).add( sampleButtons );
			RootPanel.get( "textarea-column" ).add( textarea );
			RootPanel.get( "generate-column" ).add( generateButton );
			RootPanel.get( "metawidget-column" ).add( metawidget );
		}
		else
		{
			mPanel.add( textarea );
			mPanel.add( metawidget );
		}
	}

	public Panel getPanel()
	{
		return mPanel;
	}
}
