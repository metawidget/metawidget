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

package org.metawidget.inspector.impl;

import java.util.regex.Pattern;

import org.metawidget.inspector.InspectorException;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.util.ClassUtils;

/**
 * Base class for AbstractPojoInspector configurations.
 * <p>
 * Handles configuring the pattern to recognize proxied classes.
 *
 * @author Richard Kennard
 */

public class AbstractPropertyInspectorConfig
{
	//
	//
	// Private members
	//
	//

	private Pattern							mProxyPattern	= ClassUtils.DEFAULT_PROXY_PATTERN;

	private Class<? extends PropertyStyle>	mPropertyStyle	= JavaBeanPropertyStyle.class;

	//
	//
	// Public methods
	//
	//

	/**
	 * Sets the pattern used to recognize proxied classes.
	 * <p>
	 * Proxy detection is done by regular expression String-matching on the classname. This avoids
	 * classpath dependencies on proxy libraries. The default proxy pattern recognizes CGLIB and
	 * Javassist proxies by matching <code>ByCGLIB\$\$|_\$\$_javassist_</code>.
	 */

	public void setProxyPattern( Pattern proxyPattern )
	{
		mProxyPattern = proxyPattern;
	}

	Pattern getProxyPattern()
	{
		return mProxyPattern;
	}

	/**
	 * Sets the style used to recognize properties. Defaults to <code>JavaBeanPropertyStyle</code>.
	 */

	public void setPropertyStyle( Class<? extends PropertyStyle> propertyStyleClass )
	{
		mPropertyStyle = propertyStyleClass;
	}

	/**
	 * Gets the style used to instantiate properties.
	 * <p>
	 * We have the <code>AbstractPropertyInspectorConfig</code> instantiate the PropertyStyle,
	 * rather than the <code>AbstractPropertyInspector</code>, to provide a 'hook' for
	 * configurable PropertyStyles.
	 */

	PropertyStyle getPropertyStyle()
	{
		try
		{
			return mPropertyStyle.newInstance();
		}
		catch ( Exception e )
		{
			throw InspectorException.newException( e );
		}
	}
}
