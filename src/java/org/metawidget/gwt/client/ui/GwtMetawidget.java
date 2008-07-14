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

package org.metawidget.gwt.client.ui;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.util.simple.StringUtils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import org.metawidget.gwt.client.binding.Binding;
import org.metawidget.gwt.client.binding.BindingFactory;
import org.metawidget.gwt.client.inspector.InspectorFactory;
import org.metawidget.gwt.client.ui.layout.FlexTableLayout;
import org.metawidget.gwt.client.ui.layout.Layout;
import org.metawidget.gwt.client.ui.layout.LayoutFactory;
import org.metawidget.inspector.gwt.remote.client.GwtRemoteInspectorProxy;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.util.simple.StringUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;

/**
 * Metawidget for GWT environments.
 * <p>
 * Automatically creates native GWT Widgets, such as <code>TextBox</code> and <code>ListBox</code>,
 * to suit the inspected fields.
 * <p>
 * GWT compiles Java to JavaScript, and JavaScript lacks Java's comprehensive reflection support.
 * The only viable Inspector the JavaScript could run would be XmlInspector, and even that would
 * have to be considerably rewritten as GWT supplies its own variant of <code>org.w3c.dom</code>.
 * <p>
 * A more interesting solution is to have the JavaScript client send its objects (via AJAX) to the
 * Java server for inspection. The full power of Java Inspectors can then be brought to bear,
 * including inspecting annotations and server-side configuration files (such as
 * <code>hibernate.cfg.xml</code>).
 *
 * @author Richard Kennard
 */

