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

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.layout.UIComponentNestedSectionLayoutDecorator;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.tabview.Tab;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;

import static org.metawidget.inspector.InspectionResultConstants.LABEL;
import static org.metawidget.inspector.InspectionResultConstants.PROPERTY;

/**
 * Layout to decorate widgets from different sections using a PrimeFaces AccordionPanel
 *
 * @author DanilAREFY
 */

public class AccordionPanelLayoutDecorator
        extends UIComponentNestedSectionLayoutDecorator {

    //
    // Private members
    //

    private String  mActiveIndex;

    private boolean mDynamic;

    private boolean mCache;

    private boolean mMultiple;

    private String  mStyle;

    private String  mStyleClass;

    //
    // Constructor
    //

    public AccordionPanelLayoutDecorator( AccordionPanelLayoutDecoratorConfig config ) {

        super( config );

        mActiveIndex = config.getActiveIndex();
        mDynamic = config.isDynamic();
        mCache = config.isCache();
        mMultiple = config.isMultiple();
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

        AccordionPanel accordionPanel;

        if ( previousSectionWidget == null ) {
            accordionPanel = FacesUtils.createComponent( AccordionPanel.COMPONENT_TYPE, "org.primefaces.component.AccordionPanelRenderer" );
            accordionPanel.setId( FacesUtils.createUniqueId() );
            accordionPanel.setActiveIndex( mActiveIndex );
            accordionPanel.setDynamic( mDynamic );
            accordionPanel.setCache( mCache );
            accordionPanel.setMultiple( mMultiple );
            accordionPanel.setStyle( mStyle );
            accordionPanel.setStyleClass( mStyleClass );

            // Add to parent container

            Map<String, String> tabPanelAttributes = CollectionUtils.newHashMap();
            tabPanelAttributes.put( LABEL, "" );
            accordionPanel.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, tabPanelAttributes );

            getDelegate().layoutWidget( accordionPanel, PROPERTY, tabPanelAttributes, container, metawidget );
        } else {
            accordionPanel = (AccordionPanel) previousSectionWidget.getParent().getParent();
        }

        // New tab

        Tab tab = FacesUtils.createComponent( Tab.COMPONENT_TYPE, "org.primefaces.component.TabRenderer" );
        tab.setId( FacesUtils.createUniqueId() );
        accordionPanel.getChildren().add( tab );

        // Tab name (possibly localized)

        String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase(section) );

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
