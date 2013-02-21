// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

package org.metawidget.example.gwt.addressbook.server.tagext;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.metawidget.util.CollectionUtils;

/**
 * Tag to render a ResourceBundle as a JavaScript variable.
 * <p>
 * This allows us to reuse our
 * <code>org.metawidget.example.shared.addressbook.resource.Resources</code> within the GWT
 * <code>Dictionary</code> mechanism by using <code>GwtMetawidget.setDictionaryName</code>, thus
 * reducing unnecessary duplication.
 *
 * @author Richard Kennard
 */

public class Bundle2VariableTag
	extends TagSupport {

	//
	// Private statics
	//

	private static final String			DEFAULT_VARIABLE_NAME	= "bundle";

	//
	// Private members
	//

	private transient ResourceBundle	mBundle;

	private String						mVariableName			= DEFAULT_VARIABLE_NAME;

	//
	// Public methods
	//

	public void setBundle( ResourceBundle bundle ) {

		mBundle = bundle;
	}

	public void setVariableName( String variableName ) {

		mVariableName = variableName;
	}

	@Override
	public int doEndTag()
		throws JspException {

		if ( mBundle == null ) {
			throw new JspException( "Bundle is required" );
		}

		if ( mVariableName == null || "".equals( mVariableName ) ) {
			throw new JspException( "Variable name is required" );
		}

		try {
			JspWriter writer = pageContext.getOut();

			writer.write( "<script type=\"text/javascript\">\n\tvar " );
			writer.write( mVariableName );
			writer.write( " = {\n" );

			List<String> keys = CollectionUtils.newArrayList();

			for ( Enumeration<String> e = mBundle.getKeys(); e.hasMoreElements(); ) {

				keys.add( e.nextElement() );
			}

			Collections.sort( keys );

			for ( int loop = 0, length = keys.size(); loop < length; loop++ ) {
				String key = keys.get( loop );
				writer.write( "\t\"" );
				writer.write( key );
				writer.write( "\": \"" );
				writer.write( mBundle.getString( key ) );
				writer.write( "\"" );

				if ( loop < length - 1 ) {
					writer.write( "," );
				}

				writer.write( "\n" );
			}

			writer.write( "};\n</script>\n" );
		} catch ( IOException e ) {
			throw new JspException( e );
		}

		return super.doEndTag();
	}

	@Override
	public void release() {

		super.release();

		mBundle = null;
		mVariableName = DEFAULT_VARIABLE_NAME;
	}
}
