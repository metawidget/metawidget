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

package org.metawidget.swing.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Component;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.metawidget.MetawidgetException;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetbuilder.impl.BaseWidgetBuilder;

/**
 * WidgetBuilder for Swing environments.
 * <p>
 * Automatically creates native Swing <code>JComponents</code>, such as <code>JTextField</code>
 * and <code>JComboBox</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

public class SwingWidgetBuilder
	extends BaseWidgetBuilder<JComponent, SwingMetawidget>
{
	//
	// Protected methods
	//

	@Override
	protected JComponent buildReadOnlyWidget( String elementName, Map<String, String> attributes, SwingMetawidget metawidget )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return new Stub();

		// Action

		if ( ACTION.equals( elementName ) )
			return new Stub();

		// Masked (return a JPanel, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
			return new JPanel();

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
		{
			// May have alternate labels

			String lookupLabels = attributes.get( LOOKUP_LABELS );

			if ( lookupLabels != null && !"".equals( lookupLabels ) )
				return new LookupLabel( getLabelsMap( CollectionUtils.fromString( lookup ), CollectionUtils.fromString( lookupLabels ) ) );

			return new JLabel();
		}

		String type = attributes.get( TYPE );

		// If no type, fail gracefully with a JTextField

		if ( type == null || "".equals( type ) )
			return new JLabel();

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz != null )
		{
			// Primitives

			if ( clazz.isPrimitive() )
				return new JLabel();

			if ( String.class.equals( clazz ) )
			{
				if ( TRUE.equals( attributes.get( LARGE ) ) )
				{
					// Avoid just using a JLabel, in case the contents are
					// really large. This also helps the layout of this field
					// be consistent with its activated, JTextArea version

					JLabel label = new JLabel();
					label.setVerticalAlignment( SwingConstants.TOP );

					JScrollPane scrollPane = new JScrollPane( label );
					scrollPane.setOpaque( false );
					scrollPane.getViewport().setOpaque( false );
					scrollPane.setBorder( null );
					return scrollPane;
				}

				return new JLabel();
			}

			if ( Date.class.equals( clazz ) )
				return new JLabel();

			if ( Boolean.class.equals( clazz ) )
				return new JLabel();

			if ( Number.class.isAssignableFrom( clazz ) )
				return new JLabel();

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return new Stub();
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return new JLabel();

		// Nested Metawidget

		return null;
	}

	@Override
	protected JComponent buildActiveWidget( String elementName, Map<String, String> attributes, SwingMetawidget metawidget )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return new Stub();

		// Action

		if ( ACTION.equals( elementName ) )
			return new JButton( metawidget.getLabelString( attributes ) );

		String type = attributes.get( TYPE );

		// If no type, fail gracefully with a JTextField

		if ( type == null || "".equals( type ) )
			return new JTextField();

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
		{
			JComboBox comboBox = new JComboBox();

			// Add an empty choice (if nullable, and not required)
			//
			// Note: there's an extra caveat for Groovy dynamic types: if we can't load
			// the class, assume it is non-primitive and therefore add a null choice
			// (unless 'required=true' is specified)

			// TODO: Swing Address Book still has empty choice for 'title'?

			if ( ( clazz == null || !clazz.isPrimitive() ) && !TRUE.equals( attributes.get( REQUIRED ) ) )
				comboBox.addItem( null );

			List<String> values = CollectionUtils.fromString( lookup );

			for ( String value : values )
			{
				Object convertedValue = metawidget.convertFromString( value, clazz );

				comboBox.addItem( convertedValue );
			}

			// May have alternate labels

			String lookupLabels = attributes.get( LOOKUP_LABELS );

			if ( lookupLabels != null && !"".equals( lookupLabels ) )
			{
				Map<String, String> labelsMap = getLabelsMap( values, CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ) );

				comboBox.setEditor( new LookupComboBoxEditor( labelsMap ) );
				comboBox.setRenderer( new LookupComboBoxRenderer( labelsMap ) );
			}

			return comboBox;
		}

		if ( clazz != null )
		{
			// Primitives

			if ( clazz.isPrimitive() )
			{
				// booleans

				if ( boolean.class.equals( clazz ) )
					return new JCheckBox();

				// chars

				if ( char.class.equals( clazz ) )
					return new JTextField();

				// Ranged

				String minimumValue = attributes.get( MINIMUM_VALUE );
				String maximumValue = attributes.get( MAXIMUM_VALUE );

				if ( minimumValue != null && !"".equals( minimumValue ) && maximumValue != null && !"".equals( maximumValue ) )
				{
					JSlider slider = new JSlider();
					slider.setMinimum( Integer.parseInt( minimumValue ) );
					slider.setMaximum( Integer.parseInt( maximumValue ) );

					return slider;
				}

				// Not-ranged

				JSpinner spinner = new JSpinner();

				// (use 'new', not '.valueOf', for JDK 1.4 compatibility)

				if ( byte.class.equals( clazz ) )
				{
					byte minimum = Byte.MIN_VALUE;
					byte maximum = Byte.MAX_VALUE;

					if ( minimumValue != null && !"".equals( minimumValue ) )
						minimum = Byte.parseByte( minimumValue );

					if ( maximumValue != null && !"".equals( maximumValue ) )
						maximum = Byte.parseByte( maximumValue );

					setSpinnerModel( spinner, (byte) 0, minimum, maximum, (byte) 1 );
				}
				else if ( short.class.equals( clazz ) )
				{
					short minimum = Short.MIN_VALUE;
					short maximum = Short.MAX_VALUE;

					if ( minimumValue != null && !"".equals( minimumValue ) )
						minimum = Short.parseShort( minimumValue );

					if ( maximumValue != null && !"".equals( maximumValue ) )
						maximum = Short.parseShort( maximumValue );

					setSpinnerModel( spinner, (short) 0, minimum, maximum, (short) 1 );
				}
				else if ( int.class.equals( clazz ) )
				{
					int minimum = Integer.MIN_VALUE;
					int maximum = Integer.MAX_VALUE;

					if ( minimumValue != null && !"".equals( minimumValue ) )
						minimum = Integer.parseInt( minimumValue );

					if ( maximumValue != null && !"".equals( maximumValue ) )
						maximum = Integer.parseInt( maximumValue );

					setSpinnerModel( spinner, 0, minimum, maximum, 1 );
				}
				else if ( long.class.equals( clazz ) )
				{
					long minimum = Long.MIN_VALUE;
					long maximum = Long.MAX_VALUE;

					if ( minimumValue != null && !"".equals( minimumValue ) )
						minimum = Long.parseLong( minimumValue );

					if ( maximumValue != null && !"".equals( maximumValue ) )
						maximum = Long.parseLong( maximumValue );

					setSpinnerModel( spinner, (long) 0, minimum, maximum, (long) 1 );
				}
				else if ( float.class.equals( clazz ) )
				{
					float minimum = -Float.MAX_VALUE;
					float maximum = Float.MAX_VALUE;

					if ( minimumValue != null && !"".equals( minimumValue ) )
						minimum = Float.parseFloat( minimumValue );

					if ( maximumValue != null && !"".equals( maximumValue ) )
						maximum = Float.parseFloat( maximumValue );

					setSpinnerModel( spinner, (float) 0, minimum, maximum, (float) 0.1 );
				}
				else if ( double.class.equals( clazz ) )
				{
					double minimum = -Double.MAX_VALUE;
					double maximum = Double.MAX_VALUE;

					if ( minimumValue != null && !"".equals( minimumValue ) )
						minimum = Double.parseDouble( minimumValue );

					if ( maximumValue != null && !"".equals( maximumValue ) )
						maximum = Double.parseDouble( maximumValue );

					setSpinnerModel( spinner, (double) 0, minimum, maximum, 0.1 );
				}

				return spinner;
			}

			// Strings

			if ( String.class.equals( clazz ) )
			{
				if ( TRUE.equals( attributes.get( MASKED ) ) )
					return new JPasswordField();

				if ( TRUE.equals( attributes.get( LARGE ) ) )
				{
					JTextArea textarea = new JTextArea();

					// Since we know we are dealing with Strings, we consider
					// word-wrapping a sensible default

					textarea.setLineWrap( true );
					textarea.setWrapStyleWord( true );

					// We also consider 2 rows a sensible default, so that the
					// JTextArea is always distinguishable from a JTextField

					textarea.setRows( 2 );
					return new JScrollPane( textarea );
				}

				return new JTextField();
			}

			// Dates

			if ( Date.class.equals( clazz ) )
				return new JTextField();

			// Booleans (are tri-state)

			if ( Boolean.class.equals( clazz ) )
			{
				JComboBox comboBox = new JComboBox();

				// TODO: checkbox if required!
				// TODO: allow onclick and stuff to be applied to the UIComponent

				comboBox.addItem( null );
				comboBox.addItem( Boolean.TRUE );
				comboBox.addItem( Boolean.FALSE );

				return comboBox;
			}

			// Numbers
			//
			// Note: we use a text field, not a JSpinner or JSlider, because
			// BeansBinding gets upset at doing 'setValue( null )' if the Integer
			// is null. We can still use JSpinner/JSliders for primitives, though.

			if ( Number.class.isAssignableFrom( clazz ) )
				return new JTextField();

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return new Stub();
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return new JTextField();

		// Nested Metawidget

		return null;
	}

	//
	// Private methods
	//

	/**
	 * Sets the JSpinner model.
	 * <p>
	 * By default, a JSpinner calls <code>setColumns</code> upon <code>setModel</code>. For
	 * numbers like <code>Integer.MAX_VALUE</code> and <code>Double.MAX_VALUE</code>, this can
	 * be very large and mess up the layout. Here, we reset <code>setColumns</code> to 0.
	 * <p>
	 * Note it is very important we set the initial value of the <code>JSpinner</code> to the same
	 * type as the property it maps to (eg. float or double, int or long).
	 */

	private void setSpinnerModel( JSpinner spinner, Number value, Comparable<? extends Number> minimum, Comparable<? extends Number> maximum, Number stepSize )
	{
		spinner.setModel( new SpinnerNumberModel( value, minimum, maximum, stepSize ) );
		( (JSpinner.DefaultEditor) spinner.getEditor() ).getTextField().setColumns( 0 );
	}

	private Map<String, String> getLabelsMap( List<String> values, List<String> labels )
	{
		Map<String, String> labelsMap = CollectionUtils.newHashMap();

		if ( labels.size() != values.size() )
			throw MetawidgetException.newException( "Labels list must be same size as values list" );

		for ( int loop = 0, length = values.size(); loop < length; loop++ )
		{
			labelsMap.put( values.get( loop ), labels.get( loop ) );
		}

		return labelsMap;
	}

	//
	// Inner class
	//

	/**
	 * Label whose values use a lookup
	 */

	public static class LookupLabel
		extends JLabel
	{
		//
		//
		// Private statics
		//
		//

		private static final long	serialVersionUID	= 1l;

		//
		//
		// Private members
		//
		//

		private Map<String, String>	mLookup;

		//
		//
		// Constructor
		//
		//

		public LookupLabel( Map<String, String> lookup )
		{
			if ( lookup == null )
				throw new NullPointerException( "lookup" );

			mLookup = lookup;
		}

		//
		//
		// Public methods
		//
		//

		@Override
		public void setText( String text )
		{
			String lookup = null;

			if ( text != null && mLookup != null )
				lookup = mLookup.get( text );

			super.setText( lookup );
		}
	}

	/**
	 * Editor for ComboBox whose values use a lookup.
	 */

	private static class LookupComboBoxEditor
		extends BasicComboBoxEditor
	{
		//
		//
		// Private members
		//
		//

		private Map<String, String>	mLookups;

		//
		//
		// Constructor
		//
		//

		public LookupComboBoxEditor( Map<String, String> lookups )
		{
			if ( lookups == null )
				throw new NullPointerException( "lookups" );

			mLookups = lookups;
		}

		//
		//
		// Public methods
		//
		//

		@Override
		public void setItem( Object item )
		{
			super.setItem( mLookups.get( item ) );
		}
	}

	/**
	 * Renderer for ComboBox whose values use a lookup.
	 */

	private static class LookupComboBoxRenderer
		extends BasicComboBoxRenderer
	{
		//
		//
		// Private statics
		//
		//

		private final static long	serialVersionUID	= 1l;

		//
		//
		// Private members
		//
		//

		private Map<String, String>	mLookups;

		//
		//
		// Constructor
		//
		//

		public LookupComboBoxRenderer( Map<String, String> lookups )
		{
			if ( lookups == null )
				throw new NullPointerException( "lookups" );

			mLookups = lookups;
		}

		//
		//
		// Public methods
		//
		//

		@Override
		public Component getListCellRendererComponent( JList list, Object value, int index, boolean selected, boolean hasFocus )
		{
			Component component = super.getListCellRendererComponent( list, value, index, selected, hasFocus );

			String lookup = mLookups.get( value );

			if ( lookup != null )
				( (JLabel) component ).setText( lookup );

			return component;
		}
	}
}
