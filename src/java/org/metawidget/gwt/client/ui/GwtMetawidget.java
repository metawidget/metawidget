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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.metawidget.gwt.client.rpc.InspectorService;
import org.metawidget.gwt.client.rpc.InspectorServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
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
	extends VerticalPanel
{
	//
	//
	// Private members
	//
	//

	private Serializable		mToInspect;

	private String				mPath;

	private boolean				mReadOnly;

	private Map<String, Widget>	mChildren;

	//
	//
	// Public methods
	//
	//

	/**
	 * Sets the Object to inspect.
	 */

	public void setToInspect( Serializable toInspect )
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

	/**
	 * Gets the value from the Widget with the given name.
	 * <p>
	 * The value is returned as it is stored in the Widget (eg. String for TextBox) so may need some
	 * conversion before being reapplied to the object being inspected. This obviously requires
	 * knowledge of which Widget GwtMetawidget created, which is not ideal, so clients may prefer to
	 * use binding instead.
	 */

	// TODO: RuntimeExceptions should be MetawidgetExceptions
	public Object getValue( String... names )
	{
		Widget widget = findWidget( names );

		if ( widget == null )
			throw new RuntimeException( "No such widget" );

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
			throw new RuntimeException( "No such widget" );

		// TextBox

		if ( widget instanceof TextBox )
		{
			( (TextBox) widget ).setText( (String) value );
			return;
		}

		// Unknown (subclasses should override this)

		throw new RuntimeException( "Don't know how to setValue of a " + widget.getClass().getName() );
	}

	public void buildWidgets()
	{
		// Inspect

		InspectorServiceAsync service = (InspectorServiceAsync) GWT.create( InspectorService.class );
		( (ServiceDefTarget) service ).setServiceEntryPoint( GWT.getModuleBaseURL() + "metawidget-inspector" );

		TypeAndNames typeAndNames = parsePath( mPath, '/' );

		try
		{
			service.inspect( mToInspect, typeAndNames.getType(), typeAndNames.getNames(), new AsyncCallback<String>()
			{
				public void onFailure( Throwable caught )
				{
					// Failed
				}

				public void onSuccess( String result )
				{
					try
					{
						Document document = XMLParser.parse( result );

						if ( document != null )
						{
							startBuild();

							// Build simple widget (from the top-level element)

							Element element = (Element) document.getDocumentElement().getFirstChild();
							Map<String, String> attributes = getAttributesAsMap( element );

							// It is a little counter-intuitive that there can ever be an override
							// of the top-level element. However, if we go down the path that builds
							// a single widget (eg. doesn't invoke buildCompoundWidget), then our
							// child is at the same top-level as us, and there are some scenarios
							// (like
							// Java Server Faces POST backs) where we need to re-identify that

							Widget widget = getOverridenWidget( attributes );

							if ( widget == null )
								widget = buildWidget( attributes );

							if ( widget != null )
							{
								// If the returned component is itself a Metawidget, it must have
								// the same path as us. In that case, DON'T use it, as that would
								// be infinite recursion

								if ( !isMetawidget( widget ) )
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

						// Even if no inspectors match, we still call endBuild(). This makes us
						// behave better in visual builder tools when dropping child components in

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

	//
	//
	// Protected methods
	//
	//

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

			Map<String, String> attributes = getAttributesAsMap( child );

			Widget widget = getOverridenWidget( attributes );

			if ( widget == null )
			{
				widget = buildWidget( attributes );

				if ( widget == null )
					continue;

				if ( isMetawidget( widget ) )
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
		metawidget.setToInspect( mToInspect );
		metawidget.buildWidgets();

		return metawidget;
	}

	//
	//
	// Protected abstract methods
	//
	//

	/**
	 * @return false if the build should not proceed (for example if there was a previous validation
	 *         error)
	 */

	protected void startBuild()
		throws Exception
	{
		clear();
		mChildren = new HashMap<String, Widget>();
	}

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

	protected boolean isMetawidget( Widget widget )
	{
		return widget instanceof GwtMetawidget;
	}

	protected Widget buildReadOnlyWidget( Map<String, String> attributes )
		throws Exception
	{
		return new Label();
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

		// String Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
		{
			ListBox listBox = new ListBox();
			listBox.setVisibleItemCount( 1 );

			addListBoxItems( listBox, fromString( lookup, ',' ), fromString( attributes.get( LOOKUP_LABELS ), ',' ), attributes );
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

		return new GwtMetawidget();
	}

	protected void addWidget( Widget widget, Map<String, String> attributes )
		throws Exception
	{
		String name = attributes.get( "name" );
		Label label = new Label( name + ":" );
		add( label );
		add( widget );

		mChildren.put( name, widget );
	}

	protected void endBuild()
		throws Exception
	{
		// Empty for now
	}

	protected void addListBoxItems( ListBox listBox, String[] values, String[] labels, Map<String, String> attributes )
	{
		if ( values == null )
			return;

		// Add an empty choice (if nullable)

		// TODO:if ( ClassUtils.isPrimitive( attributes.get( TYPE ) ) )
		addListBoxItem( listBox, "", null );

		// See if we're using labels

		if ( labels != null && labels.length != 0 && labels.length != values.length )
			throw new RuntimeException( "Labels list must be same size as values list" );

		// Add the select items

		for ( int loop = 0, length = values.length; loop < length; loop++ )
		{
			String value = values[loop];
			String label = null;

			if ( labels != null && labels.length != 0 )
				label = labels[loop];

			addListBoxItem( listBox, value, label );
		}
	}

	protected void addListBoxItem( ListBox listBox, String value, String label )
	{
		if ( value == null )
		{
			listBox.addItem( "" );
			return;
		}

		if ( label != null )
		{
			listBox.addItem( label, value );
			return;
		}

		listBox.addItem( value );
	}

	//
	//
	// Other bits
	//
	//

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

			if ( !isMetawidget( widget ) )
				return null;

			children = ( (GwtMetawidget) widget ).mChildren;
		}

		return null;
	}

	protected static Map<String, String> getAttributesAsMap( Element element )
	{
		NamedNodeMap nodes = element.getAttributes();

		int length = nodes.getLength();

		if ( length == 0 )
			return Collections.emptyMap();

		Map<String, String> attributes = new HashMap<String, String>( length );

		for ( int loop = 0; loop < length; loop++ )
		{
			Node node = nodes.item( loop );
			attributes.put( node.getNodeName(), node.getNodeValue() );
		}

		return attributes;
	}

	protected static String[] fromString( String collection, char separator )
	{
		if ( collection == null )
			return new String[0];

		return collection.split( String.valueOf( separator ) );
	}

	protected static TypeAndNames parsePath( String path, char separator )
	{
		int indexOfTypeEnd = path.indexOf( separator );

		// Just type?

		if ( indexOfTypeEnd == -1 )
			return new TypeAndNames( path, null );

		String type = path.substring( 0, indexOfTypeEnd );

		// Parse names

		int indexOfNameEnd = indexOfTypeEnd;

		List<String> names = new ArrayList<String>();

		while ( true )
		{
			int indexOfNameStart = indexOfNameEnd + 1;
			indexOfNameEnd = path.indexOf( separator, indexOfNameStart );

			if ( indexOfNameEnd == -1 )
			{
				names.add( path.substring( indexOfNameStart ) );
				break;
			}

			names.add( path.substring( indexOfNameStart, indexOfNameEnd ) );
		}

		if ( names.isEmpty() )
			return new TypeAndNames( type, null );

		return new TypeAndNames( type, names.toArray( new String[names.size()] ) );
	}

	/**
	 * Tuple for returning a <code>type</code> and an array of <code>names</code>.
	 */

	public static class TypeAndNames
	{
		//
		//
		// Private methods
		//
		//

		private String		mType;

		private String[]	mNames;

		//
		//
		// Constructor
		//
		//

		public TypeAndNames( String type, String[] names )
		{
			mType = type;
			mNames = names;
		}

		//
		//
		// Public methods
		//
		//

		public String getType()
		{
			return mType;
		}

		public String[] getNames()
		{
			return mNames;
		}
	}
}
