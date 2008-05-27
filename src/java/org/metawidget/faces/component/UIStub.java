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

package org.metawidget.faces.component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.metawidget.util.CollectionUtils;

/**
 * Stub for Java Server Faces environments.
 * <p>
 * A UIStub takes a <code>value</code> binding but does nothing with it. Stubs are used to 'stub
 * out' what Metawidget would normally create - either to suppress widget creation entirely or to
 * create child widgets with a different name.
 *
 * @author Richard Kennard
 */

public class UIStub
	extends UIComponentBase
{
	//
	//
	// Private members
	//
	//

	private String	mStubAttributes;

	//
	//
	// Public methods
	//
	//

	@Override
	public String getFamily()
	{
		return "org.metawidget";
	}

	@Override
	public String getRendererType()
	{
		return "org.metawidget.Stub";
	}

	public Map<String, String> getStubAttributes()
	{
		// Static attributes

		String stubAttributes = mStubAttributes;

		// Dynamic attributes (take precedence if set)

		ValueBinding bindingStubAttributes = getValueBinding( "attributes" );

		if ( bindingStubAttributes != null )
			stubAttributes = (String) bindingStubAttributes.getValue( getFacesContext() );

		if ( stubAttributes == null )
		{
			// (use Collections.EMPTY_MAP, not Collections.emptyMap, so that we're 1.4 compatible)

			@SuppressWarnings( "unchecked" )
			Map<String, String> empty = Collections.EMPTY_MAP;
			return empty;
		}

		// Parse attributes

		// TODO: test 'attributes="required:"'

		Map<String, String> attributes = CollectionUtils.newHashMap();

		for ( String nameAndValue : CollectionUtils.fromString( stubAttributes, ';' ) )
		{
			List<String> nameAndValueList = CollectionUtils.fromString( nameAndValue, ':' );

			if ( nameAndValueList.size() != 2 )
				throw new FacesException( "Unrecognized value '" + nameAndValue + "'" );

			attributes.put( nameAndValueList.get( 0 ), nameAndValueList.get( 1 ) );
		}

		return attributes;
	}

	public void setStubAttributes( String stubAttributes )
	{
		mStubAttributes = stubAttributes;
	}

	@Override
	public Object saveState( FacesContext context )
	{
		Object values[] = new Object[2];
		values[0] = super.saveState( context );
		values[1] = mStubAttributes;

		return values;
	}

	@Override
	public void restoreState( FacesContext context, Object state )
	{
		Object values[] = (Object[]) state;
		super.restoreState( context, values[0] );

		mStubAttributes = (String) values[1];
	}
}