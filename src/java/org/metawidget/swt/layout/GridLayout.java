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
 * Layout to arrange widgets using <code>javax.awt.GridLayout</code>.
 * <p>
 * Widgets are arranged in a table, with one column for labels and another for the widget.
 *
 * @author Richard Kennard
 */

public class GridLayout
	implements AdvancedLayout<Control, Composite, SwtMetawidget>
{
	//
	// Private statics
	//

	private final static int	LABEL_AND_CONTROL	= 2;

	private final static String	LABEL_NAME_SUFFIX	= "_label";

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

	public GridLayout()
	{
		this( new GridLayoutConfig() );
	}

	public GridLayout( GridLayoutConfig config )
	{
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

	public void onStartBuild( SwtMetawidget metawidget )
	{
		// Do nothing
	}

	public void startContainerLayout( Composite container, SwtMetawidget metawidget )
	{
		// Calculate default label inset
		//
		// We top align all our labels, not just those belonging to 'tall' components,
		// so that tall components, regular components and nested Metawidget components all line up.
		// However, we still want the JLabels to be middle aligned for one-line components (such as
		// JTextFields), so we top inset them a bit

		org.eclipse.swt.layout.GridLayout layoutManager = new org.eclipse.swt.layout.GridLayout( mNumberOfColumns * LABEL_AND_CONTROL, false );
		container.setLayout( layoutManager );
	}

	public void layoutWidget( Control component, String elementName, Map<String, String> attributes, Composite container, SwtMetawidget metawidget )
	{
		// Do not layout space for empty stubs

		if ( component instanceof Stub && ( (Stub) component ).getChildren().length == 0 )
		{
			GridData stubData = new GridData();
			stubData.exclude = true;
			component.setLayoutData( stubData );
			return;
		}

		// Special support for large components

		boolean spanAllColumns = willFillHorizontally( component, attributes );

		if ( spanAllColumns )
		{
			int numberOfChildren = container.getChildren().length;
			int numberColumnsRemainingOnRow = numberOfChildren % ( mNumberOfColumns * LABEL_AND_CONTROL );

			if ( numberColumnsRemainingOnRow != 0 && numberOfChildren > 1 )
			{
				Control lastControl = container.getChildren()[numberOfChildren - 2];
				((GridData) lastControl.getLayoutData()).horizontalSpan = numberColumnsRemainingOnRow;
			}
		}

		// Layout a label...

		String labelText = null;

		if ( attributes != null )
			labelText = metawidget.getLabelString( attributes );

		layoutBeforeChild( component, labelText, elementName, attributes, container, metawidget );

		// ...and layout the component

		GridData componentLayoutData = new GridData();
		componentLayoutData.grabExcessHorizontalSpace = true;

		if ( !( component instanceof Button ) )
		{
			componentLayoutData.horizontalAlignment = SWT.FILL;
			componentLayoutData.verticalAlignment = SWT.FILL;
		}

		if ( spanAllColumns )
			componentLayoutData.horizontalSpan = mNumberOfColumns * LABEL_AND_CONTROL;
		else if ( !SimpleLayoutUtils.needsLabel( labelText, elementName ) )
			componentLayoutData.horizontalSpan = LABEL_AND_CONTROL;

		if ( willFillVertically( component, attributes ) )
			componentLayoutData.grabExcessVerticalSpace = true;

		// Add it

		component.setLayoutData( componentLayoutData );
	}

	public void endContainerLayout( Composite container, SwtMetawidget metawidget )
	{
		// Do nothing
	}

	public void onEndBuild( SwtMetawidget metawidget )
	{
		// Buttons

		Facet buttonsFacet = metawidget.getFacet( "buttons" );

		if ( buttonsFacet != null )
		{
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

	protected String layoutBeforeChild( Control component, String labelText, String elementName, Map<String, String> attributes, Composite container, SwtMetawidget metawidget )
	{
		// Add label

		if ( SimpleLayoutUtils.needsLabel( labelText, elementName ) )
		{
			Label label = new Label( container, SWT.None );
			label.setData( NAME, attributes.get( NAME ) + LABEL_NAME_SUFFIX );

			if ( mLabelFont != null )
				label.setFont( mLabelFont );

			if ( mLabelForeground != null )
				label.setForeground( mLabelForeground );

			label.setAlignment( mLabelAlignment );

			// Required

			String labelTextToUse = labelText;

			if ( mRequiredText != null && TRUE.equals( attributes.get( REQUIRED ) ) && !TRUE.equals( attributes.get( READ_ONLY ) ) && !metawidget.isReadOnly() )
			{
				if ( mRequiredAlignment == SWT.CENTER )
					labelTextToUse += mRequiredText;
				else if ( mRequiredAlignment == SWT.LEFT )
					labelTextToUse = mRequiredText + labelTextToUse;

				labelTextToUse += mRequiredText;
			}

			if ( mLabelSuffix != null )
				labelTextToUse += mLabelSuffix;

			label.setText( labelTextToUse );

			GridData labelLayoutData = new GridData();
			labelLayoutData.horizontalAlignment = SWT.FILL;
			labelLayoutData.verticalAlignment = SWT.FILL;

			label.setLayoutData( labelLayoutData );
			label.moveAbove( component );
		}

		return labelText;
	}

	protected boolean willFillHorizontally( Control component, Map<String, String> attributes )
	{
		if ( component instanceof SwtMetawidget )
			return true;

		return SimpleLayoutUtils.isSpanAllColumns( attributes );
	}

	protected boolean willFillVertically( Control component, Map<String, String> attributes )
	{
		if ( attributes != null && TRUE.equals( attributes.get( LARGE ) ) )
			return true;

		return false;
	}
}
