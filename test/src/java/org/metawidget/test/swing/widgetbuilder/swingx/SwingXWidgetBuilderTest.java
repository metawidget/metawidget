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

package org.metawidget.test.swing.widgetbuilder.swingx;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.jdesktop.swingx.JXDatePicker;
import org.metawidget.swing.widgetbuilder.swingx.SwingXWidgetBuilder;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class SwingXWidgetBuilderTest
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public SwingXWidgetBuilderTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testWidgetBuilder()
		throws Exception
	{
		SwingXWidgetBuilder widgetBuilder = new SwingXWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ));

		attributes.put( TYPE, String.class.getName() );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ));

		attributes.put( TYPE, Date.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, null ) instanceof JXDatePicker );
	}
}
