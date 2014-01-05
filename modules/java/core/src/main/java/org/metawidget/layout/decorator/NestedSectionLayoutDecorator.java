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

package org.metawidget.layout.decorator;

import java.util.Map;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections, rendering multi-level sections (ie. section="foo,bar") as nested components.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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

		// Stay where we are?
		//
		// Note: Ignore empty stubs. Do not create a new section in case it ends up being empty

		String section = stripSection( attributes );
		State<C> state = getState( container, metawidget );

		if ( isIgnored( widget ) || section == null || section.equals( state.currentSection ) ) {
			if ( state.currentSectionWidget == null ) {
				super.layoutWidget( widget, elementName, attributes, container, metawidget );
			} else {
				super.layoutWidget( widget, elementName, attributes, state.currentSectionWidget, metawidget );
			}

			return;
		}

		// End current section

		C previousSectionWidget = state.currentSectionWidget;

		if ( state.currentSectionWidget != null ) {
			super.endContainerLayout( state.currentSectionWidget, metawidget );
		}

		state.currentSection = section;
		state.currentSectionWidget = null;

		// No new section?

		if ( "".equals( section ) ) {
			super.layoutWidget( widget, elementName, attributes, container, metawidget );
			return;
		}

		// Start new section

		state.currentSectionWidget = createSectionWidget( previousSectionWidget, section, attributes, container, metawidget );
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

	protected abstract boolean isIgnored( W widget );

	/**
	 * Creates a new widget to hold this section (<code>getState().currentSection</code>).
	 *
	 * @param previousSectionWidget
	 *            the previous section widget (if any). This can be useful for tracing back to, say,
	 *            a TabHost
	 */

	protected abstract C createSectionWidget( C previousSectionWidget, String section, Map<String, String> attributes, C container, M metawidget );

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
