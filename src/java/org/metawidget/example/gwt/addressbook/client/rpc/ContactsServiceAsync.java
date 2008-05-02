package org.metawidget.example.gwt.addressbook.client.rpc;

import java.util.List;

import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

public interface ContactsServiceAsync
	extends RemoteService
{
	//
	//
	// Methods
	//
	//

	void getAllByExample( ContactSearch search, AsyncCallback<List<Contact>> callback );

	void load( long id, AsyncCallback<Contact> callback );

	void save( Contact contact, AsyncCallback<List<Contact>> callback );

	void delete( Contact contact, AsyncCallback<Boolean> callback );

	void delete( long id, AsyncCallback<Boolean> callback );
}
