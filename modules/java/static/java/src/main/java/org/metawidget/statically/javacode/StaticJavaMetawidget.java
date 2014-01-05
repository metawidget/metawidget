// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.statically.javacode;

import java.util.Set;

import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.StaticWidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;

/**
 * Static Metawidget for Java environments.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
