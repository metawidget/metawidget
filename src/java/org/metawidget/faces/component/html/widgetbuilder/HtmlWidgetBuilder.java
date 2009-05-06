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

package org.metawidget.faces.component.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectMany;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

import org.metawidget.MetawidgetException;
import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.impl.BaseWidgetBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * WidgetBuilder for Java Server Faces environments.
 * <p>
 * Automatically creates native JSF HTML UIComponents, such as <code>HtmlInputText</code> and
 * <code>HtmlSelectOneListbox</code>, to suit the inspected fields.
 * <p>
 * This implementation recognizes the following <code>&lt;f:param&gt;</code> parameters:
 * <p>
 * <ul>
 * <li><code>dataTableStyleClass</code>
 * <li><code>dataTableColumnClasses</code>
 * <li><code>dataTableRowClasses</code>
 * </ul>
 *
 * @author Richard Kennard
 */

@SuppressWarnings( "deprecation" )
public class HtmlWidgetBuilder
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

	/**
	 * The 'var' name to use for generated <code>dataTable</code>s.
	 */

	private final static String	DATA_TABLE_VAR_NAME	= "_internal";

	//
	// Protected methods
	//

	/**
	 * Purely creates the widget. Does not concern itself with the widget's id, value binding or
	 * preparing metadata for the renderer.
	 *
	 * @return the widget to use in read-only scenarios
	 */

	@Override
	protected UIComponent buildReadOnlyWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return createHiddenComponent( attributes, metawidget );

		// Masked (return a couple of nested Stubs, so that we DO still render a label)

		Application application = FacesContext.getCurrentInstance().getApplication();

		if ( TRUE.equals( attributes.get( MASKED ) ) )
		{
			UIComponent component = application.createComponent( "org.metawidget.Stub" );
			List<UIComponent> listChildren = component.getChildren();
			listChildren.add( application.createComponent( "org.metawidget.Stub" ) );

			return component;
		}

		// Action

		if ( ACTION.equals( elementName ) )
			return application.createComponent( "org.metawidget.Stub" );

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
		{
			String lookupLabels = attributes.get( LOOKUP_LABELS );

			if ( lookupLabels == null )
				return createReadOnlyComponent( attributes, metawidget );

			// Special support for read-only lookups with labels

			List<String> labels = CollectionUtils.fromString( lookupLabels );

			if ( labels.isEmpty() )
				return createReadOnlyComponent( attributes, metawidget );

			HtmlLookupOutputText lookupOutputText = (HtmlLookupOutputText) application.createComponent( "org.metawidget.HtmlLookupOutputText" );
			lookupOutputText.setLabels( CollectionUtils.fromString( lookup ), labels );

			return createReadOnlyComponent( attributes, lookupOutputText, metawidget );
		}

		String facesLookup = attributes.get( FACES_LOOKUP );

		if ( facesLookup != null && !"".equals( facesLookup ) )
			return createReadOnlyComponent( attributes, metawidget );

		String type = attributes.get( TYPE );

		// If no type, fail gracefully with a javax.faces.HtmlOutputText

		if ( type == null || "".equals( type ) )
			return createReadOnlyComponent( attributes, metawidget );

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz != null )
		{
			// Primitives

			if ( clazz.isPrimitive() )
				return createReadOnlyComponent( attributes, metawidget );

			// Object primitives

			if ( ClassUtils.isPrimitiveWrapper( clazz ) )
				return createReadOnlyComponent( attributes, metawidget );

			// Dates

			if ( Date.class.isAssignableFrom( clazz ) )
				return createReadOnlyComponent( attributes, metawidget );

			// Strings

			if ( String.class.equals( clazz ) )
				return createReadOnlyComponent( attributes, metawidget );

			// Supported Collections

			if ( List.class.isAssignableFrom( clazz ) || DataModel.class.isAssignableFrom( clazz ) || clazz.isArray() )
				return createDataTableComponent( clazz, attributes, metawidget );

			// Other Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return createHiddenComponent( attributes, metawidget );
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return createReadOnlyComponent( attributes, metawidget );

		// Nested Metawidget

		return null;
	}

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
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return createHiddenComponent( attributes, metawidget );

		// Overridden component

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		UIComponent component = null;
		String componentName = attributes.get( FACES_COMPONENT );

		if ( componentName != null )
			component = application.createComponent( componentName );

		// Action

		if ( ACTION.equals( elementName ) )
		{
			if ( component == null )
				component = application.createComponent( "javax.faces.HtmlCommandButton" );

			( (UICommand) component ).setValue( metawidget.getLabelString( context, attributes ) );

			return component;
		}

		// Faces Lookups

		String type = attributes.get( TYPE );
		Class<?> clazz = null;

		if ( type != null )
			clazz = ClassUtils.niceForName( type );

		String facesLookup = attributes.get( FACES_LOOKUP );

		if ( facesLookup != null && !"".equals( facesLookup ) )
		{
			if ( component == null )
			{
				// UISelectMany...

				if ( clazz != null && ( List.class.isAssignableFrom( clazz ) || clazz.isArray() ) )
				{
					component = application.createComponent( "javax.faces.HtmlSelectManyCheckbox" );
				}

				// ...otherwise just a UISelectOne

				else
				{
					component = application.createComponent( "javax.faces.HtmlSelectOneListbox" );
					( (HtmlSelectOneListbox) component ).setSize( 1 );
				}
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

		// If no type, fail gracefully with a javax.faces.HtmlInputText
		//
		// Note: we don't do this if there is a FACES_LOOKUP, because a FACES_LOOKUP
		// can get away with not specifying a type
		//
		// Note: having no type is very different from having a type for which
		// clazz == null (eg. type="Login Screen"), because the latter should
		// end up becoming a nested Metawidget

		if ( type == null || "".equals( type ) )
			return application.createComponent( "javax.faces.HtmlInputText" );

		// clazz may be null, if type is symbolic (eg. type="Login Screen")

		if ( clazz != null )
		{
			// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
			// Lookup)

			if ( component == null && Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) )
				return application.createComponent( "javax.faces.HtmlSelectBooleanCheckbox" );

			// String Lookups

			String lookup = attributes.get( LOOKUP );

			if ( lookup != null && !"".equals( lookup ) )
			{
				List<?> values = CollectionUtils.fromString( lookup );

				if ( component == null )
				{
					// UISelectMany...

					if ( List.class.isAssignableFrom( clazz ) || clazz.isArray() )
					{
						component = application.createComponent( "javax.faces.HtmlSelectManyCheckbox" );
					}

					// ...otherwise just a UISelectOne

					else
					{
						component = application.createComponent( "javax.faces.HtmlSelectOneListbox" );
						( (HtmlSelectOneListbox) component ).setSize( 1 );
					}
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
			}

			// If no component specified yet, pick one

			if ( component == null )
			{
				if ( boolean.class.equals( clazz ) )
				{
					component = application.createComponent( "javax.faces.HtmlSelectBooleanCheckbox" );
				}
				else if ( char.class.equals( clazz ) )
				{
					component = application.createComponent( "javax.faces.HtmlInputText" );
					( (HtmlInputText) component ).setMaxlength( 1 );
				}
				else if ( clazz.isPrimitive() )
				{
					component = application.createComponent( "javax.faces.HtmlInputText" );
				}
				else if ( Date.class.isAssignableFrom( clazz ) )
				{
					component = application.createComponent( "javax.faces.HtmlInputText" );
				}
				else if ( Number.class.isAssignableFrom( clazz ) )
				{
					component = application.createComponent( "javax.faces.HtmlInputText" );
				}
				else if ( String.class.equals( clazz ) )
				{
					if ( TRUE.equals( attributes.get( MASKED ) ) )
					{
						component = application.createComponent( "javax.faces.HtmlInputSecret" );
					}
					else if ( TRUE.equals( attributes.get( LARGE ) ) )
					{
						component = application.createComponent( "javax.faces.HtmlInputTextarea" );

						// XHTML requires the 'cols' and 'rows' attributes be set, even though
						// most people override them with CSS widths and heights. The default is
						// generally 20 columns by 2 rows

						( (HtmlInputTextarea) component ).setCols( 20 );
						( (HtmlInputTextarea) component ).setRows( 2 );
					}
					else
					{
						component = application.createComponent( "javax.faces.HtmlInputText" );
					}
				}

				// Supported Collections

				else if ( List.class.isAssignableFrom( clazz ) || DataModel.class.isAssignableFrom( clazz ) || clazz.isArray() )
					return createDataTableComponent( clazz, attributes, metawidget );

				// Other Collections

				else if ( Collection.class.isAssignableFrom( clazz ) )
					return createHiddenComponent( attributes, metawidget );
			}

			// Limit maximum length

			String maximumLength = attributes.get( MAXIMUM_LENGTH );

			if ( maximumLength != null && !"".equals( maximumLength ) )
			{
				if ( component instanceof HtmlInputText )
					( (HtmlInputText) component ).setMaxlength( Integer.parseInt( maximumLength ) );
				else if ( component instanceof HtmlInputSecret )
					( (HtmlInputSecret) component ).setMaxlength( Integer.parseInt( maximumLength ) );
			}

			if ( component != null )
				return component;
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return application.createComponent( "javax.faces.HtmlInputText" );

		// Nested Metawidget

		return null;
	}

	//
	// Private methods
	//

	private UIComponent createHiddenComponent( Map<String, String> attributes, UIMetawidget metawidget )
	{
		Application application = FacesContext.getCurrentInstance().getApplication();

		if ( !( (HtmlMetawidget) metawidget ).isCreateHiddenFields() || TRUE.equals( attributes.get( NO_SETTER ) ) )
			return application.createComponent( "org.metawidget.Stub" );

		// If using hidden fields, create a hidden field to POST back

		return application.createComponent( "javax.faces.HtmlInputHidden" );
	}

	private UIComponent createReadOnlyComponent( Map<String, String> attributes, UIMetawidget metawidget )
	{
		Application application = FacesContext.getCurrentInstance().getApplication();

		// Note: it is important to use 'javax.faces.HtmlOutputText', not just 'javax.faces.Output',
		// because the latter is not HTML escaped (according to the JSF 1.2 spec)

		UIComponent readOnlyComponent = application.createComponent( "javax.faces.HtmlOutputText" );
		return createReadOnlyComponent( attributes, readOnlyComponent, metawidget );
	}

	private UIComponent createReadOnlyComponent( Map<String, String> attributes, UIComponent readOnlyComponent, UIMetawidget metawidget )
	{
		Application application = FacesContext.getCurrentInstance().getApplication();

		if ( !( (HtmlMetawidget) metawidget ).isCreateHiddenFields() || TRUE.equals( attributes.get( NO_SETTER ) ) )
			return readOnlyComponent;

		// If using hidden fields, create both a label and a hidden field

		UIComponent componentStub = application.createComponent( "org.metawidget.Stub" );

		List<UIComponent> children = componentStub.getChildren();

		children.add( application.createComponent( "javax.faces.HtmlInputHidden" ) );
		children.add( readOnlyComponent );

		return componentStub;
	}

	private UIComponent createDataTableComponent( Class<?> clazz, Map<String, String> attributes, UIMetawidget metawidget )
	{
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		UIViewRoot viewRoot = context.getViewRoot();

		HtmlDataTable dataTable = (HtmlDataTable) application.createComponent( "javax.faces.HtmlDataTable" );
		dataTable.setVar( DATA_TABLE_VAR_NAME );

		// CSS

		UIParameter parameter = FacesUtils.findParameterWithName( metawidget, "dataTableStyleClass" );

		if ( parameter != null )
			dataTable.setStyleClass( (String) parameter.getValue() );

		parameter = FacesUtils.findParameterWithName( metawidget, "dataTableColumnClasses" );

		if ( parameter != null )
			dataTable.setColumnClasses( (String) parameter.getValue() );

		parameter = FacesUtils.findParameterWithName( metawidget, "dataTableRowClasses" );

		if ( parameter != null )
			dataTable.setRowClasses( (String) parameter.getValue() );

		// Inspect component type

		String componentType;

		if ( clazz.isArray() )
			componentType = clazz.getComponentType().getName();
		else
			componentType = attributes.get( PARAMETERIZED_TYPE );

		String inspectedType = metawidget.inspect( null, componentType, (String[]) null );

		// If there is no type...

		List<UIComponent> dataChildren = dataTable.getChildren();

		if ( inspectedType == null )
		{
			// ...resort to a single column table...

			UIComponent columnText = application.createComponent( "javax.faces.HtmlOutputText" );
			ValueExpression expression = application.getExpressionFactory().createValueExpression( context.getELContext(), "#{_internal}", Object.class );
			columnText.setId( viewRoot.createUniqueId() );
			columnText.setValueExpression( "value", expression );

			HtmlColumn column = (HtmlColumn) application.createComponent( "javax.faces.HtmlColumn" );
			column.setId( viewRoot.createUniqueId() );
			column.getChildren().add( columnText );
			dataChildren.add( column );

			// ...with a localized header

			HtmlOutputText headerText = (HtmlOutputText) application.createComponent( "javax.faces.HtmlOutputText" );
			headerText.setId( viewRoot.createUniqueId() );
			headerText.setValue( metawidget.getLabelString( context, attributes ) );
			column.setHeader( headerText );
		}

		// ...otherwise, iterate over the component type...

		else
		{
			Document document = XmlUtils.documentFromString( inspectedType );
			NodeList elements = document.getDocumentElement().getFirstChild().getChildNodes();

			// ...and for each property...

			for ( int loop = 0, length = elements.getLength(); loop < length; loop++ )
			{
				Node node = elements.item( loop );

				if ( !( node instanceof Element ) )
					continue;

				Element element = (Element) node;

				// ...that is visible...

				if ( TRUE.equals( element.getAttribute( HIDDEN ) ) )
					continue;

				// ...make a label...

				String columnName = element.getAttribute( NAME );
				UIComponent columnText = application.createComponent( "javax.faces.HtmlOutputText" );
				ValueExpression expression = application.getExpressionFactory().createValueExpression( context.getELContext(), "#{_internal." + columnName + "}", Object.class );
				columnText.setId( viewRoot.createUniqueId() );
				columnText.setValueExpression( "value", expression );

				// ...and put it in a column...

				HtmlColumn column = (HtmlColumn) application.createComponent( "javax.faces.HtmlColumn" );
				column.setId( viewRoot.createUniqueId() );
				column.getChildren().add( columnText );
				dataChildren.add( column );

				// ...with a localized header

				HtmlOutputText headerText = (HtmlOutputText) application.createComponent( "javax.faces.HtmlOutputText" );
				headerText.setId( viewRoot.createUniqueId() );
				headerText.setValue( metawidget.getLabelString( context, XmlUtils.getAttributesAsMap( element ) ) );
				column.setHeader( headerText );
			}
		}

		return createReadOnlyComponent( attributes, dataTable, metawidget );
	}

	private void addSelectItems( UIComponent component, List<?> values, List<String> labels, Map<String, String> attributes, UIMetawidget metawidget )
	{
		if ( values == null )
			return;

		// Add an empty choice (if nullable, and not required)

		String type = attributes.get( TYPE );

		if ( type == null )
		{
			// Type may be null if this lookup was specified by a metawidget-metadata.xml
			// and the type was omitted from the XML. In that case, assume nullable

			addSelectItem( component, null, null, metawidget );
		}
		else
		{
			Class<?> clazz = ClassUtils.niceForName( type );

			if ( component instanceof HtmlSelectOneListbox && ( clazz == null || TRUE.equals( attributes.get( LOOKUP_HAS_EMPTY_CHOICE ) ) || ( !clazz.isPrimitive() && !TRUE.equals( attributes.get( REQUIRED ) ) ) ) )
				addSelectItem( component, null, null, metawidget );
		}

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

			if ( FacesUtils.isValueReference( label ) )
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
		UIViewRoot viewRoot = context.getViewRoot();

		List<UIComponent> children = component.getChildren();

		// Add an empty choice (if nullable, and not required)

		if ( component instanceof HtmlSelectOneListbox )
		{
			String type = attributes.get( TYPE );

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
		selectItems.setId( viewRoot.createUniqueId() );
		children.add( selectItems );

		if ( !FacesUtils.isValueReference( binding ) )
			throw MetawidgetException.newException( "Lookup '" + binding + "' is not of the form #{...}" );

		selectItems.setValueBinding( "value", application.createValueBinding( binding ) );
	}
}
