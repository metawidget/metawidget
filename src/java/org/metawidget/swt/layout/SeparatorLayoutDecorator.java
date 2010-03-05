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

package org.metawidget.swt.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Insets;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to decorate widgets from different sections using a JSeparator.
 *
 * @author Richard Kennard
 */

public class SeparatorLayoutDecorator
	extends SwtFlatSectionLayoutDecorator
{
	//
	// Private statics
	//

	/**
	 * The border around the entire separator.
	 */

	private final static Border	BORDER_SECTION				= BorderFactory.createEmptyBorder( 5, 0, 5, 0 );

	/**
	 * The insets around the separator label.
	 */

	private final static Insets	INSETS_SECTION_LABEL_LEFT	= new Insets( 0, 0, 0, 5 );

	private final static Insets	INSETS_SECTION_LABEL_RIGHT	= new Insets( 0, 5, 0, 0 );

	//
	// Private members
	//

	private int					mAlignment;

	//
	// Constructor
	//

	public SeparatorLayoutDecorator( SeparatorLayoutDecoratorConfig config )
	{
		super( config );

		mAlignment = config.getAlignment();
	}

	//
	// Protected methods
	//

	@Override
	protected void addSectionWidget( Control control, String section, int level, Composite container, SwtMetawidget metawidget )
	{
		Composite separatorComposite = new Composite( container, SWT.NONE );
		separatorComposite.setLayout( new org.eclipse.swt.layout.GridLayout( 2, false ) );
		separatorComposite.moveAbove( control );

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		Label label = new Label( separatorComposite, SWT.NONE );
		label.setText( localizedSection );

		Label separator = new Label( separatorComposite, SWT.HORIZONTAL | SWT.SEPARATOR );
		GridData separatorLayoutData = new GridData();
		separatorLayoutData.horizontalAlignment = SWT.FILL;
		separatorLayoutData.grabExcessHorizontalSpace = true;
		separator.setLayoutData( separatorLayoutData );

		if ( mAlignment == SWT.RIGHT )
			separator.moveAbove( label );

		// Add to parent container

		Map<String, String> separatorCompositeAttributes = CollectionUtils.newHashMap();
		separatorCompositeAttributes.put( LABEL, "" );
		separatorCompositeAttributes.put( WIDE, TRUE );
		getDelegate().layoutWidget( separatorComposite, PROPERTY, separatorCompositeAttributes, container, metawidget );
	}
}
