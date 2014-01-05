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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to decorate widgets from different sections using a TabFolder.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TabFolderLayoutDecorator
	extends SwtNestedSectionLayoutDecorator {

	//
	// Private members
	//

	private final int	mTabLocation;

	//
	// Constructor
	//

	public TabFolderLayoutDecorator( TabFolderLayoutDecoratorConfig config ) {

		super( config );

		mTabLocation = config.getTabLocation();
	}

	//
	// Protected methods
	//

	@Override
	protected Composite createSectionWidget( Composite previousSectionWidget, String section, Map<String, String> attributes, Composite container, SwtMetawidget metawidget ) {

		TabFolder tabFolder;

		// Whole new tabbed pane?

		if ( previousSectionWidget == null ) {
			tabFolder = new TabFolder( container, SWT.NONE | mTabLocation );

			// Add to parent container

			Map<String, String> tabbedPaneAttributes = CollectionUtils.newHashMap();
			tabbedPaneAttributes.put( LABEL, "" );
			tabbedPaneAttributes.put( LARGE, TRUE );
			getDelegate().layoutWidget( tabFolder, PROPERTY, tabbedPaneAttributes, container, metawidget );
		} else {
			tabFolder = (TabFolder) previousSectionWidget.getParent();
		}

		// New tab

		TabItem tabItem = new TabItem( tabFolder, SWT.NONE );
		tabItem.setControl( new Composite( tabFolder, SWT.NONE ) );

		// Tab name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		tabItem.setText( localizedSection );

		return (Composite) tabItem.getControl();
	}
}
