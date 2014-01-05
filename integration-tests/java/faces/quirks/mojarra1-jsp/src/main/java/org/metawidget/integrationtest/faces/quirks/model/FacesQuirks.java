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

package org.metawidget.integrationtest.faces.quirks.model;

import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.inspector.faces.UiFacesComponent;
import org.metawidget.inspector.faces.UiFacesLookup;
import org.metawidget.util.CollectionUtils;

/**
 * Models an entity that tests some Faces-specific quirks.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class FacesQuirks {

	//
	// Private members
	//

	private Boolean						mBoolean;

	private String						mLarge;

	private List<? extends Object>		mStrings					= CollectionUtils.newArrayList();

	private List<Integer>				mIntegers					= CollectionUtils.newArrayList();

	private String[]					mStringArray;

	private Set<String>					mReadOnlyLookupWithLabels	= CollectionUtils.newHashSet( "_foo", "_bar" );

	private List<HtmlDataTableQuirks>	mList						= CollectionUtils.newArrayList();

	//
	// Constructor
	//

	public FacesQuirks() {

		mList.add( new HtmlDataTableQuirks( 0, "Foo", "A Foo" ) );
		mList.add( new HtmlDataTableQuirks( 1, "Bar", "A Bar" ) );
		mList.add( new HtmlDataTableQuirks( 2, "Baz", "A Baz" ) );
	}

	//
	// Public methods
	//

	@UiFacesComponent( "javax.faces.HtmlSelectOneRadio" )
	@UiLabel( "#{40+2} boolean" )
	public Boolean getBooleanObject() {

		return mBoolean;
	}

	public void setBooleanObject( Boolean booleanObject ) {

		mBoolean = booleanObject;
	}

	@UiLarge
	@UiLabel( "" )
	@UiComesAfter( "booleanObject" )
	@UiRequired
	public String getLarge() {

		return mLarge;
	}

	public void setLarge( String large ) {

		mLarge = large;
	}

	@UiComesAfter( "large" )
	@UiLookup( { "Foo", "Bar", "Baz" } )
	public List<? extends Object> getStrings() {

		return mStrings;
	}

	public void setStrings( List<? extends Object> strings ) {

		mStrings = strings;
	}

	@UiComesAfter( "strings" )
	@UiFacesLookup( "#{quirks.possibleIntegers}" )
	public List<Integer> getIntegers() {

		return mIntegers;
	}

	public void setIntegers( List<Integer> integers ) {

		// Check converters are hooked up

		if ( integers != null ) {
			for ( Object integer : integers ) {
				if ( !( integer instanceof Integer ) ) {
					throw new ClassCastException( integer + " is not of type Integer" );
				}
			}
		}

		mIntegers = integers;
	}

	@UiComesAfter( "integers" )
	@UiLookup( { "Foo2", "Bar2", "Baz2" } )
	public String[] getStringArray() {

		return mStringArray;
	}

	public void setStringArray( String[] stringArray ) {

		mStringArray = new String[stringArray.length];
		System.arraycopy( stringArray, 0, mStringArray, 0, stringArray.length );
	}

	@UiHidden
	public SelectItem[] getPossibleIntegers() {

		return new SelectItem[] { new SelectItem( 1 ), new SelectItem( 2 ), new SelectItem( 3 ), new SelectItem( 4 ) };
	}

	@UiLookup( value = { "_foo", "_bar", "_baz" }, labels = { "Foo", "Bar", "Baz" } )
	@UiComesAfter( "stringArray" )
	public Set<String> getReadOnlyLookupWithLabels() {

		return mReadOnlyLookupWithLabels;
	}

	@UiComesAfter( "readOnlyLookupWithLabels" )
	public List<HtmlDataTableQuirks> getList() {

		return mList;
	}

	public void setList( List<HtmlDataTableQuirks> list ) {

		mList = list;
	}

	@UiAction
	@UiComesAfter
	public void refresh() {

		// Test refreshing the screen
	}
}
