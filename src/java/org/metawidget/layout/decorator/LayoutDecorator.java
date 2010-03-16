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

package org.metawidget.layout.decorator;

import java.util.Map;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.layout.iface.Layout;
import org.metawidget.layout.iface.LayoutException;

/**
 * Decorates a Layout with additional functionality.
 * <p>
 * LayoutDecorator is abstract. Unlike CompositeInspector and CompositeWidgetBuilder, there is no
 * equivalent CompositeLayout for Layouts. This is because most Layouts are 'end points' and cannot
 * sensibly be composed into Lists. It is unclear what should happen if, say, a CompositeLayout
 * combined a GridBagLayout with a FlowLayout. Rather, Layouts must be combined in a
 * <em>heirarchical</em> fashion, with an 'outer' Layout delegating to a single 'inner' Layout.
 * <p>
 * This delegation approach allows us to extract, say, TabbedPaneLayoutDecorator such that you can
 * decorate other layouts (eg. GridBagLayout, GroupLayout etc) with tabbed section functionality. It
 * also makes it possible to configure whether 'sections within sections' should be rendered as
 * 'tabs within tabs' or 'headings within tabs'.
 * <p>
 * Note: the name Layout<em>Decorator</em> refers to the Decorator design pattern.
 *
 * @author Richard Kennard
 */

public abstract class LayoutDecorator<W, C extends W, M extends C>
	implements AdvancedLayout<W, C, M>
{
	//
	// Private members
	//

	private final Layout<W, C, M>	mDelegate;

	//
	// Constructor
	//

	protected LayoutDecorator( LayoutDecoratorConfig<W, C, M> config )
	{
		mDelegate = config.getLayout();

		if ( mDelegate == null )
			throw LayoutException.newException( getClass().getName() + " needs a Layout to decorate (use " + config.getClass().getName() + ".setLayout)" );
	}

	//
	// Public methods
	//

	public void onStartBuild( M metawidget )
	{
		if ( getDelegate() instanceof AdvancedLayout<?, ?, ?> )
			( (AdvancedLayout<W, C, M>) getDelegate() ).onStartBuild( metawidget );
	}

	@Override
	public void startContainerLayout( C container, M metawidget )
	{
		if ( getDelegate() instanceof AdvancedLayout<?, ?, ?> )
			( (AdvancedLayout<W, C, M>) getDelegate() ).startContainerLayout( container, metawidget );
	}

	public void layoutWidget( W component, String elementName, Map<String, String> attributes, C container, M metawidget )
	{
		// HIGH: concession

		if ( component == null )
			return;

		getDelegate().layoutWidget( component, elementName, attributes, container, metawidget );
	}

	@Override
	public void endContainerLayout( C container, M metawidget )
	{
		if ( getDelegate() instanceof AdvancedLayout<?, ?, ?> )
			( (AdvancedLayout<W, C, M>) getDelegate() ).endContainerLayout( container, metawidget );
	}

	public void onEndBuild( M metawidget )
	{
		if ( getDelegate() instanceof AdvancedLayout<?, ?, ?> )
			( (AdvancedLayout<W, C, M>) getDelegate() ).onEndBuild( metawidget );
	}

	//
	// Protected methods
	//

	protected Layout<W, C, M> getDelegate()
	{
		return mDelegate;
	}
}
