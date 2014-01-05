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

package org.metawidget.inspector.impl.actionstyle;

import java.util.Map;

import org.metawidget.inspector.impl.BaseTraitStyle;
import org.metawidget.inspector.impl.BaseTraitStyleConfig;

/**
 * Convenience implementation for ActionStyles.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class BaseActionStyle
	extends BaseTraitStyle<Action>
	implements ActionStyle {

	//
	// Constructor
	//

	protected BaseActionStyle( BaseTraitStyleConfig config ) {

		super( config );
	}

	//
	// Public methods
	//

	public Map<String, Action> getActions( String type ) {

		return getTraits( type );
	}

	//
	// Protected methods
	//

	@Override
	protected final Map<String, Action> getUncachedTraits( String type ) {

		return inspectActions( type );
	}

	/**
	 * @return the actions of the given class. Never null.
	 */

	protected abstract Map<String, Action> inspectActions( String type );
}
