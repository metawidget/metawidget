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

package org.metawidget.jsp.tagext;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Base Stub for JSP environments.
 * <p>
 * A Stub takes a <code>path</code> but does nothing with it. Stubs are used to 'stub out' what
 * Metawidget would normally create - either to suppress widget creation entirely or to create child
 * widgets with a different path.
 * <p>
 * StubTags differ from FacetTags in that they define a path and override widget creation.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class StubTag
	extends BodyTagSupport {

	//
	// Private members
	//

	/**
	 * Path to stub out.
	 * <p>
	 * Set by subclasses according to what they prefer to call it (eg. <code>name</code> for Struts,
	 * <code>property</code> for Spring).
	 */

	private String				mPath;

	private String				mSavedBodyContent;

	private Map<String, String>	mAttributes;

	//
	// Public methods
	//

	public void setAttributes( String attributes )
		throws JspException {

		if ( mAttributes == null ) {
			mAttributes = CollectionUtils.newHashMap();
		}

		for ( String nameAndValue : CollectionUtils.fromString( attributes, ';' ) ) {
			List<String> nameAndValueList = CollectionUtils.fromString( nameAndValue, StringUtils.SEPARATOR_COLON_CHAR );

			if ( nameAndValueList.size() != 2 ) {
				throw new JspException( "Unrecognized value '" + nameAndValue + "'" );
			}

			mAttributes.put( nameAndValueList.get( 0 ), nameAndValueList.get( 1 ) );
		}
	}

	public Map<String, String> getAttributesMap() {

		return mAttributes;
	}

	/**
	 * Get the body content as saved during <code>doEndTag</code>.
	 * <p>
	 * It seems <code>getBodyContent().toString</code> only returns a meaningful result while we are
	 * in the <code>doEndTag</code> method. We capture it there for use later.
	 */

	public String getSavedBodyContent() {

		return mSavedBodyContent;
	}

	@Override
	public int doEndTag()
		throws JspException {

		MetawidgetTag tagMetawidget = (MetawidgetTag) findAncestorWithClass( this, MetawidgetTag.class );

		if ( tagMetawidget == null ) {
			throw new JspTagException( getClass() + " must be used within " + MetawidgetTag.class );
		}

		if ( bodyContent == null ) {
			mSavedBodyContent = null;
		} else {
			mSavedBodyContent = bodyContent.getString();
		}

		tagMetawidget.setStub( mPath, this );

		return super.doEndTag();
	}

	//
	// Protected members
	//

	/**
	 * Sets the path.
	 * <p>
	 * Set by subclasses according to what they prefer to call it (eg. <code>name</code> for Struts,
	 * <code>property</code> for Spring).
	 */

	protected void setPathInternal( String path ) {

		mPath = path;
	}
}
