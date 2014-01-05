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

package org.metawidget.iface;

/**
 * Interface to indicate a class (ie. an <code>Inspector</code>, a <code>Layout</code> etc) should
 * be considered immutable.
 * <p>
 * Immutable classes should:
 * <ul>
 * <li>not provide any setter methods or mutators</li>
 * <li>make defensive copies for any getter methods</li>
 * <li>mark all fields final and private</li>
 * </ul>
 * <p>
 * Note: this interface is not an annotation because we want it to be automatically inherited when
 * placed on interfaces (such as <code>Inspector</code>). Even the <code>Inherited</code> annotation
 * does not propagate from interfaces.
 * <p>
 * <strong>This is an internal API and is subject to change. Clients should use one of its
 * subinterfaces (ie. <code>Inspector</code>, <code>Layout</code> etc) instead.</strong>
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface Immutable {
	// Just a marker interface
}
