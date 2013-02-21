// Metawidget (licensed under LGPL)
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

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.propertytype.PropertyTypeInspectionResultConstants.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Utilities for working with Google Web Toolkit.
 * <p>
 * These utility methods are copies of those defined in <code>org.metawidget.util</code>, but they
 * either are 'GWT flavoured' (eg. they use <code>com.google.gwt.xml</code> instead of
 * <code>org.w3c.dom</code>) or they are free encumberances (eg. <code>java.util.regex</code>) that
 * GWT doesn't support.
 *
 * @author Richard Kennard
 */

public final class GwtUtils {

	//
	// Public statics
	//

	/**
	 * GWT-ified <code>Class.isPrimitive</code>.
	 * <p>
	 * This version takes a String argument, not a Class argument, because we won't have been able
	 * to use Class.forName to create a Class.
	 */

	public static boolean isPrimitive( String className ) {

		if ( GwtUtils.isIntegerPrimitive( className ) ) {
			return true;
		}

		if ( "float".equals( className ) || "double".equals( className ) ) {
			return true;
		}

		if ( "boolean".equals( className ) ) {
			return true;
		}

		if ( "char".equals( className ) ) {
			return true;
		}

		return false;
	}

	/**
	 * Returns <code>true</code> if the type is an integer primitive.
	 * <p>
	 * We mean 'integer' in the mathematical sense (ie. a whole number), not the Java sense, so
	 * byte, short, int and long all return <code>true</code>. Determining whether a type is 'whole
	 * number compatible' is useful for widgets like sliders and spinners.
	 */

	public static boolean isIntegerPrimitive( String className ) {

		if ( "byte".equals( className ) || "short".equals( className ) ) {
			return true;
		}

		if ( "int".equals( className ) || "long".equals( className ) ) {
			return true;
		}

		return false;
	}

	/**
	 * GWT-ified <code>ClassUtils.isPrimitiveWrapper</code>.
	 * <p>
	 * This version takes a String argument, not a Class argument, because we won't have been able
	 * to use Class.forName to create a Class.
	 */

	public static boolean isPrimitiveWrapper( String className ) {

		if ( Byte.class.getName().equals( className ) || Short.class.getName().equals( className ) ) {
			return true;
		}

		if ( Integer.class.getName().equals( className ) || Long.class.getName().equals( className ) ) {
			return true;
		}

		if ( Float.class.getName().equals( className ) || Double.class.getName().equals( className ) ) {
			return true;
		}

		if ( Boolean.class.getName().equals( className ) ) {
			return true;
		}

		if ( Character.class.getName().equals( className ) ) {
			return true;
		}

		return false;
	}

	/**
	 * Whether the given class name is a Collection. This is a crude, GWT-equivalent of...
	 * <p>
	 * <code>
	 *    Collection.class.isAssignableFrom( ... );
	 * </code>
	 * <p>
	 * ...subclasses may need to override this method if they introduce a new Collection subtype.
	 */

	public static boolean isCollection( String className ) {

		if ( Collection.class.getName().equals( className ) ) {
			return true;
		}

		if ( List.class.getName().equals( className ) || ArrayList.class.getName().equals( className ) ) {
			return true;
		}

		if ( Set.class.getName().equals( className ) || HashSet.class.getName().equals( className ) ) {
			return true;
		}

		if ( Map.class.getName().equals( className ) || HashMap.class.getName().equals( className ) ) {
			return true;
		}

		return false;
	}

	/**
	 * GWT-ified <code>CollectionUtils.fromString</code>.
	 * <p>
	 * This version does not use regular expressions.
	 */

	public static List<String> fromString( String collection, char separator ) {

		if ( collection == null || "".equals( collection ) ) {
			return Collections.emptyList();
		}

		List<String> split = new ArrayList<String>();

		for ( String item : collection.split( String.valueOf( separator ) ) ) {
			split.add( item.trim() );
		}

		// Hack for collections with an empty string on the end (eg. "Foo,Bar,")

		if ( collection.charAt( collection.length() - 1 ) == separator ) {
			split.add( "" );
		}

		return split;
	}

	/**
	 * GWT-ified <code>CollectionUtils.toString</code>.
	 * <p>
	 * This version does not use regular expressions.
	 */

	public static String toString( String[] collection, char separator ) {

		if ( collection == null ) {
			return "";
		}

		StringBuilder builder = new StringBuilder();

		for ( String item : collection ) {
			if ( builder.length() > 0 ) {
				builder.append( separator );
			}

			builder.append( item );
		}

		return builder.toString();
	}

