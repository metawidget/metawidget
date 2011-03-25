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

package org.metawidget.swing.widgetbuilder.swingx;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.Map;

import javax.swing.JComponent;

import junit.framework.TestCase;

import org.jdesktop.swingx.JXDatePicker;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilderConfig;

/**
 * @author Richard Kennard
 */

public class SwingXWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetBuilder()
		throws Exception {

		SwingXWidgetBuilder widgetBuilder = new SwingXWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );

		attributes.put( TYPE, String.class.getName() );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );

		attributes.put( TYPE, Date.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, null ) instanceof JXDatePicker );

		attributes.put( HIDDEN, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, null ) instanceof Stub );
	}

	@SuppressWarnings( { "deprecation", "unchecked" } )
	public void testValuePropertyProvider()
		throws Exception {

		DateHolder dateHolder = new DateHolder();
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setWidgetBuilder( new CompositeWidgetBuilder<JComponent, SwingMetawidget>( new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>().setWidgetBuilders( new SwingXWidgetBuilder(), new SwingWidgetBuilder() ) ) );
		metawidget.addWidgetProcessor( new BeansBindingProcessor() );
		metawidget.setToInspect( dateHolder );

		Date date = new Date();
		metawidget.setValue( date, "date" );
		metawidget.setValue( "Foo", "string" );
		Date dateFromMetawidget = (Date) metawidget.getValue( "date" );
		assertTrue( date.getDay() == dateFromMetawidget.getDay() );
		assertTrue( date.getMonth() == dateFromMetawidget.getMonth() );
		assertTrue( date.getYear() == dateFromMetawidget.getYear() );
		metawidget.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidget );

		assertEquals( dateFromMetawidget, dateHolder.getDate() );

		// Test SwingXWidgetBuilder.getValueProperty passthrough

		assertEquals( "Foo", dateHolder.getString() );
	}

	//
	// Inner class
	//

	public static class DateHolder {

		//
		// Private members
		//

		private Date	mDate;

		private String	mString;

		//
		// Public methods
		//

		public Date getDate() {

			return mDate;
		}

		public void setDate( Date date ) {

			mDate = date;
		}

		public String getString() {

			return mString;
		}

		public void setString( String string ) {

			mString = string;
		}
	}
}
