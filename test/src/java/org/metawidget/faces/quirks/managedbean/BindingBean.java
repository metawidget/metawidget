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

import javax.faces.context.FacesContext;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.widgetprocessor.ReadableIdProcessor;
import org.metawidget.faces.component.widgetprocessor.StandardBindingProcessor;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiRequired;

/**
 * @author Richard Kennard
 */

public class BindingBean {

	//
	// Private members
	//

	private UIMetawidget	mMetawidget;

	private Foo				mFoo	= new Foo();

	//
	// Public methods
	//

	@SuppressWarnings( "deprecation" )
	@UiHidden
	public UIMetawidget getMetawidget() {

		// TODO: not hit?

		if ( mMetawidget == null ) {

			mMetawidget = new HtmlMetawidget();
			mMetawidget.setValueBinding( "value", FacesContext.getCurrentInstance().getApplication().createValueBinding( "#{binding}" ) );
			mMetawidget.addWidgetProcessor( new StandardBindingProcessor() );
			mMetawidget.addWidgetProcessor( new ReadableIdProcessor() );
		}

		return mMetawidget;
	}

	public void setMetawidget( UIMetawidget metawidget ) {

		mMetawidget = metawidget;
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
	// Inner class
	//

	public static class Foo {

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
