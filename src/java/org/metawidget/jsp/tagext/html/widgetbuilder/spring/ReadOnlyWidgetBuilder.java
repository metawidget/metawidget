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

package org.metawidget.jsp.tagext.html.widgetbuilder.spring;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.LiteralTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.BaseHtmlMetawidgetTag;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springframework.web.servlet.tags.form.HiddenInputTag;

/**
 * ReadOnlyWidgetBuilder for Spring environments.
 *
 * @author Richard Kennard
 */

public class ReadOnlyWidgetBuilder
	extends org.metawidget.jsp.tagext.html.widgetbuilder.ReadOnlyWidgetBuilder {

	//
	// Protected methods
	//

	@Override
	protected Tag writeReadOnlyTag( Map<String, String> attributes, MetawidgetTag metawidget ) {

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		// Use the Spring binder to render the read-only value

		RequestContext requestContext = (RequestContext) metawidget.getPageContext().getAttribute( RequestContextAwareTag.REQUEST_CONTEXT_PAGE_ATTRIBUTE );
		String path = metawidget.getPath() + StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME );
		String value = requestContext.getBindStatus( path ).getDisplayValue();

		// Support lookup labels

		String lookupLabels = attributes.get( LOOKUP_LABELS );

		if ( lookupLabels != null ) {
			List<String> lookupList = CollectionUtils.fromString( attributes.get( LOOKUP ) );
			int indexOf = lookupList.indexOf( value );

			if ( indexOf != -1 ) {
				List<String> lookupLabelsList = CollectionUtils.fromString( lookupLabels );

				if ( indexOf < lookupLabelsList.size() ) {
					value = lookupLabelsList.get( indexOf );
				}
			}
		}

		buffer.append( value );

		// May need a hidden input tag too

		if ( ( (BaseHtmlMetawidgetTag) metawidget ).isCreateHiddenFields() && !TRUE.equals( attributes.get( NO_SETTER ) ) ) {
			HiddenInputTag hiddenTag = new HiddenInputTag();
			path = attributes.get( NAME );

			if ( metawidget.getPathPrefix() != null ) {
				path = metawidget.getPathPrefix() + path;
			}

			hiddenTag.setPath( path );

			try {
				buffer.append( JspUtils.writeTag( metawidget.getPageContext(), hiddenTag, metawidget, null ) );
			} catch ( JspException e ) {
				throw WidgetBuilderException.newException( e );
			}
		}

		return new LiteralTag( buffer.toString() );
	}
}
