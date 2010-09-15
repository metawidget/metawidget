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

package org.metawidget.jsp.tagext.html;

import java.util.Map;

import org.metawidget.jsp.tagext.MetawidgetTag;

/**
 * Base Metawidget for JSP environments that output HTML.
 *
 * @author Richard Kennard
 */

public abstract class BaseHtmlMetawidgetTag
	extends MetawidgetTag {

	//
	// Private statics
	//

	private static final long	serialVersionUID	= 1l;

	//
	// Protected members
	//

	protected String			mStyle;

	protected String			mStyleClass;

	//
	// Public methods
	//

	public String getStyle() {

		return mStyle;
	}

	public void setStyle( String style ) {

		mStyle = style;
	}

	public String getStyleClass() {

		return mStyleClass;
	}

	public void setStyleClass( String styleClass ) {

		mStyleClass = styleClass;
	}

	//
	// Protected methods
	//

	@Override
	protected void initNestedMetawidget( MetawidgetTag metawidget, Map<String, String> attributes ) {

		super.initNestedMetawidget( metawidget, attributes );

		BaseHtmlMetawidgetTag tag = (BaseHtmlMetawidgetTag) metawidget;

		tag.setStyle( mStyle );
		tag.setStyleClass( mStyleClass );
	}
}
