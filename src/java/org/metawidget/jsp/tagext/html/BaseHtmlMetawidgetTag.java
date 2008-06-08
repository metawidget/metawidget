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

package org.metawidget.jsp.tagext.html;

import org.metawidget.jsp.tagext.MetawidgetTag;

/**
 * Base Metawidget for JSP environments that output HTML.
 *
 * @author Richard Kennard
 */

public abstract class BaseHtmlMetawidgetTag
	extends MetawidgetTag
{
	//
	//
	// Protected members
	//
	//

	protected String	mStyle;

	protected String	mStyleClass;

	protected boolean	mCreateHiddenFields;

	//
	//
	// Constructor
	//
	//

	public BaseHtmlMetawidgetTag()
	{
		// Default constructor
	}

	public BaseHtmlMetawidgetTag( BaseHtmlMetawidgetTag tag )
	{
		super( tag );

		mStyle = tag.mStyle;
		mStyleClass = tag.mStyleClass;
		mCreateHiddenFields = tag.mCreateHiddenFields;
	}

	//
	//
	// Public methods
	//
	//

	public void setStyle( String style )
	{
		mStyle = style;
	}

	public void setStyleClass( String styleClass )
	{
		mStyleClass = styleClass;
	}

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
	public void release()
	{
		super.release();

		mStyle = null;
		mStyleClass = null;
		mCreateHiddenFields = false;
	}
}
