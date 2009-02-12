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
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
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
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.example.swing.addressbook.converter.DateConverter;
import org.metawidget.example.swing.addressbook.converter.EnumConverter;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.commons.jexl.UiJexlAttribute;
import org.metawidget.swing.Facet;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.layout.FlowLayout;
import org.metawidget.swing.propertybinding.beansbinding.BeansBinding;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Dialog box for Address Book Contacts.
 * <p>
 * Note: for brevity, this example is not optimized to use <code>SwingMetawidget.rebind</code>
 * (see 'rebinding' in the Reference Documentation). For an example using rebinding, see
 * <code>org.metawidget.example.gwt.addressbook.client.ui.ContactDialog</code>.
 *
 * @author Richard Kennard
 */

public class ContactDialog
	extends JDialog
{
	//
	// Private statics
	//

	private final static long			serialVersionUID	= 1l;

	private final static int			COMPONENT_SPACING	= 5;

	//
	// Package-level members
	//

	ListTableModel<Communication>		mCommunicationsModel;

	//
	// Private members
	//

	private ContactsControllerProvider	mProvider;

	private SwingMetawidget				mContactMetawidget;

	private SwingMetawidget				mButtonsMetawidget;

	private boolean						mShowConfirmDialog	= true;

	//
	// Constructor
	//

	@SuppressWarnings("serial")
	public ContactDialog( ContactsControllerProvider provider, final Contact contact )
	{
		mProvider = provider;

		setSize( new Dimension( 800, 600 ) );
		getContentPane().setBackground( Color.white );

		// Background

		ImagePanel panelBackground = new ImagePanel();
		panelBackground.setImage( Thread.currentThread().getContextClassLoader().getResource( "org/metawidget/example/shared/addressbook/media/background.jpg" ) );
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

		if ( builder.length() > 0 )
			builder.append( " - " );

		// Personal/business icon

		if ( contact instanceof PersonalContact )
		{
			builder.append( bundle.getString( "personalContact" ) );
			imageLabel.setIcon( new ImageIcon( Thread.currentThread().getContextClassLoader().getResource( "org/metawidget/example/shared/addressbook/media/personal.gif" ) ) );
		}
		else
		{
			builder.append( bundle.getString( "businessContact" ) );
			imageLabel.setIcon( new ImageIcon( Thread.currentThread().getContextClassLoader().getResource( "org/metawidget/example/shared/addressbook/media/business.gif" ) ) );
		}

		setTitle( builder.toString() );

		// BeansBinding

		BeansBinding.registerConverter( Date.class, String.class, new DateConverter() );
		BeansBinding.registerConverter( Gender.class, Object.class, new EnumConverter<Gender>( Gender.class ) );

		// Metawidget

		mContactMetawidget = new SwingMetawidget();
		mContactMetawidget.setBorder( BorderFactory.createEmptyBorder( COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING ) );
		mContactMetawidget.setOpaque( false );
		mContactMetawidget.setBundle( ResourceBundle.getBundle( "org.metawidget.example.shared.addressbook.resource.Resources" ) );
		mContactMetawidget.setInspectorConfig( "org/metawidget/example/swing/addressbook/inspector-config.xml" );
		mContactMetawidget.setPropertyBindingClass( BeansBinding.class );
		mContactMetawidget.setToInspect( contact );
		mContactMetawidget.setReadOnly( contact.getId() != 0 );
		panelBackground.add( mContactMetawidget, BorderLayout.CENTER );

		// Communications override

		mCommunicationsModel = new ListTableModel<Communication>( Communication.class, contact.getCommunications(), "Type", "Value" );
		final JTable tableCommunications = new JTable( mCommunicationsModel );
		tableCommunications.putClientProperty( "terminateEditOnFocusLost", Boolean.TRUE );
		tableCommunications.setDefaultEditor( Object.class, new CommunicationEditor() );

		JScrollPane scrollPane = new JScrollPane( tableCommunications );
		scrollPane.setName( "communications" );
		scrollPane.setPreferredSize( new Dimension( 150, 150 ) );

		final JPopupMenu menuPopup = new JPopupMenu();
		menuPopup.add( new AbstractAction( bundle.getString( "delete" ) )
		{
			public void actionPerformed( ActionEvent event )
			{
				if ( JOptionPane.showConfirmDialog( ContactDialog.this, "Sure you want to delete this communication?" ) == JOptionPane.OK_OPTION )
				{
					int rowAtPoint = tableCommunications.rowAtPoint( menuPopup.getLocation() );

					if ( rowAtPoint >= 0 && rowAtPoint < mCommunicationsModel.getRowCount() )
					{
						Communication communication = mCommunicationsModel.getValueAt( rowAtPoint );

						contact.removeCommunication( communication );
						mCommunicationsModel.importCollection( contact.getCommunications() );
					}
				}

				menuPopup.setVisible( false );
			}
		} );
		tableCommunications.add( menuPopup );

		tableCommunications.addMouseListener( new MouseAdapter()
		{
			@Override
			public void mouseReleased( MouseEvent event )
			{
				if ( event.isPopupTrigger() )
				{
					menuPopup.setLocation( event.getLocationOnScreen() );
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
		mButtonsMetawidget.setInspectorConfig( "org/metawidget/example/swing/addressbook/inspector-config.xml" );
		mButtonsMetawidget.setBundle( ResourceBundle.getBundle( "org.metawidget.example.shared.addressbook.resource.Resources" ) );
		mButtonsMetawidget.setLayoutClass( FlowLayout.class );
		mButtonsMetawidget.setToInspect( this );
		facetButtons.add( mButtonsMetawidget );
	}

	//
	// Public methods
	//

	@UiHidden
	public boolean isReadOnly()
	{
		return mContactMetawidget.isReadOnly();
	}

	@UiHidden
	public boolean isNewContact()
	{
		return (((Contact) mContactMetawidget.getToInspect()).getId() == 0 );
	}

	/**
	 * For unit tests
	 */

	@UiHidden
	public void setShowConfirmDialog( boolean showConfirmDialog )
	{
		mShowConfirmDialog = showConfirmDialog;
	}

	@UiAction
	@UiJexlAttribute( name = HIDDEN, expression = "!this.readOnly" )
	public void edit()
	{
		mContactMetawidget.setReadOnly( false );

		mCommunicationsModel.setAllRowsEditable( true );
		mCommunicationsModel.setExtraBlankRow( true );
		mButtonsMetawidget.setToInspect( mButtonsMetawidget.getToInspect() );
	}

	@UiAction
	@UiJexlAttribute( name = HIDDEN, expression = "this.readOnly" )
	public void save()
	{
		try
		{
			mContactMetawidget.save();
			Contact contact = (Contact) mContactMetawidget.getToInspect();
			contact.setCommunications( CollectionUtils.newHashSet( mCommunicationsModel.exportList() ) );
			mProvider.getContactsController().save( contact );
		}
		catch ( Exception e )
		{
			JOptionPane.showMessageDialog( ContactDialog.this, e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE );
			return;
		}

		ContactDialog.this.setVisible( false );
		mProvider.fireRefresh();
	}

	@UiAction
	@UiComesAfter( "save" )
	@UiJexlAttribute( name = HIDDEN, expression = "this.readOnly || this.newContact" )
	public void delete()
	{
		Contact contact = (Contact) mContactMetawidget.getToInspect();

		if ( !mShowConfirmDialog && JOptionPane.showConfirmDialog( ContactDialog.this, "Sure you want to delete this contact?" ) != JOptionPane.OK_OPTION )
			return;

		ContactDialog.this.setVisible( false );
		mProvider.getContactsController().delete( contact );
		mProvider.fireRefresh();
	}

	@UiAction
	@UiComesAfter( { "edit", "delete" } )
	@UiJexlAttribute( name = LABEL, expression = "if ( this.readOnly ) 'Back'" )
	public void cancel()
	{
		setVisible( false );
	}

	//
	// Inner class
	//

	private static class CommunicationEditor
		extends AbstractCellEditor
		implements TableCellEditor
	{
		//
		//
		// Private statics
		//
		//

		private final static long	serialVersionUID	= 1l;

		//
		//
		// Private members
		//
		//

		private SwingMetawidget		mEditor;

		private String				mColumnName;

		//
		//
		// Constructor
		//
		//

		public CommunicationEditor()
		{
			mEditor = new SwingMetawidget();
			mEditor.setLayoutClass( null );
			mEditor.setInspectorConfig( "org/metawidget/example/swing/addressbook/inspector-config.xml" );
		}

		//
		//
		// Public methods
		//
		//

		public Object getCellEditorValue()
		{
			return mEditor.getValue( mColumnName );
		}

		@SuppressWarnings( "unchecked" )
		public Component getTableCellEditorComponent( JTable table, Object value, boolean selected, int row, int column )
		{
			Communication communication = ( (ListTableModel<Communication>) table.getModel() ).getValueAt( row );

			if ( communication == null )
				communication = new Communication();

			mEditor.setToInspect( communication );
			mColumnName = StringUtils.lowercaseFirstLetter( table.getModel().getColumnName( column ) );
			mEditor.setPath( Communication.class.getName() + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + mColumnName );
			mEditor.setValue( value, mColumnName );

			// Metawidget just creates regular components, so we can modify them if we need. Here,
			// we remove the border so that the component looks nicer when used as a CellEditor

			( (JComponent) mEditor.getComponent( 0 ) ).setBorder( null );

			return mEditor;
		}
	}
}
