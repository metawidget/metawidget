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

package org.metawidget.faces.component.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LayoutUtils;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within JSF Layouts.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class UIComponentNestedSectionLayoutDecorator
	extends org.metawidget.layout.decorator.NestedSectionLayoutDecorator<UIComponent, UIComponent, UIMetawidget> {

	//
	// Private static
	//

	private static final String	COMPONENT_ATTRIBUTE_BOTTOM_LEVEL_SECTION_WIDGET	= "section-widget-bottom-level";

	//
	// Constructor
	//

	protected UIComponentNestedSectionLayoutDecorator( LayoutDecoratorConfig<UIComponent, UIComponent, UIMetawidget> config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected String stripSection( Map<String, String> attributes ) {

		return LayoutUtils.stripSection( attributes );
	}

	@Override
	protected State<UIComponent> getState( UIComponent container, UIMetawidget metawidget ) {

		@SuppressWarnings( "unchecked" )
		Map<UIComponent, UIComponentState> stateMap = (Map<UIComponent, UIComponentState>) metawidget.getClientProperty( getClass() );

		if ( stateMap == null ) {
			stateMap = CollectionUtils.newHashMap();
			metawidget.putClientProperty( getClass(), stateMap );
		}

		UIComponentState state = stateMap.get( container );

		if ( state == null ) {
			state = new UIComponentState();
			stateMap.put( container, state );
		}

		return state;
	}

	@Override
	protected boolean isIgnored( UIComponent component ) {

		return component instanceof UIStub && component.getChildren().isEmpty();
	}

	/**
	 * Overridden, and made final, to support searching for existing section widgets before creating
	 * new ones.
	 * <p>
	 * This avoids creating duplicate section widgets for components that use
	 * <code>UIMetawidget.COMPONENT_ATTRIBUTE_NOT_RECREATABLE</code>. Such components, and their
	 * enclosing section widgets, will survive POST-back so we must reconnect with them.
	 */

	@Override
	protected final UIComponent createSectionWidget( UIComponent previousSectionWidget, String section, Map<String, String> attributes, UIComponent container, UIMetawidget metawidget ) {

		// If there is an existing section widget...

		UIComponentState state = (UIComponentState) getState( container, metawidget );

		for ( UIComponent child : container.getChildren() ) {

			Map<String, Object> childAttributes = child.getAttributes();
			@SuppressWarnings( "unchecked" )
			List<String> storedSectionDecorator = (List<String>) childAttributes.get( UIMetawidget.COMPONENT_ATTRIBUTE_SECTION_DECORATOR );

			if ( storedSectionDecorator == null ) {
				continue;
			}

			// ...that has a section we can use...

			List<String> temporarySectionDecorator = state.getExistingSectionsUsed().get( child );

			if ( temporarySectionDecorator == null ) {
				temporarySectionDecorator = CollectionUtils.newArrayList( storedSectionDecorator );
				state.getExistingSectionsUsed().put( child, temporarySectionDecorator );
			}

			// ...remove it so we don't use it twice...

			if ( !temporarySectionDecorator.remove( section ) ) {
				continue;
			}

			// ...re-lay it out (so that it appears properly positioned)...

			@SuppressWarnings( "unchecked" )
			Map<String, String> childMetadataAttributes = (Map<String, String>) childAttributes.get( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA );
			getDelegate().layoutWidget( child, PROPERTY, childMetadataAttributes, container, metawidget );

			// ...and return its nested section widget (i.e. a layout Metawidget)

			UIComponent childToReturn = child;

			outer: while ( childToReturn != null ) {

				List<UIComponent> nestedChildren = childToReturn.getChildren();
				childToReturn = null;

				for ( UIComponent nestedChild : nestedChildren ) {

					if ( nestedChild.getAttributes().containsKey( COMPONENT_ATTRIBUTE_BOTTOM_LEVEL_SECTION_WIDGET ) ) {
						return nestedChild;
					}

					@SuppressWarnings( "unchecked" )
					List<String> nestedSavedChildSectionDecorator = (List<String>) nestedChild.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_SECTION_DECORATOR );
					List<String> nestedTemporarySectionDecorator = state.getExistingSectionsUsed().get( nestedChild );

					if ( nestedTemporarySectionDecorator == null ) {
						nestedTemporarySectionDecorator = CollectionUtils.newArrayList( nestedSavedChildSectionDecorator );
						state.getExistingSectionsUsed().put( nestedChild, nestedTemporarySectionDecorator );
					}

					if ( nestedTemporarySectionDecorator.remove( section ) ) {
						childToReturn = nestedChild;
						continue outer;
					}
				}
			}

			// Fall through
		}

		// Otherwise, create a new section widget...

		UIComponent sectionWidget = createNewSectionWidget( previousSectionWidget, section, attributes, container, metawidget );

		// ...and tag it (for layout Metawidget -> UITab -> UITabPanel, may need tagging at 3
		// levels)

		UIComponent taggedSectionWidget = sectionWidget;
		taggedSectionWidget.getAttributes().put( COMPONENT_ATTRIBUTE_BOTTOM_LEVEL_SECTION_WIDGET, TRUE );

		while ( !taggedSectionWidget.equals( container ) ) {

			Map<String, Object> sectionWidgetParentAttributes = taggedSectionWidget.getAttributes();
			@SuppressWarnings( "unchecked" )
			List<String> sectionDecorator = (List<String>) sectionWidgetParentAttributes.get( UIMetawidget.COMPONENT_ATTRIBUTE_SECTION_DECORATOR );

			if ( sectionDecorator == null ) {
				sectionDecorator = CollectionUtils.newArrayList();
				sectionWidgetParentAttributes.put( UIMetawidget.COMPONENT_ATTRIBUTE_SECTION_DECORATOR, sectionDecorator );
			}

			// Tag this section widget as 'used'

			if ( state.getExistingSectionsUsed().get( taggedSectionWidget ) == null ) {
				state.getExistingSectionsUsed().put( taggedSectionWidget, new ArrayList<String>() );
			}

			sectionDecorator.add( section );

			taggedSectionWidget = taggedSectionWidget.getParent();
		}

		return sectionWidget;
	}

	/**
	 * Creates a new widget to hold this section (<code>getState().currentSection</code>).
	 * <p>
	 * Clients should use this version of the method instead of <code>createSectionWidget</code>, in
	 * order to support <code>UIMetawidget.COMPONENT_ATTRIBUTE_NOT_RECREATABLE</code>.
	 *
	 * @param previousSectionWidget
	 *            the previous section widget (if any). This can be useful for tracing back to, say,
	 *            a TabHost
	 */

	protected abstract UIComponent createNewSectionWidget( UIComponent previousSectionWidget, String section, Map<String, String> attributes, UIComponent container, UIMetawidget metawidget );

	/**
	 * Clears out all <code>existingSectionsUsed</code>. Needed for ICEfaces support.
	 */

	@Override
	public void onEndBuild( UIMetawidget metawidget ) {

		super.onEndBuild( metawidget );

		metawidget.putClientProperty( getClass(), null );
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	public static class UIComponentState
		extends State<UIComponent> {

		//
		// Private members
		//

		/**
		 * Map to track which existing sections we have already reused. This is important when two
		 * or more sections on the page have the same name.
		 */

		private Map<UIComponent, List<String>>	mExistingSectionsUsed	= CollectionUtils.newHashMap();

		//
		// Public methods
		//

		public Map<UIComponent, List<String>> getExistingSectionsUsed() {

			return mExistingSectionsUsed;
		}
	}
}
