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
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.JTextComponent;

import org.metawidget.MetawidgetException;
import org.metawidget.inspector.ConfigReader;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.mixin.w3c.MetawidgetMixin;
import org.metawidget.swing.binding.Binding;
import org.metawidget.swing.layout.Layout;
import org.metawidget.swing.layout.TableGridBagLayout;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.PathUtils;
import org.metawidget.util.PathUtils.TypeAndNames;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Element;

/**
 * Metawidget for Swing environments.
 * <p>
 * Automatically creates native Swing <code>JComponents</code>, such as <code>JTextField</code>
 * and <code>JComboBox</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

public class SwingMetawidget
	extends JComponent
{
	//
	// Private statics
	//

	private final static long					serialVersionUID	= 5614421785160728346L;

	private final static Map<String, Inspector>	INSPECTORS			= Collections.synchronizedMap( new HashMap<String, Inspector>() );

	private final static Stroke					STROKE_DOTTED		= new BasicStroke( 1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0f, new float[] { 3f }, 0f );

	//
	// Private members
	//

	private Object								mToInspect;

	private String								mPath;

	private String[]							mNamesPrefix;

	private String								mInspectorConfig	= "inspector-config.xml";

	private Inspector							mInspector;

	private Class<? extends Layout>				mLayoutClass		= TableGridBagLayout.class;

	private Layout								mLayout;

	/**
	 * The Binding class.
	 * <p>
	 * Binding class is <code>null</code> by default, to avoid default dependencies on third-party
	 * JARs
	 */

	private Class<? extends Binding>			mBindingClass;

	private Binding								mBinding;

	private ResourceBundle						mBundle;

	private Map<String, Object>					mParameters;

	private boolean								mNeedToBuildWidgets;

	private String								mLastInspection;

	private boolean								mIgnoreAddRemove;

	/**
	 * List of existing, manually added components.
	 * <p>
	 * This is a List, not a Set, for consistency in unit tests.
	 */

	private List<Component>						mExistingComponents	= CollectionUtils.newArrayList();

	/**
	 * List of existing, manually added, but unused by Metawidget components.
	 * <p>
	 * This is a List, not a Set, for consistency in unit tests.
	 */

	private List<Component>						mExistingComponentsUnused;

	private Map<String, Facet>					mFacets				= CollectionUtils.newHashMap();

	private MetawidgetMixin<JComponent>			mMetawidgetMixin;

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
	 */

	public void setToInspect( Object toInspect )
	{
		mToInspect = toInspect;

		// If no path, or path points to an old class, override it

		if ( toInspect != null && ( mPath == null || mPath.indexOf( StringUtils.SEPARATOR_FORWARD_SLASH_CHAR ) == -1 ) )
			mPath = ClassUtils.getUnproxiedClass( toInspect.getClass() ).getName();

		invalidateInspection();
	}

	/**
	 * Gets the Object being inspected.
	 * <p>
	 * Exposed for GUI builders and binding implementations.
	 */

	public Object getToInspect()
	{
		return mToInspect;
	}

	public void setPath( String path )
	{
		mPath = path;
		invalidateInspection();
	}

	public void setInspectorConfig( String inspectorConfig )
	{
		mInspectorConfig = inspectorConfig;
		mInspector = null;
		invalidateInspection();
	}

	public void setInspector( Inspector inspector )
	{
		mInspector = inspector;
		mInspectorConfig = null;
		invalidateInspection();
	}

	/**
	 * @param layoutClass
	 *            may be null
	 */

	public void setLayoutClass( Class<? extends Layout> layoutClass )
	{
		mLayoutClass = layoutClass;
		mLayout = null;
		invalidateWidgets();
	}

	/**
	 * Sets the Binding implementation to use for automatic, two-way Component-to-Object data
	 * binding. Current implementations include <code>BeansBinding</code> and
	 * <code>BeanUtilsBinding</code>.
	 *
	 * @param bindingClass
	 *            may be null
	 */

	public void setBindingClass( Class<? extends Binding> bindingClass )
	{
		mBindingClass = bindingClass;
		invalidateWidgets();
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
	 * Sets parameters to pass to Layout/Binding implementations.
	 * <p>
	 * Exposed for GUI builders.
	 */

	public void setParameters( Map<String, Object> parameters )
	{
		mParameters = parameters;
	}

	/**
	 * Gets the parameter value. Used by the chosen <code>Layout</code> or <code>Binding</code>
	 * implementation.
	 */

	public Object getParameter( String name )
	{
		if ( mParameters == null )
			return null;

		return mParameters.get( name );
	}

	/**
	 * Gets the parameter value. Used by the chosen <code>Layout</code> or <code>Binding</code>
	 * implementation.
	 * <p>
	 * Convenience method. Equivalent to <code>getParameter( clazz.getName() )</code>
	 */

	public Object getParameter( Class<?> clazz )
	{
		return getParameter( clazz.getName() );
	}

	/**
	 * Sets the parameter value. Parameters are passed to the chosen <code>Layout</code> or
	 * <code>Binding</code> implementation.
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
	 * <code>Binding</code> implementation.
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
	 * Rebinds the values in the UI to the given Object.
	 * <p>
	 * <code>rebind</code> can be thought of as a lightweight version of <code>setToInspect</code>.
	 * Unlike <code>setToInspect</code>, <code>rebind</code> does <em>not</em> reinspect the
	 * Object or recreate any <code>JComponents</code>. Rather, <code>rebind</code> applies
	 * only at the binding level, and updates the binding with values from the given Object.
	 * <p>
	 * This is more performant, and allows the Metawidget to be created 'in advance' and reused many
	 * times with different Objects, but it is the caller's responsibility that the Object passed to
	 * <code>rebind</code> is of the same type as the one previously passed to
	 * <code>setToInspect</code>.
	 * <p>
	 * For client's not using a Binding implementation, there is no need to call <code>rebind</code>.
	 * They can simply use <code>setValue</code> to update existing values in the UI.
	 * <p>
	 * In many ways, <code>rebind</code> can be thought of as the opposite of <code>save</code>.
	 *
	 * @throws MetawidgetException
	 *             if no binding configured
	 */

	public void rebind( Object toRebind )
	{
		// buildWidgets() so that mBinding is initialized

		buildWidgets();

		if ( mBinding == null )
			throw MetawidgetException.newException( "No binding configured. Use SwingMetawidget.setBindingClass" );

		mToInspect = toRebind;
		mBinding.rebind();

		for ( Component component : getComponents() )
		{
			if ( component instanceof SwingMetawidget )
			{
				( (SwingMetawidget) component ).rebind( toRebind );
			}
		}
	}

	/**
	 * Saves the values from the binding back to the Object being inspected.
	 *
	 * @throws MetawidgetException
	 *             if no binding configured
	 */

	public void save()
	{
		// buildWidgets() so that mBinding is initialized

		buildWidgets();

		if ( mBinding == null )
			throw MetawidgetException.newException( "No binding configured. Use SwingMetawidget.setBindingClass" );

		mBinding.save();

		// Having a save() method avoids having to expose a getBinding() method, which is handy
		// because we can worry about nested Metawidgets here, not in the Binding class

		for ( Component component : getComponents() )
		{
			if ( component instanceof SwingMetawidget )
			{
				( (SwingMetawidget) component ).save();
			}
		}
	}

	/**
	 * Overriden to build widgets just-in-time.
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
	 * Overriden to build widgets just-in-time.
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
	 * Overriden to build widgets just-in-time.
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
	 * Overriden to build widgets just-in-time.
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
	 * Overriden to build widgets just-in-time.
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
	 * Gets the value from the Component with the given name.
	 * <p>
	 * The value is returned as it was stored in the Component (eg. String for JTextField) so may
	 * need some conversion before being reapplied to the object being inspected. This obviously
	 * requires knowledge of which Component SwingMetawidget created, which is not ideal, so clients
	 * may prefer to use bindingClass instead.
	 */

	public Object getValue( String... names )
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

		return ClassUtils.getProperty( component, componentProperty );
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
	 * Finds the Component with the given name.
	 */

	public Component getComponent( String... names )
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
				return topComponent;

			if ( topComponent == null )
				throw MetawidgetException.newException( "No such component '" + name + "' of '" + ArrayUtils.toString( names, "', '" ) + "'" );

			if ( !( topComponent instanceof Container ) )
				throw MetawidgetException.newException( "'" + name + "' is not a Container" );
		}

		return topComponent;
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

	protected MetawidgetMixin<JComponent> newMetawidgetMixin()
	{
		return new SwingMetawidgetMixin();
	}

	@Override
	protected void paintComponent( Graphics graphics )
	{
		buildWidgets();

		super.paintComponent( graphics );

		// When used as part of an IDE builder tool, render as a dotted square so that we can see
		// something!

		if ( mToInspect == null && getComponentCount() == 0 )
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

		mNeedToBuildWidgets = true;
		super.removeAll();

		mNamesPrefix = null;

		if ( mBinding != null )
		{
			mBinding.unbind();
			mBinding = null;
		}

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

			if ( component instanceof Facet )
			{
				mFacets.put( component.getName(), (Facet) component );
			}
			else
			{
				mExistingComponents.add( component );
			}
		}

		super.addImpl( component, constraints, index );
	}

	protected void buildWidgets()
	{
		// No need to build?

		if ( !mNeedToBuildWidgets )
			return;

		mNeedToBuildWidgets = false;
		mIgnoreAddRemove = true;

		try
		{
			if ( mLastInspection == null )
				mLastInspection = inspect();

			// Don't buildWidgets if null, in order to protect
			// our 'dotted rectangle in IDE tools' effect

			if ( mLastInspection != null )
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

		// Start layout
		//
		// (we start a new layout each time, rather than complicating the Layouts with a
		// layoutCleanup method)

		if ( mLayoutClass != null )
		{
			mLayout = mLayoutClass.getConstructor( SwingMetawidget.class ).newInstance( this );
			mLayout.layoutBegin();
		}
		else
		{
			// Default to BoxLayout, which is like FlowLayout except it fills width. This
			// is useful for JTable CellEditors

			setLayout( new BoxLayout( this, BoxLayout.LINE_AXIS ) );
		}

		// Start binding

		if ( mBindingClass != null )
			mBinding = mBindingClass.getConstructor( SwingMetawidget.class ).newInstance( this );
	}

	protected void beforeBuildCompoundWidget( Element element )
	{
		mNamesPrefix = PathUtils.parsePath( mPath ).getNames();
	}

	protected void addWidget( JComponent component, String elementName, Map<String, String> attributes )
		throws Exception
	{
		String childName = attributes.get( NAME );

		// Bind actions

		if ( ACTION.equals( elementName ) && component instanceof JButton )
		{
			final Object toInspect = getToInspect();

			if ( toInspect != null )
			{
				JButton button = (JButton) component;
				String text = button.getText();
				final String name = attributes.get( NAME );

				try
				{
					// Parameterless

					final Method parameterlessActionMethod = toInspect.getClass().getMethod( name, (Class[]) null );

					button.setAction( new AbstractAction( text )
					{
						@Override
						public void actionPerformed( ActionEvent e )
						{
							try
							{
								parameterlessActionMethod.invoke( toInspect, (Object[]) null );
							}
							catch ( Exception ex )
							{
								throw new RuntimeException( ex );
							}
						}
					} );
				}
				catch( NoSuchMethodException e )
				{
					// ActionEvent-parameter based

					final Method parameterizedActionMethod = toInspect.getClass().getMethod( name, ActionEvent.class );

					button.setAction( new AbstractAction( text )
					{
						@Override
						public void actionPerformed( ActionEvent event )
						{
							try
							{
								parameterizedActionMethod.invoke( toInspect, new ActionEvent( toInspect, 0, name ) );
							}
							catch ( Exception ex )
							{
								throw new RuntimeException( ex );
							}
						}
					} );
				}
			}
		}

		// Bind properties

		else
		{
			if ( mBinding != null )
			{
				if ( mNamesPrefix == null )
					bind( component, childName );
				else
					bind( component, ArrayUtils.add( mNamesPrefix, childName ) );
			}
		}

		// Add to layout

		remove( component );
		component.setName( childName );

		if ( mLayout == null )
		{
			add( component );
		}
		else
		{
			if ( component instanceof Stub )
				attributes.putAll( ( (Stub) component ).getAttributes() );

			mLayout.layoutChild( component, attributes );
		}

	}

	protected JComponent getOverridenWidget( String elementName, Map<String, String> attributes )
	{
		String name = attributes.get( NAME );

		if ( name == null )
			return null;

		JComponent component = null;

		for ( Component componentExisting : mExistingComponentsUnused )
		{
			if ( name.equals( componentExisting.getName() ) )
			{
				component = (JComponent) componentExisting;
				break;
			}
		}

		if ( component != null )
			mExistingComponentsUnused.remove( component );

		return component;
	}

	protected JComponent buildReadOnlyWidget( String elementName, Map<String, String> attributes )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		// Action

		if ( ACTION.equals( elementName ) )
			return null;

		// Masked (return a JPanel, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
			return new JPanel();

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
		{
			// May have alternate labels

			String lookupLabels = attributes.get( LOOKUP_LABELS );

			if ( lookupLabels != null && !"".equals( lookupLabels ) )
				return new LookupLabel( getLabelsMap( CollectionUtils.fromString( lookup ), CollectionUtils.fromString( lookupLabels ) ) );

			return new JLabel();
		}

		String type = attributes.get( TYPE );

		// If no type, fail gracefully with a JTextField

		if ( type == null || "".equals( type ) )
			return new JLabel();

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz != null )
		{
			// Primitives

			if ( clazz.isPrimitive() )
				return new JLabel();

			if ( String.class.equals( clazz ) )
			{
				if ( TRUE.equals( attributes.get( LARGE ) ) )
				{
					// Avoid just using a JLabel, in case the contents are
					// really large. This also helps the layout of this field
					// be consistent with its activated, JTextArea version

					JLabel label = new JLabel();
					label.setVerticalAlignment( SwingConstants.TOP );

					JScrollPane scrollPane = new JScrollPane( label );
					scrollPane.setOpaque( false );
					scrollPane.getViewport().setOpaque( false );
					scrollPane.setBorder( null );
					return scrollPane;
				}

				return new JLabel();
			}

			if ( Date.class.equals( clazz ) )
				return new JLabel();

			if ( Boolean.class.equals( clazz ) )
				return new JLabel();

			if ( Number.class.isAssignableFrom( clazz ) )
				return new JLabel();

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return null;

			// Not simple, but don't expand

			if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
				return new JLabel();
		}

		// Nested Metawidget

		return getClass().newInstance();
	}

	protected JComponent buildActiveWidget( String elementName, Map<String, String> attributes )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		// Action

		if ( ACTION.equals( elementName ) )
			return new JButton( getLabelString( attributes ));

		String type = attributes.get( TYPE );

		// If no type, fail gracefully with a JTextField

		if ( type == null || "".equals( type ) )
			return new JTextField();

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
		{
			JComboBox comboBox = new JComboBox();

			// Add an empty choice (if nullable, and not required)

			if ( clazz == null || ( !clazz.isPrimitive() && !TRUE.equals( attributes.get( REQUIRED ) ) ) )
				comboBox.addItem( null );

			List<String> values = CollectionUtils.fromString( lookup );

			for ( String value : values )
			{
				Object convertedValue = value;

				if ( mBinding != null )
					convertedValue = mBinding.convertFromString( value, clazz );

				comboBox.addItem( convertedValue );
			}

			// May have alternate labels

			String lookupLabels = attributes.get( LOOKUP_LABELS );

			if ( lookupLabels != null && !"".equals( lookupLabels ) )
			{
				Map<String, String> labelsMap = getLabelsMap( values, CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ) );

				comboBox.setEditor( new LookupComboBoxEditor( labelsMap ) );
				comboBox.setRenderer( new LookupComboBoxRenderer( labelsMap ) );
			}

			return comboBox;
		}

		if ( clazz != null )
		{
			// Primitives

			if ( clazz.isPrimitive() )
			{
				// booleans

				if ( "boolean".equals( type ) )
					return new JCheckBox();

				// chars

				if ( "char".equals( type ) )
					return new JTextField();

				// Ranged

				String minimumValue = attributes.get( MINIMUM_VALUE );
				String maximumValue = attributes.get( MAXIMUM_VALUE );

				if ( minimumValue != null && !"".equals( minimumValue ) && maximumValue != null && !"".equals( maximumValue ) )
				{
					JSlider slider = new JSlider();
					slider.setMinimum( Integer.parseInt( minimumValue ) );
					slider.setMaximum( Integer.parseInt( maximumValue ) );

					return slider;
				}

				// Not-ranged

				JSpinner spinner = new JSpinner();

				// (use 'new', not '.valueOf', for JDK 1.4 compatibility)

				if ( "byte".equals( type ) )
					setSpinnerModel( spinner, new Byte( (byte) 0 ), new Byte( Byte.MIN_VALUE ), new Byte( Byte.MAX_VALUE ), new Byte( (byte) 1 ) );
				else if ( "short".equals( type ) )
					setSpinnerModel( spinner, new Short( (short) 0 ), new Short( Short.MIN_VALUE ), new Short( Short.MAX_VALUE ), new Short( (short) 1 ) );
				else if ( "int".equals( type ) )
					setSpinnerModel( spinner, new Integer( 0 ), new Integer( Integer.MIN_VALUE ), new Integer( Integer.MAX_VALUE ), new Integer( 1 ) );
				else if ( "long".equals( type ) )
					setSpinnerModel( spinner, new Long( 0l ), new Long( Long.MIN_VALUE ), new Long( Long.MAX_VALUE ), new Long( 1l ) );
				else if ( "float".equals( type ) )
					setSpinnerModel( spinner, new Float( 0f ), new Float( -Float.MAX_VALUE ), new Float( Float.MAX_VALUE ), new Float( 0.1f ) );
				else if ( "double".equals( type ) )
					setSpinnerModel( spinner, new Double( 0f ), new Double( -Double.MAX_VALUE ), new Double( Double.MAX_VALUE ), new Double( 0.1f ) );

				return spinner;
			}

			// Strings

			if ( String.class.equals( clazz ) )
			{
				if ( TRUE.equals( attributes.get( MASKED ) ) )
					return new JPasswordField();

				if ( TRUE.equals( attributes.get( LARGE ) ) )
				{
					JTextArea textarea = new JTextArea();
					textarea.setRows( 2 );
					return new JScrollPane( textarea );
				}

				return new JTextField();
			}

			// Dates

			if ( Date.class.equals( clazz ) )
				return new JTextField();

			// Booleans (are tri-state)

			if ( Boolean.class.equals( clazz ) )
			{
				JComboBox comboBox = new JComboBox();
				comboBox.addItem( null );
				comboBox.addItem( Boolean.TRUE );
				comboBox.addItem( Boolean.FALSE );

				return comboBox;
			}

			// Numbers
			//
			// Note: we use a text field, not a JSpinner or JSlider, because
			// BeansBinding gets upset at doing 'setValue( null )' if the Integer
			// is null. We can still use JSpinner/JSliders for primitives, though.

			if ( Number.class.isAssignableFrom( clazz ) )
				return new JTextField();

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return null;

			// Not simple, but don't expand

			if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
				return new JTextField();
		}

		// Nested Metawidget

		return getClass().newInstance();
	}

	protected void endBuild()
	{
		if ( mLayout != null )
		{
			if ( mExistingComponentsUnused != null )
			{
				for ( Component componentExisting : mExistingComponentsUnused )
				{
					Map<String, String> miscAttributes = CollectionUtils.newHashMap();

					// Manually created components default to no section

					miscAttributes.put( SECTION, "" );

					if ( componentExisting instanceof Stub )
						miscAttributes.putAll( ( (Stub) componentExisting ).getAttributes() );

					mLayout.layoutChild( componentExisting, miscAttributes );
				}
			}

			mLayout.layoutEnd();
		}

		// Call validate because JComponents have been added/removed, and
		// JComponent layout information has changed

		validate();
	}

	protected String inspect()
	{
		if ( mPath == null )
			return null;

		// If this Inspector has been set externally, use it...

		if ( mInspector == null )
		{
			// ...otherwise, if this InspectorConfig has already been read, use it...

			mInspector = INSPECTORS.get( mInspectorConfig );

			// ...otherwise, initialize the Inspector

			if ( mInspector == null )
			{
				mInspector = new ConfigReader().read( mInspectorConfig );
				INSPECTORS.put( mInspectorConfig, mInspector );
			}
		}

		// Use the inspector to inspect the path

		TypeAndNames typeAndNames = PathUtils.parsePath( mPath );
		return mInspector.inspect( mToInspect, typeAndNames.getType(), typeAndNames.getNames() );
	}

	protected SwingMetawidget initMetawidget( SwingMetawidget metawidget, Map<String, String> attributes )
	{
		metawidget.setPath( mPath + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME ) );

		if ( mInspectorConfig != null )
			metawidget.setInspectorConfig( mInspectorConfig );
		else
			metawidget.setInspector( mInspector );

		metawidget.setLayoutClass( mLayoutClass );
		metawidget.setBindingClass( mBindingClass );
		metawidget.setBundle( mBundle );
		metawidget.setOpaque( isOpaque() );

		if ( mParameters != null )
			metawidget.setParameters( CollectionUtils.newHashMap( mParameters ) );

		metawidget.setToInspect( mToInspect );

		return metawidget;
	}

	/**
	 * Returns the property used to get/set the value of the component.
	 * <p>
	 * If the component is not known, returns <code>null</code>. Does not throw an Exception, as
	 * we want to fail gracefully if, say, someone tries to bind to a JPanel.
	 * <p>
	 * Subclasses who introduce new component types (eg. JXDatePicker) should override this method
	 * to return the value property for the new component (eg. getDate/setDate).
	 */

	protected String getValueProperty( Component component )
	{
		if ( component instanceof JComboBox )
			return "selectedItem";

		if ( component instanceof JLabel )
			return "text";

		if ( component instanceof JTextComponent )
			return "text";

		if ( component instanceof JSpinner )
			return "value";

		if ( component instanceof JSlider )
			return "value";

		if ( component instanceof JCheckBox )
			return "selected";

		return null;
	}

	protected void bind( Component component, String... names )
	{
		Component actualComponent = component;

		// Drill into JScrollPanes

		if ( actualComponent instanceof JScrollPane )
			actualComponent = ( (JScrollPane) actualComponent ).getViewport().getView();

		mBinding.bind( actualComponent, getValueProperty( actualComponent ), names );
	}

	//
	// Private methods
	//

	/**
	 * Sets the JSpinner model.
	 * <p>
	 * By default, a JSpinner calls <code>setColumns</code> upon <code>setModel</code>. For
	 * numbers like <code>Integer.MAX_VALUE</code> and <code>Double.MAX_VALUE</code>, this can
	 * be very large and mess up the layout. Here, we reset <code>setColumns</code> to 0.
	 * <p>
	 * Note it is very important we set the initial value of the <code>JSpinner</code> to the same
	 * type as the property it maps to (eg. float or double, int or long).
	 */

	private void setSpinnerModel( JSpinner spinner, Number value, Comparable<? extends Number> minimum, Comparable<? extends Number> maximum, Number stepSize )
	{
		spinner.setModel( new SpinnerNumberModel( value, minimum, maximum, stepSize ) );
		( (JSpinner.DefaultEditor) spinner.getEditor() ).getTextField().setColumns( 0 );
	}

	private Map<String, String> getLabelsMap( List<String> values, List<String> labels )
	{
		Map<String, String> labelsMap = CollectionUtils.newHashMap();

		if ( labels.size() != values.size() )
			throw MetawidgetException.newException( "Labels list must be same size as values list" );

		for ( int loop = 0, length = values.size(); loop < length; loop++ )
		{
			labelsMap.put( values.get( loop ), labels.get( loop ) );
		}

		return labelsMap;
	}

	//
	// Inner class
	//

	protected class SwingMetawidgetMixin
		extends MetawidgetMixin<JComponent>
	{
		//
		//
		// Protected methods
		//
		//

		@Override
		protected void startBuild()
			throws Exception
		{
			SwingMetawidget.this.startBuild();
		}

		@Override
		protected void addWidget( JComponent component, String elementName, Map<String, String> attributes )
			throws Exception
		{
			SwingMetawidget.this.addWidget( component, elementName, attributes );
		}

		@Override
		protected JComponent getOverridenWidget( String elementName, Map<String, String> attributes )
		{
			return SwingMetawidget.this.getOverridenWidget( elementName, attributes );
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
		protected void buildCompoundWidget( Element element )
			throws Exception
		{
			SwingMetawidget.this.beforeBuildCompoundWidget( element );
			super.buildCompoundWidget( element );
		}

		@Override
		protected JComponent buildReadOnlyWidget( String elementName, Map<String, String> attributes )
			throws Exception
		{
			return SwingMetawidget.this.buildReadOnlyWidget( elementName, attributes );
		}

		@Override
		protected JComponent buildActiveWidget( String elementName, Map<String, String> attributes )
			throws Exception
		{
			return SwingMetawidget.this.buildActiveWidget( elementName, attributes );
		}

		@Override
		public JComponent initMetawidget( JComponent widget, Map<String, String> attributes )
		{
			SwingMetawidget metawidget = (SwingMetawidget) widget;
			metawidget.setReadOnly( isReadOnly( attributes ) );
			metawidget.setMaximumInspectionDepth( getMaximumInspectionDepth() - 1 );

			return SwingMetawidget.this.initMetawidget( metawidget, attributes );
		}

		@Override
		protected void endBuild()
		{
			SwingMetawidget.this.endBuild();
		}
	}

	/**
	 * Label whose values use a lookup
	 */

	public static class LookupLabel
		extends JLabel
	{
		//
		//
		// Private statics
		//
		//

		private static final long	serialVersionUID	= -7037413818513317265L;

		//
		//
		// Private members
		//
		//

		private Map<String, String>	mLookup;

		//
		//
		// Constructor
		//
		//

		public LookupLabel( Map<String, String> lookup )
		{
			if ( lookup == null )
				throw new NullPointerException( "lookup" );

			mLookup = lookup;
		}

		//
		//
		// Public methods
		//
		//

		@Override
		public void setText( String text )
		{
			String lookup = null;

			if ( text != null && mLookup != null )
				lookup = mLookup.get( text );

			super.setText( lookup );
		}
	}

	/**
	 * Editor for ComboBox whose values use a lookup.
	 */

	private static class LookupComboBoxEditor
		extends BasicComboBoxEditor
	{
		//
		//
		// Private members
		//
		//

		private Map<String, String>	mLookups;

		//
		//
		// Constructor
		//
		//

		public LookupComboBoxEditor( Map<String, String> lookups )
		{
			if ( lookups == null )
				throw new NullPointerException( "lookups" );

			mLookups = lookups;
		}

		//
		//
		// Public methods
		//
		//

		@Override
		public void setItem( Object item )
		{
			super.setItem( mLookups.get( item ) );
		}
	}

	/**
	 * Renderer for ComboBox whose values use a lookup.
	 */

	private static class LookupComboBoxRenderer
		extends BasicComboBoxRenderer
	{
		//
		//
		// Private statics
		//
		//

		private final static long	serialVersionUID	= -6726292881904521906L;

		//
		//
		// Private members
		//
		//

		private Map<String, String>	mLookups;

		//
		//
		// Constructor
		//
		//

		public LookupComboBoxRenderer( Map<String, String> lookups )
		{
			if ( lookups == null )
				throw new NullPointerException( "lookups" );

			mLookups = lookups;
		}

		//
		//
		// Public methods
		//
		//

		@Override
		public Component getListCellRendererComponent( JList list, Object value, int index, boolean selected, boolean hasFocus )
		{
			Component component = super.getListCellRendererComponent( list, value, index, selected, hasFocus );

			String lookup = mLookups.get( value );

			if ( lookup != null )
				( (JLabel) component ).setText( lookup );

			return component;
		}
	}
}
