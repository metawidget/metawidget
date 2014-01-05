// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.jsp.tagext.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.servlet.jsp.el.Expression;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.VariableResolver;

import junit.framework.TestCase;

import org.metawidget.jsp.JspMetawidgetTests.MockPageContext;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.HtmlMetawidgetTag;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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

		assertEquals( null, HtmlWidgetBuilderUtils.evaluate( "${foo}", metawidget ) );
	}

	/**
	 * JDK 5's BooleanEditor returns 'true' (lowercase 't') but JDK 6+ returns 'True' (uppercase
	 * 'T'). This prevents matching in SELECT boxes.
	 */

	public void testBooleanEvaluate() {

		HtmlMetawidgetTag metawidget = new HtmlMetawidgetTag() {

			@Override
			public String getPathPrefix() {

				return "foo";
			}
		};

		metawidget.setPageContext( new MockPageContext() {

			@Override
			public ExpressionEvaluator getExpressionEvaluator() {

				return new ExpressionEvaluator() {

					@SuppressWarnings( "rawtypes" )
					@Override
					public Object evaluate( String expression, Class arg1, VariableResolver arg2, FunctionMapper arg3 ) {

						return true;
					}

					@Override
					@SuppressWarnings( "rawtypes" )
					public Expression parseExpression( String arg0, Class arg1, FunctionMapper arg2 ) {

						throw new NoSuchMethodError( "Should fail gracefully" );
					}
				};
			}
		} );

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );
		assertEquals( "true", HtmlWidgetBuilderUtils.evaluateAsText( attributes, metawidget ) );

		metawidget.setPageContext( new MockPageContext() {

			@Override
			public ExpressionEvaluator getExpressionEvaluator() {

				return new ExpressionEvaluator() {

					@SuppressWarnings( "rawtypes" )
					@Override
					public Object evaluate( String expression, Class arg1, VariableResolver arg2, FunctionMapper arg3 ) {

						return Boolean.TRUE;
					}

					@Override
					@SuppressWarnings( "rawtypes" )
					public Expression parseExpression( String arg0, Class arg1, FunctionMapper arg2 ) {

						throw new NoSuchMethodError( "Should fail gracefully" );
					}
				};
			}
		} );

		assertEquals( "true", HtmlWidgetBuilderUtils.evaluateAsText( attributes, metawidget ) );
	}
}
