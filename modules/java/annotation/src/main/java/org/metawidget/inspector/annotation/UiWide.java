// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.inspector.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates the field should be 'wide' in the UI, spanning all columns in a multi-column layout.
 * <p>
 * UiWide is different to UiLarge, because 'large' implies a data size (ie. BLOB or CLOB) whereas
 * 'wide' refers purely to spanning columns. Generally all 'large' fields are implicitly 'wide', but
 * not all 'wide' fields are 'large'. For example, you may want a normal text field (not a textarea)
 * to span all columns.
 *
 * @author Illya Yalovyy
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiWide {
	// Just a marker annotation
}
