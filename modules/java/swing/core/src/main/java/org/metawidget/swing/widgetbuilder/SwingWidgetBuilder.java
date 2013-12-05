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

package org.metawidget.swing.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Component;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.JTextComponent;

import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.SwingValuePropertyProvider;
import org.metawidget.swing.widgetprocessor.binding.BindingConverter;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * WidgetBuilder for Swing environments.
 * <p>
 * Creates native Swing <code>JComponents</code>, such as <code>JTextField</code> and
 * <code>JComboBox</code>, to suit the inspected fields.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwingWidgetBuilder
	implements WidgetBuilder<JComponent, SwingMetawidget>, SwingValuePropertyProvider {

	//
	// Private members
	//

	private PropertyStyle	mPropertyStyle;

	//
	// Constructor
	//

	public SwingWidgetBuilder() {

		this( new SwingWidgetBuilderConfig() );
	}

	public SwingWidgetBuilder( SwingWidgetBuilderConfig config ) {

		mPropertyStyle = config.getPropertyStyle();
	}

	//
	// Public methods
	//

	public String getValueProperty( Component component ) {

		if ( component instanceof JComboBox ) {
			return "selectedItem";
		}

		if ( component instanceof JTextComponent ) {
			return "text";
		}

		if ( component instanceof JSpinner ) {
			return "value";
		}

		if ( component instanceof JSlider ) {
			return "value";
		}

		if ( component instanceof JCheckBox ) {
			return "selected";
		}

		return null;
	}

	public JComponent buildWidget( String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return new Stub();
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			return new JButton( metawidget.getLabelString( attributes ) );
		}

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, String.class );

		// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
		// Lookup)

		if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) ) {
			return new JCheckBox();
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			JComboBox comboBox = new JComboBox();

			// Add an empty choice (if nullable, and not required)

			if ( WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
				comboBox.addItem( null );
			}

			List<String> values = CollectionUtils.fromString( lookup );
			List<Object> convertedValues = CollectionUtils.newArrayList();
			BindingConverter converter = metawidget.getWidgetProcessor( BindingConverter.class );

			for ( String value : values ) {
				// Convert (if supported)

				Object convertedValue;

				if ( converter == null ) {
					convertedValue = value;
				} else {
					convertedValue = converter.convertFromString( value, clazz );
				}

				comboBox.addItem( convertedValue );
				convertedValues.add( convertedValue );
			}

			// May have alternate labels

			String lookupLabels = attributes.get( LOOKUP_LABELS );

			if ( lookupLabels != null && !"".equals( lookupLabels ) ) {
				Map<Object, String> labelsMap = CollectionUtils.newHashMap( convertedValues, CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ) );
				comboBox.setEditor( new LookupComboBoxEditor( labelsMap ) );
				comboBox.setRenderer( new LookupComboBoxRenderer( labelsMap ) );
			}

			return comboBox;
		}

		if ( clazz != null ) {
			// Primitives

			if ( clazz.isPrimitive() ) {
				// booleans

				if ( boolean.class.equals( clazz ) ) {
					return new JCheckBox();
				}

				// chars

				if ( char.class.equals( clazz ) ) {
					return new JTextField();
				}

				// Ranged

				String minimumValue = attributes.get( MINIMUM_VALUE );
				String maximumValue = attributes.get( MAXIMUM_VALUE );

				if ( minimumValue != null && !"".equals( minimumValue ) && maximumValue != null && !"".equals( maximumValue ) ) {
					JSlider slider = new JSlider();
					slider.setMinimum( Integer.parseInt( minimumValue ) );
					slider.setValue( slider.getMinimum() );
					slider.setMaximum( Integer.parseInt( maximumValue ) );

					return slider;
				}

				// Not-ranged

				Comparable<Number> minimum;
				Comparable<Number> maximum;

				if ( minimumValue != null && !"".equals( minimumValue ) ) {
					@SuppressWarnings( "unchecked" )
					Comparable<Number> comparable = (Comparable<Number>) ClassUtils.parseNumber( clazz, minimumValue );
					minimum = comparable;
				} else {
					@SuppressWarnings( "unchecked" )
					Comparable<Number> comparable = (Comparable<Number>) ClassUtils.getNumberMinValue( clazz );
					minimum = comparable;
				}

				if ( maximumValue != null && !"".equals( maximumValue ) ) {
					@SuppressWarnings( "unchecked" )
					Comparable<Number> comparable = (Comparable<Number>) ClassUtils.parseNumber( clazz, maximumValue );
					maximum = comparable;
				} else {
					@SuppressWarnings( "unchecked" )
					Comparable<Number> comparable = (Comparable<Number>) ClassUtils.getNumberMaxValue( clazz );
					maximum = comparable;
				}

				// Configurable step

				Number stepSize;

				if ( attributes.containsKey( MAXIMUM_FRACTIONAL_DIGITS ) ) {
					stepSize = Math.pow( 10, -Integer.parseInt( attributes.get( MAXIMUM_FRACTIONAL_DIGITS ) ) );
				} else if ( float.class.equals( clazz ) || Float.class.equals( clazz ) ) {
					stepSize = 0.1f;
				} else if ( double.class.equals( clazz ) || Double.class.equals( clazz ) ) {
					stepSize = 0.1f;
				} else {
					stepSize = 1;
				}

				// Note it is very important we set the initial value of the JSpinner to the same
				// type as the property it maps to (eg. float or double, int or long).

				Number value = (Number) ClassUtils.parseNumber( clazz, "0" );

				if ( minimum.compareTo( value ) > 0 ) {
					value = (Number) minimum;
				} else if ( maximum.compareTo( value ) < 0 ) {
					value = (Number) maximum;
				}

				JSpinner spinner = new JSpinner( new SpinnerNumberModel( value, minimum, maximum, stepSize ) );

				// By default, a JSpinner calls setColumns. For numbers like Integer.MAX_VALUE and
				// Double.MAX_VALUE, this can be very large and mess up the layout. Here, we reset
				// setColumns to 0.

				JSpinner.NumberEditor editor = (JSpinner.NumberEditor) spinner.getEditor();
				editor.getTextField().setColumns( 0 );

				DecimalFormat format = editor.getFormat();

				if ( attributes.containsKey( MINIMUM_FRACTIONAL_DIGITS ) ) {
					format.setMinimumFractionDigits( Integer.parseInt( attributes.get( MINIMUM_FRACTIONAL_DIGITS ) ) );
				}

				if ( attributes.containsKey( MAXIMUM_FRACTIONAL_DIGITS ) ) {
					format.setMaximumFractionDigits( Integer.parseInt( attributes.get( MAXIMUM_FRACTIONAL_DIGITS ) ) );
				}

				if ( attributes.containsKey( MINIMUM_INTEGER_DIGITS ) ) {
					format.setMinimumIntegerDigits( Integer.parseInt( attributes.get( MINIMUM_INTEGER_DIGITS ) ) );
				}

				if ( attributes.containsKey( MAXIMUM_INTEGER_DIGITS ) ) {
					format.setMaximumIntegerDigits( Integer.parseInt( attributes.get( MAXIMUM_INTEGER_DIGITS ) ) );
				}

				return spinner;
			}

			// Strings

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( MASKED ) ) ) {
					return new JPasswordField();
				}

				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					JTextArea textarea = new JTextArea();

					// Since we know we are dealing with Strings, we consider
					// word-wrapping a sensible default

					textarea.setLineWrap( true );
					textarea.setWrapStyleWord( true );

					// We also consider 2 rows a sensible default, so that the
					// JTextArea is always distinguishable from a JTextField

					textarea.setRows( 2 );
					return new JScrollPane( textarea );
				}

				return new JTextField();
			}

			// Characters

			if ( Character.class.isAssignableFrom( clazz ) ) {
				return new JTextField();
			}

			// Dates

			if ( Date.class.equals( clazz ) ) {
				return new JTextField();
			}

			// Numbers
			//
			// Note: we use a text field, not a JSpinner or JSlider, because
			// BeansBinding gets upset at doing 'setValue( null )' if the Integer
			// is null. We can still use JSpinner/JSliders for primitives, though.

			if ( Number.class.isAssignableFrom( clazz ) ) {
				return new JTextField();
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				return createTable( attributes, metawidget );
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return new JTextField();
		}

		// Nested Metawidget

		return null;
	}

	//
	// Protected methods
	//

	protected JComponent createTable( Map<String, String> attributes, SwingMetawidget metawidget ) {

		String componentType = attributes.get( PARAMETERIZED_TYPE );
		String inspectedType = metawidget.inspect( null, componentType, (String[]) null );

		// Determine columns

		List<String> columns = CollectionUtils.newArrayList();

		if ( inspectedType != null ) {
			Element root = XmlUtils.documentFromString( inspectedType ).getDocumentElement();
			NodeList elements = root.getFirstChild().getChildNodes();

			for ( int loop = 0, length = elements.getLength(); loop < length; loop++ ) {

				Node node = elements.item( loop );
				columns.add( metawidget.getLabelString( XmlUtils.getAttributesAsMap( node ) ) );
			}
		}

		// Fetch the data (if any)

		Collection<?> collection = null;

		if ( metawidget.getToInspect() != null && mPropertyStyle != null ) {
			String type = metawidget.getToInspect().getClass().getName();
			Map<String, Property> properties = mPropertyStyle.getProperties( type );

			if ( properties != null ) {
				Property property = properties.get( attributes.get( NAME ) );

				if ( property != null ) {
					collection = (Collection<?>) property.read( metawidget.getToInspect() );
				}
			}
		}

		// Return the JTable

		@SuppressWarnings( { "unchecked", "rawtypes" } )
		CollectionTableModel<?> tableModel = new CollectionTableModel( collection, columns );

		return new JScrollPane( new JTable( tableModel ) );
	}

	//
	// Inner class
	//

	/**
	 * Editor for ComboBox whose values use a lookup.
	 */

	private static class LookupComboBoxEditor
		extends BasicComboBoxEditor {

		//
		// Private members
		//

		private Map<Object, String>	mLookups;

		//
		// Constructor
		//

		public LookupComboBoxEditor( Map<Object, String> lookups ) {

			if ( lookups == null ) {
				throw new NullPointerException( "lookups" );
			}

			mLookups = lookups;
		}

		//
		// Public methods
		//

		@Override
		public void setItem( Object item ) {

			super.setItem( mLookups.get( item ) );
		}
	}

	/**
	 * Renderer for ComboBox whose values use a lookup.
	 */

	private static class LookupComboBoxRenderer
		extends BasicComboBoxRenderer {

		//
		// Private members
		//

		private Map<Object, String>	mLookups;

		//
		// Constructor
		//

		public LookupComboBoxRenderer( Map<Object, String> lookups ) {

			if ( lookups == null ) {
				throw new NullPointerException( "lookups" );
			}

			mLookups = lookups;
		}

		//
		// Public methods
		//

		@Override
		public Component getListCellRendererComponent( JList list, Object value, int index, boolean selected, boolean hasFocus ) {

			Component component = super.getListCellRendererComponent( list, value, index, selected, hasFocus );

			String lookup = mLookups.get( value );

			if ( lookup != null ) {
				( (JLabel) component ).setText( lookup );
			}

			return component;
		}
	}
}
