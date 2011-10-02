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

package org.metawidget.inspector.jpa;

import java.util.Collections;
import java.util.EnumMap;
import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Java Persistence API (JPA).
 *
 * @author Richard Kennard
 */

public class JpaInspector
	extends BaseObjectInspector {
    
        //
        // Private constants
        //
        private static final Map<TemporalType, String> TEMPORAL_TO_TYPE;
        static {
             EnumMap<TemporalType, String> temporalToType = 
                new EnumMap(TemporalType.class);
             temporalToType.put(TemporalType.DATE, "date");
             temporalToType.put(TemporalType.TIME, "time");
             temporalToType.put(TemporalType.TIMESTAMP, "both");
             TEMPORAL_TO_TYPE = Collections.unmodifiableMap(temporalToType);
        }

	//
	// Private members
	//

	private final boolean	mHideIds;

	private final boolean	mHideVersions;

	private final boolean	mHideTransients;

	//
	// Constructor
	//

	public JpaInspector() {

		this( new JpaInspectorConfig() );
	}

	public JpaInspector( JpaInspectorConfig config ) {

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

		ManyToOne manyToOne = property.getAnnotation( ManyToOne.class );

		if ( manyToOne != null ) {
			if ( !manyToOne.optional() ) {
				attributes.put( REQUIRED, TRUE );
			}
		}

		// Hidden

		if ( mHideIds && property.isAnnotationPresent( Id.class ) ) {
			attributes.put( HIDDEN, TRUE );
		} else if ( mHideVersions && property.isAnnotationPresent( Version.class ) ) {
			attributes.put( HIDDEN, TRUE );
		} else if ( mHideTransients && property.isAnnotationPresent( Transient.class ) ) {
			attributes.put( HIDDEN, TRUE );
		}
                
                // DateTime Type
                
                Temporal temporal = property.getAnnotation(Temporal.class);
                if (temporal != null) {
                    String type = TEMPORAL_TO_TYPE.get(temporal.value());
                    if (type != null) {
                        attributes.put( DATETIME_TYPE, type );
                    }
                }
                
		return attributes;
	}
}
