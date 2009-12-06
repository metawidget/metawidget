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

package org.metawidget.faces.component.html.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.layout.decorator.LayoutDecorator;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to separate widgets in different sections using an HtmlOutputText.
 * <p>
 * As dictated by the JSF spec, CSS styles and style classes applied to an HtmlOptionText are
 * wrapped in an HTML span tag. Therefore you must use CSS 'display: block' if you want to use
 * margins or padding around the HtmlOutputText.
 *
 * @author Richard Kennard
 */

public class OutputTextSectionLayout
	extends LayoutDecorator<UIComponent, UIMetawidget>
{
	//
	// Private members
	//

	private String	mStyle;

	private String	mStyleClass;

	//
	// Constructor
	//

	public OutputTextSectionLayout( OutputTextSectionLayoutConfig config )
	{
		super( config );

		mStyle = config.getStyle();
		mStyleClass = config.getStyleClass();
	}

	//
	// Public methods
	//

	@Override
	public void layoutWidget( UIComponent component, String elementName, Map<String, String> attributes, UIComponent container, UIMetawidget metawidget )
	{
		String[] sections = ArrayUtils.fromString( attributes.get( SECTION ) );
		State state = getState( container, metawidget );

		// Stay where we are?

		if ( sections.length == 0 || sections.equals( state.currentSections ) )
		{
			super.layoutWidget( component, elementName, attributes, container, metawidget );
			return;
		}

		// For each of the new sections...

		for ( int loop = 0; loop < sections.length; loop++ )
		{
			String section = sections[loop];

			// ...that are different from our current...

			if ( "".equals( section ) )
				continue;

			if ( state.currentSections != null && loop < state.currentSections.length && section.equals( state.currentSections[loop] ) )
				continue;

			// ...add a heading

			FacesContext context = FacesContext.getCurrentInstance();
			Application application = context.getApplication();
			HtmlOutputText heading = (HtmlOutputText) application.createComponent( "javax.faces.HtmlOutputText" );
			heading.setId( context.getViewRoot().createUniqueId() );
			heading.setStyle( mStyle );
			heading.setStyleClass( mStyleClass );

			// Section name (possibly localized)

			String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

			if ( localizedSection == null )
				localizedSection = section;

			heading.setValue( localizedSection );

			// Add to parent container
			//
			// Note: unfortunately this outputs as a SPAN tag, so you'll need to put 'display:
			// block' in your CSS if you want to use margins. Vanilla JSF doesn't have anything that
			// outputs a DIV tag

			Map<String, String> metadataAttributes = CollectionUtils.newHashMap();
			metadataAttributes.put( LABEL, "" );
			metadataAttributes.put( WIDE, TRUE );
			heading.getAttributes().put( UIMetawidget.COMPONENT_ATTRIBUTE_METADATA, metadataAttributes );

			super.layoutWidget( heading, PROPERTY, metadataAttributes, container, metawidget );
		}

		state.currentSections = sections;

		// Add component as normal

		super.layoutWidget( component, elementName, attributes, container, metawidget );
	}

	//
	// Private methods
	//

	private State getState( UIComponent container, UIMetawidget metawidget )
	{
		@SuppressWarnings( "unchecked" )
		Map<UIComponent, State> stateMap = (Map<UIComponent, State>) metawidget.getClientProperty( OutputTextSectionLayout.class );

		if ( stateMap == null )
		{
			stateMap = CollectionUtils.newHashMap();
			metawidget.putClientProperty( OutputTextSectionLayout.class, stateMap );
		}

		State state = stateMap.get( container );

		if ( state == null )
		{
			state = new State();
			stateMap.put( container, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */class State
	{
		public String[]	currentSections;
	}
}
