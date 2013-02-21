// Metawidget (licensed under LGPL)
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

package com.test.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.metawidget.inspector.annotation.UiAction;

import com.test.model.Person;

/**
 * Backing bean for Address entities.
 * <p>
 * This class provides CRUD functionality for all Address entities. It focuses purely on Java EE 6
 * standards (e.g. <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for searches) rather than
 * introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PersonBean
	implements Serializable {

	private static final long	serialVersionUID	= 1L;

	/*
	 * Support creating and retrieving Address entities
	 */

	private Long				mId;

	public Long getId() {

		return mId;
	}

	public void setId( Long id ) {

		mId = id;
	}

	private Person	mPerson;

	public Person getPerson() {

		return mPerson;
	}

	@Inject
	private Conversation				mConversation;

	@PersistenceContext( type = PersistenceContextType.EXTENDED )
	/* package private */EntityManager	mEntityManager;

	@UiAction
	public String create() {

		mConversation.begin();
		return "create?faces-redirect=true";
	}

	public String view() {

		ListDataModel<Person> pageItems = getPageItems();
		@SuppressWarnings( "unchecked" )
		Person selectedPerson = ( (List<Person>) pageItems.getWrappedData() ).get( pageItems.getRowIndex() );
		return "view?id=" + selectedPerson.getId() + "&faces-redirect=true";
	}

	public void retrieve() {

		if ( FacesContext.getCurrentInstance().isPostback() ) {
			return;
		}

		if ( mConversation.isTransient() ) {
			mConversation.begin();
		}

		if ( mId == null ) {
			mPerson = mSearch;
		} else {
			mPerson = findById( getId() );
		}
	}

	public Person findById( Long id ) {

		return mEntityManager.find( Person.class, id );
	}

	/*
	 * Support updating and deleting Address entities
	 */

	public String update() {

		mConversation.end();

		try {
			if ( mId == null ) {
				mEntityManager.persist( mPerson );
				return "search?faces-redirect=true";
			}
			mEntityManager.merge( mPerson );
			return "view?faces-redirect=true&id=" + mPerson.getId();
		} catch ( Exception e ) {
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( e.getMessage() ) );
			return null;
		}
	}

	public String delete() {

		mConversation.end();

		try {
			mEntityManager.remove( findById( getId() ) );
			mEntityManager.flush();
			return "search?faces-redirect=true";
		} catch ( Exception e ) {
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( e.getMessage() ) );
			return null;
		}
	}

	/*
	 * Support searching Address entities with pagination
	 */

	private int		mPage;

	private long	mCount;

	private Person	mSearch	= new Person();

	public int getPage() {

		return mPage;
	}

	public void setPage( int page ) {

		mPage = page;
	}

	public int getPageSize() {

		return 10;
	}

	public Person getSearch() {

		return mSearch;
	}

	public void setSearch( Person search ) {

		mSearch = search;
	}

	@UiAction
	public void search() {

		mPage = 0;
	}

	public void paginate() {

		CriteriaBuilder builder = mEntityManager.getCriteriaBuilder();

		// Populate mCount

		CriteriaQuery<Long> countCriteria = builder.createQuery( Long.class );
		Root<Person> root = countCriteria.from( Person.class );
		countCriteria = countCriteria.select( builder.count( root ) ).where( getSearchPredicates( root ) );
		mCount = mEntityManager.createQuery( countCriteria ).getSingleResult();

		// Populate pageItems

		CriteriaQuery<Person> criteria = builder.createQuery( Person.class );
		root = criteria.from( Person.class );
		TypedQuery<Person> query = mEntityManager.createQuery( criteria.select( root ).where( getSearchPredicates( root ) ) );
		query.setFirstResult( mPage * getPageSize() ).setMaxResults( getPageSize() );

		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
		sessionMap.put( Person.class.getName(), new ListDataModel<Person>( query.getResultList() ) );
	}

	private Predicate[] getSearchPredicates( Root<Person> root ) {

		CriteriaBuilder builder = mEntityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String title = mSearch.getTitle();
		if ( title != null && !"".equals( title ) ) {
			predicatesList.add( builder.like( root.<String> get( "title" ), '%' + title + '%' ) );
		}
		String firstname = mSearch.getFirstname();
		if ( firstname != null && !"".equals( firstname ) ) {
			predicatesList.add( builder.like( root.<String> get( "firstname" ), '%' + firstname + '%' ) );
		}
		String surname = mSearch.getSurname();
		if ( surname != null && !"".equals( surname ) ) {
			predicatesList.add( builder.like( root.<String> get( "surname" ), '%' + surname + '%' ) );
		}
		String email = mSearch.getEmail();
		if ( email != null && !"".equals( email ) ) {
			predicatesList.add( builder.like( root.<String> get( "email" ), '%' + email + '%' ) );
		}

		return predicatesList.toArray( new Predicate[predicatesList.size()] );
	}

	public ListDataModel<Person> getPageItems() {

		// getPageItems must be stored 'above' request level. See
		// http://stackoverflow.com/questions/2118656/hcommandlink-hcommandbutton-is-not-being-invoked

		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
		@SuppressWarnings( "unchecked" )
		ListDataModel<Person> pageItems = (ListDataModel<Person>) sessionMap.get( Person.class.getName() );
		return pageItems;
	}

	public long getCount() {

		return mCount;
	}

	/*
	 * Support listing and POSTing back Pet entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Person> getAll() {

		CriteriaQuery<Person> criteria = mEntityManager.getCriteriaBuilder().createQuery( Person.class );
		return mEntityManager.createQuery( criteria.select( criteria.from( Person.class ) ) ).getResultList();
	}

	@Resource
	private SessionContext	mSessionContext;

	public Converter getConverter() {

		final PersonBean ejbProxy = mSessionContext.getBusinessObject( PersonBean.class );

		return new Converter() {

			@Override
			public Object getAsObject( FacesContext context, UIComponent component, String value ) {

				return ejbProxy.findById( Long.valueOf( value ) );
			}

			@Override
			public String getAsString( FacesContext context, UIComponent component, Object value ) {

				if ( value == null ) {
					return "";
				}

				if ( component instanceof UIInput ) {
					return String.valueOf( ( (Person) value ).getId() );
				}

				return value.toString();
			}
		};
	}
}