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

package org.metawidget.jsp.tagext.html.widgetprocessor.spring;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.tagext.MetawidgetTag;
import org.springframework.web.servlet.tags.form.HiddenInputTag;

/**
 * WidgetProcessor that sets <code>HiddenTag.setDisabled( false )</code> on all
 * <code>HiddenTag</code>s, so that they POST back.
 * <p>
 * Note: passing values via hidden tags is a potential security risk: they can be modified by
 * malicious clients before being returned to the server.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HiddenFieldProcessor
	extends org.metawidget.jsp.tagext.html.widgetprocessor.HiddenFieldProcessor {

	//
	// Protected methods
	//

	@Override
	protected Tag createHiddenTag( Map<String, String> attributes, MetawidgetTag metawidget ) {

		HiddenInputTag hiddenTag = new HiddenInputTag();
		String path = attributes.get( NAME );

		if ( metawidget.getPathPrefix() != null ) {
			path = metawidget.getPathPrefix() + path;
		}

		hiddenTag.setPath( path );
		return hiddenTag;
	}
}
