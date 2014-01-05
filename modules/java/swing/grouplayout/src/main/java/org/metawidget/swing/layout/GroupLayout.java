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
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.SimpleLayoutUtils;
import org.metawidget.util.simple.SimpleLayoutUtils.StrippedMnemonicAndFirstIndex;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to arrange widgets using <code>javax.swing.GroupLayout</code>.
 * <p>
 * Widgets are arranged in a table, with one column for labels and another for the widget.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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

				StrippedMnemonicAndFirstIndex stripMnemonic = SimpleLayoutUtils.stripMnemonic( labelText );
				String labelTextToUse = stripMnemonic.getStrippedMnemonic();

				if ( TRUE.equals( attributes.get( REQUIRED ) ) && !WidgetBuilderUtils.isReadOnly( attributes ) && !metawidget.isReadOnly() ) {
					labelTextToUse += "*";
				}

				label.setText( labelTextToUse + StringUtils.SEPARATOR_COLON );

				// Mnemonic

				label.setLabelFor( component );

				int mnemonicIndex = stripMnemonic.getFirstIndex();

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
