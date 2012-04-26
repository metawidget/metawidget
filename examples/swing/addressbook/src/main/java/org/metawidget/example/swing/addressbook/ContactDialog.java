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

package org.metawidget.example.swing.addressbook;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;

import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.swing.Facet;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.layout.FlowLayout;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Dialog box for Address Book Contacts.
 * <p>
 * Note: for brevity, this example is not optimized to use <code>rebind</code> (see 'rebinding' in
 * the Reference Documentation). For an example using rebinding, see
 * <code>org.metawidget.example.gwt.addressbook.client.ui.ContactDialog</code>.
 *
 * @author Richard Kennard
 */

public class ContactDialog
	extends JDialog {

	//
	// Private statics
	//

	private static final int				COMPONENT_SPACING	= 5;

	//
	// Package-level members
	//

	//
	// Private members
	//

	private ContactsControllerProvider		mProvider;

	/* package private */SwingMetawidget	mContactMetawidget;

	ListTableModel<Communication>			mCommunicationsModel;

	private SwingMetawidget					mButtonsMetawidget;

	/* package private */boolean			mShowConfirmDialog	= true;

	//
	// Constructor
	//

	@SuppressWarnings( "serial" )
	public ContactDialog( ContactsControllerProvider provider, final Contact contact ) {

		mProvider = provider;

		setSize( new Dimension( 800, 600 ) );
		getContentPane().setBackground( Color.white );

		// Background

		ImagePanel panelBackground = new ImagePanel();
		panelBackground.setImage( Thread.currentThread().getContextClassLoader().getResource( "media/background.jpg" ) );
		panelBackground.setLayout( new BorderLayout() );
		add( panelBackground );

		// Left-hand image

		JLabel imageLabel = new JLabel();
		imageLabel.setVerticalAlignment( SwingConstants.TOP );
		imageLabel.setBorder( BorderFactory.createEmptyBorder( COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING * 2 ) );
		panelBackground.add( imageLabel, BorderLayout.WEST );

		// Bundle

		ResourceBundle bundle = ResourceBundle.getBundle( "org.metawidget.example.shared.addressbook.resource.Resources" );

		// Title

		StringBuilder builder = new StringBuilder( contact.getFullname() );

		if ( builder.length() > 0 ) {
			builder.append( " - " );
		}

		// Personal/business icon

		if ( contact instanceof PersonalContact ) {
			builder.append( bundle.getString( "personalContact" ) );
			imageLabel.setIcon( new ImageIcon( Thread.currentThread().getContextClassLoader().getResource( "media/personal.gif" ) ) );
		} else {
			builder.append( bundle.getString( "businessContact" ) );
			imageLabel.setIcon( new ImageIcon( Thread.currentThread().getContextClassLoader().getResource( "media/business.gif" ) ) );
		}

		setTitle( builder.toString() );

		// Metawidget

		mContactMetawidget = new SwingMetawidget();
		mContactMetawidget.setBorder( BorderFactory.createEmptyBorder( COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING ) );
		mContactMetawidget.setConfig( "org/metawidget/example/swing/addressbook/metawidget.xml" );
		mContactMetawidget.setToInspect( contact );
		mContactMetawidget.setReadOnly( contact.getId() != 0 );
		panelBackground.add( mContactMetawidget, BorderLayout.CENTER );

		// Communications override

		mCommunicationsModel = new ListTableModel<Communication>( Communication.class, contact.getCommunications(), "Type", "Value" );
		final JTable communicationsTable = new JTable( mCommunicationsModel );
		communicationsTable.putClientProperty( "terminateEditOnFocusLost", Boolean.TRUE );
		communicationsTable.setDefaultEditor( Object.class, new CommunicationEditor() );

		JScrollPane scrollPane = new JScrollPane( communicationsTable );
		scrollPane.setName( "communications" );
		scrollPane.setPreferredSize( new Dimension( 150, 150 ) );

		final JPopupMenu menuPopup = new JPopupMenu();
		menuPopup.add( new AbstractAction( bundle.getString( "delete" ) ) {

			public void actionPerformed( ActionEvent event ) {

				menuPopup.setVisible( false );

				if ( mShowConfirmDialog && JOptionPane.showConfirmDialog( ContactDialog.this, "Sure you want to delete this communication?" ) != JOptionPane.OK_OPTION ) {
					return;
				}

				int rowAtPoint = communicationsTable.rowAtPoint( menuPopup.getLocation() );

				if ( rowAtPoint < 0 || rowAtPoint >= mCommunicationsModel.getRowCount() ) {
					return;
				}

				Communication communication = mCommunicationsModel.getValueAt( rowAtPoint );
				contact.setCommunications( CollectionUtils.newHashSet( mCommunicationsModel.exportList() ) );
				contact.removeCommunication( communication );
				mCommunicationsModel.importCollection( contact.getCommunications() );
			}
		} );
		communicationsTable.add( menuPopup );

		communicationsTable.addMouseListener( new MouseAdapter() {

			@Override
			public void mouseReleased( MouseEvent event ) {

				if ( event.isPopupTrigger() && !mContactMetawidget.isReadOnly() ) {

					Component source = (Component) event.getSource();

					// (source will not be 'showing on screen' during unit tests)

					if ( source.isShowing() ) {
						Point screenLocation = source.getLocationOnScreen();
						menuPopup.setLocation( screenLocation.x + event.getX(), screenLocation.y + event.getY() );
					}

					if ( communicationsTable.getCellEditor() != null ) {
						communicationsTable.getCellEditor().stopCellEditing();
					}

					menuPopup.setVisible( true );
				}
			}
		} );

		mContactMetawidget.add( scrollPane );

		// Embedded buttons

		Facet facetButtons = new Facet();
		facetButtons.setName( "buttons" );
		facetButtons.setOpaque( false );
		mContactMetawidget.add( facetButtons );

		mButtonsMetawidget = new SwingMetawidget();
		mButtonsMetawidget.setConfig( "org/metawidget/example/swing/addressbook/metawidget.xml" );
		mButtonsMetawidget.setMetawidgetLayout( new FlowLayout() );
		mButtonsMetawidget.setToInspect( this );
		facetButtons.add( mButtonsMetawidget );
	}

	//
	// Public methods
	//

	@UiHidden
	public boolean isReadOnly() {

		return mContactMetawidget.isReadOnly();
	}

	/**
	 * For unit tests
	 */

	@UiHidden
	public void setShowConfirmDialog( boolean showConfirmDialog ) {

		mShowConfirmDialog = showConfirmDialog;
	}

	@UiAction
	@UiAttribute( name = HIDDEN, value = "${!this.readOnly}" )
	public void edit() {

		mContactMetawidget.setReadOnly( false );

		mCommunicationsModel.setEditable( true );
		mCommunicationsModel.setExtraBlankRow( true );
		mButtonsMetawidget.setToInspect( mButtonsMetawidget.getToInspect() );
	}

	@UiAction
	@UiAttribute( name = HIDDEN, value = "${this.readOnly}" )
	public void save() {

		try {
			mContactMetawidget.getWidgetProcessor( BeansBindingProcessor.class ).save( mContactMetawidget );
			Contact contact = mContactMetawidget.getToInspect();
			contact.setCommunications( CollectionUtils.newHashSet( mCommunicationsModel.exportList() ) );
			mProvider.getContactsController().save( contact );
		} catch ( Exception e ) {
			JOptionPane.showMessageDialog( ContactDialog.this, e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE );
			return;
		}

		ContactDialog.this.setVisible( false );
		mProvider.fireRefresh();
	}

	@UiAction
	@UiComesAfter( "save" )
	@UiAttribute( name = HIDDEN, value = "${this.readOnly || this.newContact}" )
	public void delete() {

		Contact contact = mContactMetawidget.getToInspect();

		if ( !mShowConfirmDialog && JOptionPane.showConfirmDialog( ContactDialog.this, "Sure you want to delete this contact?" ) != JOptionPane.OK_OPTION ) {
			return;
		}

		ContactDialog.this.setVisible( false );
		mProvider.getContactsController().delete( contact );
		mProvider.fireRefresh();
	}

	@UiAction
	@UiComesAfter( { "edit", "delete" } )
	@UiLabel( "${if ( this.readOnly ) 'Back'}" )
	public void cancel() {

		setVisible( false );
	}

	//
	// Inner class
	//

	private static class CommunicationEditor
		extends AbstractCellEditor
		implements TableCellEditor {

		//
		// Private members
		//

		private SwingMetawidget		mEditor;

		private String				mColumnName;

		//
		// Constructor
		//

		public CommunicationEditor() {

			mEditor = new SwingMetawidget();
			mEditor.setMetawidgetLayout( new org.metawidget.swing.layout.BoxLayout() );
			mEditor.setConfig( "org/metawidget/example/swing/addressbook/metawidget.xml" );
		}

		//
		// Public methods
		//

		public Object getCellEditorValue() {

			return mEditor.getValue( mColumnName );
		}

		@SuppressWarnings( "unchecked" )
		public Component getTableCellEditorComponent( JTable table, Object value, boolean selected, int row, int column ) {

			Communication communication = ( (ListTableModel<Communication>) table.getModel() ).getValueAt( row );

			if ( communication == null ) {
				communication = new Communication();
			}

			mEditor.setToInspect( communication );
			mColumnName = StringUtils.decapitalize( table.getModel().getColumnName( column ) );
			mEditor.setPath( Communication.class.getName() + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + mColumnName );
			mEditor.setValue( value, mColumnName );

			// Metawidget just creates regular components, so we can modify them if we need. Here,
			// we remove the border so that the component looks nicer when used as a CellEditor

			( (JComponent) mEditor.getComponent( 0 ) ).setBorder( null );

			return mEditor;
		}
	}
}
