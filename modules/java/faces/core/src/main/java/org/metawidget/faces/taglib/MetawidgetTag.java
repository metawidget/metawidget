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

package org.metawidget.faces.taglib;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.iface.MetawidgetException;

/**
 * Base JSP tag for UIMetawidget JSF widgets.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@SuppressWarnings( "deprecation" )
public abstract class MetawidgetTag
	extends UIComponentTag {

	//
	// Private members
	//

	private String	mValue;

	private String	mRendererType;

	private String	mConfig;

	private boolean	mInspectFromParent;

	private String	mReadOnly;

	private String	mBundle;

	//
	// Public methods
	//

	/**
	 * Sets the value binding.
	 * <p>
	 * This is JSF 1.1 style. In JSF 1.2, you use <code>setValue( ValueExpression )</code> and
	 * <code>&lt;deferred-value&gt;&lt;type&gt;java.lang.Object&lt;/type&gt;&lt;/deferred-value&gt;</code>
	 * in the TLD, but we want to be compatible with both.
	 */

	public void setValue( String value ) {

		mValue = value;
	}

	@Override
	public String getRendererType() {

		return mRendererType;
	}

	public void setRendererType( String rendererType ) {

		mRendererType = rendererType;
	}

	public void setConfig( String config ) {

		mConfig = config;
	}

	/**
	 * See doco in <code>UIMetawidget</code>.
	 */

	public void setInspectFromParent( boolean inspectFromParent ) {

		mInspectFromParent = inspectFromParent;
	}

	public void setReadOnly( String readOnly ) {

		mReadOnly = readOnly;
	}

	public void setBundle( String bundle ) {

		mBundle = bundle;
	}

	//
	// Protected methods
	//

	@Override
	protected void setProperties( UIComponent component ) {

		super.setProperties( component );

		UIMetawidget metawidetComponent = (UIMetawidget) component;
		Application application = getFacesContext().getApplication();

		// Value
		//
		// Note: Metawidets do not have to have a value. They can be used purely
		// to lay out manually-specified components. Equally, the value need not
		// be a value binding - it can be a String into a JBPM subsystem, for example

		if ( mValue != null ) {
			if ( isValueReference( mValue ) ) {
				metawidetComponent.setValueBinding( "value", application.createValueBinding( mValue ) );
			} else {
				metawidetComponent.setValue( mValue );
			}
		}

		// Renderer

		if ( mRendererType != null ) {
			metawidetComponent.setRendererType( mRendererType );
		}

		// Config

		if ( mConfig != null ) {
			metawidetComponent.setConfig( mConfig );
		}

		// Inspect from parent

		metawidetComponent.setInspectFromParent( mInspectFromParent );

		// Read-Only

		if ( mReadOnly != null ) {
			if ( isValueReference( mReadOnly ) ) {
				metawidetComponent.setValueBinding( "readOnly", application.createValueBinding( mReadOnly ) );
			} else {
				metawidetComponent.setReadOnly( Boolean.valueOf( mReadOnly ) );
			}
		}

		// Bundle

		if ( mBundle != null ) {
			if ( !isValueReference( mBundle ) ) {
				throw MetawidgetException.newException( "Bundle must be an EL expression" );
			}

			metawidetComponent.setValueBinding( "bundle", application.createValueBinding( mBundle ) );
		}
	}
}
