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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.Serializable;
import java.util.ResourceBundle;

import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.vaadin.Facet;
import org.metawidget.vaadin.VaadinMetawidget;
import org.metawidget.vaadin.layout.GridLayout;
import org.metawidget.vaadin.layout.GridLayoutConfig;
import org.metawidget.vaadin.widgetprocessor.binding.simple.CollectionBindingProcessor;
import org.metawidget.vaadin.widgetprocessor.binding.simple.CollectionBindingProcessor.CollectionBindingNotifier;
import org.metawidget.vaadin.widgetprocessor.binding.simple.SimpleBindingProcessor;

import com.vaadin.data.Buffered.SourceException;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * Dialog box for Address Book Contacts.
 *
 *
 *
 *
 * @author Loghman Barari
 */

public class ContactDialog implements Serializable {

	//
	// Private statics
	//

	private static final long serialVersionUID = 1l;

	private static int id = 1;

	//
	// Package-level members
	//

	IndexedContainer mCommunicationsModel;

	//
	// Private members
	//

	private ContactsControllerProvider mProvider;

	/* package private */VaadinMetawidget mContactMetawidget;

	private VaadinMetawidget mButtonsMetawidget;

	private Window mContent;

	/* package private */boolean mShowConfirmDialog = true;

	//
	// Constructor
	//

	public ContactDialog(ContactsControllerProvider provider,
			final Contact contact) {

		if (id > Integer.MAX_VALUE - 3) {
			id = 1;
		}

		mProvider = provider;

		mContent = new Window();
		mContent.setDebugId("ContactDialogWindow" + id++);
		mContent.setHeight("600px");
		mContent.setWidth("800px");

		// Bundle

		ResourceBundle bundle = ResourceBundle
				.getBundle("org.metawidget.example.shared.addressbook.resource.Resources");

		// Title

		StringBuilder builder = new StringBuilder(contact.getFullname());

		if (builder.length() > 0) {
			builder.append(" - ");
		}

		// Personal/business icon

		if (contact instanceof PersonalContact) {
			builder.append(bundle.getString("personalContact"));
		} else {
			builder.append(bundle.getString("businessContact"));
		}

		mContent.setCaption(builder.toString());

		// Metawidget

		CustomLayout body = new CustomLayout(
				"addressbook");

		String resource = "../addressbook/img/business.gif";
		if (contact instanceof PersonalContact) {
			resource = "../addressbook/img/personal.gif";
		}

		body
				.addComponent(new Embedded("", new ThemeResource(resource)),
						"icon");

		mContactMetawidget = new VaadinMetawidget("ContactMetawidget");
		mContactMetawidget.setBundle(MainApplication.getBundle());
		mContactMetawidget.setWidth("100%");
		mContactMetawidget
				.setConfig("org/metawidget/example/vaadin/addressbook/metawidget.xml");
		mContactMetawidget.getWidgetProcessor(CollectionBindingProcessor.class)
				.setCollectionBindingNotifier(new CollectionBindingNotifier() {

					public void removeItemFromCollection(Object item)
							throws RuntimeException {
						contact.removeCommunication((Communication) item);
					}

					public Object formatValue(Comparable<?> item, String field,
							Object value) {
						return value;
					}

					public Class<?> columnType(String field, Class<?> clazz) {
						return clazz;
					}

					public void addItemToCollection(Object item)
							throws RuntimeException {

							contact.addCommunication((Communication) item);
					}
				});

		mContactMetawidget.setReadOnly(contact.getId() != 0);
		mContactMetawidget.setToInspect(contact);

		body.addComponent(mContactMetawidget, "pagebody");

		mContent.addComponent(body);

		addEmbededButtons();
	}

	//
	// Public methods
	//

	@UiHidden
	public Window getContent() {
		return mContent;
	}

	@UiHidden
	public boolean isReadOnly() {

		return mContactMetawidget.isReadOnly();
	}

	/**
	 * For unit tests
	 */

	@UiHidden
	public void setShowConfirmDialog(boolean showConfirmDialog) {

		mShowConfirmDialog = showConfirmDialog;
	}

	@UiAction
	@UiAttribute(name = HIDDEN, value = "${!this.readOnly}")
	public void edit() {

		mContactMetawidget.setReadOnly(false);

		addEmbededButtons();
	}

	@UiAction
	@UiAttribute(name = HIDDEN, value = "${this.readOnly}")
	public void save() {

		try {
			mContactMetawidget.getWidgetProcessor(SimpleBindingProcessor.class)
					.commit(mContactMetawidget);

			Contact contact = mContactMetawidget.getToInspect();

			mProvider.getContactsController().save(contact);
		}
		catch (SourceException e) {
			mContent.showNotification("Save Error", e.getCause().getLocalizedMessage(),
					Notification.TYPE_ERROR_MESSAGE);

			return;

		} catch (Exception e) {
			e.printStackTrace();

			mContent.showNotification("Save Error", e.getLocalizedMessage(),
					Notification.TYPE_ERROR_MESSAGE);

			return;
		}

		if (!MainApplication.isTestMode()) {
			mContent.getParent().removeWindow(mContent);
		}

		mProvider.fireRefresh();
	}

	@UiAction
	@UiComesAfter("save")
	@UiAttribute(name = HIDDEN, value = "${this.readOnly || this.newContact}")
	public void delete() {

		Contact contact = mContactMetawidget.getToInspect();

		if (!MainApplication.isTestMode()) {
			mContent.getParent().removeWindow(mContent);
		}

		mProvider.getContactsController().delete(contact);
		mProvider.fireRefresh();
	}

	@UiAction
	@UiComesAfter( { "edit", "delete" })
	@UiAttribute(name = LABEL, value = "${if ( this.readOnly ) 'Back'}")
	public void cancel() {

		if (!MainApplication.isTestMode()) {
			mContent.getParent().removeWindow(mContent);
		}
	}

	//
	// Private Methods
	//
	private void addEmbededButtons() {
		// Embedded buttons

		Facet facetButtons = new Facet();
		facetButtons.setDebugId("ContactDialogButtons" + ContactDialog.id);
		facetButtons.setWidth("100%");
		mContactMetawidget.addComponent(facetButtons);

		mButtonsMetawidget = new VaadinMetawidget();
		mButtonsMetawidget.setDebugId("ButtonsMetawidget");
		mButtonsMetawidget.setBundle(MainApplication.getBundle());
		mButtonsMetawidget
				.setConfig("org/metawidget/example/vaadin/addressbook/metawidget.xml");
		mButtonsMetawidget.setLayout(new GridLayout(GridLayoutConfig
				.newHorizentalLayoutConfig()));
		mButtonsMetawidget.setToInspect(this);
		facetButtons.addComponent(mButtonsMetawidget);
		facetButtons.setComponentAlignment(mButtonsMetawidget,
				Alignment.MIDDLE_CENTER);
	}

}
