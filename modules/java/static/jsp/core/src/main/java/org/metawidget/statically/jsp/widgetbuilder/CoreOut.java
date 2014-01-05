// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.statically.jsp.widgetbuilder;

import org.metawidget.statically.html.widgetbuilder.ValueHolder;

/**
 * @author Ryan Bradley
 */

public class CoreOut
    extends CoreWidget
    implements ValueHolder {

    //
    // Constructor
    //

    public CoreOut() {

        super( "out" );
    }

    //
    // Public methods
    //

    public void setValue(String value) {

        putAttribute( "value" , value );
    }

    public String getValue() {

        return getAttribute( "value" );
    }

}