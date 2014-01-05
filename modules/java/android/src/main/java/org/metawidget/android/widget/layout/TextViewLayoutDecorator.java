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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.android.AndroidUtils;
import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Layout to decorate widgets from different sections using a TextView.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TextViewLayoutDecorator
	extends AndroidNestedSectionLayoutDecorator {

	//
	// Private members
	//

	private int	mStyle;

	//
	// Constructor
	//

	public TextViewLayoutDecorator( TextViewLayoutDecoratorConfig config ) {

		super( config );

		mStyle = config.getStyle();
	}

	//
	// Protected methods
	//

	@Override
	protected ViewGroup createSectionWidget( ViewGroup previousSectionView, String section, Map<String, String> attributes, ViewGroup container, AndroidMetawidget metawidget ) {

		TextView textView = new TextView( metawidget.getContext() );

		// Apply style (if any)

		AndroidUtils.applyStyle( textView, mStyle, metawidget );

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		textView.setText( localizedSection, TextView.BufferType.SPANNABLE );

		// Add to parent container

		Map<String, String> sectionAttributes = CollectionUtils.newHashMap();
		sectionAttributes.put( LABEL, "" );
		sectionAttributes.put( LARGE, TRUE );
		getDelegate().layoutWidget( textView, PROPERTY, sectionAttributes, container, metawidget );

		android.widget.LinearLayout newLayout = new android.widget.LinearLayout( metawidget.getContext() );
		newLayout.setOrientation( LinearLayout.VERTICAL );
		getDelegate().layoutWidget( newLayout, PROPERTY, sectionAttributes, container, metawidget );

		return newLayout;
	}
}
