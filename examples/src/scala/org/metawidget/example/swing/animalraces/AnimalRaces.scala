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

import org.metawidget.swing._
import org.metawidget.swing.layout._
import org.metawidget.swing.propertybinding.beanutils._
import org.metawidget.inspector.annotation._
import java.awt.{BorderLayout, Color, GridLayout}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing._
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border._

package org.metawidget.example.swing.animalraces
{
	//
	// Model class
	//

	class Animal( var name:String, @UiComesAfter( Array( "name" )) var delay:int )

	//
	// UI
	//

	object AnimalRaces
	{
		//
		// Model instance
		//
		
		var elephant = new Animal( "Eddie the Elephant", 10 )
		var hippo = new Animal( "Harry the Hippo", 5 )
		var panda = new Animal( "Paula the Panda", 2 )

		//
		// Look and feel
		//
				
		UIManager.getInstalledLookAndFeels().foreach
		{
			info => if ( "Nimbus".equals( info.getName() ) )
			{
				UIManager.setLookAndFeel( info.getClassName() )
			}
		}
		
		//
		// Toolbar
		//
		
		val elephantMetawidget = new SwingMetawidget()
		val hippoMetawidget = new SwingMetawidget()
		val pandaMetawidget = new SwingMetawidget()
		val elephantLabel = new JLabel( elephant.name, new ImageIcon( getClass().getResource( "/org/metawidget/example/swing/animalraces/media/elephant.png" )), 0 )
		val hippoLabel = new JLabel( hippo.name, new ImageIcon( getClass().getResource( "/org/metawidget/example/swing/animalraces/media/hippo.png" )), 0 )
		val pandaLabel = new JLabel( panda.name, new ImageIcon( getClass().getResource( "/org/metawidget/example/swing/animalraces/media/panda.png" )), 0 )

		def toolbar = new JPanel
		{
			setLayout( new GridLayout( 1, 3 ))
			setBorder( BorderFactory.createEtchedBorder() )

			elephantMetawidget.setInspectorConfig( "org/metawidget/example/swing/animalraces/inspector-config.xml" )
			elephantMetawidget.setPropertyBindingClass( classOf[ BeanUtilsBinding ])
			elephantMetawidget.setParameter( "propertyStyle", BeanUtilsBinding.PROPERTYSTYLE_SCALA )
			elephantMetawidget.setLayoutClass( classOf[ MigLayout ])
			elephantMetawidget.setToInspect( elephant )
			elephantMetawidget.getLayout().asInstanceOf[net.miginfocom.swing.MigLayout].setLayoutConstraints( new net.miginfocom.layout.LC().insets( "10" ));
			add( elephantMetawidget )

			hippoMetawidget.setInspectorConfig( "org/metawidget/example/swing/animalraces/inspector-config.xml" )
			hippoMetawidget.setPropertyBindingClass( classOf[ BeanUtilsBinding ])
			hippoMetawidget.setParameter( "propertyStyle", BeanUtilsBinding.PROPERTYSTYLE_SCALA )
			hippoMetawidget.setLayoutClass( classOf[ MigLayout ])
			hippoMetawidget.setToInspect( hippo )
			hippoMetawidget.getLayout().asInstanceOf[net.miginfocom.swing.MigLayout].setLayoutConstraints( new net.miginfocom.layout.LC().insets( "10" ));
			add( hippoMetawidget )

			pandaMetawidget.setInspectorConfig( "org/metawidget/example/swing/animalraces/inspector-config.xml" )
			pandaMetawidget.setPropertyBindingClass( classOf[ BeanUtilsBinding ])
			pandaMetawidget.setParameter( "propertyStyle", BeanUtilsBinding.PROPERTYSTYLE_SCALA )
			pandaMetawidget.setLayoutClass( classOf[ MigLayout ])
			pandaMetawidget.setToInspect( panda )
			pandaMetawidget.getLayout().asInstanceOf[net.miginfocom.swing.MigLayout].setLayoutConstraints( new net.miginfocom.layout.LC().insets( "10" ));
			add( pandaMetawidget )
		}

		//
		// Racetrack
		//
		
		val mainFrame = new JFrame( "Animal Races" )

		def racetrack = new JPanel
		{
			setLayout( null )
			setBackground( new Color( 192, 255, 192 ))
			setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( new Color( 141, 195, 141 ), 5 ), BorderFactory.createLineBorder( new Color( 147, 233, 147 ), 5 )))
			
   			elephantLabel.setVerticalTextPosition( 1 )
   			elephantLabel.setHorizontalTextPosition( 0 )
   			elephantLabel.setLocation( 0, 0 );
   			elephantLabel.setSize( 200, 200 );
			add( elephantLabel )

   			hippoLabel.setVerticalTextPosition( 1 )
   			hippoLabel.setHorizontalTextPosition( 0 )
   			hippoLabel.setLocation( 0, 200 );
   			hippoLabel.setSize( 200, 200 );
			add( hippoLabel )

   			pandaLabel.setVerticalTextPosition( 1 )
   			pandaLabel.setHorizontalTextPosition( 0 )
   			pandaLabel.setLocation( 0, 400 );
   			pandaLabel.setSize( 200, 200 );
			add( pandaLabel )

			@UiAction
			def startRace()
			{
				stopRace()
				
				elephantMetawidget.save()
				elephantLabel.setText( elephant.name )
				elephantLabel.setLocation( 0, elephantLabel.getLocation().y );
				elephantTimer.setDelay( elephant.delay )
				elephantTimer.start()		

				hippoMetawidget.save()
				hippoLabel.setText( hippo.name )
				hippoLabel.setLocation( 0, hippoLabel.getLocation().y );
				hippoTimer.setDelay( hippo.delay )
				hippoTimer.start()		

				pandaMetawidget.save()
				pandaLabel.setText( panda.name )
				pandaLabel.setLocation( 0, pandaLabel.getLocation().y );
				pandaTimer.setDelay( panda.delay )
				pandaTimer.start()		
			}

			@UiAction
			def stopRace()
			{
				elephantTimer.stop()		
				hippoTimer.stop()		
				pandaTimer.stop()
			}
			
			@UiAction
			@UiComesAfter
			def close()
			{
				System.exit( 0 )
			}
		}

		//
		// Status bar
		//
		
		val statusMetawidget = new SwingMetawidget();
		statusMetawidget.setInspectorConfig( "org/metawidget/example/swing/animalraces/inspector-config.xml" )
		statusMetawidget.setLayoutClass( classOf[ FlowLayout ])
		statusMetawidget.setToInspect( racetrack )			
		statusMetawidget.setBorder( BorderFactory.createEtchedBorder() )

		//
		// JFrame
		//
		
		mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE )
		mainFrame.getContentPane().add( toolbar, BorderLayout.NORTH )
		mainFrame.getContentPane().add( racetrack, BorderLayout.CENTER )
		mainFrame.getContentPane().add( statusMetawidget, BorderLayout.SOUTH )
		mainFrame.setSize( 600, 750 )

		//
		// Animation timers
		//
		
		implicit def actionPerformedWrapper(func: (ActionEvent) => Unit) = new ActionListener { def actionPerformed(e:ActionEvent) = func(e) }

		def elephantTimer = new Timer( elephant.delay, ((e:ActionEvent) => if ( elephantLabel.getLocation().x < mainFrame.getWidth() - 200 ) elephantLabel.setLocation( elephantLabel.getLocation().x + 1, 0 )))
		def hippoTimer = new Timer( hippo.delay, ((e:ActionEvent) => if ( hippoLabel.getLocation().x < mainFrame.getWidth() - 200 ) hippoLabel.setLocation( hippoLabel.getLocation().x + 1, 200 )))
		def pandaTimer = new Timer( panda.delay, ((e:ActionEvent) => if ( pandaLabel.getLocation().x < mainFrame.getWidth() - 200 ) pandaLabel.setLocation( pandaLabel.getLocation().x + 1, 400 )))
		
		//
		// Main method
		//
		
		def main(args : Array[String]) =
		{
			mainFrame.setVisible( true )
		}		
	}
}
