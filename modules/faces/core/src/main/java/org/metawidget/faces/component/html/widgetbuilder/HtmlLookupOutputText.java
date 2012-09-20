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

package org.metawidget.faces.component.html.widgetbuilder;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;

/**
 * Component to output text based on a lookup value.
 * <p>
 * <strong>This is an internal API and is subject to change.</strong>
 *
 * @author Richard Kennard
 */

public class HtmlLookupOutputText
	extends HtmlOutputText {

	//
	// Public statics
	//

	/**
	 * Not referenced in code, but in <code>faces-config.xml</code>.
	 */

	@SuppressWarnings( "hiding" )
	public static final String	COMPONENT_TYPE	= "org.metawidget.HtmlLookupOutputText";

	//
	// Private members
	//

	private List<String>		mValues;

	private List<String>		mLabels;

	//
	// Public methods
	//

	public void setLabels( List<String> values, List<String> labels ) {

		mValues = values;
		mLabels = labels;

		if ( mValues.size() != mLabels.size() ) {
			throw WidgetBuilderException.newException( "There are " + mValues.size() + " possible values, but " + mLabels.size() + " possible labels" );
		}
	}

	@Override
	public Object getValue() {

		Object value = super.getValue();

		if ( value == null ) {
			return null;
		}

		// Special support for Collections

		if ( value instanceof Collection<?> ) {
			Collection<?> values = (Collection<?>) value;

			if ( values.isEmpty() ) {
				return null;
			}

			try {
				boolean gotConverter = false;
				Converter converter = null;
				Collection<Object> labels;

				// Try to return the same Collection type. But don't do
				// value.getClass().newInstance() because it might be a PersistentSet or something

				if ( value instanceof Set<?> ) {
					labels = CollectionUtils.newHashSet();
				} else {
					labels = CollectionUtils.newArrayList();
				}

				for ( Object itemValue : values ) {
					Object label = null;

					if ( itemValue != null ) {
						if ( !gotConverter ) {
							converter = getFacesContext().getApplication().createConverter( itemValue.getClass() );
							gotConverter = true;
						}

						label = convertAndLookup( converter, itemValue );
					}

					labels.add( label );
				}

				return labels;
			} catch ( Exception e ) {
				throw WidgetBuilderException.newException( e );
			}
		}

		// Support regular, non-Collections

		Converter converter = getConverter();

		if ( converter == null ) {
			converter = getFacesContext().getApplication().createConverter( value.getClass() );
		}

		return convertAndLookup( converter, value );
	}

	@Override
	public Object saveState( FacesContext context ) {

		Object values[] = new Object[3];
		values[0] = super.saveState( context );
		values[1] = mValues;
		values[2] = mLabels;

		return values;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public void restoreState( FacesContext context, Object state ) {

		Object values[] = (Object[]) state;
		super.restoreState( context, values[0] );

		mValues = (List<String>) values[1];
		mLabels = (List<String>) values[2];
	}

	//
	// Private members
	//

	private Object convertAndLookup( Converter converter, Object value ) {

		// Convert...

		String convertedValue;

		if ( converter != null ) {
			convertedValue = converter.getAsString( getFacesContext(), this, value );
		} else {
			convertedValue = String.valueOf( value );
		}

		// ...and lookup

		int indexOf = mValues.indexOf( convertedValue );

		if ( indexOf != -1 ) {
			return mLabels.get( indexOf );
		}

		// There may be no such lookup. It is tempting to error at this point, but that gets
		// icky with enums, who are already converted to their .toString by EnumConverter

		return value;
	}
}
