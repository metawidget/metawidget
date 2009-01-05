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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.metawidget.MetawidgetException;
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
			Object sourceValue;

			try
			{
				sourceValue = retrieveValueFromObject( getMetawidget().getToInspect(), path );
			}
			catch ( NoSuchMethodException e )
			{
				throw MetawidgetException.newException( "Property '" + path + "' has no getter" );
			}

			SavedBinding binding = new SavedBinding( component, componentProperty, path, TRUE.equals( attributes.get( NO_SETTER ) ) );
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
				String path = binding.getPath();

				try
				{
					sourceValue = retrieveValueFromObject( sourceObject, path );
				}
				catch ( NoSuchMethodException e )
				{
					throw MetawidgetException.newException( "Property '" + path + "' has no getter" );
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
				if ( !binding.isSettable() )
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

	protected Object retrieveValueFromObject( Object source, String path )
		throws Exception
	{
		switch ( mPropertyStyle )
		{
			case PROPERTYSTYLE_SCALA:
				String[] names = PathUtils.parsePath( path, StringUtils.SEPARATOR_FORWARD_SLASH_CHAR ).getNamesAsArray();
				return scalaTraverse( source, false, names );

			default:
				String beanPath = PathUtils.parsePath( path, StringUtils.SEPARATOR_FORWARD_SLASH_CHAR ).getNames().replace( StringUtils.SEPARATOR_FORWARD_SLASH_CHAR, StringUtils.SEPARATOR_DOT_CHAR );
				return PropertyUtils.getProperty( source, beanPath );
		}
	}

	protected void saveValueToObject( Object source, SavedBinding binding, Object componentValue )
		throws Exception
	{
		switch ( mPropertyStyle )
		{
			case PROPERTYSTYLE_SCALA:

				// Traverse to the setter...

				String[] names = PathUtils.parsePath( binding.getPath(), StringUtils.SEPARATOR_FORWARD_SLASH_CHAR ).getNamesAsArray();
				Object parent = scalaTraverse( source, true, names );

				if ( parent == null )
					return;

				// ...determine its type...

				Class<?> parentClass = parent.getClass();
				String name = names[names.length - 1];
				Class<?> propertyType = parentClass.getMethod( name ).getReturnType();

				// ...convert if necessary (BeanUtils.setProperty usually does this for us)...

				Object convertedValue = ConvertUtils.convert( componentValue, propertyType );

				// ...and set it

				Method writeMethod = parentClass.getMethod( name + "_$eq", propertyType );
				writeMethod.invoke( parent, convertedValue );
				break;

			default:
				String beanPath = PathUtils.parsePath( binding.getPath(), StringUtils.SEPARATOR_FORWARD_SLASH_CHAR ).getNames().replace( StringUtils.SEPARATOR_FORWARD_SLASH_CHAR, StringUtils.SEPARATOR_DOT_CHAR );
				BeanUtils.setProperty( source, beanPath, componentValue );
		}
	}

	protected Object retrieveValueFromWidget( SavedBinding binding )
		throws Exception
	{
		return PropertyUtils.getProperty( binding.getComponent(), binding.getComponentProperty() );
	}

	protected void saveValueToWidget( SavedBinding binding, Object sourceValue )
		throws Exception
	{
		BeanUtils.setProperty( binding.getComponent(), binding.getComponentProperty(), sourceValue );
	}

	//
	// Private methods
	//

	private Object scalaTraverse( Object toTraverse, boolean onlyToParent, String... names )
		throws Exception
	{
		// Sanity check

		if ( toTraverse == null )
			return null;

		// Traverse through names (if any)

		if ( names == null )
			return toTraverse;

		int length = names.length;

		if ( length == 0 )
			return toTraverse;

		// Only to parent?

		if ( onlyToParent )
			length--;

		// Do the traversal

		Object traverse = toTraverse;

		for ( int loop = 0; loop < length; loop++ )
		{
			Method readMethod = traverse.getClass().getMethod( names[loop] );
			traverse = readMethod.invoke( traverse );

			// Can go no further?

			if ( traverse == null )
				break;
		}

		return traverse;
	}

	//
	// Inner class
	//

	protected class SavedBinding
	{
		//
		//
		// Private members
		//
		//

		private Component	mComponent;

		private String		mComponentProperty;

		private String		mPath;

		private boolean		mNoSetter;

		//
		//
		// Constructor
		//
		//

		public SavedBinding( Component component, String componentProperty, String path, boolean noSetter )
		{
			mComponent = component;
			mComponentProperty = componentProperty;
			mPath = path;
			mNoSetter = noSetter;
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

		public String getPath()
		{
			return mPath;
		}

		public boolean isSettable()
		{
			return !mNoSetter;
		}
	}
}
