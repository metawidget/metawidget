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

package org.metawidget.gwt.client.widgetbuilder.impl;

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
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * WidgetBuilder for read-only widgets in GWT environments.
 *
 * @author Richard Kennard
 */

public class ReadOnlyWidgetBuilder
	implements WidgetBuilder<Widget, GwtMetawidget>, GwtValueAccessor
{
	//
	// Public methods
	//

	public Object getValue( Widget widget )
	{
		// HasText

		if ( widget instanceof HasText )
			return ( (HasText) widget ).getText();

		return null;
	}

	public boolean setValue( Widget widget, Object value )
	{
		// HasText

		if ( widget instanceof HasText )
		{
			( (HasText) widget ).setText( StringUtils.quietValueOf( value ) );
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

	public Widget buildWidget( String elementName, Map<String, String> attributes, GwtMetawidget metawidget )
	{
		// Not read-only?

		if ( !WidgetBuilderUtils.isReadOnly( attributes ))
			return null;

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return new Stub();

		// Action

		if ( ACTION.equals( elementName ) )
			return new Stub();

		// Masked (return a Panel, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
			return new SimplePanel();

		String type = GwtUtils.getActualClassOrType( attributes );

		// If no type, assume a String

		if ( type == null )
			type = String.class.getName();

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
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
}
