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

package org.metawidget.faces.component.html.widgetbuilder.primefaces;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.*;

import javax.faces.application.Application;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.widgetbuilder.HtmlWidgetBuilder;
import org.metawidget.util.WidgetBuilderUtils;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.column.Column;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.commandlink.CommandLink;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.password.Password;
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
	// Private members
	//

	private String  mDataTableStyleClass;

	private String  mDataTableRowStyleClass;

	private String  mDataTableTableStyleClass;

	private int     mMaximumColumnsInDataTable;

	//
	// Constructor
	//

	public PrimeFacesWidgetBuilder() {
		this( new PrimeFacesWidgetBuilderConfig() );
	}

	public PrimeFacesWidgetBuilder( PrimeFacesWidgetBuilderConfig config ) {

		mDataTableStyleClass = config.getDataTableStyleClass();
		mDataTableRowStyleClass = config.getDataTableRowStyleClass();
		mDataTableTableStyleClass = config.getDataTableTableStyleClass();
		mMaximumColumnsInDataTable = config.getMaximumColumnsInDataTable();
	}

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
				} else {
					
					// ...otherwise just a UISelectOne
					
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
					} else {
						
						// ...otherwise just a UISelectOne

						component = FacesUtils.createComponent( SelectOneMenu.COMPONENT_TYPE, "org.primefaces.component.SelectOneMenuRenderer" );
					}

					initStaticSelect( component, lookup, clazz, attributes, metawidget );
					return component;
				}

				// Other types

				if ( char.class.equals( clazz ) || Character.class.isAssignableFrom( clazz ) ) {
					InputText inputText = (InputText) application.createComponent( InputText.COMPONENT_TYPE );
					inputText.setMaxlength( 1 );
					return inputText;
				}

				if ( clazz.isPrimitive() ) {

					// Not for PrimeFaces

					if ( boolean.class.equals( clazz ) ) {
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

					UIComponent component;
					if ( TRUE.equals( attributes.get( MASKED ) ) ) {
						component = FacesUtils.createComponent( Password.COMPONENT_TYPE, "org.primefaces.component.PasswordRenderer" );
					} else if ( TRUE.equals( attributes.get( LARGE ) ) ) {
						component = FacesUtils.createComponent( InputTextarea.COMPONENT_TYPE, "org.primefaces.component.InputTextareaRenderer" );
					} else {
						component = FacesUtils.createComponent( InputText.COMPONENT_TYPE, "org.primefaces.component.InputTextRenderer" );
					}

					setMaximumLength( component, attributes );

					return component;
				} else if ( List.class.isAssignableFrom( clazz ) || DataModel.class.isAssignableFrom( clazz ) || clazz.isArray() ) {

					// Supported Collections

					return createDataTableComponent( elementName, attributes, metawidget );
				} else if ( Collection.class.isAssignableFrom( clazz ) ) {

					// Unsupported Collections

					return application.createComponent( UIStub.COMPONENT_TYPE );
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
	// Protected methods
	//

	@Override
	protected void setMaximumLength( UIComponent component, Map<String, String> attributes ) {

		String maximumLength = attributes.get( MAXIMUM_LENGTH );

		if ( maximumLength != null && !"".equals( maximumLength ) ) {
			if ( component instanceof InputTextarea ) {
				( (InputTextarea) component ).setMaxlength( Integer.parseInt( maximumLength ) );
			} else {
				super.setMaximumLength( component, attributes );
			}
		}
	}
	
	@Override
	protected void configureDataTableComponent( UIData uiData ) {
		
		DataTable dataTable = (DataTable) uiData;

		dataTable.setStyleClass( mDataTableStyleClass );
		dataTable.setRowStyleClass( mDataTableRowStyleClass );
		dataTable.setTableStyleClass( mDataTableTableStyleClass );
	}

	@Override
	protected UIColumn createColumnComponent() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		return (Column) application.createComponent( Column.COMPONENT_TYPE );
	}
	
	@Override
	protected UIData createDataTableComponent() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		
		return (UIData) application.createComponent( DataTable.COMPONENT_TYPE );
	}
	
	@Override
	protected HtmlCommandLink createCommandLinkComponent() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		
		return (CommandLink) application.createComponent( CommandLink.COMPONENT_TYPE );
	}

	@Override
	protected int getMaximumColumnsInDataTable() {
		
		return mMaximumColumnsInDataTable;
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

			InputText inputText = (InputText) application
					.createComponent( InputText.COMPONENT_TYPE );
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
