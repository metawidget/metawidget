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

package org.metawidget.android.widget.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.util.CollectionUtils;

import android.view.View;
import android.widget.EditText;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class DisabledAttributeProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testProcessor()
		throws Exception {

		AndroidMetawidget metawidget = new AndroidMetawidget( null );
		
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( DISABLED, TRUE );
		
		View view = new EditText( null );
		assertTrue( view.isEnabled() );
		
		DisabledAttributeProcessor processor = new DisabledAttributeProcessor();
		assertEquals( view, processor.processWidget( view, PROPERTY, attributes, metawidget ));		
		assertTrue( !view.isEnabled() );
	}
}
