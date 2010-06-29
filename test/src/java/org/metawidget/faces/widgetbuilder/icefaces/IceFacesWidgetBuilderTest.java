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

package org.metawidget.faces.widgetbuilder.icefaces;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.html.widgetbuilder.icefaces.IceFacesWidgetBuilder;
import org.metawidget.faces.widgetbuilder.HtmlWidgetBuilderTest;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

import com.icesoft.faces.component.ext.HtmlCommandButton;
import com.icesoft.faces.component.ext.HtmlInputSecret;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.ext.HtmlInputTextarea;
import com.icesoft.faces.component.ext.HtmlSelectBooleanCheckbox;
import com.icesoft.faces.component.ext.HtmlSelectManyCheckbox;
import com.icesoft.faces.component.ext.HtmlSelectOneListbox;
import com.icesoft.faces.component.selectinputdate.SelectInputDate;

/**
 * @author Richard Kennard
 */

public class IceFacesWidgetBuilderTest
	extends HtmlWidgetBuilderTest {

	//
	// Public methods
	//

	@Override
	public void testWidgetBuilder()
		throws Exception {

		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = newWidgetBuilder();

		// Hidden

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( HIDDEN, TRUE );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( HIDDEN );

		// Masked

		attributes.put( TYPE, String.class.getName() );
		attributes.put( MASKED, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, null ) instanceof HtmlInputSecret );

		// Overridden component

		attributes.put( FACES_COMPONENT, "foo" );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( FACES_COMPONENT );

		// SelectInputDate

		attributes.put( TYPE, Date.class.getName() );
		attributes.put( DATETIME_PATTERN, "dd-MM-yyyy" );
		SelectInputDate selectInputDate = (SelectInputDate) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "dd-MM-yyyy", selectInputDate.getPopupDateFormat() );
		assertTrue( true == (Boolean) selectInputDate.getAttributes().get( UIMetawidget.COMPONENT_ATTRIBUTE_NOT_RECREATABLE ) );
		assertTrue( selectInputDate.getPartialSubmit() );
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

				if ( "com.icesoft.faces.SelectInputDate".equals( componentName ) ) {
					return new SelectInputDate();
				}

				if ( "com.icesoft.faces.HtmlInputText".equals( componentName ) ) {
					return new HtmlInputText();
				}

				if ( "com.icesoft.faces.HtmlInputTextarea".equals( componentName ) ) {
					return new HtmlInputTextarea();
				}

				if ( "com.icesoft.faces.HtmlInputSecret".equals( componentName ) ) {
					return new HtmlInputSecret();
				}

				if ( "com.icesoft.faces.HtmlCommandButton".equals( componentName ) ) {
					return new HtmlCommandButton();
				}

				if ( "com.icesoft.faces.HtmlSelectOneListbox".equals( componentName ) ) {
					return new HtmlSelectOneListbox();
				}

				if ( "com.icesoft.faces.HtmlSelectManyCheckbox".equals( componentName ) ) {
					return new HtmlSelectManyCheckbox();
				}

				if ( "com.icesoft.faces.HtmlSelectBooleanCheckbox".equals( componentName ) ) {
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
