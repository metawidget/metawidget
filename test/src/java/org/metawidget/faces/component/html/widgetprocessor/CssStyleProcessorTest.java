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

package org.metawidget.faces.component.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;

import junit.framework.TestCase;

import org.metawidget.faces.component.html.HtmlMetawidget;

/**
 * @author Richard Kennard
 */

public class CssStyleProcessorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception
	{
		CssStyleProcessor processor = new CssStyleProcessor();

		// Pass through

		HtmlMetawidget metawidget = new HtmlMetawidget();
		UIComponent component = new HtmlInputText();
		assertTrue( component == processor.processWidget( component, PROPERTY, null, metawidget ));
		assertTrue( null == component.getAttributes().get( "style" ) );
		assertTrue( null == component.getAttributes().get( "styleClass" ) );

		// Simple styles and styleClasses

		metawidget.setStyle( "foo1" );
		metawidget.setStyleClass( "bar1" );
		assertTrue( component == processor.processWidget( component, PROPERTY, null, metawidget ));
		assertTrue( "foo1".equals( component.getAttributes().get( "style" ) ));
		assertTrue( "bar1".equals( component.getAttributes().get( "styleClass" ) ));

		// Compound styles and styleClasses

		metawidget.setStyle( "foo2" );
		metawidget.setStyleClass( "bar2" );
		assertTrue( component == processor.processWidget( component, PROPERTY, null, metawidget ));
		assertTrue( "foo1 foo2".equals( component.getAttributes().get( "style" ) ));
		assertTrue( "bar1 bar2".equals( component.getAttributes().get( "styleClass" ) ));
	}
}
