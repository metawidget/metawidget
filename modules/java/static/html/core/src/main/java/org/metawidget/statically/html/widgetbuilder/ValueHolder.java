// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.statically.html.widgetbuilder;

/**
 * Marks that the widget can contain an attribute 'value'.
 *
 * @author Ryan Bradley
 */

public interface ValueHolder {

    //
    // Methods
    //

    void setValue( String value );

    String getValue();
}
