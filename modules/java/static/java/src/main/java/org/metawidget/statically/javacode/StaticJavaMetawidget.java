// Metawidget (licensed under LGPL)
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

package org.metawidget.statically.javacode;

import java.util.Set;

import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.StaticWidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;

/**
 * Static Metawidget for Java environments.
 *
 * @author Richard Kennard
 */

public class StaticJavaMetawidget
	extends StaticMetawidget
	implements StaticJavaWidget {

	//
	// Public methods
	//

	/**
	 * Recurse over all children and retrieve the imports they use.
	 *
	 * @return map of prefix and namespace
	 */

	public Set<String> getImports() {

		Set<String> imports = CollectionUtils.newHashSet();
		populateImports( this, imports );
		return imports;
	}

	//
	// Protected methods
	//

	@Override
	protected String getDefaultConfiguration() {

		return ClassUtils.getPackagesAsFolderNames( StaticJavaMetawidget.class ) + "/metawidget-static-javacode-default.xml";
	}

	//
	// Private methods
	//

	private void populateImports( StaticJavaWidget javaWidget, Set<String> imports ) {

		for ( StaticWidget child : javaWidget.getChildren() ) {

			StaticJavaWidget javaChild = (StaticJavaWidget) child;

			if ( javaChild.getImports() != null ) {
				imports.addAll( javaChild.getImports() );
			}

			populateImports( javaChild, imports );
		}
	}
}
