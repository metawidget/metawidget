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

package org.metawidget.faces.component.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.Application;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.impl.BaseWidgetProcessor;

/**
 * WidgetProcessor to add standard JSF value bindings to a UIComponent.
 *
 * @author Richard Kennard
 */

@SuppressWarnings( "deprecation" )
public class StandardBindingProcessor
	extends BaseWidgetProcessor<UIComponent, UIMetawidget>
{
	//
	// Public methods
	//

	@Override
	public void onStartBuild( UIMetawidget metawidget )
	{
		metawidget.getClientProperty( StandardBindingProcessor.class );
	}

	@Override
	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget )
	{
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();

		// Bind actions

		ValueBinding metawidgetValueBinding = component.getValueBinding( "value" );
		String facesExpression = attributes.get( FACES_EXPRESSION );

		if ( ACTION.equals( elementName ) )
		{
			ActionSource actionSource = (ActionSource) component;
			MethodBinding methodBinding = actionSource.getAction();

			if ( methodBinding == null )
			{
				// If there is a faces-expression, use it...

				if ( facesExpression != null )
				{
					methodBinding = application.createMethodBinding( facesExpression, null );
				}

				// ...otherwise try and construct the binding

				else
				{
					String name = attributes.get( NAME );

					if ( metawidgetValueBinding != null && name != null && !"".equals( name ) )
					{
						String facesExpressionPrefix = FacesUtils.unwrapExpression( metawidgetValueBinding.getExpressionString() );
						facesExpression = FacesUtils.wrapExpression( facesExpressionPrefix + StringUtils.SEPARATOR_DOT_CHAR + name );
						methodBinding = application.createMethodBinding( facesExpression, null );
					}
				}

				if ( methodBinding != null )
				{
					actionSource.setAction( methodBinding );

					// Does widget need an id?
					//
					// Note: it is very dangerous to reassign an id if the widget already has one,
					// as it will create duplicates in the child component list

					if ( component.getId() == null )
						setUniqueId( context, component, methodBinding.getExpressionString(), metawidget );
				}
			}

			return component;
		}

		// Bind properties

		// If there is a faces-expression, use it...

		String valueBindingExpression = facesExpression;

		if ( valueBindingExpression == null )
		{
			ValueBinding valueBinding = component.getValueBinding( "value" );

			// ...if we are at the top level...

			if ( ENTITY.equals( elementName ) )
			{
				if ( valueBinding != null )
					valueBindingExpression = valueBinding.getExpressionString();
			}

			// ...if we are not at the top level, try and construct the binding

			else
			{
				String name = attributes.get( NAME );

				if ( name != null && !"".equals( name ) )
				{
					String facesExpressionPrefix = FacesUtils.unwrapExpression( valueBinding.getExpressionString() );
					facesExpression = FacesUtils.wrapExpression( facesExpressionPrefix + StringUtils.SEPARATOR_DOT_CHAR + name );
				}
			}
		}

		if ( valueBindingExpression != null )
		{
			try
			{
				// JSF 1.2 mode: some components (such as
				// org.jboss.seam.core.Validators.validate()) expect ValueExpressions and do
				// not work with ValueBindings (see JBSEAM-3252)
				//
				// Note: we wrap the ValueExpression as an Object[] to stop link-time
				// dependencies on javax.el.ValueExpression, so that we still work with
				// JSF 1.1

				Object[] valueExpression = new Object[] { application.getExpressionFactory().createValueExpression( context.getELContext(), valueBindingExpression, Object.class ) };
				attachValueExpression( component, valueExpression[0], attributes );
			}
			catch ( NoSuchMethodError e )
			{
				// JSF 1.1 mode

				attachValueBinding( component, application.createValueBinding( valueBindingExpression ), attributes );
			}

			// Does widget need an id?
			//
			// Note: it is very dangerous to reassign an id if the widget already has one,
			// as it will create duplicates in the child component list

			if ( component.getId() == null )
				setUniqueId( context, component, valueBindingExpression, metawidget );
		}

		return component;
	}

	//
	// Private methods
	//

	/**
	 * Unlike <code>UIViewRoot.createUniqueId</code>, tries to make the Id human readable, both for
	 * debugging purposes and for when running unit tests (using, say, WebTest).
	 * <p>
	 * This method is not separated out into, say, FacesUtils because we want subclasses to be able
	 * to override it.
	 * <p>
	 * Subclasses can override this method to use <code>UIViewRoot.createUniqueId</code> if
	 * preferred. They can even override it to assign a different, random id to a component each
	 * time it is generated. This is a great way to fox hackers who are trying to POST back
	 * pre-generated payloads of HTTP fields (ie. CSRF attacks).
	 */

	protected void setUniqueId( FacesContext context, UIComponent component, String expressionString, UIMetawidget metawidget )
	{
		// Avoid duplicates

		Set<String> clientIds = metawidget.getClientProperty( StandardBindingProcessor.class );

		if ( clientIds == null )
		{
			clientIds = CollectionUtils.newHashSet();

			Iterator<UIComponent> iteratorFacetsAndChildren = context.getViewRoot().getFacetsAndChildren();
			gatherClientIds( iteratorFacetsAndChildren, clientIds );
		}

		// Create our ideal Id

		String idealId = StringUtils.camelCase( FacesUtils.unwrapExpression( expressionString ), StringUtils.SEPARATOR_DOT_CHAR );

		// Suffix nested Metawidgets, because otherwise if they only expand to a single child they
		// will give that child component a '_2' suffixed id

		if ( component instanceof UIMetawidget )
			idealId += "_Metawidget";

		// Convert to an actual, valid id (avoid conflicts)

		String actualId = idealId;
		int duplicateId = 1;

		while ( true )
		{
			if ( clientIds.add( actualId ) )
				break;

			duplicateId++;
			actualId = idealId + '_' + duplicateId;
		}

		// Support stubs

		if ( component instanceof UIStub )
		{
			List<UIComponent> children = component.getChildren();

			if ( !children.isEmpty() )
			{
				int childId = 1;

				for ( UIComponent componentChild : children )
				{
					if ( childId > 1 )
						componentChild.setId( actualId + '_' + childId );
					else
						componentChild.setId( actualId );

					childId++;
				}

				return;
			}
		}

		// Set Id

		component.setId( actualId );
	}

	/**
	 * Attach value binding for component.
	 * <p>
	 * If the created component is a <code>UIStub</code>, we set the same value binding on
	 * <em>all</em> its children (as well as the <code>UIStub</code> itself). This allows us to
	 * build compound components, such as a <code>HtmlOutputText</code> combined with a
	 * <code>HtmlInputHidden</code>.
	 */

	private void attachValueBinding( UIComponent widget, ValueBinding valueBinding, Map<String, String> attributes )
	{
		// Support stubs

		if ( widget instanceof UIStub )
		{
			List<UIComponent> children = widget.getChildren();

			for ( UIComponent componentChild : children )
			{
				attachValueBinding( componentChild, valueBinding, attributes );
			}
		}

		// Set binding

		widget.setValueBinding( "value", valueBinding );
	}

	/**
	 * JSF 1.2 version of attachValueBinding.
	 */

	private void attachValueExpression( UIComponent widget, Object valueExpression, Map<String, String> attributes )
	{
		// Support stubs

		if ( widget instanceof UIStub )
		{
			List<UIComponent> children = widget.getChildren();

			for ( UIComponent componentChild : children )
			{
				attachValueExpression( componentChild, valueExpression, attributes );
			}
		}

		// Set binding

		widget.setValueExpression( "value", (javax.el.ValueExpression) valueExpression );
	}

	/**
	 * Gathers client ids of existing children, so as to avoid naming clashes.
	 */

	private void gatherClientIds( Iterator<UIComponent> iteratorFacetsAndChildren, Set<String> clientIds )
	{
		for ( ; iteratorFacetsAndChildren.hasNext(); )
		{
			UIComponent component = iteratorFacetsAndChildren.next();
			String id = component.getId();

			if ( id != null )
				clientIds.add( id );

			gatherClientIds( component.getFacetsAndChildren(), clientIds );
		}
	}
}
