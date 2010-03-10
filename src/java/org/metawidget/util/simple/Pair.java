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

package org.metawidget.util.simple;


/**
 * Simple implementation of a tuple pair.
 *
 * @author Richard Kennard
 */

public class Pair<L, R>
{
	//
	// Private members
	//

	private L	mLeft;

	private R	mRight;

	//
	// Constructor
	//

	public Pair( L left, R right )
	{
		mLeft = left;
		mRight = right;
	}

	//
	// Public methods
	//

	public L getLeft()
	{
		return mLeft;
	}

	public R getRight()
	{
		return mRight;
	}

	@Override
	public boolean equals( Object that )
	{
		if ( this == that )
			return true;

		if ( that == null )
			return false;

		if ( getClass() != that.getClass() )
			return false;

		if ( !ObjectUtils.nullSafeEquals( mLeft, ( (Pair<?, ?>) that ).mLeft ) )
			return false;

		if ( !ObjectUtils.nullSafeEquals( mRight, ( (Pair<?, ?>) that ).mRight ) )
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mLeft );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mRight );

		return hashCode;
	}

	@Override
	public String toString()
	{
		return mLeft + ":" + mRight;
	}
}
