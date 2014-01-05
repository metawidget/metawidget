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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
