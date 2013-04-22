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

var metawidget = metawidget || {};

( function() {

	'use strict';

	/**
	 * @namespace Inspectors.
	 *            <p>
	 *            Inspectors must implement an interface:
	 *            </p>
	 *            <tt>function( toInspect, type, names )</tt>
	 *            <p>
	 *            Each Inspector must look to the 'type' parameter and the
	 *            'names' array. These form a path into the business object
	 *            domain model. For example the 'type' may be 'person' and the
	 *            'names' may be [ 'address', 'street' ]. This would form a path
	 *            into the domain model of 'person/address/street' (i.e. return
	 *            information on the 'street' property within the 'address'
	 *            property of the 'person' type).
	 *            </p>
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
	 *        order that properties were defined in. However you may want to
	 *        combine PropertyTypeInspector with a custom Inspector that imposes
	 *        a defined ordering using 'propertyOrder' attributes.
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

	/**
	 * @class Inspects JSON Schemas for their properties.
	 *        <p>
	 *        Metawidget <em>already</em> uses JSON Schema as its inspection
	 *        result format, so this Inspector does not need to do much. However
	 *        it adds support for JSON schemas that contain nested schemas by
	 *        traversing the given 'names' array.
	 */

	metawidget.inspector.JsonSchemaInspector = function( config ) {

		if ( ! ( this instanceof metawidget.inspector.JsonSchemaInspector ) ) {
			throw new Error( 'Constructor called as a function' );
		}

		var _schema;

		if ( config.schema !== undefined ) {
			_schema = config.schema;
		} else {
			_schema = config;
		}
		
		this.inspect = function( toInspect, type, names ) {

			// Traverse names using 'properties' intermediate name

			var traversed = metawidget.util.traversePath( _schema, names, 'properties' );

			if ( traversed === undefined ) {
				return undefined;
			}

			// Copy values

			var inspectionResult = {};
			if ( names !== undefined ) {
				inspectionResult.name = names[names.length - 1];
			}
			metawidget.util.combineInspectionResults( inspectionResult, traversed );
			return inspectionResult;
		};
	};
} )();