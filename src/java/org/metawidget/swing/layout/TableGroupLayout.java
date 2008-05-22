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

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import org.metawidget.swing.Facet;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Layout to arrange widgets in a table, with one column for labels and another for the
 * widget, using <code>javax.swing.GroupLayout</code>.
 *
 * @author Richard Kennard
 */

public class TableGroupLayout
	extends Layout
{
	//
	//
	// Private statics
	//
	//

	private final static Component[]	EMPTY_COMPONENTS_ARRAY		= new Component[] {};

	private final static int			COMPONENT_GAP				= 3;

	private final static int			SEPARATOR_ADDITIONAL_GAP	= 8;

	private final static int			SEPARATOR_TEXT_GAP			= 3;

	private final static int			SEPARATOR_HEIGHT			= 2;

	//
	//
	// Private members
	//
	//

	private GroupLayout					mLayout;

	private ParallelGroup				mGroupHorizontal;

	private SequentialGroup				mGroupVertical;

	private String						mCurrentSection;

	private List<JLabel>				mLabels;

	//
	//
	// Constructor
	//
	//

	public TableGroupLayout( SwingMetawidget metawidget )
	{
		super( metawidget );
	}

	//
	//
	// Public methods
	//
	//

	@Override
	public void layoutBegin()
	{
		mLayout = new GroupLayout( getMetawidget() );
		getMetawidget().setLayout( mLayout );

		// Horziontal group

		mGroupHorizontal = mLayout.createParallelGroup();
		mLayout.setHorizontalGroup( mGroupHorizontal );

		mLabels = CollectionUtils.newArrayList();

		// Vertical group

		mGroupVertical = mLayout.createSequentialGroup();
		mLayout.setVerticalGroup( mGroupVertical );
	}

	@Override
	public void layoutChild( Component component, Map<String, String> attributes )
	{
		// Do not render empty stubs

		if ( component instanceof Stub && ( (Stub) component ).getComponentCount() == 0 )
			return;

		JLabel label = new JLabel();

		// Section headings

		if ( attributes != null )
		{
			String section = attributes.get( SECTION );

			if ( section != null && !section.equals( mCurrentSection ) )
			{
				mCurrentSection = section;
				layoutSection( section );
			}

			// Labels

			String labelText = getMetawidget().getLabelString( attributes );

			if ( labelText != null && !"".equals( labelText ) )
				label.setText( labelText + ":" );
		}

		// Add components

		mGroupHorizontal.addGroup( mLayout.createSequentialGroup().addComponent( label ).addGap( COMPONENT_GAP, COMPONENT_GAP, COMPONENT_GAP ).addComponent( component ) );
		mGroupVertical.addGap( COMPONENT_GAP, COMPONENT_GAP, COMPONENT_GAP );
		mGroupVertical.addGroup( mLayout.createParallelGroup( Alignment.BASELINE ).addComponent( label ).addComponent( component ) );

		mLabels.add( label );
	}

	@Override
	public void layoutEnd()
	{
		// Make all labels the same width

		if ( !mLabels.isEmpty() )
			mLayout.linkSize( SwingConstants.HORIZONTAL, mLabels.toArray( EMPTY_COMPONENTS_ARRAY ) );

		// Buttons

		Facet facetButtons = getMetawidget().getFacet( "buttons" );

		if ( facetButtons != null )
		{
			mGroupHorizontal.addGroup( mLayout.createSequentialGroup().addComponent( facetButtons ) );
			mGroupVertical.addGap( COMPONENT_GAP, COMPONENT_GAP, COMPONENT_GAP );
			mGroupVertical.addGroup( mLayout.createParallelGroup( Alignment.BASELINE ).addComponent( facetButtons ) );
		}
	}

	//
	//
	// Protected methods
	//
	//

	protected void layoutSection( String section )
	{
		if ( "".equals( section ) )
			return;

		// Section name (possibly localized)

		JLabel labelSection = new JLabel();

		String localizedSection = getMetawidget().getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection != null )
			labelSection.setText( localizedSection );
		else
			labelSection.setText( section );

		JSeparator separator = new JSeparator( SwingConstants.CENTER );

		mGroupHorizontal.addGroup( mLayout.createSequentialGroup().addComponent( labelSection ).addGap( SEPARATOR_TEXT_GAP, SEPARATOR_TEXT_GAP, SEPARATOR_TEXT_GAP ).addComponent( separator ) );
		mGroupVertical.addGap( COMPONENT_GAP + SEPARATOR_ADDITIONAL_GAP, COMPONENT_GAP + SEPARATOR_ADDITIONAL_GAP, COMPONENT_GAP + SEPARATOR_ADDITIONAL_GAP );
		mGroupVertical.addGroup( mLayout.createParallelGroup( Alignment.CENTER ).addGroup( mLayout.createSequentialGroup().addComponent( labelSection ) ).addGroup( mLayout.createSequentialGroup().addGap( SEPARATOR_TEXT_GAP, SEPARATOR_TEXT_GAP, SEPARATOR_TEXT_GAP ).addComponent( separator, javax.swing.GroupLayout.PREFERRED_SIZE, SEPARATOR_HEIGHT, javax.swing.GroupLayout.PREFERRED_SIZE ) ) );
		mGroupVertical.addGap( SEPARATOR_ADDITIONAL_GAP, SEPARATOR_ADDITIONAL_GAP, SEPARATOR_ADDITIONAL_GAP );
	}
}
