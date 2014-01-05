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

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.BaseHtmlMetawidgetTag;
import org.metawidget.util.simple.StringUtils;

/**
 * Utilities for JSP WidgetBuilders.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class HtmlWidgetBuilderUtils {

	//
	// Public statics
	//

	public static String writeValueAttribute( Map<String, String> attributes, MetawidgetTag metawidget ) {

		String result = evaluateAsText( attributes, metawidget );

		if ( result == null || "".equals( result ) ) {
			return "";
		}

		StringBuilder builder = new StringBuilder();

		builder.append( " value=\"" );
		builder.append( result );
		builder.append( "\"" );

		return builder.toString();
	}

	/**
	 * Initialize the HTML tag with various attributes, CSS settings etc.
	 * <p>
	 * In other Metawidgets, this step is done after the widget has been built. However, because JSP
	 * lacks a 'true' component model (eg. buildActiveWidget returns a String) we must do it here.
	 */

	public static String writeAttributes( Map<String, String> attributes, MetawidgetTag metawidget ) {

		StringBuilder builder = new StringBuilder();

		String name = attributes.get( NAME );

		if ( name != null ) {

			builder.append( " name=\"" );
	
			if ( metawidget.getPathPrefix() != null ) {
				name = metawidget.getPathPrefix() + name;
			}
	
			builder.append( name );
			builder.append( "\"" );
		}

		// CSS

		BaseHtmlMetawidgetTag htmlMetawidgetTag = (BaseHtmlMetawidgetTag) metawidget;

		if ( htmlMetawidgetTag.getStyle() != null ) {
			builder.append( " style=\"" );
			builder.append( htmlMetawidgetTag.getStyle() );
			builder.append( "\"" );
		}

		if ( htmlMetawidgetTag.getStyleClass() != null ) {
			builder.append( " class=\"" );
			builder.append( htmlMetawidgetTag.getStyleClass() );
			builder.append( "\"" );
		}

		return builder.toString();
	}

	/**
	 * Evaluate to text (via a PropertyEditor if available).
	 */

	public static String evaluateAsText( Map<String, String> attributes, MetawidgetTag metawidget ) {

		Object evaluated = evaluate( attributes, metawidget );

		// Special support for null

		if ( evaluated == null ) {
			return "";
		}

		// Special support for Booleans. The built-in sun.beans.editors.BooleanEditor returns 'True'
		// with an uppercase 't'

		if ( Boolean.class.isAssignableFrom( evaluated.getClass() ) || boolean.class.isAssignableFrom( evaluated.getClass() )) {
			return String.valueOf( evaluated );
		}

		// Otherwise, delegate to registered PropertyEditor

		Class<?> clazz = evaluated.getClass();

		while ( clazz != null ) {
			PropertyEditor editor = PropertyEditorManager.findEditor( clazz );

			if ( editor != null ) {
				editor.setValue( evaluated );
				return editor.getAsText();
			}

			clazz = clazz.getSuperclass();
		}

		return StringUtils.quietValueOf( evaluated );
	}

	public static Object evaluate( Map<String, String> attributes, MetawidgetTag metawidget ) {

		if ( metawidget.getPathPrefix() == null ) {
			return null;
		}

		return evaluate( "${" + metawidget.getPathPrefix() + attributes.get( NAME ) + "}", metawidget );
	}

	public static Object evaluate( String expression, MetawidgetTag metawidget ) {

		try {
			PageContext context = metawidget.getPageContext();
			return context.getExpressionEvaluator().evaluate( expression, Object.class, context.getVariableResolver(), null );
		} catch ( NoSuchMethodError e ) {
			// pageContext.getExpressionEvaluator() is only available with JSP 2.0
		} catch ( Exception e ) {
			// EL should fail gracefully
		}

		return null;
	}

	//
	// Private constructor
	//

	private HtmlWidgetBuilderUtils() {

		// Can never be called
	}
}
