// Metawidget (licensed under LGPL)
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

package org.metawidget.statically.faces.component.html;

import java.util.Map;

import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.faces.component.StaticUIMetawidget;
import org.metawidget.util.ClassUtils;

/**
 * Static Metawidget for Java Server Faces environments.
 *
 * @author Richard Kennard
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
	 * Overriden to carry CSS style attributes into the nested Metawidget.
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
