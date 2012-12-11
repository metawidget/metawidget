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

package org.metawidget.config.iface;

import java.io.InputStream;

import org.metawidget.iface.Immutable;

/**
 * Reads <code>metadata.xml</code> files and configures Metawidgets.
 * <p>
 * In spirit, <code>metadata.xml</code> is a general-purpose mechanism for configuring JavaBeans
 * based on XML files. In practice, there are some Metawidget-specific features such as:
 * <p>
 * <ul>
 * <li>support for reusing immutable objects (as defined by <code>isImmutable</code>)</li>
 * <li>caching XML input based on resource name (uses <code>XmlUtils.CachingContextHandler</code>)</li>
 * <li>resolving resources from specialized locations, such as under <code>WEB-INF</code> using
 * <code>ServletContext.getResource</code> (<code>ConfigReader</code> implements
 * <code>ResourceResolver</code>)</li>
 * </ul>
 * <p>
 * This mechanism is not just static methods, because ConfigReaders need to be able to be subclassed
 * (eg. <code>ServletConfigReader</code>)
 * <h3>Important</h3>
 * <p>
 * <code>ConfigReader</code>'s support for reusing immutable objects (eg. <code>JpaInspector</code>)
 * that use config objects (eg. <code>JpaInspectorConfig</code>) is dependant on the config object
 * overriding <code>equals</code> and <code>hashCode</code>. <strong>Failure to override these
 * methods may result in your object not being reused, or being reused inappropriately</strong>.
 *
 * @author Richard Kennard
 */

public interface ConfigReader
	extends Immutable {

	//
	// Methods
	//

	/**
	 * Read configuration from an application resource.
	 * <p>
	 * This version of <code>configure</code> uses <code>openResource</code> to open the specified
	 * resource. It assumes the resource name is a unique key, so subsequent calls do not need to
	 * re-open the resource, or re-parse it, making this version of <code>configure</code> much
	 * faster than <code>configure( InputStream, Object )</code>.
	 * <p>
	 * This version further caches any immutable objects, in the same way as
	 * <code>configure( InputStream, Object )</code> (see the JavaDoc for that method).
	 *
	 * @param resource
	 *            resource name that will be looked up using openResource
	 * @param toConfigure
	 *            object to configure. Can be a subclass of the one actually in the resource
	 * @param names
	 *            path to a property within the object. If specified, siblings to this path will be
	 *            ignored. This allows ConfigReader to be used to initialise only a specific part of
	 *            an object
	 */

	Object configure( String resource, Object toConfigure, String... names );

	/**
	 * Read configuration from an input stream.
	 * <p>
	 * This version of <code>configure</code> caches any immutable objects (as determined by
	 * <code>isImmutable</code>) and reuses them for subsequent calls. This helps ensure there is
	 * only ever one instance of a, say, <code>Inspector</code> or <code>WidgetBuilder</code>.
	 * <p>
	 * If the Object to configure is a <code>Class</code>, this method will create and return an
	 * instance of that class based on the configuration file. For example, if the configuration
	 * file is...
	 * <p>
	 * <code>
	 * &lt;metawidget&gt;<br/>
	 * &nbsp;&nbsp;&nbsp;&lt;myInspector config="myConfig"&gt;<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;someConfigParameter/&gt;<br/>
	 * &nbsp;&nbsp;&nbsp;&lt;/myInspector&gt;<br/>
	 * &lt;/metawidget&gt;
	 * </code>
	 * <p>
	 * ...then the code...
	 * <p>
	 * <code>
	 * Inspector myInspector = myConfigReader.configure( stream, Inspector.class );
	 * </code>
	 * <p>
	 * ...will create a <code>MyInspector</code> configured with <code>someConfigParameter</code>.
	 * <p>
	 * Conversely, if the Object to configure is already an instance, this method will configure the
	 * instance. For example if the configuration file is...
	 * <p>
	 * <code>
	 * &lt;metawidget&gt;<br/>
	 * &nbsp;&nbsp;&nbsp;&lt;swingMetawidget&gt;<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;opaque&gt;&lt;boolean&gt;true&lt;/boolean&gt;&lt;/opaque&gt;<br/>
	 * &nbsp;&nbsp;&nbsp;&lt;/swingMetawidget&gt;<br/>
	 * &lt;/metawidget&gt;
	 * </code>
	 * <p>
	 * ...then the code...
	 * <p>
	 * <code>
	 * JPanel panel = new JPanel();
	 * myConfigReader.configure( stream, panel );
	 * </code>
	 * <p>
	 * ...will call <code>setOpaque</code> on the given <code>JPanel</code>.
	 *
	 * @param stream
	 *            XML input as a stream
	 * @param toConfigure
	 *            object to configure. Can be a subclass of the one actually in the resource
	 * @param names
	 *            path to a property within the object. If specified, siblings to this path will be
	 *            ignored. This allows ConfigReader to be used to initialise only a specific part of
	 *            an object
	 */

	Object configure( InputStream stream, Object toConfigure, String... names );
}
