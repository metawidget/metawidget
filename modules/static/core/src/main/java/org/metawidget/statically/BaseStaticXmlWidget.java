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
import java.util.List;
import java.util.Map;

import org.metawidget.statically.StaticUtils.IndentedWriter;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public abstract class BaseStaticXmlWidget
	extends BaseStaticWidget
	implements StaticXmlWidget {

	//
	// Private members
	//

	private String					mTagPrefix;

	private String					mTagName;

	private Map<String, String>		mAttributes;

	private List<StaticXmlWidget>	mChildren;

	//
	// Constructor
	//

	public BaseStaticXmlWidget( String tagName ) {

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

	public String getAttribute( String name ) {

		if ( mAttributes == null ) {
			return null;
		}

		return mAttributes.get( name );
	}

	public void addChild( StaticXmlWidget child ) {

		if ( mChildren == null ) {
			mChildren = CollectionUtils.newArrayList();
		}

		mChildren.add( child );
	}

	@Override
	public void write( Writer writer )
		throws IOException {

		if ( !hasChildren() ) {
			writeStartTagImpl( writer );
			writer.append( "/>\r\n" );
		} else {
			writeStartTagImpl( writer );
			writer.append( ">\r\n" );
			( (IndentedWriter) writer ).indent();

			for ( StaticXmlWidget child : mChildren ) {
				child.write( writer );
			}

			writeEndTag( writer );
		}
	}

	public void writeStartTag( Writer writer )
		throws IOException {

		writeStartTagImpl( writer );
		writer.append( ">\r\n" );
		( (IndentedWriter) writer ).indent();
	}

	public void writeEndTag( Writer writer )
		throws IOException {

		( (IndentedWriter) writer ).outdent();
		writer.append( "</" );

		if ( mTagPrefix != null ) {
			writer.append( mTagPrefix );
			writer.append( ':' );
		}

		writer.append( mTagName );
		writer.append( ">\r\n" );
	}

	//
	// Private methods
	//

	private boolean hasChildren() {

		if ( mChildren == null ) {
			return false;
		}

		return !mChildren.isEmpty();
	}

	private void writeStartTagImpl( Writer writer )
		throws IOException {

		writer.append( '<' );

		if ( mTagPrefix != null ) {
			writer.append( mTagPrefix );
			writer.append( ':' );
		}

		writer.append( mTagName );

		if ( mAttributes != null ) {

			for ( Map.Entry<String, String> entry : mAttributes.entrySet() ) {

				String value = entry.getValue();

				if ( value == null || value.length() == 0 ) {
					continue;
				}

				writer.append( ' ' );
				writer.append( entry.getKey() );
				writer.append( "=\"" );
				writer.append( value );
				writer.append( "\"" );
			}
		}
	}
}
