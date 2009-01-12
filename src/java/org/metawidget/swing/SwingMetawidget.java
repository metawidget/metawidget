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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
import org.metawidget.swing.actionbinding.ActionBinding;
import org.metawidget.swing.actionbinding.reflection.ReflectionBinding;
import org.metawidget.swing.layout.GridBagLayout;
import org.metawidget.swing.layout.Layout;
import org.metawidget.swing.propertybinding.PropertyBinding;
import org.metawidget.swing.validator.Validator;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.util.simple.PathUtils.TypeAndNames;

/**
 * Metawidget for Swing environments.
 * <p>
 * Automatically creates native Swing <code>JComponents</code>, such as <code>JTextField</code>
 * and <code>JComboBox</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

// Note: It would be nice for SwingMetawidget to extend AwtMetawidget, but we want
// SwingMetawidget to extend JComponent for various Swing-specific goodies like setBorder and
// setOpaque
//
public class SwingMetawidget
	extends JComponent
{
	//
	// Private statics
	//

	private final static long					serialVersionUID	= 1l;

	private final static Map<String, Inspector>	INSPECTORS			= Collections.synchronizedMap( new HashMap<String, Inspector>() );

	private final static Stroke					STROKE_DOTTED		= new BasicStroke( 1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0f, new float[] { 3f }, 0f );

	//
	// Private members
	//

	private Object								mToInspect;

	private String								mPath;

	private String								mInspectorConfig	= "inspector-config.xml";

	private Inspector							mInspector;

	private Class<? extends Layout>				mLayoutClass		= GridBagLayout.class;

	private Layout								mLayout;

	/**
	 * The PropertyBinding class.
	 * <p>
	 * PropertyBinding class is <code>null</code> by default, to avoid default dependencies on
	 * third-party JARs
	 */

	private Class<? extends PropertyBinding>	mPropertyBindingClass;

	private PropertyBinding						mPropertyBinding;

	/**
	 * The ActionBinding class.
	 */

	private Class<? extends ActionBinding>		mActionBindingClass	= ReflectionBinding.class;

	private ActionBinding						mActionBinding;

	private Class<? extends Validator>			mValidatorClass;

	private Validator							mValidator;

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

	private MetawidgetMixin<Component>			mMetawidgetMixin;

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
	 */

	public Object getToInspect()
	{
		return mToInspect;
	}

	/**
	 * Sets the path to be inspected.
	 * <p>
	 * Note <code>setPath</code> is quite different to <code>java.awt.Component.setName</code>.
	 * <code>setPath</code> is always in relation to <code>setToInspect</code>, so must include
	 * the type name and any subsequent sub-names (eg. type/name/name). Conversely,
	 * <code>setName</code> is a single name relative to our immediate parent.
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
	 * Sets the PropertyBinding implementation to use for automatic, two-way Component-to-Object
	 * data binding. Current implementations include <code>BeansBinding</code> and
	 * <code>BeanUtilsBinding</code>.
	 *
	 * @param propertyBindingClass
	 *            may be null
	 */

	public void setPropertyBindingClass( Class<? extends PropertyBinding> propertyBindingClass )
	{
		mPropertyBindingClass = propertyBindingClass;
		invalidateWidgets();
	}

	public void setActionBindingClass( Class<? extends ActionBinding> actionBindingClass )
	{
		mActionBindingClass = actionBindingClass;
		invalidateWidgets();
	}

	public void setValidatorClass( Class<? extends Validator> validatorClass )
	{
		mValidatorClass = validatorClass;
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
	 */

	public Object getParameter( String name )
	{
		if ( mParameters == null )
			return null;

		return mParameters.get( name );
	}

	/**
	 * Gets the parameter value. Used by the chosen <code>Layout</code> or
	 * <code>PropertyBinding</code> implementation.
	 * <p>
	 * Convenience method. Equivalent to <code>getParameter( clazz.getName() )</code>
	 */

	public Object getParameter( Class<?> clazz )
	{
		return getParameter( clazz.getName() );
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
	 * Rebinds the values in the UI to the given Object.
	 * <p>
	 * <code>rebind</code> can be thought of as a lightweight version of <code>setToInspect</code>.
	 * Unlike <code>setToInspect</code>, <code>rebind</code> does <em>not</em> reinspect the
	 * Object or recreate any <code>Components</code>. Rather, <code>rebind</code> applies only
	 * at the binding level, and updates the binding with values from the given Object.
	 * <p>
	 * This is more performant, and allows the Metawidget to be created 'in advance' and reused many
	 * times with different Objects, but it is the caller's responsibility that the Object passed to
	 * <code>rebind</code> is of the same type as the one previously passed to
	 * <code>setToInspect</code>.
	 * <p>
	 * For client's not using a PropertyBinding implementation, there is no need to call
	 * <code>rebind</code>. They can simply use <code>setValue</code> to update existing values
	 * in the UI.
	 * <p>
	 * In many ways, <code>rebind</code> can be thought of as the opposite of <code>save</code>.
	 *
	 * @throws MetawidgetException
	 *             if no binding configured
	 */

	public void rebind( Object toRebind )
	{
		// buildWidgets() so that mPropertyBinding is initialized

		buildWidgets();

		if ( mPropertyBinding == null )
			throw MetawidgetException.newException( "No property binding configured. Use SwingMetawidget.setPropertyBindingClass" );

		mToInspect = toRebind;
		mPropertyBinding.rebindProperties();

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
		// buildWidgets() so that mPropertyBinding is initialized

		buildWidgets();

		if ( mPropertyBinding == null )
			throw MetawidgetException.newException( "No property binding configured. Use SwingMetawidget.setPropertyBindingClass" );

		mPropertyBinding.saveProperties();

		// Having a save() method avoids having to expose a getPropertyBinding() method, which is
		// handy because we can worry about nested Metawidgets here, not in the PropertyBinding
		// class

		for ( Component component : getComponents() )
		{
			if ( component instanceof SwingMetawidget )
			{
				( (SwingMetawidget) component ).save();
			}
		}
	}

	/**
	 * Validates all component values using the current Validator (as set by
	 * <code>setValidatorClass</code>).
	 * <p>
	 * Some validation implementations will use immediate validation (ie. based on
	 * <code>keyReleased</code>). Others may prefer deferred, explicit validation. Clients may
	 * wish to call <code>validateValues</code> immediately before calling <code>save</code>.
	 *
	 * @throws MetawidgetException
	 *             if no binding configured
	 */

	public void validateValues()
	{
		// buildWidgets() so that mValidator is initialized

		buildWidgets();

		if ( mValidator == null )
			throw MetawidgetException.newException( "No validator configured. Use SwingMetawidget.setValidatorClass" );

		mValidator.validate();

		// Having a validateValues() method avoids having to expose a getValidator() method, which
		// is handy because we can worry about nested Metawidgets here, not in the Validator class

		for ( Component component : getComponents() )
		{
			if ( component instanceof SwingMetawidget )
			{
				( (SwingMetawidget) component ).validateValues();
			}
		}
	}

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
	 * Returns the property used to get/set the value of the component.
	 * <p>
	 * If the component is not known, returns <code>null</code>. Does not throw an Exception, as
	 * we want to fail gracefully if, say, someone tries to bind to a JPanel.
	 * <p>
	 * Subclasses who introduce new component types (eg. JXDatePicker) should override this method
	 * to return the value property for the new component (eg. getDate/setDate).
	 */

	public String getValueProperty( Component component )
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

	protected MetawidgetMixin<Component> newMetawidgetMixin()
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

		mNeedToBuildWidgets = true;
		super.removeAll();

		if ( mPropertyBinding != null )
		{
			mPropertyBinding.unbindProperties();
			mPropertyBinding = null;
		}

		mActionBinding = null;
		mValidator = null;

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

			mExistingComponents.add( component );
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

		if ( mPropertyBindingClass != null )
			mPropertyBinding = mPropertyBindingClass.getConstructor( SwingMetawidget.class ).newInstance( this );

		if ( mActionBindingClass != null )
			mActionBinding = mActionBindingClass.getConstructor( SwingMetawidget.class ).newInstance( this );

		// Validator

		if ( mValidatorClass != null )
			mValidator = mValidatorClass.getConstructor( SwingMetawidget.class ).newInstance( this );
	}

	protected void addWidget( Component component, String elementName, Map<String, String> attributes )
		throws Exception
	{
		// Drill into JScrollPanes

		Component actualComponent = component;

		if ( actualComponent instanceof JScrollPane )
			actualComponent = ( (JScrollPane) actualComponent ).getViewport().getView();

		// Construct path

		final String name = attributes.get( NAME );
		String path = mPath;

		if ( mMetawidgetMixin.isCompoundWidget() )
			path += StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + name;

		// Bind actions

		if ( ACTION.equals( elementName ) )
		{
			if ( mActionBinding != null )
				mActionBinding.bindAction( actualComponent, attributes, path );
		}

		// Bind properties

		else
		{
			// TODO: document widget types for GWT

			if ( mPropertyBinding != null )
				mPropertyBinding.bindProperty( actualComponent, attributes, path );

			if ( mValidator != null )
				mValidator.addValidator( actualComponent, attributes, path );
		}

		// Set the name of the component.
		//
		// If this is a JScrollPane, set the name of the top-level JScrollPane. Don't do this before
		// now, as we don't want binding/validation implementations accidentally relying on the
		// name being set (which it won't be for actualComponent)

		component.setName( name );

		// Add to layout

		remove( component );

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

	protected Component getOverriddenWidget( String elementName, Map<String, String> attributes )
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

		return component;
	}

	protected Component buildReadOnlyWidget( String elementName, Map<String, String> attributes )
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
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return new JLabel();

		// Nested Metawidget

		return createMetawidget();
	}

	protected Component buildActiveWidget( String elementName, Map<String, String> attributes )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		// Action

		if ( ACTION.equals( elementName ) )
			return new JButton( getLabelString( attributes ) );

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
			//
			// Note: there's an extra caveat for Groovy dynamic types: if we can't load
			// the class, assume it is non-primitive and therefore add a null choice
			// (unless 'required=true' is specified)

			if ( ( clazz == null || !clazz.isPrimitive() ) && !TRUE.equals( attributes.get( REQUIRED ) ) )
				comboBox.addItem( null );

			List<String> values = CollectionUtils.fromString( lookup );

			for ( String value : values )
			{
				Object convertedValue = value;

				if ( mPropertyBinding != null )
					convertedValue = mPropertyBinding.convertFromString( value, clazz );

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

				if ( boolean.class.equals( clazz ) )
					return new JCheckBox();

				// chars

				if ( char.class.equals( clazz ) )
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

				if ( byte.class.equals( clazz ) )
				{
					byte minimum = Byte.MIN_VALUE;
					byte maximum = Byte.MAX_VALUE;

					if ( minimumValue != null && !"".equals( minimumValue ) )
						minimum = Byte.parseByte( minimumValue );

					if ( maximumValue != null && !"".equals( maximumValue ) )
						maximum = Byte.parseByte( maximumValue );

					setSpinnerModel( spinner, (byte) 0, minimum, maximum, (byte) 1 );
				}
				else if ( short.class.equals( clazz ) )
				{
					short minimum = Short.MIN_VALUE;
					short maximum = Short.MAX_VALUE;

					if ( minimumValue != null && !"".equals( minimumValue ) )
						minimum = Short.parseShort( minimumValue );

					if ( maximumValue != null && !"".equals( maximumValue ) )
						maximum = Short.parseShort( maximumValue );

					setSpinnerModel( spinner, (short) 0, minimum, maximum, (short) 1 );
				}
				else if ( int.class.equals( clazz ) )
				{
					int minimum = Integer.MIN_VALUE;
					int maximum = Integer.MAX_VALUE;

					if ( minimumValue != null && !"".equals( minimumValue ) )
						minimum = Integer.parseInt( minimumValue );

					if ( maximumValue != null && !"".equals( maximumValue ) )
						maximum = Integer.parseInt( maximumValue );

					setSpinnerModel( spinner, 0, minimum, maximum, 1 );
				}
				else if ( long.class.equals( clazz ) )
				{
					long minimum = Long.MIN_VALUE;
					long maximum = Long.MAX_VALUE;

					if ( minimumValue != null && !"".equals( minimumValue ) )
						minimum = Long.parseLong( minimumValue );

					if ( maximumValue != null && !"".equals( maximumValue ) )
						maximum = Long.parseLong( maximumValue );

					setSpinnerModel( spinner, (long) 0, minimum, maximum, (long) 1 );
				}
				else if ( float.class.equals( clazz ) )
				{
					float minimum = -Float.MAX_VALUE;
					float maximum = Float.MAX_VALUE;

					if ( minimumValue != null && !"".equals( minimumValue ) )
						minimum = Float.parseFloat( minimumValue );

					if ( maximumValue != null && !"".equals( maximumValue ) )
						maximum = Float.parseFloat( maximumValue );

					setSpinnerModel( spinner, (float) 0, minimum, maximum, (float) 0.1 );
				}
				else if ( double.class.equals( clazz ) )
				{
					double minimum = -Double.MAX_VALUE;
					double maximum = Double.MAX_VALUE;

					if ( minimumValue != null && !"".equals( minimumValue ) )
						minimum = Double.parseDouble( minimumValue );

					if ( maximumValue != null && !"".equals( maximumValue ) )
						maximum = Double.parseDouble( maximumValue );

					setSpinnerModel( spinner, (double) 0, minimum, maximum, 0.1 );
				}

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

					// Since we know we are dealing with Strings, we consider
					// word-wrapping a sensible default

					textarea.setLineWrap( true );
					textarea.setWrapStyleWord( true );

					// We also consider 2 rows a sensible default, so that the
					// JTextArea is always distinguishable from a JTextField

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
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return new JTextField();

		// Nested Metawidget

		return createMetawidget();
	}

	/**
	 * Create a sub-Metawidget.
	 * <p>
	 * By default, this method will create a sub-Metawidget of the same type as the original
	 * Metawidget (ie. <code>this.getClass().newInstance()</code>. Usually this will be the
	 * desired behaviour - so that clients who subclass SwingMetawidget will get a new instance of
	 * their own subclass.
	 * <p>
	 * However, this might not work in all situations (ie. if you create an anonymous inner class
	 * based on SwingMetawidget) so clients can override this behaviour if required.
	 */

	protected SwingMetawidget createMetawidget()
		throws Exception
	{
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

		if ( mValidator != null )
			mValidator.initializeValidators();

		// Call validate because Components have been added/removed, and
		// Component layout information has changed

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
		return mInspector.inspect( mToInspect, typeAndNames.getType(), typeAndNames.getNamesAsArray() );
	}

	protected SwingMetawidget initMetawidget( SwingMetawidget metawidget, Map<String, String> attributes )
	{
		metawidget.setPath( mPath + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME ) );

		if ( mInspector != null )
			metawidget.setInspector( mInspector );
		else
			metawidget.setInspectorConfig( mInspectorConfig );

		metawidget.setLayoutClass( mLayoutClass );
		metawidget.setPropertyBindingClass( mPropertyBindingClass );
		metawidget.setActionBindingClass( mActionBindingClass );
		metawidget.setValidatorClass( mValidatorClass );
		metawidget.setBundle( mBundle );
		metawidget.setOpaque( isOpaque() );

		if ( mParameters != null )
			metawidget.setParameters( CollectionUtils.newHashMap( mParameters ) );

		metawidget.setToInspect( mToInspect );

		return metawidget;
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
		extends MetawidgetMixin<Component>
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
		protected void addWidget( Component component, String elementName, Map<String, String> attributes )
			throws Exception
		{
			SwingMetawidget.this.addWidget( component, elementName, attributes );
		}

		@Override
		protected Component getOverriddenWidget( String elementName, Map<String, String> attributes )
		{
			return SwingMetawidget.this.getOverriddenWidget( elementName, attributes );
		}

		@Override
		protected boolean isMetawidget( Component widget )
		{
			return ( widget instanceof SwingMetawidget );
		}

		@Override
		protected boolean isStub( Component component )
		{
			return ( component instanceof Stub );
		}

		@Override
		protected Map<String, String> getStubAttributes( Component stub )
		{
			return ( (Stub) stub ).getAttributes();
		}

		@Override
		protected Component buildReadOnlyWidget( String elementName, Map<String, String> attributes )
			throws Exception
		{
			return SwingMetawidget.this.buildReadOnlyWidget( elementName, attributes );
		}

		@Override
		protected Component buildActiveWidget( String elementName, Map<String, String> attributes )
			throws Exception
		{
			return SwingMetawidget.this.buildActiveWidget( elementName, attributes );
		}

		@Override
		public Component initMetawidget( Component widget, Map<String, String> attributes )
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

		private static final long	serialVersionUID	= 1l;

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

		private final static long	serialVersionUID	= 1l;

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
