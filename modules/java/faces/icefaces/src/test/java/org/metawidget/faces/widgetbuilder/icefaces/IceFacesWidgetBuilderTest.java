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

package org.metawidget.faces.widgetbuilder.icefaces;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.html.widgetbuilder.icefaces.IceFacesWidgetBuilder;
import org.metawidget.faces.component.html.widgetbuilder.icefaces.IceFacesWidgetBuilderConfig;
import org.metawidget.faces.widgetbuilder.HtmlWidgetBuilderTest;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

import com.icesoft.faces.component.ext.HtmlCommandButton;
import com.icesoft.faces.component.ext.HtmlInputSecret;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.ext.HtmlInputTextarea;
import com.icesoft.faces.component.ext.HtmlSelectBooleanCheckbox;
import com.icesoft.faces.component.ext.HtmlSelectManyCheckbox;
import com.icesoft.faces.component.ext.HtmlSelectOneMenu;
import com.icesoft.faces.component.selectinputdate.SelectInputDate;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class IceFacesWidgetBuilderTest
	extends HtmlWidgetBuilderTest {

	//
	// Public methods
	//

	@Override
	public void testReadOnly() {

		// No tests
	}

	@Override
	public void testActive()
		throws Exception {

		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = newWidgetBuilder();

		// Hidden

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( HIDDEN, TRUE );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( HIDDEN );

		// Masked

		attributes.put( TYPE, String.class.getName() );
		attributes.put( MASKED, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, null ) instanceof HtmlInputSecret );

		// Overridden component

		attributes.put( FACES_COMPONENT, "foo" );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( FACES_COMPONENT );

		// SelectInputDate

		attributes.put( TYPE, Date.class.getName() );
		attributes.put( DATETIME_PATTERN, "dd-MM-yyyy" );
		SelectInputDate selectInputDate = (SelectInputDate) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "dd-MM-yyyy", selectInputDate.getPopupDateFormat() );
		assertTrue( true == (Boolean) selectInputDate.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_NOT_RECREATABLE ) );
		assertTrue( selectInputDate.getPartialSubmit() );

		// Partial submit turned off

		widgetBuilder = new IceFacesWidgetBuilder( new IceFacesWidgetBuilderConfig().setPartialSubmit( false ) );
		selectInputDate = (SelectInputDate) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( !selectInputDate.getPartialSubmit() );
	}

	@Override
	public void testCollectionWithConverters()
		throws Exception {

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );

		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = newWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, List.class.getName() );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, metawidget ));
	}

	@Override
	public void testCollectionWithSingleColumn() {

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );

		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = newWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, List.class.getName() );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, metawidget ));
	}

	@Override
	public void testCollectionWithManyColumns()
		throws Exception {

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );

		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = newWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, List.class.getName() );
		attributes.put( PARAMETERIZED_TYPE, LargeFoo.class.getName() );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, metawidget ));
	}

	@Override
	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( IceFacesWidgetBuilderConfig.class, new IceFacesWidgetBuilderConfig() {
			// Subclass
		} );
	}

	//
	// Protected methods
	//

	@Override
	protected WidgetBuilder<UIComponent, UIMetawidget> newWidgetBuilder() {

		return new IceFacesWidgetBuilder();
	}

	@Override
	protected MockFacesContext newMockFacesContext() {

		return new MockFacesContext() {

			@Override
			public UIComponent createComponent( String componentName )
				throws FacesException {

				if ( SelectInputDate.COMPONENT_TYPE.equals( componentName ) ) {
					return new SelectInputDate();
				}

				if ( HtmlInputText.COMPONENT_TYPE.equals( componentName ) ) {
					return new HtmlInputText();
				}

				if ( HtmlInputTextarea.COMPONENT_TYPE.equals( componentName ) ) {
					return new HtmlInputTextarea();
				}

				if ( HtmlInputSecret.COMPONENT_TYPE.equals( componentName ) ) {
					return new HtmlInputSecret();
				}

				if ( HtmlCommandButton.COMPONENT_TYPE.equals( componentName ) ) {
					return new HtmlCommandButton();
				}

				if ( HtmlSelectOneMenu.COMPONENT_TYPE.equals( componentName ) ) {
					return new HtmlSelectOneMenu();
				}

				if ( HtmlSelectManyCheckbox.COMPONENT_TYPE.equals( componentName ) ) {
					return new HtmlSelectManyCheckbox();
				}

				if ( HtmlSelectBooleanCheckbox.COMPONENT_TYPE.equals( componentName ) ) {
					return new HtmlSelectBooleanCheckbox();
				}

				return super.createComponent( componentName );
			}
		};
	}

	@Override
	protected void furtherAssert( UIComponent component ) {

		// Assert every component we create is an ICEfaces component (never a regular JSF one)

		assertTrue( component.getClass().getPackage().getName().startsWith( "com.icesoft.faces" ) );

		try {
			// Assert that every ICEfaces component has 'partially submit' set to true

			Method partialSubmit = component.getClass().getMethod( "getPartialSubmit" );
			assertTrue( (Boolean) partialSubmit.invoke( component ) );
		} catch ( Exception e ) {
			throw new RuntimeException( e );
		}
	}
}