	/**
	 * GWT-ified <code>CollectionUtils.toString</code>.
	 * <p>
	 * This version does not use regular expressions.
	 */

	public static String toString( Collection<?> collection, char separator ) {

		if ( collection == null ) {
			return "";
		}

		StringBuilder builder = new StringBuilder();

		for ( Object item : collection ) {
			if ( builder.length() > 0 ) {
				builder.append( separator );
			}

			builder.append( item );
		}

		return builder.toString();
	}

	public static void setListBoxSelectedItem( ListBox listBox, String value ) {

		for ( int loop = 0, length = listBox.getItemCount(); loop < length; loop++ ) {
			if ( value.equals( listBox.getValue( loop ) ) ) {
				listBox.setSelectedIndex( loop );
				return;
			}
		}

		// Fail quietly
	}

	public static void alert( Throwable caught ) {

		StringBuilder builder = new StringBuilder( caught.getClass().getName() );

		if ( caught.getMessage() != null ) {
			builder.append( ": " );
			builder.append( caught.getMessage() );
		}

		for ( Object item : caught.getStackTrace() ) {
			builder.append( "\n\t" );
			builder.append( item );
		}

		Window.alert( builder.toString() );
	}

	/**
	 * Returns whether the attributes have READ_ONLY or NO_SETTER set to TRUE.
	 * <p>
	 * The latter case relies on complex attributes being rendered by nested Metawidgets: the nested
	 * Metawidgets will <em>not</em> have setReadOnly set on them, which gets us the desired result.
	 * Namely, primitive types without a setter are rendered as read-only, complex types without a
	 * setter are rendered as writeable (because their nested primitives are writeable).
	 * <p>
	 * Furthermore, what is considered 'primitive' is up to the platform. Some platforms may
	 * consider, say, an Address as 'primitive', using a dedicated Address widget. Other platforms
	 * may consider an Address as complex, using a nested Metawidget.
	 *
	 * @return true if the attributes have READ_ONLY set to TRUE, or NO_SETTER set to true.
	 */

	public static boolean isReadOnly( Map<String, String> attributes ) {

		if ( TRUE.equals( attributes.get( READ_ONLY ) ) ) {
			return true;
		}

		if ( TRUE.equals( attributes.get( NO_SETTER ) ) ) {
			return true;
		}

		return false;
	}

	/**
	 * Looks up the TYPE attribute, but first checks the ACTUAL_CLASS attribute.
	 *
	 * @return ACTUAL_CLASS of, if none, TYPE or, if none, null. Never an empty String.
	 */

	public static String getActualClassOrType( Map<String, String> attributes ) {

		String type = attributes.get( ACTUAL_CLASS );

		if ( type != null && !"".equals( type ) ) {
			return type;
		}

		type = attributes.get( TYPE );

		if ( "".equals( type ) ) {
			return null;
		}

		return type;
	}

	/**
	 * Returns true if the lookup is nullable, not required, or has a forced empty choice.
	 */

	public static boolean needsEmptyLookupItem( Map<String, String> attributes ) {

		if ( TRUE.equals( attributes.get( LOOKUP_HAS_EMPTY_CHOICE ) ) ) {
			return true;
		}

		if ( TRUE.equals( attributes.get( REQUIRED ) ) ) {
			return false;
		}

		String type = getActualClassOrType( attributes );

		// Type can be null if this lookup was specified by a metawidget-metadata.xml
		// and the type was omitted from the XML. In that case, assume nullable
		//
		// Note: there's an extra caveat for Groovy dynamic types: if we can't load
		// the class, assume it is non-primitive and therefore add a null choice

		if ( type != null && isPrimitive( type ) ) {
			return false;
		}

		return true;
	}

	public static String stripSection( Map<String, String> attributes ) {

		String sections = attributes.remove( SECTION );

		// (null means 'no change to current section')

		if ( sections == null ) {
			return null;
		}

		List<String> sectionAsArray = GwtUtils.fromString( sections, ',' );

		switch ( sectionAsArray.size() ) {
			// (empty String means 'end current section')

			case 0:
				return "";

			case 1:
				return sectionAsArray.get( 0 );

			default:
				String section = sectionAsArray.remove( 0 );
				attributes.put( SECTION, GwtUtils.toString( sectionAsArray, ',' ) );
				return section;
		}
	}

	//
	// Private constructor
	//

	private GwtUtils() {

		// Can never be called
	}
}
