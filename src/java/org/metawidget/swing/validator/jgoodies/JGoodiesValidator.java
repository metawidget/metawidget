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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.validator.BaseValidator;
import org.metawidget.util.simple.PathUtils;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;

/**
 * Validator to add JGoodies validators to a Component.
 *
 * @author Richard Kennard, Stefan Ackermann
 */

public class JGoodiesValidator
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
	public void addValidator( Component component, Map<String, String> attributes, String path )
	{
		Validator<?> validator = getValidator( component, attributes, path );

		if ( validator == null )
			return;

		attachValidator( component, validator, path );
	}

	//
	// Protected methods
	//

	/**
	 * Return the appropriate validators for the given Component with the given attributes.
	 */

	protected Validator<?> getValidator( Component component, Map<String, String> attributes, String path )
	{
		return null;
	}

	/**
	 * Attach the given Validator to the given Component
	 */

	protected void attachValidator( Component component, final Validator<?> validator, String path )
	{
		final SwingMetawidget metawidget = getMetawidget();
		final String[] names = PathUtils.parsePath( path ).getNamesAsArray();

		component.addKeyListener( new KeyAdapter()
		{
			@Override
			public void keyReleased( KeyEvent event )
			{
				// Fetch the value...

				Object value = metawidget.getValue( names );

				// ...run it through all the Validators...

				ValidationResult validationResult = new ValidationResult();

				@SuppressWarnings( "unchecked" )
				Validator<Object> objectValidator = (Validator<Object>) validator;
				validationResult.addAllFrom( objectValidator.validate( value ) );

				// ...and update the UI

				// TODO: How?
			}
		} );
	}
}
