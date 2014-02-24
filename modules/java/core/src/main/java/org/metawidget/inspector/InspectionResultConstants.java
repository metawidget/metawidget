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

package org.metawidget.inspector;

/**
 * Common element and attribute names appearing in DOMs conforming to inspection-result-1.0.xsd.
 * <p>
 * Metawidget promotes a loose coupling between Inspectors and widget creation. In particular, it
 * discourages any single Inspector dictating an XML attribute name, as that Inspector then holds a
 * 'monopoly' on how that attribute gets initialised (eg. it should not matter whether a
 * <code>required</code> attribute came from a JPA annotation, a Hibernate mapping file or Hibernate
 * Validator).
 * <p>
 * Metawidget also promotes that, as both Inspectors and widget creation evolve independently, the
 * XML attributes recognized should evolve in an 'ad hoc' fashion: Metawidget places no restrictions
 * on what attribute names can be used.
 * <p>
 * However, for those attribute names that have become sufficiently standardized, it is desirable to
 * have a more formalized 'middle ground', if only to avoid typos.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class InspectionResultConstants {

	//
	// Public statics
	//

	public static final String	NAMESPACE						= "http://metawidget.org/inspection-result";

	public static final String	ROOT							= "inspection-result";

	public static final String	VERSION							= "version";

	public static final String	ENTITY							= "entity";

	public static final String	PROPERTY						= "property";

	public static final String	ACTION							= "action";

	/**
	 * Name attribute.
	 * <p>
	 * Name is the <em>only</em> guide to behaviour when Metawidget is merging DOM trees from
	 * multiple Inspectors. No other attributes, element tag names or document structure are
	 * considered. For this reason, all names within any branch of the DOM-subtree must be unique.
	 */

	public static final String	NAME							= "name";

	/**
	 * Type attribute.
	 * <p>
	 * For Java classes, this should be the fully-qualified, instantiatable class name (for example,
	 * it should not have &lt;K,V&gt; or &lt;String&gt; generic arguments appended to it)
	 * <p>
	 * For more abstract concepts, this can be any unique identifier (eg. 'Login Screen').
	 */

	public static final String	TYPE							= "type";

	public static final String	COMES_AFTER						= "comes-after";

	/**
	 * Parameterized type arguments, for example the type of elements in a Set.
	 * <p>
	 * Often this comes from generics, but it can come from other sources (eg. Hibernate mapping
	 * files, UiAttribute annotations).
	 */

	public static final String	PARAMETERIZED_TYPE				= "parameterized-type";

	/**
	 * Read-only field attribute.
	 */

	public static final String	READ_ONLY						= "read-only";

	/**
	 * Whether the property has no setter method.
	 * <p>
	 * Properties without setters are <em>not</em> automatically considered <code>READ_ONLY</code>.
	 * If the property is a complex type (eg. Address), it may be writable by setting each of its
	 * nested primitives (eg. Street, City, etc) even though in itself it is 'not settable'.
	 */

	public static final String	NO_SETTER						= "no-setter";

	/**
	 * Required field attribute.
	 * <p>
	 * Can be set by, say, <code>org.hibernate.validator.NotNull</code> or
	 * <code>javax.persistence.Column( nullable = false )</code>.
	 */

	public static final String	REQUIRED						= "required";

	/**
	 * Disabled field attribute.
	 */

	public static final String	DISABLED						= "disabled";

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

	public static final String	HIDDEN							= "hidden";

	/**
	 * Possible field values are to be looked up through the given set of Strings.
	 */

	public static final String	LOOKUP							= "lookup";

	/**
	 * Possible field values are to be presented as the given set of Strings.
	 */

	public static final String	LOOKUP_LABELS					= "lookup-labels";

	/**
	 * Force the lookup to have an empty choice in cases where it would normally be suppressed (eg.
	 * on primitive or required fields)
	 */

	public static final String	LOOKUP_HAS_EMPTY_CHOICE			= "lookup-has-empty-choice";

	/**
	 * Masked field attribute.
	 * <p>
	 * Whether the field is 'masked' (eg. a password field). Expected values are <code>true</code>
	 * or <code>false</code>. Note this attribute does <em>not</em> denote 'masked' as in a date
	 * format or a number format.
	 */

	public static final String	MASKED							= "masked";

	/**
	 * Label field attribute.
	 */

	public static final String	LABEL							= "label";

	public static final String	SECTION							= "section";

	/**
	 * Field is a 'large' field, such as a BLOB or a CLOB.
	 */

	public static final String	LARGE							= "large";

	/**
	 * Field is a 'wide' field, spanning all columns in a multi-column layout.
	 * <p>
	 * Wide is different to 'large', because 'large' implies a data size (ie. BLOB or CLOB) whereas
	 * 'wide' refers purely to spanning columns. Generally all 'large' fields are implicitly 'wide',
	 * but not all 'wide' fields are 'large'. For example, you may want a normal text field (not a
	 * text area) to span all columns.
	 */

	public static final String	WIDE							= "wide";

	/**
	 * The minimum value this field can contain. May be a floating point number.
	 */

	public static final String	MINIMUM_VALUE					= "minimum-value";

	/**
	 * The maximum value this field can contain. May be a floating point number.
	 */

	public static final String	MAXIMUM_VALUE					= "maximum-value";

	/**
	 * The minimum length of this field (eg. number of characters)
	 */

	public static final String	MINIMUM_LENGTH					= "minimum-length";

	/**
	 * The maximum length of this field (eg. number of characters).
	 * <p>
	 * Can be set by, say, <code>org.hibernate.validator.Length</code> or
	 * <code>javax.persistence.Column( length )</code>.
	 */

	public static final String	MAXIMUM_LENGTH					= "maximum-length";

	/**
	 * The minimum number of integer digits in this field.
	 */

	public static final String	MINIMUM_INTEGER_DIGITS			= "minimum-integer-digits";

	/**
	 * The maximum number of integer digits in this field.
	 */

	public static final String	MAXIMUM_INTEGER_DIGITS			= "maximum-integer-digits";

	/**
	 * The minimum number of fractional digits in this field.
	 */

	public static final String	MINIMUM_FRACTIONAL_DIGITS		= "minimum-fractional-digits";

	/**
	 * The maximum number of fractional digits in this field.
	 */

	public static final String	MAXIMUM_FRACTIONAL_DIGITS		= "maximum-fractional-digits";

	/**
	 * Don't expand field attribute.
	 */

	public static final String	DONT_EXPAND						= "dont-expand";

	/**
	 * When a true/false attribute is true.
	 * <p>
	 * This stops ambiguity around using 'true', 't', 'yes', etc.
	 */

	public static final String	TRUE							= "true";

	/**
	 * When a true/false attribute is false.
	 */

	public static final String	FALSE							= "false";

	/**
	 * The field in a <em>child</em> entity that relates back to us (i.e. JPA's
	 * <code>mappedBy</code>).
	 */

	public static final String	INVERSE_RELATIONSHIP			= "inverse-relationship";

	//
	// Rarer fields (only used by Faces components so far)
	//

	/**
	 * ISO 4217 currency code to be applied when formatting currencies.
	 */

	public static final String	CURRENCY_CODE					= "currency-code";

	public static final String	CURRENCY_SYMBOL					= "currency-symbol";

	/**
	 * Whether the formatted output should contain grouping separators (eg. commas).
	 */

	public static final String	NUMBER_USES_GROUPING_SEPARATORS	= "number-uses-grouping-separators";

	public static final String	LOCALE							= "locale";

	public static final String	NUMBER_PATTERN					= "number-pattern";

	/**
	 * The type of the number, such as 'currency' or 'percentage'.
	 */

	public static final String	NUMBER_TYPE						= "number-type";

	public static final String	DATE_STYLE						= "date-style";

	public static final String	TIME_STYLE						= "time-style";

	public static final String	DATETIME_PATTERN				= "datetime-pattern";

	public static final String	TIME_ZONE						= "time-zone";

	public static final String	DATETIME_TYPE					= "datetime-type";

	/**
	 * Regular expression pattern specified by validation library.
	 */

	public static final String	VALIDATION_PATTERN				= "validation-pattern";

	//
	// Private constructor
	//

	private InspectionResultConstants() {

		// Can never be called
	}
}
