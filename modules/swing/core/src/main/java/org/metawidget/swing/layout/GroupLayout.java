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

import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.swing.Facet;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.Pair;
import org.metawidget.util.simple.SimpleLayoutUtils;

/**
 * Layout to arrange widgets using <code>javax.swing.GroupLayout</code>.
 * <p>
 * Widgets are arranged in a table, with one column for labels and another for the widget.
 *
 * @author Richard Kennard
 */

public class GroupLayout
	implements AdvancedLayout<JComponent, JComponent, SwingMetawidget> {

	//
	// Private statics
	//

	private static final Component[]	EMPTY_COMPONENTS_ARRAY	= new Component[] {};

	private static final int			COMPONENT_GAP			= 3;

	private static final String			LABEL_NAME_SUFFIX		= "_label";

	//
	// Public methods
	//

	@Override
	public void onStartBuild( SwingMetawidget metawidget ) {

		// Do nothing
	}

	@Override
	public void startContainerLayout( JComponent container, SwingMetawidget metawidget ) {

		javax.swing.GroupLayout groupLayout = new javax.swing.GroupLayout( container );
		container.setLayout( groupLayout );

		// Horizontal group

		container.putClientProperty( GroupLayout.class, null );
		State state = getState( container );
		state.groupHorizontal = groupLayout.createParallelGroup();
		groupLayout.setHorizontalGroup( state.groupHorizontal );

		state.labels = CollectionUtils.newArrayList();

		// Vertical group

		state.groupVertical = groupLayout.createSequentialGroup();
		groupLayout.setVerticalGroup( state.groupVertical );
	}

	@Override
	public void layoutWidget( JComponent component, String elementName, Map<String, String> attributes, JComponent container, SwingMetawidget metawidget ) {

		// Do not render empty stubs

		if ( component instanceof Stub && ( (Stub) component ).getComponentCount() == 0 ) {
			return;
		}

		javax.swing.GroupLayout groupLayout = (javax.swing.GroupLayout) container.getLayout();
		SequentialGroup sequentialGroup = groupLayout.createSequentialGroup();
		ParallelGroup parallelGroup = groupLayout.createParallelGroup( Alignment.BASELINE );

		State state = getState( container );
		state.groupHorizontal.addGroup( sequentialGroup );
		state.groupVertical.addGap( COMPONENT_GAP, COMPONENT_GAP, COMPONENT_GAP );
		state.groupVertical.addGroup( parallelGroup );

		// Label

		if ( attributes != null ) {
			String labelText = metawidget.getLabelString( attributes );

			if ( SimpleLayoutUtils.needsLabel( labelText, elementName ) ) {
				JLabel label = new JLabel();
				label.setName( attributes.get( NAME ) + LABEL_NAME_SUFFIX );

				// Required

				Pair<String, Integer> stripMnemonic = SimpleLayoutUtils.stripMnemonic( labelText );
				String labelTextToUse = stripMnemonic.getLeft();

				if ( TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY ) ) && !metawidget.isReadOnly() ) {
					labelTextToUse += "*";
				}

				label.setText( labelTextToUse + ":" );

				// Mnemonic

				label.setLabelFor( component );

				int mnemonicIndex = stripMnemonic.getRight();

				if ( mnemonicIndex != -1 ) {
					label.setDisplayedMnemonic( labelTextToUse.charAt( mnemonicIndex ) );
					label.setDisplayedMnemonicIndex( mnemonicIndex );
				}

				// Groups

				sequentialGroup.addComponent( label ).addGap( COMPONENT_GAP, COMPONENT_GAP, COMPONENT_GAP );
				parallelGroup.addComponent( label );

				state.labels.add( label );
			}
		}

		// Component

		sequentialGroup.addComponent( component );
		parallelGroup.addComponent( component );
	}

	@Override
	public void endContainerLayout( JComponent container, SwingMetawidget metawidget ) {

		// Make all labels the same width

		State state = getState( container );
		javax.swing.GroupLayout groupLayout = (javax.swing.GroupLayout) container.getLayout();

		if ( !state.labels.isEmpty() ) {
			groupLayout.linkSize( SwingConstants.HORIZONTAL, state.labels.toArray( EMPTY_COMPONENTS_ARRAY ) );
		}
	}

	@Override
	public void onEndBuild( SwingMetawidget metawidget ) {

		// Buttons

		Facet facetButtons = metawidget.getFacet( "buttons" );

		if ( facetButtons != null ) {
			State state = getState( metawidget );
			javax.swing.GroupLayout groupLayout = (javax.swing.GroupLayout) metawidget.getLayout();
			state.groupHorizontal.addGroup( groupLayout.createSequentialGroup().addComponent( facetButtons ) );
			state.groupVertical.addGap( COMPONENT_GAP, COMPONENT_GAP, COMPONENT_GAP );
			state.groupVertical.addGroup( groupLayout.createParallelGroup( Alignment.BASELINE ).addComponent( facetButtons ) );
		}
	}

	//
	// Private methods
	//

	private State getState( JComponent container ) {

		State state = (State) container.getClientProperty( GroupLayout.class );

		if ( state == null ) {
			state = new State();
			container.putClientProperty( GroupLayout.class, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */static class State {

		/* package private */ParallelGroup		groupHorizontal;

		/* package private */SequentialGroup	groupVertical;

		/* package private */List<JLabel>		labels;
	}
}
