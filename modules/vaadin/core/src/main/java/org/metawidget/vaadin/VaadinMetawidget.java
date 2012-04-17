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

package org.metawidget.vaadin;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.metawidget.iface.Immutable;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.layout.iface.Layout;
import org.metawidget.pipeline.w3c.W3CPipeline;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.PathUtils.TypeAndNames;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.w3c.dom.Element;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;

/**
 * Metawidget for Vaadin environments.
 *
 * @author Loghman Barari
 */

public class VaadinMetawidget
	extends CustomComponent {

	//
	// Private statics
	//

	private static int				NEXT_ID				= 1;

	//
	// Private members
	//

	private Object					mToInspect;

	private String					mPath;

	private ResourceBundle			mBundle;

	private boolean					mNeedToBuildWidgets;

	private Element					mLastInspection;

	private boolean					mIgnoreAddRemove;

	/**
	 * List of existing, manually added components.
	 * <p>
	 * This is a List, not a Set, so that mExistingUnusedComponents (which is initialized from it)
	 * is consistent.
	 */

	private List<Component>			mExistingComponents	= CollectionUtils.newArrayList();

	/**
	 * List of existing, manually added, but unused by Metawidget components.
	 * <p>
	 * This is a List, not a Set, for consistency during endBuild.
	 */

	private List<Component>			mExistingUnusedComponents;

	private Map<String, Facet>		mFacets				= CollectionUtils.newHashMap();

	private Map<Object, Object>		mClientProperties;

	/* package private */Pipeline	mPipeline;

	//
	// Constructor
	//

	public VaadinMetawidget() {

		setDebugId( "VaadinMetawidget#" );

		// setWidth( "100%" );
		mPipeline = newPipeline();
	}

	public VaadinMetawidget( String id ) {

		this();

		setDebugId( id );
	}

	//
	// Public methods
	//

	@Override
	public void setDebugId( String id ) {

		if ( VaadinMetawidget.NEXT_ID > Integer.MAX_VALUE - 10 ) {
			VaadinMetawidget.NEXT_ID = Integer.MIN_VALUE;
		}

		super.setDebugId( id + VaadinMetawidget.NEXT_ID );
	}

	/**
	 * Sets the Object to inspect.
	 * <p>
	 * If <code>setPath</code> has not been set, or points to a previous <code>setToInspect</code>,
	 * sets it to point to the given Object.
	 */
	public void setToInspect( Object toInspect ) {

		updateToInspectWithoutInvalidate( toInspect );

		invalidateInspection();

		this.buildWidgets();
	}

	/**
	 * Updates the Object to inspect, without invalidating the previous
	 * inspection results.
	 * <p>
	 * <strong>This is an internal API exposed for WidgetProcessor rebinding support. Clients should
	 * not call it directly.</strong>
	 */

	public void updateToInspectWithoutInvalidate( Object toInspect ) {

		if ( mToInspect == null ) {
			if ( mPath == null && toInspect != null ) {
				mPath = toInspect.getClass().getName();
			}
		} else if ( mToInspect.getClass().getName().equals( mPath ) ) {
			if ( toInspect == null ) {
				mPath = null;
			} else {
				mPath = toInspect.getClass().getName();
			}
		}

		mToInspect = toInspect;
	}

	/**
	 * Gets the Object being inspected.
	 * <p>
	 * Exposed for binding implementations.
	 *
	 * @return the object. Note this return type uses generics, so as to not require a cast by the
	 *         caller (eg. <code>Person p = getToInspect()</code>)
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getToInspect() {

		return (T) mToInspect;
	}

	/**
	 * Sets the path to be inspected.
	 * <p>
	 * Note <code>setPath</code> is quite different to <code>java.awt.Component.setName</code>.
	 * <code>setPath</code> is always in relation to <code>setToInspect</code>, so must include the
	 * type name and any subsequent sub-names (eg. type/name/name). Conversely, <code>setName</code>
	 * is a single name relative to our immediate parent.
	 */

	public void setPath( String path ) {

		mPath = path;
		invalidateInspection();
	}

	public String getPath() {

		return mPath;
	}

	public void setConfig( String config ) {

		mPipeline.setConfig( config );
		invalidateInspection();
		buildWidgets();
	}

	public void setInspector( Inspector inspector ) {

		mPipeline.setInspector( inspector );
		invalidateInspection();
	}

	/**
	 * Useful for WidgetBuilders to perform nested inspections (eg. for
	 * Collections).
	 */

	public String inspect( Object toInspect, String type, String... names ) {

		return mPipeline.inspect( toInspect, type, names );
	}

	public void addInspectionResultProcessor(
			InspectionResultProcessor<VaadinMetawidget> inspectionResultProcessor ) {

		mPipeline.addInspectionResultProcessor( inspectionResultProcessor );
		invalidateInspection();
	}

	public void removeInspectionResultProcessor(
			InspectionResultProcessor<VaadinMetawidget> inspectionResultProcessor ) {

		mPipeline.removeInspectionResultProcessor( inspectionResultProcessor );
		invalidateInspection();
	}

	public void setInspectionResultProcessors(
			InspectionResultProcessor<VaadinMetawidget>... inspectionResultProcessors ) {

		mPipeline.setInspectionResultProcessors( inspectionResultProcessors );
		invalidateInspection();
	}

	public void setWidgetBuilder(
			WidgetBuilder<Component, VaadinMetawidget> widgetBuilder ) {

		mPipeline.setWidgetBuilder( widgetBuilder );
		invalidateWidgets();
	}

	public void addWidgetProcessor(
			WidgetProcessor<Component, VaadinMetawidget> widgetProcessor ) {

		mPipeline.addWidgetProcessor( widgetProcessor );
		invalidateWidgets();
	}

	public void removeWidgetProcessor(
			WidgetProcessor<Component, VaadinMetawidget> widgetProcessor ) {

		mPipeline.removeWidgetProcessor( widgetProcessor );
		invalidateWidgets();
	}

	public void setWidgetProcessors(
			WidgetProcessor<Component, VaadinMetawidget>... widgetProcessors ) {

		mPipeline.setWidgetProcessors( widgetProcessors );
		invalidateWidgets();
	}

	public <T> T getWidgetProcessor( Class<T> widgetProcessorClass ) {

		buildWidgets();
		return mPipeline.getWidgetProcessor( widgetProcessorClass );
	}

	/**
	 * Set the layout for this Metawidget.
	 */

	public void setLayout(
			Layout<Component, ComponentContainer, VaadinMetawidget> layout ) {

		mPipeline.setLayout( layout );
		invalidateWidgets();
	}

	public ResourceBundle getBundle() {

		return mBundle;
	}

	public void setBundle( ResourceBundle bundle ) {

		mBundle = bundle;
		invalidateWidgets();
	}

	public String getLabelString( Map<String, String> attributes ) {

		if ( attributes == null ) {
			return "";
		}

		// Explicit label

		String label = attributes.get( LABEL );

		if ( label != null ) {
			// (may be forced blank)

			if ( "".equals( label ) ) {
				return null;
			}

			// (localize if possible)

			String localized = getLocalizedKey( StringUtils.camelCase( label ) );

			if ( localized != null ) {
				return localized.trim();
			}

			return label.trim();
		}

		// Default name

		String name = attributes.get( NAME );

		if ( name != null ) {

			// (localize if possible)

			String localized = getLocalizedKey( name );

			if ( localized != null ) {
				return localized.trim();
			}

			return StringUtils.uncamelCase( name );
		}

		return "";
	}

	/**
	 * @return null if no bundle, ???key??? if bundle is missing a key
	 */

	public String getLocalizedKey( String key ) {

		if ( mBundle == null ) {
			return null;
		}

		try {
			return mBundle.getString( key );
		} catch ( MissingResourceException e ) {
			return StringUtils.RESOURCE_KEY_NOT_FOUND_PREFIX + key + StringUtils.RESOURCE_KEY_NOT_FOUND_SUFFIX;
		}
	}

	@Override
	public boolean isReadOnly() {

		return mPipeline.isReadOnly();
	}

	@Override
	public void setReadOnly( boolean readOnly ) {

		if ( mPipeline.isReadOnly() == readOnly ) {
			return;
		}

		mPipeline.setReadOnly( readOnly );
		invalidateWidgets();
		buildWidgets();
	}

	public int getMaximumInspectionDepth() {

		return mPipeline.getMaximumInspectionDepth();
	}

	public void setMaximumInspectionDepth( int maximumInspectionDepth ) {

		mPipeline.setMaximumInspectionDepth( maximumInspectionDepth );
		invalidateWidgets();
	}

	/**
	 * Fetch a list of <code>AbstractComponents</code> that were added manually,
	 * and have so far not been used.
	 * <p>
	 * <strong>This is an internal API exposed for OverriddenWidgetBuilder. Clients should not call
	 * it directly.</strong>
	 */

	public List<Component> fetchExistingUnusedComponents() {

		return mExistingUnusedComponents;
	}

	public void setLayoutRoot( Component layoutRoot ) {

		super.setCompositionRoot( layoutRoot );
	}

	public Component getLayoutRoot() {

		return super.getCompositionRoot();
	}

	/**
	 * Overridden to build widgets just-in-time.
	 * <p>
	 * This method may be called by developers who wish to modify the created Components before they
	 * are displayed.
	 */

	@Override
	public int getComponentCount() {

		buildWidgets();

		return getComponentCount();
	}

	/**
	 * Gets the value from the Component with the given name.
	 * <p>
	 * The value is returned as it was stored in the Component (eg. String for TextField) so may
	 * need some conversion before being reapplied to the object being inspected. This obviously
	 * requires knowledge of which Component VaadinMetawidget created, which is not ideal, so
	 * clients may prefer to use bindingClass instead.
	 *
	 * @return the value. Note this return type uses generics, so as to not
	 *         require a cast by the caller (eg. <code>String s = getValue(names)</code>)
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getValue( String... names ) {

		ComponentAndValueProperty componentAndValueProperty = getComponentAndValueProperty( names );

		Object value;

		if ( ( componentAndValueProperty.getComponent() instanceof AbstractField ) && ( ( (AbstractField) componentAndValueProperty.getComponent() ).getPropertyDataSource() != null ) ) {
			value = ( (AbstractField) componentAndValueProperty.getComponent() ).getPropertyDataSource().getValue();
		} else {
			value = ClassUtils.getProperty( componentAndValueProperty.getComponent(), componentAndValueProperty.getValueProperty() );
		}

		return (T) value;
	}

	/**
	 * Sets the Component with the given name to the specified value.
	 * <p>
	 * Clients must ensure the value is of the correct type to suit the Component (eg. String for
	 * TextField). This obviously requires knowledge of which Component VaadinMetawidget created,
	 * which is not ideal, so clients may prefer to use bindingClass instead.
	 */

	public void setValue( Object value, String... names ) {

		ComponentAndValueProperty componentAndValueProperty = getComponentAndValueProperty( names );
		ClassUtils.setProperty( componentAndValueProperty.getComponent(), componentAndValueProperty.getValueProperty(), value );
	}

	/**
	 * Returns the property used to get/set the value of the component.
	 * <p>
	 * If the component is not known, returns <code>null</code>.
	 */

	public String getValueProperty( Component component ) {

		return getValueProperty( component, mPipeline.getWidgetBuilder() );
	}

	/**
	 * Finds the Component with the given name.
	 */

	@SuppressWarnings( "unchecked" )
	public <T extends Component> T getComponent( int index ) {

		Iterator<Component> iterator = getComponentIterator();

		Component component = null;

		for ( int i = 0; i <= index; i++ ) {
			component = iterator.next();
		}

		return (T) component;
	}

	@SuppressWarnings( "unchecked" )
	public <T extends Component> T getComponent( String... names ) {

		if ( names == null || names.length == 0 ) {
			return null;
		}

		Component topComponent = this;

		String fullName = this.getDebugId();

		for ( int loop = 0, length = names.length; loop < length; loop++ ) {
			String name = names[loop];

			fullName += "$" + name;

			// May need building 'just in time' if we are calling getComponent
			// immediately after a 'setToInspect'. See
			// VaadinMetawidgetTest.testNestedWithManualInspector

			if ( topComponent instanceof VaadinMetawidget ) {
				( (VaadinMetawidget) topComponent ).buildWidgets();

				fullName = topComponent.getDebugId() + "$" + name;
			}

			if ( topComponent instanceof Table ) {
				topComponent = topComponent.getParent();
			}

			// Try to find a component

			if ( topComponent instanceof ComponentContainer ) {
				topComponent = getComponent( (ComponentContainer) topComponent, fullName );
			} else {
				topComponent = null;
			}

			if ( loop == length - 1 ) {
				return (T) topComponent;
			}

			if ( topComponent == null ) {
				throw MetawidgetException.newException( "No such component '" + name + "' of '" + ArrayUtils.toString( names, "', '" ) + "'" );
			}
		}

		return (T) topComponent;
	}

	public Facet getFacet( String name ) {

		buildWidgets();

		return mFacets.get( name );
	}

	@Override
	public void removeComponent( Component component ) {

		// TODO: super.removeComponent( component );

		if ( !mIgnoreAddRemove ) {
			invalidateWidgets();

			if ( component instanceof Facet ) {
				mFacets.remove( ( (Facet) component ).getDebugId() );
			} else {
				mExistingComponents.remove( component );
			}
		}
	}

	@Override
	public void removeAllComponents() {

		super.removeAllComponents();

		if ( !mIgnoreAddRemove ) {
			invalidateWidgets();

			mFacets.clear();
			mExistingComponents.clear();
		}

		this.mClientProperties.clear();
	}

	//
	// Protected methods
	//

	/**
	 * Instantiate the Pipeline used by this Metawidget.
	 * <p>
	 * Subclasses wishing to use their own Pipeline should override this method to instantiate their
	 * version.
	 */

	protected Pipeline newPipeline() {

		return new Pipeline();
	}

	protected String getDefaultConfiguration() {

		return ClassUtils.getPackagesAsFolderNames( VaadinMetawidget.class ) + "/metawidget-vaadin-default.xml";
	}

	/**
	 * Invalidates the current inspection result (if any) <em>and</em> invalidates the widgets.
	 * <p>
	 * As an optimisation we only invalidate the widgets, not the entire inspection result, for some
	 * operations (such as adding/removing stubs, changing read-only etc.)
	 */

	protected void invalidateInspection() {

		mLastInspection = null;
		invalidateWidgets();
	}

	/**
	 * Invalidates the widgets.
	 */

	protected void invalidateWidgets() {

		if ( mNeedToBuildWidgets ) {
			return;
		}

		// Note: it is important to call removeAll BEFORE setting
		// mNeedToBuildWidgets to true.
		// immediate repaint which sets mNeedToBuildWidgets back to false

		// TODO: super.removeAllComponents();

		// Prepare to build widgets

		this.mNeedToBuildWidgets = true;

		if ( this.mClientProperties != null ) {
			this.mClientProperties.clear();
		}

		// Call repaint here, rather than just 'invalidate', for scenarios like
		// doing a 'remove' of a button that masks a Metawidget

		this.requestRepaint();
	}

	protected void buildWidgets() {

		// No need to build?

		if ( !mNeedToBuildWidgets ) {
			return;
		}

		mPipeline.configureOnce();

		mNeedToBuildWidgets = false;
		mIgnoreAddRemove = true;

		try {
			if ( mLastInspection == null ) {
				mLastInspection = inspect();
			}

			if ( mPath != null ) {
				mPipeline.buildWidgets( mLastInspection );
			}
		} catch ( Exception e ) {
			throw MetawidgetException.newException( e );
		} finally {
			mIgnoreAddRemove = false;
		}
	}

	protected void startBuild() {

		mExistingUnusedComponents = CollectionUtils.newArrayList( mExistingComponents );
	}

	/**
	 * @param elementName
	 *            XML node name of the business field. Typically 'entity',
	 *            'property' or 'action'. Never null
	 */

	protected void layoutWidget( Component component, String elementName,
			Map<String, String> attributes ) {

		// Remove, then re-add to layout (to re-order the component)

		removeComponent( component );

		// Look up any additional attributes

		Map<String, String> additionalAttributes = mPipeline.getAdditionalAttributes( component );

		if ( additionalAttributes != null ) {
			attributes.putAll( additionalAttributes );
		}
	}

	protected void endBuild() {

		if ( mExistingUnusedComponents != null ) {
			for ( Component componentExisting : mExistingUnusedComponents ) {
				// Unused facets don't count

				if ( componentExisting instanceof Facet ) {
					continue;
				}

				// Manually created components default to no section

				Map<String, String> attributes = CollectionUtils.newHashMap();
				attributes.put( SECTION, "" );

				mPipeline.layoutWidget( componentExisting, PROPERTY, attributes );
			}
		}
	}

	protected void initNestedMetawidget( VaadinMetawidget nestedMetawidget, Map<String, String> attributes ) {

		// Don't copy setConfig(). Instead, copy runtime values

		mPipeline.initNestedPipeline( nestedMetawidget.mPipeline, attributes );

		nestedMetawidget.setPath( mPath + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME ) );

		nestedMetawidget.setDebugId( this.getDebugId() + "$" + attributes.get( NAME ) );
		nestedMetawidget.setWidth( "100%" );
		nestedMetawidget.setBundle( mBundle );

		nestedMetawidget.setToInspect( mToInspect );
	}

	//
	// Private methods
	//

	private Element inspect() {

		if ( mPath == null ) {
			return null;
		}

		TypeAndNames typeAndNames = PathUtils.parsePath( mPath );

		return mPipeline.inspectAsDom( mToInspect, typeAndNames.getType(), typeAndNames.getNamesAsArray() );
	}

	private ComponentAndValueProperty getComponentAndValueProperty( String... names ) {

		Component component = getComponent( names );

		if ( component == null ) {
			throw MetawidgetException.newException( "No component named '" + ArrayUtils.toString( names, "', '" ) + "'" );
		}

		String componentProperty = getValueProperty( component );

		if ( componentProperty == null ) {
			throw MetawidgetException.newException( "Don't know how to getValue from a " + component.getClass().getName() );
		}

		return new ComponentAndValueProperty( component, componentProperty );
	}

	private String getValueProperty( Component component,
			WidgetBuilder<Component, VaadinMetawidget> widgetBuilder ) {

		// Recurse into CompositeWidgetBuilders

		try {
			if ( widgetBuilder instanceof CompositeWidgetBuilder<?, ?> ) {
				for ( WidgetBuilder<Component, VaadinMetawidget> widgetBuilderChild : ( (CompositeWidgetBuilder<Component, VaadinMetawidget>) widgetBuilder ).getWidgetBuilders() ) {

					String valueProperty = getValueProperty( component, widgetBuilderChild );

					if ( valueProperty != null ) {
						return valueProperty;
					}
				}

				return null;
			}
		} catch ( NoClassDefFoundError e ) {
			// May not be shipping with CompositeWidgetBuilder
		}

		// Interrogate ValuePropertyProviders

		if ( widgetBuilder instanceof VaadinValuePropertyProvider ) {

			return ( (VaadinValuePropertyProvider) widgetBuilder ).getValueProperty( component );
		}

		return null;
	}

	private Component getComponent( ComponentContainer container, String name ) {

		Iterator<Component> iterator = container.getComponentIterator();

		while ( iterator.hasNext() ) {

			Component childComponent = iterator.next();

			// Drill into unnamed containers

			if ( childComponent.getDebugId() == null && childComponent instanceof ComponentContainer ) {
				childComponent = getComponent( (ComponentContainer) childComponent, name );

				if ( childComponent != null ) {

					return childComponent;
				}

				continue;
			}

			// Start by name

			if ( ( childComponent.getDebugId() != null ) && childComponent.getDebugId().startsWith( name ) && childComponent.getDebugId().substring( name.length() ).matches( "^\\d*$" ) ) {

				return childComponent;
			}
		}

		// Not found

		return null;
	}

	/**
	 * Storage area for WidgetProcessors, Layouts, and other stateless clients.
	 */

	public void putClientProperty( Object key, Object value ) {

		if ( mClientProperties == null ) {
			mClientProperties = new HashMap<Object, Object>();
		}

		mClientProperties.put( key, value );
	}

	/**
	 * Storage area for WidgetProcessors, Layouts, and other stateless clients.
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getClientProperty( Object key ) {

		if ( mClientProperties == null ) {
			return null;
		}

		return (T) mClientProperties.get( key );
	}

	//
	// Inner class
	//

	protected class Pipeline
		extends W3CPipeline<Component, ComponentContainer, VaadinMetawidget>
		implements Serializable {

		//
		// Protected methods
		//

		@Override
		protected VaadinMetawidget getPipelineOwner() {

			return VaadinMetawidget.this;
		}

		@Override
		protected String getDefaultConfiguration() {

			return VaadinMetawidget.this.getDefaultConfiguration();
		}

		@Override
		protected void startBuild() {

			VaadinMetawidget.this.startBuild();

			super.startBuild();
		}

		@Override
		protected void layoutWidget( Component component, String elementName,
				Map<String, String> attributes ) {

			VaadinMetawidget.this.layoutWidget( component, elementName, attributes );

			// Support null layouts

			if ( getLayout() == null ) {
				addComponent( component );
			} else {
				super.layoutWidget( component, elementName, attributes );
			}
		}

		@SuppressWarnings( "unchecked" )
		@Override
		protected Map<String, String> getAdditionalAttributes(
				Component component ) {

			if ( component instanceof AbstractComponent ) {

				Object attributes = ( (AbstractComponent) component ).getData();
				if ( attributes instanceof Map ) {
					return (Map<String, String>) attributes;
				}
			}

			return null;
		}

		@Override
		public VaadinMetawidget buildNestedMetawidget( Map<String, String> attributes )
			throws Exception {

			if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
				return null;
			}

			VaadinMetawidget nestedMetawidget = VaadinMetawidget.this.getClass().newInstance();
			VaadinMetawidget.this.initNestedMetawidget( nestedMetawidget, attributes );

			return nestedMetawidget;
		}

		@Override
		protected void endBuild() {

			VaadinMetawidget.this.endBuild();
			super.endBuild();
		}
	}

	/**
	 * Simple immutable structure to store a component and its value property.
	 *
	 * @author Richard Kennard
	 */

	private static class ComponentAndValueProperty
		implements Immutable {

		//
		// Private members
		//

		private Component	mComponent;

		private String		mValueProperty;

		//
		// Constructor
		//

		public ComponentAndValueProperty( Component component, String valueProperty ) {

			mComponent = component;
			mValueProperty = valueProperty;
		}

		//
		// Public methods
		//

		public Component getComponent() {

			return mComponent;
		}

		public String getValueProperty() {

			return mValueProperty;
		}
	}
}
