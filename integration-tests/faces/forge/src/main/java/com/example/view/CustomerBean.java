package com.example.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.jboss.forge.persistence.PaginationHelper;
import org.jboss.forge.persistence.PersistenceUtil;
import org.jboss.seam.transaction.Transactional;

import com.example.domain.Customer;

@Transactional
@Named
@Stateful
@RequestScoped
public class CustomerBean
	extends PersistenceUtil {

	private List<Customer>				list		= null;

	private Customer					customer	= new Customer();

	private long						id			= 0;

	private PaginationHelper<Customer>	pagination;

	public void load() {

		customer = findById( Customer.class, id );
	}

	public String create() {

		create( customer );
		return "view?faces-redirect=true&id=" + customer.getId();
	}

	public String delete() {

		delete( customer );
		return "list?faces-redirect=true";
	}

	public String save() {

		save( customer );
		return "view?faces-redirect=true&id=" + customer.getId();
	}

	public long getId() {

		return id;
	}

	public void setId( long theId ) {

		this.id = theId;
		if ( id > 0 ) {
			load();
		}
	}

	public Customer getCustomer() {

		return customer;
	}

	public void setCustomer( Customer theCustomer ) {

		this.customer = theCustomer;
	}

	public List<Customer> getList() {

		if ( list == null ) {
			list = getPagination().createPageDataModel();
		}
		return list;
	}

	public void setList( List<Customer> theList ) {

		this.list = theList;
	}

	public PaginationHelper<Customer> getPagination() {

		if ( pagination == null ) {
			pagination = new PaginationHelper<Customer>( 10 ) {

				@Override
				public int getItemsCount() {

					return count( Customer.class );
				}

				@Override
				public List<Customer> createPageDataModel() {

					return new ArrayList<Customer>( findAll( Customer.class, getPageFirstItem(), getPageSize() ) );
				}
			};
		}
		return pagination;
	}

	private void recreateModel() {

		list = null;
	}

	public String next() {

		getPagination().nextPage();
		recreateModel();
		return "list";
	}

	public String previous() {

		getPagination().previousPage();
		recreateModel();
		return "list";
	}
}
