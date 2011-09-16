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

package org.metawidget.swing;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import junit.framework.TestCase;

import org.metawidget.config.ConfigReader;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class SwingMetawidgetConcurrencyTest
	extends TestCase {

	//
	// Public methods
	//

	public void testConcurrentStartup()
		throws Exception {

		final List<Exception> concurrencyFailures = CollectionUtils.newArrayList();

		// Try a few times (just to make sure)...

		for ( int tryAFewTimes = 0; tryAFewTimes < 10; tryAFewTimes++ ) {

			// ...create a ConfigReader with a clean cache...

			final ConfigReader configReader = new ConfigReader();

			// ...prepare some Threads...

			final CountDownLatch startSignal = new CountDownLatch( 1 );
			final CountDownLatch doneSignal = new CountDownLatch( 50 );

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

			// ...and run them all simultaneously

			startSignal.countDown();
			doneSignal.await();

			if ( !concurrencyFailures.isEmpty() ) {
				throw concurrencyFailures.get( 0 );
			}
		}

		assertTrue( concurrencyFailures.isEmpty() );
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
