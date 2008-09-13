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

package org.metawidget.util;

import java.util.Stack;

/**
 * Utilities for working with Threads
 *
 * @author Richard Kennard
 */

public final class ThreadUtils
{
	//
	// Public statics
	//

	/**
	 * Type-safe initializer.
	 */

	public static final <K> ThreadLocal<K> newThreadLocal()
	{
		return new ThreadLocal<K>();
	}

	/**
	 * Type-safe initializer.
	 */

	public static final <K> ReentrantThreadLocal<K> newReentrantThreadLocal()
	{
		return new ReentrantThreadLocal<K>();
	}

	//
	// Inner class
	//

	/**
	 * Variant of ThreadLocal that maintains a Stack for supporting re-entrant code.
	 * <p>
	 * Useful for giving code that is both stateless <em>and</em> re-entrant (eg. JSF Renderers),
	 * an ability to have state.
	 *
	 * @author Richard Kennard
	 */

	public static class ReentrantThreadLocal<T>
	{
		//
		//
		// Private members
		//
		//

		private ThreadLocal<Stack<T>>	mLocal	= new ThreadLocal<Stack<T>>()
												{
													@Override
													protected Stack<T> initialValue()
													{
														return new Stack<T>();
													}
												};

		//
		//
		// Public methods
		//
		//

		public void push()
		{
			mLocal.get().push( initialValue() );
		}

		public T get()
		{
			Stack<T> stack = mLocal.get();

			if ( stack.isEmpty() )
				stack.push( initialValue() );

			return stack.peek();
		}

		public void set( T t )
		{
			Stack<T> stack = mLocal.get();

			if ( !stack.isEmpty() )
				stack.pop();

			stack.push( t );
		}

		public void pop()
		{
			mLocal.get().pop();
		}

		//
		//
		// Protected methods
		//
		//

		protected T initialValue()
		{
			return null;
		}
	}

	//
	// Private constructor
	//

	private ThreadUtils()
	{
		// Can never be called
	}
}
