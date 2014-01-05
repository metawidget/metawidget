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
