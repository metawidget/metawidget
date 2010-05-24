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

package org.metawidget.faces.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.awt.Color;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockComponent;
import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.html.widgetbuilder.HtmlLookupOutputText;
import org.metawidget.faces.component.html.widgetbuilder.HtmlWidgetBuilder;
import org.metawidget.faces.component.html.widgetbuilder.HtmlWidgetBuilderConfig;
import org.metawidget.faces.component.html.widgetbuilder.ReadOnlyWidgetBuilder;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.TestUtils;
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
		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = new ReadOnlyWidgetBuilder();
		HtmlMetawidget dummyMetawdget = new HtmlMetawidget();

		// Read only

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( READ_ONLY, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof HtmlOutputText );

		// Hidden

		attributes.put( HIDDEN, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof UIStub );
		attributes.remove( HIDDEN );

		// Masked

		attributes.put( MASKED, TRUE );
		UIStub stub = (UIStub) widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget );
		assertTrue( 1 == stub.getChildCount() );
		assertTrue( stub.getChildren().get( 0 ) instanceof UIStub );
		attributes.remove( MASKED );

		// Lookups

		attributes.put( LOOKUP, "foo" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof HtmlOutputText );

		attributes.put( LOOKUP_LABELS, "Foo" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof HtmlLookupOutputText );
		attributes.remove( LOOKUP_LABELS );
		attributes.remove( LOOKUP );

		// Faces lookup

		attributes.put( FACES_LOOKUP, "#{foo.bar}" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof HtmlOutputText );
		attributes.remove( FACES_LOOKUP );

		// Other types

		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof HtmlOutputText );
		attributes.put( TYPE, int.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof HtmlOutputText );
		attributes.put( TYPE, Integer.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof HtmlOutputText );
		attributes.put( TYPE, Date.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof HtmlOutputText );

		// Arrays

		attributes.put( TYPE, Foo[].class.getName() );
		dummyMetawdget.setInspector( new PropertyTypeInspector() );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) );

		// Lists

		attributes.put( TYPE, List.class.getName() );
		attributes.put( NAME, "bar" );
		dummyMetawdget.setParameter( "dataTableRowEditAction", "#{foo.action}" );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) );

		// Other collections

		attributes.put( TYPE, Set.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof HtmlOutputText );

		// Unsupported types

		attributes.put( TYPE, Color.class.getName() );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) );

		// Don't expand

		attributes.put( DONT_EXPAND, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof HtmlOutputText );
		attributes.remove( DONT_EXPAND );

		// Non-read only

		widgetBuilder = new HtmlWidgetBuilder();

		// Hidden

		attributes.put( HIDDEN, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof UIStub );
		attributes.remove( HIDDEN );

		// Masked

		attributes.put( TYPE, String.class.getName() );
		attributes.put( MASKED, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, null ) instanceof HtmlInputSecret );

		// Overridden component

		attributes.put( FACES_COMPONENT, "foo" );
		assertEquals( "foo", ( (MockComponent) widgetBuilder.buildWidget( PROPERTY, attributes, null ) ).getFamily() );
		attributes.remove( FACES_COMPONENT );

		// Unsupport types

		attributes.put( TYPE, Color.class.getName() );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) );

		// Don't expand

		attributes.put( DONT_EXPAND, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawdget ) instanceof HtmlInputText );
		attributes.remove( DONT_EXPAND );
	}

	@SuppressWarnings( "deprecation" )
	public void testSharedWidgetBuilder()
		throws Exception
	{
		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = newWidgetBuilder();

		// Action

		Map<String, String> attributes = CollectionUtils.newHashMap();
		HtmlCommandButton htmlCommandButton = (HtmlCommandButton) widgetBuilder.buildWidget( ACTION, attributes, new HtmlMetawidget()
		{
			@Override
			public String getLabelString( Map<String, String> passedAttributes )
			{
				return "foo";
			}
		} );
		assertEquals( "foo", htmlCommandButton.getValue() );

		// No type

		HtmlInputText htmlInputText = (HtmlInputText) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		furtherAssert( htmlInputText );

		// Faces lookup

		attributes.put( FACES_LOOKUP, "#{foo.bar}" );
		HtmlSelectOneListbox htmlSelectOneListbox = (HtmlSelectOneListbox) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 1 == htmlSelectOneListbox.getSize() );
		assertTrue( "".equals( ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 0 ) ).getItemLabel() ) );
		assertTrue( null == ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 0 ) ).getItemValue() );
		assertEquals( "#{foo.bar}", ( (UISelectItems) htmlSelectOneListbox.getChildren().get( 1 ) ).getValueBinding( "value" ).getExpressionString() );
		furtherAssert( htmlSelectOneListbox );

		attributes.put( REQUIRED, TRUE );
		htmlSelectOneListbox = (HtmlSelectOneListbox) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 1 == htmlSelectOneListbox.getSize() );
		assertEquals( "#{foo.bar}", ( (UISelectItems) htmlSelectOneListbox.getChildren().get( 0 ) ).getValueBinding( "value" ).getExpressionString() );
		furtherAssert( htmlSelectOneListbox );
		attributes.remove( REQUIRED );

		attributes.put( TYPE, List.class.getName() );
		HtmlSelectManyCheckbox htmlSelectManyCheckbox = (HtmlSelectManyCheckbox) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "pageDirection", htmlSelectManyCheckbox.getLayout() );
		assertEquals( "#{foo.bar}", ( (UISelectItems) htmlSelectManyCheckbox.getChildren().get( 0 ) ).getValueBinding( "value" ).getExpressionString() );
		attributes.remove( FACES_LOOKUP );

		// Lookup

		attributes.put( TYPE, String.class.getName() );
		attributes.put( LOOKUP, "Foo, Bar, Baz" );
		htmlSelectOneListbox = (HtmlSelectOneListbox) widgetBuilder.buildWidget( PROPERTY, attributes, new HtmlMetawidget() );
		assertTrue( 1 == htmlSelectOneListbox.getSize() );
		assertEquals( "", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 0 ) ).getItemLabel() );
		assertEquals( null, ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 0 ) ).getItemValue() );
		assertEquals( "Foo", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 1 ) ).getItemLabel() );
		assertEquals( "Foo", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 1 ) ).getItemValue() );
		assertEquals( "Bar", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 2 ) ).getItemLabel() );
		assertEquals( "Bar", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 2 ) ).getItemValue() );
		assertEquals( "Baz", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 3 ) ).getItemLabel() );
		assertEquals( "Baz", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 3 ) ).getItemValue() );
		furtherAssert( htmlSelectOneListbox );

		attributes.put( LOOKUP_LABELS, "foo-label, bar-label, baz-label" );
		htmlSelectOneListbox = (HtmlSelectOneListbox) widgetBuilder.buildWidget( PROPERTY, attributes, new HtmlMetawidget() );
		assertTrue( 1 == htmlSelectOneListbox.getSize() );
		assertEquals( "", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 0 ) ).getItemLabel() );
		assertEquals( null, ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 0 ) ).getItemValue() );
		assertEquals( "foo-label", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 1 ) ).getItemLabel() );
		assertEquals( "Foo", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 1 ) ).getItemValue() );
		assertEquals( "bar-label", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 2 ) ).getItemLabel() );
		assertEquals( "Bar", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 2 ) ).getItemValue() );
		assertEquals( "baz-label", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 3 ) ).getItemLabel() );
		assertEquals( "Baz", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 3 ) ).getItemValue() );
		furtherAssert( htmlSelectOneListbox );
		attributes.remove( LOOKUP_LABELS );

		attributes.put( REQUIRED, TRUE );
		htmlSelectOneListbox = (HtmlSelectOneListbox) widgetBuilder.buildWidget( PROPERTY, attributes, new HtmlMetawidget() );
		assertTrue( 1 == htmlSelectOneListbox.getSize() );
		assertEquals( "Foo", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 0 ) ).getItemLabel() );
		assertEquals( "Foo", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 0 ) ).getItemValue() );
		assertEquals( "Bar", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 1 ) ).getItemLabel() );
		assertEquals( "Bar", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 1 ) ).getItemValue() );
		assertEquals( "Baz", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 2 ) ).getItemLabel() );
		assertEquals( "Baz", ( (UISelectItem) htmlSelectOneListbox.getChildren().get( 2 ) ).getItemValue() );
		furtherAssert( htmlSelectOneListbox );
		attributes.remove( REQUIRED );

		attributes.put( TYPE, List.class.getName() );
		htmlSelectManyCheckbox = (HtmlSelectManyCheckbox) widgetBuilder.buildWidget( PROPERTY, attributes, new HtmlMetawidget() );
		assertTrue( null == htmlSelectManyCheckbox.getLayout() );
		attributes.put( LOOKUP, "Foo, Bar, Baz, Abc" );
		htmlSelectManyCheckbox = (HtmlSelectManyCheckbox) widgetBuilder.buildWidget( PROPERTY, attributes, new HtmlMetawidget() );
		assertEquals( "pageDirection", htmlSelectManyCheckbox.getLayout() );
		attributes.remove( LOOKUP );

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

	public void testConfig()
	{
		TestUtils.testEqualsAndHashcode( HtmlWidgetBuilderConfig.class, new HtmlWidgetBuilderConfig()
		{
			// Subclass
		} );
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

	//
	// Inner class
	//

	public static class Foo
	{
		public String	bar;

		public String	baz;

		public String	abc;
	}
}
