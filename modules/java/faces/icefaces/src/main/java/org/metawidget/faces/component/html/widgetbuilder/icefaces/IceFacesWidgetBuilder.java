// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

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
import org.metawidget.util.WidgetBuilderUtils;

import com.icesoft.faces.component.ext.HtmlCommandButton;
import com.icesoft.faces.component.ext.HtmlInputSecret;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.ext.HtmlInputTextarea;
import com.icesoft.faces.component.ext.HtmlSelectBooleanCheckbox;
import com.icesoft.faces.component.ext.HtmlSelectManyCheckbox;
import com.icesoft.faces.component.ext.HtmlSelectOneMenu;
import com.icesoft.faces.component.selectinputdate.SelectInputDate;

/**
 * WidgetBuilder for ICEfaces environments.
 * <p>
 * Creates native ICEfaces UIComponents, such as <code>SelectInputDate</code>, to suit the inspected
 * fields.
 * <p>
 * As an implementation detail, <code>IceFacesWidgetBuilder</code> extends
 * <code>HtmlWidgetBuilder</code>, which is a little unusual for a widget builder (they normally
 * implement <code>WidgetBuilder</code> directly), but in this case most of the components we create
 * are ICEfaces-extended versions of regular components, and we want to reuse a lot of
 * <code>HtmlWidgetBuilder</code>'s secondary methods. Note that whilst we extend
 * <code>HtmlWidgetBuilder</code> we only create ICEfaces components, not any regular JSF
 * components.
 * <p>
 * Note: because some ICEfaces components use
 * <code>UIMetawidget.COMPONENT_ATTRIBUTE_NOT_RECREATABLE</code> this WidgetBuilder should be used
 * in conjunction with <code>OverriddenWidgetBuilder</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class IceFacesWidgetBuilder
	extends HtmlWidgetBuilder {

	//
	// Constructor
	//

	//
	// Private members
	//

	private final boolean	mPartialSubmit;

	//
	// Constructor
	//

	public IceFacesWidgetBuilder() {

		this( new IceFacesWidgetBuilderConfig() );
	}

	public IceFacesWidgetBuilder( IceFacesWidgetBuilderConfig config ) {

		mPartialSubmit = config.isPartialSubmit();
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

	@Override
	public UIComponent buildWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Not for ICEfaces?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return null;
		}

		String componentName = attributes.get( FACES_COMPONENT );

		if ( componentName != null ) {
			return null;
		}

		// Action

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		if ( ACTION.equals( elementName ) ) {
			HtmlCommandButton button = (HtmlCommandButton) application.createComponent( HtmlCommandButton.COMPONENT_TYPE );
			button.setValue( metawidget.getLabelString( attributes ) );
			button.setPartialSubmit( mPartialSubmit );

			return button;
		}

		// Lookup the class

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, String.class );

		// Faces Lookups

		String facesLookup = attributes.get( FACES_LOOKUP );

		if ( facesLookup != null && !"".equals( facesLookup ) ) {
			UIComponent component;

			// UISelectMany...

			if ( clazz != null && ( List.class.isAssignableFrom( clazz ) || clazz.isArray() ) ) {
				component = application.createComponent( HtmlSelectManyCheckbox.COMPONENT_TYPE );
				( (HtmlSelectManyCheckbox) component ).setPartialSubmit( mPartialSubmit );
			}

			// ...otherwise just a UISelectOne

			else {
				component = application.createComponent( HtmlSelectOneMenu.COMPONENT_TYPE );
				( (HtmlSelectOneMenu) component ).setPartialSubmit( mPartialSubmit );
			}

			initFacesSelect( component, facesLookup, attributes, metawidget );
			return component;
		}

		// clazz may be null, if type is symbolic (eg. type="Login Screen")

		if ( clazz != null ) {
			// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
			// Lookup)

			if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) ) {
				HtmlSelectBooleanCheckbox htmlSelectBooleanCheckbox = (HtmlSelectBooleanCheckbox) application.createComponent( HtmlSelectBooleanCheckbox.COMPONENT_TYPE );
				htmlSelectBooleanCheckbox.setPartialSubmit( mPartialSubmit );

				return htmlSelectBooleanCheckbox;
			}

			// String Lookups

			String lookup = attributes.get( LOOKUP );

			if ( lookup != null && !"".equals( lookup ) ) {
				UIComponent component;

				// UISelectMany...

				if ( List.class.isAssignableFrom( clazz ) || clazz.isArray() ) {
					component = application.createComponent( HtmlSelectManyCheckbox.COMPONENT_TYPE );
					( (HtmlSelectManyCheckbox) component ).setPartialSubmit( mPartialSubmit );
				}

				// ...otherwise just a UISelectOne

				else {
					component = application.createComponent( HtmlSelectOneMenu.COMPONENT_TYPE );
					( (HtmlSelectOneMenu) component ).setPartialSubmit( mPartialSubmit );
				}

				initStaticSelect( component, lookup, clazz, attributes, metawidget );
				return component;
			}

			// Other types

			if ( boolean.class.equals( clazz ) ) {
				HtmlSelectBooleanCheckbox htmlSelectBooleanCheckbox = (HtmlSelectBooleanCheckbox) application.createComponent( HtmlSelectBooleanCheckbox.COMPONENT_TYPE );
				htmlSelectBooleanCheckbox.setPartialSubmit( mPartialSubmit );

				return htmlSelectBooleanCheckbox;
			}

			if ( char.class.equals( clazz ) || Character.class.isAssignableFrom( clazz ) ) {
				HtmlInputText htmlInputText = (HtmlInputText) application.createComponent( HtmlInputText.COMPONENT_TYPE );
				htmlInputText.setMaxlength( 1 );
				htmlInputText.setPartialSubmit( mPartialSubmit );

				return htmlInputText;
			}

			if ( clazz.isPrimitive() || Number.class.isAssignableFrom( clazz ) ) {
				HtmlInputText htmlInputText = (HtmlInputText) application.createComponent( HtmlInputText.COMPONENT_TYPE );
				htmlInputText.setPartialSubmit( mPartialSubmit );

				return htmlInputText;
			}

			if ( Date.class.isAssignableFrom( clazz ) ) {
				SelectInputDate selectInputDate = (SelectInputDate) application.createComponent( SelectInputDate.COMPONENT_TYPE );
				selectInputDate.setRenderAsPopup( true );
				selectInputDate.setPartialSubmit( mPartialSubmit );

				if ( attributes.containsKey( DATETIME_PATTERN ) ) {
					selectInputDate.setPopupDateFormat( attributes.get( DATETIME_PATTERN ) );
				}

				selectInputDate.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_NOT_RECREATABLE, true );
				return selectInputDate;
			}

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( MASKED ) ) ) {
					HtmlInputSecret htmlInputSecret = (HtmlInputSecret) application.createComponent( HtmlInputSecret.COMPONENT_TYPE );
					htmlInputSecret.setPartialSubmit( mPartialSubmit );
					setMaximumLength( htmlInputSecret, attributes );

					return htmlInputSecret;
				}

				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					HtmlInputTextarea htmlInputTextarea = (HtmlInputTextarea) application.createComponent( HtmlInputTextarea.COMPONENT_TYPE );
					htmlInputTextarea.setPartialSubmit( mPartialSubmit );

					// XHTML requires the 'cols' and 'rows' attributes be set, even though
					// most people override them with CSS widths and heights. The default is
					// generally 20 columns by 2 rows

					htmlInputTextarea.setCols( 20 );
					htmlInputTextarea.setRows( 2 );
					setMaximumLength( htmlInputTextarea, attributes );

					return htmlInputTextarea;
				}

				HtmlInputText htmlInputText = (HtmlInputText) application.createComponent( HtmlInputText.COMPONENT_TYPE );
				htmlInputText.setPartialSubmit( mPartialSubmit );
				setMaximumLength( htmlInputText, attributes );

				return htmlInputText;
			}
		}

		// Not for ICEfaces

		return null;
	}
}
