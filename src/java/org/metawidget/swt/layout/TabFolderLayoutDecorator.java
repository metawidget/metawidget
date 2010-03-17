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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to decorate widgets from different sections using a TabFolder.
 *
 * @author Richard Kennard
 */

public class TabFolderLayoutDecorator
	extends SwtNestedSectionLayoutDecorator
{
	//
	// Private members
	//

	private final int			mTabLocation;

	//
	// Constructor
	//

	public TabFolderLayoutDecorator( TabFolderLayoutDecoratorConfig config )
	{
		super( config );

		mTabLocation = config.getTabLocation();
	}

	//
	// Protected methods
	//

	@Override
	protected Composite createSectionWidget( Composite previousSectionWidget, Map<String, String> attributes, Composite container, SwtMetawidget metawidget )
	{
		TabFolder tabFolder;

		// Whole new tabbed pane?

		if ( previousSectionWidget == null )
		{
			tabFolder = new TabFolder( container, SWT.NONE | mTabLocation );

			// Add to parent container

			Map<String, String> tabbedPaneAttributes = CollectionUtils.newHashMap();
			tabbedPaneAttributes.put( LABEL, "" );
			tabbedPaneAttributes.put( LARGE, TRUE );
			getDelegate().layoutWidget( tabFolder, PROPERTY, tabbedPaneAttributes, container, metawidget );
		}
		else
		{
			tabFolder = (TabFolder) previousSectionWidget.getParent();
		}

		// New tab

		TabItem tabItem = new TabItem( tabFolder, SWT.NONE );
		tabItem.setControl( new Composite( tabFolder, SWT.NONE ) );

		// Tab name (possibly localized)

		String section = getState( container, metawidget ).currentSection;
		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		tabItem.setText( localizedSection );

		return (Composite) tabItem.getControl();
	}
}
