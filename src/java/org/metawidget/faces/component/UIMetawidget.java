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

package org.metawidget.faces.component;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.metawidget.config.ConfigReader;
import org.metawidget.faces.FacesUtils;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.layout.iface.Layout;
import org.metawidget.pipeline.w3c.W3CPipeline;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.LogUtils.Log;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.w3c.dom.Element;

/**
 * Base Metawidget for Java Server Faces environments.
 * <p>
 * Note: <code>UIMetawidget</code> only extends <code>UIComponentBase</code>. It is not:
 * <p>
 * <ul>
 * <li>a <code>UIInput</code>, though it may contain input widgets
 * <li>a <code>UIOutput</code>, though it may contain output widgets
 * <li>a <code>ValueHolder</code>, as it does not use a <code>Converter</code>
 * </ul>
 * <p>
 * Its default RendererType is <code>table</code>.
 *
 * @author Richard Kennard
 */

@SuppressWarnings( "deprecation" )
public abstract class UIMetawidget
	extends UIComponentBase
{
	//
	// Public statics
	//

	/**
	 * Component-level attribute used to store metadata.
	 */

	public final static String				COMPONENT_ATTRIBUTE_METADATA		= "metawidget-metadata";

	/**
	 * Component-level attribute used to prevent recreation.
	 * <p>
	 * By default, Metawidget destroys and recreates every component after
	 * <code>processUpdates</code> and before <code>encodeBegin</code>. This allows components to
	 * update to reflect changed state in underlying business objects. For example, components may
	 * change from being <code>UIOutput</code> labels to <code>UIInput</code> text boxes after the
	 * user clicks <code>Edit</code>.
	 * <p>
	 * Most components work well with this approach. Some, however, maintain internal state that
	 * would get lost if the component was destroyed and recreated. For example, the ICEfaces
	 * <code>SelectInputDate</code> component keeps its popup state internally. If it is destroyed
	 * and recreated, the popup never appears.
	 * <p>
	 * Such components can be marked with <code>COMPONENT_ATTRIBUTE_NOT_RECREATABLE</code> to
	 * prevent their destruction and recreation. Of course, this somewhat impacts their flexibility.
	 * For example, a <code>SelectInputDate</code> could not change its date format in response to
	 * another component on the form.
	 * <p>
	 * <code>COMPONENT_ATTRIBUTE_NOT_RECREATABLE</code> is also used to mark components that
	 * override default component generation, such as a <code>UIStub</code> used to suppress a
	 * field.
	 */

	public final static String				COMPONENT_ATTRIBUTE_NOT_RECREATABLE	= "metawidget-not-recreatable";

	//
	// Private statics
	//

	/**
	 * Application-level attribute used to cache ConfigReader.
	 */

	private final static String				APPLICATION_ATTRIBUTE_CONFIG_READER	= "metawidget-config-reader";

	private final static String				DEFAULT_USER_CONFIG					= "metawidget.xml";

	/* package private */final static Log	LOG									= LogUtils.getLog( UIMetawidget.class );

	private static boolean					LOGGED_MISSING_CONFIG;

	//
	// Private members
	//

	private Object							mValue;

	private String							mConfig								= DEFAULT_USER_CONFIG;

	private boolean							mNeedsConfiguring					= true;

	private boolean							mInspectFromParent;

	private Boolean							mReadOnly;

	private Map<Object, Object>				mClientProperties;

	private Pipeline						mPipeline;

	private RemoveDuplicatesHack			mRemoveDuplicatesHack;

	//
	// Constructor
	//

	public UIMetawidget()
	{
		mPipeline = newPipeline();

		// Default renderer

		setRendererType( "table" );

		// SystemEvent support
		// (this is dependent on https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=1402,
		// which in turn depends on
		// https://javaserverfaces-spec-public.dev.java.net/issues/show_bug.cgi?id=636)

		if ( "true".equals( System.getProperty( UIMetawidget.class.getName() + ".UseSystemEvents" ) ) )
			new SystemEventSupport( this );
		else
			mRemoveDuplicatesHack = new RemoveDuplicatesHack( this );
	}

	//
	// Public methods
	//

	@Override
	public String getFamily()
	{
		return "org.metawidget";
	}

	public void setValue( Object value )
	{
		mValue = value;
	}

	public Object getValue()
	{
		if ( mValue != null )
			return mValue;

		ValueBinding valueBinding = getValueBinding( "value" );

		if ( valueBinding == null )
			return null;

		return valueBinding.getValue( getFacesContext() );
	}

	public boolean isReadOnly()
	{
		// Dynamic read-only (takes precedence if set)

		ValueBinding bindingReadOnly = getValueBinding( "readOnly" );

		if ( bindingReadOnly != null )
			return (Boolean) bindingReadOnly.getValue( getFacesContext() );

		// Static read only

		if ( mReadOnly != null )
			return mReadOnly.booleanValue();

		// Default to read-write

		return false;
	}

	public void setReadOnly( Boolean readOnly )
	{
		mReadOnly = readOnly;
	}

	public void setConfig( String config )
	{
		mConfig = config;
		mNeedsConfiguring = true;
	}

	public void setInspector( Inspector inspector )
	{
		mPipeline.setInspector( inspector );
	}

	public void addInspectionResultProcessor( InspectionResultProcessor<Element, UIMetawidget> inspectionResultProcessor )
	{
		mPipeline.addInspectionResultProcessor( inspectionResultProcessor );
	}

	public void removeInspectionResultProcessor( InspectionResultProcessor<Element, UIMetawidget> inspectionResultProcessor )
	{
		mPipeline.removeInspectionResultProcessor( inspectionResultProcessor );
	}

	public void setInspectionResultProcessors( InspectionResultProcessor<Element, UIMetawidget>... inspectionResultProcessors )
	{
		mPipeline.setInspectionResultProcessors( CollectionUtils.newArrayList( inspectionResultProcessors ) );
	}

	public void setWidgetBuilder( WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder )
	{
		mPipeline.setWidgetBuilder( widgetBuilder );
	}

	public void setWidgetProcessors( WidgetProcessor<UIComponent, UIMetawidget>... widgetProcessors )
	{
		mPipeline.setWidgetProcessors( CollectionUtils.newArrayList( widgetProcessors ) );
	}

	public <T> T getWidgetProcessor( Class<T> widgetProcessorClass )
	{
		return mPipeline.getWidgetProcessor( widgetProcessorClass );
	}

	public void setLayout( Layout<UIComponent, UIComponent, UIMetawidget> layout )
	{
		mPipeline.setLayout( layout );
	}

	public Layout<UIComponent, UIComponent, UIMetawidget> getLayout()
	{
		return mPipeline.getLayout();
	}

	/**
	 * Instructs the Metawidget to inspect the value binding from the parent.
	 * <p>
	 * If the value binding is of the form <code>#{foo.bar}</code>, sometimes
	 * <code>foo.getBar()</code> has useful metadata (such as <code>UiLookup</code>). Metawidget
	 * inspects from parent anyway if <code>#{foo.bar}</code> evaluates to <code>null</code>, but on
	 * occasion it may be necessary to specify parent inspection explicitly.
	 * <p>
	 * The disadvantage of inspecting from parent (and the reason it is not enabled by default) is
	 * that some Inspectors will not know the parent and so not be able to return anything. For
	 * example, HibernateInspector only knows the Hibernate XML mapping files of persistent classes,
	 * not the business class of a UI controller, so asking HibernateInspector to inspect
	 * <code>#{controller.current}</code> from its parent will always return <code>null</code>.
	 */

	public void setInspectFromParent( boolean inspectFromParent )
	{
		mInspectFromParent = inspectFromParent;
	}

	/**
	 * @return the text of the label. This may itself contain a value expression, such as
	 *         <code>UiLabel( "#{foo.name}'s name" )</code>
	 */

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
		String localizedKey = null;
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		String appBundle = application.getMessageBundle();

		// Component-specific bundle

		ValueBinding bindingBundle = getValueBinding( "bundle" );

		if ( bindingBundle != null )
		{
			// (watch out when localizing blank labels)

			if ( key == null || key.trim().length() == 0 )
				return "";

			@SuppressWarnings( "unchecked" )
			Map<String, String> bundleMap = (Map<String, String>) bindingBundle.getValue( context );

			// (check for containsKey first, because BundleMap will return a dummy value otherwise)

			if ( bundleMap.containsKey( key ) )
				localizedKey = bundleMap.get( key );
		}

		// App-specific bundle

		else if ( appBundle != null )
		{
			try
			{
				localizedKey = ResourceBundle.getBundle( appBundle ).getString( key );
			}
			catch ( MissingResourceException e )
			{
				// Fail gracefully: we seem to have problems locating, say,
				// org.jboss.seam.core.SeamResourceBundle?

				return null;
			}
		}

		// No bundle

		else
		{
			return null;
		}

		if ( localizedKey != null )
			return localizedKey;

		return StringUtils.RESOURCE_KEY_NOT_FOUND_PREFIX + key + StringUtils.RESOURCE_KEY_NOT_FOUND_SUFFIX;
	}

	/**
	 * Convenience method for <code>metawidget.xml</code>.
	 * <p>
	 * This method will not override existing, manually specified <code>&lt;f:param /&gt;</code>
	 */

	public void setParameter( String name, Object value )
	{
		UIParameter parameter = FacesUtils.findParameterWithName( this, name );

		if ( parameter != null )
			return;

		FacesContext context = getFacesContext();
		parameter = (UIParameter) context.getApplication().createComponent( "javax.faces.Parameter" );
		parameter.setId( context.getViewRoot().createUniqueId() );
		parameter.setName( name );
		parameter.setValue( value );
		getChildren().add( parameter );
	}

	/**
	 * Storage area for WidgetProcessors, Layouts, and other stateless clients.
	 * <p>
	 * Unlike <code>.setAttribute</code>, these values are not serialized by
	 * <code>ResponseStateManagerImpl</code>.
	 */

	public void putClientProperty( Object key, Object value )
	{
		if ( mClientProperties == null )
			mClientProperties = CollectionUtils.newHashMap();

		mClientProperties.put( key, value );
	}

	/**
	 * Storage area for WidgetProcessors, Layouts, and other stateless clients.
	 * <p>
	 * Unlike <code>.getAttribute</code>, these values are not serialized by
	 * <code>ResponseStateManagerImpl</code>.
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getClientProperty( Object key )
	{
		if ( mClientProperties == null )
			return null;

		return (T) mClientProperties.get( key );
	}

	@Override
	public boolean isRendered()
	{
		boolean rendered = super.isRendered();

		if ( mRemoveDuplicatesHack != null )
			mRemoveDuplicatesHack.isRendered( rendered );

		return rendered;
	}

	@Override
	public void encodeBegin( FacesContext context )
		throws IOException
	{
		if ( mRemoveDuplicatesHack != null )
			mRemoveDuplicatesHack.encodeBegin( context );

		super.encodeBegin( context );
	}

	/**
	 * This method is public for use by WidgetBuilders.
	 */

	public Element inspect( Object toInspect, String type, String... names )
	{
		if ( LOG.isTraceEnabled() )
			LOG.trace( "inspect " + type + ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) + " (start)" );

		try
		{
			return mPipeline.inspect( toInspect, type, names );
		}
		finally
		{
			if ( LOG.isTraceEnabled() )
				LOG.trace( "inspect " + type + ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) + " (end)" );
		}
	}

	/**
	 * Get the component type used to create this Metawidget.
	 * <p>
	 * Usually, clients will want to create a nested-Metawidget using the same subclass as
	 * themselves. To be 'proper' in JSF, though, we should go via
	 * <code>application.createComponent</code>. Unfortunately by default a UIComponent does not
	 * know its own component type, so subclasses must override this method.
	 * <p>
	 * This method is public for use by NestedLayoutDecorators.
	 */

	public abstract String getComponentType();

	@Override
	public Object saveState( FacesContext context )
	{
		Object values[] = new Object[5];
		values[0] = super.saveState( context );
		values[1] = mValue;
		values[2] = mReadOnly;
		values[3] = mConfig;
		values[4] = mInspectFromParent;

		return values;
	}

	@Override
	public void restoreState( FacesContext context, Object state )
	{
		Object values[] = (Object[]) state;
		super.restoreState( context, values[0] );

		mValue = values[1];
		mReadOnly = (Boolean) values[2];
		mConfig = (String) values[3];
		mInspectFromParent = (Boolean) values[4];
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

	protected void buildWidgets()
		throws Exception
	{
		configure();

		// Inspect from the value binding...

		ValueBinding valueBinding = getValueBinding( "value" );

		if ( valueBinding != null )
		{
			mPipeline.buildWidgets( inspect( valueBinding, mInspectFromParent ) );
			return;
		}

		// ...or from a raw value (for jBPM)...

		if ( mValue != null )
		{
			mPipeline.buildWidgets( inspect( null, (String) mValue ) );
			return;
		}

		// ...or run without inspection (eg. using the Metawidget purely for layout)

		mPipeline.buildWidgets( null );
	}

	protected UIMetawidget buildNestedMetawidget( String componentType, Map<String, String> attributes )
		throws Exception
	{
		// Create the nested Metawidget...

		FacesContext context = FacesContext.getCurrentInstance();
		UIMetawidget nestedMetawidget = (UIMetawidget) context.getApplication().createComponent( componentType );

		// ...but don't reconfigure it...

		nestedMetawidget.setConfig( null );

		// ...instead, copy runtime values

		mPipeline.initNestedPipeline( nestedMetawidget.mPipeline, attributes );

		// Read-only
		//
		// Note: initNestedPipeline takes care of literal values. This is concerned with the value
		// binding

		if ( !TRUE.equals( attributes.get( READ_ONLY ) ) )
		{
			ValueBinding bindingReadOnly = getValueBinding( "readOnly" );

			if ( bindingReadOnly != null )
				nestedMetawidget.setValueBinding( "readOnly", bindingReadOnly );
		}

		// Bundle

		nestedMetawidget.setValueBinding( "bundle", getValueBinding( "bundle" ) );

		// Renderer type

		nestedMetawidget.setRendererType( getRendererType() );

		// Parameters

		FacesUtils.copyParameters( this, nestedMetawidget );

		// Note: it is very dangerous to do, say...
		//
		// to.getAttributes().putAll( from.getAttributes() );
		//
		// ...in order to copy all arbitary attributes, because some frameworks (eg. Facelets) use
		// the attributes map as a storage area for special flags (eg.
		// ComponentSupport.MARK_CREATED) that should not get copied down from component to
		// component!

		return nestedMetawidget;
	}

	/**
	 * Inspect the value binding.
	 * <p>
	 * A value binding is optional. UIMetawidget can be used purely to lay out manually-specified
	 * components
	 */

	protected Element inspect( ValueBinding valueBinding, boolean inspectFromParent )
	{
		if ( valueBinding == null )
			return null;

		// Inspect the object directly

		FacesContext context = getFacesContext();
		String valueBindingString = valueBinding.getExpressionString();

		if ( !inspectFromParent || !FacesUtils.isExpression( valueBindingString ) )
		{
			Object toInspect = valueBinding.getValue( context );

			if ( toInspect != null && !ClassUtils.isPrimitiveWrapper( toInspect.getClass() ) )
			{
				Class<?> classToInspect = ClassUtils.getUnproxiedClass( toInspect.getClass() );
				return inspect( toInspect, classToInspect.getName() );
			}
		}

		// In the event the direct object is 'null' or a primitive...

		String binding = FacesUtils.unwrapExpression( valueBindingString );

		// ...and the EL expression is such that we can chop it off to get to the parent...
		//
		// Note: using EL functions in generated ValueExpressions only works in JSF 2.0,
		// see https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=813. A workaround is
		// to assign the function's return value to a temporary, request-scoped variable using c:set

		if ( binding.indexOf( ' ' ) == -1 && binding.indexOf( ':' ) == -1 && binding.indexOf( '(' ) == -1 )
		{
			int lastIndexOf = binding.lastIndexOf( StringUtils.SEPARATOR_DOT_CHAR );

			if ( lastIndexOf != -1 )
			{
				// ...traverse from the parent as there may be useful metadata there (such as 'name'
				// and 'type')

				Application application = context.getApplication();
				ValueBinding bindingParent = application.createValueBinding( FacesUtils.wrapExpression( binding.substring( 0, lastIndexOf ) ) );
				Object toInspect = bindingParent.getValue( context );

				if ( toInspect != null )
				{
					Class<?> classToInspect = ClassUtils.getUnproxiedClass( toInspect.getClass() );
					return inspect( toInspect, classToInspect.getName(), binding.substring( lastIndexOf + 1 ) );
				}
			}
		}

		return null;
	}

	protected void configure()
	{
		if ( !mNeedsConfiguring )
			return;

		LOG.trace( "configure (start)" );

		mNeedsConfiguring = false;

		FacesContext facesContext = getFacesContext();
		Map<String, Object> applicationMap = facesContext.getExternalContext().getApplicationMap();
		ConfigReader configReader = (ConfigReader) applicationMap.get( APPLICATION_ATTRIBUTE_CONFIG_READER );

		if ( configReader == null )
		{
			configReader = new FacesConfigReader();
			applicationMap.put( APPLICATION_ATTRIBUTE_CONFIG_READER, configReader );
		}

		if ( mConfig != null )
		{
			try
			{
				if ( LOG.isTraceEnabled() )
					LOG.trace( "configure from " + mConfig + " (start)" );

				configReader.configure( mConfig, this );
			}
			catch ( MetawidgetException e )
			{
				if ( !DEFAULT_USER_CONFIG.equals( mConfig ) || !( e.getCause() instanceof FileNotFoundException ) )
					throw e;

				if ( !LOGGED_MISSING_CONFIG )
				{
					LOGGED_MISSING_CONFIG = true;
					LogUtils.getLog( UIMetawidget.class ).info( "Could not locate " + DEFAULT_USER_CONFIG + ". This file is optional, but if you HAVE created one then Metawidget isn't finding it!" );
				}
			}
			finally
			{
				if ( LOG.isTraceEnabled() )
					LOG.trace( "configure from " + mConfig + " (end)" );
			}
		}

		mPipeline.configureDefaults( configReader, getDefaultConfiguration(), UIMetawidget.class );

		LOG.trace( "configure (end)" );
	}

	protected abstract String getDefaultConfiguration();

	/**
	 * Build child widgets.
	 */

	protected void startBuild()
	{
		LOG.trace( "startBuild" );

		// Metawidget has no valueBinding? Won't be destroying/recreating any components, then.
		//
		// This is an optimisation, but is also important for cases like RichFacesLayout, which
		// use nested Metawidgets (without a value binding) purely for layout. They populate a new
		// Metawidget with previously inspected components, and we don't want them destroyed
		// here and/or unnecessarily re-inspected in endBuild

		if ( getValueBinding( "value" ) == null )
			return;

		// Remove any components we created previously (this is
		// important for polymorphic controls, which may change from
		// refresh to refresh)

		List<UIComponent> children = getChildren();

		for ( Iterator<UIComponent> i = children.iterator(); i.hasNext(); )
		{
			UIComponent componentChild = i.next();
			Map<String, Object> attributes = componentChild.getAttributes();

			// The first time in, children will have no metadata attached. Use this opportunity
			// to tag the initial children so that we never recreate them

			if ( !attributes.containsKey( COMPONENT_ATTRIBUTE_METADATA ) )
			{
				attributes.put( COMPONENT_ATTRIBUTE_NOT_RECREATABLE, true );
				continue;
			}

			// Do not remove locked or overridden components...

			if ( attributes.containsKey( COMPONENT_ATTRIBUTE_NOT_RECREATABLE ) )
			{
				// ...but always remove their metadata, otherwise
				// they will not be removed/re-added (and therefore re-ordered) upon POSTback

				attributes.remove( COMPONENT_ATTRIBUTE_METADATA );
				continue;
			}

			i.remove();
		}
	}

	protected void layoutWidget( UIComponent component, String elementName, Map<String, String> attributes )
	{
		Map<String, Object> componentAttributes = component.getAttributes();
		componentAttributes.put( COMPONENT_ATTRIBUTE_METADATA, attributes );

		// If this component already exists in the list, remove it and re-add it. This
		// enables us to sort existing, manually created components in the correct order
		//
		// Doing the remove here, rather than in SimpleLayout, ensures we always remove and
		// add for cases like moving a Stub from outside a TabPanel to inside it

		getChildren().remove( component );

		// Look up any additional attributes

		Map<String, String> additionalAttributes = mPipeline.getAdditionalAttributes( component );

		if ( additionalAttributes != null )
			attributes.putAll( additionalAttributes );

		// BasePipeline will call .layoutWidget
	}

	protected void endBuild()
	{
		List<UIComponent> children = getChildren();

		// Inspect any remaining components, and sort them to the bottom

		for ( int loop = 0, index = 0, length = children.size(); loop < length; loop++ )
		{
			UIComponent component = children.get( index );

			// If this component has already been processed by the inspection (ie. contains
			// metadata), is not rendered, or is a UIParameter, skip it
			//
			// This is also handy for RichFacesLayout, which uses a nested Metawidget purely as a
			// layout tool: it populates a new Metawidget with some previously inspected components.
			// This check makes sure they aren't unnecessarily re-inspected here

			Map<String, Object> miscAttributes = component.getAttributes();

			if ( miscAttributes.containsKey( COMPONENT_ATTRIBUTE_METADATA ) || !component.isRendered() || component instanceof UIParameter )
			{
				index++;
				continue;
			}

			// Try and determine some metadata for the component by inspecting its binding. This
			// helps our layout display proper labels, required stars etc. - even for components
			// whose binding is not a descendant of our parent binding

			Map<String, String> childAttributes = CollectionUtils.newHashMap();
			miscAttributes.put( COMPONENT_ATTRIBUTE_METADATA, childAttributes );

			ValueBinding binding = component.getValueBinding( "value" );

			if ( binding != null )
			{
				Element inspectionResult = inspect( binding, true );

				if ( inspectionResult != null )
					childAttributes.putAll( XmlUtils.getAttributesAsMap( inspectionResult.getFirstChild() ) );
			}
			else
			{
				// If no found metadata, default to no section.
				//
				// This is so if a user puts, say, a <t:div/> in the component tree, it doesn't
				// appear inside an existing section

				childAttributes.put( SECTION, "" );
			}

			mPipeline.layoutWidget( component, PROPERTY, childAttributes );
		}

		LOG.trace( "endBuild" );
	}

	//
	// Inner class
	//

	protected class Pipeline
		extends W3CPipeline<UIComponent, UIComponent, UIMetawidget>
	{
		//
		// Public methods
		//

		/**
		 * Overriden to just-in-time evaluate EL binding.
		 */

		@Override
		public boolean isReadOnly()
		{
			return UIMetawidget.this.isReadOnly();
		}

		@Override
		public void setReadOnly( boolean readOnly )
		{
			UIMetawidget.this.setReadOnly( readOnly );
		}

		//
		// Protected methods
		//

		@Override
		protected void startBuild()
		{
			super.startBuild();
			UIMetawidget.this.startBuild();
		}

		@Override
		protected Map<String, String> getAdditionalAttributes( UIComponent widget )
		{
			if ( widget instanceof UIStub )
				return ( (UIStub) widget ).getStubAttributes();

			return null;
		}

		@Override
		protected UIComponent buildWidget( String elementName, Map<String, String> attributes )
		{
			if ( LOG.isTraceEnabled() )
				LOG.trace( "buildWidget for " + elementName + " named " + attributes.get( NAME ) + " (start)" );

			UIComponent widget = super.buildWidget( elementName, attributes );

			if ( LOG.isTraceEnabled() )
				LOG.trace( "buildWidget returned " + widget + " (end)" );

			return widget;
		}

		@Override
		protected UIMetawidget buildNestedMetawidget( Map<String, String> attributes )
			throws Exception
		{
			return UIMetawidget.this.buildNestedMetawidget( UIMetawidget.this.getComponentType(), attributes );
		}

		@Override
		protected void layoutWidget( UIComponent component, String elementName, Map<String, String> attributes )
		{
			UIMetawidget.this.layoutWidget( component, elementName, attributes );
			super.layoutWidget( component, elementName, attributes );
		}

		@Override
		protected void endBuild()
		{
			super.endBuild();
			UIMetawidget.this.endBuild();
		}

		@Override
		protected UIMetawidget getPipelineOwner()
		{
			return UIMetawidget.this;
		}
	}

	/**
	 * Dynamically modify the component tree using the JSF1 API.
	 * <p>
	 * <h3>Background</h3>
	 * <p>
	 * JSF1 did not have very good support for dynamically modifying the component tree. See
	 * http://osdir.com/ml/java.facelets.user/2008-06/msg00050.html:
	 * <p>
	 * Jacob Hookum: "What's actually needed in [JSF 1.2] is post component tree creation or post
	 * component creation hooks, providing the ability to then modify the component tree"<br/>
	 * Ken Paulsen: "This hasn't been resolved in the 2.0 EG yet"
	 * <p>
	 * We tried various workarounds:
	 * <p>
	 * <ol>
	 * <li>Triggering buildWidgets on getChildCount/getChildren. This does not work because those
	 * methods get called at all sorts of other times</li>
	 * <li>Doing it in super.encodeBegin for a GET, in processUpdates for a POST. This does not work
	 * because the GET still records the bad components</li>
	 * <li>A PhaseListener before PhaseId.RENDER_RESPONSE to trigger buildWidgets. This does not
	 * work because UIViewRoot has no children at that stage in the lifecycle</li>
	 * </ol>
	 * JSF2 introduced <code>SystemEvents</code> to address this exact problem. See
	 * <code>SystemEventSupport</code> below.
	 * <p>
	 * <h3>Why It's A Problem</h3>
	 * <p>
	 * JSF actually has (sort of) <em>two</em> component trees: the one in the ViewState, and the
	 * components in the original JSP page. The latter is re-merged with the former, then the whole
	 * lot is serialized. This happens after <code>processUpdates</code> but before
	 * <code>encodeBegin</code>, which is a bit of a 'dead zone' for hooking into under JSF1.
	 * <p>
	 * It can cause an Exception if the original JSP contains a manually coded control (such as an
	 * h:inputHidden) that subsequently gets moved into a Metawidget-generated sub-container (such
	 * as a rich:simpleTogglePanel). Now there are two versions of the component: one in the
	 * original JSP and one in a <em>different</em> place in the ViewState.
	 * <p>
	 * This hack removes that duplicate.
	 */

	private static class RemoveDuplicatesHack
	{
		//
		// Private members
		//

		private UIMetawidget	mMetawidget;

		//
		// Constructor
		//

		public RemoveDuplicatesHack( UIMetawidget metawidget )
		{
			mMetawidget = metawidget;
		}

		//
		// Public methods
		//

		/**
		 * If the component is never going to be rendered, then <code>encodeBegin</code> will never
		 * get called. Therefore our 'remove duplicates' code will never get called either.
		 */

		public void isRendered( boolean rendered )
		{
			if ( !rendered )
				mMetawidget.getChildren().clear();
		}

		public void encodeBegin( FacesContext context )
			throws IOException
		{
			try
			{
				// Validation error? Do not rebuild, as we will lose the invalid values in the
				// components. Instead, just move along to our renderer

				if ( context.getMaximumSeverity() != null )
				{
					// Remove duplicate
					//
					// Remove the top-level version of the duplicate, not the nested-level version,
					// because the top-level is the 'original' whereas the nested-level is the
					// 'moved'. We will not be rebuilding the component tree, so we want the
					// 'moved' one (ie. at its final destination)

					for ( Iterator<UIComponent> i = mMetawidget.getChildren().iterator(); i.hasNext(); )
					{
						UIComponent component = i.next();

						if ( findComponentWithId( mMetawidget, component.getId(), component ) != null )
							i.remove();
					}

					return;
				}

				// Build widgets as normal
				//
				// Note: calling buildWidgets here means we are modifying the component tree during
				// the Renderer phase, which is dangerous. The ideal fix would be to .buildWidgets
				// BEFORE the component tree gets serialized (see JavaDoc above)

				mMetawidget.buildWidgets();
			}
			catch ( Exception e )
			{
				// IOException does not take a Throwable 'cause' argument until Java 6, so
				// as we need to stay 1.4 compatible we output the trace here

				LogUtils.getLog( getClass() ).error( "Unable to encodeBegin", e );

				// At this level, it is more 'proper' to throw an IOException than
				// a MetawidgetException, as that is what the layers above are expecting

				throw new IOException( e.getMessage() );
			}
		}

		//
		// Private methods
		//

		private UIComponent findComponentWithId( UIComponent component, String id, UIComponent ignore )
		{
			if ( id == null )
				return null;

			for ( UIComponent child : component.getChildren() )
			{
				if ( child == ignore )
					continue;

				if ( id.equals( child.getId() ) )
					return child;

				UIComponent found = findComponentWithId( child, id, ignore );

				if ( found != null )
					return found;
			}

			return null;
		}
	}

	/**
	 * Dynamically modify the component tree using the JSF2 API.
	 * <p>
	 * JSF2 introduced <code>SystemEvents</code>, which we can use to avoid
	 * <code>RemoveDuplicatesHack</code>.
	 */

	private static class SystemEventSupport
		implements SystemEventListener
	{
		//
		// Private members
		//

		private UIMetawidget	mMetawidget;

		//
		// Constructor
		//

		public SystemEventSupport( UIMetawidget metawidget )
		{
			mMetawidget = metawidget;

			FacesContext context = FacesContext.getCurrentInstance();
			UIViewRoot root = context.getViewRoot();
			root.subscribeToViewEvent( PostAddToViewEvent.class, this );
		}

		//
		// Public methods
		//

		public boolean isListenerForSource( Object source )
		{
			return ( source instanceof UIViewRoot );
		}

		public void processEvent( SystemEvent event )
			throws AbortProcessingException
		{
			// Validation error? Do not rebuild, as we will lose the invalid values in the
			// components. Instead, just move along to our renderer

			if ( FacesContext.getCurrentInstance().getMaximumSeverity() != null )
				return;

			// Build widgets as normal

			try
			{
				mMetawidget.buildWidgets();
			}
			catch ( Exception e )
			{
				// At this level, it is more 'proper' to throw an AbortProcessingException than
				// a MetawidgetException, as that is what the layers above are expecting

				throw new AbortProcessingException( e );
			}
		}
	}
}