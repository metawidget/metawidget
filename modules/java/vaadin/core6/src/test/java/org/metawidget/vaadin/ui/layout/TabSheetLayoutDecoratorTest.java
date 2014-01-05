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

package org.metawidget.vaadin.ui.layout;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.vaadin.ui.VaadinMetawidget;
import org.metawidget.vaadin.ui.layout.FormLayout;
import org.metawidget.vaadin.ui.layout.TabSheetLayoutDecorator;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TabSheetLayoutDecoratorTest
	extends TestCase {

	//
	// Public methods
	//

	@SuppressWarnings( "cast" )
	public void testNestedTabs() {

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setLayout( new TabSheetLayoutDecorator( new LayoutDecoratorConfig<Component, ComponentContainer, VaadinMetawidget>().setLayout( new TabSheetLayoutDecorator( new LayoutDecoratorConfig<Component, ComponentContainer, VaadinMetawidget>().setLayout( new FormLayout() ) ) ) ) );
		metawidget.setToInspect( new Bar() );

		com.vaadin.ui.FormLayout layout = (com.vaadin.ui.FormLayout) metawidget.getContent();

		assertEquals( "Abc:", ( (TextField) layout.getComponent( 0 ) ).getCaption() );

		Tab outerTab = ((TabSheet) layout.getComponent( 1 )).getTab( 0 );
		assertEquals( "Foo", outerTab.getCaption() );
		com.vaadin.ui.FormLayout outerTabLayout = (com.vaadin.ui.FormLayout) ( (com.vaadin.ui.VerticalLayout) ( (Panel) outerTab.getComponent() ).getContent() ).getComponent( 0 );
		assertEquals( 3, outerTabLayout.getComponentCount() );

		Tab innerTab = ((TabSheet) outerTabLayout.getComponent( 0 )).getTab( 0 );
		assertEquals( "Bar", innerTab.getCaption() );
		com.vaadin.ui.FormLayout innerTab1Layout = (com.vaadin.ui.FormLayout) ( (com.vaadin.ui.VerticalLayout) ( (Panel) innerTab.getComponent() ).getContent() ).getComponent( 0 );
		assertEquals( "Def:", ( (CheckBox) innerTab1Layout.getComponent( 0 ) ).getCaption() );
		assertEquals( "Ghi:", ( (TextArea) innerTab1Layout.getComponent( 1 ) ).getCaption() );
		assertEquals( 2, innerTab1Layout.getComponentCount() );

		innerTab = ((TabSheet) outerTabLayout.getComponent( 0 )).getTab( 1 );
		assertEquals( "Baz", innerTab.getCaption() );
		com.vaadin.ui.FormLayout innerTab2Layout = (com.vaadin.ui.FormLayout) ( (com.vaadin.ui.VerticalLayout) ( (Panel) innerTab.getComponent() ).getContent() ).getComponent( 0 );
		assertEquals( "Jkl:", ( (TextField) innerTab2Layout.getComponent( 0 ) ).getCaption() );
		assertEquals( 1, innerTab2Layout.getComponentCount() );

		assertEquals( "Mno:", ( (CheckBox) outerTabLayout.getComponent( 1 ) ).getCaption() );

		innerTab = ((TabSheet) outerTabLayout.getComponent( 2 )).getTab( 0 );
		assertEquals( "Moo", innerTab.getCaption() );
		com.vaadin.ui.FormLayout mooTabLayout = (com.vaadin.ui.FormLayout) ( (com.vaadin.ui.VerticalLayout) ( (Panel) innerTab.getComponent() ).getContent() ).getComponent( 0 );
		assertEquals( "Pqr:", ( (TextField) mooTabLayout.getComponent( 0 ) ).getCaption() );
		assertEquals( 1, mooTabLayout.getComponentCount() );

		assertEquals( "Stu:", ( (TextField) layout.getComponent( 2 ) ).getCaption() );
		assertEquals( 3, outerTabLayout.getComponentCount() );

		// Test components within nested tabs still accessible by name

		assertEquals( (Component) layout.getComponent( 0 ), (Component) metawidget.getComponent( "abc" ) );
		assertEquals( (Component) innerTab1Layout.getComponent( 0 ), (Component) metawidget.getComponent( "def" ) );
		assertEquals( (Component) innerTab1Layout.getComponent( 1 ), (Component) metawidget.getComponent( "ghi" ) );
		assertEquals( (Component) innerTab2Layout.getComponent( 0 ), (Component) metawidget.getComponent( "jkl" ) );
		assertEquals( (Component) outerTabLayout.getComponent( 1 ), (Component) metawidget.getComponent( "mno" ) );
		assertEquals( (Component) mooTabLayout.getComponent( 0 ), (Component) metawidget.getComponent( "pqr" ) );
		assertEquals( (Component) layout.getComponent( 2 ), (Component) metawidget.getComponent( "stu" ) );
	}

	//
	// Inner class
	//

	static class Foo {

		@UiSection( "Section" )
		public String getBar() {

			return null;
		}

		public void setBar( @SuppressWarnings( "unused" ) String bar ) {

			// Do nothing
		}
	}

	public static class Bar {

		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

			// Do nothing
		}

		@UiSection( { "Foo", "Bar" } )
		public boolean isDef() {

			return false;
		}

		public void setDef( @SuppressWarnings( "unused" ) boolean def ) {

			// Do nothing
		}

		@UiLarge
		public String getGhi() {

			return null;
		}

		public void setGhi( @SuppressWarnings( "unused" ) String ghi ) {

			// Do nothing
		}

		@UiSection( { "Foo", "Baz" } )
		public String getJkl() {

			return null;
		}

		public void setJkl( @SuppressWarnings( "unused" ) String jkl ) {

			// Do nothing
		}

		@UiSection( { "Foo", "" } )
		public boolean isMno() {

			return false;
		}

		public void setMno( @SuppressWarnings( "unused" ) boolean mno ) {

			// Do nothing
		}

		@UiSection( { "Foo", "Moo" } )
		public String getPqr() {

			return null;
		}

		public void setPqr( @SuppressWarnings( "unused" ) String pqr ) {

			// Do nothing
		}

		@UiSection( "" )
		public String getStu() {

			return null;
		}

		public void setStu( @SuppressWarnings( "unused" ) String stu ) {

			// Do nothing
		}
	}

	static class Baz {

		@UiSection( { "Foo", "Bar" } )
		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

			// Do nothing
		}

		@UiSection( { "Baz" } )
		public boolean getDef() {

			return false;
		}

		public void setDef( @SuppressWarnings( "unused" ) String def ) {

			// Do nothing
		}
	}
}
