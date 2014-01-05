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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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

		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );

		attributes.put( TYPE, String.class.getName() );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );

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
		assertEquals( date.getDay(), dateFromMetawidget.getDay() );
		assertEquals( date.getMonth(), dateFromMetawidget.getMonth() );
		assertEquals( date.getYear(), dateFromMetawidget.getYear() );
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
