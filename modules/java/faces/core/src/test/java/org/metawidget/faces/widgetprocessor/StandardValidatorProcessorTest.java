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

package org.metawidget.faces.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.BeanValidator;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.LongRangeValidator;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.html.widgetbuilder.HtmlLookupOutputText;
import org.metawidget.faces.component.widgetprocessor.StandardValidatorProcessor;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StandardValidatorProcessorTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception {

		StandardValidatorProcessor processor = new StandardValidatorProcessor() {
			@Override
			protected boolean isBeanValidationAvailable() {
				return false;
			}
		};

		// Only EditableValueHolders

		HtmlOutputText htmlOutputText = new HtmlOutputText();
		assertEquals( htmlOutputText, processor.processWidget( htmlOutputText, ACTION, null, null ) );

		// Pass through

		HtmlInputText htmlInputText = new HtmlInputText();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		assertEquals( htmlInputText, processor.processWidget( htmlInputText, PROPERTY, attributes, null ) );
		assertEquals( 0, htmlInputText.getValidators().length );
		assertEquals( null, htmlInputText.getLabel() );

		// Long Range

		HtmlMetawidget metawidget = new HtmlMetawidget();

		attributes.put( NAME, "foo" );
		attributes.put( MINIMUM_VALUE, "2" );
		assertEquals( htmlInputText, processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget ) );
		assertEquals( 1, htmlInputText.getValidators().length );
		assertEquals( 2, ( (LongRangeValidator) htmlInputText.getValidators()[0] ).getMinimum() );
		assertEquals( 0, ( (LongRangeValidator) htmlInputText.getValidators()[0] ).getMaximum() );
		assertEquals( null, htmlInputText.getLabel() );

		// Should not touch existing validators

		attributes.put( MAXIMUM_VALUE, "4" );
		assertEquals( htmlInputText, processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget ) );
		assertEquals( 1, htmlInputText.getValidators().length );
		assertEquals( 2, ( (LongRangeValidator) htmlInputText.getValidators()[0] ).getMinimum() );
		assertEquals( 0, ( (LongRangeValidator) htmlInputText.getValidators()[0] ).getMaximum() );

		// Double range

		attributes.put( TYPE, double.class.getName() );
		assertEquals( htmlInputText, processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget ) );
		assertEquals( 2, htmlInputText.getValidators().length );
		assertEquals( 2d, ( (DoubleRangeValidator) htmlInputText.getValidators()[1] ).getMinimum() );
		assertEquals( 4d, ( (DoubleRangeValidator) htmlInputText.getValidators()[1] ).getMaximum() );

		// Length range

		attributes.put( MINIMUM_LENGTH, "3" );
		attributes.put( MAXIMUM_LENGTH, "10" );
		assertEquals( htmlInputText, processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget ) );
		assertEquals( 3, htmlInputText.getValidators().length );
		assertEquals( 3, ( (LengthValidator) htmlInputText.getValidators()[2] ).getMinimum() );
		assertEquals( 10, ( (LengthValidator) htmlInputText.getValidators()[2] ).getMaximum() );

		// JSF 2

		processor = new StandardValidatorProcessor();

		htmlInputText = new HtmlInputText();
		assertEquals( htmlInputText, processor.processWidget( htmlInputText, PROPERTY, null, null ) );
		assertTrue( htmlInputText.getValidators()[0] instanceof BeanValidator );
		assertEquals( 1, htmlInputText.getValidators().length );
	}

	//
	// Protected methods
	//

	@Override
	protected final void setUp()
		throws Exception {

		super.setUp();

		mContext = newMockFacesContext();
	}

	protected MockFacesContext newMockFacesContext() {

		return new MockFacesContext() {

			@Override
			public UIComponent createComponent( String componentName )
				throws FacesException {

				if ( "org.metawidget.HtmlLookupOutputText".equals( componentName ) ) {
					return new HtmlLookupOutputText();
				}

				return super.createComponent( componentName );
			}
		};
	}

	@Override
	protected final void tearDown()
		throws Exception {

		super.tearDown();

		mContext.release();
	}
}
