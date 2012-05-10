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

import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.swt.Facet;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.SimpleLayoutUtils;

/**
 * Layout to arrange widgets using <code>net.miginfocom.swing.MigLayout</code>.
 * <p>
 * Widgets are arranged in a table, with one column for labels and another for the widget.
 *
 * @author Stefan Ackerman
 */

public class MigLayout
	implements AdvancedLayout<Control, Composite, SwtMetawidget> {

	//
	// Private members
	//

	private final int	mNumberOfColumns;

	//
	// Constructor
	//

	public MigLayout() {

		this( new MigLayoutConfig() );
	}

	public MigLayout( MigLayoutConfig config ) {

		mNumberOfColumns = config.getNumberOfColumns();
	}

	//
	// Public methods
	//

	public void onStartBuild( SwtMetawidget metawidget ) {

		// Do nothing
	}

	public void startContainerLayout( Composite container, SwtMetawidget metawidget ) {

		container.setData( MigLayout.class.getName(), null );
		State state = getState( container );

		// The entire layout should fill both horizontally and vertically,
		// with no insets. This allows us to be properly nested, as well as
		// embedded within existing UIs, without alignment problems

		LC layoutConstraints = new LC().insets( "0" );

		// Debug Info (draws the red and blue lines)
		//
		// layoutConstraints.debug( 500 );

		// Create the Layout
		//
		// Note: we don't use column/row constraints, because we don't know
		// what the controls will be in advance. Rather, we use 'cell' and 'push'

		org.eclipse.swt.widgets.Layout layout = new net.miginfocom.swt.MigLayout( layoutConstraints );
		container.setLayout( layout );

		// Calculate default label inset
		//
		// We top align all our labels, not just those belonging to 'tall' controls,
		// so that tall controls, regular controls and nested Metawidget controls all line up.
		// However, we still want the Labels to be middle aligned for one-line controls (such as
		// Text boxes), so we top inset them a bit

		state.defaultLabelVerticalPadding = new GC( container ).getFontMetrics().getLeading();
	}

	public void layoutWidget( Control control, String elementName, Map<String, String> attributes, Composite container, SwtMetawidget metawidget ) {

		// Do not render empty stubs

		if ( control instanceof Stub && ( (Stub) control ).getChildren().length == 0 ) {
			// Must 'CC.setHideMode', because already added to Composite

			CC controlConstraints = new CC();
			controlConstraints.setHideMode( 3 );
			control.setLayoutData( controlConstraints );
			control.setVisible( false );
			return;
		}

		// Special support for large controls

		State state = getState( container );
		boolean spanAllColumns = ( control instanceof SwtMetawidget || SimpleLayoutUtils.isSpanAllColumns( attributes ) );

		if ( spanAllColumns && state.currentColumn > 0 ) {
			state.currentColumn = 0;
			state.currentRow++;
		}

		// Layout a label...

		String labelText = null;

		if ( attributes != null ) {
			labelText = metawidget.getLabelString( attributes );
		}

		layoutBeforeChild( control, labelText, elementName, attributes, container, metawidget );

		// ...and layout the control

		CC controlConstraints = new CC();

		if ( labelText != null ) {
			controlConstraints.cell( ( state.currentColumn * 2 ) + 1, state.currentRow );
		} else {
			controlConstraints.cell( state.currentColumn * 2, state.currentRow );
			controlConstraints.spanX( 2 );
		}

		controlConstraints.pushX( 1f ).growX();

		if ( spanAllColumns ) {
			controlConstraints.spanX();
			state.currentColumn = mNumberOfColumns;
		}

		controlConstraints.alignY( "top" );

		// Assume large controls should grow vertically

		if ( willFillVertically( control, attributes ) ) {
			controlConstraints.pushY( 1f ).growY();
		}

		// Add it

		control.setLayoutData( controlConstraints );

		state.currentColumn++;

		if ( state.currentColumn >= mNumberOfColumns ) {
			state.currentColumn = 0;
			state.currentRow++;
		}
	}

	public void endContainerLayout( Composite container, SwtMetawidget metawidget ) {

		// Do nothing
	}

	public void onEndBuild( SwtMetawidget metawidget ) {

		// Buttons

		Facet buttonsFacet = metawidget.getFacet( "buttons" );

		if ( buttonsFacet != null ) {
			State state = getState( metawidget );

			if ( state.currentColumn > 0 ) {
				state.currentColumn = 0;
				state.currentRow++;
			}

			buttonsFacet.setLayoutData( new CC().cell( 0, state.currentRow ).spanX().growX() );
			buttonsFacet.moveBelow( null );
		}
	}

	//
	// Protected methods
	//

	protected String layoutBeforeChild( Control control, String labelText, String elementName, Map<String, String> attributes, Control container, SwtMetawidget metawidget ) {

		State state = getState( (Composite) container );

		// Add label

		if ( SimpleLayoutUtils.needsLabel( labelText, elementName ) ) {
			Label label = new Label( ( (Composite) container ), SWT.None );

			// Required

			if ( attributes != null && TRUE.equals( attributes.get( REQUIRED ) ) && !WidgetBuilderUtils.isReadOnly( attributes ) && !metawidget.isReadOnly() ) {
				label.setText( labelText + "*:" );
			} else {
				label.setText( labelText + ":" );
			}

			CC labelConstraints = new CC();
			labelConstraints.cell( state.currentColumn * 2, state.currentRow );
			labelConstraints.growX();

			// Top align all labels, not just those belonging to 'tall' controls,
			// so that tall controls, regular controls and nested Metawidget
			// controls all line up

			labelConstraints.alignY( "top" );

			// Apply some vertical padding so the label lines up with the control nicely

			labelConstraints.pad( state.defaultLabelVerticalPadding, 0, state.defaultLabelVerticalPadding, 0 );

			// Add it

			label.setLayoutData( labelConstraints );
			label.moveAbove( control );
		}

		return labelText;
	}

	/**
	 * @param control
	 *            control that may fill vertically
	 */

	protected boolean willFillVertically( Control control, Map<String, String> attributes ) {

		if ( attributes != null && TRUE.equals( attributes.get( LARGE ) ) ) {
			return true;
		}

		return false;
	}

	//
	// Private methods
	//

	private State getState( Composite container ) {

		State state = (State) container.getData( MigLayout.class.getName() );

		if ( state == null ) {
			state = new State();
			container.setData( MigLayout.class.getName(), state );
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

		/* package private */int	currentColumn;

		/* package private */int	currentRow;

		/* package private */int	defaultLabelVerticalPadding;
	}
}
