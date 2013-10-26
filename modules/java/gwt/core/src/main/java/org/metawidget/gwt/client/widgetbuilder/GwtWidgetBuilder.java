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

package org.metawidget.gwt.client.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.GwtUtils;
import org.metawidget.gwt.client.ui.GwtValueAccessor;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * WidgetBuilder for GWT environments.
 * <p>
 * Creates native GWT Widgets, such as <code>TextBox</code> and <code>ListBox</code>, to suit the
 * inspected fields.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class GwtWidgetBuilder
	implements WidgetBuilder<Widget, GwtMetawidget>, GwtValueAccessor {

	//
	// Public methods
	//

	public Object getValue( Widget widget ) {

		// CheckBox (must come before HasText, because CheckBox extends
		// ButtonBase which implements HasHTML which extends HasText)

		if ( widget instanceof CheckBox ) {
			return ( (CheckBox) widget ).getValue();
		}

		// HasText

		if ( widget instanceof HasText ) {
			return ( (HasText) widget ).getText();
		}

		// ListBox

		if ( widget instanceof ListBox ) {
			ListBox listBox = (ListBox) widget;
			return listBox.getValue( listBox.getSelectedIndex() );
		}

		return null;
	}

	public boolean setValue( Widget widget, Object value ) {

		// CheckBox (must come before HasText, because CheckBox extends
		// ButtonBase which implements HasHTML which extends HasText)

		if ( widget instanceof CheckBox ) {
			( (CheckBox) widget ).setValue( (Boolean) value );
			return true;
		}

		// HasText

		if ( widget instanceof HasText ) {
			( (HasText) widget ).setText( StringUtils.quietValueOf( value ) );
			return true;
		}

		// ListBox

		if ( widget instanceof ListBox ) {
			GwtUtils.setListBoxSelectedItem( (ListBox) widget, StringUtils.quietValueOf( value ) );
			return true;
		}

		// Not for us

		return false;
	}

	public Widget buildWidget( String elementName, Map<String, String> attributes, GwtMetawidget metawidget ) {

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return new Stub();
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			return new Button( metawidget.getLabelString( attributes ) );
		}

		String type = GwtUtils.getActualClassOrType( attributes );

		// If no type, assume a String

		if ( type == null ) {
			type = String.class.getName();
		}

		// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
		// Lookup)

		if ( "Boolean".equals( type ) && TRUE.equals( attributes.get( REQUIRED ) ) ) {
			return new CheckBox();
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			ListBox listBox = new ListBox();
			listBox.setVisibleItemCount( 1 );

			addListBoxItems( listBox, GwtUtils.fromString( lookup, StringUtils.SEPARATOR_COMMA_CHAR ), GwtUtils.fromString( attributes.get( LOOKUP_LABELS ), StringUtils.SEPARATOR_COMMA_CHAR ), attributes );
			return listBox;
		}

		if ( GwtUtils.isPrimitive( type ) ) {
			// booleans

			if ( "boolean".equals( type ) ) {
				return new CheckBox();
			}

			// chars

			if ( "char".equals( type ) ) {
				TextBox textbox = new TextBox();
				textbox.setMaxLength( 1 );

				return textbox;
			}

			// Everything else

			return new TextBox();
		}

		// Strings

		if ( String.class.getName().equals( type ) ) {
			if ( TRUE.equals( attributes.get( MASKED ) ) ) {
				return new PasswordTextBox();
			}

			if ( TRUE.equals( attributes.get( LARGE ) ) ) {
				return new TextArea();
			}

			TextBox textBox = new TextBox();

			String maximumLength = attributes.get( MAXIMUM_LENGTH );

			if ( maximumLength != null && !"".equals( maximumLength ) ) {
				textBox.setMaxLength( Integer.parseInt( maximumLength ) );
			}

			return textBox;
		}

		// Dates

		if ( Date.class.getName().equals( type ) ) {
			return new TextBox();
		}

		if ( GwtUtils.isPrimitiveWrapper( type ) ) {
			// Characters

			if ( Character.class.getName().equals( type ) ) {
				TextBox textbox = new TextBox();
				textbox.setMaxLength( 1 );

				return textbox;
			}

			// Numbers

			return new TextBox();
		}

		// Collections

		if ( GwtUtils.isCollection( type ) ) {
			return new Stub();
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return new TextBox();
		}

		// Nested Metawidget

		return null;
	}

	//
	// Private methods
	//

	private void addListBoxItems( ListBox listBox, List<String> values, List<String> labels, Map<String, String> attributes ) {

		if ( values == null ) {
			return;
		}

		// Empty option
		//
		// Note: GWT doesn't seem to be able to set null for the
		// value. It always comes back as String "null"

		if ( GwtUtils.needsEmptyLookupItem( attributes ) ) {
			addListBoxItem( listBox, "", null );
		}

		// See if we're using labels

		if ( labels != null && !labels.isEmpty() && labels.size() != values.size() ) {
			throw new RuntimeException( "Labels list must be same size as values list" );
		}

		// Add the select items

		for ( int loop = 0, length = values.size(); loop < length; loop++ ) {
			String value = values.get( loop );
			String label = null;

			if ( labels != null && !labels.isEmpty() ) {
				label = labels.get( loop );
			}

			addListBoxItem( listBox, value, label );
		}
	}

	private void addListBoxItem( ListBox listBox, String value, String label ) {

		if ( label != null ) {
			listBox.addItem( label, value );
			return;
		}

		listBox.addItem( value );
	}
}
