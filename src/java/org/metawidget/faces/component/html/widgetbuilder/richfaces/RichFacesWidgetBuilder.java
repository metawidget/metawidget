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

package org.metawidget.faces.component.html.widgetbuilder.richfaces;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.awt.Color;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.application.Application;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.richfaces.component.UICalendar;
import org.richfaces.component.UIInputNumberSlider;
import org.richfaces.component.UIInputNumberSpinner;
import org.richfaces.component.UISuggestionBox;
import org.richfaces.component.html.HtmlInputNumberSpinner;

/**
 * WidgetBuilder for RichFaces environments.
 * <p>
 * Creates native RichFaces UIComponents, such as <code>HtmlCalendar</code> and
 * <code>HtmlInputNumberSlider</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

@SuppressWarnings( "deprecation" )
public class RichFacesWidgetBuilder
	implements WidgetBuilder<UIComponent, UIMetawidget>
{
	//
	// Public methods
	//

	public UIComponent buildWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget )
	{
		// Not for RichFaces?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		// Note: we tried implementing lookups using org.richfaces.ComboBox, but that
		// allows manual input and if you set enableManualInput=false it behaves a
		// bit screwy for our liking (ie. if you hit backspace the browser goes back)

		if ( attributes.containsKey( FACES_LOOKUP ) || attributes.containsKey( LOOKUP ) )
			return null;

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		if ( type == null )
			return null;

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz == null )
			return null;

		// Primitives

		if ( clazz.isPrimitive() )
		{
			// Not for RichFaces

			if ( boolean.class.equals( clazz ) || char.class.equals( clazz ) )
				return null;

			// Ranged

			UIComponent ranged = createRanged( attributes );

			if ( ranged != null )
				return ranged;

			// Not-ranged

			UIInputNumberSpinner spinner = (UIInputNumberSpinner) application.createComponent( "org.richfaces.inputNumberSpinner" );

			// May be ranged in one dimension only

			String minimumValue = attributes.get( MINIMUM_VALUE );

			if ( minimumValue != null && !"".equals( minimumValue ) )
				spinner.setMinValue( minimumValue );
			else if ( byte.class.equals( clazz ) )
				spinner.setMinValue( String.valueOf( Byte.MIN_VALUE ) );
			else if ( short.class.equals( clazz ) )
				spinner.setMinValue( String.valueOf( Short.MIN_VALUE ) );
			else if ( int.class.equals( clazz ) )
				spinner.setMinValue( String.valueOf( Integer.MIN_VALUE ) );
			else if ( long.class.equals( clazz ) )
				spinner.setMinValue( String.valueOf( Long.MIN_VALUE ) );
			else if ( float.class.equals( clazz ) )
				spinner.setMinValue( String.valueOf( -Float.MAX_VALUE ) );
			else if ( double.class.equals( clazz ) )
				spinner.setMinValue( String.valueOf( -Double.MAX_VALUE ) );

			String maximumValue = attributes.get( MAXIMUM_VALUE );

			if ( maximumValue != null && !"".equals( maximumValue ) )
				spinner.setMaxValue( maximumValue );
			else if ( byte.class.equals( clazz ) )
				spinner.setMaxValue( String.valueOf( Byte.MAX_VALUE ) );
			else if ( short.class.equals( clazz ) )
				spinner.setMaxValue( String.valueOf( Short.MAX_VALUE ) );
			else if ( int.class.equals( clazz ) )
				spinner.setMaxValue( String.valueOf( Integer.MAX_VALUE ) );
			else if ( long.class.equals( clazz ) )
				spinner.setMaxValue( String.valueOf( Long.MAX_VALUE ) );
			else if ( float.class.equals( clazz ) )
				spinner.setMaxValue( String.valueOf( Float.MAX_VALUE ) );
			else if ( double.class.equals( clazz ) )
				spinner.setMaxValue( String.valueOf( Double.MAX_VALUE ) );

			// HtmlInputNumberSpinner-specific properties

			if ( spinner instanceof HtmlInputNumberSpinner )
			{
				HtmlInputNumberSpinner htmlSpinner = (HtmlInputNumberSpinner) spinner;

				// Wraps around?

				htmlSpinner.setCycled( false );

				// Stepped

				if ( float.class.equals( clazz ) || double.class.equals( clazz ) )
					htmlSpinner.setStep( "0.1" );
			}

			return spinner;
		}

		// Dates
		//
		// Note: when http://jira.jboss.org/jira/browse/RF-2023 gets implemented, that
		// would allow external, app-level configuration of this Calendar

		if ( Date.class.isAssignableFrom( clazz ) )
		{
			UICalendar calendar = (UICalendar) application.createComponent( "org.richfaces.Calendar" );

			if ( attributes.containsKey( DATETIME_PATTERN ) )
				calendar.setDatePattern( attributes.get( DATETIME_PATTERN ) );

			if ( attributes.containsKey( LOCALE ) )
				calendar.setLocale( new Locale( attributes.get( LOCALE ) ) );

			if ( attributes.containsKey( TIME_ZONE ) )
				calendar.setTimeZone( TimeZone.getTimeZone( attributes.get( TIME_ZONE ) ) );

			return calendar;
		}

		// Object primitives

		if ( Number.class.isAssignableFrom( clazz ) )
		{
			// Ranged

			UIComponent ranged = createRanged( attributes );

			if ( ranged != null )
				return ranged;

			// Not-ranged
			//
			// Until https://jira.jboss.org/jira/browse/RF-4450 is fixed, do not use
			// UIInputNumberSpinner for nullable numbers
		}

		// Colors (as of RichFaces 3.3.1)

		if ( Color.class.isAssignableFrom( clazz ) )
		{
			if ( WidgetBuilderUtils.isReadOnly( attributes ))
				return FacesContext.getCurrentInstance().getApplication().createComponent( "javax.faces.HtmlOutputText" );

			return application.createComponent( "org.richfaces.ColorPicker" );
		}

		// Suggestion box

		if ( String.class.equals( clazz ) )
		{
			String facesSuggest = attributes.get( FACES_SUGGEST );

			if ( facesSuggest != null )
			{
				UIComponent componentStub = application.createComponent( "org.metawidget.Stub" );
				componentStub.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_NOT_RECREATABLE, true );
				List<UIComponent> children = componentStub.getChildren();

				UIViewRoot viewRoot = context.getViewRoot();

				// Standard text box

				UIComponent inputText = application.createComponent( "javax.faces.HtmlInputText" );
				inputText.setId( viewRoot.createUniqueId() );
				children.add( inputText );

				// Suggestion box

				UISuggestionBox suggestionBox = (UISuggestionBox) application.createComponent( "org.richfaces.SuggestionBox" );
				suggestionBox.setFor( inputText.getId() );
				suggestionBox.setVar( "_internal" );
				children.add( suggestionBox );

				try
				{
					// RichFaces 3.2/JSF 1.2 mode
					//
					// Note: we wrap the MethodExpression as an Object[] to stop link-time
					// dependencies on javax.el.MethodExpression, so that we still work with
					// JSF 1.1

					Object[] methodExpression = new Object[] { application.getExpressionFactory().createMethodExpression( context.getELContext(), facesSuggest, null, ClassUtils.NO_CLASSES ) };
					ClassUtils.setProperty( suggestionBox, "suggestionAction", methodExpression[0] );
				}
				catch ( NoSuchMethodError e )
				{
					// RichFaces 3.1/JSF 1.1 mode

					MethodBinding methodBinding = application.createMethodBinding( facesSuggest, null );
					suggestionBox.setSuggestionAction( methodBinding );
				}

				// Column

				UIColumn column = (UIColumn) application.createComponent( "javax.faces.Column" );
				column.setId( viewRoot.createUniqueId() );
				suggestionBox.getChildren().add( column );

				// Output text box

				UIComponent columnText = application.createComponent( "javax.faces.HtmlOutputText" );
				columnText.setId( viewRoot.createUniqueId() );
				ValueBinding valueBinding = application.createValueBinding( "#{_internal}" );
				columnText.setValueBinding( "value", valueBinding );
				column.getChildren().add( columnText );

				return componentStub;
			}
		}

		// Not for RichFaces

		return null;
	}

	//
	// Private methods
	//

	private UIComponent createRanged( Map<String, String> attributes )
	{
		// Ranged

		String minimumValue = attributes.get( MINIMUM_VALUE );
		String maximumValue = attributes.get( MAXIMUM_VALUE );

		if ( minimumValue != null && !"".equals( minimumValue ) && maximumValue != null && !"".equals( maximumValue ) )
		{
			FacesContext context = FacesContext.getCurrentInstance();
			Application application = context.getApplication();

			UIInputNumberSlider slider = (UIInputNumberSlider) application.createComponent( "org.richfaces.inputNumberSlider" );
			slider.setMinValue( minimumValue );
			slider.setMaxValue( maximumValue );

			return slider;
		}

		return null;
	}
}
