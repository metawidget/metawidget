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

package org.metawidget.swing.widgetprocessor.validator.jgoodies;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.JComponent;

import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * Processor to add JGoodies Validators to a Component.
 * <p>
 * Out of the box, JGoodies does not provide any Validator implementations, so by default this class
 * only calls JGoodies' <code>setMandatory</code> and <code>updateComponentTreeMandatory</code>
 * methods. Clients are expected to extend this class and override <code>getValidator</code> to
 * integrate their own validators.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>, Stefan Ackermann
 */

public class JGoodiesValidatorProcessor
	implements AdvancedWidgetProcessor<JComponent, SwingMetawidget> {

	//
	// Public methods
	//

	public void onStartBuild( SwingMetawidget metawidget ) {

		// Do nothing
	}

	public JComponent processWidget( final JComponent component, String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

		// Required?

		boolean required = TRUE.equals( attributes.get( REQUIRED ) );

		if ( required ) {
			ValidationComponentUtils.setMandatory( component, true );
		}

		// Custom validator?

		String path = metawidget.getPath();
		String name = attributes.get( NAME );

		if ( PROPERTY.equals( elementName ) ) {
			path += StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + name;
		}

		Validator<?> validator = getValidator( component, attributes, path, metawidget );

		if ( validator == null ) {
			// Do not attachValidator if no validator and not required

			if ( !required ) {
				return component;
			}
		} else {
			ValidationComponentUtils.setMessageKey( component, name );
		}

		// Attach

		attachValidator( component, validator, path, metawidget );

		return component;
	}

	public void onEndBuild( SwingMetawidget metawidget ) {

		ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground( metawidget );
		ValidationComponentUtils.updateComponentTreeMandatoryBorder( metawidget );
	}

	//
	// Protected methods
	//

	/**
	 * Return the appropriate validator for the given JComponent with the given attributes.
	 *
	 * @param component
	 *            component validator will be attached to (by <code>attachValidator</code>)
	 * @param attributes
	 *            attributes of the widget to layout. Never null
	 * @param path
	 *            path to the domain object property (of the form com.myapp.Foo/bar/baz)
	 * @param metawidget
	 *            the owning Metawidget. May be useful if you wish to look up other WidgetProcessors
	 *            (such as
	 */

	protected Validator<?> getValidator( JComponent component, Map<String, String> attributes, String path, SwingMetawidget metawidget ) {

		return null;
	}

	/**
	 * Attach the given Validator to the given JComponent.
	 * <p>
	 * This method attaches the Validator, but largely delegates to <tt>validateChange</tt> for the
	 * actual validation work. Clients can override this method to attach Validators to other
	 * events, whilst still calling <tt>validateChange</tt> to perform the validation.
	 */

	protected void attachValidator( final JComponent component, final Validator<?> validator, final String path, final SwingMetawidget metawidget ) {

		component.addKeyListener( new KeyAdapter() {

			@Override
			public void keyReleased( KeyEvent event ) {

				validateChange( component, validator, path, metawidget );
			}
		} );
	}

	/**
	 * Execute the given Validator against the given JComponent.
	 */

	protected void validateChange( final JComponent component, final Validator<?> validator, String path, final SwingMetawidget metawidget ) {

		final String[] names = PathUtils.parsePath( path ).getNamesAsArray();

		// JGoodies' API concentrates on bulk updates of sub-components in a component tree.
		// For example the <code>ValidationComponentUtils.updateComponentTreeXXX</code> and
		// <code>ValidationComponentUtils.visitComponentTree</code> methods take a top-level
		// component and traverse it setting all validation messages for all sub-components.
		//
		// Because of this, when updating it is important to retain previous validation
		// results, or their messages will be lost during the bulk update of new validation
		// results

		@SuppressWarnings( "unchecked" )
		Map<JComponent, ValidationResult> validationResults = (Map<JComponent, ValidationResult>) metawidget.getClientProperty( JGoodiesValidatorProcessor.class );

		if ( validationResults == null ) {
			validationResults = CollectionUtils.newHashMap();
			metawidget.putClientProperty( JGoodiesValidatorProcessor.class, validationResults );
		}

		// Fetch the value...

		Object value = metawidget.getValue( names );

		// ...run it through the Validator...

		if ( validator != null ) {
			@SuppressWarnings( "unchecked" )
			Validator<Object> objectValidator = (Validator<Object>) validator;
			ValidationResult validationResult = objectValidator.validate( value );

			if ( validationResult == null ) {
				validationResults.remove( component );
			} else {
				validationResults.put( component, validationResult );
			}
		}

		// ...collate all ValidationResults...

		ValidationResult validationResult = new ValidationResult();
		for ( ValidationResult previousValidationResult : validationResults.values() ) {
			validationResult.addAllFrom( previousValidationResult );
		}

		// ...and update the UI

		updateComponent( component, validationResult, metawidget );
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

	protected void updateComponent( JComponent component, ValidationResult validationResult, SwingMetawidget metawidget ) {

		// Note: it may be nicer to only update the JComponent, not revisit the entire
		// tree, but JGoodies' built-in (private) MandatoryAndBlankBackgroundVisitor uses
		// its (private) restoreBackground, so seemingly this is the way JGoodies wants us
		// to do it

		if ( ValidationComponentUtils.isMandatory( component ) ) {
			ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground( metawidget );
		}

		// Do the severity background after the mandatory background, as presumably it
		// has precedence

		ValidationComponentUtils.updateComponentTreeSeverityBackground( metawidget, validationResult );
	}
}
