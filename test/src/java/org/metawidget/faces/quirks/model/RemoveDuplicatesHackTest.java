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

package org.metawidget.faces.quirks.model;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiSection;

/**
 * Models an entity that tests RemoveDuplicatesHack.
 *
 * @author Richard Kennard
 */

public class RemoveDuplicatesHackTest
{
	//
	// Private members
	//

	private String							mFoo1;

	private RemoveDuplicatesHackEmbedded	mEmbedded;

	//
	// Public methods
	//

	public String getFoo1()
	{
		return mFoo1;
	}

	public void setFoo1( String foo1 )
	{
		mFoo1 = foo1;
	}

	@UiComesAfter( "foo1" )
	public RemoveDuplicatesHackEmbedded getEmbedded()
	{
		return mEmbedded;
	}

	public void setEmbedded( RemoveDuplicatesHackEmbedded embedded )
	{
		mEmbedded = embedded;
	}

	public void clearEmbedded()
	{
		setEmbedded( null );
	}

	public void newEmbedded()
	{
		setEmbedded( new RemoveDuplicatesHackEmbedded() );
	}

	//
	// Public methods
	//

	public class RemoveDuplicatesHackEmbedded
	{
		//
		// Private members
		//

		private String	mBar1;

		private String	mBar2;

		private String	mBar3;

		//
		// Public methods
		//

		public String getBar1()
		{
			return mBar1;
		}

		public void setBar1( String bar1 )
		{
			mBar1 = bar1;
		}

		@UiSection( "More" )
		public String getBar2()
		{
			return mBar2;
		}

		public void setBar2( String bar2 )
		{
			mBar2 = bar2;
		}

		public String getBar3()
		{
			return mBar3;
		}

		public void setBar3( String bar3 )
		{
			mBar3 = bar3;
		}
	}
}
