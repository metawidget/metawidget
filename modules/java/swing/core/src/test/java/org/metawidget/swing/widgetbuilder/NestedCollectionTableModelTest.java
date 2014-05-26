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

package org.metawidget.swing.widgetbuilder;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import junit.framework.TestCase;

import org.metawidget.swing.SwingMetawidget;

public class NestedCollectionTableModelTest
	extends TestCase {

	//
	// Public methods
	//

	public void testCollections()
		throws Exception {

		Child child = new Child();
		List<Parameter> parameters = new ArrayList<Parameter>();
		Parameter parameter = new Parameter();
		parameter.setValue( "myValue" );
		parameter.setDataType( "special type" );
		parameters.add( parameter );
		child.setParameters( parameters );

		Parent parent = new Parent();
		parent.setChild( child );
		List<Value> parentParameters = new ArrayList<Value>();
		Value value = new Value();
		value.setValue( "parent value" );
		parentParameters.add( value );
		parent.setParameters( parentParameters );

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( parent );

		// Test

		assertEquals( "Child:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		SwingMetawidget nestedMetawidget = (SwingMetawidget) metawidget.getComponent( 1 );
		assertEquals( "Parameters:", ( (JLabel) nestedMetawidget.getComponent( 0 ) ).getText() );
		JTable table = (JTable) ( (JScrollPane) nestedMetawidget.getComponent( 1 ) ).getViewport().getView();
		assertEquals( "Data Type", table.getColumnName( 0 ) );
		assertEquals( "Value", table.getColumnName( 1 ) );
		assertEquals( "special type", table.getModel().getValueAt( 0, 0 ) );
		assertEquals( 1, table.getRowCount() );

		assertEquals( "Parameters:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		table = (JTable) ( (JScrollPane) metawidget.getComponent( 3 ) ).getViewport().getView();
		assertEquals( "Value", table.getColumnName( 0 ) );
		assertEquals( "parent value", table.getModel().getValueAt( 0, 0 ) );
		assertEquals( 1, table.getRowCount() );
	}

	//
	// Inner class
	//

	public static class Parent {

		//
		// Private members
		//

		private List<Value>	mParameters;

		private Child		mChild;

		//
		// Public methods
		//

		public List<Value> getParameters() {

			return mParameters;
		}

		public void setParameters( List<Value> parameters ) {

			this.mParameters = parameters;
		}

		public Child getChild() {

			return mChild;
		}

		public void setChild( Child child ) {

			this.mChild = child;
		}
	}

	public static class Child {

		//
		// Private members
		//

		private List<Parameter>	mParameters;

		//
		// Public methods
		//

		public List<Parameter> getParameters() {

			return mParameters;
		}

		public void setParameters( List<Parameter> parameters ) {

			this.mParameters = parameters;
		}
	}

	public static class Value {

		//
		// Private members
		//

		private String	mValue;

		//
		// Public methods
		//

		public String getValue() {

			return mValue;
		}

		public void setValue( String value ) {

			this.mValue = value;
		}
	}

	public static class Parameter {

		//
		// Private members
		//

		private String	mValue;

		private String	mDataType;

		//
		// Public methods
		//

		public String getValue() {

			return mValue;
		}

		public void setValue( String value ) {

			this.mValue = value;
		}

		public String getDataType() {

			return mDataType;
		}

		public void setDataType( String dataType ) {

			this.mDataType = dataType;
		}
	}
}