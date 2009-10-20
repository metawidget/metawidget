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
public @interface UiWide
{
	// Just a marker annotation
}
