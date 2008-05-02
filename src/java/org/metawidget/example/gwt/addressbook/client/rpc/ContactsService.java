package org.metawidget.example.gwt.addressbook.client.rpc;

import java.util.List;

import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ContactsService
	extends RemoteService
{
	//
	//
	// Methods
	//
	//

	List<Contact> getAllByExample( ContactSearch search );

	Contact load( long id );

	void save( Contact contact );

	boolean delete( Contact contact );

	boolean delete( long id );
}
