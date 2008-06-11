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

package org.metawidget.inspector.annotation;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Iterator;
import java.util.Map;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BasePropertyInspector;
import org.metawidget.inspector.impl.BasePropertyInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Inspects annotations defined by Metawidget (declared in this same package).
 * <p>
 * Note: the name of this class is longwinded for extra clarity. It is not just a
 * 'MetawidgetInspector', because of course there are lots of different Metawidget Inspectors.
 * Equally, it is not just an 'AnnotationInspector', because it doesn't generically scan all
 * possible annotations.
 *
 * @author Richard Kennard
 */

public class MetawidgetAnnotationInspector
	extends BasePropertyInspector
{
	//
	//
	// Constructor
	//
	//

	public MetawidgetAnnotationInspector()
	{
		this( new BasePropertyInspectorConfig() );
	}

	public MetawidgetAnnotationInspector( BasePropertyInspectorConfig config )
	{
		super( config );
	}

	//
	//
	// Protected methods
	//
	//

	@Override
	protected void inspect( Class<?> clazz, Object toInspect, Element toAddTo )
		throws Exception
	{
		Document document = toAddTo.getOwnerDocument();
		Map<String, ElementWithComesAfter> elementsWithComesAfter = CollectionUtils.newHashMap();

		// For each property...

		Map<String, Property> properties = getProperties( clazz );

		for ( Property property : properties.values() )
		{
			// ...inspect its attributes...

			Map<String, String> attributes = inspect( property, toInspect );

			// ...(defer the UiComesAfter ones)...

			String name = property.getName();
			UiComesAfter uiComesAfter = property.getAnnotation( UiComesAfter.class );

			if ( uiComesAfter != null )
			{
				Element element = document.createElementNS( NAMESPACE, PROPERTY );
				element.setAttribute( NAME, name );

				XmlUtils.setMapAsAttributes( element, attributes );
				elementsWithComesAfter.put( name, new ElementWithComesAfter( element, uiComesAfter.value() ) );
				continue;
			}

			if ( attributes == null || attributes.isEmpty() )
				continue;

			// ...create an element...

			Element element = document.createElementNS( NAMESPACE, PROPERTY );
			element.setAttribute( NAME, name );

			XmlUtils.setMapAsAttributes( element, attributes );

			// ...and add it

			toAddTo.appendChild( element );
		}

		// If there are any UiComeAfters...

		int size = elementsWithComesAfter.size();

		if ( size == 0 )
			return;

		// ...sort them by...

		int infiniteLoop = elementsWithComesAfter.size();
		infiniteLoop *= infiniteLoop;

		while ( !elementsWithComesAfter.isEmpty() )
		{
			infiniteLoop--;

			if ( infiniteLoop < 0 )
				throw InspectorException.newException( "Infinite loop detected when sorting @UiComesAfter" );

			// ...looking at each entry in the Map, and...

			outer: for ( Iterator<Map.Entry<String, ElementWithComesAfter>> i = elementsWithComesAfter.entrySet().iterator(); i.hasNext(); )
			{
				Map.Entry<String, ElementWithComesAfter> entry = i.next();
				ElementWithComesAfter elementWithComesAfter = entry.getValue();
				String[] comesAfters = elementWithComesAfter.getComesAfter();

				// ...if it 'Comes After everything', make sure there are only
				// other 'Comes After everything's left...

				if ( comesAfters.length == 0 )
				{
					for ( ElementWithComesAfter elementWithComesAfterExisting : elementsWithComesAfter.values() )
					{
						if ( elementWithComesAfterExisting.getComesAfter().length > 0 )
							continue outer;
					}
				}

				// ...or, if it 'Comes After' something, make sure none of those
				// somethings are left...

				else
				{
					String name = entry.getKey();

					for ( String comesAfter : comesAfters )
					{
						if ( name.equals( comesAfter ) )
							throw InspectorException.newException( "'" + comesAfter + "' is annotated to @UiComesAfter itself" );

						if ( elementsWithComesAfter.keySet().contains( comesAfter ) )
							continue outer;

						// ...(if it has 'Comes Afters' not already in the list, add them
						// just-in-time)...

						Element existing = XmlUtils.getChildWithAttributeValue( toAddTo, NAME, comesAfter );

						if ( existing == null )
						{
							if ( properties.containsKey( comesAfter ) )
							{
								Element element = document.createElementNS( NAMESPACE, PROPERTY );
								element.setAttribute( NAME, comesAfter );

								toAddTo.appendChild( element );
							}
						}
					}
				}

				// ...and add the element

				toAddTo.appendChild( elementWithComesAfter.getElement() );

				i.remove();
			}
		}
	}

	@Override
	protected Map<String, String> inspect( Property property, Object toInspect )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// UiLookup

		UiLookup lookup = property.getAnnotation( UiLookup.class );

		if ( lookup != null )
		{
			if ( !lookup.onlyIfNull() || property.read( toInspect ) == null )
			{
				attributes.put( LOOKUP, ArrayUtils.toString( lookup.value() ) );

				// (note: values().length == labels().length() is not validated
				// here, as XmlInspector could bypass it anyway)

				if ( lookup.labels().length > 0 )
					attributes.put( LOOKUP_LABELS, ArrayUtils.toString( lookup.labels() ) );
			}
		}

		// UiMasked

		if ( property.isAnnotationPresent( UiMasked.class ) )
			attributes.put( MASKED, TRUE );

		// UiHidden

		if ( property.isAnnotationPresent( UiHidden.class ) )
			attributes.put( HIDDEN, TRUE );

		// UiLarge

		if ( property.isAnnotationPresent( UiLarge.class ) )
			attributes.put( LARGE, TRUE );

		// UiReadOnly

		UiReadOnly readOnly = property.getAnnotation( UiReadOnly.class );

		if ( readOnly != null )
			attributes.put( READ_ONLY, TRUE );

		// UiDontExpand

		UiDontExpand dontExpand = property.getAnnotation( UiDontExpand.class );

		if ( dontExpand != null )
			attributes.put( DONT_EXPAND, TRUE );

		// UiSection

		UiSection uiSection = property.getAnnotation( UiSection.class );

		if ( uiSection != null )
			attributes.put( SECTION, uiSection.value() );

		// UiLabel

		UiLabel label = property.getAnnotation( UiLabel.class );

		if ( label != null )
			attributes.put( LABEL, label.value() );

		// UiAttribute

		UiAttribute attribute = property.getAnnotation( UiAttribute.class );

		if ( attribute != null )
		{
			attributes.put( attribute.name(), attribute.value() );
		}

		// UiAttributes

		UiAttributes uiAttributes = property.getAnnotation( UiAttributes.class );

		if ( uiAttributes != null )
		{
			for ( UiAttribute nestedAttribute : uiAttributes.value() )
			{
				attributes.put( nestedAttribute.name(), nestedAttribute.value() );
			}
		}

		return attributes;
	}

	//
	//
	// Inner class
	//
	//

	private static class ElementWithComesAfter
	{
		//
		//
		// Private members
		//
		//

		private Element		mElement;

		private String[]	mComesAfter;

		//
		//
		// Constructor
		//
		//

		public ElementWithComesAfter( Element element, String[] comesAfter )
		{
			mElement = element;
			mComesAfter = comesAfter;
		}

		//
		//
		// Public methods
		//
		//

		public Element getElement()
		{
			return mElement;
		}

		public String[] getComesAfter()
		{
			return mComesAfter;
		}
	}
}
