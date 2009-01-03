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

package org.metawidget.swing.validator.verifier;

import java.awt.Component;
import java.util.Map;

import javax.swing.InputVerifier;
import javax.swing.JComponent;

import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.validator.BaseValidator;

/**
 * Validator to add Swing InputVerifiers to a Component.
 *
 * @author Richard Kennard
 */

public class InputVerifierValidator
	extends BaseValidator
{
	//
	// Constructor
	//

	public InputVerifierValidator( SwingMetawidget metawidget )
	{
		super( metawidget );
	}

	//
	// Public methods
	//

	@Override
	public void addValidator( Component component, Map<String, String> attributes, String path )
	{
		if ( !( component instanceof JComponent ))
			return;

		InputVerifier verifier = getInputVerifier( component, attributes, path );

		if ( verifier == null )
			return;

		((JComponent) component).setInputVerifier( verifier );
	}

	//
	// Protected methods
	//

	/**
	 * Return the appropriate InputVerifier for the given Component with the given attributes.
	 */

	protected InputVerifier getInputVerifier( Component component, Map<String, String> attributes, String path )
	{
		return null;
	}
}
