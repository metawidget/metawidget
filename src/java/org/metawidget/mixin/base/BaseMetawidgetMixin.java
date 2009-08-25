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

import java.util.List;
import java.util.Map;

import org.metawidget.inspector.iface.Inspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

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
 * <p>
 * Note: this class is located in <code>org.metawidget.mixin.base</code>, as opposed to just
 * <code>org.metawidget.mixin</code>, to make it easier to integrate GWT (which is bad at ignoring
 * sub-packages such as <code>org.metawidget.mixin.w3c</code>).
 *
 * @author Richard Kennard
 */

public abstract class BaseMetawidgetMixin<W, E, M extends W>
{
	//
	// Private statics
	//

	private final static int			DEFAULT_MAXIMUM_INSPECTION_DEPTH	= 10;

	//
	// Private members
	//

	private boolean						mReadOnly;

	private boolean						mCompoundWidget;

	private int							mMaximumInspectionDepth				= DEFAULT_MAXIMUM_INSPECTION_DEPTH;

	private Inspector					mInspector;

	private WidgetBuilder<W, M>			mWidgetBuilder;

	private List<WidgetProcessor<W, M>>	mWidgetProcessors;

	//
	// Public methods
	//

	public void setReadOnly( boolean readOnly )
	{
		mReadOnly = readOnly;
	}

	public boolean isReadOnly()
	{
		return mReadOnly;
	}

