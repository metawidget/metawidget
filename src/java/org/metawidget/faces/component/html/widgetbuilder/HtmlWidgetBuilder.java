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

import javax.faces.application.Application;
import javax.faces.component.UIColumn;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectMany;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlCommandLink;
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
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.widgetprocessor.ConverterProcessor;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * WidgetBuilder for Java Server Faces environments.
 * <p>
 * Creates native JSF HTML UIComponents, such as <code>HtmlInputText</code> and
 * <code>HtmlSelectOneListbox</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

@SuppressWarnings( "deprecation" )
public class HtmlWidgetBuilder
	implements WidgetBuilder<UIComponent, UIMetawidget>
{
	//
	// Private statics
	//

	private final static String	DATATABLE_ROW_ACTION	= "dataTableRowEditAction";

	/**
	 * The number of items in a multi-select lookup at which it should change from being a
	 * 'lineDirection' to a 'pageDirection' layout. The latter is generally the safer choice, as it
	 * stops the Metawidget blowing out horizontally.
	 */

	private final static int	SHORT_LOOKUP_SIZE		= 3;

	//
	// Private members
	//

	private final String		mDataTableStyleClass;

	private final String[]		mDataTableColumnClasses;

	private final String[]		mDataTableRowClasses;

	//
	// Constructor
	//

	public HtmlWidgetBuilder()
	{
		this( new HtmlWidgetBuilderConfig() );
	}

	public HtmlWidgetBuilder( HtmlWidgetBuilderConfig config )
	{
		mDataTableStyleClass = config.getDataTableStyleClass();
		mDataTableColumnClasses = config.getDataTableColumnClasses();
		mDataTableRowClasses = config.getDataTableRowClasses();
	}

	//
	// Public methods
	//

	/**
	 * Purely creates the widget. Does not concern itself with the widget's id, value binding or
	 * preparing metadata for the renderer.
	 *
	 * @return the widget to use in non-read-only scenarios
	 */

	public UIComponent buildWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget )
	{
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return application.createComponent( "org.metawidget.Stub" );

		// Overridden component

		UIComponent component = null;
		String componentName = attributes.get( FACES_COMPONENT );

		if ( componentName != null )
			component = application.createComponent( componentName );

		// Action

		if ( ACTION.equals( elementName ) )
		{
			if ( component == null )
				component = application.createComponent( "javax.faces.HtmlCommandButton" );

			( (UICommand) component ).setValue( metawidget.getLabelString( attributes ) );

			return component;
		}

		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, assume a String

		if ( type == null )
			type = String.class.getName();

		Class<?> clazz = ClassUtils.niceForName( type );

		// Faces Lookups

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

			initFacesSelect( component, facesLookup, attributes, metawidget );
			return component;
		}

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

				initStaticSelect( component, lookup, clazz, attributes, metawidget );
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
					return application.createComponent( "org.metawidget.Stub" );
			}

			setMaximumLength( component, attributes );

			if ( component != null )
				return component;
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return application.createComponent( "javax.faces.HtmlInputText" );

		// Nested Metawidget

		return null;
	}

	protected void initFacesSelect( UIComponent component, String facesLookup, Map<String, String> attributes, UIMetawidget metawidget )
	{
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
	}

	protected void initStaticSelect( UIComponent component, String lookup, Class<?> clazz, Map<String, String> attributes, UIMetawidget metawidget )
	{
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		// (pageDirection is a 'safer' default for anything but short lists)

		List<?> values = CollectionUtils.fromString( lookup );

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

		List<?> valuesAfterConversion = values;

		if ( component instanceof ValueHolder )
		{
			// ...using the specified converter (call setConverter prematurely so
			// we can find out what Converter to use)...

			Converter converter = null;
			ConverterProcessor processor = metawidget.getWidgetProcessor( ConverterProcessor.class );

			if ( processor != null )
				converter = processor.getConverter( (ValueHolder) component, attributes );

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
				int size = valuesAfterConversion.size();
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

				valuesAfterConversion = convertedValues;
			}
		}

		addSelectItems( component, valuesAfterConversion, CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ), attributes, metawidget );
	}

	protected void setMaximumLength( UIComponent component, Map<String, String> attributes )
	{
		String maximumLength = attributes.get( MAXIMUM_LENGTH );

		if ( maximumLength != null && !"".equals( maximumLength ) )
		{
			if ( component instanceof HtmlInputText )
				( (HtmlInputText) component ).setMaxlength( Integer.parseInt( maximumLength ) );
			else if ( component instanceof HtmlInputSecret )
				( (HtmlInputSecret) component ).setMaxlength( Integer.parseInt( maximumLength ) );
		}
	}

	//
	// Private methods
	//

	private void addSelectItems( UIComponent component, List<?> values, List<String> labels, Map<String, String> attributes, UIMetawidget metawidget )
	{
		if ( values == null )
			return;

		// Empty option

		if ( component instanceof HtmlSelectOneListbox && WidgetBuilderUtils.needsEmptyLookupItem( attributes ) )
			addSelectItem( component, null, null, metawidget );

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
			// If no label, make it the same as the value. For JSF-RI, this is needed for
			// labels next to UISelectMany checkboxes. See
			// https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=913

			selectItem.setItemLabel( StringUtils.quietValueOf( value ) );
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

				String localizedLabel = metawidget.getLocalizedKey( StringUtils.camelCase( label ) );

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

		// Empty option

		if ( component instanceof HtmlSelectOneListbox && WidgetBuilderUtils.needsEmptyLookupItem( attributes ) )
			addSelectItem( component, null, null, metawidget );

		UISelectItems selectItems = (UISelectItems) application.createComponent( "javax.faces.SelectItems" );
		selectItems.setId( viewRoot.createUniqueId() );
		component.getChildren().add( selectItems );

		if ( !FacesUtils.isExpression( binding ) )
			throw WidgetBuilderException.newException( "Lookup '" + binding + "' is not of the form #{...}" );

		selectItems.setValueBinding( "value", application.createValueBinding( binding ) );
	}

	private UIComponent createDataTableComponent( Class<?> clazz, Map<String, String> attributes, UIMetawidget metawidget )
	{
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		UIViewRoot viewRoot = context.getViewRoot();

		HtmlDataTable dataTable = (HtmlDataTable) application.createComponent( "javax.faces.HtmlDataTable" );
		dataTable.setVar( "_internal" );

		// CSS

		dataTable.setStyleClass( mDataTableStyleClass );
		dataTable.setColumnClasses( ArrayUtils.toString( mDataTableColumnClasses ) );
		dataTable.setRowClasses( ArrayUtils.toString( mDataTableRowClasses ) );

		// Inspect component type

		String componentType;

		if ( clazz.isArray() )
			componentType = clazz.getComponentType().getName();
		else
			componentType = attributes.get( PARAMETERIZED_TYPE );

		String inspectedType = null;

		if ( componentType != null )
			inspectedType = metawidget.inspect( null, componentType, (String[]) null );

		// If there is no type...

		List<UIComponent> dataChildren = dataTable.getChildren();

		if ( inspectedType == null )
		{
			// ...resort to a single column table...

			UIComponent columnText = application.createComponent( "javax.faces.HtmlOutputText" );
			columnText.setId( viewRoot.createUniqueId() );
			ValueBinding binding = application.createValueBinding( FacesUtils.wrapExpression( dataTable.getVar() ) );
			columnText.setValueBinding( "value", binding );

			UIColumn column = (UIColumn) application.createComponent( "javax.faces.Column" );
			column.setId( viewRoot.createUniqueId() );
			column.getChildren().add( columnText );
			dataChildren.add( column );

			// ...with a localized header

			HtmlOutputText headerText = (HtmlOutputText) application.createComponent( "javax.faces.HtmlOutputText" );
			headerText.setId( viewRoot.createUniqueId() );
			headerText.setValue( metawidget.getLabelString( attributes ) );
			column.setHeader( headerText );
		}

		// ...otherwise, iterate over the component type...

		else
		{
			Element root = XmlUtils.documentFromString( inspectedType ).getDocumentElement();
			NodeList elements = root.getFirstChild().getChildNodes();

			// ...and try to create columns for just the 'required' fields...

			createColumnComponents( elements, dataChildren, metawidget, true );

			// ...but, failing that, create columns for every field

			if ( dataChildren.isEmpty() )
				createColumnComponents( elements, dataChildren, metawidget, false );
		}

		// Add an 'edit action' column (if requested)

		UIParameter parameter = FacesUtils.findParameterWithName( metawidget, DATATABLE_ROW_ACTION );

		if ( parameter != null )
		{
			HtmlCommandLink rowAction = (HtmlCommandLink) application.createComponent( "javax.faces.HtmlCommandLink" );
			rowAction.setId( viewRoot.createUniqueId() );
			String localizedKey = metawidget.getLocalizedKey( "edit" );

			if ( localizedKey == null )
				rowAction.setValue( "Edit" );
			else
				rowAction.setValue( localizedKey );

			MethodBinding binding = application.createMethodBinding( FacesUtils.wrapExpression( (String) parameter.getValue() ), null );
			rowAction.setAction( binding );

			UIColumn column = (UIColumn) application.createComponent( "javax.faces.Column" );
			column.setId( viewRoot.createUniqueId() );
			column.getChildren().add( rowAction );
			dataChildren.add( column );

			// Put a blank header, so that CSS styling (such as border-bottom) still applies

			HtmlOutputText headerText = (HtmlOutputText) application.createComponent( "javax.faces.HtmlOutputText" );
			headerText.setId( viewRoot.createUniqueId() );
			headerText.setValue( "<div></div>" );
			headerText.setEscape( false );
			column.setHeader( headerText );
		}

		return dataTable;
	}

	private void createColumnComponents( NodeList elements, List<UIComponent> dataChildren, UIMetawidget metawidget, boolean onlyRequired )
	{
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		UIViewRoot viewRoot = context.getViewRoot();

		// For each property...

		for ( int loop = 0, length = elements.getLength(); loop < length; loop++ )
		{
			Node node = elements.item( loop );

			if ( !( node instanceof Element ) )
				continue;

			Element element = (Element) node;

			// ...(not action)...

			if ( ACTION.equals( element.getNodeName() ) )
				continue;

			// ...that is visible...

			if ( TRUE.equals( element.getAttribute( HIDDEN ) ) )
				continue;

			// ...and is required...
			//
			// Note: this is a controversial choice. Our logic is that a) we need to limit
			// the number of columns somehow, and b) displaying all the required fields should
			// be enough to uniquely identify the row to the user. However, users may wish
			// to override this default behaviour

			if ( onlyRequired && !TRUE.equals( element.getAttribute( REQUIRED ) ) )
				continue;

			// ...make a label...

			String columnName = element.getAttribute( NAME );
			UIComponent columnText = application.createComponent( "javax.faces.HtmlOutputText" );
			columnText.setId( viewRoot.createUniqueId() );
			ValueBinding binding = application.createValueBinding( "#{_internal." + columnName + "}" );
			columnText.setValueBinding( "value", binding );

			// ...and put it in a column...

			UIColumn column = (UIColumn) application.createComponent( "javax.faces.Column" );
			column.setId( viewRoot.createUniqueId() );
			column.getChildren().add( columnText );
			dataChildren.add( column );

			// ...with a localized header

			HtmlOutputText headerText = (HtmlOutputText) application.createComponent( "javax.faces.HtmlOutputText" );
			headerText.setId( viewRoot.createUniqueId() );
			headerText.setValue( metawidget.getLabelString( XmlUtils.getAttributesAsMap( element ) ) );
			column.setHeader( headerText );
		}
	}
}
