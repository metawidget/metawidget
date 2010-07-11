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

package org.metawidget.pipeline.w3c;

import java.util.Map;

import org.metawidget.config.ConfigReader;
import org.metawidget.pipeline.base.BasePipeline;
import org.metawidget.util.XmlUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Pipeline for platforms that support <code>org.w3c.dom</code>.
 *
 * @author Richard Kennard
 */

public abstract class W3CPipeline<W, C extends W, M extends C>
	extends BasePipeline<W, C, Element, M> {

	//
	// Public methods
	//

	/**
	 * Returns the first WidgetProcessor in this pipeline's list of WidgetProcessors (ie. as added
	 * by <code>addWidgetProcessor</code>) that the given class <code>isAssignableFrom</code>.
	 * <p>
	 * This method is here, rather than in <code>BasePipeline</code>, because even though
	 * <code>GwtPipeline</code> overrides it the GWT compiler still chokes on the
	 * <code>isAssignableFrom</code>.
	 *
	 * @param widgetProcessorClass
	 *            the class, or interface or superclass, to find. Returns <code>null</code> if no
	 *            such WidgetProcessor
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getWidgetProcessor( Class<T> widgetProcessorClass ) {

		if ( getWidgetProcessors() == null ) {
			return null;
		}

		for ( WidgetProcessor<W, M> widgetProcessor : getWidgetProcessors() ) {
			if ( widgetProcessorClass.isAssignableFrom( widgetProcessor.getClass() ) ) {
				return (T) widgetProcessor;
			}
		}

		return null;
	}

	/**
	 * Uses the given <code>ConfigReader</code> to configure a default Inspector (
	 * <code>setInspector</code>), WidgetBuilder (<code>setWidgetBuilder</code>), list of
	 * WidgetProcessors (<code>setWidgetProcessors</code>) and a Layout (<code>setLayout</code>).
	 *
	 * @param metawidgetClass
	 *            the base class of the Metawidget. This is different from
	 *            getPipelineOwner().getClass(), as that will return the instance (and therefore
	 *            potentially a subclass)
	 */

	public void configureDefaults( ConfigReader configReader, String configuration, Class<M> metawidgetClass ) {

		if ( getInspector() == null ) {
			configReader.configure( configuration, getPipelineOwner(), "inspector" );
		}

		if ( getInspectionResultProcessors() == null ) {
			configReader.configure( configuration, getPipelineOwner(), "inspectionResultProcessors" );
		}

		if ( getWidgetBuilder() == null ) {
			configReader.configure( configuration, getPipelineOwner(), "widgetBuilder" );
		}

		if ( getWidgetProcessors() == null ) {
			configReader.configure( configuration, getPipelineOwner(), "widgetProcessors" );
		}

		if ( getLayout() == null ) {
			configReader.configure( configuration, getPipelineOwner(), "layout" );
		}
	}

	//
	// Protected methods
	//

	@Override
	protected Element stringToElement( String xml ) {

		Document document = XmlUtils.documentFromString( xml );
		return document.getDocumentElement();
	}

	@Override
	protected String elementToString( Element element ) {

		return XmlUtils.nodeToString( element, false );
	}


	@Override
	protected int getChildCount( Element element ) {

		return element.getChildNodes().getLength();
	}

	@Override
	protected Element getChildAt( Element element, int index ) {

		return XmlUtils.getElementAt( element, index );
	}

	@Override
	protected String getElementName( Element element ) {

		return element.getNodeName();
	}

	@Override
	protected Map<String, String> getAttributesAsMap( Element element ) {

		return XmlUtils.getAttributesAsMap( element );
	}
}
