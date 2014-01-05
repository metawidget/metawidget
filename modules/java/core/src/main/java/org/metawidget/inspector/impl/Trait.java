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

package org.metawidget.inspector.impl;

import java.lang.annotation.Annotation;

/**
 * Interface common to both <code>org.metawidget.inspector.impl.propertystyle.Property</code>
 * (domain object properties) and <code>org.metawidget.inspector.impl.actionstyle.Action</code>
 * (domain object actions).
 * <p>
 * This is essentially <code>java.lang.reflect.AnnotatedElement</code>, but simplified with fewer
 * methods.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
