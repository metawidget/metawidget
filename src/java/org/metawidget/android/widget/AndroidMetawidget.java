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

package org.metawidget.android.widget;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;

import org.metawidget.MetawidgetException;
import org.metawidget.android.widget.layout.Layout;
import org.metawidget.android.widget.layout.TableLayout;
import org.metawidget.android.widget.widgetbuilder.AndroidWidgetBuilder;
import org.metawidget.inspector.ConfigReader;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.mixin.w3c.MetawidgetMixin;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.util.simple.PathUtils.TypeAndNames;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.w3c.dom.Element;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Metawidget for Android environments.
 * <p>
 * Note: this class extends <code>LinearLayout</code> rather than <code>FrameLayout</code>,
 * because <code>FrameLayout</code> would <em>always</em> need to have another
 * <code>Layout</code> embedded within it, whereas <code>LinearLayout</code> is occasionally
 * useful directly.
 *
 * @author Richard Kennard
 */

public class AndroidMetawidget
	extends LinearLayout
{
	//
	// Private statics
	//

	private static Inspector								DEFAULT_INSPECTOR;

	private static WidgetBuilder<View, AndroidMetawidget>	DEFAULT_WIDGETBUILDER;

	private static ConfigReader								CONFIG_READER;

	private final static String								PARAM_PREFIX	= "param";

	//
	// Private members
	//

	private Object											mToInspect;

	private String											mPath;

	private int												mConfig;

	private boolean											mNeedsConfiguring;

	private Class<? extends Layout>							mLayoutClass	= TableLayout.class;

	private Layout											mLayout;

	private Map<String, Object>								mParameters;

	private boolean											mNeedToBuildWidgets;

	String													mLastInspection;

	private Set<View>										mExistingViews;

	private Set<View>										mExistingViewsUnused;

	private Map<String, Facet>								mFacets;

	private AndroidMetawidgetMixin							mMetawidgetMixin;

	//
	// Constructor
	//

	public AndroidMetawidget( Context context )
	{
		super( context );
		mMetawidgetMixin = newMetawidgetMixin();

		setOrientation( LinearLayout.VERTICAL );
	}

	@SuppressWarnings( "unchecked" )
	public AndroidMetawidget( Context context, AttributeSet attributes )
	{
		super( context, attributes );
		mMetawidgetMixin = newMetawidgetMixin();

		setOrientation( LinearLayout.VERTICAL );

		// For each attribute...

		for ( int loop = 0, length = attributes.getAttributeCount(); loop < length; loop++ )
		{
			// ...that looks like a parameter...

			String name = attributes.getAttributeName( loop );

			if ( !name.startsWith( PARAM_PREFIX ) )
				continue;

			name = name.substring( PARAM_PREFIX.length() );

			if ( !StringUtils.isFirstLetterUppercase( name ) )
				continue;

			// ...remember it

			String value = attributes.getAttributeValue( loop );

			// (process resource lookups)

			if ( value.startsWith( "@" ) )
			{
				setParameter( StringUtils.lowercaseFirstLetter( name ), attributes.getAttributeResourceValue( loop, 0 ) );
				continue;
			}

			setParameter( StringUtils.lowercaseFirstLetter( name ), value );
		}

		// Support configuring inspectors in the XML

		mConfig = attributes.getAttributeResourceValue( null, "config", 0 );

		if ( mConfig != 0 )
			mNeedsConfiguring = true;

		// Support configuring layouts in the XML

		String layoutClass = attributes.getAttributeValue( null, "layout" );

		if ( layoutClass != null && !"".equals( layoutClass ) )
		{
			mLayoutClass = (Class<? extends Layout>) ClassUtils.niceForName( layoutClass );
		}

		// Support readOnly in the XML

		String readOnly = attributes.getAttributeValue( null, "readOnly" );

		if ( readOnly != null && !"".equals( readOnly ) )
		{
			mMetawidgetMixin.setReadOnly( Boolean.parseBoolean( readOnly ) );
		}
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
	 * Sets the path to be inspected.
	 * <p>
	 * Note <code>setPath</code> is quite different to <code>setTag</code>.
	 * <code>setPath</code> is always in relation to <code>setToInspect</code>, so must include
	 * the type name and any subsequent sub-names (eg. type/name/name). Conversely,
	 * <code>setTag</code> is a single name relative to our immediate parent.
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

	/**
	 * Provides an id for the inspector configuration.
	 * <p>
	 * Typically, the id will be retrieved by <code>R.raw.inspector</code>
	 */

	public void setConfig( int config )
	{
		mConfig = config;
		mNeedsConfiguring = true;
		invalidateInspection();
	}

	public void setInspector( Inspector inspector )
	{
		mMetawidgetMixin.setInspector( inspector );
		invalidateInspection();
	}

	public void setWidgetBuilder( WidgetBuilder<View, AndroidMetawidget> widgetBuilder )
	{
		mMetawidgetMixin.setWidgetBuilder( widgetBuilder );
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
		// Android doesn't support i18n yet

		return null;
	}

	/**
	 * Gets the parameter with the given name.
	 *
	 * @return the value of the parameter. Note this return type uses generics, so as to not require
	 *         a cast by the caller (eg. <code>String s = getParameter(name)</code>)
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getParameter( String name )
	{
		if ( mParameters == null )
			return null;

		return (T) mParameters.get( name );
	}

	/**
	 * Sets a parameter value.
	 */

	public void setParameter( String name, Object value )
	{
		if ( mParameters == null )
			mParameters = CollectionUtils.newHashMap();

		mParameters.put( name, value );
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
	// The following methods all kick off a buildWidgets()
	//

	@Override
	public int getChildCount()
	{
		buildWidgets();
		return super.getChildCount();
	}

	@Override
	public View getChildAt( int index )
	{
		buildWidgets();
		return super.getChildAt( index );
	}

	/**
	 * Gets the value from the View with the given name.
	 * <p>
	 * The value is returned as it is stored in the View (eg. String for EditText) so may need some
	 * conversion before being reapplied to the object being inspected. This obviously requires
	 * knowledge of which View AndroidMetawidget created, which is not ideal.
	 *
	 * @return the value from the View. Note this return type uses generics, so as to not require a
	 *         cast by the caller (eg. <code>String s = getValue(names)</code>)
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getValue( String... names )
	{
		if ( names == null )
			throw MetawidgetException.newException( "No names specified" );

		View view = findViewWithTags( names );

		if ( view == null )
			throw MetawidgetException.newException( "No view with tag " + ArrayUtils.toString( names ) );

		return (T) getValue( view, mMetawidgetMixin.getWidgetBuilder() );
	}

	/**
	 * Sets the value of the View with the given name.
	 * <p>
	 * Clients must ensure the value is of the correct type to suit the View (eg. String for
	 * EditText). This obviously requires knowledge of which View AndroidMetawidget created, which
	 * is not ideal.
	 */

	public void setValue( Object value, String... names )
	{
		if ( names == null )
			throw MetawidgetException.newException( "No names specified" );

		View view = findViewWithTags( names );

		if ( view == null )
			throw MetawidgetException.newException( "No view with tag " + ArrayUtils.toString( names ) );

		if ( !setValue( value, view, mMetawidgetMixin.getWidgetBuilder() ))
			throw MetawidgetException.newException( "Don't know how to setValue of a " + view.getClass().getName() );
	}

	public Facet getFacet( String name )
	{
		buildWidgets();

		return mFacets.get( name );
	}

	/**
	 * This method is public for use by WidgetBuilders.
	 */

	public String inspect( Object toInspect, String type, String... names )
	{
		return mMetawidgetMixin.inspect( toInspect, type, names );
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

	protected AndroidMetawidgetMixin newMetawidgetMixin()
	{
		return new AndroidMetawidgetMixin();
	}

	@Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec )
	{
		buildWidgets();
		super.onMeasure( widthMeasureSpec, heightMeasureSpec );
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

		postInvalidate();
	}

	@SuppressWarnings( "unchecked" )
	protected void configure()
	{
		if ( !mNeedsConfiguring )
			return;

		mNeedsConfiguring = false;

		try
		{
			if ( mConfig != 0 )
			{
				if ( CONFIG_READER == null )
					CONFIG_READER = new AndroidConfigReader( getContext() );

				CONFIG_READER.configure( getContext().getResources().openRawResource( mConfig ), this );
			}

			// Sensible defaults

			if ( mMetawidgetMixin.getWidgetBuilder() == null )
			{
				if ( DEFAULT_WIDGETBUILDER == null )
					DEFAULT_WIDGETBUILDER = new AndroidWidgetBuilder();

				mMetawidgetMixin.setWidgetBuilder( DEFAULT_WIDGETBUILDER );
			}

			if ( mMetawidgetMixin.getInspector() == null )
			{
				if ( DEFAULT_INSPECTOR == null )
					DEFAULT_INSPECTOR = new CompositeInspector( new CompositeInspectorConfig().setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() ) );

				mMetawidgetMixin.setInspector( DEFAULT_INSPECTOR );
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

		try
		{
			if ( mLastInspection == null )
			{
				mLastInspection = inspect();
				Log.d( getClass().getSimpleName(), "Inspection returned " + mLastInspection );
			}
			else
			{
				Log.d( getClass().getSimpleName(), "Reusing previous inspection " + mLastInspection );
			}

			mMetawidgetMixin.buildWidgets( mLastInspection );
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
	}

	protected void startBuild()
		throws Exception
	{
		Log.d( getClass().getSimpleName(), "Starting build" );

		if ( mExistingViews == null )
		{
			mExistingViews = CollectionUtils.newHashSet();
			mFacets = CollectionUtils.newHashMap();

			for ( int loop = 0, length = getChildCount(); loop < length; loop++ )
			{
				View view = getChildAt( loop );

				if ( view instanceof Facet )
				{
					Facet facet = (Facet) view;

					mFacets.put( facet.getName(), facet );
					continue;
				}

				mExistingViews.add( view );
			}
		}

		removeAllViews();

		mExistingViewsUnused = CollectionUtils.newHashSet( mExistingViews );

		// Start layout

		mLayout = mLayoutClass.getConstructor( AndroidMetawidget.class ).newInstance( this );
		mLayout.layoutBegin();
	}

	protected void addWidget( View view, String elementName, Map<String, String> attributes )
	{
		String childName = attributes.get( NAME );
		view.setTag( childName );

		if ( mLayout != null )
		{
			if ( view.getParent() != null )
				( (ViewGroup) view.getParent() ).removeView( view );
			mLayout.layoutChild( view, attributes );
		}
	}

	protected View getOverriddenWidget( String elementName, Map<String, String> attributes )
	{
		View view = null;
		String childName = attributes.get( NAME );

		if ( childName == null )
			return null;

		for ( View viewExisting : mExistingViewsUnused )
		{
			if ( childName.equals( viewExisting.getTag() ) )
			{
				view = viewExisting;
				break;
			}
		}

		if ( view != null )
			mExistingViewsUnused.remove( view );

		return view;
	}

	protected void endBuild()
	{
		// End layout

		if ( mLayout != null )
		{
			for ( View viewExisting : mExistingViewsUnused )
			{
				mLayout.layoutChild( viewExisting, null );
			}

			mLayout.layoutEnd();
		}

		Log.d( getClass().getSimpleName(), "Build complete" );
	}

	protected String inspect()
	{
		Log.d( getClass().getSimpleName(), "Starting inspection of " + mPath );

		if ( mPath == null )
			return null;

		TypeAndNames typeAndNames = PathUtils.parsePath( mPath );
		return inspect( mToInspect, typeAndNames.getType(), typeAndNames.getNamesAsArray() );
	}

	protected void initNestedMetawidget( AndroidMetawidget nestedMetawidget, Map<String, String> attributes )
	{
		nestedMetawidget.setPath( mPath + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME ) );
		nestedMetawidget.setInspector( mMetawidgetMixin.getInspector() );
		nestedMetawidget.setWidgetBuilder( mMetawidgetMixin.getWidgetBuilder() );
		nestedMetawidget.setLayoutClass( mLayoutClass );

		if ( mParameters != null )
			nestedMetawidget.mParameters = CollectionUtils.newHashMap( mParameters );

		nestedMetawidget.setToInspect( mToInspect );
	}

	//
	// Private methods
	//

	private View findViewWithTags( String... tags )
	{
		if ( tags == null )
			return null;

		ViewGroup viewgroup = this;

		for ( int tagsLoop = 0, tagsLength = tags.length; tagsLoop < tagsLength; tagsLoop++ )
		{
			Object tag = tags[tagsLoop];

			// buildWidgets just-in-time

			if ( viewgroup instanceof AndroidMetawidget )
				( (AndroidMetawidget) viewgroup ).buildWidgets();

			// Use our own findViewWithTag, not View.findViewWithTag!

			View match = findViewWithTag( viewgroup, tag );

			// Not found

			if ( match == null )
				return null;

			// Found

			if ( tagsLoop == tagsLength - 1 )
				return match;

			// Keep traversing

			if ( !( match instanceof ViewGroup ) )
				return null;

			viewgroup = (ViewGroup) match;
		}

		// Not found

		return null;
	}

	/**
	 * Version of <code>View.findViewWithTag</code> that only traverses child Views if they don't
	 * define a tag of their own.
	 * <p>
	 * This stops us incorrectly finding arbitrary bits of tag in arbitrary bits of the heirarchy.
	 */

	private View findViewWithTag( ViewGroup viewgroup, Object tag )
	{
		for ( int childLoop = 0, childLength = viewgroup.getChildCount(); childLoop < childLength; childLoop++ )
		{
			View child = viewgroup.getChildAt( childLoop );
			Object childTag = child.getTag();

			// Only recurse if child does not define a tag (eg. something like
			// an embedded Layout or a TableRow)

			if ( childTag == null && child instanceof ViewGroup )
			{
				View view = findViewWithTag( (ViewGroup) child, tag );

				if ( view != null )
					return view;

				continue;
			}

			// Keep looking

			if ( !tag.equals( child.getTag() ) )
				continue;

			// Found

			return child;
		}

		// Not found

		return null;
	}

	private Object getValue( View widget, WidgetBuilder<View, AndroidMetawidget> widgetBuilder )
	{
		// Recurse into CompositeWidgetBuilders

		if ( widgetBuilder instanceof CompositeWidgetBuilder )
		{
			for ( WidgetBuilder<View, AndroidMetawidget> widgetBuilderChild : ( (CompositeWidgetBuilder<View, AndroidMetawidget>) widgetBuilder ).getWidgetBuilders() )
			{
				Object value = getValue( widget, widgetBuilderChild );

				if ( value != null )
					return value;
			}

			return null;
		}

		// Interrogate AndroidValueAccessors

		if ( widgetBuilder instanceof AndroidValueAccessor )
			return ( (AndroidValueAccessor) widgetBuilder ).getValue( widget );

		return null;
	}

	private boolean setValue( Object value, View widget, WidgetBuilder<View, AndroidMetawidget> widgetBuilder )
	{
		// Recurse into CompositeWidgetBuilders

		if ( widgetBuilder instanceof CompositeWidgetBuilder )
		{
			for ( WidgetBuilder<View, AndroidMetawidget> widgetBuilderChild : ( (CompositeWidgetBuilder<View, AndroidMetawidget>) widgetBuilder ).getWidgetBuilders() )
			{
				if ( setValue( value, widget, widgetBuilderChild ) )
					return true;
			}

			return false;
		}

		// Interrogate AndroidValueAccessors

		if ( widgetBuilder instanceof AndroidValueAccessor )
			return ( (AndroidValueAccessor) widgetBuilder ).setValue( value, widget );

		return false;
	}

	//
	// Inner class
	//

	protected class AndroidMetawidgetMixin
		extends MetawidgetMixin<View, AndroidMetawidget>
	{
		//
		// Protected methods
		//

		@Override
		protected String getElementName( Element element )
		{
			return element.getNodeName();
		}

		@Override
		protected void startBuild()
			throws Exception
		{
			AndroidMetawidget.this.startBuild();
		}

		@Override
		protected void addWidget( View view, String elementName, Map<String, String> attributes )
		{
			AndroidMetawidget.this.addWidget( view, elementName, attributes );
		}

		@Override
		protected View getOverriddenWidget( String elementName, Map<String, String> attributes )
		{
			return AndroidMetawidget.this.getOverriddenWidget( elementName, attributes );
		}

		@Override
		protected boolean isStub( View view )
		{
			return ( view instanceof Stub );
		}

		@Override
		protected Map<String, String> getStubAttributes( View stub )
		{
			return ( (Stub) stub ).getAttributes();
		}

		@Override
		public AndroidMetawidget buildNestedMetawidget( Map<String, String> attributes )
			throws Exception
		{
			Constructor<?> constructor = AndroidMetawidget.this.getClass().getConstructor( Context.class );
			AndroidMetawidget nestedMetawidget = (AndroidMetawidget) constructor.newInstance( getContext() );
			nestedMetawidget.setReadOnly( isReadOnly() || TRUE.equals( attributes.get( READ_ONLY ) ) );
			nestedMetawidget.setMaximumInspectionDepth( getMaximumInspectionDepth() - 1 );

			AndroidMetawidget.this.initNestedMetawidget( nestedMetawidget, attributes );

			return nestedMetawidget;
		}

		@Override
		protected void endBuild()
		{
			AndroidMetawidget.this.endBuild();
		}

		@Override
		protected AndroidMetawidget getMixinOwner()
		{
			return AndroidMetawidget.this;
		}
	}
}
