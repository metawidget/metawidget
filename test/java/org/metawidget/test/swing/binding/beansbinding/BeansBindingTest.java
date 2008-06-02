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

package org.metawidget.test.swing.binding.beansbinding;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.metawidget.inspector.property.PropertyInspector;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.binding.beansbinding.BeansBinding;
import org.metawidget.swing.binding.beansbinding.ReadOnlyToStringConverter;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class BeansBindingTest
	extends TestCase
{
	//
	//
	// Public statics
	//
	//

	public static void testBinding()
		throws Exception
	{
		// Model

		Foo foo = new Foo();
		foo.setBar( 42 );

		// Inspect

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setBindingClass( BeansBinding.class );
		metawidget.setInspector( new PropertyInspector() );
		metawidget.setToInspect( foo );

		// Test UpdateStrategy.READ_ONCE

		JSpinner spinner = (JSpinner) metawidget.getComponent( 1 );
		assertTrue( 42 == (Integer) spinner.getValue() );
		JLabel label = (JLabel) metawidget.getComponent( 3 );
		assertTrue( "4".equals( label.getText() ) );

		// Test UpdateStrategy.READ

		metawidget.setParameter( UpdateStrategy.class, UpdateStrategy.READ );

		spinner = (JSpinner) metawidget.getComponent( 1 );
		foo.setBar( 43 );
		assertTrue( 43 == (Integer) spinner.getValue() );
		spinner.setValue( 44 );
		assertTrue( 43 == foo.getBar() );
		metawidget.save();
		assertTrue( 44 == foo.getBar() );

		// Test UpdateStrategy.READ_WRITE

		metawidget.setParameter( UpdateStrategy.class, UpdateStrategy.READ_WRITE );

		spinner = (JSpinner) metawidget.getComponent( 1 );
		spinner.setValue( 45 );
		assertTrue( 45 == foo.getBar() );

		// Test unbind

		metawidget.setBindingClass( null );
	}

	public static void testSingleComponentBinding()
		throws Exception
	{
		// Model

		Foo foo = new Foo();
		foo.setBar( 42 );

		// Inspect

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setBindingClass( BeansBinding.class );
		metawidget.setParameter( UpdateStrategy.class, UpdateStrategy.READ_WRITE );
		metawidget.setInspector( new PropertyInspector() );
		metawidget.setLayoutClass( null );
		metawidget.setToInspect( foo );
		metawidget.setPath( Foo.class.getName() + "/bar" );

		JSpinner spinner = (JSpinner) metawidget.getComponent( 0 );
		assertTrue( 42 == (Integer) spinner.getValue() );
		spinner.setValue( 43 );
		assertTrue( 43 == foo.getBar() );
	}

	public static void testDefaultTableBinding()
		throws Exception
	{
		// Model

		Foo foo = new Foo();

		// Inspect active

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setBindingClass( BeansBinding.class );
		metawidget.setInspector( new PropertyInspector() );
		metawidget.setToInspect( foo );

		assertTrue( metawidget.getComponent( 1 ) instanceof JSpinner );
		JTable table = (JTable) ( (JScrollPane) metawidget.getComponent( 5 ) ).getViewport().getView();
		assertTrue( table.getModel().getRowCount() == 2 );

		// Inspect read-only

		metawidget.setReadOnly( true );

		assertTrue( metawidget.getComponent( 1 ) instanceof JLabel );
		table = (JTable) ( (JScrollPane) metawidget.getComponent( 5 ) ).getViewport().getView();
		assertTrue( table.getModel().getRowCount() == 2 );
	}

	public static void testReadOnlyToStringConverter()
		throws Exception
	{
		// Model

		ReadOnlyToStringConverter<Boolean> converter = new ReadOnlyToStringConverter<Boolean>();

		assertTrue( "true".equals( converter.convertForward( Boolean.TRUE ) ) );

		try
		{
			converter.convertReverse( "true" );
			assertTrue( false );
		}
		catch ( UnsupportedOperationException e )
		{
			assertTrue( e.getMessage().indexOf( "cannot convertReverse" ) != -1 );
		}
	}

	public static void testConvert()
		throws Exception
	{
		// Model

		BeansBinding binding = new BeansBinding( new SwingMetawidget() );
		BeansBinding.registerConverter( String.class, int.class, new IntConverter() );

		assertTrue( 1 == binding.convertFromString( "1", int.class ) );
	}

	//
	//
	// Constructor
	//
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public BeansBindingTest( String name )
	{
		super( name );
	}

	//
	//
	// Inner class
	//
	//

	protected static class Foo
	{
		//
		//
		// Private members
		//
		//

		private final PropertyChangeSupport	mPropertyChangeSupport	= new PropertyChangeSupport( this );

		private int							mBar;

		private int							mBaz					= 4;

		private List<String>				mList					= CollectionUtils.unmodifiableList( "element1", "element2" );

		//
		//
		// Public methods
		//
		//

		public int getBar()
		{
			return mBar;
		}

		public void setBar( int bar )
		{
			int oldBar = mBar;
			mBar = bar;
			mPropertyChangeSupport.firePropertyChange( "bar", oldBar, mBar );
		}

		public int getBaz()
		{
			return mBaz;
		}

		public void addPropertyChangeListener( PropertyChangeListener listener )
		{
			mPropertyChangeSupport.addPropertyChangeListener( listener );
		}

		public void removePropertyChangeListener( PropertyChangeListener listener )
		{
			mPropertyChangeSupport.removePropertyChangeListener( listener );
		}

		public List<String> getList()
		{
			return mList;
		}

		public void setList( List<String> list )
		{
			mList = list;
		}
	}

	protected static class IntConverter
		extends Converter<String, Integer>
	{
		@Override
		public Integer convertForward( String value )
		{
			return Integer.valueOf( value );
		}

		@Override
		public String convertReverse( Integer value )
		{
			return String.valueOf( value );
		}
	}
}
