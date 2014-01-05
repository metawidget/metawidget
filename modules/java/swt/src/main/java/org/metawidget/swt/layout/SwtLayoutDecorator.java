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

package org.metawidget.swt.layout;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.metawidget.swt.SwtMetawidget;

/**
 * Common interface implemented by all SWT LayoutDecorators.
 * <p>
 * All SWT Controls require a Composite be passed into their constructor. This means the Composite
 * must be known in advance, which prevents one from either creating a Control independent of a
 * Composite, or moving a Control from one Composite to another. This poses a challenge for
 * <code>LayoutDecorators</code>, which decide how to decorate a Control <em>after</em> it has been
 * built by the <code>WidgetBuilder</code>.
 * <p>
 * To work around this, SWT <code>LayoutDecorators</code> implement an additional lifecycle hook
 * that allows them to examine the attributes and decide on a suitable Composite before
 * <code>buildWidget</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface SwtLayoutDecorator {

	//
	// Methods
	//

	Composite startBuildWidget( String elementName, Map<String, String> attributes, Composite container, SwtMetawidget metawidget );
}
