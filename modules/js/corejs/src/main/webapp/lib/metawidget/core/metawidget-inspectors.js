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

	this.inspect = function( toInspect, type, names ) {

		var compositeInspectionResult = [];

		for ( var ins = 0, insLength = inspectors.length; ins < insLength; ins++ ) {

			var inspectionResult;
			var inspector = inspectors[ins];

			if ( inspector.inspect ) {
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
		throw new Error( "Constructor called as a function" );
	}
};

metawidget.inspector.PropertyTypeInspector.prototype.inspect = function( toInspect, type, names ) {

	// Traverse names

	toInspect = metawidget.util.traversePath( toInspect, type, names );

	if ( toInspect == null ) {
		return;
	}

	// Inspect leaf node
	
	var inspectionResult = [ {
		"name": "__root",
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
			// descriptive inspection result from a previous Inspector

			if ( typeOfProperty != 'object' ) {
				inspectedProperty.type = typeOfProperty;
			}
		}

		inspectionResult.push( inspectedProperty );
	}

	return inspectionResult;
};
