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

package org.metawidget.spring.allwidgets.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.metawidget.shared.allwidgets.model.AllWidgets.NestedWidgets;
import org.metawidget.spring.allwidgets.editor.DateEditor;
import org.metawidget.spring.allwidgets.editor.NestedWidgetsEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * @author Richard Kennard
 */

public class AllWidgetsController
	extends SimpleFormController
{
	//
	// Protected methods
	//

	@Override
	protected void initBinder( HttpServletRequest request, ServletRequestDataBinder binder )
		throws Exception
	{
		super.initBinder( request, binder );

		binder.registerCustomEditor( Date.class, new DateEditor() );
		binder.registerCustomEditor( NestedWidgets.class, new NestedWidgetsEditor() );
	}
}
