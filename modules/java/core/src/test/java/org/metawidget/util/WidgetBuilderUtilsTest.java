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

package org.metawidget.util;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.propertytype.PropertyTypeInspectionResultConstants.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class WidgetBuilderUtilsTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetBuilderUtils()
		throws Exception {

		// getType

		Map<String, String> attributes = CollectionUtils.newHashMap();
		assertEquals( null, WidgetBuilderUtils.getActualClassOrType( attributes, null ) );
		attributes.put( TYPE, "" );
		assertEquals( null, WidgetBuilderUtils.getActualClassOrType( attributes, null ) );
		attributes.put( TYPE, Set.class.getName() );
		assertEquals( Set.class, WidgetBuilderUtils.getActualClassOrType( attributes, null ) );
		attributes.put( ACTUAL_CLASS, "" );
		assertEquals( Set.class, WidgetBuilderUtils.getActualClassOrType( attributes, null ) );
		attributes.put( ACTUAL_CLASS, HashSet.class.getName() );
		assertEquals( HashSet.class, WidgetBuilderUtils.getActualClassOrType( attributes, null ) );
	}
}
