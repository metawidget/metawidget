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

package org.metawidget.inspector.xml;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.inspector.impl.BaseXmlInspector;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * Inspects <code>inspection-result-1.0.xsd</code>-compliant files (such as
 * <code>metawidget-metadata.xml</code>).
 * <p>
 * XmlInspector is a very simple Inspector: it takes as its input XML in the same format that
 * Inspectors usually output.
 * <p>
 * Note: this class does not extend <code>ValidatingXmlInspector</code> as we want to be able
 * to use it in Android environments.
 *
 * @author Richard Kennard
 */

public class XmlInspector
	extends BaseXmlInspector
{
	//
	// Constructors
	//

	public XmlInspector( XmlInspectorConfig config )
	{
		super( config );
	}

	//
	// Public methods
	//

	@Override
	protected String getExtendsAttribute()
	{
		return "extends";
	}

	@Override
	protected Map<String, String> inspectProperty( Element toInspect )
	{
		if ( PROPERTY.equals( toInspect.getNodeName() ))
			return XmlUtils.getAttributesAsMap( toInspect );

		return null;
	}

	@Override
	protected Map<String, String> inspectAction( Element toInspect )
	{
		if ( ACTION.equals( toInspect.getNodeName() ))
			return XmlUtils.getAttributesAsMap( toInspect );

		return null;
	}
}
