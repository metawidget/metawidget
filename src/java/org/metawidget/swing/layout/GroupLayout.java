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

package org.metawidget.swing.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Component;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import org.metawidget.layout.iface.Layout;
import org.metawidget.layout.impl.LayoutUtils;
import org.metawidget.swing.Facet;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to arrange widgets using <code>javax.swing.GroupLayout</code>.
 * <p>
 * Widgets are arranged in a table, with one column for labels and another for the widget.
 *
 * @author Richard Kennard
 */

public class GroupLayout
	implements Layout<JComponent, SwingMetawidget>
{
	//
	// Private statics
	//

	private final static Component[]	EMPTY_COMPONENTS_ARRAY		= new Component[] {};

	private final static int			COMPONENT_GAP				= 3;

	private final static int			SEPARATOR_ADDITIONAL_GAP	= 8;

	private final static int			SEPARATOR_TEXT_GAP			= 3;

	private final static int			SEPARATOR_HEIGHT			= 2;

	//
	// Public methods
	//

	public void onStartBuild( SwingMetawidget metawidget )
	{
		javax.swing.GroupLayout groupLayout = new javax.swing.GroupLayout( metawidget );
		metawidget.setLayout( groupLayout );

		// Horizontal group

		metawidget.putClientProperty( GroupLayout.class, null );
		State state = getState( metawidget );
		state.groupHorizontal = groupLayout.createParallelGroup();
		groupLayout.setHorizontalGroup( state.groupHorizontal );

		state.labels = CollectionUtils.newArrayList();

		// Vertical group

		state.groupVertical = groupLayout.createSequentialGroup();
		groupLayout.setVerticalGroup( state.groupVertical );
	}

	public void layoutChild( JComponent component, String elementName, Map<String, String> attributes, SwingMetawidget metawidget )
	{
		// Do not render empty stubs

		if ( component instanceof Stub && ( (Stub) component ).getComponentCount() == 0 )
			return;

		JLabel label = new JLabel();

		// Section headings

		State state = getState( metawidget );

		if ( attributes != null )
		{
			String section = attributes.get( SECTION );

			if ( section != null && !section.equals( state.currentSection ) )
			{
				state.currentSection = section;
				layoutSection( section, metawidget );
			}

			// Labels

			String labelText = metawidget.getLabelString( attributes );

			if ( LayoutUtils.needsLabel( labelText, elementName ))
			{
				// Required

				if ( TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY ) ) && !metawidget.isReadOnly() )
					labelText += "*";

				label.setText( labelText + ":" );
			}
		}

		// Add components

		javax.swing.GroupLayout groupLayout = (javax.swing.GroupLayout) metawidget.getLayout();
		state.groupHorizontal.addGroup( groupLayout.createSequentialGroup().addComponent( label ).addGap( COMPONENT_GAP, COMPONENT_GAP, COMPONENT_GAP ).addComponent( component ) );
		state.groupVertical.addGap( COMPONENT_GAP, COMPONENT_GAP, COMPONENT_GAP );
		state.groupVertical.addGroup( groupLayout.createParallelGroup( Alignment.BASELINE ).addComponent( label ).addComponent( component ) );

		state.labels.add( label );
	}

	public void onEndBuild( SwingMetawidget metawidget )
	{
		// Make all labels the same width

		State state = getState( metawidget );
		javax.swing.GroupLayout groupLayout = (javax.swing.GroupLayout) metawidget.getLayout();

		if ( !state.labels.isEmpty() )
			groupLayout.linkSize( SwingConstants.HORIZONTAL, state.labels.toArray( EMPTY_COMPONENTS_ARRAY ) );

		// Buttons

		Facet facetButtons = metawidget.getFacet( "buttons" );

		if ( facetButtons != null )
		{
			state.groupHorizontal.addGroup( groupLayout.createSequentialGroup().addComponent( facetButtons ) );
			state.groupVertical.addGap( COMPONENT_GAP, COMPONENT_GAP, COMPONENT_GAP );
			state.groupVertical.addGroup( groupLayout.createParallelGroup( Alignment.BASELINE ).addComponent( facetButtons ) );
		}
	}

	//
	// Protected methods
	//

	protected void layoutSection( String section, SwingMetawidget metawidget )
	{
		if ( "".equals( section ) )
			return;

		// Section name (possibly localized)

		JLabel labelSection = new JLabel();

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection != null )
			labelSection.setText( localizedSection );
		else
			labelSection.setText( section );

		JSeparator separator = new JSeparator( SwingConstants.CENTER );

		State state = getState( metawidget );
		javax.swing.GroupLayout groupLayout = (javax.swing.GroupLayout) metawidget.getLayout();

		state.groupHorizontal.addGroup( groupLayout.createSequentialGroup().addComponent( labelSection ).addGap( SEPARATOR_TEXT_GAP, SEPARATOR_TEXT_GAP, SEPARATOR_TEXT_GAP ).addComponent( separator ) );
		state.groupVertical.addGap( COMPONENT_GAP + SEPARATOR_ADDITIONAL_GAP, COMPONENT_GAP + SEPARATOR_ADDITIONAL_GAP, COMPONENT_GAP + SEPARATOR_ADDITIONAL_GAP );
		state.groupVertical.addGroup( groupLayout.createParallelGroup( Alignment.CENTER ).addGroup( groupLayout.createSequentialGroup().addComponent( labelSection ) ).addGroup( groupLayout.createSequentialGroup().addGap( SEPARATOR_TEXT_GAP, SEPARATOR_TEXT_GAP, SEPARATOR_TEXT_GAP ).addComponent( separator, javax.swing.GroupLayout.PREFERRED_SIZE, SEPARATOR_HEIGHT, javax.swing.GroupLayout.PREFERRED_SIZE ) ) );
		state.groupVertical.addGap( SEPARATOR_ADDITIONAL_GAP, SEPARATOR_ADDITIONAL_GAP, SEPARATOR_ADDITIONAL_GAP );
	}

	//
	// Private methods
	//

	private State getState( SwingMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( GroupLayout.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( GroupLayout.class, state );
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
		/* package private */ParallelGroup		groupHorizontal;

		/* package private */SequentialGroup	groupVertical;

		/* package private */String				currentSection;

		/* package private */List<JLabel>		labels;
	}
}