public class GwtMetawidget
	extends FlowPanel
	implements HasName
{
	//
	//
	// Private statics
	//
	//

	private final static int										BUILDING_COMPLETE		= 0;

	private final static int										BUILDING_IN_PROGRESS	= 1;

	private final static int										BUILDING_NEEDED			= 2;

	/**
	 * Delay before rebuilding widgets (in milliseconds).
	 * <p>
	 * GWT does not define a good 'paint' method to override, so we must call
	 * <code>buildWidgets</code> after every <code>invalidateWidgets</code>. Many methods (eg.
	 * most setters) trigger <code>invalidateWidgets</code>, however, so we impose a short delay
	 * to try and 'batch' multiple <code>buildWidgets</code> requests (and their associated AJAX
	 * calls) into one.
	 */

	private final static int										BUILD_DELAY				= 50;

	/**
	 * Static cache of Inspectors.
	 * <p>
	 * Note this needn't be <code>synchronized</code> like the SwingMetawidget one, because
	 * JavaScript is not multi-threaded.
	 */

	private final static Map<Class<? extends Inspector>, Inspector>	INSPECTORS				= new HashMap<Class<? extends Inspector>, Inspector>();

	//
	//
	// Private members
	//
	//

	private Object													mToInspect;

	private Class<? extends Layout>									mLayoutClass			= FlexTableLayout.class;

	private Layout													mLayout;

	private Class<? extends Inspector>								mInspectorClass			= GwtRemoteInspectorProxy.class;

	private Inspector												mInspector;

	/**
	 * The Binding class.
	 * <p>
	 * Binding class is <code>null</code> by default, because setting up Binding is non-trivial
	 * (eg. you have to generate some SimpleBindingAdapters)
	 */

	private Class<? extends Binding>								mBindingClass;

	private Binding													mBinding;

	private String													mDictionaryName;

	private Dictionary												mDictionary;

	private Map<String, Object>										mParameters;

	private Map<String, Facet>										mFacets					= new HashMap<String, Facet>();

	private Set<Widget>												mExistingWidgets		= new HashSet<Widget>();

	private Set<Widget>												mExistingWidgetsUnused	= new HashSet<Widget>();

	/**
	 * Map of widgets added to this Metawidget.
	 * <p>
	 * Searching for Widgets by traversing children is complicated in GWT, because not all Widgets
	 * that contain child Widgets extend a common base class. For example, both ComplexPanel and
	 * FlexTable can contain child Widgets but have very different APIs. It is easier to keep a
	 * separate Map of the widgets we have encountered.
	 */

	private Map<String, Widget>										mAddedWidgets			= new HashMap<String, Widget>();

	private Timer													mBuildWidgets;

	//
	//
	// Package-level members
	//
	//

	String															mType;

	String															mName;

	String[]														mNamesPrefix;

	int																mNeedToBuildWidgets;

	String															mLastInspection;

	boolean															mIgnoreAddRemove;

	/**
	 * For unit tests.
	 */

	Timer															mExecuteAfterBuildWidgets;

	GwtMetawidgetMixin<Widget>										mMetawidgetMixin;

	//
	//
	// Constructor
	//
	//

	public GwtMetawidget()
	{
		mMetawidgetMixin = newMetawidgetMixin();
	}

	//
	//
	// Public methods
	//
	//

	public Object getToInspect()
	{
		return mToInspect;
	}

	/**
	 * Sets the Object to inspect.
	 */

	public void setToInspect( Object toInspect )
	{
		mToInspect = toInspect;

		if ( toInspect != null && mType == null )
			mType = toInspect.getClass().getName();

		invalidateInspection();
	}

	public void setName( String name )
	{
		mName = name;
		invalidateInspection();
	}

	public String getName()
	{
		return mName;
	}

	public void setInspectorClass( Class<? extends Inspector> inspectorClass )
	{
		mInspectorClass = inspectorClass;
		invalidateInspection();
	}

	public void setLayoutClass( Class<? extends Layout> layoutClass )
	{
		mLayoutClass = layoutClass;
		invalidateWidgets();
	}

	/**
	 * @param bindingClass
	 *            may be null
	 */

	public void setBindingClass( Class<? extends Binding> bindingClass )
	{
		mBindingClass = bindingClass;
		invalidateWidgets();
	}

	/**
	 * Set the Dictionary name for localization.
	 * <p>
	 * The Dictionary name must be a JavaScript variable declared in the host HTML page.
	 */

	public void setDictionaryName( String dictionaryName )
	{
		mDictionaryName = dictionaryName;
		mDictionary = null;
		invalidateWidgets();
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
	 * Sets the parameter value. Parameters are passed to the chosen <code>Layout</code> or
	 * <code>Binding</code> implementation.
	 */

	public void setParameter( String name, Object value )
	{
		if ( mParameters == null )
			mParameters = new HashMap<String, Object>();

		mParameters.put( name, value );

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
		if ( mDictionaryName == null )
			return null;

		try
		{
			if ( mDictionary == null )
				mDictionary = Dictionary.getDictionary( mDictionaryName );

			return mDictionary.get( key );
		}
		catch ( MissingResourceException e )
		{
			return StringUtils.RESOURCE_KEY_NOT_FOUND_PREFIX + key + StringUtils.RESOURCE_KEY_NOT_FOUND_SUFFIX;
		}
	}

	public void setReadOnly( boolean readOnly )
	{
		if ( mMetawidgetMixin.isReadOnly() == readOnly )
			return;

		mMetawidgetMixin.setReadOnly( readOnly );
		invalidateWidgets();
	}

	public boolean isReadOnly()
	{
		return mMetawidgetMixin.isReadOnly();
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

	/**
	 * Finds the widget with the given name.
	 */

	public Widget findWidget( String... names )
	{
		if ( names == null )
			return null;

		if ( mNeedToBuildWidgets != BUILDING_COMPLETE )
			throw new RuntimeException( "Widgets still building asynchronously: need to complete before calling findWidget( " + GwtUtils.toString( names, SEPARATOR_DOT_CHAR ) + ")" );

		Map<String, Widget> children = mAddedWidgets;

		for ( int loop = 0, length = names.length; loop < length; loop++ )
		{
			if ( children == null )
				return null;

			String name = names[loop];
			Widget widget = children.get( name );

			if ( widget == null )
				return null;

			if ( loop == length - 1 )
				return widget;

			if ( !( widget instanceof GwtMetawidget ) )
				return null;

			children = ( (GwtMetawidget) widget ).mAddedWidgets;
		}

		return null;
	}

	/**
	 * Gets the value from the Widget with the given name.
	 * <p>
	 * The value is returned as it is stored in the Widget (eg. String for TextBox) so may need some
	 * conversion before being reapplied to the object being inspected. This obviously requires
	 * knowledge of which Widget GwtMetawidget created, which is not ideal, so clients may prefer to
	 * use binding instead.
	 */

	public Object getValue( String... names )
	{
		Widget widget = findWidget( names );

		if ( widget == null )
			throw new RuntimeException( "No such widget " + GwtUtils.toString( names, SEPARATOR_DOT_CHAR ) );

		return getValue( widget );
	}

	/**
	 * Gets the value from the given Widget.
	 */

	public Object getValue( Widget widget )
	{
		// CheckBox (must come before HasText, because CheckBox extends
		// ButtonBase which implements HasHTML which extends HasText)

		if ( widget instanceof CheckBox )
			return ( (CheckBox) widget ).isChecked();

		// HasText

		if ( widget instanceof HasText )
			return ( (HasText) widget ).getText();

		// ListBox

		if ( widget instanceof ListBox )
		{
			ListBox listBox = (ListBox) widget;
			return listBox.getValue( listBox.getSelectedIndex() );
		}

		// Unknown (subclasses should override this)

		throw new RuntimeException( "Don't know how to getValue from a " + widget.getClass().getName() );
	}

	/**
	 * Sets the Widget with the given name to the specified value.
	 * <p>
	 * Clients must ensure the value is of the correct type to suit the Widget (eg. String for
	 * TextBox). This obviously requires knowledge of which Widget GwtMetawidget created, which is
	 * not ideal, so clients may prefer to use binding instead.
	 */

	public void setValue( Object value, String... names )
	{
		Widget widget = findWidget( names );

		if ( widget == null )
			throw new RuntimeException( "No such widget " + GwtUtils.toString( names, SEPARATOR_DOT_CHAR ) );

		setValue( value, widget );
	}

	/**
	 * Sets the given Widget to the specified value.
	 */

	public void setValue( Object value, Widget widget )
	{
		// HasText

		if ( widget instanceof HasText )
		{
			( (HasText) widget ).setText( StringUtils.quietValueOf( value ) );
			return;
		}

		// CheckBox

		if ( widget instanceof CheckBox )
		{
			( (CheckBox) widget ).setChecked( (Boolean) value );
			return;
		}

		// ListBox

		if ( widget instanceof ListBox )
		{
			GwtUtils.setListBoxSelectedItem( (ListBox) widget, StringUtils.quietValueOf( value ) );
			return;
		}

		// Panel (fail gracefully for MASKED fields)

		if ( widget instanceof SimplePanel )
			return;

		// Unknown (subclasses should override this)

		throw new RuntimeException( "Don't know how to setValue of a " + widget.getClass().getName() );
	}

	/**
	 * Rebinds the values in the UI to the given Object.
	 * <p>
	 * <code>rebind</code> can be thought of as a lightweight version of <code>setToInspect</code>.
	 * Unlike <code>setToInspect</code>, <code>rebind</code> does <em>not</em> reinspect the
	 * Object or recreate any <code>Widgets</code>. Rather, <code>rebind</code> applies only at
	 * the binding level, and updates the binding with values from the given Object.
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
	 * @throws RuntimeException
	 *             if no binding configured
	 */

	public void rebind( Object toRebind )
	{
		if ( mNeedToBuildWidgets != BUILDING_COMPLETE )
			throw new RuntimeException( "Widgets still building asynchronously: need to complete before calling rebind()" );

		if ( mBinding == null )
			throw new RuntimeException( "No binding configured. Use GwtMetawidget.setBindingClass" );

		mToInspect = toRebind;
		mBinding.rebind();

		for ( Widget widget : mAddedWidgets.values() )
		{
			if ( widget instanceof GwtMetawidget )
			{
				( (GwtMetawidget) widget ).rebind( toRebind );
			}
		}
	}

	/**
	 * Saves the values from the binding back to the Object being inspected.
	 *
	 * @throws RuntimeException
	 *             if no binding configured
	 */

	public void save()
	{
		if ( mNeedToBuildWidgets != BUILDING_COMPLETE )
			throw new RuntimeException( "Widgets still building asynchronously: need to complete before calling save()" );

		if ( mBinding == null )
			throw new RuntimeException( "No binding configured. Use GwtMetawidget.setBindingClass" );

		mBinding.save();

		// Having a save() method avoids having to expose a getBinding() method, which is handy
		// because we can worry about nested Metawidgets here, not in the Binding class

		for ( Widget widget : mAddedWidgets.values() )
		{
			if ( widget instanceof GwtMetawidget )
			{
				( (GwtMetawidget) widget ).save();
			}
		}
	}

	public Facet getFacet( String name )
	{
		return mFacets.get( name );
	}

	@Override
	public void add( Widget widget )
	{
		if ( !mIgnoreAddRemove )
		{
			invalidateWidgets();

			if ( widget instanceof Facet )
			{
				Facet facet = (Facet) widget;
				mFacets.put( facet.getName(), facet );
			}
			else
			{
				mExistingWidgets.add( widget );
			}

			// Because of the lag between invalidateWidgets() and buildWidgets(), and
			// because some CSS styles aren't applied until buildWidgets(), we
			// see a visual 'glitch' when adding new widgets (like buttons). To stop
			// this, we don't call super.add directly when !mIgnoreAddRemove

			return;
		}

		super.add( widget );
	}

	@Override
	public void insert( Widget widget, int beforeIndex )
	{
		if ( !mIgnoreAddRemove )
		{
			invalidateWidgets();

			if ( widget instanceof Facet )
			{
				Facet facet = (Facet) widget;
				mFacets.put( facet.getName(), facet );
			}
			else
			{
				mExistingWidgets.add( widget );
			}

			// Because of the lag between invalidateWidgets() and buildWidgets(), and
			// because some CSS styles aren't applied until buildWidgets(), we
			// see a visual 'glitch' when inserting new widgets (like buttons). To stop
			// this, we don't call super.insert directly when !mIgnoreAddRemove

			return;
		}

		super.insert( widget, beforeIndex );
	}

	@Override
	public boolean remove( int index )
	{
		if ( !mIgnoreAddRemove )
		{
			invalidateWidgets();

			Widget widget = getChildren().get( index );

			if ( widget instanceof Facet )
			{
				mFacets.remove( ( (Facet) widget ).getName() );
			}
			else
			{
				mExistingWidgets.remove( widget );
			}
		}

		return super.remove( index );
	}

	@Override
	public boolean remove( Widget widget )
	{
		if ( !mIgnoreAddRemove )
		{
			invalidateWidgets();

			if ( widget instanceof Facet )
			{
				mFacets.remove( ( (Facet) widget ).getName() );
			}
			else
			{
				mExistingWidgets.remove( widget );
			}
		}

		return super.remove( widget );
	}

	@Override
	public void clear()
	{
		super.clear();

		if ( !mIgnoreAddRemove )
		{
			invalidateWidgets();

			mFacets.clear();
			mExistingWidgets.clear();
		}
	}

	//
	//
	// Protected methods
	//
	//

	/**
	 * Instantiate the MetawidgetMixin used by this Metawidget.
	 * <p>
	 * Subclasses wishing to use their own MetawidgetMixin should override this method to
	 * instantiate their version.
	 */

	protected GwtMetawidgetMixin<Widget> newMetawidgetMixin()
	{
		return new GwtMetawidgetMixinImpl();
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
	 * <p>
	 * If the widgets are already invalidated, but rebuilding is not yet in progress, cancels the
	 * pending rebuild and resets the timer. This tries to 'batch' multiple invalidate requests into
	 * one.
	 */

	protected void invalidateWidgets()
	{
		// If widgets are already invalidated...

		if ( mNeedToBuildWidgets == BUILDING_NEEDED )
		{
			// ...cancel the pending rebuild...

			mBuildWidgets.cancel();
		}
		else
		{
			mNeedToBuildWidgets = BUILDING_NEEDED;

			// ...otherwise, clear the widgets

			super.clear();
			mAddedWidgets.clear();

			mNamesPrefix = null;

			if ( mBinding != null )
			{
				mBinding.unbind();
				mBinding = null;
			}
		}

		// Schedule a new build

		mBuildWidgets = new Timer()
		{
			@Override
			public void run()
			{
				buildWidgets();
			}
		};

		mBuildWidgets.schedule( BUILD_DELAY );
	}

	/**
	 * Builds the widgets.
	 * <p>
	 * Unlike <code>buildWidgets</code> in other Metawidget implementations, this method may be
	 * asynchronous. If the <code>GwtMetawidget</code> is using an <code>GwtInspectorAsync</code>
	 * Inspector (which it does by default), clients should not expect the widgets to be built by
	 * the time this method returns.
	 */

	protected void buildWidgets()
	{
		// No need to build?

		if ( mNeedToBuildWidgets != BUILDING_NEEDED )
		{
			// For unit tests: if buildWidgets is already underway, rely on
			// mExecuteAfterBuildWidgets being injected into it. This is preferrable to running
			// buildWidgets() twice without calling invalidateWidgets()

			if ( mNeedToBuildWidgets == BUILDING_COMPLETE && mExecuteAfterBuildWidgets != null )
			{
				Timer executeAfterBuildWidgets = mExecuteAfterBuildWidgets;
				mExecuteAfterBuildWidgets = null;

				executeAfterBuildWidgets.run();
			}

			return;
		}

		mNeedToBuildWidgets = BUILDING_IN_PROGRESS;

		if ( mToInspect != null )
		{
			if ( mLastInspection == null )
			{
				// If this Inspector has been set externally, use it...

				if ( mInspector == null )
				{
					// ...otherwise, if this InspectorConfig has already been created, use it...

					mInspector = INSPECTORS.get( mInspectorClass );

					// ...otherwise, initialize the Inspector

					if ( mInspector == null )
					{
						mInspector = ( (InspectorFactory) GWT.create( InspectorFactory.class ) ).newInspector( mInspectorClass );
						INSPECTORS.put( mInspectorClass, mInspector );
					}
				}

				// Special support for GwtRemoteInspectorProxy

				if ( mInspector instanceof GwtRemoteInspectorProxy )
				{
					String[] names = GwtUtils.fromStringToArray( mName, StringUtils.SEPARATOR_FORWARD_SLASH_CHAR );
					( (GwtRemoteInspectorProxy) mInspector ).inspect( mToInspect, mType, names, new AsyncCallback<String>()
					{
						public void onFailure( Throwable caught )
						{
							GwtUtils.alert( caught );

							mNeedToBuildWidgets = BUILDING_COMPLETE;
						}

						public void onSuccess( String xml )
						{
							mLastInspection = xml;

							try
							{
								mIgnoreAddRemove = true;
								mMetawidgetMixin.buildWidgets( mLastInspection );
							}
							catch ( Exception e )
							{
								GwtUtils.alert( e );
							}
							finally
							{
								mIgnoreAddRemove = false;
							}

							mNeedToBuildWidgets = BUILDING_COMPLETE;

							// For unit tests

							if ( mExecuteAfterBuildWidgets != null )
							{
								Timer executeAfterBuildWidgets = mExecuteAfterBuildWidgets;
								mExecuteAfterBuildWidgets = null;

								executeAfterBuildWidgets.run();
							}
						}
					} );

					return;
				}
			}

			// Regular GwtInspectors

			try
			{
				mIgnoreAddRemove = true;

				if ( mLastInspection == null )
				{
					String[] names = GwtUtils.fromStringToArray( mName, StringUtils.SEPARATOR_FORWARD_SLASH_CHAR );
					mLastInspection = mInspector.inspect( mToInspect, mType, names );
				}

				mMetawidgetMixin.buildWidgets( mLastInspection );
			}
			catch ( Exception e )
			{
				GwtUtils.alert( e );
			}
			finally
			{
				mIgnoreAddRemove = false;
			}

			mNeedToBuildWidgets = BUILDING_COMPLETE;

			// For unit tests

			if ( mExecuteAfterBuildWidgets != null )
			{
				mExecuteAfterBuildWidgets.run();
				mExecuteAfterBuildWidgets = null;
			}
		}
	}

	protected void startBuild()
		throws Exception
	{
		mExistingWidgetsUnused = new HashSet<Widget>( mExistingWidgets );

		// Start layout
		//
		// (we start a new layout each time, rather than complicating the Layouts with a
		// layoutCleanup method)

		mLayout = ( (LayoutFactory) GWT.create( LayoutFactory.class ) ).newLayout( mLayoutClass, this );
		mLayout.layoutBegin();

		// Start binding

		if ( mBindingClass != null )
			mBinding = ( (BindingFactory) GWT.create( BindingFactory.class ) ).newBinding( mBindingClass, this );
	}

	protected Widget getOverridenWidget( Map<String, String> attributes )
	{
		String name = attributes.get( NAME );

		if ( name == null )
			return null;

		Widget widget = null;

		for ( Widget widgetExisting : mExistingWidgetsUnused )
		{
			if ( !( widgetExisting instanceof HasName ) )
				continue;

			if ( name.equals( ( (HasName) widgetExisting ).getName() ) )
			{
				widget = widgetExisting;
				break;
			}
		}

		if ( widget != null )
			mExistingWidgetsUnused.remove( widget );

		return widget;
	}

	protected void beforeBuildCompoundWidget( Element element )
	{
		mNamesPrefix = GwtUtils.fromStringToArray( mName, StringUtils.SEPARATOR_FORWARD_SLASH_CHAR );
	}

	protected Widget buildReadOnlyWidget( Map<String, String> attributes )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		// Masked (return a Panel, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
			return new SimplePanel();

		String type = attributes.get( TYPE );

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
			return new Label();

		// If no type, fail gracefully with a Label

		if ( type == null || "".equals( type ) )
			return new Label();

		if ( GwtUtils.isPrimitive( type ) || GwtUtils.isPrimitiveWrapper( type ) )
			return new Label();

		if ( String.class.getName().equals( type ) )
			return new Label();

		if ( Date.class.getName().equals( type ) )
			return new Label();

		// Collections

		if ( isCollection( type ) )
			return null;

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return new Label();

		// Nested Metawidget

		return createMetawidget();
	}

	protected Widget buildActiveWidget( Map<String, String> attributes )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		String type = attributes.get( TYPE );

		// If no type, fail gracefully with a TextBox

		if ( type == null || "".equals( type ) )
			return new TextBox();

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
		{
			ListBox listBox = new ListBox();
			listBox.setVisibleItemCount( 1 );

			addListBoxItems( listBox, GwtUtils.fromString( lookup, StringUtils.SEPARATOR_COMMA_CHAR ), GwtUtils.fromString( attributes.get( LOOKUP_LABELS ), StringUtils.SEPARATOR_COMMA_CHAR ), attributes );
			return listBox;
		}

		if ( GwtUtils.isPrimitive( type ) )
		{
			// booleans

			if ( "boolean".equals( type ) )
				return new CheckBox();

			// chars

			if ( "char".equals( type ) )
			{
				TextBox textbox = new TextBox();
				textbox.setMaxLength( 1 );

				return textbox;
			}

			// Everything else

			return new TextBox();
		}

		// Strings

		if ( String.class.getName().equals( type ) )
		{
			if ( TRUE.equals( attributes.get( MASKED ) ) )
				return new PasswordTextBox();

			if ( TRUE.equals( attributes.get( LARGE ) ) )
				return new TextArea();

			TextBox textBox = new TextBox();

			String maximumLength = attributes.get( MAXIMUM_LENGTH );

			if ( maximumLength != null && !"".equals( maximumLength ) )
				textBox.setMaxLength( Integer.parseInt( maximumLength ) );

			return textBox;
		}

		// Dates

		if ( Date.class.getName().equals( type ) )
			return new TextBox();

		if ( GwtUtils.isPrimitiveWrapper( type ) )
		{
			// Booleans (are tri-state)

			if ( Boolean.class.getName().equals( type ) )
			{
				ListBox listBox = new ListBox();
				addListBoxItem( listBox, null, null );
				addListBoxItem( listBox, "true", "True" );
				addListBoxItem( listBox, "false", "False" );

				return listBox;
			}

			// Characters

			if ( Character.class.getName().equals( type ) )
			{
				TextBox textbox = new TextBox();
				textbox.setMaxLength( 1 );

				return textbox;
			}

			// Numbers

			return new TextBox();
		}

		// Collections

		if ( isCollection( type ) )
			return null;

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return new TextBox();

		// Nested Metawidget

		return createMetawidget();
	}

	protected Widget afterBuildWidget( Widget widget, Map<String, String> attributes )
	{
		if ( widget == null )
			return null;

		// CSS

		String styleName = getStyleName();

		if ( styleName != null )
			widget.setStyleName( styleName );

		return widget;
	}

	protected void addWidget( Widget widget, Map<String, String> attributes )
		throws Exception
	{
		String name = attributes.get( "name" );
		mAddedWidgets.put( name, widget );

		// Layout

		mLayout.layoutChild( widget, attributes );

		// Bind

		if ( mBinding != null && !( widget instanceof GwtMetawidget ) )
		{
			if ( mNamesPrefix == null )
			{
				mBinding.bind( widget, name );
			}
			else
			{
				String[] names = new String[mNamesPrefix.length + 1];

				System.arraycopy( mNamesPrefix, 0, names, 0, mNamesPrefix.length );
				names[mNamesPrefix.length] = name;

				mBinding.bind( widget, names );
			}
		}
	}

	/**
	 * Subclasses should override to instantiate their own flavour of GwtMetawidget.
	 */

	protected GwtMetawidget createMetawidget()
	{
		return new GwtMetawidget();
	}

	protected Widget initMetawidget( GwtMetawidget metawidget, Map<String, String> attributes )
		throws Exception
	{
		metawidget.setInspectorClass( mInspectorClass );

		if ( mName == null )
			metawidget.setName( attributes.get( NAME ) );
		else
			metawidget.setName( mName + SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME ) );

		metawidget.setLayoutClass( mLayoutClass );
		metawidget.setBindingClass( mBindingClass );
		metawidget.setDictionaryName( mDictionaryName );

		if ( mParameters != null )
			metawidget.mParameters = new HashMap<String, Object>( mParameters );

		metawidget.setToInspect( mToInspect );

		return metawidget;
	}

	protected void endBuild()
		throws Exception
	{
		if ( mExistingWidgetsUnused != null )
		{
			for ( Widget widgetExisting : mExistingWidgetsUnused )
			{
				Map<String, String> miscAttributes = new HashMap<String, String>();

				// Manually created components default to no section

				miscAttributes.put( SECTION, "" );

				if ( widgetExisting instanceof Stub )
					miscAttributes.putAll( ( (Stub) widgetExisting ).getAttributes() );

				mLayout.layoutChild( widgetExisting, miscAttributes );
			}
		}

		mLayout.layoutEnd();
	}

	/**
	 * Whether the given class name is a Collection. This is a crude, GWT-equivalent of...
	 * <p>
	 * <code>
	 *    Collection.class.isAssignableFrom( ... );
	 * </code>
	 * <p>
	 * ...subclasses may need to override this method if they introduce a new Collection subtype.
	 */

	protected boolean isCollection( String className )
	{
		if ( Collection.class.getName().equals( className ) )
			return true;

		if ( List.class.getName().equals( className ) || ArrayList.class.getName().equals( className ) )
			return true;

		if ( Set.class.getName().equals( className ) || HashSet.class.getName().equals( className ) )
			return true;

		if ( Map.class.getName().equals( className ) || HashMap.class.getName().equals( className ) )
			return true;

		return false;
	}

	//
	//
	// Inner class
	//
	//

	protected class GwtMetawidgetMixinImpl
		extends GwtMetawidgetMixin<Widget>
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
			GwtMetawidget.this.startBuild();
		}

		@Override
		protected Widget initMetawidget( Widget widget, Map<String, String> attributes )
			throws Exception
		{
			GwtMetawidget metawidget = (GwtMetawidget) widget;
			metawidget.setReadOnly( isReadOnly( attributes ) );
			metawidget.setMaximumInspectionDepth( getMaximumInspectionDepth() - 1 );

			return GwtMetawidget.this.initMetawidget( metawidget, attributes );
		}

		@Override
		protected Widget getOverridenWidget( Map<String, String> attributes )
		{
			return GwtMetawidget.this.getOverridenWidget( attributes );
		}

		@Override
		protected Map<String, String> getStubAttributes( Widget stub )
		{
			return ( (Stub) stub ).getAttributes();
		}

		@Override
		protected void buildCompoundWidget( Element element )
			throws Exception
		{
			GwtMetawidget.this.beforeBuildCompoundWidget( element );
			super.buildCompoundWidget( element );
		}

		@Override
		protected Widget buildWidget( Map<String, String> attributes )
			throws Exception
		{
			Widget widget = super.buildWidget( attributes );

			return GwtMetawidget.this.afterBuildWidget( widget, attributes );
		}

		@Override
		protected Widget buildReadOnlyWidget( Map<String, String> attributes )
			throws Exception
		{
			return GwtMetawidget.this.buildReadOnlyWidget( attributes );
		}

		@Override
		protected Widget buildActiveWidget( Map<String, String> attributes )
			throws Exception
		{
			return GwtMetawidget.this.buildActiveWidget( attributes );
		}

		@Override
		protected void addWidget( Widget widget, Map<String, String> attributes )
			throws Exception
		{
			GwtMetawidget.this.addWidget( widget, attributes );
		}

		@Override
		protected boolean isMetawidget( Widget widget )
		{
			return ( widget instanceof GwtMetawidget );
		}

		@Override
		protected boolean isStub( Widget widget )
		{
			return ( widget instanceof Stub );
		}

		@Override
		protected void endBuild()
			throws Exception
		{
			GwtMetawidget.this.endBuild();
		}
	}

	//
	//
	// Private methods
	//
	//

	private void addListBoxItems( ListBox listBox, List<String> values, List<String> labels, Map<String, String> attributes )
	{
		if ( values == null )
			return;

		// Add an empty choice (if nullable)
		//
		// Note: GWT doesn't seem to be able to set null for the
		// value. It always comes back as String "null"

		if ( !GwtUtils.isPrimitive( attributes.get( TYPE ) ) )
			addListBoxItem( listBox, "", null );

		// See if we're using labels

		if ( labels != null && !labels.isEmpty() && labels.size() != values.size() )
			throw new RuntimeException( "Labels list must be same size as values list" );

		// Add the select items

		for ( int loop = 0, length = values.size(); loop < length; loop++ )
		{
			String value = values.get( loop );
			String label = null;

			if ( labels != null && !labels.isEmpty() )
				label = labels.get( loop );

			addListBoxItem( listBox, value, label );
		}
	}

	private void addListBoxItem( ListBox listBox, String value, String label )
	{
		if ( label != null )
		{
			listBox.addItem( label, value );
			return;
		}

		listBox.addItem( value );
	}
}
