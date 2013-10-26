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

package org.metawidget.example.swing.animalraces

import java.awt.{BorderLayout, Color, GridLayout}
import java.awt.event.{ActionEvent, ActionListener}
import java.util.HashMap
import javax.swing._
import javax.swing.border._
import scala.annotation.target._

import org.metawidget.swing._
import org.metawidget.swing.layout._
import org.metawidget.swing.widgetprocessor.binding.beanutils._
import org.metawidget.inspector.annotation._
import org.metawidget.inspector.impl.propertystyle.scala._;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

//
// Model class
//

class Animal(
	var name:String,
	
	@(UiComesAfter @field)( Array( "name" )) @(UiAttribute @field)( name = Array( "minimum-value" ), value = "0" )
	var delay:Int,
	
	@(UiLookup @field)( Array( "Elephant", "Hippo", "Panda" )) @(UiRequired @getter) @(UiComesAfter @field)
	var animal:String
)

//
// UI
//

object AnimalRaces
{
	// Model instance
	
	val elephant = new Animal( "Eddie", 10, "Elephant" )
	val hippo = new Animal( "Harry", 5, "Hippo" )
	val panda = new Animal( "Paula", 2, "Panda" )

	// Look and feel
			
	UIManager.getInstalledLookAndFeels().foreach
	{
		info => if ( "Nimbus".equals( info.getName() ) )
		{
			UIManager.setLookAndFeel( info.getClassName() )
		}
	}
	
	// Toolbar (3 Metawidgets in a row)
	
	val elephantMetawidget = newAnimalMetawidget( elephant )
	val hippoMetawidget = newAnimalMetawidget( hippo )
	val pandaMetawidget = newAnimalMetawidget( panda )
	
	private def newAnimalMetawidget( animal:Animal ):SwingMetawidget =
	{
		val metawidget = new SwingMetawidget()
		metawidget.setConfig( "org/metawidget/example/swing/animalraces/metawidget.xml" )
		metawidget.addWidgetProcessor( new BeanUtilsBindingProcessor( new BeanUtilsBindingProcessorConfig().setPropertyStyle( new ScalaPropertyStyle() )))
		metawidget.setMetawidgetLayout( new MigLayout() )
		metawidget.setToInspect( animal )
		metawidget.getLayout().asInstanceOf[net.miginfocom.swing.MigLayout].setLayoutConstraints( new net.miginfocom.layout.LC().insets( "10" ));
		
		return metawidget
	}
	
	val toolbar = new JPanel
	{
		setLayout( new GridLayout( 1, 3 ))
		setBorder( BorderFactory.createEtchedBorder() )
		add( elephantMetawidget )
		add( hippoMetawidget )
		add( pandaMetawidget )
	}
	
	// Labels and animation timers
	
	val images = new HashMap[String, ImageIcon]
	addImageIcon( "Elephant" )
	addImageIcon( "Hippo" )
	addImageIcon( "Panda" )
 
	def addImageIcon( name:String )
	{
		val imageIcon = new ImageIcon( getClass().getResource( "/org/metawidget/example/swing/animalraces/media/" + name.toLowerCase() + ".png" ))
		images.put( name, imageIcon )
	}

	val elephantLabel = newLabel( elephant )
	val hippoLabel = newLabel( hippo )
	val pandaLabel = newLabel( panda )

	def newLabel( animal:Animal ) =
	{
		new JLabel( animal.name, images.get( animal.animal ), SwingConstants.CENTER )
	}
 
	val elephantTimer = newTimer( elephantLabel )
	val hippoTimer = newTimer( hippoLabel )
	val pandaTimer = newTimer( pandaLabel )

	def newTimer( label:JLabel ) =
	{
		implicit def actionPerformedWrapper(func: (ActionEvent) => Unit) = new ActionListener { def actionPerformed(e:ActionEvent) = func(e) }
		new Timer( 0, ((e:ActionEvent) => if ( label.getLocation().x < mainFrame.getWidth() - 200 ) label.setLocation( label.getLocation().x + 1, label.getLocation().y )))
	}
	
	// Racetrack
	
	def racetrack = new JPanel
	{
		setLayout( null )
		setBackground( new Color( 192, 255, 192 ))
		setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( new Color( 141, 195, 141 ), 5 ), BorderFactory.createLineBorder( new Color( 147, 233, 147 ), 5 )))

		addLabel( elephantLabel, 0 )
		addLabel( hippoLabel, 200 )
		addLabel( pandaLabel, 400 )
		
		@UiAction
		def startRace()
		{
			stopRace()
			
			startRace( elephantMetawidget, elephantLabel, elephantTimer )
			startRace( hippoMetawidget, hippoLabel, hippoTimer )
			startRace( pandaMetawidget, pandaLabel, pandaTimer )
		}

		@UiAction
		def stopRace()
		{
			stopRace( elephantLabel, elephantTimer )		
			stopRace( hippoLabel, hippoTimer )		
			stopRace( pandaLabel, pandaTimer )		
		}
		
		@UiAction
		@UiComesAfter
		def close()
		{
			System.exit( 0 )
		}
		
		private def addLabel( label:JLabel, top:Int )
		{
			label.setVerticalTextPosition( 1 )
   			label.setHorizontalTextPosition( 0 )
   			label.setLocation( 0, top );
			label.setSize( 200, 200 );
			add( label )				
		}

		private def startRace( metawidget:SwingMetawidget, label:JLabel, timer:Timer )
		{
			metawidget.getWidgetProcessor( classOf[ BeanUtilsBindingProcessor ] ).save( metawidget )
			val animal = metawidget.getToInspect().asInstanceOf[Animal]
			label.setText( animal.name )
			label.setIcon( images.get( animal.animal ))				
			timer.setDelay( animal.delay * 20 )
			timer.start()
		}
		
		private def stopRace( label:JLabel, timer:Timer )
		{
			timer.stop()
			label.setLocation( 0, label.getLocation().y );
		}
	}

	// Status bar (a Metawidget hooked into 'racetrack')
	
	val statusMetawidget = new SwingMetawidget();
	statusMetawidget.setConfig( "org/metawidget/example/swing/animalraces/metawidget.xml" )
	statusMetawidget.setMetawidgetLayout( new FlowLayout() )
	statusMetawidget.setToInspect( racetrack )			
	statusMetawidget.setBorder( BorderFactory.createEtchedBorder() )

	// JFrame
	
	val mainFrame = new JFrame( "Animal Races" )
	mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE )
	mainFrame.getContentPane().add( toolbar, BorderLayout.NORTH )
	mainFrame.getContentPane().add( racetrack, BorderLayout.CENTER )
	mainFrame.getContentPane().add( statusMetawidget, BorderLayout.SOUTH )
	mainFrame.setSize( 600, 790 )

	//
	// Main method
	//
	
	def main(args : Array[String]) =
	{
		mainFrame.setVisible( true )
	}		
}
