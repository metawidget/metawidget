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

package org.metawidget.test.gwt.quirks.client.ui;

import org.metawidget.gwt.client.binding.simple.SimpleBinding;
import org.metawidget.gwt.client.binding.simple.SimpleBindingAdapter;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.test.gwt.quirks.client.model.Quirks;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Panel;

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
		metawidget.setParameter( "sectionStyleName", "aSectionStyleName" );
		metawidget.setToInspect( new Quirks() );

		// Binding

		metawidget.setBindingClass( SimpleBinding.class );

		@SuppressWarnings( "unchecked" )
		SimpleBindingAdapter<Quirks> quirksAdapter = (SimpleBindingAdapter<Quirks>) GWT.create( Quirks.class );
		SimpleBinding.registerAdapter( Quirks.class, quirksAdapter );

		// Add to the given Panel (for unit tests)

		mPanel.add( metawidget );
	}
}
