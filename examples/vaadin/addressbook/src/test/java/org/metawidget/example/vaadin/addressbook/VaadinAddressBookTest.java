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

package org.metawidget.example.vaadin.addressbook;

import java.text.DateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.metawidget.example.shared.addressbook.controller.ContactsController;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.shared.addressbook.model.Gender;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.util.CollectionUtils;
import org.metawidget.vaadin.VaadinMetawidget;
import org.metawidget.vaadin.widgetprocessor.binding.simple.SimpleBindingProcessor;

import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickShortcut;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Loghman Barari
 */

public class VaadinAddressBookTest
	extends TestCase {

	//
	// Public methods
	//

	@SuppressWarnings( "cast" )
	public void testAddressBook()
		throws Exception {

		// Set Locale because we will be checking date formatting

		Locale.setDefault( Locale.UK );
		TimeZone.setDefault( TimeZone.getTimeZone( "GMT" ) );
		DateFormat dateFormat = DateFormat.getDateInstance( DateFormat.SHORT,
				Locale.UK );

		// Start app

		MainApplication.setTestMode( true );
		MainApplication application = new MainApplication();

		AddressBook addressBook = application.getAddressBook();
		CustomLayout customLayout = (CustomLayout) addressBook.getContent();
		assertEquals( customLayout.getTemplateName() , "addressbook" );
		VerticalLayout contactsMainBody = (VerticalLayout) customLayout
				.getComponent( "pagebody" );
		assertEquals( contactsMainBody.getComponentCount(), 2 );
		Table contactsTable = (Table) contactsMainBody.getComponent( 1 );
		assertEquals( contactsTable.getContainerDataSource().getItemIds().size(), 6 );

		// Check searching

		VaadinMetawidget metawidgetSearch = (VaadinMetawidget) contactsMainBody
				.getComponent( 0 );

		metawidgetSearch.setValue( "Simpson", "surname" );
		metawidgetSearch.setValue( ContactType.PERSONAL, "type" );

		assertEquals( metawidgetSearch.getComponentCount(), 6 );
		assertEquals( metawidgetSearch.getColumns(), 1 );
		assertEquals( metawidgetSearch.getRows(), 4 );
		HorizontalLayout buttonsHorizontalLayout = (HorizontalLayout) metawidgetSearch
				.getComponent( 0, 3 );
		assertEquals( buttonsHorizontalLayout.getWidth(), 100f );
		Button buttonSearch = (Button) ( (VaadinMetawidget) buttonsHorizontalLayout
				.getComponent( 0 ) ).getComponent( 0, 0 );
		assertEquals( "Search", buttonSearch.getCaption() );
		assertEquals( "Add Personal Contact",
				( (Button) ( (VaadinMetawidget) buttonsHorizontalLayout
						.getComponent( 0 ) ).getComponent( 1, 0 ) ).getCaption() );
		assertEquals( "Add Business Contact",
				( (Button) ( (VaadinMetawidget) buttonsHorizontalLayout
						.getComponent( 0 ) ).getComponent( 2, 0 ) ).getCaption() );

		assertEquals( ContactType.PERSONAL, ( (ComboBox) metawidgetSearch
				.getComponent( "type" ) ).getValue() );

		ContactsController contactsController = addressBook
				.getContactsController();
		assertTrue( contactsController.getAllByExample(
				(ContactSearch) metawidgetSearch.getToInspect() ).size() == 2 );

		// Open dialog for Personal Contact

		Contact contact = contactsController.load( 1 );
		assertEquals( "Mr Homer Simpson", contact.getFullname() );
		assertEquals( "Mr Homer Simpson", contact.toString() );
		assertEquals( 32, contact.hashCode() );
		assertFalse( contact.equals( new Object() ) );
		assertEquals( contact, contact );
		assertEquals( contact.compareTo( null ), -1 );
		assertEquals( contact.compareTo( contact ), 0 );
		assertEquals( "742 Evergreen Terrace", contact.getAddress().getStreet() );
		assertEquals( contact.getCommunications().size(), 1 );

		ContactDialog dialog = new ContactDialog( addressBook, contact );
		dialog.setShowConfirmDialog( false );

		// Check loading

		VaadinMetawidget metawidgetContact = (VaadinMetawidget) ( (CustomLayout) getComponentByIndex(
				dialog.getContent(), 0 ) ).getComponent( "pagebody" );
		assertEquals( "Homer", metawidgetContact.getValue( "firstname" ) );
		assertTrue( ( (Component) metawidgetContact.getComponent( "firstname" ) ) instanceof Label );
		assertEquals( Gender.MALE.toString(), metawidgetContact.getValue( "gender" ) );
		assertEquals(
				"org.metawidget.vaadin.layout.SeparatorLayoutDecorator$Separator",
				getComponentByIndex( metawidgetContact, 5 ).getClass().getName() );
		assertEquals( "<br/><b>Contact Details</b><hr/>",
				( (Label) getComponentByIndex( metawidgetContact, 5 ) ).getValue() );
		assertEquals( Label.CONTENT_XHTML, ( (Label) getComponentByIndex(
				metawidgetContact, 5 ) ).getContentMode() );
		assertEquals( dateFormat.parse( "12/05/56" ), metawidgetContact
				.getValue( "dateOfBirth" ) );

		Table communications = metawidgetContact.getComponent( "communications" );
		IndexedContainer model = (IndexedContainer) communications
				.getContainerDataSource();

		assertEquals( model.getItemIds().size(), 1 );
		assertEquals( metawidgetContact.getComponentCount(), 15 );

		// Read-only editing does nothing

		assertFalse( communications.isEditable() );

		// Check editing

		HorizontalLayout buttonsLayout = (HorizontalLayout) getComponentByIndex(
				metawidgetContact, 10 );
		Button editButton = (Button) ( (VaadinMetawidget) buttonsLayout
				.getComponent( 0 ) ).getComponent( 0, 0 );
		assertEquals( "Edit", editButton.getCaption() );
		clickButton( editButton );

		assertEquals( Gender.MALE, ( (ComboBox) metawidgetContact
				.getComponent( "gender" ) ).getValue() );
		metawidgetContact.setValue( "Sapien", "surname" );
		assertEquals(
				"org.metawidget.vaadin.layout.SeparatorLayoutDecorator$Separator",
				getComponentByIndex( metawidgetContact, 5 ).getClass().getName() );
		assertEquals( "<br/><b>Contact Details</b><hr/>",
				( (Label) getComponentByIndex( metawidgetContact, 5 ) ).getValue() );
		assertEquals( Label.CONTENT_XHTML, ( (Label) getComponentByIndex(
				metawidgetContact, 5 ) ).getContentMode() );
		assertEquals( metawidgetContact.getComponentCount(), 18 );

		// Check editing a communication

		communications = metawidgetContact.getComponent( "communications" );
		model = (IndexedContainer) communications.getContainerDataSource();

		assertEquals( model.getItemIds().size(), 1 );

		assertTrue( communications.isEditable() );

		// Check adding a communication
		Button addButton = metawidgetContact.getComponent( "communications",
				"add" );
		clickButton( addButton );

		// Check 'adding' a blank communication
		try {
			metawidgetContact.getWidgetProcessor( SimpleBindingProcessor.class )
					.commit( metawidgetContact );
			fail();
		} catch ( Exception e ) {

			assertEquals( "Communication type is required", e.getCause()
					.getMessage() );
		}

		model.getItem( model.lastItemId() ).getItemProperty( "type" ).setValue(
				"Mobile" );
		model.getItem( model.lastItemId() ).getItemProperty( "value" ).setValue(
				"(0402) 123 456" );
		assertEquals( 2, model.getItemIds().size() );

		// Check deleting a communication

		Button deleteButton = metawidgetContact.getComponent( "communications",
				"delete" );
		assertFalse( deleteButton.isEnabled() );
		communications.setValue( model.getIdByIndex( 0 ) );
		assertTrue( deleteButton.isEnabled() );
		clickButton( deleteButton );
		assertEquals( 1, model.getItemIds().size() );

		// Check date validator.
		try {
			metawidgetContact.setValue( "foo", "dateOfBirth" );
			fail();
		} catch ( Exception e ) {
			assertEquals( "Date format not recognized", e.getCause().getCause()
					.getMessage() );
		}

		// Check saving

		metawidgetContact.setValue( dateFormat.parse( "12/05/57" ), "dateOfBirth" );
		buttonsLayout = (HorizontalLayout) getComponentByIndex(
				metawidgetContact, 10 );
		Button buttonSave = (Button) ( (VaadinMetawidget) buttonsLayout
				.getComponent( 0 ) ).getComponent( 0, 0 );
		assertEquals( "Save", buttonSave.getCaption() );
		clickButton( buttonSave );

		assertEquals( "Sapien", contact.getSurname() );
		assertEquals( dateFormat.parse( "12/05/57" ), ( (PersonalContact) contact )
				.getDateOfBirth() );
		assertEquals( ( (PersonalContact) contact ).getDateOfBirth().getTime(), -398908800000l );

		Iterator<Communication> iterator = contact.getCommunications()
				.iterator();
		Communication communication = iterator.next();
		assertEquals( "Mobile", communication.getType() );
		assertEquals( "(0402) 123 456", communication.getValue() );
		assertFalse( iterator.hasNext() );

		// Check re-viewing

		dialog = new ContactDialog( addressBook, contact );
		metawidgetContact = dialog.mContactMetawidget;
		assertEquals( "Sapien", metawidgetContact.getValue( "surname" ) );
		assertEquals( dateFormat.parse( "12/05/57" ), metawidgetContact
				.getValue( "dateOfBirth" ) );

		communications = metawidgetContact.getComponent( "communications" );
		model = (IndexedContainer) communications.getContainerDataSource();
		assertEquals( model.getItemIds().size(), 1 );
		assertEquals( metawidgetContact.getComponentCount(), 15 );

		// Search everything

		metawidgetSearch.setValue( null, "surname" );
		metawidgetSearch.setValue( null, "type" );
		clickButton( buttonSearch );
		assertEquals( 170f, communications.getHeight() );
		assertEquals( 100f, communications.getWidth() );

		// Open dialog for Business Contact

		contact = contactsController.load( 5 );
		assertEquals( "Mr Charles Montgomery Burns", contact.getFullname() );
		dialog = new ContactDialog( addressBook, contact );
		metawidgetContact = (VaadinMetawidget) ( (CustomLayout) getComponentByIndex(
				dialog.getContent(), 0 ) ).getComponent( "pagebody" );

		buttonsLayout = (HorizontalLayout) getComponentByIndex(
				metawidgetContact, 11 );
		Button buttonBack = (Button) ( (VaadinMetawidget) buttonsLayout
				.getComponent( 0 ) ).getComponent( 1, 0 );
		assertEquals( "Back", buttonBack.getCaption() );
		editButton = (Button) ( (VaadinMetawidget) buttonsLayout.getComponent( 0 ) )
				.getComponent( 0, 0 );
		assertEquals( "Edit", editButton.getCaption() );
		clickButton( editButton );
		assertEquals( "Charles Montgomery", metawidgetContact
				.getValue( "firstname" ) );
		assertTrue( Gender.MALE.equals( metawidgetContact.getValue( "gender" ) ) );
		assertEquals( 0, metawidgetContact.getValue( "numberOfStaff" ) );

		// Check saving

		metawidgetContact.setValue( 2, "numberOfStaff" );
		metawidgetContact.setValue( "A Company", "company" );

		assertEquals( metawidgetContact.getComponentCount(), 19 );

		buttonsLayout = (HorizontalLayout) getComponentByIndex(
				metawidgetContact, 11 );
		buttonSave = (Button) ( (VaadinMetawidget) buttonsLayout.getComponent( 0 ) )
				.getComponent( 0, 0 );
		clickButton( buttonSave );

		assertEquals( 2, ( (BusinessContact) contact ).getNumberOfStaff() );
		assertEquals( "A Company", ( (BusinessContact) contact ).getCompany() );

		// Check deleting

		Button buttonDelete = (Button) ( (VaadinMetawidget) buttonsLayout
				.getComponent( 0 ) ).getComponent( 1, 0 );
		assertEquals( "Delete", buttonDelete.getCaption() );
		Button buttonCancel = (Button) ( (VaadinMetawidget) buttonsLayout
				.getComponent( 0 ) ).getComponent( 2, 0 );
		assertEquals( "Cancel", buttonCancel.getCaption() );

		assertEquals( contactsController.getAllByExample( null ).size(), 6 );
		clickButton( buttonDelete );
		assertEquals( contactsController.getAllByExample( null ).size(), 5 );
		assertEquals( contactsTable.getContainerDataSource().getItemIds().size(), 5 );

		// Open dialog for new Personal Contact

		dialog = new ContactDialog( addressBook, new PersonalContact() );
		metawidgetContact = (VaadinMetawidget) (VaadinMetawidget) ( (CustomLayout) getComponentByIndex(
				dialog.getContent(), 0 ) ).getComponent( "pagebody" );

		// Check saving doesn't error on null date

		try {
			metawidgetContact.getWidgetProcessor( SimpleBindingProcessor.class )
					.commit( metawidgetContact );
			fail();
		} catch ( EmptyValueException e ) {
			assertEquals( "Firstname is required", e.getMessage() );
		}
		buttonsLayout = (HorizontalLayout) getComponentByIndex(
				metawidgetContact, 10 );
		buttonCancel = (Button) ( (VaadinMetawidget) buttonsLayout
				.getComponent( 0 ) ).getComponent( 2, 0 );
		assertEquals( "Cancel", buttonCancel.getCaption() );
		clickButton( buttonCancel );
		assertNull( dialog.getContent().getParent() );

		// Check adding

		String[] titles = new String[] { "Mr", "Mrs", "Miss", "Dr", "Cpt" };
		assertEquals( titles.length, ( (ComboBox) metawidgetContact
				.getComponent( "title" ) ).getItemIds().size() );
		for ( String title : titles ) {
			assertTrue( ( (ComboBox) metawidgetContact.getComponent( "title" ) )
					.getItemIds().contains( title ) );
		}
		metawidgetContact.setValue( "Miss", "title" );
		metawidgetContact.setValue( "Business", "firstname" );
		metawidgetContact.setValue( "Contact", "surname" );
		assertEquals( 2, ( (ComboBox) metawidgetContact.getComponent( "gender" ) )
				.getItemIds().size() );
		assertNull( ( (ComboBox) ( (Component) metawidgetContact.getComponent( "gender" ) ) )
				.getValue() );
		( (ComboBox) metawidgetContact.getComponent( "gender" ) )
				.setValue( Gender.FEMALE );
		assertTrue( ( (Component) metawidgetContact.getComponent( "address", "street" ) ) instanceof TextField );

		buttonSave = (Button) ( (VaadinMetawidget) buttonsLayout.getComponent( 0 ) )
				.getComponent( 0, 0 );
		assertEquals( "Save", buttonSave.getCaption() );
		clickButton( buttonSave );
		assertEquals( contactsTable.getContainerDataSource().getItemIds().size(), 6 );

		// Check viewing

		contact = contactsController.load( 7 );
		assertEquals( "Miss Business Contact", contact.getFullname() );
		assertEquals( Gender.FEMALE, contact.getGender() );
		metawidgetContact.setReadOnly( true );
		assertEquals( Gender.FEMALE.toString(), ( (Label) metawidgetContact.getComponent( "gender" ) ).getValue() );

		metawidgetContact.setReadOnly( false );
		assertEquals( Gender.FEMALE, ( (ComboBox) metawidgetContact
				.getComponent( "gender" ) ).getValue() );
	}

	public void testListTableModel()
		throws Exception {

		// Test nulls

		TableDataSource<Foo> model = new TableDataSource<Foo>( Foo.class, null ) {

			/**
			 *
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public Class<?> columnType( String field, Class<?> clazz ) {

				return clazz;
			}

			@Override
			public Object formatValue( Foo item, String field, Object value ) {

				return value;
			}

			@Override
			public void itemClick( ItemClickEvent event ) {

			}

		};

		assertEquals( 4, model.getContainerPropertyIds().size() );
		assertEquals( 0, model.getItemIds().size() );

		// Test normal list

		List<Foo> fooList = CollectionUtils.newArrayList();
		Foo foo = new Foo();
		fooList.add( foo );
		model = new TableDataSource<Foo>( Foo.class, fooList, "Foo", "Bar" ) {

			/**
			 *
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public Class<?> columnType( String field, Class<?> clazz ) {

				return clazz;
			}

			@Override
			public Object formatValue( Foo item, String field, Object value ) {

				return value;
			}

			@Override
			public void itemClick( ItemClickEvent event ) {

			}

		};
		assertEquals( 2, model.getContainerPropertyIds().size() );
		assertEquals( 1, model.getItemIds().size() );
		assertEquals( foo, model.getDataRow( model.getIdByIndex( 0 ) ) );

	}

	public void testCreateContactDialog() {

		AddressBook addressBook = new AddressBook();

		PersonalContact personalContact = new PersonalContact();
		assertTrue( !personalContact.equals( addressBook
				.createContactDialog( personalContact ).mContactMetawidget
				.getToInspect() ) );

		BusinessContact businessContact = new BusinessContact();
		assertTrue( !businessContact.equals( addressBook
				.createContactDialog( businessContact ).mContactMetawidget
				.getToInspect() ) );
	}

	//
	// Private methods
	//

	private void clickButton( Button button ) {

		ClickShortcut clickShortcut = new ClickShortcut( button, "" );

		clickShortcut.handleAction( null, null );
	}

	private static Component getComponentByIndex( ComponentContainer container,
			int index ) {

		Iterator<Component> iterator = container.getComponentIterator();

		for ( int i = 0; i < index; i++ ) {
			iterator.next();
		}

		return iterator.next();
	}

	//
	// Inner class
	//

	public static class Foo
		implements Comparable<Foo> {

		//
		// Private members
		//

		private String	mFoo	= "myFoo";

		private Boolean	mBar	= Boolean.TRUE;

		//
		// Public Field
		//
		public String	field;

		//
		// Public methods
		//

		public String getFoo() {

			return mFoo;
		}

		public void setFoo( String foo ) {

			mFoo = foo;
		}

		public Boolean getBar() {

			return mBar;
		}

		public void setBar( Boolean bar ) {

			mBar = bar;
		}

		public int compareTo( Foo that ) {

			return hashCode() - that.hashCode();
		}
	}
}
