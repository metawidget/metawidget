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

package org.metawidget.faces.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
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
 * @author Richard Kennard
 */

public class StandardValidatorProcessorTest
	extends TestCase
{
	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception
	{
		StandardValidatorProcessor processor = new StandardValidatorProcessor();

		// Only EditableValueHolders

		HtmlOutputText htmlOutputText = new HtmlOutputText();
		assertTrue( htmlOutputText == processor.processWidget( htmlOutputText, ACTION, null, null ));

		// Pass through

		HtmlInputText htmlInputText = new HtmlInputText();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		assertTrue( htmlInputText == processor.processWidget( htmlInputText, PROPERTY, attributes, null ));
		assertTrue( 0 == htmlInputText.getValidators().length );
		assertTrue( null == htmlInputText.getLabel() );

		// Long Range

		HtmlMetawidget metawidget = new HtmlMetawidget();

		attributes.put( NAME, "foo" );
		attributes.put( MINIMUM_VALUE, "2" );
		assertTrue( htmlInputText == processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget ));
		assertTrue( 1 == htmlInputText.getValidators().length );
		assertTrue( 2 == ((LongRangeValidator) htmlInputText.getValidators()[0]).getMinimum() );
		assertTrue( 0 == ((LongRangeValidator) htmlInputText.getValidators()[0]).getMaximum() );
		assertTrue( "Foo".equals( htmlInputText.getLabel() ));

		// Should not touch existing validators

		attributes.put( MAXIMUM_VALUE, "4" );
		assertTrue( htmlInputText == processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget ));
		assertTrue( 1 == htmlInputText.getValidators().length );
		assertTrue( 2 == ((LongRangeValidator) htmlInputText.getValidators()[0]).getMinimum() );
		assertTrue( 0 == ((LongRangeValidator) htmlInputText.getValidators()[0]).getMaximum() );

		// Double range

		attributes.put( TYPE, double.class.getName() );
		assertTrue( htmlInputText == processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget ));
		assertTrue( 2 == htmlInputText.getValidators().length );
		assertTrue( 2d == ((DoubleRangeValidator) htmlInputText.getValidators()[1]).getMinimum() );
		assertTrue( 4d == ((DoubleRangeValidator) htmlInputText.getValidators()[1]).getMaximum() );

		// Length range

		attributes.put( MINIMUM_LENGTH, "3" );
		attributes.put( MAXIMUM_LENGTH, "10" );
		assertTrue( htmlInputText == processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget ));
		assertTrue( 3 == htmlInputText.getValidators().length );
		assertTrue( 3 == ((LengthValidator) htmlInputText.getValidators()[2]).getMinimum() );
		assertTrue( 10 == ((LengthValidator) htmlInputText.getValidators()[2]).getMaximum() );
	}

	//
	// Protected methods
	//

	@Override
	protected final void setUp()
		throws Exception
	{
		super.setUp();

		mContext = newMockFacesContext();
	}

	protected MockFacesContext newMockFacesContext()
	{
		return new MockFacesContext()
		{
			@Override
			public UIComponent createComponent( String componentName )
				throws FacesException
			{
				if ( "org.metawidget.HtmlLookupOutputText".equals( componentName ) )
					return new HtmlLookupOutputText();

				return super.createComponent( componentName );
			}
		};
	}

	@Override
	protected final void tearDown()
		throws Exception
	{
		super.tearDown();

		mContext.release();
	}
}
