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

package org.metawidget.faces.component.html.widgetbuilder.primefaces;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.awt.Color;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.slider.Slider;
import org.primefaces.component.spinner.Spinner;

/**
 * WidgetBuilder for PrimeFaces environments.
 * <p>
 * Creates native PrimeFaces UIComponents, such as <code>HtmlCalendar</code> and
 * <code>HtmlInputNumberSlider</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

public class PrimeFacesWidgetBuilder
	implements WidgetBuilder<UIComponent, UIMetawidget> {

	//
	// Public methods
	//

	public UIComponent buildWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Not for PrimeFaces?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return null;
		}

		if ( attributes.containsKey( FACES_LOOKUP ) || attributes.containsKey( LOOKUP ) ) {
			return null;
		}

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		if ( type == null ) {
			return null;
		}

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz == null ) {
			return null;
		}

		// Primitives

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

			Spinner spinner = (Spinner) application.createComponent( "org.primefaces.component.Spinner" );

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
			Calendar calendar = (Calendar) application.createComponent( "org.primefaces.component.Calendar" );

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

		// Colors

		if ( Color.class.isAssignableFrom( clazz ) ) {
			if ( WidgetBuilderUtils.isReadOnly( attributes ) ) {
				return FacesContext.getCurrentInstance().getApplication().createComponent( "javax.faces.HtmlOutputText" );
			}

			return application.createComponent( "org.primefaces.component.ColorPicker" );
		}

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

		if ( minimumValue != null && !"".equals( minimumValue ) && maximumValue != null && !"".equals( maximumValue ) ) {
			FacesContext context = FacesContext.getCurrentInstance();
			Application application = context.getApplication();

			UIStub stub = (UIStub) application.createComponent( "org.metawidget.Stub" );

			HtmlInputText inputText = (HtmlInputText) application.createComponent( "javax.faces.HtmlInputText" );
			inputText.setId( FacesContext.getCurrentInstance().getViewRoot().createUniqueId() );
			stub.getChildren().add( inputText );

			Slider slider = (Slider) application.createComponent( "org.primefaces.component.Slider" );
			slider.setMinValue( Integer.parseInt( minimumValue ) );
			slider.setMaxValue( Integer.parseInt( maximumValue ) );
			slider.setFor( inputText.getId() );
			stub.getChildren().add( slider );

			return stub;
		}

		return null;
	}
}
