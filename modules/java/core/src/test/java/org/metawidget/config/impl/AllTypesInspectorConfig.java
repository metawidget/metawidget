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
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;

import org.metawidget.util.simple.ObjectUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class AllTypesInspectorConfig {

	//
	// Public statics
	//

	public static final int	CONSTANT_VALUE	= 42;

	//
	// Private members
	//

	private List<Object>	mList;

	private Set<Object>		mSet;

	private int				mInt;

	private int				mConstant;

	private int				mExternalConstant;

	private boolean			mBooleanPrimitive;

	private Pattern			mPattern;

	private InputStream		mInputStream;

	private ResourceBundle	mResourceBundle;

	private String[]		mStringArray;

	private FooEnum			mEnum;

	private boolean			mFailDuringConstruction;

	//
	// Public methods
	//

	public void setList( List<Object> list ) {

		mList = list;
	}

	public void setSet( Set<Object> set ) {

		mSet = set;
	}

	public void setInt( int anInt ) {

		mInt = anInt;
	}

	public void setConstant( int constant ) {

		mConstant = constant;
	}

	public void setExternalConstant( int externalConstant ) {

		mExternalConstant = externalConstant;
	}

	public void setBooleanPrimitive( boolean booleanPrimitive ) {

		mBooleanPrimitive = booleanPrimitive;
	}

	public void setPattern( Pattern pattern ) {

		mPattern = pattern;
	}

	public void setInputStream( InputStream inputStream ) {

		mInputStream = inputStream;
	}

	public void setResourceBundle( ResourceBundle resourceBundle ) {

		mResourceBundle = resourceBundle;
	}

	public void setStringArray( String[] stringArray ) {

		mStringArray = stringArray;
	}

	public void setEnum( FooEnum anEnum ) {

		mEnum = anEnum;
	}

	/**
	 * @param failDuringConstruction
	 */

	public void setFailDuringConstruction( boolean failDuringConstruction ) {

		mFailDuringConstruction = true;
	}

	/**
	 * @param date
	 */

	public void setDate( Date date ) {

		// Test unsupported types
	}

	public void setNoParameters() {

		throw new UnsupportedOperationException( "Called setNoParameters" );
	}

	@Override
	public boolean equals( Object that ) {

		if ( this == that ) {
			return true;
		}

		if ( !ObjectUtils.nullSafeClassEquals( this, that )) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mList, ( (AllTypesInspectorConfig) that ).mList ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mSet, ( (AllTypesInspectorConfig) that ).mSet ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mInt, ( (AllTypesInspectorConfig) that ).mInt ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mBooleanPrimitive, ( (AllTypesInspectorConfig) that ).mBooleanPrimitive ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mPattern, ( (AllTypesInspectorConfig) that ).mPattern ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mInputStream, ( (AllTypesInspectorConfig) that ).mInputStream ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mResourceBundle, ( (AllTypesInspectorConfig) that ).mResourceBundle ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mStringArray, ( (AllTypesInspectorConfig) that ).mStringArray ) ) {
			return false;
		}

		if ( !ObjectUtils.nullSafeEquals( mFailDuringConstruction, ( (AllTypesInspectorConfig) that ).mFailDuringConstruction ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mList );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mSet );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mInt );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mBooleanPrimitive );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mPattern );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mInputStream );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mResourceBundle );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mStringArray );
		hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode( mFailDuringConstruction );

		return hashCode;
	}

	//
	// Protected methods
	//

	protected List<Object> getList() {

		return mList;
	}

	protected Set<Object> getSet() {

		return mSet;
	}

	protected int getConstant() {

		return mConstant;
	}

	protected int getExternalConstant() {

		return mExternalConstant;
	}

	protected boolean isBoolean() {

		return mBooleanPrimitive;
	}

	protected int getInt() {

		return mInt;
	}

	protected Pattern getPattern() {

		return mPattern;
	}

	protected InputStream getInputStream() {

		return mInputStream;
	}

	protected ResourceBundle getResourceBundle() {

		return mResourceBundle;
	}

	protected String[] getStringArray() {

		return mStringArray;
	}

	protected FooEnum getEnum() {

		return mEnum;
	}

	protected boolean isFailDuringConstruction() {

		return mFailDuringConstruction;
	}

	//
	// Inner class
	//

	static enum FooEnum {

		FOO, BAR, BAZ
	}
}
