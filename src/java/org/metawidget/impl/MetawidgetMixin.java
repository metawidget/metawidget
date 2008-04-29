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

package org.metawidget.impl;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.MetawidgetException;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Base class functionality for Metawidgets.
 * <p>
 * Use of MetawidgetMixin when developing Metawidgets is entirely optional, and may not be possible
 * on all platforms (eg. those that don't support <code>org.w3c.dom</code>). However, it
 * provides a level of functionality and structure to the code which most Metawidgets will benefit
 * from.
 * <p>
 * Specifically, the mixin provides support for:
 * <ul>
 * <li>single/compound widgets</li>
 * <li>widget overriding</li>
 * <li>stubs/stub attributes</li>
 * <li>read-only/active widgets</li>
 * </ul>
 *
 * @author Richard Kennard
 */

public abstract class MetawidgetMixin<W>
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

	public void buildWidgets()
	{
		try
		{
			startBuild();

			// Inspect

			Document document = inspect();

			if ( document != null )
			{
				// Build simple widget (from the top-level element)

				Element element = (Element) document.getDocumentElement().getFirstChild();
				Map<String, String> attributes = XmlUtils.getAttributesAsMap( element );

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
					// If the returned component is itself a Metawidget, it must have
					// the same path as us. In that case, DON'T use it, as that would
					// be infinite recursion

					if ( !isMetawidget( widget ) )
					{
						addWidget( widget, attributes );
					}

					// Failing that, build a compound widget (from our child elements)

					else
					{
						buildCompoundWidget( element );
					}
				}
			}

			// Even if no inspectors match, we still call endBuild(). This makes us
			// behave better in visual builder tools when dropping child components in

			endBuild();
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
	}

	//
	//
	// Protected methods
	//
	//

	protected void buildCompoundWidget( Element element )
		throws Exception
	{
		NodeList children = element.getChildNodes();

		for ( int loop = 0, length = children.getLength(); loop < length; loop++ )
		{
			Node node = children.item( loop );

			if ( !( node instanceof Element ) )
				continue;

			Element child = (Element) node;
			String childName = child.getAttribute( NAME );

			if ( childName == null || "".equals( childName ) )
				throw MetawidgetException.newException( "Child element #" + loop + " of '" + element.getAttribute( TYPE ) + "' has no @name" );

			Map<String, String> attributes = XmlUtils.getAttributesAsMap( child );

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

	/**
	 * @return false if the build should not proceed (for example if there was a previous validation
	 *         error)
	 */

	protected abstract void startBuild()
		throws Exception;

	protected abstract Document inspect();

	protected abstract W getOverridenWidget( Map<String, String> attributes );

	protected abstract boolean isStub( W widget );

	protected abstract Map<String, String> getStubAttributes( W stub );

	protected boolean isMetawidget( W widget )
	{
		return getClass().getDeclaringClass().isAssignableFrom( widget.getClass() );
	}

	protected abstract W buildReadOnlyWidget( Map<String, String> attributes )
		throws Exception;

	protected abstract W buildActiveWidget( Map<String, String> attributes )
		throws Exception;

	protected abstract void addWidget( W widget, Map<String, String> attributes )
		throws Exception;

	protected abstract void endBuild()
		throws Exception;
}
