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

package org.metawidget.vaadin.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.vaadin.Stub;
import org.metawidget.vaadin.VaadinMetawidget;
import org.metawidget.vaadin.VaadinValuePropertyProvider;
import org.metawidget.vaadin.widgetprocessor.binding.simple.Converter;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * WidgetBuilder for Vaadin environments.
 * <p>
 * Creates native Vaadin <code>Components</code>, such as <code>TextField</code> and
 * <code>ComboBox</code> or <code>CheckBox<code>, to suit the inspected fields.
 * 
 * @author Loghman Barari
 */

@SuppressWarnings( "serial" )
public class VaadinWidgetBuilder
	implements
		WidgetBuilder<Component, VaadinMetawidget>,
		VaadinValuePropertyProvider, Serializable {

	protected final static String	TABLE_COLUMNS	= "tablecolumns";

	//
	// Public methods
	//

	public String getValueProperty( Component component ) {

		if ( component instanceof Property ) {
			return "value";
		}

		return null;
	}

	public Component buildWidget( String elementName,
			Map<String, String> attributes, VaadinMetawidget metawidget ) {

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {

			// TODO: this should return a Stub
			return null;
		}

		String labelString = metawidget.getLabelString( attributes );

		// Action

		if ( ACTION.equals( elementName ) ) {

			return prepareComponent( new Button( labelString ), attributes, metawidget );
		}

		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, assume a String

		if ( type == null ) {
			type = String.class.getName();
		}

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		// Support mandatory Booleans (can be rendered as a checkbox, even
		// though they have a Lookup)

		if ( Boolean.class.equals( clazz )
				&& TRUE.equals( attributes.get( REQUIRED ) ) ) {

			return prepareComponent( new CheckBox( labelString ), attributes, metawidget );
		}

		// Enums
		if ( clazz.isEnum() ) {
			return createComboBox4EnumComponent( labelString, attributes, clazz, metawidget );
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {

			return createComboBoxComponent( labelString, attributes, lookup, metawidget );
		}

		if ( clazz != null ) {

			String minimumValue = attributes.get( MINIMUM_VALUE );
			String maximumValue = attributes.get( MAXIMUM_VALUE );
			boolean allowNull = !TRUE.equals( attributes.get( REQUIRED ) );

			// Primitives
			if ( clazz.isPrimitive() ) {
				clazz = ClassUtils.getWrapperClass( clazz );
			}

			// booleans
			if ( Boolean.class.equals( clazz ) ) {
				return prepareComponent( new CheckBox( labelString ), attributes, metawidget );
			}

			// chars

			if ( Character.class.equals( clazz ) ) {
				TextField textField = new TextField( labelString );
				textField.addValidator( new StringLengthValidator( metawidget.getBundle(), 1, 1, allowNull ) );

				return prepareComponent( textField, attributes, metawidget );
			}

			// Ranged and Not-ranged numeric value

			if ( Byte.class.equals( clazz ) ) {
				TextField textField = new TextField( labelString );

				byte value = 0;
				byte minimum = Byte.MIN_VALUE;
				byte maximum = Byte.MAX_VALUE;

				if ( minimumValue != null && !"".equals( minimumValue ) ) {
					minimum = Byte.parseByte( minimumValue );
					value = (byte) Math.max( value, minimum );
				}

				if ( maximumValue != null && !"".equals( maximumValue ) ) {
					maximum = Byte.parseByte( maximumValue );
					value = (byte) Math.min( value, maximum );
				}

				textField.addValidator( new NumericValidator<Byte>( metawidget.getBundle(), minimum, maximum ) );

				return prepareComponent( textField, attributes, metawidget );

			} else if ( Short.class.equals( clazz ) ) {
				TextField textField = new TextField( labelString );

				short value = 0;
				short minimum = Short.MIN_VALUE;
				short maximum = Short.MAX_VALUE;

				if ( minimumValue != null && !"".equals( minimumValue ) ) {
					minimum = Short.parseShort( minimumValue );
					value = (short) Math.max( value, minimum );
				}

				if ( maximumValue != null && !"".equals( maximumValue ) ) {
					maximum = Short.parseShort( maximumValue );
					value = (short) Math.min( value, maximum );
				}

				textField.addValidator( new NumericValidator<Short>( metawidget.getBundle(), minimum, maximum ) );

				return prepareComponent( textField, attributes, metawidget );

			} else if ( Integer.class.equals( clazz ) ) {
				TextField textField = new TextField( labelString );

				int value = 0;
				int minimum = Integer.MIN_VALUE;
				int maximum = Integer.MAX_VALUE;

				if ( minimumValue != null && !"".equals( minimumValue ) ) {
					minimum = Integer.parseInt( minimumValue );
					value = Math.max( value, minimum );
				}

				if ( maximumValue != null && !"".equals( maximumValue ) ) {
					maximum = Integer.parseInt( maximumValue );
					value = Math.min( value, maximum );
				}

				textField.addValidator( new NumericValidator<Integer>( metawidget.getBundle(), minimum, maximum ) );

				return prepareComponent( textField, attributes, metawidget );

			} else if ( Long.class.equals( clazz ) ) {
				TextField textField = new TextField( labelString );

				long value = 0;
				long minimum = Long.MIN_VALUE;
				long maximum = Long.MAX_VALUE;

				if ( minimumValue != null && !"".equals( minimumValue ) ) {
					minimum = Long.parseLong( minimumValue );
					value = Math.max( value, minimum );
				}

				if ( maximumValue != null && !"".equals( maximumValue ) ) {
					maximum = Long.parseLong( maximumValue );
					value = Math.min( value, maximum );
				}

				textField.addValidator( new NumericValidator<Long>( metawidget.getBundle(), minimum, maximum ) );

				return prepareComponent( textField, attributes, metawidget );

			} else if ( Float.class.equals( clazz ) ) {
				TextField textField = new TextField( labelString );

				float value = 0;
				float minimum = -Float.MAX_VALUE;
				float maximum = Float.MAX_VALUE;

				if ( minimumValue != null && !"".equals( minimumValue ) ) {
					minimum = Float.parseFloat( minimumValue );
					value = Math.max( value, minimum );
				}

				if ( maximumValue != null && !"".equals( maximumValue ) ) {
					maximum = Float.parseFloat( maximumValue );
					value = Math.min( value, maximum );
				}

				textField.addValidator( new NumericValidator<Float>( metawidget.getBundle(), minimum, maximum ) );

				return prepareComponent( textField, attributes, metawidget );
			} else if ( Double.class.equals( clazz ) ) {
				TextField textField = new TextField( labelString );

				double value = 0;
				double minimum = -Double.MAX_VALUE;
				double maximum = Double.MAX_VALUE;

				if ( minimumValue != null && !"".equals( minimumValue ) ) {
					minimum = Double.parseDouble( minimumValue );
					value = Math.max( value, minimum );
				}

				if ( maximumValue != null && !"".equals( maximumValue ) ) {
					maximum = Double.parseDouble( maximumValue );
					value = Math.min( value, maximum );
				}

				textField.addValidator( new NumericValidator<Double>( metawidget.getBundle(), minimum, maximum ) );

				return prepareComponent( textField, attributes, metawidget );
			}

			// Strings

			if ( String.class.equals( clazz ) ) {

				AbstractTextField textField;

				maximumValue = attributes.get( MAXIMUM_LENGTH );
				minimumValue = attributes.get( MINIMUM_LENGTH );

				if ( TRUE.equals( attributes.get( MASKED ) ) ) {

					textField = new PasswordField( labelString );

				} else if ( TRUE.equals( attributes.get( LARGE ) ) ) {

					textField = new TextArea( labelString );

					// Since we know we are dealing with Strings, we consider
					// word-wrapping a sensible default

					( (TextArea) textField ).setWordwrap( true );

					// We also consider 3 rows a sensible default

					( (TextArea) textField ).setRows( 3 );

				} else {
					textField = new TextField( labelString );
				}

				int minimum = -1;
				int maximum = -1;

				if ( minimumValue != null && !"".equals( minimumValue ) ) {
					minimum = Integer.parseInt( minimumValue );
					if ( minimum < -1 ) {
						minimum = -1;
					}
				}

				if ( maximumValue != null && !"".equals( maximumValue ) ) {
					maximum = Integer.parseInt( maximumValue );
					if ( maximum < minimum ) {
						maximum = minimum;
					}
				}

				if ( ( minimum > -1 ) || ( maximum > -1 ) || ( !allowNull ) ) {

					textField = new TextField( labelString );

					textField.addValidator( new StringLengthValidator( metawidget.getBundle(), minimum, maximum, allowNull ) );

					textField.setMaxLength( maximum );
				}

				return prepareComponent( textField, attributes, metawidget );
			}

			// Dates

			if ( Date.class.equals( clazz ) ) {
				return prepareComponent( new PopupDateField( labelString ), attributes, metawidget );
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				Component table = createTableComponent( labelString, attributes, metawidget );

				if ( table != null ) {

					return table;
				}

				// Unsupported Collection

				return new Stub();
			}

		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return prepareComponent( new TextField( labelString ), attributes, metawidget );
		}

		return null;
	}

	//
	// Private methods
	//

	private Component prepareComponent( final AbstractField abstractField, Map<String, String> attributes, final VaadinMetawidget metawidget ) {

		abstractField.setDebugId( metawidget.getDebugId() + "$" + attributes.get( NAME ) );
		abstractField.setImmediate( true );

		if ( TRUE.equals( attributes.get( REQUIRED ) ) ) {
			abstractField.setRequired( true );

			String errorMessage = null;
			if ( metawidget.getBundle() != null ) {
				// TODO: javax.faces???
				errorMessage = metawidget.getBundle().getString( "javax.faces.component.UIInput.REQUIRED" );
			}

			if ( ( errorMessage == null ) || ( "".equals( errorMessage ) ) ) {
				errorMessage = "{0} is required";
			}

			errorMessage = MessageFormat.format( errorMessage, abstractField.getCaption() );

			abstractField.setRequiredError( errorMessage );
		}

		abstractField.setWidth( "100%" );

		abstractField.addListener( new ValueChangeListener() {

			public void valueChange( ValueChangeEvent event ) {

				abstractField.setComponentError( null );
				abstractField.validate();
			}
		} );

		return abstractField;
	}

	private AbstractComponent createTableComponent( String caption, Map<String, String> attributes, VaadinMetawidget metawidget ) {

		if ( !attributes.containsKey( PARAMETERIZED_TYPE ) ) {
			return null;
		}

		String name = attributes.get( NAME );

		List<String> columns = null;

		String table_columns = attributes.get( TABLE_COLUMNS );

		String componentType = attributes.get( PARAMETERIZED_TYPE );

		Class<?> componentClass = ClassUtils.niceForName( componentType );

		if ( componentClass.asSubclass( Comparable.class ) == null ) {
			return null;
		}

		if ( table_columns == null || "".equals( attributes ) ) {

			// Inspect type of Collection

			String inspectedType = metawidget.inspect( null, componentType );

			// Determine columns

			columns = CollectionUtils.newArrayList();

			Element root = XmlUtils.documentFromString( inspectedType ).getDocumentElement();
			NodeList elements = root.getFirstChild().getChildNodes();

			for ( int loop = 0, length = elements.getLength(); loop < length; loop++ ) {

				Node node = elements.item( loop );

				if ( node.getNodeName() != PROPERTY ) {
					continue;
				}

				Map<String, String> property_attribute = XmlUtils
						.getAttributesAsMap( node );

				if ( TRUE.equals( property_attribute.get( HIDDEN ) ) ) {
					continue;
				}

				columns.add( property_attribute.get( NAME ) );
			}

			attributes.put( TABLE_COLUMNS, ArrayUtils
					.toString( columns.toArray() ) );

		}

		boolean readOnly = metawidget.isReadOnly();

		final Table table = new Table();

		table.setDebugId( metawidget.getDebugId() + "$" + name );
		table.setImmediate( true );
		table.setHeight( "170px" );
		table.setWidth( "100%" );
		table.setWriteThrough( false );

		table.setEditable( !readOnly );

		if ( readOnly ) {
			table.setCaption( caption );

			return table;
		}

		return new TableWrapper( caption, table, metawidget );
	}

	private Component createComboBoxComponent( String labelString, Map<String, String> attributes, String lookup, VaadinMetawidget metawidget ) {

		// Add an empty choice (if nullable, and not required)

		List<String> values = CollectionUtils.fromString( lookup );
		Map<String, String> labelsMap = new Hashtable<String, String>();

		Converter<?> converter = metawidget.getWidgetProcessor( Converter.class );

		// May have alternate labels

		String lookupLabels = attributes.get( LOOKUP_LABELS );

		if ( lookupLabels != null && !"".equals( lookupLabels ) ) {
			labelsMap = VaadinWidgetBuilderUtils.getLabelsMap( values, CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ) );

		} else {
			for ( String value : values ) {
				labelsMap.put( value, value );
			}
		}

		ComboBox comboBox = new ComboBox( labelString );

		for ( final Entry<String, String> item : labelsMap.entrySet() ) {

			Object convertedValue = null;

			if ( converter == null ) {
				convertedValue = item.getKey();
			} else {
				convertedValue = converter.convert( item.getKey() );
			}

			comboBox.addItem( convertedValue );

			String caption = item.getValue();

			if ( metawidget.getBundle() != null ) {

				try {
					caption = metawidget.getLocalizedKey( caption );
				} catch ( MissingResourceException e ) {
					// Use default caption
				}
			}

			comboBox.setItemCaption( convertedValue, caption );
		}

		if ( !WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
			comboBox.setRequired( true );
		}

		prepareComponent( comboBox, attributes, metawidget );

		return comboBox;
	}

	public <T> ComboBox createComboBox4EnumComponent( String labelString,
			Map<String, String> attributes, Class<T> clazz,
			VaadinMetawidget metawidget ) {

		ComboBox comboBox = new ComboBox( labelString );

		for ( T enumItem : clazz.getEnumConstants() ) {
			String caption = enumItem.toString();

			if ( metawidget.getBundle() != null ) {

				try {
					caption = metawidget.getBundle().getString(
							caption );
				} catch ( MissingResourceException e ) {
					// Use existing caption
				}
			}

			comboBox.addItem( enumItem );
			comboBox.setItemCaption( enumItem, caption );
		}

		prepareComponent( comboBox, attributes, metawidget );

		return comboBox;
	}

	//
	// Inner Class
	//

	private static class TableWrapper
		extends VerticalLayout
		implements WrapperComponent {

		//
		// Private members
		//

		private Table	mTable;

		//
		// Public methods
		//

		public TableWrapper( String caption, final Table table, VaadinMetawidget metawidget ) {

			addComponent( table );
			setCaption( caption );
			setImmediate( true );
			setWidth( "100%" );

			String captionToUse = metawidget.getLocalizedKey( "add" );
			if ( captionToUse == null ) {
				captionToUse = "Add";
			}

			Button addNewButton = new Button( captionToUse );
			addNewButton.setDebugId( table.getDebugId() + "$" + "add" );
			addNewButton.setImmediate( true );
			addNewButton.addListener( new ClickListener() {

				public void buttonClick( ClickEvent event ) {

					table.addItem();
				}
			} );

			captionToUse = metawidget.getLocalizedKey( "delete" );
			if ( captionToUse == null ) {
				captionToUse = "Delete";
			}

			final Button deleteButton = new Button( captionToUse );
			deleteButton.setDebugId( table.getDebugId() + "$" + "delete" );
			deleteButton.setImmediate( true );
			deleteButton.setEnabled( false );
			deleteButton.addListener( new ClickListener() {

				public void buttonClick( ClickEvent event ) {

					table.removeItem( table.getValue() );
				}
			} );

			HorizontalLayout actionLayout = new HorizontalLayout();
			// actionLayout.setImmediate(true);
			actionLayout.setMargin( false );
			actionLayout.setSpacing( true );
			actionLayout.addComponent( addNewButton );
			actionLayout.addComponent( deleteButton );

			addComponent( actionLayout );

			setComponentAlignment( actionLayout, Alignment.MIDDLE_CENTER );

			table.setSelectable( true );
			table.addListener( new Table.ValueChangeListener() {

				public void valueChange( ValueChangeEvent event ) {

					boolean enabled = ( null != event.getProperty().getValue() );

					deleteButton.setEnabled( enabled );
				}
			} );

			mTable = table;
		}

		public Component getMasterComponent() {

			return mTable;
		}

	}

	public static interface WrapperComponent {

		Component getMasterComponent();
	}

	/* package private */static class NumericValidator<T extends Number>
			extends AbstractValidator {

		private final static String	DEFAULT_TYPE_ERROR_MESSAGE		= "Not correct type";

		private final static String	DEFAULT_RANGE_ERROR_MESSAGE		= "{2} must be between {0} and {1}";

		private final static String	DEFAULT_MAXIMUM_ERROR_MESSAGE	= "{1} must not be greater than {0}";

		private final static String	DEFAULT_MINIMUM_ERROR_MESSAGE	= "{1} must not be less than {0}";

		private enum ValidatorType {
			TYPE_VALIDATOR, MAXIMUM_VALIDATOR, MINIMUM_VALIDATOR, RANGE_VALIDATOR
		}

		private enum ErrorType {
			NO_ERROR, TYPE_ERROR, RANGE_ERROR
		}

		private ValidatorType			mValidatorType;

		private Class<? extends Number>	mValueType;

		private T						mMinimum;

		private T						mMaximum;

		private String					mTypeErrorMessage;

		private ResourceBundle			mBundle;

		public NumericValidator( ResourceBundle bundle, T minimum, T maximum ) {

			super( "" );

			mBundle = bundle;

			mMinimum = minimum;
			mMaximum = maximum;
			mValueType = minimum.getClass();

			String errorMessage = "";

			mValidatorType = ValidatorType.TYPE_VALIDATOR;

			if ( mValueType == Byte.class ) {
				mTypeErrorMessage = getBundleString( "javax.faces.validator.ByteRangeValidator.TYPE" );

				if ( ( Byte.MAX_VALUE != (Byte) maximum )
						&& ( Byte.MIN_VALUE != (Byte) minimum ) ) {

					errorMessage = getBundleString( "javax.faces.validator.ByteRangeValidator.NOT_IN_RANGE" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_RANGE_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.RANGE_VALIDATOR;
				} else if ( Byte.MAX_VALUE == (Byte) maximum ) {
					errorMessage = getBundleString( "javax.faces.validator.ByteRangeValidator.MAXIMUM" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_MAXIMUM_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.MAXIMUM_VALIDATOR;
				} else if ( Byte.MIN_VALUE == (Byte) minimum ) {
					errorMessage = getBundleString( "javax.faces.validator.ByteRangeValidator.MINIMUM" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_MINIMUM_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.MINIMUM_VALIDATOR;
				}
			}

			if ( mValueType == Short.class ) {

				mTypeErrorMessage = getBundleString( "javax.faces.validator.ShortRangeValidator.TYPE" );

				if ( ( Short.MAX_VALUE != (Short) maximum )
						&& ( Short.MIN_VALUE != (Short) minimum ) ) {
					errorMessage = getBundleString( "javax.faces.validator.ShortRangeValidator.NOT_IN_RANGE" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_RANGE_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.RANGE_VALIDATOR;
				} else if ( Short.MAX_VALUE == (Short) maximum ) {
					errorMessage = getBundleString( "javax.faces.validator.ShortRangeValidator.MAXIMUM" );

					if ( bundle == null ) {
						errorMessage = DEFAULT_MAXIMUM_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.MAXIMUM_VALIDATOR;
				} else if ( Short.MIN_VALUE == (Short) minimum ) {

					errorMessage = getBundleString( "javax.faces.validator.ShortRangeValidator.MINIMUM" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_MINIMUM_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.MINIMUM_VALIDATOR;
				}
			}

			if ( mValueType == Integer.class ) {

				mTypeErrorMessage = getBundleString( "javax.faces.validator.IntegerRangeValidator.TYPE" );

				if ( ( Integer.MAX_VALUE != (Integer) maximum )
						&& ( Integer.MIN_VALUE != (Integer) minimum ) ) {

					errorMessage = getBundleString( "javax.faces.validator.IntegerRangeValidator.NOT_IN_RANGE" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_RANGE_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.RANGE_VALIDATOR;
				} else if ( Integer.MAX_VALUE == (Integer) maximum ) {

					errorMessage = getBundleString( "javax.faces.validator.IntegerRangeValidator.MAXIMUM" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_MAXIMUM_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.MAXIMUM_VALIDATOR;
				} else if ( Integer.MIN_VALUE == (Integer) minimum ) {

					errorMessage = getBundleString( "javax.faces.validator.IntegerRangeValidator.MINIMUM" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_MINIMUM_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.MINIMUM_VALIDATOR;
				}
			}

			if ( mValueType == Long.class ) {

				mTypeErrorMessage = getBundleString( "javax.faces.validator.LongRangeValidator.TYPE" );

				if ( ( Long.MAX_VALUE != (Long) maximum )
						&& ( Long.MIN_VALUE != (Long) minimum ) ) {

					errorMessage = getBundleString( "javax.faces.validator.LongRangeValidator.NOT_IN_RANGE" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_RANGE_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.RANGE_VALIDATOR;
				} else if ( Long.MAX_VALUE == (Long) maximum ) {
					errorMessage = getBundleString( "javax.faces.validator.LongRangeValidator.MAXIMUM" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_MAXIMUM_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.MAXIMUM_VALIDATOR;
				} else if ( Long.MIN_VALUE == (Long) minimum ) {
					errorMessage = getBundleString( "javax.faces.validator.LongRangeValidator.MINIMUM" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_MINIMUM_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.MINIMUM_VALIDATOR;
				}

			}

			if ( mValueType == Float.class ) {

				mTypeErrorMessage = getBundleString( "javax.faces.validator.FloatRangeValidator.TYPE" );

				if ( ( Float.MAX_VALUE != (Float) maximum )
						&& ( Float.MIN_VALUE != (Float) minimum ) ) {

					errorMessage = getBundleString( "javax.faces.validator.FloatRangeValidator.NOT_IN_RANGE" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_RANGE_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.RANGE_VALIDATOR;
				} else if ( Float.MAX_VALUE == (Float) maximum ) {

					errorMessage = getBundleString( "javax.faces.validator.FloatRangeValidator.MAXIMUM" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_MAXIMUM_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.MAXIMUM_VALIDATOR;
				} else if ( Float.MIN_VALUE == (Float) minimum ) {

					errorMessage = getBundleString( "javax.faces.validator.FloatRangeValidator.MINIMUM" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_MINIMUM_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.MINIMUM_VALIDATOR;
				}

			}

			if ( mValueType == Double.class ) {

				mTypeErrorMessage = getBundleString( "javax.faces.validator.DoubleRangeValidator.TYPE" );

				if ( ( Double.MAX_VALUE != (Double) maximum )
						&& ( Double.MIN_VALUE != (Double) minimum ) ) {

					errorMessage = getBundleString( "javax.faces.validator.DoubleRangeValidator.NOT_IN_RANGE" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_RANGE_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.RANGE_VALIDATOR;
				} else if ( Double.MAX_VALUE == (Double) maximum ) {

					errorMessage = getBundleString( "javax.faces.validator.DoubleRangeValidator.MAXIMUM" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_MAXIMUM_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.MAXIMUM_VALIDATOR;
				} else if ( Double.MIN_VALUE == (Double) minimum ) {
					errorMessage = getBundleString( "javax.faces.validator.DoubleRangeValidator.MINIMUM" );

					if ( errorMessage == null ) {
						errorMessage = DEFAULT_MINIMUM_ERROR_MESSAGE;
					}

					mValidatorType = ValidatorType.MINIMUM_VALIDATOR;
				}
			}

			if ( mTypeErrorMessage == null ) {
				mTypeErrorMessage = DEFAULT_TYPE_ERROR_MESSAGE;
			}

			setErrorMessage( errorMessage );
		}

		//
		// Private Methods
		//

		private String getBundleString( String key ) {

			if ( mBundle != null ) {
				try {
					return mBundle.getString( key );
				} catch ( MissingResourceException e ) {
					// return null
				}
			}

			return null;
		}

		private ErrorType validating( Object value ) {

			if ( value == null ) {
				return ErrorType.NO_ERROR;
			}

			String valueAsString = String.valueOf( value );

			try {
				if ( mValueType == Byte.class ) {
					Byte val = Byte.parseByte( valueAsString );
					return ( ( val >= (Byte) mMinimum ) && ( val <= (Byte) mMaximum ) ) ? ErrorType.NO_ERROR : ErrorType.RANGE_ERROR;
				}

				if ( mValueType == Short.class ) {
					Short val = Short.parseShort( valueAsString );
					return ( ( val >= (Short) mMinimum ) && ( val <= (Short) mMaximum ) ) ? ErrorType.NO_ERROR : ErrorType.RANGE_ERROR;
				}

				if ( mValueType == Integer.class ) {
					Integer val = Integer.parseInt( valueAsString );
					return ( ( val >= (Integer) mMinimum ) && ( val <= (Integer) mMaximum ) ) ? ErrorType.NO_ERROR : ErrorType.RANGE_ERROR;
				}

				if ( mValueType == Long.class ) {
					Long val = Long.parseLong( valueAsString );
					return ( ( val >= (Long) mMinimum ) && ( val <= (Long) mMaximum ) ) ? ErrorType.NO_ERROR : ErrorType.RANGE_ERROR;
				}

				if ( mValueType == Float.class ) {
					Float val = Float.parseFloat( valueAsString );
					return ( ( val >= (Float) mMinimum ) && ( val <= (Float) mMaximum ) ) ? ErrorType.NO_ERROR : ErrorType.RANGE_ERROR;
				}

				if ( mValueType == Double.class ) {
					Double val = Double.parseDouble( valueAsString );
					return ( ( val >= (Double) mMinimum ) && ( val <= (Double) mMaximum ) ) ? ErrorType.NO_ERROR : ErrorType.RANGE_ERROR;
				}

				return ErrorType.NO_ERROR;
			} catch ( Exception e ) {
				return ErrorType.TYPE_ERROR;
			}
		}

		//
		// Public Methods
		//

		public boolean isValid( Object value ) {

			return validating( value ).equals( ErrorType.NO_ERROR );
		}

		@Override
		public void validate( Object value )
			throws InvalidValueException {

			String message = "";
			ErrorType errorType = validating( value );

			switch ( errorType ) {
				case RANGE_ERROR:
					switch ( mValidatorType ) {
						case RANGE_VALIDATOR:
							message = MessageFormat.format( getErrorMessage(), mMinimum, mMaximum, value );
							break;
						case MAXIMUM_VALIDATOR:
							message = MessageFormat.format( getErrorMessage(), mMaximum, value );
							break;
						case MINIMUM_VALIDATOR:
							message = MessageFormat.format( getErrorMessage(), mMinimum, value );
							break;
					}
					break;

				case TYPE_ERROR:
					message = MessageFormat.format( mTypeErrorMessage, value );
					break;

				default:
					return;
			}

			throw new InvalidValueException( message );
		}

	}

	private static class StringLengthValidator
		extends
			com.vaadin.data.validator.StringLengthValidator {

		private String	mMaximumLengthErrorMessage;

		private String	mMinimumLengthErrorMessage;

		public StringLengthValidator( ResourceBundle bundle, int minLength,
				int maxLength, boolean allowNull ) {

			super( "", minLength, maxLength, allowNull );

			if ( bundle == null ) {

				mMaximumLengthErrorMessage = "{1} must not be longer than {0} characters";
				mMinimumLengthErrorMessage = "{1} must not be shorter than {0} characters";
			} else {

				mMaximumLengthErrorMessage = bundle.getString( "javax.faces.validator.LengthValidator.MAXIMUM" );
				mMinimumLengthErrorMessage = bundle.getString( "javax.faces.validator.LengthValidator.MINIMUM" );
			}
		}

		@Override
		public void validate( Object value )
			throws InvalidValueException {

			if ( !isValid( value ) ) {
				String message = "";

				if ( value == null ) {
					message = MessageFormat.format( mMinimumLengthErrorMessage, getMinLength(), "''" );
				} else {

					if ( value.toString().length() < getMinLength() ) {
						message = MessageFormat.format( mMinimumLengthErrorMessage, getMinLength(), value );
					} else if ( value.toString().length() < getMaxLength() ) {
						message = MessageFormat.format( mMinimumLengthErrorMessage, getMaxLength(), value );
					}

				}
				throw new InvalidValueException( message );
			}
		}
	}

}
