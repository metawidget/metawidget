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

package org.metawidget.gwt.quirks.client.model;

import java.io.Serializable;

import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;

public class GwtTabQuirks
	implements Serializable {

	private final static long	serialVersionUID	= 1l;

	public String				abc;

	@UiSection( { "Foo", "Bar" } )
	public boolean				def;

	@UiLarge
	public String				ghi;

	@UiSection( { "Foo", "Baz" } )
	public String				jkl;

	@UiSection( { "Foo", "" } )
	public boolean				mno;

	@UiSection( { "Foo", "Moo" } )
	public String				pqr;

	@UiSection( "" )
	public String				stu;
}
