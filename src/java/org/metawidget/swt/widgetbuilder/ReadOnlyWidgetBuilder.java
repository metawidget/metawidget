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
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtValuePropertyProvider;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * WidgetBuilder for SWT environments.
 * <p>
 * Creates native SWT read-only <code>Controls</code>, such as <code>Labels</code>, to suit the
 * inspected fields.
 *
 * @author Richard Kennard
 */

public class ReadOnlyWidgetBuilder
	implements WidgetBuilder<Control, SwtMetawidget>, SwtValuePropertyProvider
{
	//
	// Public methods
	//

	public String getValueProperty( Control control )
	{
		if ( control instanceof Label )
			return "text";

		return null;
	}

	@Override
	public Control buildWidget( String elementName, Map<String, String> attributes, SwtMetawidget metawidget )
	{
		// Not read-only?

		if ( !WidgetBuilderUtils.isReadOnly( attributes ) )
			return null;

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return new Stub( metawidget, SWT.NONE );

		// Action

		if ( ACTION.equals( elementName ) )
			return new Stub( metawidget, SWT.NONE );

		// Masked (return a Composite, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
			return new Composite( metawidget, SWT.NONE );

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
			return new Label( metawidget, SWT.NONE );

		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, assume a String

		if ( type == null )
			type = String.class.getName();

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz != null )
		{
			// Primitives

			if ( clazz.isPrimitive() )
				return new Label( metawidget, SWT.NONE );

			if ( String.class.equals( clazz ) )
			{
				if ( TRUE.equals( attributes.get( LARGE ) ) )
					return new Text( metawidget, SWT.MULTI | SWT.READ_ONLY );

				return new Label( metawidget, SWT.NONE );
			}

			if ( Date.class.equals( clazz ) )
				return new Label( metawidget, SWT.NONE );

			if ( Boolean.class.equals( clazz ) )
				return new Label( metawidget, SWT.NONE );

			if ( Number.class.isAssignableFrom( clazz ) )
				return new Label( metawidget, SWT.NONE );

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return new Stub( metawidget, SWT.None );
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return new Label( metawidget, SWT.NONE );

		// Nested Metawidget

		return null;
	}
}
