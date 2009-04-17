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
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;

import javax.faces.application.Application;
import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.NumberConverter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpSession;

import org.metawidget.MetawidgetException;
import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.validator.StandardValidator;
import org.metawidget.faces.component.validator.Validator;
import org.metawidget.inspector.ConfigReader;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.jsp.ServletConfigReader;
import org.metawidget.mixin.w3c.MetawidgetMixin;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.w3c.dom.Document;
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

	public final static String	COMPONENT_ATTRIBUTE_METADATA				= "metawidget-metadata";

	//
	// Private statics
	//

	/**
	 * Component-level attribute used to tag components as being created by Metawidget.
	 */

	private final static String	COMPONENT_ATTRIBUTE_CREATED_BY_METAWIDGET	= "metawidget-created-by";

	/**
	 * Application-level attribute used to cache ConfigReader.
	 */

	private final static String	APPLICATION_ATTRIBUTE_CONFIG_READER			= "metawidget-config-reader";

	//
	// Private members
	//

	private Object				mValue;

	private String				mConfig										= "metawidget.xml";

	private boolean				mNeedsConfiguring							= true;

	private boolean				mInspectFromParent;

	private Set<String>			mClientIds;

	private Boolean				mReadOnly;

	private String				mValidatorClass								= StandardValidator.class.getName();

	private Validator			mValidator;

	private String				mBindingPrefix;

	private UIMetawidgetMixin	mMetawidgetMixin;

	//
	// Constructor
	//

	public UIMetawidget()
	{
		mMetawidgetMixin = newMetawidgetMixin();

		// Default renderer

		setRendererType( "table" );
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
		mMetawidgetMixin.setInspector( inspector );
	}

	public void setWidgetBuilder( WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder )
	{
		mMetawidgetMixin.setWidgetBuilder( widgetBuilder );
	}

	/**
	 * Instructs the Metawidget to inspect the value binding from the parent.
	 * <p>
	 * If the value binding is of the form <code>#{foo.bar}</code>, sometimes
	 * <code>foo.getBar()</code> has useful metadata (such as <code>UiLookup</code>).
	 * Metawidget inspects from parent anyway if <code>#{foo.bar}</code> evaluates to
	 * <code>null</code>, but on occasion it may be necessary to specify parent inspection
	 * explicitly.
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

	public void setValidatorClass( String validatorClass )
	{
		mValidatorClass = validatorClass;
		mValidator = null;
	}

	/**
	 * @return the text of the label. This may itself contain a value expression, such as
	 *         <code>UiLabel( "#{foo.name}'s name" )</code>
	 */

	public String getLabelString( FacesContext context, Map<String, String> attributes )
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

			String localized = getLocalizedKey( context, StringUtils.camelCase( label ) );

			if ( localized != null )
				return localized.trim();

			return label.trim();
		}

		// Default name

		String name = attributes.get( NAME );

		if ( name != null )
		{
			// (localize if possible)

			String localized = getLocalizedKey( context, name );

			if ( localized != null )
				return localized.trim();

			return StringUtils.uncamelCase( name );
		}

		return "";
	}

	/**
	 * @return null if no bundle, ???key??? if bundle is missing a key
	 */

	public String getLocalizedKey( FacesContext context, String key )
	{
		String localizedKey = null;
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
	 */

	public void setParameter( String name, Object value )
	{
		UIParameter parameter = FacesUtils.findParameterWithName( this, name );

		if ( parameter == null )
		{
			parameter = new UIParameter();
			getChildren().add( parameter );
		}

		parameter.setName( name );
		parameter.setValue( value );
	}

	@Override
	public void encodeBegin( FacesContext context )
		throws IOException
	{
		try
		{
			configure();

			// Inspect from the value binding...

			ValueBinding valueBinding = getValueBinding( "value" );

			if ( valueBinding != null )
			{
				mMetawidgetMixin.buildWidgets( inspect( valueBinding, mInspectFromParent ) );
				super.encodeBegin( context );
				return;
			}

			// ...or from a raw value (for jBPM)...

			if ( mValue != null )
			{
				mMetawidgetMixin.buildWidgets( mMetawidgetMixin.inspect( null, (String) mValue ) );
				super.encodeBegin( context );
				return;
			}

			// ...or run without inspection (eg. using the Metawidget purely for layout)

			mMetawidgetMixin.buildWidgets( null );
			super.encodeBegin( context );
		}
		catch ( Exception e )
		{
			// IOException does not take a Throwable 'cause' argument until 1.6, so
			// as we need to stay 1.4 compatible we output the trace here

			LogUtils.getLog( getClass() ).error( "Unable to encodeBegin", e );

			// At this top level, it is more 'proper' to throw an IOException than
			// a MetawidgetException, as that is what the layers above are expecting

			throw new IOException( e.getMessage() );
		}
	}

	@Override
	public Object saveState( FacesContext context )
	{
		Object values[] = new Object[6];
		values[0] = super.saveState( context );
		values[1] = mValue;
		values[2] = mReadOnly;
		values[3] = mConfig;
		values[4] = mValidatorClass;
		values[5] = mInspectFromParent;

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
		mValidatorClass = (String) values[4];
		mInspectFromParent = (Boolean) values[5];
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

	protected UIMetawidgetMixin newMetawidgetMixin()
	{
		return new UIMetawidgetMixin();
	}

	protected UIMetawidgetMixin getMetawidgetMixin()
	{
		return mMetawidgetMixin;
	}

	/**
	 * Inspect the value binding.
	 * <p>
	 * A value binding is optional. UIMetawidget can be used purely to lay out manually-specified
	 * components
	 */

	protected String inspect( ValueBinding valueBinding, boolean inspectFromParent )
	{
		if ( valueBinding == null )
			return null;

		// Inspect the object directly

		FacesContext context = getFacesContext();
		String valueBindingString = valueBinding.getExpressionString();

		if ( !inspectFromParent || !FacesUtils.isValueReference( valueBindingString ) )
		{
			Object toInspect = valueBinding.getValue( context );

			if ( toInspect != null && !ClassUtils.isPrimitiveWrapper( toInspect.getClass() ) )
			{
				Class<?> classToInspect = ClassUtils.getUnproxiedClass( toInspect.getClass() );
				return mMetawidgetMixin.inspect( toInspect, classToInspect.getName() );
			}
		}

		// In the event the direct object is 'null' or a primitive...

		String binding = FacesUtils.unwrapValueReference( valueBindingString );

		// ...and the EL expression is such that we can chop it off to get to the parent...
		//
		// Note: using EL functions in generated ValueExpressions doesn't actually work yet,
		// see https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=813. A workaround is
		// to assign the function to a temporary, request-scoped variable using c:set

		if ( binding.indexOf( ' ' ) == -1 && binding.indexOf( ':' ) == -1 && binding.indexOf( '(' ) == -1 )
		{
			int lastIndexOf = binding.lastIndexOf( StringUtils.SEPARATOR_DOT_CHAR );

			if ( lastIndexOf != -1 )
			{
				// ...traverse from the parent as there may be useful metadata there (such as 'name'
				// and 'type')

				Application application = context.getApplication();
				ValueBinding bindingParent = application.createValueBinding( FacesUtils.wrapValueReference( binding.substring( 0, lastIndexOf ) ) );
				Object toInspect = bindingParent.getValue( context );

				if ( toInspect != null )
				{
					Class<?> classToInspect = ClassUtils.getUnproxiedClass( toInspect.getClass() );
					return mMetawidgetMixin.inspect( toInspect, classToInspect.getName(), binding.substring( lastIndexOf + 1 ) );
				}
			}
		}

		return null;
	}

	@SuppressWarnings( "unchecked" )
	protected void configure()
		throws Exception
	{
		if ( !mNeedsConfiguring )
			return;

		mNeedsConfiguring = false;

		FacesContext facesContext = getFacesContext();
		Map<String, Object> applicationMap = facesContext.getExternalContext().getApplicationMap();
		@SuppressWarnings( "unchecked" )
		ConfigReader configReader = (ConfigReader) applicationMap.get( APPLICATION_ATTRIBUTE_CONFIG_READER );

		if ( configReader == null )
		{
			configReader = new ServletConfigReader( ( (HttpSession) facesContext.getExternalContext().getSession( false ) ).getServletContext() );
			applicationMap.put( APPLICATION_ATTRIBUTE_CONFIG_READER, configReader );
		}

		if ( mConfig != null )
			configReader.configure( mConfig, this );

		// Sensible defaults

		if ( mMetawidgetMixin.getWidgetBuilder() == null )
			mMetawidgetMixin.setWidgetBuilder( configReader.configure( getDefaultConfiguration(), WidgetBuilder.class ) );

		if ( mMetawidgetMixin.getInspector() == null )
			mMetawidgetMixin.setInspector( configReader.configure( getDefaultConfiguration(), Inspector.class ) );
	}

	protected abstract String getDefaultConfiguration();

	/**
	 * Build child widgets.
	 */

	protected void startBuild()
	{
		// Remove any components we created previously (this is
		// important for polymorphic controls, which may change from
		// refresh to refresh)

		List<UIComponent> children = getChildren();

		for ( Iterator<UIComponent> i = children.iterator(); i.hasNext(); )
		{
			UIComponent componentChild = i.next();

			Map<String, Object> attributes = componentChild.getAttributes();

			if ( attributes.containsKey( COMPONENT_ATTRIBUTE_CREATED_BY_METAWIDGET ) )
			{
				// Do not recreate the components in the event of a validation error,
				// as that will clear any values in the components (eg. the erroneous values)

				if ( getFacesContext().getMaximumSeverity() == null )
				{
					i.remove();
					continue;
				}
			}

			// If we did not create the component, or if we created it but this is a POSTback, at
			// least remove its metadata. This is important as otherwise ad-hoc components (eg.
			// those not directly descended from our value binding) are not removed/re-added (and
			// therefore re-ordered) upon POSTback

			attributes.remove( COMPONENT_ATTRIBUTE_METADATA );
		}

		mClientIds = null;
		mBindingPrefix = null;

		// Validator

		if ( mValidator == null && mValidatorClass != null && !"".equals( mValidatorClass ) )
		{
			try
			{
				@SuppressWarnings( "unchecked" )
				Constructor<? extends Validator> constructor = ( (Class<? extends Validator>) ClassUtils.niceForName( mValidatorClass ) ).getConstructor( UIMetawidget.class );
				mValidator = constructor.newInstance( this );
			}
			catch ( Exception e )
			{
				throw MetawidgetException.newException( e );
			}
		}
	}

	protected UIComponent getOverriddenWidget( String elementName, Map<String, String> attributes )
	{
		String binding = attributes.get( FACES_EXPRESSION );

		// Actions

		if ( ACTION.equals( elementName ) )
		{
			if ( binding == null )
			{
				if ( mBindingPrefix == null )
				{
					ValueBinding methodBinding = getValueBinding( "value" );

					if ( methodBinding != null )
					{
						binding = FacesUtils.unwrapValueReference( methodBinding.getExpressionString() );
					}

					// Not using a valueBinding? Using a raw value (eg. for jBPM)?

					else
					{
						binding = attributes.get( NAME );
					}
				}
				else
				{
					binding = FacesUtils.wrapValueReference( mBindingPrefix + attributes.get( NAME ) );
				}
			}

			UIComponent overridenWidget = FacesUtils.findRenderedComponentWithMethodBinding( UIMetawidget.this, binding );

			if ( overridenWidget != null )
				return overridenWidget;

			// Special support for overriding a method binding using a component with a matching
			// value binding (eg. for overriding UICommands using UIStubs)

			return FacesUtils.findRenderedComponentWithValueBinding( UIMetawidget.this, binding );
		}

		// Properties

		if ( binding == null )
		{
			if ( mBindingPrefix == null )
			{
				ValueBinding valueBindingChild = getValueBinding( "value" );

				// Metawidget has no valueBinding? Not overridable, then

				if ( valueBindingChild == null )
					return null;

				binding = valueBindingChild.getExpressionString();
			}
			else
			{
				binding = FacesUtils.wrapValueReference( mBindingPrefix + attributes.get( NAME ) );
			}
		}

		return FacesUtils.findRenderedComponentWithValueBinding( UIMetawidget.this, binding );
	}

	protected void endBuild()
		throws Exception
	{
		List<UIComponent> children = getChildren();

		// Inspect any remaining components, and sort them to the bottom

		for ( int loop = 0, index = 0, length = children.size(); loop < length; loop++ )
		{
			UIComponent component = children.get( index );

			Map<String, Object> miscAttributes = component.getAttributes();

			if ( miscAttributes.containsKey( COMPONENT_ATTRIBUTE_METADATA ) || !component.isRendered() )
			{
				index++;
				continue;
			}

			Map<String, String> childAttributes = CollectionUtils.newHashMap();
			miscAttributes.put( COMPONENT_ATTRIBUTE_METADATA, childAttributes );

			// Inspect metadata

			ValueBinding binding = component.getValueBinding( "value" );

			if ( binding != null )
			{
				String xml = inspect( binding, true );

				if ( xml != null )
				{
					Document document = XmlUtils.documentFromString( xml );
					childAttributes.putAll( XmlUtils.getAttributesAsMap( document.getDocumentElement().getFirstChild() ) );
				}
			}
			else
			{
				// Manually created components with no found metadata default to no section

				if ( !miscAttributes.containsKey( COMPONENT_ATTRIBUTE_CREATED_BY_METAWIDGET ) )
					childAttributes.put( SECTION, "" );
			}

			// Stubs

			if ( component instanceof UIStub )
				childAttributes.putAll( ( mMetawidgetMixin ).getStubAttributes( component ) );

			addWidget( component );
		}
	}

	protected void beforeBuildCompoundWidget( Element element )
	{
		ValueBinding valueBinding = getValueBinding( "value" );

		// Not using a valueBinding? Using a raw value (eg. for jBPM)?

		if ( valueBinding == null )
			return;

		mBindingPrefix = FacesUtils.unwrapValueReference( valueBinding.getExpressionString() ) + StringUtils.SEPARATOR_DOT_CHAR;
	}

	protected UIComponent afterBuildWidget( UIComponent component, Map<String, String> attributes )
		throws Exception
	{
		if ( component == null )
			return null;

		// Immediate

		String immediateString = attributes.get( FACES_IMMEDIATE );

		if ( immediateString != null )
		{
			boolean immediate = Boolean.parseBoolean( immediateString );

			if ( component instanceof ActionSource )
				( (ActionSource) component ).setImmediate( immediate );
			else if ( component instanceof EditableValueHolder )
				( (EditableValueHolder) component ).setImmediate( immediate );
			else
				throw new Exception( "'Immediate' cannot be applied to " + component.getClass() );
		}

		return component;
	}

	protected abstract UIMetawidget buildNestedMetawidget( Map<String, String> attributes );

	protected void initNestedMetawidget( UIMetawidget nestedMetawidget, Map<String, String> attributes )
		throws Exception
	{
		nestedMetawidget.setConfig( mConfig );

		// Read-only
		//
		// Note: this isn't just the read-only setting of the parent Metawidget, it must also
		// take into account any property-level attributes

		if ( TRUE.equals( attributes.get( READ_ONLY ) ) )
		{
			nestedMetawidget.setReadOnly( true );
		}
		else
		{
			ValueBinding bindingReadOnly = getValueBinding( "readOnly" );

			if ( bindingReadOnly != null )
				nestedMetawidget.setValueBinding( "readOnly", bindingReadOnly );
			else
				nestedMetawidget.setReadOnly( mReadOnly );
		}

		// Bundle

		nestedMetawidget.setValueBinding( "bundle", getValueBinding( "bundle" ) );

		// Renderer type

		nestedMetawidget.setRendererType( getRendererType() );

		// Attributes and Parameters

		FacesUtils.copyAttributes( this, nestedMetawidget );
		FacesUtils.copyParameters( this, nestedMetawidget, "columns" );

		// Other

		nestedMetawidget.setValidatorClass( mValidatorClass );

		// Don't use human-readable Id's for nested Metawidgets, because if they
		// only expand to a single child it will give the child component
		// a suffixed id

		nestedMetawidget.setId( getFacesContext().getViewRoot().createUniqueId() );
	}

	/**
	 * Unlike <code>UIViewRoot.createUniqueId</code>, tries to make the Id human readable, both
	 * for debugging purposes and for when running unit tests (using, say, WebTest).
	 */

	protected void setUniqueId( FacesContext context, UIComponent component, String expressionString )
	{
		// Avoid duplicates

		if ( mClientIds == null )
		{
			mClientIds = CollectionUtils.newHashSet();

			Iterator<UIComponent> iteratorFacetsAndChildren = context.getViewRoot().getFacetsAndChildren();
			gatherClientIds( iteratorFacetsAndChildren );
		}

		// Create our ideal Id

		Map<String, Object> attributes = component.getAttributes();
		attributes.put( COMPONENT_ATTRIBUTE_CREATED_BY_METAWIDGET, Boolean.TRUE );

		String idealId = StringUtils.camelCase( FacesUtils.unwrapValueReference( expressionString ), StringUtils.SEPARATOR_DOT_CHAR );
		String actualId = idealId;
		int duplicateId = 1;

		while ( true )
		{
			if ( mClientIds.add( actualId ) )
				break;

			duplicateId++;
			actualId = idealId + '_' + duplicateId;
		}

		// Support stubs

		if ( component instanceof UIStub )
		{
			List<UIComponent> children = component.getChildren();

			if ( !children.isEmpty() )
			{
				int childId = 1;

				for ( UIComponent componentChild : children )
				{
					if ( childId > 1 )
						componentChild.setId( actualId + '_' + childId );
					else
						componentChild.setId( actualId );

					childId++;
				}

				return;
			}
		}

		// Set Id

		component.setId( actualId );
	}

	/**
	 * Attach value binding for component. We only do this for created components.
	 * <p>
	 * In addition, if the created component is a UIStub, we set the same value binding on
	 * <em>all</em> its children. This allows us to built compound components.
	 */

	protected void attachValueBinding( UIComponent widget, ValueBinding valueBinding, Map<String, String> attributes )
	{
		// Support stubs

		if ( widget instanceof UIStub )
		{
			List<UIComponent> children = widget.getChildren();

			for ( UIComponent componentChild : children )
			{
				attachValueBinding( componentChild, valueBinding, attributes );
			}

			return;
		}

		// Set binding

		widget.setValueBinding( "value", valueBinding );
	}

	/**
	 * JSF 1.2 version of attachValueBinding.
	 */

	protected void attachValueExpression( UIComponent widget, Object valueExpression, Map<String, String> attributes )
	{
		// Support stubs

		if ( widget instanceof UIStub )
		{
			List<UIComponent> children = widget.getChildren();

			for ( UIComponent componentChild : children )
			{
				attachValueExpression( componentChild, valueExpression, attributes );
			}

			return;
		}

		// Set binding

		widget.setValueExpression( "value", (javax.el.ValueExpression) valueExpression );
	}

	/**
	 * Attach metadata for renderer. We do this even for manually created components.
	 */

	protected void putMetadata( UIComponent widget, Map<String, String> attributes )
	{
		Map<String, Object> componentAttributes = widget.getAttributes();
		componentAttributes.put( COMPONENT_ATTRIBUTE_METADATA, attributes );
	}

	/**
	 * Attach converter for renderer.
	 *
	 * @param component
	 *            the component to attach the converter to. Need not be a ValueHolder (eg. might be
	 *            a UIStub, in which case the converter is attached to its children)
	 * @return the converter that was attached
	 */

	public Converter setConverter( UIComponent component, Map<String, String> attributes )
	{
		// Recurse into stubs

		if ( component instanceof UIStub )
		{
			List<UIComponent> children = component.getChildren();

			for ( UIComponent componentChild : children )
			{
				setConverter( componentChild, attributes );
			}

			return null;
		}

		// Ignore components that cannot have Converters

		if ( !( component instanceof ValueHolder ) )
			return null;

		// Apply the converter

		try
		{
			ValueHolder valueHolder = (ValueHolder) component;

			// Do not override existing Converter (if any)

			Converter converter = valueHolder.getConverter();

			if ( converter != null )
				return converter;

			// Create from id

			String converterId = attributes.get( FACES_CONVERTER_ID );

			if ( converterId != null )
			{
				converter = getFacesContext().getApplication().createConverter( converterId );
			}

			// Create from parameterized type (eg. a Date converter for List<Date>)

			else if ( component instanceof UISelectOne || component instanceof UISelectMany )
			{
				String parameterizedType = attributes.get( PARAMETERIZED_TYPE );

				if ( parameterizedType != null )
				{
					Class<?> parameterizedClass = ClassUtils.niceForName( parameterizedType );

					// The parameterized type might be null, or might not be concrete
					// enough to be instantiatable (eg. List<? extends Foo>)

					if ( parameterizedClass != null )
						converter = getFacesContext().getApplication().createConverter( parameterizedClass );
				}
			}

			// Native support for DateTimeConverter

			if ( attributes.containsKey( DATE_STYLE ) )
			{
				converter = getDateTimeConverter( converter );
				( (DateTimeConverter) converter ).setDateStyle( attributes.get( DATE_STYLE ) );
			}

			if ( attributes.containsKey( LOCALE ) )
			{
				converter = getDateTimeConverter( converter );
				( (DateTimeConverter) converter ).setLocale( new Locale( attributes.get( LOCALE ) ) );
			}

			if ( attributes.containsKey( DATETIME_PATTERN ) )
			{
				converter = getDateTimeConverter( converter );
				( (DateTimeConverter) converter ).setPattern( attributes.get( DATETIME_PATTERN ) );
			}

			if ( attributes.containsKey( TIME_STYLE ) )
			{
				converter = getDateTimeConverter( converter );
				( (DateTimeConverter) converter ).setTimeStyle( attributes.get( TIME_STYLE ) );
			}

			if ( attributes.containsKey( TIME_ZONE ) )
			{
				converter = getDateTimeConverter( converter );
				( (DateTimeConverter) converter ).setTimeZone( TimeZone.getTimeZone( attributes.get( TIME_ZONE ) ) );
			}

			if ( attributes.containsKey( DATETIME_TYPE ) )
			{
				converter = getDateTimeConverter( converter );
				( (DateTimeConverter) converter ).setType( attributes.get( DATETIME_TYPE ) );
			}

			// Native support for NumberConverter

			if ( attributes.containsKey( CURRENCY_CODE ) )
			{
				converter = getNumberConverter( converter );
				( (NumberConverter) converter ).setCurrencyCode( attributes.get( CURRENCY_CODE ) );
			}

			if ( attributes.containsKey( CURRENCY_SYMBOL ) )
			{
				converter = getNumberConverter( converter );
				( (NumberConverter) converter ).setCurrencySymbol( attributes.get( CURRENCY_SYMBOL ) );
			}

			if ( attributes.containsKey( NUMBER_USES_GROUPING_SEPARATORS ) )
			{
				converter = getNumberConverter( converter );
				( (NumberConverter) converter ).setGroupingUsed( Boolean.parseBoolean( attributes.get( NUMBER_USES_GROUPING_SEPARATORS ) ) );
			}

			if ( attributes.containsKey( MINIMUM_INTEGER_DIGITS ) )
			{
				converter = getNumberConverter( converter );
				( (NumberConverter) converter ).setMinIntegerDigits( Integer.parseInt( attributes.get( MINIMUM_INTEGER_DIGITS ) ) );
			}

			if ( attributes.containsKey( MAXIMUM_INTEGER_DIGITS ) )
			{
				converter = getNumberConverter( converter );
				( (NumberConverter) converter ).setMaxIntegerDigits( Integer.parseInt( attributes.get( MAXIMUM_INTEGER_DIGITS ) ) );
			}

			if ( attributes.containsKey( MINIMUM_FRACTIONAL_DIGITS ) )
			{
				converter = getNumberConverter( converter );
				( (NumberConverter) converter ).setMinFractionDigits( Integer.parseInt( attributes.get( MINIMUM_FRACTIONAL_DIGITS ) ) );
			}

			if ( attributes.containsKey( MAXIMUM_FRACTIONAL_DIGITS ) )
			{
				converter = getNumberConverter( converter );
				( (NumberConverter) converter ).setMaxFractionDigits( Integer.parseInt( attributes.get( MAXIMUM_FRACTIONAL_DIGITS ) ) );
			}

			if ( attributes.containsKey( LOCALE ) )
			{
				converter = getNumberConverter( converter );
				( (NumberConverter) converter ).setLocale( new Locale( attributes.get( LOCALE ) ) );
			}

			if ( attributes.containsKey( NUMBER_PATTERN ) )
			{
				converter = getNumberConverter( converter );
				( (NumberConverter) converter ).setPattern( attributes.get( NUMBER_PATTERN ) );
			}

			if ( attributes.containsKey( NUMBER_TYPE ) )
			{
				converter = getNumberConverter( converter );
				( (NumberConverter) converter ).setType( attributes.get( NUMBER_TYPE ) );
			}

			// Set it and return it

			valueHolder.setConverter( converter );
			return converter;
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
	}

	protected void addWidget( UIComponent widget, String elementName, Map<String, String> attributes )
		throws Exception
	{
		FacesContext context = getFacesContext();
		Application application = context.getApplication();

		// Bind actions

		if ( widget instanceof ActionSource )
		{
			ActionSource actionSource = (ActionSource) widget;
			MethodBinding binding = actionSource.getAction();

			if ( binding == null )
			{
				// If there is a faces-expression, use it...

				String methodBinding = attributes.get( FACES_EXPRESSION );

				if ( methodBinding != null )
				{
					binding = application.createMethodBinding( methodBinding, null );
				}
				else
				{
					// ...if there is no binding prefix yet, we must be at
					// the top level...

					if ( mBindingPrefix == null )
					{
						ValueBinding valueBinding = getValueBinding( "value" );

						// ...or just invoke the expression

						if ( valueBinding != null )
						{
							methodBinding = FacesUtils.unwrapValueReference( valueBinding.getExpressionString() );
							binding = application.createMethodBinding( methodBinding, null );
						}

						// ...or just the raw value (for jBPM)

						else
						{
							binding = application.createMethodBinding( attributes.get( NAME ), null );
						}
					}

					// ...if there is a prefix and a name, try and construct the binding

					else
					{
						String name = attributes.get( NAME );

						if ( name != null && !"".equals( name ) )
						{
							methodBinding = FacesUtils.wrapValueReference( mBindingPrefix + name );
							binding = application.createMethodBinding( methodBinding, null );
						}
					}
				}

				if ( binding != null )
				{
					actionSource.setAction( binding );

					// Does widget need an id?
					//
					// Note: it is very dangerous to reassign an id if the widget already has one,
					// as it will create duplicates in the child component list

					if ( widget.getId() == null )
						setUniqueId( context, widget, binding.getExpressionString() );
				}
			}
		}

		// Bind properties

		else
		{
			ValueBinding binding = widget.getValueBinding( "value" );

			if ( binding == null )
			{
				// If there is a faces-expression, use it...

				String valueBinding = attributes.get( FACES_EXPRESSION );

				if ( valueBinding == null )
				{
					// ...if there is no binding prefix yet, we must be at
					// the top level...

					if ( mBindingPrefix == null )
					{
						binding = getValueBinding( "value" );

						if ( binding != null )
							valueBinding = binding.getExpressionString();
					}

					// ...if there is a prefix and a name, try and construct the binding

					else
					{
						String name = attributes.get( NAME );

						if ( name != null && !"".equals( name ) )
						{
							valueBinding = FacesUtils.wrapValueReference( mBindingPrefix + name );
						}
					}
				}

				if ( valueBinding != null )
				{
					try
					{
						// JSF 1.2 mode: some components (such as
						// org.jboss.seam.core.Validators.validate()) expect ValueExpressions and do
						// not work with ValueBindings (see JBSEAM-3252)
						//
						// Note: we wrap the ValueExpression as an Object[] to stop link-time
						// dependencies on javax.el.ValueExpression, so that we still work with
						// JSF 1.1

						Object[] valueExpression = new Object[] { application.getExpressionFactory().createValueExpression( context.getELContext(), valueBinding, Object.class ) };
						attachValueExpression( widget, valueExpression[0], attributes );
					}
					catch ( NoSuchMethodError e )
					{
						// JSF 1.1 mode

						attachValueBinding( widget, application.createValueBinding( valueBinding ), attributes );
					}

					// Does widget need an id?
					//
					// Note: it is very dangerous to reassign an id if the widget already has one,
					// as it will create duplicates in the child component list

					if ( widget.getId() == null )
						setUniqueId( context, widget, valueBinding );
				}
			}

			setConverter( widget, attributes );

			if ( mValidator != null && widget instanceof EditableValueHolder )
				mValidator.addValidators( context, (EditableValueHolder) widget, attributes );
		}

		// Add to layout

		putMetadata( widget, attributes );
		addWidget( widget );
	}

	/**
	 * Adds the given widget.
	 * <p>
	 * This method does not attach metadata, validators, etc. because it is used for adding both
	 * generated widgets and manully-created widgets.
	 */

	protected void addWidget( UIComponent widget )
		throws Exception
	{
		List<UIComponent> children = getChildren();

		// If this component already exists in the list, remove it and re-add it. This
		// enables us to sort existing, manually created components in the correct order

		children.remove( widget );

		// Note: delegating to the Renderer to do the adding, such that it can decorate
		// the UIComponent if necessary (eg. adding a UIMessage) doesn't work out too well.
		// Specifically, the Renderer should not care whether a UIComponent is manually created
		// or overridden, but if it wraps a UIComponent with a UIStub then it needs to specify
		// whether the UIStub is for a manually created component or an overridden one, so that
		// UIMetawidget will clean it up again during startBuild. This just smells wrong, because
		// Renderers should render, not manipulate the UIComponent tree.

		children.add( widget );
	}

	protected class UIMetawidgetMixin
		extends MetawidgetMixin<UIComponent, UIMetawidget>
	{
		//
		// Public methods
		//

		@Override
		public boolean isReadOnly()
		{
			// Check read-only value binding

			return UIMetawidget.this.isReadOnly();
		}

		//
		// Protected methods
		//

		@Override
		protected void startBuild()
			throws Exception
		{
			UIMetawidget.this.startBuild();
		}

		@Override
		protected void buildCompoundWidget( Element element )
			throws Exception
		{
			UIMetawidget.this.beforeBuildCompoundWidget( element );
			super.buildCompoundWidget( element );
		}

		@Override
		protected void addWidget( UIComponent widget, String elementName, Map<String, String> attributes )
			throws Exception
		{
			UIMetawidget.this.addWidget( widget, elementName, attributes );
		}

		@Override
		protected UIComponent getOverriddenWidget( String elementName, Map<String, String> attributes )
		{
			return UIMetawidget.this.getOverriddenWidget( elementName, attributes );
		}

		@Override
		protected boolean isStub( UIComponent widget )
		{
			return ( widget instanceof UIStub );
		}

		@Override
		protected Map<String, String> getStubAttributes( UIComponent stub )
		{
			return ( (UIStub) stub ).getStubAttributes();
		}

		@Override
		protected UIComponent buildWidget( String type, Map<String, String> attributes )
			throws Exception
		{
			UIComponent component = super.buildWidget( type, attributes );
			return UIMetawidget.this.afterBuildWidget( component, attributes );
		}

		@Override
		protected UIMetawidget buildNestedMetawidget( Map<String, String> attributes )
			throws Exception
		{
			UIMetawidget nestedMetawidget = UIMetawidget.this.buildNestedMetawidget( attributes );
			UIMetawidget.this.initNestedMetawidget( nestedMetawidget, attributes );

			return nestedMetawidget;
		}

		@Override
		protected void endBuild()
			throws Exception
		{
			UIMetawidget.this.endBuild();
		}

		@Override
		protected UIMetawidget getMixinOwner()
		{
			return UIMetawidget.this;
		}
	}

	//
	// Private members
	//

	private void gatherClientIds( Iterator<UIComponent> iteratorFacetsAndChildren )
	{
		for ( ; iteratorFacetsAndChildren.hasNext(); )
		{
			UIComponent component = iteratorFacetsAndChildren.next();
			String id = component.getId();

			if ( id != null )
				mClientIds.add( id );

			gatherClientIds( component.getFacetsAndChildren() );
		}
	}

	private DateTimeConverter getDateTimeConverter( Converter existingConverter )
	{
		if ( existingConverter != null && !( existingConverter instanceof DateTimeConverter ) )
			throw MetawidgetException.newException( "Unable to set date/time attributes on a " + existingConverter.getClass() );

		// In case the application defines its own one

		DateTimeConverter dateTimeConverter = (DateTimeConverter) getFacesContext().getApplication().createConverter( Date.class );

		if ( dateTimeConverter != null )
			return dateTimeConverter;

		// The JSF default

		return new DateTimeConverter();
	}

	private NumberConverter getNumberConverter( Converter existingConverter )
	{
		if ( existingConverter != null )
		{
			if ( !( existingConverter instanceof NumberConverter ) )
				throw MetawidgetException.newException( "Unable to set number attributes on a " + existingConverter.getClass() );

			return (NumberConverter) existingConverter;
		}

		// In case the application defines its own one

		NumberConverter numberConverter = (NumberConverter) getFacesContext().getApplication().createConverter( Number.class );

		if ( numberConverter != null )
			return numberConverter;

		// The JSF default

		return new NumberConverter();
	}
}