// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.vaadin.ui.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.vaadin.ui.VaadinMetawidget;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;

/**
 * Layout to decorate widgets from different sections using <code>Label</code>s with
 * <code>styleName</code>s of h1, h2 etc.
 * 
 * @author Loghman Barari
 */

public class HeadingTagLayoutDecorator
	extends VaadinFlatSectionLayoutDecorator {

	//
	// Constructor
	//

	public HeadingTagLayoutDecorator( LayoutDecoratorConfig<Component, ComponentContainer, VaadinMetawidget> config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected void addSectionWidget( String section, int level, ComponentContainer container, VaadinMetawidget metawidget ) {

		String currentActiveSection = metawidget.getClientProperty( "currentActiveSection" );

		if ( ( currentActiveSection != null ) && currentActiveSection.equals( section ) ) {
			return;
		}

		Label label = new Label();

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			label.setValue( section );
		} else {
			label.setValue( localizedSection );
		}

		int headingLevel = level + 1;
		label.setStyleName( "h" + headingLevel );

		// Add to parent container

		Map<String, String> attributes = CollectionUtils.newHashMap();

		getDelegate().layoutWidget( label, PROPERTY, attributes, container, metawidget );

		metawidget.putClientProperty( "currentActiveSection", section );
	}
}
