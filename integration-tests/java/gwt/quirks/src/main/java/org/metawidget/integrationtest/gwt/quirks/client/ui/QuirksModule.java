// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.integrationtest.gwt.quirks.client.ui;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.gwt.client.ui.layout.FlexTableLayout;
import org.metawidget.gwt.client.ui.layout.LabelLayoutDecorator;
import org.metawidget.gwt.client.ui.layout.LabelLayoutDecoratorConfig;
import org.metawidget.gwt.client.ui.layout.TabPanelLayoutDecorator;
import org.metawidget.gwt.client.widgetprocessor.binding.simple.SimpleBindingProcessor;
import org.metawidget.gwt.client.widgetprocessor.binding.simple.SimpleBindingProcessorAdapter;
import org.metawidget.gwt.client.widgetprocessor.binding.simple.SimpleBindingProcessorConfig;
import org.metawidget.integrationtest.gwt.quirks.client.model.GwtQuirks;
import org.metawidget.integrationtest.gwt.quirks.client.model.GwtTabQuirks;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class QuirksModule
	implements EntryPoint {

	//
	// Private members
	//

	private Panel	mPanel;

	//
	// Constructor
	//

	public QuirksModule( Panel panel ) {

		mPanel = panel;
	}

	//
	// Public methods
	//

	public void onModuleLoad() {

		// Metawidget

		final GwtMetawidget metawidget = new GwtMetawidget();
		metawidget.setStyleName( "componentStyleName" );
		metawidget.setLayout( new LabelLayoutDecorator( new LabelLayoutDecoratorConfig().setLayout( new FlexTableLayout() ).setStyleName( "aSectionStyleName" ) ) );
		metawidget.setToInspect( new GwtQuirks() );

		// Binding

		@SuppressWarnings( "unchecked" )
		SimpleBindingProcessorAdapter<GwtQuirks> quirksAdapter = (SimpleBindingProcessorAdapter<GwtQuirks>) GWT.create( GwtQuirks.class );
		SimpleBindingProcessorConfig config = new SimpleBindingProcessorConfig().setAdapter( GwtQuirks.class, quirksAdapter );

		metawidget.addWidgetProcessor( new SimpleBindingProcessor( config ) );

		// Arbitrary stub

		Stub stub = new Stub();
		stub.setAttribute( "name", "foo" );
		stub.add( new TextBox() );
		metawidget.add( stub );

		// Add to the given Panel (for unit tests)

		mPanel.add( metawidget );

		// Tabbed Metawidget

		GwtMetawidget tabbedMetawidget = new GwtMetawidget();
		tabbedMetawidget.setLayout( new TabPanelLayoutDecorator( new LabelLayoutDecoratorConfig().setLayout( new TabPanelLayoutDecorator( new LabelLayoutDecoratorConfig().setLayout( new FlexTableLayout() ) ) ) ) );
		tabbedMetawidget.setToInspect( new GwtTabQuirks() );

		// Add to the given Panel (for unit tests)

		mPanel.add( tabbedMetawidget );
	}
}
