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

package org.metawidget.swing.widgetprocessor.binding.beanutils;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetprocessor.binding.BindingConverter;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;
import org.metawidget.widgetprocessor.impl.BaseWidgetProcessor;

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

public class BeanUtilsBindingProcessor
	extends BaseWidgetProcessor<JComponent, SwingMetawidget>
	implements BindingConverter
{
	//
	// Private statics
	//

	private final static String	SCALA_SET_SUFFIX		= "_$eq";

	//
	// Public statics
	//

	public final static int		PROPERTYSTYLE_JAVABEAN	= 0;

	public final static int		PROPERTYSTYLE_SCALA		= 1;

	//
	// Public methods
	//

	@Override
	public void onAdd( JComponent component, String elementName, Map<String, String> attributes, SwingMetawidget metawidget )
	{
		// Unwrap JScrollPanes (for JTextAreas etc)

		JComponent componentToBind = component;

		if ( componentToBind instanceof JScrollPane )
			componentToBind = (JComponent) ( (JScrollPane) componentToBind ).getViewport().getView();

		// Determine value property

		String componentProperty = metawidget.getValueProperty( componentToBind );

		if ( componentProperty == null )
			return;

		String path = metawidget.getPath();

		if ( PROPERTY.equals( elementName ) )
			path += StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME );

		try
		{
			// Convert 'com.Foo/bar/baz' into BeanUtils notation 'bar.baz'

			String names = PathUtils.parsePath( path, StringUtils.SEPARATOR_FORWARD_SLASH_CHAR ).getNames().replace( StringUtils.SEPARATOR_FORWARD_SLASH_CHAR, StringUtils.SEPARATOR_DOT_CHAR );

			Object sourceValue;

			try
			{
				sourceValue = retrieveValueFromObject( metawidget, metawidget.getToInspect(), names );
			}
			catch ( NoSuchMethodException e )
			{
				throw WidgetProcessorException.newException( "Property '" + names + "' has no getter" );
			}

			SavedBinding binding = new SavedBinding( componentToBind, componentProperty, names, TRUE.equals( attributes.get( NO_SETTER ) ) );
			saveValueToWidget( binding, sourceValue );

			State state = getState( metawidget );

			if ( state.bindings == null )
				state.bindings = CollectionUtils.newHashSet();

			state.bindings.add( binding );
		}
		catch ( Exception e )
		{
			throw WidgetProcessorException.newException( e );
		}
	}

	/**
	 * Rebinds the Metawidget to the given Object.
	 * <p>
	 * This method is an optimization that allows clients to load a new object into the binding
	 * <em>without</em> calling setToInspect, and therefore without reinspecting the object or
	 * recreating the components. It is the client's responsbility to ensure the setToRebind object
	 * is compatible with the original setToInspect.
	 * <p>
	 * Note this method does not call <code>setToInspect</code>, so the rebound object cannot
	 * be retrieved using <code>getToInspect</code>. Rather, clients should use <code>getToRebind</code>.
	 */

	public void setToRebind( Object toRebind, SwingMetawidget metawidget )
	{
		State state = getState( metawidget );
		state.toRebind = toRebind;

		// Our bindings

		if ( state.bindings != null )
		{
			try
			{
				for ( SavedBinding binding : state.bindings )
				{
					Object sourceValue;
					String names = binding.getNames();

					try
					{
						sourceValue = retrieveValueFromObject( metawidget, toRebind, names );
					}
					catch ( NoSuchMethodException e )
					{
						throw WidgetProcessorException.newException( "Property '" + names + "' has no getter" );
					}

					saveValueToWidget( binding, sourceValue );
				}
			}
			catch ( Exception e )
			{
				throw WidgetProcessorException.newException( e );
			}
		}

		// Nested bindings

		for ( Component component : metawidget.getComponents() )
		{
			if ( component instanceof SwingMetawidget )
				setToRebind( toRebind, (SwingMetawidget) component );
		}
	}

	public Object getToRebind( SwingMetawidget metawidget )
	{
		return getState( metawidget ).toRebind;
	}

	public void save( SwingMetawidget metawidget )
	{
		State state = getState( metawidget );

		// Our bindings

		if ( state.bindings != null )
		{
			try
			{
				for ( SavedBinding binding : state.bindings )
				{
					if ( !binding.isSettable() )
						continue;

					Object componentValue = retrieveValueFromWidget( binding );
					saveValueToObject( metawidget, binding.getNames(), componentValue );
				}
			}
			catch ( Exception e )
			{
				throw WidgetProcessorException.newException( e );
			}
		}

		// Nested bindings

		for ( Component component : metawidget.getComponents() )
		{
			if ( component instanceof SwingMetawidget )
				save( (SwingMetawidget) component );
		}
	}

	public Object convertFromString( String value, Class<?> expectedType )
	{
		return ConvertUtils.convert( value, expectedType );
	}

	//
	// Protected methods
	//

	/**
	 * Retrieve value identified by the given names from the given source.
	 * <p>
	 * Clients may override this method to incorporate their own getter convention.
	 */

	protected Object retrieveValueFromObject( SwingMetawidget metawidget, Object source, String names )
		throws Exception
	{
		switch ( getPropertyStyle( metawidget ) )
		{
			case PROPERTYSTYLE_SCALA:
				return scalaTraverse( source, false, names.split( "\\" + StringUtils.SEPARATOR_DOT_CHAR ) );

			default:
				return PropertyUtils.getProperty( source, names );
		}
	}

	/**
	 * Save the given value into the given source at the location specified by the given names.
	 * <p>
	 * Clients may override this method to incorporate their own setter convention.
	 *
	 * @param componentValue
	 *            the raw value from the <code>JComponent</code>
	 */

	protected void saveValueToObject( SwingMetawidget metawidget, String names, Object componentValue )
		throws Exception
	{
		State state = getState( metawidget );
		Object source = state.toRebind;

		if ( source == null )
			source = metawidget.getToInspect();

		switch ( getPropertyStyle( metawidget ) )
		{
			case PROPERTYSTYLE_SCALA:

				// Traverse to the setter...

				String[] namesAsArray = names.split( "\\" + StringUtils.SEPARATOR_DOT_CHAR );
				Object parent = scalaTraverse( source, true, namesAsArray );

				if ( parent == null )
					return;

				// ...determine its type...

				Class<?> parentClass = parent.getClass();
				String lastName = namesAsArray[namesAsArray.length - 1];
				Class<?> propertyType = parentClass.getMethod( lastName ).getReturnType();

				// ...convert if necessary (BeanUtils.setProperty usually does this for us)...

				Object convertedValue = ConvertUtils.convert( componentValue, propertyType );

				// ...and set it

				Method writeMethod = parentClass.getMethod( lastName + SCALA_SET_SUFFIX, propertyType );
				writeMethod.invoke( parent, convertedValue );
				break;

			default:
				BeanUtils.setProperty( source, names, componentValue );
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

	private int getPropertyStyle( SwingMetawidget metawidget )
	{
		// Read parameters

		Integer propertyStyle = metawidget.getParameter( "propertyStyle" );

		if ( propertyStyle != null )
			return propertyStyle;

		return PROPERTYSTYLE_JAVABEAN;
	}

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
			// Scala getter methods are just 'foo()', not 'getFoo()'

			Method readMethod = traverse.getClass().getMethod( names[loop] );
			traverse = readMethod.invoke( traverse );

			// Can go no further?

			if ( traverse == null )
				break;
		}

		return traverse;
	}

	private State getState( SwingMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( BeanUtilsBindingProcessor.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( BeanUtilsBindingProcessor.class, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */class State
	{
		/* package private */ Set<SavedBinding>	bindings;

		/* package private */ Object				toRebind;
	}

	class SavedBinding
	{
		//
		//
		// Private members
		//
		//

		private Component	mComponent;

		private String		mComponentProperty;

		private String		mNames;

		private boolean		mNoSetter;

		//
		//
		// Constructor
		//
		//

		public SavedBinding( Component component, String componentProperty, String names, boolean noSetter )
		{
			mComponent = component;
			mComponentProperty = componentProperty;
			mNames = names;
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

		/**
		 * Property names into the source object.
		 * <p>
		 * Stored in BeanUtils style <code>foo.bar.baz</code>.
		 */

		public String getNames()
		{
			return mNames;
		}

		public boolean isSettable()
		{
			return !mNoSetter;
		}
	}
}
