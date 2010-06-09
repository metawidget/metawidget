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

package org.metawidget.example.swing.userguide;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import javax.swing.JComponent;

import junit.framework.TestCase;

import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * @author Richard Kennard
 */

public class TooltipInspectorExampleTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testInspectorExample()
		throws Exception
	{
		TestPerson person = new TestPerson();

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new CompositeInspector( new CompositeInspectorConfig().setInspectors( new PropertyTypeInspector(), new TooltipInspector() )) );
		metawidget.addWidgetProcessor( new TooltipProcessor() );
		metawidget.setToInspect( person );

		assertEquals( "tooltip-age", ( (JComponent) metawidget.getComponent( 1 ) ).getToolTipText() );
		assertEquals( "tooltip-name", ( (JComponent) metawidget.getComponent( 3 ) ).getToolTipText() );
		assertEquals( "tooltip-retired", ( (JComponent) metawidget.getComponent( 5 ) ).getToolTipText() );
	}

	//
	// Inner classes
	//

	@Retention( RetentionPolicy.RUNTIME )
	@Target( { ElementType.FIELD, ElementType.METHOD } )
	static @interface Tooltip
	{
		String value();
	}

	static class TestPerson
	{
		@Tooltip( "tooltip-name" )
		public String	name;

		@Tooltip( "tooltip-age" )
		public int		age;

		@Tooltip( "tooltip-retired" )
		public boolean	retired;
	}

	static class TooltipInspector
		extends BaseObjectInspector
	{
		@Override
		protected Map<String, String> inspectProperty( Property property )
			throws Exception
		{
			Map<String, String> attributes = CollectionUtils.newHashMap();

			Tooltip tooltip = property.getAnnotation( Tooltip.class );

			if ( tooltip != null )
			{
				attributes.put( "tooltip", tooltip.value() );
			}

			return attributes;
		}
	}

	static class TooltipProcessor
		implements WidgetProcessor<JComponent, SwingMetawidget>
	{
		public JComponent processWidget( JComponent widget, String elementName, Map<String, String> attributes, SwingMetawidget metawidget )
		{
			widget.setToolTipText( attributes.get( "tooltip" ) );
			return widget;
		}
	}
}
