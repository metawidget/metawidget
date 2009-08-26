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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspector.ConfigReader;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.jsp.JspAnnotationInspector;
import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.ServletConfigReader;
import org.metawidget.jsp.tagext.FacetTag.FacetContent;
import org.metawidget.jsp.tagext.StubTag.StubContent;
import org.metawidget.jsp.tagext.html.layout.HtmlTableLayout;
import org.metawidget.mixin.w3c.MetawidgetMixin;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.util.simple.PathUtils.TypeAndNames;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
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
	// Private statics
	//

	private final static long				serialVersionUID		= 1l;

	private final static String				CONFIG_READER_ATTRIBUTE	= "metawidget-config-reader";

	//
	// Private members
	//

	/**
	 * Path to inspect.
	 * <p>
	 * Set by subclasses according to what they prefer to call it (eg. <code>name</code> for Struts,
	 * <code>property</code> for Spring). Read by <code>WidgetBuilders</code>.
	 */

	private String							mPath;

	/**
	 * Prefix of path to inspect, to support nesting.
	 */

	private String							mPathPrefix;

	private String							mConfig					= "metawidget.xml";

	private boolean							mNeedsConfiguring		= true;

	private String							mLayoutClass			= HtmlTableLayout.class.getName();

	private Layout							mLayout;

	private ResourceBundle					mBundle;

	private Map<String, String>				mParameters;

	private Map<String, FacetContent>		mFacets;

	private Map<String, StubContent>		mStubs;

	private MetawidgetMixin<Object, Object>	mMetawidgetMixin;

	//
	// Constructor
	//

	public MetawidgetTag()
	{
		mMetawidgetMixin = newMetawidgetMixin();
	}

	//
	// Public methods
	//

	public String getPath()
	{
		return mPath;
	}

	public String getPathPrefix()
	{
		return mPathPrefix;
	}

	public void setConfig( String config )
	{
		mConfig = config;
		mNeedsConfiguring = true;
	}

	public void setLayoutClass( String layoutClass )
	{
		mLayoutClass = layoutClass;
		mLayout = null;
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

	public void setParameter( String name, String value )
	{
		if ( mParameters == null )
			mParameters = CollectionUtils.newHashMap();

		mParameters.put( name, value );
	}

	public String getParameter( String name )
	{
		if ( mParameters == null )
			return null;

		return mParameters.get( name );
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
		return mMetawidgetMixin.isReadOnly();
	}

	public void setReadOnly( boolean readOnly )
	{
		mMetawidgetMixin.setReadOnly( readOnly );
	}

	public void setInspector( Inspector inspector )
	{
		mMetawidgetMixin.setInspector( inspector );
	}

	@SuppressWarnings( "unchecked" )
	public void setWidgetBuilder( WidgetBuilder<Object, ? extends MetawidgetTag> widgetBuilder )
	{
		mMetawidgetMixin.setWidgetBuilder( (WidgetBuilder) widgetBuilder );
	}

	/**
	 * Exposed for WidgetBuilders.
	 */

	public PageContext getPageContext()
	{
		return pageContext;
	}

	/**
	 * This method is public for use by WidgetBuilders.
	 */

	public String inspect( Object toInspect, String type, String... names )
	{
		return mMetawidgetMixin.inspect( toInspect, type, names );
	}

	@Override
	public int doStartTag()
		throws JspException
	{
		// According to this bug report https://issues.apache.org/bugzilla/show_bug.cgi?id=16001 and
		// this article http://onjava.com/pub/a/onjava/2001/11/07/jsp12.html?page=3, we do not need
		// to worry about overriding super.release() for member variables associated with a property
		// getter/setter (nor can we ever rely on super.release() being called). We just need to
		// reset some internal variables during doStartTag

		mFacets = null;
		mParameters = null;
		mStubs = null;

		// Needs configuring again in case metawidget.xml calls setParameter

		mNeedsConfiguring = true;

		// TODO: can remove this when layouts are immutable

		mLayout = null;

		return super.doStartTag();
	}

	@Override
	public int doEndTag()
		throws JspException
	{
		configure();

		try
		{
			mMetawidgetMixin.buildWidgets( inspect() );
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}

		return super.doEndTag();
	}

	//
	// Protected methods
	//

	/**
	 * Sets the path.
	 * <p>
	 * Set by subclasses according to what they prefer to call it (eg. <code>name</code> for Struts,
	 * <code>property</code> for Spring).
	 */

	protected void setPathInternal( String path )
	{
		mPath = path;

		// If changed the path, all bets are off what the prefix is

		mPathPrefix = null;
	}

	protected void setPathPrefix( String pathPrefix )
	{
		mPathPrefix = pathPrefix;
	}

	/**
	 * Sets the ResourceBundle used to localize labels.
	 * <p>
	 * This will need to be exposed in framework-specific ways. For example, JSTL can use
	 * <code>LocalizationContext</code>s, though these are not necessarily available to a Struts
	 * app.
	 */

	protected void setBundle( ResourceBundle bundle )
	{
		mBundle = bundle;
	}

	/**
	 * Instantiate the MetawidgetMixin used by this Metawidget.
	 * <p>
	 * Subclasses wishing to use their own MetawidgetMixin should override this method to
	 * instantiate their version.
	 */

	protected MetawidgetMixin<Object, Object> newMetawidgetMixin()
	{
		return new MetawidgetTagMixin();
	}

	protected MetawidgetMixin<Object, Object> getMetawidgetMixin()
	{
		return mMetawidgetMixin;
	}

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

	protected abstract void beforeBuildCompoundWidget( Element element );

	protected void addWidget( String widget, String elementName, Map<String, String> attributes )
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

	protected void initNestedMetawidget( MetawidgetTag nestedMetawidget, Map<String, String> attributes )
	{
		mMetawidgetMixin.initNestedMixin( nestedMetawidget.mMetawidgetMixin, attributes );

		nestedMetawidget.setPathInternal( mPath + StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME ) );
		nestedMetawidget.setConfig( mConfig );
		nestedMetawidget.setLayoutClass( mLayoutClass );
		nestedMetawidget.setBundle( mBundle );

		if ( mParameters != null )
			nestedMetawidget.mParameters = CollectionUtils.newHashMap( mParameters );
	}

	protected String inspect()
	{
		TypeAndNames typeAndNames = PathUtils.parsePath( mPath, '.' );
		String type = typeAndNames.getType();

		// Inject the PageContext (in case it is used)

		try
		{
			JspAnnotationInspector.setThreadLocalPageContext( pageContext );
		}
		catch ( NoClassDefFoundError e )
		{
			// Fail gracefully (if running without JspAnnotationInspector installed)
		}
		catch ( UnsupportedClassVersionError e )
		{
			// Fail gracefully (if running without annotations)
		}

		// Inspect using the 'raw' type (eg. contactForm)

		String xml = inspect( null, type, typeAndNames.getNamesAsArray() );

		// Try to locate the runtime bean. This allows some Inspectors
		// to act on it polymorphically.

		Object obj = pageContext.findAttribute( type );

		if ( obj != null )
		{
			type = ClassUtils.getUnproxiedClass( obj.getClass() ).getName();
			String additionalXml = inspect( obj, type, typeAndNames.getNamesAsArray() );
			xml = combineSubtrees( xml, additionalXml );
		}

		return xml;
	}

	@SuppressWarnings( "unchecked" )
	protected void configure()
	{
		if ( !mNeedsConfiguring )
			return;

		mNeedsConfiguring = false;

		try
		{
			ServletContext servletContext = pageContext.getServletContext();
			ConfigReader configReader = (ConfigReader) servletContext.getAttribute( CONFIG_READER_ATTRIBUTE );

			if ( configReader == null )
			{
				configReader = new ServletConfigReader( servletContext );
				servletContext.setAttribute( CONFIG_READER_ATTRIBUTE, configReader );
			}

			if ( mConfig != null )
				configReader.configure( mConfig, this );

			// Sensible defaults

			if ( mMetawidgetMixin.getWidgetBuilder() == null )
				mMetawidgetMixin.setWidgetBuilder( configReader.configure( getDefaultConfiguration(), WidgetBuilder.class ) );

			if ( mMetawidgetMixin.getInspector() == null )
				mMetawidgetMixin.setInspector( configReader.configure( getDefaultConfiguration(), Inspector.class ) );
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
	}

	protected abstract String getDefaultConfiguration();

	protected StubContent getStub( String path )
	{
		if ( mStubs == null )
			return null;

		return mStubs.get( path );
	}

	//
	// Private methods
	//

	/**
	 * Combine the subtrees.
	 * <p>
	 * Note the top-level types attribute will be different, because one is the 'raw' type (eg.
	 * contactForm) and one the runtime bean (eg.
	 * org.metawidget.example.struts.addressbook.form.BusinessContactForm)
	 */

	public static String combineSubtrees( String master, String toAdd )
	{
		if ( master == null )
			return toAdd;

		if ( toAdd == null )
			return master;

		Document document = XmlUtils.documentFromString( master );
		Element masterElement = XmlUtils.getElementAt( document.getDocumentElement(), 0 );
		Element toAddElement = XmlUtils.getElementAt( XmlUtils.documentFromString( toAdd ).getDocumentElement(), 0 );
		XmlUtils.combineElements( masterElement, toAddElement, NAME, null );

		return XmlUtils.documentToString( document, false );
	}

	//
	// Inner class
	//

	protected class MetawidgetTagMixin
		extends MetawidgetMixin<Object, Object>
	{
		//
		// Protected methods
		//

		@Override
		protected void startBuild()
			throws Exception
		{
			MetawidgetTag.this.startBuild();
		}

		@Override
		protected void addWidget( Object widget, String elementName, Map<String, String> attributes )
			throws Exception
		{
			if ( widget instanceof StubContent )
			{
				String stubContent = ( (StubContent) widget ).getContent();

				// Ignore empty stubs

				if ( stubContent == null || stubContent.length() == 0 )
					return;

				MetawidgetTag.this.addWidget( stubContent, elementName, attributes );
			}
			else
			{
				MetawidgetTag.this.addWidget( (String) widget, elementName, attributes );
			}
		}

		@Override
		protected StubContent getOverriddenWidget( String elementName, Map<String, String> attributes )
		{
			return MetawidgetTag.this.getStub( attributes.get( NAME ) );
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
		protected void buildCompoundWidget( Element element )
			throws Exception
		{
			MetawidgetTag.this.beforeBuildCompoundWidget( element );
			super.buildCompoundWidget( element );
		}

		@SuppressWarnings( "synthetic-access" )
		@Override
		protected Object buildNestedMetawidget( final Map<String, String> attributes )
			throws Exception
		{
			final MetawidgetTag metawidget = MetawidgetTag.this.getClass().newInstance();

			return JspUtils.writeTag( pageContext, metawidget, MetawidgetTag.this, new JspUtils.BodyPreparer()
			{
				@Override
				public void prepareBody( PageContext delegateContext )
				{
					// mParameters gets cleared during doStartTag, so we can't set
					// mParameters until the body (ie. thereby simulating ParamTags)

					MetawidgetTag.this.initNestedMetawidget( metawidget, attributes );
				}
			} );
		}

		@Override
		protected void endBuild()
			throws Exception
		{
			MetawidgetTag.this.endBuild();
		}

		@Override
		protected MetawidgetTag getMixinOwner()
		{
			return MetawidgetTag.this;
		}
	}
}
