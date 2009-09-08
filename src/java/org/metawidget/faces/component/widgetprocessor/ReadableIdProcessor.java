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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * WidgetProcessor to set 'human readable' ids on a UIComponent.
 * <p>
 * Unlike <code>UIViewRoot.createUniqueId</code>, tries to make the id human readable, both for
 * debugging purposes and for when running unit tests (using, say, WebTest). Because the ids are
 * based off the value binding (or method binding) of the UIComponent, this WidgetProcessor must
 * come after <code>StandardBindingProcessor</code> (or equivalent).
 * <p>
 * Clients can plug in a different WidgetProcessor to use <code>UIViewRoot.createUniqueId</code> if
 * preferred. They can even plug in assigning a changing, random id to a component each time it is
 * generated. This is a great way to fox hackers who are trying to POST back pre-generated payloads
 * of HTTP fields (ie. CSRF attacks).
 *
 * @author Richard Kennard
 */

@SuppressWarnings( "deprecation" )
public class ReadableIdProcessor
	extends BaseWidgetProcessor<UIComponent, UIMetawidget>
{
	//
	// Public methods
	//

	@Override
	public void onStartBuild( UIMetawidget metawidget )
	{
		metawidget.putClientProperty( ReadableIdProcessor.class, null );
	}

	@Override
	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget )
	{
		// Does widget need an id?
		//
		// Note: it is very dangerous to reassign an id if the widget already has one,
		// as it will create duplicates in the child component list

		if ( component.getId() != null )
			return component;

		// Base action ids on the methodBinding

		if ( ACTION.equals( elementName ) )
		{
			MethodBinding methodBinding = ( (ActionSource) component ).getAction();

			if ( methodBinding != null )
				setUniqueId( component, methodBinding.getExpressionString(), metawidget );
		}
		else
		{
			// Base property ids on the valueBinding

			ValueBinding valueBinding = component.getValueBinding( "value" );

			if ( valueBinding != null )
				setUniqueId( component, valueBinding.getExpressionString(), metawidget );
		}

		return component;
	}

	//
	// Protected methods
	//

	protected void setUniqueId( UIComponent component, String expressionString, UIMetawidget metawidget )
	{
		String idealId = StringUtils.camelCase( FacesUtils.unwrapExpression( expressionString ), StringUtils.SEPARATOR_DOT_CHAR );

		// Suffix nested Metawidgets, because otherwise if they only expand to a single child they
		// will give that child component a '_2' suffixed id

		if ( component instanceof UIMetawidget )
			idealId += "_Metawidget";

		// Convert to an actual, valid id (avoid conflicts)

		Set<String> clientIds = getClientIds( metawidget );
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

	protected Set<String> getClientIds( UIMetawidget metawidget )
	{
		Set<String> clientIds = metawidget.getClientProperty( ReadableIdProcessor.class );

		if ( clientIds == null )
		{
			clientIds = CollectionUtils.newHashSet();
			metawidget.putClientProperty( ReadableIdProcessor.class, clientIds );

			FacesContext context = FacesContext.getCurrentInstance();
			Iterator<UIComponent> iteratorFacetsAndChildren = context.getViewRoot().getFacetsAndChildren();
			gatherClientIds( iteratorFacetsAndChildren, clientIds );
		}

		return clientIds;
	}

	//
	// Private methods
	//

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
