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

'use strict';

/**
 * Inspectors.
 */

var metawidget = metawidget || {};
metawidget.inspector = metawidget.inspector || {};

//
// CompositeInspector
//

/**
 * Delegates inspection to one or more sub-inspectors, then combines the
 * resulting metadata.
 * <p>
 * The combining algorithm should be suitable for most use cases, but one
 * benefit of having a separate CompositeInspector is that developers can
 * replace it with their own version, with its own combining algorithm, if
 * required.
 * <p>
 * Note: the name <em>Composite</em>Inspector refers to the Composite design
 * pattern.
 */

metawidget.inspector.CompositeInspector = function( config ) {

	if ( ! ( this instanceof metawidget.inspector.CompositeInspector ) ) {
		throw new Error( 'Constructor called as a function' );
	}

	var inspectors;
	
	if ( config.inspectors !== undefined ) {
		inspectors = config.inspectors;
	} else {
		inspectors = config;
	}

	this.inspect = function( toInspect, type, names ) {

		var compositeInspectionResult = [];

		for ( var ins = 0, insLength = inspectors.length; ins < insLength; ins++ ) {

			var inspectionResult;
			var inspector = inspectors[ins];

			if ( inspector.inspect !== undefined ) {
				inspectionResult = inspector.inspect( toInspect, type, names );
			} else {
				inspectionResult = inspector( toInspect, type, names );
			}

			compositeInspectionResult = metawidget.util.combineInspectionResults( compositeInspectionResult, inspectionResult );
		}

		return compositeInspectionResult;
	};
};

//
// PropertyTypeInspector
//

metawidget.inspector.PropertyTypeInspector = function() {

	if ( ! ( this instanceof metawidget.inspector.PropertyTypeInspector ) ) {
		throw new Error( 'Constructor called as a function' );
	}
};

/**
 * Inspects JavaScript objects for their property names and types.
 * <p>
 * In principal, ordering of property names within JavaScript objects is not
 * guaranteed. In practice, most browsers respect the original ordering that
 * properties were defined in. However you may want to preceed
 * PropertyTypeInspector with a custom Inspector that imposes a defined
 * ordering.
 */

metawidget.inspector.PropertyTypeInspector.prototype.inspect = function( toInspect, type, names ) {

	// Traverse names

	toInspect = metawidget.util.traversePath( toInspect, type, names );

	if ( toInspect === undefined ) {
		return;
	}

	// Inspect root node. Important if the Metawidget is
	// pointed directly at a primitive type
	
	var inspectionResult = [ {
		_root: 'true',
		type: typeof ( toInspect )
	} ];

	if ( names !== undefined && names.length > 0 ) {
		inspectionResult[0].name = names[names.length-1];
	}
	
	for ( var property in toInspect ) {

		var inspectedProperty = {};
		inspectedProperty.name = property;

		// Inspect the type of the property as best we can

		var value = toInspect[property];

		if ( value instanceof Date ) {

			// typeof never returns 'date'

			inspectedProperty.type = 'date';
		} else {

			var typeOfProperty = typeof ( value );

			// type 'object' doesn't convey much, and can override a more
			// descriptive inspection result from a previous Inspector

			if ( typeOfProperty !== 'object' ) {
				inspectedProperty.type = typeOfProperty;
			}
		}

		inspectionResult.push( inspectedProperty );
	}

	return inspectionResult;
};
