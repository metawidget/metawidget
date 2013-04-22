// Metawidget (licensed under LGPL)
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

package org.metawidget.vaadin.ui;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.w3c.dom.Element;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Panel;

/**
 * Metawidget for Vaadin environments.
 *
 * @author Loghman Barari
 */

public class VaadinMetawidget
	extends CustomComponent
	implements ComponentContainer {

	//
	// Private members
	//

	private Object					mToInspect;

	private String					mPath;

	private ResourceBundle			mBundle;

	private boolean					mNeedToBuildWidgets;

	private Element					mLastInspection;

	private boolean					mIgnoreAddRemove;

	private List<Component>			mComponents = CollectionUtils.newArrayList();

	/**
	 * List of existing, manually added components.
	 * <p>
	 * This is a List, not a Set, so that mExistingUnusedComponents (which is initialized from it)
	 * is consistent.
	 */

	private List<AbstractComponent>	mExistingComponents	= CollectionUtils.newArrayList();

	/**
	 * List of existing, manually added, but unused by Metawidget components.
	 * <p>
	 * This is a List, not a Set, for consistency during endBuild.
	 */

	private List<AbstractComponent>	mExistingUnusedComponents;

	private Map<Object, Facet>		mFacets				= CollectionUtils.newHashMap();

	private Map<Object, Object>		mClientProperties;

	/* package private */Pipeline	mPipeline;

	//
	// Constructor
	//

	public VaadinMetawidget() {

		mPipeline = newPipeline();
	}

	//
	// Public methods
	//

	/**
	 * Sets the Object to inspect.
	 * <p>
	 * If <code>setPath</code> has not been set, or points to a previous <code>setToInspect</code>,
	 * sets it to point to the given Object.
	 */

	public void setToInspect( Object toInspect ) {

		updateToInspectWithoutInvalidate( toInspect );
		invalidateInspection();
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

	public void addInspectionResultProcessor( InspectionResultProcessor<VaadinMetawidget> inspectionResultProcessor ) {

		mPipeline.addInspectionResultProcessor( inspectionResultProcessor );
		invalidateInspection();
	}

	public void removeInspectionResultProcessor( InspectionResultProcessor<VaadinMetawidget> inspectionResultProcessor ) {

		mPipeline.removeInspectionResultProcessor( inspectionResultProcessor );
		invalidateInspection();
	}

	public void setInspectionResultProcessors( InspectionResultProcessor<VaadinMetawidget>... inspectionResultProcessors ) {

		mPipeline.setInspectionResultProcessors( inspectionResultProcessors );
		invalidateInspection();
	}

	public void setWidgetBuilder( WidgetBuilder<Component, VaadinMetawidget> widgetBuilder ) {

		mPipeline.setWidgetBuilder( widgetBuilder );
		invalidateWidgets();
	}

	public void addWidgetProcessor( WidgetProcessor<Component, VaadinMetawidget> widgetProcessor ) {

		mPipeline.addWidgetProcessor( widgetProcessor );
		invalidateWidgets();
	}

	public void removeWidgetProcessor( WidgetProcessor<Component, VaadinMetawidget> widgetProcessor ) {

		mPipeline.removeWidgetProcessor( widgetProcessor );
		invalidateWidgets();
	}

	public void setWidgetProcessors( WidgetProcessor<Component, VaadinMetawidget>... widgetProcessors ) {

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

	public void setLayout( Layout<Component, ComponentContainer, VaadinMetawidget> layout ) {

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

	/**
	 * Returns a label for the given set of attributes.
	 * <p>
	 * The label is determined using the following algorithm:
	 * <p>
	 * <ul>
	 * <li>if <tt>attributes.get( "label" )</tt> exists...
	 * <ul>
	 * <li><tt>attributes.get( "label" )</tt> is camel-cased and used as a lookup into
	 * <tt>getLocalizedKey( camelCasedLabel )</tt>. This means developers can initially build their
	 * UIs without worrying about localization, then turn it on later</li>
	 * <li>if no such lookup exists, return <tt>attributes.get( "label" )</tt>
	 * </ul>
	 * </li>
	 * <li>if <tt>attributes.get( "label" )</tt> does not exist...
	 * <ul>
	 * <li><tt>attributes.get( "name" )</tt> is used as a lookup into
	 * <tt>getLocalizedKey( name )</tt></li>
	 * <li>if no such lookup exists, return <tt>attributes.get( "name" )</tt>
	 * </ul>
	 * </li>
	 * </ul>
	 */

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

	public List<AbstractComponent> fetchExistingUnusedComponents() {

		return mExistingUnusedComponents;
	}

	//
	// The following methods all kick off buildWidgets() if necessary
	//

	@Override
	public int getComponentCount() {

		buildWidgets();
		return mComponents.size();
	}

	@Override
	public Iterator<Component> iterator() {

		buildWidgets();
		return mComponents.iterator();
	}

	/**
	 * Finds the Component with the given name.
	 */

	@SuppressWarnings( "unchecked" )
	public <T extends Component> T getComponent( String... names ) {

		if ( names == null || names.length == 0 ) {
			return null;
		}

		Component topComponent = this;

		for ( int loop = 0, length = names.length; loop < length; loop++ ) {
			String name = names[loop];

			// May need building 'just in time' if we are calling getComponent
			// immediately after a 'setToInspect'. See
			// VaadinMetawidgetTest.testNestedWithManualInspector

			if ( topComponent instanceof VaadinMetawidget ) {
				( (VaadinMetawidget) topComponent ).buildWidgets();
			}

			// Try to find a component

			if ( topComponent instanceof HasComponents ) {
				topComponent = getComponent( (HasComponents) topComponent, name );
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

	/**
	 * Named after <code>Panel.getContent</code>.
	 */

	@SuppressWarnings( "unchecked" )
	public <C extends Component> C getContent() {

		buildWidgets();

		// Simulate getCompositionRoot from Vaadin 6

		if ( mComponents.isEmpty() ) {
			return null;
		}

		return (C) mComponents.get( 0 );
	}

	@Override
	public void addComponent( Component component ) {

		if ( !mIgnoreAddRemove ) {
			invalidateWidgets();

			// Don't fall through to super.addImpl for facets. Tuck them away
			// in mFacets instead. Some layouts may never use them, and
			// others (eg. MigLayout) don't like adding components
			// without constraints

			AbstractComponent abstractComponent = (AbstractComponent) component;

			if ( component instanceof Facet ) {
				mFacets.put( abstractComponent.getData(), (Facet) component );
				return;
			}

			mExistingComponents.add( abstractComponent );
		} else {

			// Do clear so that we simulate setCompositionRoot

			mComponents.clear();
			mComponents.add( component );
			component.setParent( this );
		}
	}

	@Override
	public void removeComponent( Component component ) {

		if ( !mIgnoreAddRemove ) {
			invalidateWidgets();

			if ( component instanceof Facet ) {
				mFacets.remove( ( (Facet) component ).getData() );
			} else {
				mExistingComponents.remove( component );
			}
		}
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

		// Prepare to build widgets

		this.mNeedToBuildWidgets = true;

		if ( this.mClientProperties != null ) {
			this.mClientProperties.clear();
		}

		// Call repaint here, rather than just 'invalidate', for scenarios like
		// doing a 'remove' of a button that masks a Metawidget

		markAsDirty();
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

	protected void layoutWidget( Component component, String elementName, Map<String, String> attributes ) {

		// Set the name of the component.
		//
		// Note: we haven't split this out into a separate WidgetProcessor, because other methods
		// like getValue/setValue/getComponent( String... names ) rely on it

		( (AbstractComponent) component ).setData( attributes.get( NAME ) );

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
		nestedMetawidget.setBundle( mBundle );
		nestedMetawidget.setToInspect( mToInspect );
	}

	//
	// Private methods
	//

	/**
	 * Updates the Object to inspect, without invalidating the previous
	 * inspection results.
	 */

	private void updateToInspectWithoutInvalidate( Object toInspect ) {

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

	private Element inspect() {

		if ( mPath == null ) {
			return null;
		}

		TypeAndNames typeAndNames = PathUtils.parsePath( mPath );

		return mPipeline.inspectAsDom( mToInspect, typeAndNames.getType(), typeAndNames.getNamesAsArray() );
	}

	private Component getComponent( HasComponents container, String name ) {

		Iterator<Component> iterator = container.iterator();

		while ( iterator.hasNext() ) {

			AbstractComponent childComponent = (AbstractComponent) iterator.next();

			// Drill into unnamed containers

			if ( childComponent.getData() == null && childComponent instanceof Panel ) {
				childComponent = (AbstractComponent) getComponent( (HasComponents) ( (Panel) childComponent ).getContent(), name );

				if ( childComponent != null ) {
					return childComponent;
				}

				continue;
			}

			if ( childComponent.getData() == null && childComponent instanceof HasComponents ) {
				childComponent = (AbstractComponent) getComponent( (HasComponents) childComponent, name );

				if ( childComponent != null ) {
					return childComponent;
				}

				continue;
			}

			// Match by name

			if ( name.equals( childComponent.getData() ) ) {
				return childComponent;
			}
		}

		// Not found

		return null;
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
		protected void layoutWidget( Component component, String elementName, Map<String, String> attributes ) {

			VaadinMetawidget.this.layoutWidget( component, elementName, attributes );
			super.layoutWidget( component, elementName, attributes );
		}

		@Override
		protected Map<String, String> getAdditionalAttributes( Component component ) {

			if ( component instanceof Stub ) {
				return ( (Stub) component ).getAttributes();
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

	@Override
	public void addComponentAttachListener( ComponentAttachListener listener ) {

		// TODO Auto-generated method stub

	}

	@Override
	public void removeComponentAttachListener( ComponentAttachListener listener ) {

		// TODO Auto-generated method stub

	}

	@Override
	public void addComponentDetachListener( ComponentDetachListener listener ) {

		// TODO Auto-generated method stub

	}

	@Override
	public void removeComponentDetachListener( ComponentDetachListener listener ) {

		// TODO Auto-generated method stub

	}

	@Override
	public void addComponents( Component... components ) {

		for( Component component : components ) {
			addComponent( component );
		}
	}

	@Override
	public void removeAllComponents() {

		mComponents.clear();
	}

	@Override
	public void replaceComponent( Component oldComponent, Component newComponent ) {

		// TODO Auto-generated method stub

	}

	@Override
	@Deprecated
	public Iterator<Component> getComponentIterator() {

		return mComponents.iterator();
	}

	@Override
	public void moveComponentsFrom( ComponentContainer source ) {

		// TODO Auto-generated method stub

	}

	@Override
	@Deprecated
	public void addListener( ComponentAttachListener listener ) {

		// TODO Auto-generated method stub

	}

	@Override
	@Deprecated
	public void removeListener( ComponentAttachListener listener ) {

		// TODO Auto-generated method stub

	}

	@Override
	@Deprecated
	public void addListener( ComponentDetachListener listener ) {

		// TODO Auto-generated method stub

	}

	@Override
	@Deprecated
	public void removeListener( ComponentDetachListener listener ) {

		// TODO Auto-generated method stub

	}
}
