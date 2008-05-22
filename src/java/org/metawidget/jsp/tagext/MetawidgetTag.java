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

package org.metawidget.jsp.tagext;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.metawidget.MetawidgetException;
import org.metawidget.impl.MetawidgetMixin;
import org.metawidget.inspector.Inspector;
import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.FacetTag.FacetContent;
import org.metawidget.jsp.tagext.StubTag.StubContent;
import org.metawidget.jsp.tagext.html.HtmlTableLayout;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.PathUtils;
import org.metawidget.util.PathUtils.TypeAndNames;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Base Metawidget for JSP environments.
 *
 * @author Richard Kennard
 */

public abstract class MetawidgetTag
	extends BodyTagSupport
{
	//
	//
	// Protected statics
	//
	//

	protected final static String		METAWIDGET				= MetawidgetTag.class.getName();

	//
	//
	// Private statics
	//
	//

	private final static String			INSPECTORS_ATTRIBUTE	= "metawidget-inspectors";

	//
	//
	// Protected members
	//
	//

	/**
	 * Path to inspect.
	 * <p>
	 * Set by subclasses according to what they prefer to call it (eg. <code>name</code> for
	 * Struts, <code>property</code> for Spring). Read by subclasses during
	 * <code>buildCompoundWidget</code>.
	 */

	protected String					mPath;

	//
	//
	// Private members
	//
	//

	private String						mInspectorConfig		= "inspector-config.xml";

	private String						mLayoutClass			= HtmlTableLayout.class.getName();

	private Layout						mLayout;

	private ResourceBundle				mBundle;

	private Map<String, String>			mParams;

	private Map<String, FacetContent>	mFacets;

	private Map<String, StubContent>	mStubs;

	private JspMetawidgetMixin			mMixin					= new JspMetawidgetMixin();

	//
	//
	// Constuctor
	//
	//

	public MetawidgetTag()
	{
		// Default constructor
	}

	public MetawidgetTag( MetawidgetTag tag )
	{
		// Do not copy mPath: could lead to infinite recursion

		mInspectorConfig = tag.mInspectorConfig;
		mLayoutClass = tag.mLayoutClass;
		mBundle = tag.mBundle;

		if ( tag.mParams != null )
			mParams = CollectionUtils.newHashMap( tag.mParams );
	}

	//
	//
	// Public methods
	//
	//

	public void setLayoutClass( String layoutClass )
	{
		mLayoutClass = layoutClass;
		mLayout = null;
	}

	/**
	 * Sets the ResourceBundle used to localize labels.
	 */

	public void setBundle( ResourceBundle bundle )
	{
		mBundle = bundle;
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
			String localizedKey = mBundle.getString( key );

			if ( localizedKey != null )
				return localizedKey;
		}
		catch ( MissingResourceException e )
		{
			// Fall through
		}

		return StringUtils.RESOURCE_KEY_NOT_FOUND_PREFIX + key + StringUtils.RESOURCE_KEY_NOT_FOUND_SUFFIX;
	}

	public void setParam( String name, String value )
	{
		if ( mParams == null )
			mParams = CollectionUtils.newHashMap();

		mParams.put( name, value );
	}

	public String getParam( String name )
	{
		if ( mParams == null )
			return null;

		return mParams.get( name );
	}

	public FacetContent getFacet( String name )
	{
		if ( mFacets == null )
			return null;

		return mFacets.get( name );
	}

	public void setFacet( String name, FacetContent facetContent )
	{
		if ( mFacets == null )
			mFacets = CollectionUtils.newHashMap();

		mFacets.put( name, facetContent );
	}

	public void setStub( String path, StubContent stubContent )
	{
		if ( mStubs == null )
			mStubs = CollectionUtils.newHashMap();

		mStubs.put( path, stubContent );
	}

	public boolean isReadOnly()
	{
		return mMixin.isReadOnly();
	}

	public void setReadOnly( boolean readOnly )
	{
		mMixin.setReadOnly( readOnly );
	}

	@Override
	public int doEndTag()
	{
		try
		{
			mMixin.buildWidgets( inspect() );
		}
		catch( Exception e )
		{
			throw MetawidgetException.newException( e );
		}

		// In the case of, say, clicking 'Edit' in the Address Book sample application, the
		// container will not call release(). However we must use a new Layout, else
		// layout state such as mCurrentSection will not be cleared

		mLayout = null;

		return EVAL_PAGE;
	}

	@Override
	public void release()
	{
		super.release();

		mPath = null;
		mInspectorConfig = null;
		mLayoutClass = null;
		mBundle = null;
		mParams = null;
		mStubs = null;
	}

	//
	//
	// Protected methods
	//
	//

	protected void startBuild()
		throws Exception
	{
		if ( mLayout == null && mLayoutClass != null && !"".equals( mLayoutClass ) )
		{
			@SuppressWarnings( "unchecked" )
			Class<? extends Layout> layout = (Class<? extends Layout>) ClassUtils.niceForName( mLayoutClass );

			Constructor<? extends Layout> constructor = layout.getConstructor( MetawidgetTag.class );
			mLayout = constructor.newInstance( this );
		}

		JspWriter writer = pageContext.getOut();

		if ( mLayout != null )
		{
			writer.write( mLayout.layoutBegin( mPath ) );
		}
	}

	protected void endBuild()
		throws Exception
	{
		JspWriter writer = pageContext.getOut();

		if ( mLayout != null )
		{
			writer.write( mLayout.layoutEnd() );
		}
	}

	protected abstract void beforeBuildCompoundWidget();

	protected abstract String buildReadOnlyWidget( Map<String, String> attributes )
		throws Exception;

	protected abstract String buildActiveWidget( Map<String, String> attributes )
		throws Exception;

	protected void addWidget( String widget, Map<String, String> attributes )
		throws IOException
	{
		JspWriter writer = pageContext.getOut();

		if ( mLayout != null )
		{
			writer.write( mLayout.layoutChild( widget, attributes ) );
		}
		else
		{
			writer.write( widget );
		}
	}

	/**
	 * Creates a nested Metawidget.
	 * <p>
	 * Because creating a Metawidget directly in <code>buildWidget</code> would lead to infinite
	 * recursion during <code>buildWidgets</code> (as opposed to during
	 * <code>buildCompoundWidget</code>), we add this deferred step.
	 */

	protected MetawidgetTag createMetawidget( String path, String childName )
	{
		try
		{
			Constructor<? extends MetawidgetTag> constructor = getClass().getConstructor( getClass() );
			MetawidgetTag tag = constructor.newInstance( this );

			tag.mPath = path;

			return tag;
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
	}

	protected Document inspect()
	{
		Inspector inspector;

		// If this InspectorConfig has already been read...

		ServletContext servletContext = pageContext.getServletContext();

		@SuppressWarnings( "unchecked" )
		Map<String, Inspector> inspectors = (Map) servletContext.getAttribute( INSPECTORS_ATTRIBUTE );

		// ...use it...

		if ( inspectors != null )
		{
			inspector = inspectors.get( mInspectorConfig );
		}
		else
		{
			inspectors = CollectionUtils.newHashMap();
			servletContext.setAttribute( INSPECTORS_ATTRIBUTE, inspectors );
			inspector = null;
		}

		// ...otherwise, initialize the Inspector

		if ( inspector == null )
		{
			inspector = new ServletConfigReader( pageContext.getServletContext() ).read( mInspectorConfig );
			inspectors.put( mInspectorConfig, inspector );
		}

		// Use the inspector to inspect the path

		return inspect( inspector, mPath );
	}

	protected Document inspect( Inspector inspector, String path )
	{
		TypeAndNames typeAndNames = PathUtils.parsePath( path, '.' );
		String type = typeAndNames.getType();

		// Try to locate a runtime bean. This allows the Inspectors
		// to act on it polymorphically

		Object obj = pageContext.findAttribute( type );

		if ( obj != null )
			type = ClassUtils.getUnproxiedClass( obj.getClass() ).getName();

		return inspector.inspect( obj, type, typeAndNames.getNames() );
	}

	protected StubContent getStub( String path )
	{
		if ( mStubs == null )
			return null;

		return mStubs.get( path );
	}

	//
	//
	// Inner class
	//
	//

	protected class JspMetawidgetMixin
		extends MetawidgetMixin<Object>
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
			MetawidgetTag.this.startBuild();
		}

		@Override
		protected void addWidget( Object widget, Map<String, String> attributes )
			throws Exception
		{
			if ( widget instanceof StubContent )
				MetawidgetTag.this.addWidget( ( (StubContent) widget ).getContent(), attributes );
			else
				MetawidgetTag.this.addWidget( (String) widget, attributes );
		}

		@Override
		protected StubContent getOverridenWidget( Map<String, String> attributes )
		{
			return MetawidgetTag.this.getStub( attributes.get( NAME ) );
		}

		@Override
		protected boolean isMetawidget( Object widget )
		{
			return METAWIDGET.equals( widget );
		}

		@Override
		protected boolean isStub( Object widget )
		{
			return ( widget instanceof StubContent );
		}

		@Override
		protected Map<String, String> getStubAttributes( Object stub )
		{
			return ( (StubContent) stub ).getAttributes();
		}

		@Override
		protected void beforeBuildCompoundWidget( Element element )
		{
			MetawidgetTag.this.beforeBuildCompoundWidget();
		}

		@Override
		protected String buildReadOnlyWidget( Map<String, String> attributes )
			throws Exception
		{
			return MetawidgetTag.this.buildReadOnlyWidget( attributes );
		}

		@Override
		protected String buildActiveWidget( Map<String, String> attributes )
			throws Exception
		{
			return MetawidgetTag.this.buildActiveWidget( attributes );
		}

		@SuppressWarnings( "synthetic-access" )
		@Override
		protected Object initMetawidget( Object widget, Map<String, String> attributes )
			throws Exception
		{
			String name = attributes.get( NAME );
			MetawidgetTag metawidget = createMetawidget( mPath + '.' + name, name );
			metawidget.setReadOnly( isReadOnly( attributes ) );

			return JspUtils.writeTag( pageContext, metawidget, MetawidgetTag.this, null );
		}

		@Override
		protected void endBuild()
			throws Exception
		{
			MetawidgetTag.this.endBuild();
		}
	}
}
