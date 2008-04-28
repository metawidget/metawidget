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

package org.metawidget.faces.component.html;

import java.util.List;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * Component to output text based on a lookup value.
 *
 * @author Richard Kennard
 */

public class HtmlLookupOutputText
	extends HtmlOutputText
{
	//
	//
	// Private members
	//
	//

	private List<String>	mValues;

	private List<String>	mLabels;

	//
	//
	// Public methods
	//
	//

	public void setLabels( List<String> values, List<String> labels )
	{
		mValues = values;
		mLabels = labels;
	}

	@Override
	public Object getValue()
	{
		Object value = super.getValue();

		if ( value == null )
			return null;

		if ( mValues != null && mLabels != null && !mLabels.isEmpty() )
		{
			// Convert as necessary

			FacesContext context = FacesContext.getCurrentInstance();

			if ( getConverter() != null )
			{
				value = getConverter().getAsString( FacesContext.getCurrentInstance(), this, value );
			}
			else
			{
				Converter converter = context.getApplication().createConverter( value.getClass() );

				if ( converter != null )
					value = converter.getAsString( FacesContext.getCurrentInstance(), this, value );
				else
					value = String.valueOf( value );
			}

			// Map to lookup value

			int indexOf = mValues.indexOf( value );

			if ( indexOf >= 0 && indexOf < mLabels.size() )
				value = mLabels.get( indexOf );
		}

		return value;
	}

	@Override
	public Object saveState( FacesContext context )
	{
		Object values[] = new Object[3];
		values[0] = super.saveState( context );
		values[1] = mValues;
		values[2] = mLabels;

		return values;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void restoreState( FacesContext context, Object state )
	{
		Object values[] = (Object[]) state;
		super.restoreState( context, values[0] );

		mValues = (List<String>) values[1];
		mLabels = (List<String>) values[2];
	}
}
