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

package org.metawidget.swing.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.swing.SwingMetawidget;

/**
 * @author Richard Kennard
 */

public class EnabledProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testProcessor()
		throws Exception {

		SwingMetawidget metawidget = new SwingMetawidget();
		EnabledProcessor processor = new EnabledProcessor();

		JTextField component = new JTextField();
		processor.processWidget( component, PROPERTY, null, metawidget );
		assertTrue( component.isEnabled() );

		metawidget.setEnabled( false );
		processor.processWidget( component, PROPERTY, null, metawidget );
		assertTrue( !component.isEnabled() );
	}
}
