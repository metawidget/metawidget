// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.faces.component.html;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.util.ClassUtils;

/**
 * Metawidget for Java Server Faces environments.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlMetawidget
	extends UIMetawidget {

	//
	// Public statics
	//

	@SuppressWarnings( "hiding" )
	public static final String	COMPONENT_TYPE	= "org.metawidget.HtmlMetawidget";

	//
	// Private members
	//

	private String				mStyle;

	private String				mStyleClass;

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

	@Override
	public String getComponentType() {

		return COMPONENT_TYPE;
	}

	@Override
	public Object saveState( FacesContext context ) {

		Object[] values = new Object[3];
		values[0] = super.saveState( context );
		values[1] = mStyle;
		values[2] = mStyleClass;

		return values;
	}

	@Override
	public void restoreState( FacesContext context, Object state ) {

		Object[] values = (Object[]) state;
		super.restoreState( context, values[0] );

		mStyle = (String) values[1];
		mStyleClass = (String) values[2];
	}

	//
	// Protected methods
	//

	@Override
	protected String getDefaultConfiguration() {

		return ClassUtils.getPackagesAsFolderNames( HtmlMetawidget.class ) + "/metawidget-html-default.xml";
	}

	/**
	 * Overridden to carry CSS style attributes into the nested Metawidget.
	 */

	@Override
	public void initNestedMetawidget( UIMetawidget metawidget, Map<String, String> attributes ) {

		super.initNestedMetawidget( metawidget, attributes );

		// Attributes

		((HtmlMetawidget) metawidget).setStyle( mStyle );
		((HtmlMetawidget) metawidget).setStyleClass( mStyleClass );
	}
}
