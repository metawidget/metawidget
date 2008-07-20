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

package org.metawidget.example.faces.addressbook.managedbean;

import org.metawidget.example.shared.addressbook.model.Communication;

/**
 * @author Richard
 */

public class CommunicationBean
{
	//
	//
	// Private statics
	//
	//

	private Communication				mCommunicationCurrent;

	//
	//
	// Constructor
	//
	//

	public CommunicationBean()
	{
		clear();
	}

	//
	//
	// Public methods
	//
	//

	public Communication getCurrent()
	{
		return mCommunicationCurrent;
	}

	public void setCurrent( Communication communicationCurrent )
	{
		mCommunicationCurrent = communicationCurrent;
	}

	public void clear()
	{
		mCommunicationCurrent = new Communication();
	}
}
