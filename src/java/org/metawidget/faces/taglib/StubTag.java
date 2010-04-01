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

import org.metawidget.faces.component.UIStub;
import org.metawidget.iface.MetawidgetException;

/**
 * JSP tag for UIStub widget.
 *
 * @author Richard Kennard
 */

@SuppressWarnings( "deprecation" )
public class StubTag
	extends UIComponentTag
{
	//
	// Private members
	//

	private String	mAction;

	private String	mValue;

	private String	mAttributes;

	//
	// Public methods
	//

	@Override
	public String getComponentType()
	{
		return "org.metawidget.Stub";
	}

	public void setAction( String action )
	{
		mAction = action;
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
	// Protected methods
	//

	@Override
	protected void setProperties( UIComponent component )
	{
		super.setProperties( component );

		UIStub componentStub = (UIStub) component;
		Application application = getFacesContext().getApplication();

		// Action

		if ( mAction != null )
		{
			if ( !isValueReference( mAction ) )
				throw MetawidgetException.newException( "Action '" + mAction + "' must be an EL expression" );

			componentStub.setAction( application.createMethodBinding( mAction, null ) );
		}

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
