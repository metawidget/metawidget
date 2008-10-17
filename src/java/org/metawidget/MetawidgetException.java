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

package org.metawidget;

/**
 * Any exception that occurs during Metawidget widget creation.
 *
 * @author Richard Kennard
 */

public class MetawidgetException
	extends RuntimeException
{
	//
	// Private statics
	//

	private final static long	serialVersionUID	= 1l;

	//
	// Public statics
	//

	/**
	 * Static constructor.
	 * <p>
	 * Using static constructor methods prevents unnecessarily nesting MetawidgetExceptions
	 * within MetawidgetExceptions.
	 */

	public static MetawidgetException newException( Exception exception )
	{
		if ( exception instanceof MetawidgetException )
			return (MetawidgetException) exception;

		return new MetawidgetException( exception );
	}

	/**
	 * Static constructor.
	 * <p>
	 * For consistency with <code>MetawidgetException.newException( Exception )</code>.
	 */

	public static MetawidgetException newException( String message )
	{
		return new MetawidgetException( message );
	}

	/**
	 * Static constructor.
	 * <p>
	 * For consistency with <code>MetawidgetException.newException( Exception )</code>.
	 */

	public static MetawidgetException newException( String message, Exception exception )
	{
		return new MetawidgetException( message, exception );
	}

	//
	// Constructor
	//

	private MetawidgetException( String message )
	{
		super( message );
	}

	private MetawidgetException( Exception exception )
	{
		super( exception );
	}

	private MetawidgetException( String message, Exception exception )
	{
		super( message, exception );
	}
}
