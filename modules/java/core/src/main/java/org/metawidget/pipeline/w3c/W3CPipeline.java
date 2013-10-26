// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.pipeline.w3c;

import java.util.Map;

import org.metawidget.config.iface.ConfigReader;
import org.metawidget.config.impl.BaseConfigReader;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.pipeline.base.BasePipeline;
import org.metawidget.util.XmlUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Pipeline for platforms that support <code>org.w3c.dom</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class W3CPipeline<W, C extends W, M extends C>
	extends BasePipeline<W, C, Element, M> {

	//
	// Private statics
	//

	private static ConfigReader	DEFAULT_CONFIG_READER;

	//
	// Private methods
	//

	private ConfigReader		mConfigReader;

	private Object				mConfig;

	//
	// Public methods
	//

	/**
	 * Gets the current <code>ConfigReader</code>, or creates a default one if one hasn't been set.
	 * <p>
	 * Subclasses wishing to set a different default should call <code>setConfigReader</code>. Care
	 * should be taken to <em>reuse</em> the same <code>ConfigReader</code> instance as much as
	 * possible, to maximize caching.
	 */

	public final ConfigReader getConfigReader() {

		if ( mConfigReader == null ) {
			if ( DEFAULT_CONFIG_READER == null ) {
				DEFAULT_CONFIG_READER = new BaseConfigReader();
			}

			mConfigReader = DEFAULT_CONFIG_READER;
		}

		return mConfigReader;
	}

	public void setConfigReader( ConfigReader configReader ) {

		mConfigReader = configReader;
	}

	/**
	 * Reference to the configuration file. Typically this is a Resource path (e.g.
	 * <code>com/myapp/metawidget.xml</code>), but can also be an id (e.g. for Android).
	 */

	public Object getConfig() {

		return mConfig;
	}

	public void setConfig( Object config ) {

		mConfig = config;
		setNeedsConfiguring();
	}

	/**
	 * Returns the first InspectionResultProcessor in this pipeline's list of
	 * InspectionResultProcessors (ie. as added by <code>addInspectionResultProcessor</code>) that
	 * the given class <code>isAssignableFrom</code>.
	 * <p>
	 * This method is here, rather than in <code>BasePipeline</code>, because even though
	 * <code>GwtPipeline</code> overrides it the GWT compiler still chokes on the
	 * <code>isAssignableFrom</code>.
	 *
	 * @param inspectionResultProcessorClass
	 *            the class, or interface or superclass, to find. Returns <code>null</code> if no
	 *            such InspectionResultProcessor
	 * @param <T>
	 *            the type of the InspectionResultProcessor. Note this needn't be a subclass of
	 *            <code>InspectionResultProcessor</code>
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getInspectionResultProcessor( Class<T> inspectionResultProcessorClass ) {

		configureOnce();

		if ( getInspectionResultProcessors() == null ) {
			return null;
		}

		for ( InspectionResultProcessor<M> inspectionResultProcessor : getInspectionResultProcessors() ) {
			if ( inspectionResultProcessorClass.isAssignableFrom( inspectionResultProcessor.getClass() ) ) {
				return (T) inspectionResultProcessor;
			}
		}

		return null;
	}

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
	 * @param <T>
	 *            the type of the WidgetProcessor. Note this needn't be a subclass of
	 *            <code>WidgetProcessor</code>. It may be some orthagonal interface (like
	 *            <code>org.metawidget.faces.component.widgetprocessor.ConverterProcessor</code>)
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getWidgetProcessor( Class<T> widgetProcessorClass ) {

		configureOnce();

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
	 * Overridden to support custom ConfigReaders.
	 */

	@Override
	public void initNestedPipeline( BasePipeline<W, C, Element, M> nestedPipeline, Map<String, String> attributes ) {

		( (W3CPipeline<W, C, M>) nestedPipeline ).setConfigReader( getConfigReader() );
		super.initNestedPipeline( nestedPipeline, attributes );
	}

	//
	// Protected methods
	//

	@Override
	protected void configure() {

		if ( mConfig != null ) {
			getConfigReader().configure( (String) mConfig, getPipelineOwner() );
		}

		configureDefaults();
	}

	/**
	 * @return the resource path to the default configuration file, or null if there is no default
	 *         configuration.
	 */

	protected abstract String getDefaultConfiguration();

	/**
	 * Configure a default Inspector (<code>setInspector</code>),
	 * list of InspectionResultProcessors (<code>setInspectionResultProcessors</code>),
	 * WidgetBuilder (<code>setWidgetBuilder</code>), list of
	 * WidgetProcessors (<code>setWidgetProcessors</code>) and a Layout (<code>setLayout</code>).
	 */

	protected void configureDefaults() {

		String defaultConfiguration = getDefaultConfiguration();

		if ( defaultConfiguration != null ) {

			ConfigReader configReader = getConfigReader();

			if ( getInspector() == null ) {
				configReader.configure( defaultConfiguration, getPipelineOwner(), "inspector" );
			}

			if ( getInspectionResultProcessors() == null ) {
				configReader.configure( defaultConfiguration, getPipelineOwner(), "inspectionResultProcessors" );
			}

			if ( getWidgetBuilder() == null ) {
				configReader.configure( defaultConfiguration, getPipelineOwner(), "widgetBuilder" );
			}

			if ( getWidgetProcessors() == null ) {
				configReader.configure( defaultConfiguration, getPipelineOwner(), "widgetProcessors" );
			}

			if ( getLayout() == null ) {
				configReader.configure( defaultConfiguration, getPipelineOwner(), "layout" );
			}
		}
	}

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
	protected Element getFirstChildElement( Element parent ) {

		return XmlUtils.getFirstChildElement( parent );
	}

	@Override
	protected Element getNextSiblingElement( Element element ) {

		return XmlUtils.getNextSiblingElement( element );
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
