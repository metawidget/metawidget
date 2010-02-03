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

package org.metawidget.gwt.client.widgetprocessor.binding.simple;

import java.util.Date;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Richard Kennard
 */

public class SimpleBindingProcessorTest
	extends GWTTestCase
{
	//
	// Public methods
	//

	@Override
	public String getModuleName()
	{
		return "org.metawidget.gwt.GwtMetawidgetTest";
	}

	public void testConfig()
	{
		SimpleBindingProcessorConfig config1 = new SimpleBindingProcessorConfig();
		SimpleBindingProcessorConfig config2 = new SimpleBindingProcessorConfig();

		assertTrue( !config1.equals( "foo" ) );
		assertTrue( !config1.equals( new SimpleBindingProcessorConfig()
		{
			// Subclass
		} ) );
		assertTrue( config1.equals( config1 ) );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// adapter

		SimpleBindingProcessorAdapter<Date> adapter = new SimpleBindingProcessorAdapter<Date>()
		{
			@Override
			public Object getProperty( Date object, String... property )
			{
				return null;
			}

			@Override
			public Class<?> getPropertyType( Date object, String... property )
			{
				return null;
			}

			@Override
			public void invokeAction( Date object, String... action )
			{
				// Do nothing
			}

			@Override
			public void setProperty( Date object, Object value, String... property )
			{
				// Do nothing
			}
		};

		assertTrue( null == config1.getAdapters() );
		config1.setAdapter( Date.class, adapter );
		assertTrue( !config1.equals( config2 ) );
		config2.setAdapter( Date.class, adapter );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		assertTrue( 1 == config1.getAdapters().size() );
		assertTrue( adapter == config1.getAdapters().values().iterator().next() );

		// converter

		Converter<Date> converter = new Converter<Date>()
		{
			@Override
			public Object convertForWidget( Widget widget, Date value )
			{
				return null;
			}

			@Override
			public Date convertFromWidget( Widget widget, Object value, Class<?> intoClass )
			{
				return null;
			}
		};

		assertTrue( null == config1.getConverters() );
		config1.setConverter( Date.class, converter );
		assertTrue( !config1.equals( config2 ) );
		config2.setConverter( Date.class, converter );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		assertTrue( 1 == config1.getConverters().size() );
		assertTrue( converter == config1.getConverters().values().iterator().next() );
	}
}
