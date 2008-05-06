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

package org.metawidget.swing.binding.beanutils;

import java.awt.Component;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.metawidget.MetawidgetException;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.binding.Binding;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.StringUtils;

/**
 * Automatic binding implementation based on BeansUtils.
 *
 * @author Richard Kennard
 */

public class BeanUtilsBinding
	extends Binding
{
	//
	//
	// Private members
	//
	//

	private Set<SavedBinding>	mBindings;

	//
	//
	// Constructor
	//
	//

	public BeanUtilsBinding( SwingMetawidget metawidget )
	{
		super( metawidget );
	}

	//
	//
	// Public methods
	//
	//

	@Override
	public void bind( Component component, String componentProperty, String... names )
	{
		if ( componentProperty == null )
			return;

		try
		{
			String sourceBinding = ArrayUtils.toString( names, StringUtils.SEPARATOR_DOT );
			Object sourceValue;

			try
			{
				sourceValue = PropertyUtils.getProperty( getMetawidget().getToInspect(), sourceBinding );
			}
			catch ( NoSuchMethodException e )
			{
				// Fail gracefully

				return;
			}

			BeanUtils.setProperty( component, componentProperty, sourceValue );

			if ( mBindings == null )
				mBindings = CollectionUtils.newHashSet();

			mBindings.add( new SavedBinding( component, componentProperty, sourceBinding ) );
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
	}

	@Override
	public void save()
	{
		if ( mBindings == null )
			return;

		try
		{
			Object source = getMetawidget().getToInspect();

			for ( SavedBinding binding : mBindings )
			{
				Object componentValue = PropertyUtils.getProperty( binding.getComponent(), binding.getComponentProperty() );
				BeanUtils.setProperty( source, binding.getSourceBinding(), componentValue );
			}
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T> T convertFromString( String value, Class<T> type )
	{
		return (T) ConvertUtils.convert( value, type );
	}

	//
	//
	// Inner class
	//
	//

	private final static class SavedBinding
	{
		//
		//
		// Private members
		//
		//

		private Component	mComponent;

		private String		mComponentProperty;

		private String		mSourceBinding;

		//
		//
		// Constructor
		//
		//

		public SavedBinding( Component component, String componentProperty, String sourceBinding )
		{
			mComponent = component;
			mComponentProperty = componentProperty;
			mSourceBinding = sourceBinding;
		}

		//
		//
		// Public methods
		//
		//

		public Component getComponent()
		{
			return mComponent;
		}

		public String getComponentProperty()
		{
			return mComponentProperty;
		}

		public String getSourceBinding()
		{
			return mSourceBinding;
		}
	}
}
