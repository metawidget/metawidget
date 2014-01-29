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

package org.metawidget.swt.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Arrays;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.layout.decorator.NestedSectionLayoutDecorator;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.LayoutUtils;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within SWT Layouts.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class SwtFlatSectionLayoutDecorator
	extends org.metawidget.layout.decorator.FlatSectionLayoutDecorator<Control, Composite, SwtMetawidget>
	implements SwtLayoutDecorator {

	//
	// Constructor
	//

	protected SwtFlatSectionLayoutDecorator( LayoutDecoratorConfig<Control, Composite, SwtMetawidget> config ) {

		super( config );
	}

	//
	// Public methods
	//

	@Override
	public void startContainerLayout( Composite container, SwtMetawidget metawidget ) {

		super.startContainerLayout( container, metawidget );
		container.setData( getClass().getName(), null );
	}

	public Composite startBuildWidget( String elementName, Map<String, String> attributes, Composite container, SwtMetawidget metawidget ) {

		// If our delegate is itself a NestedSectionLayoutDecorator, strip the section

		State state = getState( container, metawidget );

		if ( getDelegate() instanceof NestedSectionLayoutDecorator ) {
			String section = stripSection( attributes );

			// Stay where we are?

			if ( section == null || ( state.getCurrentSections() != null && section.equals( state.getCurrentSections()[0] ) ) ) {
				return delegateStartBuildWidget( elementName, attributes, container, metawidget );
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

			if ( sections.length == 0 || Arrays.equals( sections, state.getCurrentSections() ) ) {
				return delegateStartBuildWidget( elementName, attributes, container, metawidget );
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

				addSectionWidget( section, level, container, metawidget );
			}

			state.setCurrentSections( sections );
		}

		// Add component as normal

		return delegateStartBuildWidget( elementName, attributes, container, metawidget );
	}

	@Override
	public void layoutWidget( Control widget, String elementName, Map<String, String> attributes, Composite container, SwtMetawidget metawidget ) {

		getDelegate().layoutWidget( widget, elementName, attributes, container, metawidget );
	}

	//
	// Protected methods
	//

	@Override
	protected String stripSection( Map<String, String> attributes ) {

		return LayoutUtils.stripSection( attributes );
	}

	@Override
	protected String[] getSections( Map<String, String> attributes ) {

		return ArrayUtils.fromString( attributes.get( SECTION ) );
	}

	@Override
	protected State getState( Composite container, SwtMetawidget metawidget ) {

		State state = (State) container.getData( getClass().getName() );

		if ( state == null ) {
			state = new State();
			container.setData( getClass().getName(), state );
		}

		return state;
	}

	@Override
	protected boolean isIgnored( Control control ) {

		return control instanceof Stub && ( (Stub) control ).getChildren().length == 0;
	}

	//
	// Private methods
	//

	private Composite delegateStartBuildWidget( String elementName, Map<String, String> attributes, Composite container, SwtMetawidget metawidget ) {

		if ( getDelegate() instanceof SwtLayoutDecorator ) {
			return ( (SwtLayoutDecorator) getDelegate() ).startBuildWidget( elementName, attributes, container, metawidget );
		}

		return container;
	}
}
