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

package org.metawidget.inspector.faces;

/**
 * Faces-specific element and attribute names appearing in DOMs conforming to
 * inspection-result-1.0.xsd.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class FacesInspectionResultConstants {

	//
	// Public statics
	//

	public static final String	FACES_LOOKUP			= "faces-lookup";

	public static final String	FACES_LOOKUP_VAR		= "faces-lookup-var";

	public static final String	FACES_LOOKUP_ITEM_VALUE	= "faces-lookup-item-value";

	public static final String	FACES_LOOKUP_ITEM_LABEL	= "faces-lookup-item-label";

	public static final String	FACES_SUGGEST			= "faces-suggest";

	public static final String	FACES_EXPRESSION		= "faces-expression";

	public static final String	FACES_COMPONENT			= "faces-component";

	public static final String	FACES_AJAX_EVENT		= "faces-ajax-event";

	public static final String	FACES_AJAX_ACTION		= "faces-ajax-action";

	/**
	 * The Faces Converter to use for this component. Can be an id or an EL expression that
	 * evaluates to a <code>javax.faces.convert.Converter</code> instance.
	 */

	public static final String	FACES_CONVERTER			= "faces-converter";

	/**
	 * Attribute indicated the UIComponent should have its <code>immediate</code> flag set.
	 * <p>
	 * Should be used in conjunction with <code>ImmediateAttributeWidgetProcessor</code>.
	 */

	public static final String	FACES_IMMEDIATE			= "faces-immediate";

	//
	// Private constructor
	//

	private FacesInspectionResultConstants() {

		// Can never be called
	}
}
