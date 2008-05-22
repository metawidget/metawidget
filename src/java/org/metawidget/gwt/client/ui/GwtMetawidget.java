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

package org.metawidget.gwt.client.ui;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.util.simple.StringUtils.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.metawidget.gwt.client.binding.Binding;
import org.metawidget.gwt.client.binding.BindingFactory;
import org.metawidget.gwt.client.inspector.GwtInspector;
import org.metawidget.gwt.client.inspector.GwtInspectorAsync;
import org.metawidget.gwt.client.inspector.GwtInspectorFactory;
import org.metawidget.gwt.client.inspector.remote.GwtRemoteInspectorProxy;
import org.metawidget.gwt.client.ui.layout.FlexTableLayout;
import org.metawidget.gwt.client.ui.layout.Layout;
import org.metawidget.gwt.client.ui.layout.LayoutFactory;
import org.metawidget.impl.base.BaseMetawidgetMixin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;

/**
 * Metawidget for GWT environments.
 * <p>
 * Automatically creates native GWT Widgets, such as <code>TextBox</code> and <code>ListBox</code>,
 * to suit the inspected fields.
 * <p>
 * GWT compiles Java to JavaScript, and JavaScript lacks Java's comprehensive reflection support.
 * The only viable Inspector the JavaScript could run would be XmlInspector, and even that would
 * have to be considerably rewritten as GWT supplies its own variant of <code>org.w3c.dom</code>.
 * <p>
 * A more interesting solution is to have the JavaScript client send its objects (via AJAX) to the
 * Java server for inspection. The full power of Java Inspectors can then be brought to bear,
 * including inspecting annotations and server-side configuration files (such as
 * <code>hibernate.cfg.xml</code>).
 *
 * @author Richard Kennard
 */

