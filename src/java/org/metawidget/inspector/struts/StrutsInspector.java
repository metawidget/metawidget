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

package org.metawidget.inspector.struts;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

import org.metawidget.inspector.ConfigReader;
import org.metawidget.inspector.ResourceResolver;
import org.metawidget.inspector.impl.AbstractXmlInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Inspector to look for relevant settings in struts-config.xml files.
 *
 * @author Richard Kennard
 */

public class StrutsInspector
	extends AbstractXmlInspector
{
	//
	//
	// Private statics
	//
	//

	private final static String	FORM_BEANS_ELEMENT	= "form-beans";

	//
	//
	// Constructor
	//
	//

	public StrutsInspector()
	{
		this( new StrutsInspectorConfig() );
	}

	public StrutsInspector( StrutsInspectorConfig config )
	{
		this( config, new ConfigReader() );
	}

	public StrutsInspector( ResourceResolver resolver )
	{
		this( new StrutsInspectorConfig(), resolver );
	}

	public StrutsInspector( StrutsInspectorConfig config, ResourceResolver resolver )
	{
		super( config, resolver );
	}

	//
	//
	// Protected methods
	//
	//

	@Override
	protected Element getDocumentElement( DocumentBuilder builder, ResourceResolver resolver, InputStream... files )
		throws Exception
	{
		Document document = builder.newDocument();
		Element root = document.createElement( FORM_BEANS_ELEMENT );
		document.appendChild( root );

		for ( InputStream file : files )
		{
			Document documentParsed = builder.parse( file );
			Element formBeans = XmlUtils.getChildNamed( documentParsed.getDocumentElement(), FORM_BEANS_ELEMENT );

			if ( formBeans == null )
				continue;

			XmlUtils.combineElements( root, formBeans, getTopLevelTypeAttribute(), getNameAttribute() );
		}

		return root;
	}

	@Override
	protected Map<String, String> inspect( Element toInspect )
	{
		if ( !"form-property".equals( toInspect.getNodeName() ) )
			return null;

		Map<String, String> attributes = CollectionUtils.newHashMap();

		if ( toInspect.hasAttribute( NAME ) )
			attributes.put( NAME, toInspect.getAttribute( NAME ) );

		if ( toInspect.hasAttribute( TYPE ) )
			attributes.put( TYPE, toInspect.getAttribute( TYPE ) );

		return attributes;
	}

	/**
	 * Overriden to search by <code>name=</code>, not <code>type=</code>.
	 * <p>
	 * This is because <code>struts-config.xml</code> requires <code>form-beans/name</code> be
	 * unique, whereas <code>form-beans/type</code> can be, say, <code>DynaActionForm</code>.
	 */

	@Override
	protected String getTopLevelTypeAttribute()
	{
		return NAME;
	}
}
