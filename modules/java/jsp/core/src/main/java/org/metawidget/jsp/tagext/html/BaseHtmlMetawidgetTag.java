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

package org.metawidget.jsp.tagext.html;

import java.util.Map;

import org.metawidget.jsp.tagext.MetawidgetTag;

/**
 * Base Metawidget for JSP environments that output HTML.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class BaseHtmlMetawidgetTag
	extends MetawidgetTag {

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
