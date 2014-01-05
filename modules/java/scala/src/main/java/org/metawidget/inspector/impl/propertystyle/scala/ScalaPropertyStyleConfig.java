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

package org.metawidget.inspector.impl.propertystyle.scala;

import java.text.MessageFormat;

import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;

/**
 * Configures a ScalaPropertyStyleConfig prior to use. Once instantiated, PropertyStyles are
 * immutable.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ScalaPropertyStyleConfig
	extends JavaBeanPropertyStyleConfig {

	//
	// Constructor
	//

	/**
	 * Configures a ScalaPropertyStyleConfig.
	 * <p>
	 * Overridden to set a default <code>privateFieldConvention</code> of <code>{0}</code>, because
	 * Scala getters/setters always map to a private field of the same name.
	 */

	public ScalaPropertyStyleConfig() {

		setPrivateFieldConvention( new MessageFormat( "{0}" ) );
	}
}
