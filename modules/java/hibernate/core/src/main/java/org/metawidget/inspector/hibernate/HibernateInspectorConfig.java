// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.inspector.hibernate;

import java.io.InputStream;

import org.metawidget.inspector.impl.BaseXmlInspectorConfig;
import org.metawidget.util.simple.ObjectUtils;

/**
 * Configures a HibernateInspector prior to use. Once instantiated, Inspectors are immutable.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HibernateInspectorConfig
	extends BaseXmlInspectorConfig {

	//
	// Private members
	//

	private boolean	mHideIds	= true;

	//
	// Constructor
	//

	public HibernateInspectorConfig() {

		setDefaultFile( "hibernate.cfg.xml" );
	}

	//
	// Public methods
	//

	/**
	 * Overridden to provide a covariant return type for our fluent interface.
	 */

	@Override
	public HibernateInspectorConfig setInputStream( InputStream stream ) {

		return (HibernateInspectorConfig) super.setInputStream( stream );
	}

	/**
	 * Sets whether the Inspector returns &lt;id&gt; properties as <code>hidden="true"</code>. True
	 * by default.
	 * <p>
	 * Hibernate recommends using synthetic ids, so generally they don't appear in the UI.
	 *
	 * @return this, as part of a fluent interface
	 */

	public HibernateInspectorConfig setHideIds( boolean hideIds ) {

		mHideIds = hideIds;

		// Fluent interface

		return this;
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that )) {
			return false;
		}

		if ( mHideIds != ( (HibernateInspectorConfig) that ).mHideIds ) {
			return false;
		}

		return super.equals( that );
	}

	@Override
	public int hashCode() {

		int hashCode = super.hashCode();
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mHideIds );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected boolean isHideIds() {

		return mHideIds;
	}
}