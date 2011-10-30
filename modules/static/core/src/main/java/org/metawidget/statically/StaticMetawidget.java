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

package org.metawidget.statically;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.metawidget.config.ConfigReader;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.layout.iface.Layout;
import org.metawidget.pipeline.w3c.W3CPipeline;
import org.metawidget.statically.StaticUtils.IndentedWriter;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.PathUtils.TypeAndNames;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.w3c.dom.Element;

/**
 * Base Metawidget for static code generation.
 * <p>
 * Although Metawidget focuses on <em>runtime</em> UI generation, there are a category of
 * applications that require static code generation instead. It is still possible to make full use
 * of Metawidget's 5 stage pipeline (inspectors, inspection result processors, widget builders,
 * widget processors, layouts) during static code generation.
 * <p>
 * An important requirement for static code generation is the Metawidget should not rely on the
 * technology being generated being available. For example, a static JSF Metawidget should not rely
 * on <code>FacesContext.getCurrentInstance</code>. Architectually, this makes all static
 * Metawidgets more similar to each other than to their corresponding runtime version. For example,
 * the static JSF Metawidget is more similar to the static Swing Metawidget than it is to the
 * runtime JSF Metawidget. Therefore, it makes sense to have a <code>StaticMetawidget</code> base
 * class.
 *
 * @author Richard Kennard
 */

public abstract class StaticMetawidget
	extends BaseStaticWidget {

	//
	// Private statics
	//

	private static final ConfigReader									CONFIG_READER	= new ConfigReader();

	//
	// Private members
	//

	/**
	 * Path to inspect.
	 */

	private String														mPath;

	/**
	 * Prefix of path to inspect, to support nesting.
	 */

	private String														mPathPrefix;

	private String														mConfig;

	private W3CPipeline<StaticWidget, StaticWidget, StaticMetawidget>	mPipeline;

	private Writer														mWriter			= new StringWriter();

	//
	// Constructor
	//

	public StaticMetawidget() {

		mPipeline = newPipeline();
	}

	//
	// Public methods
	//

	public void setPath( String path ) {

		mPath = path;
	}

	public String getPath() {

		return mPath;
	}

	public String getPathPrefix() {

		return mPathPrefix;
	}

	public void setConfig( String config ) {

		mConfig = config;
		mPipeline.setNeedsConfiguring();
	}

	public String getLabelString( Map<String, String> attributes ) {

		if ( attributes == null ) {
			return "";
		}

		// Explicit label

		String label = attributes.get( LABEL );

		if ( label != null ) {
			// (may be forced blank)

			if ( "".equals( label ) ) {
				return null;
			}

			return label.trim();
		}

		// Default name

		String name = attributes.get( NAME );

		if ( name != null ) {
			return StringUtils.uncamelCase( name );
		}

		return "";
	}

	public boolean isReadOnly() {

		return mPipeline.isReadOnly();
	}

	public void setReadOnly( boolean readOnly ) {

		mPipeline.setReadOnly( readOnly );
	}

	public void setInspector( Inspector inspector ) {

		mPipeline.setInspector( inspector );
	}

	public void setInspectionResultProcessors( InspectionResultProcessor<StaticMetawidget>... inspectionResultProcessors ) {

		mPipeline.setInspectionResultProcessors( inspectionResultProcessors );
	}

	public void setWidgetBuilder( WidgetBuilder<StaticWidget, StaticMetawidget> widgetBuilder ) {

		mPipeline.setWidgetBuilder( widgetBuilder );
	}

	public void setWidgetProcessors( WidgetProcessor<StaticWidget, StaticMetawidget>... widgetProcessors ) {

		mPipeline.setWidgetProcessors( widgetProcessors );
	}

	public void setLayout( Layout<StaticWidget, StaticWidget, StaticMetawidget> layout ) {

		mPipeline.setLayout( layout );
	}

	@Override
	public void write( Writer writer ) {

		write( writer, 0 );
	}

	/**
	 * Write the Metawidget output using the given Writer.
	 *
	 * @param initialIndent
	 *            the initialIndent that will be applied to every line
	 * @param indentFirstLine
	 *            whether to indent the first line by initialIndent
	 */

	public void write( Writer writer, int initialIndent ) {

		mWriter = new IndentedWriter( writer, initialIndent );
		mPipeline.configureOnce();

		try {
			mPipeline.buildWidgets( inspect() );
		} catch ( Exception e ) {
			throw MetawidgetException.newException( e );
		}
	}

	public Writer getWriter() {

		return mWriter;
	}

	//
	// Protected methods
	//

	/**
	 * Instantiate the Pipeline used by this Metawidget.
	 * <p>
	 * Subclasses wishing to use their own Pipeline should override this method to instantiate their
	 * version.
	 */

	protected W3CPipeline<StaticWidget, StaticWidget, StaticMetawidget> newPipeline() {

		return new Pipeline();
	}

	protected void initNestedMetawidget( StaticMetawidget nestedMetawidget, Map<String, String> attributes ) {

		// Don't reconfigure...

		nestedMetawidget.setConfig( null );

		// ...instead, copy runtime values

		mPipeline.initNestedPipeline( nestedMetawidget.mPipeline, attributes );
		nestedMetawidget.setPath( mPath + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME ) );
	}

	protected Element inspect() {

		if ( mPath == null ) {
			return null;
		}

		TypeAndNames typeAndNames = PathUtils.parsePath( mPath, StringUtils.SEPARATOR_FORWARD_SLASH_CHAR );
		String type = typeAndNames.getType();

		return mPipeline.inspectAsDom( null, type, typeAndNames.getNamesAsArray() );
	}

	protected void configure() {

		try {
			if ( mConfig != null ) {
				CONFIG_READER.configure( mConfig, this );
			}

			// SwingMetawidget uses setMetawidgetLayout, not setLayout

			if ( mPipeline.getLayout() == null ) {
				CONFIG_READER.configure( getDefaultConfiguration(), this, "metawidgetLayout" );
			}

			mPipeline.configureDefaults( CONFIG_READER, getDefaultConfiguration(), StaticMetawidget.class );
		} catch ( Exception e ) {
			throw MetawidgetException.newException( e );
		}
	}

	protected abstract String getDefaultConfiguration();

	//
	// Inner class
	//

	protected class Pipeline
		extends W3CPipeline<StaticWidget, StaticWidget, StaticMetawidget> {

		//
		// Protected methods
		//

		@Override
		protected void configure() {

			StaticMetawidget.this.configure();
		}

		@Override
		protected Map<String, String> getAdditionalAttributes( StaticWidget tag ) {

			return null;
		}

		@Override
		protected StaticMetawidget buildNestedMetawidget( final Map<String, String> attributes )
			throws Exception {

			StaticMetawidget metawidgetTag = StaticMetawidget.this.getClass().newInstance();
			StaticMetawidget.this.initNestedMetawidget( metawidgetTag, attributes );

			return metawidgetTag;
		}

		@Override
		protected StaticMetawidget getPipelineOwner() {

			return StaticMetawidget.this;
		}
	}
}
