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

package org.metawidget.inspector.impl;

import java.lang.annotation.Annotation;

/**
 * Interface common to both <code>org.metawidget.inspector.impl.propertystyle.Property</code>
 * (business object properties) and <code>org.metawidget.inspector.impl.actionstyle.Action</code>
 * (business object actions).
 * <p>
 * This is essentially <code>java.lang.reflect.AnnotatedElement</code> from Java 5, but simplified
 * with fewer methods and duplicated so that we don't get a <code>NoClassDefError</code> under 1.4.
 *
 * @author Richard Kennard
 */

public interface Trait {

	//
	// Methods
	//

	String getName();

	/**
	 * Returns this element's annotation for the specified type if such an annotation is present,
	 * else null.
	 * <p>
	 * Note: for subclasses, whether anything is returned for annotations defined by the superclass
	 * is decided by the <code>java.lang.annotation.Inherited</code> annotation.
	 */

	<T extends Annotation> T getAnnotation( Class<T> annotation );

	/**
	 * Returns true if an annotation for the specified type is present on this element, else false.
	 * This method is designed primarily for convenient access to marker annotations.
	 * <p>
	 * Note: for subclasses, whether true is returned for annotations defined by the superclass is
	 * decided by the <code>java.lang.annotation.Inherited</code> annotation.
	 */

	boolean isAnnotationPresent( Class<? extends Annotation> annotation );
}
