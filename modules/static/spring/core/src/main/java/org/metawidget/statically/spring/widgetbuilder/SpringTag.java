package org.metawidget.statically.spring.widgetbuilder;

import org.metawidget.statically.BaseStaticXmlWidget;

/**
 * Tags within the Spring form: namespace.
 *
 * @author Richard Kennard
 */

public abstract class SpringTag
			extends BaseStaticXmlWidget {

	//
	// Constructor
	//

	public SpringTag( String tagName ) {

		super( "form", tagName, "http://www.springframework.org/tags/form" );
	}
}
