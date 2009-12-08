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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import org.metawidget.gwt.client.ui.layout.FlexTableLayout;
import org.metawidget.gwt.client.widgetbuilder.impl.GwtWidgetBuilder;
import org.metawidget.gwt.client.widgetbuilder.impl.OverriddenWidgetBuilder;
import org.metawidget.gwt.client.widgetbuilder.impl.ReadOnlyWidgetBuilder;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspector.gwt.remote.client.GwtRemoteInspectorProxy;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.layout.iface.Layout;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.util.simple.PathUtils.TypeAndNames;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilderConfig;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;

/**
 * Metawidget for GWT environments.
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
	// Private statics
	//

	private final static int							BUILDING_COMPLETE		= 0;

	private final static int							BUILDING_IN_PROGRESS	= 1;

	private final static int							BUILDING_NEEDED			= 2;

	/**
	 * Delay before rebuilding widgets (in milliseconds).
	 * <p>
	 * GWT does not define a good 'paint' method to override, so we must call
	 * <code>buildWidgets</code> after every <code>invalidateWidgets</code>. Many methods (eg. most
	 * setters) trigger <code>invalidateWidgets</code>, however, so we impose a short delay to try
	 * and 'batch' multiple <code>buildWidgets</code> requests (and their associated AJAX calls)
	 * into one.
	 */

	private final static int							BUILD_DELAY				= 50;

	/**
	 * Static cache of the default Inspector.
	 * <p>
	 * Note this needn't be <code>synchronized</code> because JavaScript is not multi-threaded.
	 * <p>
	 * <code>GWTMetawidget</code> cannot use our <code>ConfigReader</code>, because that relies
	 * heavily on reflection which is not available client-side. Note
	 * <code>GwtRemoteInspectorProxy</code> <em>does</em> use <code>ConfigReader</code>.
	 */

	private static Inspector							DEFAULT_INSPECTOR;

	private static WidgetBuilder<Widget, GwtMetawidget>	DEFAULT_WIDGETBUILDER;

	private static Layout<Widget, GwtMetawidget>		DEFAULT_LAYOUT;

	//
	// Private members
	//

	private Object										mToInspect;

	private String										mDictionaryName;

	private Dictionary									mDictionary;

	private Map<String, Facet>							mFacets					= new HashMap<String, Facet>();

	private Set<Widget>									mExistingWidgets		= new HashSet<Widget>();

	private Set<Widget>									mExistingUnusedWidgets	= new HashSet<Widget>();

	/**
	 * Map of widgets added to this Metawidget.
	 * <p>
	 * Searching for Widgets by traversing children is complicated in GWT, because not all Widgets
	 * that contain child Widgets extend a common base class. For example, both ComplexPanel and
	 * FlexTable can contain child Widgets but have very different APIs. It is easier to keep a
	 * separate Map of the widgets we have encountered.
	 */

	private Map<String, Widget>							mAddedWidgets			= new HashMap<String, Widget>();

	private Timer										mBuildWidgets;

	private Map<Object, Object>							mClientProperties;

	//
	// Package-private members
	//

	/* package private */String							mPath;

	/**
	 * Name used to implement <code>HasName</code>. Subtly different from <code>mPath</code> and
	 * <code>mNamesPrefix</code>.
	 */

	/* package private */String							mName;

	/* package private */int							mNeedToBuildWidgets;

	/* package private */Element						mLastInspection;

	/* package private */boolean						mIgnoreAddRemove;

	/**
	 * For unit tests.
	 */

	/* package private */Timer							mExecuteAfterBuildWidgets;

	/* package private */Pipeline						mPipeline;

	//
	// Constructor
	//

	public GwtMetawidget()
	{
		mPipeline = newPipeline();
	}

	//
	// Public methods
	//

	/**
	 * Gets the object being inspected.
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
	 * Sets the Object to inspect.
	 * <p>
	 * If <code>setPath</code> has not been set, or points to a previous <code>setToInspect</code>,
	 * sets it to point to the given Object.
	 */

	public void setToInspect( Object toInspect )
	{
		updateToInspectWithoutInvalidate( toInspect );
		invalidateInspection();
	}

	/**
	 * Updates the Object to inspect, without invalidating the previous inspection results.
	 * <p>
	 * <strong>This is an internal API exposed for WidgetProcessor rebinding support. Clients should
	 * not call it directly.</strong>
	 */

	public void updateToInspectWithoutInvalidate( Object toInspect )
	{
		if ( mToInspect == null )
		{
			if ( mPath == null && toInspect != null )
				mPath = toInspect.getClass().getName();
		}
		else if ( mToInspect.getClass().getName().equals( mPath ) )
		{
			if ( toInspect == null )
				mPath = null;
			else
				mPath = toInspect.getClass().getName();
		}

		mToInspect = toInspect;
	}

	/**
	 * Sets the path to be inspected.
	 * <p>
	 * Note <code>setPath</code> is quite different to
	 * <code>com.google.gwt.user.client.ui.HasName.setName</code>. <code>setPath</code> is always in
	 * relation to <code>setToInspect</code>, so must include the type name and any subsequent
	 * sub-names (eg. type/name/name). Conversely, <code>setName</code> is a single name relative to
	 * our immediate parent.
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

	public void setName( String name )
	{
		mName = name;
	}

	public String getName()
	{
		return mName;
	}

	public void setInspector( Inspector inspector )
	{
		mPipeline.setInspector( inspector );
		invalidateInspection();
	}

	public void addInspectionResultProcessor( InspectionResultProcessor<Element, GwtMetawidget> inspectionResultProcessor )
	{
		mPipeline.addInspectionResultProcessor( inspectionResultProcessor );
		invalidateWidgets();
	}

	public void setWidgetBuilder( WidgetBuilder<Widget, GwtMetawidget> widgetBuilder )
	{
		mPipeline.setWidgetBuilder( widgetBuilder );
		invalidateInspection();
	}

	public void addWidgetProcessor( WidgetProcessor<Widget, GwtMetawidget> widgetProcessor )
	{
		mPipeline.addWidgetProcessor( widgetProcessor );
		invalidateInspection();
	}

	public <T> T getWidgetProcessor( Class<T> widgetProcessorClass )
	{
		return mPipeline.getWidgetProcessor( widgetProcessorClass );
	}

	public void setLayout( Layout<Widget, GwtMetawidget> layout )
	{
		mPipeline.setLayout( layout );
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
		if ( mPipeline.isReadOnly() == readOnly )
			return;

		mPipeline.setReadOnly( readOnly );
		invalidateWidgets();
	}

	public boolean isReadOnly()
	{
		return mPipeline.isReadOnly();
	}

	public int getMaximumInspectionDepth()
	{
		return mPipeline.getMaximumInspectionDepth();
	}

	public void setMaximumInspectionDepth( int maximumInspectionDepth )
	{
		mPipeline.setMaximumInspectionDepth( maximumInspectionDepth );
		invalidateWidgets();
	}

	/**
	 * Gets the widget with the given name.
	 */

	@SuppressWarnings( "unchecked" )
	public <T extends Widget> T getWidget( String... names )
	{
		if ( names == null )
			return null;

		if ( mNeedToBuildWidgets != BUILDING_COMPLETE )
			throw new RuntimeException( "Widgets still building asynchronously: need to complete before calling getWidget( \"" + GwtUtils.toString( names, SEPARATOR_DOT_CHAR ) + "\" )" );

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
				return (T) widget;

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
	 *
	 * @return the value. Note this return type uses generics, so as to not require a cast by the
	 *         caller (eg. <code>String s = getValue(names)</code>)
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getValue( String... names )
	{
		Widget widget = getWidget( names );

		if ( widget == null )
			throw new RuntimeException( "No such widget " + GwtUtils.toString( names, SEPARATOR_DOT_CHAR ) );

		return (T) getValue( widget );
	}

	/**
	 * Gets the value from the given Widget.
	 *
	 * @return the value. Note this return type uses generics, so as to not require a cast by the
	 *         caller (eg. <code>String s = getValue(widget)</code>)
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getValue( Widget widget )
	{
		return (T) getValue( widget, mPipeline.getWidgetBuilder() );
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
		Widget widget = getWidget( names );

		if ( widget == null )
			throw new RuntimeException( "No such widget " + GwtUtils.toString( names, SEPARATOR_DOT_CHAR ) );

		setValue( value, widget );
	}

	/**
	 * Sets the given Widget to the specified value.
	 */

	public void setValue( Object value, Widget widget )
	{
		if ( !setValue( value, widget, mPipeline.getWidgetBuilder() ) )
			throw new RuntimeException( "Don't know how to setValue of a " + widget.getClass().getName() );
	}

	public Facet getFacet( String name )
	{
		return mFacets.get( name );
	}

	/**
	 * Storage area for WidgetProcessors, Layouts, and other stateless clients.
	 */

	public void putClientProperty( Object key, Object value )
	{
		if ( mClientProperties == null )
			mClientProperties = new HashMap<Object, Object>();

		mClientProperties.put( key, value );
	}

	/**
	 * Storage area for WidgetProcessors, Layouts, and other stateless clients.
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getClientProperty( Object key )
	{
		if ( mClientProperties == null )
			return null;

		return (T) mClientProperties.get( key );
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

	/**
	 * Fetch a list of <code>Widgets</code> that were added manually, and have so far not been used.
	 * <p>
	 * <strong>This is an internal API exposed for OverriddenWidgetBuilder. Clients should not call
	 * it directly.</strong>
	 */

	public Set<Widget> fetchExistingUnusedWidgets()
	{
		return mExistingUnusedWidgets;
	}

	/**
	 * This method is public for use by WidgetBuilders.
	 */

	public Inspector getInspector()
	{
		return mPipeline.getInspector();
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

	protected Pipeline newPipeline()
	{
		return new Pipeline();
	}

	@Override
	protected void add( Widget child, com.google.gwt.user.client.Element container )
	{
		if ( !mIgnoreAddRemove )
		{
			invalidateWidgets();

			if ( child instanceof Facet )
			{
				Facet facet = (Facet) child;
				mFacets.put( facet.getName(), facet );
			}
			else
			{
				mExistingWidgets.add( child );
			}

			// Because of the lag between invalidateWidgets() and buildWidgets(), and
			// because some CSS styles aren't applied until buildWidgets(), we
			// see a visual 'glitch' when adding new widgets (like buttons). To stop
			// this, we don't call super.add directly when !mIgnoreAddRemove

			return;
		}

		super.add( child, container );
	}

	@Override
	protected void insert( Widget child, com.google.gwt.user.client.Element container, int beforeIndex, boolean domInsert )
	{
		if ( !mIgnoreAddRemove )
		{
			invalidateWidgets();

			if ( child instanceof Facet )
			{
				Facet facet = (Facet) child;
				mFacets.put( facet.getName(), facet );
			}
			else
			{
				mExistingWidgets.add( child );
			}

			// Because of the lag between invalidateWidgets() and buildWidgets(), and
			// because some CSS styles aren't applied until buildWidgets(), we
			// see a visual 'glitch' when inserting new widgets (like buttons). To stop
			// this, we don't call super.insert directly when !mIgnoreAddRemove

			return;
		}

		super.insert( child, container, beforeIndex, domInsert );
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

	protected void configure()
	{
		// Sensible defaults
		//
		// We cannot use ConfigReader, because GWT's client-side JavaScript is not up to it

		if ( mPipeline.getInspector() == null )
		{
			if ( DEFAULT_INSPECTOR == null )
				DEFAULT_INSPECTOR = new GwtRemoteInspectorProxy();

			mPipeline.setInspector( DEFAULT_INSPECTOR );
		}

		if ( mPipeline.getWidgetBuilder() == null )
		{
			if ( DEFAULT_WIDGETBUILDER == null )
			{
				@SuppressWarnings( "unchecked" )
				CompositeWidgetBuilderConfig<Widget, GwtMetawidget> config = new CompositeWidgetBuilderConfig<Widget, GwtMetawidget>().setWidgetBuilders( new OverriddenWidgetBuilder(), new ReadOnlyWidgetBuilder(), new GwtWidgetBuilder() );
				DEFAULT_WIDGETBUILDER = new CompositeWidgetBuilder<Widget, GwtMetawidget>( config );
			}

			mPipeline.setWidgetBuilder( DEFAULT_WIDGETBUILDER );
		}

		if ( mPipeline.getLayout() == null )
		{
			if ( DEFAULT_LAYOUT == null )
				DEFAULT_LAYOUT = new FlexTableLayout();

			mPipeline.setLayout( DEFAULT_LAYOUT );
		}
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

		configure();

		if ( mToInspect != null )
		{
			Inspector inspector = getInspector();

			if ( mLastInspection == null )
			{
				// Special support for GwtRemoteInspectorProxy

				if ( inspector instanceof GwtRemoteInspectorProxy )
				{
					TypeAndNames typeAndNames = PathUtils.parsePath( mPath );
					( (GwtRemoteInspectorProxy) inspector ).inspect( mToInspect, typeAndNames.getType(), typeAndNames.getNamesAsArray(), new AsyncCallback<Element>()
					{
						public void onFailure( Throwable caught )
						{
							GwtUtils.alert( caught );

							mNeedToBuildWidgets = BUILDING_COMPLETE;
						}

						public void onSuccess( Element inspectionResult )
						{
							mLastInspection = inspectionResult;

							try
							{
								mIgnoreAddRemove = true;
								mPipeline.buildWidgets( mLastInspection );
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
					TypeAndNames typeAndNames = PathUtils.parsePath( mPath );
					mLastInspection = mPipeline.inspect( mToInspect, typeAndNames.getType(), typeAndNames.getNamesAsArray() );
				}

				mPipeline.buildWidgets( mLastInspection );
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
	{
		mExistingUnusedWidgets = new HashSet<Widget>( mExistingWidgets );
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

	protected void addWidget( Widget widget, String elementName, Map<String, String> attributes )
	{
		String name = attributes.get( NAME );
		mAddedWidgets.put( name, widget );

		if ( widget instanceof HasName )
			( (HasName) widget ).setName( name );
	}

	/**
	 * Hook so subclasses can change which class gets created.
	 */

	protected GwtMetawidget buildNestedMetawidget()
	{
		return new GwtMetawidget();
	}

	protected void initNestedMetawidget( GwtMetawidget nestedMetawidget, Map<String, String> attributes )
		throws Exception
	{
		mPipeline.initNestedPipeline( nestedMetawidget.mPipeline, attributes );
		nestedMetawidget.setPath( mPath + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME ) );
		nestedMetawidget.setDictionaryName( mDictionaryName );
		nestedMetawidget.setToInspect( mToInspect );
	}

	protected void endBuild()
	{
		if ( mExistingUnusedWidgets != null )
		{
			Layout<Widget, GwtMetawidget> layout = mPipeline.getLayout();
			for ( Widget widgetExisting : mExistingUnusedWidgets )
			{
				Map<String, String> miscAttributes = new HashMap<String, String>();

				// Manually created components default to no section

				miscAttributes.put( SECTION, "" );

				if ( widgetExisting instanceof Stub )
				{
					Map<String, String> stubAttributes = ( (Stub) widgetExisting ).getAttributes();

					if ( stubAttributes != null )
						miscAttributes.putAll( stubAttributes );
				}

				layout.layoutWidget( widgetExisting, PROPERTY, miscAttributes, this, this );
			}
		}
	}

	//
	// Private members
	//

	private Object getValue( Widget widget, WidgetBuilder<Widget, GwtMetawidget> widgetBuilder )
	{
		// Recurse into CompositeWidgetBuilders

		if ( widgetBuilder instanceof CompositeWidgetBuilder<?, ?> )
		{
			for ( WidgetBuilder<Widget, GwtMetawidget> widgetBuilderChild : ( (CompositeWidgetBuilder<Widget, GwtMetawidget>) widgetBuilder ).getWidgetBuilders() )
			{
				Object value = getValue( widget, widgetBuilderChild );

				if ( value != null )
					return value;
			}

			return null;
		}

		// Interrogate GwtValueAccessors

		if ( widgetBuilder instanceof GwtValueAccessor )
			return ( (GwtValueAccessor) widgetBuilder ).getValue( widget );

		return null;
	}

	private boolean setValue( Object value, Widget widget, WidgetBuilder<Widget, GwtMetawidget> widgetBuilder )
	{
		// Recurse into CompositeWidgetBuilders

		if ( widgetBuilder instanceof CompositeWidgetBuilder<?, ?> )
		{
			for ( WidgetBuilder<Widget, GwtMetawidget> widgetBuilderChild : ( (CompositeWidgetBuilder<Widget, GwtMetawidget>) widgetBuilder ).getWidgetBuilders() )
			{
				if ( setValue( value, widget, widgetBuilderChild ) )
					return true;
			}

			return false;
		}

		// Interrogate GwtValueAccessors

		if ( widgetBuilder instanceof GwtValueAccessor )
			return ( (GwtValueAccessor) widgetBuilder ).setValue( widget, value );

		return false;
	}

	//
	// Inner class
	//

	protected class Pipeline
		extends GwtPipeline<Widget, GwtMetawidget>
	{
		//
		// Protected methods
		//

		@Override
		protected void startBuild()
		{
			GwtMetawidget.this.startBuild();
			super.startBuild();
		}

		@Override
		protected Widget buildWidget( String elementName, Map<String, String> attributes )
		{
			Widget widget = super.buildWidget( elementName, attributes );
			return GwtMetawidget.this.afterBuildWidget( widget, attributes );
		}

		@Override
		protected GwtMetawidget buildNestedMetawidget( Map<String, String> attributes )
			throws Exception
		{
			GwtMetawidget nestedMetawidget = GwtMetawidget.this.buildNestedMetawidget();
			GwtMetawidget.this.initNestedMetawidget( nestedMetawidget, attributes );

			return nestedMetawidget;
		}

		@Override
		protected Map<String, String> getAdditionalAttributes( Widget widget )
		{
			if ( widget instanceof Stub )
				return ( (Stub) widget ).getAttributes();

			return null;
		}

		@Override
		protected void addWidget( Widget widget, String elementName, Map<String, String> attributes )
		{
			GwtMetawidget.this.addWidget( widget, elementName, attributes );
			super.addWidget( widget, elementName, attributes );
		}

		@Override
		protected void endBuild()
		{
			GwtMetawidget.this.endBuild();
			super.endBuild();
		}

		@Override
		protected GwtMetawidget getPipelineOwner()
		{
			return GwtMetawidget.this;
		}
	}
}
