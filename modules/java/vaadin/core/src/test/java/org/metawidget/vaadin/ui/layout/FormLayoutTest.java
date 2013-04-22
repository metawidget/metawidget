// Metawidget (licensed under LGPL)
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

package org.metawidget.vaadin.ui.layout;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.annotation.UiReadOnly;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.vaadin.ui.Stub;
import org.metawidget.vaadin.ui.VaadinMetawidget;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * @author Richard Kennard
 */

public class FormLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	@SuppressWarnings( "deprecation" )
	public void testLayout()
		throws Exception {

		// Without stub

		VaadinMetawidget metawidget = new VaadinMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setToInspect( new Foo() );

		metawidget.setLayout( new TabSheetLayoutDecorator( new LayoutDecoratorConfig<Component, ComponentContainer, VaadinMetawidget>().setLayout( new FormLayout() ) ) );

		com.vaadin.ui.FormLayout layout = (com.vaadin.ui.FormLayout) metawidget.getContent();

		assertEquals( "Abc:", ( (TextField) layout.getComponent( 0 ) ).getCaption() );
		assertEquals( "Def:", ( (TextField) layout.getComponent( 1 ) ).getCaption() );
		assertEquals( "Ghi:", ( (CheckBox) layout.getComponent( 2 ) ).getCaption() );

		// TabSheet

		TabSheet tabSheet = (TabSheet) layout.getComponent( 3 );
		assertEquals( 3, tabSheet.getComponentCount() );

		Tab tab = tabSheet.getTab( 0 );
		assertTrue( "tab1".equals( tab.getCaption() ) );
		com.vaadin.ui.FormLayout tabLayout = (com.vaadin.ui.FormLayout) ( (com.vaadin.ui.VerticalLayout) ( (Panel) tab.getComponent() ).getContent() ).getComponent( 0 );
		assertEquals( "Tab 1_jkl:", ( (Label) tabLayout.getComponent( 0 ) ).getCaption() );
		assertEquals( "Tab 1_mno:", ( (com.vaadin.ui.Select) tabLayout.getComponent( 1 ) ).getCaption() );
		assertEquals( "Tab 1_pqr:", ( (TextField) tabLayout.getComponent( 2 ) ).getCaption() );
		assertEquals( 3, tabLayout.getComponentCount() );

		tab = tabSheet.getTab( 1 );
		assertTrue( "tab2".equals( tab.getCaption() ) );
		tabLayout = (com.vaadin.ui.FormLayout) ( (com.vaadin.ui.VerticalLayout) ( (Panel) tab.getComponent() ).getContent() ).getComponent( 0 );
		assertEquals( null, ( (TextArea) tabLayout.getComponent( 0 ) ).getCaption() );
		assertEquals( 1, tabLayout.getComponentCount() );

		tab = tabSheet.getTab( 2 );
		assertTrue( "tab3".equals( tab.getCaption() ) );
		tabLayout = (com.vaadin.ui.FormLayout) ( (com.vaadin.ui.VerticalLayout) ( (Panel) tab.getComponent() ).getContent() ).getComponent( 0 );
		assertEquals( null, ( (TextField) tabLayout.getComponent( 0 ) ).getCaption() );
		assertEquals( "Tab 3_mno:", ( (TextField) tabLayout.getComponent( 1 ) ).getCaption() );
		assertEquals( "Tab 3_pqr:", ( (TextField) tabLayout.getComponent( 2 ) ).getCaption() );
		assertEquals( 3, tabLayout.getComponentCount() );

		assertEquals( "Mno:", ( (TextField) layout.getComponent( 4 ) ).getCaption() );
		assertEquals( 5, layout.getComponentCount() );

		// With stub

		metawidget.addComponent( new Stub( "mno" ) );

		// With stub attributes

		Stub stubWithAttributes = new Stub();
		stubWithAttributes.setData( "def" );
		stubWithAttributes.addComponent( new CheckBox() );
		stubWithAttributes.setAttribute( "label", "" );
		metawidget.addComponent( stubWithAttributes );

		// With an arbitrary component

		Slider arbitrary = new Slider();
		metawidget.addComponent( arbitrary );

		// With an arbitrary stub with attributes

		Stub arbitraryStubWithAttributes = new Stub();
		arbitraryStubWithAttributes.addComponent( new TextField() );
		arbitraryStubWithAttributes.setAttribute( "label", "" );
		metawidget.addComponent( arbitraryStubWithAttributes );

		layout = (com.vaadin.ui.FormLayout) metawidget.getContent();

		assertEquals( "Abc:", ( (TextField) layout.getComponent( 0 ) ).getCaption() );
		assertEquals( null, ( (Stub) layout.getComponent( 1 ) ).getCaption() );
		assertTrue( ( (com.vaadin.ui.VerticalLayout) ( (Stub) layout.getComponent( 1 ) ).getContent() ).getComponent( 0 ) instanceof CheckBox );
		assertEquals( "Ghi:", ( (CheckBox) layout.getComponent( 2 ) ).getCaption() );
		assertTrue( layout.getComponent( 3 ) instanceof TabSheet );
		assertTrue( arbitrary == layout.getComponent( 4 ) );
		assertTrue( arbitraryStubWithAttributes == layout.getComponent( 5 ) );
		assertEquals( null, ( (Stub) layout.getComponent( 5 ) ).getCaption() );

		assertEquals( 6, layout.getComponentCount() );
	}

	public void testLabelSuffix() {

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setToInspect( new Foo() );

		// Different label suffix

		metawidget.setLayout( new FormLayout( new FormLayoutConfig().setLabelSuffix( "#" ) ) );
		com.vaadin.ui.FormLayout layout = (com.vaadin.ui.FormLayout) metawidget.getContent();
		assertEquals( "Abc#", ( (TextField) layout.getComponent( 0 ) ).getCaption() );

		// No suffix

		metawidget.setLayout( new FormLayout( new FormLayoutConfig().setLabelSuffix( null ) ) );
		layout = (com.vaadin.ui.FormLayout) metawidget.getContent();
		assertEquals( "Abc", ( (TextField) layout.getComponent( 0 ) ).getCaption() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( FormLayoutConfig.class, new FormLayoutConfig() {
			// Subclass
		} );
	}

	//
	// Inner class
	//

	public static class Foo {

		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

			// Do nothing
		}

		@UiComesAfter( "abc" )
		@UiRequired
		public int getDef() {

			return 0;
		}

		public void setDef( @SuppressWarnings( "unused" ) int def ) {

			// Do nothing
		}

		@UiComesAfter( "def" )
		public boolean isGhi() {

			return false;
		}

		public void setGhi( @SuppressWarnings( "unused" ) boolean ghi ) {

			// Do nothing
		}

		@UiSection( "tab1" )
		@UiComesAfter( "ghi" )
		@UiAttribute( name = "required", value = "true" )
		@UiReadOnly
		public String getTab1_jkl() {

			return null;
		}

		public void setTab1_jkl( @SuppressWarnings( "unused" ) String tab1_jkl ) {

			// Do nothing
		}

		@UiComesAfter( "tab1_jkl" )
		@UiLookup( { "foo", "bar" } )
		public String getTab1_mno() {

			return null;
		}

		public void setTab1_mno( @SuppressWarnings( "unused" ) String tab1_mno ) {

			// Do nothing
		}

		@UiComesAfter( "tab1_mno" )
		public String getTab1_pqr() {

			return null;
		}

		public void setTab1_pqr( @SuppressWarnings( "unused" ) String tab1_pqr ) {

			// Do nothing
		}

		@UiSection( "tab2" )
		@UiComesAfter( "tab1_pqr" )
		@UiLarge
		@UiLabel( "" )
		public String getTab2_jkl() {

			return null;
		}

		public void setTab2_jkl( @SuppressWarnings( "unused" ) String tab2_jkl ) {

			// Do nothing
		}

		@UiSection( "tab3" )
		@UiComesAfter( "tab2_jkl" )
		@UiLabel( "" )
		public String getTab3_jkl() {

			return null;
		}

		public void setTab3_jkl( @SuppressWarnings( "unused" ) String tab3_jkl ) {

			// Do nothing
		}

		@UiComesAfter( "tab3_jkl" )
		public String getTab3_mno() {

			return null;
		}

		public void setTab3_mno( @SuppressWarnings( "unused" ) String tab3_mno ) {

			// Do nothing
		}

		@UiComesAfter( "tab3_mno" )
		public String getTab3_pqr() {

			return null;
		}

		public void setTab3_pqr( @SuppressWarnings( "unused" ) String tab3_pqr ) {

			// Do nothing
		}

		@UiSection( "" )
		@UiComesAfter( "tab3_pqr" )
		public String getMno() {

			return null;
		}

		public void setMno( @SuppressWarnings( "unused" ) String mno ) {

			// Do nothing
		}
	}
}
