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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.metawidget.MetawidgetException;
import org.metawidget.android.AndroidUtils.ResourcelessArrayAdapter;
import org.metawidget.android.widget.layout.Layout;
import org.metawidget.android.widget.layout.TableLayout;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.mixin.w3c.MetawidgetMixin;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.PathUtils;
import org.metawidget.util.PathUtils.TypeAndNames;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Element;

import android.content.Context;
import android.graphics.Canvas;
import android.text.InputFilter;
import android.text.method.DateInputMethod;
import android.text.method.DigitsInputMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Metawidget for Android environments.
 * <p>
 * Automatically creates native Android Views, such as <code>EditText</code> and
 * <code>Spinner</code>, to suit the inspected fields.
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
	//
	// Private statics
	//
	//

	private final static List<Boolean>				LIST_BOOLEAN_VALUES	= CollectionUtils.unmodifiableList( null, Boolean.TRUE, Boolean.FALSE );

	private final static Map<Integer, Inspector>	INSPECTORS			= Collections.synchronizedMap( new HashMap<Integer, Inspector>() );

	private final static String						PARAM_PREFIX		= "param";

	//
	//
	// Private members
	//
	//

	private Object									mToInspect;

	private String									mPath;

	private int										mInspectorConfig;

	private Inspector								mInspector;

	private Class<? extends Layout>					mLayoutClass		= TableLayout.class;

	private Layout									mLayout;

	private Map<String, Object>						mParameters;

	private boolean									mNeedToBuildWidgets;

	String											mLastInspection;

	private Set<View>								mExistingViews;

	private Set<View>								mExistingViewsUnused;

	private Map<String, Facet>						mFacets;

	private AndroidMetawidgetMixin					mMetawidgetMixin;

	//
	//
	// Constructor
	//
	//

	public AndroidMetawidget( Context context )
	{
		super( context );
		mMetawidgetMixin = newMetawidgetMixin();

		setOrientation( LinearLayout.VERTICAL );
	}

	@SuppressWarnings( "unchecked" )
	public AndroidMetawidget( Context context, AttributeSet attributes, Map inflateParams )
	{
		super( context, attributes, inflateParams );
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

		mInspectorConfig = attributes.getAttributeResourceValue( null, "inspectorConfig", 0 );

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
	//
	// Public methods
	//
	//

	public void setToInspect( Object toInspect )
	{
		mToInspect = toInspect;

		// If no path, or path points to an old class, override it

		if ( toInspect != null && ( mPath == null || mPath.indexOf( StringUtils.SEPARATOR_FORWARD_SLASH_CHAR ) == -1 ) )
			mPath = ClassUtils.getUnproxiedClass( toInspect.getClass() ).getName();

		invalidateInspection();
	}

	public void setPath( String path )
	{
		mPath = path;
		invalidateInspection();
	}

	/**
	 * Provides an id for the inspector configuration.
	 * <p>
	 * Typically, the id will be retrieved by <code>R.raw.inspector</code>
	 */

	public void setInspectorConfig( int inspectorConfig )
	{
		mInspectorConfig = inspectorConfig;
		mInspector = null;
		invalidateInspection();
	}

	public void setInspector( Inspector inspector )
	{
		mInspector = inspector;
		mInspectorConfig = 0;
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

	public Object getParameter( String name )
	{
		if ( mParameters == null )
			return null;

		return mParameters.get( name );
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

	/**
	 * Gets the value from the View with the given name.
	 * <p>
	 * The value is returned as it is stored in the View (eg. String for EditText) so may need some
	 * conversion before being reapplied to the object being inspected. This obviously requires
	 * knowledge of which View AndroidMetawidget created, which is not ideal.
	 */

	public Object getValue( String... names )
	{
		View view = findViewWithTag( names );

		if ( view == null )
			throw MetawidgetException.newException( "No view with tag " + ArrayUtils.toString( names ) );

		// CheckBox

		if ( view instanceof CheckBox )
			return ( (CheckBox) view ).isChecked();

		// EditText

		if ( view instanceof EditText )
			return ( (EditText) view ).getText().toString();

		// TextView

		if ( view instanceof TextView )
			return ( (TextView) view ).getText();

		// AdapterView

		if ( view instanceof AdapterView )
			return ( (AdapterView<?>) view ).getSelectedItem();

		// Unknown (subclasses should override this)

		throw MetawidgetException.newException( "Don't know how to getValue from a " + view.getClass().getName() );
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
		View view = findViewWithTag( names );

		if ( view == null )
			throw MetawidgetException.newException( "No view with tag " + ArrayUtils.toString( names ) );

		// CheckBox

		if ( view instanceof CheckBox )
		{
			( (CheckBox) view ).setChecked( (Boolean) value );
			return;
		}

		// EditView/TextView

		if ( view instanceof TextView )
		{
			( (TextView) view ).setText( StringUtils.quietValueOf( value ) );
			return;
		}

		// AdapterView

		if ( view instanceof AdapterView )
		{
			@SuppressWarnings( "unchecked" )
			AdapterView<ArrayAdapter<Object>> adapterView = (AdapterView<ArrayAdapter<Object>>) view;

			// Set the backing collection

			if ( value instanceof Collection )
			{
				@SuppressWarnings( "unchecked" )
				Collection<Object> collection = (Collection<Object>) value;
				adapterView.setAdapter( new ResourcelessArrayAdapter<Object>( getContext(), collection ) );
			}

			// Set the selected value

			else
			{
				adapterView.setSelection( adapterView.getAdapter().getPosition( value ) );
			}

			return;
		}

		// Unknown (subclasses should override this)

		throw MetawidgetException.newException( "Don't know how to setValue of a " + view.getClass().getName() );
	}

	public Facet getFacet( String name )
	{
		buildWidgets();

		return mFacets.get( name );
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

	protected AndroidMetawidgetMixin newMetawidgetMixin()
	{
		return new AndroidMetawidgetMixin();
	}

	@Override
	protected void onDraw( Canvas canvas )
	{
		buildWidgets();
		super.onDraw( canvas );
	}

	@Override
	protected View findViewTraversal( int id )
	{
		buildWidgets();
		return super.findViewTraversal( id );
	}

	@Override
	protected View findViewWithTagTraversal( Object tag )
	{
		buildWidgets();
		return super.findViewWithTagTraversal( tag );
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

		invalidate();
	}

	protected void buildWidgets()
	{
		// No need to build?

		if ( !mNeedToBuildWidgets )
			return;

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

	protected void addWidget( View view, Map<String, String> attributes )
	{
		String childName = attributes.get( NAME );
		view.setTag( childName );

		if ( mLayout != null )
			mLayout.layoutChild( view, attributes );
	}

	protected View getOverridenWidget( String elementName, Map<String, String> attributes )
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

	protected View buildReadOnlyWidget( String elementName, Map<String, String> attributes )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		// Masked (return an invisible View, so that we DO still
		// render a label and reserve some blank space)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
		{
			TextView view = new TextView( getContext() );
			view.setVisibility( View.INVISIBLE );

			return view;
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
			return new TextView( getContext() );

		String type = attributes.get( TYPE );

		// If no type, fail gracefully with a JTextField

		if ( type == null || "".equals( type ) )
			return new TextView( getContext() );

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz != null )
		{
			if ( clazz.isPrimitive() )
				return new TextView( getContext() );

			if ( String.class.equals( clazz ) )
				return new TextView( getContext() );

			if ( Date.class.equals( clazz ) )
				return new TextView( getContext() );

			if ( Boolean.class.equals( clazz ) )
				return new TextView( getContext() );

			if ( Number.class.isAssignableFrom( clazz ) )
				return new TextView( getContext() );

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return null;
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return new TextView( getContext() );

		// Nested Metawidget

		return createMetawidget( attributes );
	}

	protected View buildActiveWidget( String elementName, Map<String, String> attributes )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		String type = attributes.get( TYPE );

		// If no type, fail gracefully with an EditText

		if ( type == null || "".equals( type ) )
			return new EditText( getContext() );

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz != null )
		{
			// String Lookups

			String lookup = attributes.get( LOOKUP );

			if ( lookup != null && !"".equals( lookup ) )
			{
				List<String> lookupList = CollectionUtils.fromString( lookup );

				// (CollectionUtils.fromString returns unmodifiable EMPTY_LIST if empty)

				if ( !clazz.isPrimitive() && !lookupList.isEmpty() )
					lookupList.add( 0, null );

				List<String> lookupLabelsList = null;
				String lookupLabels = attributes.get( LOOKUP_LABELS );

				if ( lookupLabels != null && !"".equals( lookupLabels ) )
				{
					lookupLabelsList = CollectionUtils.fromString( lookupLabels );

					// (CollectionUtils.fromString returns unmodifiable EMPTY_LIST if empty)

					if ( !lookupLabelsList.isEmpty() )
						lookupLabelsList.add( 0, null );
				}

				Spinner spinner = new Spinner( getContext() );
				spinner.setAdapter( new ResourcelessArrayAdapter<String>( getContext(), lookupList, lookupLabelsList ) );

				return spinner;
			}

			if ( clazz.isPrimitive() )
			{
				// booleans

				if ( boolean.class.equals( clazz ) )
					return new CheckBox( getContext() );

				EditText editText = new EditText( getContext() );

				// DigitsInputMethod is 0-9 and +

				if ( byte.class.equals( clazz ) || short.class.equals( clazz ) || int.class.equals( clazz ) || long.class.equals( clazz ) )
					editText.setInputMethod( new DigitsInputMethod() );

				return editText;
			}

			// Strings

			if ( String.class.equals( clazz ) )
			{
				EditText editText = new EditText( getContext() );

				if ( TRUE.equals( attributes.get( MASKED ) ) )
					editText.setTransformationMethod( PasswordTransformationMethod.getInstance() );

				if ( TRUE.equals( attributes.get( LARGE ) ) )
					editText.setMinLines( 3 );

				String maximumLength = attributes.get( MAXIMUM_LENGTH );

				if ( maximumLength != null && !"".equals( maximumLength ) )
					editText.setFilters( new InputFilter[] { new InputFilter.LengthFilter( Integer.parseInt( maximumLength ) ) } );

				return editText;
			}

			// Dates

			if ( Date.class.equals( clazz ) )
			{
				EditText editText = new EditText( getContext() );
				editText.setInputMethod( new DateInputMethod() );

				return editText;
			}

			// Booleans (are tri-state)

			if ( Boolean.class.equals( clazz ) )
			{
				Spinner spinner = new Spinner( getContext() );
				spinner.setAdapter( new ResourcelessArrayAdapter<Boolean>( getContext(), LIST_BOOLEAN_VALUES, null ) );

				return spinner;
			}

			// Numbers

			if ( Number.class.isAssignableFrom( clazz ) )
			{
				EditText editText = new EditText( getContext() );

				// DigitsInputMethod is 0-9 and +

				if ( Byte.class.isAssignableFrom( clazz ) || Short.class.isAssignableFrom( clazz ) || Integer.class.isAssignableFrom( clazz ) || Long.class.isAssignableFrom( clazz ) )
					editText.setInputMethod( new DigitsInputMethod() );

				return editText;
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return null;
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return new EditText( getContext() );

		// Nested Metawidget

		return createMetawidget( attributes );
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

		// If this Inspector has been set externally, use it...

		Inspector inspector = mInspector;

		if ( inspector == null )
		{
			if ( mInspectorConfig == 0 )
				throw MetawidgetException.newException( "No inspector or inspectorConfig specified" );

			// ...otherwise, if this InspectorConfig has already been read, use it...

			inspector = INSPECTORS.get( mInspectorConfig );

			// ...otherwise, initialize the Inspector

			if ( inspector == null )
			{
				inspector = new AndroidConfigReader( getContext() ).read( getContext().getResources().openRawResource( mInspectorConfig ) );
				INSPECTORS.put( mInspectorConfig, inspector );
			}
		}

		// Use the inspector to inspect the path

		return inspect( inspector, mPath );
	}

	protected String inspect( Inspector inspector, String path )
	{
		TypeAndNames typeAndNames = PathUtils.parsePath( path );
		return inspector.inspect( mToInspect, typeAndNames.getType(), typeAndNames.getNames() );
	}

	protected AndroidMetawidget createMetawidget( Map<String, String> attributes )
		throws Exception
	{
		Constructor<? extends AndroidMetawidget> constructor = getClass().getConstructor( Context.class );
		return constructor.newInstance( getContext() );
	}

	protected AndroidMetawidget initMetawidget( AndroidMetawidget metawidget, Map<String, String> attributes )
	{
		metawidget.setPath( mPath + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME ) );

		if ( mInspectorConfig != 0 )
			metawidget.setInspectorConfig( mInspectorConfig );
		else
			metawidget.setInspector( mInspector );

		metawidget.setLayoutClass( mLayoutClass );

		if ( mParameters != null )
			metawidget.mParameters = CollectionUtils.newHashMap( mParameters );

		metawidget.setToInspect( mToInspect );

		return metawidget;
	}

	//
	//
	// Private methods
	//
	//

	private View findViewWithTag( String... tags )
	{
		if ( tags == null )
			return null;

		ViewGroup viewgroup = this;

		for ( int tagsLoop = 0, tagsLength = tags.length; tagsLoop < tagsLength; tagsLoop++ )
		{
			Object tag = tags[tagsLoop];

			// buildWidgets just-in-time

			if ( mMetawidgetMixin.isMetawidget( viewgroup ) )
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

	//
	//
	// Inner class
	//
	//

	protected class AndroidMetawidgetMixin
		extends MetawidgetMixin<View>
	{
		//
		//
		// Protected methods
		//
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
		protected void addWidget( View view, Map<String, String> attributes )
		{
			AndroidMetawidget.this.addWidget( view, attributes );
		}

		@Override
		protected View getOverridenWidget( String elementName, Map<String, String> attributes )
		{
			return AndroidMetawidget.this.getOverridenWidget( elementName, attributes );
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
		protected boolean isMetawidget( View view )
		{
			// The MetawidgetMixin base class uses .getDeclaringClass, which
			// isn't implemented in m5-rc15

			return ( view instanceof AndroidMetawidget );
		}

		@Override
		protected View buildReadOnlyWidget( String elementName, Map<String, String> attributes )
			throws Exception
		{
			return AndroidMetawidget.this.buildReadOnlyWidget( elementName, attributes );
		}

		@Override
		protected View buildActiveWidget( String elementName, Map<String, String> attributes )
			throws Exception
		{
			return AndroidMetawidget.this.buildActiveWidget( elementName, attributes );
		}

		@Override
		public View initMetawidget( View widget, Map<String, String> attributes )
		{
			AndroidMetawidget metawidget = (AndroidMetawidget) widget;
			metawidget.setReadOnly( isReadOnly( attributes ) );

			return AndroidMetawidget.this.initMetawidget( metawidget, attributes );
		}

		@Override
		protected void endBuild()
		{
			AndroidMetawidget.this.endBuild();
		}
	}
}
