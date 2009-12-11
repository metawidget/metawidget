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

package org.metawidget.gwt.allwidgets.client.ui;

import java.util.Date;

import org.metawidget.gwt.allwidgets.client.converter.DateConverter;
import org.metawidget.gwt.allwidgets.client.converter.NestedWidgetsConverter;
import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.gwt.client.ui.layout.FlexTableLayout;
import org.metawidget.gwt.client.ui.layout.FlexTableLayoutConfig;
import org.metawidget.gwt.client.ui.layout.LabelLayoutDecorator;
import org.metawidget.gwt.client.ui.layout.LabelLayoutDecoratorConfig;
import org.metawidget.gwt.client.widgetprocessor.binding.simple.SimpleBindingProcessor;
import org.metawidget.gwt.client.widgetprocessor.binding.simple.SimpleBindingProcessorAdapter;
import org.metawidget.gwt.client.widgetprocessor.binding.simple.SimpleBindingProcessorConfig;
import org.metawidget.inspector.gwt.remote.client.GwtRemoteInspectorProxy;
import org.metawidget.shared.allwidgets.model.AllWidgets;
import org.metawidget.shared.allwidgets.model.AllWidgets.NestedWidgets;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Panel;

/**
 * @author Richard Kennard
 */

public class AllWidgetsModule
	implements EntryPoint
{
	//
	// Private members
	//

	private Panel	mPanel;

	//
	// Constructor
	//

	public AllWidgetsModule( Panel panel )
	{
		mPanel = panel;
	}

	//
	// Public methods
	//

	public void onModuleLoad()
	{
		// Metawidget

		final GwtMetawidget metawidget = new GwtMetawidget();
		metawidget.setInspector( new GwtRemoteInspectorProxy( "/metawidget-inspector-allwidgets" ) );
		metawidget.setLayout( new LabelLayoutDecorator( new LabelLayoutDecoratorConfig().setLayout( new FlexTableLayout( new FlexTableLayoutConfig().setNumberOfColumns( 2 )))) );
		metawidget.setToInspect( new AllWidgets() );

		// Binding

		SimpleBindingProcessorConfig config = new SimpleBindingProcessorConfig();

		@SuppressWarnings( "unchecked" )
		SimpleBindingProcessorAdapter<AllWidgets> allWidgetsAdapter = (SimpleBindingProcessorAdapter<AllWidgets>) GWT.create( AllWidgets.class );
		config.setAdapter( AllWidgets.class, allWidgetsAdapter );
		config.setConverter( Date.class, new DateConverter() );
		config.setConverter( NestedWidgets.class, new NestedWidgetsConverter() );

		metawidget.addWidgetProcessor( new SimpleBindingProcessor( config ) );

		// Stubs

		metawidget.add( new Stub( "mystery" ));

		// Embedded buttons

		Facet buttonsFacet = new Facet();
		buttonsFacet.setName( "buttons" );
		metawidget.add( buttonsFacet );

		final Button saveButton = new Button( "Save" );
		saveButton.addClickHandler( new ClickHandler()
		{
			public void onClick( ClickEvent event )
			{
				metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );
				metawidget.setReadOnly( true );
				metawidget.setLayout( new LabelLayoutDecorator( new LabelLayoutDecoratorConfig().setLayout( new FlexTableLayout( new FlexTableLayoutConfig().setNumberOfColumns( 0 ))).setStyleName( "aSectionStyleName" )) );
				metawidget.setDictionaryName( "bundle" );
			}
		} );

		buttonsFacet.add( saveButton );

		// Add to the given Panel (for unit tests)

		mPanel.add( metawidget );
	}
}
