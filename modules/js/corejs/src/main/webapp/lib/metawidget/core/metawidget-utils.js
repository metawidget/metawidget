// Metawidget ${project.version}
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

var metawidget = metawidget || {};

( function() {

	'use strict';

	/**
	 * @namespace Utilities.
	 */

	metawidget.util = metawidget.util || {};

	/**
	 * Returns a label for the given set of attributes.
	 * <p>
	 * The label is determined using the following algorithm:
	 * <p>
	 * <ul>
	 * <li> if <tt>attributes.title</tt> exists...
	 * <ul>
	 * <li>if the given <tt>mw</tt> has a property <tt>l10n</tt>, then
	 * <tt>attributes.title</tt> is camel-cased and used as a lookup into
	 * <tt>mw.i10n[camelCasedTitle]</tt>. This means developers can initially
	 * build their UIs without worrying about localization, then turn it on
	 * later</li>
	 * <li>if no such lookup exists (or <tt>mw.l10n</tt> does not exist),
	 * return <tt>attributes.title</tt>
	 * </ul>
	 * </li>
	 * <li> if <tt>attributes.title</tt> does not exist...
	 * <ul>
	 * <li>if the given <tt>mw</tt> has a property <tt>l10n</tt>, then
	 * <tt>attributes.name</tt> is used as a lookup into
	 * <tt>mw.i10n[attributes.name]</tt></li>
	 * <li>if no such lookup exists (or <tt>mw.l10n</tt> does not exist),
	 * return <tt>attributes.name</tt>
	 * </ul>
	 * </li>
	 * </ul>
	 * 
	 * @return the label string. Empty string if no such name. Null if name has
	 *         been forced to blank (i.e. should be hidden)
	 * 
	 */

	metawidget.util.getLabelString = function( attributes, mw ) {

		// Explicit title

		if ( attributes.title !== undefined ) {

			if ( attributes.title === null ) {
				return null;
			}

			return metawidget.util.getLocalizedString( attributes.title, mw );
		}

		// Localize if possible

		var name = attributes.name;

		if ( mw.l10n !== undefined && mw.l10n[name] !== undefined ) {
			return mw.l10n[name];
		}

		// Default name, uncamel case

		return metawidget.util.uncamelCase( name );
	};

	/**
	 * Uncamel case the given name (e.g. from 'fooBarBaz1' to 'Foo Bar Baz 1').
	 * Ported from StringUtils.uncamelCase.
	 */

	metawidget.util.uncamelCase = function( name ) {

		/**
		 * @returns true if the character is a digit.
		 */

		function _isDigit( c ) {

			var charCode = c.charCodeAt( 0 );
			return ( charCode >= 48 && charCode <= 57 );
		}

		/**
		 * @returns true if the character is an upper or lower case letter.
		 */

		function _isLetter( c ) {

			var charCode = c.charCodeAt( 0 );
			return ( charCode >= 65 && charCode <= 90 ) || ( charCode >= 97 && charCode <= 122 );
		}

		var uncamelCasedName = '';
		var first = true;
		var lastChar = ' ';

		for ( var loop = 0; loop < name.length; loop++ ) {
			
			// Use 'charAt', not '[]' for IE compatibility
			
			var c = name.charAt( loop );

			if ( first === true ) {
				uncamelCasedName += c.toUpperCase();
				first = false;
			} else if ( _isUpperCase( c ) && ( !_isUpperCase( lastChar ) || ( loop < name.length - 1 && name[loop + 1] !== ' ' && !_isUpperCase( name[loop + 1] ) ) ) ) {
				if ( lastChar !== ' ' ) {
					uncamelCasedName += ' ';
				}

				// Don't do: if ( loop + 1 < length && !_isUpperCase( chars[loop
				// + 1] ) ) uncamelCasedName += _toLowerCase( c );
				//
				// It's ambiguous if we should lowercase the letter following a
				// space, but in general it looks nicer most of the time not to.
				// The exception is 'joining' words such as 'of' in 'Date of
				// Birth'

				uncamelCasedName += c;
			} else if ( _isDigit( c ) && _isLetter( lastChar ) && lastChar !== ' ' ) {
				uncamelCasedName += ' ' + c;
			} else {
				uncamelCasedName += c;
			}

			lastChar = c;
		}

		return uncamelCasedName;
	};

	/**
	 * Localizes the given value.
	 * <p>
	 * First, camelCases the given value to create a key. Then looks this key up
	 * in <tt>mw.l10n</tt>. If it exists, returns the value associated with
	 * that key. Otherwise, returns the original value.
	 */

	metawidget.util.getLocalizedString = function( value, mw ) {

		var key = metawidget.util.camelCase( value );

		if ( mw.l10n !== undefined && mw.l10n[key] !== undefined ) {
			return mw.l10n[key];
		}

		return value;
	};

	/**
	 * Following the rules defined in <tt>capitalize</tt>: "This normally
	 * means converting the first character from upper case to lower case, but
	 * in the (unusual) special case when there is more than one character and
	 * both the first and second characters are upper case, we leave it alone.
	 * Thus 'FooBah' becomes 'fooBah' and 'X' becomes 'x', but 'URL' stays as
	 * 'URL'"
	 */

	metawidget.util.decapitalize = function( name ) {

		if ( name.length === 0 ) {
			return name;
		}

		// Nothing to do?

		var firstChar = name.charAt( 0 );

		if ( !_isUpperCase( firstChar ) ) {
			return name;
		}

		// Second letter uppercase?

		if ( name.length > 1 ) {
			if ( _isUpperCase( name.charAt( 1 ) ) ) {
				return name;
			}
		}

		return name.charAt( 0 ).toLowerCase() + name.slice( 1 );
	};

	/**
	 * Capitalize by uppercasing the first letter of the given String (e.g. from
	 * 'fooBarBaz' to 'FooBarBaz').
	 * <p>
	 * The rules for capitalizing are not clearly, but we try to make
	 * <tt>capitalize</tt> the inverse of <tt>decapitalize</tt> (this
	 * includes the 'second character' clause). For example, in Eclipse if you
	 * define a property 'aB123' and then 'generate getters' Eclipse will
	 * generate a method called 'getaB123' <em>not</em> 'getAB123'. See:
	 * https://community.jboss.org/thread/203202?start=0&tstart=0
	 */

	metawidget.util.capitalize = function( name ) {

		if ( name.length === 0 ) {
			return name;
		}

		// Second letter uppercase?

		if ( name.length > 1 ) {
			if ( _isUpperCase( name.charAt( 1 ) ) ) {
				return name;
			}
		}

		return name.charAt( 0 ).toUpperCase() + name.slice( 1 );
	};

	/**
	 * @return true if the value is boolean true or string 'true', but false for
	 *         any other value (including other JavaScript 'truthy' values)
	 */

	metawidget.util.isTrueOrTrueString = function( value ) {

		return ( value === 'true' || value === true );
	};

	/**
	 * Camel cases the given array of names (e.g. from ['foo','bar','baz'] to
	 * 'fooBarBaz'). The first name is decapitalized. Subsequent names are
	 * capitalized.
	 * <p>
	 * If <tt>names</tt> is not an array, first calls
	 * <tt>names.split( ' ' )</tt>.
	 * 
	 * @return the camel cased name. Or an empty string if no name
	 */

	metawidget.util.camelCase = function( names ) {

		if ( ! ( names instanceof Array ) ) {
			names = names.split( ' ' );
		}

		var toString = '';
		var length = names.length;

		if ( length > 0 ) {
			toString += metawidget.util.decapitalize( names[0] );
		}

		for ( var loop = 1; loop < length; loop++ ) {
			toString += metawidget.util.capitalize( names[loop] );
		}

		return toString;
	};

	metawidget.util.fillString = function( repeat, times ) {

		// From:
		// http://stackoverflow.com/questions/202605/repeat-string-javascript

		var toReturn = '';

		for ( ;; ) {

			if ( times & 1 ) {
				toReturn += repeat;
			}

			times >>= 1;

			if ( times ) {
				repeat += repeat;
			} else {
				break;
			}
		}

		return toReturn;
	};

	metawidget.util.lookupEnumTitle = function( value, anEnum, enumTitles ) {

		// Locate the value within the enums (if there)...

		var indexOf = anEnum.indexOf( value );

		if ( indexOf === -1 || indexOf >= enumTitles.length ) {
			
			// ...(cope with Java's UiLookup only supporting strings)...
			
			indexOf = anEnum.indexOf( '' + value );
			
			if ( indexOf === -1 || indexOf >= enumTitles.length ) {
				return value;
			}
		}

		// ...and return its equivalent title (if any)

		return enumTitles[indexOf];
	};

	/**
	 * Gets a camelCased id based on the given attributes.name and the given
	 * mw.path.
	 */

	metawidget.util.getId = function( elementName, attributes, mw ) {

		if ( mw.path !== undefined ) {
			var splitPath = mw.path.split( '.' );

			if ( splitPath[0] === 'object' ) {
				splitPath = splitPath.slice( 1 );
			}

			if ( attributes.name && elementName !== 'entity' ) {
				splitPath.push( attributes.name );
			} else if ( splitPath.length === 0 ) {
				return undefined;
			}

			var id = metawidget.util.camelCase( splitPath );

			// Strip array qualifiers

			id = id.replace( /[\[\]]/g, '' );

			return id;
		}

		if ( attributes !== undefined ) {
			return attributes.name;
		}
	};

	/**
	 * Returns true if the given node has child <em>elements</em>. That is,
	 * their <tt>nodeType === 1</tt>. Ignores other sorts of child nodes,
	 * such as text nodes.
	 */

	metawidget.util.hasChildElements = function( node ) {

		var childNodes = node.childNodes;

		for ( var loop = 0, length = childNodes.length; loop < length; loop++ ) {

			if ( childNodes[loop].nodeType === 1 ) {
				return true;
			}
		}

		return false;
	};

	/**
	 * @true if the given attributes define 'large' or 'wide'.
	 */

	metawidget.util.isSpanAllColumns = function( attributes ) {

		if ( attributes === undefined ) {
			return false;
		}

		if ( metawidget.util.isTrueOrTrueString( attributes.large ) ) {
			return true;
		}

		return metawidget.util.isTrueOrTrueString( attributes.wide );
	};

	/**
	 * Splits the given path into its type and an array of names (e.g.
	 * 'foo.bar['baz']' into type 'foo' and names ['bar','baz']).
	 * 
	 * @returns an object with properties 'type' and 'names' (provided there is
	 *          at least 1 name)
	 */

	metawidget.util.splitPath = function( path ) {

		var splitPath = {};

		if ( path !== undefined ) {

			// Match at every '.' and '[' boundary

			var pathArray = path.match( /([^\.\[\]]*)/g );
			splitPath.type = pathArray[0];

			for ( var loop = 1, length = pathArray.length; loop < length; loop++ ) {

				// Ignore empty matches

				if ( pathArray[loop] === '' ) {
					continue;
				}

				if ( splitPath.names === undefined ) {
					splitPath.names = [];
				}

				// Strip surrounding spaces and quotes (eg. foo[ 'bar' ])

				var stripQuotes = pathArray[loop].match( /^(?:\s*(?:\'|\"))([^\']*)(?:(?:\'|\")\s*)$/ );

				if ( stripQuotes !== null && stripQuotes[1] !== undefined ) {
					pathArray[loop] = stripQuotes[1];
				}

				splitPath.names.push( pathArray[loop] );
			}
		}

		return splitPath;
	};

	/**
	 * Appends the 'path' property from the given Metawidget to the 'name'
	 * property in the given attributes.
	 */

	metawidget.util.appendPath = function( attributes, mw ) {

		if ( mw.path !== undefined ) {
			return metawidget.util.appendPathWithName( mw.path, attributes );
		}

		if ( mw.toInspect !== undefined ) {
			return metawidget.util.appendPathWithName( typeof ( mw.toInspect ), attributes );
		}

		return metawidget.util.appendPathWithName( 'object', attributes );
	};

	/**
	 * Returns the given path appended with the given name (e.g. 'foo' with
	 * 'bar' becomes 'foo.bar'). Supports nameIncludesSeparator. Also supports
	 * using bracket notation if the name contains illegal characters (e.g.
	 * 'foo['bar bar']')
	 */

	metawidget.util.appendPathWithName = function( path, attributes ) {

		var name = attributes.name;

		// In general, add a dot before the attributes.name. However support
		// nameIncludesSeparator for alwaysUseNestedMetawidgetInTables

		if ( metawidget.util.isTrueOrTrueString( attributes.nameIncludesSeparator ) ) {
			return path + name;
		}

		if ( name.indexOf( '.' ) !== -1 || name.indexOf( '\'' ) !== -1 || name.indexOf( '"' ) !== -1 || name.indexOf( ' ' ) !== -1 ) {
			return path + '[\'' + name.replace( '\'', '\\\'' ) + '\']';
		}

		return path + '.' + name;
	};

	/**
	 * Traverses the given 'toInspect' along properties defined by the array of
	 * 'names'.
	 * 
	 * @param toInspect
	 *            object to traverse
	 * @param names
	 *            array of propery names to traverse along
	 */

	metawidget.util.traversePath = function( toInspect, names ) {

		if ( toInspect === undefined ) {
			return undefined;
		}

		if ( names !== undefined ) {

			// Sanity check against passing a single string

			if ( ! ( names instanceof Array ) ) {
				throw new Error( "Expected array of names" );
			}

			for ( var loop = 0, length = names.length; loop < length; loop++ ) {

				toInspect = toInspect[names[loop]];

				// We don't need to worry about array indexes here: they should
				// have been parsed out by splitPath

				if ( toInspect === undefined ) {
					return undefined;
				}
			}
		}

		return toInspect;
	};

	/**
	 * Return an array of the given inspection result's properties, sorted by
	 * 'propertyOrder' (if any).
	 * <p>
	 * See: https://github.com/json-stylesheet/json-stylesheet/issues/1
	 * https://github.com/json-schema/json-schema/issues/87
	 */

	metawidget.util.getSortedInspectionResultProperties = function( inspectionResult ) {

		// Extract the given inspection result's properties into an array...

		var sortedProperties = [];

		if ( inspectionResult !== undefined ) {

			for ( var propertyName in inspectionResult.properties ) {

				var properties = inspectionResult.properties[propertyName];
				sortedProperties.push( properties );

				properties.name = propertyName;
				properties._syntheticOrder = sortedProperties.length;
			}

			// ...sort the array...

			sortedProperties.sort( function( a, b ) {

				if ( a.propertyOrder === undefined ) {
					if ( b.propertyOrder === undefined ) {
						return ( a._syntheticOrder - b._syntheticOrder );
					}
					return 1;
				}

				if ( b.propertyOrder === undefined ) {
					return -1;
				}

				var diff = ( a.propertyOrder - b.propertyOrder );

				if ( diff === 0 ) {
					return ( a._syntheticOrder - b._syntheticOrder );
				}

				return diff;
			} );
		}

		// ...and return it

		return sortedProperties;
	};

	/**
	 * Combines the given first inspection result with the given second
	 * inspection result.
	 * <p>
	 * Inspection results are expected to be JSON Schema (v3) objects. They are
	 * combined based on their property name. If no elements match, new
	 * properties are appended.
	 */

	metawidget.util.combineInspectionResults = function( existingInspectionResult, newInspectionResult ) {

		// Inspector may return undefined

		if ( newInspectionResult === undefined ) {
			return;
		}

		// Combine based on propertyName

		_copyPrimitives( newInspectionResult, existingInspectionResult );

		if ( newInspectionResult.properties === undefined ) {
			return;
		}

		existingInspectionResult.properties = existingInspectionResult.properties || {};

		for ( var propertyName in newInspectionResult.properties ) {

			existingInspectionResult.properties[propertyName] = existingInspectionResult.properties[propertyName] || {};
			_copyPrimitives( newInspectionResult.properties[propertyName], existingInspectionResult.properties[propertyName] );
		}

		//
		// Private methods
		//

		function _copyPrimitives( from, to ) {

			for ( var propertyName in from ) {

				var propertyValue = from[propertyName];

				if ( propertyValue instanceof Array ) {
					to[propertyName] = propertyValue.slice( 0 );
					continue;
				}

				if ( propertyValue instanceof Object ) {
					continue;
				}

				to[propertyName] = from[propertyName];
			}
		}
	};

	/**
	 * Strips the first section off the section attribute (if any).
	 */

	metawidget.util.stripSection = function( attributes ) {

		var section = attributes.section;

		// (undefined means 'no change to current section')

		if ( section === undefined ) {
			return undefined;
		}

		if ( ! ( section instanceof Array ) ) {
			delete attributes.section;
			return section;
		}

		switch ( section.length ) {

			// (empty String means 'end current section')
			case 0:
				delete attributes.section;
				return '';

			case 1:
				delete attributes.section;
				return section[0];

			default:
				attributes.section = section.slice( 1 );
				return section[0];
		}
	};

	/**
	 * Sets the given 'toAppend' to the given widget's 'attributeName'. If the
	 * given widget already has a value for 'attributeName', appends a space and
	 * then adds 'toAppend'.
	 * 
	 * @param separator
	 *            separator to use (defaults to a space)
	 */

	metawidget.util.appendToAttribute = function( widget, attributeName, toAppend, separator ) {

		var existingAttribute = widget.getAttribute( attributeName );

		if ( separator === undefined ) {
			separator = ' ';
		}

		if ( existingAttribute === null ) {
			widget.setAttribute( attributeName, toAppend );
		} else if ( existingAttribute !== toAppend && existingAttribute.indexOf( toAppend + separator ) === -1 && existingAttribute.indexOf( separator + toAppend ) === -1 ) {
			widget.setAttribute( attributeName, existingAttribute + separator + toAppend );
		}
	};

	/**
	 * Creates an element by calling <tt>ownerDocument</tt> rather than simply
	 * <tt>document</tt>. This stops us relying on a global <tt>document</tt>
	 * variable.
	 */

	metawidget.util.createElement = function( mw, element ) {

		// Explicitly call toUpperCase, as IE8 doesn't appear to do this for
		// non-HTML4 tags (like 'output')

		if ( mw.ownerDocument !== undefined ) {
			return mw.ownerDocument.createElement( element.toUpperCase() );
		}

		return mw.getElement().ownerDocument.createElement( element.toUpperCase() );
	};

	/**
	 * Creates a text node by calling <tt>ownerDocument</tt> rather than
	 * simply <tt>document</tt>. This stops us relying on a global
	 * <tt>document</tt> variable.
	 */

	metawidget.util.createTextNode = function( mw, text ) {

		if ( mw.ownerDocument !== undefined ) {
			return mw.ownerDocument.createTextNode( text );
		}

		return mw.getElement().ownerDocument.createTextNode( text );
	};

	/**
	 * Creates an event by calling <tt>ownerDocument</tt> rather than simply
	 * <tt>document</tt>. This stops us relying on a global <tt>document</tt>
	 * variable.
	 */

	metawidget.util.createEvent = function( mw, name ) {

		var event;

		if ( mw.ownerDocument !== undefined ) {
			event = mw.ownerDocument.createEvent( 'Event' );
		} else {
			event = mw.getElement().ownerDocument.createEvent( 'Event' );
		}

		event.initEvent( name, true, true );

		return event;
	};

	/**
	 * Finds the indexOf the given item in the given array.
	 * 
	 * @return -1 if either array or item are undefined, otherwise indexOf
	 */

	metawidget.util.niceIndexOf = function( array, item ) {

		if ( array === undefined || item === undefined ) {
			return -1;
		}

		return array.indexOf( item );
	}

	/**
	 * Backward compatibility for IE.
	 */
	
	metawidget.util.hasAttribute = function( element, attribute ) {
		
		if ( element.hasAttribute !== undefined ) {
			return element.hasAttribute( attribute );
		}
		
		return ( element.getAttribute( attribute ) !== null );
	}
	
	//
	// Private methods
	//

	function _isUpperCase( c ) {

		var charCode = c.charCodeAt( 0 );
		return ( charCode >= 65 && charCode <= 90 );
	}

} )();
