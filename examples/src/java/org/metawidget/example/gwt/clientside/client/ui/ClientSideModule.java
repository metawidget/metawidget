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

import java.util.Map;
import java.util.TreeMap;

import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.widgetbuilder.extgwt.ExtGwtWidgetBuilder;
import org.metawidget.gwt.client.widgetbuilder.impl.GwtWidgetBuilder;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilderConfig;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Richard Kennard
 */

public class ClientSideModule
	implements EntryPoint
{
	//
	// Package-level statics
	//

	final static String	SAMPLE1;

	final static String	SAMPLE2;

	final static String	SAMPLE3;

	static
	{
		String sample1 = "<metawidget-metadata>\r\n\r\n\t<!-- Album Sample -->\r\n\r\n\t<entity type=\"sample\">";
		sample1 += "\r\n\t\t<property name=\"artist\" required=\"true\"/>";
		sample1 += "\r\n\t\t<property name=\"album\" required=\"true\"/>";
		sample1 += "\r\n\t\t<action name=\"addTracks\"/>";
		sample1 += "\r\n\t\t<property name=\"genre\" lookup=\"Art rock, Disco, Experimental rock, Glam rock, Protopunk, Punk, Rock\"/>";
		sample1 += "\r\n\t\t<property name=\"releaseDate\" type=\"java.util.Date\"/>";
		sample1 += "\r\n\t\t<property name=\"notes\" large=\"true\"/>";
		sample1 += "\r\n\t</entity>\r\n\r\n</metawidget-metadata>";

		SAMPLE1 = sample1;

		String sample2 = "<metawidget-metadata>\r\n\r\n\t<!-- Person Sample -->\r\n\r\n\t<entity type=\"sample\">";
		sample2 += "\r\n\t\t<property name=\"title\" lookup=\"Mr, Mrs, Miss\" required=\"true\"/>";
		sample2 += "\r\n\t\t<property name=\"firstname\" required=\"true\"/>";
		sample2 += "\r\n\t\t<property name=\"surname\" required=\"true\"/>";
		sample2 += "\r\n\t\t<property name=\"password\" masked=\"true\"/>";
		sample2 += "\r\n\t\t<property name=\"homeAddress\" type=\"address\"/>";
		sample2 += "\r\n\t\t<property name=\"workAddress\" type=\"address\"/>";
		sample2 += "\r\n\t</entity>\r\n\r\n\t<entity type=\"address\">";
		sample2 += "\r\n\t\t<property name=\"street\"/>";
		sample2 += "\r\n\t\t<property name=\"city\"/>";
		sample2 += "\r\n\t\t<property name=\"state\"/>";
		sample2 += "\r\n\t\t<property name=\"postcode\"/>";
		sample2 += "\r\n\t</entity>\r\n\r\n</metawidget-metadata>";

		SAMPLE2 = sample2;

		String sample3 = "<metawidget-metadata>\r\n\r\n\t<!-- Pet Sample -->\r\n\r\n\t<entity type=\"sample\">";
		sample3 += "\r\n\t\t<property name=\"petName\" required=\"true\"/>";
		sample3 += "\r\n\t\t<property name=\"gender\" lookup=\"Male, Female\"/>";
		sample3 += "\r\n\t\t<property name=\"species\" label=\"Species (eg. dog)\"/>";
		sample3 += "\r\n\t\t<property name=\"deceased\" type=\"boolean\"/>";
		sample3 += "\r\n\t</entity>\r\n\r\n</metawidget-metadata>";

		SAMPLE3 = sample3;
	}

	//
	// Package-level members
	//

	Panel				mPanel;

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
		@SuppressWarnings( "unchecked" )
		CompositeWidgetBuilderConfig<Widget, GwtMetawidget> config = new CompositeWidgetBuilderConfig<Widget, GwtMetawidget>().setWidgetBuilders( new ExtGwtWidgetBuilder(), new GwtWidgetBuilder() );
		metawidget.setWidgetBuilder( new CompositeWidgetBuilder<Widget, GwtMetawidget>( config ) );
		metawidget.setPropertyBindingClass( MapPropertyBinding.class );
		metawidget.setActionBindingClass( AlertActionBinding.class );
		metawidget.setPath( "sample" );

		// Model
		//
		// (we use a TreeMap for consistent ordering of the .toString() for unit tests)

		final Map<String, Object> model = new TreeMap<String, Object>();
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

				// (do not Window.alert during unit tests)

				if ( mPanel instanceof RootPanel )
					Window.alert( "MapPropertyBinding retrieved the following values:\n\n" + model.toString() );
			}
		} );

		buttonsFacet.add( saveButton );
		metawidget.add( buttonsFacet );
		metawidget.setParameter( "tableStyleName", "table-form" );
		metawidget.setParameter( "columnStyleNames", "table-label-column,table-component-column,table-required-column" );
		metawidget.setParameter( "footerStyleName", "buttons" );

		// Load button

		Button generateButton = new Button( "Generate" );
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
			mPanel.add( sampleButtons );
			mPanel.add( textarea );
			mPanel.add( generateButton );
			mPanel.add( metawidget );
		}
	}

	public Panel getPanel()
	{
		return mPanel;
	}
}
