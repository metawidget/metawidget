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

package org.metawidget.inspector.jakartajpa;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;

import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Java Persistence API (JPA).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JakartaJpaInspector
	extends BaseObjectInspector {

	//
	// Private members
	//

	private final boolean	mHideIds;

	private final boolean	mHideVersions;

	private final boolean	mHideTransients;

	//
	// Constructor
	//

	public JakartaJpaInspector() {

		this( new JakartaJpaInspectorConfig() );
	}

	public JakartaJpaInspector( JakartaJpaInspectorConfig config ) {

		super( config );

		mHideIds = config.isHideIds();
		mHideVersions = config.isHideVersions();
		mHideTransients = config.isHideTransients();
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectProperty( Property property )
		throws Exception {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Large

		if ( property.isAnnotationPresent( Lob.class ) ) {
			attributes.put( LARGE, TRUE );
		}

		// Required

		Column column = property.getAnnotation( Column.class );

		if ( column != null ) {
			if ( !column.nullable() ) {
				attributes.put( REQUIRED, TRUE );
			}

			// Length
			//
			// Note: the default is 255. It is tempting to enforce this as MAXIMUM_LENGTH anyway,
			// given that otherwise data may get silently truncated, but that doesn't work very
			// well because not every JPA property will be annotated @Column

			if ( column.length() != 255 ) {
				attributes.put( MAXIMUM_LENGTH, String.valueOf( column.length() ) );
			}
		}

		OneToOne oneToOne = property.getAnnotation( OneToOne.class );

		if ( oneToOne != null ) {

			if ( !oneToOne.optional() ) {
				attributes.put( REQUIRED, TRUE );
			}

			String mappedBy = oneToOne.mappedBy();

			if ( !"".equals( mappedBy ) ) {
				attributes.put( INVERSE_RELATIONSHIP, mappedBy );
			}
		}

		OneToMany oneToMany = property.getAnnotation( OneToMany.class );

		if ( oneToMany != null ) {

			String mappedBy = oneToMany.mappedBy();

			if ( !"".equals( mappedBy ) ) {
				attributes.put( INVERSE_RELATIONSHIP, mappedBy );
			}
		}

		ManyToOne manyToOne = property.getAnnotation( ManyToOne.class );

		if ( manyToOne != null && !manyToOne.optional() ) {
			attributes.put( REQUIRED, TRUE );
		}

		// Hidden

		if ( mHideIds && property.isAnnotationPresent( Id.class ) ) {
			attributes.put( HIDDEN, TRUE );
		} else if ( mHideIds && property.isAnnotationPresent( EmbeddedId.class ) ) {
			attributes.put( HIDDEN, TRUE );
		} else if ( mHideVersions && property.isAnnotationPresent( Version.class ) ) {
			attributes.put( HIDDEN, TRUE );
		} else if ( mHideTransients && property.isAnnotationPresent( Transient.class ) ) {
			attributes.put( HIDDEN, TRUE );
		}

		// Temporal

		Temporal temporal = property.getAnnotation( Temporal.class );
		if ( temporal != null ) {
			switch ( temporal.value() ) {
				case DATE:
					attributes.put( DATETIME_TYPE, "date" );
					break;
				case TIME:
					attributes.put( DATETIME_TYPE, "time" );
					break;
				case TIMESTAMP:
					attributes.put( DATETIME_TYPE, "both" );
					break;
				default:
					throw new UnsupportedOperationException( temporal.value().toString() );
			}
		}

		return attributes;
	}
}
