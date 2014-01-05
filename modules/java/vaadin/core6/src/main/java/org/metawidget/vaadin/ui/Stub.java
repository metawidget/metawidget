// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.vaadin.ui;

import java.util.Map;

import org.metawidget.util.CollectionUtils;

import com.vaadin.ui.Panel;

/**
 * Stub for Vaadin environments.
 * <p>
 * Stubs are used to 'stub out' what Metawidget would normally create - either to suppress widget
 * creation entirely or to create child widgets with a different name. They differ from Facets in
 * that Facets are simply 'decorations' (such as button bars) to be recognized and arranged at the
 * discretion of the Layout.
 * <p>
 * We define separate Stub widgets, as opposed to simply a <code>VaadinMetawidget.addStub</code>
 * method, as this is more amenable to visual UI builders.
 *
 * @author Loghman Barari
 */

public class Stub
	extends Panel {

	//
	// Private members
	//

	private Map<String, String>	mAttributes;

	//
	// Constructors
	//

	public Stub() {

		addStyleName( "light" );
		((com.vaadin.ui.Layout) getContent()).setMargin( false );
	}

	/**
	 * Convenience constructor.
	 * <p>
	 * Useful for creating stubs that will otherwise be empty, such as
	 * <code>metawidget.add( new Stub( "foo" ))</code>
	 */

	public Stub( String data ) {

		this();
		setData( data );
	}

	//
	// Public methods
	//

	public void setAttribute( String name, String value ) {

		if ( mAttributes == null ) {
			mAttributes = CollectionUtils.newHashMap();
		}

		mAttributes.put( name, value );
	}

	public Map<String, String> getAttributes() {

		return mAttributes;
	}
}
