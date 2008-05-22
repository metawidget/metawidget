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

package org.metawidget.impl.base;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

/**
 * Base class functionality for MetawidgetMixins.
 * <p>
 * Use of MetawidgetMixins when developing Metawidgets is entirely optional. However, it provides a
 * level of functionality and structure to the code which most Metawidgets will benefit from.
 * <p>
 * Specifically, the mixin provides support for:
 * <ul>
 * <li>single/compound widgets</li>
 * <li>widget overriding</li>
 * <li>stubs/stub attributes</li>
 * <li>read-only/active widgets</li>
 * </ul>
 * This base class abstracts the Metawidget lifecycle without enforcing which XML libraries to use.
 * Most subclasses will choose <code>MetawidgetMixin</code>, which uses <code>org.w3c.dom</code>.
 *
 * @author Richard Kennard
 */

public abstract class BaseMetawidgetMixin<W, D, E, N>
{
	//
	//
	// Private members
	//
	//

	private boolean	mReadOnly;

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

	public void buildWidgets( D document )
		throws Exception
	{
		startBuild();

		if ( document != null )
		{
			// Build simple widget (from the top-level element)

			E element = getFirstElement( document );
			Map<String, String> attributes = getAttributesAsMap( element );

			// It is a little counter-intuitive that there can ever be an override
			// of the top-level element. However, if we go down the path that builds
			// a single widget (eg. doesn't invoke buildCompoundWidget), then our
			// child is at the same top-level as us, and there are some scenarios (like
			// Java Server Faces POST backs) where we need to re-identify that

			W widget = getOverridenWidget( attributes );

			if ( widget == null )
				widget = buildWidget( attributes );

			if ( widget != null )
			{
				// If the returned widget is itself a Metawidget, it must have
				// the same path as us. In that case, DON'T use it, as that would
				// be infinite recursion

				if ( !isMetawidget( widget ) )
				{
					addWidget( widget, attributes );
				}

				// Failing that, build a compound widget (from our child elements)

				else
				{
					beforeBuildCompoundWidget( element );
					buildCompoundWidget( element );
				}
			}
		}

		// Even if no inspectors match, we still call endBuild(). This makes us
		// behave better in visual builder tools when dropping child widgets in

		endBuild();
	}

	//
	//
	// Protected methods
	//
	//

	protected void beforeBuildCompoundWidget( E element )
	{
		// Do nothing by default
	}

	protected void buildCompoundWidget( E element )
		throws Exception
	{
		for ( int loop = 0, length = getChildCount( element ); loop < length; loop++ )
		{
			N node = getChildAt( element, loop );

			if ( !isElement( node ) )
				continue;

			@SuppressWarnings( "unchecked" )
			E child = (E) node;
			Map<String, String> attributes = getAttributesAsMap( child );

			String childName = attributes.get( NAME );

			if ( childName == null || "".equals( childName ) )
				throw new Exception( "Child element #" + loop + " of '" + attributes.get( TYPE ) + "' has no @name" );

			W widget = getOverridenWidget( attributes );

			if ( widget == null )
			{
				widget = buildWidget( attributes );

				if ( widget == null )
					continue;

				if ( isMetawidget( widget ) )
					widget = initMetawidget( widget, attributes );
			}
			else if ( isStub( widget ) )
			{
				attributes.putAll( getStubAttributes( widget ) );
			}

			addWidget( widget, attributes );
		}
	}

	protected boolean isReadOnly( Map<String, String> attributes )
	{
		if ( TRUE.equals( attributes.get( READ_ONLY ) ) )
			return true;

		return mReadOnly;
	}

	protected W buildWidget( Map<String, String> attributes )
		throws Exception
	{
		if ( isReadOnly( attributes ) )
			return buildReadOnlyWidget( attributes );

		return buildActiveWidget( attributes );
	}

	protected W initMetawidget( W widget, Map<String, String> attributes )
		throws Exception
	{
		// Not all Metawidgets use deferred initialization

		return widget;
	}

	//
	//
	// Protected abstract methods
	//
	//

	protected abstract E getFirstElement( D document );

	protected abstract int getChildCount( E element );

	protected abstract N getChildAt( E element, int index );

	protected abstract boolean isElement( N node );

	protected abstract Map<String, String> getAttributesAsMap( E element );

	protected abstract void startBuild()
		throws Exception;

	protected abstract W getOverridenWidget( Map<String, String> attributes );

	protected abstract boolean isStub( W widget );

	protected abstract Map<String, String> getStubAttributes( W stub );

	protected abstract boolean isMetawidget( W widget );

	protected abstract W buildReadOnlyWidget( Map<String, String> attributes )
		throws Exception;

	protected abstract W buildActiveWidget( Map<String, String> attributes )
		throws Exception;

	protected abstract void addWidget( W widget, Map<String, String> attributes )
		throws Exception;

	protected abstract void endBuild()
		throws Exception;
}
