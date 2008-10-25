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

package org.metawidget.inspector.commons.validator;

import java.util.Map;

import org.metawidget.inspector.ConfigReader;
import org.metawidget.inspector.ResourceResolver;
import org.metawidget.inspector.impl.BaseXmlInspector;
import org.w3c.dom.Element;

/**
 * Inspector to look for relevant settings in validator.xml.
 *
 * @author Richard Kennard
 */

public class CommonsValidatorInspector
	extends BaseXmlInspector
{
	//
	// Constructor
	//

	public CommonsValidatorInspector()
	{
		this( new CommonsValidatorInspectorConfig() );
	}

	public CommonsValidatorInspector( CommonsValidatorInspectorConfig config )
	{
		this( config, new ConfigReader() );
	}

	public CommonsValidatorInspector( ResourceResolver resolver )
	{
		this( new CommonsValidatorInspectorConfig(), resolver );
	}

	public CommonsValidatorInspector( CommonsValidatorInspectorConfig config, ResourceResolver resolver )
	{
		super( config, resolver );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectProperty( Element toInspect )
	{
		return null;
	}
}
