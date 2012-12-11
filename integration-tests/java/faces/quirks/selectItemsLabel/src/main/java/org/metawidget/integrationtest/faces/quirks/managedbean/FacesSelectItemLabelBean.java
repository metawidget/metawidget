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

package org.metawidget.integrationtest.faces.quirks.managedbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.faces.UiFacesLookup;
import org.metawidget.util.CollectionUtils;

/**
 * Models a bean that uses <code>itemLabel</code> and <code>itemValue</code>
 *
 * @author Richard Kennard
 */

@ManagedBean( name = "selectItemLabel" )
@SessionScoped
public class FacesSelectItemLabelBean {

	//
	// Private members
	//

	private String			mEntityValue;

	private List<Entity>	mEntities	= CollectionUtils.newArrayList();

	//
	// Constructor
	//

	public FacesSelectItemLabelBean() {

		mEntities.add( new Entity( "Label1", "Value1" ) );
		mEntities.add( new Entity( "Label2", "Value2" ) );
		mEntities.add( new Entity( "Label3", "Value3" ) );
	}

	//
	// Public methods
	//

	public List<Entity> getEntities() {

		return mEntities;
	}

	@UiFacesLookup( value = "#{selectItemLabel.entities}", var = "_entity", itemLabel = "#{_entity.label}", itemValue = "#{_entity.value}" )
	public String getEntityValue() {

		return mEntityValue;
	}

	public void setEntityValue( String entityValue ) {

		mEntityValue = entityValue;
	}

	@UiAction
	public void update() {

		// Do nothing
	}

	//
	// Inner class
	//

	public static class Entity {

		//
		// Private members
		//

		private String		mLabel;

		private String	mValue;

		//
		// Constructor
		//

		public Entity( String label, String value ) {

			mLabel = label;
			mValue = value;
		}

		//
		// Public methods
		//

		public String getLabel() {

			return mLabel;
		}

		public String getValue() {

			return mValue;
		}
	}
}
