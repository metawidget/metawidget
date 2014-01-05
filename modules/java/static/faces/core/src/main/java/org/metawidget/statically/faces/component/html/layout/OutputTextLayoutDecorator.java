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

package org.metawidget.statically.faces.component.html.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.layout.StaticFlatSectionLayoutDecorator;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class OutputTextLayoutDecorator
	extends StaticFlatSectionLayoutDecorator {

	//
	// Constructor
	//

	public OutputTextLayoutDecorator( LayoutDecoratorConfig<StaticWidget, StaticWidget, StaticMetawidget> config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected void addSectionWidget( String section, int level, StaticWidget container, StaticMetawidget metawidget ) {

		HtmlSectionOutputText outputText = new HtmlSectionOutputText();
		outputText.putAttribute( "value", section );

		Map<String, String> attributes = CollectionUtils.newHashMap();
		getDelegate().layoutWidget( outputText, PROPERTY, attributes, container, metawidget );
	}
}
