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

package org.metawidget.statically;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface StaticWidget {

	//
	// Methods
	//

	/**
	 * @return	the list of this widget's children. Never null
	 */

	List<StaticWidget> getChildren();

	void write( Writer writer )
		throws IOException;

	/**
	 * General-purpose storage area, rather like <code>JComponent.putClientProperty</code>.
	 */

	void putClientProperty( Object key, Object value );

	/**
	 * General-purpose storage area, rather like <code>JComponent.getClientProperty</code>.
	 */

	<T> T getClientProperty( Object key );
}
