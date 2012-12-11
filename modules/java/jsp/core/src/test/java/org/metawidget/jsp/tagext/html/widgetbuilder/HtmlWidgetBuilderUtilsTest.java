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

package org.metawidget.jsp.tagext.html.widgetbuilder;

import javax.servlet.jsp.el.Expression;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.VariableResolver;

import junit.framework.TestCase;

import org.metawidget.jsp.JspMetawidgetTests.MockPageContext;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.HtmlMetawidgetTag;

/**
 * @author Richard Kennard
 */

public class HtmlWidgetBuilderUtilsTest
	extends TestCase {

	//
	// Public methods
	//

	/**
	 * Should fail gracefully, even in the face of NoSuchMethodError.
	 */

	public void testEvaluate()
		throws Exception {

		MetawidgetTag metawidget = new HtmlMetawidgetTag();
		metawidget.setPageContext( new MockPageContext() {

			@Override
			public ExpressionEvaluator getExpressionEvaluator() {

				return new ExpressionEvaluator() {

					@SuppressWarnings( "rawtypes" )
					@Override
					public Object evaluate( String expression, Class arg1, VariableResolver arg2, FunctionMapper arg3 ) {

						throw new NoSuchMethodError( "Should fail gracefully" );
					}

					@Override
					@SuppressWarnings( "rawtypes" )
					public Expression parseExpression( String arg0, Class arg1, FunctionMapper arg2 ) {

						throw new NoSuchMethodError( "Should fail gracefully" );
					}
				};
			}
		} );

		assertEquals( null,  HtmlWidgetBuilderUtils.evaluate( "${foo}", metawidget ));
	}
}
