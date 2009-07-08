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
import javax.faces.context.FacesContext;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.html.widgetbuilder.HtmlWidgetBuilder;
import org.metawidget.util.ClassUtils;

import com.icesoft.faces.component.ext.HtmlCommandButton;
import com.icesoft.faces.component.ext.HtmlInputSecret;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.ext.HtmlInputTextarea;
import com.icesoft.faces.component.ext.HtmlSelectBooleanCheckbox;
import com.icesoft.faces.component.ext.HtmlSelectManyCheckbox;
import com.icesoft.faces.component.ext.HtmlSelectOneListbox;
import com.icesoft.faces.component.selectinputdate.SelectInputDate;

/**
 * WidgetBuilder for ICEfaces environments.
 * <p>
 * Automatically creates native ICEfaces UIComponents, such as <code>SelectInputDate</code>, to suit
 * the inspected fields.
 * <p>
 * <code>IceFacesWidgetBuilder</code> extends <code>HtmlWidgetBuilder</code>, which is a little
 * unusual for a widget builder (they normally extend <code>BaseWidgetBuilder</code>), but in this
 * case most of the components we create are ICEfaces-extended versions of regular components, and
 * we want to reuse a lot of <code>HtmlWidgetBuilder</code>'s secondary methods. Note that whilst we
 * extend <code>HtmlWidgetBuilder</code> we only create ICEfaces components, not any regular
 * components.
 *
 * @author Richard Kennard
 */

public class IceFacesWidgetBuilder
	extends HtmlWidgetBuilder
{
	//
	// Protected methods
	//

	/**
	 * Overridden to always return null, thereby deferring to another widget builder in the chain.
	 */

	@Override
	protected UIComponent buildReadOnlyWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget )
		throws Exception
	{
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

			initFacesSelect( component, facesLookup, attributes, metawidget );
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

				initStaticSelect( component, lookup, clazz, attributes, metawidget );
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
					setMaximumLength( htmlInputSecret, attributes );

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
					setMaximumLength( htmlInputTextarea, attributes );

					return htmlInputTextarea;
				}

				HtmlInputText htmlInputText = (HtmlInputText) application.createComponent( "com.icesoft.faces.HtmlInputText" );
				htmlInputText.setPartialSubmit( true );
				setMaximumLength( htmlInputText, attributes );

				return htmlInputText;
			}
		}

		// Not for ICEfaces

		return null;
	}
}
