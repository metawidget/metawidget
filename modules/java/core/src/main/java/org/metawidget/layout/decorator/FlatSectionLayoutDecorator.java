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

import java.util.Arrays;
import java.util.Map;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections, rendering multi-level sections (ie. section="foo,bar") as siblings.
 * <p>
 * Because sections are rendered as siblings, <code>FlatSectionLayoutDecorator</code> never creates
 * a sub <code>container</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class FlatSectionLayoutDecorator<W, C extends W, M extends C>
	extends LayoutDecorator<W, C, M> {

	//
	// Constructor
	//

	protected FlatSectionLayoutDecorator( LayoutDecoratorConfig<W, C, M> config ) {

		super( config );
	}

	//
	// Public methods
	//

	@Override
	public void startContainerLayout( C container, M metawidget ) {

		super.startContainerLayout( container, metawidget );

		State state = getState( container, metawidget );
		state.setCurrentSections( null );
	}

	@Override
	public void layoutWidget( W widget, String elementName, Map<String, String> attributes, C container, M metawidget ) {

		// If our delegate is itself a NestedSectionLayoutDecorator, strip the section

		State state = getState( container, metawidget );

		if ( getDelegate() instanceof NestedSectionLayoutDecorator<?, ?, ?> ) {

			String section = stripSection( attributes );

			// Stay where we are?
			//
			// Note: Ignore empty stubs. Do not create a new section in case it ends up being empty

			if ( isIgnored( widget ) || section == null || ( state.getCurrentSections() != null && section.equals( state.getCurrentSections()[0] ) ) ) {
				super.layoutWidget( widget, elementName, attributes, container, metawidget );
				return;
			}

			// End nested LayoutDecorator's current section

			if ( state.getCurrentSections() != null && !section.equals( state.getCurrentSections()[0] ) ) {
				super.endContainerLayout( container, metawidget );
			}

			state.setCurrentSections( new String[] { section } );

			// Add a heading

			if ( !"".equals( section ) ) {
				addSectionWidget( section, 0, container, metawidget );
			}
		} else {

			String[] sections = getSections( attributes );

			// Stay where we are?
			//
			// Note: Ignore empty stubs. Do not create a new section in case it ends up being empty

			if ( isIgnored( widget ) || sections.length == 0 || Arrays.equals( sections, state.getCurrentSections() ) ) {
				super.layoutWidget( widget, elementName, attributes, container, metawidget );
				return;
			}

			// For each of the new sections...

			for ( int level = 0; level < sections.length; level++ ) {
				String section = sections[level];

				// ...that are different from our current...

				if ( "".equals( section ) ) {
					continue;
				}

				if ( state.getCurrentSections() != null && level < state.getCurrentSections().length && section.equals( state.getCurrentSections()[level] ) ) {
					continue;
				}

				// ...add a heading
				//
				// Note: we cannot stop/start the delegate layout here. It is tempting, but remember
				// addSectionWidget needs to use the delegate. If you stop/add section heading/start
				// the delegate, who is laying out the section heading?

				addSectionWidget( section, level, container, metawidget );
			}

			state.setCurrentSections( sections );
		}

		// Add component as normal

		super.layoutWidget( widget, elementName, attributes, container, metawidget );
	}

	//
	// Protected methods
	//

	protected abstract String stripSection( Map<String, String> attributes );

	protected abstract String[] getSections( Map<String, String> attributes );

	protected abstract State getState( C container, M metawidget );

	/**
	 * Returns <code>true</code> if the given widget should be ignored, and no section heading
	 * created for it. For example, the widget might be an empty Stub widget.
	 * <p>
	 * Subclasses should override this method and return <code>true</code> if the widget is some
	 * other kind of ignored component (eg. <code>HtmlInputHidden</code>).
	 */

	protected abstract boolean isIgnored( W widget );

	/**
	 * @param section
	 *            section text (needs localizing)
	 * @param level
	 *            level of section heading (ie. 0=highest, 1=next level down etc.)
	 * @param container
	 * @param metawidget
	 */

	protected abstract void addSectionWidget( String section, int level, C container, M metawidget );

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	public static class State {

		//
		// Private members
		//

		private String[]	mCurrentSections;

		//
		// Public methods
		//

		public String[] getCurrentSections() {

			return mCurrentSections;
		}

		public void setCurrentSections( String[] currentSections ) {

			this.mCurrentSections = currentSections;
		}
	}
}
