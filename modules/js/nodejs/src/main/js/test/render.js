var assert = require( 'assert' );
var metawidget = require( 'metawidget' );

// Use a simple DOM implementation

var simpleDocument = {
	createElement: function( elementName ) {

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

				for ( var loop = 0, length = this.attributes.length; loop < length; loop++ ) {
					if ( this.attributes[loop].nodeName === name ) {
						return true;
					}
				}

				return false;
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
			removeChild: function( childNode ) {

				for ( var loop = 0, length = this.childNodes.length; loop < length; loop++ ) {
					if ( this.childNodes[loop] === childNode ) {
						this.childNodes.splice( loop, 1 );
						return childNode;
					}
				}

				throw new Error( "childNode not found: " + childNode );
			},
			ownerDocument: this,
			toString: function() {

				var toString = "<" + elementName;

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

				if ( this.innerHTML !== undefined ) {
					toString += this.innerHTML;
				}

				toString += "</" + elementName + ">";
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
	}
};

var element = simpleDocument.createElement( 'metawidget' );
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
				'<metawidget><table><tbody><tr id="table-name-row"><th id="table-name-label-cell"><label for="name" id="table-name-label">Name:</label></th><td id="table-name-cell"><input type="text" id="name" name="name" value="Joe Bloggs"></input></td><td></td></tr><tr id="table-DOB-row"><th id="table-DOB-label-cell"><label for="DOB" id="table-DOB-label">DOB:</label></th><td id="table-DOB-cell"><input type="text" id="DOB" name="DOB" value="1/1/2001"></input></td><td></td></tr></tbody></table></metawidget>' );
