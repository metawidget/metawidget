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

import org.metawidget.util.LayoutUtils;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections.
 *
 * @author Richard Kennard
 */

public abstract class SectionLayoutDecorator<W, M extends W>
	extends LayoutDecorator<W, M>
{
	//
	// Constructor
	//

	protected SectionLayoutDecorator( LayoutDecoratorConfig<W, M> config )
	{
		super( config );
	}

	//
	// Public methods
	//

	@Override
	public void layoutWidget( W widget, String elementName, Map<String, String> attributes, W container, M metawidget )
	{
		String section = LayoutUtils.stripSection( attributes );
		State<W> state = getState( container, metawidget );

		// Stay where we are?

		if ( section == null || section.equals( state.currentSection ) )
		{
			if ( state.currentSectionWidget == null )
				super.layoutWidget( widget, elementName, attributes, container, metawidget );
			else
				super.layoutWidget( widget, elementName, attributes, state.currentSectionWidget, metawidget );

			return;
		}

		state.currentSection = section;

		// End current section

		if ( state.currentSectionWidget != null )
			super.endLayout( state.currentSectionWidget, metawidget );

		// No new section?

		if ( "".equals( section ) )
		{
			super.layoutWidget( widget, elementName, attributes, container, metawidget );
			state.currentSectionWidget = null;
			return;
		}

		// Ignore empty stubs. Do not create a new tab in case it ends up being empty

		if ( isEmptyStub( widget ) )
			return;

		state.currentSectionWidget = createSectionWidget( container, metawidget );

		// Add component to new tab

		super.layoutWidget( widget, elementName, attributes, state.currentSectionWidget, metawidget );
	}

	//
	// Protected methods
	//

	protected abstract State<W> getState( W container, M metawidget );

	protected abstract boolean isEmptyStub( W widget );

	protected abstract W createSectionWidget( W container, M metawidget );

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	public static class State<W>
	{
		public String	currentSection;

		public W		currentSectionWidget;
	}
}
