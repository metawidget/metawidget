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

package org.metawidget.inspector.commons.jexl;

import org.metawidget.inspector.impl.BaseXmlInspectorConfig;

/**
 * Configures an JexlXmlInspector prior to use. Once instantiated, Inspectors are immutable.
 * <p>
 * By default, looks for a file called <code>metawidget-metadata.xml</code>. This is the same
 * default used by <code>XmlInspector</code>, so that <code>JexlInspector</code> can be used
 * as a drop-in replacement for <code>XmlInspector</code>.
 *
 * @author Richard Kennard
 */

public class JexlXmlInspectorConfig
	extends BaseXmlInspectorConfig
{
	//
	// Constructor
	//

	public JexlXmlInspectorConfig()
	{
		setFile( "metawidget-metadata.xml" );
	}
}
