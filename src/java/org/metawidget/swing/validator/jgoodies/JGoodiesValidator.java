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

package org.metawidget.swing.validator.jgoodies;

import java.awt.Component;
import java.util.Map;

import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.validator.BaseValidator;

/**
 * Convenience implementation.
 *
 * @author Richard Kennard
 */

public abstract class JGoodiesValidator
	extends BaseValidator
{
	//
	// Constructor
	//

	public JGoodiesValidator( SwingMetawidget metawidget )
	{
		super( metawidget );
	}

	//
	// Public methods
	//

	@Override
	public void addValidators( Component component, Map<String, String> attributes, String path )
	{
		throw new UnsupportedOperationException();
	}
}
