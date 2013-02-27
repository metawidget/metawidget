// Metawidget (licensed under LGPL)
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

package org.metawidget.statically.freemarker.layout;

import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a FreemarkerLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author Ryan Bradley
 */

public class FreemarkerLayoutConfig {

	//
	// Private members
	//

	private String	mDirectoryForTemplateLoading;

	private String	mTemplate;

	//
	// Public methods
	//

	/**
	 * @return this, as part of a fluent interface
	 */

	public FreemarkerLayoutConfig setDirectoryForTemplateLoading( String directoryForTemplateLoading ) {

		mDirectoryForTemplateLoading = directoryForTemplateLoading;
		return this;
	}

	/**
	 * @return this, as part of a fluent interface
	 */


	public FreemarkerLayoutConfig setTemplate( String template ) {

		mTemplate = template;
		return this;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mDirectoryForTemplateLoading, ( (FreemarkerLayoutConfig) that ).mDirectoryForTemplateLoading ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mTemplate, ( (FreemarkerLayoutConfig) that ).mTemplate ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mDirectoryForTemplateLoading );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mTemplate );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected String getDirectoryForTemplateLoading() {

		return mDirectoryForTemplateLoading;
	}

	protected String getTemplate() {

		return mTemplate;
	}
}
