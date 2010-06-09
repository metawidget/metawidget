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
 * @author Richard Kennard
 */

public class SeparatorLayoutDecorator
	extends SwtFlatSectionLayoutDecorator
{
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
	protected void addSectionWidget( String section, int level, Composite container, SwtMetawidget metawidget )
	{
		Composite separatorComposite = new Composite( container, SWT.NONE );
		GridLayout gridLayout = new GridLayout( 2, false );
		gridLayout.marginWidth = 0;
		separatorComposite.setLayout( gridLayout );

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
		{
			localizedSection = section;
		}

		Label label = new Label( separatorComposite, SWT.NONE );
		label.setText( localizedSection );

		Label separator = new Label( separatorComposite, SWT.HORIZONTAL | SWT.SEPARATOR );
		GridData separatorLayoutData = new GridData();
		separatorLayoutData.horizontalAlignment = SWT.FILL;
		separatorLayoutData.grabExcessHorizontalSpace = true;
		separator.setLayoutData( separatorLayoutData );

		if ( mAlignment == SWT.RIGHT )
		{
			separator.moveAbove( label );
		}

		// Add to parent container

		Map<String, String> separatorCompositeAttributes = CollectionUtils.newHashMap();
		separatorCompositeAttributes.put( LABEL, "" );
		separatorCompositeAttributes.put( WIDE, TRUE );
		getDelegate().layoutWidget( separatorComposite, PROPERTY, separatorCompositeAttributes, container, metawidget );
	}
}
