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

import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.widgetprocessor.ReadableIdProcessor;

/**
 * @author Richard Kennard
 */

public class ReadableIdProcessorTest
	extends TestCase
{
	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	@SuppressWarnings( "deprecation" )
	public void testWidgetProcessor()
		throws Exception
	{
		ReadableIdProcessor processor = new ReadableIdProcessor();
		HtmlMetawidget metawidget = new HtmlMetawidget();

		// Property bindings

		HtmlInputText component = new HtmlInputText();
		component.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{foo}" ) );
		assertTrue( component == processor.processWidget( component, PROPERTY, null, metawidget ) );
		assertTrue( "foo".equals( component.getId() ) );

		// Existing ids should not get overwritten

		component.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{foo.bar}" ) );
		assertTrue( component == processor.processWidget( component, ENTITY, null, metawidget ) );
		assertTrue( "foo".equals( component.getId() ) );

		// Multi-name bindings

		component.setId( null );
		component.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{foo.bar}" ) );
		assertTrue( component == processor.processWidget( component, ENTITY, null, metawidget ) );
		assertTrue( "fooBar".equals( component.getId() ) );

		// Duplicate bindings

		component.setId( null );
		metawidget.getChildren().add( component );
		assertTrue( component == processor.processWidget( component, PROPERTY, null, metawidget ) );
		assertTrue( "fooBar_2".equals( component.getId() ) );

		// Action bindings

		HtmlCommandButton command = new HtmlCommandButton();
		command.setAction( mContext.getApplication().createMethodBinding( "#{foo.bar.action}", null ) );
		assertTrue( command == processor.processWidget( command, ACTION, null, metawidget ) );
		assertTrue( "fooBarAction".equals( command.getId() ) );

		// Metawidgets

		HtmlMetawidget childMetawidget = new HtmlMetawidget();
		childMetawidget.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{foo.bar.baz}" ) );
		assertTrue( childMetawidget == processor.processWidget( childMetawidget, PROPERTY, null, metawidget ) );
		assertTrue( "fooBarBaz_Metawidget".equals( childMetawidget.getId() ) );

		// Duplicate metawidgets

		metawidget.getChildren().add( childMetawidget );
		childMetawidget.setId( null );
		childMetawidget.setValueBinding( "value", mContext.getApplication().createValueBinding( "#{foo.bar.baz}" ) );
		assertTrue( childMetawidget == processor.processWidget( childMetawidget, PROPERTY, null, metawidget ) );
		assertTrue( "fooBarBaz_Metawidget_2".equals( childMetawidget.getId() ) );
	}

	//
	// Protected methods
	//

	@Override
	protected final void setUp()
		throws Exception
	{
		super.setUp();

		mContext = new MockFacesContext();
	}

	@Override
	protected final void tearDown()
		throws Exception
	{
		super.tearDown();

		mContext.release();
	}
}
