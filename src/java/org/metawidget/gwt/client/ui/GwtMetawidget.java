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
import static org.metawidget.util.StringUtils.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.metawidget.gwt.client.binding.Binding;
import org.metawidget.gwt.client.layout.FlexTableLayout;
import org.metawidget.gwt.client.layout.Layout;
import org.metawidget.gwt.client.rpc.InspectorService;
import org.metawidget.gwt.client.rpc.InspectorServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

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
	extends SimplePanel
{
	//
	//
	// Private members
	//
	//

	private Object				mToInspect;

	private String				mPath;

	private Layout				mLayout;

	private boolean				mReadOnly;

	private Binding				mBinding;

	private Map<String, Widget>	mChildren;

	private Map<String, Facet>	mFacets	= new HashMap<String, Facet>();

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
	}

	public void setPath( String path )
	{
		mPath = path;
	}

	public void setReadOnly( boolean readOnly )
	{
		mReadOnly = readOnly;
	}

	public boolean isReadOnly()
	{
		return mReadOnly;
	}

	public void setLayout( Layout layout )
	{
		mLayout = layout;
	}

	/**
	 * Sets the binding interface for the domain object.
	 * <p>
	 * Generally, the object implementing the Binding interface will have been generated using
	 * <code>BindingAdapterGenerator</code>.
	 *
	 * @param binding
	 */

	public void setBinding( Binding binding )
	{
		// In most cases, the object implementing the Binding interface will have been generated
		// using BindingAdapterGenerator. It is also possible the domain object may choose to
		// implement Binding directly. However, setBinding should always be separate from
		// setToInspect - otherwise BindingAdapterGenerator would need to be a
		// BindingDelegateGenerator and generating different classes (even subclasses) can
		// affect the inspectors (eg. XmlInspector does not automatically know about subclasses)

		mBinding = binding;
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
			throw new RuntimeException( "No such widget " + GwtUtils.toString( names, ',' ) );

		// TextBox

		if ( widget instanceof TextBox )
			return ( (TextBox) widget ).getText();

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
		Widget widget = findWidget( names );

		if ( widget == null )
			throw new RuntimeException( "No such widget " + GwtUtils.toString( names, ',' ) );

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

		// Unknown (subclasses should override this)

		// throw new RuntimeException( "Don't know how to setValue of a " +
		// widget.getClass().getName() );
	}

	public void save()
	{
		throw new RuntimeException( "No binding configured. Use GwtMetawidget.setBinding" );
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
	}

	public Facet getFacet( String name )
	{
		return mFacets.get( name );
	}

	//
	//
	// Protected methods. This methods are all equivalent to
	// those in MetawidgetMixin, but GwtMetawidget doesn't use MetawidgetMixin
	// because a) that uses 'org.w3c.dom' and b) GwtMetawidget doesn't use
	// any files from outside the 'org.metawidget.gwt.client' folder
	//
	//

	protected void startBuild()
		throws Exception
	{
		clear();

		mChildren = new HashMap<String, Widget>();

		// Start layout

		if ( mLayout == null )
			mLayout = new FlexTableLayout( this );

		mLayout.layoutBegin();
	}

	public void buildWidgets()
	{
		// Inspect

		InspectorServiceAsync service = (InspectorServiceAsync) GWT.create( InspectorService.class );
		( (ServiceDefTarget) service ).setServiceEntryPoint( GWT.getModuleBaseURL() + "metawidget-inspector" );

		Object[] typeAndNames = GwtUtils.parsePath( mPath, SEPARATOR_SLASH_CHAR );

		try
		{
			service.inspect( (Serializable) mToInspect, (String) typeAndNames[0], (String[]) typeAndNames[1], new AsyncCallback<String>()
			{
				public void onFailure( Throwable caught )
				{
					Window.alert( caught.getMessage() );
				}

				public void onSuccess( String xml )
				{
					try
					{
						Document document = XMLParser.parse( xml );

						if ( document != null )
						{
							startBuild();

							// Build simple widget (from the top-level element)

							Element element = (Element) document.getDocumentElement().getFirstChild();
							Map<String, String> attributes = GwtUtils.getAttributesAsMap( element );

							Widget widget = buildWidget( attributes );

							if ( widget != null )
							{
								// If the returned widget is itself a Metawidget, it must have
								// the same path as us. In that case, DON'T use it, as that would
								// be infinite recursion

								if ( !( widget instanceof GwtMetawidget ) )
								{
									addWidget( widget, attributes );
								}

								// Failing that, build a compound widget (from our child elements)

								else
								{
									buildCompoundWidget( element );
								}
							}
						}

						// Even if no inspectors match, we still call endBuild()

						endBuild();
					}
					catch ( Exception e )
					{
						throw new RuntimeException( e );
					}
				}
			} );
		}
		catch ( Exception e )
		{
			throw new RuntimeException( e );
		}
	}

	protected void buildCompoundWidget( Element element )
		throws Exception
	{
		NodeList children = element.getChildNodes();

		for ( int loop = 0, length = children.getLength(); loop < length; loop++ )
		{
			Node node = children.item( loop );

			if ( !( node instanceof Element ) )
				continue;

			Element child = (Element) node;
			String childName = child.getAttribute( "name" );

			if ( childName == null || "".equals( childName ) )
				throw new RuntimeException( "Child element #" + loop + " of '" + element.getAttribute( "type" ) + "' has no @name" );

			Map<String, String> attributes = GwtUtils.getAttributesAsMap( child );

			Widget widget = getOverridenWidget( attributes );

			if ( widget == null )
			{
				widget = buildWidget( attributes );

				if ( widget == null )
					continue;

				if ( widget instanceof GwtMetawidget )
					widget = initMetawidget( (GwtMetawidget) widget, attributes );
			}
			else if ( isStub( widget ) )
			{
				attributes.putAll( getStubAttributes( widget ) );
			}

			addWidget( widget, attributes );
		}
	}

	protected boolean isReadOnly( Map<String, String> attributes )
	{
		if ( "true".equals( attributes.get( "read-only" ) ) )
			return true;

		return mReadOnly;
	}

	protected Widget buildWidget( Map<String, String> attributes )
		throws Exception
	{
		if ( isReadOnly( attributes ) )
			return buildReadOnlyWidget( attributes );

		return buildActiveWidget( attributes );
	}

	protected Widget initMetawidget( GwtMetawidget metawidget, Map<String, String> attributes )
		throws Exception
	{
		metawidget.setPath( mPath + '/' + attributes.get( NAME ) );
		metawidget.setLayout( mLayout.newInstance( metawidget ) );
		metawidget.setReadOnly( mReadOnly );
		metawidget.setToInspect( mToInspect );

		metawidget.buildWidgets();

		return metawidget;
	}

	/**
	 * @return false if the build should not proceed (for example if there was a previous validation
	 *         error)
	 */

	protected Widget getOverridenWidget( Map<String, String> attributes )
	{
		return null;
	}

	protected boolean isStub( Widget widget )
	{
		return false;
	}

	protected Map<String, String> getStubAttributes( Widget stub )
	{
		return null;
	}

	protected Widget buildReadOnlyWidget( Map<String, String> attributes )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		// Masked (return a Panel, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
			return new ScrollPanel();

		String type = attributes.get( TYPE );

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
			return new Label();

		// If no type, fail gracefully with a JTextField

		if ( type == null || "".equals( type ) )
			return new Label();

		if ( GwtUtils.isPrimitive( type ) )
			return new Label();

		if ( String.class.getName().equals( type ) )
			return new Label();

		if ( Date.class.getName().equals( type ) )
			return new Label();

		if ( Boolean.class.getName().equals( type ) )
			return new Label();

		// if ( Number.class.isAssignableFrom( clazz ) )
		// return new Label();

		// TODO: Collections

		// if ( HashSet.class.getName().equals( type ) )
		// return new FlexTable();

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

			return new TextBox();
		}

		// Dates

		if ( Date.class.getName().equals( type ) )
			return new TextBox();

		// Booleans (are tri-state)

		if ( Boolean.class.getName().equals( type ) )
		{
			ListBox listBox = new ListBox();
			addListBoxItem( listBox, null, null );
			addListBoxItem( listBox, "TRUE", "True" );
			addListBoxItem( listBox, "FALSE", "False" );

			return listBox;
		}

		// Numbers

		// if ( Number.class.isAssignableFrom( type ) )
		// return new TextBox();

		// Collections

		// if ( Collection.class.isAssignableFrom( type ) )
		// return new FlexTable();

		// Nested Metawidget

		return createMetawidget();
	}

	protected GwtMetawidget createMetawidget()
	{
		return new GwtMetawidget();
	}

	protected void addWidget( Widget widget, Map<String, String> attributes )
		throws Exception
	{
		String name = attributes.get( "name" );
		mChildren.put( name, widget );

		// Layout

		mLayout.layoutChild( widget, attributes );

		// Bind

		if ( mBinding != null )
		{
			setValue( mBinding.getProperty( name ), name );
		}
	}

	protected void endBuild()
		throws Exception
	{
		mLayout.layoutEnd();
	}

	//
	//
	// Other protected methods
	//
	//

	protected void addListBoxItems( ListBox listBox, List<String> values, List<String> labels, Map<String, String> attributes )
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

	protected void addListBoxItem( ListBox listBox, String value, String label )
	{
		if ( label != null )
		{
			listBox.addItem( label, value );
			return;
		}

		listBox.addItem( value );
	}

	protected Widget findWidget( String... names )
	{
		if ( names == null )
			return null;

		Map<String, Widget> children = mChildren;

		for ( int loop = 0, length = names.length; loop < length; loop++ )
		{
			if ( children == null )
				return null;

			String name = names[loop];
			Widget widget = mChildren.get( name );

			if ( widget == null )
				return null;

			if ( loop == length - 1 )
				return widget;

			if ( !( widget instanceof GwtMetawidget ) )
				return null;

			children = ( (GwtMetawidget) widget ).mChildren;
		}

		return null;
	}
}
