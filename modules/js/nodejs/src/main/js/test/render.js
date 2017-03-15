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

var assert = require( 'assert' );
var metawidget = require( 'metawidget' );

// Use a simple DOM implementation

var simpleDocument = {
	createElement: function( elementName ) {

		var _eventListeners = [];

		return {
			nodeType: 1,
			tagName: elementName.toUpperCase(),
			attributes: [],
			childNodes: [],
			setAttribute: function( name, value ) {

				for ( var loop = 0, length = this.attributes.length; loop < length; loop++ ) {
					if ( this.attributes[loop].nodeName === name ) {
						this.attributes[loop].nodeValue = value;
						return;
					}
				}

				this.attributes.push( {
					nodeName: name,
					nodeValue: value
				} );
			},
			hasAttribute: function( name ) {

				return ( this.getAttribute( name ) !== null );
			},
			getAttribute: function( name ) {

				for ( var loop = 0, length = this.attributes.length; loop < length; loop++ ) {
					if ( this.attributes[loop].nodeName === name ) {
						return this.attributes[loop].nodeValue;
					}
				}

				return null;
			},
			appendChild: function( childNode ) {

				this.childNodes.push( childNode );
			},
			cloneNode: function() {

				var clone = simpleDocument.createElement( elementName );

				for ( var loop = 0, length = this.attributes.length; loop < length; loop++ ) {
					var attribute = this.attributes[loop];
					clone.setAttribute( attribute.nodeName, attribute.nodeValue );
				}
				for ( var loop = 0, length = this.childNodes.length; loop < length; loop++ ) {
					clone.appendChild( this.childNodes[loop].cloneNode() );
				}
				return clone;
			},
			addEventListener: function( name, callback ) {
				
				_eventListeners.push( {
					name: name,
					callback: callback
				} );
			},
			dispatchEvent: function( event ) {

				for( var loop = 0, length = _eventListeners.length; loop < length; loop++ ) {
					
					var eventListener = _eventListeners[loop];
					
					if ( eventListener.name === event.name ) {
						eventListener.callback( event );
					}
				}
			},			
			ownerDocument: this,
			toString: function() {

				var toString = "<" + elementName.toLowerCase();

				for ( var loop = 0, length = this.attributes.length; loop < length; loop++ ) {
					var attribute = this.attributes[loop];
					toString += ' ' + attribute.nodeName + '="' + attribute.nodeValue + '"';
				}

				if ( this.value !== undefined ) {
					toString += ' value="' + this.value + '"';
				}

				toString += ">";

				for ( var loop = 0, length = this.childNodes.length; loop < length; loop++ ) {
					toString += this.childNodes[loop].toString();
				}

				if ( this.textContent !== undefined ) {
					toString += this.textContent;
				}

				toString += "</" + elementName.toLowerCase() + ">";
				return toString;
			}
		};
	},
	createTextNode: function( data ) {

		return {
			nodeType: 3,
			toString: function() {

				return data;
			}
		}
	},
	createEvent: function() {

		return {
		
			initEvent: function( name ) {

				this.name = name;
			}
		};
	}	
};

var element = simpleDocument.createElement( 'div' );
var mw = new metawidget.Metawidget( element );
mw.toInspect = {
	name: "Joe Bloggs",
	"DOB": "1/1/2001"
};
mw.buildWidgets();

// Test what was rendered

assert.ok( element.toString().indexOf( '<label for="name" id="table-name-label">Name:</label>' ) !== -1 );
assert.ok( element.toString().indexOf( '<input type="text" id="name" name="name" value="Joe Bloggs"></input>' ) !== -1 );
assert.ok( element.toString().indexOf( '<label for="DOB" id="table-DOB-label">DOB:</label>' ) !== -1 );
assert.ok( element.toString().indexOf( '<input type="text" id="DOB" name="DOB" value="1/1/2001"></input>' ) !== -1 );
assert
		.equal(
				element.toString(),
				'<div><table><tbody><tr id="table-name-row"><th id="table-name-label-cell"><label for="name" id="table-name-label">Name:</label></th><td id="table-name-cell"><input type="text" id="name" name="name" value="Joe Bloggs"></input></td><td></td></tr><tr id="table-DOB-row"><th id="table-DOB-label-cell"><label for="DOB" id="table-DOB-label">DOB:</label></th><td id="table-DOB-cell"><input type="text" id="DOB" name="DOB" value="1/1/2001"></input></td><td></td></tr></tbody></table></div>' );
