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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.JComponent;

import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.validator.BaseValidator;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.PathUtils;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * Validator to add JGoodies validators to a Component.
 * <p>
 * Out of the box, JGoodies does not provide any Validator implementations, so by default this class
 * only calls JGoodies' <code>setMandatory</code> and <code>updateComponentTreeMandatory</code>
 * methods. Clients are expected to extend this class and override <code>getValidator</code> to
 * integrate their own validators.
 *
 * @author Richard Kennard, Stefan Ackermann
 */

public class JGoodiesValidator
	extends BaseValidator
{
	//
	// Protected members
	//

	/**
	 * JGoodies' API concentrates on bulk updates of sub-components in a component tree. For example
	 * the <code>ValidationComponentUtils.updateComponentTreeXXX</code> and
	 * <code>ValidationComponentUtils.visitComponentTree</code> methods take a top-level component
	 * and traverse it setting all validation messages for all sub-components.
	 * <p>
	 * Because of this, when updating it is important to retain previous validation results, or
	 * their messages will be lost during the bulk update of new validation results.
	 */

	protected Map<JComponent, ValidationResult>	mValidationResults	= CollectionUtils.newHashMap();

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
	public void addValidator( final Component component, Map<String, String> attributes, String path )
	{
		// JGoodies only supports JComponents

		if ( !( component instanceof JComponent ) )
			return;

		JComponent jcomponent = (JComponent) component;

		// Required?

		boolean required = ( TRUE.equals( attributes.get( REQUIRED ) ) );

		if ( required )
			ValidationComponentUtils.setMandatory( jcomponent, true );

		// Custom validator?

		Validator<?> validator = getValidator( jcomponent, attributes, path );

		// Do not attachValidator if no validator and not required

		if ( validator == null && !required )
			return;

		// Attach

		attachValidator( jcomponent, validator, path );
	}

	@Override
	public void initializeValidators()
	{
		super.initializeValidators();

		ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground( getMetawidget() );
		ValidationComponentUtils.updateComponentTreeMandatoryBorder( getMetawidget() );
	}

	//
	// Protected methods
	//

	/**
	 * Return the appropriate validator for the given JComponent with the given attributes.
	 */

	protected Validator<?> getValidator( JComponent component, Map<String, String> attributes, String path )
	{
		return null;
	}

	/**
	 * Attach the given Validator to the given JComponent.
	 */

	protected void attachValidator( final JComponent component, final Validator<?> validator, String path )
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

				// ...run it through the Validator...

				if ( validator != null )
				{
					@SuppressWarnings( "unchecked" )
					Validator<Object> objectValidator = (Validator<Object>) validator;
					mValidationResults.put( component, objectValidator.validate( value ) );
				}

				// ...collate all ValidationResults...

				ValidationResult validationResult = new ValidationResult();
				for ( ValidationResult previousValidationResult : mValidationResults.values() )
				{
					validationResult.addAllFrom( previousValidationResult );
				}

				// ...and update the UI

				updateComponent( component, validationResult );
			}
		} );
	}

	/**
	 * Update the given component with the given validation result.
	 * <p>
	 * Clients may override this method to integrate their own Visitors. For example
	 * <p>
	 * <code>ValidationComponentUtils.visitComponentTree( getMetawidget(), validationResult.keyMap(), new MyCustomVisitor() );</code>
	 * </p>
	 *
	 * @param component
	 *            the component that was validated
	 * @param validationResult
	 *            contains the latest ValidationResult plus any previous ValidationResults for other
	 *            components (so can be used correctly with updateComponentTreeXXX)
	 */

	protected void updateComponent( JComponent component, ValidationResult validationResult )
	{
		ValidationComponentUtils.updateComponentTreeSeverityBackground( getMetawidget(), validationResult );

		// Note: it may be nicer to only update the JComponent, not revisit the entire
		// tree, but JGoodies' built-in (private) MandatoryAndBlankBackgroundVisitor uses
		// its (private) restoreBackground, so seemingly this is the way JGoodies wants us
		// to do it

		if ( ValidationComponentUtils.isMandatory( component ) )
			ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground( getMetawidget() );
	}
}
