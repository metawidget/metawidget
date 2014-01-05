// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.statically.freemarker.layout;

import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a FreemarkerLayout prior to use. Once instantiated, Layouts are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
