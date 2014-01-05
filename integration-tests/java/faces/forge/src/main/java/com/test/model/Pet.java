// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package com.test.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.faces.UiFacesConverter;
import org.metawidget.inspector.faces.UiFacesLookup;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Entity
public class Pet
	implements Serializable {

	//
	// Private members
	//

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column( nullable = false, updatable = false )
	private Long	id;

	@Version
	private int		version;

	@NotNull
	@Size( max = 25 )
	private String	name;

	@ManyToOne
	@UiFacesLookup( "#{personBean.all}" )
	@UiFacesConverter( "#{personBean.converter}" )
	@UiComesAfter( "name" )
	private Person	owner;

	@UiComesAfter( "owner" )
	@Max( 100 )
	private int		age;

	@UiComesAfter( "age" )
	private boolean	deceased;

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

	public String getName() {

		return this.name;
	}

	public void setName( final String theName ) {

		this.name = theName;
	}

	public Person getOwner() {

		return this.owner;
	}

	public void setOwner( final Person theOwner ) {

		this.owner = theOwner;
	}

	public int getAge() {

		return this.age;
	}

	public void setAge( final int theAge ) {

		this.age = theAge;
	}

	public boolean isDeceased() {

		return this.deceased;
	}

	public void setDeceased( final boolean theDeceased ) {

		this.deceased = theDeceased;
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
			return this.id.equals( ( (Pet) that ).id );
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
}
