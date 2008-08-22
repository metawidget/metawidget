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

package org.metawidget.mixin.base;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

/**
 * Mixin to help build Metawidgets.
 * <p>
 * Use of MetawidgetMixin when developing Metawidgets is entirely optional. However, it provides a
 * level of functionality and structure to the code which most Metawidgets will benefit from.
 * <p>
 * Specifically, the mixin provides support for:
 * <ul>
 * <li>single/compound widgets</li>
 * <li>widget overriding</li>
 * <li>stubs/stub attributes</li>
 * <li>read-only/active widgets</li>
 * <li>maximum inspection depth</li>
 * </ul>
 * This base class abstracts the Metawidget lifecycle without enforcing which XML libraries to use.
 * Most subclasses will choose <code>org.metawidget.mixin.w3c.MetawidgetMixin</code>, which uses
 * <code>org.w3c.dom</code>.
 *
 * @author Richard Kennard
 */

public abstract class BaseMetawidgetMixin<W, E>
{
	//
	//
	// Private statics
	//
	//

	private final static int	DEFAULT_MAXIMUM_INSPECTION_DEPTH	= 10;

	//
	//
	// Private members
	//
	//

	private boolean				mReadOnly;

	private int					mMaximumInspectionDepth				= DEFAULT_MAXIMUM_INSPECTION_DEPTH;

	//
	//
	// Public methods
	//
	//

	public void setReadOnly( boolean readOnly )
	{
		mReadOnly = readOnly;
	}

	public boolean isReadOnly()
	{
		return mReadOnly;
	}

	public int getMaximumInspectionDepth()
	{
		return mMaximumInspectionDepth;
	}

	/**
	 * Sets the maximum depth of inspection.
	 * <p>
	 * Metawidget renders most non-primitve types by using nested Metawidgets. This value limits the
	 * number of nestings.
	 * <p>
	 * This can be useful in detecing cyclic references. Although <code>BaseObjectInspector</code>-derived
	 * Inspectors are capable of detecting cyclic references, other Inspectors may not be. For
	 * example, <code>BaseXmlInspector</code>-derived Inspectors cannot because they only test
	 * types, not actual objects.
	 *
	 * @param maximumDepth
	 *            0 for top-level only, 1 for 1 level deep etc.
	 */

	public void setMaximumInspectionDepth( int maximumInspectionDepth )
	{
		mMaximumInspectionDepth = maximumInspectionDepth;
	}

	/**
	 * Build widgets from the given XML inspection result.
	 * <p>
	 * Note: the <code>BaseMetawidgetMixin</code> expects the XML to be passed in internally,
	 * rather than fetching it itself, because some XML inspections may be asynchronous.
	 */

	public void buildWidgets( String xml )
		throws Exception
	{
		startBuild();

		if ( xml != null )
		{
			// Build simple widget (from the top-level element)

			E element = getFirstElement( xml );
			Map<String, String> attributes = getAttributesAsMap( element );

			// It is a little counter-intuitive that there can ever be an override
			// of the top-level element. However, if we go down the path that builds
			// a single widget (eg. doesn't invoke buildCompoundWidget), then our
			// child is at the same top-level as us, and there are some scenarios (like
			// Java Server Faces POST backs) where we need to re-identify that

			String elementName = getElementName( element );
			W widget = getOverridenWidget( elementName, attributes );

			if ( widget == null )
				widget = buildWidget( elementName, attributes );

			if ( widget != null )
			{
				// If the returned widget is itself a Metawidget, it must have
				// the same path as us. In that case, DON'T use it, as that would
				// be infinite recursion

				if ( !isMetawidget( widget ) )
				{
					addWidget( widget, elementName, attributes );
				}

				// Failing that, build a compound widget (from our child elements)

				else
				{
					buildCompoundWidget( element );
				}
			}
		}

		// Even if no inspectors match, we still call endBuild() because:
		//
		// 1. it makes us behave better in visual builder tools when dropping child widgets in
		// 2. you can use a Metawidget purely for layout, with no inspection

		endBuild();
	}

	//
	//
	// Protected methods
	//
	//

	protected void buildCompoundWidget( E element )
		throws Exception
	{
		for ( int loop = 0, length = getChildCount( element ); loop < length; loop++ )
		{
			E child = getChildAt( element, loop );

			if ( child == null )
				continue;

			Map<String, String> attributes = getAttributesAsMap( child );

			String childName = attributes.get( NAME );

			if ( childName == null || "".equals( childName ) )
				throw new Exception( "Child element #" + loop + " of '" + attributes.get( TYPE ) + "' has no @" + NAME );

			String elementName = getElementName( child );
			W widget = getOverridenWidget( elementName, attributes );

			if ( widget == null )
			{
				widget = buildWidget( elementName, attributes );

				if ( widget == null )
					continue;

				if ( isMetawidget( widget ) )
				{
					if ( mMaximumInspectionDepth <= 0 )
						continue;

					widget = initMetawidget( widget, attributes );
				}
			}
			else if ( isStub( widget ) )
			{
				attributes.putAll( getStubAttributes( widget ) );
			}

			addWidget( widget, elementName, attributes );
		}
	}

	protected boolean isReadOnly( Map<String, String> attributes )
	{
		if ( TRUE.equals( attributes.get( READ_ONLY ) ) )
			return true;

		return mReadOnly;
	}

	protected W buildWidget( String elementName, Map<String, String> attributes )
		throws Exception
	{
		if ( isReadOnly( attributes ) )
			return buildReadOnlyWidget( elementName, attributes );

		return buildActiveWidget( elementName, attributes );
	}

	//
	//
	// Protected abstract methods
	//
	//

	protected abstract E getFirstElement( String xml );

	protected abstract int getChildCount( E element );

	/**
	 * @return the child at the given index. If the given child is not an Element, return null.
	 */

	protected abstract E getChildAt( E element, int index );

	protected abstract String getElementName( E element );

	protected abstract Map<String, String> getAttributesAsMap( E element );

	protected abstract void startBuild()
		throws Exception;

	protected abstract W getOverridenWidget( String elementName, Map<String, String> attributes );

	protected abstract boolean isStub( W widget );

	protected abstract Map<String, String> getStubAttributes( W stub );

	protected abstract boolean isMetawidget( W widget );

	protected abstract W buildReadOnlyWidget( String elementName, Map<String, String> attributes )
		throws Exception;

	/**
	 * Build 'active' (as opposed to read-only) widgets.
	 */

	protected abstract W buildActiveWidget( String elementName, Map<String, String> attributes )
		throws Exception;

	protected abstract W initMetawidget( W widget, Map<String, String> attributes )
		throws Exception;

	protected abstract void addWidget( W widget, String elementName, Map<String, String> attributes )
		throws Exception;

	protected abstract void endBuild()
		throws Exception;
}
