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

package org.metawidget.statically;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public abstract class StaticXmlWidget
	extends StaticWidget {

	//
	// Private members
	//

	private String				mTagPrefix;

	private String				mTagName;

	private Map<String, String>	mAttributes;

	//
	// Constructor
	//

	public StaticXmlWidget( String tagName ) {

		mTagName = tagName;
	}

	//
	// Public methods
	//

	public void setTagPrefix( String tagPrefix ) {

		mTagPrefix = tagPrefix;
	}

	public void putAttribute( String name, String value ) {

		if ( mAttributes == null ) {

			// (use TreeMap for consistent unit tests)

			mAttributes = CollectionUtils.newTreeMap();
		}

		mAttributes.put( name, value );
	}

	@Override
	public void write( Writer writer )
		throws IOException {

		writer.append( '<' );

		if ( mTagPrefix != null ) {
			writer.append( mTagPrefix );
			writer.append( ':' );
		}

		writer.append( mTagName );

		if ( mAttributes != null ) {

			for( Map.Entry<String, String> entry : mAttributes.entrySet() ) {

				writer.append( ' ' );
				writer.append( entry.getKey() );
				writer.append( "=\"" );
				writer.append( entry.getValue() );
				writer.append( "\"" );
			}
		}

		writer.append( "/>" );
	}
}
