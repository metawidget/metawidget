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

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections, rendering multi-level sections (ie. section="foo,bar") as nested components.
 *
 * @author Richard Kennard
 */

public abstract class NestedSectionLayoutDecorator<W, C extends W, M extends C>
	extends LayoutDecorator<W, C, M> {

	//
	// Constructor
	//

	protected NestedSectionLayoutDecorator( LayoutDecoratorConfig<W, C, M> config ) {

		super( config );
	}

	//
	// Public methods
	//

	@Override
	public void startContainerLayout( C container, M metawidget ) {

		super.startContainerLayout( container, metawidget );

		State<C> state = getState( container, metawidget );
		state.currentSection = null;
		state.currentSectionWidget = null;
	}

	@Override
	public void layoutWidget( W widget, String elementName, Map<String, String> attributes, C container, M metawidget ) {

		String section = stripSection( attributes );
		State<C> state = getState( container, metawidget );

		// Stay where we are?

		if ( section == null || section.equals( state.currentSection ) ) {
			if ( state.currentSectionWidget == null ) {
				super.layoutWidget( widget, elementName, attributes, container, metawidget );
			} else {
				super.layoutWidget( widget, elementName, attributes, state.currentSectionWidget, metawidget );
			}

			return;
		}

		state.currentSection = section;

		C previousSectionWidget = state.currentSectionWidget;

		// End current section

		if ( state.currentSectionWidget != null ) {
			super.endContainerLayout( state.currentSectionWidget, metawidget );
		}

		state.currentSectionWidget = null;

		// No new section?

		if ( "".equals( section ) ) {
			super.layoutWidget( widget, elementName, attributes, container, metawidget );
			return;
		}

		// Ignore empty stubs. Do not create a new tab in case it ends up being empty

		if ( isEmptyStub( widget ) ) {
			return;
		}

		state.currentSectionWidget = createSectionWidget( previousSectionWidget, attributes, container, metawidget );
		super.startContainerLayout( state.currentSectionWidget, metawidget );

		// Add component to new section

		super.layoutWidget( widget, elementName, attributes, state.currentSectionWidget, metawidget );
	}

	@Override
	public void endContainerLayout( C container, M metawidget ) {

		// End hanging layouts

		State<C> state = getState( container, metawidget );

		if ( state.currentSectionWidget != null ) {
			super.endContainerLayout( state.currentSectionWidget, metawidget );
		}

		super.endContainerLayout( container, metawidget );
		state.currentSection = null;
		state.currentSectionWidget = null;
	}

	//
	// Protected methods
	//

	protected abstract String stripSection( Map<String, String> attributes );

	protected abstract State<C> getState( C container, M metawidget );

	protected abstract boolean isEmptyStub( W widget );

	/**
	 * Creates a new widget to hold this section (<code>getState().currentSection</code>).
	 *
	 * @param previousSectionWidget
	 *            the previous section widget (if any). This can be useful for tracing back to, say,
	 *            a TabHost
	 */

	protected abstract C createSectionWidget( C previousSectionWidget, Map<String, String> attributes, C container, M metawidget );

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	public static class State<C> {

		public String	currentSection;

		public C		currentSectionWidget;
	}
}
