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

package org.metawidget.jsp.tagext.html.struts.subclass;

import junit.framework.TestCase;

import org.metawidget.jsp.tagext.html.struts.StrutsMetawidgetTag;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StrutsMetawidgetTagSubclassTest
	extends TestCase {

	//
	// Public methods
	//

	/**
	 * Test default configuration is tied to the superclass, not the subclass.
	 */

	public static void testDefaultConfiguration() {

		assertEquals( "/org/metawidget/jsp/tagext/html/struts/metawidget-struts-default.xml", new StaticStrutsMetawidgetTagSubclass().getDefaultConfiguration() );
	}

	//
	// Inner class
	//

	public static class StaticStrutsMetawidgetTagSubclass
		extends StrutsMetawidgetTag {

		@Override
		public String getDefaultConfiguration() {

			return super.getDefaultConfiguration();
		}
	}
}
