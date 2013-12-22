// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.faces;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * Utilities for working with Java Server Faces.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class FacesUtils {

	//
	// Public statics
	//

	/**
	 * Return <code>true</code> if the specified value conforms to the syntax requirements of a
	 * value binding expression.
	 * <p>
	 * This method is a mirror of the one in <code>UIComponentTag.isValueReference</code>, but that
	 * one is deprecated so may be removed in the future.
	 *
	 * @param value
	 *            The value to evaluate
	 * @throws NullPointerException
	 *             if <code>value</code> is <code>null</code>
	 */

	public static boolean isExpression( String value ) {

		return matchExpression( value ).matches();
	}

	public static Matcher matchExpression( String value ) {

		return PATTERN_EXPRESSION.matcher( value );
	}

	/**
	 * @return the original String, not wrapped in #{...}. If the original String was not wrapped,
	 *         returns the original String
	 */

	public static String unwrapExpression( String value ) {

		Matcher matcher = PATTERN_EXPRESSION.matcher( value );

		if ( !matcher.matches() ) {
			return value;
		}

		return matcher.group( 2 );
	}

	/**
	 * @return the original String, wrapped in #{...}. If the original String was already wrapped,
	 *         returns the original String
	 */

	public static String wrapExpression( String value ) {

		if ( isExpression( value ) ) {
			return value;
		}

		return EXPRESSION_START + unwrapExpression( value ) + EXPRESSION_END;
	}

	public static void render( FacesContext context, UIComponent component )
		throws IOException {

		if ( component == null || !component.isRendered() ) {
			return;
		}

		component.encodeBegin( context );

		if ( component.getRendersChildren() ) {
			component.encodeChildren( context );
		} else {
			renderChildren( context, component );
		}

		component.encodeEnd( context );
	}

	/**
	 * @return true if the JSF version is 2 or above, false otherwise
	 */

	public static boolean isJsf2() {

		return IS_JSF_2;
	}

	public static boolean isValidationFailed() {

		FacesContext context = FacesContext.getCurrentInstance();

		if ( isJsf2() ) {
			return FacesContext.getCurrentInstance().isValidationFailed();
		}

		// Under JSF 1.2, any severity (even SEVERITY_INFO) is considered a validation error. See
		// http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-1035

		return context.getMaximumSeverity() != null;
	}

	public static boolean isPartialStateSavingDisabled() {

		FacesContext context = FacesContext.getCurrentInstance();
		return "false".equals( context.getExternalContext().getInitParameter( "javax.faces.PARTIAL_STATE_SAVING" ) );
	}

	/**
	 * Create a component using either the JSF 2.x
	 * <code>createComponent( context, componentType, rendererType )</code> method, or the JSF 1.x
	 * <code>createComponent( componentType )</code> method. The former can be important for JSF 2.x
	 * controls that need to dynamically include additional resources (such as JavaScript/CSS
	 * files). See <a href="http://community.jboss.org/message/601807">this forum discussion</a>.
	 */

	@SuppressWarnings( "unchecked" )
	public static <T extends UIComponent> T createComponent( String componentType, String rendererType ) {

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		// JSF 2.x

		if ( isJsf2() ) {
			return (T) application.createComponent( context, componentType, rendererType );
		}

		// JSF 1.x

		return (T) application.createComponent( componentType );
	}

	/**
	 * Sets the given style and styleClass on the given Component.
	 * <p>
	 * JSF does not provide a common interface for all <code>UIComponent</code>s that support
	 * <code>style</code> and <code>styleClass</code>, so we must interrogate their attributes
	 * directly.
	 */

	public static void setStyleAndStyleClass( UIComponent component, String style, String styleClass ) {

		Map<String, Object> attributes = component.getAttributes();

		if ( style != null ) {
			String existingStyle = (String) attributes.get( "style" );

			if ( existingStyle == null || "".equals( existingStyle ) ) {
				attributes.put( "style", style );
			} else if ( !existingStyle.contains( style )){
				attributes.put( "style", existingStyle + " " + style );
			}
		}

		if ( styleClass != null ) {
			String existingStyleClass = (String) attributes.get( "styleClass" );

			if ( existingStyleClass == null || "".equals( existingStyleClass ) ) {
				attributes.put( "styleClass", styleClass );
			} else if ( !existingStyleClass.contains( styleClass )){
				attributes.put( "styleClass", existingStyleClass + " " + styleClass );
			}
		}
	}

	/**
	 * Custom implementation of <code>UIViewRoot.createUniqueId</code>. The 'proper' approach
	 * requires traversing the component tree, which could be expensive. See
	 * http://java.net/jira/browse/JAVASERVERFACES-2283
	 */

	public static String createUniqueId() {

		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> attributes = context.getViewRoot().getAttributes();

		Integer uniqueId = (Integer) attributes.get( UNIQUE_ID_VIEW_ATTRIBUTE );

		if ( uniqueId == null ) {
			uniqueId = 0;
		}

		attributes.put( UNIQUE_ID_VIEW_ATTRIBUTE, uniqueId + 1 );

		// Still use UNIQUE_ID_PREFIX, so that ids are suppressed from the output. As per
		// the RenderKit specification:
		//
		// "If the value returned from component.getId() is non-null and does not start
		// with UIViewRoot.UNIQUE_ID_PREFIX, call component.getClientId() and render
		// the result as the value of the id attribute in the markup for the component."

		return UIViewRoot.UNIQUE_ID_PREFIX + "mw" + uniqueId;
	}

	//
	// Private statics
	//

	private static void renderChildren( FacesContext context, UIComponent component )
		throws IOException {

		for ( UIComponent componentChild : component.getChildren() ) {
			render( context, componentChild );
		}
	}

	/**
	 * Match #{...} and ${...}. This mirrors the approach in
	 * <code>UIComponentTag.isValueReference</code>, but that one is deprecated so may be removed in
	 * the future.
	 */

	private static final Pattern	PATTERN_EXPRESSION			= Pattern.compile( "(#|\\$)\\{([^\\}]+)\\}" );

	private static final String		EXPRESSION_START			= "#{";

	private static final String		EXPRESSION_END				= "}";

	private static final String		UNIQUE_ID_VIEW_ATTRIBUTE	= FacesUtils.class.getName() + ".UNIQUE_ID";

	private static final boolean	IS_JSF_2;

	static {

		boolean isJsf2;

		try {

			Class.forName( "javax.faces.event.PreRenderViewEvent" );
			isJsf2 = true;

		} catch ( ClassNotFoundException e ) {

			isJsf2 = false;
		}

		IS_JSF_2 = isJsf2;
	}

	//
	// Private constructor
	//

	private FacesUtils() {

		// Can never be called
	}
}
