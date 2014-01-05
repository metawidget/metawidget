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

package org.metawidget.jsp.tagext.layout;

import java.util.Map;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.layout.iface.Layout;
import org.metawidget.layout.iface.LayoutException;

/**
 * Layout to simply output components one after another, with no labels and no structure.
 * <p>
 * This Layout is suited to rendering single components, or for rendering components whose layout
 * relies entirely on CSS.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SimpleLayout
	implements Layout<Tag, BodyTag, MetawidgetTag> {

	//
	// Public methods
	//

	public void layoutWidget( Tag tag, String elementName, Map<String, String> attributes, BodyTag containerTag, MetawidgetTag metawidgetTag ) {

		try {
			JspWriter writer = metawidgetTag.getPageContext().getOut();
			writer.write( JspUtils.writeTag( metawidgetTag.getPageContext(), tag, containerTag ) );
		} catch ( Exception e ) {
			throw LayoutException.newException( e );
		}
	}
}
