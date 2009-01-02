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

package org.metawidget.example.gwt.addressbook.server.tagext;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.metawidget.util.CollectionUtils;

/**
 * Tag to render a ResourceBundle as a JavaScript variable.
 * <p>
 * This allows us to reuse our
 * <code>org.metawidget.example.shared.addressbook.resource.Resources</code> within the GWT
 * <code>Dictionary</code> mechanism by using <code>GwtMetawidget.setDictionaryName</code>, thus
 * reducing unnecessary duplication.
 *
 * @author Richard Kennard
 */

public class Bundle2VariableTag
	extends TagSupport
{
	//
	// Private statics
	//

	private static final long	serialVersionUID		= 1l;

	private final static String	DEFAULT_VARIABLE_NAME	= "bundle";

	//
	// Private members
	//

	private ResourceBundle		mBundle;

	private String				mVariableName			= DEFAULT_VARIABLE_NAME;

	//
	// Public methods
	//

	public void setBundle( ResourceBundle bundle )
	{
		mBundle = bundle;
	}

	public void setVariableName( String variableName )
	{
		mVariableName = variableName;
	}

	@Override
	public int doEndTag()
		throws JspException
	{
		if ( mBundle == null )
			throw new JspException( "Bundle is required" );

		if ( mVariableName == null || "".equals( mVariableName ) )
			throw new JspException( "Variable name is required" );

		try
		{
			JspWriter writer = pageContext.getOut();

			writer.write( "<script type=\"text/javascript\">\n\tvar " );
			writer.write( mVariableName );
			writer.write( " = {\n" );

			List<String> keys = CollectionUtils.newArrayList( mBundle.keySet());
			Collections.sort( keys );

			for ( int loop = 0, length = keys.size(); loop < length; loop++ )
			{
				String key = keys.get( loop );
				writer.write( "\t\"" );
				writer.write( key );
				writer.write( "\": \"" );
				writer.write( mBundle.getString( key ) );
				writer.write( "\"" );

				if ( loop < length - 1 )
					writer.write( "," );

				writer.write( "\n" );
			}

			writer.write( "};\n</script>\n" );
		}
		catch ( IOException e )
		{
			throw new JspException( e );
		}

		return super.doEndTag();
	}

	@Override
	public void release()
	{
		super.release();

		mBundle = null;
		mVariableName = DEFAULT_VARIABLE_NAME;
	}
}
