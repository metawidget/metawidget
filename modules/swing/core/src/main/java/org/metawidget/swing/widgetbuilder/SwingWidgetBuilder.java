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

package org.metawidget.swing.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Component;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.JTextComponent;

import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.SwingValuePropertyProvider;
import org.metawidget.swing.widgetprocessor.binding.BindingConverter;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * WidgetBuilder for Swing environments.
 * <p>
 * Creates native Swing <code>JComponents</code>, such as <code>JTextField</code> and
 * <code>JComboBox</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

public class SwingWidgetBuilder
	implements WidgetBuilder<JComponent, SwingMetawidget>, SwingValuePropertyProvider {

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

		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, assume a String

		if ( type == null ) {
			type = String.class.getName();
		}

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

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
			}

			// May have alternate labels

			String lookupLabels = attributes.get( LOOKUP_LABELS );

			if ( lookupLabels != null && !"".equals( lookupLabels ) ) {
				Map<String, String> labelsMap = CollectionUtils.newHashMap( values, CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ) );
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

				JSpinner spinner = new JSpinner();

				if ( byte.class.equals( clazz ) ) {
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

					setSpinnerModel( spinner, value, minimum, maximum, (byte) 1 );
				} else if ( short.class.equals( clazz ) ) {
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

					setSpinnerModel( spinner, value, minimum, maximum, (short) 1 );
				} else if ( int.class.equals( clazz ) ) {
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

					setSpinnerModel( spinner, value, minimum, maximum, 1 );
				} else if ( long.class.equals( clazz ) ) {
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

					setSpinnerModel( spinner, value, minimum, maximum, (long) 1 );
				} else if ( float.class.equals( clazz ) ) {
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

					// Configurable step

					float stepSize;

					if ( attributes.containsKey( MAXIMUM_FRACTIONAL_DIGITS ) ) {
						stepSize = (float) Math.pow( 10, -Integer.parseInt( attributes.get( MAXIMUM_FRACTIONAL_DIGITS ) ) );
					} else {
						stepSize = 0.1f;
					}

					setSpinnerModel( spinner, value, minimum, maximum, stepSize );
				} else if ( double.class.equals( clazz ) ) {
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

					// Configurable step

					double stepSize;

					if ( attributes.containsKey( MAXIMUM_FRACTIONAL_DIGITS ) ) {
						stepSize = (float) Math.pow( 10, -Integer.parseInt( attributes.get( MAXIMUM_FRACTIONAL_DIGITS ) ) );
					} else {
						stepSize = 0.1d;
					}

					setSpinnerModel( spinner, value, minimum, maximum, stepSize );
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
				return new Stub();
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
	// Private methods
	//

	/**
	 * Sets the JSpinner model.
	 * <p>
	 * By default, a JSpinner calls <code>setColumns</code> upon <code>setModel</code>. For numbers
	 * like <code>Integer.MAX_VALUE</code> and <code>Double.MAX_VALUE</code>, this can be very large
	 * and mess up the layout. Here, we reset <code>setColumns</code> to 0.
	 * <p>
	 * Note it is very important we set the initial value of the <code>JSpinner</code> to the same
	 * type as the property it maps to (eg. float or double, int or long).
	 */

	private void setSpinnerModel( JSpinner spinner, Number value, Comparable<? extends Number> minimum, Comparable<? extends Number> maximum, Number stepSize ) {

		spinner.setModel( new SpinnerNumberModel( value, minimum, maximum, stepSize ) );
		( (JSpinner.DefaultEditor) spinner.getEditor() ).getTextField().setColumns( 0 );
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

		private Map<String, String>	mLookups;

		//
		// Constructor
		//

		public LookupComboBoxEditor( Map<String, String> lookups ) {

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

		private Map<String, String>	mLookups;

		//
		// Constructor
		//

		public LookupComboBoxRenderer( Map<String, String> lookups ) {

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
