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

package org.metawidget.inspector.impl.propertystyle.javabean;

import java.text.MessageFormat;

import org.metawidget.inspector.impl.propertystyle.BasePropertyStyleConfig;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a JavaBeanPropertyStyle prior to use. Once instantiated, PropertyStyles are immutable.
 *
 * @author Richard Kennard
 */

public class JavaBeanPropertyStyleConfig
	extends BasePropertyStyleConfig {

	//
	// Private members
	//

	private boolean			mSupportPublicFields = true;

	private MessageFormat	mPrivateFieldConvention;

	//
	// Public methods
	//

	// TODO: getters should be protected?

	/**
	 * Sets whether to recognize public fields as properties. True by default.
	 *
	 * @return this, as part of a fluent interface
	 */

	public JavaBeanPropertyStyleConfig setSupportPublicFields( boolean supportPublicFields ) {

		mSupportPublicFields = supportPublicFields;

		return this;
	}

	/**
	 * Sets the naming convention used to identify the private field, given the property
	 * name.
	 * <p>
	 * The JavaBean specification does not establish a relationship between getters/setters and
	 * their private fields. This is because some getters/setters will not be simple one-to-one
	 * mappings. For example, a <code>getAge</code> method may calculate itself based off a
	 * <code>mDateOfBirth</code> field, rather than an <code>mAge</code> field.
	 * <p>
	 * However, it is a common requirement to want to <em>annotate</em> the private field rather
	 * than its getter/setter. Frameworks like JPA allow this because they can populate the private
	 * field directly. This does not work well for Metawidget because most UI frameworks, including
	 * binding and validation frameworks, rely on public getters/setters.
	 * <p>
	 * To support the best of both worlds, <code>JavaBeanPropertyStyle</code> can attempt to map a
	 * getter/setter to its private field if given the naming convention to use. The naming
	 * convention is specified as a <code>MessageFormat</code>. Some examples:
	 * <p>
	 * <ul>
	 * <li>{0} = dateOfBirth, surname</li>
	 * <li>'m'{1} = mDateOfBirth, mSurname</li>
	 * <li>'m_'{0} = m_dateOfBirth, m_surname</li>
	 * </ul>
	 * <p>
	 * This mapping will fail silently in cases where there is no private field. It will also fail
	 * silently if the private field name is misspelt, so be careful!
	 *
	 * @return this, as part of a fluent interface
	 */

	public JavaBeanPropertyStyleConfig setPrivateFieldConvention( MessageFormat privateFieldConvention ) {

		mPrivateFieldConvention = privateFieldConvention;

		return this;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( that == null ) {
			return false;
		}

		if ( getClass() != that.getClass() ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mPrivateFieldConvention, ( (JavaBeanPropertyStyleConfig) that ).mPrivateFieldConvention ) ) {
			return false;
		}

		if ( mSupportPublicFields != ( (JavaBeanPropertyStyleConfig) that ).mSupportPublicFields ) {
			return false;
		}

		return super.equals( that );
	}

	@Override
	public int hashCode() {

		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mPrivateFieldConvention );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mSupportPublicFields );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected boolean isSupportPublicFields() {

		return mSupportPublicFields;
	}

	protected MessageFormat getPrivateFieldConvention() {

		return mPrivateFieldConvention;
	}
}
