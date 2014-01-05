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

package org.metawidget.swing;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import junit.framework.TestCase;

import org.metawidget.config.iface.ConfigReader;
import org.metawidget.config.impl.BaseConfigReader;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwingMetawidgetConcurrencyTest
	extends TestCase {

	//
	// Public methods
	//

	public void testConcurrentStartup()
		throws Exception {

		// Try a few times (just to make sure)...

		for ( int tryAFewTimes = 0; tryAFewTimes < 100; tryAFewTimes++ ) {

			// ...create a ConfigReader with a clean cache...

			final ConfigReader configReader = new BaseConfigReader();

			// ...prepare some Threads...

			final CountDownLatch startSignal = new CountDownLatch( 1 );
			final CountDownLatch doneSignal = new CountDownLatch( 50 );

			final List<Exception> concurrencyFailures = CollectionUtils.newArrayList();

			for ( int concurrentThreads = 0; concurrentThreads < doneSignal.getCount(); concurrentThreads++ ) {

				new Thread( new Runnable() {

					@Override
					public void run() {

						try {
							startSignal.await();
						} catch ( InterruptedException e ) {
							// (do nothing)
						}

						try {
							configReader.configure( "org/metawidget/swing/metawidget-swing-default.xml", new SwingMetawidget() );
						} catch ( Exception e ) {
							concurrencyFailures.add( e );
							assertTrue( "Concurrency failure: " + e.getClass() + " " + e.getMessage(), false );
						} finally {
							doneSignal.countDown();
						}
					}
				} ).start();
			}

			// ...run them all simultaneously...

			startSignal.countDown();
			doneSignal.await();

			// ...and see if any failed

			if ( !concurrencyFailures.isEmpty() ) {
				throw concurrencyFailures.get( 0 );
			}
		}
	}

	//
	// Inner class
	//

	public static class Person {

		//
		// Private members
		//

		public String	mName;

		public int		mAge;

		public boolean	mRetired;
	}
}
