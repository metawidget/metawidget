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

package org.metawidget.statically.javacode;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.StaticStub;
import org.metawidget.statically.StaticWidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

public class QueryByExampleMetawidgetTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetBuilder() {

		QueryByExampleWidgetBuilder widgetBuilder = new QueryByExampleWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "abc" );
		attributes.put( TYPE, String.class.getName() );
		StaticWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, new StaticJavaMetawidget() );

		assertEquals(
					"String abc = this.search.getAbc();if (abc != null && !\"\".equals(abc)) { predicatesList.add(builder.equal(root.get(\"abc\"),abc)); }",
					widget.toString() );
	}

	public void testMetawidget() {

		StaticJavaMetawidget metawidget = new StaticJavaMetawidget();
		metawidget.setPath( Foo.class.getName() );
		metawidget.setWidgetBuilder( new QueryByExampleWidgetBuilder() );

		String result = "String abc = this.search.getAbc();\r\n" +
				"if (abc != null && !\"\".equals(abc)) {\r\n" +
				"\tpredicatesList.add(builder.equal(root.get(\"abc\"),abc));\r\n" +
				"}\r\n" +
				"String def = this.search.getDef();\r\n" +
				"if (def != null && !\"\".equals(def)) {\r\n" +
				"\tpredicatesList.add(builder.equal(root.get(\"def\"),def));\r\n" +
				"}\r\n" +
				"String ghi = this.search.getGhi();\r\n" +
				"if (ghi != null && !\"\".equals(ghi)) {\r\n" +
				"\tpredicatesList.add(builder.equal(root.get(\"ghi\"),ghi));\r\n" +
				"}\r\n";

		assertEquals( result, metawidget.toString() );
	}

	//
	// Inner class
	//

	public static class QueryByExampleWidgetBuilder
		implements WidgetBuilder<StaticWidget, StaticJavaMetawidget> {

		//
		// Public methods
		//

		public StaticWidget buildWidget( String elementName, Map<String, String> attributes, StaticJavaMetawidget metawidget ) {

			// Hidden

			if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
				return new StaticStub();
			}

			String type = WidgetBuilderUtils.getActualClassOrType( attributes );

			// If no type, fail gracefully

			if ( type == null ) {
				return new StaticStub();
			}

			// Lookup the Class

			Class<?> clazz = ClassUtils.niceForName( type );

			// Strings

			if ( String.class.equals( clazz ) ) {
				String name = attributes.get( NAME );

				StaticWidget toReturn = new StaticStub();
				toReturn.getChildren().add( new JavaStatement( "String " + name + " = this.search.get" + StringUtils.capitalize( name ) + "()" ) );
				JavaStatement ifNotEmpty = new JavaStatement( "if (" + name + " != null && !\"\".equals(" + name + "))" );
				ifNotEmpty.getChildren().add( new JavaStatement( "predicatesList.add(builder.equal(root.get(\"" + name + "\")," + name + "))" ) );
				toReturn.getChildren().add( ifNotEmpty );
				return toReturn;
			}

			// Do not recurse sub-entities for now

			if ( !ENTITY.equals( elementName ) ) {
				return new StaticStub();
			}

			return null;
		}
	}

	public static class Foo {

		public String	ghi;

		public String	def;

		public Bar		bar;

		public String	abc;
	}

	public static class Bar {

		public String	ignored;
	}
}
