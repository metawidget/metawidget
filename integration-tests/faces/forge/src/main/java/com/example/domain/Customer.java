package com.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import org.metawidget.inspector.annotation.UiComesAfter;

@Entity
public class Customer
	implements java.io.Serializable {

	@Id
	private @GeneratedValue( strategy = GenerationType.AUTO )
	@Column( name = "id", updatable = false, nullable = false )
	Long	id		= null;

	@Version
	private @Column( name = "version" )
	int		version	= 0;

	public Long getId() {

		return this.id;
	}

	public void setId( final Long theId ) {

		this.id = theId;
	}

	public int getVersion() {

		return this.version;
	}

	public void setVersion( final int theVersion ) {

		this.version = theVersion;
	}

	@Column( nullable = false )
	private String	name;

	public String getName() {

		return this.name;
	}

	public void setName( final String theName ) {

		this.name = theName;
	}

	@Column
	@UiComesAfter( "name" )
	private String	bio;

	public String getBio() {

		return this.bio;
	}

	public void setBio( final String theBio ) {

		this.bio = theBio;
	}
}
