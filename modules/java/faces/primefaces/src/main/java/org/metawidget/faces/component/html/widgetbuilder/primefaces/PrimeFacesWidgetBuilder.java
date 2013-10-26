// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.faces.component.html.widgetbuilder.primefaces;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.widgetbuilder.HtmlWidgetBuilder;
import org.metawidget.util.WidgetBuilderUtils;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.slider.Slider;
import org.primefaces.component.spinner.Spinner;

/**
 * WidgetBuilder for PrimeFaces environments.
 * <p>
 * Creates native PrimeFaces UIComponents, such as <code>HtmlCalendar</code> and
 * <code>HtmlInputNumberSlider</code>, to suit the inspected fields.
 * <p>
 * As an implementation detail, <code>PrimeFacesWidgetBuilder</code> extends
 * <code>HtmlWidgetBuilder</code>, which is a little unusual for a widget builder (they normally
 * implement <code>WidgetBuilder</code> directly), but we want to reuse a lot of
 * <code>HtmlWidgetBuilder</code>'s secondary methods. Note that whilst we extend
 * <code>HtmlWidgetBuilder</code> we only create PrimeFaces components, not any regular JSF
 * components.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>, Marcel H
 */

public class PrimeFacesWidgetBuilder
	extends HtmlWidgetBuilder {

	//
	// Public methods
	//

	@Override
	public UIComponent buildWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Not for PrimeFaces?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return null;
		}

		// Action

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		if ( ACTION.equals( elementName ) ) {
			CommandButton button = (CommandButton) application.createComponent( CommandButton.COMPONENT_TYPE );
			button.setValue( metawidget.getLabelString( attributes ) );

			// (not sure how to handle this, so turn it off for now)
			button.setAjax( false );

			return button;
		}

		// Lookup the class

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, null );

		// Faces Lookups

		boolean readOnly = WidgetBuilderUtils.isReadOnly( attributes );

		if ( !readOnly ) {
			String facesLookup = attributes.get( FACES_LOOKUP );

			if ( facesLookup != null && !"".equals( facesLookup ) ) {
				UIComponent component;

				// UISelectMany...

				if ( clazz != null && ( List.class.isAssignableFrom( clazz ) || clazz.isArray() ) ) {
					component = FacesUtils.createComponent( SelectManyCheckbox.COMPONENT_TYPE, "org.primefaces.component.SelectManyCheckboxRenderer" );
				}

				// ...otherwise just a UISelectOne

				else {
					component = FacesUtils.createComponent( SelectOneMenu.COMPONENT_TYPE, "org.primefaces.component.SelectOneMenuRenderer" );
				}

				initFacesSelect( component, facesLookup, attributes, metawidget );
				return component;
			}

			// clazz may be null, if type is symbolic (eg. type="Login Screen")

			if ( clazz != null ) {

				// Not for PrimeFaces

				if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) ) {
					return null;
				}

				// String Lookups

				String lookup = attributes.get( LOOKUP );

				if ( lookup != null && !"".equals( lookup ) ) {
					UIComponent component;

					// UISelectMany...

					if ( List.class.isAssignableFrom( clazz ) || clazz.isArray() ) {
						component = FacesUtils.createComponent( SelectManyCheckbox.COMPONENT_TYPE, "org.primefaces.component.SelectManyCheckboxRenderer" );
					}

					// ...otherwise just a UISelectOne

					else {
						component = FacesUtils.createComponent( SelectOneMenu.COMPONENT_TYPE, "org.primefaces.component.SelectOneMenuRenderer" );
					}

					initStaticSelect( component, lookup, clazz, attributes, metawidget );
					return component;
				}

				// Other types

				if ( clazz.isPrimitive() ) {

					// Not for PrimeFaces

					if ( boolean.class.equals( clazz ) || char.class.equals( clazz ) ) {
						return null;
					}

					// Ranged

					UIComponent ranged = createRanged( attributes );

					if ( ranged != null ) {
						return ranged;
					}

					// Not-ranged

					Spinner spinner = FacesUtils.createComponent( Spinner.COMPONENT_TYPE, "org.primefaces.component.SpinnerRenderer" );

					// May be ranged in one dimension only

					String minimumValue = attributes.get( MINIMUM_VALUE );

					if ( minimumValue != null && !"".equals( minimumValue ) ) {
						spinner.setMin( Double.parseDouble( minimumValue ) );
					} else if ( byte.class.equals( clazz ) ) {
						spinner.setMin( Byte.MIN_VALUE );
					} else if ( short.class.equals( clazz ) ) {
						spinner.setMin( Short.MIN_VALUE );
					} else if ( int.class.equals( clazz ) ) {
						spinner.setMin( Integer.MIN_VALUE );
					} else if ( long.class.equals( clazz ) ) {
						spinner.setMin( Long.MIN_VALUE );
					} else if ( float.class.equals( clazz ) ) {
						spinner.setMin( -Float.MAX_VALUE );
					} else if ( double.class.equals( clazz ) ) {
						spinner.setMin( -Double.MAX_VALUE );
					}

					String maximumValue = attributes.get( MAXIMUM_VALUE );

					if ( maximumValue != null && !"".equals( maximumValue ) ) {
						spinner.setMax( Double.parseDouble( maximumValue ) );
					} else if ( byte.class.equals( clazz ) ) {
						spinner.setMax( Byte.MAX_VALUE );
					} else if ( short.class.equals( clazz ) ) {
						spinner.setMax( Short.MAX_VALUE );
					} else if ( int.class.equals( clazz ) ) {
						spinner.setMax( Integer.MAX_VALUE );
					} else if ( long.class.equals( clazz ) ) {
						spinner.setMax( Long.MAX_VALUE );
					} else if ( float.class.equals( clazz ) ) {
						spinner.setMax( Float.MAX_VALUE );
					} else if ( double.class.equals( clazz ) ) {
						spinner.setMax( Double.MAX_VALUE );
					}

					if ( float.class.equals( clazz ) || double.class.equals( clazz ) ) {
						spinner.setStepFactor( 0.1 );
					}

					return spinner;
				}

				// Dates

				if ( Date.class.isAssignableFrom( clazz ) ) {
					Calendar calendar = FacesUtils.createComponent( Calendar.COMPONENT_TYPE, "org.primefaces.component.CalendarRenderer" );

					if ( attributes.containsKey( DATETIME_PATTERN ) ) {
						calendar.setPattern( attributes.get( DATETIME_PATTERN ) );
					}

					if ( attributes.containsKey( LOCALE ) ) {
						calendar.setLocale( new Locale( attributes.get( LOCALE ) ) );
					}

					if ( attributes.containsKey( TIME_ZONE ) ) {
						calendar.setTimeZone( TimeZone.getTimeZone( attributes.get( TIME_ZONE ) ) );
					}

					return calendar;
				}

				// Object primitives

				if ( Number.class.isAssignableFrom( clazz ) ) {
					// Ranged

					UIComponent ranged = createRanged( attributes );

					if ( ranged != null ) {
						return ranged;
					}

					// Not-ranged
					//
					// Do not use Spinner for nullable numbers
				}

				// Autocomplete (contributed by Marcel H:
				// https://sourceforge.net/p/metawidget/discussion/747623/thread/0b903862)

				if ( String.class.equals( clazz ) ) {
					String facesSuggest = attributes.get( FACES_SUGGEST );

					if ( facesSuggest != null ) {
						AutoComplete autoComplete = FacesUtils.createComponent( AutoComplete.COMPONENT_TYPE, "org.primefaces.component.autocomplete.AutoCompleteRenderer" );
						autoComplete.setCompleteMethod( application.getExpressionFactory().createMethodExpression( context.getELContext(), facesSuggest, Object.class, new Class[] { String.class } ) );
						return autoComplete;
					}
				}
			}
		}

		// Colors. Note org.primefaces.component.ColorPickerRenderer does *not*
		// support java.awt.Color (http://forum.primefaces.org/viewtopic.php?t=21593) so
		// it isn't much good to us here

		// Not for PrimeFaces

		return null;
	}

	//
	// Private methods
	//

	private UIComponent createRanged( Map<String, String> attributes ) {

		// Ranged

		String minimumValue = attributes.get( MINIMUM_VALUE );
		String maximumValue = attributes.get( MAXIMUM_VALUE );

		if ( minimumValue != null && !"".equals( minimumValue )
				&& maximumValue != null && !"".equals( maximumValue ) ) {
			FacesContext context = FacesContext.getCurrentInstance();
			Application application = context.getApplication();

			UIStub stub = (UIStub) application
					.createComponent( UIStub.COMPONENT_TYPE );

			HtmlInputText inputText = (HtmlInputText) application
					.createComponent( HtmlInputText.COMPONENT_TYPE );
			inputText.setId( FacesUtils.createUniqueId() );
			stub.getChildren().add( inputText );

			Slider slider = FacesUtils.createComponent( Slider.COMPONENT_TYPE,
					"org.primefaces.component.SliderRenderer" );
			slider.setMinValue( Integer.parseInt( minimumValue ) );
			slider.setMaxValue( Integer.parseInt( maximumValue ) );
			slider.setFor( inputText.getId() );
			stub.getChildren().add( slider );

			return stub;
		}

		return null;
	}
}
