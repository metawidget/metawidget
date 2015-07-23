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

package org.metawidget.faces.component.html.layout.primefaces;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.layout.UIComponentNestedSectionLayoutDecorator;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;

/**
 * Layout to decorate widgets from different sections using a PrimeFaces TabView.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TabViewLayoutDecorator
	extends UIComponentNestedSectionLayoutDecorator {

	//
	// Private members
	//

	private int     mActiveIndex;

	private String 	mEffect;

	private String	mEffectDuration;

	private boolean mDynamic;

	private boolean	mCache;

	private String  mOrientation;

	private String  mStyle;

	private String  mStyleClass;

	//
	// Constructor
	//

	public TabViewLayoutDecorator( TabViewLayoutDecoratorConfig config ) {

		super( config );

		mActiveIndex = config.getActiveIndex();
		mEffect = config.getEffect();
		mEffectDuration = config.getEffectDuration();
		mDynamic = config.isDynamic();
		mCache = config.isCache();
		mOrientation = config.getOrientation();
		mStyle = config.getStyle();
		mStyleClass = config.getStyleClass();
	}

	//
	// Protected methods
	//

	@Override
	protected UIComponent createNewSectionWidget( UIComponent previousSectionWidget, String section, Map<String, String> attributes, UIComponent container, UIMetawidget metawidget ) {

		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		TabView tabView;

		// Whole new PanelTabSet?

		if ( previousSectionWidget == null ) {
			tabView = FacesUtils.createComponent( TabView.COMPONENT_TYPE, "org.primefaces.component.TabViewRenderer" );
			tabView.setId( FacesUtils.createUniqueId() );
			tabView.setActiveIndex( mActiveIndex );
			tabView.setEffect( mEffect );
			tabView.setEffectDuration( mEffectDuration );
			tabView.setDynamic( mDynamic );
			tabView.setCache( mCache );
			tabView.setOrientation( mOrientation );
			tabView.setStyle( mStyle);
			tabView.setStyleClass( mStyleClass );

			// Add to parent container

			Map<String, String> tabPanelAttributes = CollectionUtils.newHashMap();
			tabPanelAttributes.put( LABEL, "" );
			tabView.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, tabPanelAttributes );

			getDelegate().layoutWidget( tabView, PROPERTY, tabPanelAttributes, container, metawidget );
		} else {
			tabView = (TabView) previousSectionWidget.getParent().getParent();
		}

		// New tab

		Tab tab = FacesUtils.createComponent( Tab.COMPONENT_TYPE, null );
		tab.setId( FacesUtils.createUniqueId() );
		tabView.getChildren().add( tab );

		// Tab name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		tab.setTitle( localizedSection );

		// Create nested Metawidget (for layout)

		UIMetawidget nestedMetawidget = (UIMetawidget) application.createComponent( metawidget.getComponentType() );
		nestedMetawidget.setRendererType( metawidget.getRendererType() );
		nestedMetawidget.setId( FacesUtils.createUniqueId() );
		nestedMetawidget.setLayout( metawidget.getLayout() );
		nestedMetawidget.copyParameters( metawidget );
		tab.getChildren().add( nestedMetawidget );

		return nestedMetawidget;
	}
}
