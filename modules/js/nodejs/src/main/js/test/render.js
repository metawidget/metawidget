var metawidget = require( 'metawidget' );

describe( 'render', function() {

	describe( 'the NodeJS plugin', function() {

		it( 'should render Metawidget correctly', function( done ) {

			var myObj = {
				"firstname": "Joe",
				"surname": "Bloggs"
			};

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
			mw.toInspect = myObj;
			mw.buildWidgets();
			
			expect( mw.toString() ).to.equal( '<metawidget></metawidget>' );
			done();
		} );
	} );
} );
