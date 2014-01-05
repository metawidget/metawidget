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

package org.metawidget.faces.component.html.widgetbuilder.richfaces;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.awt.Color;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.el.ELContext;
import javax.faces.application.Application;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.richfaces.component.AutocompleteMode;
import org.richfaces.component.UIAutocomplete;
import org.richfaces.component.UICalendar;
import org.richfaces.component.UISuggestionBox;
import org.richfaces.component.html.HtmlCalendar;
import org.richfaces.component.html.HtmlInputNumberSlider;
import org.richfaces.component.html.HtmlInputNumberSpinner;
import org.richfaces.component.html.HtmlSuggestionBox;

/**
 * WidgetBuilder for RichFaces environments.
 * <p>
 * Creates native RichFaces UIComponents, such as <code>HtmlCalendar</code> and
 * <code>HtmlInputNumberSlider</code>, to suit the inspected fields.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@SuppressWarnings( "deprecation" )
public class RichFacesWidgetBuilder
	implements WidgetBuilder<UIComponent, UIMetawidget> {

	//
	// Private statics
	//

	private static final RichFacesVersionSpecificSupport	VERSION_SPECIFIC_WIDGET_BUILDER;

	static {

		if ( ClassUtils.niceForName( "org.richfaces.component.UISuggestionBox" ) != null ) {
			VERSION_SPECIFIC_WIDGET_BUILDER = new RichFaces3Support();
		} else {
			VERSION_SPECIFIC_WIDGET_BUILDER = new RichFaces4Support();
		}
	}

	//
	// Public methods
	//

	public UIComponent buildWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Not for RichFaces?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return null;
		}

		// Note: we tried implementing lookups using org.richfaces.ComboBox, but that
		// allows manual input and if you set enableManualInput=false it behaves a
		// bit screwy for our liking (ie. if you hit backspace the browser goes back)

		if ( attributes.containsKey( FACES_LOOKUP ) || attributes.containsKey( LOOKUP ) ) {
			return null;
		}

		// Lookup the class

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, null );

		if ( clazz == null ) {
			return null;
		}

		// Primitives

		if ( clazz.isPrimitive() ) {
			// Not for RichFaces

			if ( boolean.class.equals( clazz ) || char.class.equals( clazz ) ) {
				return null;
			}

			// Ranged

			UIComponent ranged = createRanged( attributes );

			if ( ranged != null ) {
				return ranged;
			}

			// Not-ranged

			HtmlInputNumberSpinner spinner = VERSION_SPECIFIC_WIDGET_BUILDER.createInputNumberSpinner();

			// May be ranged in one dimension only. In which case, explictly range the *other*
			// dimension because RichFaces defaults to 0-100

			String minimumValue = attributes.get( MINIMUM_VALUE );

			if ( minimumValue != null && !"".equals( minimumValue ) ) {
				spinner.setMinValue( minimumValue );
			} else if ( byte.class.equals( clazz ) ) {
				spinner.setMinValue( String.valueOf( Byte.MIN_VALUE ) );
			} else if ( short.class.equals( clazz ) ) {
				spinner.setMinValue( String.valueOf( Short.MIN_VALUE ) );
			} else if ( int.class.equals( clazz ) ) {
				spinner.setMinValue( String.valueOf( Integer.MIN_VALUE ) );
			} else if ( long.class.equals( clazz ) ) {
				spinner.setMinValue( String.valueOf( Long.MIN_VALUE ) );
			} else if ( float.class.equals( clazz ) ) {
				spinner.setMinValue( String.valueOf( -Float.MAX_VALUE ) );
			} else if ( double.class.equals( clazz ) ) {
				spinner.setMinValue( String.valueOf( -Double.MAX_VALUE ) );
			}

			String maximumValue = attributes.get( MAXIMUM_VALUE );

			if ( maximumValue != null && !"".equals( maximumValue ) ) {
				spinner.setMaxValue( maximumValue );
			} else if ( byte.class.equals( clazz ) ) {
				spinner.setMaxValue( String.valueOf( Byte.MAX_VALUE ) );
			} else if ( short.class.equals( clazz ) ) {
				spinner.setMaxValue( String.valueOf( Short.MAX_VALUE ) );
			} else if ( int.class.equals( clazz ) ) {
				spinner.setMaxValue( String.valueOf( Integer.MAX_VALUE ) );
			} else if ( long.class.equals( clazz ) ) {
				spinner.setMaxValue( String.valueOf( Long.MAX_VALUE ) );
			} else if ( float.class.equals( clazz ) ) {
				spinner.setMaxValue( String.valueOf( Float.MAX_VALUE ) );
			} else if ( double.class.equals( clazz ) ) {
				spinner.setMaxValue( String.valueOf( Double.MAX_VALUE ) );
			}

			// Wraps around?

			spinner.setCycled( false );

			// Stepped

			if ( float.class.equals( clazz ) || double.class.equals( clazz ) ) {
				spinner.setStep( "0.1" );
			}

			return spinner;
		}

		// Dates
		//
		// Note: when http://jira.jboss.org/jira/browse/RF-2023 gets implemented, that
		// would allow external, app-level configuration of this Calendar

		if ( Date.class.isAssignableFrom( clazz ) ) {
			UICalendar calendar = FacesUtils.createComponent( HtmlCalendar.COMPONENT_TYPE, "org.richfaces.CalendarRenderer" );

			if ( attributes.containsKey( DATETIME_PATTERN ) ) {
				calendar.setDatePattern( attributes.get( DATETIME_PATTERN ) );
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
			// Until https://jira.jboss.org/jira/browse/RF-4450 is fixed, do not use
			// UIInputNumberSpinner for nullable numbers
		}

		// RichFaces version-specific

		return VERSION_SPECIFIC_WIDGET_BUILDER.buildWidget( elementName, attributes, metawidget );
	}

	public UIComponent createRanged( Map<String, String> attributes ) {

		// Ranged

		String minimumValue = attributes.get( MINIMUM_VALUE );
		String maximumValue = attributes.get( MAXIMUM_VALUE );

		if ( minimumValue != null && !"".equals( minimumValue ) && maximumValue != null && !"".equals( maximumValue ) ) {

			HtmlInputNumberSlider slider = VERSION_SPECIFIC_WIDGET_BUILDER.createInputNumberSlider();
			slider.setMinValue( minimumValue );
			slider.setMaxValue( maximumValue );

			return slider;
		}

		return null;
	}

	//
	// Inner class
	//

	/* package private */interface RichFacesVersionSpecificSupport
		extends WidgetBuilder<UIComponent, UIMetawidget> {

		HtmlInputNumberSlider createInputNumberSlider();

		HtmlInputNumberSpinner createInputNumberSpinner();
	}

	/**
	 * RichFaces 3.x-specific components.
	 * <p>
	 * Defined as a separate class to avoid class-loading issues.
	 */

	/* package private */static class RichFaces3Support
		implements RichFacesVersionSpecificSupport {

		//
		// Public methods
		//

		public UIComponent buildWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

			FacesContext context = FacesContext.getCurrentInstance();
			Application application = context.getApplication();
			Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, null );

			if ( clazz == null ) {
				return null;
			}

			// Colors (as of RichFaces 3.3.1)

			if ( Color.class.equals( clazz ) ) {
				if ( WidgetBuilderUtils.isReadOnly( attributes ) ) {
					return FacesContext.getCurrentInstance().getApplication().createComponent( HtmlOutputText.COMPONENT_TYPE );
				}

				return application.createComponent( "org.richfaces.ColorPicker" );
			}

			// Suggestion box
			//
			// Note: for suggestion box to work in table column footer facets, you need
			// https://jira.jboss.org/jira/browse/RF-7700

			if ( String.class.equals( clazz ) ) {
				String facesSuggest = attributes.get( FACES_SUGGEST );

				if ( facesSuggest != null ) {
					UIComponent stubComponent = application.createComponent( UIStub.COMPONENT_TYPE );
					List<UIComponent> children = stubComponent.getChildren();

					// Standard text box

					HtmlInputText inputText = (HtmlInputText) application.createComponent( HtmlInputText.COMPONENT_TYPE );
					inputText.setStyle( ( (HtmlMetawidget) metawidget ).getStyle() );
					inputText.setStyleClass( ( (HtmlMetawidget) metawidget ).getStyleClass() );
					children.add( inputText );

					UISuggestionBox suggestionBox = (UISuggestionBox)
							application.createComponent( HtmlSuggestionBox.COMPONENT_TYPE );

					// Lock the 'id's so they don't get changed. This is important for the
					// JavaScript getElementById that RichFaces generates. Also, do not just use
					// 'viewRoot.createUniqueId' because, as per the RenderKit specification:
					//
					// "If the value returned from component.getId() is non-null and does not start
					// with UIViewRoot.UNIQUE_ID_PREFIX, call component.getClientId() and render
					// the result as the value of the id attribute in the markup for the component."
					//
					// Therefore the 'id' attribute is never rendered, therefore the JavaScript
					// getElementById doesn't work. Add our own prefix instead

					inputText.setId( "suggestionText_" + FacesUtils.createUniqueId() );
					suggestionBox.setId( "suggestionBox_" + FacesUtils.createUniqueId() );

					// Suggestion box

					suggestionBox.setFor( inputText.getId() );
					suggestionBox.setVar( "_internal" );
					children.add( suggestionBox );

					try {
						// RichFaces 3.2/JSF 1.2 mode
						//
						// Note: we wrap the MethodExpression as an Object[] to stop link-time
						// dependencies on javax.el.MethodExpression, so that we still work with
						// JSF 1.1
						//
						// Note: according to JavaDocs returnType is only important when literal
						// (i.e. without #{...}) expression is used, otherwise Object.class is fine
						// (http://community.jboss.org/message/516830#516830)

						Object[] methodExpression = new Object[] { application.getExpressionFactory().createMethodExpression( context.getELContext(), facesSuggest, Object.class, new Class[] { Object.class } ) };
						ClassUtils.setProperty( suggestionBox, "suggestionAction", methodExpression[0] );
					} catch ( Exception e ) {
						// RichFaces 3.1/JSF 1.1 mode

						MethodBinding methodBinding = application.createMethodBinding( facesSuggest, new Class[] { Object.class } );
						suggestionBox.setSuggestionAction( methodBinding );
					}

					// Column
					//
					// Note: this must be javax.faces.component.html.HtmlColumn, not
					// org.richfaces.component.html.HtmlColumn. The latter displayed okay, but when
					// a value was selected it did not populate back to the HtmlInputText

					UIColumn column = (UIColumn) application.createComponent( javax.faces.component.html.HtmlColumn.COMPONENT_TYPE );
					column.setId( FacesUtils.createUniqueId() );
					suggestionBox.getChildren().add( column );

					// Output text box

					UIComponent columnText = application.createComponent( HtmlOutputText.COMPONENT_TYPE );
					columnText.setId( FacesUtils.createUniqueId() );
					ValueBinding valueBinding = application.createValueBinding( "#{_internal}" );
					columnText.setValueBinding( "value", valueBinding );
					column.getChildren().add( columnText );

					return stubComponent;
				}
			}

			return null;
		}

		public HtmlInputNumberSlider createInputNumberSlider() {

			// RichFaces 3 uses lowercase 'i' for component, uppercase 'I' for renderer

			return FacesUtils.createComponent( "org.richfaces.inputNumberSlider", "org.richfaces.InputNumberSliderRenderer" );
		}

		public HtmlInputNumberSpinner createInputNumberSpinner() {

			// RichFaces 3 uses lowercase 'i' for component, uppercase 'I' for renderer

			return FacesUtils.createComponent( "org.richfaces.inputNumberSpinner", "org.richfaces.InputNumberSpinnerRenderer" );
		}
	}

	/**
	 * RichFaces 4.x-specific components.
	 * <p>
	 * Defined as a separate class to avoid class-loading issues.
	 */

	/* package private */static class RichFaces4Support
		implements RichFacesVersionSpecificSupport {

		//
		// Public methods
		//

		public UIComponent buildWidget( String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

			FacesContext context = FacesContext.getCurrentInstance();
			Application application = context.getApplication();
			Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, null );

			if ( clazz == null ) {
				return null;
			}

			// Autocomplete box

			if ( String.class.equals( clazz ) ) {
				String facesSuggest = attributes.get( FACES_SUGGEST );

				if ( facesSuggest != null ) {
					UIAutocomplete autoComplete = FacesUtils.createComponent( UIAutocomplete.COMPONENT_TYPE, "org.richfaces.AutocompleteRenderer" );

					// new Class[] { ELContext.class, UIComponent.class, String.class } is what
					// AutocompleteRendererBase.getItems is looking for

					autoComplete.setAutocompleteMethod( application.getExpressionFactory().createMethodExpression( context.getELContext(), facesSuggest, Object.class, new Class[] { ELContext.class, UIComponent.class, String.class } ) );

					// Some reasonable defaults

					autoComplete.setMinChars( 2 );
					autoComplete.setMode( AutocompleteMode.cachedAjax );

					// RichFaces 4 UIAutocomplete doesn't appear to have support setMaxLength?

					return autoComplete;
				}
			}

			return null;
		}

		public HtmlInputNumberSlider createInputNumberSlider() {

			// RichFaces 4 uses uppercase 'I', uppercase 'I' for renderer

			return FacesUtils.createComponent( HtmlInputNumberSlider.COMPONENT_TYPE, "org.richfaces.InputNumberSliderRenderer" );
		}

		public HtmlInputNumberSpinner createInputNumberSpinner() {

			// RichFaces 4 uses uppercase 'I' for component, uppercase 'I' for renderer

			return FacesUtils.createComponent( HtmlInputNumberSpinner.COMPONENT_TYPE, "org.richfaces.InputNumberSpinnerRenderer" );
		}
	}
}
