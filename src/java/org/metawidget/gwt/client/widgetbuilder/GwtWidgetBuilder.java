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

package org.metawidget.gwt.client.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.GwtUtils;
import org.metawidget.gwt.client.ui.GwtValueAccessor;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.impl.BaseWidgetBuilder;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * WidgetBuilder for GWT environments.
 * <p>
 * Automatically creates native GWT Widgets, such as <code>TextBox</code> and <code>ListBox</code>,
 * to suit the inspected fields.
 *
 * @author Richard Kennard
 */

public class GwtWidgetBuilder
	extends BaseWidgetBuilder<Widget, GwtMetawidget>
	implements GwtValueAccessor
{
	//
	// Public methods
	//

	@SuppressWarnings( "unchecked" )
	public Object getValue( Widget widget )
	{
		// CheckBox (must come before HasText, because CheckBox extends
		// ButtonBase which implements HasHTML which extends HasText)

		if ( widget instanceof CheckBox )
			return Boolean.valueOf( ( (CheckBox) widget ).isChecked() );

		// HasText

		if ( widget instanceof HasText )
			return ( (HasText) widget ).getText();

		// ListBox

		if ( widget instanceof ListBox )
		{
			ListBox listBox = (ListBox) widget;
			return listBox.getValue( listBox.getSelectedIndex() );
		}

		return null;
	}

	public boolean setValue( Object value, Widget widget )
	{
		// HasText

		if ( widget instanceof HasText )
		{
			( (HasText) widget ).setText( StringUtils.quietValueOf( value ) );
			return true;
		}

		// CheckBox (must come before HasText, because CheckBox extends
		// ButtonBase which implements HasHTML which extends HasText)

		if ( widget instanceof CheckBox )
		{
			( (CheckBox) widget ).setChecked( (Boolean) value );
			return true;
		}

		// ListBox

		if ( widget instanceof ListBox )
		{
			GwtUtils.setListBoxSelectedItem( (ListBox) widget, StringUtils.quietValueOf( value ) );
			return true;
		}

		// Panel (fail gracefully for MASKED fields)

		if ( widget instanceof SimplePanel )
			return true;

		// Not for us

		return false;
	}

	//
	// Protected methods
	//

	@Override
	protected Widget buildReadOnlyWidget( String elementName, Map<String, String> attributes, GwtMetawidget metawidget )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return new Stub();

		// Action

		if ( ACTION.equals( elementName ) )
			return new Stub();

		// Masked (return a Panel, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
			return new SimplePanel();

		String type = attributes.get( TYPE );

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
			return new Label();

		// If no type, fail gracefully with a Label

		if ( type == null || "".equals( type ) )
			return new Label();

		if ( GwtUtils.isPrimitive( type ) || GwtUtils.isPrimitiveWrapper( type ) )
			return new Label();

		if ( String.class.getName().equals( type ) )
			return new Label();

		if ( Date.class.getName().equals( type ) )
			return new Label();

		// Collections

		if ( isCollection( type ) )
			return new Stub();

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return new Label();

		// Nested Metawidget

		return null;
	}

	@Override
	protected Widget buildActiveWidget( String elementName, Map<String, String> attributes, GwtMetawidget metawidget )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return new Stub();

		// Action

		if ( ACTION.equals( elementName ) )
			return new Button( metawidget.getLabelString( attributes ) );

		String type = attributes.get( TYPE );

		// If no type, fail gracefully with a TextBox

		if ( type == null || "".equals( type ) )
			return new TextBox();

		// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
		// Lookup)

		if ( "Boolean".equals( type ) && TRUE.equals( attributes.get( REQUIRED )))
			return new CheckBox();

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
		{
			ListBox listBox = new ListBox();
			listBox.setVisibleItemCount( 1 );

			addListBoxItems( listBox, GwtUtils.fromString( lookup, StringUtils.SEPARATOR_COMMA_CHAR ), GwtUtils.fromString( attributes.get( LOOKUP_LABELS ), StringUtils.SEPARATOR_COMMA_CHAR ), attributes );
			return listBox;
		}

		if ( GwtUtils.isPrimitive( type ) )
		{
			// booleans

			if ( "boolean".equals( type ))
				return new CheckBox();

			// chars

			if ( "char".equals( type ) )
			{
				TextBox textbox = new TextBox();
				textbox.setMaxLength( 1 );

				return textbox;
			}

			// Everything else

			return new TextBox();
		}

		// Strings

		if ( String.class.getName().equals( type ) )
		{
			if ( TRUE.equals( attributes.get( MASKED ) ) )
				return new PasswordTextBox();

			if ( TRUE.equals( attributes.get( LARGE ) ) )
				return new TextArea();

			TextBox textBox = new TextBox();

			String maximumLength = attributes.get( MAXIMUM_LENGTH );

			if ( maximumLength != null && !"".equals( maximumLength ) )
				textBox.setMaxLength( Integer.parseInt( maximumLength ) );

			return textBox;
		}

		// Dates

		if ( Date.class.getName().equals( type ) )
			return new TextBox();

		if ( GwtUtils.isPrimitiveWrapper( type ) )
		{
			// Booleans (are tri-state)

			if ( Boolean.class.getName().equals( type ) )
			{
				ListBox listBox = new ListBox();
				addListBoxItem( listBox, null, null );
				addListBoxItem( listBox, "true", "True" );
				addListBoxItem( listBox, "false", "False" );

				return listBox;
			}

			// Characters

			if ( Character.class.getName().equals( type ) )
			{
				TextBox textbox = new TextBox();
				textbox.setMaxLength( 1 );

				return textbox;
			}

			// Numbers

			return new TextBox();
		}

		// Collections

		if ( isCollection( type ) )
			return new Stub();

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return new TextBox();

		// Nested Metawidget

		return null;
	}

	//
	// Private methods
	//

	/**
	 * Whether the given class name is a Collection. This is a crude, GWT-equivalent of...
	 * <p>
	 * <code>
	 *    Collection.class.isAssignableFrom( ... );
	 * </code>
	 * <p>
	 * ...subclasses may need to override this method if they introduce a new Collection subtype.
	 */

	private boolean isCollection( String className )
	{
		if ( Collection.class.getName().equals( className ) )
			return true;

		if ( List.class.getName().equals( className ) || ArrayList.class.getName().equals( className ) )
			return true;

		if ( Set.class.getName().equals( className ) || HashSet.class.getName().equals( className ) )
			return true;

		if ( Map.class.getName().equals( className ) || HashMap.class.getName().equals( className ) )
			return true;

		return false;
	}

	private void addListBoxItems( ListBox listBox, List<String> values, List<String> labels, Map<String, String> attributes )
	{
		if ( values == null )
			return;

		// Add an empty choice (if nullable, and not required)
		//
		// Note: GWT doesn't seem to be able to set null for the
		// value. It always comes back as String "null"

		String type = attributes.get( TYPE );

		if ( type == null )
		{
			// Type can be null if this lookup was specified by a metawidget-metadata.xml
			// and the type was omitted from the XML. In that case, assume nullable

			addListBoxItem( listBox, "", null );
		}
		else
		{
			if ( !GwtUtils.isPrimitive( type ) && !TRUE.equals( attributes.get( REQUIRED ) ) )
				addListBoxItem( listBox, "", null );
		}

		// See if we're using labels

		if ( labels != null && !labels.isEmpty() && labels.size() != values.size() )
			throw new RuntimeException( "Labels list must be same size as values list" );

		// Add the select items

		for ( int loop = 0, length = values.size(); loop < length; loop++ )
		{
			String value = values.get( loop );
			String label = null;

			if ( labels != null && !labels.isEmpty() )
				label = labels.get( loop );

			addListBoxItem( listBox, value, label );
		}
	}

	private void addListBoxItem( ListBox listBox, String value, String label )
	{
		if ( label != null )
		{
			listBox.addItem( label, value );
			return;
		}

		listBox.addItem( value );
	}
}
