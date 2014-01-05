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

package org.metawidget.faces.taglib.html;

import javax.faces.component.UIComponent;

import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.taglib.MetawidgetTag;

/**
 * JSP tag for HtmlMetawidget JSF widgets.
 * <p>
 * Includes HTML-specific attributes, such as <code>style</code> and <code>styleClass</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
