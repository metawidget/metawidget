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
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.NumberConverter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import org.metawidget.MetawidgetException;
import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.validator.StandardValidator;
import org.metawidget.faces.component.validator.Validator;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.mixin.w3c.MetawidgetMixin;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.StringUtils;
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
 *
 * @author Richard Kennard
 */

public abstract class UIMetawidget
	extends UIComponentBase
{
	//
	//
	// Public statics
	//
	//

	/**
	 * Component-level attribute used to store metadata.
	 */

	public final static String	COMPONENT_ATTRIBUTE_METADATA				= "metawidget-metadata";

	//
	//
	// Private statics
	//
	//

	/**
	 * Component-level attribute used to tag components as being created by Metawidget.
	 */

	private final static String	COMPONENT_ATTRIBUTE_CREATED_BY_METAWIDGET	= "metawidget-created-by";

	/**
	 * Application-level attribute used to cache Inspectors.
	 */

	private final static String	APPLICATION_ATTRIBUTE_INSPECTORS			= "metawidget-inspectors";

	//
	//
	// Private members
	//
	//

	private Object				mValue;

	private String				mInspectorConfig							= "inspector-config.xml";

	private boolean				mInspectFromParent;

	private Set<String>			mClientIds;

	private Boolean				mReadOnly;

	private String				mValidatorClass								= StandardValidator.class.getName();

	private Validator			mValidator;

	private String				mBindingPrefix;

	private UIMetawidgetMixin	mMetawidgetMixin;

	//
	//
	// Constructor
	//
	//

	public UIMetawidget()
	{
		mMetawidgetMixin = newMetawidgetMixin();

		// Default renderer

		setRendererType( "table" );
	}

	//
	//
	// Public methods
	//
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

	public void setInspectorConfig( String inspectorConfig )
	{
		mInspectorConfig = inspectorConfig;
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

	@Override
	public void encodeBegin( FacesContext context )
		throws IOException
	{
		try
		{
			// Inspect from the value binding...

			ValueBinding valueBinding = getValueBinding( "value" );

			if ( valueBinding != null )
			{
				mMetawidgetMixin.buildWidgets( inspect( getInspector(), valueBinding, mInspectFromParent ) );
			}

			// ...or from a raw value (for jBPM)...

			else if ( mValue != null )
			{
				mMetawidgetMixin.buildWidgets( getInspector().inspect( null, (String) mValue ) );
			}

			// ...or run without inspection (eg. using the Metawidget purely for layout)

			else
			{
				mMetawidgetMixin.buildWidgets( null );
			}
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

		// Delegate to our subclass, which in turn
		// will delegate to our renderer

		super.encodeBegin( context );
	}

	@Override
	public Object saveState( FacesContext context )
	{
		Object values[] = new Object[6];
		values[0] = super.saveState( context );
		values[1] = mValue;
		values[2] = mReadOnly;
		values[3] = mInspectorConfig;
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
		mInspectorConfig = (String) values[3];
		mValidatorClass = (String) values[4];
		mInspectFromParent = (Boolean) values[5];
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

	protected UIMetawidgetMixin newMetawidgetMixin()
	{
		return new UIMetawidgetMixin();
	}

	/**
	 * Inspect the value binding.
	 * <p>
	 * A value binding is optional. UIMetawidget can be used purely to lay out manually-specified
	 * components
	 */

	protected String inspect( Inspector inspector, ValueBinding valueBinding, boolean inspectFromParent )
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
				return inspector.inspect( toInspect, classToInspect.getName() );
			}
		}

		// In the event the direct object is 'null' or a primitive, traverse from the parent
		// as there may be useful metadata there (such as 'name' and 'type')

		String binding = FacesUtils.unwrapValueReference( valueBindingString );
		int lastIndexOf = binding.lastIndexOf( StringUtils.SEPARATOR_DOT_CHAR );

		Application application = context.getApplication();
		ValueBinding bindingParent = application.createValueBinding( FacesUtils.wrapValueReference( binding.substring( 0, lastIndexOf ) ) );
		Object toInspect = bindingParent.getValue( context );

		if ( toInspect != null )
		{
			Class<?> classToInspect = ClassUtils.getUnproxiedClass( toInspect.getClass() );
			return inspector.inspect( toInspect, classToInspect.getName(), binding.substring( lastIndexOf + 1 ) );
		}

		return null;
	}

	protected Inspector getInspector()
	{
		Inspector inspector;

		// If this InspectorConfig has already been read...

		@SuppressWarnings( "unchecked" )
		Map<String, Map<String, Inspector>> application = getFacesContext().getExternalContext().getApplicationMap();
		Map<String, Inspector> inspectors = application.get( APPLICATION_ATTRIBUTE_INSPECTORS );

		// ...use it...

		if ( inspectors != null )
		{
			inspector = inspectors.get( mInspectorConfig );
		}
		else
		{
			inspectors = CollectionUtils.newHashMap();
			application.put( APPLICATION_ATTRIBUTE_INSPECTORS, inspectors );
			inspector = null;
		}

		// ...otherwise, initialize the Inspector

		if ( inspector == null )
		{
			inspector = new FacesConfigReader().read( mInspectorConfig );
			inspectors.put( mInspectorConfig, inspector );
		}

		return inspector;
	}

	/**
	 * Build child widgets.
	 */

	protected void startBuild()
	{
		// Remove any components we created previously (this is
		// important for polymorphic controls, which may change from
		// refresh to refresh)

		@SuppressWarnings( "unchecked" )
		List<UIComponent> children = getChildren();

		for ( Iterator<UIComponent> i = children.iterator(); i.hasNext(); )
		{
			UIComponent componentChild = i.next();

			@SuppressWarnings( "unchecked" )
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

	protected UIComponent getOverridenWidget( String elementName, Map<String, String> attributes )
	{
		Application application = getFacesContext().getApplication();
		String binding = attributes.get( FACES_BINDING );

		// Actions

		if ( ACTION.equals( elementName ) )
		{
			MethodBinding methodBindingChild;

			if ( binding != null )
			{
				methodBindingChild = application.createMethodBinding( binding, null );
			}
			else
			{
				if ( mBindingPrefix == null )
				{
					ValueBinding valueBinding = getValueBinding( "value" );

					if ( valueBinding != null )
					{
						binding = FacesUtils.unwrapValueReference( valueBinding.getExpressionString() );
						methodBindingChild = application.createMethodBinding( binding, null );
					}

					// Not using a valueBinding? Using a raw value (eg. for jBPM)?

					else
					{
						methodBindingChild = application.createMethodBinding( attributes.get( NAME ), null );
					}
				}
				else
				{
					methodBindingChild = application.createMethodBinding( FacesUtils.wrapValueReference( mBindingPrefix + attributes.get( NAME ) ), null );
				}
			}

			return FacesUtils.findRenderedComponentWithMethodBinding( UIMetawidget.this, methodBindingChild );
		}

		// Properties

		ValueBinding valueBindingChild;

		if ( binding != null )
		{
			valueBindingChild = application.createValueBinding( binding );
		}
		else
		{
			if ( mBindingPrefix == null )
			{
				valueBindingChild = getValueBinding( "value" );

				// Metawidget has no valueBinding? Not overridable, then

				if ( valueBindingChild == null )
					return null;
			}
			else
			{
				valueBindingChild = application.createValueBinding( FacesUtils.wrapValueReference( mBindingPrefix + attributes.get( NAME ) ) );
			}
		}

		return FacesUtils.findRenderedComponentWithValueBinding( UIMetawidget.this, valueBindingChild );
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

	protected void endBuild()
		throws Exception
	{
		@SuppressWarnings( "unchecked" )
		List<UIComponent> children = getChildren();

		// Inspect any remaining components, and sort them to the bottom

		Inspector inspector = getInspector();

		for ( int loop = 0, index = 0, length = children.size(); loop < length; loop++ )
		{
			UIComponent component = children.get( index );

			@SuppressWarnings( "unchecked" )
			Map<String, Object> miscAttributes = component.getAttributes();

			if ( miscAttributes.get( COMPONENT_ATTRIBUTE_METADATA ) != null || !component.isRendered() )
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
				String xml = inspect( inspector, binding, true );

				if ( xml != null )
				{
					Document document = XmlUtils.documentFromString( xml );
					childAttributes.putAll( XmlUtils.getAttributesAsMap( document.getDocumentElement().getFirstChild() ) );
				}
			}
			else
			{
				// Manually created components with no found metadata default to no section

				if ( miscAttributes.get( COMPONENT_ATTRIBUTE_CREATED_BY_METAWIDGET ) == null )
					childAttributes.put( SECTION, "" );
			}

			// Stubs

			if ( mMetawidgetMixin.isStub( component ) )
				childAttributes.putAll( mMetawidgetMixin.getStubAttributes( component ) );

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

	protected void initMetawidget( UIMetawidget metawidget, Map<String, String> attributes )
		throws Exception
	{
		// Read-only
		//
		// Note: this isn't just the read-only setting of the parent Metawidget, it must also
		// take into account any property-level attributes

		if ( TRUE.equals( attributes.get( READ_ONLY ) ) )
		{
			metawidget.setReadOnly( true );
		}
		else
		{
			ValueBinding bindingReadOnly = getValueBinding( "readOnly" );

			if ( bindingReadOnly != null )
				metawidget.setValueBinding( "readOnly", bindingReadOnly );
			else
				metawidget.setReadOnly( mReadOnly );
		}

		// Bundle

		metawidget.setValueBinding( "bundle", getValueBinding( "bundle" ) );

		// Renderer type

		metawidget.setRendererType( getRendererType() );

		// Attributes and Parameters

		FacesUtils.copyAttributes( this, metawidget );
		FacesUtils.copyParameters( this, metawidget, "columns" );

		// Other

		metawidget.setInspectorConfig( mInspectorConfig );
		metawidget.setValidatorClass( mValidatorClass );

		// Don't use human-readable Id's for nested Metawidgets, because if they
		// only expand to a single child it will give the child component
		// a suffixed id

		metawidget.setId( getFacesContext().getViewRoot().createUniqueId() );
	}

	/**
	 * Unlike <code>UIViewRoot.createUniqueId</code>, tries to make the Id human readable, both
	 * for debugging purposes and for when running unit tests (using, say, WebTest).
	 */

	protected void setUniqueId( FacesContext context, UIComponent component, String expressionString )
	{
		// Create our ideal Id

		@SuppressWarnings( "unchecked" )
		Map<String, Object> attributes = component.getAttributes();
		attributes.put( COMPONENT_ATTRIBUTE_CREATED_BY_METAWIDGET, Boolean.TRUE );

		String idealId = StringUtils.camelCase( FacesUtils.unwrapValueReference( expressionString ), StringUtils.SEPARATOR_DOT_CHAR );
		String actualId = idealId;

		// Avoid duplicates

		if ( mClientIds == null )
		{
			mClientIds = CollectionUtils.newHashSet();

			@SuppressWarnings( "unchecked" )
			Iterator<UIComponent> iteratorFacetsAndChildren = context.getViewRoot().getFacetsAndChildren();
			gatherClientIds( iteratorFacetsAndChildren );
		}

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
			@SuppressWarnings( "unchecked" )
			List<UIComponent> children = component.getChildren();

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
			@SuppressWarnings( "unchecked" )
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
	 * Attach metadata for renderer. We do this even for manually created components.
	 */

	protected void putMetadata( UIComponent widget, Map<String, String> attributes )
	{
		@SuppressWarnings( "unchecked" )
		Map<String, Object> componentAttributes = widget.getAttributes();
		componentAttributes.put( COMPONENT_ATTRIBUTE_METADATA, attributes );
	}

	/**
	 * Attach converters for renderer.
	 *
	 * @return the attached converter
	 */

	protected Converter setConverter( UIComponent component, Map<String, String> attributes )
	{
		// Ignore if no converter

		String converterId = attributes.get( FACES_CONVERTER_ID );
		String converterClass = attributes.get( FACES_CONVERTER_CLASS );

		if ( converterId == null && converterClass == null )
			return null;

		// Recurse into stubs

		if ( component instanceof UIStub )
		{
			@SuppressWarnings( "unchecked" )
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

			if ( converterId != null )
			{
				converter = getFacesContext().getApplication().createConverter( converterId );
			}
			else
			{
				// Create from class

				converter = (Converter) ClassUtils.niceForName( converterClass ).newInstance();
			}

			// Native support for DateTimeConverter

			if ( converter instanceof DateTimeConverter )
			{
				DateTimeConverter dateTimeConverter = (DateTimeConverter) converter;

				if ( attributes.get( DATE_STYLE ) != null )
					dateTimeConverter.setDateStyle( attributes.get( DATE_STYLE ) );

				if ( attributes.get( LOCALE ) != null )
					dateTimeConverter.setLocale( new Locale( attributes.get( LOCALE ) ) );

				if ( attributes.get( DATETIME_PATTERN ) != null )
					dateTimeConverter.setPattern( attributes.get( DATETIME_PATTERN ) );

				if ( attributes.get( TIME_STYLE ) != null )
					dateTimeConverter.setTimeStyle( attributes.get( TIME_STYLE ) );

				if ( attributes.get( TIME_ZONE ) != null )
					dateTimeConverter.setTimeZone( TimeZone.getTimeZone( attributes.get( TIME_ZONE ) ) );

				if ( attributes.get( DATETIME_TYPE ) != null )
					dateTimeConverter.setType( attributes.get( DATETIME_TYPE ) );
			}

			// Native support for NumberConverter

			else if ( converter instanceof NumberConverter )
			{
				NumberConverter numberConverter = (NumberConverter) converter;

				if ( attributes.get( CURRENCY_CODE ) != null )
					numberConverter.setCurrencyCode( attributes.get( CURRENCY_CODE ) );

				if ( attributes.get( CURRENCY_SYMBOL ) != null )
					numberConverter.setCurrencySymbol( attributes.get( CURRENCY_SYMBOL ) );

				if ( attributes.get( NUMBER_USES_GROUPING_SEPARATORS ) != null )
					numberConverter.setGroupingUsed( Boolean.parseBoolean( attributes.get( NUMBER_USES_GROUPING_SEPARATORS ) ) );

				if ( "int".equals( attributes.get( TYPE ) ) || "long".equals( attributes.get( TYPE ) ) || Integer.class.getName().equals( attributes.get( TYPE ) ) || Long.class.getName().equals( attributes.get( TYPE ) ) )
					numberConverter.setIntegerOnly( true );

				if ( attributes.get( MINIMUM_INTEGER_DIGITS ) != null )
					numberConverter.setMinIntegerDigits( Integer.parseInt( attributes.get( MINIMUM_INTEGER_DIGITS ) ) );

				if ( attributes.get( MAXIMUM_INTEGER_DIGITS ) != null )
					numberConverter.setMaxIntegerDigits( Integer.parseInt( attributes.get( MAXIMUM_INTEGER_DIGITS ) ) );

				if ( attributes.get( MINIMUM_FRACTIONAL_DIGITS ) != null )
					numberConverter.setMinFractionDigits( Integer.parseInt( attributes.get( MINIMUM_FRACTIONAL_DIGITS ) ) );

				if ( attributes.get( MAXIMUM_FRACTIONAL_DIGITS ) != null )
					numberConverter.setMaxFractionDigits( Integer.parseInt( attributes.get( MAXIMUM_FRACTIONAL_DIGITS ) ) );

				if ( attributes.get( LOCALE ) != null )
					numberConverter.setLocale( new Locale( attributes.get( LOCALE ) ) );

				if ( attributes.get( NUMBER_PATTERN ) != null )
					numberConverter.setPattern( attributes.get( NUMBER_PATTERN ) );

				if ( attributes.get( NUMBER_TYPE ) != null )
					numberConverter.setType( attributes.get( NUMBER_TYPE ) );
			}

			valueHolder.setConverter( converter );
			return converter;
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
	}

	protected abstract UIComponent buildReadOnlyWidget( String elementName, Map<String, String> attributes )
		throws Exception;

	protected abstract UIComponent buildActiveWidget( String elementName, Map<String, String> attributes )
		throws Exception;

	protected void addWidget( UIComponent widget, Map<String, String> attributes )
		throws Exception
	{
		FacesContext context = getFacesContext();
		Application application = context.getApplication();

		// Does widget need an action binding?

		if ( widget instanceof ActionSource )
		{
			ActionSource actionSource = (ActionSource) widget;
			MethodBinding binding = actionSource.getAction();

			if ( binding == null )
			{
				// If there is a faces-binding, use it...

				String methodBinding = attributes.get( FACES_BINDING );

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

		// Does widget need a value binding?

		else
		{
			ValueBinding binding = widget.getValueBinding( "value" );

			if ( binding == null )
			{
				// If there is a faces-binding, use it...

				String valueBinding = attributes.get( FACES_BINDING );

				if ( valueBinding != null )
				{
					binding = application.createValueBinding( valueBinding );
				}
				else
				{
					// ...if there is no binding prefix yet, we must be at
					// the top level...

					if ( mBindingPrefix == null )
					{
						binding = getValueBinding( "value" );
					}

					// ...if there is a prefix and a name, try and construct the binding

					else
					{
						String name = attributes.get( NAME );

						if ( name != null && !"".equals( name ) )
						{
							valueBinding = FacesUtils.wrapValueReference( mBindingPrefix + name );
							binding = application.createValueBinding( valueBinding );
						}
					}
				}

				if ( binding != null )
				{
					attachValueBinding( widget, binding, attributes );

					// Does widget need an id?
					//
					// Note: it is very dangerous to reassign an id if the widget already has one,
					// as it will create duplicates in the child component list

					if ( widget.getId() == null )
						setUniqueId( context, widget, binding.getExpressionString() );
				}
			}

			setConverter( widget, attributes );

			if ( mValidator != null )
				mValidator.addValidators( context, widget, attributes );
		}

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
		@SuppressWarnings( "unchecked" )
		List<UIComponent> children = getChildren();

		// If this component already exists in the list, remove it and re-add it. This
		// enables us to sort existing, manually created components in the correct order

		children.remove( widget );

		// Note: delegating to the Renderer to do the adding, such that it can decorate
		// the UIComponent if necessary (eg. adding a UIMessage) doesn't work out too well.
		// Specifically, the Renderer should not care whether a UIComponent is manually created
		// or overriden, but if it wraps a UIComponent with a UIStub then it needs to specify
		// whether the UIStub is for a manually created component or an overriden one, so that
		// UIMetawidget will clean it up again during startBuild. This just smells wrong, because
		// Renderers should render, not manipulate the UIComponent tree.

		children.add( widget );
	}

	protected void addSelectItems( UIComponent component, List<?> values, List<String> labels, Map<String, String> attributes )
	{
		if ( values == null )
			return;

		// Add an empty choice (if a listbox, and if nullable)

		Class<?> clazz = ClassUtils.niceForName( attributes.get( TYPE ) );

		if ( component instanceof HtmlSelectOneListbox && ( clazz == null || !clazz.isPrimitive() ) )
			addSelectItem( component, "", null );

		// See if we're using labels
		//
		// (note: where possible, it is better to use a Converter than a hard-coded label)

		if ( labels != null && !labels.isEmpty() && labels.size() != values.size() )
			throw MetawidgetException.newException( "Labels list must be same size as values list" );

		// Add the select items

		for ( int loop = 0, length = values.size(); loop < length; loop++ )
		{
			Object value = values.get( loop );
			String label = null;

			if ( labels != null && !labels.isEmpty() )
				label = labels.get( loop );

			addSelectItem( component, value, label );
		}
	}

	protected void addSelectItem( UIComponent component, Object value, String label )
	{
		FacesContext context = getFacesContext();
		Application application = context.getApplication();

		UISelectItem selectItem = (UISelectItem) application.createComponent( "javax.faces.SelectItem" );
		selectItem.setId( context.getViewRoot().createUniqueId() );

		if ( value == null )
			selectItem.setItemValue( "" );
		else
			selectItem.setItemValue( value );

		if ( label != null )
		{
			// Label may be a value reference (eg. into a bundle)

			if ( FacesUtils.isValueReference( label ))
				selectItem.setValueBinding( "itemLabel", application.createValueBinding( label ));
			else
				selectItem.setItemLabel( label );
		}

		@SuppressWarnings( "unchecked" )
		List<UIComponent> children = component.getChildren();
		children.add( selectItem );
	}

	protected void addSelectItems( UIComponent component, String binding, Map<String, String> attributes )
	{
		if ( binding == null )
			return;

		FacesContext context = getFacesContext();
		Application application = context.getApplication();
		UIViewRoot viewRoot = context.getViewRoot();

		@SuppressWarnings( "unchecked" )
		List<UIComponent> children = component.getChildren();

		// Add an empty choice (if a listbox, and if nullable)

		if ( component instanceof HtmlSelectOneListbox )
		{
			String type = attributes.get( TYPE );

			if ( type != null )
			{
				Class<?> clazz = ClassUtils.niceForName( type );

				if ( clazz == null || !clazz.isPrimitive() )
					addSelectItem( component, "", null );
			}
			else
			{
				addSelectItem( component, "", null );
			}
		}

		UISelectItems selectItems = (UISelectItems) application.createComponent( "javax.faces.SelectItems" );
		selectItems.setId( viewRoot.createUniqueId() );
		children.add( selectItems );

		if ( !FacesUtils.isValueReference( binding ) )
			throw MetawidgetException.newException( "Lookup '" + binding + "' is not of the form #{...}" );

		selectItems.setValueBinding( "value", application.createValueBinding( binding ) );
	}

	protected class UIMetawidgetMixin
		extends MetawidgetMixin<UIComponent>
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
			UIMetawidget.this.startBuild();
		}

		@Override
		protected boolean isReadOnly( Map<String, String> attributes )
		{
			if ( TRUE.equals( attributes.get( READ_ONLY ) ) )
				return true;

			// Read-only value-binding

			return UIMetawidget.this.isReadOnly();
		}

		@Override
		protected void buildCompoundWidget( Element element )
			throws Exception
		{
			UIMetawidget.this.beforeBuildCompoundWidget( element );
			super.buildCompoundWidget( element );
		}

		@Override
		protected void addWidget( UIComponent widget, Map<String, String> attributes )
			throws Exception
		{
			UIMetawidget.this.addWidget( widget, attributes );
		}

		@Override
		protected UIComponent getOverridenWidget( String elementName, Map<String, String> attributes )
		{
			return UIMetawidget.this.getOverridenWidget( elementName, attributes );
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
		protected UIComponent buildReadOnlyWidget( String elementName, Map<String, String> attributes )
			throws Exception
		{
			return UIMetawidget.this.buildReadOnlyWidget( elementName, attributes );
		}

		@Override
		protected UIComponent buildActiveWidget( String elementName, Map<String, String> attributes )
			throws Exception
		{
			return UIMetawidget.this.buildActiveWidget( elementName, attributes );
		}

		@Override
		protected UIComponent initMetawidget( UIComponent widget, Map<String, String> attributes )
			throws Exception
		{
			UIMetawidget.this.initMetawidget( (UIMetawidget) widget, attributes );

			return widget;
		}

		@Override
		protected void endBuild()
			throws Exception
		{
			UIMetawidget.this.endBuild();
		}
	}

	//
	//
	// Private members
	//
	//

	@SuppressWarnings( "unchecked" )
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
}