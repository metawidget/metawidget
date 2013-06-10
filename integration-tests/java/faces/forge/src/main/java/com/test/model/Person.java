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

package com.test.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLookup;

@Entity
public class Person
	implements Serializable {

	//
	// Private members
	//

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column( nullable = false, updatable = false )
	private Long		id;

	@Version
	private int			version;

	@NotNull
	@Size( max = 5 )
	@UiLookup( { "Mr", "Mrs", "Miss", "Dr", "Cpt" } )
	private String		title;

	@NotNull
	@UiComesAfter( "title" )
	@Size( max = 30 )
	private String		firstname;

	@NotNull
	@UiComesAfter( "firstname" )
	@Size( max = 30 )
	private String		surname;

	@Column
	@UiComesAfter( "surname" )
	@Pattern( regexp = "(([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+))|", message = "Email is not in valid format" )
	private String		email;

	@UiComesAfter( "email" )
	@OneToMany( mappedBy = "owner" )
	@OrderBy( "name" )
	private List<Pet>	pets;

	//
	// Public methods
	//

	public Long getId() {

		return this.id;
	}

	public void setId( Long theId ) {

		this.id = theId;
	}

	public int getVersion() {

		return this.version;
	}

	public void setVersion( final int theVersion ) {

		this.version = theVersion;
	}

	public String getTitle() {

		return this.title;
	}

	public void setTitle( final String theTitle ) {

		this.title = theTitle;
	}

	public String getFirstname() {

		return this.firstname;
	}

	public void setFirstname( final String theFirstname ) {

		this.firstname = theFirstname;
	}

	public String getSurname() {

		return this.surname;
	}

	public void setSurname( final String theSurname ) {

		this.surname = theSurname;
	}

	public String getEmail() {

		return this.email;
	}

	public void setEmail( final String theEmail ) {

		this.email = theEmail;
	}

	public List<Pet> getPets() {

		return this.pets;
	}

	public void setPets( final List<Pet> thePets ) {

		this.pets = thePets;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}
		if ( that == null ) {
			return false;
		}
		if ( getClass() != that.getClass() ) {
			return false;
		}
		if ( this.id != null ) {
			return this.id.equals( ( (Person) that ).id );
		}
		return super.equals( that );
	}

	@Override
	public int hashCode() {

		if ( this.id != null ) {
			return this.id.hashCode();
		}
		return super.hashCode();
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		if ( this.title != null ) {
			builder.append( this.title );
		}

		if ( this.firstname != null ) {
			builder.append( ' ' );
			builder.append( this.firstname );
		}

		if ( this.surname != null ) {
			builder.append( ' ' );
			builder.append( this.surname );
		}

		return builder.toString();
	}
}