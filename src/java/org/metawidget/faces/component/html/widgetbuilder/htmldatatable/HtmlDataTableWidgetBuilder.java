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

package org.metawidget.faces.component.html.widgetbuilder.htmldatatable;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Richard Kennard
 */

public class HtmlDataTableWidgetBuilder
	implements WidgetBuilder<UIComponent, UIMetawidget>
{
	//
	// Private statics
	//

	private final static String	VAR_NAME	= "_internal";

	//
	// Private members
	//

	private String	mStyleClass;

	private String	mColumnClasses;

	private String	mRowClasses;

	//
	// Constructor
	//

	public HtmlDataTableWidgetBuilder()
	{
		this( new HtmlDataTableWidgetBuilderConfig() );
	}

	public HtmlDataTableWidgetBuilder( HtmlDataTableWidgetBuilderConfig config )
	{
		mStyleClass = config.getStyleClass();
		mColumnClasses = config.getColumnClasses();
		mRowClasses = config.getRowClasses();
	}

	//
	// Public methods
	//

	@Override
	public UIComponent buildWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget )
		throws Exception
	{
		// Not for us?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		String type = attributes.get( TYPE );

		if ( type == null || type.length() == 0 )
			return null;

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz == null )
			return null;

		if ( !List.class.isAssignableFrom( clazz ) && !DataModel.class.isAssignableFrom( clazz ) && !clazz.isArray() )
			return null;

		// Build the HtmlDataTable

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		HtmlDataTable dataTable = (HtmlDataTable) application.createComponent( "javax.faces.HtmlDataTable" );
		dataTable.setVar( VAR_NAME );
		dataTable.setStyleClass( mStyleClass );
		dataTable.setColumnClasses( mColumnClasses );
		dataTable.setRowClasses( mRowClasses );
		List<UIComponent> dataChildren = dataTable.getChildren();

		// Determine component type

		String componentType;

		if ( clazz.isArray() )
			componentType = clazz.getComponentType().getName();
		else
			componentType = attributes.get( PARAMETERIZED_TYPE );

		// If there is no type...

		if ( componentType == null )
		{
			// ...resort to a single column table...

			UIComponent columnText = application.createComponent( "javax.faces.HtmlOutputText" );
			ValueExpression expression = application.getExpressionFactory().createValueExpression( context.getELContext(), "#{_internal}", Object.class );
			columnText.setValueExpression( "value", expression );

			HtmlColumn column = (HtmlColumn) application.createComponent( "javax.faces.HtmlColumn" );
			dataChildren.add( column );
			List<UIComponent> columnChildren = column.getChildren();
			columnChildren.add( columnText );

			// ...with a localized header

			HtmlOutputText headerText = (HtmlOutputText) application.createComponent( "javax.faces.HtmlOutputText" );
			headerText.setValue( metawidget.getLabelString( context, attributes ));
			column.setHeader( headerText );
		}

		// ...otherwise, inspect the component type...

		else
		{
			String inspectedType = metawidget.inspect( null, componentType, (String[]) null );
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

				if ( TRUE.equals( element.getAttribute( HIDDEN )))
					continue;

				// ...make a label...

				String columnName = element.getAttribute( NAME );
				UIComponent columnText = application.createComponent( "javax.faces.HtmlOutputText" );
				ValueExpression expression = application.getExpressionFactory().createValueExpression( context.getELContext(), "#{_internal." + columnName + "}", Object.class );
				columnText.setValueExpression( "value", expression );

				// ...and put it in a column...

				HtmlColumn column = (HtmlColumn) application.createComponent( "javax.faces.HtmlColumn" );
				dataChildren.add( column );
				List<UIComponent> columnChildren = column.getChildren();
				columnChildren.add( columnText );

				// ...with a localized header

				HtmlOutputText headerText = (HtmlOutputText) application.createComponent( "javax.faces.HtmlOutputText" );
				headerText.setValue( metawidget.getLabelString( context, XmlUtils.getAttributesAsMap( element ) ));
				column.setHeader( headerText );
			}
		}

		return dataTable;
	}
}
