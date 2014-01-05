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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
