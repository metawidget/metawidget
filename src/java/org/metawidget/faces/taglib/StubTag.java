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

package org.metawidget.faces.taglib;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import org.metawidget.MetawidgetException;
import org.metawidget.faces.component.UIStub;

/**
 * JSP tag for UIStub widget.
 * <p>
 * Note there are no equivalent <code>ParamTag</code> and <code>FacetTag</code>s,
 * as there are for other environments, because Java Server Faces itself already
 * supplies <code>f:param</code> and <code>f:facet</code>.
 *
 * @author Richard Kennard
 */

public class StubTag
	extends UIComponentTag
{
	//
	//
	// Private members
	//
	//

	private String	mValue;

	private String	mAttributes;

	//
	//
	// Public methods
	//
	//

	@Override
	public String getComponentType()
	{
		return "org.metawidget.Stub";
	}

	public void setValue( String value )
	{
		mValue = value;
	}

	public void setAttributes( String attributes )
	{
		mAttributes = attributes;
	}

	@Override
	public String getRendererType()
	{
		return null;
	}

	//
	//
	// Protected methods
	//
	//

	@Override
	protected void setProperties( UIComponent component )
	{
		super.setProperties( component );

		UIStub componentStub = (UIStub) component;
		Application application = getFacesContext().getApplication();

		// Value

		if ( mValue != null )
		{
			if ( !isValueReference( mValue ) )
				throw MetawidgetException.newException( "Value '" + mValue + "' must be an EL expression" );

			componentStub.setValueBinding( "value", application.createValueBinding( mValue ) );
		}

		// Attributes

		if ( mAttributes != null )
		{
			if ( isValueReference( mAttributes ) )
				componentStub.setValueBinding( "attributes", application.createValueBinding( mAttributes ) );
			else
				componentStub.setStubAttributes( mAttributes );
		}
	}
}
