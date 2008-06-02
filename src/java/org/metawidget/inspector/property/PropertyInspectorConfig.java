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

package org.metawidget.inspector.property;

import org.metawidget.inspector.impl.AbstractPropertyInspectorConfig;

/**
 * Configures a JavaBeanInspector prior to use. Once instantiated, Inspectors are immutable.
 *
 * @author Richard Kennard
 */

public class PropertyInspectorConfig
	extends AbstractPropertyInspectorConfig
{
	//
	//
	// Private members
	//
	//

	private boolean		mSorted			= true;

	//
	//
	// Public methods
	//
	//

	/**
	 * Whether to sort the JavaBean properties alphabetically.
	 * <p>
	 * The Java Language Specification does not retain field ordering information within class
	 * files, so JavaBeanInspector sorts them alphabetically for consistency - both during unit
	 * tests and for identifying CompositeInspector-merging related problems.
	 */

	public void setSorted( boolean sorted )
	{
		mSorted = sorted;
	}

	boolean isSorted()
	{
		return mSorted;
	}
}
