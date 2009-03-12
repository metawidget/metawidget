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

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;

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
	// Protected members
	//

	protected boolean			mCreateHiddenFields;

	//
	// Public methods
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

	public boolean isCreateHiddenFields()
	{
		return mCreateHiddenFields;
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
	// Protected methods
	//

	@Override
	protected UIComponent afterBuildWidget( UIComponent component, Map<String, String> attributes )
		throws Exception
	{
		// Apply CSS attributes

		UIComponent componentToStyle = super.afterBuildWidget( component, attributes );

		if ( componentToStyle == null )
			return componentToStyle;

		if ( component instanceof UIStub )
			componentToStyle = component.getChildren().get( 0 );

		Map<String, Object> componentAttributes = componentToStyle.getAttributes();
		Map<String, Object> thisAttributes = getAttributes();
		String style = (String) thisAttributes.get( "style" );

		if ( style != null && !componentAttributes.containsKey( "style" ) )
			componentAttributes.put( "style", style );

		String styleClass = (String) thisAttributes.get( "styleClass" );

		if ( styleClass != null && !componentAttributes.containsKey( "styleClass" ) )
			componentAttributes.put( "styleClass", styleClass );

		return component;
	}

	/**
	 * Create a sub-Metawidget.
	 * <p>
	 * Usually, clients will want to create a sub-Metawidget using the same subclass as themselves.
	 * To be 'proper' in JSF, though, we should go via <code>application.createComponent</code>.
	 * Unfortunately a UIComponent does not know its own component name, so subclasses must override
	 * this method.
	 */

	@Override
	protected HtmlMetawidget buildMetawidget( Map<String, String> attributes )
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
}
