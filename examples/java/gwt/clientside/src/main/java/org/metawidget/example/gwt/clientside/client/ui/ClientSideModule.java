// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

package org.metawidget.example.gwt.clientside.client.ui;

import java.util.Map;
import java.util.TreeMap;

import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.layout.FlexTableLayout;
import org.metawidget.gwt.client.ui.layout.FlexTableLayoutConfig;
import org.metawidget.gwt.client.ui.layout.LabelLayoutDecorator;
import org.metawidget.gwt.client.ui.layout.LabelLayoutDecoratorConfig;
import org.metawidget.gwt.client.widgetbuilder.GwtWidgetBuilder;
import org.metawidget.gwt.client.widgetbuilder.extgwt.ExtGwtWidgetBuilder;
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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ClientSideModule
	implements EntryPoint {

	//
	// Package-level statics
	//

	static final String	SAMPLE1;

	static final String	SAMPLE2;

	static final String	SAMPLE3;

	static {
		String sample1 = "<metawidget-metadata>\r\n\r\n\t<!-- Album Sample -->\r\n\r\n\t<entity type=\"sample\">";
		sample1 += "\r\n\t\t<property name=\"artist\" required=\"true\"/>";
		sample1 += "\r\n\t\t<property name=\"album\" required=\"true\"/>";
		sample1 += "\r\n\t\t<action name=\"addTracks\"/>";
		sample1 += "\r\n\t\t<property name=\"genre\" lookup=\"Art rock, Disco, Experimental rock, Glam rock, Protopunk, Punk, Rock\"/>";
		sample1 += "\r\n\t\t<property name=\"releaseDate\" type=\"java.util.Date\"/>";
		sample1 += "\r\n\t\t<property name=\"rating\" type=\"int\" minimum-value=\"1\" maximum-value=\"10\"/>";
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

	public ClientSideModule() {

		this( RootPanel.get() );
	}

	public ClientSideModule( Panel panel ) {

		mPanel = panel;
	}

	//
	// Public methods
	//

	public void onModuleLoad() {

		// TextArea

		final TextArea textarea = new TextArea();
		textarea.setStylePrimaryName( "input" );
		textarea.setText( SAMPLE1 );

		// Samples

		FlowPanel sampleButtons = new FlowPanel();
		sampleButtons.setStylePrimaryName( "samples" );
		Button sampleButton1 = new Button( "Sample #1" );
		sampleButton1.addClickHandler( new ClickHandler() {

			public void onClick( ClickEvent event ) {

				textarea.setText( SAMPLE1 );
			}
		} );
		sampleButtons.add( sampleButton1 );
		Button sampleButton2 = new Button( "Sample #2" );
		sampleButton2.addClickHandler( new ClickHandler() {

			public void onClick( ClickEvent event ) {

				textarea.setText( SAMPLE2 );
			}
		} );
		sampleButtons.add( sampleButton2 );
		Button sampleButton3 = new Button( "Sample #3" );
		sampleButton3.addClickHandler( new ClickHandler() {

			public void onClick( ClickEvent event ) {

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
		metawidget.addWidgetProcessor( new MapBindingProcessor() );
		metawidget.addWidgetProcessor( new AlertActionProcessor() );
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
		saveButton.addClickHandler( new ClickHandler() {

			public void onClick( ClickEvent event ) {

				metawidget.getWidgetProcessor( MapBindingProcessor.class ).save( metawidget );

				// (do not Window.alert during unit tests)

				if ( mPanel instanceof RootPanel ) {
					Window.alert( "MapPropertyBinding retrieved the following values:\n\n" + model.toString() );
				}
			}
		} );

		buttonsFacet.add( saveButton );
		metawidget.add( buttonsFacet );

		FlexTableLayoutConfig layoutConfig = new FlexTableLayoutConfig();
		layoutConfig.setTableStyleName( "table-form" );
		layoutConfig.setColumnStyleNames( "table-label-column", "table-component-column", "table-required-column" );
		layoutConfig.setFooterStyleName( "buttons" );
		metawidget.setLayout( new LabelLayoutDecorator( new LabelLayoutDecoratorConfig().setLayout( new FlexTableLayout( layoutConfig ) ).setStyleName( "section-heading" ) ) );

		// Load button

		Button generateButton = new Button( "Generate" );
		generateButton.addClickHandler( new ClickHandler() {

			public void onClick( ClickEvent event ) {

				model.clear();
				metawidget.setToInspect( model );
			}
		} );

		// Add to either RootPanel or the given Panel (for unit tests)

		if ( mPanel instanceof RootPanel ) {
			RootPanel.get( "textarea-column" ).add( sampleButtons );
			RootPanel.get( "textarea-column" ).add( textarea );
			RootPanel.get( "generate-column" ).add( generateButton );
			RootPanel.get( "metawidget" ).add( metawidget );
		} else {
			mPanel.add( sampleButtons );
			mPanel.add( textarea );
			mPanel.add( generateButton );
			mPanel.add( metawidget );
		}
	}

	public Panel getPanel() {

		return mPanel;
	}
}
