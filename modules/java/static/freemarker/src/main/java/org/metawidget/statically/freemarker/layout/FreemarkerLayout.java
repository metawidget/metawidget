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

package org.metawidget.statically.freemarker.layout;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.util.CollectionUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * Layout to arrange widgets using a FreeMarker template.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class FreemarkerLayout
	implements AdvancedLayout<StaticXmlWidget, StaticXmlWidget, StaticXmlMetawidget> {

	//
	// Private members
	//

	private Template	mTemplate;

	//
	// Constructor
	//

	/**
	 * Configure the FreemarkerLayout using the given
	 * <tt>config.getDirectoryForTemplateLoading()</tt> and <tt>config.getTemplate()</tt> (see
	 * <tt>freemarker.template.Configuration</tt>).
	 */

	public FreemarkerLayout( FreemarkerLayoutConfig config ) {

		try {
			Configuration configuration = new Configuration();
			configuration.setDirectoryForTemplateLoading( new File( config.getDirectoryForTemplateLoading() ) );
			configuration.setObjectWrapper( new DefaultObjectWrapper() );
			mTemplate = configuration.getTemplate( config.getTemplate() );
		} catch ( Exception e ) {
			throw LayoutException.newException( e );
		}
	}

	//
	// Public methods
	//

	public void onStartBuild( StaticXmlMetawidget metawidget ) {

		// Do nothing
	}

	public void startContainerLayout( StaticXmlWidget container, StaticXmlMetawidget metawidget ) {

		container.putClientProperty( FreemarkerLayout.class, null );
	}

	public void layoutWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticXmlWidget container, StaticXmlMetawidget metawidget ) {

		Map<String, Object> map = CollectionUtils.newHashMap();
		map.put( "xml", widget );
		map.put( "label", metawidget.getLabelString( attributes ) );
		map.put( "attributes", attributes );

		getState( container ).widgets.add( map );
	}

	public void endContainerLayout( StaticXmlWidget container, StaticXmlMetawidget metawidget ) {

		Map<String, Object> rootMap = CollectionUtils.newHashMap();
		rootMap.put( "widgets", getState( container ).widgets );

		Writer writer = new StringWriter();

		try {
			mTemplate.process( rootMap, writer );
			writer.flush();
		} catch ( Exception e ) {
			throw LayoutException.newException( e );
		}

		container.setTextContent( writer.toString() );
	}

	public void onEndBuild( StaticXmlMetawidget metawidget ) {

		// Do nothing
	}

	//
	// Private methods
	//

	/* package private */State getState( StaticXmlWidget container ) {

		State state = (State) container.getClientProperty( FreemarkerLayout.class );

		if ( state == null ) {
			state = new State();
			container.putClientProperty( FreemarkerLayout.class, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */static class State {

		/* package private */List<Map<String, Object>>	widgets	= CollectionUtils.newArrayList();
	}
}
