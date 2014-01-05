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

package org.metawidget.android.widget.layout;

import java.util.Map;

import org.metawidget.android.AndroidUtils;
import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.Stub;
import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.util.simple.SimpleLayoutUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Layout to arrange widgets vertically, with one row for the label and the next for the widget.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class LinearLayout
	implements AdvancedLayout<View, ViewGroup, AndroidMetawidget> {

	//
	// Private members
	//

	private final int	mLabelStyle;

	//
	// Constructor
	//

	public LinearLayout() {

		this( new LinearLayoutConfig() );
	}

	public LinearLayout( LinearLayoutConfig config ) {

		mLabelStyle = config.getLabelStyle();
	}

	//
	// Public methods
	//

	public void onStartBuild( AndroidMetawidget metawidget ) {

		// Do nothing
	}

	public void startContainerLayout( ViewGroup container, AndroidMetawidget metawidget ) {

		// Do nothing
	}

	public void layoutWidget( View view, String elementName, Map<String, String> attributes, ViewGroup container, AndroidMetawidget metawidget ) {

		// Ignore empty Stubs

		if ( view instanceof Stub && ( (Stub) view ).getChildCount() == 0 ) {
			return;
		}

		ViewGroup viewToAddTo = newViewToAddTo( container );

		String labelText = metawidget.getLabelString( attributes );
		boolean needsLabel = SimpleLayoutUtils.needsLabel( labelText, elementName );

		// Labels

		if ( needsLabel ) {
			TextView textView = new TextView( metawidget.getContext() );
			textView.setText( labelText + ": " );

			AndroidUtils.applyStyle( textView, mLabelStyle, metawidget );

			viewToAddTo.addView( textView );
		}

		// View

		layoutWidget( view, viewToAddTo, container, needsLabel );
	}

	public void endContainerLayout( ViewGroup container, AndroidMetawidget metawidget ) {

		// Do nothing
	}

	public void onEndBuild( AndroidMetawidget metawidget ) {

		View viewButtons = metawidget.getFacet( "buttons" );

		if ( viewButtons != null ) {
			metawidget.addView( viewButtons, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
		}
	}

	//
	// Protected methods
	//

	/**
	 * @param viewToAddTo
	 *            the view to add to. This may be different from container. For example it may be a
	 *            TableRow
	 * @param hasLabel
	 *            whether the view has a label. This may affect how it is laid out. For example a
	 *            TableRow may need to span two columns
	 */

	protected void layoutWidget( View view, ViewGroup viewToAddTo, ViewGroup container, boolean hasLabel ) {

		android.view.ViewGroup.LayoutParams params = view.getLayoutParams();

		if ( params == null ) {
			// Hack for sizing ListViews

			if ( view instanceof ListView ) {
				params = new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 100 );
			} else {
				params = new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
			}
		}

		getLayout( container ).addView( view, params );
	}

	//
	// Protected methods
	//

	protected ViewGroup newViewToAddTo( ViewGroup metawidget ) {

		// AndroidMetawidget is already a LinearLayout

		return metawidget;
	}

	protected ViewGroup getLayout( ViewGroup container ) {

		// AndroidMetawidget is already a LinearLayout

		return container;
	}
}
