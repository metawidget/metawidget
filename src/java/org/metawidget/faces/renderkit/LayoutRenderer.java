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

package org.metawidget.faces.renderkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.FacesUtils.ReentrantThreadLocal;
import org.metawidget.util.CollectionUtils;

/**
 * Base class for all JSF layout renderers.
 * <p>
 * Manages a 'Re-entrant Thread Local' mechanism to allow JSF Renderers to maintain state even
 * though they are both multi-threaded and re-entrant.
 *
 * @author Richard Kennard
 */

public abstract class LayoutRenderer
	extends Renderer
{
	//
	//
	// Private members
	//
	//

	/**
	 * Mechanism to pass state between Renderer methods whose signature we cannot control (eg.
	 * encodeBegin, encodeChildren, encodeEnd). This mechanism needs to be reentrant because
	 * Metawidgets can be embedded whereas JSF Renderers are shared.
	 */

	private ReentrantThreadLocal<Map<String, Object>>	mLocalState	= new ReentrantThreadLocal<Map<String, Object>>()
																	{
																		@Override
																		protected Map<String, Object> initialValue()
																		{
																			return CollectionUtils.newHashMap();
																		}
																	};

	//
	//
	// Public methods
	//
	//

	@Override
	public void decode( FacesContext context, UIComponent component )
	{
		super.decode( context, component );

		// Reset ReentrantThreadLocal (just in case there was a previous
		// Exception and our push/pops got out of sync)
		//
		// Note: don't use mLocalState.remove() because we need J2SE 1.4 compatibility

		mLocalState.set( new HashMap<String, Object>() );
	}

	@Override
	public final void encodeBegin( FacesContext context, UIComponent component )
		throws IOException
	{
		// Support re-entry

		mLocalState.push();

		reentrantEncodeBegin( context, component );
	}

	public void reentrantEncodeBegin( FacesContext context, UIComponent component )
		throws IOException
	{
		super.encodeBegin( context, component );
	}

	/**
	 * Denotes that this Renderer renders its own children (eg. JSF should not call
	 * <code>encodeBegin</code> on each child for us)
	 */

	@Override
	public boolean getRendersChildren()
	{
		return true;
	}

	@Override
	public void encodeEnd( FacesContext context, UIComponent component )
		throws IOException
	{
		reentrantEncodeEnd( context, component );

		// Support re-entry

		mLocalState.pop();
	}

	public void reentrantEncodeEnd( FacesContext context, UIComponent component )
		throws IOException
	{
		super.encodeEnd( context, component );
	}

	//
	//
	// Protected methods
	//
	//

	protected Object getState( String key )
	{
		return mLocalState.get().get( key );
	}

	protected Object putState( String key, Object value )
	{
		return mLocalState.get().put( key, value );
	}

	protected Object removeState( String key )
	{
		return mLocalState.get().remove( key );
	}

	protected void writeStyleAndClass( UIComponent component, ResponseWriter writer, String style )
		throws IOException
	{
		UIParameter parameterStyle = FacesUtils.findParameterWithName( component, style + "Style" );

		if ( parameterStyle != null )
		{
			writer.write( " style=\"" );
			writer.write( (String) parameterStyle.getValue() );
			writer.write( "\"" );
		}

		UIParameter parameterStyleClass = FacesUtils.findParameterWithName( component, style + "StyleClass" );

		if ( parameterStyleClass != null )
		{
			writer.write( " class=\"" );
			writer.write( (String) parameterStyleClass.getValue() );
			writer.write( "\"" );
		}
	}
}
