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

import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspector.ConfigReader;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.layout.iface.Layout;
import org.metawidget.mixin.w3c.MetawidgetMixin;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.util.simple.PathUtils.TypeAndNames;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

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
	extends JComponent
{
	//
	// Private statics
	//

	private final static long			serialVersionUID	= 1l;

	private final static ConfigReader	CONFIG_READER		= new ConfigReader();

	private final static String			DEFAULT_CONFIG		= "org/metawidget/swing/metawidget-swing-default.xml";

	private final static Stroke			STROKE_DOTTED		= new BasicStroke( 1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0f, new float[] { 3f }, 0f );

	//
	// Private members
	//

	private Object						mToInspect;

	private String						mPath;

	private String						mConfig;

	private boolean						mNeedsConfiguring	= true;

	private ResourceBundle				mBundle;

	private Map<String, Object>			mParameters;

	private boolean						mNeedToBuildWidgets;

	private String						mLastInspection;

	private boolean						mIgnoreAddRemove;

	/**
	 * List of existing, manually added components.
	 * <p>
	 * This is a List, not a Set, for consistency in unit tests.
	 */

	private List<JComponent>			mExistingComponents	= CollectionUtils.newArrayList();

	/**
	 * List of existing, manually added, but unused by Metawidget components.
	 * <p>
	 * This is a List, not a Set, for consistency in unit tests.
	 */

	private List<JComponent>			mExistingComponentsUnused;

	private Map<String, Facet>			mFacets				= CollectionUtils.newHashMap();

	private SwingMetawidgetMixin		mMetawidgetMixin;

	//
	// Constructor
	//

	public SwingMetawidget()
	{
		mMetawidgetMixin = newMetawidgetMixin();
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

	public void setToInspect( Object toInspect )
	{
		if ( mToInspect == null )
		{
			if ( mPath == null && toInspect != null )
				mPath = ClassUtils.getUnproxiedClass( toInspect.getClass() ).getName();
		}
		else if ( ClassUtils.getUnproxiedClass( mToInspect.getClass() ).getName().equals( mPath ) )
		{
			if ( toInspect == null )
				mPath = null;
			else
				mPath = ClassUtils.getUnproxiedClass( toInspect.getClass() ).getName();
		}

		mToInspect = toInspect;
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
	public <T> T getToInspect()
	{
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

	public void setPath( String path )
	{
		mPath = path;
		invalidateInspection();
	}

	public String getPath()
	{
		return mPath;
	}

	public void setConfig( String inspectorConfig )
	{
		mConfig = inspectorConfig;
		mNeedsConfiguring = true;
		invalidateInspection();
	}

	public void setInspector( Inspector inspector )
	{
		mMetawidgetMixin.setInspector( inspector );
		invalidateInspection();
	}

	public void setWidgetBuilder( WidgetBuilder<JComponent, SwingMetawidget> widgetBuilder )
	{
		mMetawidgetMixin.setWidgetBuilder( widgetBuilder );
		invalidateInspection();
	}

	public void addWidgetProcessor( WidgetProcessor<JComponent, SwingMetawidget> widgetProcessor )
	{
		mMetawidgetMixin.addWidgetProcessor( widgetProcessor );
		invalidateInspection();
	}

	public void setWidgetProcessors( List<WidgetProcessor<JComponent, SwingMetawidget>> widgetProcessors )
	{
		mMetawidgetMixin.setWidgetProcessors( widgetProcessors );
		invalidateInspection();
	}

	public <T> T getWidgetProcessor( Class<T> widgetProcessorClass )
	{
		return mMetawidgetMixin.getWidgetProcessor( widgetProcessorClass );
	}

	/**
	 * Set the layout for this Metawidget.
	 * <p>
	 * Named <code>setMetawidgetLayout</code>, rather than the usual <code>setLayout</code>, because
	 * Swing already defines a <code>setLayout</code>. Overloading Swing's <code>setLayout</code>
	 * was considered cute, but ultimately confusing and dangerous. For example, what should
	 * <code>setLayout( null )</code> do?
	 */

	public void setMetawidgetLayout( Layout<JComponent, SwingMetawidget> layout )
	{
		mMetawidgetMixin.setLayout( layout );
		invalidateInspection();
	}

	public void setBundle( ResourceBundle bundle )
	{
		mBundle = bundle;
		invalidateWidgets();
	}

	public String getLabelString( Map<String, String> attributes )
	{
		if ( attributes == null )
			return "";

		// Explicit label

		String label = attributes.get( LABEL );

		if ( label != null )
		{
			// (may be forced blank)

			if ( "".equals( label ) )
				return null;

			// (localize if possible)

			String localized = getLocalizedKey( StringUtils.camelCase( label ) );

			if ( localized != null )
				return localized.trim();

			return label.trim();
		}

		// Default name

		String name = attributes.get( NAME );

		if ( name != null )
		{
			// (localize if possible)

			String localized = getLocalizedKey( name );

			if ( localized != null )
				return localized.trim();

			return StringUtils.uncamelCase( name );
		}

		return "";
	}

	/**
	 * @return null if no bundle, ???key??? if bundle is missing a key
	 */

	public String getLocalizedKey( String key )
	{
		if ( mBundle == null )
			return null;

		try
		{
			return mBundle.getString( key );
		}
		catch ( MissingResourceException e )
		{
			return StringUtils.RESOURCE_KEY_NOT_FOUND_PREFIX + key + StringUtils.RESOURCE_KEY_NOT_FOUND_SUFFIX;
		}
	}

	public Map<String, Object> getParameters()
	{
		return mParameters;
	}

	/**
	 * Sets parameters to pass to Layout/PropertyBinding implementations.
	 * <p>
	 * Exposed for GUI builders.
	 */

	public void setParameters( Map<String, Object> parameters )
	{
		mParameters = parameters;
	}

	/**
	 * Gets the parameter value. Used by the chosen <code>Layout</code> or
	 * <code>PropertyBinding</code> implementation.
	 *
	 * @return the value. Note this return type uses generics, so as to not require a cast by the
	 *         caller (eg. <code>String s = getParameter(name)</code>)
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getParameter( String name )
	{
		if ( mParameters == null )
			return null;

		return (T) mParameters.get( name );
	}

	/**
	 * Gets the parameter value. Used by the chosen <code>Layout</code> or
	 * <code>PropertyBinding</code> implementation.
	 * <p>
	 * Convenience method. Equivalent to <code>getParameter( clazz.getName() )</code>
	 *
	 * @return the value. Note this return type uses generics, so as to not require a cast by the
	 *         caller (eg. <code>String s = getParameter(clazz)</code>)
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getParameter( Class<?> clazz )
	{
		return (T) getParameter( clazz.getName() );
	}

	/**
	 * Sets the parameter value. Parameters are passed to the chosen <code>Layout</code> or
	 * <code>PropertyBinding</code> implementation.
	 */

	public void setParameter( String name, Object value )
	{
		if ( mParameters == null )
			mParameters = CollectionUtils.newHashMap();

		mParameters.put( name, value );

		invalidateWidgets();
	}

	/**
	 * Sets the parameter value. Parameters are passed to the chosen <code>Layout</code> or
	 * <code>PropertyBinding</code> implementation.
	 * <p>
	 * Convenience method. Equivalent to <code>setParameter( clazz.getName(), value )</code>
	 */

	public void setParameter( Class<?> clazz, Object value )
	{
		setParameter( clazz.getName(), value );
	}

	public boolean isReadOnly()
	{
		return mMetawidgetMixin.isReadOnly();
	}

	public void setReadOnly( boolean readOnly )
	{
		if ( mMetawidgetMixin.isReadOnly() == readOnly )
			return;

		mMetawidgetMixin.setReadOnly( readOnly );
		invalidateWidgets();
	}

	public int getMaximumInspectionDepth()
	{
		return mMetawidgetMixin.getMaximumInspectionDepth();
	}

	public void setMaximumInspectionDepth( int maximumInspectionDepth )
	{
		mMetawidgetMixin.setMaximumInspectionDepth( maximumInspectionDepth );
		invalidateWidgets();
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
	public Dimension getPreferredSize()
	{
		buildWidgets();

		return super.getPreferredSize();
	}

	/**
	 * Overridden to build widgets just-in-time.
	 * <p>
	 * This is the first method a JTable.editCellAt calls.
	 */

	@Override
	public void setBounds( Rectangle rectangle )
	{
		buildWidgets();

		super.setBounds( rectangle );
	}

	/**
	 * Overridden to build widgets just-in-time.
	 * <p>
	 * This is the first method a JComponent.paintChildren calls (this includes JDialog)
	 */

	@Override
	public Rectangle getBounds( Rectangle rectangle )
	{
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
	public Component getComponent( int index )
	{
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
	public int getComponentCount()
	{
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
	public LayoutManager getLayout()
	{
		buildWidgets();

		return super.getLayout();
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
	public <T> T getValue( String... names )
	{
		Component component = getComponent( names );

		if ( component == null )
			throw MetawidgetException.newException( "No component named '" + ArrayUtils.toString( names, "', '" ) + "'" );

		// Drill into JScrollPanes

		if ( component instanceof JScrollPane )
			component = ( (JScrollPane) component ).getViewport().getView();

		String componentProperty = getValueProperty( component );

		if ( componentProperty == null )
			throw MetawidgetException.newException( "Don't know how to getValue from a " + component.getClass().getName() );

		return (T) ClassUtils.getProperty( component, componentProperty );
	}

	/**
	 * Sets the Component with the given name to the specified value.
	 * <p>
	 * Clients must ensure the value is of the correct type to suit the Component (eg. String for
	 * JTextField). This obviously requires knowledge of which Component SwingMetawidget created,
	 * which is not ideal, so clients may prefer to use bindingClass instead.
	 */

	public void setValue( Object value, String... names )
	{
		Component component = getComponent( names );

		if ( component == null )
			throw MetawidgetException.newException( "No component named '" + ArrayUtils.toString( names, "', '" ) + "'" );

		// Drill into JScrollPanes

		if ( component instanceof JScrollPane )
			component = ( (JScrollPane) component ).getViewport().getView();

		String componentProperty = getValueProperty( component );

		if ( componentProperty == null )
			throw MetawidgetException.newException( "Don't know how to getValue from a " + component.getClass().getName() );

		ClassUtils.setProperty( component, componentProperty, value );
	}

	/**
	 * Returns the property used to get/set the value of the component.
	 * <p>
	 * If the component is not known, returns <code>null</code>. Does not throw an Exception, as we
	 * want to fail gracefully if, say, someone tries to bind to a JPanel.
	 */

	public String getValueProperty( Component component )
	{
		return getValueProperty( component, mMetawidgetMixin.getWidgetBuilder() );
	}

	/**
	 * Finds the Component with the given name.
	 */

	@SuppressWarnings( "unchecked" )
	public <T extends Component> T getComponent( String... names )
	{
		if ( names == null || names.length == 0 )
			return null;

		Component topComponent = this;

		for ( int loop = 0, length = names.length; loop < length; loop++ )
		{
			String name = names[loop];

			// May need building 'just in time' if we are calling getComponent
			// immediately after a 'setToInspect'. See
			// SwingMetawidgetTest.testNestedWithManualInspector

			if ( topComponent instanceof SwingMetawidget )
				( (SwingMetawidget) topComponent ).buildWidgets();

			// Try to find a component...

			Component[] children = ( (Container) topComponent ).getComponents();
			topComponent = null;

			for ( Component childComponent : children )
			{
				// ...with the name we're interested in

				if ( name.equals( childComponent.getName() ) )
				{
					topComponent = childComponent;
					break;
				}
			}

			if ( loop == length - 1 )
				return (T) topComponent;

			if ( topComponent == null )
				throw MetawidgetException.newException( "No such component '" + name + "' of '" + ArrayUtils.toString( names, "', '" ) + "'" );

			if ( !( topComponent instanceof Container ) )
				throw MetawidgetException.newException( "'" + name + "' is not a Container" );
		}

		return (T) topComponent;
	}

	public Facet getFacet( String name )
	{
		buildWidgets();

		return mFacets.get( name );
	}

	@Override
	public void remove( Component component )
	{
		super.remove( component );

		if ( !mIgnoreAddRemove )
		{
			invalidateWidgets();

			if ( component instanceof Facet )
			{
				mFacets.remove( ( (Facet) component ).getName() );
			}
			else
			{
				mExistingComponents.remove( component );
			}
		}
	}

	@Override
	public void remove( int index )
	{
		Component component = getComponent( index );

		// (don't be tempted to call remove( component ), as that may infinite
		// recurse on some JDK implementations)

		super.remove( index );

		if ( !mIgnoreAddRemove )
		{
			invalidateWidgets();

			if ( component instanceof Facet )
			{
				mFacets.remove( ( (Facet) component ).getName() );
			}
			else
			{
				mExistingComponents.remove( component );
			}
		}
	}

	@Override
	public void removeAll()
	{
		super.removeAll();

		if ( !mIgnoreAddRemove )
		{
			invalidateWidgets();

			mFacets.clear();
			mExistingComponents.clear();
		}
	}

	//
	// Protected methods
	//

	/**
	 * Instantiate the MetawidgetMixin used by this Metawidget.
	 * <p>
	 * Subclasses wishing to use their own MetawidgetMixin should override this method to
	 * instantiate their version.
	 */

	protected SwingMetawidgetMixin newMetawidgetMixin()
	{
		return new SwingMetawidgetMixin();
	}

	protected SwingMetawidgetMixin getMetawidgetMixin()
	{
		return mMetawidgetMixin;
	}

	@Override
	protected void paintComponent( Graphics graphics )
	{
		buildWidgets();

		super.paintComponent( graphics );

		// When used as part of an IDE builder tool, render as a dotted square so that we can see
		// something!

		if ( mPath == null && getComponentCount() == 0 )
		{
			Graphics2D graphics2d = (Graphics2D) graphics;
			Stroke strokeBefore = graphics2d.getStroke();

			try
			{
				graphics2d.setStroke( STROKE_DOTTED );
				int height = getHeight();
				graphics2d.drawRect( 0, 0, getWidth() - 1, height - 1 );
				graphics2d.drawString( "Metawidget", 10, height / 2 );
			}
			finally
			{
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

	protected void invalidateInspection()
	{
		mLastInspection = null;
		invalidateWidgets();
	}

	/**
	 * Invalidates the widgets.
	 */

	protected void invalidateWidgets()
	{
		if ( mNeedToBuildWidgets )
			return;

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
	protected void addImpl( Component component, Object constraints, int index )
	{
		if ( !mIgnoreAddRemove )
		{
			invalidateWidgets();

			// Don't fall through to super.addImpl for facets. Tuck them away
			// in mFacets instead. Some layouts may never use them, and
			// others (eg. MigLayout) don't like adding components
			// without constraints

			if ( component instanceof Facet )
			{
				mFacets.put( component.getName(), (Facet) component );
				return;
			}

			if ( component instanceof JComponent )
				mExistingComponents.add( (JComponent) component );
		}

		super.addImpl( component, constraints, index );
	}

	protected void configure()
	{
		if ( !mNeedsConfiguring )
			return;

		// Special support for visual IDE builders

		if ( mPath == null )
			return;

		mNeedsConfiguring = false;

		try
		{
			if ( mConfig != null )
				CONFIG_READER.configure( mConfig, this );

			mMetawidgetMixin.configureDefaults( CONFIG_READER, DEFAULT_CONFIG, SwingMetawidget.class );

			// SwingMetawidget uses setMetawidgetLayout, not setLayout

			if ( mMetawidgetMixin.getLayout() == null )
			{
				SwingMetawidget dummyMetawidget = CONFIG_READER.configure( DEFAULT_CONFIG, SwingMetawidget.class, "metawidgetLayout" );
				mMetawidgetMixin.setLayout( dummyMetawidget.getMetawidgetMixin().getLayout() );
			}

		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
	}

	protected void buildWidgets()
	{
		// No need to build?

		if ( !mNeedToBuildWidgets )
			return;

		configure();

		mNeedToBuildWidgets = false;
		mIgnoreAddRemove = true;

		try
		{
			if ( mLastInspection == null )
				mLastInspection = inspect();

			// Don't buildWidgets if null, in order to protect our 'dotted rectangle in IDE tools'
			// effect. However, do 'getComponentCount() > 0' in case the SwingMetawidget is being
			// used purely for layout purposes

			if ( mPath != null || getComponentCount() > 0 )
				mMetawidgetMixin.buildWidgets( mLastInspection );
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
		finally
		{
			mIgnoreAddRemove = false;
		}
	}

	protected void startBuild()
		throws Exception
	{
		mExistingComponentsUnused = CollectionUtils.newArrayList( mExistingComponents );
	}

	protected void addWidget( Component component, String elementName, Map<String, String> attributes )
		throws Exception
	{
		// Drill into JScrollPanes

		Component actualComponent = component;

		if ( actualComponent instanceof JScrollPane )
			actualComponent = ( (JScrollPane) actualComponent ).getViewport().getView();

		// Set the name of the component.
		//
		// If this is a JScrollPane, set the name of the top-level JScrollPane. Don't do this before
		// now, as we don't want binding/validation implementations accidentally relying on the
		// name being set (which it won't be for actualComponent)

		component.setName( attributes.get( NAME ) );

		// Remove, then re-add to layout (to re-order the component)

		remove( component );

		if ( mMetawidgetMixin.getLayout() == null )
		{
			// Support null layouts

			add( component );
		}
		else
		{
			if ( component instanceof Stub )
				attributes.putAll( ( (Stub) component ).getAttributes() );
		}

		// MetawidgetMixin will call .layoutChild
	}

	protected JComponent getOverriddenWidget( String elementName, Map<String, String> attributes )
	{
		String name = attributes.get( NAME );

		if ( name == null )
			return null;

		Component component = null;

		for ( Component componentExisting : mExistingComponentsUnused )
		{
			if ( name.equals( componentExisting.getName() ) )
			{
				component = componentExisting;
				break;
			}
		}

		if ( component != null )
			mExistingComponentsUnused.remove( component );

		return (JComponent) component;
	}

	protected void endBuild()
	{
		if ( mExistingComponentsUnused != null )
		{
			Layout<JComponent, SwingMetawidget> layout = mMetawidgetMixin.getLayout();

			for ( JComponent componentExisting : mExistingComponentsUnused )
			{
				// Unused facets don't count

				if ( componentExisting instanceof Facet )
					continue;

				Map<String, String> miscAttributes = CollectionUtils.newHashMap();

				// Manually created components default to no section

				miscAttributes.put( SECTION, "" );

				if ( componentExisting instanceof Stub )
					miscAttributes.putAll( ( (Stub) componentExisting ).getAttributes() );

				if ( layout == null )
					add( componentExisting );
				else
					layout.layoutChild( componentExisting, miscAttributes, this );
			}
		}

		// Call validate because Components have been added/removed, and
		// Component layout information has changed

		validate();
	}

	protected String inspect()
	{
		if ( mPath == null )
			return null;

		TypeAndNames typeAndNames = PathUtils.parsePath( mPath );
		return mMetawidgetMixin.inspect( mToInspect, typeAndNames.getType(), typeAndNames.getNamesAsArray() );
	}

	protected void initNestedMetawidget( SwingMetawidget nestedMetawidget, Map<String, String> attributes )
	{
		// Don't copy setConfig(). Instead, copy runtime values

		mMetawidgetMixin.initNestedMixin( nestedMetawidget.mMetawidgetMixin, attributes );
		nestedMetawidget.setPath( mPath + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME ) );
		nestedMetawidget.setBundle( mBundle );
		nestedMetawidget.setOpaque( isOpaque() );

		if ( mParameters != null )
			nestedMetawidget.setParameters( CollectionUtils.newHashMap( mParameters ) );

		nestedMetawidget.setToInspect( mToInspect );
	}

	//
	// Private methods
	//

	private String getValueProperty( Component component, WidgetBuilder<JComponent, SwingMetawidget> widgetBuilder )
	{
		// Recurse into CompositeWidgetBuilders

		if ( widgetBuilder instanceof CompositeWidgetBuilder )
		{
			for ( WidgetBuilder<JComponent, SwingMetawidget> widgetBuilderChild : ( (CompositeWidgetBuilder<JComponent, SwingMetawidget>) widgetBuilder ).getWidgetBuilders() )
			{
				String valueProperty = getValueProperty( component, widgetBuilderChild );

				if ( valueProperty != null )
					return valueProperty;
			}

			return null;
		}

		// Interrogate ValuePropertyProviders

		if ( widgetBuilder instanceof SwingValuePropertyProvider )
			return ( (SwingValuePropertyProvider) widgetBuilder ).getValueProperty( component );

		return null;
	}

	//
	// Inner class
	//

	protected class SwingMetawidgetMixin
		extends MetawidgetMixin<JComponent, SwingMetawidget>
	{
		//
		// Protected methods
		//

		@Override
		protected void startBuild()
			throws Exception
		{
			super.startBuild();
			SwingMetawidget.this.startBuild();
		}

		@Override
		protected void addWidget( JComponent component, String elementName, Map<String, String> attributes )
			throws Exception
		{
			SwingMetawidget.this.addWidget( component, elementName, attributes );
			super.addWidget( component, elementName, attributes );
		}

		@Override
		protected JComponent getOverriddenWidget( String elementName, Map<String, String> attributes )
		{
			return SwingMetawidget.this.getOverriddenWidget( elementName, attributes );
		}

		@Override
		protected boolean isStub( JComponent component )
		{
			return ( component instanceof Stub );
		}

		@Override
		protected Map<String, String> getStubAttributes( JComponent stub )
		{
			return ( (Stub) stub ).getAttributes();
		}

		@Override
		public SwingMetawidget buildNestedMetawidget( Map<String, String> attributes )
			throws Exception
		{
			SwingMetawidget nestedMetawidget = SwingMetawidget.this.getClass().newInstance();
			SwingMetawidget.this.initNestedMetawidget( nestedMetawidget, attributes );

			return nestedMetawidget;
		}

		@Override
		protected void endBuild()
			throws Exception
		{
			SwingMetawidget.this.endBuild();
			super.endBuild();
		}

		@Override
		protected SwingMetawidget getMixinOwner()
		{
			return SwingMetawidget.this;
		}

		@Override
		protected MetawidgetMixin<JComponent, SwingMetawidget> getNestedMixin( SwingMetawidget metawidget )
		{
			return metawidget.getMetawidgetMixin();
		}
	}
}
