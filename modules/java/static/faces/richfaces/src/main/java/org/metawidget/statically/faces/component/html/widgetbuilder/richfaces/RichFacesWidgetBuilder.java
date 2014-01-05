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

package org.metawidget.statically.faces.component.html.widgetbuilder.richfaces;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Color;
import java.util.Date;
import java.util.Map;

import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlOutputText;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * WidgetBuilder for RichFaces environments.
 * <p>
 * Creates native RichFaces UIComponents, such as <code>HtmlCalendar</code> and
 * <code>HtmlInputNumberSlider</code>, to suit the inspected fields.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class RichFacesWidgetBuilder
	implements WidgetBuilder<StaticXmlWidget, StaticXmlMetawidget> {

	//
	// Public methods
	//

	public StaticXmlWidget buildWidget( String elementName, Map<String, String> attributes, StaticXmlMetawidget metawidget ) {

		// Not for RichFaces?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return null;
		}

		// Note: we tried implementing lookups using org.richfaces.ComboBox, but that
		// allows manual input and if you set enableManualInput=false it behaves a
		// bit screwy for our liking (ie. if you hit backspace the browser goes back)

		if ( /* attributes.containsKey( FACES_LOOKUP ) || */attributes.containsKey( LOOKUP ) ) {
			return null;
		}

		// Lookup the class

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, null );

		if ( clazz == null ) {
			return null;
		}

		String minimumValue = attributes.get( MINIMUM_VALUE );
		String maximumValue = attributes.get( MAXIMUM_VALUE );

		// Primitives

		if ( clazz.isPrimitive() ) {
			// Not for RichFaces

			if ( boolean.class.equals( clazz ) || char.class.equals( clazz ) ) {
				return null;
			}

			// Ranged

			if ( minimumValue != null && !"".equals( minimumValue ) && maximumValue != null && !"".equals( maximumValue ) ) {

				HtmlInputNumberSlider slider = new HtmlInputNumberSlider();
				slider.putAttribute( "minValue", minimumValue );
				slider.putAttribute( "maxValue", maximumValue );

				return slider;
			}

			// Not-ranged

			HtmlInputNumberSpinner spinner = new HtmlInputNumberSpinner();

			// May be ranged in one dimension only

			if ( minimumValue != null && !"".equals( minimumValue ) ) {
				spinner.putAttribute( "minValue", minimumValue );
			}

			if ( maximumValue != null && !"".equals( maximumValue ) ) {
				spinner.putAttribute( "maxValue", maximumValue );
			} else {
				// RichFaces sets maxValue="100" by default
				spinner.putAttribute( "maxValue", "999999999" );
			}

			// Wraps around?

			spinner.putAttribute( "cycled", "false" );

			// Stepped

			if ( float.class.equals( clazz ) || double.class.equals( clazz ) ) {
				spinner.putAttribute( "step", "0.1" );
			}

			return spinner;
		}

		// Dates
		//
		// Note: when http://jira.jboss.org/jira/browse/RF-2023 gets implemented, that
		// would allow external, app-level configuration of this Calendar

		if ( Date.class.isAssignableFrom( clazz ) ) {
			HtmlCalendar calendar = new HtmlCalendar();

			if ( attributes.containsKey( DATETIME_PATTERN ) ) {
				calendar.putAttribute( "datePattern", attributes.get( DATETIME_PATTERN ) );
			}

			if ( attributes.containsKey( LOCALE ) ) {
				calendar.putAttribute( "locale", attributes.get( DATETIME_PATTERN ) );
			}

			if ( attributes.containsKey( TIME_ZONE ) ) {
				calendar.putAttribute( "timeZone", attributes.get( DATETIME_PATTERN ) );
			}

			return calendar;
		}

		// Object primitives

		if ( Number.class.isAssignableFrom( clazz ) ) {
			// Ranged

			if ( minimumValue != null && !"".equals( minimumValue ) && maximumValue != null && !"".equals( maximumValue ) ) {

				HtmlInputNumberSlider slider = new HtmlInputNumberSlider();
				slider.putAttribute( "minValue", minimumValue );
				slider.putAttribute( "maxValue", maximumValue );

				return slider;
			}

			// Not-ranged
			//
			// Until https://jira.jboss.org/jira/browse/RF-4450 is fixed, do not use
			// UIInputNumberSpinner for nullable numbers
		}

		// Colors (as of RichFaces 3.3.1)

		if ( Color.class.equals( clazz ) ) {
			if ( WidgetBuilderUtils.isReadOnly( attributes ) ) {
				return new HtmlOutputText();
			}

			return new HtmlColorPicker();
		}

		// Not for RichFaces?

		return null;
	}
}
