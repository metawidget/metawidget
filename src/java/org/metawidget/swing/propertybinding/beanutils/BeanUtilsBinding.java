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

package org.metawidget.swing.propertybinding.beanutils;

import java.awt.Component;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.metawidget.MetawidgetException;
import org.metawidget.inspector.InspectionResultConstants;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.propertybinding.BasePropertyBinding;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Property binding implementation based on BeansUtils.
 * <p>
 * This implementation recognizes the following <code>SwingMetawidget.setParameter</code>
 * parameters:
 * <p>
 * <ul>
 * <li><code>propertyStyle</code> - either <code>PROPERTYSTYLE_JAVABEAN</code> (default) or
 * <code>PROPERTYSTYLE_SCALA</code> (for Scala-style getters and setters).
 * </ul>
 *
 * @author Richard Kennard, Stefan Ackermann
 */

public class BeanUtilsBinding
	extends BasePropertyBinding
{
	//
	// Public statics
	//

	public final static int		PROPERTYSTYLE_JAVABEAN	= 0;

	public final static int		PROPERTYSTYLE_SCALA		= 1;

	//
	// Private members
	//

	private Set<SavedBinding>	mBindings;

	private int					mPropertyStyle			= PROPERTYSTYLE_JAVABEAN;

	//
	// Constructor
	//

	public BeanUtilsBinding( SwingMetawidget metawidget )
	{
		super( metawidget );

		// Read parameters

		Integer propertyStyle = (Integer) metawidget.getParameter( "propertyStyle" );

		if ( propertyStyle != null )
			mPropertyStyle = propertyStyle;
	}

	//
	// Public methods
	//

	@Override
	public void bindProperty( Component component, Map<String, String> attributes, String path )
	{
		String componentProperty = getMetawidget().getValueProperty( component );

		if ( componentProperty == null )
			return;

		try
		{
			String sourceBinding = PathUtils.parsePath( path ).getNames().replace( StringUtils.SEPARATOR_FORWARD_SLASH_CHAR, StringUtils.SEPARATOR_DOT_CHAR );
			Object sourceValue;

			try
			{
				sourceValue = retrieveValueFromObject( getMetawidget().getToInspect(), sourceBinding );
			}
			catch ( NoSuchMethodException e )
			{
				throw MetawidgetException.newException( "Property '" + sourceBinding + "' has no getter" );
			}
			String readonlykey = InspectionResultConstants.READ_ONLY;
			boolean readonly = attributes.containsKey( readonlykey ) && attributes.get( readonlykey ).equals( "true" );
			SavedBinding binding = new SavedBinding( component, componentProperty, sourceBinding, readonly );
			saveValueToWidget( binding, sourceValue );

			if ( mBindings == null )
				mBindings = CollectionUtils.newHashSet();

			mBindings.add( binding );
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
	}

	@Override
	public void rebindProperties()
	{
		if ( mBindings == null )
			return;

		try
		{
			Object sourceObject = getMetawidget().getToInspect();

			for ( SavedBinding binding : mBindings )
			{
				Object sourceValue;
				String sourceBinding = binding.getSourceBinding();

				try
				{
					sourceValue = retrieveValueFromObject( sourceObject, sourceBinding );
				}
				catch ( NoSuchMethodException e )
				{
					throw MetawidgetException.newException( "Property '" + sourceBinding + "' has no getter" );
				}

				saveValueToWidget( binding, sourceValue );
			}
		}
		catch ( Exception e )
		{
			throw MetawidgetException.newException( e );
		}
	}

	@Override
	public void saveProperties()
	{
		if ( mBindings == null )
			return;

		try
		{
			Object source = getMetawidget().getToInspect();

			for ( SavedBinding binding : mBindings )
			{
				if ( binding.isReadOnly() )
					continue;

				Object componentValue = retrieveValueFromWidget( binding );
				saveValueToObject( source, binding, componentValue );
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
	// Protected methods
	//

	protected Object retrieveValueFromObject( Object sourceObject, String sourceBinding )
		throws Exception
	{
		switch ( mPropertyStyle )
		{
			case PROPERTYSTYLE_SCALA:
				// TODO: implement me!
				return null;

			default:
				return PropertyUtils.getProperty( sourceObject, sourceBinding );
		}
	}

	private void saveValueToObject( Object source, SavedBinding binding, Object componentValue )
		throws Exception
	{
		switch ( mPropertyStyle )
		{
			case PROPERTYSTYLE_SCALA:
				// TODO: implement me!
				break;

			default:
				BeanUtils.setProperty( source, binding.getSourceBinding(), componentValue );
		}
	}

	protected Object retrieveValueFromWidget( SavedBinding binding )
		throws Exception
	{
		Object componentValue = PropertyUtils.getProperty( binding.getComponent(), binding.getComponentProperty() );
		return componentValue;
	}

	protected void saveValueToWidget( SavedBinding binding, Object sourceValue )
		throws Exception
	{
		BeanUtils.setProperty( binding.getComponent(), binding.getComponentProperty(), sourceValue );
	}

	//
	// Inner class
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

		private boolean		mReadOnly;

		//
		//
		// Constructor
		//
		//

		public SavedBinding( Component component, String componentProperty, String sourceBinding, boolean readonly )
		{
			mComponent = component;
			mComponentProperty = componentProperty;
			mSourceBinding = sourceBinding;
			mReadOnly = readonly;
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

		public boolean isReadOnly()
		{
			return mReadOnly;
		}
	}
}
