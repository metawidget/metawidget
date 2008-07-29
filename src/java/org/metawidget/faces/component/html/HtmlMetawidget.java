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

package org.metawidget.faces.component.html;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;
import static org.metawidget.inspector.propertytype.PropertyTypeInspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;

/**
 * Metawidget for Java Server Faces environments.
 * <p>
 * Automatically creates native JSF HTML UIComponents, such as <code>HtmlInputText</code> and
 * <code>HtmlSelectOneListbox</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

public class HtmlMetawidget
	extends UIMetawidget
{
	//
	//
	// Protected members
	//
	//

	protected boolean	mCreateHiddenFields;

	//
	//
	// Public methods
	//
	//

	/**
	 * Whether to create hidden HTML input fields for hidden values.
	 * <p>
	 * Defaults to <code>false</code>, as passing values via
	 * <code>&lt;input type="hidden"&gt;</code> tags is a potential security risk: they can be
	 * modified by malicious clients before being returned to the server.
	 */

	public void setCreateHiddenFields( boolean createHiddenFields )
	{
		mCreateHiddenFields = createHiddenFields;
	}

	@Override
	public Object saveState( FacesContext context )
	{
		Object values[] = new Object[2];
		values[0] = super.saveState( context );
		values[1] = mCreateHiddenFields;

		return values;
	}

	@Override
	public void restoreState( FacesContext context, Object state )
	{
		Object values[] = (Object[]) state;
		super.restoreState( context, values[0] );

		mCreateHiddenFields = (Boolean) values[1];
	}

	//
	//
	// Protected methods
	//
	//

	@Override
	protected UIComponent afterBuildWidget( UIComponent component, Map<String, String> attributes )
		throws Exception
	{
		if ( component == null )
			return component;

		// Apply CSS attributes

		UIComponent componentToStyle = component;

		if ( component instanceof UIStub )
			componentToStyle = (UIComponent) component.getChildren().get( 0 );

		@SuppressWarnings( "unchecked" )
		Map<String, String> componentAttributes = componentToStyle.getAttributes();

		@SuppressWarnings( "unchecked" )
		Map<String, String> thisAttributes = getAttributes();
		String style = thisAttributes.get( "style" );

		if ( style != null && !componentAttributes.containsKey( "style" ) )
			componentAttributes.put( "style", style );

		String styleClass = thisAttributes.get( "styleClass" );

		if ( styleClass != null && !componentAttributes.containsKey( "styleClass" ) )
			componentAttributes.put( "styleClass", styleClass );

		return component;
	}

	/**
	 * Purely creates the widget. Does not concern itself with the widget's id, value binding or
	 * preparing metadata for the renderer.
	 *
	 * @return the widget to use in read-only scenarios
	 */

	@Override
	protected UIComponent buildReadOnlyWidget( String elementName, Map<String, String> attributes )
		throws Exception
	{
		Application application = getFacesContext().getApplication();

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
		{
			if ( !mCreateHiddenFields || TRUE.equals( attributes.get( NO_SETTER ) ) )
				return null;

			return application.createComponent( "javax.faces.HtmlInputHidden" );
		}

		// Masked (return a couple of nested Stubs, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
		{
			UIComponent component = application.createComponent( "org.metawidget.Stub" );
			@SuppressWarnings( "unchecked" )
			List<UIComponent> listChildren = component.getChildren();
			listChildren.add( application.createComponent( "org.metawidget.Stub" ) );

			return component;
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
		{
			String lookupLabels = attributes.get( LOOKUP_LABELS );

			if ( lookupLabels == null )
				return createReadOnlyComponent( attributes );

			// Special support for read-only lookups with labels

			HtmlLookupOutputText lookupOutputText = (HtmlLookupOutputText) application.createComponent( "org.metawidget.HtmlLookupOutputText" );
			lookupOutputText.setLabels( CollectionUtils.fromString( lookup ), CollectionUtils.fromString( lookupLabels ) );

			return createReadOnlyComponent( attributes, lookupOutputText );
		}

		String facesLookup = attributes.get( FACES_LOOKUP );

		if ( facesLookup != null && !"".equals( facesLookup ) )
			return createReadOnlyComponent( attributes );

		String type = attributes.get( TYPE );

		// If no type, fail gracefully with a javax.faces.Output

		if ( type == null || "".equals( type ) )
			return createReadOnlyComponent( attributes );

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz != null )
		{
			// Primitives

			if ( clazz.isPrimitive() )
				return createReadOnlyComponent( attributes );

			// Object primitives

			if ( ClassUtils.isPrimitiveWrapper( clazz ) )
				return createReadOnlyComponent( attributes );

			// Dates

			if ( Date.class.isAssignableFrom( clazz ) )
				return createReadOnlyComponent( attributes );

			// Strings

			if ( String.class.equals( clazz ) )
				return createReadOnlyComponent( attributes );

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return createCollectionComponent( clazz, attributes );
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return createReadOnlyComponent( attributes );

		// Nested Metawidget

		return createMetawidget();
	}

	/**
	 * Purely creates the widget. Does not concern itself with the widget's id, value binding or
	 * preparing metadata for the renderer.
	 *
	 * @return the widget to use in non-read-only scenarios
	 */

	@Override
	protected UIComponent buildActiveWidget( String elementName, Map<String, String> attributes )
		throws Exception
	{
		FacesContext context = getFacesContext();
		Application application = context.getApplication();
		String type = attributes.get( TYPE );

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
		{
			if ( !mCreateHiddenFields || TRUE.equals( attributes.get( NO_SETTER ) ) )
				return null;

			return application.createComponent( "javax.faces.HtmlInputHidden" );
		}

		// Overriden component

		UIComponent component = null;
		String componentName = attributes.get( FACES_COMPONENT );

		if ( componentName != null )
			component = application.createComponent( componentName );

		// Action
		// TODO: these double up

		if ( ACTION.equals( elementName ))
		{
			if ( component == null )
				component = application.createComponent( "javax.faces.HtmlCommandButton" );

			((UICommand) component).setValue( getLabelString( context, attributes ) );

			return component;
		}

		// Faces Lookups

		String facesLookup = attributes.get( FACES_LOOKUP );

		if ( facesLookup != null && !"".equals( facesLookup ) )
		{
			if ( component == null )
			{
				component = application.createComponent( "javax.faces.HtmlSelectOneListbox" );
				HtmlSelectOneListbox select = (HtmlSelectOneListbox) component;
				select.setSize( 1 );
			}

			addSelectItems( component, facesLookup, attributes );
			return component;
		}

		// If no type, fail gracefully with a javax.faces.HtmlInputText
		//
		// Note: we don't do this if there is a FACES_LOOKUP, because a FACES_LOOKUP
		// can get away with not specifying a type

		if ( type == null || "".equals( type ) )
			return application.createComponent( "javax.faces.HtmlInputText" );

		Class<?> clazz = ClassUtils.niceForName( type );

		// String Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
		{
			if ( component == null )
			{
				component = application.createComponent( "javax.faces.HtmlSelectOneListbox" );
				HtmlSelectOneListbox select = (HtmlSelectOneListbox) component;
				select.setSize( 1 );
			}

			List<?> values = CollectionUtils.fromString( lookup );

			// Convert values of SelectItems (eg. from Strings to ints)...

			if ( component instanceof ValueHolder )
			{
				ValueHolder valueHolder = (ValueHolder) component;

				// ...using either the specified converter...

				Converter converter = setConverter( component, attributes );

				// ...or an application-wide converter...

				if ( converter == null )
				{
					converter = application.createConverter( clazz );

					if ( converter != null )
						valueHolder.setConverter( converter );
				}

				// ...if any

				if ( converter != null )
				{
					int size = values.size();
					List<Object> convertedValues = CollectionUtils.newArrayList( size );

					for ( int loop = 0; loop < size; loop++ )
					{
						Object convertedValue = converter.getAsObject( context, component, (String) values.get( loop ) );
						convertedValues.add( convertedValue );
					}

					values = convertedValues;
				}
			}

			addSelectItems( component, values, CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ), attributes );
		}

		// Already specified an overriden component?

		if ( component != null )
			return component;

		// Primitives

		if ( boolean.class.equals( clazz ) )
			return application.createComponent( "javax.faces.SelectBoolean" );

		if ( char.class.equals( clazz ) )
		{
			component = application.createComponent( "javax.faces.HtmlInputText" );
			( (HtmlInputText) component ).setMaxlength( 1 );

			return component;
		}

		if ( clazz.isPrimitive() )
			return application.createComponent( "javax.faces.HtmlInputText" );

		// Dates

		if ( Date.class.isAssignableFrom( clazz ) )
			return application.createComponent( "javax.faces.HtmlInputText" );

		// Numbers

		if ( Number.class.isAssignableFrom( clazz ) )
			return application.createComponent( "javax.faces.HtmlInputText" );

		// Booleans (are tri-state)

		if ( Boolean.class.isAssignableFrom( clazz ) )
		{
			component = application.createComponent( "javax.faces.HtmlSelectOneListbox" );

			HtmlSelectOneListbox select = (HtmlSelectOneListbox) component;
			select.setSize( 1 );

			addSelectItem( component, "", null );
			addSelectItem( component, Boolean.TRUE, "Yes" );
			addSelectItem( component, Boolean.FALSE, "No" );

			return component;
		}

		// Strings

		if ( String.class.equals( clazz ) )
		{
			if ( TRUE.equals( attributes.get( MASKED ) ) )
			{
				component = application.createComponent( "javax.faces.HtmlInputSecret" );

				String maximumLength = attributes.get( MAXIMUM_LENGTH );

				if ( maximumLength != null && !"".equals( maximumLength ) )
					( (HtmlInputSecret) component ).setMaxlength( Integer.parseInt( maximumLength ) );

				return component;
			}

			if ( TRUE.equals( attributes.get( "large" ) ) )
				return application.createComponent( "javax.faces.HtmlInputTextarea" );

			component = application.createComponent( "javax.faces.HtmlInputText" );

			String maximumLength = attributes.get( MAXIMUM_LENGTH );

			if ( maximumLength != null && !"".equals( maximumLength ) )
				( (HtmlInputText) component ).setMaxlength( Integer.parseInt( maximumLength ) );

			return component;
		}

		// Collections

		if ( Collection.class.isAssignableFrom( clazz ) )
			return createCollectionComponent( clazz, attributes );

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return application.createComponent( "javax.faces.HtmlInputText" );

		// Nested Metawidget

		return createMetawidget();
	}

	/**
	 * Create a sub-Metawidget.
	 * <p>
	 * Usually, clients will want to create a sub-Metawidget using the same subclass as themselves.
	 * To be 'proper' in JSF, though, we should go via <code>application.createComponent</code>.
	 * Unfortunately a UIComponent does not know its own component name, so subclasses must override
	 * this method.
	 */

	protected HtmlMetawidget createMetawidget()
	{
		Application application = getFacesContext().getApplication();
		return (HtmlMetawidget) application.createComponent( "org.metawidget.HtmlMetawidget" );
	}

	@Override
	protected void initMetawidget( UIMetawidget metawidget, Map<String, String> attributes )
		throws Exception
	{
		super.initMetawidget( metawidget, attributes );

		( (HtmlMetawidget) metawidget ).setCreateHiddenFields( mCreateHiddenFields );
	}

	protected UIComponent createReadOnlyComponent( Map<String, String> attributes )
	{
		Application application = getFacesContext().getApplication();
		return createReadOnlyComponent( attributes, application.createComponent( "javax.faces.Output" ) );
	}

	protected UIComponent createReadOnlyComponent( Map<String, String> attributes, UIComponent readOnlyComponent )
	{
		Application application = getFacesContext().getApplication();

		if ( !mCreateHiddenFields || TRUE.equals( attributes.get( NO_SETTER ) ) )
			return readOnlyComponent;

		// If using hidden fields, create both a label and a hidden field

		UIComponent componentStub = application.createComponent( "org.metawidget.Stub" );

		@SuppressWarnings( "unchecked" )
		List<UIComponent> children = componentStub.getChildren();

		children.add( application.createComponent( "javax.faces.HtmlInputHidden" ) );
		children.add( readOnlyComponent );

		return componentStub;
	}

	protected UIComponent createCollectionComponent( Class<?> collectionClass, Map<String, String> attributes )
	{
		FacesContext context = getFacesContext();
		Application application = context.getApplication();

		if ( !mCreateHiddenFields || TRUE.equals( attributes.get( NO_SETTER ) ) )
			return null;

		// If using hidden fields, create a hidden field to POST back the Collection

		return application.createComponent( "javax.faces.HtmlInputHidden" );
	}
}
