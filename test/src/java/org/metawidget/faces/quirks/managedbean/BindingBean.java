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

package org.metawidget.faces.quirks.managedbean;

import java.io.Serializable;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.widgetprocessor.ReadableIdProcessor;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * @author Richard Kennard
 */

public class BindingBean {

	//
	// Private members
	//

	private Foo	mFoo	= new Foo();

	//
	// Public methods
	//

	@UiHidden
	public UIMetawidget getMetawidget1() {

		// First-time init

		UIMetawidget metawidget = new HtmlMetawidget();
		initMetawidget1( metawidget );
		return metawidget;
	}

	public void setMetawidget1( UIMetawidget metawidget ) {

		if ( metawidget.getWidgetProcessors().size() != 8 ) {
			throw new RuntimeException( "Saw " + metawidget.getWidgetProcessors().size() + " WidgetProcessors by default" );
		}

		// POST-back init

		initMetawidget1( metawidget );
	}

	@UiHidden
	public UIMetawidget getMetawidget2() {

		// First-time init

		UIMetawidget metawidget = new HtmlMetawidget();
		initMetawidget2( metawidget );
		return metawidget;
	}

	/**
	 * @param metawidget	not used
	 */

	public void setMetawidget2( UIMetawidget metawidget ) {

		// Do nothing
	}

	@UiHidden
	public UIMetawidget getMetawidget3() {

		// First-time init

		UIMetawidget metawidget = new HtmlMetawidget();
		initMetawidget3( metawidget );
		return metawidget;
	}

	/**
	 * @param metawidget	not used
	 */

	public void setMetawidget3( UIMetawidget metawidget ) {

		// Do nothing
	}

	public Foo getFoo() {

		return mFoo;
	}

	@UiAction
	@UiComesAfter( "foo" )
	public void save() {

		// Do nothing
	}

	//
	// Private methods
	//

	private void initMetawidget1( UIMetawidget metawidget ) {

		metawidget.removeWidgetProcessor( metawidget.getWidgetProcessor( ReadableIdProcessor.class ) );
		metawidget.addWidgetProcessor( new WidgetProcessor<UIComponent, UIMetawidget>() {

			@Override
			public UIComponent processWidget( UIComponent widget, String elementName, Map<String, String> attributes, UIMetawidget parentMetawidget ) {

				return setId( widget, parentMetawidget );
			}
		} );
	}

	private void initMetawidget2( UIMetawidget metawidget ) {

		metawidget.setValue( new Foo() );
	}

	private void initMetawidget3( UIMetawidget metawidget ) {

		metawidget.setValue( Foo.class );
	}

	/**
	 * Example of using an anonymous inner class (the WidgetProcessor) tied to an outer class
	 * method.
	 */

	/* package private */UIComponent setId( UIComponent widget, UIMetawidget metawidget ) {

		if ( widget.getId() != null ) {
			throw new RuntimeException( "Id is '" + widget.getId() + "'. ReadableIdProcessor still active?" );
		}

		if ( widget instanceof UIInput ) {
			widget.setId( "child" + metawidget.getChildren().size() );
		} else {
			widget.setId( FacesContext.getCurrentInstance().getViewRoot().createUniqueId() );
		}

		return widget;
	}

	//
	// Inner class
	//

	public static class Foo
		implements Serializable {

		//
		// Private members
		//

		private String	mBar	= "Bar Value";

		private String	mBaz	= "Baz Value";

		//
		// Public methods
		//

		@UiRequired
		public String getBar() {

			return mBar;
		}

		public void setBar( String bar ) {

			mBar = bar;
		}

		@UiComesAfter( "bar" )
		@UiLarge
		public String getBaz() {

			return mBaz;
		}

		public void setBaz( String baz ) {

			mBaz = baz;
		}
	}
}
