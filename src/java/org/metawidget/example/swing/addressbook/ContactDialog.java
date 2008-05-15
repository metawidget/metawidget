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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import org.metawidget.swing.Facet;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.binding.beansbinding.BeansBinding;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.StringUtils;

/**
 * @author Richard Kennard
 */

public class ContactDialog
	extends JDialog
{
	//
	//
	// Private statics
	//
	//

	private final static long		serialVersionUID	= 1907130713267121112L;

	private final static int		COMPONENT_SPACING	= 5;

	//
	//
	// Constructor
	//
	//

	public ContactDialog( final ContactsControllerProvider provider, final Contact contact )
	{
		super( (Frame) provider, true );
		setSize( new Dimension( 800, 600 ) );
		getContentPane().setBackground( Color.white );

		// Background

		ImagePanel panelBackground = new ImagePanel();
		panelBackground.setImage( getClass().getResource( "/org/metawidget/example/shared/addressbook/media/background.jpg" ) );
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
			imageLabel.setIcon( new ImageIcon( getClass().getResource( "/org/metawidget/example/shared/addressbook/media/personal.gif" ) ) );
		}
		else
		{
			builder.append( bundle.getString( "businessContact" ) );
			imageLabel.setIcon( new ImageIcon( getClass().getResource( "/org/metawidget/example/shared/addressbook/media/business.gif" ) ) );
		}

		setTitle( builder.toString() );

		// BeansBinding

		BeansBinding.registerConverter( Date.class, String.class, new DateConverter() );
		BeansBinding.registerConverter( Gender.class, Object.class, new EnumConverter<Gender>( Gender.class ) );

		// Metawidget

		final SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setBorder( BorderFactory.createEmptyBorder( COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING ) );
		metawidget.setOpaque( false );
		metawidget.setBundle( ResourceBundle.getBundle( "org.metawidget.example.shared.addressbook.resource.Resources" ) );
		metawidget.setInspectorConfig( "org/metawidget/example/swing/addressbook/inspector-config.xml" );
		metawidget.setBindingClass( BeansBinding.class );
		metawidget.setToInspect( contact );
		metawidget.setReadOnly( contact.getId() != 0 );
		panelBackground.add( metawidget, BorderLayout.CENTER );

		// Communications override

		final ListTableModel<Communication> communicationsModel = new ListTableModel<Communication>( Communication.class, contact.getCommunications(), "Type", "Value" );
		final JTable tableCommunications = new JTable( communicationsModel );
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
				if ( JOptionPane.showConfirmDialog( ContactDialog.this, "Sure you want to delete this communication?" ) != JOptionPane.OK_OPTION )
					return;

				Communication communication = communicationsModel.getValueAt( tableCommunications.rowAtPoint( menuPopup.getLocation() ) );

				contact.removeCommunication( communication );
				communicationsModel.importCollection( contact.getCommunications() );
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

		metawidget.add( scrollPane );

		// Embedded buttons

		Facet facetButtons = new Facet();
		facetButtons.setName( "buttons" );
		facetButtons.setOpaque( false );
		metawidget.add( facetButtons );

		final JButton saveButton = new JButton( new AbstractAction( bundle.getString( "save" ) )
		{
			public void actionPerformed( ActionEvent event )
			{
				try
				{
					metawidget.save();
					contact.setCommunications( CollectionUtils.newHashSet( communicationsModel.exportList() ) );
					provider.getContactsController().save( contact );
				}
				catch ( Exception e )
				{
					JOptionPane.showMessageDialog( ContactDialog.this, e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE );
					return;
				}

				ContactDialog.this.setVisible( false );
			}
		} );

		final JButton deleteButton = new JButton( new AbstractAction( bundle.getString( "delete" ) )
		{
			public void actionPerformed( ActionEvent event )
			{
				// (if event is null, assume this was called manually, like through a unit test)

				if ( event != null && JOptionPane.showConfirmDialog( ContactDialog.this, "Sure you want to delete this contact?" ) != JOptionPane.OK_OPTION )
					return;

				ContactDialog.this.setVisible( false );

				provider.getContactsController().delete( contact );
			}
		} );

		saveButton.setVisible( !metawidget.isReadOnly() );
		facetButtons.add( saveButton );
		deleteButton.setVisible( !metawidget.isReadOnly() && contact.getId() != 0 );
		facetButtons.add( deleteButton );

		final JButton editButton = new JButton( new AbstractAction( bundle.getString( "edit" ) )
		{
			public void actionPerformed( ActionEvent event )
			{
				metawidget.setReadOnly( false );

				if ( event != null )
					( (JButton) event.getSource() ).setVisible( false );

				saveButton.setVisible( true );
				deleteButton.setVisible( true );
				communicationsModel.setAllRowsEditable( true );
				communicationsModel.setExtraBlankRow( true );
				ContactDialog.this.repaint();
			}
		} );

		editButton.setVisible( metawidget.isReadOnly() );
		facetButtons.add( editButton );

		facetButtons.add( new JButton( new AbstractAction( bundle.getString( "cancel" ) )
		{
			public void actionPerformed( ActionEvent event )
			{
				ContactDialog.this.setVisible( false );
			}
		} ) );
	}

	//
	//
	// Inner class
	//
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

		private final static long	serialVersionUID	= -3986617226200636100L;

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
			mEditor.setPath( Communication.class.getName() + StringUtils.SEPARATOR_SLASH + mColumnName );
			mEditor.setValue( value, mColumnName );

			// Metawidget just creates regular components, so we can modify them if we need. Here,
			// we remove the border so that the component looks nicer when used as a CellEditor

			( (JComponent) mEditor.getComponent( 0 ) ).setBorder( null );

			return mEditor;
		}
	}
}
