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

package org.metawidget.swt.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtValuePropertyProvider;
import org.metawidget.swt.widgetprocessor.binding.BindingConverter;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;

/**
 * WidgetBuilder for SWT environments.
 * <p>
 * Creates native SWT <code>Controls</code>, such as <code>Text</code> and <code>Combo</code>, to
 * suit the inspected fields.
 *
 * @author Richard Kennard
 */

public class SwtWidgetBuilder
	implements WidgetBuilder<Control, SwtMetawidget>, SwtValuePropertyProvider {

	//
	// Public methods
	//

	public String getValueProperty( Control control ) {

		if ( control instanceof Text || control instanceof Combo ) {
			return "text";
		}

		if ( control instanceof Spinner || control instanceof Scale || control instanceof Button ) {
			return "selection";
		}

		return null;
	}

	public Control buildWidget( String elementName, Map<String, String> attributes, SwtMetawidget metawidget ) {

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return new Stub( metawidget.getCurrentLayoutComposite(), SWT.NONE );
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			Button button = new Button( metawidget.getCurrentLayoutComposite(), SWT.NONE );
			button.setText( metawidget.getLabelString( attributes ) );

			return button;
		}

		// Lookup the Class

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, String.class );

		// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
		// Lookup)

		if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) ) {
			return new Button( metawidget.getCurrentLayoutComposite(), SWT.CHECK );
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			Combo comboDropDown = new Combo( metawidget.getCurrentLayoutComposite(), SWT.READ_ONLY );

			// Add an empty choice (if nullable, and not required)

			if ( WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
				comboDropDown.add( "" );
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

				comboDropDown.add( String.valueOf( convertedValue ) );
			}

			// Vanilla SWT doesn't support differing Combo values and labels (JFace does)

			return comboDropDown;
		}

		if ( clazz != null ) {
			// Primitives

			if ( clazz.isPrimitive() ) {
				// booleans

				if ( boolean.class.equals( clazz ) ) {
					return new Button( metawidget.getCurrentLayoutComposite(), SWT.CHECK );
				}

				// chars

				if ( char.class.equals( clazz )) {
					return new Text( metawidget.getCurrentLayoutComposite(), SWT.BORDER );
				}

				// Ranged

				String minimumValue = attributes.get( MINIMUM_VALUE );
				String maximumValue = attributes.get( MAXIMUM_VALUE );

				if ( minimumValue != null && !"".equals( minimumValue ) && maximumValue != null && !"".equals( maximumValue ) ) {
					Scale scale = new Scale( metawidget.getCurrentLayoutComposite(), SWT.NONE );
					scale.setMinimum( Integer.parseInt( minimumValue ) );
					scale.setSelection( scale.getMinimum() );
					scale.setMaximum( Integer.parseInt( maximumValue ) );

					return scale;
				}

				// Not-ranged

				if ( byte.class.equals( clazz ) || short.class.equals( clazz ) || int.class.equals( clazz )) {

					int minimum;
					int maximum;

					if ( minimumValue != null && !"".equals( minimumValue ) ) {
						minimum = Integer.parseInt( minimumValue );
					} else {
						minimum = ((Number) ClassUtils.getNumberMinValue( clazz )).intValue();
					}

					if ( maximumValue != null && !"".equals( maximumValue ) ) {
						maximum = Integer.parseInt( maximumValue );
					} else {
						maximum = ((Number) ClassUtils.getNumberMaxValue( clazz )).intValue();
					}

					int selection = Math.max( Math.min( 0, maximum ), minimum );

					Spinner spinner = new Spinner( metawidget.getCurrentLayoutComposite(), SWT.BORDER );
					spinner.setMinimum( minimum );
					spinner.setMaximum( maximum );
					spinner.setSelection( selection );

					return spinner;

				} else if ( long.class.equals( clazz ) ) {
					return new Text( metawidget.getCurrentLayoutComposite(), SWT.BORDER );
				} else if ( float.class.equals( clazz ) ) {
					return new Text( metawidget.getCurrentLayoutComposite(), SWT.BORDER );
				} else if ( double.class.equals( clazz ) ) {
					return new Text( metawidget.getCurrentLayoutComposite(), SWT.BORDER );
				}

				throw WidgetBuilderException.newException( "Unknown primitive " + clazz );
			}

			// Strings

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( MASKED ) ) ) {
					return new Text( metawidget.getCurrentLayoutComposite(), SWT.PASSWORD | SWT.BORDER );
				}

				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					return new Text( metawidget.getCurrentLayoutComposite(), SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP );
				}

				return new Text( metawidget.getCurrentLayoutComposite(), SWT.BORDER );
			}

			// Characters

			if ( Character.class.isAssignableFrom( clazz )) {
				return new Text( metawidget.getCurrentLayoutComposite(), SWT.BORDER );
			}

			// Dates

			if ( Date.class.equals( clazz ) ) {
				return new Text( metawidget.getCurrentLayoutComposite(), SWT.BORDER );
			}

			// Numbers
			//
			// Note: we use a text field, not a Spinner or Scale, because we need to be able to
			// convey 'null'

			if ( Number.class.isAssignableFrom( clazz ) ) {
				return new Text( metawidget.getCurrentLayoutComposite(), SWT.BORDER );
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				return new Stub( metawidget.getCurrentLayoutComposite(), SWT.NONE );
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return new Text( metawidget.getCurrentLayoutComposite(), SWT.BORDER );
		}

		// Nested Metawidget

		return null;
	}
}
