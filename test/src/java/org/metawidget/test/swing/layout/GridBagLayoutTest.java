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

package org.metawidget.test.swing.layout;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.MetawidgetException;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.annotation.UiReadOnly;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;

/**
 * @author Richard Kennard
 */

public class GridBagLayoutTest
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public GridBagLayoutTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testLayout()
		throws Exception
	{
		// Without stub

		SwingMetawidget metawidget = new SwingMetawidget();
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( new MetawidgetAnnotationInspector(), new PropertyTypeInspector() );
		metawidget.setInspector( new CompositeInspector( config ) );
		metawidget.setParameter( "numberOfColumns", 0 );
		metawidget.setParameter( "sectionStyle", org.metawidget.swing.layout.GridBagLayout.SECTION_AS_TAB );
		metawidget.setToInspect( new Foo() );

		try
		{
			metawidget.getComponent( 0 );
			assertTrue( false );
		}
		catch( MetawidgetException e )
		{
			assertTrue( "numberOfColumns must be >= 1".equals( e.getCause().getCause().getMessage() ));
		}

		metawidget.setParameter( "numberOfColumns", 2 );

		assertTrue( "Abc:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridx );
		assertTrue( "Def*:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof JSpinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridx );
		assertTrue( "Ghi:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridx );

		// JTabbedPane

		JTabbedPane tabbedPane = (JTabbedPane) metawidget.getComponent( 6 );
		assertTrue( 3 == tabbedPane.getComponentCount());
		assertTrue( -1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( tabbedPane ).gridx );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( tabbedPane ).gridy );
		assertTrue( GridBagConstraints.BOTH == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( tabbedPane ).fill );
		assertTrue( GridBagConstraints.REMAINDER == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( tabbedPane ).gridwidth );
		assertTrue( 1.0f == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( tabbedPane ).weighty );

		JPanel panelTab = (JPanel) tabbedPane.getComponent( 0 );
		assertTrue( "tab1".equals( panelTab.getName() ));
		assertTrue( "Tab 1_jkl:".equals( ( (JLabel) panelTab.getComponent( 0 ) ).getText() ) );
		assertTrue( panelTab.getComponent( 1 ) instanceof JLabel );
		assertTrue( 1 == ( (GridBagLayout) panelTab.getLayout() ).getConstraints( panelTab.getComponent( 1 ) ).gridx );
		assertTrue( "Tab 1_mno:".equals( ( (JLabel) panelTab.getComponent( 2 ) ).getText() ) );
		assertTrue( panelTab.getComponent( 3 ) instanceof JComboBox );
		assertTrue( 3 == ( (GridBagLayout) panelTab.getLayout() ).getConstraints( panelTab.getComponent( 3 ) ).gridx );
		assertTrue( "Tab 1_pqr:".equals( ( (JLabel) panelTab.getComponent( 4 ) ).getText() ) );
		assertTrue( panelTab.getComponent( 5 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) panelTab.getLayout() ).getConstraints( panelTab.getComponent( 5 ) ).gridx );
		assertTrue( panelTab.getComponent( 6 ) instanceof JPanel );
		assertTrue( 1.0f == ( (GridBagLayout) panelTab.getLayout() ).getConstraints( panelTab.getComponent( 6 ) ).weighty );

		panelTab = (JPanel) tabbedPane.getComponent( 1 );
		assertTrue( "tab2".equals( panelTab.getName() ));
		assertTrue( panelTab.getComponent( 0 ) instanceof JScrollPane );
		assertTrue( 0 == ( (GridBagLayout) panelTab.getLayout() ).getConstraints( panelTab.getComponent( 0 ) ).gridx );
		assertTrue( GridBagConstraints.REMAINDER == ( (GridBagLayout) panelTab.getLayout() ).getConstraints( panelTab.getComponent( 0 ) ).gridwidth );
		assertTrue( 1 == panelTab.getComponentCount() );

		panelTab = (JPanel) tabbedPane.getComponent( 2 );
		assertTrue( "tab3".equals( panelTab.getName() ));
		assertTrue( panelTab.getComponent( 0 ) instanceof JTextField );
		assertTrue( 0 == ( (GridBagLayout) panelTab.getLayout() ).getConstraints( panelTab.getComponent( 0 ) ).gridx );
		assertTrue( 2 == ( (GridBagLayout) panelTab.getLayout() ).getConstraints( panelTab.getComponent( 0 ) ).gridwidth );
		assertTrue( "Tab 3_mno:".equals( ( (JLabel) panelTab.getComponent( 1 ) ).getText() ) );
		assertTrue( panelTab.getComponent( 2 ) instanceof JTextField );
		assertTrue( 3 == ( (GridBagLayout) panelTab.getLayout() ).getConstraints( panelTab.getComponent( 2 ) ).gridx );
		assertTrue( "Tab 3_pqr:".equals( ( (JLabel) panelTab.getComponent( 3 ) ).getText() ) );
		assertTrue( panelTab.getComponent( 4 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) panelTab.getLayout() ).getConstraints( panelTab.getComponent( 4 ) ).gridx );
		assertTrue( panelTab.getComponent( 5 ) instanceof JPanel );
		assertTrue( 1.0f == ( (GridBagLayout) panelTab.getLayout() ).getConstraints( panelTab.getComponent( 5 ) ).weighty );

		assertTrue( "Mno:".equals( ( (JLabel) metawidget.getComponent( 7 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 8 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridx );

		// With stub

		Stub stub = new Stub();
		stub.setName( "mno" );
		metawidget.add( stub );

		// With stub attributes

		Stub stubWithAttributes = new Stub();
		stubWithAttributes.setName( "def" );
		stubWithAttributes.add( new JSpinner() );
		stubWithAttributes.setAttribute( "large", "true" );
		metawidget.add( stubWithAttributes );

		// With an arbitrary component

		JSpinner arbitrary = new JSpinner();
		metawidget.add( arbitrary );

		// With an arbirary stub with attributes

		Stub arbitraryStubWithAttributes = new Stub();
		arbitraryStubWithAttributes.add( new JTextField() );
		arbitraryStubWithAttributes.setAttribute( "label", "" );
		arbitraryStubWithAttributes.setAttribute( "large", "true" );
		metawidget.add( arbitraryStubWithAttributes );

		assertTrue( "Abc:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 1 ) ).gridx );
		assertTrue( "Def*:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof Stub );
		assertTrue( ( (Stub) metawidget.getComponent( 3 ) ).getComponent( 0 ) instanceof JSpinner );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridx );
		assertTrue( GridBagConstraints.REMAINDER == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridwidth );
		assertTrue( "Ghi:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 5 ) instanceof JCheckBox );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridx );

		assertTrue( metawidget.getComponent( 6 ) instanceof JTabbedPane );
		assertTrue( arbitrary.equals( metawidget.getComponent( 7 )));
		assertTrue( arbitraryStubWithAttributes.equals( metawidget.getComponent( 8 )));
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridx );
		assertTrue( GridBagConstraints.REMAINDER == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 8 ) ).gridwidth );

		// Read-only on required labels

		metawidget.setReadOnly( true );
		assertTrue( "Def:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
	}

	//
	// Inner class
	//

	static class Foo
	{
		public String	abc;

		@UiComesAfter( "abc" )
		@UiRequired
		public int		def;

		@UiComesAfter( "def" )
		public boolean	ghi;

		@UiSection( "tab1")
		@UiComesAfter( "ghi" )
		@UiAttribute( name = "required", value = "true" )
		@UiReadOnly
		public String	tab1_jkl;

		@UiComesAfter( "tab1_jkl" )
		@UiLookup( { "foo", "bar" } )
		public String	tab1_mno;

		@UiComesAfter( "tab1_mno" )
		public String	tab1_pqr;

		@UiSection( "tab2")
		@UiComesAfter( "tab1_pqr" )
		@UiLarge
		@UiLabel( "" )
		public String	tab2_jkl;

		@UiSection( "tab3")
		@UiComesAfter( "tab2_jkl" )
		@UiLabel( "" )
		public String	tab3_jkl;

		@UiComesAfter( "tab3_jkl" )
		public String	tab3_mno;

		@UiComesAfter( "tab3_mno" )
		public String	tab3_pqr;

		@UiSection( "" )
		@UiComesAfter( "tab3_pqr" )
		public String	mno;
	}
}
