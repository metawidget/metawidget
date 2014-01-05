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

package org.metawidget.android.widget;

import java.util.Map;

import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Stub for Android environments.
 * <p>
 * Stubs are used to 'stub out' what Metawidget would normally create - either to suppress widget
 * creation entirely or to create child widgets with a different tag. They differ from Facets in
 * that Facets are simply 'decorations' (such as button bars) to be recognized and arranged at the
 * discretion of the Layout.
 * <p>
 * This class supports setting additional metadata attributes (such as 'label') either
 * programmatically (<code>setAttribute</code>) or through the Android XML. For the latter, the XML
 * attribute name must begin with the prefix <code>attrib</code>, with the following letter
 * uppercased (ie. <code>attribLabel</code>).
 * <p>
 * Note: this class extends <code>LinearLayout</code> rather than <code>FrameLayout</code>, because
 * <code>FrameLayout</code> would <em>always</em> need to have another <code>Layout</code> embedded
 * within it, whereas <code>LinearLayout</code> is occasionally useful directly.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class Stub
	extends LinearLayout {

	//
	// Private statics
	//

	private static final String	ATTRIBUTE_PREFIX	= "attrib";

	//
	// Private members
	//

	private Map<String, String>	mAttributes;

	//
	// Constructor
	//

	public Stub( Context context ) {

		super( context );
	}

	public Stub( Context context, AttributeSet attributes ) {

		super( context, attributes );

		setTag( attributes.getAttributeValue( null, "tag" ) );

		// For each attribute...

		for ( int loop = 0, length = attributes.getAttributeCount(); loop < length; loop++ ) {
			// ...that looks like a stub attribute...

			String name = attributes.getAttributeName( loop );

			if ( !name.startsWith( ATTRIBUTE_PREFIX ) ) {
				continue;
			}

			name = name.substring( ATTRIBUTE_PREFIX.length() );

			if ( !Character.isUpperCase( name.charAt( 0 ) ) ) {
				continue;
			}

			// ...remember it

			String value = attributes.getAttributeValue( loop );
			setAttribute( StringUtils.decapitalize( name ), value );
		}
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
