// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.statically.jsp.widgetbuilder;

import org.metawidget.statically.BaseStaticXmlWidget;

/**
 * Widgets within the JSTL c: namespace.
 *
 * @author Ryan Bradley
 */

public abstract class CoreWidget
    extends BaseStaticXmlWidget {

    //
    // Constructor
    //

    protected CoreWidget( String tagName ) {

        super( "c", tagName, "http://java.sun.com/jsp/jstl/core" );
    }
}
