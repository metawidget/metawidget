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

metawidget.inspector.CompositeInspector = function( config ) {

	if ( ! ( this instanceof metawidget.inspector.CompositeInspector ) ) {
		throw new Error( "Constructor called as a function" );
	}

	var inspectors;
	
	if ( config.inspectors ) {
		inspectors = config.inspectors;
	} else {
		inspectors = config;
	}

	this.inspect = function( toInspect, type ) {

		var compositeInspectionResult = [];

		for ( var ins = 0, insLength = inspectors.length; ins < insLength; ins++ ) {

			var inspectionResult;
			var inspector = inspectors[ins];

			if ( inspector.inspect ) {
				inspectionResult = inspector.inspect( toInspect, type );
			} else {
				inspectionResult = inspector( toInspect, type );
			}

			// Inspector may return null

			if ( !inspectionResult ) {
				continue;
			}

			// If this is the first result...

			if ( compositeInspectionResult.length == 0 ) {

				// ...copy it

				for ( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {

					var newAttributes = inspectionResult[loop];
					var existingAttributes = {};

					for ( var attribute in newAttributes ) {
						existingAttributes[attribute] = newAttributes[attribute];
					}

					compositeInspectionResult.push( existingAttributes );
				}

			} else {

				// ...otherwise merge it

				outer: for ( var loop1 = 0, length1 = inspectionResult.length; loop1 < length1; loop1++ ) {

					var newAttributes = inspectionResult[loop1];

					for ( var loop2 = 0, length2 = compositeInspectionResult.length; loop2 < length2; loop2++ ) {
						var existingAttributes = compositeInspectionResult[loop2];

						if ( existingAttributes.name == newAttributes.name ) {

							for ( var attribute in newAttributes ) {
								existingAttributes[attribute] = newAttributes[attribute];
							}

							continue outer;
						}
					}

					// If no existing attributes matched, push a new one

					var existingAttributes = {};

					for ( var attribute in newAttributes ) {
						existingAttributes[attribute] = newAttributes[attribute];
					}

					compositeInspectionResult.push( existingAttributes );
				}
			}
		}

		return compositeInspectionResult;
	};
};

//
// PropertyTypeInspector
//

metawidget.inspector.PropertyTypeInspector = function() {

	if ( ! ( this instanceof metawidget.inspector.PropertyTypeInspector ) ) {
		throw new Error( "Constructor called as a function" );
	}
};

metawidget.inspector.PropertyTypeInspector.prototype.inspect = function( toInspect, type ) {

	var inspectionResult = [ {
		"name": "$root",
		"type": typeof ( toInspect )
	} ];

	for ( var property in toInspect ) {

		var inspectedProperty = {};
		inspectedProperty.name = property;

		// Inspect the type of the property as best we can

		var value = toInspect[property];

		if ( value instanceof Date ) {

			// typeof never seems to return 'date'. It returns 'number' and
			// 'string' fine

			inspectedProperty.type = 'date';
		} else {

			var typeOfProperty = typeof ( value );

			// type 'object' doesn't convey much, and can override a more
			// descriptive string from a previous inspection result

			if ( typeOfProperty != 'object' ) {
				inspectedProperty.type = typeOfProperty;
			}
		}

		inspectionResult.push( inspectedProperty );
	}

	return inspectionResult;
};
