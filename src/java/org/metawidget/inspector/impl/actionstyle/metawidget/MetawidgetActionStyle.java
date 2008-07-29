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

package org.metawidget.inspector.impl.actionstyle.metawidget;

import java.lang.reflect.Method;
import java.util.Map;

import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.actionstyle.Action;
import org.metawidget.inspector.impl.actionstyle.MethodActionStyle;
import org.metawidget.util.CollectionUtils;

/**
 * ActionStyle for Metawidget-style actions.
 *
 * @author Richard Kennard
 */

public class MetawidgetActionStyle
	extends MethodActionStyle
{
	//
	//
	// Protected methods
	//
	//

	/**
	 * @return the actions of the given class. Never null.
	 */

	@Override
	protected Map<String, Action> inspectActions( Class<?> clazz )
	{
		// TreeMap so that returns alphabetically sorted actions

		Map<String, Action> actions = CollectionUtils.newTreeMap();

		// For each action...

		for( Method method : clazz.getMethods() )
		{
			UiAction action = method.getAnnotation( UiAction.class );

			if ( action == null )
				continue;

			// ...validate it...

			if ( method.getParameterTypes().length > 0 )
				throw InspectorException.newException( "@UiAction " + method + " must not take any parameters" );

			// ...and add it

			String methodName = method.getName();
			actions.put( methodName, new MethodAction( methodName, method ) );
		}

		return actions;
	}
}
