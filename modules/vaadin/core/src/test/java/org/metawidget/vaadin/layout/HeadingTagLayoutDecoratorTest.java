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

package org.metawidget.vaadin.layout;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.vaadin.VaadinMetawidget;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * @author Richard Kennard
 */

public class HeadingTagLayoutDecoratorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testNestedHeadingTags() {

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setLayout( new HeadingTagLayoutDecorator( new LayoutDecoratorConfig<Component, ComponentContainer,VaadinMetawidget>().setLayout( new HeadingTagLayoutDecorator( new LayoutDecoratorConfig<Component, ComponentContainer,VaadinMetawidget>().setLayout( new FormLayout() ) ) ) ) );
		metawidget.setToInspect( new Bar() );

		com.vaadin.ui.FormLayout layout = (com.vaadin.ui.FormLayout) metawidget.getContent();

		assertEquals( "Abc:", ( (TextField) layout.getComponent( 0 ) ).getCaption() );
		assertEquals( "Foo", ( (Label) layout.getComponent( 1 ) ).getValue() );
		assertEquals( "h1", ( (Label) layout.getComponent( 1 ) ).getStyleName() );

		assertEquals( "Bar", ( (Label) layout.getComponent( 2 ) ).getValue() );
		assertEquals( "h2", ( (Label) layout.getComponent( 2 ) ).getStyleName() );

		assertEquals( "Def:", ( (CheckBox) layout.getComponent( 3 ) ).getCaption() );
		assertEquals( "Ghi:", ( (TextArea) layout.getComponent( 4 ) ).getCaption() );

		assertEquals( "Baz", ( (Label) layout.getComponent( 5 ) ).getValue() );
		assertEquals( "h2", ( (Label) layout.getComponent( 5 ) ).getStyleName() );

		assertEquals( "Jkl:", ( (TextField) layout.getComponent( 6 ) ).getCaption() );
		assertEquals( "Mno:", ( (CheckBox) layout.getComponent( 7 ) ).getCaption() );

		assertEquals( "Moo", ( (Label) layout.getComponent( 8 ) ).getValue() );
		assertEquals( "h2", ( (Label) layout.getComponent( 8 ) ).getStyleName() );

		assertEquals( "Pqr:", ( (TextField) layout.getComponent( 9 ) ).getCaption() );

		assertEquals( "Zoo", ( (Label) layout.getComponent( 10 ) ).getValue() );
		assertEquals( "h2", ( (Label) layout.getComponent( 10 ) ).getStyleName() );

		assertEquals( "Zoo:", ( (VaadinMetawidget) layout.getComponent( 11 ) ).getCaption() );

		com.vaadin.ui.FormLayout nestedLayout = (com.vaadin.ui.FormLayout) ( (VaadinMetawidget) layout.getComponent( 11 ) ).getContent();
		assertEquals( "Name:", ( (TextField) nestedLayout.getComponent( 0 ) ).getCaption() );

		assertEquals( "Stu:", ( (TextField) layout.getComponent( 12 ) ).getCaption() );
		assertEquals( 13, layout.getComponentCount() );
	}

	public void testEmptyStub() {

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setLayout( new HeadingTagLayoutDecorator( new LayoutDecoratorConfig<Component, ComponentContainer,VaadinMetawidget>().setLayout( new FormLayout() ) ) );
		metawidget.setToInspect( new Baz() );

		assertTrue( metawidget.getContent() instanceof com.vaadin.ui.FormLayout );
		assertEquals( 0, ((com.vaadin.ui.FormLayout) metawidget.getContent()).getComponentCount() );

		metawidget.setLayout( new HeadingTagLayoutDecorator( new LayoutDecoratorConfig<Component, ComponentContainer,VaadinMetawidget>().setLayout( new TabSheetLayoutDecorator( new LayoutDecoratorConfig<Component, ComponentContainer,VaadinMetawidget>().setLayout( new FormLayout() ) ) ) ) );
		metawidget.setToInspect( new Baz() );
		assertEquals( 0, ((com.vaadin.ui.FormLayout) metawidget.getContent()).getComponentCount() );
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

		public String setAbc() {

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

		@UiSection( { "Foo", "Zoo" } )
		public Zoo getZoo() {

			return new Zoo();
		}

		public void setZoo() {

			// Do nothing
		}

		@UiSection( "" )
		@UiComesAfter( "zoo" )
		public String getStu() {

			return null;
		}

		public void setStu( @SuppressWarnings( "unused" ) String stu ) {

			// Do nothing
		}
	}

	static class Baz {

		@UiSection( "Section" )
		@UiHidden
		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

			// Do nothing
		}
	}

	public static class Zoo {

		public String getName() {

			return null;
		}

		public void setName( @SuppressWarnings( "unused" ) String name ) {

			// Do nothing
		}
	}
}
