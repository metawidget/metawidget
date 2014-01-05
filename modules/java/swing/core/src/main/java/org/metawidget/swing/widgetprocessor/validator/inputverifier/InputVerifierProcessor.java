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

package org.metawidget.swing.widgetprocessor.validator.inputverifier;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.swing.InputVerifier;
import javax.swing.JComponent;

import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * Processor to add Swing InputVerifiers to a Component.
 * <p>
 * This class is abstract, because Swing does not provide any implemented InputVerifiers out of the
 * box. Clients must extend this class and implement <code>getInputVerifier</code> to integrate
 * their own verifiers.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class InputVerifierProcessor
	implements WidgetProcessor<JComponent, SwingMetawidget> {

	//
	// Public methods
	//

	public JComponent processWidget( JComponent component, String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

		String path = metawidget.getPath();

		if ( PROPERTY.equals( elementName ) ) {
			path += StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME );
		}

		InputVerifier verifier = getInputVerifier( component, attributes, metawidget, path );

		if ( verifier == null ) {
			return component;
		}

		component.setInputVerifier( verifier );

		return component;
	}

	//
	// Protected methods
	//

	/**
	 * Return the appropriate InputVerifier for the given Component with the given attributes.
	 *
	 * @param component
	 *            component needing a verifier
	 * @param attributes
	 *            Map of attributes as returned by the Inspectors
	 * @param path
	 *            path to the source object. Can be parsed using PathUtils.parsePath
	 */

	protected abstract InputVerifier getInputVerifier( JComponent component, Map<String, String> attributes, SwingMetawidget metawidget, String path );
}
