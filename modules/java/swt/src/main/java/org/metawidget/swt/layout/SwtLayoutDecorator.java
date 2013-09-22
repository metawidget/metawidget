// Metawidget (licensed under LGPL)
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
