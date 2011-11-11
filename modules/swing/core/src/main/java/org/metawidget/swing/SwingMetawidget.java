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

package org.metawidget.swing;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.metawidget.config.ConfigReader;
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

/**
 * Metawidget for Swing environments.
 * 
 * @author Richard Kennard
 */

// Note: It would be nice for SwingMetawidget to extend AwtMetawidget, but we want
// SwingMetawidget to extend JComponent for various Swing-specific features like setBorder and
// setOpaque
//
public class SwingMetawidget
	extends JComponent {

	//
	// Private statics
	//

	private static final long			serialVersionUID	= 1l;

	private static final ConfigReader	CONFIG_READER		= new ConfigReader();

	private static final String			DEFAULT_CONFIG		= ClassUtils.getPackagesAsFolderNames( SwingMetawidget.class ) + "/metawidget-swing-default.xml";

	private static final Stroke			STROKE_DOTTED		= new BasicStroke( 1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0f, new float[] { 3f }, 0f );

	//
	// Private members
	//

	private Object						mToInspect;

	private String						mPath;

	private String						mConfig;

	private ResourceBundle				mBundle;

	private boolean						mNeedToBuildWidgets;

	private Element						mLastInspectionResult;

	private boolean						mIgnoreAddRemove;

	/**
	 * List of existing, manually added components.
	 * <p>
	 * This is a List, not a Set, so that mExistingUnusedComponents (which is initialized from it)
	 * is consistent.
	 */

	private List<JComponent>			mExistingComponents	= CollectionUtils.newArrayList();

	/**
	 * List of existing, manually added, but unused by Metawidget components.
	 * <p>
	 * This is a List, not a Set, for consistency during endBuild.
	 */

	private List<JComponent>			mExistingUnusedComponents;

	private Map<String, Facet>			mFacets				= CollectionUtils.newHashMap();

	/* package private */Pipeline		mPipeline;

	//
	// Constructor
	//

	public SwingMetawidget() {

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
	 * Updates the Object to inspect, without invalidating the previous inspection results.
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

		mConfig = config;
		mPipeline.setNeedsConfiguring();
		invalidateInspection();
	}

	public void setInspector( Inspector inspector ) {

		mPipeline.setInspector( inspector );
		invalidateInspection();
	}

	/**
	 * Useful for WidgetBuilders to perform nested inspections (eg. for Collections).
	 */

	public String inspect( Object toInspect, String type, String... names ) {

		return mPipeline.inspect( toInspect, type, names );
	}

	public void addInspectionResultProcessor( InspectionResultProcessor<SwingMetawidget> inspectionResultProcessor ) {

		mPipeline.addInspectionResultProcessor( inspectionResultProcessor );
		invalidateWidgets();
	}

	public void removeInspectionResultProcessor( InspectionResultProcessor<SwingMetawidget> inspectionResultProcessor ) {

		mPipeline.removeInspectionResultProcessor( inspectionResultProcessor );
		invalidateWidgets();
	}

	public void setInspectionResultProcessors( InspectionResultProcessor<SwingMetawidget>... inspectionResultProcessors ) {

		mPipeline.setInspectionResultProcessors( inspectionResultProcessors );
		invalidateWidgets();
	}

	public void setWidgetBuilder( WidgetBuilder<JComponent, SwingMetawidget> widgetBuilder ) {

		mPipeline.setWidgetBuilder( widgetBuilder );
		invalidateWidgets();
	}

	public void addWidgetProcessor( WidgetProcessor<JComponent, SwingMetawidget> widgetProcessor ) {

		mPipeline.addWidgetProcessor( widgetProcessor );
		invalidateWidgets();
	}

	public void removeWidgetProcessor( WidgetProcessor<JComponent, SwingMetawidget> widgetProcessor ) {

		mPipeline.removeWidgetProcessor( widgetProcessor );
		invalidateWidgets();
	}

	public void setWidgetProcessors( WidgetProcessor<JComponent, SwingMetawidget>... widgetProcessors ) {

		mPipeline.setWidgetProcessors( widgetProcessors );
		invalidateWidgets();
	}

	public <T> T getWidgetProcessor( Class<T> widgetProcessorClass ) {

		buildWidgets();
		return mPipeline.getWidgetProcessor( widgetProcessorClass );
	}

	/**
	 * Set the layout for this Metawidget.
	 * <p>
	 * Named <code>setMetawidgetLayout</code>, rather than the usual <code>setLayout</code>, because
	 * Swing already defines a <code>setLayout</code>. Overloading Swing's <code>setLayout</code>
	 * was considered cute, but ultimately confusing and dangerous. For example, what should
	 * <code>setLayout( null )</code> do?
	 */

	public void setMetawidgetLayout( Layout<JComponent, JComponent, SwingMetawidget> layout ) {

		mPipeline.setLayout( layout );
		invalidateWidgets();
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

	public boolean isReadOnly() {

		return mPipeline.isReadOnly();
	}

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
	 * Fetch a list of <code>JComponents</code> that were added manually, and have so far not been
	 * used.
	 * <p>
	 * <strong>This is an internal API exposed for OverriddenWidgetBuilder. Clients should not call
	 * it directly.</strong>
	 */

	public List<JComponent> fetchExistingUnusedComponents() {

		return mExistingUnusedComponents;
	}

	//
	// The following methods all kick off buildWidgets() if necessary
	//

	/**
	 * Overridden to build widgets just-in-time.
	 * <p>
	 * This is the first method a JFrame.pack calls.
	 */

	@Override
	public Dimension getPreferredSize() {

		buildWidgets();

		return super.getPreferredSize();
	}

	/**
	 * Overridden to build widgets just-in-time.
	 * <p>
	 * This is the first method a JTable.editCellAt calls.
	 */

	@Override
	public void setBounds( Rectangle rectangle ) {

		buildWidgets();

		super.setBounds( rectangle );
	}

	/**
	 * Overridden to build widgets just-in-time.
	 * <p>
	 * This is the first method a JComponent.paintChildren calls (this includes JDialog)
	 */

	@Override
	public Rectangle getBounds( Rectangle rectangle ) {

		buildWidgets();

		return super.getBounds( rectangle );
	}

	/**
	 * Overridden to build widgets just-in-time.
	 * <p>
	 * This method may be called by developers who wish to modify the created Components before they
	 * are displayed. For example, they may wish to call .setBorder( null ) if the component is to
	 * be used as a JTable CellEditor.
	 */

	@Override
	public Component getComponent( int index ) {

		buildWidgets();

		return super.getComponent( index );
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

		return super.getComponentCount();
	}

	/**
	 * Overridden to build widgets just-in-time.
	 * <p>
	 * This method may be called by developers who wish to test the SwingMetawidget's active
	 * LayoutManager.
	 */

	@Override
	public LayoutManager getLayout() {

		buildWidgets();

		return super.getLayout();
	}

	/**
	 * Overridden to build widgets just-in-time.
	 * <p>
	 * When adding a Stub that immediately stubs out a widget and therefore disappears from the
	 * component list, we must override <code>addNotify</code> to build widgets or else Swing gets
	 * confused trying to addNotify a component that isn't there.
	 * <p>
	 * See <code>SwingTutorialTest.testAddNotify</code>.
	 */

	@Override
	public void addNotify() {

		buildWidgets();

		super.addNotify();
	}

	/**
	 * Gets the value from the Component with the given name.
	 * <p>
	 * The value is returned as it was stored in the Component (eg. String for JTextField) so may
	 * need some conversion before being reapplied to the object being inspected. This obviously
	 * requires knowledge of which Component SwingMetawidget created, which is not ideal, so clients
	 * may prefer to use bindingClass instead.
	 * 
	 * @return the value. Note this return type uses generics, so as to not require a cast by the
	 *         caller (eg. <code>String s = getValue(names)</code>)
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getValue( String... names ) {

		ComponentAndValueProperty componentAndValueProperty = getComponentAndValueProperty( names );
		return (T) ClassUtils.getProperty( componentAndValueProperty.getComponent(), componentAndValueProperty.getValueProperty() );
	}

	/**
	 * Sets the Component with the given name to the specified value.
	 * <p>
	 * Clients must ensure the value is of the correct type to suit the Component (eg. String for
	 * JTextField). This obviously requires knowledge of which Component SwingMetawidget created,
	 * which is not ideal, so clients may prefer to use bindingClass instead.
	 */

	public void setValue( Object value, String... names ) {

		ComponentAndValueProperty componentAndValueProperty = getComponentAndValueProperty( names );
		ClassUtils.setProperty( componentAndValueProperty.getComponent(), componentAndValueProperty.getValueProperty(), value );
	}

	/**
	 * Returns the property used to get/set the value of the component.
	 * <p>
	 * If the component is not known, returns <code>null</code>. Does not throw an Exception, as we
	 * want to fail gracefully if, say, someone tries to bind to a JPanel.
	 */

	public String getValueProperty( Component component ) {

		return getValueProperty( component, mPipeline.getWidgetBuilder() );
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
			// SwingMetawidgetTest.testNestedWithManualInspector

			if ( topComponent instanceof SwingMetawidget ) {
				( (SwingMetawidget) topComponent ).buildWidgets();
			}

			// Try to find a component

			topComponent = getComponent( (Container) topComponent, name );

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
	public void remove( Component component ) {

		super.remove( component );

		if ( !mIgnoreAddRemove ) {
			invalidateWidgets();

			if ( component instanceof Facet ) {
				mFacets.remove( ( (Facet) component ).getName() );
			} else {
				mExistingComponents.remove( component );
			}
		}
	}

	@Override
	public void remove( int index ) {

		Component component = getComponent( index );

		// (don't be tempted to call remove( component ), as that may infinite
		// recurse on some JDK implementations)

		super.remove( index );

		if ( !mIgnoreAddRemove ) {
			invalidateWidgets();

			if ( component instanceof Facet ) {
				mFacets.remove( ( (Facet) component ).getName() );
			} else {
				mExistingComponents.remove( component );
			}
		}
	}

	@Override
	public void removeAll() {

		super.removeAll();

		if ( !mIgnoreAddRemove ) {
			invalidateWidgets();

			mFacets.clear();
			mExistingComponents.clear();
		}
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

	@Override
	protected void paintComponent( Graphics graphics ) {

		buildWidgets();

		super.paintComponent( graphics );

		// When used as part of an IDE builder tool, render as a dotted square so that we can see
		// something!

		if ( mPath == null && getComponentCount() == 0 ) {
			Graphics2D graphics2d = (Graphics2D) graphics;
			Stroke strokeBefore = graphics2d.getStroke();

			try {
				graphics2d.setStroke( STROKE_DOTTED );
				int height = getHeight();
				graphics2d.drawRect( 0, 0, getWidth() - 1, height - 1 );
				graphics2d.drawString( "Metawidget", 10, height / 2 );
			} finally {
				graphics2d.setStroke( strokeBefore );
			}
		}
	}

	/**
	 * Invalidates the current inspection result (if any) <em>and</em> invalidates the widgets.
	 * <p>
	 * As an optimisation we only invalidate the widgets, not the entire inspection result, for some
	 * operations (such as adding/removing stubs, changing read-only etc.)
	 */

	protected void invalidateInspection() {

		mLastInspectionResult = null;
		invalidateWidgets();
	}

	/**
	 * Invalidates the widgets.
	 */

	protected void invalidateWidgets() {

		if ( mNeedToBuildWidgets ) {
			return;
		}

		// Note: it is important to call removeAll BEFORE setting mNeedToBuildWidgets
		// to true. On some JRE implementations (ie. 1.6_12) removeAll triggers an
		// immediate repaint which sets mNeedToBuildWidgets back to false

		super.removeAll();

		// Prepare to build widgets

		mNeedToBuildWidgets = true;

		// Call repaint here, rather than just 'invalidate', for scenarios like doing
		// a 'remove' of a button that masks a Metawidget

		repaint();
	}

	@Override
	protected void addImpl( Component component, Object constraints, int index ) {

		if ( !mIgnoreAddRemove ) {
			invalidateWidgets();

			// Don't fall through to super.addImpl for facets. Tuck them away
			// in mFacets instead. Some layouts may never use them, and
			// others (eg. MigLayout) don't like adding components
			// without constraints

			if ( component instanceof Facet ) {
				mFacets.put( component.getName(), (Facet) component );
				return;
			}

			if ( component instanceof JComponent ) {
				mExistingComponents.add( (JComponent) component );
			}
		}

		super.addImpl( component, constraints, index );
	}

	protected void configure() {

		// Special support for visual IDE builders

		if ( mPath == null ) {

			mPipeline.setNeedsConfiguring();
			return;
		}

		try {
			if ( mConfig != null ) {
				CONFIG_READER.configure( mConfig, this );
			}

			// SwingMetawidget uses setMetawidgetLayout, not setLayout

			if ( mPipeline.getLayout() == null ) {
				CONFIG_READER.configure( DEFAULT_CONFIG, this, "metawidgetLayout" );
			}

			mPipeline.configureDefaults( CONFIG_READER, DEFAULT_CONFIG, SwingMetawidget.class );
		} catch ( Exception e ) {
			throw MetawidgetException.newException( e );
		}
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
			if ( mLastInspectionResult == null ) {
				mLastInspectionResult = inspect();
			}

			// Don't buildWidgets if null, in order to protect our 'dotted rectangle in IDE tools'
			// effect. However, do 'getComponentCount() > 0' in case the SwingMetawidget is being
			// used purely for layout purposes

			if ( mPath != null || getComponentCount() > 0 ) {
				mPipeline.buildWidgets( mLastInspectionResult );
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
	 *            XML node name of the business field. Typically 'entity', 'property' or 'action'.
	 *            Never null
	 */

	protected void layoutWidget( Component component, String elementName, Map<String, String> attributes ) {

		// Set the name of the component.
		//
		// If this is a JScrollPane, set the name of the top-level JScrollPane. Don't do this before
		// now, as we don't want binding/validation implementations accidentally relying on the
		// name being set (which it won't be for actualComponent)
		//
		// Note: we haven't split this out into a separate WidgetProcessor, because other methods
		// like getValue/setValue/getComponent( String... names ) rely on it

		component.setName( attributes.get( NAME ) );

		// Remove, then re-add to layout (to re-order the component)

		remove( component );

		// Look up any additional attributes

		Map<String, String> additionalAttributes = mPipeline.getAdditionalAttributes( (JComponent) component );

		if ( additionalAttributes != null ) {
			attributes.putAll( additionalAttributes );
		}

		// BasePipeline will call .layoutWidget
	}

	protected void endBuild() {

		if ( mExistingUnusedComponents != null ) {
			for ( JComponent componentExisting : mExistingUnusedComponents ) {
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

	protected void initNestedMetawidget( SwingMetawidget nestedMetawidget, Map<String, String> attributes ) {

		// Don't copy setConfig(). Instead, copy runtime values

		mPipeline.initNestedPipeline( nestedMetawidget.mPipeline, attributes );
		nestedMetawidget.setPath( mPath + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME ) );
		nestedMetawidget.setBundle( mBundle );
		nestedMetawidget.setOpaque( isOpaque() );
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

		// Drill into JScrollPanes

		if ( component instanceof JScrollPane ) {
			component = ( (JScrollPane) component ).getViewport().getView();
		}

		String componentProperty = getValueProperty( component );

		if ( componentProperty == null ) {
			throw MetawidgetException.newException( "Don't know how to getValue from a " + component.getClass().getName() );
		}

		return new ComponentAndValueProperty( component, componentProperty );
	}

	private String getValueProperty( Component component, WidgetBuilder<JComponent, SwingMetawidget> widgetBuilder ) {

		// Recurse into CompositeWidgetBuilders

		try {
			if ( widgetBuilder instanceof CompositeWidgetBuilder<?, ?> ) {
				for ( WidgetBuilder<JComponent, SwingMetawidget> widgetBuilderChild : ( (CompositeWidgetBuilder<JComponent, SwingMetawidget>) widgetBuilder ).getWidgetBuilders() ) {
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

		if ( widgetBuilder instanceof SwingValuePropertyProvider ) {
			return ( (SwingValuePropertyProvider) widgetBuilder ).getValueProperty( component );
		}

		return null;
	}

	private Component getComponent( Container container, String name ) {

		for ( Component childComponent : container.getComponents() ) {
			// Drill into unnamed containers (ie. for TabbedPanes)

			if ( childComponent.getName() == null && childComponent instanceof Container ) {
				childComponent = getComponent( (Container) childComponent, name );

				if ( childComponent != null ) {
					return childComponent;
				}

				continue;
			}

			// Match by name

			if ( name.equals( childComponent.getName() ) ) {
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
		extends W3CPipeline<JComponent, JComponent, SwingMetawidget> {

		//
		// Protected methods
		//

		@Override
		protected void configure() {

			SwingMetawidget.this.configure();
		}

		@Override
		protected void startBuild() {

			super.startBuild();
			SwingMetawidget.this.startBuild();
		}

		@Override
		protected void layoutWidget( JComponent component, String elementName, Map<String, String> attributes ) {

			SwingMetawidget.this.layoutWidget( component, elementName, attributes );

			// Support null layouts

			if ( getLayout() == null ) {
				add( component );
			} else {
				super.layoutWidget( component, elementName, attributes );
			}

			// If the component is itself a SwingMetawidget, build it immediately. This:
			//
			// 1. stops an addNotify problem if OverriddenWidgetBuilder steals a component from its
			// parent
			// 2. stops us having to check for (name == null) before calling component.setName in
			// layoutWidget (because the component will already have been stolen if needed)
			// 3. means the endBuild of the parent SwingMetawidget gets called *after* the endBuild
			// of the nested SwingMetawidget. This makes more sense, as otherwise the nested
			// SwingMetawidget waits until being asked to paint, which is after the endBuild of the
			// parent

			if ( component instanceof SwingMetawidget ) {
				( (SwingMetawidget) component ).buildWidgets();
			}
		}

		@Override
		protected Map<String, String> getAdditionalAttributes( JComponent component ) {

			if ( component instanceof Stub ) {
				return ( (Stub) component ).getAttributes();
			}

			return null;
		}

		@Override
		public SwingMetawidget buildNestedMetawidget( Map<String, String> attributes )
			throws Exception {

			SwingMetawidget nestedMetawidget = SwingMetawidget.this.getClass().newInstance();
			SwingMetawidget.this.initNestedMetawidget( nestedMetawidget, attributes );

			return nestedMetawidget;
		}

		@Override
		protected void endBuild() {

			SwingMetawidget.this.endBuild();
			super.endBuild();

			// Call validate because Components have been added/removed, and
			// Component layout information has changed. Must do this after
			// super.endBuild, because that is what calls layout.endBuild

			validate();
		}

		@Override
		protected SwingMetawidget getPipelineOwner() {

			return SwingMetawidget.this;
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
