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

import org.metawidget.config.iface.ConfigReader;
import org.metawidget.config.impl.BaseConfigReader;
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

	//
	// Protected methods
	//

	/**
	 * Gets the current <code>ConfigReader</code>, or creates a default one if one hasn't been set.
	 */

	protected final ConfigReader getConfigReader() {

		if ( mConfigReader == null ) {
			if ( DEFAULT_CONFIG_READER == null ) {
				DEFAULT_CONFIG_READER = new BaseConfigReader();
			}

			mConfigReader = DEFAULT_CONFIG_READER;
		}

		return mConfigReader;
	}

	@Override
	protected void configure() {

		ConfigReader configReader = getConfigReader();

		if ( mConfig != null ) {
			configReader.configure( (String) mConfig, getPipelineOwner() );
		}

		configureDefaults();
	}

	protected abstract String getDefaultConfiguration();

	/**
	 * Uses <code>ConfigReader</code> to configure a default Inspector ( <code>setInspector</code>),
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
