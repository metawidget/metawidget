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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.swt.Facet;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.simple.SimpleLayoutUtils;

/**
 * Layout to arrange widgets using <code>org.eclipse.swt.layout.GridLayout</code>.
 * <p>
 * Widgets are arranged in a table, with one column for labels and another for the widget.
 *
 * @author Richard Kennard
 */

public class GridLayout
	implements AdvancedLayout<Control, Composite, SwtMetawidget> {

	//
	// Private statics
	//

	private static final int	LABEL_AND_CONTROL	= 2;

	private static final String	LABEL_NAME_SUFFIX	= "_label";

	//
	// Private members
	//

	private final int			mNumberOfColumns;

	private final int			mLabelAlignment;

	private final Font			mLabelFont;

	private final Color			mLabelForeground;

	private final String		mLabelSuffix;

	private final int			mRequiredAlignment;

	private final String		mRequiredText;

	//
	// Constructor
	//

	public GridLayout() {

		this( new GridLayoutConfig() );
	}

	public GridLayout( GridLayoutConfig config ) {

		mNumberOfColumns = config.getNumberOfColumns();
		mLabelAlignment = config.getLabelAlignment();
		mLabelForeground = config.getLabelForeground();
		mLabelFont = config.getLabelFont();
		mLabelSuffix = config.getLabelSuffix();
		mRequiredAlignment = config.getRequiredAlignment();
		mRequiredText = config.getRequiredText();
	}

	//
	// Public methods
	//

	public void onStartBuild( SwtMetawidget metawidget ) {

		// Do nothing
	}

	public void startContainerLayout( Composite composite, SwtMetawidget metawidget ) {

		// Calculate default label inset
		//
		// We top align all our labels, not just those belonging to 'tall' controls,
		// so that tall controls, regular controls and nested Metawidget controls all line up.
		// However, we still want the JLabels to be middle aligned for one-line controls (such as
		// Text boxes), so we top inset them a bit

		org.eclipse.swt.layout.GridLayout layoutManager = new org.eclipse.swt.layout.GridLayout( mNumberOfColumns * LABEL_AND_CONTROL, false );
		composite.setLayout( layoutManager );
	}

	public void layoutWidget( Control control, String elementName, Map<String, String> attributes, Composite composite, SwtMetawidget metawidget ) {

		// Do not layout space for empty stubs

		if ( control instanceof Stub && ( (Stub) control ).getChildren().length == 0 ) {
			GridData stubData = new GridData();
			stubData.exclude = true;
			control.setLayoutData( stubData );
			return;
		}

		// Special support for large controls

		boolean spanAllColumns = willFillHorizontally( control, attributes );

		if ( spanAllColumns ) {
			int numberOfChildren = countNonExcludedChildren( composite );
			int numberColumnsRemainingOnRow = numberOfChildren % ( mNumberOfColumns * LABEL_AND_CONTROL );

			if ( numberColumnsRemainingOnRow != 0 && numberOfChildren > 1 ) {
				Control lastControl = composite.getChildren()[numberOfChildren - 2];
				( (GridData) lastControl.getLayoutData() ).horizontalSpan = numberColumnsRemainingOnRow;
			}
		}

		// Layout a label...

		String labelText = null;

		if ( attributes != null ) {
			labelText = metawidget.getLabelString( attributes );
		}

		layoutBeforeChild( control, labelText, elementName, attributes, composite, metawidget );

		// ...and layout the control

		GridData controlLayoutData = new GridData();
		controlLayoutData.grabExcessHorizontalSpace = true;

		if ( !( control instanceof Button ) ) {
			controlLayoutData.horizontalAlignment = SWT.FILL;
			controlLayoutData.verticalAlignment = SWT.FILL;
		}

		if ( spanAllColumns ) {
			controlLayoutData.horizontalSpan = mNumberOfColumns * LABEL_AND_CONTROL;

			if ( SimpleLayoutUtils.needsLabel( labelText, elementName ) ) {
				controlLayoutData.horizontalSpan--;
			}
		} else if ( !SimpleLayoutUtils.needsLabel( labelText, elementName ) ) {
			controlLayoutData.horizontalSpan = LABEL_AND_CONTROL;
		}

		if ( willFillVertically( control, attributes ) ) {
			controlLayoutData.grabExcessVerticalSpace = true;
		}

		// Add it

		control.setLayoutData( controlLayoutData );
	}

	public void endContainerLayout( Composite composite, SwtMetawidget metawidget ) {

		// Do nothing
	}

	public void onEndBuild( SwtMetawidget metawidget ) {

		// Buttons

		Facet buttonsFacet = metawidget.getFacet( "buttons" );

		if ( buttonsFacet != null ) {
			GridData buttonLayoutData = new GridData();
			buttonLayoutData.horizontalSpan = 2;
			buttonLayoutData.horizontalAlignment = SWT.FILL;
			buttonLayoutData.grabExcessHorizontalSpace = true;
			buttonsFacet.setLayoutData( buttonLayoutData );

			buttonsFacet.moveBelow( null );
		}
	}

	//
	// Protected methods
	//

	protected String layoutBeforeChild( Control control, String labelText, String elementName, Map<String, String> attributes, Composite composite, SwtMetawidget metawidget ) {

		// Add label

		if ( SimpleLayoutUtils.needsLabel( labelText, elementName ) ) {
			Label label = new Label( composite, SWT.None );
			label.setData( NAME, attributes.get( NAME ) + LABEL_NAME_SUFFIX );

			if ( mLabelFont != null ) {
				label.setFont( mLabelFont );
			}

			if ( mLabelForeground != null ) {
				label.setForeground( mLabelForeground );
			}

			label.setAlignment( mLabelAlignment );

			// Required

			String labelTextToUse = labelText;

			if ( mRequiredText != null && TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY ) ) && !metawidget.isReadOnly() ) {
				if ( mRequiredAlignment == SWT.CENTER ) {
					labelTextToUse += mRequiredText;
				} else if ( mRequiredAlignment == SWT.LEFT ) {
					labelTextToUse = mRequiredText + labelTextToUse;
				}
			}

			if ( mLabelSuffix != null ) {
				labelTextToUse += mLabelSuffix;
			}

			label.setText( labelTextToUse );

			GridData labelLayoutData = new GridData();
			labelLayoutData.horizontalAlignment = SWT.FILL;
			labelLayoutData.verticalAlignment = SWT.FILL;

			label.setLayoutData( labelLayoutData );
			label.moveAbove( control );
		}

		return labelText;
	}

	protected boolean willFillHorizontally( Control control, Map<String, String> attributes ) {

		if ( control instanceof SwtMetawidget ) {
			return true;
		}

		return SimpleLayoutUtils.isSpanAllColumns( attributes );
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

	private int countNonExcludedChildren( Composite composite ) {

		int nonExcludedChildren = 0;

		for( Control control : composite.getChildren() ) {

			// (manually added components will have no GridData)

			GridData gridData = (GridData) control.getLayoutData();

			if ( gridData != null && gridData.exclude ) {
				continue;
			}

			nonExcludedChildren++;
		}

		return nonExcludedChildren;
	}
}
