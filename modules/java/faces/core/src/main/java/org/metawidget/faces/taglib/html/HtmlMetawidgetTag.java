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

package org.metawidget.faces.taglib.html;

import javax.faces.component.UIComponent;

import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.taglib.MetawidgetTag;

/**
 * JSP tag for HtmlMetawidget JSF widgets.
 * <p>
 * Includes HTML-specific attributes, such as <code>style</code> and <code>styleClass</code>.
 *
 * @author Richard Kennard
 */

public class HtmlMetawidgetTag
	extends MetawidgetTag {

	//
	// Private members
	//

	private String	mStyle;

	private String	mStyleClass;

	//
	// Public methods
	//

	@Override
	public String getComponentType() {

		return HtmlMetawidget.COMPONENT_TYPE;
	}

	public void setStyle( String style ) {

		mStyle = style;
	}

	public void setStyleClass( String styleClass ) {

		mStyleClass = styleClass;
	}

	//
	// Protected methods
	//

	@Override
	protected void setProperties( UIComponent component ) {

		super.setProperties( component );

		HtmlMetawidget metawidget = (HtmlMetawidget) component;

		// CSS

		metawidget.setStyle( mStyle );
		metawidget.setStyleClass( mStyleClass );
	}
}
