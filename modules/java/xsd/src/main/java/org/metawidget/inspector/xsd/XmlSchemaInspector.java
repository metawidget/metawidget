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

package org.metawidget.inspector.xsd;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.inspector.impl.BaseXmlInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * Inspector to look for metadata in XML Schema files.
 *
 * @author Richard Kennard
 */

public class XmlSchemaInspector
	extends BaseXmlInspector {

	//
	// Constructor
	//

	public XmlSchemaInspector( XmlSchemaInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	/**
	 * Overridden to search by <code>name=</code>, not <code>type=</code>.
	 */

	@Override
	protected String getTopLevelTypeAttribute() {

		return NAME;
	}

	// TODO: beef this up!

	@Override
	protected void inspectTraits( Element toInspect, Element toAddTo ) {

		if ( toInspect == null ) {
			return;
		}

		// Skip over 'complexType' and 'sequence' elements

		Element complexType = XmlUtils.getFirstChildElement( toInspect );
		Element sequence = XmlUtils.getFirstChildElement( complexType );

		super.inspectTraits( sequence, toAddTo );
	}

	@Override
	protected Map<String, String> inspectProperty( Element toInspect ) {

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, toInspect.getAttribute( NAME ));

		// Required

		String notNull = toInspect.getAttribute( "not-null" );

		if ( !"".equals( notNull ) && Integer.parseInt( notNull ) > 0 ) {
			attributes.put( REQUIRED, TRUE );
		}

		return attributes;
	}
}
