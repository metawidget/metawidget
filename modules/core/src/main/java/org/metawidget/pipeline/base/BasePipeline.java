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

package org.metawidget.pipeline.base;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspectionresultprocessor.iface.DomInspectionResultProcessor;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessorException;
import org.metawidget.inspector.iface.DomInspector;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.layout.iface.Layout;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * Convenience implementation for implementing pipelines.
 * <p>
 * Use of BasePipeline when developing Metawidgets is entirely optional. However, it provides a
 * level of functionality and structure to the code which most Metawidgets will benefit from.
 * <p>
 * Specifically, BasePipeline provides support for:
 * <ul>
 * <li>Inspectors, InspectionResultProcessors, WidgetBuilders, WidgetProcessors and Layouts</li>
 * <li>single/compound widgets</li>
 * <li>stubs/stub attributes</li>
 * <li>read-only/active widgets</li>
 * <li>maximum inspection depth</li>
 * </ul>
 * This base class abstracts the pipeline without enforcing which XML libraries to use. Most
 * subclasses will choose <code>org.metawidget.pipeline.w3c.W3CPipeline</code>, which uses
 * <code>org.w3c.dom</code>.
 * <p>
 * Note: this class is located in <code>org.metawidget.pipeline.base</code>, as opposed to just
 * <code>org.metawidget.pipeline</code>, to make it easier to integrate GWT (which is bad at
 * ignoring sub-packages such as <code>org.metawidget.pipeline.w3c</code>).
 *
 * @author Richard Kennard
 */

public abstract class BasePipeline<W, C extends W, E, M extends C> {

	//
	// Private statics
	//

	private static final int					DEFAULT_MAXIMUM_INSPECTION_DEPTH	= 10;

	//
	// Private members
	//

	private boolean								mReadOnly;

	private int									mMaximumInspectionDepth				= DEFAULT_MAXIMUM_INSPECTION_DEPTH;

	private boolean								mNeedsConfiguring					= true;

	private Inspector							mInspector;

	private List<InspectionResultProcessor<M>>	mInspectionResultProcessors;

	private WidgetBuilder<W, M>					mWidgetBuilder;

	private List<WidgetProcessor<W, M>>			mWidgetProcessors;

	private Layout<W, C, M>						mLayout;

	//
	// Public methods
	//

	public void setReadOnly( boolean readOnly ) {

		mReadOnly = readOnly;
	}

	public boolean isReadOnly() {

		return mReadOnly;
	}

