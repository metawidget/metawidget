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

package org.metawidget.test.faces.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.html.widgetbuilder.HtmlWidgetBuilder;
import org.metawidget.test.faces.FacesMetawidgetTests.MockComponent;
import org.metawidget.test.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author Richard Kennard
 */

public class HtmlWidgetBuilderTest
	extends TestCase
{
	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testWidgetBuilder()
		throws Exception
	{
		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = newWidgetBuilder();

		// Hidden

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( HIDDEN, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, new HtmlMetawidget()
		{
			@Override
			public boolean isCreateHiddenFields()
			{
				return false;
			}
		} ) instanceof UIStub );
		attributes.remove( HIDDEN );

		// Masked

		attributes.put( TYPE, String.class.getName() );
		attributes.put( MASKED, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, null ) instanceof HtmlInputSecret );

		// Overridden component

		attributes.put( FACES_COMPONENT, "foo" );
		assertTrue( "foo".equals( ((MockComponent) widgetBuilder.buildWidget( PROPERTY, attributes, null )).getFamily() ));
		attributes.remove( FACES_COMPONENT );

	}

	@SuppressWarnings("deprecation")
	public void testSharedWidgetBuilder()
		throws Exception
	{
		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = newWidgetBuilder();

		// Action

		Map<String, String> attributes = CollectionUtils.newHashMap();
		HtmlCommandButton htmlCommandButton = (HtmlCommandButton) widgetBuilder.buildWidget( ACTION, attributes, new HtmlMetawidget()
		{
			@Override
			public String getLabelString( FacesContext context, Map<String, String> passedAttributes )
			{
				return "foo";
			}
		});
		assertTrue( "foo".equals( htmlCommandButton.getValue() ));

		// No type

		HtmlInputText htmlInputText = (HtmlInputText) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		furtherAssert( htmlInputText );

		// Faces lookup

		attributes.put( FACES_LOOKUP, "#{foo.bar}" );
		HtmlSelectOneListbox htmlSelectOneListbox = (HtmlSelectOneListbox) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 1 == htmlSelectOneListbox.getSize() );
		assertTrue( null == ((UISelectItem) htmlSelectOneListbox.getChildren().get( 0 )).getItemLabel() );
		assertTrue( null == ((UISelectItem) htmlSelectOneListbox.getChildren().get( 0 )).getItemValue() );
		assertTrue( "#{foo.bar}".equals( ((UISelectItems) htmlSelectOneListbox.getChildren().get( 1 )).getValueBinding( "value" ).getExpressionString() ));
		furtherAssert( htmlSelectOneListbox );
		attributes.remove( FACES_LOOKUP );

		// Boolean

		attributes.put( TYPE, Boolean.class.getName() );
		attributes.put( REQUIRED, TRUE );
		HtmlSelectBooleanCheckbox htmlSelectBooleanCheckbox = (HtmlSelectBooleanCheckbox) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		furtherAssert( htmlSelectBooleanCheckbox );

		// boolean

		attributes.put( TYPE, boolean.class.getName() );
		htmlSelectBooleanCheckbox = (HtmlSelectBooleanCheckbox) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		furtherAssert( htmlSelectBooleanCheckbox );

		// char

		attributes.put( TYPE, char.class.getName() );
		htmlInputText = (HtmlInputText) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 1 == htmlInputText.getMaxlength() );
		furtherAssert( htmlInputText );

		// int

		attributes.put( TYPE, int.class.getName() );
		htmlInputText = (HtmlInputText) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( Integer.MIN_VALUE == htmlInputText.getMaxlength() );
		furtherAssert( htmlInputText );

		// Integer

		attributes.put( TYPE, Integer.class.getName() );
		htmlInputText = (HtmlInputText) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( Integer.MIN_VALUE == htmlInputText.getMaxlength() );
		furtherAssert( htmlInputText );

		// Large

		attributes.put( TYPE, String.class.getName() );
		attributes.remove( MASKED );
		attributes.put( LARGE, TRUE );
		HtmlInputTextarea htmlInputTextarea = (HtmlInputTextarea) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 20 == htmlInputTextarea.getCols() );
		assertTrue( 2 == htmlInputTextarea.getRows() );
		furtherAssert( htmlInputTextarea );
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
		return new MockFacesContext();
	}

	protected WidgetBuilder<UIComponent, UIMetawidget> newWidgetBuilder()
	{
		return new HtmlWidgetBuilder();
	}

	protected void furtherAssert( UIComponent component )
	{
		// Nothing by default
	}

	@Override
	protected final void tearDown()
		throws Exception
	{
		super.tearDown();

		mContext.release();
	}
}
