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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.metawidget.inspector.Inspector;
import org.metawidget.inspector.InspectorException;

/**
 * Convenience implementation for Inspectors.
 * <p>
 * Handles setting up the DocumentFactory for returning the DOM.
 *
 * @author Richard Kennard
 */

public abstract class AbstractInspector
	implements Inspector
{
	//
	//
	// Private statics
	//
	//

	private final static DocumentBuilderFactory	DOCUMENT_BUILDER_FACTORY;

	static
	{
		DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
		DOCUMENT_BUILDER_FACTORY.setNamespaceAware( true );
	}

	//
	//
	// Protected methods
	//
	//

	protected DocumentBuilder newDocumentBuilder()
	{
		try
		{
			return DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
		}
		catch ( Exception e )
		{
			throw InspectorException.newException( e );
		}
	}
}
