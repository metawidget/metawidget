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

package org.metawidget.faces.component.html.widgetbuilder.icefaces;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectMany;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;
import org.metawidget.widgetbuilder.impl.BaseWidgetBuilder;

import com.icesoft.faces.component.ext.HtmlCommandButton;
import com.icesoft.faces.component.ext.HtmlInputSecret;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.ext.HtmlInputTextarea;
import com.icesoft.faces.component.ext.HtmlSelectBooleanCheckbox;
import com.icesoft.faces.component.ext.HtmlSelectManyCheckbox;
import com.icesoft.faces.component.ext.HtmlSelectOneListbox;
import com.icesoft.faces.component.ext.HtmlSelectOneRadio;
import com.icesoft.faces.component.selectinputdate.SelectInputDate;

/**
 * WidgetBuilder for ICEfaces environments.
 * <p>
 * Automatically creates native ICEfaces UIComponents, such as <code>SelectInputDate</code>, to suit
 * the inspected fields.
 *
 * @author Richard Kennard
 */

@SuppressWarnings( "deprecation" )
public class IceFacesWidgetBuilder
	extends BaseWidgetBuilder<UIComponent, UIMetawidget>
{
	//
	// Private statics
	//

	/**
	 * The number of items in a multi-select lookup at which it should change from being a
	 * 'lineDirection' to a 'pageDirection' layout. The latter is generally the safer choice, as it
	 * stops the Metawidget blowing out horizontally.
	 */

	private final static int	SHORT_LOOKUP_SIZE	= 3;

	//
	// Protected methods
	//

	/**
	 * Purely creates the widget. Does not concern itself with the widget's id, value binding or
	 * preparing metadata for the renderer.
	 *
	 * @return the widget to use in non-read-only scenarios
	 */

	@Override
	protected UIComponent buildActiveWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget )
		throws Exception
	{
		// Not for ICEfaces?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		String componentName = attributes.get( FACES_COMPONENT );

		if ( componentName != null )
			return null;

		// Action

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		if ( ACTION.equals( elementName ) )
		{
			HtmlCommandButton button = (HtmlCommandButton) application.createComponent( "com.icesoft.faces.HtmlCommandButton" );
			button.setValue( metawidget.getLabelString( context, attributes ) );
			button.setPartialSubmit( true );

			return button;
		}

		String type = getType( attributes );

		// If no type, assume a String

		if ( type == null )
			type = String.class.getName();

		Class<?> clazz = ClassUtils.niceForName( type );

		// Faces Lookups

		String facesLookup = attributes.get( FACES_LOOKUP );

		if ( facesLookup != null && !"".equals( facesLookup ) )
		{
			UIComponent component;

			// UISelectMany...

			if ( clazz != null && ( List.class.isAssignableFrom( clazz ) || clazz.isArray() ) )
			{
				component = application.createComponent( "com.icesoft.faces.HtmlSelectManyCheckbox" );
				( (HtmlSelectManyCheckbox) component ).setPartialSubmit( true );
			}

			// ...otherwise just a UISelectOne

			else
			{
				component = application.createComponent( "com.icesoft.faces.HtmlSelectOneListbox" );
				( (HtmlSelectOneListbox) component ).setSize( 1 );
				( (HtmlSelectOneListbox) component ).setPartialSubmit( true );
			}

			// (pageDirection is a 'safer' default for anything but short lists)

			if ( component instanceof HtmlSelectManyCheckbox )
			{
				( (HtmlSelectManyCheckbox) component ).setLayout( "pageDirection" );
			}
			else if ( component instanceof HtmlSelectOneRadio )
			{
				( (HtmlSelectOneRadio) component ).setLayout( "pageDirection" );
			}

			addSelectItems( component, facesLookup, attributes, metawidget );
			return component;
		}

		// clazz may be null, if type is symbolic (eg. type="Login Screen")

		if ( clazz != null )
		{
			// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
			// Lookup)

			if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) )
			{
				HtmlSelectBooleanCheckbox htmlSelectBooleanCheckbox = (HtmlSelectBooleanCheckbox) application.createComponent( "com.icesoft.faces.HtmlSelectBooleanCheckbox" );
				htmlSelectBooleanCheckbox.setPartialSubmit( true );

				return htmlSelectBooleanCheckbox;
			}

			// String Lookups

			String lookup = attributes.get( LOOKUP );

			if ( lookup != null && !"".equals( lookup ) )
			{
				List<?> values = CollectionUtils.fromString( lookup );

				UIComponent component;

				// UISelectMany...

				if ( List.class.isAssignableFrom( clazz ) || clazz.isArray() )
				{
					component = application.createComponent( "com.icesoft.faces.HtmlSelectManyCheckbox" );
					( (HtmlSelectManyCheckbox) component ).setPartialSubmit( true );
				}

				// ...otherwise just a UISelectOne

				else
				{
					component = application.createComponent( "com.icesoft.faces.HtmlSelectOneListbox" );
					( (HtmlSelectOneListbox) component ).setSize( 1 );
					( (HtmlSelectOneListbox) component ).setPartialSubmit( true );
				}

				// (pageDirection is a 'safer' default for anything but short lists)

				if ( values.size() > SHORT_LOOKUP_SIZE )
				{
					if ( component instanceof HtmlSelectManyCheckbox )
					{
						( (HtmlSelectManyCheckbox) component ).setLayout( "pageDirection" );
					}
					else if ( component instanceof HtmlSelectOneRadio )
					{
						( (HtmlSelectOneRadio) component ).setLayout( "pageDirection" );
					}
				}

				// Convert values of SelectItems (eg. from Strings to ints)...

				if ( component instanceof ValueHolder )
				{
					// ...using the specified converter (call setConverter prematurely so
					// we can find out what Converter to use)...

					Converter converter = metawidget.setConverter( component, attributes );

					// ...(setConverter doesn't do application-wide converters)...

					if ( converter == null )
					{
						// ...(we don't try a 'clazz' converter for a UISelectMany,
						// because the 'clazz' will generally be a Collection. setConverter
						// will have already tried PARAMETERIZED_TYPE)...

						if ( !( component instanceof UISelectMany ) )
							converter = application.createConverter( clazz );
					}

					// ...if any

					if ( converter != null )
					{
						int size = values.size();
						List<Object> convertedValues = CollectionUtils.newArrayList( size );

						for ( int loop = 0; loop < size; loop++ )
						{
							// Note: the component at this point will not have a ValueBinding, as
							// that gets added in addWidget. This can scupper clever Converters that
							// try to determine the type based on the ValueBinding. For those, we
							// recommend overriding 'Application.createConverter' and passing the
							// type in the Converter's constructor instead

							Object convertedValue = converter.getAsObject( context, component, (String) values.get( loop ) );
							convertedValues.add( convertedValue );
						}

						values = convertedValues;
					}
				}

				addSelectItems( component, values, CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ), attributes, metawidget );
				return component;
			}

			// Other types

			if ( boolean.class.equals( clazz ) )
			{
				HtmlSelectBooleanCheckbox htmlSelectBooleanCheckbox = (HtmlSelectBooleanCheckbox) application.createComponent( "com.icesoft.faces.HtmlSelectBooleanCheckbox" );
				htmlSelectBooleanCheckbox.setPartialSubmit( true );

				return htmlSelectBooleanCheckbox;
			}

			if ( char.class.equals( clazz ) )
			{
				HtmlInputText htmlInputText = (HtmlInputText) application.createComponent( "com.icesoft.faces.HtmlInputText" );
				htmlInputText.setMaxlength( 1 );
				htmlInputText.setPartialSubmit( true );

				return htmlInputText;
			}

			if ( clazz.isPrimitive() || Number.class.isAssignableFrom( clazz ) )
			{
				HtmlInputText htmlInputText = (HtmlInputText) application.createComponent( "com.icesoft.faces.HtmlInputText" );
				htmlInputText.setPartialSubmit( true );

				return htmlInputText;
			}

			if ( Date.class.isAssignableFrom( clazz ) )
			{
				SelectInputDate selectInputDate = (SelectInputDate) application.createComponent( "com.icesoft.faces.SelectInputDate" );
				selectInputDate.setRenderAsPopup( true );
				selectInputDate.setPartialSubmit( true );

				if ( attributes.containsKey( DATETIME_PATTERN ) )
					selectInputDate.setPopupDateFormat( attributes.get( DATETIME_PATTERN ) );

				selectInputDate.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_NOT_RECREATABLE, true );
				return selectInputDate;
			}

			if ( String.class.equals( clazz ) )
			{
				if ( TRUE.equals( attributes.get( MASKED ) ) )
				{
					HtmlInputSecret htmlInputSecret = (HtmlInputSecret) application.createComponent( "com.icesoft.faces.HtmlInputSecret" );
					htmlInputSecret.setPartialSubmit( true );

					String maximumLength = attributes.get( MAXIMUM_LENGTH );

					if ( maximumLength != null && !"".equals( maximumLength ) )
						htmlInputSecret.setMaxlength( Integer.parseInt( maximumLength ) );

					return htmlInputSecret;
				}

				if ( TRUE.equals( attributes.get( LARGE ) ) )
				{
					HtmlInputTextarea htmlInputTextarea = (HtmlInputTextarea) application.createComponent( "com.icesoft.faces.HtmlInputTextarea" );
					htmlInputTextarea.setPartialSubmit( true );

					// XHTML requires the 'cols' and 'rows' attributes be set, even though
					// most people override them with CSS widths and heights. The default is
					// generally 20 columns by 2 rows

					htmlInputTextarea.setCols( 20 );
					htmlInputTextarea.setRows( 2 );

					String maximumLength = attributes.get( MAXIMUM_LENGTH );

					if ( maximumLength != null && !"".equals( maximumLength ) )
						htmlInputTextarea.setMaxlength( Integer.parseInt( maximumLength ) );

					return htmlInputTextarea;
				}

				HtmlInputText htmlInputText = (HtmlInputText) application.createComponent( "com.icesoft.faces.HtmlInputText" );
				htmlInputText.setPartialSubmit( true );

				String maximumLength = attributes.get( MAXIMUM_LENGTH );

				if ( maximumLength != null && !"".equals( maximumLength ) )
					htmlInputText.setMaxlength( Integer.parseInt( maximumLength ) );

				return htmlInputText;
			}
		}

		// Not for ICEfaces

		return null;
	}

	//
	// Private methods
	//

	private void addSelectItems( UIComponent component, List<?> values, List<String> labels, Map<String, String> attributes, UIMetawidget metawidget )
	{
		if ( values == null )
			return;

		// Add an empty choice (if nullable, and not required)

		if ( !TRUE.equals( attributes.get( REQUIRED ) ) )
		{
			String type = getType( attributes );

			// Type can be null if this lookup was specified by a metawidget-metadata.xml
			// and the type was omitted from the XML. In that case, assume nullable

			if ( type == null )
			{
				addSelectItem( component, null, null, metawidget );
			}
			else
			{
				Class<?> clazz = ClassUtils.niceForName( type );

				if ( component instanceof HtmlSelectOneListbox && ( clazz == null || TRUE.equals( attributes.get( LOOKUP_HAS_EMPTY_CHOICE ) ) || !clazz.isPrimitive() ) )
					addSelectItem( component, null, null, metawidget );
			}
		}

		// See if we're using labels
		//
		// (note: where possible, it is better to use a Converter than a hard-coded label)

		if ( labels != null && !labels.isEmpty() && labels.size() != values.size() )
			throw WidgetBuilderException.newException( "Labels list must be same size as values list" );

		// Add the select items

		for ( int loop = 0, length = values.size(); loop < length; loop++ )
		{
			Object value = values.get( loop );
			String label = null;

			if ( labels != null && !labels.isEmpty() )
				label = labels.get( loop );

			addSelectItem( component, value, label, metawidget );
		}
	}

	private void addSelectItem( UIComponent component, Object value, String label, UIMetawidget metawidget )
	{
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		UISelectItem selectItem = (UISelectItem) application.createComponent( "javax.faces.SelectItem" );
		selectItem.setId( context.getViewRoot().createUniqueId() );

		// JSF 1.1 doesn't allow 'null' as the item value, but JSF 1.2 requires
		// it for proper behaviour (see
		// https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=795)

		if ( value == null )
		{
			try
			{
				UISelectItem.class.getMethod( "getValueExpression", String.class );
				selectItem.setValue( new SelectItem( null, "" ) );
			}
			catch ( NoSuchMethodException e )
			{
				selectItem.setItemValue( "" );
			}
		}
		else
		{
			selectItem.setItemValue( value );
		}

		if ( label == null )
		{
			// If no label, make it the same as the value. For JSF-RI, this is needed for labels
			// next to UISelectMany checkboxes

			selectItem.setItemLabel( label );
		}
		else
		{
			// Label may be a value reference (eg. into a bundle)

			if ( FacesUtils.isExpression( label ) )
			{
				selectItem.setValueBinding( "itemLabel", application.createValueBinding( label ) );
			}
			else
			{
				// Label may be localized

				String localizedLabel = metawidget.getLocalizedKey( context, StringUtils.camelCase( label ) );

				if ( localizedLabel != null )
					selectItem.setItemLabel( localizedLabel );
				else
					selectItem.setItemLabel( label );
			}
		}

		List<UIComponent> children = component.getChildren();
		children.add( selectItem );
	}

	private void addSelectItems( UIComponent component, String binding, Map<String, String> attributes, UIMetawidget metawidget )
	{
		if ( binding == null )
			return;

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		List<UIComponent> children = component.getChildren();

		// Add an empty choice (if nullable, and not required)

		if ( component instanceof HtmlSelectOneListbox )
		{
			String type = getType( attributes );

			if ( type == null )
			{
				// Type can be null if this lookup was specified by a metawidget-metadata.xml
				// and the type was omitted from the XML. In that case, assume nullable

				addSelectItem( component, null, null, metawidget );
			}
			else
			{
				Class<?> clazz = ClassUtils.niceForName( type );

				if ( clazz == null || TRUE.equals( attributes.get( LOOKUP_HAS_EMPTY_CHOICE ) ) || ( !clazz.isPrimitive() && !TRUE.equals( attributes.get( REQUIRED ) ) ) )
					addSelectItem( component, null, null, metawidget );
			}
		}

		UISelectItems selectItems = (UISelectItems) application.createComponent( "javax.faces.SelectItems" );
		selectItems.setId( context.getViewRoot().createUniqueId() );
		children.add( selectItems );

		if ( !FacesUtils.isExpression( binding ) )
			throw WidgetBuilderException.newException( "Lookup '" + binding + "' is not of the form #{...}" );

		selectItems.setValueBinding( "value", application.createValueBinding( binding ) );
	}
}
