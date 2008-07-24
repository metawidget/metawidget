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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.metawidget.inspector.impl.BasePropertyInspector;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Java Persistence Architecture (JPA).
 *
 * @author Richard Kennard
 */

public class JpaInspector
	extends BasePropertyInspector
{
	//
	//
	// Private members
	//
	//

	private boolean	mHideIds;

	//
	//
	// Constructor
	//
	//

	public JpaInspector()
	{
		this( new JpaInspectorConfig() );
	}

	public JpaInspector( JpaInspectorConfig config )
	{
		super( config );

		mHideIds = config.isHideIds();
	}

	//
	//
	// Protected methods
	//
	//

	@Override
	protected Map<String, String> inspectProperty( Property property, Object toInspect )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Large

		if ( property.isAnnotationPresent( Lob.class ) )
			attributes.put( LARGE, TRUE );

		// Required

		Column column = property.getAnnotation( Column.class );

		if ( column != null )
		{
			if ( !column.nullable() )
				attributes.put( REQUIRED, TRUE );

			// Length

			if ( column.length() > 0 )
				attributes.put( MAXIMUM_LENGTH, String.valueOf( column.length() ) );
		}

		ManyToOne manyToOne = property.getAnnotation( ManyToOne.class );

		if ( manyToOne != null )
		{
			if ( !manyToOne.optional() )
				attributes.put( REQUIRED, TRUE );
		}

		// Hidden

		if ( mHideIds && property.isAnnotationPresent( Id.class ))
			attributes.put( HIDDEN, TRUE );

		return attributes;
	}
}
