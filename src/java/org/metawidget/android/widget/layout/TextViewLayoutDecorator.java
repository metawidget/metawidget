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
 * @author Richard Kennard
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
	protected ViewGroup createSectionWidget( ViewGroup previousSectionView, Map<String, String> attributes, ViewGroup container, AndroidMetawidget metawidget ) {

		TextView textView = new TextView( metawidget.getContext() );

		// Apply style (if any)

		AndroidUtils.applyStyle( textView, mStyle, metawidget );

		// Section name (possibly localized)

		String section = getState( container, metawidget ).currentSection;
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
