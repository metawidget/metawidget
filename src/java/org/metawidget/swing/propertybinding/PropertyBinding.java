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

package org.metawidget.swing.propertybinding;

import java.awt.Component;
import java.util.Map;

import org.metawidget.swing.SwingMetawidget;

/**
 * Base class for automatic, two-way binding of properties.
 * <p>
 * Swing does not define a <code>JComponent</code> to <code>Object</code> mapping mechanism like
 * other UI frameworks (eg. Java Server Faces). However, a couple of third party alternatives exist
 * and Metawidget supports these.
 * <p>
 * Implementations need not be Thread-safe.
 *
 * @author Richard Kennard
 */

public abstract class PropertyBinding
{
	//
	// Private members
	//

	private SwingMetawidget	mMetawidget;

	//
	// Constructor
	//

	protected PropertyBinding( SwingMetawidget metawidget )
	{
		mMetawidget = metawidget;
	}

	//
	// Public methods
	//

	/**
	 * Bind the given Widget to the given 'path of names' within the source Object.
	 *
	 * @param widget
	 *            the widget to bind to
	 * @param attributes
	 *            metadata of the property being bound
	 * @param path
	 *            path to bind to (can be parsed using PathUtils.parsePath)
	 */

	public abstract void bind( Component component, Map<String, String> attributes, String path );

	/**
	 * Update bound values in the Components from the source Object.
	 */

	public abstract void rebind();

	/**
	 * Save bound values from the Components back to the source Object.
	 */

	public abstract void save();

	/**
	 * Convert the given String into the given type, if necessary. If no
	 * conversion is required, return the original String.
	 * <p>
	 * Used when adding lookup values to a <code>JComboBox</code>. The lookup
	 * values as specified by <code>UiLookup</code> or <code>XmlInspector</code>
	 * are always Strings, so may need converting to the same type as the
	 * property.
	 */

	public abstract <T> T convertFromString( String value, Class<T> type );

	public void unbind()
	{
		// Do nothing by default
	}

	//
	// Protected methods
	//

	protected SwingMetawidget getMetawidget()
	{
		return mMetawidget;
	}
}
