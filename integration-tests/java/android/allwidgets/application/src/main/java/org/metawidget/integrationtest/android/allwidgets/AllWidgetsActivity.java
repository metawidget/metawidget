// Metawidget (licensed under LGPL)
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

package org.metawidget.integrationtest.android.allwidgets;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.widgetprocessor.binding.simple.SimpleBindingProcessor;
import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets;
import org.metawidget.integrationtest.shared.allwidgets.proxy.AllWidgetsProxy.AllWidgets_$$_javassist_1;
import org.metawidget.util.LogUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;

/**
 * @author Richard Kennard
 */

public class AllWidgetsActivity
	extends Activity {

	//
	// Protected members
	//

	protected AllWidgets	mAllWidgets;

	//
	// Public methods
	//

	@Override
	public void onCreate( Bundle bundle ) {

		super.onCreate( bundle );

		mAllWidgets = new AllWidgets_$$_javassist_1();

		// Layout from resource

		setContentView( R.layout.main );

		// Metawidget

		AndroidMetawidget metawidget = (AndroidMetawidget) findViewById( R.id.metawidget );
		metawidget.setToInspect( mAllWidgets );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {

		super.onCreateOptionsMenu( menu );

		menu.add( "Save" );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {

		if ( item.getItemId() == 0 ) {
			AndroidMetawidget metawidget = (AndroidMetawidget) findViewById( R.id.metawidget );

			try {
				// Already saved?

				if ( metawidget.isReadOnly() ) {
					return false;
				}

				// Save

				metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );

				// New, read-only View will be a lot shorter

				metawidget.setReadOnly( true );
				metawidget.buildWidgets();
				( (ScrollView) metawidget.getParent() ).fullScroll( View.FOCUS_UP );
			} catch ( Exception e ) {
				LogUtils.getLog( AllWidgetsActivity.class ).error( "Save error", e );

				String message = e.getMessage();

				if ( message == null || "".equals( message ) ) {
					message = e.getClass().getSimpleName();
				}

				AlertDialog.Builder builder = new AlertDialog.Builder( metawidget.getContext() );
				builder.setTitle( "Save error" );
				builder.setMessage( "Unable to save:\n" + message );
				builder.setPositiveButton( "OK", null );
				builder.show();
			}
		}

		return false;
	}
}
