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

package org.metawidget.android;

import java.io.InputStream;

import org.metawidget.config.ConfigReader;
import org.metawidget.iface.MetawidgetException;

import android.content.Context;
import android.content.res.Resources;

/**
 * Specialized ConfigReader for Android.
 * <p>
 * Resolves references by using <code>Context.getResources</code> first. Resource strings should be
 * of the form <code>@com.foo:raw/metawidget_metadata</code>.
 *
 * @author Richard Kennard
 */

public class AndroidConfigReader
	extends ConfigReader
{
	//
	// Private members
	//

	private Context	mContext;

	//
	// Constructor
	//

	public AndroidConfigReader( Context context )
	{
		mContext = context;
	}

	//
	// Public methods
	//

	/**
	 * Overridden to try <code>Context.getResources</code> first.
	 * <p>
	 * Resource strings should be of the form <code>@com.foo:raw/metawidget_metadata</code>.
	 */

	@Override
	public InputStream openResource( String resource )
	{
		if ( !resource.startsWith( "@" ) )
			throw MetawidgetException.newException( "Resource name does not start with '@': " + resource );

		Resources resources = mContext.getResources();
		int id = resources.getIdentifier( resource, null, null );

		if ( id == 0 )
			throw MetawidgetException.newException( "Resource.getIdentifier returns 0 for " + resource );

		return resources.openRawResource( id );
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
		throws Exception
	{
		if ( "int".equals( name ) && recordedText.startsWith( "@" ))
		{
			Resources resources = mContext.getResources();
			int id = resources.getIdentifier( recordedText, null, null );

			if ( id == 0 )
				throw MetawidgetException.newException( "Resource.getIdentifier returns 0 for " + recordedText );

			return id;
		}

		return super.createNative( name, namespace, recordedText );
	}
}