	public int getMaximumInspectionDepth() {

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

	public void setMaximumInspectionDepth( int maximumInspectionDepth ) {

		mMaximumInspectionDepth = maximumInspectionDepth;
	}

	public void setNeedsConfiguring() {

		mNeedsConfiguring = true;
	}

	/**
	 * Configures the Metawidget, then sets a flag so that subsequent
	 * calls to <code>configureOnce</code> do nothing.
	 * <p>
	 * The flag can be reset by calling <code>setNeedsConfiguring</code>.
	 */

	public void configureOnce() {

		if ( !mNeedsConfiguring ) {
			return;
		}

		mNeedsConfiguring = false;

		configure();
	}

	public void setInspector( Inspector inspector ) {

		mInspector = inspector;
	}

	public Inspector getInspector() {

		configureOnce();
		return mInspector;
	}

	/**
	 * Gets the List of InspectionResultProcessors.
	 * <p>
	 * This pipeline only references a single Inspector and single WidgetBuilder. It relies on
	 * CompositeInspector and CompositeWidgetBuilder to support multiples, which allows the
	 * combination algorithm itself to be pluggable.
	 * <p>
	 * We use a List of InspectionResultProcessors, however, so as to be consistent with
	 * WidgetProcessors. Note ordering of InspectionResultProcessors is significant.
	 */

	public List<InspectionResultProcessor<M>> getInspectionResultProcessors() {

		configureOnce();
		return mInspectionResultProcessors;
	}

	public void setInspectionResultProcessors( List<InspectionResultProcessor<M>> inspectionResultProcessors ) {

		mInspectionResultProcessors = inspectionResultProcessors;
	}

	public void addInspectionResultProcessor( InspectionResultProcessor<M> inspectionResultProcessor ) {

		configureOnce();

		if ( mInspectionResultProcessors == null ) {
			mInspectionResultProcessors = new ArrayList<InspectionResultProcessor<M>>();
		} else if ( mInspectionResultProcessors.contains( inspectionResultProcessor ) ) {
			throw InspectionResultProcessorException.newException( "List of InspectionResultProcessors already contains " + inspectionResultProcessor.getClass().getName() );
		}

		mInspectionResultProcessors.add( inspectionResultProcessor );
	}

	public void removeInspectionResultProcessor( InspectionResultProcessor<M> inspectionResultProcessors ) {

		configureOnce();

		if ( mInspectionResultProcessors == null ) {
			return;
		}

		mInspectionResultProcessors.remove( inspectionResultProcessors );
	}

	public void setWidgetBuilder( WidgetBuilder<W, M> widgetBuilder ) {

		mWidgetBuilder = widgetBuilder;
	}

	public WidgetBuilder<W, M> getWidgetBuilder() {

		configureOnce();
		return mWidgetBuilder;
	}

	/**
	 * Gets the List of WidgetProcessors.
	 * <p>
	 * This pipeline only references a single Inspector and single WidgetBuilder. It relies on
	 * CompositeInspector and CompositeWidgetBuilder to support multiples, which allows the
	 * combination algorithm itself to be pluggable.
	 * <p>
	 * We cannot use this same approach for WidgetProcessors, however, because we want to support
	 * event handling. We want to allow, for example:
	 * <p>
	 * <code>
	 * metawidget.addWidgetProcessor( new WidgetProcessor() {<br/>
	 * ...handle event...<br/>
	 * }
	 * </code>
	 * <p>
	 * This mechanism cannot be delegated to a CompositeWidgetProcessor, because WidgetProcessors
	 * must be immutable, and we want to allow event handlers that are non-static anonymous inner
	 * classes.
	 * <p>
	 * There, we use a List of WidgetProcessors. Note ordering of WidgetProcessors is significant.
	 */

	public List<WidgetProcessor<W, M>> getWidgetProcessors() {

		configureOnce();
		return mWidgetProcessors;
	}

	public void setWidgetProcessors( List<WidgetProcessor<W, M>> widgetProcessors ) {

		mWidgetProcessors = widgetProcessors;
	}

	public void addWidgetProcessor( WidgetProcessor<W, M> widgetProcessor ) {

		configureOnce();

		if ( mWidgetProcessors == null ) {
			mWidgetProcessors = new ArrayList<WidgetProcessor<W, M>>();
		} else if ( mWidgetProcessors.contains( widgetProcessor ) ) {
			throw WidgetProcessorException.newException( "List of WidgetProcessors already contains " + widgetProcessor.getClass().getName() );
		}

		mWidgetProcessors.add( widgetProcessor );
	}

	public void removeWidgetProcessor( WidgetProcessor<W, M> widgetProcessor ) {

		configureOnce();

		if ( mWidgetProcessors == null ) {
			return;
		}

		mWidgetProcessors.remove( widgetProcessor );
	}

	public Layout<W, C, M> getLayout() {

		configureOnce();

		return mLayout;
	}

	/**
	 * Set the Layout to use for the Metawidget.
	 */

	public void setLayout( Layout<W, C, M> layout ) {

		mLayout = layout;
	}

	/**
	 * Inspect the given Object according to the given path, and return the result as a String
	 * conforming to inspection-result-1.0.xsd.
	 * <p>
	 * This method mirrors the <code>Inspector</code> interface. Internally it looks up the
	 * Inspector to use. It is a useful hook for subclasses wishing to inspect different Objects
	 * using our same <code>Inspector</code>.
	 * <p>
	 * In addition, this method runs the <code>InspectionResultProcessors</code>.
	 */

	public String inspect( Object toInspect, String type, String... names ) {

		E element = inspectAsDom( toInspect, type, names );
		return elementToString( element );
	}

	/**
	 * Inspect the given Object according to the given path, and return the result as a String
	 * conforming to inspection-result-1.0.xsd.
	 * <p>
	 * This method mirrors the <code>DomInspector</code> interface. Internally it looks up the
	 * Inspector to use. It is a useful hook for subclasses wishing to inspect different Objects
	 * using our same <code>Inspector</code>.
	 * <p>
	 * In addition, this method runs the <code>InspectionResultProcessors</code>.
	 */

	public E inspectAsDom( Object toInspect, String type, String... names ) {

		configureOnce();

		if ( mInspector == null ) {
			throw new NullPointerException( "No inspector configured" );
		}

		Object inspectionResult;

		if ( mInspector instanceof DomInspector<?> ) {
			inspectionResult = ( (DomInspector<?>) mInspector ).inspectAsDom( toInspect, type, names );
		} else {
			inspectionResult = mInspector.inspect( toInspect, type, names );
		}

		if ( inspectionResult == null ) {
			return null;
		}

		return processInspectionResult( inspectionResult );
	}

	/**
	 * Build widgets from the given XML inspection result.
	 * <p>
	 * Note: the <code>BasePipeline</code> expects the XML to be passed in externally, rather than
	 * fetching it itself, because some XML inspections may be asynchronous.
	 */

	public void buildWidgets( E inspectionResult )
		throws Exception {

		configureOnce();
		startBuild();

		if ( inspectionResult != null ) {
			// Build simple widget (from the top-level element)

			E element = getChildAt( inspectionResult, 0 );

			// Sanity check

			String elementName = getElementName( element );

			if ( !ENTITY.equals( elementName ) ) {
				throw MetawidgetException.newException( "Top-level element name should be " + ENTITY + ", not " + elementName );
			}

			// Metawidget-wide read-only

			Map<String, String> attributes = getAttributesAsMap( element );

			if ( isReadOnly() ) {
				attributes.put( READ_ONLY, TRUE );
			}

			// Build top-level widget.
			//
			// This includes invoking all WidgetBuilders, such as OverriddenWidgetBuilder. It is a
			// little counter-intuitive that there can ever be an override of the top-level element.
			// However, if we go down the path that builds a single widget (eg. doesn't invoke
			// buildCompoundWidget), then our child is at the same top-level as us, and there are
			// some scenarios (like Java Server Faces POST backs) where we need to re-identify that

			W widget = buildWidget( ENTITY, attributes );

			// If mWidgetBuilder.buildWidget returns null, try buildCompoundWidget (from our child
			// elements)

			if ( widget == null ) {
				buildCompoundWidget( element );
			} else {
				widget = processWidget( widget, ENTITY, attributes );

				if ( widget != null ) {
					layoutWidget( widget, ENTITY, attributes );
				}
			}
		}

		// Even if no inspectors match, we still call startBuild()/endBuild() because:
		//
		// 1. you can use a Metawidget purely for layout, with no inspection
		// 2. it makes us behave better in visual builder tools when dropping child widgets in

		endBuild();
	}

	/**
	 * Copies this pipeline's values into another pipeline. Useful for when a Metawidget creates a
	 * nested Metawidget.
	 * <p>
	 * Special behaviour is:
	 * <ul>
	 * <li>the given pipeline has setReadOnly if the current pipeline has setReadOnly <em>or</em> if
	 * the attributes map contains <code>READ_ONLY</code></li>
	 * <li>the given pipeline is initialised with a maximumInspectionDepth of 1 less than the
	 * current maximumInspectionDepth. This is so that, as nesting continues, eventually the
	 * maximumInspectionDepth reaches zero</li>
	 * <li>the given pipeline is initialised with the same Inspectors, InspectionResultProcessors,
	 * WidgetBuilders, WidgetProcessors and Layouts as the current pipeline. This is safe because
	 * they are all immutable</li>
	 * </ul>
	 */

	public void initNestedPipeline( BasePipeline<W, C, E, M> nestedPipeline, Map<String, String> attributes ) {

		nestedPipeline.setReadOnly( isReadOnly() || TRUE.equals( attributes.get( READ_ONLY ) ) );
		nestedPipeline.setMaximumInspectionDepth( getMaximumInspectionDepth() - 1 );

		// Inspectors, WidgetBuilders, WidgetProcessors and Layouts can be shared because they are
		// immutable

		nestedPipeline.setInspector( getInspector() );
		nestedPipeline.setWidgetBuilder( getWidgetBuilder() );
		nestedPipeline.setWidgetProcessors( getWidgetProcessors() );
		nestedPipeline.setLayout( getLayout() );
	}

	//
	// Protected methods
	//

	/**
	 * Build a compound widget by iterating through children of the given element, calling
	 * <code>buildWidget</code> and <code>addWidget</code> on each.
	 */

	protected void buildCompoundWidget( E element )
		throws Exception {

		for ( int loop = 0, length = getChildCount( element ); loop < length; loop++ ) {
			E child = getChildAt( element, loop );

			if ( child == null ) {
				continue;
			}

			Map<String, String> attributes = getAttributesAsMap( child );

			String childName = attributes.get( NAME );

			if ( childName == null || "".equals( childName ) ) {
				throw new Exception( "Child element #" + loop + " of '" + attributes.get( TYPE ) + "' has no @" + NAME );
			}

			// Metawidget as a whole may have had setReadOnly( true )

			boolean forcedReadOnly = false;

			if ( !TRUE.equals( attributes.get( READ_ONLY ) ) && isReadOnly() ) {
				attributes.put( READ_ONLY, TRUE );
				forcedReadOnly = true;
			}

			String elementName = getElementName( child );
			W widget = buildWidget( elementName, attributes );

			if ( widget == null ) {
				if ( mMaximumInspectionDepth <= 0 ) {
					continue;
				}

				// If setReadOnly( true ), remove our forced attribute so the nestedMetawidget
				// can differentiate whether it was forced or in the inspector XML

				if ( forcedReadOnly ) {
					attributes.remove( READ_ONLY );
				}

				widget = buildNestedMetawidget( attributes );
			}

			Map<String, String> additionalAttributes = getAdditionalAttributes( widget );

			if ( additionalAttributes != null ) {
				attributes.putAll( additionalAttributes );
			}

			widget = processWidget( widget, elementName, attributes );

			// A WidgetProcessor could return null to cancel the widget

			if ( widget == null ) {
				continue;
			}

			layoutWidget( widget, elementName, attributes );
		}
	}

	//
	// Protected abstract methods
	//

	protected abstract E stringToElement( String xml );

	/**
	 * Serialize the given element to an XML String.
	 *
	 * @param element
	 *            the element to serialize. May be null.
	 */

	protected abstract String elementToString( E element );

	protected abstract int getChildCount( E element );

	/**
	 * @return the child at the given index. If the given child is not an Element, return null.
	 */

	protected abstract E getChildAt( E element, int index );

	protected abstract String getElementName( E element );

	protected abstract Map<String, String> getAttributesAsMap( E element );

	protected abstract void configure();

	protected void startBuild() {

		M pipelineOwner = getPipelineOwner();

		if ( mWidgetProcessors != null ) {
			for ( WidgetProcessor<W, M> widgetProcessor : mWidgetProcessors ) {
				if ( widgetProcessor instanceof AdvancedWidgetProcessor<?, ?> ) {
					( (AdvancedWidgetProcessor<W, M>) widgetProcessor ).onStartBuild( pipelineOwner );
				}
			}
		}

		// (layout can be null if no path, in an IDE visual builder)

		if ( mLayout instanceof AdvancedLayout<?, ?, ?> ) {
			AdvancedLayout<W, C, M> advancedLayout = (AdvancedLayout<W, C, M>) mLayout;

			advancedLayout.onStartBuild( pipelineOwner );
			advancedLayout.startContainerLayout( pipelineOwner, pipelineOwner );
		}
	}

	/**
	 * @param inspectionResult
	 *            may be a String of XML, or an E, depending on whether the Inspector was a
	 *            DomInspector
	 */

	protected E processInspectionResult( Object inspectionResult ) {

		Object inspectionResultToProcess = inspectionResult;

		if ( mInspectionResultProcessors != null ) {
			M pipelineOwner = getPipelineOwner();

			for ( InspectionResultProcessor<M> inspectionResultProcessor : mInspectionResultProcessors ) {
				if ( inspectionResultProcessor instanceof DomInspectionResultProcessor<?, ?> ) {
					if ( inspectionResultToProcess instanceof String ) {
						inspectionResultToProcess = stringToElement( (String) inspectionResultToProcess );
					}
					@SuppressWarnings( "unchecked" )
					DomInspectionResultProcessor<E, M> domInspectionResultProcessor = (DomInspectionResultProcessor<E, M>) inspectionResultProcessor;
					@SuppressWarnings( "unchecked" )
					E inspectionResultToProcessElement = (E) inspectionResultToProcess;
					inspectionResultToProcess = domInspectionResultProcessor.processInspectionResultAsDom( inspectionResultToProcessElement, pipelineOwner );
				} else {
					if ( !( inspectionResultToProcess instanceof String ) ) {
						@SuppressWarnings( "unchecked" )
						E inspectionResultToProcessElement = (E) inspectionResultToProcess;
						inspectionResultToProcess = elementToString( inspectionResultToProcessElement );
					}
					inspectionResultToProcess = inspectionResultProcessor.processInspectionResult( (String) inspectionResultToProcess, pipelineOwner );
				}

				// An InspectionResultProcessor could return null to cancel the inspection

				if ( inspectionResultToProcess == null ) {
					return null;
				}
			}
		}

		if ( inspectionResultToProcess instanceof String ) {
			return stringToElement( (String) inspectionResultToProcess );
		}

		@SuppressWarnings( "unchecked" )
		E processedInspectionResult = (E) inspectionResultToProcess;
		return processedInspectionResult;
	}

	/**
	 * Returns additional attributes associated with the widget.
	 * <p>
	 * At the very least, this method should be implemented to support returning additional
	 * attributes from stubs.
	 *
	 * @return the additional attributes. May be null
	 */

	protected abstract Map<String, String> getAdditionalAttributes( W widget );

	protected W buildWidget( String elementName, Map<String, String> attributes ) {

		if ( mWidgetBuilder == null ) {
			return null;
		}

		return mWidgetBuilder.buildWidget( elementName, attributes, getPipelineOwner() );
	}

	/**
	 * Process the built widget.
	 */

	protected W processWidget( W widget, String elementName, Map<String, String> attributes ) {

		W processedWidget = widget;

		if ( mWidgetProcessors != null ) {
			M pipelineOwner = getPipelineOwner();

			for ( WidgetProcessor<W, M> widgetProcessor : mWidgetProcessors ) {
				processedWidget = widgetProcessor.processWidget( processedWidget, elementName, attributes, pipelineOwner );

				// A WidgetProcessor could return null to cancel the widget

				if ( processedWidget == null ) {
					return null;
				}
			}
		}

		return processedWidget;
	}

	protected abstract M buildNestedMetawidget( Map<String, String> attributes )
		throws Exception;

	protected abstract M getPipelineOwner();

	/**
	 * Lays out the built and processed widget.
	 */

	protected void layoutWidget( W widget, String elementName, Map<String, String> attributes ) {

		M pipelineOwner = getPipelineOwner();

		mLayout.layoutWidget( widget, elementName, attributes, pipelineOwner, pipelineOwner );
	}

	protected void endBuild() {

		M pipelineOwner = getPipelineOwner();

		if ( mWidgetProcessors != null ) {
			for ( WidgetProcessor<W, M> widgetProcessor : mWidgetProcessors ) {
				if ( widgetProcessor instanceof AdvancedWidgetProcessor<?, ?> ) {
					( (AdvancedWidgetProcessor<W, M>) widgetProcessor ).onEndBuild( pipelineOwner );
				}
			}
		}

		// (layout can be null if no path, in an IDE visual builder)

		if ( mLayout instanceof AdvancedLayout<?, ?, ?> ) {
			AdvancedLayout<W, C, M> advancedLayout = (AdvancedLayout<W, C, M>) mLayout;

			advancedLayout.endContainerLayout( pipelineOwner, pipelineOwner );
			advancedLayout.onEndBuild( pipelineOwner );
		}
	}
}
