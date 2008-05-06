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

package org.metawidget.inspector.javabean;

import org.metawidget.inspector.impl.AbstractPojoInspectorConfig;

/**
 * Configures a JavaBeanInspector prior to use. Once instantiated, Inspectors are immutable.
 *
 * @author Richard Kennard
 */

public class JavaBeanInspectorConfig
	extends AbstractPojoInspectorConfig
{
	//
	//
	// Private members
	//
	//

	private String[]	mExcludeProperties	= new String[] { "propertyChangeListeners", "vetoableChangeListeners" };

	private Class<?>[]	mExcludeReturnTypes	= new Class<?>[] { Class.class };

	private boolean		mSorted			= true;

	//
	//
	// Public methods
	//
	//

	public void setExcludeProperties( String... excludeProperties )
	{
		mExcludeProperties = excludeProperties;
	}

	public String[] getExcludeProperties()
	{
		return mExcludeProperties;
	}

	public void setExcludeReturnTypes( Class<?>... excludeReturnTypes )
	{
		mExcludeReturnTypes = excludeReturnTypes;
	}

	public Class<?>[] getExcludeReturnTypes()
	{
		return mExcludeReturnTypes;
	}

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
