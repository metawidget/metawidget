// Metawidget (licensed under LGPL)
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

package org.metawidget.integrationtest.swing.userguide;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.SwingValuePropertyProvider;
import org.metawidget.swing.widgetbuilder.OverriddenWidgetBuilder;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.swing.widgetprocessor.binding.beansbinding.BeansBindingProcessor;
import org.metawidget.util.ArrayUtils;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilderConfig;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class WidgetBuilderExampleTest
	extends TestCase {

	//
	// Public methods
	//

	@SuppressWarnings( "unchecked" )
	public void testWidgetProcessorExample()
		throws Exception {

		WidgetProcessorExampleModel person = new WidgetProcessorExampleModel();

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setWidgetBuilder( new CompositeWidgetBuilder<JComponent, SwingMetawidget>( new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>().setWidgetBuilders( new JRadioButtonWidgetBuilder(), new SwingWidgetBuilder() ) ) );
		metawidget.setToInspect( person );

		assertTrue( metawidget.getComponent( 1 ) instanceof JSpinner );
		assertTrue( metawidget.getComponent( 3 ) instanceof JTextField );
		assertTrue( metawidget.getComponent( 5 ) instanceof JPanel );
		assertTrue( ( (JPanel) metawidget.getComponent( 5 ) ).getComponent( 0 ) instanceof JRadioButton );
		assertTrue( ( (JPanel) metawidget.getComponent( 5 ) ).getComponent( 1 ) instanceof JRadioButton );
	}

	/**
	 * Test of http://blog.kennardconsulting.com/2010/10/metawidget-jradiobutton-instead-of.html
	 */

	@SuppressWarnings( "unchecked" )
	public void testExtendedWidgetProcessorExample()
		throws Exception {

		ExtendedWidgetProcessorExampleModel person = new ExtendedWidgetProcessorExampleModel();
		person.setTitle( "Miss" );

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setWidgetBuilder( new CompositeWidgetBuilder<JComponent, SwingMetawidget>( new CompositeWidgetBuilderConfig<JComponent, SwingMetawidget>().setWidgetBuilders( new OverriddenWidgetBuilder(), new GenericJRadioButtonWidgetBuilder(), new SwingWidgetBuilder() ) ) );
		metawidget.addWidgetProcessor( new BeansBindingProcessor() );
		metawidget.setToInspect( person );

		ButtonGroupPanel panel = (ButtonGroupPanel) metawidget.getComponent( 1 );
		assertEquals( "Mr", ( (JRadioButton) panel.getComponent( 0 ) ).getText() );
		assertEquals( "Mr", ( (JRadioButton) panel.getComponent( 0 ) ).getActionCommand() );
		assertTrue( !( (JRadioButton) panel.getComponent( 0 ) ).isSelected() );
		assertEquals( "Mrs", ( (JRadioButton) panel.getComponent( 1 ) ).getText() );
		assertEquals( "Mrs", ( (JRadioButton) panel.getComponent( 1 ) ).getActionCommand() );
		assertTrue( !( (JRadioButton) panel.getComponent( 1 ) ).isSelected() );
		assertEquals( "Miss", ( (JRadioButton) panel.getComponent( 2 ) ).getText() );
		assertEquals( "Miss", ( (JRadioButton) panel.getComponent( 2 ) ).getActionCommand() );
		assertTrue( ( (JRadioButton) panel.getComponent( 2 ) ).isSelected() );
		assertEquals( "Dr", ( (JRadioButton) panel.getComponent( 3 ) ).getText() );
		assertEquals( "Dr", ( (JRadioButton) panel.getComponent( 3 ) ).getActionCommand() );
		assertTrue( !( (JRadioButton) panel.getComponent( 3 ) ).isSelected() );
		assertEquals( "Dr", ( (JRadioButton) panel.getComponent( 3 ) ).getText() );
		assertEquals( "Dr", ( (JRadioButton) panel.getComponent( 3 ) ).getActionCommand() );
		assertTrue( !( (JRadioButton) panel.getComponent( 4 ) ).isSelected() );
		assertEquals( 5, panel.getComponentCount() );

		panel.setSelected( "Cpt" );
		assertTrue( ( (JRadioButton) panel.getComponent( 4 ) ).isSelected() );
		assertTrue( !( (JRadioButton) panel.getComponent( 2 ) ).isSelected() );
		assertEquals( "Miss", person.getTitle() );
		metawidget.getWidgetProcessor( BeansBindingProcessor.class ).save( metawidget );
		assertEquals( "Cpt", person.getTitle() );
	}

	//
	// Inner class
	//

	static class JRadioButtonWidgetBuilder
		implements WidgetBuilder<JComponent, SwingMetawidget> {

		public JComponent buildWidget( String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

			if ( !"boolean".equals( attributes.get( TYPE ) ) ) {
				return null;
			}

			JRadioButton trueButton = new JRadioButton( "True" );
			JRadioButton falseButton = new JRadioButton( "False" );
			JPanel panel = new JPanel();
			panel.setLayout( new GridLayout( 2, 1 ) );
			panel.add( trueButton );
			panel.add( falseButton );

			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add( trueButton );
			buttonGroup.add( falseButton );

			return panel;
		}
	}

	/**
	 * Test of http://blog.kennardconsulting.com/2010/10/metawidget-jradiobutton-instead-of.html
	 */

	static class GenericJRadioButtonWidgetBuilder
		implements WidgetBuilder<JComponent, SwingMetawidget>, SwingValuePropertyProvider {

		public JComponent buildWidget( String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

			String lookupAttribute = attributes.get( LOOKUP );

			if ( lookupAttribute == null ) {
				return null;
			}

			String[] lookups = ArrayUtils.fromString( lookupAttribute );
			String[] lookupLabels = ArrayUtils.fromString( attributes.get( LOOKUP_LABELS ) );

			ButtonGroupPanel panel = new ButtonGroupPanel();
			panel.setLayout( new GridLayout( 1, lookups.length ) );

			for ( int loop = 0; loop < lookups.length; loop++ ) {

				JRadioButton radioButton = new JRadioButton();

				if ( lookupLabels.length == 0 ) {
					radioButton.setText( lookups[loop] );
				} else {
					radioButton.setText( lookupLabels[loop] );
				}

				radioButton.setActionCommand( lookups[loop] );
				panel.add( radioButton );
			}

			return panel;
		}

		public String getValueProperty( Component component ) {

			if ( component instanceof ButtonGroupPanel ) {
				return "selected";
			}

			return null;
		}
	}

	public static class ButtonGroupPanel
		extends JPanel {

		private ButtonGroup	mButtonGroup	= new ButtonGroup();

		public String getSelected() {

			ButtonModel buttonModel = mButtonGroup.getSelection();

			if ( buttonModel == null ) {
				return null;
			}

			return buttonModel.getActionCommand();
		}

		public void setSelected( String selected ) {

			for ( Enumeration<AbstractButton> e = mButtonGroup.getElements(); e.hasMoreElements(); ) {

				AbstractButton button = e.nextElement();

				if ( !selected.equals( button.getActionCommand() ) ) {
					continue;
				}

				String oldValue = getSelected();
				mButtonGroup.setSelected( button.getModel(), true );
				firePropertyChange( "selected", oldValue, selected );
				break;
			}
		}

		@Override
		protected void addImpl( Component component, Object constraints, int index ) {

			super.addImpl( component, constraints, index );

			if ( component instanceof AbstractButton ) {
				mButtonGroup.add( (AbstractButton) component );
			}
		}
	}

	public static class WidgetProcessorExampleModel {

		private String	mName;

		private int		mAge;

		private boolean	mRetired;

		public String getName() {

			return mName;
		}

		public void setName( String name ) {

			mName = name;
		}

		public int getAge() {

			return mAge;
		}

		public void setAge( int age ) {

			mAge = age;
		}

		public boolean isRetired() {

			return mRetired;
		}

		public void setRetired( boolean retired ) {

			mRetired = retired;
		}
	}

	public static class ExtendedWidgetProcessorExampleModel {

		//
		// Private members
		//

		private String	mTitle;

		//
		// Public methods
		//

		@UiLookup( { "Mr", "Mrs", "Miss", "Dr", "Cpt" } )
		public String getTitle() {

			return mTitle;
		}

		public void setTitle( String title ) {

			mTitle = title;
		}
	}
}
