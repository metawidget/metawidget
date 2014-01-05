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

package org.metawidget.swt.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to decorate widgets from different sections using a JSeparator.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SeparatorLayoutDecorator
	extends SwtFlatSectionLayoutDecorator {

	//
	// Private members
	//

	private int	mAlignment;

	//
	// Constructor
	//

	public SeparatorLayoutDecorator( SeparatorLayoutDecoratorConfig config ) {

		super( config );

		mAlignment = config.getAlignment();
	}

	//
	// Protected methods
	//

	@Override
	protected void addSectionWidget( String section, int level, Composite container, SwtMetawidget metawidget ) {

		Composite separatorComposite = new Composite( container, SWT.NONE );
		GridLayout gridLayout = new GridLayout( 2, false );
		gridLayout.marginWidth = 0;
		separatorComposite.setLayout( gridLayout );

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		Label label = new Label( separatorComposite, SWT.NONE );
		label.setText( localizedSection );

		Label separator = new Label( separatorComposite, SWT.HORIZONTAL | SWT.SEPARATOR );
		GridData separatorLayoutData = new GridData();
		separatorLayoutData.horizontalAlignment = SWT.FILL;
		separatorLayoutData.grabExcessHorizontalSpace = true;
		separator.setLayoutData( separatorLayoutData );

		if ( mAlignment == SWT.RIGHT ) {
			separator.moveAbove( label );
		}

		// Add to parent container

		Map<String, String> separatorCompositeAttributes = CollectionUtils.newHashMap();
		separatorCompositeAttributes.put( LABEL, "" );
		separatorCompositeAttributes.put( WIDE, TRUE );
		getDelegate().layoutWidget( separatorComposite, PROPERTY, separatorCompositeAttributes, container, metawidget );
	}
}