public class GwtMetawidget
	extends FlowPanel
{
	//
	//
	// Private members
	//
	//

	private Object							mToInspect;

	private Class<? extends Layout>			mLayoutClass	= FlexTableLayout.class;

	private Layout							mLayout;

	private Class<? extends GwtInspector>	mInspectorClass	= GwtRemoteInspectorProxy.class;

	private GwtInspector					mInspector;

	/**
	 * The Binding class.
	 * <p>
	 * Binding class is <code>null</code> by default, because setting up Binding is non-trivial
	 * (eg. you have to generate some SimpleBindingAdapters)
	 */

	private Class<? extends Binding>		mBindingClass;

	private Binding							mBinding;

	private boolean							mNeedToBuildWidgets;

	private Map<String, Widget>				mWidgetNames;

	private Map<String, Stub>				mStubs			= new HashMap<String, Stub>();

	private Map<String, Facet>				mFacets			= new HashMap<String, Facet>();

	//
	//
	// Package-level members
	//
	//

	String									mPath;

	String[]								mNamesPrefix;

	GwtMetawidgetMixin						mMixin			= new GwtMetawidgetMixin();

	//
	//
	// Public methods
	//
	//

	public Object getToInspect()
	{
		return mToInspect;
	}

	/**
	 * Sets the Object to inspect.
	 */

	public void setToInspect( Object toInspect )
	{
		mToInspect = toInspect;

		if ( toInspect != null && ( mPath == null || mPath.indexOf( '/' ) == -1 ) )
			mPath = toInspect.getClass().getName();

		invalidateWidgets();
	}

	public void setPath( String path )
	{
		mPath = path;
		invalidateWidgets();
	}

	public void setInspectorClass( Class<? extends GwtInspector> inspectorClass )
	{
		mInspectorClass = inspectorClass;
		invalidateWidgets();
	}

	public void setLayoutClass( Class<? extends Layout> layoutClass )
	{
		mLayoutClass = layoutClass;
		invalidateWidgets();
	}

	/**
	 * @param bindingClass
	 *            may be null
	 */

	public void setBindingClass( Class<? extends Binding> bindingClass )
	{
		mBindingClass = bindingClass;
		invalidateWidgets();
	}

	public void setReadOnly( boolean readOnly )
	{
		mMixin.setReadOnly( readOnly );
		invalidateWidgets();
	}

	public boolean isReadOnly()
	{
		return mMixin.isReadOnly();
	}

	/**
	 * Finds the widget with the given name.
	 */

	public Widget findWidget( String... names )
	{
		if ( names == null )
			return null;

		Map<String, Widget> children = mWidgetNames;

		for ( int loop = 0, length = names.length; loop < length; loop++ )
		{
			if ( children == null )
				return null;

			String name = names[loop];
			Widget widget = mWidgetNames.get( name );

			if ( widget == null )
				return null;

			if ( loop == length - 1 )
				return widget;

			if ( !( widget instanceof GwtMetawidget ) )
				return null;

			children = ( (GwtMetawidget) widget ).mWidgetNames;
		}

		return null;
	}

	/**
	 * Gets the value from the Widget with the given name.
	 * <p>
	 * The value is returned as it is stored in the Widget (eg. String for TextBox) so may need some
	 * conversion before being reapplied to the object being inspected. This obviously requires
	 * knowledge of which Widget GwtMetawidget created, which is not ideal, so clients may prefer to
	 * use binding instead.
	 */

	public Object getValue( String... names )
	{
		Widget widget = findWidget( names );

		if ( widget == null )
			throw new RuntimeException( "No such widget " + GwtUtils.toString( names, SEPARATOR_DOT_CHAR ) );

		return getValue( widget );
	}

	/**
	 * Gets the value from the given Widget.
	 */

	public Object getValue( Widget widget )
	{
		// Label

		if ( widget instanceof Label )
			return ( (Label) widget ).getText();

		// TextBox

		if ( widget instanceof TextBox )
			return ( (TextBox) widget ).getText();

		// TextArea

		if ( widget instanceof TextArea )
			return ( (TextArea) widget ).getText();

		// CheckBox

		if ( widget instanceof CheckBox )
			return ( (CheckBox) widget ).isChecked();

		// ListBox

		if ( widget instanceof ListBox )
		{
			ListBox listBox = (ListBox) widget;
			return listBox.getValue( listBox.getSelectedIndex() );
		}

		// Unknown (subclasses should override this)

		throw new RuntimeException( "Don't know how to getValue from a " + widget.getClass().getName() );
	}

	/**
	 * Sets the Widget with the given name to the specified value.
	 * <p>
	 * Clients must ensure the value is of the correct type to suit the Widget (eg. String for
	 * TextBox). This obviously requires knowledge of which Widget GwtMetawidget created, which is
	 * not ideal, so clients may prefer to use binding instead.
	 */

	public void setValue( Object value, String... names )
	{
		buildWidgets();
		Widget widget = findWidget( names );

		if ( widget == null )
			throw new RuntimeException( "No such widget '" + GwtUtils.toString( names, ',' ) );

		setValue( value, widget );
	}

	/**
	 * Sets the given Widget to the specified value.
	 */

	public void setValue( Object value, Widget widget )
	{
		// Label

		if ( widget instanceof Label )
		{
			if ( value != null )
				( (Label) widget ).setText( String.valueOf( value ) );
			return;
		}

		// TextBox

		if ( widget instanceof TextBox )
		{
			if ( value != null )
				( (TextBox) widget ).setText( String.valueOf( value ) );
			return;
		}

		// TextArea

		if ( widget instanceof TextArea )
		{
			if ( value != null )
				( (TextArea) widget ).setText( String.valueOf( value ) );
			return;
		}

		// CheckBox

		if ( widget instanceof CheckBox )
		{
			( (CheckBox) widget ).setChecked( (Boolean) value );
			return;
		}

		// ListBox

		if ( widget instanceof ListBox )
		{
			ListBox listBox = (ListBox) widget;
			String valueString;

			if ( value == null )
				valueString = "";
			else
				valueString = String.valueOf( value );

			for ( int loop = 0, length = listBox.getItemCount(); loop < length; loop++ )
			{
				if ( valueString.equals( listBox.getValue( loop ) ) )
				{
					listBox.setSelectedIndex( loop );
					return;
				}
			}

			throw new RuntimeException( "'" + value + "' is not a valid value for the ListBox" );
		}

		// Unknown (subclasses should override this)

		throw new RuntimeException( "Don't know how to setValue of a " + widget.getClass().getName() );
	}

	/**
	 * Saves the values from the binding back to the object being inspected.
	 */

	// Note: this method avoids having to expose a getBinding() method, which is handy
	// because we can worry about nested Metawidgets here, not in the Binding class
	public void save()
	{
		// buildWidgets may be necessary here if we have nested Metawidgets
		// and have only ever called getValue (eg. never been visible)

		buildWidgets();

		if ( mBinding == null )
			throw new RuntimeException( "No binding configured. Use GwtMetawidget.setBindingClass" );

		mBinding.save();

		WidgetCollection widgets = getChildren();

		for ( int loop = 0, length = widgets.size(); loop < length; loop++ )
		{
			Widget widget = widgets.get( loop );

			if ( widget instanceof GwtMetawidget )
			{
				( (GwtMetawidget) widget ).save();
			}
		}
	}

	@Override
	public void add( Widget widget )
	{
		super.add( widget );

		if ( widget instanceof Facet )
		{
			Facet facet = (Facet) widget;
			mFacets.put( facet.getName(), facet );
		}
		else if ( widget instanceof Stub )
		{
			Stub stub = (Stub) widget;
			mStubs.put( stub.getName(), stub );
		}
	}

	public Facet getFacet( String name )
	{
		return mFacets.get( name );
	}

	protected void invalidateWidgets()
	{
		if ( mNeedToBuildWidgets )
			return;

		clear();

		mWidgetNames = new HashMap<String, Widget>();
		mNamesPrefix = null;

		if ( mBinding != null )
		{
			mBinding.unbind();
			mBinding = null;
		}

		mNeedToBuildWidgets = true;
	}

	//
	//
	// Protected methods
	//
	//

	protected void startBuild()
		throws Exception
	{
		// Start layout
		//
		// (we start a new layout each time, rather than complicating the Layouts with a
		// layoutCleanup method)

		mLayout = ( (LayoutFactory) GWT.create( LayoutFactory.class ) ).newLayout( mLayoutClass, this );
		mLayout.layoutBegin();

		// Start binding

		if ( mBindingClass != null )
			mBinding = ( (BindingFactory) GWT.create( BindingFactory.class ) ).newBinding( mBindingClass, this );
	}

	protected Widget getOverridenWidget( Map<String, String> attributes )
	{
		String name = attributes.get( NAME );

		return mStubs.get( name );
	}

	protected void beforeBuildCompoundWidget()
	{
		mNamesPrefix = (String[]) GwtUtils.parsePath( mPath, SEPARATOR_SLASH_CHAR )[1];
	}

	protected Widget buildReadOnlyWidget( Map<String, String> attributes )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		// Masked (return a Panel, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
			return new Stub();

		String type = attributes.get( TYPE );

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
			return new Label();

		// If no type, fail gracefully with a JTextField

		if ( type == null || "".equals( type ) )
			return new Label();

		if ( GwtUtils.isPrimitive( type ) || GwtUtils.isPrimitiveWrapper( type ) )
			return new Label();

		if ( String.class.getName().equals( type ) )
			return new Label();

		if ( Date.class.getName().equals( type ) )
			return new Label();

		// Collections

		if ( GwtUtils.isCollection( type ) )
			return new FlexTable();

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return new Label();

		// Nested Metawidget

		return createMetawidget();
	}

	protected Widget buildActiveWidget( Map<String, String> attributes )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		String type = attributes.get( TYPE );

		// If no type, fail gracefully with a TextBox

		if ( type == null || "".equals( type ) )
			return new TextBox();

		if ( GwtUtils.isPrimitive( type ) )
		{
			// booleans

			if ( "boolean".equals( type ) )
				return new CheckBox();

			// chars

			if ( "char".equals( type ) )
			{
				TextBox textbox = new TextBox();
				textbox.setMaxLength( 1 );

				return textbox;
			}

			// Everything else

			return new TextBox();
		}

		// String Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
		{
			ListBox listBox = new ListBox();
			listBox.setVisibleItemCount( 1 );

			addListBoxItems( listBox, GwtUtils.fromString( lookup, ',' ), GwtUtils.fromString( attributes.get( LOOKUP_LABELS ), ',' ), attributes );
			return listBox;
		}

		// Strings

		if ( String.class.getName().equals( type ) )
		{
			if ( TRUE.equals( attributes.get( MASKED ) ) )
				return new PasswordTextBox();

			if ( TRUE.equals( attributes.get( LARGE ) ) )
				return new TextArea();

			TextBox textBox = new TextBox();

			String maximumLength = attributes.get( MAXIMUM_LENGTH );

			if ( maximumLength != null && !"".equals( maximumLength ) )
				textBox.setMaxLength( Integer.parseInt( maximumLength ) );

			return textBox;
		}

		// Dates

		if ( Date.class.getName().equals( type ) )
			return new TextBox();

		if ( GwtUtils.isPrimitiveWrapper( type ) )
		{
			// Booleans (are tri-state)

			if ( Boolean.class.getName().equals( type ) )
			{
				ListBox listBox = new ListBox();
				addListBoxItem( listBox, null, null );
				addListBoxItem( listBox, "true", "True" );
				addListBoxItem( listBox, "false", "False" );

				return listBox;
			}

			// Characters

			if ( Character.class.getName().equals( type ) )
			{
				TextBox textbox = new TextBox();
				textbox.setMaxLength( 1 );

				return textbox;
			}

			// Numbers

			return new TextBox();
		}

		// Collections

		if ( GwtUtils.isCollection( type ) )
			return new FlexTable();

		// Nested Metawidget

		return createMetawidget();
	}

	protected void addWidget( Widget widget, Map<String, String> attributes )
		throws Exception
	{
		String name = attributes.get( "name" );
		mWidgetNames.put( name, widget );

		// Layout

		mLayout.layoutChild( widget, attributes );

		// Bind

		if ( mBinding != null && !( widget instanceof GwtMetawidget ) )
		{
			if ( mNamesPrefix == null )
				mBinding.bind( widget, name );
			else
				mBinding.bind( widget, GwtUtils.add( mNamesPrefix, name ) );
		}
	}

	public void buildWidgets()
	{
		// No need to build?

		if ( !mNeedToBuildWidgets )
			return;

		mNeedToBuildWidgets = false;

		// Inspect

		if ( mInspector == null )
			mInspector = ( (GwtInspectorFactory) GWT.create( GwtInspectorFactory.class ) ).newInspector( mInspectorClass );

		Object[] typeAndNames = GwtUtils.parsePath( mPath, SEPARATOR_SLASH_CHAR );
		String type = (String) typeAndNames[0];
		String[] names = (String[]) typeAndNames[1];

		// Special support for GwtInspectorAsync

		if ( mInspector instanceof GwtInspectorAsync )
		{
			( (GwtInspectorAsync) mInspector ).inspect( mToInspect, type, names, new AsyncCallback<Document>()
			{
				public void onFailure( Throwable caught )
				{
					Window.alert( caught.getMessage() );
				}

				public void onSuccess( Document document )
				{
					try
					{
						mMixin.buildWidgets( document );
					}
					catch ( Exception e )
					{
						throw new RuntimeException( e );
					}
				}
			} );
		}

		// Regular GwtInspectors

		else
		{
			try
			{
				Document document = mInspector.inspect( mToInspect, type, names );
				mMixin.buildWidgets( document );
			}
			catch ( Exception e )
			{
				Window.alert( e.getMessage() );
			}
		}
	}

	/**
	 * Subclasses should override to instantiate their own flavour of GwtMetawidget.
	 */

	protected GwtMetawidget createMetawidget()
	{
		return new GwtMetawidget();
	}

	protected Widget initMetawidget( GwtMetawidget metawidget, Map<String, String> attributes )
		throws Exception
	{
		metawidget.setPath( mPath + '/' + attributes.get( NAME ) );
		metawidget.setLayoutClass( mLayoutClass );
		metawidget.setBindingClass( mBindingClass );
		metawidget.setToInspect( mToInspect );

		return metawidget;
	}

	protected void endBuild()
		throws Exception
	{
		mLayout.layoutEnd();
	}

	//
	//
	// Inner class
	//
	//

	protected class GwtMetawidgetMixin
		extends BaseMetawidgetMixin<Widget, Document, Element, Node>
	{
		//
		//
		// Protected methods
		//
		//

		@Override
		protected void startBuild()
			throws Exception
		{
			GwtMetawidget.this.startBuild();
		}

		@Override
		protected Widget initMetawidget( Widget widget, Map<String, String> attributes )
			throws Exception
		{
			GwtMetawidget metawidget = (GwtMetawidget) widget;
			GwtMetawidget.this.initMetawidget( metawidget, attributes );
			metawidget.setReadOnly( isReadOnly( attributes ) );

			// TODO: remove this

			metawidget.buildWidgets();

			return metawidget;
		}

		@Override
		protected Widget getOverridenWidget( Map<String, String> attributes )
		{
			return GwtMetawidget.this.getOverridenWidget( attributes );
		}

		@Override
		protected Map<String, String> getStubAttributes( Widget stub )
		{
			return ( (Stub) stub ).getAttributes();
		}

		@Override
		protected void beforeBuildCompoundWidget( Element element )
		{
			GwtMetawidget.this.beforeBuildCompoundWidget();
		}

		@Override
		protected Widget buildReadOnlyWidget( Map<String, String> attributes )
			throws Exception
		{
			return GwtMetawidget.this.buildReadOnlyWidget( attributes );
		}

		@Override
		protected Widget buildActiveWidget( Map<String, String> attributes )
			throws Exception
		{
			return GwtMetawidget.this.buildActiveWidget( attributes );
		}

		@Override
		protected void addWidget( Widget widget, Map<String, String> attributes )
			throws Exception
		{
			GwtMetawidget.this.addWidget( widget, attributes );
		}

		@Override
		protected boolean isMetawidget( Widget widget )
		{
			return ( widget instanceof GwtMetawidget );
		}

		@Override
		protected boolean isStub( Widget widget )
		{
			return ( widget instanceof Stub );
		}

		@Override
		protected void endBuild()
			throws Exception
		{
			GwtMetawidget.this.endBuild();
		}

		@Override
		protected Element getFirstElement( Document document )
		{
			return (Element) document.getDocumentElement().getFirstChild();
		}

		@Override
		protected int getChildCount( Element element )
		{
			return element.getChildNodes().getLength();
		}

		@Override
		protected Node getChildAt( Element element, int index )
		{
			return element.getChildNodes().item( index );
		}

		@Override
		protected boolean isElement( Node node )
		{
			return ( node instanceof Element );
		}

		@Override
		protected Map<String, String> getAttributesAsMap( Element element )
		{
			NamedNodeMap nodes = element.getAttributes();

			int length = nodes.getLength();

			if ( length == 0 )
			{
				@SuppressWarnings( { "cast", "unchecked" } )
				Map<String, String> empty = (Map<String, String>) Collections.EMPTY_MAP;
				return empty;
			}

			Map<String, String> attributes = new HashMap<String, String>( length );

			for ( int loop = 0; loop < length; loop++ )
			{
				Node node = nodes.item( loop );
				attributes.put( node.getNodeName(), node.getNodeValue() );
			}

			return attributes;
		}
	}

	//
	//
	// Private methods
	//
	//

	private void addListBoxItems( ListBox listBox, List<String> values, List<String> labels, Map<String, String> attributes )
	{
		if ( values == null )
			return;

		// Add an empty choice (if nullable)
		//
		// Note: GWT doesn't seem to be able to set null for the
		// value. It always comes back as String "null"

		if ( !GwtUtils.isPrimitive( attributes.get( TYPE ) ) )
			addListBoxItem( listBox, "", null );

		// See if we're using labels

		if ( labels != null && !labels.isEmpty() && labels.size() != values.size() )
			throw new RuntimeException( "Labels list must be same size as values list" );

		// Add the select items

		for ( int loop = 0, length = values.size(); loop < length; loop++ )
		{
			String value = values.get( loop );
			String label = null;

			if ( labels != null && !labels.isEmpty() )
				label = labels.get( loop );

			addListBoxItem( listBox, value, label );
		}
	}

	private void addListBoxItem( ListBox listBox, String value, String label )
	{
		if ( label != null )
		{
			listBox.addItem( label, value );
			return;
		}

		listBox.addItem( value );
	}
}
