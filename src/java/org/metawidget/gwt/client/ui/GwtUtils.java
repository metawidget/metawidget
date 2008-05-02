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

package org.metawidget.gwt.client.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;

/**
 * Utilities for working with Google Web Toolkit.
 * <p>
 * These utility methods are copies of those defined in <code>org.metawidget.util</code>, but they
 * either are 'GWT flavoured' (eg. they use <code>com.google.gwt.xml</code> instead of <code>org.w3c.dom</code>)
 * or they are free encumberances (eg. <code>java.util.regex</code>) that GWT doesn't support.
 *
 * @author Richard Kennard
 */

public final class GwtUtils
{
	//
	//
	// Public statics
	//
	//

	public static Map<String, String> getAttributesAsMap( Element element )
	{
		NamedNodeMap nodes = element.getAttributes();

		int length = nodes.getLength();

		if ( length == 0 )
			return Collections.emptyMap();

		Map<String, String> attributes = new HashMap<String, String>( length );

		for ( int loop = 0; loop < length; loop++ )
		{
			Node node = nodes.item( loop );
			attributes.put( node.getNodeName(), node.getNodeValue() );
		}

		return attributes;
	}

	public static boolean isPrimitive( String className )
	{
		if ( "byte".equals( className ) || "short".equals( className ) )
			return true;

		if ( "int".equals( className ) || "long".equals( className ) )
			return true;

		if ( "float".equals( className ) || "double".equals( className ) )
			return true;

		if ( "boolean".equals( className ) )
			return true;

		if ( "char".equals( className ) )
			return true;

		return false;
	}

	public static String[] fromString( String collection, char separator )
	{
		if ( collection == null )
			return new String[0];

		return collection.split( String.valueOf( separator ) );
	}

	public static String toString( String[] collection, char separator )
	{
		StringBuilder builder = new StringBuilder();

		for( String item : collection )
		{
			if ( builder.length() > 0 )
				builder.append( separator );

			builder.append( item );
		}

		return builder.toString();
	}

	public static Object[] parsePath( String path, char separator )
	{
		int indexOfTypeEnd = path.indexOf( separator );

		// Just type?

		if ( indexOfTypeEnd == -1 )
			return new String[]{ path, null };

		String type = path.substring( 0, indexOfTypeEnd );

		// Parse names

		int indexOfNameEnd = indexOfTypeEnd;

		List<String> names = new ArrayList<String>();

		while ( true )
		{
			int indexOfNameStart = indexOfNameEnd + 1;
			indexOfNameEnd = path.indexOf( separator, indexOfNameStart );

			if ( indexOfNameEnd == -1 )
			{
				names.add( path.substring( indexOfNameStart ) );
				break;
			}

			names.add( path.substring( indexOfNameStart, indexOfNameEnd ) );
		}

		if ( names.isEmpty() )
			return new Object[]{ path, null };

		return new Object[]{ type, names.toArray( new String[names.size()] ) };
	}

	//
	//
	// Private constructor
	//
	//

	private GwtUtils()
	{
		// Can never be called
	}
}
