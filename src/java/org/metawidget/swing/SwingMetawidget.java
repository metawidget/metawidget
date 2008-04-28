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
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.JTextComponent;

import org.metawidget.MetawidgetException;
import org.metawidget.impl.MetawidgetMixin;
import org.metawidget.inspector.ConfigReader;
import org.metawidget.inspector.Inspector;
import org.metawidget.swing.binding.Binding;
import org.metawidget.swing.layout.Layout;
import org.metawidget.swing.layout.TableGridBagLayout;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.StringUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.XmlUtils.TypeAndNames;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Metawidget for Swing environments.
 * <p>
 * Automatically creates native Swing JComponents, such as <code>JTextField</code> and
 * <code>JComboBox</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

// SwingMetawidget extends JPanel for its double buffering support, as well as it
// behaves a little better in applet viewers

public class SwingMetawidget
	extends JPanel
{
	//
	//
	// Private statics
	//
	//

	private final static long					serialVersionUID	= 5614421785160728346L;

	private final static Map<String, Inspector>	INSPECTORS			= Collections.synchronizedMap( new HashMap<String, Inspector>() );

	private final static Stroke					STROKE_DOTTED		= new BasicStroke( 1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0f, new float[] { 3f }, 0f );

	//
	//
	// Private members
	//
	//

	private Object								mToInspect;

	private String								mPath;

	private String[]							mNamesPrefix;

	private String								mInspectorConfig	= "inspector-config.xml";

	private Inspector							mInspector;

	private Class<? extends Layout>				mLayoutClass		= TableGridBagLayout.class;

	private Layout								mLayout;

	/**
	 * Binding class is <code>null</code> by default, to avoid default dependencies on third-party
	 * JARs
	 */

	private Class<? extends Binding>			mBindingClass;

	private Binding								mBinding;

	private ResourceBundle						mBundle;

	private Map<String, Object>					mParameters;

	private boolean								mNeedToBuildWidgets;

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

	private SwingMetawidgetMixin				mMixin				= new SwingMetawidgetMixin();

	//
	//
	// Constructor
	//
	//

	public SwingMetawidget()
	{
		// Default constructor
	}

	public SwingMetawidget( SwingMetawidget metawidget )
	{
		// Do not copy mPath: could lead to infinite recursion

		mToInspect = metawidget.mToInspect;
		mInspector = metawidget.mInspector;
		mInspectorConfig = metawidget.mInspectorConfig;
		mLayoutClass = metawidget.mLayoutClass;
		mBindingClass = metawidget.mBindingClass;
		mBundle = metawidget.mBundle;

		if ( metawidget.mParameters != null )
			mParameters = CollectionUtils.newHashMap( metawidget.mParameters );
	}

	//
	//
	// Public methods
	//
	//

	/**
	 * Sets the Object to inspect.
	 */

	public void setToInspect( Object toInspect )
	{
		mToInspect = toInspect;

		// If no path, or path points to an old class, override it

		if ( toInspect != null && ( mPath == null || mPath.indexOf( StringUtils.SEPARATOR_SLASH ) == -1 ) )
			mPath = ClassUtils.getUnproxiedClass( toInspect.getClass() ).getName();

		invalidateWidgets();
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
		invalidateWidgets();
	}

	public void setInspectorConfig( String inspectorConfig )
	{
		mInspectorConfig = inspectorConfig;
		mInspector = null;
		invalidateWidgets();
	}

	public void setInspector( Inspector inspector )
	{
		mInspector = inspector;
		mInspectorConfig = null;
		invalidateWidgets();
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
	 * @param bindingClass
	 *            may be null
	 */

	public void setBindingClass( Class<? extends Binding> bindingClass )
	{
		mBindingClass = bindingClass;

		if ( mBinding != null )
		{
			mBinding.unbind();
			mBinding = null;
		}

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
				return localized;

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
		return mMixin.isReadOnly();
	}

	public void setReadOnly( boolean readOnly )
	{
		mMixin.setReadOnly( readOnly );
		invalidateWidgets();
	}

	//
	// The following methods all kick off buildWidgets() if necessary
	//

	/**
	 * Saves the values from the binding back to the object being inspected.
	 *
	 * @throws MetawidgetException
	 *             if no binding configured
	 */

	// Note: this method avoids having to expose a getBinding() method, which is handy
	// because we can worry about nested Metawidgets here, not in the Binding class
	public void save()
	{
		// buildWidgets may be necessary here if we have nested Metawidgets
		// and have only ever called getComponent (eg. never been visible)

		buildWidgets();

		if ( mBinding == null )
			throw MetawidgetException.newException( "No binding configured. Use SwingMetawidget.setBindingClass" );

		mBinding.save();

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
		buildWidgets();
		Component component = SwingUtils.findComponentNamed( this, names );

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
		buildWidgets();
		Component component = SwingUtils.findComponentNamed( this, names );

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
			mFacets.clear();
			mExistingComponents.clear();
		}
	}

	//
	//
	// Protected methods
	//
	//

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

	protected void invalidateWidgets()
	{
		if ( mNeedToBuildWidgets )
			return;

		mNeedToBuildWidgets = true;
		super.removeAll();

		mNamesPrefix = null;

		if ( mBinding != null )
			mBinding.unbind();

		// Note: call invalidate() here, not validate(), else components
		// will disappear during addImpl() when in visual GUI builder tools

		invalidate();
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

	public void buildWidgets()
	{
		// No need to build?

		if ( !mNeedToBuildWidgets )
			return;

		mNeedToBuildWidgets = false;
		mIgnoreAddRemove = true;

		try
		{
			mMixin.buildWidgets();
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

		if ( mLayoutClass != null )
		{
			mLayout = mLayoutClass.getConstructor( SwingMetawidget.class ).newInstance( this );
			mLayout.layoutBegin();
		}
		else
		{
			mLayout = null;
			setLayout( new BorderLayout() );
		}

		// Start binding

		if ( mBindingClass != null )
		{
			mBinding = mBindingClass.getConstructor( SwingMetawidget.class ).newInstance( this );
		}
		else
		{
			mBinding = null;
		}
	}

	protected void beforeBuildCompoundWidget()
	{
		mNamesPrefix = XmlUtils.parsePath( mPath ).getNames();
	}

	protected void addWidget( JComponent component, Map<String, String> attributes )
	{
		remove( component );

		String childName = attributes.get( NAME );
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

		if ( mBinding != null )
		{
			if ( mNamesPrefix == null )
				bind( component, childName );
			else
				bind( component, ArrayUtils.add( mNamesPrefix, childName ) );
		}
	}

	protected JComponent getOverridenWidget( Map<String, String> attributes )
	{
		JComponent component = null;
		String childName = attributes.get( NAME );

		if ( childName == null )
			return null;

		for ( Component componentExisting : mExistingComponentsUnused )
		{
			if ( childName.equals( componentExisting.getName() ) )
			{
				component = (JComponent) componentExisting;
				break;
			}
		}

		if ( component != null )
			mExistingComponentsUnused.remove( component );

		return component;
	}

	public JComponent buildReadOnlyWidget( Map<String, String> attributes )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		// Masked (return a JPanel, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
			return new JPanel();

		String type = attributes.get( TYPE );

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
			return new JLabel();

		// If no type, fail gracefully with a JTextField

		if ( type == null || "".equals( type ) )
			return new JLabel();

		if ( ClassUtils.isPrimitive( type ) )
			return new JLabel();

		// Lookup the Class
		//
		// Don't use Class.forName( type ), in case metawidget.jar is in JRE/lib/ext. This
		// is much less likely with Web-based or mobile-based applications

		Class<?> clazz = null;

		try
		{
			clazz = Thread.currentThread().getContextClassLoader().loadClass( type );
		}
		catch ( ClassNotFoundException e )
		{
			// Might be a symbolic type (eg. @type="Login Form")
		}

		if ( clazz != null )
		{
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
				return new JScrollPane( new JTable() );

			// Not simple, but don't expand

			if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
				return new JLabel();
		}

		// Nested Metawidget

		return createMetawidget( attributes );
	}

	public JComponent buildActiveWidget( Map<String, String> attributes )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		String type = attributes.get( TYPE );

		// If no type, fail gracefully with a JTextField

		if ( type == null || "".equals( type ) )
			return new JTextField();

		if ( ClassUtils.isPrimitive( type ) )
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

			if ( "byte".equals( type ) )
				setSpinnerModel( spinner, Byte.MIN_VALUE, Byte.MAX_VALUE );
			else if ( "short".equals( type ) )
				setSpinnerModel( spinner, Short.MIN_VALUE, Short.MAX_VALUE );
			else if ( "int".equals( type ) )
				setSpinnerModel( spinner, Integer.MIN_VALUE, Integer.MAX_VALUE );
			else if ( "long".equals( type ) )
				setSpinnerModel( spinner, Long.MIN_VALUE, Long.MAX_VALUE );
			else if ( "float".equals( type ) )
				setSpinnerModel( spinner, -Float.MAX_VALUE, Float.MAX_VALUE );
			else if ( "double".equals( type ) )
				setSpinnerModel( spinner, -Double.MAX_VALUE, Double.MAX_VALUE );

			return spinner;
		}

		// Lookup the Class
		//
		// Don't use Class.forName( type ), in case metawidget.jar is in JRE/lib/ext. This
		// is much less likely with Web-based or mobile-based applications

		Class<?> clazz = null;

		try
		{
			clazz = Thread.currentThread().getContextClassLoader().loadClass( type );
		}
		catch ( ClassNotFoundException e )
		{
			// Might be a symbolic type (eg. @type="Login Form")
		}

		if ( clazz != null )
		{
			// String Lookups

			String lookup = attributes.get( LOOKUP );

			if ( lookup != null && !"".equals( lookup ) )
			{
				JComboBox comboBox = new JComboBox();
				comboBox.addItem( null );

				List<String> values = CollectionUtils.fromString( lookup );

				for ( String value : values )
				{
					comboBox.addItem( value );
				}

				// May have alternate labels

				String lookupLabels = attributes.get( LOOKUP_LABELS );

				if ( lookupLabels != null && !"".equals( lookupLabels ) )
				{
					Map<Object, String> labelsMap = CollectionUtils.newHashMap();
					List<String> labels = CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) );

					if ( labels.size() != values.size() )
						throw MetawidgetException.newException( "Labels list must be same size as values list" );

					for ( int loop = 0, length = values.size(); loop < length; loop++ )
					{
						labelsMap.put( values.get( loop ), labels.get( loop ) );
					}

					comboBox.setEditor( new AlternateComboBoxEditor( labelsMap ) );
					comboBox.setRenderer( new AlternateComboBoxRenderer( labelsMap ) );
				}

				return comboBox;
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
				return new JScrollPane( new JTable() );
		}

		// Nested Metawidget

		return createMetawidget( attributes );
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

	protected Document inspect()
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

		return inspect( mInspector, mPath );
	}

	protected Document inspect( Inspector inspector, String path )
	{
		TypeAndNames typeAndNames = XmlUtils.parsePath( path );
		return inspector.inspect( mToInspect, typeAndNames.getType(), typeAndNames.getNames() );
	}

	protected SwingMetawidget createMetawidget( Map<String, String> attributes )
		throws Exception
	{
		try
		{
			Constructor<? extends SwingMetawidget> constructor = getClass().getConstructor( getClass() );
			SwingMetawidget metawidget = constructor.newInstance( this );
			metawidget.setOpaque( isOpaque() );

			return metawidget;
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
	}

	protected void initMetawidget( SwingMetawidget metawidget, Map<String, String> attributes )
	{
		metawidget.setPath( mPath + StringUtils.SEPARATOR_SLASH + attributes.get( NAME ) );
		metawidget.setToInspect( mToInspect );
	}

	/**
	 * Returns the property used to get/set the value of the component.
	 * <p>
	 * If the component is not known, returns <code>null</code>. Does not throw an Exception, as
	 * we want to fail gracefully if, say, someone tries to bind to a JPanel.
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

	protected void bind( JComponent component, String... names )
	{
		Component actualComponent = component;

		// Drill into JScrollPanes

		if ( actualComponent instanceof JScrollPane )
			actualComponent = ( (JScrollPane) actualComponent ).getViewport().getView();

		mBinding.bind( actualComponent, getValueProperty( actualComponent ), names );
	}

	//
	//
	// Private methods
	//
	//

	/**
	 * Sets the JSpinner model.
	 * <p>
	 * By default, a JSpinner calls <code>setColumns</code> upon <code>setModel</code>. For
	 * numbers like <code>Integer.MAX_VALUE</code> and <code>Double.MAX_VALUE</code>, this can
	 * be very large and mess up the layout. Here, we reset <code>setColumns</code> to 0.
	 */

	private void setSpinnerModel( JSpinner spinner, int minimum, int maximum )
	{
		spinner.setModel( new SpinnerNumberModel( 0, minimum, maximum, 1 ) );
		( (JSpinner.DefaultEditor) spinner.getEditor() ).getTextField().setColumns( 0 );
	}

	/**
	 * Sets the JSpinner model.
	 * <p>
	 * By default, a JSpinner calls <code>setColumns</code> upon <code>setModel</code>. For
	 * numbers like <code>Integer.MAX_VALUE</code> and <code>Double.MAX_VALUE</code>, this can
	 * be very large and mess up the layout. Here, we reset <code>setColumns</code> to 0.
	 */

	private void setSpinnerModel( JSpinner spinner, double minimum, double maximum )
	{
		spinner.setModel( new SpinnerNumberModel( 0, minimum, maximum, 1 ) );
		( (JSpinner.DefaultEditor) spinner.getEditor() ).getTextField().setColumns( 0 );
	}

	//
	//
	// Inner class
	//
	//

	protected class SwingMetawidgetMixin
		extends MetawidgetMixin<JComponent>
	{
		//
		//
		// Public methods
		//
		//

		@Override
		protected void startBuild()
			throws Exception
		{
			SwingMetawidget.this.startBuild();
		}

		@Override
		protected Document inspect()
		{
			return SwingMetawidget.this.inspect();
		}

		@Override
		protected void addWidget( JComponent component, Map<String, String> attributes )
		{
			SwingMetawidget.this.addWidget( component, attributes );
		}

		@Override
		protected JComponent getOverridenWidget( Map<String, String> attributes )
		{
			return SwingMetawidget.this.getOverridenWidget( attributes );
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
			SwingMetawidget.this.beforeBuildCompoundWidget();

			super.buildCompoundWidget( element );
		}

		@Override
		protected JComponent buildReadOnlyWidget( Map<String, String> attributes )
			throws Exception
		{
			return SwingMetawidget.this.buildReadOnlyWidget( attributes );
		}

		@Override
		protected JComponent buildActiveWidget( Map<String, String> attributes )
			throws Exception
		{
			return SwingMetawidget.this.buildActiveWidget( attributes );
		}

		@Override
		public JComponent initMetawidget( JComponent widget, Map<String, String> attributes )
		{
			SwingMetawidget metawidget = (SwingMetawidget) widget;
			SwingMetawidget.this.initMetawidget( metawidget, attributes );
			metawidget.setReadOnly( isReadOnly( attributes ) );

			return metawidget;
		}

		@Override
		protected void endBuild()
		{
			SwingMetawidget.this.endBuild();
		}
	}

	/**
	 * Editor for values whose <code>toString</code> is not the same as the desired label.
	 * <p>
	 */

	private static class AlternateComboBoxEditor
		extends BasicComboBoxEditor
	{
		//
		//
		// Private members
		//
		//

		private Map<Object, String>	mLabels;

		//
		//
		// Constructor
		//
		//

		public AlternateComboBoxEditor( Map<Object, String> labels )
		{
			if ( labels == null )
				throw new NullPointerException( "labels" );

			mLabels = labels;
		}

		//
		//
		// Public methods
		//
		//

		@Override
		public void setItem( Object item )
		{
			super.setItem( mLabels.get( item ) );
		}
	}

	/**
	 * Renderer for values whose <code>toString</code> is not the same as the desired label.
	 */

	private static class AlternateComboBoxRenderer
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

		private Map<Object, String>	mLabels;

		//
		//
		// Constructor
		//
		//

		public AlternateComboBoxRenderer( Map<Object, String> labels )
		{
			if ( labels == null )
				throw new NullPointerException( "labels" );

			mLabels = labels;
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

			String label = mLabels.get( value );

			if ( label != null )
				( (JLabel) component ).setText( label );

			return component;
		}
	}
}
