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

package org.metawidget.inspector;

/**
 * Common element and attribute names appearing in DOMs conforming to inspection-result-1.0.xsd.
 * <p>
 * Metawidget promotes a loose coupling between Inspectors and widget creation. In particular, it
 * discourages any single Inspector dictating an XML attribute name, as that Inspector then holds a
 * 'monopoly' on how that attribute gets initialised (eg. it should not matter whether a
 * <code>required</code> attribute came from a JPA annotation, a Hibernate mapping file or
 * Hibernate Validator).
 * <p>
 * Metawidget also promotes that, as both Inspectors and widget creation evolve independently, the
 * XML attributes recognized should evolve in an 'ad hoc' fashion: Metawidget places no restrictions
 * on what attribute names can be used.
 * <p>
 * However, for those attribute names that have become sufficiently standardized, it is desirable to
 * have a more formalized 'middle ground', if only to avoid typos.
 *
 * @author Richard Kennard
 */

public final class InspectionResultConstants
{
	//
	// Public statics
	//

	public final static String	NAMESPACE					= "http://metawidget.org/inspection-result";

	public final static String	ROOT						= "inspection-result";

	public final static String	VERSION						= "version";

	public final static String	ENTITY						= "entity";

	public final static String	PROPERTY					= "property";

	public final static String	ACTION						= "action";

	/**
	 * Name attribute.
	 * <p>
	 * Name is the <em>only</em> guide to behaviour when Metawidget is merging DOM trees from
	 * multiple Inspectors. No other attributes, element tag names or document structure are
	 * considered. For this reason, all names within any branch of the DOM-subtree must be unique.
	 */

	public final static String	NAME						= "name";

	/**
	 * Type attribute.
	 * <p>
	 * For Java classes, this should be the fully-qualified, instantiatable class name (for example,
	 * it should not have &lt;K,V&gt; or &lt;String&gt; generic arguments appended to it)
	 * <p>
	 * For more abstract concepts, this can be any unique identifier (eg. 'Login Screen').
	 */

	public final static String	TYPE						= "type";

	/**
	 * Parameterized type arguments, for example the type of elements in a Set.
	 * <p>
	 * Often this comes from Java5 generics, but it can come from other sources (eg. Hibernate
	 * mapping files).
	 */

	public final static String	PARAMETERIZED_TYPE			= "parameterized-type";

	/**
	 * Read-only field attribute.
	 */

	public final static String	READ_ONLY					= "read-only";

	/**
	 * Write-only field attribute.
	 */

	public final static String	WRITE_ONLY					= "write-only";

	/**
	 * Required field attribute.
	 * <p>
	 * Can be set by, say, <code>org.hibernate.validator.NotNull</code> or
	 * <code>javax.persistence.Column( nullable = false )</code>.
	 */

	public final static String	REQUIRED					= "required";

	/**
	 * Hidden field attribute.
	 * <p>
	 * Even though a field is 'hidden' from the user, it may still be a necessary part of the User
	 * Interface, and must therefore be part of the inspection result. For example, HTML metawidgets
	 * may need to render hidden fields using <code>input type=&quot;hidden&quot;</code> tags.
	 * <p>
	 * Also, it is not possible for an Inspector to hide a field by simply not returning it, as
	 * other Inspectors in the chain (who are not concerned with a 'hidden' attribute) will add it
	 * back in.
	 */

	public final static String	HIDDEN						= "hidden";

	/**
	 * Possible field values are to be looked up through the given set of Strings.
	 */

	public final static String	LOOKUP						= "lookup";

	/**
	 * Possible field values are to be presented as the given set of Strings.
	 */

	public final static String	LOOKUP_LABELS				= "lookup-labels";

	/**
	 * Force the lookup to have an empty choice in cases where it would normally be suppressed (eg.
	 * on primitive or required fields)
	 */

	public final static String	LOOKUP_HAS_EMPTY_CHOICE		= "lookup-has-empty-choice";

	/**
	 * Masked field attribute.
	 * <p>
	 * Whether the field is 'masked' (eg. a password field). Expected values are <code>true</code>
	 * or <code>false</code>. Note this attribute does <em>not</em> denote 'masked' as in a date
	 * format or a number format.
	 */

	public final static String	MASKED						= "masked";

	/**
	 * Label field attribute.
	 */

	public final static String	LABEL						= "label";

	public final static String	SECTION						= "section";

	/**
	 * Field is a 'large' field, such as a BLOB or a CLOB.
	 */

	public final static String	LARGE						= "large";

	/**
	 * The minimum value this field can contain.
	 */

	public final static String	MINIMUM_VALUE				= "minimum-value";

	/**
	 * The maximum value this field can contain.
	 */

	public final static String	MAXIMUM_VALUE				= "maximum-value";

	/**
	 * The minimum length of this field (eg. number of characters)
	 */

	public final static String	MINIMUM_LENGTH				= "minimum-length";

	/**
	 * The maximum length of this field (eg. number of characters)
	 * <p>
	 * Can be set by, say, <code>org.hibernate.validator.Length</code> or
	 * <code>javax.persistence.Column( length )</code>.
	 */

	public final static String	MAXIMUM_LENGTH				= "maximum-length";

	/**
	 * The minimum number of integer digits in this field
	 */

	public final static String	MINIMUM_INTEGER_DIGITS		= "minimum-integer-digits";

	/**
	 * The maximum number of integer digits in this field
	 */

	public final static String	MAXIMUM_INTEGER_DIGITS		= "maximum-integer-digits";

	/**
	 * The minimum number of fractional digits in this field
	 */

	public final static String	MINIMUM_FRACTIONAL_DIGITS	= "minimum-fractional-digits";

	/**
	 * The maximum number of fractional digits in this field
	 */

	public final static String	MAXIMUM_FRACTIONAL_DIGITS	= "maximum-fractional-digits";

	/**
	 * Don't expand field attribute.
	 */

	public final static String	DONT_EXPAND					= "dont-expand";

	/**
	 * When a true/false attribute is true.
	 * <p>
	 * This stops ambiguity around using 'true', 't', 'yes', etc.
	 */

	public final static String	TRUE						= "true";

	/**
	 * When a true/false attribute is false.
	 */

	public final static String	FALSE						= "false";

	//
	// Private constructor
	//

	private InspectionResultConstants()
	{
		// Can never be called
	}
}