	public boolean isCompoundWidget()
	{
		return mCompoundWidget;
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
	 * This can be useful in detecing cyclic references. Although <code>BaseObjectInspector</code>
	 * -derived Inspectors are capable of detecting cyclic references, other Inspectors may not be.
	 * For example, <code>BaseXmlInspector</code>-derived Inspectors cannot because they only test
	 * types, not actual objects.
	 *
	 * @param maximumInspectionDepth
	 *            0 for top-level only, 1 for 1 level deep etc.
	 */

	public void setMaximumInspectionDepth( int maximumInspectionDepth )
	{
		mMaximumInspectionDepth = maximumInspectionDepth;
	}

	public void setInspector( Inspector inspector )
	{
		mInspector = inspector;
	}

	public Inspector getInspector()
	{
		return mInspector;
	}

	public void setWidgetBuilder( WidgetBuilder<W, M> widgetBuilder )
	{
		mWidgetBuilder = widgetBuilder;
	}

	public WidgetBuilder<W, M> getWidgetBuilder()
	{
		return mWidgetBuilder;
	}

	public List<WidgetProcessor<W, M>> getWidgetProcessors()
	{
		return mWidgetProcessors;
	}

	public void setWidgetProcessors( List<WidgetProcessor<W, M>> widgetProcessors )
	{
		if ( widgetProcessors == null )
		{
			mWidgetProcessors = null;
			return;
		}

		// Defensive copy: the WidgetProcessors are immutable, but the List is not

		mWidgetProcessors = CollectionUtils.newArrayList( widgetProcessors );
	}

	public void addWidgetProcessor( WidgetProcessor<W, M> widgetProcessor )
	{
		if ( mWidgetProcessors == null )
			mWidgetProcessors = CollectionUtils.newArrayList();

		mWidgetProcessors.add( widgetProcessor );
	}

	/**
	 * Returns the first WidgetProcessor in this mixin's list of WidgetProcessors (ie. as added by
	 * <code>addWidgetProcessor</code>) that <code>isAssignableFrom</code> the given class.
	 *
	 * @param widgetProcessorClass
	 *            the class, or interface or superclass, to find. Returns <code>null</code> if no
	 *            such WidgetProcessor
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getWidgetProcessor( Class<T> widgetProcessorClass )
	{
		if ( mWidgetProcessors == null )
			return null;

		for ( WidgetProcessor<W, M> widgetProcessor : mWidgetProcessors )
		{
			if ( widgetProcessor.getClass().isAssignableFrom( widgetProcessorClass ) )
				return (T) widgetProcessor;
		}

		return null;
	}

	/**
	 * Inspect the given Object according to the given path, and return the result as a String
	 * conforming to inspection-result-1.0.xsd.
	 * <p>
	 * This method mirrors the <code>Inspector</code> interface. Internally it looks up the
	 * Inspector to use. It is a useful hook for subclasses wishing to inspect different Objects
	 * using our same <code>Inspector</code>.
	 */

	public String inspect( Object toInspect, String type, String... names )
	{
		if ( mInspector == null )
			throw new NullPointerException( "No inspector configured" );

		return mInspector.inspect( toInspect, type, names );
	}

	/**
	 * Build widgets from the given XML inspection result.
	 * <p>
	 * Note: the <code>BaseMetawidgetMixin</code> expects the XML to be passed in internally, rather
	 * than fetching it itself, because some XML inspections may be asynchronous.
	 */

	public void buildWidgets( String xml )
		throws Exception
	{
		mCompoundWidget = false;

		startBuild();
		processorStartBuild();

		if ( xml != null && !"".equals( xml ) )
		{
			// Build simple widget (from the top-level element)

			E element = getChildAt( getDocumentElement( xml ), 0 );
			Map<String, String> attributes = getAttributesAsMap( element );

			if ( isReadOnly() )
				attributes.put( READ_ONLY, TRUE );

			// It is a little counter-intuitive that there can ever be an override
			// of the top-level element. However, if we go down the path that builds
			// a single widget (eg. doesn't invoke buildCompoundWidget), then our
			// child is at the same top-level as us, and there are some scenarios (like
			// Java Server Faces POST backs) where we need to re-identify that

			String elementName = getElementName( element );
			W widget = getOverriddenWidget( elementName, attributes );

			if ( widget == null )
				widget = buildWidget( elementName, attributes );

			// If mWidgetBuilder.buildWidget returns null, try buildCompoundWidget (from our child
			// elements)

			if ( widget == null )
			{
				buildCompoundWidget( element );
			}
			else
			{
				processorAdd( widget, attributes );
				addWidget( widget, elementName, attributes );
			}
		}

		// Even if no inspectors match, we still call endBuild() because:
		//
		// 1. it makes us behave better in visual builder tools when dropping child widgets in
		// 2. you can use a Metawidget purely for layout, with no inspection

		endBuild();
		processorEndBuild();
	}

	/**
	 * Copies this mixin's values into another mixin. Useful for when a Metawidget creates a nested
	 * Metawidget.
	 * <p>
	 * Special behaviour is:
	 * <ul>
	 * <li>the given mixin is initialised with a maximumInspectionDepth of 1 less than the current
	 * maximumInspectionDepth. This is so that, as nesting continues, eventually the
	 * maximumInspectionDepth reaches zero</li>
	 * <li>the given mixin has setReadOnly if the current mixin has setReadOnly <em>or</em> if the
	 * attributes map contains <code>READ_ONLY</code></li>
	 * <li>the given mixin is initialised with the same Inspectors, WidgetBuilders and
	 * WidgetProcessors as the current mixin. This is safe because they are all immutable and
	 * threadsafe</li>
	 * </ul>
	 */

	public void initNestedMixin( BaseMetawidgetMixin<W, E, M> nestedMixin, Map<String, String> attributes )
	{
		nestedMixin.setReadOnly( isReadOnly() || TRUE.equals( attributes.get( READ_ONLY ) ) );
		nestedMixin.setMaximumInspectionDepth( getMaximumInspectionDepth() - 1 );

		// Inspectors, WidgetBuilders and WidgetProcessors can be shared because they are immutable
		// and threadsafe

		nestedMixin.setInspector( getInspector() );
		nestedMixin.setWidgetBuilder( getWidgetBuilder() );
		nestedMixin.setWidgetProcessors( mWidgetProcessors );
	}

	//
	// Protected methods
	//

	/**
	 * Build a compound widget by iterating through children of the given element, calling
	 * <code>buildWidget</code> and <code>addWidget</code on each.
	 */

	protected void buildCompoundWidget( E element )
		throws Exception
	{
		mCompoundWidget = true;

		for ( int loop = 0, length = getChildCount( element ); loop < length; loop++ )
		{
			E child = getChildAt( element, loop );

			if ( child == null )
				continue;

			Map<String, String> attributes = getAttributesAsMap( child );

			String childName = attributes.get( NAME );

			if ( childName == null || "".equals( childName ) )
				throw new Exception( "Child element #" + loop + " of '" + attributes.get( TYPE ) + "' has no @" + NAME );

			// Metawidget as a whole may have had setReadOnly( true )

			boolean forcedReadOnly = false;

			if ( !TRUE.equals( attributes.get( READ_ONLY ) ) && isReadOnly() )
			{
				attributes.put( READ_ONLY, TRUE );
				forcedReadOnly = true;
			}

			String elementName = getElementName( child );
			W widget = getOverriddenWidget( elementName, attributes );

			if ( widget == null )
			{
				widget = buildWidget( elementName, attributes );

				if ( widget == null )
				{
					if ( mMaximumInspectionDepth <= 0 )
						continue;

					// If setReadOnly( true ), remove our forced attribute so the nestedMetawidget
					// can differentiate whether it was forced or in the inspector XML

					if ( forcedReadOnly )
						attributes.remove( READ_ONLY );

					widget = buildNestedMetawidget( attributes );
				}
			}
			else if ( isStub( widget ) )
			{
				attributes.putAll( getStubAttributes( widget ) );
			}

			processorAdd( widget, attributes );
			addWidget( widget, elementName, attributes );
		}
	}

	protected void processorStartBuild()
	{
		if ( mWidgetProcessors == null )
			return;

		for ( WidgetProcessor<W, M> widgetProcessor : mWidgetProcessors )
		{
			widgetProcessor.onStartBuild( getMixinOwner() );
		}
	}

	protected void processorAdd( W widget, Map<String, String> attributes )
	{
		if ( mWidgetProcessors == null )
			return;

		for ( WidgetProcessor<W, M> widgetProcessor : mWidgetProcessors )
		{
			widgetProcessor.onAdd( widget, attributes, getMixinOwner() );
		}
	}

	protected void processorEndBuild()
	{
		if ( mWidgetProcessors == null )
			return;

		for ( WidgetProcessor<W, M> widgetProcessor : mWidgetProcessors )
		{
			widgetProcessor.onEndBuild( getMixinOwner() );
		}
	}

	//
	// Protected abstract methods
	//

	protected abstract E getDocumentElement( String xml );

	protected abstract int getChildCount( E element );

	/**
	 * @return the child at the given index. If the given child is not an Element, return null.
	 */

	protected abstract E getChildAt( E element, int index );

	protected abstract String getElementName( E element );

	protected abstract Map<String, String> getAttributesAsMap( E element );

	protected abstract void startBuild()
		throws Exception;

	protected abstract W getOverriddenWidget( String elementName, Map<String, String> attributes );

	protected abstract boolean isStub( W widget );

	protected abstract Map<String, String> getStubAttributes( W stub );

	/**
	 * Builds the widget using our <code>WidgetBuilder</code>.
	 * <p>
	 * Subclasses can override this method to perform post-initalization on the built widget. Those
	 * looking to perform post-initialization on any widget (whether built or overridden) should
	 * override <code>addWidget</code>.
	 */

	protected W buildWidget( String elementName, Map<String, String> attributes )
		throws Exception
	{
		return mWidgetBuilder.buildWidget( elementName, attributes, getMixinOwner() );
	}

	protected abstract M buildNestedMetawidget( Map<String, String> attributes )
		throws Exception;

	protected abstract M getMixinOwner();

	protected abstract void addWidget( W widget, String elementName, Map<String, String> attributes )
		throws Exception;

	protected abstract void endBuild()
		throws Exception;
}
