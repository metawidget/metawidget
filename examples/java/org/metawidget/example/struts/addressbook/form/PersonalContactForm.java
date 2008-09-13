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

package org.metawidget.example.struts.addressbook.form;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLabel;

/**
 * @author Richard Kennard
 */

public class PersonalContactForm
	extends ContactForm
{
	//
	// Private statics
	//

	private final static long	serialVersionUID	= -1146370265385364438L;

	//
	// Private members
	//

	private String	mDateOfBirthAsString;

	private Date	mOfBirth;

	//
	// Public methods
	//

	/**
	 * Returns the Date of Birth, as a String.
	 * <p>
	 * Ideally, Struts 1.x will one day include the patch at
	 * https://issues.apache.org/struts/browse/STR-1455. Until then, it is necessary to do the Date
	 * conversion in the form.
	 */

	@UiComesAfter( "surname" )
	@UiLabel( "dateOfBirth" )
	public String getDateOfBirthAsString()
	{
		return mDateOfBirthAsString;
	}

	public void setDateOfBirthAsString( String ofBirthAsString )
	{
		mDateOfBirthAsString = ofBirthAsString;
	}

	@UiHidden
	public Date getDateOfBirth()
	{
		return mOfBirth;
	}

	public void setDateOfBirth( Date ofBirth )
	{
		mOfBirth = ofBirth;

		if ( mOfBirth != null )
			mDateOfBirthAsString = (String) ConvertUtils.convert( mOfBirth, String.class );
	}

	@Override
	public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
	{
		ActionErrors errors = new ActionErrors();

		// Date of Birth

		try
		{
			if ( mDateOfBirthAsString != null )
				mOfBirth = (Date) ConvertUtils.convert( mDateOfBirthAsString, Date.class );
		}
		catch( Exception e )
		{
			errors.add( "dateOfBirth", new ActionMessage( e.toString() ) );
		}

		// Base class

		errors.add( super.validate( mapping, request ));

		return errors;
	}
}
