package org.metawidget.statically.jsp.html.widgetbuilder;

import org.metawidget.statically.BaseStaticXmlWidget;

/**
 * Models a plain HTML tag.
 *
 * @author Richard Kennard
 */


public class HtmlTag extends BaseStaticXmlWidget
{
   //
   // Constructor
   //

    public HtmlTag( String tagName ) {

		super( null, tagName, "http://www.w3.org/1999/xhtml" );
	}
}
