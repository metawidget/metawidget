// Metawidget ${project.version} (licensed under LGPL)
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

/**
 * @namespace Metawidget for pure JavaScript environments.
 */

var metawidget = metawidget || {};

( function() {

	'use strict';

	/**
	 * @namespace Inspectors.
	 *            <p>
	 *            Inspectors return inspection results as an <em>array</em> of
	 *            objects. JSON Schmea is not used, because JavaScript
	 *            implementations allow for implementation dependent iteration
	 *            order of objects with named properties.
	 */

	metawidget.inspector = metawidget.inspector || {};

	/**
	 * @class Delegates inspection to one or more sub-inspectors, then combines
	 *        the resulting metadata using
	 *        <tt>metawidget.util.combineInspectionResults</tt>.
	 *        <p>
	 *        The combining algorithm should be suitable for most use cases, but
	 *        one benefit of having a separate CompositeInspector is that
	 *        developers can replace it with their own version, with its own
	 *        combining algorithm, if required.
	 *        <p>
	 *        Note: the name <em>Composite</em>Inspector refers to the
	 *        Composite design pattern.
	 */

	metawidget.inspector.CompositeInspector = function( config ) {

		if ( ! ( this instanceof metawidget.inspector.CompositeInspector ) ) {
			throw new Error( 'Constructor called as a function' );
		}

		var _inspectors;

		if ( config.inspectors !== undefined ) {
			_inspectors = config.inspectors.slice( 0 );
		} else {
			_inspectors = config.slice( 0 );
		}

		this.inspect = function( toInspect, type, names ) {

			var compositeInspectionResult = {};

			for ( var ins = 0, insLength = _inspectors.length; ins < insLength; ins++ ) {

				var inspectionResult;
				var inspector = _inspectors[ins];

				if ( inspector.inspect !== undefined ) {
					inspectionResult = inspector.inspect( toInspect, type, names );
				} else {
					inspectionResult = inspector( toInspect, type, names );
				}

				metawidget.util.combineInspectionResults( compositeInspectionResult, inspectionResult );
			}

			return compositeInspectionResult;
		};
	};

	/**
	 * @class Inspects JavaScript objects for their property names and types.
	 *        <p>
	 *        In principal, ordering of property names within JavaScript objects
	 *        is not guaranteed. In practice, most browsers respect the original
	 *        ordering that properties were defined in. However you may want to
	 *        precede PropertyTypeInspector with a custom Inspector that imposes
	 *        a defined ordering.
	 */

	metawidget.inspector.PropertyTypeInspector = function() {

		if ( ! ( this instanceof metawidget.inspector.PropertyTypeInspector ) ) {
			throw new Error( 'Constructor called as a function' );
		}
	};

	metawidget.inspector.PropertyTypeInspector.prototype.inspect = function( toInspect, type, names ) {

		// Traverse names

		toInspect = metawidget.util.traversePath( toInspect, names );

		var inspectionResult = {};

		// Inspect root node. Important if the Metawidget is
		// pointed directly at a primitive type

		if ( names !== undefined && names.length > 0 ) {
			inspectionResult.name = names[names.length - 1];
		} else {

			// Nothing useful to return?

			if ( toInspect === undefined ) {
				return;
			}
		}

		if ( toInspect !== undefined ) {

			inspectionResult.type = typeof ( toInspect );
			inspectionResult.properties = {};

			for ( var property in toInspect ) {

				// Inspect the type of the property as best we can

				var value = toInspect[property];
				var typeOfProperty;

				if ( value instanceof Array ) {

					// typeof never returns 'array', even though JavaScript has
					// a built-in Array type

					typeOfProperty = 'array';

				} else if ( value instanceof Date ) {

					// typeof never returns 'date', even though JavaScript has a
					// built-in Date type

					typeOfProperty = 'date';

				} else {

					typeOfProperty = typeof ( value );

					// type 'object' doesn't convey much, and can override a
					// more descriptive inspection result from a previous
					// Inspector. If you leave it off, Metawidget's default
					// behaviour is to recurse into the object anyway

					if ( typeOfProperty === 'object' ) {

						inspectionResult.properties[property] = {};
						continue;
					}
				}

				// JSON Schema primitive types are: 'array', 'boolean',
				// 'integer', 'number', 'null', 'object' and 'string'

				inspectionResult.properties[property] = {
					type: typeOfProperty
				};
			}
		}

		return inspectionResult;
	};
} )();