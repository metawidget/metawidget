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

package org.metawidget.integrationtest.swt.tutorial;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class Person {

	private String	mName;

	private int		mAge;

	private boolean	mRetired;

	public String getName() {

		return mName;
	}

	public void setName( String name ) {

		mName = name;
	}

	public int getAge() {

		return mAge;
	}

	public void setAge( int age ) {

		mAge = age;
	}

	public boolean isRetired() {

		return mRetired;
	}

	public void setRetired( boolean retired ) {

		mRetired = retired;
	}
}
