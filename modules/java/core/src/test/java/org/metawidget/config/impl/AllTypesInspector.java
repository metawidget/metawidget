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

package org.metawidget.config.impl;

import java.io.InputStream;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;

import org.metawidget.config.impl.AllTypesInspectorConfig.FooEnum;
import org.metawidget.inspector.iface.Inspector;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class AllTypesInspector
	implements Inspector {

	//
	// Private members
	//

	private List<Object>	mList;

	private Set<Object>		mSet;

	private int				mInt;

	private int				mConstant;

	private int				mExternalConstant;

	private boolean			mBoolean;

	private Pattern			mPattern;

	private InputStream		mInputStream;

	private ResourceBundle	mResourceBundle;

	private String[]		mStringArray;

	private FooEnum			mEnum;

	//
	// Constructor
	//

	public AllTypesInspector( AllTypesInspectorConfig config ) {

		if ( config.isFailDuringConstruction() ) {
			throw new RuntimeException( "Failed during construction" );
		}

		mList = config.getList();
		mSet = config.getSet();
		mInt = config.getInt();
		mConstant = config.getConstant();
		mExternalConstant = config.getExternalConstant();
		mBoolean = config.isBoolean();
		mPattern = config.getPattern();
		mInputStream = config.getInputStream();
		mResourceBundle = config.getResourceBundle();
		mStringArray = config.getStringArray();
		mEnum = config.getEnum();
	}

	/**
	 * @param config
	 */

	public AllTypesInspector( NoEqualsInspectorConfig config ) {

		// Do nothing
	}

	/**
	 * @param config
	 */

	public AllTypesInspector( NoHashCodeInspectorConfig config ) {

		// Do nothing
	}

	/**
	 * @param config
	 */

	public AllTypesInspector( UnbalancedEqualsInspectorConfig config ) {

		// Do nothing
	}

	/**
	 * @param config
	 */

	public AllTypesInspector( NoEqualsSubclassInspectorConfig config ) {

		// Do nothing
	}

	/**
	 * @param config
	 */

	public AllTypesInspector( NoEqualsHasMethodsSubclassInspectorConfig config ) {

		// Do nothing
	}

	/**
	 * @param config
	 */

	public AllTypesInspector( DumbHashCodeInspectorConfig config ) {

		// Do nothing
	}

	//
	// Public methods
	//

	public String inspect( Object toInspect, String type, String... names ) {

		return null;
	}

	public List<Object> getList() {

		return mList;
	}

	public Set<Object> getSet() {

		return mSet;
	}

	public int getInt() {

		return mInt;
	}

	public int getConstant() {

		return mConstant;
	}

	public int getExternalConstant() {

		return mExternalConstant;
	}

	public boolean isBoolean() {

		return mBoolean;
	}

	public Pattern getPattern() {

		return mPattern;
	}

	public InputStream getInputStream() {

		return mInputStream;
	}

	public ResourceBundle getResourceBundle() {

		return mResourceBundle;
	}

	public String[] getStringArray() {

		return mStringArray;
	}

	public FooEnum getEnum() {

		return mEnum;
	}
}
