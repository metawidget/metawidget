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

package org.metawidget.faces.component.html.widgetbuilder;

import org.metawidget.util.simple.ObjectUtils;


/**
 * Configures a HtmlWidgetBuilder prior to use. Once instantiated, WidgetBuilders are immutable.
 *
 * @author Richard Kennard
 */

public class HtmlWidgetBuilderConfig
{
	//
	// Private members
	//

	private String		mDataTableStyleClass;

	private String[]	mDataTableColumnClasses;

	private String[]	mDataTableRowClasses;

	//
	// Public methods
	//

	public String getDataTableStyleClass()
	{
		return mDataTableStyleClass;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public HtmlWidgetBuilderConfig setDataTableStyleClass( String dataTableStyleClass )
	{
		mDataTableStyleClass = dataTableStyleClass;

		return this;
	}

	public String[] getDataTableColumnClasses()
	{
		return mDataTableColumnClasses;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public HtmlWidgetBuilderConfig setDataTableColumnClasses( String... dataTableColumnClasses )
	{
		mDataTableColumnClasses = dataTableColumnClasses;

		return this;
	}

	public String[] getDataTableRowClasses()
	{
		return mDataTableRowClasses;
	}

	/**
	 * @return this, as part of a fluent interface
	 */

	public HtmlWidgetBuilderConfig setDataTableRowClasses( String... dataTableRowClasses )
	{
		mDataTableRowClasses = dataTableRowClasses;

		return this;
	}

	@Override
	public boolean equals( Object that )
	{
		if ( this == that )
			return true;

		if ( that == null )
			return false;

		if ( getClass() != that.getClass() )
			return false;

		if ( !ObjectUtils.nullSafeEquals( mDataTableStyleClass, ((HtmlWidgetBuilderConfig) that).mDataTableStyleClass ))
			return false;

		if ( !ObjectUtils.nullSafeEquals( mDataTableColumnClasses, ((HtmlWidgetBuilderConfig) that).mDataTableColumnClasses ))
			return false;

		if ( !ObjectUtils.nullSafeEquals( mDataTableRowClasses, ((HtmlWidgetBuilderConfig) that).mDataTableRowClasses ))
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mDataTableStyleClass );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mDataTableColumnClasses );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mDataTableRowClasses );

		return hashCode;
	}
}
