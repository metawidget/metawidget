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

package org.metawidget.layout.delegate;

import java.util.Map;

import org.metawidget.layout.iface.Layout;
import org.metawidget.layout.iface.LayoutException;

/**
 * Delegates Layout functionality to the given Layout.
 * <p>
 * DelegateLayout is abstract. Unlike CompositeInspector and CompositeWidgetBuilder, there is no
 * equivalent CompositeLayout for Layouts. This is because most Layouts are 'end points' and cannot
 * sensibly be composed into Lists. It is unclear what should happen if, say, a CompositeLayout
 * combined a GridBagLayout with a FlowLayout. Rather, Layouts must be combined in a
 * <em>heirarchical</em> fashion, with an 'outer' Layout delegating to a single 'inner' Layout.
 * <p>
 * This delegation approach allows us to extract, say, TabbedPaneSectionLayout such that you can
 * wrap tabbed section functionality around one of the other layouts (eg. GridBagLayout, GroupLayout
 * etc). It also makes it possible to configure whether 'sections within sections' should be
 * rendered as 'tabs within tabs' or 'headings within tabs'.
 *
 * @author Richard Kennard
 */

public abstract class DelegateLayout<W, M extends W>
	implements Layout<W, M>
{
	//
	// Private members
	//

	private Layout<W, M>	mDelegate;

	//
	// Constructor
	//

	public DelegateLayout( DelegateLayoutConfig<W, M> config )
	{
		mDelegate = config.getLayout();

		if ( mDelegate == null )
			throw LayoutException.newException( getClass().getName() + " needs a Layout to delegate to (use " + config.getClass().getName() + ".setLayout)" );
	}

	//
	// Public methods
	//

	@Override
	public void startLayout( W container, M metawidget )
	{
		mDelegate.startLayout( container, metawidget );
	}

	public void layoutWidget( W component, String elementName, Map<String, String> attributes, W container, M metawidget )
	{
		mDelegate.layoutWidget( component, elementName, attributes, container, metawidget );
	}

	@Override
	public void endLayout( W container, M metawidget )
	{
		mDelegate.endLayout( container, metawidget );
	}
}
