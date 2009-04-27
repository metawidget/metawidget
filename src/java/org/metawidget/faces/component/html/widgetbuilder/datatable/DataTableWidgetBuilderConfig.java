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

package org.metawidget.faces.component.html.widgetbuilder.datatable;


/**
 * WidgetBuilder for Java Server Faces environments.
 * <p>
 * Automatically creates native JSF HTML UIComponents, such as <code>HtmlInputText</code> and
 * <code>HtmlSelectOneListbox</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

public class DataTableWidgetBuilderConfig
{
	//
	// Private members
	//

	private String	mStyleClass;

	private String	mColumnClasses;

	private String	mRowClasses;

	//
	// Public methods
	//

	public String getStyleClass()
	{
		return mStyleClass;
	}

	public void setStyleClass( String styleClass )
	{
		mStyleClass = styleClass;
	}

	public String getColumnClasses()
	{
		return mColumnClasses;
	}

	public void setColumnClasses( String columnClasses )
	{
		mColumnClasses = columnClasses;
	}

	public String getRowClasses()
	{
		return mRowClasses;
	}

	public void setRowClasses( String rowClasses )
	{
		mRowClasses = rowClasses;
	}
}
