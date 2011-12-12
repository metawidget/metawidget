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

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.metawidget.config.ConfigReader;
import org.metawidget.config.ServletConfigReader;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.layout.iface.Layout;
import org.metawidget.pipeline.w3c.W3CPipeline;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.PathUtils.TypeAndNames;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.w3c.dom.Element;

/**
 * Base Metawidget for JSP environments.
 *
 * @author Richard Kennard
 */

public abstract class MetawidgetTag
	extends BodyTagSupport {

	//
	// Public statics
	//

	/**
	 * Marks a tag as potentially needing a hidden field, if <code>HiddenFieldProcessor</code> is
	 * used.
	 * <p>
	 * In order to align tightly with the <code>StubTag</code>s created by the
	 * <code>WidgetBuilder</code>s, and to avoid confusion with manually created
	 * <code>StubTag</code>s, we take a flag-based approach to hidden field processing. This would
	 * be cleaner if JSP had a richer component model (ie. in JSF we do
	 * <code>instanceof UIInput</code>).
	 */

	public static final String							ATTRIBUTE_NEEDS_HIDDEN_FIELD	= "metawidget-needs-hidden-field";

	//
	// Private statics
	//

	private static final long							serialVersionUID				= 1l;

	/**
	 * Cache the ConfigReader at the ServletContext level. Although, interestingly, this can also be
	 * used to inject a different ConfigReader if needed (ie. for Grails).
	 */

	private static final String							CONFIG_READER_ATTRIBUTE			= "metawidget-config-reader";

	private static final String							DEFAULT_USER_CONFIG				= "metawidget.xml";

	private static boolean								LOGGED_MISSING_CONFIG;

	//
	// Private members
	//

	/**
	 * Path to inspect.
	 * <p>
	 * Set by subclasses according to what they prefer to call it (eg. <code>name</code> for Struts,
	 * <code>property</code> for Spring).
	 */

	private String										mPath;

	/**
	 * Prefix of path to inspect, to support nesting.
	 */

	private String										mPathPrefix;

	private String										mConfig;

	private boolean										mNullConfig;

	private ResourceBundle								mBundle;

	private Map<String, FacetTag>						mFacets;

	private Map<String, StubTag>						mStubs;

	private Map<Object, Object>							mClientProperties;

	private W3CPipeline<Tag, BodyTag, MetawidgetTag>	mPipeline;

	//
	// Constructor
	//

	public MetawidgetTag() {

		mPipeline = newPipeline();
	}

	//
	// Public methods
	//

	@Override
	public void setPageContext( PageContext newPageContext ) {

		super.setPageContext( newPageContext );
		ServletContext servletContext = pageContext.getServletContext();

		// Some more initialization

		if ( mConfig == null ) {
			String configFile = servletContext.getInitParameter( "org.metawidget.jsp.tagext.CONFIG_FILE" );

			if ( configFile == null ) {
				if ( !mNullConfig ) {
					mConfig = DEFAULT_USER_CONFIG;
				}
			} else {
				mConfig = configFile;
			}
		}

		if ( servletContext.getAttribute( CONFIG_READER_ATTRIBUTE ) == null ) {
			servletContext.setAttribute( CONFIG_READER_ATTRIBUTE, new ServletConfigReader( servletContext ) );
		}
	}

	public String getPath() {

		return mPath;
	}

	public String getPathPrefix() {

		return mPathPrefix;
	}

	public void setConfig( String config ) {

		mConfig = config;
		mNullConfig = ( config == null );
		mPipeline.setNeedsConfiguring();
	}

	public String getLabelString( Map<String, String> attributes ) {

		if ( attributes == null ) {
			return "";
		}

		// Explicit label

		String label = attributes.get( LABEL );

		if ( label != null ) {
			// (may be forced blank)

			if ( "".equals( label ) ) {
				return null;
			}

			// (localize if possible)

			String localized = getLocalizedKey( StringUtils.camelCase( label ) );

			if ( localized != null ) {
				return localized.trim();
			}

			return label.trim();
		}

		// Default name

		String name = attributes.get( NAME );

		if ( name != null ) {
			// (localize if possible)

			String localized = getLocalizedKey( name );

			if ( localized != null ) {
				return localized.trim();
			}

			return StringUtils.uncamelCase( name );
		}

		return "";
	}

	/**
	 * @return null if no bundle, ???key??? if bundle is missing a key
	 */

	public String getLocalizedKey( String key ) {

		if ( mBundle == null ) {
			return null;
		}

		try {
			String localizedKey = mBundle.getString( key );

			if ( localizedKey != null ) {
				return localizedKey;
			}
		} catch ( MissingResourceException e ) {
			// Fall through
		}

		return StringUtils.RESOURCE_KEY_NOT_FOUND_PREFIX + key + StringUtils.RESOURCE_KEY_NOT_FOUND_SUFFIX;
	}

	public FacetTag getFacet( String name ) {

		if ( mFacets == null ) {
			return null;
		}

		return mFacets.get( name );
	}

	public void setFacet( String name, FacetTag facetTag ) {

		if ( mFacets == null ) {
			mFacets = CollectionUtils.newHashMap();
		}

		mFacets.put( name, facetTag );
	}

	public void setStub( String path, StubTag stubTag ) {

		if ( mStubs == null ) {
			mStubs = CollectionUtils.newHashMap();
		}

		mStubs.put( path, stubTag );
	}

	public boolean isReadOnly() {

		return mPipeline.isReadOnly();
	}

	public void setReadOnly( boolean readOnly ) {

		mPipeline.setReadOnly( readOnly );
	}

	public void setInspector( Inspector inspector ) {

		mPipeline.setInspector( inspector );
	}

	/**
	 * Useful for WidgetBuilders to perform nested inspections (eg. for Collections).
	 */

	public String inspect( Object toInspect, String type, String... names ) {

		return mPipeline.inspect( toInspect, type, names );
	}

	public void setInspectionResultProcessors( InspectionResultProcessor<MetawidgetTag>... inspectionResultProcessors ) {

		mPipeline.setInspectionResultProcessors( inspectionResultProcessors );
	}

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	public void setWidgetBuilder( WidgetBuilder<Object, ? extends MetawidgetTag> widgetBuilder ) {

		mPipeline.setWidgetBuilder( (WidgetBuilder) widgetBuilder );
	}

	public void setWidgetProcessors( WidgetProcessor<Tag, MetawidgetTag>... widgetProcessors ) {

		mPipeline.setWidgetProcessors( widgetProcessors );
	}

	public void setLayout( Layout<Tag, BodyTag, MetawidgetTag> layout ) {

		mPipeline.setLayout( layout );
	}

	/**
	 * This method is public for use by WidgetBuilders to perform nested inspections (eg. for
	 * Collections).
	 */

	public PageContext getPageContext() {

		return pageContext;
	}

	/**
	 * Storage area for WidgetProcessors, Layouts, and other stateless clients.
	 */

	public void putClientProperty( Object key, Object value ) {

		if ( mClientProperties == null ) {
			mClientProperties = CollectionUtils.newHashMap();
		}

		mClientProperties.put( key, value );
	}

	/**
	 * Storage area for WidgetProcessors, Layouts, and other stateless clients.
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getClientProperty( Object key ) {

		if ( mClientProperties == null ) {
			return null;
		}

		return (T) mClientProperties.get( key );
	}

	@Override
	public int doStartTag()
		throws JspException {

		// According to this bug report https://issues.apache.org/bugzilla/show_bug.cgi?id=16001 and
		// this article http://onjava.com/pub/a/onjava/2001/11/07/jsp12.html?page=3, we do not need
		// to worry about overriding super.release() for values associated with an optional property
		// getter/setter (nor can we ever rely on super.release() being called). We just need to
		// reset some internal variables during doStartTag. The pooling is smart enough
		// not to pool tags with different optional values, and of course non-optional values will
		// get reset each time

		mFacets = null;
		mStubs = null;
		mPipeline.setNeedsConfiguring();

		return super.doStartTag();
	}

	@Override
	public int doEndTag()
		throws JspException {

		mPipeline.configureOnce();

		try {
			mPipeline.buildWidgets( inspect() );
		} catch ( Exception e ) {
			throw MetawidgetException.newException( e );
		}

		return super.doEndTag();
	}

	/**
	 * Fetch a <code>StubTag</code> with the given path.
	 * <p>
	 * <strong>This is an internal API exposed for OverriddenWidgetBuilder. Clients should not call
	 * it directly.</strong>
	 */

	public StubTag getStub( String path ) {

		if ( mStubs == null ) {
			return null;
		}

		return mStubs.get( path );
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

	protected void setPathInternal( String path ) {

		mPath = path;

		// If changed the path, all bets are off what the prefix is

		mPathPrefix = null;
	}

	protected void setPathPrefix( String pathPrefix ) {

		mPathPrefix = pathPrefix;
	}

	/**
	 * Sets the ResourceBundle used to localize labels.
	 * <p>
	 * This will need to be exposed in framework-specific ways. For example, JSTL can use
	 * <code>LocalizationContext</code>s, though these are not necessarily available to a Struts
	 * app.
	 */

	protected void setBundle( ResourceBundle bundle ) {

		mBundle = bundle;
	}

	/**
	 * Instantiate the Pipeline used by this Metawidget.
	 * <p>
	 * Subclasses wishing to use their own Pipeline should override this method to instantiate their
	 * version.
	 */

	protected W3CPipeline<Tag, BodyTag, MetawidgetTag> newPipeline() {

		return new MetawidgetTagPipeline();
	}

	protected abstract void beforeBuildCompoundWidget( Element element );

	protected void initNestedMetawidget( MetawidgetTag nestedMetawidget, Map<String, String> attributes ) {

		// Don't reconfigure...

		nestedMetawidget.setConfig( null );

		// ...instead, copy runtime values

		mPipeline.initNestedPipeline( nestedMetawidget.mPipeline, attributes );
		nestedMetawidget.setPathInternal( mPath + StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME ) );
		nestedMetawidget.setBundle( mBundle );
	}

	protected Element inspect() {

		TypeAndNames typeAndNames = PathUtils.parsePath( mPath, StringUtils.SEPARATOR_DOT_CHAR );
		String type = typeAndNames.getType();

		// Inspect using the 'raw' type (eg. contactForm)

		Element inspectionResult = mPipeline.inspectAsDom( null, type, typeAndNames.getNamesAsArray() );

		// (pageContext may be null in unit tests)

		if ( pageContext != null ) {
			// Try to locate the runtime bean. This allows some Inspectors
			// to act on it polymorphically.

			Object obj = pageContext.findAttribute( type );

			if ( obj != null ) {
				Element additionalInspectionResult = mPipeline.inspectAsDom( obj, obj.getClass().getName(), typeAndNames.getNamesAsArray() );

				// Combine the subtrees
				//
				// Note the top-level types attribute will be different, because one is the 'raw'
				// type (eg. contactForm) and one the runtime bean (eg.
				// org.metawidget.example.struts.addressbook.form.BusinessContactForm)

				if ( inspectionResult == null ) {
					inspectionResult = additionalInspectionResult;
				} else if ( additionalInspectionResult != null ) {
					Element inspectionResultEntity = XmlUtils.getFirstChildElement( inspectionResult );
					Element additionalInspectionResultEntity = XmlUtils.getFirstChildElement( additionalInspectionResult );
					XmlUtils.combineElements( inspectionResultEntity, additionalInspectionResultEntity, NAME, null );
				}
			}
		}

		return inspectionResult;

		// REFACTOR: instead of the above, just do mPipeline.inspectAsDom( obj, type,
		// typeAndNames.getNamesAsArray() ) and the user can use restrictAgainstObject to figure out
		// the Object relationship
	}

	protected void configure() {

		try {
			ServletContext servletContext = pageContext.getServletContext();
			ConfigReader configReader = (ConfigReader) servletContext.getAttribute( CONFIG_READER_ATTRIBUTE );

			if ( mConfig != null ) {
				try {
					configReader.configure( mConfig, this );
				} catch ( MetawidgetException e ) {
					if ( !DEFAULT_USER_CONFIG.equals( mConfig ) || !( e.getCause() instanceof FileNotFoundException ) ) {
						throw e;
					}

					// Log a warning. Still log the Exception message, in case the FileNotFoundException
					// is from inside metawidget.xml, for example 'Unable to locate checkout.jpdl.xml on
					// CLASSPATH'

					if ( !LOGGED_MISSING_CONFIG ) {
						LOGGED_MISSING_CONFIG = true;
						LogUtils.getLog( MetawidgetTag.class ).info( "Could not locate " + DEFAULT_USER_CONFIG + ". This file is optional, but if you HAVE created one then Metawidget isn''t finding it: {0}", e.getMessage() );
					}
				}
			}

			mPipeline.configureDefaults( configReader, getDefaultConfiguration(), MetawidgetTag.class );
		} catch ( Exception e ) {
			throw MetawidgetException.newException( e );
		}
	}

	protected abstract String getDefaultConfiguration();

	//
	// Inner class
	//

	protected class MetawidgetTagPipeline
		extends W3CPipeline<Tag, BodyTag, MetawidgetTag> {

		//
		// Protected methods
		//

		@Override
		protected void configure() {

			MetawidgetTag.this.configure();
		}

		@Override
		protected Map<String, String> getAdditionalAttributes( Tag tag ) {

			if ( tag instanceof StubTag ) {
				return ( (StubTag) tag ).getAttributesMap();
			}

			return null;
		}

		@Override
		protected void buildCompoundWidget( Element element )
			throws Exception {

			MetawidgetTag.this.beforeBuildCompoundWidget( element );
			super.buildCompoundWidget( element );
		}

		@Override
		protected MetawidgetTag buildNestedMetawidget( final Map<String, String> attributes )
			throws Exception {

			MetawidgetTag metawidgetTag = MetawidgetTag.this.getClass().newInstance();
			MetawidgetTag.this.initNestedMetawidget( metawidgetTag, attributes );

			return metawidgetTag;
		}

		@Override
		protected MetawidgetTag getPipelineOwner() {

			return MetawidgetTag.this;
		}
	}
}
