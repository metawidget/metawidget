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

package org.metawidget.statically.faces.component.html;

import java.util.Map;

import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.faces.component.StaticUIMetawidget;
import org.metawidget.util.ClassUtils;

/**
 * Static Metawidget for Java Server Faces environments.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StaticHtmlMetawidget
	extends StaticUIMetawidget {

	//
	// Private members
	//

	private String	mStyle;

	private String	mStyleClass;

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

	/**
	 * Overridden to carry CSS style attributes into the nested Metawidget.
	 */

	@Override
	public void initNestedMetawidget( StaticMetawidget nestedMetawidget, Map<String, String> attributes ) {

		super.initNestedMetawidget( nestedMetawidget, attributes );

		StaticHtmlMetawidget htmlMetawidget = (StaticHtmlMetawidget) nestedMetawidget;

		// Attributes

		htmlMetawidget.setStyle( mStyle );
		htmlMetawidget.setStyleClass( mStyleClass );
	}

	//
	// Protected methods
	//

	@Override
	protected String getDefaultConfiguration() {

		return ClassUtils.getPackagesAsFolderNames( StaticHtmlMetawidget.class ) + "/metawidget-static-html-default.xml";
	}
}
