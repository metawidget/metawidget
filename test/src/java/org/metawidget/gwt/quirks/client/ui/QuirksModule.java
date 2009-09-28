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

package org.metawidget.gwt.quirks.client.ui;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.gwt.client.ui.layout.FlexTableLayout;
import org.metawidget.gwt.client.ui.layout.FlexTableLayoutConfig;
import org.metawidget.gwt.client.widgetprocessor.binding.simple.SimpleBindingProcessor;
import org.metawidget.gwt.client.widgetprocessor.binding.simple.SimpleBindingProcessorAdapter;
import org.metawidget.gwt.quirks.client.model.GwtQuirks;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Richard Kennard
 */

public class QuirksModule
	implements EntryPoint
{
	//
	// Private members
	//

	private Panel	mPanel;

	//
	// Constructor
	//

	public QuirksModule( Panel panel )
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
		metawidget.setStyleName( "componentStyleName" );
		metawidget.setLayout( new FlexTableLayout( new FlexTableLayoutConfig().setSectionStyleName( "aSectionStyleName" )) );
		metawidget.setToInspect( new GwtQuirks() );

		// Binding

		metawidget.addWidgetProcessor( new SimpleBindingProcessor() );

		@SuppressWarnings( "unchecked" )
		SimpleBindingProcessorAdapter<GwtQuirks> quirksAdapter = (SimpleBindingProcessorAdapter<GwtQuirks>) GWT.create( GwtQuirks.class );
		SimpleBindingProcessor.registerAdapter( GwtQuirks.class, quirksAdapter );

		// Arbitrary stub

		Stub stub = new Stub();
		stub.setAttribute( "name", "foo" );
		stub.add( new TextBox() );
		metawidget.add( stub );

		// Add to the given Panel (for unit tests)

		mPanel.add( metawidget );
	}
}
