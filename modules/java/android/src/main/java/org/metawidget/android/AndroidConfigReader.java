// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.android;

import java.io.InputStream;

import org.metawidget.config.iface.ResourceResolver;
import org.metawidget.config.impl.BaseConfigReader;
import org.metawidget.iface.MetawidgetException;

import android.content.Context;
import android.content.res.Resources;

/**
 * Specialized ConfigReader for Android.
 * <p>
 * Resolves references by using <code>Context.getResources</code> first. Resource strings should be
 * of the form <code>@com.foo:raw/metawidget_metadata</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class AndroidConfigReader
	extends BaseConfigReader {

	//
	// Private members
	//

	private Context	mContext;

	//
	// Constructor
	//

	public AndroidConfigReader( final Context context ) {

		super( new ResourceResolver() {

			/**
			 * Overridden to try <code>Context.getResources</code> first.
			 * <p>
			 * Resource strings should be of the form <code>@com.foo:raw/metawidget_metadata</code>.
			 */

			public InputStream openResource( String resource ) {

				if ( !resource.startsWith( "@" ) ) {
					throw MetawidgetException.newException( "Resource name does not start with '@': " + resource );
				}

				Resources resources = context.getResources();
				int id = resources.getIdentifier( resource, null, null );

				if ( id == 0 ) {
					throw MetawidgetException.newException( "Resource.getIdentifier returns 0 for " + resource );
				}

				return resources.openRawResource( id );
			}
		} );

		mContext = context;
	}

	//
	// Protected methods
	//

	/**
	 * Overridden to process <code>int</code>s using <code>Resources.getIdentifier</code>
	 * <p>
	 * Resource strings should be of the form <code>@style/section</code>.
	 */

	@Override
	protected Object createNative( String name, Class<?> namespace, String recordedText )
		throws Exception {

		if ( "int".equals( name ) && recordedText.startsWith( "@" ) ) {
			Resources resources = mContext.getResources();
			int id = resources.getIdentifier( recordedText, null, null );

			if ( id == 0 ) {
				throw MetawidgetException.newException( "Resource.getIdentifier returns 0 for " + recordedText );
			}

			return id;
		}

		return super.createNative( name, namespace, recordedText );
	}
}
