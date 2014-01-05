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

package org.metawidget.integrationtest.swing.userguide;

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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TooltipInspectorExampleTest
	extends TestCase {

	//
	// Public methods
	//

	public void testInspectorExample()
		throws Exception {

		TestPerson person = new TestPerson();

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new CompositeInspector( new CompositeInspectorConfig().setInspectors( new PropertyTypeInspector(), new TooltipInspector() ) ) );
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
	static @interface Tooltip {

		String value();
	}

	static class TestPerson {

		private String	mName;

		private int		mAge;

		private boolean	mRetired;

		@Tooltip( "tooltip-name" )
		public String getName() {

			return mName;
		}

		public void setName( String name ) {

			mName = name;
		}

		@Tooltip( "tooltip-age" )
		public int getAge() {

			return mAge;
		}

		public void setAge( int age ) {

			mAge = age;
		}

		@Tooltip( "tooltip-retired" )
		public boolean isRetired() {

			return mRetired;
		}

		public void setRetired( boolean retired ) {

			mRetired = retired;
		}
	}

	static class TooltipInspector
		extends BaseObjectInspector {

		@Override
		protected Map<String, String> inspectProperty( Property property )
			throws Exception {

			Map<String, String> attributes = CollectionUtils.newHashMap();

			Tooltip tooltip = property.getAnnotation( Tooltip.class );

			if ( tooltip != null ) {
				attributes.put( "tooltip", tooltip.value() );
			}

			return attributes;
		}
	}

	static class TooltipProcessor
		implements WidgetProcessor<JComponent, SwingMetawidget> {

		public JComponent processWidget( JComponent widget, String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

			widget.setToolTipText( attributes.get( "tooltip" ) );
			return widget;
		}
	}
}
