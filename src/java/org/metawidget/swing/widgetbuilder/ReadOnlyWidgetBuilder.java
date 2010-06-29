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
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.SwingValuePropertyProvider;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * WidgetBuilder for Swing environments.
 * <p>
 * Creates native Swing read-only <code>JComponents</code>, such as <code>JLabels</code>, to suit
 * the inspected fields.
 *
 * @author Richard Kennard
 */

public class ReadOnlyWidgetBuilder
	implements WidgetBuilder<JComponent, SwingMetawidget>, SwingValuePropertyProvider {

	//
	// Public methods
	//

	public String getValueProperty( Component component ) {

		if ( component instanceof JLabel ) {
			return "text";
		}

		return null;
	}

	@Override
	public JComponent buildWidget( String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

		// Not read-only?

		if ( !WidgetBuilderUtils.isReadOnly( attributes ) ) {
			return null;
		}

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return new Stub();
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			return new Stub();
		}

		// Masked (return a JPanel, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) ) {
			return new JPanel();
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			// May have alternate labels

			String lookupLabels = attributes.get( LOOKUP_LABELS );

			if ( lookupLabels != null && !"".equals( lookupLabels ) ) {
				return new LookupLabel( SwingWidgetBuilderUtils.getLabelsMap( CollectionUtils.fromString( lookup ), CollectionUtils.fromString( lookupLabels ) ) );
			}

			return new JLabel();
		}

		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, assume a String

		if ( type == null ) {
			type = String.class.getName();
		}

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz != null ) {
			// Primitives

			if ( clazz.isPrimitive() ) {
				return new JLabel();
			}

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					// Do not use a JLabel: JLabels do not support carriage returns like JTextAreas
					// do, so a multi-line JTextArea formats to a single line JLabel. Instead use
					// a non-editable JTextArea within a borderless JScrollPane

					JTextArea textarea = new JTextArea();

					// Since we know we are dealing with Strings, we consider
					// word-wrapping a sensible default

					textarea.setLineWrap( true );
					textarea.setWrapStyleWord( true );
					textarea.setEditable( false );

					// We also consider 2 rows a sensible default, so that the
					// read-only JTextArea is always distinguishable from a JLabel

					textarea.setRows( 2 );
					JScrollPane scrollPane = new JScrollPane( textarea );
					scrollPane.setBorder( null );

					return scrollPane;
				}

				return new JLabel();
			}

			if ( Date.class.equals( clazz ) ) {
				return new JLabel();
			}

			if ( Boolean.class.equals( clazz ) ) {
				return new JLabel();
			}

			if ( Number.class.isAssignableFrom( clazz ) ) {
				return new JLabel();
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				return new Stub();
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return new JLabel();
		}

		// Nested Metawidget

		return null;
	}
}
