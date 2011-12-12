package org.metawidget.statically.spring.widgetbuilder;

import org.metawidget.statically.BaseStaticXmlWidget;

/**
 * Tags within the Spring form: namespace.
 *
 * @author Richard Kennard
 */

public abstract class SpringFormTag
			extends BaseStaticXmlWidget {

	//
	// Constructor
	//

	public SpringFormTag( String tagName ) {

		super( "form", tagName, "http://www.springframework.org/tags/form" );
	}
}
